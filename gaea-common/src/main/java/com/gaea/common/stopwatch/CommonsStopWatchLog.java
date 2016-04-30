package com.gaea.common.stopwatch;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author panwang.chengpw 1/12/15 11:44 PM
 */
public class CommonsStopWatchLog extends StopWatchLog {

    protected Log stopWatchlogger;

    public CommonsStopWatchLog() {
        this(LogFactory.getLog(DEFAULT_LOGGER_NAME));
    }

    public CommonsStopWatchLog(Log stopWatchlogger) {
        this.stopWatchlogger = stopWatchlogger;
    }

    @Override
    public void log(StopWatch sw) {
        stopWatchlogger.info(sw);
    }

    @Override
    public void shutdown() {

    }
}
