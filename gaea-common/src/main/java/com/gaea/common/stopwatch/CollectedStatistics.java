/*
 * Copyright 2011 Janne Jalkanen Licensed under the Apache License, Version 2.0 (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions and limitations under the
 * License.
 */
package com.gaea.common.stopwatch;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

public class CollectedStatistics {

    private DoubleList times = new DoubleList();
    private double     min   = Double.MAX_VALUE;
    private double     max   = 0.0;

    public synchronized void add(StopWatch sw) {
        long timeMillis = sw.getElapsedTimeMillis();
        times.add(timeMillis);
        if (timeMillis < min) {
            min = timeMillis;
        }
        if (timeMillis > max) {
            max = timeMillis;
        }
    }

    public synchronized int getInvocations() {
        return times.size();
    }

    public synchronized double getMin() {
        return min;
    }

    public synchronized double getMax() {
        return max;
    }

    public synchronized double getAverageMS() {
        double result = 0.0;

        for (Double d : times.doubles) {
            result += d;
        }

        return result / times.size();
    }

    public synchronized double getStdDev() {
        return Math.sqrt(variance());
    }

    public synchronized double variance() {
        long n = 0;
        double mean = 0;
        double s = 0.0;

        for (double x : times.doubles) {
            n++;
            double delta = x - mean;
            mean += delta / n;
            s += delta * (x - mean);
        }

        return (s / n);
    }

    public synchronized double getPercentile(int percentile) {
        return getPercentile((double) percentile);
    }

    public double getPercentile(double percentile) {
        return getPercentiles(Collections.singleton(percentile)).get(percentile);
    }

    public Map<Double, Double> getPercentiles(Collection<Double> percentiles) {
        Percentile p = new Percentile();
        return p.evaluates(times.doubles, 0, times.size(), percentiles);
    }

    private static final class DoubleList {

        public double[] doubles = new double[256];
        public int      size;

        public void add(double d) {
            ensureCapacity(size + 1);
            doubles[size++] = d;
        }

        public int size() {
            return size;
        }

        private void ensureCapacity(int capacity) {
            if (capacity > doubles.length) {
                int newsize = ((doubles.length * 3) / 2) + 1;
                double[] olddata = doubles;
                doubles = new double[newsize < capacity ? capacity : newsize];
                System.arraycopy(olddata, 0, doubles, 0, size);
            }
        }
    }
}
