package karat.hashmap;

import java.util.*;
import karat.T;
import karat.hashmap.HashMapEasy.*;
import karat.hashmap.HashMapMedium.*;

/** Runs every example from Part 1's PDFs against the code. */
public class HashMapTests {
    public static void main(String[] args) {
        Examples1.run();
        Examples2.run();
        Examples3.run();
        Examples4.run();
        T.done("HashMap");
    }

    static class Examples1 {
        static char[][] emptyBoard() {
            char[][] b = new char[9][9];
            for (char[] row : b) Arrays.fill(row, '.');
            return b;
        }

        static void run() {
            TwoSum ts = new TwoSum();
            T.check("twoSum1", ts.twoSum(new int[] {3, 8, 5, 1}, 9), new int[] {1, 3});
            T.check("twoSum2", ts.twoSum(new int[] {2, 7, 11, 15}, 9), new int[] {0, 1});
            T.check("twoSum3", ts.twoSum(new int[] {3, 3}, 6), new int[] {0, 1});
            T.check("twoSum4", ts.twoSum(new int[] {1, 2}, 10), new int[] {});

            RansomNote rn = new RansomNote();
            T.check("ransom1", rn.canConstruct("java", "avajx"), true);
            T.check("ransom2", rn.canConstruct("java", "javx"), false);
            T.check("ransom3", rn.canConstruct("aa", "aab"), true);

            FormWords fw = new FormWords();
            T.check("form1", fw.countCharacters(new String[] {"cat", "bt", "hat", "tree"}, "atach"), 6);
            T.check("form2", fw.countCharacters(new String[] {"code", "cod", "deco", "coder"}, "edoc"), 11);

            GroupAnagrams ga = new GroupAnagrams();
            T.check("anagram1", T.norm(ga.groupAnagrams(new String[] {"tea", "eat", "tan", "ate", "nat", "bat"})),
                    T.norm(Arrays.asList(Arrays.asList("tea", "eat", "ate"), Arrays.asList("tan", "nat"), Arrays.asList("bat"))));

            TopKFrequent tk = new TopKFrequent();
            T.check("topk1", tk.topKFrequent(new int[] {4, 4, 4, 7, 7, 9}, 2), new int[] {4, 7});
            T.check("topk2", tk.topKFrequent(new int[] {1}, 1), new int[] {1});

            SortByFrequency sf = new SortByFrequency();
            T.check("freq1", sf.frequencySort("cabcac"), "cccaab");
            String tree = sf.frequencySort("tree");
            T.check("freq2", tree.startsWith("ee") && tree.length() == 4, true);

            ZeroOrOneLosses zl = new ZeroOrOneLosses();
            T.check("winners1", zl.findWinners(new int[][] {{1, 3}, {2, 3}, {3, 6}, {5, 6}, {5, 7}, {4, 5}, {4, 8}, {4, 9}, {10, 4}, {10, 9}}),
                    Arrays.asList(Arrays.asList(1, 2, 10), Arrays.asList(4, 5, 7, 8)));
            T.check("winners2", zl.findWinners(new int[][] {{1, 2}, {3, 2}, {2, 4}}),
                    Arrays.asList(Arrays.asList(1, 3), Arrays.asList(4)));

            SubdomainVisits sv = new SubdomainVisits();
            List<String> got = sv.subdomainVisits(new String[] {"900 mail.google.com", "50 yahoo.com", "1 maps.google.com", "5 wiki.org"});
            Collections.sort(got);
            List<String> want = new ArrayList<>(Arrays.asList("901 google.com", "951 com", "900 mail.google.com", "50 yahoo.com",
                    "1 maps.google.com", "5 wiki.org", "5 org"));
            Collections.sort(want);
            T.check("subdomain1", got, want);

            LongestConsecutive lc = new LongestConsecutive();
            T.check("consec1", lc.longestConsecutive(new int[] {100, 4, 200, 1, 3, 2}), 4);
            T.check("consec2", lc.longestConsecutive(new int[] {9, 1, 4, 7, 3, 2, 8, 5, 6, 0}), 10);
            T.check("consec3", lc.longestConsecutive(new int[] {}), 0);

            ValidSudoku vs = new ValidSudoku();
            char[][] b1 = emptyBoard(); b1[0][0] = '5'; b1[4][4] = '5';
            T.check("sudoku valid", vs.isValidSudoku(b1), true);
            char[][] b2 = emptyBoard(); b2[0][0] = '5'; b2[0][8] = '5';
            T.check("sudoku row", vs.isValidSudoku(b2), false);
            char[][] b3 = emptyBoard(); b3[0][0] = '5'; b3[1][1] = '5';
            T.check("sudoku box", vs.isValidSudoku(b3), false);
            char[][] b4 = emptyBoard(); b4[0][3] = '7'; b4[8][3] = '7';
            T.check("sudoku col", vs.isValidSudoku(b4), false);
        }
    }

    static class Examples2 {
        static char[][] emptyBoard() {
            char[][] b = new char[9][9];
            for (char[] row : b) Arrays.fill(row, '.');
            return b;
        }

        static void run() {
            TwoSumBrute ts = new TwoSumBrute();
            T.check("twoSumB1", ts.twoSum(new int[] {3, 8, 5, 1}, 9), new int[] {1, 3});
            T.check("twoSumB2", ts.twoSum(new int[] {3, 3}, 6), new int[] {0, 1});
            T.check("twoSumB3", ts.twoSum(new int[] {1, 2}, 10), new int[] {});

            RansomNoteBrute rn = new RansomNoteBrute();
            T.check("ransomB1", rn.canConstruct("java", "avajx"), true);
            T.check("ransomB2", rn.canConstruct("java", "javx"), false);

            FormWordsBrute fw = new FormWordsBrute();
            T.check("formB1", fw.countCharacters(new String[] {"cat", "bt", "hat", "tree"}, "atach"), 6);
            T.check("formB2", fw.countCharacters(new String[] {"code", "cod", "deco", "coder"}, "edoc"), 11);

            GroupAnagramsBrute ga = new GroupAnagramsBrute();
            T.check("anagramB", T.norm(ga.groupAnagrams(new String[] {"tea", "eat", "tan", "ate", "nat", "bat"})),
                    T.norm(Arrays.asList(Arrays.asList("tea", "eat", "ate"), Arrays.asList("tan", "nat"), Arrays.asList("bat"))));

            TopKFrequentBrute tk = new TopKFrequentBrute();
            T.check("topkB", tk.topKFrequent(new int[] {4, 4, 4, 7, 7, 9}, 2), new int[] {4, 7});

            SortByFrequencyBrute sf = new SortByFrequencyBrute();
            T.check("freqB", sf.frequencySort("cabcac"), "cccaab");

            ZeroOrOneLossesBrute zl = new ZeroOrOneLossesBrute();
            T.check("winnersB", zl.findWinners(new int[][] {{1, 2}, {3, 2}, {2, 4}}),
                    Arrays.asList(Arrays.asList(1, 3), Arrays.asList(4)));

            SubdomainVisitsBrute sv = new SubdomainVisitsBrute();
            List<String> got = sv.subdomainVisits(new String[] {"900 mail.google.com", "50 yahoo.com", "1 maps.google.com", "5 wiki.org"});
            Collections.sort(got);
            List<String> want = new ArrayList<>(Arrays.asList("901 google.com", "951 com", "900 mail.google.com", "50 yahoo.com",
                    "1 maps.google.com", "5 wiki.org", "5 org"));
            Collections.sort(want);
            T.check("subdomainB", got, want);

            LongestConsecutiveSort lc = new LongestConsecutiveSort();
            T.check("consecS1", lc.longestConsecutive(new int[] {100, 4, 200, 1, 3, 2}), 4);
            T.check("consecS2", lc.longestConsecutive(new int[] {1, 2, 2, 3}), 3);
            T.check("consecS3", lc.longestConsecutive(new int[] {}), 0);

            ValidSudokuPasses vs = new ValidSudokuPasses();
            char[][] b1 = emptyBoard(); b1[0][0] = '5'; b1[4][4] = '5';
            T.check("sudokuP valid", vs.isValidSudoku(b1), true);
            char[][] b2 = emptyBoard(); b2[0][0] = '5'; b2[0][8] = '5';
            T.check("sudokuP row", vs.isValidSudoku(b2), false);
            char[][] b3 = emptyBoard(); b3[0][0] = '5'; b3[1][1] = '5';
            T.check("sudokuP box", vs.isValidSudoku(b3), false);
            char[][] b4 = emptyBoard(); b4[0][3] = '7'; b4[8][3] = '7';
            T.check("sudokuP col", vs.isValidSudoku(b4), false);
        }
    }

    static class Examples3 {
        static String sorted(String[] a) {
            String[] c = a.clone();
            Arrays.sort(c);
            return Arrays.toString(c);
        }

        static String sorted(int[] a) {
            int[] c = a.clone();
            Arrays.sort(c);
            return Arrays.toString(c);
        }

        static void run() {
            int[] d1 = {1, 2, 3, 1}, d2 = {1, 2, 3, 4};
            T.check("dupB1", new ContainsDuplicateBrute().containsDuplicate(d1), true);
            T.check("dupB2", new ContainsDuplicateBrute().containsDuplicate(d2), false);
            T.check("dup1", new ContainsDuplicate().containsDuplicate(d1), true);
            T.check("dup2", new ContainsDuplicate().containsDuplicate(d2), false);

            T.check("anaS1", new ValidAnagramSort().isAnagram("listen", "silent"), true);
            T.check("anaS2", new ValidAnagramSort().isAnagram("rat", "car"), false);
            T.check("ana1", new ValidAnagram().isAnagram("listen", "silent"), true);
            T.check("ana2", new ValidAnagram().isAnagram("rat", "car"), false);
            T.check("ana3", new ValidAnagram().isAnagram("ab", "a"), false);

            int[] m1 = {2, 2, 1, 1, 1, 2, 2}, m2 = {3, 2, 3};
            T.check("majB", new MajorityElementBrute().majorityElement(m1), 2);
            T.check("maj1", new MajorityElement().majorityElement(m1), 2);
            T.check("maj2", new MajorityElement().majorityElement(m2), 3);
            T.check("vote1", new MajorityVote().majorityElement(m1), 2);
            T.check("vote2", new MajorityVote().majorityElement(m2), 3);

            T.check("uniqB", new FirstUniqueCharBrute().firstUniqChar("loveleetcode"), 2);
            T.check("uniq1", new FirstUniqueChar().firstUniqChar("loveleetcode"), 2);
            T.check("uniq2", new FirstUniqueChar().firstUniqChar("aabb"), -1);
            T.check("uniq3", new FirstUniqueChar().firstUniqChar("karat"), 0);

            T.check("isoB1", new IsomorphicBrute().isIsomorphic("egg", "add"), true);
            T.check("isoB2", new IsomorphicBrute().isIsomorphic("badc", "baba"), false);
            T.check("iso1", new Isomorphic().isIsomorphic("egg", "add"), true);
            T.check("iso2", new Isomorphic().isIsomorphic("foo", "bar"), false);
            T.check("iso3", new Isomorphic().isIsomorphic("badc", "baba"), false);
            T.check("iso4", new Isomorphic().isIsomorphic("paper", "title"), true);

            T.check("wpB1", new WordPatternBrute().wordPattern("abba", "dog cat cat dog"), true);
            T.check("wpB2", new WordPatternBrute().wordPattern("abba", "dog dog dog dog"), false);
            T.check("wp1", new WordPattern().wordPattern("abba", "dog cat cat dog"), true);
            T.check("wp2", new WordPattern().wordPattern("abba", "dog cat cat fish"), false);
            T.check("wp3", new WordPattern().wordPattern("abba", "dog dog dog dog"), false);
            T.check("wp4", new WordPattern().wordPattern("aaa", "dog dog"), false);

            T.check("intB", sorted(new IntersectBrute().intersect(new int[] {1, 2, 2, 1}, new int[] {2, 2})), "[2, 2]");
            T.check("int1", sorted(new Intersect().intersect(new int[] {1, 2, 2, 1}, new int[] {2, 2})), "[2, 2]");
            T.check("int2", sorted(new Intersect().intersect(new int[] {4, 9, 5}, new int[] {9, 4, 9, 8, 4})), "[4, 9]");

            T.check("nearB1", new NearbyDuplicateBrute().containsNearbyDuplicate(new int[] {1, 2, 3, 1}, 3), true);
            T.check("nearB2", new NearbyDuplicateBrute().containsNearbyDuplicate(new int[] {1, 2, 3, 1, 2, 3}, 2), false);
            T.check("near1", new NearbyDuplicate().containsNearbyDuplicate(new int[] {1, 2, 3, 1}, 3), true);
            T.check("near2", new NearbyDuplicate().containsNearbyDuplicate(new int[] {1, 0, 1, 1}, 1), true);
            T.check("near3", new NearbyDuplicate().containsNearbyDuplicate(new int[] {1, 2, 3, 1, 2, 3}, 2), false);

            T.check("happyB1", new HappyNumberBrute().isHappy(19), true);
            T.check("happyB2", new HappyNumberBrute().isHappy(2), false);
            T.check("happy1", new HappyNumber().isHappy(19), true);
            T.check("happy2", new HappyNumber().isHappy(2), false);
            T.check("happy3", new HappyNumber().isHappy(1), true);

            T.check("uncB", sorted(new UncommonWordsBrute().uncommonFromSentences("this apple is sweet", "this apple is sour")), "[sour, sweet]");
            T.check("unc1", sorted(new UncommonWords().uncommonFromSentences("this apple is sweet", "this apple is sour")), "[sour, sweet]");
            T.check("unc2", sorted(new UncommonWords().uncommonFromSentences("apple apple", "banana")), "[banana]");
        }
    }

    static class Examples4 {
        static String sorted(String[] a) {
            String[] c = a.clone();
            Arrays.sort(c);
            return Arrays.toString(c);
        }

        // result must be a rearrangement of s, with order's letters in order
        static boolean customOk(String order, String s, String r) {
            char[] a = s.toCharArray(), b = r.toCharArray();
            Arrays.sort(a);
            Arrays.sort(b);
            if (!Arrays.equals(a, b)) return false;
            int last = -1;
            for (char c : r.toCharArray()) {
                int p = order.indexOf(c);
                if (p == -1) continue;
                if (p < last) return false;
                last = p;
            }
            return true;
        }

        static void run() {
            String para = "Bob hit a ball, the hit BALL flew far after it was hit.";
            T.check("mcwB", new MostCommonWordBrute().mostCommonWord(para, new String[] {"hit"}), "ball");
            T.check("mcw1", new MostCommonWord().mostCommonWord(para, new String[] {"hit"}), "ball");
            T.check("mcw2", new MostCommonWord().mostCommonWord("a.", new String[] {}), "a");

            T.check("palB", new LongestPalindromeBrute().longestPalindrome("abccccdd"), 7);
            T.check("pal1", new LongestPalindromeBuild().longestPalindrome("abccccdd"), 7);
            T.check("pal2", new LongestPalindromeBuild().longestPalindrome("a"), 1);
            T.check("pal3", new LongestPalindromeBuild().longestPalindrome("Aa"), 1);

            String[] l1 = {"Dosa Hut", "Pizza Point", "Chai Spot"};
            String[] l2 = {"Chai Spot", "Momo Bar", "Dosa Hut"};
            T.check("misB", sorted(new MinIndexSumBrute().findRestaurant(l1, l2)), "[Chai Spot, Dosa Hut]");
            T.check("mis1", sorted(new MinIndexSum().findRestaurant(l1, l2)), "[Chai Spot, Dosa Hut]");
            T.check("mis2", sorted(new MinIndexSum().findRestaurant(new String[] {"a", "b", "c"}, new String[] {"c", "b", "a"})),
                    "[a, b, c]");
            T.check("mis3", sorted(new MinIndexSum().findRestaurant(new String[] {"x", "y"}, new String[] {"y", "x", "z"})),
                    "[x, y]");
            T.check("mis4", sorted(new MinIndexSum().findRestaurant(new String[] {"x", "y", "z"}, new String[] {"z", "w", "y"})),
                    "[z]");

            T.check("fsB", new FrequencySortBrute().frequencySort(new int[] {1, 1, 2, 2, 2, 3}), new int[] {3, 1, 1, 2, 2, 2});
            T.check("fs1", new FrequencySortIncreasing().frequencySort(new int[] {1, 1, 2, 2, 2, 3}), new int[] {3, 1, 1, 2, 2, 2});
            T.check("fs2", new FrequencySortIncreasing().frequencySort(new int[] {2, 3, 1, 3, 2}), new int[] {1, 3, 3, 2, 2});

            String cs = new CustomSortBrute().customSortString("cba", "abcd");
            T.check("csB", customOk("cba", "abcd", cs), true);
            T.check("cs1", new CustomSort().customSortString("cba", "abcd"), "cbad");
            T.check("cs2", customOk("bcafg", "abcd", new CustomSort().customSortString("bcafg", "abcd")), true);

            String[] w = {"i", "love", "leetcode", "i", "love", "coding"};
            T.check("tkS", new TopKWordsSort().topKFrequent(w, 2), Arrays.asList("i", "love"));
            T.check("tkH", new TopKWordsHeap().topKFrequent(w, 2), Arrays.asList("i", "love"));
            String[] w2 = {"the", "day", "is", "sunny", "the", "the", "the", "sunny", "is", "is"};
            T.check("tkH2", new TopKWordsHeap().topKFrequent(w2, 4), Arrays.asList("the", "is", "sunny", "day"));
            T.check("tkS2", new TopKWordsSort().topKFrequent(w2, 4), Arrays.asList("the", "is", "sunny", "day"));

            T.check("bcB1", new BullsCowsBrute().getHint("1807", "7810"), "1A3B");
            T.check("bcB2", new BullsCowsBrute().getHint("1123", "0111"), "1A1B");
            T.check("bc1", new BullsCows().getHint("1807", "7810"), "1A3B");
            T.check("bc2", new BullsCows().getHint("1123", "0111"), "1A1B");

            String dna = "AAAAACCCCCAAAAACCCCCCAAAAAGGGTTT";
            T.check("dnaB", new RepeatedDnaBrute().findRepeatedDnaSequences(dna), Arrays.asList("AAAAACCCCC", "CCCCCAAAAA"));
            T.check("dna1", new RepeatedDna().findRepeatedDnaSequences(dna), Arrays.asList("AAAAACCCCC", "CCCCCAAAAA"));
            T.check("dna2", new RepeatedDna().findRepeatedDnaSequences("AAAAAAAAAAAAA"), Arrays.asList("AAAAAAAAAA"));

            T.check("ssB", new SubarraySumBrute().subarraySum(new int[] {1, 2, 3, -2, 2}, 3), 4);
            T.check("ss1", new SubarraySum().subarraySum(new int[] {1, 2, 3, -2, 2}, 3), 4);
            T.check("ss2", new SubarraySum().subarraySum(new int[] {1, 1, 1}, 2), 2);
            T.check("ss3", new SubarraySum().subarraySum(new int[] {1, -1, 0}, 0), 3);

            List<List<Integer>> wall = Arrays.asList(Arrays.asList(1, 2, 2, 1), Arrays.asList(3, 1, 2), Arrays.asList(1, 3, 2),
                    Arrays.asList(2, 4), Arrays.asList(3, 1, 2), Arrays.asList(1, 3, 1, 1));
            T.check("wallB", new BrickWallBrute().leastBricks(wall), 2);
            T.check("wall1", new BrickWall().leastBricks(wall), 2);
            List<List<Integer>> thin = Arrays.asList(Arrays.asList(1), Arrays.asList(1), Arrays.asList(1));
            T.check("wallB2", new BrickWallBrute().leastBricks(thin), 3);
            T.check("wall2", new BrickWall().leastBricks(thin), 3);
        }
    }

}
