package karat.intervals;

import java.util.*;

/**
 * Part 5 · Time Windows & Intervals · Medium (17 problems).
 *
 * Explanations, dry runs and pitfalls: pdfs/Karat-5b-Medium-Time-Windows-Intervals.pdf
 * Each problem: Step 1 = brute force, Step 2 = the better way.
 * Every example is checked in src/test/java/karat/intervals/IntervalsTests.java
 */
public final class IntervalsMedium {

    private IntervalsMedium() { }

    // ====================================================================
    // 1. Merge Intervals
    // LeetCode 56 · Medium
    // Trick: sort by start; overlap → stretch end with max  ·  O(n log n)
    // ====================================================================

    // Step 1 · brute force
    public static class MergeIntervalsBrute {
        public int[][] merge(int[][] intervals) {
            List<int[]> list = new ArrayList<>();
            for (int[] iv : intervals) list.add(iv.clone());
            boolean merged = true;
            while (merged) {                  // repeat until nothing merges
                merged = false;
                outer:
                for (int i = 0; i < list.size(); i++) {
                    for (int j = i + 1; j < list.size(); j++) {
                        int[] a = list.get(i), b = list.get(j);
                        if (a[0] <= b[1] && b[0] <= a[1]) {        // overlap
                            a[0] = Math.min(a[0], b[0]);
                            a[1] = Math.max(a[1], b[1]);
                            list.remove(j);
                            merged = true;
                            break outer;           // start the scan again
                        }
                    }
                }
            }
            list.sort((a, b) -> Integer.compare(a[0], b[0]));
            return list.toArray(new int[0][]);
        }
    }

    // Step 2 · the better way
    public static class MergeIntervals {
        public int[][] merge(int[][] intervals) {
            if (intervals.length == 0) return intervals;
            // 1) sort by start time
            Arrays.sort(intervals, (a, b) -> Integer.compare(a[0], b[0]));

            List<int[]> merged = new ArrayList<>();
            int[] current = intervals[0];             // interval being built
            merged.add(current);
            for (int[] next : intervals) {
                if (next[0] <= current[1]) {          // overlap: stretch
                    current[1] = Math.max(current[1], next[1]);
                } else {                              // gap: start a new one
                    current = next;
                    merged.add(current);
                }
            }
            return merged.toArray(new int[0][]);
        }
    }

    // ====================================================================
    // 2. Insert Interval
    // LeetCode 57 · Medium
    // Trick: 3 phases: before → absorb overlaps → after  ·  O(n)
    // ====================================================================

    // Step 1 · brute force
    public static class InsertIntervalMerge {
        public int[][] insert(int[][] intervals, int[] newInterval) {
            int[][] all = new int[intervals.length + 1][];
            for (int i = 0; i < intervals.length; i++) {
                all[i] = intervals[i].clone();
            }
            all[intervals.length] = newInterval.clone();
            return new MergeIntervals().merge(all);  // Merge Intervals
        }
    }

    // Step 2 · the better way
    public static class InsertInterval {
        public int[][] insert(int[][] intervals, int[] newInterval) {
            List<int[]> out = new ArrayList<>();
            int start = newInterval[0], end = newInterval[1];
            int i = 0, n = intervals.length;
            while (i < n && intervals[i][1] < start) {  // 1) entirely before
                out.add(intervals[i]);
                i++;
            }
            while (i < n && intervals[i][0] <= end) {   // 2) overlapping
                start = Math.min(start, intervals[i][0]);
                end = Math.max(end, intervals[i][1]);
                i++;
            }
            out.add(new int[] { start, end });
            while (i < n) {                             // 3) entirely after
                out.add(intervals[i]);
                i++;
            }
            return out.toArray(new int[0][]);
        }
    }

    // ====================================================================
    // 3. Non-overlapping Intervals
    // LeetCode 435 · greedy · Medium
    // Trick: sort by END; keep each interval that starts after the last kept end  ·  O(n log n)
    // ====================================================================

    // Step 2 · the better way
    public static class NonOverlapping {
        public int eraseOverlapIntervals(int[][] intervals) {
            int[][] iv = intervals.clone();
            Arrays.sort(iv, (a, b) -> Integer.compare(a[1], b[1]));  // by END
            int kept = 0;
            int lastEnd = Integer.MIN_VALUE;
            for (int[] x : iv) {
                if (x[0] >= lastEnd) {       // fits after the last kept
                    kept++;
                    lastEnd = x[1];
                }                               // else: remove x
            }
            return iv.length - kept;
        }
    }

    // ====================================================================
    // 4. Minimum Number of Arrows to Burst Balloons
    // LeetCode 452 · greedy · Medium
    // Trick: sort by END; shoot at the end of the first un-hit balloon  ·  O(n log n)
    // ====================================================================

    // Step 2 · the better way
    public static class MinArrows {
        public int findMinArrowShots(int[][] points) {
            int[][] p = points.clone();
            Arrays.sort(p, (a, b) -> Integer.compare(a[1], b[1]));   // by END
            int arrows = 0;
            long arrowAt = Long.MIN_VALUE;      // where the last arrow went
            for (int[] b : p) {
                if (b[0] > arrowAt) {           // last arrow misses this one
                    arrows++;
                    arrowAt = b[1];             // shoot at its END
                }
            }
            return arrows;
        }
    }

    // ====================================================================
    // 5. Interval List Intersections
    // LeetCode 986 · Medium
    // Trick: two pointers; overlap = [max(starts), min(ends)]; advance earlier end  ·  O(n + m)
    // ====================================================================

    // Step 1 · brute force
    public static class IntersectionsBrute {
        public int[][] intervalIntersection(int[][] a, int[][] b) {
            List<int[]> out = new ArrayList<>();
            for (int[] x : a) {
                for (int[] y : b) {             // every pair
                    int lo = Math.max(x[0], y[0]);
                    int hi = Math.min(x[1], y[1]);
                    if (lo <= hi) out.add(new int[] { lo, hi });
                }
            }
            out.sort((p, q) -> Integer.compare(p[0], q[0]));
            return out.toArray(new int[0][]);
        }
    }

    // Step 2 · the better way
    public static class Intersections {
        public int[][] intervalIntersection(int[][] a, int[][] b) {
            List<int[]> out = new ArrayList<>();
            int i = 0, j = 0;
            while (i < a.length && j < b.length) {
                int lo = Math.max(a[i][0], b[j][0]);   // later start
                int hi = Math.min(a[i][1], b[j][1]);   // earlier end
                if (lo <= hi) out.add(new int[] { lo, hi });   // they overlap
                if (a[i][1] < b[j][1]) {
                    i++;                  // a[i] ends first: done
                } else {
                    j++;
                }
            }
            return out.toArray(new int[0][]);
        }
    }

    // ====================================================================
    // 6. Meeting Fits & Free Slots (2 parts)  ★ Karat
    // A Karat-style question asked in parts · Medium
    // Trick: clash: s  m0; busyUntil sweep for gaps  ·  O(n log n)
    // ====================================================================

    // Step 1 · brute force
    public static class FreeSlotsMinutes {
        // Part 2, brute force: mark every busy minute of the day
        public List<int[]> freeSlots(int[][] meetings, int dayStart,
                                     int dayEnd) {
            boolean[] busy = new boolean[24 * 60];
            for (int[] m : meetings) {
                for (int t = toMin(m[0]); t < toMin(m[1]); t++) {
                    busy[t] = true;                 // one slot per minute
                }
            }
            List<int[]> free = new ArrayList<>();
            int t = toMin(dayStart), end = toMin(dayEnd);
            while (t < end) {
                if (busy[t]) {
                    t++;
                    continue;
                }
                int start = t;
                while (t < end && !busy[t]) t++;    // walk the free run
                free.add(new int[] { toHhmm(start), toHhmm(t) });
            }
            return free;
        }

        private int toMin(int hhmm) { return (hhmm / 100) * 60 + hhmm % 100; }

        private int toHhmm(int min) { return (min / 60) * 100 + min % 60; }
    }

    // Step 2 · the better way
    public static class KaratFreeTime {
        // Part 1: can a new meeting [start, end) fit with no clash?
        public boolean canSchedule(int[][] meetings, int start, int end) {
            for (int[] m : meetings) {
                // clash if it starts before m ends AND ends after m starts
                if (start < m[1] && end > m[0]) return false;
            }
            return true;
        }

        // Part 2: every free slot between dayStart and dayEnd
        public List<int[]> freeSlots(int[][] meetings, int dayStart,
                                     int dayEnd) {
            int[][] sorted = meetings.clone();
            Arrays.sort(sorted, (a, b) -> Integer.compare(a[0], b[0]));
            List<int[]> free = new ArrayList<>();
            int busyUntil = dayStart;          // latest end time seen so far
            for (int[] m : sorted) {
                if (m[0] > busyUntil) {
                    free.add(new int[] { busyUntil, m[0] });   // a gap
                }
                busyUntil = Math.max(busyUntil, m[1]);
            }
            if (busyUntil < dayEnd) {
                free.add(new int[] { busyUntil, dayEnd });     // after last
            }
            return free;
        }
    }

    // ====================================================================
    // 7. My Calendar I
    // LeetCode 729 · Medium
    // Trick: TreeMap: check floorKey's end and ceilingKey's start  ·  O(log n) per booking
    // ====================================================================

    // Step 1 · brute force
    public static class MyCalendarList {
        private List<int[]> bookings = new ArrayList<>();

        public boolean book(int start, int end) {
            for (int[] b : bookings) {                 // check every booking
                if (start < b[1] && end > b[0]) return false;   // overlap
            }
            bookings.add(new int[] { start, end });
            return true;
        }
    }

    // Step 2 · the better way
    public static class MyCalendar {
        // start -> end, kept sorted by start
        private TreeMap<Integer, Integer> cal = new TreeMap<>();

        public boolean book(int start, int end) {
            // the booking that starts at or just before us
            Integer before = cal.floorKey(start);
            if (before != null && cal.get(before) > start) return false;
            // the booking that starts at or just after us
            Integer after = cal.ceilingKey(start);
            if (after != null && after < end) return false;
            cal.put(start, end);
            return true;
        }
    }

    // ====================================================================
    // 8. Minimum Meeting Rooms  ★ Karat
    // LeetCode 253 (Premium) · a common Karat-style meeting follow-up · Medium
    // Trick: sort by start; min-heap of end times; reuse room if top ≤ start  ·  O(n log n)
    // ====================================================================

    // Step 1 · brute force
    public static class MinRoomsBrute {
        public int minMeetingRooms(int[][] meetings) {
            int best = 0;
            for (int[] m : meetings) {          // at the moment m starts...
                int running = 0;
                for (int[] o : meetings) {      // ...how many are going on?
                    if (o[0] <= m[0] && m[0] < o[1]) running++;
                }
                best = Math.max(best, running);
            }
            return best;
        }
    }

    // Step 2 · the better way
    public static class MinRooms {
        public int minMeetingRooms(int[][] meetings) {
            int[][] m = meetings.clone();
            Arrays.sort(m, (a, b) -> Integer.compare(a[0], b[0])); // start
            // end times of the rooms in use; earliest end on top
            PriorityQueue<Integer> ends = new PriorityQueue<>();
            for (int[] x : m) {
                if (!ends.isEmpty() && ends.peek() <= x[0]) {
                    ends.poll();            // earliest room is free: reuse
                }
                ends.add(x[1]);                 // this meeting holds a room
            }
            return ends.size();                 // rooms ever needed at once
        }
    }

    // ====================================================================
    // 9. Car Pooling
    // LeetCode 1094 · difference array · Medium
    // Trick: +people at pickup, −people at drop-off; running sum ≤ capacity  ·  O(n + max location)
    // ====================================================================

    // Step 1 · brute force
    public static class CarPoolingBrute {
        public boolean carPooling(int[][] trips, int capacity) {
            int[] load = new int[1001];     // people in the car per km
            for (int[] t : trips) {
                for (int km = t[1]; km < t[2]; km++) {
                    load[km] += t[0];           // every km of every trip
                }
            }
            for (int people : load) {
                if (people > capacity) return false;
            }
            return true;
        }
    }

    // Step 2 · the better way
    public static class CarPooling {
        public boolean carPooling(int[][] trips, int capacity) {
            int[] change = new int[1001];       // "difference array"
            for (int[] t : trips) {
                change[t[1]] += t[0];           // they get in here
                change[t[2]] -= t[0];           // they get out here
            }
            int load = 0;
            for (int c : change) {
                load += c;                      // running total = in the car
                if (load > capacity) return false;
            }
            return true;
        }
    }

    // ====================================================================
    // 10. Longest Substring Without Repeating Characters
    // LeetCode 3 · the classic sliding window · Medium
    // Trick: window; left = lastSeen + 1 only if inside window  ·  O(n)
    // ====================================================================

    // Step 1 · brute force
    public static class LongestUniqueBrute {
        public int lengthOfLongestSubstring(String s) {
            int best = 0;
            for (int start = 0; start < s.length(); start++) {
                Set<Character> seen = new HashSet<>();
                int end = start;
                while (end < s.length() && seen.add(s.charAt(end))) {
                    end++;                         // grow until a repeat
                }
                best = Math.max(best, end - start);
            }
            return best;
        }
    }

    // Step 2 · the better way
    public static class LongestUniqueSubstring {
        public int lengthOfLongestSubstring(String s) {
            Map<Character, Integer> lastSeen = new HashMap<>();
            int best = 0;
            int left = 0;                        // window is s[left..right]
            for (int right = 0; right < s.length(); right++) {
                char c = s.charAt(right);
                if (lastSeen.containsKey(c) && lastSeen.get(c) >= left) {
                    left = lastSeen.get(c) + 1;  // jump past the repeat
                }
                lastSeen.put(c, right);
                best = Math.max(best, right - left + 1);
            }
            return best;
        }
    }

    // ====================================================================
    // 11. Minimum Size Subarray Sum
    // LeetCode 209 · variable-size window · Medium
    // Trick: grow right; while sum ≥ target, record and shrink left  ·  O(n)
    // ====================================================================

    // Step 1 · brute force
    public static class MinSubArrayBrute {
        public int minSubArrayLen(int target, int[] nums) {
            int best = Integer.MAX_VALUE;
            for (int i = 0; i < nums.length; i++) {
                int sum = 0;
                for (int j = i; j < nums.length; j++) {
                    sum += nums[j];
                    if (sum >= target) {        // shortest from i: stop here
                        best = Math.min(best, j - i + 1);
                        break;
                    }
                }
            }
            return best == Integer.MAX_VALUE ? 0 : best;
        }
    }

    // Step 2 · the better way
    public static class MinSubArray {
        public int minSubArrayLen(int target, int[] nums) {
            int best = Integer.MAX_VALUE, sum = 0, left = 0;
            for (int right = 0; right < nums.length; right++) {
                sum += nums[right];             // grow on the right
                while (sum >= target) {         // shrink while still enough
                    best = Math.min(best, right - left + 1);
                    sum -= nums[left];
                    left++;
                }
            }
            return best == Integer.MAX_VALUE ? 0 : best;
        }
    }

    // ====================================================================
    // 12. Max Consecutive Ones III
    // LeetCode 1004 · Medium
    // Trick: window may hold at most k zeros; shrink when it holds more  ·  O(n)
    // ====================================================================

    // Step 1 · brute force
    public static class LongestOnesBrute {
        public int longestOnes(int[] nums, int k) {
            int best = 0;
            for (int i = 0; i < nums.length; i++) {
                int zeros = 0;
                for (int j = i; j < nums.length; j++) {
                    if (nums[j] == 0) zeros++;
                    if (zeros > k) break;       // would need too many flips
                    best = Math.max(best, j - i + 1);
                }
            }
            return best;
        }
    }

    // Step 2 · the better way
    public static class LongestOnes {
        public int longestOnes(int[] nums, int k) {
            int best = 0, zeros = 0, left = 0;
            for (int right = 0; right < nums.length; right++) {
                if (nums[right] == 0) zeros++;
                while (zeros > k) {             // too many flips: shrink
                    if (nums[left] == 0) zeros--;
                    left++;
                }
                best = Math.max(best, right - left + 1);
            }
            return best;
        }
    }

    // ====================================================================
    // 13. Permutation in String
    // LeetCode 567 · Medium
    // Trick: fixed window of len(s1) with letter counts; compare arrays  ·  O(26 × n)
    // ====================================================================

    // Step 1 · brute force
    public static class PermutationInStringBrute {
        public boolean checkInclusion(String s1, String s2) {
            char[] target = s1.toCharArray();
            Arrays.sort(target);
            for (int i = 0; i + s1.length() <= s2.length(); i++) {
                String part = s2.substring(i, i + s1.length());
                char[] window = part.toCharArray();
                Arrays.sort(window);                  // sort EVERY window
                if (Arrays.equals(window, target)) return true;
            }
            return false;
        }
    }

    // Step 2 · the better way
    public static class PermutationInString {
        public boolean checkInclusion(String s1, String s2) {
            int m = s1.length();
            if (m > s2.length()) return false;
            int[] need = new int[26], have = new int[26];
            for (char c : s1.toCharArray()) need[c - 'a']++;
            for (int i = 0; i < s2.length(); i++) {
                have[s2.charAt(i) - 'a']++;               // char enters
                if (i >= m) have[s2.charAt(i - m) - 'a']--;   // char leaves
                if (Arrays.equals(need, have)) return true;   // 26 compares
            }
            return false;
        }
    }

    // ====================================================================
    // 14. Find All Anagrams in a String
    // LeetCode 438 · Medium
    // Trick: same window as Permutation in String; record start i − m + 1  ·  O(26 × n)
    // ====================================================================

    // Step 1 · brute force
    public static class FindAnagramsBrute {
        public List<Integer> findAnagrams(String s, String p) {
            List<Integer> out = new ArrayList<>();
            int m = p.length();
            for (int i = 0; i + m <= s.length(); i++) {
                int[] count = new int[26];            // recount every window
                for (int j = 0; j < m; j++) {
                    count[s.charAt(i + j) - 'a']++;
                    count[p.charAt(j) - 'a']--;
                }
                boolean ok = true;
                for (int c : count) {
                    if (c != 0) ok = false;
                }
                if (ok) out.add(i);
            }
            return out;
        }
    }

    // Step 2 · the better way
    public static class FindAnagrams {
        public List<Integer> findAnagrams(String s, String p) {
            List<Integer> out = new ArrayList<>();
            int m = p.length();
            int[] need = new int[26], have = new int[26];
            for (char c : p.toCharArray()) need[c - 'a']++;
            for (int i = 0; i < s.length(); i++) {
                have[s.charAt(i) - 'a']++;                // char enters
                if (i >= m) have[s.charAt(i - m) - 'a']--;    // char leaves
                if (i >= m - 1 && Arrays.equals(need, have)) {
                    out.add(i - m + 1);                   // window start
                }
            }
            return out;
        }
    }

    // ====================================================================
    // 15. Alert Using Same Key-Card 3+ Times in One Hour  ★ Karat
    // LeetCode 1604 · Medium
    // Trick: group → sort → t[i] − t[i−2] <= 60  ·  O(n log n)
    // ====================================================================

    // Step 1 · brute force
    public static class KeyCardBrute {
        public List<String> alertNames(String[] keyName, String[] keyTime) {
            Map<String, List<Integer>> times = new HashMap<>();
            for (int i = 0; i < keyName.length; i++) {
                times.putIfAbsent(keyName[i], new ArrayList<>());
                String t = keyTime[i];
                int m = Integer.parseInt(t.substring(0, 2)) * 60
                      + Integer.parseInt(t.substring(3, 5));
                times.get(keyName[i]).add(m);
            }
            List<String> result = new ArrayList<>();
            for (String name : times.keySet()) {
                List<Integer> t = times.get(name);
                // for EVERY use, count uses in the hour starting there
                for (int start : t) {
                    int count = 0;
                    for (int other : t) {
                        if (other >= start && other - start <= 60) count++;
                    }
                    if (count >= 3) {
                        result.add(name);
                        break;
                    }
                }
            }
            Collections.sort(result);
            return result;
        }
    }

    // Step 2 · the better way
    public static class KeyCardAlert {
        public List<String> alertNames(String[] keyName, String[] keyTime) {
            // name -> every time they used the card, in minutes
            Map<String, List<Integer>> times = new HashMap<>();
            for (int i = 0; i < keyName.length; i++) {
                times.putIfAbsent(keyName[i], new ArrayList<>());
                times.get(keyName[i]).add(toMinutes(keyTime[i]));
            }
            List<String> result = new ArrayList<>();
            for (String name : times.keySet()) {
                List<Integer> t = times.get(name);
                Collections.sort(t);
                // any 3 uses in a row that fit inside 60 minutes?
                for (int i = 2; i < t.size(); i++) {
                    if (t.get(i) - t.get(i - 2) <= 60) {
                        result.add(name);
                        break;
                    }
                }
            }
            Collections.sort(result);
            return result;
        }

        private int toMinutes(String hhmm) {        // "09:45" -> 585
            int h = Integer.parseInt(hhmm.substring(0, 2));
            int m = Integer.parseInt(hhmm.substring(3, 5));
            return h * 60 + m;
        }
    }

    // ====================================================================
    // 16. High-Access Employees  ★ Karat
    // LeetCode 2933 · Medium
    // Trick: same as 4, "HHMM" and strictly < 60  ·  O(n log n)
    // ====================================================================

    // Step 2 · the better way
    public static class HighAccessEmployees {
        public List<String> findHighAccessEmployees(
                List<List<String>> access) {
            Map<String, List<Integer>> times = new HashMap<>();
            for (List<String> a : access) {
                String name = a.get(0), hhmm = a.get(1);       // "0549"
                int minutes = Integer.parseInt(hhmm.substring(0, 2)) * 60
                            + Integer.parseInt(hhmm.substring(2));
                times.putIfAbsent(name, new ArrayList<>());
                times.get(name).add(minutes);
            }
            List<String> result = new ArrayList<>();
            for (String name : times.keySet()) {
                List<Integer> t = times.get(name);
                Collections.sort(t);
                for (int i = 2; i < t.size(); i++) {
                    if (t.get(i) - t.get(i - 2) < 60) {      // STRICTLY < 60
                        result.add(name);
                        break;
                    }
                }
            }
            return result;
        }
    }

    // ====================================================================
    // 17. Badge Records (2 parts)  ★ Karat
    // A Karat question asked in parts · Medium
    // Trick: P1: inside set + leftovers; P2: = Key-Card Alert + collect burst  ·  O(n log n)
    // ====================================================================

    // Step 2 · the better way
    public static class KaratBadgeRecords {
        // returns [entered-without-exiting, exited-without-entering]
        public List<List<String>> mismatches(String[][] records) {
            Set<String> inside = new HashSet<>();
            Set<String> badEnter = new TreeSet<>();   // TreeSet = sorted
            Set<String> badExit = new TreeSet<>();
            for (String[] r : records) {
                String name = r[0], action = r[1];
                if (action.equals("enter")) {
                    if (inside.contains(name)) {
                        badEnter.add(name);           // entered twice
                    }
                    inside.add(name);
                } else {                              // "exit"
                    if (!inside.contains(name)) {
                        badExit.add(name);            // exit with no enter
                    }
                    inside.remove(name);
                }
            }
            badEnter.addAll(inside);       // still inside at end of the day
            return Arrays.asList(new ArrayList<>(badEnter),
                                 new ArrayList<>(badExit));
        }
    }

    // Step 2 · the better way
    public static class KaratBadgeTimes {
        // Part 2: who badged 3+ times within one hour, and at what times?
        // times are ints like 830 (08:30) or 1415 (14:15)
        public Map<String, List<Integer>> badgedOften(String[][] records) {
            Map<String, List<Integer>> times = new HashMap<>();
            for (String[] r : records) {
                times.putIfAbsent(r[0], new ArrayList<>());
                times.get(r[0]).add(Integer.parseInt(r[1]));
            }
            Map<String, List<Integer>> result = new TreeMap<>();
            for (String name : times.keySet()) {
                List<Integer> t = times.get(name);
                Collections.sort(t);
                for (int i = 0; i + 2 < t.size(); i++) {
                    int start = minutes(t.get(i));
                    if (minutes(t.get(i + 2)) - start <= 60) {
                        // collect every badge time within an hour of t[i]
                        List<Integer> burst = new ArrayList<>();
                        for (int j = i; j < t.size(); j++) {
                            if (minutes(t.get(j)) - start > 60) break;
                            burst.add(t.get(j));
                        }
                        result.put(name, burst);
                        break;                       // first burst only
                    }
                }
            }
            return result;
        }

        private int minutes(int hhmm) {             // 1415 -> 14*60 + 15
            return (hhmm / 100) * 60 + hhmm % 100;
        }
    }

}
