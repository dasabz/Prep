package karat.dp;

import java.util.*;
import karat.T;
import karat.dp.DpGreedyEasy.*;
import karat.dp.DpGreedyMedium.*;

/** Runs every example from Part 6's PDFs against the code. */
public class DpGreedyTests {
    public static void main(String[] args) {
        Examples1.run();
        Examples2.run();
        Examples3.run();
        Examples4.run();
        T.done("DpGreedy");
    }

    static class Examples1 {
        static char[][] cg(String... rows) {
            char[][] g = new char[rows.length][];
            for (int i = 0; i < rows.length; i++) g[i] = rows[i].toCharArray();
            return g;
        }

        static void run() {
            ClimbingStairs cs = new ClimbingStairs();
            T.check("stairs1", cs.climbStairs(1), 1);
            T.check("stairs2", cs.climbStairs(2), 2);
            T.check("stairs3", cs.climbStairs(3), 3);
            T.check("stairs5", cs.climbStairs(5), 8);
            T.check("stairs45", cs.climbStairs(45), 1836311903);

            BestTimeToBuySell bt = new BestTimeToBuySell();
            T.check("stock1", bt.maxProfit(new int[] {7, 1, 5, 3, 6, 4}), 5);
            T.check("stock2", bt.maxProfit(new int[] {7, 6, 4, 3, 1}), 0);
            T.check("stock3", bt.maxProfit(new int[] {2, 9, 1, 4}), 7);

            HouseRobber hr = new HouseRobber();
            T.check("rob1", hr.rob(new int[] {2, 7, 9, 3, 1}), 12);
            T.check("rob2", hr.rob(new int[] {1, 2, 3, 1}), 4);
            T.check("rob3", hr.rob(new int[] {5}), 5);
            T.check("rob4", hr.rob(new int[] {2, 1, 1, 2}), 4);

            JumpGame jg = new JumpGame();
            T.check("jump1", jg.canJump(new int[] {2, 3, 1, 1, 4}), true);
            T.check("jump2", jg.canJump(new int[] {3, 2, 1, 0, 4}), false);
            T.check("jump3", jg.canJump(new int[] {0}), true);
            T.check("jump4", jg.canJump(new int[] {1, 0, 1}), false);

            MaximalSquare ms = new MaximalSquare();
            T.check("square1", ms.maximalSquare(cg("10100", "10111", "11111", "10010")), 4);
            T.check("square2", ms.maximalSquare(cg("0110", "1111", "1111", "0111")), 9);
            T.check("square3", ms.maximalSquare(cg("0")), 0);
            T.check("square4", ms.maximalSquare(cg("01", "10")), 1);

            LongestCommonHistory lch = new LongestCommonHistory();
            String[] u1 = {"home", "login", "search", "cart", "pay"};
            String[] u2 = {"search", "home", "login", "search", "cart", "logout"};
            T.check("hist1", lch.longestCommon(u1, u2), Arrays.asList("home", "login", "search", "cart"));
            T.check("hist2", lch.longestCommon(new String[] {"a", "b"}, new String[] {"c", "d"}), Arrays.asList());
            T.check("718a", lch.findLength(new int[] {1, 2, 3, 2, 1}, new int[] {3, 2, 1, 4, 7}), 3);
            T.check("718b", lch.findLength(new int[] {0, 0, 0}, new int[] {0, 0}), 2);
        }
    }

    static class Examples2 {
        static char[][] cg(String... rows) {
            char[][] g = new char[rows.length][];
            for (int i = 0; i < rows.length; i++) g[i] = rows[i].toCharArray();
            return g;
        }

        static void run() {
            ClimbingStairsRecursive cs = new ClimbingStairsRecursive();
            T.check("stairsR3", cs.climbStairs(3), 3);
            T.check("stairsR5", cs.climbStairs(5), 8);
            T.check("stairsR1", cs.climbStairs(1), 1);

            T.check("stockB1", new StockBrute().maxProfit(new int[] {7, 1, 5, 3, 6, 4}), 5);
            T.check("stockB2", new StockBrute().maxProfit(new int[] {7, 6, 4, 3, 1}), 0);

            T.check("robR1", new HouseRobberRecursive().rob(new int[] {2, 7, 9, 3, 1}), 12);
            T.check("robR2", new HouseRobberRecursive().rob(new int[] {1, 2, 3, 1}), 4);
            T.check("robR3", new HouseRobberRecursive().rob(new int[] {5}), 5);

            T.check("jumpD1", new JumpGameDP().canJump(new int[] {2, 3, 1, 1, 4}), true);
            T.check("jumpD2", new JumpGameDP().canJump(new int[] {3, 2, 1, 0, 4}), false);
            T.check("jumpD3", new JumpGameDP().canJump(new int[] {0}), true);

            T.check("sqB1", new MaximalSquareBrute().maximalSquare(cg("10100", "10111", "11111", "10010")), 4);
            T.check("sqB2", new MaximalSquareBrute().maximalSquare(cg("0110", "1111", "1111", "0111")), 9);
            T.check("sqB3", new MaximalSquareBrute().maximalSquare(cg("0")), 0);

            String[] u1 = {"home", "login", "search", "cart", "pay"};
            String[] u2 = {"search", "home", "login", "search", "cart", "logout"};
            T.check("histB1", new CommonHistoryBrute().longestCommon(u1, u2), Arrays.asList("home", "login", "search", "cart"));
            T.check("histB2", new CommonHistoryBrute().longestCommon(new String[] {"a"}, new String[] {"b"}), Arrays.asList());
        }
    }

    static class Examples3 {
        static void run() {
            int[] c1 = {10, 15, 20}, c2 = {1, 100, 1, 1, 1, 100, 1, 1, 100, 1};
            T.check("mcsR1", new MinCostStairsRecursive().minCostClimbingStairs(c1), 15);
            T.check("mcsR2", new MinCostStairsRecursive().minCostClimbingStairs(c2), 6);
            T.check("mcs1", new MinCostStairs().minCostClimbingStairs(c1), 15);
            T.check("mcs2", new MinCostStairs().minCostClimbingStairs(c2), 6);

            T.check("cookS1", new AssignCookiesScan().findContentChildren(new int[] {1, 2, 3}, new int[] {1, 1}), 1);
            T.check("cookS2", new AssignCookiesScan().findContentChildren(new int[] {1, 2}, new int[] {1, 2, 3}), 2);
            T.check("cook1", new AssignCookies().findContentChildren(new int[] {1, 2, 3}, new int[] {1, 1}), 1);
            T.check("cook2", new AssignCookies().findContentChildren(new int[] {1, 2}, new int[] {1, 2, 3}), 2);
            T.check("cook3", new AssignCookies().findContentChildren(new int[] {5, 2, 8}, new int[] {6, 1, 9, 3}), 3);

            T.check("st2a", new StockII().maxProfit(new int[] {7, 1, 5, 3, 6, 4}), 7);
            T.check("st2b", new StockII().maxProfit(new int[] {1, 2, 3, 4, 5}), 4);
            T.check("st2c", new StockII().maxProfit(new int[] {7, 6, 4, 3, 1}), 0);

            T.check("rob2a", new HouseRobberII().rob(new int[] {2, 3, 2}), 3);
            T.check("rob2b", new HouseRobberII().rob(new int[] {1, 2, 3, 1}), 4);
            T.check("rob2c", new HouseRobberII().rob(new int[] {1, 2, 3}), 3);
            T.check("rob2d", new HouseRobberII().rob(new int[] {7}), 7);

            int[] ms = {-2, 1, -3, 4, -1, 2, 1, -5, 4};
            T.check("msB", new MaxSubarrayBrute().maxSubArray(ms), 6);
            T.check("ms1", new MaxSubarray().maxSubArray(ms), 6);
            T.check("ms2", new MaxSubarray().maxSubArray(new int[] {-3, -1, -2}), -1);
            T.check("ms3", new MaxSubarray().maxSubArray(new int[] {5, 4, -1, 7, 8}), 23);

            T.check("j2D1", new JumpGameIIDP().jump(new int[] {2, 3, 1, 1, 4}), 2);
            T.check("j2D2", new JumpGameIIDP().jump(new int[] {2, 3, 0, 1, 4}), 2);
            T.check("j2a", new JumpGameII().jump(new int[] {2, 3, 1, 1, 4}), 2);
            T.check("j2b", new JumpGameII().jump(new int[] {2, 3, 0, 1, 4}), 2);
            T.check("j2c", new JumpGameII().jump(new int[] {0}), 0);
            T.check("j2d", new JumpGameII().jump(new int[] {1, 1, 1, 1}), 3);

            String pl = "ababcbacadefegdehijhklij";
            T.check("plS", new PartitionLabelsScan().partitionLabels(pl), "[9, 7, 8]");
            T.check("pl1", new PartitionLabels().partitionLabels(pl), "[9, 7, 8]");
            T.check("pl2", new PartitionLabels().partitionLabels("eccbbbbdec"), "[10]");
            T.check("pl3", new PartitionLabels().partitionLabels("karat"), "[1, 3, 1]");
        }
    }

    static class Examples4 {
        static void run() {
            T.check("upR", new UniquePathsRecursive().uniquePaths(3, 7), 28);
            T.check("up1", new UniquePaths().uniquePaths(3, 7), 28);
            T.check("up2", new UniquePaths().uniquePaths(3, 2), 3);
            T.check("up3", new UniquePaths().uniquePaths(1, 1), 1);

            int[][] g = {{1, 3, 1}, {1, 5, 1}, {4, 2, 1}};
            T.check("mpsR", new MinPathSumRecursive().minPathSum(g), 7);
            T.check("mps1", new MinPathSum().minPathSum(g), 7);
            T.check("mps2", new MinPathSum().minPathSum(new int[][] {{1, 2, 3}, {4, 5, 6}}), 12);

            T.check("ccR1", new CoinChangeRecursive().coinChange(new int[] {1, 2, 5}, 11), 3);
            T.check("ccR2", new CoinChangeRecursive().coinChange(new int[] {2}, 3), -1);
            T.check("cc1", new CoinChange().coinChange(new int[] {1, 2, 5}, 11), 3);
            T.check("cc2", new CoinChange().coinChange(new int[] {2}, 3), -1);
            T.check("cc3", new CoinChange().coinChange(new int[] {1}, 0), 0);
            T.check("cc4", new CoinChange().coinChange(new int[] {1, 3, 4}, 6), 2);

            T.check("dwR1", new DecodeWaysRecursive().numDecodings("226"), 3);
            T.check("dwR2", new DecodeWaysRecursive().numDecodings("06"), 0);
            T.check("dw1", new DecodeWays().numDecodings("12"), 2);
            T.check("dw2", new DecodeWays().numDecodings("226"), 3);
            T.check("dw3", new DecodeWays().numDecodings("06"), 0);
            T.check("dw4", new DecodeWays().numDecodings("2101"), 1);
            T.check("dw5", new DecodeWays().numDecodings("27"), 1);

            List<String> d1 = Arrays.asList("karat", "code");
            List<String> d2 = Arrays.asList("cats", "dog", "sand", "and", "cat");
            T.check("wbR1", new WordBreakRecursive().wordBreak("karatcode", d1), true);
            T.check("wbR2", new WordBreakRecursive().wordBreak("catsandog", d2), false);
            T.check("wb1", new WordBreak().wordBreak("karatcode", d1), true);
            T.check("wb2", new WordBreak().wordBreak("catsandog", d2), false);
            T.check("wb3", new WordBreak().wordBreak("applepenapple", Arrays.asList("apple", "pen")), true);

            int[] lis = {10, 9, 2, 5, 3, 7, 101, 18};
            T.check("lisQ", new LISQuadratic().lengthOfLIS(lis), 4);
            T.check("lisF", new LISFast().lengthOfLIS(lis), 4);
            T.check("lisF2", new LISFast().lengthOfLIS(new int[] {0, 1, 0, 3, 2, 3}), 4);
            T.check("lisF3", new LISFast().lengthOfLIS(new int[] {7, 7, 7, 7}), 1);
            T.check("lisQ3", new LISQuadratic().lengthOfLIS(new int[] {7, 7, 7, 7}), 1);

            T.check("lcsR", new LCSRecursive().longestCommonSubsequence("abcde", "ace"), 3);
            T.check("lcs1", new LCS().longestCommonSubsequence("abcde", "ace"), 3);
            T.check("lcs2", new LCS().longestCommonSubsequence("abc", "def"), 0);
            T.check("lcs3", new LCS().longestCommonSubsequence("karat", "kart"), 4);
        }
    }

}
