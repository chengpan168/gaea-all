/*
 * Copyright 2011 Janne Jalkanen Licensed under the Apache License, Version 2.0 (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions and limitations under the
 * License.
 */
package com.gaea.common.stopwatch;

public class LoggingStopWatch extends StopWatch {

    private static final long serialVersionUID = 334586314519287730L;

    private StopWatchLog      logger;

    public LoggingStopWatch(StopWatchLog logger) {
        this.logger = logger;
    }

    public LoggingStopWatch(String tag, StopWatchLog logger) {
        super(tag);
        this.logger = logger;
    }

    /**
     * Creates a LoggingStopWatch associated with the given Log.
     *
     * @param tag The tag.
     * @param message A free-form message.
     */
    public LoggingStopWatch(String tag, String message, StopWatchLog logger) {
        super(tag, message);
        this.logger = logger;
    }

    /**
     * If the log exists and is enabled, sends this StopWatch to the given Log to be logged.
     */
    protected void internalStop() {
        super.internalStop();

        // Do the logging here
        log(this);
    }

    /**
     * stop的时候记录日志
     *
     * @param sw
     */
    public void log(StopWatch sw) {
        logger.log(sw);
    }

}
