package karat.dp;

import java.util.*;

/**
 * Part 6 · Light DP & Greedy · Easy (4 problems).
 *
 * Explanations, dry runs and pitfalls: pdfs/Karat-6a-Easy-Light-DP-Greedy.pdf
 * Each problem: Step 1 = brute force, Step 2 = the better way.
 * Every example is checked in src/test/java/karat/dp/DpGreedyTests.java
 */
public final class DpGreedyEasy {

    private DpGreedyEasy() { }

    // ====================================================================
    // 1. Climbing Stairs
    // LeetCode 70 · Easy
    // Trick: ways[i] = ways[i−1] + ways[i−2]  ·  O(n)
    // ====================================================================

    /**
     * QUESTION
     * There are n steps, and each move climbs 1 or 2 of them. How many
     * different ways are there to reach the top?
     *
     * EXAMPLE
     *   n = 3  ->  3      (1+1+1, 1+2, 2+1)
     *   n = 5  ->  8
     *
     * STEP 1 · BRUTE FORCE
     * Write the rule directly as recursion: ways(n) = ways(n−1) + ways(n−2).
     *
     * Brute force cost: O(2ⁿ) time, because ways(n−2) is recomputed over and
     * over. n = 45 takes seconds. Storing each answer once (Step 2) makes it
     * O(n). That is the whole idea of DP.
     */
    // Step 1 · brute force
    public static class ClimbingStairsRecursive {
        public int climbStairs(int n) {
            if (n <= 2) return n;
            // last move was 1 step or 2 steps
            return climbStairs(n - 1) + climbStairs(n - 2);
        }
    }

    /**
     * STEP 2 · THE BETTER IDEA
     * To stand on step i, your last move came either from step i−1 (a 1-step)
     * or from step i−2 (a 2-step). So ways[i] = ways[i−1] + ways[i−2]. That's
     * the Fibonacci sequence.
     *
     * DRY RUN
     *   step i | 1 | 2 | 3 | 4 | 5
     *   ways[i] | 1 | 2 | 1+2 = 3 | 2+3 = 5 | 3+5 = 8
     */
    // Step 2 · the better way
    public static class ClimbingStairs {
        public int climbStairs(int n) {
            if (n <= 2) return n;
            int[] ways = new int[n + 1];    // ways[i] = ways to reach step i
            ways[1] = 1;                    // 1
            ways[2] = 2;                    // 1+1 or 2
            for (int i = 3; i <= n; i++) {
                // last move was 1 step (from i-1) or 2 steps (from i-2)
                ways[i] = ways[i - 1] + ways[i - 2];
            }
            return ways[n];
        }
    }

    /*
     * Time: O(n) · Space: O(n)
     *
     * WATCH OUT
     * - Return early for n ≤ 2. For n = 1 the array has size 2, so writing
     *   ways[2] would crash.
     *
     * IF THEY ASK FOR O(1) SPACE
     * You only ever need the last two values, so keep two variables, prev2
     * and prev1, and slide them forward instead of keeping the whole array.
     */

    // ====================================================================
    // 2. Min Cost Climbing Stairs
    // LeetCode 746 · Easy
    // Trick: best[i] = min(best[i−1] + cost[i−1], best[i−2] + cost[i−2])  ·  O(n)
    // ====================================================================

    /**
     * QUESTION
     * cost[i] is the price of standing on step i. You can start on step 0 or
     * step 1, and each move climbs 1 or 2 steps. What's the cheapest way to
     * get past the last step?
     *
     * EXAMPLE
     *   [10, 15, 20]  ->  15     (start on step 1, pay 15, jump 2 to the top)
     *
     * STEP 1 · BRUTE FORCE
     * Use recursion. From step i you pay cost[i], then take the cheaper of
     * jumping 1 or 2 steps.
     *
     * Brute force cost: O(2ⁿ), because the same steps are solved again and
     * again.
     */
    // Step 1 · brute force
    public static class MinCostStairsRecursive {
        public int minCostClimbingStairs(int[] cost) {
            return Math.min(from(cost, 0), from(cost, 1));  // start on 0 or 1
        }

        // cheapest way to the top if you stand on step i (and pay cost[i])
        private int from(int[] cost, int i) {
            if (i >= cost.length) return 0;            // already at the top
            return cost[i] + Math.min(from(cost, i + 1), from(cost, i + 2));
        }
    }

    /**
     * STEP 2 · THE BETTER IDEA
     * Let best[i] = the cheapest cost to arrive on step i, where the top is
     * step n. You arrive at i either from i − 1 (paying cost[i−1]) or from i
     * − 2 (paying cost[i−2]). Steps 0 and 1 are free starting points.
     *
     * DRY RUN ([10, 15, 20])
     *   best[0] = 0, best[1] = 0
     *   best[2] = min(0 + 10, 0 + 15) = 10
     *   best[3] = min(10 + 20, 0 + 15) = 15   <- the top
     */
    // Step 2 · the better way
    public static class MinCostStairs {
        public int minCostClimbingStairs(int[] cost) {
            int n = cost.length;
            // best[i] = cheapest cost to STAND on step i (the top is step n)
            int[] best = new int[n + 1];
            best[0] = 0;                               // you may start here
            best[1] = 0;                               // ...or here, for free
            for (int i = 2; i <= n; i++) {
                best[i] = Math.min(best[i - 1] + cost[i - 1],   // 1 step up
                                   best[i - 2] + cost[i - 2]);  // 2 steps up
            }
            return best[n];
        }
    }

    /*
     * Time: O(n) · Space: O(n) (O(1) with two variables)
     */

    // ====================================================================
    // 3. Best Time to Buy and Sell Stock
    // LeetCode 121 · greedy · Easy
    // Trick: keep cheapest so far; profit = price − cheapest  ·  O(n)
    // ====================================================================

    /**
     * QUESTION
     * prices[i] is the price on day i. Buy once, then sell once on a later
     * day. Return the biggest profit, or 0 if no profit is possible.
     *
     * EXAMPLE
     *   [7, 1, 5, 3, 6, 4]  ->  5      (buy at 1, sell at 6)
     *   [7, 6, 4, 3, 1]     ->  0      (prices only fall)
     *
     * STEP 1 · BRUTE FORCE
     * Try every buy day with every later sell day.
     *
     * Brute force cost: O(n²) time · O(1) space
     */
    // Step 1 · brute force
    public static class StockBrute {
        public int maxProfit(int[] prices) {
            int best = 0;
            for (int buy = 0; buy < prices.length; buy++) {
                for (int sell = buy + 1; sell < prices.length; sell++) {
                    best = Math.max(best, prices[sell] - prices[buy]);
                }
            }
            return best;
        }
    }

    /**
     * STEP 2 · THE BETTER IDEA
     * Go through the days once, keeping the cheapest price so far. On each
     * day, imagine selling today: the profit would be price − cheapest. Keep
     * the best profit you've seen.
     *
     * DRY RUN
     *   day | price | cheapest so far | profit if sold today | best
     *   0 | 7 | 7 | 0 | 0
     *   1 | 1 | 1 | 0 | 0
     *   2 | 5 | 1 | 4 | 4
     *   3 | 3 | 1 | 2 | 4
     *   4 | 6 | 1 | 5 | 5
     *   5 | 4 | 1 | 3 | 5
     */
    // Step 2 · the better way
    public static class BestTimeToBuySell {
        public int maxProfit(int[] prices) {
            int cheapest = Integer.MAX_VALUE;   // lowest price seen so far
            int best = 0;
            for (int price : prices) {
                cheapest = Math.min(cheapest, price);       // buy day?
                best = Math.max(best, price - cheapest);    // sell today?
            }
            return best;
        }
    }

    /*
     * Time: O(n) · Space: O(1)
     *
     * WATCH OUT
     * - Update cheapest before working out the profit. Selling on the buy day
     *   then gives 0, never a negative number.
     * - "Cheapest so far" only looks at earlier days, so you can never sell
     *   before you buy.
     */

    // ====================================================================
    // 4. Assign Cookies
    // LeetCode 455 · greedy · Easy
    // Trick: sort both; give the smallest cookie that satisfies the least greedy child  ·  O(n log n)
    // ====================================================================

    /**
     * QUESTION
     * Child i is happy with a cookie of size at least greed[i]. Each child
     * gets at most one cookie. How many children can you make happy?
     *
     * EXAMPLE
     *   greed [1, 2, 3], cookies [1, 1]      ->  1
     *   greed [1, 2],    cookies [1, 2, 3]   ->  2
     *
     * STEP 1 · BRUTE FORCE
     * Go through the children from least greedy to most greedy. For each one,
     * scan all the cookies for the smallest unused cookie that's big enough.
     *
     * Brute force cost: O(n × m) time
     */
    // Step 1 · brute force
    public static class AssignCookiesScan {
        public int findContentChildren(int[] greed, int[] cookies) {
            int[] g = greed.clone();
            Arrays.sort(g);                            // least greedy first
            boolean[] used = new boolean[cookies.length];
            int happy = 0;
            for (int need : g) {
                int pick = -1;              // smallest cookie that fits
                for (int j = 0; j < cookies.length; j++) {
                    if (!used[j] && cookies[j] >= need
                            && (pick == -1 || cookies[j] < cookies[pick])) {
                        pick = j;
                    }
                }
                if (pick != -1) {
                    used[pick] = true;
                    happy++;
                }
            }
            return happy;
        }
    }

    /**
     * STEP 2 · THE BETTER IDEA
     * Sort both lists, then walk them with two pointers. If the current
     * cookie satisfies the current child, both move on. If it's too small, it
     * can't satisfy anyone greedier either, so throw it away and try the next
     * cookie.
     */
    // Step 2 · the better way
    public static class AssignCookies {
        public int findContentChildren(int[] greed, int[] cookies) {
            int[] g = greed.clone(), s = cookies.clone();
            Arrays.sort(g);
            Arrays.sort(s);
            int child = 0, cookie = 0;
            while (child < g.length && cookie < s.length) {
                if (s[cookie] >= g[child]) {
                    child++;                           // this child is happy
                }
                cookie++;                   // cookie used, or too small
            }
            return child;
        }
    }

    /*
     * Time: O(n log n + m log m) · Space: O(n + m) for the sorted copies
     *
     * WHY GREEDY IS SAFE HERE
     * Using the smallest cookie that works never hurts, because bigger
     * cookies stay available for greedier children.
     */

}
