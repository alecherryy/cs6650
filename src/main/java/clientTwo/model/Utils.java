package clientTwo.model;

import java.util.*;

/**
 * This is a Utility class. It contains useful methods
 * for formatting data.
 */
public final class Utils {
    /**
     * Use this method to calculate latency. Given a start
     * time and an end time, calculate the difference to
     * find total run time.
     *
     * @param start time
     * @param end time
     * @return the latency as a float
     */
    public static float calcLatency(long start, long end) {
        return (float) (end - start) / 1000000000.f;
    }

    /**
     * Use this method to calculate the mean.
     *
     * @param data to calculate
     * @return the mean response time
     */
    public static float calcMean(ArrayList<Float> data) {
        double sum = 0;
        for (float num : data) {
            sum += num;
        }
        return (float) (sum / data.size());
    }

    /**
     * Use this method to calculate the mean.
     *
     * @param data to calculate
     * @return the mean response time
     */
    public static float calcMedian(ArrayList<Float> data) {
        Collections.sort(data);
        return (data.get(data.size() / 2) + data.get(data.size() -1) / 2) / 2;
    }
}

