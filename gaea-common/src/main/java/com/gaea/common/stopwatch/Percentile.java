package com.gaea.common.stopwatch;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.gaea.common.util.extractor.Extractor;
import com.gaea.common.util.extractor.ExtractorUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.tuple.Pair;

public class Percentile implements Serializable {

    static final long serialVersionUID = -8091216485095130416L;

    private double    quantile         = 0.0;

    public Percentile() {
        this(50.0);
    }

    public Percentile(final double p) {
        setQuantile(p);
    }

    private void test(final double[] values, int start, int end) {
        if (start < 0 || start > values.length || end < start || end > values.length) {
            throw new IllegalArgumentException("This is not a valid subrange");
        }

    }

    public double evaluate(final double[] values, final double p) {
        test(values, 0, 0);
        return evaluate(values, 0, values.length, p);
    }

    public double evaluate(final double[] values, final int start, final int length) {
        return evaluate(values, start, length, quantile);
    }

    public double evaluate(final double[] values, final int begin, final int length, final double p) {
        return evaluates(values, begin, length, Collections.singleton(p)).get(p);
    }

    public Map<Double, Double> evaluates(final double[] values, final int begin, final int length, Collection<Double> percentiles) {
        if (CollectionUtils.isEmpty(percentiles)) {
            return Collections.emptyMap();
        }
        test(values, begin, length);
        if (length == 0) {
            return ExtractorUtils.extractAsMap(percentiles, new Extractor<Double, Pair<Double, Double>>() {

                @Override
                public Pair<Double, Double> extract(Double value) {
                    return Pair.of(value, Double.NaN);
                }
            });
        }

        if (length == 1) {
            return ExtractorUtils.extractAsMap(percentiles, new Extractor<Double, Pair<Double, Double>>() {

                @Override
                public Pair<Double, Double> extract(Double value) {
                    return Pair.of(value, values[begin]);
                }
            });
        }

        // Sort array
        final double[] sorted = new double[length];
        System.arraycopy(values, begin, sorted, 0, length);
        Arrays.sort(sorted);
        return ExtractorUtils.extractAsMap(percentiles, new Extractor<Double, Pair<Double, Double>>() {

            @Override
            public Pair<Double, Double> extract(Double percentile) {
                return Pair.of(percentile, evaluateSorted(sorted, percentile));
            }
        });

    }

    private double evaluateSorted(final double[] sorted, final double p) {
        double n = sorted.length;
        double pos = p * (n + 1) / 100;
        double fpos = Math.floor(pos);
        int intPos = (int) fpos;
        double dif = pos - fpos;

        if (pos < 1) {
            return sorted[0];
        }
        if (pos >= n) {
            return sorted[sorted.length - 1];
        }
        double lower = sorted[intPos - 1];
        double upper = sorted[intPos];
        return lower + dif * (upper - lower);
    }

    public static void main(String[] args) {
        System.out.println(95 * (100 + 1) / 100);
    }

    public double evaluate(List<Double> list, int p) {
        if ((p > 100) || (p <= 0)) {
            throw new IllegalArgumentException("invalid quantile value: " + p);
        }

        if (list.size() == 0) {
            return Double.NaN;
        }
        if (list.size() == 1) {
            return list.get(0); // always return single value for n = 1
        }

        // Sort array. We avoid a third copy here by just creating the
        // list directly.
        double[] sorted = new double[list.size()];
        for (int i = 0; i < list.size(); i++) {
            sorted[i] = list.get(i);
        }
        Arrays.sort(sorted);

        return evaluateSorted(sorted, p);
    }

    public double getQuantile() {
        return quantile;
    }

    public void setQuantile(final double p) {
        if (p <= 0 || p > 100) {
            throw new IllegalArgumentException("Illegal quantile value: " + p);
        }
        quantile = p;
    }

}
