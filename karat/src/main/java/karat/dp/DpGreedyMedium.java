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

    // ====================================================================
    // 2. House Robber
    // LeetCode 198 · Medium
    // Trick: best[i] = max(best[i−1], best[i−2] + nums[i])  ·  O(n)
    // ====================================================================

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

    // ====================================================================
    // 3. House Robber II
    // LeetCode 213 · Medium
    // Trick: circle → run House Robber twice: skip first OR skip last  ·  O(n)
    // ====================================================================

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

    // ====================================================================
    // 4. Maximum Subarray (Kadane)
    // LeetCode 53 · Medium
    // Trick: cur = max(x, cur + x); best = max(best, cur)  ·  O(n)
    // ====================================================================

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

    // ====================================================================
    // 5. Jump Game  ★ Karat
    // LeetCode 55 · greedy · Medium
    // Trick: farthest; if i > farthest → false  ·  O(n)
    // ====================================================================

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

    // ====================================================================
    // 6. Jump Game II
    // LeetCode 45 · greedy levels · Medium
    // Trick: BFS-like levels: when i hits levelEnd, jump++ and levelEnd = farthest  ·  O(n)
    // ====================================================================

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

    // ====================================================================
    // 7. Partition Labels
    // LeetCode 763 · greedy · Medium
    // Trick: last[c] per letter; extend end = max(end, last[c]); cut when i == end  ·  O(n)
    // ====================================================================

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

    // ====================================================================
    // 8. Unique Paths
    // LeetCode 62 · grid DP · Medium
    // Trick: ways[r][c] = ways[r−1][c] + ways[r][c−1]; edges = 1  ·  O(m × n)
    // ====================================================================

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

    // ====================================================================
    // 9. Minimum Path Sum
    // LeetCode 64 · grid DP · Medium
    // Trick: best[r][c] = grid[r][c] + min(above, left)  ·  O(R × C)
    // ====================================================================

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

    // ====================================================================
    // 10. Maximal Square  ★ Karat
    // LeetCode 221 · Medium
    // Trick: 1 + min(up, left, diag); return side²  ·  O(R·C)
    // ====================================================================

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

    // ====================================================================
    // 11. Coin Change
    // LeetCode 322 · Medium
    // Trick: fewest[a] = min over coins of fewest[a − coin] + 1  ·  O(amount × coins)
    // ====================================================================

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

    // ====================================================================
    // 12. Decode Ways
    // LeetCode 91 · Medium
    // Trick: ways[i] = ways[i+1] (1 digit) + ways[i+2] (if 10..26); '0' → 0  ·  O(n)
    // ====================================================================

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

    // ====================================================================
    // 13. Word Break
    // LeetCode 139 · Medium
    // Trick: ok[end] = ok[start] && dict has s[start..end)  ·  O(n² × L)
    // ====================================================================

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

    // ====================================================================
    // 14. Longest Increasing Subsequence
    // LeetCode 300 · Medium
    // Trick: O(n²): len[i] = 1 + max len[j] for nums[j] < nums[i]; O(n log n): tails  ·  O(n log n)
    // ====================================================================

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

    // ====================================================================
    // 15. Longest Common Browsing History  ★ Karat
    // Karat story version of LeetCode 718 (Maximum Length of Repeated Subarray) · Medium
    // Trick: match → diag + 1, else 0; track best & end  ·  O(m·n)
    // ====================================================================

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

    // ====================================================================
    // 16. Longest Common Subsequence
    // LeetCode 1143 · Medium
    // Trick: match → diag + 1; else max(up, left)  ·  O(m × n)
    // ====================================================================

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

}
