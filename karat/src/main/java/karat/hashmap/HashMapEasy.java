package karat.hashmap;

import java.util.*;

/**
 * Part 1 · Hash Map, Counting & Sorting · Easy (17 problems).
 *
 * Explanations, dry runs and pitfalls: pdfs/Karat-1a-Easy-HashMap-Counting-Sorting.pdf
 * Each problem: Step 1 = brute force, Step 2 = the better way.
 * Every example is checked in src/test/java/karat/hashmap/HashMapTests.java
 */
public final class HashMapEasy {

    private HashMapEasy() { }

    // ====================================================================
    // 1. Two Sum
    // LeetCode 1 · Easy
    // Trick: map value→index; look up target − x before storing x  ·  O(n)
    // ====================================================================

    // Step 1 · brute force
    public static class TwoSumBrute {
        public int[] twoSum(int[] nums, int target) {
            for (int i = 0; i < nums.length; i++) {
                for (int j = i + 1; j < nums.length; j++) { // each pair
                    if (nums[i] + nums[j] == target) {
                        return new int[] { i, j };
                    }
                }
            }
            return new int[0];
        }
    }

    // Step 2 · the better way
    public static class TwoSum {
        public int[] twoSum(int[] nums, int target) {
            // value -> index where we saw it
            Map<Integer, Integer> seen = new HashMap<>();
            for (int i = 0; i < nums.length; i++) {
                int need = target - nums[i];
                if (seen.containsKey(need)) {
                    return new int[] { seen.get(need), i };
                }
                seen.put(nums[i], i);   // remember it for later numbers
            }
            return new int[0];          // no pair found
        }
    }

    // ====================================================================
    // 2. Contains Duplicate
    // LeetCode 217 · Easy
    // Trick: HashSet; add() returns false on a repeat  ·  O(n)
    // ====================================================================

    // Step 1 · brute force
    public static class ContainsDuplicateBrute {
        public boolean containsDuplicate(int[] nums) {
            for (int i = 0; i < nums.length; i++) {
                for (int j = i + 1; j < nums.length; j++) {
                    if (nums[i] == nums[j]) return true;
                }
            }
            return false;
        }
    }

    // Step 2 · the better way
    public static class ContainsDuplicate {
        public boolean containsDuplicate(int[] nums) {
            Set<Integer> seen = new HashSet<>();
            for (int n : nums) {
                if (!seen.add(n)) return true;   // add() false = seen before
            }
            return false;
        }
    }

    // ====================================================================
    // 3. Valid Anagram
    // LeetCode 242 · Easy
    // Trick: int[26]: s adds, t subtracts, all must end at 0  ·  O(n)
    // ====================================================================

    // Step 1 · brute force
    public static class ValidAnagramSort {
        public boolean isAnagram(String s, String t) {
            char[] a = s.toCharArray();
            char[] b = t.toCharArray();
            Arrays.sort(a);
            Arrays.sort(b);
            return Arrays.equals(a, b);          // same letters, same order?
        }
    }

    // Step 2 · the better way
    public static class ValidAnagram {
        public boolean isAnagram(String s, String t) {
            if (s.length() != t.length()) return false;
            int[] count = new int[26];
            for (int i = 0; i < s.length(); i++) {
                count[s.charAt(i) - 'a']++;      // s adds a letter
                count[t.charAt(i) - 'a']--;      // t takes it away
            }
            for (int c : count) {
                if (c != 0) return false;        // something didn't cancel
            }
            return true;
        }
    }

    // ====================================================================
    // 4. Majority Element
    // LeetCode 169 · Easy
    // Trick: count map (or Boyer–Moore vote for O(1) space)  ·  O(n)
    // ====================================================================

    // Step 1 · brute force
    public static class MajorityElementBrute {
        public int majorityElement(int[] nums) {
            for (int x : nums) {
                int count = 0;
                for (int y : nums) {
                    if (y == x) count++;         // count x the slow way
                }
                if (count > nums.length / 2) return x;
            }
            return -1;
        }
    }

    // Step 2 · the better way
    public static class MajorityElement {
        public int majorityElement(int[] nums) {
            Map<Integer, Integer> count = new HashMap<>();
            for (int n : nums) {
                int c = count.getOrDefault(n, 0) + 1;
                count.put(n, c);
                if (c > nums.length / 2) return n;    // can stop early
            }
            return -1;
        }
    }

    // Step 2 · the better way
    public static class MajorityVote {
        public int majorityElement(int[] nums) {
            int candidate = 0, votes = 0;
            for (int n : nums) {
                if (votes == 0) candidate = n;        // pick a new candidate
                votes += (n == candidate) ? 1 : -1;   // vote for or against
            }
            return candidate;                         // the majority survives
        }
    }

    // ====================================================================
    // 5. Ransom Note  ★ Karat
    // LeetCode 383 · Easy
    // Trick: int[26]; spend letters; any count < 0 → false  ·  O(n)
    // ====================================================================

    // Step 1 · brute force
    public static class RansomNoteBrute {
        public boolean canConstruct(String note, String magazine) {
            StringBuilder mag = new StringBuilder(magazine);
            for (char c : note.toCharArray()) {
                int pos = mag.indexOf(String.valueOf(c));  // search magazine
                if (pos == -1) return false;               // letter missing
                mag.deleteCharAt(pos);                     // use it up
            }
            return true;
        }
    }

    // Step 2 · the better way
    public static class RansomNote {
        public boolean canConstruct(String note, String magazine) {
            int[] count = new int[26];          // letters we have
            for (char c : magazine.toCharArray()) {
                count[c - 'a']++;
            }
            for (char c : note.toCharArray()) {
                count[c - 'a']--;               // use one letter
                if (count[c - 'a'] < 0) {
                    return false;               // ran out of this letter
                }
            }
            return true;
        }
    }

    // ====================================================================
    // 6. First Unique Character in a String
    // LeetCode 387 · Easy
    // Trick: two passes: count, then first index with count 1  ·  O(n)
    // ====================================================================

    // Step 1 · brute force
    public static class FirstUniqueCharBrute {
        public int firstUniqChar(String s) {
            for (int i = 0; i < s.length(); i++) {
                boolean repeated = false;
                for (int j = 0; j < s.length(); j++) {
                    if (i != j && s.charAt(i) == s.charAt(j)) {
                        repeated = true;
                        break;
                    }
                }
                if (!repeated) return i;
            }
            return -1;
        }
    }

    // Step 2 · the better way
    public static class FirstUniqueChar {
        public int firstUniqChar(String s) {
            int[] count = new int[26];
            for (char c : s.toCharArray()) {
                count[c - 'a']++;                     // pass 1: count
            }
            for (int i = 0; i < s.length(); i++) {    // pass 2: find first
                if (count[s.charAt(i) - 'a'] == 1) return i;
            }
            return -1;
        }
    }

    // ====================================================================
    // 7. Isomorphic Strings
    // LeetCode 205 · Easy
    // Trick: two maps (s→t and t→s) must agree at every step  ·  O(n)
    // ====================================================================

    // Step 1 · brute force
    public static class IsomorphicBrute {
        public boolean isIsomorphic(String s, String t) {
            for (int i = 0; i < s.length(); i++) {
                for (int j = i + 1; j < s.length(); j++) {
                    boolean sameInS = s.charAt(i) == s.charAt(j);
                    boolean sameInT = t.charAt(i) == t.charAt(j);
                    if (sameInS != sameInT) return false;   // shapes differ
                }
            }
            return true;
        }
    }

    // Step 2 · the better way
    public static class Isomorphic {
        public boolean isIsomorphic(String s, String t) {
            Map<Character, Character> sToT = new HashMap<>();
            Map<Character, Character> tToS = new HashMap<>();
            for (int i = 0; i < s.length(); i++) {
                char a = s.charAt(i), b = t.charAt(i);
                Character mapped = sToT.get(a);
                if (mapped != null && mapped != b) return false;
                Character back = tToS.get(b);
                if (back != null && back != a) return false;
                sToT.put(a, b);                       // a always becomes b
                tToS.put(b, a);                       // and only a becomes b
            }
            return true;
        }
    }

    // ====================================================================
    // 8. Word Pattern
    // LeetCode 290 · Easy
    // Trick: same as Isomorphic, but letter ↔ word maps  ·  O(n)
    // ====================================================================

    // Step 1 · brute force
    public static class WordPatternBrute {
        public boolean wordPattern(String pattern, String s) {
            String[] words = s.split(" ");
            if (words.length != pattern.length()) return false;
            for (int i = 0; i < words.length; i++) {
                for (int j = i + 1; j < words.length; j++) {
                    boolean sameLetter = pattern.charAt(i)
                                         == pattern.charAt(j);
                    boolean sameWord = words[i].equals(words[j]);
                    if (sameLetter != sameWord) return false;
                }
            }
            return true;
        }
    }

    // Step 2 · the better way
    public static class WordPattern {
        public boolean wordPattern(String pattern, String s) {
            String[] words = s.split(" ");
            if (words.length != pattern.length()) return false;
            Map<Character, String> letterToWord = new HashMap<>();
            Map<String, Character> wordToLetter = new HashMap<>();
            for (int i = 0; i < words.length; i++) {
                char c = pattern.charAt(i);
                String w = words[i];
                String mapped = letterToWord.get(c);
                if (mapped != null && !mapped.equals(w)) return false;
                Character back = wordToLetter.get(w);
                if (back != null && back != c) return false;
                letterToWord.put(c, w);
                wordToLetter.put(w, c);
            }
            return true;
        }
    }

    // ====================================================================
    // 9. Find Words That Can Be Formed by Characters  ★ Karat
    // LeetCode 1160 · Easy
    // Trick: count have once; fresh need array per word  ·  O(total)
    // ====================================================================

    // Step 1 · brute force
    public static class FormWordsBrute {
        public int countCharacters(String[] words, String chars) {
            int total = 0;
            for (String word : words) {
                StringBuilder pool = new StringBuilder(chars);  // fresh copy
                boolean ok = true;
                for (char c : word.toCharArray()) {
                    int pos = pool.indexOf(String.valueOf(c));
                    if (pos == -1) {
                        ok = false;                         // can't build it
                        break;
                    }
                    pool.deleteCharAt(pos);
                }
                if (ok) total += word.length();
            }
            return total;
        }
    }

    // Step 2 · the better way
    public static class FormWords {
        // Sum of lengths of the words we can build from 'chars'
        public int countCharacters(String[] words, String chars) {
            int[] have = new int[26];
            for (char c : chars.toCharArray()) {
                have[c - 'a']++;
            }
            int total = 0;
            for (String word : words) {
                if (canBuild(word, have)) {
                    total += word.length();
                }
            }
            return total;
        }

        private boolean canBuild(String word, int[] have) {
            int[] need = new int[26];
            for (char c : word.toCharArray()) {
                need[c - 'a']++;
                if (need[c - 'a'] > have[c - 'a']) {
                    return false;               // needs more than we have
                }
            }
            return true;
        }
    }

    // ====================================================================
    // 10. Intersection of Two Arrays II
    // LeetCode 350 · Easy
    // Trick: count one array, spend counts while walking the other  ·  O(n + m)
    // ====================================================================

    // Step 1 · brute force
    public static class IntersectBrute {
        public int[] intersect(int[] a, int[] b) {
            List<Integer> rest = new ArrayList<>();
            for (int x : b) rest.add(x);
            List<Integer> out = new ArrayList<>();
            for (int x : a) {
                int pos = rest.indexOf(x);            // linear search
                if (pos != -1) {
                    out.add(x);
                    rest.remove(pos);                 // each copy used once
                }
            }
            int[] res = new int[out.size()];
            for (int i = 0; i < res.length; i++) res[i] = out.get(i);
            return res;
        }
    }

    // Step 2 · the better way
    public static class Intersect {
        public int[] intersect(int[] a, int[] b) {
            Map<Integer, Integer> count = new HashMap<>();
            for (int x : a) {
                count.put(x, count.getOrDefault(x, 0) + 1);
            }
            List<Integer> out = new ArrayList<>();
            for (int x : b) {
                int c = count.getOrDefault(x, 0);
                if (c > 0) {
                    out.add(x);
                    count.put(x, c - 1);              // one copy used up
                }
            }
            int[] res = new int[out.size()];
            for (int i = 0; i < res.length; i++) res[i] = out.get(i);
            return res;
        }
    }

    // ====================================================================
    // 11. Contains Duplicate II
    // LeetCode 219 · Easy
    // Trick: map value → last index; check i − last ≤ k  ·  O(n)
    // ====================================================================

    // Step 1 · brute force
    public static class NearbyDuplicateBrute {
        public boolean containsNearbyDuplicate(int[] nums, int k) {
            for (int i = 0; i < nums.length; i++) {
                // only look at the next k positions
                for (int j = i + 1; j <= i + k && j < nums.length; j++) {
                    if (nums[i] == nums[j]) return true;
                }
            }
            return false;
        }
    }

    // Step 2 · the better way
    public static class NearbyDuplicate {
        public boolean containsNearbyDuplicate(int[] nums, int k) {
            Map<Integer, Integer> lastIndex = new HashMap<>();
            for (int i = 0; i < nums.length; i++) {
                Integer prev = lastIndex.get(nums[i]);
                if (prev != null && i - prev <= k) return true;
                lastIndex.put(nums[i], i);            // keep the latest index
            }
            return false;
        }
    }

    // ====================================================================
    // 12. Happy Number
    // LeetCode 202 · Easy
    // Trick: replace n by sum of digit squares; a repeat means a loop  ·  O(log n) per step
    // ====================================================================

    // Step 1 · brute force
    public static class HappyNumberBrute {
        public boolean isHappy(int n) {
            List<Integer> seen = new ArrayList<>();   // a plain list
            while (n != 1) {
                if (seen.contains(n)) return false;   // O(size) search
                seen.add(n);
                n = next(n);
            }
            return true;
        }

        private int next(int n) {                     // 19 -> 1 + 81 = 82
            int sum = 0;
            while (n > 0) {
                int d = n % 10;
                sum += d * d;
                n /= 10;
            }
            return sum;
        }
    }

    // Step 2 · the better way
    public static class HappyNumber {
        public boolean isHappy(int n) {
            Set<Integer> seen = new HashSet<>();
            while (n != 1) {
                if (!seen.add(n)) return false;       // repeat = endless loop
                n = next(n);
            }
            return true;
        }

        private int next(int n) {
            int sum = 0;
            while (n > 0) {
                int d = n % 10;
                sum += d * d;
                n /= 10;
            }
            return sum;
        }
    }

    // ====================================================================
    // 13. Uncommon Words from Two Sentences
    // LeetCode 884 · Easy
    // Trick: count words across BOTH sentences; keep count == 1  ·  O(n)
    // ====================================================================

    // Step 1 · brute force
    public static class UncommonWordsBrute {
        public String[] uncommonFromSentences(String s1, String s2) {
            String[] all = (s1 + " " + s2).split(" ");
            List<String> result = new ArrayList<>();
            for (String w : all) {
                int count = 0;
                for (String x : all) {
                    if (x.equals(w)) count++;         // rescan everything
                }
                if (count == 1) result.add(w);
            }
            return result.toArray(new String[0]);
        }
    }

    // Step 2 · the better way
    public static class UncommonWords {
        public String[] uncommonFromSentences(String s1, String s2) {
            Map<String, Integer> count = new HashMap<>();
            for (String w : (s1 + " " + s2).split(" ")) {
                count.put(w, count.getOrDefault(w, 0) + 1);
            }
            List<String> result = new ArrayList<>();
            for (String w : count.keySet()) {
                if (count.get(w) == 1) result.add(w); // once in total
            }
            return result.toArray(new String[0]);
        }
    }

    // ====================================================================
    // 14. Most Common Word
    // LeetCode 819 · Easy
    // Trick: lower-case, split on [^a-z]+, count, skip banned set  ·  O(n)
    // ====================================================================

    // Step 1 · brute force
    public static class MostCommonWordBrute {
        public String mostCommonWord(String paragraph, String[] banned) {
            String[] words = paragraph.toLowerCase().split("[^a-z]+");
            List<String> bannedList = Arrays.asList(banned);
            String best = "";
            int bestCount = 0;
            for (String w : words) {
                if (w.isEmpty() || bannedList.contains(w)) continue;
                int count = 0;
                for (String x : words) {
                    if (x.equals(w)) count++;          // rescan every time
                }
                if (count > bestCount) {
                    bestCount = count;
                    best = w;
                }
            }
            return best;
        }
    }

    // Step 2 · the better way
    public static class MostCommonWord {
        public String mostCommonWord(String paragraph, String[] banned) {
            Set<String> ban = new HashSet<>(Arrays.asList(banned));
            Map<String, Integer> count = new HashMap<>();
            // lower-case, then split on anything that is not a letter
            for (String w : paragraph.toLowerCase().split("[^a-z]+")) {
                if (w.isEmpty() || ban.contains(w)) continue;
                count.put(w, count.getOrDefault(w, 0) + 1);
            }
            String best = "";
            for (String w : count.keySet()) {
                if (best.isEmpty() || count.get(w) > count.get(best)) {
                    best = w;
                }
            }
            return best;
        }
    }

    // ====================================================================
    // 15. Longest Palindrome (build one)
    // LeetCode 409 · Easy
    // Trick: add all pairs; +1 if any letter count is odd  ·  O(n)
    // ====================================================================

    // Step 1 · brute force
    public static class LongestPalindromeBrute {
        public int longestPalindrome(String s) {
            int length = 0;
            boolean odd = false;
            Set<Character> done = new HashSet<>();
            for (char c : s.toCharArray()) {
                if (!done.add(c)) continue;            // counted already
                int count = 0;
                for (char d : s.toCharArray()) {
                    if (d == c) count++;               // count c the slow way
                }
                length += (count / 2) * 2;             // use every pair
                if (count % 2 == 1) odd = true;
            }
            return odd ? length + 1 : length;          // one in the middle
        }
    }

    // Step 2 · the better way
    public static class LongestPalindromeBuild {
        public int longestPalindrome(String s) {
            int[] count = new int[128];                // every ASCII char
            for (char c : s.toCharArray()) count[c]++;
            int length = 0;
            boolean odd = false;
            for (int c : count) {
                length += (c / 2) * 2;         // pairs go on both sides
                if (c % 2 == 1) odd = true;    // a spare letter exists
            }
            return odd ? length + 1 : length;
        }
    }

    // ====================================================================
    // 16. Minimum Index Sum of Two Lists
    // LeetCode 599 · Easy
    // Trick: map list1 name → index; walk list2; keep all ties  ·  O(n + m)
    // ====================================================================

    // Step 1 · brute force
    public static class MinIndexSumBrute {
        public String[] findRestaurant(String[] list1, String[] list2) {
            int best = Integer.MAX_VALUE;
            List<String> result = new ArrayList<>();
            for (int i = 0; i < list1.length; i++) {
                for (int j = 0; j < list2.length; j++) {
                    if (!list1[i].equals(list2[j])) continue;
                    if (i + j < best) {                // new best: restart
                        best = i + j;
                        result.clear();
                        result.add(list1[i]);
                    } else if (i + j == best) {
                        result.add(list1[i]);          // a tie
                    }
                }
            }
            return result.toArray(new String[0]);
        }
    }

    // Step 2 · the better way
    public static class MinIndexSum {
        public String[] findRestaurant(String[] list1, String[] list2) {
            Map<String, Integer> index1 = new HashMap<>();
            for (int i = 0; i < list1.length; i++) index1.put(list1[i], i);
            int best = Integer.MAX_VALUE;
            List<String> result = new ArrayList<>();
            for (int j = 0; j < list2.length; j++) {
                Integer i = index1.get(list2[j]);       // O(1) lookup
                if (i == null) continue;                // not in list1
                if (i + j < best) {
                    best = i + j;
                    result.clear();
                    result.add(list2[j]);
                } else if (i + j == best) {
                    result.add(list2[j]);
                }
            }
            return result.toArray(new String[0]);
        }
    }

    // ====================================================================
    // 17. Sort Array by Increasing Frequency
    // LeetCode 1636 · Easy
    // Trick: count first, then comparator (count asc, value desc)  ·  O(n log n)
    // ====================================================================

    // Step 1 · brute force
    public static class FrequencySortBrute {
        public int[] frequencySort(int[] nums) {
            Integer[] a = new Integer[nums.length];
            for (int i = 0; i < nums.length; i++) a[i] = nums[i];
            Arrays.sort(a, (x, y) -> {
                int fx = 0, fy = 0;
                for (int n : nums) {           // recount EVERY compare
                    if (n == x) fx++;
                    if (n == y) fy++;
                }
                if (fx != fy) return fx - fy;          // rarer first
                return y - x;                          // tie: bigger first
            });
            int[] res = new int[nums.length];
            for (int i = 0; i < res.length; i++) res[i] = a[i];
            return res;
        }
    }

    // Step 2 · the better way
    public static class FrequencySortIncreasing {
        public int[] frequencySort(int[] nums) {
            Map<Integer, Integer> count = new HashMap<>();
            for (int n : nums) count.put(n, count.getOrDefault(n, 0) + 1);
            Integer[] a = new Integer[nums.length];    // boxed, so we can
            for (int i = 0; i < nums.length; i++) {    // use a comparator
                a[i] = nums[i];
            }
            Arrays.sort(a, (x, y) -> {
                int fx = count.get(x), fy = count.get(y);
                if (fx != fy) return fx - fy;          // rarer first
                return y - x;                          // tie: bigger first
            });
            int[] res = new int[nums.length];
            for (int i = 0; i < res.length; i++) res[i] = a[i];
            return res;
        }
    }

}
