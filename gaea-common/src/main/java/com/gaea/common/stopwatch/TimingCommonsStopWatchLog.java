package com.gaea.common.stopwatch;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.*;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class TimingCommonsStopWatchLog extends CommonsStopWatchLog {

    private static final int                 DEFAULT_MAX_QUEUE_SIZE = 300000;
    private static final int                 TAG_WIDTH              = 60;
    private Log                              logger                 = LogFactory.getLog(getClass());
    private LinkedBlockingDeque<StopWatch>   blockingQueue          = new LinkedBlockingDeque<StopWatch>(DEFAULT_MAX_QUEUE_SIZE);

    private AtomicInteger                    rejectedStopWatches    = new AtomicInteger();

    private CollectorThread                  collectorThread;

    /**
     * 默认每隔多长时间计算一次,单位秒
     */
    private int                              periodSeconds          = 30;

    private volatile boolean                 stopCollector          = false;
    private List<Double>                     percentiles            = Collections.singletonList(95d);
    /**
     * 单线程操作
     */
    private Map<String, CollectedStatistics> stats                  = new TreeMap<String, CollectedStatistics>();

    public TimingCommonsStopWatchLog() {
        this(LogFactory.getLog(DEFAULT_LOGGER_NAME));
    }

    public TimingCommonsStopWatchLog(Log stopWatchlogger) {
        super(stopWatchlogger);
        Runtime.getRuntime().addShutdownHook(new Thread() {

            @Override
            public void run() {
                shutdown();
            }
        });
    }

    @Override
    public void log(StopWatch sw) {
        if (!blockingQueue.offer(sw.clone())) {
            rejectedStopWatches.incrementAndGet();
        }

        if (collectorThread == null) {
            synchronized (this) {
                if (collectorThread == null) {
                    collectorThread = new CollectorThread();
                    collectorThread.setName("Timing CommonsStopWatch Collector Thread");
                    collectorThread.setDaemon(true);
                    collectorThread.start();
                }
            }// synchronized
        }
    }

    /**
     * 每隔几秒钟计算一次，尽量保证整点，例如：5s,那每次0,5,10……进行计算
     *
     * @param periodSeconds
     */
    public void setPeriodSeconds(int periodSeconds) {
        this.periodSeconds = periodSeconds;
    }

    @Override
    public void shutdown() {
        super.shutdown();
        this.stopCollector = true;
        if (this.collectorThread != null) {
            this.collectorThread.interrupt();
        }
    }

    public void setPercentiles(String percentiles) {
        if (StringUtils.isBlank(percentiles)) {
            this.percentiles = Collections.emptyList();
            return;
        }
        String[] splitPercentiles = StringUtils.split(percentiles, ",");
        if (ArrayUtils.isEmpty(splitPercentiles)) {
            throw new IllegalArgumentException("percentiles format is err.");
        }

        List<Double> tmpPercs = Lists.newArrayListWithExpectedSize(splitPercentiles.length);

        for (String percentile : splitPercentiles) {
            tmpPercs.add(Double.parseDouble(percentile));
        }

        this.percentiles = tmpPercs;

    }

    /**
     * @param finalMoment
     */
    private boolean pollQueue(long finalMoment) {
        StopWatch stopWatch;
        try {
            while ((stopWatch = this.blockingQueue.poll(100, TimeUnit.MILLISECONDS)) != null) {
                if (StringUtils.isBlank(stopWatch.getTag())) {
                    continue;// 这种不统计了
                }
                // 不在时间范围内的，不统计
                if (stopWatch.getCreationTime() > finalMoment) {
                    this.blockingQueue.addFirst(stopWatch);
                    return true;
                }

                CollectedStatistics collectedStatistics = stats.get(stopWatch.getTag());
                if (collectedStatistics == null) {
                    collectedStatistics = new CollectedStatistics();
                    stats.put(stopWatch.getTag(), collectedStatistics);
                }
                collectedStatistics.add(stopWatch);
            }
        } catch (InterruptedException ignore) {
            // 这个log，暂时加上观察下，以后可能就去掉了
            logger.warn("pollQueue interrupted. finalMoment:" + finalMoment);
        }
        return false;
    }

    private void doLog(long startTimeMillis, long endTimeMillis) {

        // 暂时限制输出info级别的
        if (this.stopWatchlogger != null && this.stopWatchlogger.isInfoEnabled()) {
            StringBuilder percString = new StringBuilder();
            for (double percentile : this.percentiles) {
                percString.append(String.format(" %6sth", saneDoubleToString(percentile)));
            }

            printf("Statistics from %tc to %tc", new Date(startTimeMillis), new Date(endTimeMillis));

            printf("%-" + TAG_WIDTH + "s %8s %8s %8s %8s%s%8s", "Tag", "Avg(ms)", "Min", "Max", "Std Dev", percString, "Count");

            for (Map.Entry<String, CollectedStatistics> e : stats.entrySet()) {
                CollectedStatistics cs = e.getValue();
                StringBuilder sb = new StringBuilder();

                sb.append(String.format("%-" + TAG_WIDTH + "s %8.2f %8.2f %8.2f %8.2f", e.getKey(), cs.getAverageMS(), cs.getMin(), cs.getMax(),
                                        cs.getStdDev()));
                Map<Double, Double> percentilesMap = cs.getPercentiles(this.percentiles);
                for (Double percentile : percentiles) {
                    Double time = percentilesMap.get(percentile);
                    sb.append(String.format(" %8.2f", time));
                }

                sb.append(String.format("%8d", cs.getInvocations()));
                stopWatchlogger.info(sb.toString());
            }

            stopWatchlogger.info("");
        }
        // 这个一定要，要不会死的很惨
        this.stats.clear();
    }

    private void printf(String pattern, Object... args) {
        if (stopWatchlogger != null) {
            StringBuilder sb = new StringBuilder();
            Formatter formatter = new Formatter(sb);

            try {
                formatter.format(pattern, args);

                stopWatchlogger.info(sb.toString());
            } finally {
                formatter.close();
            }
        }
    }

    private String saneDoubleToString(double pp) {
        String perTitle = Double.toString(pp);
        if (perTitle.endsWith(".0")) {
            perTitle = perTitle.substring(0, perTitle.length() - 2);
        }
        return perTitle;
    }

    /**
     * An internal Thread which wakes up periodically and checks whether the data should be collected and dumped.
     */
    private class CollectorThread extends Thread {

        long lastRun = System.currentTimeMillis();
        long nextWakeup;

        void nextPeriod() {
            long now = System.currentTimeMillis();
            long periodMillis = TimeUnit.SECONDS.toMillis(periodSeconds);
            nextWakeup = (now / periodMillis) * periodMillis + periodMillis;
        }

        @Override
        public void run() {
            nextPeriod();

            while (!stopCollector) {
                try {
                    if (pollQueue(nextWakeup)) {
                        doLog(lastRun, nextWakeup);
                        lastRun = nextWakeup;
                        nextPeriod();
                    }
                } catch (Throwable t) {
                    // Make sure that we keep running no matter what.
                    logger.warn("Problem while doing logging; continuing nevertheless...", t);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ignore) {
                    }
                }
            }

            long now = System.currentTimeMillis();
            pollQueue(now);
            doLog(lastRun, now);
        }
    }

}
