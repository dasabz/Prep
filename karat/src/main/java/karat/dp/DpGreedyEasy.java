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

    // Step 1 · brute force
    public static class ClimbingStairsRecursive {
        public int climbStairs(int n) {
            if (n <= 2) return n;
            // last move was 1 step or 2 steps
            return climbStairs(n - 1) + climbStairs(n - 2);
        }
    }

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

    // ====================================================================
    // 2. Min Cost Climbing Stairs
    // LeetCode 746 · Easy
    // Trick: best[i] = min(best[i−1] + cost[i−1], best[i−2] + cost[i−2])  ·  O(n)
    // ====================================================================

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

    // ====================================================================
    // 3. Best Time to Buy and Sell Stock
    // LeetCode 121 · greedy · Easy
    // Trick: keep cheapest so far; profit = price − cheapest  ·  O(n)
    // ====================================================================

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

    // ====================================================================
    // 4. Assign Cookies
    // LeetCode 455 · greedy · Easy
    // Trick: sort both; give the smallest cookie that satisfies the least greedy child  ·  O(n log n)
    // ====================================================================

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

}
