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

    /**
     * QUESTION
     * You're given an array of numbers and a target. Find the two positions
     * whose numbers add up to the target. LeetCode guarantees exactly one
     * answer.
     *
     * EXAMPLE
     *   nums = [3, 8, 5, 1], target = 9
     *   answer = [1, 3]       because nums[1] + nums[3] = 8 + 1 = 9
     *
     * STEP 1 · BRUTE FORCE
     * Try every pair (i, j) with i < j and check whether nums[i] + nums[j] ==
     * target.
     *
     * Brute force cost: O(n²) time · O(1) space. Fine for 1,000 numbers, far
     * too slow for 100,000.
     */
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

    /**
     * STEP 2 · THE BETTER IDEA
     * For each number x, the partner you need is target - x. Searching the
     * array for it every time is O(n²). Instead, keep a map of numbers
     * already seen → their index, so checking for the partner is a single
     * lookup.
     *
     * DRY RUN
     *   i | nums[i] | need | map before this step | result
     *   0 | 3 | 6 | { } | not found → store 3→0
     *   1 | 8 | 1 | {3:0} | not found → store 8→1
     *   2 | 5 | 4 | {3:0, 8:1} | not found → store 5→2
     *   3 | 1 | 8 | {3:0, 8:1, 5:2} | found 8 at index 1 → [1, 3]
     */
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

    /*
     * Time: O(n) · Space: O(n)
     *
     * WATCH OUT
     * - Look up the partner before you store the current number. That way a
     *   number can't pair with itself: [3, 4] with target 6 must not return
     *   [0, 0].
     * - Duplicates such as [3, 3] with target 6 still work, because the first
     *   3 is already in the map when you reach the second one.
     *
     * POSSIBLE FOLLOW-UP
     * "The array is sorted. Can you use O(1) extra space?" → Use two
     * pointers: left at the start, right at the end. If the sum is too small,
     * move left++; if it's too big, move right--.
     */

    // ====================================================================
    // 2. Contains Duplicate
    // LeetCode 217 · Easy
    // Trick: HashSet; add() returns false on a repeat  ·  O(n)
    // ====================================================================

    /**
     * QUESTION
     * Return true if any value appears at least twice in the array.
     *
     * EXAMPLE
     *   [1, 2, 3, 1]  ->  true
     *   [1, 2, 3, 4]  ->  false
     *
     * STEP 1 · BRUTE FORCE
     * Compare every pair of positions and look for two equal values.
     *
     * Brute force cost: O(n²) time · O(1) space
     */
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

    /**
     * STEP 2 · THE BETTER IDEA
     * Walk once, keeping a HashSet of the values seen so far. set.add(x)
     * returns false when x is already there, which means you've found a
     * duplicate.
     *
     * DRY RUN
     *   1 -> add ok   {1}
     *   2 -> add ok   {1, 2}
     *   3 -> add ok   {1, 2, 3}
     *   1 -> add() returns false  ->  true
     */
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

    /*
     * Time: O(n) · Space: O(n)
     *
     * WATCH OUT
     * - If they ask you to save memory: sort, then compare neighbours. That's
     *   O(n log n) time and O(1) extra space.
     */

    // ====================================================================
    // 3. Valid Anagram
    // LeetCode 242 · Easy
    // Trick: int[26]: s adds, t subtracts, all must end at 0  ·  O(n)
    // ====================================================================

    /**
     * QUESTION
     * Is t a rearrangement of the letters of s?
     *
     * EXAMPLE
     *   "listen", "silent"  ->  true
     *   "rat", "car"        ->  false
     *
     * STEP 1 · BRUTE FORCE
     * Sort both strings and compare them. Anagrams become identical once
     * sorted.
     *
     * Brute force cost: O(n log n) time. This is honestly a fine first
     * answer.
     */
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

    /**
     * STEP 2 · THE BETTER IDEA
     * Use one int[26]. Each letter of s adds 1 and each letter of t subtracts
     * 1. If they're anagrams, every count cancels back to 0.
     *
     * DRY RUN ("RAT" VS "CAR")
     *   r: +1 (s), c: -1 (t)    a: +1, a: -1    t: +1, r: -1
     *   counts left:  c = -1,  t = +1   ->  not all zero  ->  false
     */
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

    /*
     * Time: O(n) · Space: O(1)
     *
     * WATCH OUT
     * - Check the lengths first. Different lengths can never be anagrams.
     * - For Unicode or uppercase input, use a HashMap instead of int[26].
     */

    // ====================================================================
    // 4. Majority Element
    // LeetCode 169 · Easy
    // Trick: count map (or Boyer–Moore vote for O(1) space)  ·  O(n)
    // ====================================================================

    /**
     * QUESTION
     * One value appears more than n/2 times. Return it.
     *
     * EXAMPLE
     *   [2, 2, 1, 1, 1, 2, 2]  ->  2       (4 out of 7)
     *
     * STEP 1 · BRUTE FORCE
     * For every value, count it with a full scan. Return it once a count goes
     * past n/2.
     *
     * Brute force cost: O(n²) time · O(1) space
     */
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

    /**
     * STEP 2 · THE BETTER IDEA
     * Count with a HashMap in one pass. You can return the moment a count
     * goes past n / 2.
     */
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

    /**
     * Time: O(n) · Space: O(n)
     *
     * BONUS: O(1) SPACE (BOYER–MOORE VOTING)
     * Think of it as an election. Keep one candidate and a votes counter. A
     * matching value votes for the candidate (+1); any other value votes
     * against (−1). When the votes reach 0, the next value becomes the new
     * candidate. The majority has more votes than everyone else combined, so
     * it is always the candidate left at the end.
     */
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

    /*
     * WATCH OUT
     * - Boyer–Moore only works because a majority is guaranteed. Without that
     *   guarantee, do a second pass to confirm the count.
     */

    // ====================================================================
    // 5. Ransom Note  ★ Karat
    // LeetCode 383 · Easy
    // Trick: int[26]; spend letters; any count < 0 → false  ·  O(n)
    // ====================================================================

    /**
     * QUESTION
     * Can you build the note from the letters in magazine? Each magazine
     * letter can be used only once.
     *
     * EXAMPLE
     *   note = "java", magazine = "avajx"   ->  true
     *   note = "java", magazine = "javx"    ->  false   (needs two 'a', has one)
     *
     * STEP 1 · BRUTE FORCE
     * For each letter of the note, search the magazine for it and delete it
     * so it can't be used twice. If a letter isn't found, return false.
     *
     * Brute force cost: O(m × n) time, because every search scans the
     * magazine again.
     */
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

    /**
     * STEP 2 · THE BETTER IDEA
     * Count the magazine's letters, then spend one letter for each character
     * of the note. If any count drops below 0, you've run out of that letter.
     *
     * Why int[26]? With only lowercase letters, c - 'a' turns a letter into
     * an index: 'a'→0, 'b'→1, … 'z'→25. That's simpler and faster than a
     * HashMap.
     *
     * DRY RUN (NOTE = "JAVA", MAGAZINE = "JAVX")
     *   step | letter | counts after | result
     *   count magazine |  | a=1, j=1, v=1, x=1 |
     *   spend | j | j=0 | ok
     *   spend | a | a=0 | ok
     *   spend | v | v=0 | ok
     *   spend | a | a=−1 | return false
     */
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

    /*
     * Time: O(m + n) · Space: O(1) (always 26 slots)
     *
     * WATCH OUT
     * - If the input can contain capital letters, spaces or digits, c - 'a'
     *   breaks. Use int[128] (covers all ASCII) or a HashMap.
     *
     * KARAT-STYLE FOLLOW-UP
     * "Which letters are missing, and how many of each?" → After spending,
     * every count below 0 is a shortfall: letter (char) ('a' + i) is missing
     * -count[i] times.
     */

    // ====================================================================
    // 6. First Unique Character in a String
    // LeetCode 387 · Easy
    // Trick: two passes: count, then first index with count 1  ·  O(n)
    // ====================================================================

    /**
     * QUESTION
     * Return the index of the first character that appears only once, or −1
     * if there isn't one.
     *
     * EXAMPLE
     *   "loveleetcode"  ->  2      ('v')
     *   "aabb"          ->  -1
     *
     * STEP 1 · BRUTE FORCE
     * For each position, scan the whole string looking for the same character
     * somewhere else. The first one with no match wins.
     *
     * Brute force cost: O(n²) time · O(1) space
     */
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

    /**
     * STEP 2 · THE BETTER IDEA
     * Use two passes. Pass 1 counts every letter. Pass 2 walks the string in
     * order and returns the first index whose letter has a count of 1.
     *
     * DRY RUN ("LOVELEETCODE")
     *   counts: l=2 o=2 v=1 e=4 t=1 c=1 d=1
     *   index 0 'l' -> 2   index 1 'o' -> 2   index 2 'v' -> 1  ->  return 2
     */
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

    /*
     * Time: O(n) · Space: O(1)
     *
     * KARAT ANGLE
     * "First X that happens only once" questions (first user who logged in
     * exactly once, first unique page) all use this count-then-scan shape.
     */

    // ====================================================================
    // 7. Isomorphic Strings
    // LeetCode 205 · Easy
    // Trick: two maps (s→t and t→s) must agree at every step  ·  O(n)
    // ====================================================================

    /**
     * QUESTION
     * Can you turn s into t by replacing letters, where each letter always
     * maps to the same letter and no two letters map to the same one?
     *
     * EXAMPLE
     *   "egg",   "add"    ->  true     (e->a, g->d)
     *   "foo",   "bar"    ->  false    (o would need to be a and r)
     *   "badc",  "baba"   ->  false    (b->b and d->b clash)
     *
     * STEP 1 · BRUTE FORCE
     * Two strings have the same "shape" if, for every pair of positions, the
     * letters are equal in s exactly when they're equal in t.
     *
     * Brute force cost: O(n²) time · O(1) space
     */
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

    /**
     * STEP 2 · THE BETTER IDEA
     * Keep two maps: s → t and t → s. At each position, if a mapping already
     * exists it must agree with what you see now. You need both directions,
     * because one map alone misses "badc" → "baba".
     *
     * DRY RUN ("BADC", "BABA")
     *   b->b ok    a->a ok    d->b ?  sToT has no d, but tToS says b came from b  ->  false
     */
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

    /*
     * Time: O(n) · Space: O(1) (at most 256 letters)
     *
     * WATCH OUT
     * - mapped != b compares a Character with a char. Java unboxes it, so
     *   it's safe. Comparing two Character objects with != is not safe; use
     *   .equals().
     */

    // ====================================================================
    // 8. Word Pattern
    // LeetCode 290 · Easy
    // Trick: same as Isomorphic, but letter ↔ word maps  ·  O(n)
    // ====================================================================

    /**
     * QUESTION
     * Does the sentence s follow pattern? Each letter must match one word,
     * and each word must match one letter.
     *
     * EXAMPLE
     *   "abba", "dog cat cat dog"   ->  true
     *   "abba", "dog cat cat fish"  ->  false
     *   "abba", "dog dog dog dog"   ->  false     (a and b both -> dog)
     *
     * STEP 1 · BRUTE FORCE
     * Split into words. For every pair of positions, "same letter" must equal
     * "same word".
     *
     * Brute force cost: O(n²) time
     */
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

    /**
     * STEP 2 · THE BETTER IDEA
     * It's Isomorphic Strings with words: keep a map from letter to word and
     * a map from word to letter. Check the word count against the pattern
     * length first.
     */
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

    /*
     * Time: O(n) · Space: O(n)
     *
     * WATCH OUT
     * - Compare words with .equals(), never ==.
     * - "aaa" with "dog dog" has different lengths, so return false straight
     *   away.
     */

    // ====================================================================
    // 9. Find Words That Can Be Formed by Characters  ★ Karat
    // LeetCode 1160 · Easy
    // Trick: count have once; fresh need array per word  ·  O(total)
    // ====================================================================

    /**
     * QUESTION
     * You get a list of words and a string chars. A word is "good" if you can
     * build it from chars, using each character at most once per word. Return
     * the total length of all good words.
     *
     * EXAMPLE
     *   words = ["cat", "bt", "hat", "tree"], chars = "atach"
     *   good words: "cat" (3) and "hat" (3)     ->  answer 6
     *
     * STEP 1 · BRUTE FORCE
     * For each word, make a fresh copy of chars and delete letters from the
     * copy as the word uses them. If a letter can't be found, the word fails.
     *
     * Brute force cost: O(total word letters × |chars|) time.
     */
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

    /**
     * STEP 2 · THE BETTER IDEA
     * Count chars once into have. For each word, count its letters into a
     * fresh need array. If any letter needs more than you have, the word
     * fails. Each word gets its own need, so one word doesn't use up letters
     * for the next.
     *
     * DRY RUN (HAVE: A=2, C=1, H=1, T=1)
     *   word | check | good?
     *   cat | c 1≤1, a 1≤2, t 1≤1 | yes (+3)
     *   bt | b 1 > 0 | no
     *   hat | h 1≤1, a 1≤2, t 1≤1 | yes (+3)
     *   tree | t ok, r 1 > 0 | no
     */
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

    /*
     * Time: O(total letters in all words) · Space: O(1)
     *
     * WATCH OUT
     * - Don't subtract from have while checking a word. A word that fails
     *   halfway would leave have broken for every word after it.
     *
     * KARAT VERSION
     * "Given a word list and a string of letters, return a word that can be
     * made from the letters (or null)." Reuse canBuild and return word; on
     * the first match. Common follow-up: return the longest word that can be
     * made.
     */

    // ====================================================================
    // 10. Intersection of Two Arrays II
    // LeetCode 350 · Easy
    // Trick: count one array, spend counts while walking the other  ·  O(n + m)
    // ====================================================================

    /**
     * QUESTION
     * Return the common elements of two arrays, as many times as they appear
     * in both. Any order is fine.
     *
     * EXAMPLE
     *   [1, 2, 2, 1] and [2, 2]          ->  [2, 2]
     *   [4, 9, 5]   and [9, 4, 9, 8, 4]  ->  [4, 9]
     *
     * STEP 1 · BRUTE FORCE
     * For each number in a, search a list copy of b for it and remove it when
     * found, so it can't match twice.
     *
     * Brute force cost: O(n × m) time
     */
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

    /**
     * STEP 2 · THE BETTER IDEA
     * Count array a in a map. Walk b: whenever a number still has a count
     * above 0, add it to the answer and subtract 1. This is the same "spend
     * the counts" idea as Ransom Note.
     */
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

    /*
     * Time: O(n + m) · Space: O(n)
     *
     * FOLLOW-UP THEY LIKE
     * "What if both arrays are already sorted?" Use two pointers and move the
     * one pointing at the smaller value. That needs O(1) extra space.
     */

    // ====================================================================
    // 11. Contains Duplicate II
    // LeetCode 219 · Easy
    // Trick: map value → last index; check i − last ≤ k  ·  O(n)
    // ====================================================================

    /**
     * QUESTION
     * Are there two equal values whose positions are at most k apart?
     *
     * EXAMPLE
     *   [1, 2, 3, 1],       k = 3  ->  true     (positions 0 and 3)
     *   [1, 2, 3, 1, 2, 3], k = 2  ->  false    (repeats are 3 apart)
     *
     * STEP 1 · BRUTE FORCE
     * For each position, check only the next k positions for an equal value.
     *
     * Brute force cost: O(n × k) time
     */
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

    /**
     * STEP 2 · THE BETTER IDEA
     * Remember the latest index of every value. When you meet the value
     * again, the gap to its latest index is the smallest possible, so if that
     * gap is ≤ k you're done.
     */
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

    /*
     * Time: O(n) · Space: O(n)
     *
     * SAME IDEA AS A SLIDING WINDOW
     * You could also keep a HashSet of just the last k values, removing
     * nums[i − k] as you move on. Both versions are O(n).
     */

    // ====================================================================
    // 12. Happy Number
    // LeetCode 202 · Easy
    // Trick: replace n by sum of digit squares; a repeat means a loop  ·  O(log n) per step
    // ====================================================================

    /**
     * QUESTION
     * Replace n with the sum of the squares of its digits, and repeat. If you
     * reach 1, n is "happy". If the numbers loop forever without reaching 1,
     * it isn't.
     *
     * EXAMPLE
     *   19 -> 1 + 81 = 82 -> 64 + 4 = 68 -> 36 + 64 = 100 -> 1   ->  true
     *   2  -> 4 -> 16 -> 37 -> 58 -> 89 -> 145 -> 42 -> 20 -> 4 (repeat)  ->  false
     *
     * STEP 1 · BRUTE FORCE
     * Store each number you've seen in a plain list and search the list every
     * step.
     *
     * Brute force cost: each check scans the whole list, so it's O(steps²)
     * work.
     */
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

    /**
     * STEP 2 · THE BETTER IDEA
     * Use a HashSet of the numbers you've seen. If a number shows up a second
     * time, you're in a loop that will never reach 1.
     */
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

    /*
     * Time: small in practice (the numbers quickly drop below 243) · Space:
     * O(steps)
     *
     * O(1) SPACE VERSION
     * Floyd's cycle check: move a slow number one step at a time and a fast
     * one two steps at a time. If they ever meet at something other than 1,
     * there's a loop. It's the same trick as detecting a cycle in a linked
     * list.
     */

    // ====================================================================
    // 13. Uncommon Words from Two Sentences
    // LeetCode 884 · Easy
    // Trick: count words across BOTH sentences; keep count == 1  ·  O(n)
    // ====================================================================

    /**
     * QUESTION
     * A word is "uncommon" if it appears exactly once in total across both
     * sentences. Return all of them.
     *
     * EXAMPLE
     *   "this apple is sweet", "this apple is sour"  ->  ["sweet", "sour"]
     *   "apple apple", "banana"                      ->  ["banana"]
     *
     * STEP 1 · BRUTE FORCE
     * Join the sentences into one word list. For each word, count it with a
     * full scan of the list.
     *
     * Brute force cost: O(w²) time for w words
     */
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

    /**
     * STEP 2 · THE BETTER IDEA
     * The trick is noticing that "once in one sentence and absent from the
     * other" is the same as once in total. Count every word of both sentences
     * in one map, then keep the words with count 1.
     */
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

    /*
     * Time: O(total characters) · Space: O(w)
     */

    // ====================================================================
    // 14. Most Common Word
    // LeetCode 819 · Easy
    // Trick: lower-case, split on [^a-z]+, count, skip banned set  ·  O(n)
    // ====================================================================

    /**
     * QUESTION
     * Given a paragraph with punctuation and mixed case, and a list of banned
     * words, return the most frequent word that isn't banned (in lower case).
     *
     * EXAMPLE
     *   "Bob hit a ball, the hit BALL flew far after it was hit."   banned = ["hit"]
     *   ->  "ball"      (hit appears 3 times but is banned; ball appears 2 times)
     *
     * STEP 1 · BRUTE FORCE
     * Clean the text into a word list. For each word that isn't banned, count
     * it with a full scan and keep the best.
     *
     * Brute force cost: O(w²) time, and bannedList.contains is a linear
     * search too.
     */
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

    /**
     * STEP 2 · THE BETTER IDEA
     * Clean once, count once. toLowerCase(), then split("[^a-z]+") splits on
     * any run of non-letters, which removes punctuation in a single step. Put
     * the banned words in a HashSet and count the rest in a map.
     *
     * DRY RUN
     *   lower + split -> [bob, hit, a, ball, the, hit, ball, flew, far, after, it, was, hit]
     *   count (no 'hit') -> ball=2, everything else 1   ->  "ball"
     */
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

    /*
     * Time: O(n) · Space: O(n)
     *
     * WATCH OUT
     * - Splitting can produce an empty first word when the text starts with
     *   punctuation. Skip empty strings.
     *
     * KARAT ANGLE
     * "Clean messy text, then count" is a common first step in log and click-
     * stream questions. Get the regex [^a-z]+ into your fingers.
     */

    // ====================================================================
    // 15. Longest Palindrome (build one)
    // LeetCode 409 · Easy
    // Trick: add all pairs; +1 if any letter count is odd  ·  O(n)
    // ====================================================================

    /**
     * QUESTION
     * Using the letters of s (case-sensitive), how long is the longest
     * palindrome you could build?
     *
     * EXAMPLE
     *   "abccccdd"  ->  7      (e.g. "dccaccd": cc cc dd pairs + one 'a' in the middle)
     *
     * STEP 1 · BRUTE FORCE
     * For each distinct letter, count it with a full scan. Add its pairs, and
     * remember whether any letter had an odd count left over.
     *
     * Brute force cost: O(n × d) time for d distinct letters
     */
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

    /**
     * STEP 2 · THE BETTER IDEA
     * A palindrome is pairs mirrored around the middle, plus at most one
     * single letter in the centre. So the answer is the sum of (count / 2) *
     * 2 over all letters, plus 1 if any count is odd.
     *
     * DRY RUN ("ABCCCCDD")
     *   a=1 -> 0 pairs (odd)   b=1 -> 0 (odd)   c=4 -> 4   d=2 -> 2
     *   pairs total = 6, some odd exists -> 6 + 1 = 7
     */
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

    /*
     * Time: O(n) · Space: O(1)
     *
     * WATCH OUT
     * - 'A' and 'a' are different letters here, which is why the code uses
     *   int[128] instead of int[26].
     */

    // ====================================================================
    // 16. Minimum Index Sum of Two Lists
    // LeetCode 599 · Easy
    // Trick: map list1 name → index; walk list2; keep all ties  ·  O(n + m)
    // ====================================================================

    /**
     * QUESTION
     * Two people list their favourite restaurants in order. Find the common
     * restaurants with the smallest index sum (position in list 1 + position
     * in list 2). Return every restaurant that ties.
     *
     * EXAMPLE
     *   list1 = [Dosa Hut, Pizza Point, Chai Spot]
     *   list2 = [Chai Spot, Momo Bar, Dosa Hut]
     *   Dosa Hut: 0 + 2 = 2     Chai Spot: 2 + 0 = 2     ->  [Chai Spot, Dosa Hut]
     *
     * STEP 1 · BRUTE FORCE
     * Compare every pair of names. On a match, if the sum beats the best,
     * restart the result list; if it ties, add to it.
     *
     * Brute force cost: O(n × m × L) time (L = name length)
     */
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

    /**
     * STEP 2 · THE BETTER IDEA
     * Put list 1 in a map from name to index. Walk list 2, and for each name
     * you can find its list-1 index in O(1). Use the same "new best → clear,
     * tie → add" rule.
     */
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

    /*
     * Time: O(n + m) · Space: O(n)
     *
     * WATCH OUT
     * - Don't forget the ties. Using clear() when you find a strictly better
     *   sum is the clean way to handle them.
     */

    // ====================================================================
    // 17. Sort Array by Increasing Frequency
    // LeetCode 1636 · Easy
    // Trick: count first, then comparator (count asc, value desc)  ·  O(n log n)
    // ====================================================================

    /**
     * QUESTION
     * Sort the numbers so the rarer values come first. If two values are
     * equally common, put the bigger value first.
     *
     * EXAMPLE
     *   [1, 1, 2, 2, 2, 3]  ->  [3, 1, 1, 2, 2, 2]
     *   [2, 3, 1, 3, 2]     ->  [1, 3, 3, 2, 2]     (3 and 2 tie: bigger 3 first)
     *
     * STEP 1 · BRUTE FORCE
     * Sort with a comparator that recounts both values by scanning the array
     * on every comparison.
     *
     * Brute force cost: O(n² log n) time, because each of the n log n
     * comparisons costs O(n).
     */
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

    /**
     * STEP 2 · THE BETTER IDEA
     * Count every value once into a map, then sort with a comparator that
     * just looks the counts up: count ascending, then value descending.
     */
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

    /*
     * Time: O(n log n) · Space: O(n)
     *
     * WATCH OUT
     * - Arrays.sort with a comparator needs Integer[], not int[], so you have
     *   to box the values first.
     * - The lesson: never do expensive work inside a comparator. Precompute
     *   it.
     */

}
