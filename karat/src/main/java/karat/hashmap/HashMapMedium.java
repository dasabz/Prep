package karat.hashmap;

import java.util.*;

/**
 * Part 1 · Hash Map, Counting & Sorting · Medium (13 problems).
 *
 * Explanations, dry runs and pitfalls: pdfs/Karat-1b-Medium-HashMap-Counting-Sorting.pdf
 * Each problem: Step 1 = brute force, Step 2 = the better way.
 * Every example is checked in src/test/java/karat/hashmap/HashMapTests.java
 */
public final class HashMapMedium {

    private HashMapMedium() { }

    // ====================================================================
    // 1. Group Anagrams
    // LeetCode 49 · Medium
    // Trick: key = sorted letters  ·  O(n·k log k)
    // ====================================================================

    /**
     * QUESTION
     * Group together words that are anagrams of each other: the same letters
     * in a different order.
     *
     * EXAMPLE
     *   ["tea", "eat", "tan", "ate", "nat", "bat"]
     *   ->  [["tea","eat","ate"], ["tan","nat"], ["bat"]]      (any order)
     *
     * STEP 1 · BRUTE FORCE
     * Keep a list of groups. For each word, compare it with the first word of
     * every group using a letter-count check. Join the first group that
     * matches; otherwise start a new group.
     *
     * Brute force cost: O(n² × k) time in the worst case (every word compared
     * with every group).
     */
    // Step 1 · brute force
    public static class GroupAnagramsBrute {
        public List<List<String>> groupAnagrams(String[] words) {
            List<List<String>> groups = new ArrayList<>();
            for (String w : words) {
                boolean placed = false;
                for (List<String> g : groups) {        // try every group
                    if (isAnagram(g.get(0), w)) {
                        g.add(w);
                        placed = true;
                        break;
                    }
                }
                if (!placed) {                         // start a new group
                    List<String> g = new ArrayList<>();
                    g.add(w);
                    groups.add(g);
                }
            }
            return groups;
        }

        private boolean isAnagram(String a, String b) {
            if (a.length() != b.length()) return false;
            int[] count = new int[26];
            for (int i = 0; i < a.length(); i++) {
                count[a.charAt(i) - 'a']++;
                count[b.charAt(i) - 'a']--;
            }
            for (int c : count) {
                if (c != 0) return false;
            }
            return true;
        }
    }

    /**
     * STEP 2 · THE BETTER IDEA
     * Anagrams become identical once you sort their letters: "tea", "eat" and
     * "ate" all become "aet". Use that sorted string as the map key, with a
     * list of the original words as the value.
     *
     * DRY RUN
     *   word | key (sorted) | map after
     *   tea | aet | aet:[tea]
     *   eat | aet | aet:[tea, eat]
     *   tan | ant | …, ant:[tan]
     *   ate | aet | aet:[tea, eat, ate]
     *   nat | ant | ant:[tan, nat]
     *   bat | abt | …, abt:[bat]
     */
    // Step 2 · the better way
    public static class GroupAnagrams {
        public List<List<String>> groupAnagrams(String[] words) {
            // sorted letters -> all words made of those letters
            Map<String, List<String>> groups = new HashMap<>();
            for (String w : words) {
                char[] letters = w.toCharArray();
                Arrays.sort(letters);
                String key = new String(letters);   // "tea" -> "aet"
                groups.putIfAbsent(key, new ArrayList<>());
                groups.get(key).add(w);
            }
            return new ArrayList<>(groups.values());
        }
    }

    /*
     * Time: O(n · k log k), where k is the word length · Space: O(n · k)
     *
     * WATCH OUT
     * - A char[] can't be used as a map key, because arrays are compared by
     *   reference rather than content. Convert it to a String first.
     * - Faster key if they ask: count letters into int[26] and use
     *   Arrays.toString(count) as the key. That needs no sort, so it's O(n ·
     *   k).
     */

    // ====================================================================
    // 2. Top K Frequent Elements
    // LeetCode 347 · Medium
    // Trick: count → sort keys by count desc → take k  ·  O(n log n)
    // ====================================================================

    /**
     * QUESTION
     * Return the k numbers that appear most often.
     *
     * EXAMPLE
     *   nums = [4, 4, 4, 7, 7, 9], k = 2   ->  [4, 7]
     *
     * STEP 1 · BRUTE FORCE
     * For each distinct number, scan the whole array to count it. Then pick
     * the largest count, k times.
     *
     * Brute force cost: O(n²) time, because every distinct number rescans the
     * array.
     */
    // Step 1 · brute force
    public static class TopKFrequentBrute {
        public int[] topKFrequent(int[] nums, int k) {
            List<Integer> distinct = new ArrayList<>();
            List<Integer> counts = new ArrayList<>();
            for (int n : nums) {
                if (distinct.contains(n)) continue;     // done already
                int c = 0;
                for (int m : nums) {
                    if (m == n) c++;                    // scan whole array
                }
                distinct.add(n);
                counts.add(c);
            }
            int[] result = new int[k];
            for (int i = 0; i < k; i++) {        // pick the max, k times
                int best = 0;
                for (int j = 1; j < counts.size(); j++) {
                    if (counts.get(j) > counts.get(best)) best = j;
                }
                result[i] = distinct.get(best);
                counts.set(best, -1);                   // never pick it again
            }
            return result;
        }
    }

    /**
     * STEP 2 · THE BETTER IDEA
     * Two simple steps: count with a map, then sort the distinct numbers by
     * their count (highest first) and take the first k.
     *
     * DRY RUN
     *   count        = {4:3, 7:2, 9:1}
     *   sorted keys  = [4, 7, 9]        (by count, high to low)
     *   first k = 2  = [4, 7]
     */
    // Step 2 · the better way
    public static class TopKFrequent {
        public int[] topKFrequent(int[] nums, int k) {
            // 1) count each number
            Map<Integer, Integer> count = new HashMap<>();
            for (int n : nums) {
                count.put(n, count.getOrDefault(n, 0) + 1);
            }
            // 2) sort the distinct numbers, most frequent first
            List<Integer> keys = new ArrayList<>(count.keySet());
            keys.sort((a, b) -> count.get(b) - count.get(a));

            // 3) take the first k
            int[] result = new int[k];
            for (int i = 0; i < k; i++) {
                result[i] = keys.get(i);
            }
            return result;
        }
    }

    /*
     * Time: O(n log n) in the worst case (sorting the distinct values) ·
     * Space: O(n)
     *
     * WATCH OUT
     * - The comparator (a, b) -> count.get(b) - count.get(a) means bigger
     *   count first. Swap a and b and you get ascending order instead.
     *
     * IF THEY ASK "CAN YOU BEAT N LOG N?"
     * - Min-heap of size k: push each number and pop when the heap grows past
     *   k. That's O(n log k).
     * - Bucket sort: bucket[c] holds the numbers that appear exactly c times.
     *   Walk the buckets from high to low. That's O(n).
     *
     * Write the simple version first, then mention these.
     */

    // ====================================================================
    // 3. Custom Sort String
    // LeetCode 791 · Medium
    // Trick: count s; write letters in order's order, then the rest  ·  O(n)
    // ====================================================================

    /**
     * QUESTION
     * order lists some letters in a custom order. Rearrange s so that its
     * letters follow that order. Letters not in order can go anywhere.
     *
     * EXAMPLE
     *   order = "cba", s = "abcd"   ->  "cbad"   ("dcba", "cdba", ... also accepted)
     *
     * STEP 1 · BRUTE FORCE
     * Sort the characters of s with a comparator based on order.indexOf(c).
     * Letters missing from order get −1 and simply go first, which is
     * allowed.
     *
     * Brute force cost: O(n log n × 26) time, since indexOf scans order on
     * every comparison.
     */
    // Step 1 · brute force
    public static class CustomSortBrute {
        public String customSortString(String order, String s) {
            Character[] chars = new Character[s.length()];
            for (int i = 0; i < s.length(); i++) chars[i] = s.charAt(i);
            // sort by position in 'order' (letters not in order get -1)
            Arrays.sort(chars, (a, b) -> order.indexOf(a) - order.indexOf(b));
            StringBuilder sb = new StringBuilder();
            for (char c : chars) sb.append(c);
            return sb.toString();
        }
    }

    /**
     * STEP 2 · THE BETTER IDEA
     * It's counting, not sorting. Count the letters of s. Then write the
     * letters in order's order, each as many times as it was counted.
     * Finally, write whatever letters are left.
     *
     * DRY RUN
     *   s counts: a1 b1 c1 d1
     *   order c -> "c"   b -> "cb"   a -> "cba"
     *   leftovers: d -> "cbad"
     */
    // Step 2 · the better way
    public static class CustomSort {
        public String customSortString(String order, String s) {
            int[] count = new int[26];
            for (char c : s.toCharArray()) count[c - 'a']++;
            StringBuilder sb = new StringBuilder();
            for (char c : order.toCharArray()) {       // 1) order's letters
                while (count[c - 'a'] > 0) {
                    sb.append(c);
                    count[c - 'a']--;
                }
            }
            for (char c = 'a'; c <= 'z'; c++) {        // 2) everything else
                while (count[c - 'a'] > 0) {
                    sb.append(c);
                    count[c - 'a']--;
                }
            }
            return sb.toString();
        }
    }

    /*
     * Time: O(n + 26) · Space: O(1)
     *
     * PATTERN
     * Whenever the "sort order" is a small fixed alphabet, counting beats
     * sorting. It's the same trick as bucket sort.
     */

    // ====================================================================
    // 4. Top K Frequent Words
    // LeetCode 692 · Medium
    // Trick: count → size-k min-heap (worst on top) → reverse  ·  O(n log k)
    // ====================================================================

    /**
     * QUESTION
     * Return the k most frequent words, most frequent first. If two words are
     * equally frequent, put them in alphabetical order.
     *
     * EXAMPLE
     *   ["i","love","leetcode","i","love","coding"], k = 2   ->  ["i", "love"]
     *   (i and love both appear twice; "i" < "love" alphabetically)
     *
     * STEP 1 · BRUTE FORCE
     * Count with a map, sort all the distinct words (count descending, then
     * A–Z), and take the first k.
     *
     * Brute force cost: O(n log n) time. Completely fine in an interview as
     * the first answer.
     */
    // Step 1 · brute force
    public static class TopKWordsSort {
        public List<String> topKFrequent(String[] words, int k) {
            Map<String, Integer> count = new HashMap<>();
            for (String w : words) count.put(w, count.getOrDefault(w, 0) + 1);
            List<String> keys = new ArrayList<>(count.keySet());
            keys.sort((a, b) -> {
                int ca = count.get(a), cb = count.get(b);
                if (ca != cb) return cb - ca;          // more frequent first
                return a.compareTo(b);                 // tie: A to Z
            });
            return new ArrayList<>(keys.subList(0, k));
        }
    }

    /**
     * STEP 2 · THE BETTER IDEA
     * To avoid sorting everything, keep a min-heap of size k whose top is the
     * worst word you're keeping. Push each word; when the heap grows past k,
     * pop the worst. At the end, pop everything and reverse so the best comes
     * first.
     *
     * What counts as "worse": fewer occurrences, or the same count but later
     * alphabetically.
     */
    // Step 2 · the better way
    public static class TopKWordsHeap {
        public List<String> topKFrequent(String[] words, int k) {
            Map<String, Integer> count = new HashMap<>();
            for (String w : words) count.put(w, count.getOrDefault(w, 0) + 1);
            // heap top = the WORST word we are keeping (thrown out first)
            PriorityQueue<String> heap = new PriorityQueue<>((a, b) -> {
                int ca = count.get(a), cb = count.get(b);
                if (ca != cb) return ca - cb;          // fewer = worse
                return b.compareTo(a);                 // later in A-Z = worse
            });
            for (String w : count.keySet()) {
                heap.add(w);
                if (heap.size() > k) heap.poll();      // drop the worst
            }
            List<String> result = new ArrayList<>();
            while (!heap.isEmpty()) result.add(heap.poll());
            Collections.reverse(result);               // best first
            return result;
        }
    }

    /*
     * Time: O(n log k) · Space: O(n)
     *
     * WATCH OUT
     * - In the sort version, compare counts as ints (int ca = count.get(a)).
     *   Comparing two Integer objects with == breaks for values above 127.
     * - The heap comparator is the reverse of the output order. That's why
     *   you reverse at the end.
     *
     * KARAT ANGLE
     * "Top k pages / users / errors from a log" is Part 1 counting plus this
     * heap. Say both versions, then code the one they ask for.
     */

    // ====================================================================
    // 5. Sort Characters By Frequency
    // LeetCode 451 · Medium
    // Trick: same as Top K + StringBuilder  ·  O(n log n)
    // ====================================================================

    /**
     * QUESTION
     * Rearrange a string so the most frequent characters come first, with all
     * copies of a character kept together.
     *
     * EXAMPLE
     *   "cabcac"  ->  "cccaab"        (c×3, a×2, b×1)
     *   "tree"    ->  "eetr" or "eert" (ties may come in any order)
     *
     * STEP 1 · BRUTE FORCE
     * Repeatedly find the most frequent character not used yet, counting it
     * with a full scan of the string. Append it that many times, mark it
     * used, and repeat.
     *
     * Brute force cost: O(d × n²) time for d distinct characters.
     */
    // Step 1 · brute force
    public static class SortByFrequencyBrute {
        public String frequencySort(String s) {
            StringBuilder sb = new StringBuilder();
            boolean[] used = new boolean[128];
            while (true) {
                // find the most frequent character not used yet
                char bestChar = 0;
                int bestCount = 0;
                for (char c : s.toCharArray()) {
                    if (used[c]) continue;
                    int count = 0;
                    for (char d : s.toCharArray()) {
                        if (d == c) count++;            // full rescan
                    }
                    if (count > bestCount) {
                        bestCount = count;
                        bestChar = c;
                    }
                }
                if (bestCount == 0) break;              // everything used
                used[bestChar] = true;
                for (int i = 0; i < bestCount; i++) sb.append(bestChar);
            }
            return sb.toString();
        }
    }

    /**
     * STEP 2 · THE BETTER IDEA
     * It's the same as Top K: count the characters, sort them by count, then
     * write each character count times using a StringBuilder.
     */
    // Step 2 · the better way
    public static class SortByFrequency {
        public String frequencySort(String s) {
            Map<Character, Integer> count = new HashMap<>();
            for (char c : s.toCharArray()) {
                count.put(c, count.getOrDefault(c, 0) + 1);
            }
            List<Character> chars = new ArrayList<>(count.keySet());
            chars.sort((a, b) -> count.get(b) - count.get(a));

            StringBuilder sb = new StringBuilder();
            for (char c : chars) {
                int times = count.get(c);
                for (int i = 0; i < times; i++) {
                    sb.append(c);
                }
            }
            return sb.toString();
        }
    }

    /*
     * Time: O(n + d log d), where d is the number of distinct characters ·
     * Space: O(n)
     *
     * WATCH OUT
     * - Build the result with StringBuilder. Writing result += c in a loop
     *   copies the whole string every time, which makes it O(n²).
     */

    // ====================================================================
    // 6. Bulls and Cows
    // LeetCode 299 · Medium
    // Trick: bulls by position; cows = Σ min(leftover counts)  ·  O(n)
    // ====================================================================

    /**
     * QUESTION
     * In a guessing game, bulls are digits that are right and in the right
     * place. Cows are right digits in the wrong place, with each digit used
     * only once. Return "xAyB".
     *
     * EXAMPLE
     *   secret "1807", guess "7810"   ->  "1A3B"   (8 is a bull; 1, 7, 0 are cows)
     *   secret "1123", guess "0111"   ->  "1A1B"
     *
     * STEP 1 · BRUTE FORCE
     * First mark the bulls. Then, for each leftover guess digit, search the
     * secret for an unused matching digit and mark it used.
     *
     * Brute force cost: O(n²) time
     */
    // Step 1 · brute force
    public static class BullsCowsBrute {
        public String getHint(String secret, String guess) {
            int n = secret.length(), bulls = 0, cows = 0;
            boolean[] usedS = new boolean[n], usedG = new boolean[n];
            for (int i = 0; i < n; i++) {              // bulls: same place
                if (secret.charAt(i) == guess.charAt(i)) {
                    bulls++;
                    usedS[i] = true;
                    usedG[i] = true;
                }
            }
            for (int i = 0; i < n; i++) {              // each leftover guess
                if (usedG[i]) continue;
                for (int j = 0; j < n; j++) {          // find an unused match
                    if (!usedS[j] && secret.charAt(j) == guess.charAt(i)) {
                        cows++;
                        usedS[j] = true;
                        break;
                    }
                }
            }
            return bulls + "A" + cows + "B";
        }
    }

    /**
     * STEP 2 · THE BETTER IDEA
     * Bulls come from comparing position by position. For the leftovers,
     * count the secret's digits and the guess's digits separately. For each
     * digit 0–9, the number of cows is min(secretCount, guessCount), because
     * that's how many pairs you can actually match.
     *
     * DRY RUN ("1123" VS "0111")
     *   pos 1: 1 == 1 -> bull
     *   leftover secret digits: 1, 2, 3     leftover guess digits: 0, 1, 1
     *   digit 1: min(1, 2) = 1 cow          others: 0
     *   ->  "1A1B"
     */
    // Step 2 · the better way
    public static class BullsCows {
        public String getHint(String secret, String guess) {
            int bulls = 0, cows = 0;
            int[] s = new int[10];          // leftover secret digits
            int[] g = new int[10];          // leftover guess digits
            for (int i = 0; i < secret.length(); i++) {
                char a = secret.charAt(i), b = guess.charAt(i);
                if (a == b) {
                    bulls++;
                } else {
                    s[a - '0']++;
                    g[b - '0']++;
                }
            }
            for (int d = 0; d < 10; d++) {
                cows += Math.min(s[d], g[d]);          // pairs we can match
            }
            return bulls + "A" + cows + "B";
        }
    }

    /*
     * Time: O(n) · Space: O(1)
     */

    // ====================================================================
    // 7. Find Players With Zero or One Losses  ★ Karat
    // LeetCode 2225 · Medium
    // Trick: TreeMap player→losses; putIfAbsent for winners  ·  O(n log n)
    // ====================================================================

    /**
     * QUESTION
     * Each match is [winner, loser]. Return two sorted lists: players who
     * never lost, and players who lost exactly once. Only count players who
     * played at least one match.
     *
     * EXAMPLE
     *   matches = [[1,2], [3,2], [2,4]]
     *   lost 0 times:  [1, 3]
     *   lost 1 time:   [4]          (2 lost twice, so it's in neither list)
     *
     * STEP 1 · BRUTE FORCE
     * Collect every player. For each player, scan all the matches and count
     * how many they lost.
     *
     * Brute force cost: O(p × m) time for p players and m matches.
     */
    // Step 1 · brute force
    public static class ZeroOrOneLossesBrute {
        public List<List<Integer>> findWinners(int[][] matches) {
            Set<Integer> players = new TreeSet<>();     // sorted, no repeats
            for (int[] m : matches) {
                players.add(m[0]);
                players.add(m[1]);
            }
            List<Integer> zero = new ArrayList<>();
            List<Integer> one = new ArrayList<>();
            for (int p : players) {
                int lost = 0;
                for (int[] m : matches) {
                    if (m[1] == p) lost++;              // scan every match
                }
                if (lost == 0) zero.add(p);
                if (lost == 1) one.add(p);
            }
            return Arrays.asList(zero, one);
        }
    }

    /**
     * STEP 2 · THE BETTER IDEA
     * Keep a map of player → number of losses. Add winners with 0, using
     * putIfAbsent so you don't reset a count they already have, and add 1 for
     * each loser. A TreeMap keeps the players sorted for you.
     *
     * DRY RUN
     *   match | map after
     *   [1, 2] | {1:0, 2:1}
     *   [3, 2] | {1:0, 2:2, 3:0}
     *   [2, 4] | {1:0, 2:2, 3:0, 4:1}
     *
     * zero losses → [1, 3]  ·  one loss → [4]
     */
    // Step 2 · the better way
    public static class ZeroOrOneLosses {
        public List<List<Integer>> findWinners(int[][] matches) {
            // player -> number of losses. TreeMap keeps players sorted.
            Map<Integer, Integer> losses = new TreeMap<>();
            for (int[] m : matches) {
                int winner = m[0], loser = m[1];
                losses.putIfAbsent(winner, 0);       // 0 losses so far
                losses.put(loser, losses.getOrDefault(loser, 0) + 1);
            }
            List<Integer> zero = new ArrayList<>();
            List<Integer> one = new ArrayList<>();
            for (int player : losses.keySet()) {
                int lost = losses.get(player);
                if (lost == 0) zero.add(player);
                if (lost == 1) one.add(player);
            }
            return Arrays.asList(zero, one);
        }
    }

    /*
     * Time: O(n log n) (TreeMap) · Space: O(n)
     *
     * WATCH OUT
     * - Use putIfAbsent for winners. put(winner, 0) would wipe out losses
     *   that player already had.
     *
     * THIS IS A FAMOUS KARAT PROBLEM IN DISGUISE
     * In Karat's parent–child pairs question, Part 1 gives you pairs [parent,
     * child] and asks for the people with zero parents and the people with
     * exactly one parent. It's the same code: parent = winner, child = loser.
     *
     * Parts 2 and 3 (common ancestor, earliest ancestor) are in Part 4 —
     * Graph Basics.
     */

    // ====================================================================
    // 8. Subdomain Visit Count  ★ Karat
    // LeetCode 811 · Medium
    // Trick: add count to the domain and every suffix after '.'  ·  O(chars)
    // ====================================================================

    /**
     * QUESTION
     * Each entry looks like "900 mail.google.com". A visit to mail.google.com
     * also counts as a visit to google.com and to com. Return the total
     * number of visits for every domain and subdomain.
     *
     * EXAMPLE
     *   ["900 mail.google.com", "50 yahoo.com", "1 maps.google.com", "5 wiki.org"]
     *
     *   ->  900 mail.google.com    1 maps.google.com    901 google.com
     *       50 yahoo.com           951 com              5 wiki.org
     *       5 org                                          (any order)
     *
     * STEP 1 · BRUTE FORCE
     * Without a hash map: keep two lists, names and totals, and linear-search
     * the names list for every domain and every parent domain.
     *
     * Brute force cost: O(s²) time for s distinct subdomains, because each
     * lookup scans the list.
     */
    // Step 1 · brute force
    public static class SubdomainVisitsBrute {
        public List<String> subdomainVisits(String[] cpdomains) {
            List<String> names = new ArrayList<>();     // no hash map:
            List<Integer> totals = new ArrayList<>();   // two parallel lists
            for (String entry : cpdomains) {
                String[] parts = entry.split(" ");
                int visits = Integer.parseInt(parts[0]);
                String domain = parts[1];
                while (true) {
                    int idx = names.indexOf(domain);    // linear search
                    if (idx == -1) {
                        names.add(domain);
                        totals.add(visits);
                    } else {
                        totals.set(idx, totals.get(idx) + visits);
                    }
                    int dot = domain.indexOf('.');
                    if (dot == -1) break;               // no parent left
                    domain = domain.substring(dot + 1); // drop first part
                }
            }
            List<String> result = new ArrayList<>();
            for (int i = 0; i < names.size(); i++) {
                result.add(totals.get(i) + " " + names.get(i));
            }
            return result;
        }
    }

    /**
     * STEP 2 · THE BETTER IDEA
     * Split each entry into a count and a domain. Add the count to the full
     * domain, then to every suffix after a dot. For mail.google.com those
     * suffixes are google.com and com.
     *
     * DRY RUN FOR "900 MAIL.GOOGLE.COM"
     *   m a i l . g o o g l e . c o m
     *           ^ index 4      ^ index 11
     *   full domain          mail.google.com  += 900
     *   after dot at 4   ->  google.com       += 900
     *   after dot at 11  ->  com              += 900
     */
    // Step 2 · the better way
    public static class SubdomainVisits {
        public List<String> subdomainVisits(String[] cpdomains) {
            Map<String, Integer> count = new HashMap<>();
            for (String entry : cpdomains) {
                String[] parts = entry.split(" "); // "900", "mail.google.com"
                int visits = Integer.parseInt(parts[0]);
                String domain = parts[1];

                // the full domain counts...
                count.put(domain, count.getOrDefault(domain, 0) + visits);
                // ...and so does every part after a '.'
                for (int i = 0; i < domain.length(); i++) {
                    if (domain.charAt(i) == '.') {
                        String sub = domain.substring(i + 1);
                        count.put(sub, count.getOrDefault(sub, 0) + visits);
                    }
                }
            }
            List<String> result = new ArrayList<>();
            for (String d : count.keySet()) {
                result.add(count.get(d) + " " + d);
            }
            return result;
        }
    }

    /*
     * Time: O(total characters) · Space: O(number of distinct subdomains)
     *
     * WATCH OUT
     * - split(".") doesn't work, because . in a regex means "any character".
     *   Use split("\\."), or scan for '.' the way this code does.
     *
     * KARAT VERSION
     * Karat's version gives lines like "900,google.com", so split on ","
     * instead. A related Karat follow-up asks for the longest common
     * continuous browsing history of two users. That one is in Part 6 — DP.
     */

    // ====================================================================
    // 9. Repeated DNA Sequences
    // LeetCode 187 · Medium
    // Trick: every 10-letter window into seen; second time → answer  ·  O(n)
    // ====================================================================

    /**
     * QUESTION
     * Return every 10-letter substring that appears more than once in a DNA
     * string.
     *
     * EXAMPLE
     *   "AAAAACCCCCAAAAACCCCCCAAAAAGGGTTT"  ->  ["AAAAACCCCC", "CCCCCAAAAA"]
     *
     * STEP 1 · BRUTE FORCE
     * Compare every window with every later window.
     *
     * Brute force cost: O(n² × 10) time
     */
    // Step 1 · brute force
    public static class RepeatedDnaBrute {
        public List<String> findRepeatedDnaSequences(String s) {
            Set<String> result = new TreeSet<>();
            for (int i = 0; i + 10 <= s.length(); i++) {
                String a = s.substring(i, i + 10);
                for (int j = i + 1; j + 10 <= s.length(); j++) {
                    if (a.equals(s.substring(j, j + 10))) {   // compare all
                        result.add(a);
                        break;
                    }
                }
            }
            return new ArrayList<>(result);
        }
    }

    /**
     * STEP 2 · THE BETTER IDEA
     * Slide a 10-letter window along the string. Put each window in a seen
     * set. If add returns false, you've seen it before, so add it to
     * repeated, which is also a set so each answer appears once.
     */
    // Step 2 · the better way
    public static class RepeatedDna {
        public List<String> findRepeatedDnaSequences(String s) {
            Set<String> seen = new HashSet<>();
            Set<String> repeated = new TreeSet<>();
            for (int i = 0; i + 10 <= s.length(); i++) {
                String part = s.substring(i, i + 10);
                if (!seen.add(part)) repeated.add(part);  // 2nd time seen
            }
            return new ArrayList<>(repeated);
        }
    }

    /*
     * Time: O(n × 10) · Space: O(n × 10)
     *
     * KARAT ANGLE
     * "Find the repeated patterns / sessions in a log" questions use exactly
     * this window + seen set.
     */

    // ====================================================================
    // 10. Longest Consecutive Sequence
    // LeetCode 128 · Medium
    // Trick: HashSet; start only where n−1 is missing  ·  O(n)
    // ====================================================================

    /**
     * QUESTION
     * Given an unsorted array, find the length of the longest run of
     * consecutive integers (like 1, 2, 3, 4). It must be O(n), so you can't
     * sort.
     *
     * EXAMPLE
     *   [100, 4, 200, 1, 3, 2]   ->  4        (the run 1, 2, 3, 4)
     *
     * STEP 1 · BRUTE FORCE
     * Sort a copy, then walk it, counting how long each run of +1 steps lasts
     * and skipping duplicates.
     *
     * Brute force cost: O(n log n) time. This is a perfectly good first
     * answer! The HashSet version only matters when they insist on O(n).
     */
    // Step 1 · brute force
    public static class LongestConsecutiveSort {
        public int longestConsecutive(int[] nums) {
            if (nums.length == 0) return 0;
            int[] a = nums.clone();
            Arrays.sort(a);
            int best = 1, run = 1;
            for (int i = 1; i < a.length; i++) {
                if (a[i] == a[i - 1]) continue;         // duplicate: skip
                if (a[i] == a[i - 1] + 1) {
                    run++;                              // run continues
                } else {
                    run = 1;                            // run broken
                }
                best = Math.max(best, run);
            }
            return best;
        }
    }

    /**
     * STEP 2 · THE BETTER IDEA
     * Put every number in a HashSet. A number n is the start of a run only if
     * n - 1 is not in the set. From each start, count up through n+1, n+2, …
     * for as long as they exist. Every number is touched at most twice, so
     * the whole thing is O(n).
     *
     * DRY RUN (SET = {100, 4, 200, 1, 3, 2})
     *   n | is n−1 in set? | action | length
     *   100 | 99: no | start, 101 missing | 1
     *   4 | 3: yes | skip | –
     *   200 | 199: no | start, 201 missing | 1
     *   1 | 0: no | start: 2 ✓ 3 ✓ 4 ✓ 5 ✗ | 4
     *   3, 2 | yes | skip | –
     */
    // Step 2 · the better way
    public static class LongestConsecutive {
        public int longestConsecutive(int[] nums) {
            Set<Integer> set = new HashSet<>();
            for (int n : nums) {
                set.add(n);
            }
            int best = 0;
            for (int n : set) {
                // only start counting at the FIRST number of a run
                if (!set.contains(n - 1)) {
                    int length = 1;
                    while (set.contains(n + length)) {
                        length++;
                    }
                    best = Math.max(best, length);
                }
            }
            return best;
        }
    }

    /*
     * Time: O(n) · Space: O(n)
     *
     * WATCH OUT
     * - Without the !set.contains(n - 1) check you restart the count from
     *   every number, and input like 1…100000 becomes O(n²).
     * - Loop over the set, not the array, so duplicates are only processed
     *   once.
     */

    // ====================================================================
    // 11. Subarray Sum Equals K
    // LeetCode 560 · Medium
    // Trick: prefix sums; count += times (sum − k) was seen  ·  O(n)
    // ====================================================================

    /**
     * QUESTION
     * Count the contiguous subarrays whose sum is exactly k. The numbers can
     * be negative.
     *
     * EXAMPLE
     *   [1, 2, 3, -2, 2], k = 3   ->  4      [1,2]  [3]  [2,3,-2]  [3,-2,2]
     *
     * STEP 1 · BRUTE FORCE
     * Try every start, then extend the end one step at a time, keeping a
     * running sum.
     *
     * Brute force cost: O(n²) time · O(1) space
     */
    // Step 1 · brute force
    public static class SubarraySumBrute {
        public int subarraySum(int[] nums, int k) {
            int count = 0;
            for (int start = 0; start < nums.length; start++) {
                int sum = 0;
                for (int end = start; end < nums.length; end++) {
                    sum += nums[end];                  // sum of start..end
                    if (sum == k) count++;
                }
            }
            return count;
        }
    }

    /**
     * STEP 2 · THE BETTER IDEA
     * Let sum be the prefix sum up to the current position. A subarray that
     * ends here sums to k exactly when some earlier prefix equals sum − k. So
     * keep a map of prefix sum → how many times you've seen it, and add that
     * count to the answer at every step.
     *
     * Start the map with {0: 1}. That's the empty prefix, so subarrays that
     * start at index 0 are counted.
     *
     * DRY RUN ([1, 2, 3, -2, 2], K = 3)
     *   n | sum | need sum − k | seen before? | count | map after
     *   1 | 1 | −2 | 0 | 0 | {0:1, 1:1}
     *   2 | 3 | 0 | 1 | 1 | +{3:1}
     *   3 | 6 | 3 | 1 | 2 | +{6:1}
     *   −2 | 4 | 1 | 1 | 3 | +{4:1}
     *   2 | 6 | 3 | 1 | 4 | {6:2}
     */
    // Step 2 · the better way
    public static class SubarraySum {
        public int subarraySum(int[] nums, int k) {
            Map<Integer, Integer> seen = new HashMap<>();  // prefix -> times
            seen.put(0, 1);                            // the empty prefix
            int sum = 0, count = 0;
            for (int n : nums) {
                sum += n;
                // a subarray ending here sums to k if (sum - k) was a prefix
                count += seen.getOrDefault(sum - k, 0);
                seen.put(sum, seen.getOrDefault(sum, 0) + 1);
            }
            return count;
        }
    }

    /*
     * Time: O(n) · Space: O(n)
     *
     * WATCH OUT
     * - A sliding window does not work here, because negative numbers break
     *   the "grow / shrink" logic.
     * - Look up sum − k before you store the current sum.
     */

    // ====================================================================
    // 12. Brick Wall
    // LeetCode 554 · Medium
    // Trick: count edge positions; answer = rows − most common edge  ·  O(total bricks)
    // ====================================================================

    /**
     * QUESTION
     * A wall has rows of bricks with different widths, but every row has the
     * same total width. Draw one vertical line from top to bottom that
     * crosses the fewest bricks. A line that runs along an edge between two
     * bricks doesn't cross them, and you can't draw it along the outer sides
     * of the wall.
     *
     * EXAMPLE
     *   [1,2,2,1]  [3,1,2]  [1,3,2]  [2,4]  [3,1,2]  [1,3,1,1]   ->  2
     *   (at x = 4, four of the six rows have an edge)
     *
     * STEP 1 · BRUTE FORCE
     * Try every x from 1 to width − 1, and for each row check whether x lands
     * exactly on an edge.
     *
     * Brute force cost: O(width × bricks) time. The width can be huge (bricks
     * can be 2³¹ wide), so this can be hopeless.
     */
    // Step 1 · brute force
    public static class BrickWallBrute {
        public int leastBricks(List<List<Integer>> wall) {
            int width = 0;
            for (int b : wall.get(0)) width += b;
            int best = wall.size();                    // worst case: cut all
            for (int x = 1; x < width; x++) {          // every possible line
                int crossed = 0;
                for (List<Integer> row : wall) {
                    int edge = 0;
                    boolean onEdge = false;
                    for (int b : row) {
                        edge += b;
                        if (edge == x) onEdge = true;
                        if (edge >= x) break;
                    }
                    if (!onEdge) crossed++;            // line cuts a brick
                }
                best = Math.min(best, crossed);
            }
            return best;
        }
    }

    /**
     * STEP 2 · THE BETTER IDEA
     * Flip the question: instead of minimising the bricks crossed, maximise
     * the edges you pass through. For each row, add up the brick widths to
     * get every edge position (skipping the last one, which is the wall's
     * side) and count them in a map. The answer is rows − (most common edge
     * count).
     */
    // Step 2 · the better way
    public static class BrickWall {
        public int leastBricks(List<List<Integer>> wall) {
            Map<Integer, Integer> edges = new HashMap<>();  // x -> rows
            int most = 0;
            for (List<Integer> row : wall) {
                int x = 0;
                for (int i = 0; i < row.size() - 1; i++) {  // skip right wall
                    x += row.get(i);
                    int c = edges.getOrDefault(x, 0) + 1;
                    edges.put(x, c);
                    most = Math.max(most, c);
                }
            }
            return wall.size() - most;                 // rows the line cuts
        }
    }

    /*
     * Time: O(total number of bricks) · Space: O(number of distinct edges)
     *
     * PATTERN
     * "Count positions where something happens, then pick the most common" is
     * the same map trick as finding the busiest minute in a log.
     */

    // ====================================================================
    // 13. Valid Sudoku  ★ Karat
    // LeetCode 36 · Medium
    // Trick: set of "d in row r / col c / box b" strings  ·  O(1)
    // ====================================================================

    /**
     * QUESTION
     * Check a partly filled 9×9 board. No row, column or 3×3 box may repeat a
     * digit. Empty cells are '.'. You do not need to check whether the board
     * can be solved.
     *
     * EXAMPLE
     *   '5' at (0,0) and '5' at (1,1)  ->  false   (same 3x3 box)
     *   '5' at (0,0) and '5' at (4,4)  ->  true    (different row, column and box)
     *
     * STEP 1 · BRUTE FORCE
     * Check each rule separately: every row and column in one pass, then
     * every 3×3 box in a second pass, each with a fresh boolean[10] seen-
     * array.
     *
     * Brute force cost: still O(81), so it's not slower, just more code. Step
     * 2 does all three checks in a single loop.
     */
    // Step 1 · brute force
    public static class ValidSudokuPasses {
        public boolean isValidSudoku(char[][] b) {
            // pass 1: every row i and every column i
            for (int i = 0; i < 9; i++) {
                boolean[] row = new boolean[10];
                boolean[] col = new boolean[10];
                for (int j = 0; j < 9; j++) {
                    if (b[i][j] != '.') {
                        int d = b[i][j] - '0';
                        if (row[d]) return false;
                        row[d] = true;
                    }
                    if (b[j][i] != '.') {
                        int d = b[j][i] - '0';
                        if (col[d]) return false;
                        col[d] = true;
                    }
                }
            }
            // pass 2: every 3x3 box, top-left corner at (br, bc)
            for (int br = 0; br < 9; br += 3) {
                for (int bc = 0; bc < 9; bc += 3) {
                    boolean[] seen = new boolean[10];
                    for (int r = br; r < br + 3; r++) {
                        for (int c = bc; c < bc + 3; c++) {
                            if (b[r][c] == '.') continue;
                            int d = b[r][c] - '0';
                            if (seen[d]) return false;
                            seen[d] = true;
                        }
                    }
                }
            }
            return true;
        }
    }

    /**
     * STEP 2 · THE BETTER IDEA
     * Use one HashSet of descriptive strings. For a digit d at (r, c), add "d
     * in row r", "d in col c" and "d in box b". If any add returns false,
     * that string was already there, so the board is invalid.
     *
     * WHICH BOX IS (R, C) IN?
     * box = (r / 3) * 3 + (c / 3). For example, (1, 4) → 0·3 + 1 = box 1.
     *   box numbers:   0 | 1 | 2
     *                  3 | 4 | 5
     *                  6 | 7 | 8
     */
    // Step 2 · the better way
    public static class ValidSudoku {
        public boolean isValidSudoku(char[][] board) {
            Set<String> seen = new HashSet<>();
            for (int r = 0; r < 9; r++) {
                for (int c = 0; c < 9; c++) {
                    char d = board[r][c];
                    if (d == '.') continue;             // empty cell
                    int box = (r / 3) * 3 + (c / 3);    // box number 0..8
                    // add() returns false if the text was already there
                    if (!seen.add(d + " in row " + r)
                            || !seen.add(d + " in col " + c)
                            || !seen.add(d + " in box " + box)) {
                        return false;
                    }
                }
            }
            return true;
        }
    }

    /*
     * Time: O(81) = O(1) · Space: O(1)
     *
     * WATCH OUT
     * - / on ints is integer division in Java (4 / 3 = 1), which is exactly
     *   what the box formula needs.
     * - d + " in row " + r works because char + String builds a String. d + r
     *   on its own would add the numbers instead.
     *
     * KARAT VERSION
     * Karat often asks a simpler cousin: "Check that every row and column of
     * an N×N matrix contains each number 1..N exactly once." That's LeetCode
     * 2133, solved in Part 3 — Grids.
     */

}
