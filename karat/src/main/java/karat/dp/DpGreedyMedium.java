package karat.dp;

import java.util.*;

/**
 * Part 6 · Light DP & Greedy · Medium (16 problems).
 *
 * Explanations, dry runs and pitfalls: pdfs/Karat-6b-Medium-Light-DP-Greedy.pdf
 * Each problem: Step 1 = brute force, Step 2 = the better way.
 * Every example is checked in src/test/java/karat/dp/DpGreedyTests.java
 */
public final class DpGreedyMedium {

    private DpGreedyMedium() { }

    // ====================================================================
    // 1. Best Time to Buy and Sell Stock II
    // LeetCode 122 · greedy · Medium
    // Trick: add every positive day-to-day rise  ·  O(n)
    // ====================================================================

    /**
     * QUESTION
     * This time you can buy and sell as many times as you like, holding at
     * most one share at a time. What's the most profit?
     *
     * EXAMPLE
     *   [7, 1, 5, 3, 6, 4]  ->  7     (buy 1 sell 5: +4, buy 3 sell 6: +3)
     *   [1, 2, 3, 4, 5]     ->  4
     *
     * STEP 1 · BRUTE FORCE
     * Recursion: on each day, choose buy, sell or wait. It's exponential, and
     * it hides a much simpler truth (Step 2).
     *
     * STEP 2 · THE IDEA
     * Any profitable trade from day a to day b equals the sum of the daily
     * rises in between. So just add up every day where the price goes up:
     * prices[i] − prices[i−1] whenever it's positive.
     *
     * DRY RUN
     *   7->1 down   1->5 +4   5->3 down   3->6 +3   6->4 down     total 7
     */
    // Step 2 · the better way
    public static class StockII {
        public int maxProfit(int[] prices) {
            int profit = 0;
            for (int i = 1; i < prices.length; i++) {
                if (prices[i] > prices[i - 1]) {
                    profit += prices[i] - prices[i - 1];   // take every rise
                }
            }
            return profit;
        }
    }

    /*
     * Time: O(n) · Space: O(1)
     */

    // ====================================================================
    // 2. House Robber
    // LeetCode 198 · Medium
    // Trick: best[i] = max(best[i−1], best[i−2] + nums[i])  ·  O(n)
    // ====================================================================

    /**
     * QUESTION
     * Houses stand in a row, and nums[i] is the money in house i. You can't
     * rob two neighbouring houses. What's the most money you can take?
     *
     * EXAMPLE
     *   [2, 7, 9, 3, 1]  ->  12     (2 + 9 + 1)
     *   [1, 2, 3, 1]     ->  4      (1 + 3)
     *
     * STEP 1 · BRUTE FORCE
     * Write the choice as recursion: for house i, either skip it (best(i−1))
     * or rob it (nums[i] + best(i−2)).
     *
     * Brute force cost: O(2ⁿ) time, because the same best(i) is solved again
     * and again. Step 2 stores each answer in an array.
     */
    // Step 1 · brute force
    public static class HouseRobberRecursive {
        public int rob(int[] nums) {
            return best(nums, nums.length - 1);
        }

        // most money from houses 0..i
        private int best(int[] nums, int i) {
            if (i < 0) return 0;
            int skip = best(nums, i - 1);              // don't rob house i
            int take = nums[i] + best(nums, i - 2);    // rob i, skip i-1
            return Math.max(skip, take);
        }
    }

    /**
     * STEP 2 · THE BETTER IDEA
     * Let best[i] = the most money from houses 0..i. For house i there are
     * two choices:
     *
     * - skip it: best[i−1]
     * - rob it: nums[i] + best[i−2] (house i−1 must be skipped)
     *
     * Take whichever is bigger.
     *
     * DRY RUN ([2, 7, 9, 3, 1])
     *   i | nums[i] | skip = best[i−1] | take = best[i−2] + nums[i] | best[i]
     *   0 | 2 | – | – | 2
     *   1 | 7 | – | – | max(2, 7) = 7
     *   2 | 9 | 7 | 2 + 9 = 11 | 11
     *   3 | 3 | 11 | 7 + 3 = 10 | 11
     *   4 | 1 | 11 | 11 + 1 = 12 | 12
     */
    // Step 2 · the better way
    public static class HouseRobber {
        public int rob(int[] nums) {
            int n = nums.length;
            if (n == 1) return nums[0];
            int[] best = new int[n];     // best[i] = most money from 0..i
            best[0] = nums[0];
            best[1] = Math.max(nums[0], nums[1]);
            for (int i = 2; i < n; i++) {
                int skip = best[i - 1];              // don't rob house i
                int take = best[i - 2] + nums[i];    // rob i, so skip i-1
                best[i] = Math.max(skip, take);
            }
            return best[n - 1];
        }
    }

    /*
     * Time: O(n) · Space: O(n), or O(1) with two variables
     *
     * WATCH OUT
     * - best[1] = max(nums[0], nums[1]), not nums[1]. With only two houses,
     *   you take the richer one.
     * - With a single house, return nums[0] before touching nums[1].
     */

    // ====================================================================
    // 3. House Robber II
    // LeetCode 213 · Medium
    // Trick: circle → run House Robber twice: skip first OR skip last  ·  O(n)
    // ====================================================================

    /**
     * QUESTION
     * It's House Robber again, but the houses are in a circle, so the first
     * and last houses are neighbours too.
     *
     * EXAMPLE
     *   [2, 3, 2]     ->  3      (can't take both 2s; they're neighbours)
     *   [1, 2, 3, 1]  ->  4
     *
     * STEP 1 · BRUTE FORCE
     * Try every set of non-adjacent houses around the circle. That's
     * exponential, and the circle is really just two straight-line problems.
     *
     * STEP 2 · THE IDEA
     * Because the first and last houses can't both be robbed, any answer
     * either skips the last house or skips the first. So run plain House
     * Robber on nums[0 .. n−2] and on nums[1 .. n−1], and take the larger
     * result.
     */
    // Step 2 · the better way
    public static class HouseRobberII {
        public int rob(int[] nums) {
            int n = nums.length;
            if (n == 1) return nums[0];
            // first and last are neighbours: never take both
            return Math.max(robLine(nums, 0, n - 2),   // skip the last house
                            robLine(nums, 1, n - 1));  // skip the first house
        }

        // House Robber I on nums[from..to]
        private int robLine(int[] nums, int from, int to) {
            int prev2 = 0, prev1 = 0;                  // best(i-2), best(i-1)
            for (int i = from; i <= to; i++) {
                int cur = Math.max(prev1, prev2 + nums[i]);
                prev2 = prev1;
                prev1 = cur;
            }
            return prev1;
        }
    }

    /*
     * Time: O(n) · Space: O(1)
     *
     * WATCH OUT
     * - One house: return it directly, because both ranges would be empty.
     * - robLine uses two variables instead of an array. That's the O(1)-space
     *   version of House Robber I.
     */

    // ====================================================================
    // 4. Maximum Subarray (Kadane)
    // LeetCode 53 · Medium
    // Trick: cur = max(x, cur + x); best = max(best, cur)  ·  O(n)
    // ====================================================================

    /**
     * QUESTION
     * Find the contiguous block with the largest sum and return that sum.
     *
     * EXAMPLE
     *   [-2, 1, -3, 4, -1, 2, 1, -5, 4]   ->  6      ([4, -1, 2, 1])
     *
     * STEP 1 · BRUTE FORCE
     * Try every start and keep a running sum as the end moves right.
     *
     * Brute force cost: O(n²) time
     */
    // Step 1 · brute force
    public static class MaxSubarrayBrute {
        public int maxSubArray(int[] nums) {
            int best = Integer.MIN_VALUE;
            for (int i = 0; i < nums.length; i++) {
                int sum = 0;
                for (int j = i; j < nums.length; j++) {
                    sum += nums[j];                    // sum of nums[i..j]
                    best = Math.max(best, sum);
                }
            }
            return best;
        }
    }

    /**
     * STEP 2 · THE BETTER IDEA
     * Kadane's algorithm: let cur be the best sum of a block ending at the
     * current index. At each number, either extend the previous block (cur +
     * x) or start fresh (x). If the previous block is negative, starting
     * fresh is better. The overall answer is the largest cur you ever see.
     *
     * DRY RUN
     *   x:    -2   1   -3   4   -1   2   1   -5   4
     *   cur:  -2   1   -2   4    3   5   6    1   5
     *   best: -2   1    1   4    4   5   6    6   6
     */
    // Step 2 · the better way
    public static class MaxSubarray {
        public int maxSubArray(int[] nums) {
            int cur = nums[0];        // best sum of a block ENDING here
            int best = nums[0];
            for (int i = 1; i < nums.length; i++) {
                // extend the old block, or start fresh at nums[i]?
                cur = Math.max(nums[i], cur + nums[i]);
                best = Math.max(best, cur);
            }
            return best;
        }
    }

    /*
     * Time: O(n) · Space: O(1)
     *
     * WATCH OUT
     * - If every number is negative, the answer is the largest single number,
     *   not 0. That's why cur and best start at nums[0].
     */

    // ====================================================================
    // 5. Jump Game  ★ Karat
    // LeetCode 55 · greedy · Medium
    // Trick: farthest; if i > farthest → false  ·  O(n)
    // ====================================================================

    /**
     * QUESTION
     * nums[i] is the maximum jump length from index i. Starting at index 0,
     * can you reach the last index?
     *
     * EXAMPLE
     *   [2, 3, 1, 1, 4]  ->  true
     *   [3, 2, 1, 0, 4]  ->  false     (every route lands on index 3, which is 0)
     *
     * STEP 1 · BRUTE FORCE
     * Mark which indexes you can stand on. From every reachable index, mark
     * every index it can jump to.
     *
     * Brute force cost: O(n²) time in the worst case (big jump values). Step
     * 2 notices you only need the farthest reach.
     */
    // Step 1 · brute force
    public static class JumpGameDP {
        public boolean canJump(int[] nums) {
            int n = nums.length;
            boolean[] reach = new boolean[n];   // can we stand on i?
            reach[0] = true;
            for (int i = 0; i < n; i++) {
                if (!reach[i]) continue;
                for (int j = 1; j <= nums[i] && i + j < n; j++) {
                    reach[i + j] = true;               // mark every landing
                }
            }
            return reach[n - 1];
        }
    }

    /**
     * STEP 2 · THE BETTER IDEA
     * Keep farthest, the farthest index you can reach so far. Walk left to
     * right. If i > farthest, you can't even stand on i, so return false.
     * Otherwise, extend farthest = max(farthest, i + nums[i]).
     *
     * Why greedy works: you never need to know which jumps you took, only how
     * far you could get.
     *
     * DRY RUN ([3, 2, 1, 0, 4])
     *   i | nums[i] | can stand on i? | farthest after
     *   0 | 3 | yes | 3
     *   1 | 2 | yes | 3
     *   2 | 1 | yes | 3
     *   3 | 0 | yes | 3
     *   4 | 4 | 4 > 3 → false |
     */
    // Step 2 · the better way
    public static class JumpGame {
        public boolean canJump(int[] nums) {
            int farthest = 0;                  // farthest index reachable
            for (int i = 0; i < nums.length; i++) {
                if (i > farthest) return false;           // can't get to i
                farthest = Math.max(farthest, i + nums[i]);
            }
            return true;
        }
    }

    /*
     * Time: O(n) · Space: O(1)
     *
     * WATCH OUT
     * - [0] returns true, because you're already at the end.
     * - Optional speed-up: return true as soon as farthest >= nums.length -
     *   1.
     *
     * POSSIBLE FOLLOW-UP
     * "What's the minimum number of jumps?" (Jump Game II, LeetCode 45). Go
     * greedily level by level, like BFS: all the indexes reachable in 1 jump,
     * then in 2 jumps, and so on. Count the levels until the end is
     * reachable.
     */

    // ====================================================================
    // 6. Jump Game II
    // LeetCode 45 · greedy levels · Medium
    // Trick: BFS-like levels: when i hits levelEnd, jump++ and levelEnd = farthest  ·  O(n)
    // ====================================================================

    /**
     * QUESTION
     * As in Jump Game, nums[i] is the maximum jump from i. You can always
     * reach the end. Return the fewest jumps needed.
     *
     * EXAMPLE
     *   [2, 3, 1, 1, 4]  ->  2      (0 -> 1 -> 4)
     *
     * STEP 1 · BRUTE FORCE
     * Use DP. minJumps[i + j] = min(minJumps[i + j], minJumps[i] + 1) for
     * every jump length j from every index i.
     *
     * Brute force cost: O(n²) time in the worst case
     */
    // Step 1 · brute force
    public static class JumpGameIIDP {
        public int jump(int[] nums) {
            int n = nums.length;
            int[] minJumps = new int[n];
            Arrays.fill(minJumps, Integer.MAX_VALUE);
            minJumps[0] = 0;
            for (int i = 0; i < n; i++) {
                if (minJumps[i] == Integer.MAX_VALUE) continue;
                for (int j = 1; j <= nums[i] && i + j < n; j++) {
                    int viaI = minJumps[i] + 1;     // land on i+j from i
                    minJumps[i + j] = Math.min(minJumps[i + j], viaI);
                }
            }
            return minJumps[n - 1];
        }
    }

    /**
     * STEP 2 · THE BETTER IDEA
     * Think in levels, like BFS: level 0 is index 0; level 1 is every index
     * reachable in 1 jump; and so on. Walk left to right, tracking farthest,
     * the furthest index the next jump could reach. When i reaches the end of
     * the current level, you must jump: add 1 to jumps, and the new level
     * ends at farthest.
     *
     * DRY RUN ([2, 3, 1, 1, 4])
     *   i=0: farthest 2;  i == levelEnd 0 -> jumps 1, levelEnd 2
     *   i=1: farthest 4
     *   i=2: farthest 4;  i == levelEnd 2 -> jumps 2, levelEnd 4  (covers the end)
     */
    // Step 2 · the better way
    public static class JumpGameII {
        public int jump(int[] nums) {
            int jumps = 0;
            int levelEnd = 0;     // last index reachable in 'jumps' jumps
            int farthest = 0;         // farthest index with one more jump
            for (int i = 0; i < nums.length - 1; i++) {
                farthest = Math.max(farthest, i + nums[i]);
                if (i == levelEnd) {  // used up this level: must jump again
                    jumps++;
                    levelEnd = farthest;
                }
            }
            return jumps;
        }
    }

    /*
     * Time: O(n) · Space: O(1)
     *
     * WATCH OUT
     * - Loop to n − 2, not n − 1. Once you're standing on the last index, you
     *   don't jump again.
     */

    // ====================================================================
    // 7. Partition Labels
    // LeetCode 763 · greedy · Medium
    // Trick: last[c] per letter; extend end = max(end, last[c]); cut when i == end  ·  O(n)
    // ====================================================================

    /**
     * QUESTION
     * Split the string into as many parts as possible so that each letter
     * appears in only one part. Return the part sizes.
     *
     * EXAMPLE
     *   "ababcbacadefegdehijhklij"  ->  [9, 7, 8]
     *    ababcbaca | defegde | hijhklij
     *
     * STEP 1 · BRUTE FORCE
     * The same greedy, but find each letter's last position with
     * s.lastIndexOf(c), which scans the whole string every time.
     *
     * Brute force cost: O(n²) time
     */
    // Step 1 · brute force
    public static class PartitionLabelsScan {
        public List<Integer> partitionLabels(String s) {
            List<Integer> sizes = new ArrayList<>();
            int start = 0, end = 0;
            for (int i = 0; i < s.length(); i++) {
                // lastIndexOf scans the whole string EVERY time
                end = Math.max(end, s.lastIndexOf(s.charAt(i)));
                if (i == end) {
                    sizes.add(end - start + 1);
                    start = i + 1;
                }
            }
            return sizes;
        }
    }

    /**
     * STEP 2 · THE BETTER IDEA
     * Precompute last[c], the last index of every letter. Walk the string
     * keeping end, the furthest last-occurrence of any letter in the current
     * part. When i reaches end, nothing in this part appears later, so cut
     * here and start a new part.
     *
     * DRY RUN (FIRST PART)
     *   a: last 8 -> end 8    b: last 5    c: last 7   ... i reaches 8 = end -> cut (size 9)
     */
    // Step 2 · the better way
    public static class PartitionLabels {
        public List<Integer> partitionLabels(String s) {
            int[] last = new int[26];          // last index of each letter
            for (int i = 0; i < s.length(); i++) last[s.charAt(i) - 'a'] = i;
            List<Integer> sizes = new ArrayList<>();
            int start = 0, end = 0;
            for (int i = 0; i < s.length(); i++) {
                // this part must stretch at least to this letter's last spot
                end = Math.max(end, last[s.charAt(i) - 'a']);
                if (i == end) {                // nothing reaches further
                    sizes.add(end - start + 1);
                    start = i + 1;
                }
            }
            return sizes;
        }
    }

    /*
     * Time: O(n) · Space: O(1) (26 letters)
     *
     * SAME TRICK AS MERGE INTERVALS
     * Each letter spans an interval [first, last]. The parts are exactly the
     * merged intervals.
     */

    // ====================================================================
    // 8. Unique Paths
    // LeetCode 62 · grid DP · Medium
    // Trick: ways[r][c] = ways[r−1][c] + ways[r][c−1]; edges = 1  ·  O(m × n)
    // ====================================================================

    /**
     * QUESTION
     * A robot starts at the top-left of an m × n grid and can only move right
     * or down. How many different paths reach the bottom-right?
     *
     * EXAMPLE
     *   m = 3, n = 7  ->  28
     *   m = 3, n = 2  ->  3      (RDD, DRD, DDR)
     *
     * STEP 1 · BRUTE FORCE
     * Recursion: the paths to (r, c) are the paths to the cell above plus the
     * paths to the cell on the left.
     *
     * Brute force cost: exponential, because the same cells are recomputed
     * many times.
     */
    // Step 1 · brute force
    public static class UniquePathsRecursive {
        public int uniquePaths(int m, int n) {
            return paths(m - 1, n - 1);
        }

        private int paths(int r, int c) {
            if (r == 0 || c == 0) return 1;       // along an edge: one way
            return paths(r - 1, c) + paths(r, c - 1);  // from above or left
        }
    }

    /**
     * STEP 2 · THE BETTER IDEA
     * Fill a table top-left to bottom-right. Cells in the first row or first
     * column have exactly 1 path (a straight line). Every other cell = the
     * cell above + the cell to the left.
     *
     * DRY RUN (3 × 3)
     *   1  1  1
     *   1  2  3
     *   1  3  6     ->  6
     */
    // Step 2 · the better way
    public static class UniquePaths {
        public int uniquePaths(int m, int n) {
            int[][] ways = new int[m][n];              // ways to reach (r, c)
            for (int r = 0; r < m; r++) {
                for (int c = 0; c < n; c++) {
                    if (r == 0 || c == 0) {
                        ways[r][c] = 1;                // first row / column
                    } else {
                        ways[r][c] = ways[r - 1][c] + ways[r][c - 1];
                    }
                }
            }
            return ways[m - 1][n - 1];
        }
    }

    /*
     * Time: O(m × n) · Space: O(m × n), or one row if you reuse it
     */

    // ====================================================================
    // 9. Minimum Path Sum
    // LeetCode 64 · grid DP · Medium
    // Trick: best[r][c] = grid[r][c] + min(above, left)  ·  O(R × C)
    // ====================================================================

    /**
     * QUESTION
     * Moving only right or down, find the path from top-left to bottom-right
     * with the smallest sum of cell values.
     *
     * EXAMPLE
     *   1 3 1
     *   1 5 1      ->  7      (1 → 3 → 1 → 1 → 1)
     *   4 2 1
     *
     * STEP 1 · BRUTE FORCE
     * Recursion: the cheapest way to (r, c) is the cell's value plus the
     * cheaper of the cell above and the cell to the left.
     *
     * Brute force cost: exponential
     */
    // Step 1 · brute force
    public static class MinPathSumRecursive {
        public int minPathSum(int[][] g) {
            return best(g, g.length - 1, g[0].length - 1);
        }

        // cheapest path from (0,0) to (r,c)
        private int best(int[][] g, int r, int c) {
            if (r == 0 && c == 0) return g[0][0];
            if (r == 0) return g[r][c] + best(g, r, c - 1);  // from left
            if (c == 0) return g[r][c] + best(g, r - 1, c);  // from above
            return g[r][c] + Math.min(best(g, r - 1, c), best(g, r, c - 1));
        }
    }

    /**
     * STEP 2 · THE BETTER IDEA
     * It's the same table as Unique Paths, but you take min instead of add.
     * The first row can only come from the left and the first column only
     * from above. Every other cell = its own value + min(above, left).
     *
     * DRY RUN
     *   grid        best
     *   1 3 1       1 4 5
     *   1 5 1  ->   2 7 6
     *   4 2 1       6 8 7     ->  7
     */
    // Step 2 · the better way
    public static class MinPathSum {
        public int minPathSum(int[][] g) {
            int rows = g.length, cols = g[0].length;
            int[][] best = new int[rows][cols];   // cheapest cost to (r,c)
            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < cols; c++) {
                    if (r == 0 && c == 0) {
                        best[r][c] = g[0][0];
                    } else if (r == 0) {
                        best[r][c] = best[r][c - 1] + g[r][c];
                    } else if (c == 0) {
                        best[r][c] = best[r - 1][c] + g[r][c];
                    } else {
                        best[r][c] = Math.min(best[r - 1][c], best[r][c - 1])
                                   + g[r][c];
                    }
                }
            }
            return best[rows - 1][cols - 1];
        }
    }

    /*
     * Time: O(R × C) · Space: O(R × C)
     */

    // ====================================================================
    // 10. Maximal Square  ★ Karat
    // LeetCode 221 · Medium
    // Trick: 1 + min(up, left, diag); return side²  ·  O(R·C)
    // ====================================================================

    /**
     * QUESTION
     * In a grid of '0' and '1', find the biggest square made only of 1s and
     * return its area.
     *
     * EXAMPLE
     *   1 0 1 0 0
     *   1 0 1 1 1      ->  4      (a 2x2 square at rows 1-2, columns 3-4)
     *   1 1 1 1 1
     *   1 0 0 1 0
     *
     * STEP 1 · BRUTE FORCE
     * For every cell as a top-left corner, grow a square one size at a time,
     * checking only the new bottom row and right column, until a '0' appears.
     *
     * Brute force cost: O(R × C × min(R, C)²) time. Step 2 reuses the answers
     * of the three neighbouring cells instead.
     */
    // Step 1 · brute force
    public static class MaximalSquareBrute {
        public int maximalSquare(char[][] m) {
            int rows = m.length, cols = m[0].length, best = 0;
            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < cols; c++) {       // (r, c) = top-left
                    int k = 0;
                    // grow while the next row and column are all '1'
                    while (r + k < rows && c + k < cols
                            && edgeOk(m, r, c, k)) {
                        k++;
                    }
                    best = Math.max(best, k);
                }
            }
            return best * best;
        }

        // is the new bottom row and right column of a (k+1) square all 1?
        private boolean edgeOk(char[][] m, int r, int c, int k) {
            for (int i = 0; i <= k; i++) {
                if (m[r + k][c + i] != '1') return false;   // bottom row
                if (m[r + i][c + k] != '1') return false;   // right column
            }
            return true;
        }
    }

    /**
     * STEP 2 · THE BETTER IDEA
     * Let side[r][c] = the side of the biggest all-1 square whose bottom-
     * right corner is (r, c). A square of side k there needs squares of side
     * k−1 ending at the cell above, the cell to the left, and the diagonal
     * cell. The smallest of the three limits you:
     *
     * side[r][c] = 1 + min(up, left, diag)
     *
     *   diag | up
     *   -----+------
     *   left | (r,c)     side[r][c] = 1 + min(up, left, diag)
     *
     * DRY RUN (THE SIDE TABLE FOR THE EXAMPLE)
     *   matrix          side
     *   1 0 1 0 0       1 0 1 0 0
     *   1 0 1 1 1       1 0 1 1 1
     *   1 1 1 1 1       1 1 1 2 2      (2,3): 1 + min(up 1, left 1, diag 1) = 2
     *   1 0 0 1 0       1 0 0 1 0
     *                   biggest side = 2  ->  area = 4
     */
    // Step 2 · the better way
    public static class MaximalSquare {
        public int maximalSquare(char[][] matrix) {
            int rows = matrix.length, cols = matrix[0].length;
            // side[r][c] = side of the biggest all-1 square
            //              whose bottom-right corner is (r, c)
            int[][] side = new int[rows][cols];
            int best = 0;
            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < cols; c++) {
                    if (matrix[r][c] == '0') continue;     // stays 0
                    if (r == 0 || c == 0) {
                        side[r][c] = 1;                    // edge cell
                    } else {
                        int up = side[r - 1][c];
                        int left = side[r][c - 1];
                        int diag = side[r - 1][c - 1];
                        side[r][c] = 1 + Math.min(up, Math.min(left, diag));
                    }
                    best = Math.max(best, side[r][c]);
                }
            }
            return best * best;                            // area
        }
    }

    /*
     * Time: O(rows × cols) · Space: O(rows × cols)
     *
     * WATCH OUT
     * - Return the area (best * best), not the side.
     * - The grid holds chars, so compare with '1', not 1.
     */

    // ====================================================================
    // 11. Coin Change
    // LeetCode 322 · Medium
    // Trick: fewest[a] = min over coins of fewest[a − coin] + 1  ·  O(amount × coins)
    // ====================================================================

    /**
     * QUESTION
     * What's the fewest coins that add up to amount? Each coin type can be
     * used any number of times. Return −1 if it can't be done.
     *
     * EXAMPLE
     *   coins [1, 2, 5], amount 11  ->  3     (5 + 5 + 1)
     *   coins [1, 3, 4], amount 6   ->  2     (3 + 3; greedy 4+1+1 would give 3!)
     *
     * STEP 1 · BRUTE FORCE
     * Recursion: try each coin as the last coin and solve the smaller amount.
     *
     * Brute force cost: exponential. Even amount 100 is painfully slow.
     */
    // Step 1 · brute force
    public static class CoinChangeRecursive {
        public int coinChange(int[] coins, int amount) {
            if (amount == 0) return 0;
            int best = Integer.MAX_VALUE;
            for (int coin : coins) {              // try each coin as last
                if (coin > amount) continue;
                int rest = coinChange(coins, amount - coin);
                if (rest != -1) best = Math.min(best, rest + 1);
            }
            return best == Integer.MAX_VALUE ? -1 : best;
        }
    }

    /**
     * STEP 2 · THE BETTER IDEA
     * Build the answers for every amount from 0 up: fewest[a] = min(fewest[a
     * − coin] + 1) over every coin that fits. fewest[0] = 0. Amounts you
     * can't make stay at "infinity".
     *
     * DRY RUN (COINS [1, 3, 4], AMOUNT 6)
     *   a:      0  1  2  3  4  5  6
     *   fewest: 0  1  2  1  1  2  2      (6 = 3 + 3)
     */
    // Step 2 · the better way
    public static class CoinChange {
        public int coinChange(int[] coins, int amount) {
            int INF = Integer.MAX_VALUE;
            int[] fewest = new int[amount + 1];   // fewest coins to make a
            Arrays.fill(fewest, INF);
            fewest[0] = 0;
            for (int a = 1; a <= amount; a++) {
                for (int coin : coins) {
                    if (coin <= a && fewest[a - coin] != INF) {
                        fewest[a] = Math.min(fewest[a], fewest[a - coin] + 1);
                    }
                }
            }
            return fewest[amount] == INF ? -1 : fewest[amount];
        }
    }

    /*
     * Time: O(amount × coins) · Space: O(amount)
     *
     * WATCH OUT
     * - Greedy fails here. Always taking the biggest coin gives 4 + 1 + 1 = 3
     *   coins for 6, but the answer is 2. That's why this needs DP.
     * - Check fewest[a − coin] != INF before adding 1, or the value
     *   overflows.
     */

    // ====================================================================
    // 12. Decode Ways
    // LeetCode 91 · Medium
    // Trick: ways[i] = ways[i+1] (1 digit) + ways[i+2] (if 10..26); '0' → 0  ·  O(n)
    // ====================================================================

    /**
     * QUESTION
     * Letters are encoded as A = 1 … Z = 26. Given a digit string, how many
     * ways can it be decoded?
     *
     * EXAMPLE
     *   "12"   ->  2     (AB, L)
     *   "226"  ->  3     (BBF, BZ, VF)
     *   "06"   ->  0     (no letter is "06")
     *
     * STEP 1 · BRUTE FORCE
     * Recursion from position i: take one digit (if it isn't '0'), or take
     * two digits (if they make 10–26).
     *
     * Brute force cost: exponential, like Fibonacci.
     */
    // Step 1 · brute force
    public static class DecodeWaysRecursive {
        public int numDecodings(String s) {
            return ways(s, 0);
        }

        // number of ways to decode s from position i to the end
        private int ways(String s, int i) {
            if (i == s.length()) return 1;             // decoded everything
            if (s.charAt(i) == '0') return 0;     // no letter starts with 0
            int total = ways(s, i + 1);                // take 1 digit
            if (i + 1 < s.length()
                    && Integer.parseInt(s.substring(i, i + 2)) <= 26) {
                total += ways(s, i + 2);          // take 2 digits (10..26)
            }
            return total;
        }
    }

    /**
     * STEP 2 · THE BETTER IDEA
     * Let ways[i] = the number of ways to decode s[i..], filled from the end
     * backwards. If s[i] is '0', ways[i] = 0. Otherwise ways[i] = ways[i+1],
     * plus ways[i+2] if the two digits starting at i make 10–26. The empty
     * tail counts as 1 way.
     *
     * DRY RUN ("226")
     *   ways[3] = 1
     *   i=2 '6':  ways = ways[3] = 1
     *   i=1 '2':  ways[2] = 1, and "26" <= 26 -> + ways[3] = 2
     *   i=0 '2':  ways[1] = 2, and "22" <= 26 -> + ways[2] = 3
     */
    // Step 2 · the better way
    public static class DecodeWays {
        public int numDecodings(String s) {
            int n = s.length();
            int[] ways = new int[n + 1];          // ways to decode s[i..]
            ways[n] = 1;                               // empty tail: one way
            for (int i = n - 1; i >= 0; i--) {
                if (s.charAt(i) == '0') {
                    ways[i] = 0;
                    continue;
                }
                ways[i] = ways[i + 1];                 // one digit
                boolean twoOk = i + 1 < n
                        && Integer.parseInt(s.substring(i, i + 2)) <= 26;
                if (twoOk) {
                    ways[i] += ways[i + 2];            // two digits
                }
            }
            return ways[0];
        }
    }

    /*
     * Time: O(n) · Space: O(n)
     *
     * WATCH OUT
     * - Zeros are the trap. "10" and "20" are fine, but "30" and a leading
     *   "0" decode in 0 ways.
     */

    // ====================================================================
    // 13. Word Break
    // LeetCode 139 · Medium
    // Trick: ok[end] = ok[start] && dict has s[start..end)  ·  O(n² × L)
    // ====================================================================

    /**
     * QUESTION
     * Can the string be split into a sequence of dictionary words (words may
     * repeat)?
     *
     * EXAMPLE
     *   "karatcode", [karat, code]                  ->  true
     *   "catsandog", [cats, dog, sand, and, cat]    ->  false
     *
     * STEP 1 · BRUTE FORCE
     * Recursion: try every dictionary word as the first word and recurse on
     * the rest.
     *
     * Brute force cost: exponential on inputs like "aaaa…ab".
     */
    // Step 1 · brute force
    public static class WordBreakRecursive {
        public boolean wordBreak(String s, List<String> dict) {
            if (s.isEmpty()) return true;
            for (String w : dict) {                    // try each first word
                if (s.startsWith(w)
                        && wordBreak(s.substring(w.length()), dict)) {
                    return true;
                }
            }
            return false;
        }
    }

    /**
     * STEP 2 · THE BETTER IDEA
     * Let ok[i] = the first i letters can be split into words. ok[0] = true.
     * For each end, look for any start with ok[start] true where
     * s[start..end) is a dictionary word. If you find one, ok[end] = true.
     *
     * DRY RUN ("KARATCODE")
     *   ok[0] = T
     *   ok[5]: ok[0] && "karat" in dict  -> T
     *   ok[9]: ok[5] && "code"  in dict  -> T   ->  true
     */
    // Step 2 · the better way
    public static class WordBreak {
        public boolean wordBreak(String s, List<String> dict) {
            Set<String> words = new HashSet<>(dict);
            // ok[i] = the first i letters can be split into words
            boolean[] ok = new boolean[s.length() + 1];
            ok[0] = true;                              // empty prefix
            for (int end = 1; end <= s.length(); end++) {
                for (int start = 0; start < end; start++) {
                    String last = s.substring(start, end);
                    if (ok[start] && words.contains(last)) {
                        ok[end] = true;                // good prefix + a word
                        break;
                    }
                }
            }
            return ok[s.length()];
        }
    }

    /*
     * Time: O(n² × L) for the substrings · Space: O(n)
     *
     * SPEED-UP
     * Only check start values within the longest dictionary word's length of
     * end.
     */

    // ====================================================================
    // 14. Longest Increasing Subsequence
    // LeetCode 300 · Medium
    // Trick: O(n²): len[i] = 1 + max len[j] for nums[j] < nums[i]; O(n log n): tails  ·  O(n log n)
    // ====================================================================

    /**
     * QUESTION
     * Find the length of the longest strictly increasing subsequence. The
     * elements don't need to be next to each other.
     *
     * EXAMPLE
     *   [10, 9, 2, 5, 3, 7, 101, 18]  ->  4     (2, 3, 7, 18)
     *
     * STEP 1 · BRUTE FORCE
     * Checking all 2ⁿ subsequences is hopeless. The standard first answer is
     * O(n²) DP: len[i] = the longest run ending at i = 1 + the best len[j]
     * over earlier j with nums[j] < nums[i].
     *
     * Brute force cost: O(n²) time. This is the answer most interviewers
     * expect; Step 2 is the follow-up.
     */
    // Step 1 · brute force
    public static class LISQuadratic {
        public int lengthOfLIS(int[] nums) {
            int[] len = new int[nums.length];          // longest ENDING at i
            int best = 0;
            for (int i = 0; i < nums.length; i++) {
                len[i] = 1;                            // just nums[i] itself
                for (int j = 0; j < i; j++) {
                    if (nums[j] < nums[i]) {           // can put i after j
                        len[i] = Math.max(len[i], len[j] + 1);
                    }
                }
                best = Math.max(best, len[i]);
            }
            return best;
        }
    }

    /**
     * STEP 2 · THE BETTER IDEA
     * Keep a list tails, where tails[k] is the smallest possible last value
     * of an increasing run of length k + 1. For each number x, binary search
     * for the first tail ≥ x. If there isn't one, x makes a longer run, so
     * append it. Otherwise replace that tail with x: a smaller ending is
     * always better for the future. The length of tails is the answer.
     *
     * DRY RUN
     *   10 -> [10]       9 -> [9]        2 -> [2]       5 -> [2,5]
     *    3 -> [2,3]      7 -> [2,3,7]  101 -> [2,3,7,101]
     *   18 -> [2,3,7,18]                 ->  length 4
     */
    // Step 2 · the better way
    public static class LISFast {
        public int lengthOfLIS(int[] nums) {
            // tails.get(k) = smallest possible LAST value of an
            //                increasing run of length k + 1
            List<Integer> tails = new ArrayList<>();
            for (int x : nums) {
                int lo = 0, hi = tails.size();         // first tail >= x
                while (lo < hi) {
                    int mid = (lo + hi) / 2;
                    if (tails.get(mid) < x) lo = mid + 1;
                    else hi = mid;
                }
                if (lo == tails.size()) {
                    tails.add(x);                      // x makes a longer run
                } else {
                    tails.set(lo, x);              // x is a smaller ending
                }
            }
            return tails.size();
        }
    }

    /*
     * Time: O(n log n) · Space: O(n)
     *
     * WATCH OUT
     * - tails is not an actual subsequence. Only its length is meaningful.
     */

    // ====================================================================
    // 15. Longest Common Browsing History  ★ Karat
    // Karat story version of LeetCode 718 (Maximum Length of Repeated Subarray) · Medium
    // Trick: match → diag + 1, else 0; track best & end  ·  O(m·n)
    // ====================================================================

    /**
     * QUESTION
     * Karat version: you have two users' browsing histories (lists of pages,
     * in order). Find the longest run of pages that appears back to back in
     * both: the same pages in the same order with no gaps. Return that run.
     *
     * LeetCode 718: the same thing with two int arrays, returning only the
     * length.
     *
     * EXAMPLE
     *   user 1:  home, login, search, cart, pay
     *   user 2:  search, home, login, search, cart, logout
     *
     *   ->  [home, login, search, cart]
     *
     * STEP 1 · BRUTE FORCE
     * Try every pair of starting points (i, j), and extend while a[i + len]
     * equals b[j + len].
     *
     * Brute force cost: O(m × n × L) time, where L is the length of the
     * match. It's easy to write and fine for short histories; say it first in
     * Karat.
     */
    // Step 1 · brute force
    public static class CommonHistoryBrute {
        public List<String> longestCommon(String[] a, String[] b) {
            int best = 0, bestStart = 0;
            for (int i = 0; i < a.length; i++) {
                for (int j = 0; j < b.length; j++) {   // every pair of starts
                    int len = 0;
                    while (i + len < a.length && j + len < b.length
                            && a[i + len].equals(b[j + len])) {
                        len++;                         // extend while equal
                    }
                    if (len > best) {
                        best = len;
                        bestStart = i;
                    }
                }
            }
            List<String> run = new ArrayList<>();
            for (int k = bestStart; k < bestStart + best; k++) run.add(a[k]);
            return run;
        }
    }

    /**
     * STEP 2 · THE BETTER IDEA
     * Let len[i][j] = the length of the common run that ends exactly at
     * a[i−1] and b[j−1].
     *
     * - If a[i−1] equals b[j−1], the run continues the one that ended one
     *   step earlier in both lists: len[i][j] = len[i−1][j−1] + 1.
     * - If they differ, the run is broken: len[i][j] = 0.
     *
     * Keep track of the biggest value and where it ends in a.
     *
     * Why the +1 sizes? Row 0 and column 0 act as a border of zeros, so i−1
     * and j−1 never go below 0.
     *
     * DRY RUN (THE DIAGONAL THAT BUILDS THE ANSWER)
     *   a[0]=home   == b[1]=home    ->  len[1][2] = len[0][1] + 1 = 1
     *   a[1]=login  == b[2]=login   ->  len[2][3] = len[1][2] + 1 = 2
     *   a[2]=search == b[3]=search  ->  len[3][4] = len[2][3] + 1 = 3
     *   a[3]=cart   == b[4]=cart    ->  len[4][5] = len[3][4] + 1 = 4
     *                                   best = 4, endA = 4
     *
     *   (a[2]=search also equals b[0]=search -> len[3][1] = 1, a short run)
     *
     *   answer = a[endA - best .. endA - 1] = a[0..3] = home, login, search, cart
     */
    // Step 2 · the better way
    public static class LongestCommonHistory {
        // Karat: longest run of pages visited in the same order by both
        public List<String> longestCommon(String[] a, String[] b) {
            // len[i][j] = length of the common run that ENDS at
            //             a[i-1] and b[j-1]  (0 if they differ)
            int[][] len = new int[a.length + 1][b.length + 1];
            int best = 0, endA = 0;
            for (int i = 1; i <= a.length; i++) {
                for (int j = 1; j <= b.length; j++) {
                    if (a[i - 1].equals(b[j - 1])) {
                        len[i][j] = len[i - 1][j - 1] + 1;  // extend the run
                        if (len[i][j] > best) {
                            best = len[i][j];
                            endA = i;                       // run ends here
                        }
                    }
                }
            }
            // the run is a[endA - best] .. a[endA - 1]
            List<String> run = new ArrayList<>();
            for (int k = endA - best; k < endA; k++) {
                run.add(a[k]);
            }
            return run;
        }

        // LeetCode 718: same idea on int arrays, return only the length
        public int findLength(int[] a, int[] b) {
            int[][] len = new int[a.length + 1][b.length + 1];
            int best = 0;
            for (int i = 1; i <= a.length; i++) {
                for (int j = 1; j <= b.length; j++) {
                    if (a[i - 1] == b[j - 1]) {
                        len[i][j] = len[i - 1][j - 1] + 1;
                        best = Math.max(best, len[i][j]);
                    }
                }
            }
            return best;
        }
    }

    /*
     * Time: O(m × n) · Space: O(m × n)
     *
     * WATCH OUT
     * - This is a substring (no gaps). For a subsequence (gaps allowed; LCS,
     *   LeetCode 1143), when the items differ you take max(len[i−1][j],
     *   len[i][j−1]) instead of 0.
     * - Compare page names with .equals(), never ==.
     *
     * KARAT LINK
     * This often follows the domain-visit counting question from Part 1
     * (Subdomain Visit Count). The data is the same kind of log; only the
     * question changes.
     */

    // ====================================================================
    // 16. Longest Common Subsequence
    // LeetCode 1143 · Medium
    // Trick: match → diag + 1; else max(up, left)  ·  O(m × n)
    // ====================================================================

    /**
     * QUESTION
     * Find the length of the longest sequence of characters that appears in
     * order in both strings. Gaps are allowed.
     *
     * EXAMPLE
     *   "abcde", "ace"   ->  3     ("ace")
     *   "karat", "kart"  ->  4     ("kart")
     *
     * STEP 1 · BRUTE FORCE
     * Recursion from (i, j): if the letters match, use both and move on;
     * otherwise, try skipping a letter from either string and take the better
     * result.
     *
     * Brute force cost: exponential
     */
    // Step 1 · brute force
    public static class LCSRecursive {
        public int longestCommonSubsequence(String a, String b) {
            return lcs(a, b, 0, 0);
        }

        private int lcs(String a, String b, int i, int j) {
            if (i == a.length() || j == b.length()) return 0;
            if (a.charAt(i) == b.charAt(j)) {
                return 1 + lcs(a, b, i + 1, j + 1);    // use both letters
            }
            return Math.max(lcs(a, b, i + 1, j),       // skip a's letter
                            lcs(a, b, i, j + 1));      // or skip b's letter
        }
    }

    /**
     * STEP 2 · THE BETTER IDEA
     * Let dp[i][j] = the LCS of the first i letters of a and the first j
     * letters of b. If a[i−1] == b[j−1], it's dp[i−1][j−1] + 1. Otherwise
     * it's max(dp[i−1][j], dp[i][j−1]). Row 0 and column 0 are zeros.
     */
    // Step 2 · the better way
    public static class LCS {
        public int longestCommonSubsequence(String a, String b) {
            // dp[i][j] = LCS of the first i letters of a and first j of b
            int[][] dp = new int[a.length() + 1][b.length() + 1];
            for (int i = 1; i <= a.length(); i++) {
                for (int j = 1; j <= b.length(); j++) {
                    if (a.charAt(i - 1) == b.charAt(j - 1)) {
                        dp[i][j] = dp[i - 1][j - 1] + 1;           // match
                    } else {
                        dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
                    }
                }
            }
            return dp[a.length()][b.length()];
        }
    }

    /*
     * Time: O(m × n) · Space: O(m × n)
     *
     * COMPARE WITH THE KARAT HISTORY QUESTION
     * Longest Common Browsing History (problem 15) is the substring version:
     * no gaps allowed, so a mismatch resets to 0. Here gaps are allowed, so a
     * mismatch keeps the best from above or the left. Same table, one line
     * different.
     */

}
