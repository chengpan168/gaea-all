package com.gaea.common.stopwatch;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

public class StopWatch implements Serializable, Cloneable {

    public static final String DEFAULT_TAG      = "[default]";

    private static final long  serialVersionUID = 5154481161113185022L;

    private long               createTimeMillis;
    private long               startTimeNanos;
    private long               stopTimeNanos;
    private String             tag;
    private String             message;

    private static final long  NANOS_IN_SECOND  = 1000 * 1000 * 1000;

    public StopWatch() {
        this(null, null);
    }

    public StopWatch(String tag) {
        this(tag, null);
    }

    public StopWatch(String tag, String message) {
        this.tag = tag;
        this.message = message;
        createTimeMillis = System.currentTimeMillis();
        start();
    }

    public StopWatch start() {
        startTimeNanos = System.nanoTime();
        return this;
    }

    /***
     * stop的时候会执行这个方法，子类可以覆盖这个方法，执行某些操作
     */
    protected void internalStop() {
        stopTimeNanos = System.nanoTime();
    }

    public StopWatch stop() {
        internalStop();

        return this;
    }

    public StopWatch stop(String tag) {
        this.tag = tag;
        stop();
        return this;
    }

    public StopWatch stop(String tag, String message) {
        this.tag = tag;
        this.message = message;
        stop();

        return this;
    }

    public StopWatch lap() {
        stop();
        start();

        return this;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String msg) {
        message = msg;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    private long getTimeNanos() {
        if (stopTimeNanos != 0) return stopTimeNanos - startTimeNanos;

        return System.nanoTime() - startTimeNanos;
    }

    public long getElapsedTimeMicros() {
        return TimeUnit.NANOSECONDS.toMicros(getTimeNanos());
    }

    public long getElapsedTimeMillis() {
        return TimeUnit.NANOSECONDS.toMillis(getTimeNanos());
    }

    public long getCreationTime() {
        return createTimeMillis;
    }

    public String toString() {
        return (tag != null ? tag : DEFAULT_TAG).concat(": ").concat(getReadableTime()).concat((message != null ? " " + message : ""));
    }

    public String toString(int iterations) {
        StringBuilder sb = new StringBuilder();

        sb.append(tag != null ? tag : DEFAULT_TAG);
        sb.append(": ");
        sb.append(getReadableTime());
        if (message != null) sb.append(" " + message);
        sb.append(" (" + iterations * NANOS_IN_SECOND / getTimeNanos() + " iterations/second)");

        return sb.toString();
    }

    private String getReadableTime() {
        long ns = getTimeNanos();

        if (ns < 50L * 1000) return ns + " ns";

        if (ns < 50L * 1000 * 1000) return (ns / 1000) + " us";

        if (ns < 50L * 1000 * 1000 * 1000) return (ns / (1000 * 1000)) + " ms";

        return ns / NANOS_IN_SECOND + " s";
    }

    public StopWatch clone() {
        try {
            return (StopWatch) super.clone();
        } catch (CloneNotSupportedException cnse) {
            throw new Error("Unexpected CloneNotSupportedException");
        }
    }
}
