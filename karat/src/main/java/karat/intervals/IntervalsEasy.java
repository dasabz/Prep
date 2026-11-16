package karat.intervals;

import java.util.*;

/**
 * Part 5 · Time Windows & Intervals · Easy (3 problems).
 *
 * Explanations, dry runs and pitfalls: pdfs/Karat-5a-Easy-Time-Windows-Intervals.pdf
 * Each problem: Step 1 = brute force, Step 2 = the better way.
 * Every example is checked in src/test/java/karat/intervals/IntervalsTests.java
 */
public final class IntervalsEasy {

    private IntervalsEasy() { }

    // ====================================================================
    // 1. Summary Ranges
    // LeetCode 228 · Easy
    // Trick: walk runs where next == current + 1  ·  O(n)
    // ====================================================================

    // Step 2 · the better way
    public static class SummaryRanges {
        public List<String> summaryRanges(int[] nums) {
            List<String> out = new ArrayList<>();
            int i = 0;
            while (i < nums.length) {
                int start = nums[i];
                // extend while the next number is exactly one more
                while (i + 1 < nums.length && nums[i + 1] == nums[i] + 1) i++;
                if (start == nums[i]) {
                    out.add(String.valueOf(start));        // a single number
                } else {
                    out.add(start + "->" + nums[i]);       // a range
                }
                i++;
            }
            return out;
        }
    }

    // ====================================================================
    // 2. Teemo Attacking
    // LeetCode 495 · Easy
    // Trick: add min(duration, gap to next attack); last one adds duration  ·  O(n)
    // ====================================================================

    // Step 1 · brute force
    public static class TeemoBrute {
        public int findPoisonedDuration(int[] timeSeries, int duration) {
            Set<Integer> poisoned = new HashSet<>();
            for (int t : timeSeries) {
                for (int s = t; s < t + duration; s++) {
                    poisoned.add(s);                       // every second
                }
            }
            return poisoned.size();
        }
    }

    // Step 2 · the better way
    public static class Teemo {
        public int findPoisonedDuration(int[] ts, int duration) {
            int total = 0;
            for (int i = 0; i < ts.length; i++) {
                if (i + 1 < ts.length) {
                    // poison is cut short if the next attack comes sooner
                    total += Math.min(duration, ts[i + 1] - ts[i]);
                } else {
                    total += duration;            // last one runs fully
                }
            }
            return total;
        }
    }

    // ====================================================================
    // 3. Maximum Average Subarray I
    // LeetCode 643 · fixed-size sliding window · Easy
    // Trick: window sum: add the new number, subtract the one leaving  ·  O(n)
    // ====================================================================

    // Step 1 · brute force
    public static class MaxAverageBrute {
        public double findMaxAverage(int[] nums, int k) {
            double best = Double.NEGATIVE_INFINITY;
            for (int i = 0; i + k <= nums.length; i++) {
                int sum = 0;
                for (int j = i; j < i + k; j++) {
                    sum += nums[j];                        // re-add k numbers
                }
                best = Math.max(best, (double) sum / k);
            }
            return best;
        }
    }

    // Step 2 · the better way
    public static class MaxAverage {
        public double findMaxAverage(int[] nums, int k) {
            int sum = 0;
            for (int i = 0; i < k; i++) sum += nums[i];    // first window
            int best = sum;
            for (int i = k; i < nums.length; i++) {
                sum += nums[i] - nums[i - k];     // add new, drop old
                best = Math.max(best, sum);
            }
            return (double) best / k;
        }
    }

}
