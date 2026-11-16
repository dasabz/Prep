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

    /**
     * QUESTION
     * Given a sorted list of distinct integers, describe it as ranges: "a->b"
     * for a run of consecutive numbers, or just "a" for a number on its own.
     *
     * EXAMPLE
     *   [0, 1, 2, 4, 5, 7]  ->  ["0->2", "4->5", "7"]
     *
     * STEP 1 · BRUTE FORCE
     * A single left-to-right pass is the natural solution, and there's
     * nothing slower worth writing. The skill being tested is clean run
     * handling.
     *
     * STEP 2 · THE IDEA
     * Remember where the current run starts. Keep stepping forward while the
     * next number is exactly one more. When the run breaks, write either
     * start or start->end, then start the next run.
     */
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

    /*
     * Time: O(n) · Space: O(1) besides the output
     *
     * PATTERN
     * The "start of run → extend while it continues → write it out" loop is
     * the same one used in Count and Say and String Compression (Part 2).
     */

    // ====================================================================
    // 2. Teemo Attacking
    // LeetCode 495 · Easy
    // Trick: add min(duration, gap to next attack); last one adds duration  ·  O(n)
    // ====================================================================

    /**
     * QUESTION
     * Each attack at time t poisons the target for duration seconds (t to t +
     * duration − 1). A new attack resets the timer. How many seconds is the
     * target poisoned in total?
     *
     * EXAMPLE
     *   attacks [1, 2, 6], duration 3   ->  7
     *   poisoned seconds: 1 2 3 4 (attack at 2 restarts the timer) and 6 7 8
     *
     * STEP 1 · BRUTE FORCE
     * Put every poisoned second into a HashSet and count them.
     *
     * Brute force cost: O(n × duration) time and space, which is hopeless
     * when duration is 10⁹.
     */
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

    /**
     * STEP 2 · THE BETTER IDEA
     * Each attack contributes either its full duration or the gap to the next
     * attack, whichever is smaller, because the next attack cuts it short.
     * The last attack always gets its full duration.
     *
     * DRY RUN
     *   attack 1: next at 2 -> min(3, 1) = 1
     *   attack 2: next at 6 -> min(3, 4) = 3
     *   attack 6: last      -> 3          total = 7
     */
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

    /*
     * Time: O(n) · Space: O(1)
     *
     * PATTERN
     * "Total covered time" of sorted intervals: add each interval's length,
     * minus whatever overlaps the next one. The same idea underlies merged-
     * interval lengths.
     */

    // ====================================================================
    // 3. Maximum Average Subarray I
    // LeetCode 643 · fixed-size sliding window · Easy
    // Trick: window sum: add the new number, subtract the one leaving  ·  O(n)
    // ====================================================================

    /**
     * QUESTION
     * Find the contiguous block of exactly k numbers with the largest
     * average.
     *
     * EXAMPLE
     *   [1, 12, -5, -6, 50, 3], k = 4   ->  12.75     (12 - 5 - 6 + 50 = 51, / 4)
     *
     * STEP 1 · BRUTE FORCE
     * For every starting position, add up all k numbers again.
     *
     * Brute force cost: O(n × k) time
     */
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

    /**
     * STEP 2 · THE BETTER IDEA
     * Neighbouring windows share k − 1 numbers. When the window slides one
     * step right, add the number coming in and subtract the number going out.
     * Track the best sum, and divide by k only once at the end.
     *
     * DRY RUN
     *   window [1, 12, -5, -6]   sum = 2
     *   slide: +50 -1            sum = 51   best
     *   slide: +3  -12           sum = 42
     *   best 51 / 4 = 12.75
     */
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

    /*
     * Time: O(n) · Space: O(1)
     *
     * THIS IS THE SIMPLEST SLIDING WINDOW
     * Fixed size: add one in, take one out. Every other window problem in
     * this part grows and shrinks, but the add/remove bookkeeping is the
     * same.
     */

}
