package com.gaea.common.stopwatch;

public abstract class StopWatchLog {

    public static final String DEFAULT_LOGGER_NAME = "com.bamboo.statistics.Logger";

    public abstract void log(StopWatch sw);

    public void shutdown() {
        // nothing
    }
}
