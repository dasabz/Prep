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

    // ====================================================================
    // 2. Top K Frequent Elements
    // LeetCode 347 · Medium
    // Trick: count → sort keys by count desc → take k  ·  O(n log n)
    // ====================================================================

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

    // ====================================================================
    // 3. Custom Sort String
    // LeetCode 791 · Medium
    // Trick: count s; write letters in order's order, then the rest  ·  O(n)
    // ====================================================================

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

    // ====================================================================
    // 4. Top K Frequent Words
    // LeetCode 692 · Medium
    // Trick: count → size-k min-heap (worst on top) → reverse  ·  O(n log k)
    // ====================================================================

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

    // ====================================================================
    // 5. Sort Characters By Frequency
    // LeetCode 451 · Medium
    // Trick: same as Top K + StringBuilder  ·  O(n log n)
    // ====================================================================

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

    // ====================================================================
    // 6. Bulls and Cows
    // LeetCode 299 · Medium
    // Trick: bulls by position; cows = Σ min(leftover counts)  ·  O(n)
    // ====================================================================

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

    // ====================================================================
    // 7. Find Players With Zero or One Losses  ★ Karat
    // LeetCode 2225 · Medium
    // Trick: TreeMap player→losses; putIfAbsent for winners  ·  O(n log n)
    // ====================================================================

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

    // ====================================================================
    // 8. Subdomain Visit Count  ★ Karat
    // LeetCode 811 · Medium
    // Trick: add count to the domain and every suffix after '.'  ·  O(chars)
    // ====================================================================

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

    // ====================================================================
    // 9. Repeated DNA Sequences
    // LeetCode 187 · Medium
    // Trick: every 10-letter window into seen; second time → answer  ·  O(n)
    // ====================================================================

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

    // ====================================================================
    // 10. Longest Consecutive Sequence
    // LeetCode 128 · Medium
    // Trick: HashSet; start only where n−1 is missing  ·  O(n)
    // ====================================================================

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

    // ====================================================================
    // 11. Subarray Sum Equals K
    // LeetCode 560 · Medium
    // Trick: prefix sums; count += times (sum − k) was seen  ·  O(n)
    // ====================================================================

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

    // ====================================================================
    // 12. Brick Wall
    // LeetCode 554 · Medium
    // Trick: count edge positions; answer = rows − most common edge  ·  O(total bricks)
    // ====================================================================

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

    // ====================================================================
    // 13. Valid Sudoku  ★ Karat
    // LeetCode 36 · Medium
    // Trick: set of "d in row r / col c / box b" strings  ·  O(1)
    // ====================================================================

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

}
