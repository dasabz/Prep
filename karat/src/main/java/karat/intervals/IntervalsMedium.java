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

    /**
     * QUESTION
     * Merge all the overlapping intervals and return the result.
     *
     * EXAMPLE
     *   [[8,10], [1,3], [2,6], [15,18]]   ->  [[1,6], [8,10], [15,18]]
     *   [[1,4], [4,5]]                    ->  [[1,5]]     (touching counts here)
     *
     * STEP 1 · BRUTE FORCE
     * Keep merging any two overlapping intervals until a full scan finds no
     * overlap. No sorting needed, just repeated pair checks.
     *
     * Brute force cost: O(n³) time in the worst case (n merges × n² pair
     * checks). Sorting first (Step 2) lets one left-to-right pass do
     * everything.
     */
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

    /**
     * STEP 2 · THE BETTER IDEA
     * Sort by start. Keep a current interval that you're building. For each
     * next interval: if it starts on or before current ends, it overlaps, so
     * stretch current's end to the larger of the two ends. Otherwise there's
     * a gap: start a new current.
     *
     * DRY RUN
     *   next | current | overlap? | merged list
     *   [1,3] | [1,3] | itself | [1,3]
     *   [2,6] | [1,3] | 2 ≤ 3 → yes, stretch | [1,6]
     *   [8,10] | [1,6] | 8 > 6 → no, new | [1,6] [8,10]
     *   [15,18] | [8,10] | no, new | [1,6] [8,10] [15,18]
     *
     *   1 2 3 4 5 6 7 8 9 10      15 16 17 18
     *   [---]
     *     [-------]
     *                 [----]
     *                             [--------]
     *   [---------]   [----]      [--------]     <- result
     */
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

    /*
     * Time: O(n log n) · Space: O(n)
     *
     * WATCH OUT
     * - Use Math.max for the end. [1,10] followed by [2,3] must stay [1,10],
     *   not shrink to [1,3].
     * - current is the same array object that's stored in merged, so changing
     *   current[1] updates the list too. No need to put it back.
     * - Integer.compare(a[0], b[0]) is safer than a[0] - b[0], which can
     *   overflow with very large values.
     */

    // ====================================================================
    // 2. Insert Interval
    // LeetCode 57 · Medium
    // Trick: 3 phases: before → absorb overlaps → after  ·  O(n)
    // ====================================================================

    /**
     * QUESTION
     * Given sorted, non-overlapping intervals, insert a new interval and
     * merge wherever needed. Keep the result sorted.
     *
     * EXAMPLE
     *   [[1,2],[3,5],[6,7],[8,10],[12,16]] + [4,8]   ->  [[1,2],[3,10],[12,16]]
     *
     * STEP 1 · BRUTE FORCE
     * Add the new interval to the list and run Merge Intervals (sort plus one
     * pass).
     *
     * Brute force cost: O(n log n), because of the sort. This throws away the
     * fact that the input is already sorted.
     */
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

    /**
     * STEP 2 · THE BETTER IDEA
     * The list is already sorted, so one pass in three phases is enough:
     *
     * 1. Copy every interval that ends before the new one starts.
     * 2. Absorb every interval that overlaps the new one, stretching start
     *    and end.
     * 3. Add the merged interval, then copy everything left.
     *
     * DRY RUN (NEW = [4, 8])
     *   phase 1: [1,2] ends before 4 -> copy
     *   phase 2: [3,5] overlaps -> [3,8];  [6,7] -> [3,8];  [8,10] -> [3,10]
     *            [12,16] starts after 10 -> stop absorbing; add [3,10]
     *   phase 3: copy [12,16]
     */
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

    /*
     * Time: O(n) · Space: O(n) for the output
     *
     * WATCH OUT
     * - Overlap here includes touching: [1,3] and [3,5] merge, which is why
     *   phase 2 uses <=.
     */

    // ====================================================================
    // 3. Non-overlapping Intervals
    // LeetCode 435 · greedy · Medium
    // Trick: sort by END; keep each interval that starts after the last kept end  ·  O(n log n)
    // ====================================================================

    /**
     * QUESTION
     * Remove the fewest intervals so that the rest don't overlap. Touching
     * ends ([1,2] and [2,3]) is fine.
     *
     * EXAMPLE
     *   [[1,2], [2,3], [3,4], [1,3]]   ->  1      (remove [1,3])
     *   [[1,2], [1,2], [1,2]]          ->  2
     *
     * STEP 1 · BRUTE FORCE
     * Try every subset of intervals to keep, check whether it's overlap-free,
     * and keep the biggest one. That's 2ⁿ subsets, which is exponential.
     *
     * STEP 2 · THE IDEA
     * Turn it around: keep as many as possible. Always keep the interval that
     * ends earliest, because it leaves the most room for the rest. So sort by
     * end time, keep an interval whenever it starts at or after the last kept
     * end, and drop the others. The answer is n − kept.
     *
     * DRY RUN
     *   sorted by end: [1,2] [2,3] [1,3] [3,4]
     *   [1,2] keep (end 2)   [2,3] 2 >= 2 keep (end 3)
     *   [1,3] 1 < 3 drop      [3,4] keep         kept 3 of 4  ->  remove 1
     */
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

    /*
     * Time: O(n log n) · Space: O(n) for the sorted copy
     *
     * WATCH OUT
     * - Sort by end, not start. Sorting by start keeps a long early interval
     *   that blocks many short ones.
     */

    // ====================================================================
    // 4. Minimum Number of Arrows to Burst Balloons
    // LeetCode 452 · greedy · Medium
    // Trick: sort by END; shoot at the end of the first un-hit balloon  ·  O(n log n)
    // ====================================================================

    /**
     * QUESTION
     * Each balloon covers [start, end] on a line. An arrow shot at x bursts
     * every balloon with start ≤ x ≤ end. What's the fewest arrows that burst
     * them all?
     *
     * EXAMPLE
     *   [[10,16], [2,8], [1,6], [7,12]]   ->  2     (shoot at 6 and at 12)
     *
     * STEP 1 · BRUTE FORCE
     * Try arrow positions at every balloon end and search over the
     * combinations. That's exponential, and the greedy in Step 2 is both
     * simpler and correct.
     *
     * STEP 2 · THE IDEA
     * Sort by end. Shoot the first arrow at the end of the first balloon:
     * that's as far right as it can go while still hitting it, so it catches
     * as many later balloons as possible. Skip every balloon that arrow
     * already hits (start ≤ arrow), and shoot again at the end of the next
     * one it misses.
     *
     * DRY RUN
     *   sorted by end: [1,6] [2,8] [7,12] [10,16]
     *   arrow at 6: hits [1,6], [2,8] (2 <= 6)
     *   [7,12]: 7 > 6 -> new arrow at 12: also hits [10,16]      ->  2 arrows
     */
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

    /*
     * Time: O(n log n) · Space: O(n)
     *
     * WATCH OUT
     * - Store the arrow position in a long (starting at Long.MIN_VALUE), and
     *   compare with Integer.compare. Values can be the extreme ints, and a -
     *   b would overflow.
     */

    // ====================================================================
    // 5. Interval List Intersections
    // LeetCode 986 · Medium
    // Trick: two pointers; overlap = [max(starts), min(ends)]; advance earlier end  ·  O(n + m)
    // ====================================================================

    /**
     * QUESTION
     * Two lists of closed intervals, each sorted and non-overlapping on its
     * own. Return every overlap between the two lists.
     *
     * EXAMPLE
     *   A = [[0,2],[5,10],[13,23],[24,25]]
     *   B = [[1,5],[8,12],[15,24],[25,26]]
     *   ->  [[1,2],[5,5],[8,10],[15,23],[24,24],[25,25]]
     *
     * STEP 1 · BRUTE FORCE
     * Compare every interval in A with every interval in B.
     *
     * Brute force cost: O(n × m) time
     */
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

    /**
     * STEP 2 · THE BETTER IDEA
     * Use two pointers, one per list. The overlap of A[i] and B[j] is
     * [max(starts), min(ends)], and it's real only if lo ≤ hi. Then move
     * forward in whichever list's interval ends first: it can't overlap
     * anything else in the other list.
     */
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

    /*
     * Time: O(n + m) · Space: O(1) besides the output
     *
     * KARAT ANGLE
     * "When are both people free?" is this problem: run it on two people's
     * free-slot lists from Meeting Fits & Free Slots.
     */

    // ====================================================================
    // 6. Meeting Fits & Free Slots (2 parts)  ★ Karat
    // A Karat-style question asked in parts · Medium
    // Trick: clash: s  m0; busyUntil sweep for gaps  ·  O(n log n)
    // ====================================================================

    /**
     * QUESTION
     * Meetings are [start, end], with times like 900 (09:00) or 1530 (15:30).
     *
     * - Part 1: can a new meeting [start, end] be added without clashing?
     *   Touching is fine: a meeting ending at 1000 doesn't clash with one
     *   starting at 1000.
     * - Part 2: return every free slot in the day [dayStart, dayEnd].
     *
     * EXAMPLE
     *   meetings = [[1030,1200], [900,1000], [1500,1600], [1130,1230]]
     *
     *   canSchedule(1000, 1030)  ->  true     (fits exactly in a gap)
     *   canSchedule(1145, 1300)  ->  false    (clashes with 1130-1230)
     *   freeSlots(800, 1800)     ->  [800,900] [1000,1030] [1230,1500] [1600,1800]
     *
     * STEP 1 · BRUTE FORCE
     * Part 2 brute force: make a boolean[1440] (one slot per minute of the
     * day), mark every minute each meeting covers as busy, then walk the day
     * collecting the runs of free minutes. It's very easy to get right.
     *
     * Brute force cost: O(minutes in the day + total meeting minutes). Fine
     * for one day, but it breaks for second-level times or multi-year
     * calendars. Step 2 depends only on the number of meetings.
     */
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

    /**
     * STEP 2 · THE BETTER IDEA
     * Part 1: a new meeting clashes with m if it starts before m ends and
     * ends after m starts. Check it against every meeting.
     *
     * Part 2: sort by start. Keep busyUntil, the latest end time so far,
     * starting at dayStart. If the next meeting starts after busyUntil, the
     * gap [busyUntil, start] is free. Then set busyUntil = max(busyUntil,
     * end). When the loop ends, [busyUntil, dayEnd] is free if anything is
     * left.
     *
     * DRY RUN, PART 2
     *   meeting (sorted) | busyUntil before | free gap? | busyUntil after
     *   [900, 1000] | 800 | 800–900 ✓ | 1000
     *   [1030, 1200] | 1000 | 1000–1030 ✓ | 1200
     *   [1130, 1230] | 1200 | no (overlap) | 1230
     *   [1500, 1600] | 1230 | 1230–1500 ✓ | 1600
     *   end of day | 1600 | 1600–1800 ✓ |
     */
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

    /*
     * Time: Part 1 O(n), Part 2 O(n log n) · Space: O(n)
     *
     * WATCH OUT
     * - Comparing hhmm numbers is fine for order (900 < 1030), so sorting and
     *   the clash check work without converting.
     * - For durations ("only gaps of at least 30 minutes"), convert to
     *   minutes first.
     * - The code sorts a copy (meetings.clone()) so the caller's array isn't
     *   changed.
     *
     * KARAT FOLLOW-UPS
     * - "Only gaps of at least X minutes": filter the gaps by their length in
     *   minutes.
     * - "Free time common to several people": put everyone's meetings into
     *   one list and run Part 2 unchanged.
     */

    // ====================================================================
    // 7. My Calendar I
    // LeetCode 729 · Medium
    // Trick: TreeMap: check floorKey's end and ceilingKey's start  ·  O(log n) per booking
    // ====================================================================

    /**
     * QUESTION
     * Build a calendar. book(start, end) (with end exclusive) succeeds and
     * returns true only if it doesn't overlap any existing booking.
     *
     * EXAMPLE
     *   book(10, 20) -> true
     *   book(15, 25) -> false   (overlaps 10-20)
     *   book(20, 30) -> true    (touching is fine)
     *
     * STEP 1 · BRUTE FORCE
     * Keep a list of bookings and check the new one against every booking.
     *
     * Brute force cost: O(n) per booking, O(n²) in total. Honestly the
     * expected first answer.
     */
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

    /**
     * STEP 2 · THE BETTER IDEA
     * Keep the bookings in a TreeMap sorted by start. A new booking can only
     * clash with its two neighbours:
     *
     * - the booking that starts at or before it (floorKey): it clashes if
     *   that one ends after our start;
     * - the booking that starts at or after it (ceilingKey): it clashes if it
     *   starts before our end.
     */
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

    /*
     * Time: O(log n) per booking · Space: O(n)
     *
     * TREEMAP TOOLS WORTH KNOWING
     * floorKey(x) = largest key ≤ x · ceilingKey(x) = smallest key ≥ x ·
     * lowerKey/higherKey = strictly less / greater. They're handy in every
     * "sorted bookings" question.
     */

    // ====================================================================
    // 8. Minimum Meeting Rooms  ★ Karat
    // LeetCode 253 (Premium) · a common Karat-style meeting follow-up · Medium
    // Trick: sort by start; min-heap of end times; reuse room if top ≤ start  ·  O(n log n)
    // ====================================================================

    /**
     * QUESTION
     * Given meeting times, what's the minimum number of rooms so that every
     * meeting gets a room? A meeting ending at 10:30 frees the room for one
     * starting at 10:30.
     *
     * EXAMPLE
     *   [900,1030] [1000,1100] [1030,1200] [1100,1130] [1015,1045]   ->  3
     *   (at 10:15, three meetings are running at once)
     *
     * STEP 1 · BRUTE FORCE
     * The busiest moment is always when some meeting starts. For each
     * meeting's start time, count how many meetings are running then, and
     * take the maximum.
     *
     * Brute force cost: O(n²) time. It's correct and easy to explain, so
     * start here.
     */
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

    /**
     * STEP 2 · THE BETTER IDEA
     * Sort by start time. Keep a min-heap of end times, one entry per room in
     * use. For each meeting, if the earliest-ending room is free (top ≤
     * start), pop it and reuse it; either way, push this meeting's end. The
     * heap's size at the end is the number of rooms needed.
     *
     * DRY RUN
     *   900-1030  heap [1030]                    1 room
     *   1000-1100 1030 > 1000 -> new room        [1030, 1100]
     *   1015-1045 1030 > 1015 -> new room        [1030, 1045, 1100]   3 rooms
     *   1030-1200 1030 <= 1030 -> reuse          [1045, 1100, 1200]
     *   1100-1130 1045 <= 1100 -> reuse          [1100, 1130, 1200]   -> 3
     */
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

    /*
     * Time: O(n log n) · Space: O(n)
     *
     * ANOTHER WAY TO SAY IT
     * Split the meetings into a sorted list of starts and a sorted list of
     * ends, then sweep: +1 at each start, −1 at each end, and take the
     * highest running total. It's the same idea as Car Pooling.
     */

    // ====================================================================
    // 9. Car Pooling
    // LeetCode 1094 · difference array · Medium
    // Trick: +people at pickup, −people at drop-off; running sum ≤ capacity  ·  O(n + max location)
    // ====================================================================

    /**
     * QUESTION
     * Each trip is [people, from, to] along a straight road (locations
     * 0..1000). The car only drives forward. Can it do every trip without
     * ever carrying more than capacity people?
     *
     * EXAMPLE
     *   trips [[2,1,5],[3,3,7]], capacity 4  ->  false   (between 3 and 5: 2 + 3 = 5)
     *   same trips, capacity 5               ->  true
     *
     * STEP 1 · BRUTE FORCE
     * For each trip, add its passengers to every kilometre it covers, then
     * check the busiest kilometre.
     *
     * Brute force cost: O(n × trip length). Fine here, but it gets slow if
     * the road is long.
     */
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

    /**
     * STEP 2 · THE BETTER IDEA
     * Only record where the load changes: +people at the pickup and −people
     * at the drop-off. Walking along the road and keeping a running total
     * gives the number of people in the car at every point. This is called a
     * difference array.
     *
     * DRY RUN
     *   change[1] += 2   change[5] -= 2   change[3] += 3   change[7] -= 3
     *   km:   0  1  2  3  4  5  6  7
     *   load: 0  2  2  5  5  3  3  0     ->  5 > 4  ->  false
     */
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

    /*
     * Time: O(n + 1001) · Space: O(1001)
     *
     * KARAT ANGLE
     * "Maximum people in the building / servers busy / overlapping sessions
     * at once" is all this +1 / −1 sweep.
     */

    // ====================================================================
    // 10. Longest Substring Without Repeating Characters
    // LeetCode 3 · the classic sliding window · Medium
    // Trick: window; left = lastSeen + 1 only if inside window  ·  O(n)
    // ====================================================================

    /**
     * QUESTION
     * Find the length of the longest run of characters (contiguous) in which
     * no character repeats.
     *
     * EXAMPLE
     *   "abcabcbb"  ->  3    ("abc")
     *   "pwwkew"    ->  3    ("wke")
     *   "abba"      ->  2    ("ab" or "ba")
     *
     * STEP 1 · BRUTE FORCE
     * For every starting position, grow the substring with a HashSet until a
     * character repeats, and keep the longest length.
     *
     * Brute force cost: O(n²) time (O(n × alphabet) at worst). Step 2 never
     * moves left backwards, so each character is handled once.
     */
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

    /**
     * STEP 2 · THE BETTER IDEA
     * Use a sliding window [left..right]. Move right one character at a time
     * and keep lastSeen[c], the last index where c appeared. If c was already
     * seen inside the current window (lastSeen[c] >= left), jump left to just
     * after that old position. The window now has no repeats, so record its
     * length.
     *
     * DRY RUN ("PWWKEW")
     *   right | char | seen inside window? | left | window | best
     *   0 | p | no | 0 | p | 1
     *   1 | w | no | 0 | pw | 2
     *   2 | w | yes, at 1 | 2 | w | 2
     *   3 | k | no | 2 | wk | 2
     *   4 | e | no | 2 | wke | 3
     *   5 | w | yes, at 2 | 3 | kew | 3
     */
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

    /*
     * Time: O(n) · Space: O(number of distinct characters)
     *
     * WATCH OUT
     * - The >= left check matters. In "abba", when you reach the last a, its
     *   old position 0 is before left (which is 2), so left must not move
     *   backwards.
     *
     * WHY IT'S IN THIS PART
     * It's the same window move as the badge-time questions: grow on the
     * right, shrink from the left when a rule breaks. Learn it once here on
     * strings.
     */

    // ====================================================================
    // 11. Minimum Size Subarray Sum
    // LeetCode 209 · variable-size window · Medium
    // Trick: grow right; while sum ≥ target, record and shrink left  ·  O(n)
    // ====================================================================

    /**
     * QUESTION
     * The numbers are all positive. Find the length of the shortest
     * contiguous block whose sum is at least target, or 0 if there isn't one.
     *
     * EXAMPLE
     *   target 7, [2, 3, 1, 2, 4, 3]  ->  2     ([4, 3])
     *
     * STEP 1 · BRUTE FORCE
     * For every start, extend to the right until the sum reaches the target,
     * then stop (that's the shortest block from this start).
     *
     * Brute force cost: O(n²) time
     */
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

    /**
     * STEP 2 · THE BETTER IDEA
     * Use a sliding window. Move right forward, adding numbers. As soon as
     * the sum is big enough, record the length, then shrink from the left
     * while it's still big enough, recording each shorter length. Because all
     * numbers are positive, shrinking always lowers the sum and growing
     * always raises it, which is why the window works.
     *
     * DRY RUN
     *   add 2,3,1,2 -> sum 8 >= 7: len 4, drop 2 -> 6
     *   add 4 -> 10: len 4, drop 3 -> 7: len 3, drop 1 -> 6
     *   add 3 -> 9: len 3, drop 2 -> 7: len 2, drop 4 -> 3    ->  best 2
     */
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

    /*
     * Time: O(n), since each element enters and leaves once · Space: O(1)
     *
     * WATCH OUT
     * - With negative numbers this window logic breaks. That's Subarray Sum
     *   Equals K (Part 1), which uses prefix sums instead.
     */

    // ====================================================================
    // 12. Max Consecutive Ones III
    // LeetCode 1004 · Medium
    // Trick: window may hold at most k zeros; shrink when it holds more  ·  O(n)
    // ====================================================================

    /**
     * QUESTION
     * In an array of 0s and 1s, you may flip at most k zeros to ones. What's
     * the longest run of 1s you can get?
     *
     * EXAMPLE
     *   [1,1,1,0,0,0,1,1,1,1,0], k = 2   ->  6      (flip the last two 0s)
     *
     * STEP 1 · BRUTE FORCE
     * For every start, extend to the right while the window contains at most
     * k zeros.
     *
     * Brute force cost: O(n²) time
     */
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

    /**
     * STEP 2 · THE BETTER IDEA
     * Rephrase it: the answer is the longest window containing at most k
     * zeros. Grow right and count the zeros. When the count goes over k, move
     * left forward until it's back to k (subtracting a zero whenever one
     * leaves). Record the window length every step.
     */
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

    /*
     * Time: O(n) · Space: O(1)
     *
     * THE GENERAL RULE
     * "Longest window where some count stays within a limit" is always: grow
     * right, and while (rule broken) shrink left. You'll see it in Longest
     * Substring Without Repeating Characters too.
     */

    // ====================================================================
    // 13. Permutation in String
    // LeetCode 567 · Medium
    // Trick: fixed window of len(s1) with letter counts; compare arrays  ·  O(26 × n)
    // ====================================================================

    /**
     * QUESTION
     * Does s2 contain some rearrangement of s1 as a contiguous block?
     *
     * EXAMPLE
     *   s1 = "ab", s2 = "eidbaooo"   ->  true     ("ba")
     *   s1 = "ab", s2 = "eidboaoo"   ->  false
     *
     * STEP 1 · BRUTE FORCE
     * For every window of length |s1| in s2, sort it and compare it with
     * sorted s1.
     *
     * Brute force cost: O(n × m log m) time
     */
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

    /**
     * STEP 2 · THE BETTER IDEA
     * A rearrangement means the same letter counts. Slide a window of length
     * m over s2, keeping a count array have: add the letter coming in,
     * subtract the letter going out. Whenever have equals need (the counts of
     * s1), you've found one.
     */
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

    /*
     * Time: O(26 × n) · Space: O(1)
     *
     * SPEED-UP IF ASKED
     * Instead of comparing all 26 counts each step, track how many letters
     * currently match and update it only for the two letters that changed.
     * That makes it O(n).
     */

    // ====================================================================
    // 14. Find All Anagrams in a String
    // LeetCode 438 · Medium
    // Trick: same window as Permutation in String; record start i − m + 1  ·  O(26 × n)
    // ====================================================================

    /**
     * QUESTION
     * Return every starting index in s where an anagram of p begins.
     *
     * EXAMPLE
     *   s = "cbaebabacd", p = "abc"   ->  [0, 6]     ("cba" and "bac")
     *
     * STEP 1 · BRUTE FORCE
     * For each window, recount its letters from scratch and compare them with
     * p.
     *
     * Brute force cost: O(n × m) time
     */
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

    /**
     * STEP 2 · THE BETTER IDEA
     * It's exactly Permutation in String, but instead of returning true you
     * record the window's start (i − m + 1) every time the counts match, and
     * keep going.
     */
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

    /*
     * Time: O(26 × n) · Space: O(1)
     *
     * WATCH OUT
     * - Only compare once the window is full (i >= m − 1). Before that, a
     *   partial window can never match.
     */

    // ====================================================================
    // 15. Alert Using Same Key-Card 3+ Times in One Hour  ★ Karat
    // LeetCode 1604 · Medium
    // Trick: group → sort → t[i] − t[i−2] <= 60  ·  O(n log n)
    // ====================================================================

    /**
     * QUESTION
     * keyName[i] used their card at keyTime[i] ("HH:MM", all on one day).
     * Alert anyone who used their card 3 or more times within one hour. Here
     * an hour includes both ends, so 10:00 and 11:00 are in the same hour.
     * Return the names, sorted.
     *
     * EXAMPLE
     *   daniel: 10:00, 10:40, 11:00         ->  alert   (11:00 - 10:00 = 60 min)
     *   luis:   09:00, 11:00, 13:00, 15:00  ->  no
     *   answer: ["daniel"]
     *
     * STEP 1 · BRUTE FORCE
     * For every card use, count how many of the same person's uses fall
     * within the 60 minutes starting there. Alert if any count reaches 3.
     *
     * Brute force cost: O(k²) per person. With sorting (Step 2), one look at
     * t[i] and t[i−2] is enough.
     */
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

    /**
     * STEP 2 · THE BETTER IDEA
     * Group the times by name (map name → list of minutes) and sort each
     * list. Three uses within an hour exist exactly when some t[i] − t[i−2] ≤
     * 60: the 1st and 3rd of three back-to-back uses are at most an hour
     * apart. If any three uses fit in an hour, three consecutive ones do too.
     *
     * DRY RUN
     *   daniel minutes: [600, 640, 660]
     *   i = 2:  660 - 600 = 60  <= 60   ->  alert
     */
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

    /*
     * Time: O(n log n) · Space: O(n)
     *
     * WATCH OUT
     * - The input is not sorted and not grouped by person, so group first,
     *   then sort.
     * - Ask whether exactly 60 minutes counts. Here it does (<= 60).
     */

    // ====================================================================
    // 16. High-Access Employees  ★ Karat
    // LeetCode 2933 · Medium
    // Trick: same as 4, "HHMM" and strictly < 60  ·  O(n log n)
    // ====================================================================

    /**
     * QUESTION
     * Records are [name, "HHMM"] (no colon). An employee is "high-access" if
     * they accessed the system 3 or more times in less than one hour. Here
     * 10:00 and 11:00 are not in the same hour. Return the names in any
     * order.
     *
     * EXAMPLE
     *   [["a","0549"], ["b","0457"], ["a","0532"], ["a","0621"], ["b","0540"]]
     *
     *   a: 05:32, 05:49, 06:21   ->  06:21 - 05:32 = 49 min < 60  ->  high-access
     *   b: only 2 accesses
     *   answer: ["a"]
     *
     * STEP 1 · BRUTE FORCE
     * It's the same brute force as Key-Card Alert (count uses within an hour
     * of each use), using < 60 instead of <= 60.
     *
     * STEP 2 · THE IDEA
     * It's Key-Card Alert (problem 15) again with two small changes: the
     * times are "HHMM", read with substring(0, 2) and substring(2), and the
     * rule is strictly < 60.
     */
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

    /*
     * Time: O(n log n) · Space: O(n)
     *
     * WATCH OUT
     * - < versus <= is the whole difference between problems 15 and 16. In
     *   the Karat call, ask "Does exactly one hour count?" Interviewers
     *   notice when you do.
     */

    // ====================================================================
    // 17. Badge Records (2 parts)  ★ Karat
    // A Karat question asked in parts · Medium
    // Trick: P1: inside set + leftovers; P2: = Key-Card Alert + collect burst  ·  O(n log n)
    // ====================================================================

    /**
     * QUESTION
     * - Part 1: records are [name, "enter" | "exit"] in time order for one
     *   day. Everyone should enter, then exit, then enter, and so on. Return
     *   two lists: people who entered without exiting (they entered twice in
     *   a row, or were still inside at the end), and people who exited
     *   without entering (they exited while not inside).
     * - Part 2: records are [name, time] with times like 830 or 1415. Find
     *   everyone who badged 3 or more times within one hour, and return the
     *   times in that burst.
     *
     * EXAMPLE, PART 1
     *   Asha enter, Ravi enter, Asha exit, Meera exit, Ravi enter,
     *   Ravi exit, Kiran enter, Meera enter, Meera exit
     *
     *   entered without exiting:  [Kiran, Ravi]   (Ravi entered twice; Kiran never left)
     *   exited without entering:  [Meera]         (Meera's first record is an exit)
     *
     * EXAMPLE, PART 2
     *   Asha:  800, 845, 855, 900, 1300   ->  burst [800, 845, 855, 900]
     *   Ravi:  930, 1100, 1215            ->  no burst
     *   Meera: 1400, 1430, 1459           ->  burst [1400, 1430, 1459]
     *
     * STEP 1 · BRUTE FORCE
     * Part 1 is already one pass with a set, which is optimal. Part 2 has the
     * same brute force as Key-Card Alert: for every badge time, count the
     * times within the next hour. Step 2 uses the sorted t[i+2] − t[i] check.
     *
     * STEP 2 · THE IDEA
     * Part 1: keep a set inside. On enter: if they're already inside, that's
     * a bad enter; then add them. On exit: if they're not inside, that's a
     * bad exit; then remove them. At the end of the day, everyone still
     * inside is also a bad enter.
     *
     * Part 2: it's Key-Card Alert (problem 15) again. Group the times by
     * person and sort them. Find the first i where t[i+2] − t[i] ≤ 60
     * minutes, then collect every time from t[i] onwards that is still within
     * 60 minutes of t[i].
     *
     * DRY RUN, PART 1
     *   record | inside after | entered w/o exit | exited w/o enter
     *   Asha enter | {Asha} |  |
     *   Ravi enter | {Asha, Ravi} |  |
     *   Asha exit | {Ravi} |  |
     *   Meera exit | {Ravi} |  | Meera
     *   Ravi enter | {Ravi} | Ravi | Meera
     *   Ravi exit | { } | Ravi | Meera
     *   Kiran enter | {Kiran} | Ravi | Meera
     *   Meera enter | {Kiran, Meera} | Ravi | Meera
     *   Meera exit | {Kiran} | Ravi | Meera
     *   end of day |  | Kiran, Ravi | Meera
     */
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

    /*
     * Time: O(n log n) for both parts · Space: O(n)
     *
     * WATCH OUT
     * - Part 1: the same person can appear in both lists (exits first, then
     *   later enters twice). That's correct; the two lists are independent.
     * - Part 2: 900 − 800 = 100 as plain numbers, but the real gap is 60
     *   minutes. Always convert with (t / 100) * 60 + t % 100.
     * - Ask whether "within one hour" includes exactly 60 minutes. This code
     *   says yes.
     */

}
