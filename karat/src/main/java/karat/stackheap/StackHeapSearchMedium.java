package karat.stackheap;

import java.util.*;

/**
 * Part 7 · Stack, Heap & Binary Search · Medium & Hard (13 problems).
 *
 * Explanations, dry runs and pitfalls: pdfs/Karat-7b-Medium-Stack-Heap-Binary-Search.pdf
 * Each problem: Step 1 = brute force, Step 2 = the better way.
 * Every example is checked in src/test/java/karat/stackheap/StackHeapSearchTests.java
 */
public final class StackHeapSearchMedium {

    private StackHeapSearchMedium() { }

    // ====================================================================
    // 1. Daily Temperatures
    // LeetCode 739 · monotonic stack · Medium
    // Trick: stack of waiting indexes; pop while today is warmer  ·  O(n)
    // ====================================================================

    /**
     * QUESTION
     * For each day, how many days do you wait for a warmer temperature? Use 0
     * if it never comes.
     *
     * EXAMPLE
     *   [73, 74, 75, 71, 69, 72, 76, 73]  ->  [1, 1, 4, 2, 1, 1, 0, 0]
     *
     * STEP 1 · BRUTE FORCE
     * For each day, scan forward until you find a warmer day.
     *
     * Brute force cost: O(n²) time on inputs like a long cold stretch. Step 2
     * answers each day exactly once.
     */
    // Step 1 · brute force
    public static class DailyTempsBrute {
        public int[] dailyTemperatures(int[] temps) {
            int[] answer = new int[temps.length];
            for (int i = 0; i < temps.length; i++) {
                for (int j = i + 1; j < temps.length; j++) {   // look ahead
                    if (temps[j] > temps[i]) {
                        answer[i] = j - i;
                        break;
                    }
                }
            }
            return answer;
        }
    }

    /**
     * STEP 2 · THE BETTER IDEA
     * Keep a stack of days still waiting for a warmer day. Their temperatures
     * go down from bottom to top. When today is warmer than the day on top,
     * that day's wait is over: its answer is today − day. Pop it and check
     * the next one. Then push today.
     *
     * DRY RUN ([30, 31, 29, 33])
     *   today | temp | answered (pop) | waiting after
     *   0 | 30 | – | [0]
     *   1 | 31 | day 0 → 1 | [1]
     *   2 | 29 | – | [1, 2]
     *   3 | 33 | day 2 → 1, then day 1 → 2 | [3]
     *
     * Day 3 never finds a warmer day, so it stays 0. Answer: [1, 2, 1, 0]
     */
    // Step 2 · the better way
    public static class DailyTemperatures {
        public int[] dailyTemperatures(int[] temps) {
            int[] answer = new int[temps.length];     // default 0
            Deque<Integer> waiting = new ArrayDeque<>();  // day indexes
            for (int today = 0; today < temps.length; today++) {
                // today is warmer than the days still waiting: answer them
                while (!waiting.isEmpty()
                        && temps[today] > temps[waiting.peek()]) {
                    int day = waiting.pop();
                    answer[day] = today - day;
                }
                waiting.push(today);                  // today waits too
            }
            return answer;
        }
    }

    /*
     * Time: O(n), since each day is pushed once and popped at most once ·
     * Space: O(n)
     *
     * WATCH OUT
     * - Put indexes on the stack, not temperatures. You need the index for
     *   the distance, and you can always look up the temperature.
     * - Use strictly >: a day with the same temperature is not warmer.
     *
     * SAME PATTERN ELSEWHERE
     * Next Greater Element (496), Online Stock Span (901), Largest Rectangle
     * in Histogram (84).
     */

    // ====================================================================
    // 2. Min Stack
    // LeetCode 155 · design · Medium
    // Trick: second stack holding "minimum so far" alongside each push  ·  O(1) per op
    // ====================================================================

    /**
     * QUESTION
     * Build a stack with push, pop, top and getMin, where every operation is
     * O(1).
     *
     * EXAMPLE
     *   push -2, push 0, push -3   getMin -> -3
     *   pop                        top -> 0,  getMin -> -2
     *
     * STEP 1 · BRUTE FORCE
     * Use a normal stack, and scan every element when getMin is called.
     *
     * Brute force cost: getMin is O(n). Everything else is O(1).
     */
    // Step 1 · brute force
    public static class MinStackScan {
        private final Deque<Integer> stack = new ArrayDeque<>();

        public void push(int x) { stack.push(x); }

        public void pop() { stack.pop(); }

        public int top() { return stack.peek(); }

        public int getMin() {
            int min = Integer.MAX_VALUE;
            for (int x : stack) min = Math.min(min, x);    // scan everything
            return min;
        }
    }

    /**
     * STEP 2 · THE BETTER IDEA
     * Keep a second stack that, at each level, stores the minimum of
     * everything at that level and below. On push, it gets min(x, current
     * min); on pop, both stacks pop together. Then getMin is just a peek.
     *
     * DRY RUN
     *   push -2: stack [-2]        mins [-2]
     *   push  0: stack [-2, 0]     mins [-2, -2]
     *   push -3: stack [-2, 0,-3]  mins [-2, -2, -3]   getMin = -3
     *   pop    : stack [-2, 0]     mins [-2, -2]       getMin = -2
     */
    // Step 2 · the better way
    public static class MinStack {
        private final Deque<Integer> stack = new ArrayDeque<>();
        private final Deque<Integer> mins = new ArrayDeque<>();  // min so far

        public void push(int x) {
            stack.push(x);
            // the new minimum is x or the old minimum, whichever is smaller
            mins.push(mins.isEmpty() ? x : Math.min(x, mins.peek()));
        }

        public void pop() {
            stack.pop();
            mins.pop();                                // keep them in step
        }

        public int top() { return stack.peek(); }

        public int getMin() { return mins.peek(); }    // O(1)
    }

    /*
     * Time: O(1) per operation · Space: O(n)
     *
     * KARAT ANGLE
     * "Add an O(1) extra query to a data structure" is a common design
     * follow-up. The usual answer is to store the answer alongside each
     * element as you add it.
     */

    // ====================================================================
    // 3. Evaluate Reverse Polish Notation
    // LeetCode 150 · Medium
    // Trick: numbers push; an operator pops b then a, pushes a op b  ·  O(n)
    // ====================================================================

    /**
     * QUESTION
     * Evaluate an expression written in postfix form (the operator comes
     * after its two operands). Division truncates toward zero.
     *
     * EXAMPLE
     *   ["2", "1", "+", "3", "*"]        ->  9      ((2 + 1) * 3)
     *   ["4", "13", "5", "/", "+"]       ->  6      (4 + 13 / 5)
     *
     * STEP 1 · BRUTE FORCE
     * Postfix exists precisely so that one left-to-right pass with a stack is
     * enough. Nothing simpler or slower is worth writing.
     *
     * STEP 2 · THE IDEA
     * Push every number. When you see an operator, pop b (the top) and then
     * a, compute a op b, and push the result. At the end, the stack holds the
     * answer.
     *
     * DRY RUN (["4","13","5","/","+"])
     *   4 -> [4]   13 -> [4,13]   5 -> [4,13,5]
     *   /  -> pop 5, pop 13 -> 13/5 = 2 -> [4,2]
     *   +  -> pop 2, pop 4  -> 4+2 = 6 -> [6]
     */
    // Step 2 · the better way
    public static class EvalRPN {
        public int evalRPN(String[] tokens) {
            Deque<Integer> stack = new ArrayDeque<>();
            for (String t : tokens) {
                if (t.equals("+") || t.equals("-")
                        || t.equals("*") || t.equals("/")) {
                    int b = stack.pop();          // 2nd operand is on top
                    int a = stack.pop();
                    if (t.equals("+")) stack.push(a + b);
                    else if (t.equals("-")) stack.push(a - b);
                    else if (t.equals("*")) stack.push(a * b);
                    else stack.push(a / b);
                } else {
                    stack.push(Integer.parseInt(t));   // a number
                }
            }
            return stack.pop();
        }
    }

    /*
     * Time: O(n) · Space: O(n)
     *
     * WATCH OUT
     * - Order matters for − and /: the first pop is the right-hand operand.
     * - "-11" is a number, not an operator. That's why the check compares
     *   whole tokens.
     */

    // ====================================================================
    // 4. Asteroid Collision
    // LeetCode 735 · Medium
    // Trick: stack of survivors; a left-mover fights right-movers on top  ·  O(n)
    // ====================================================================

    /**
     * QUESTION
     * Positive asteroids move right and negative ones move left, all at the
     * same speed. When two meet, the smaller one explodes; if they're equal,
     * both explode. Asteroids moving in the same direction never meet. What's
     * left?
     *
     * EXAMPLE
     *   [5, 10, -5]    ->  [5, 10]      (-5 hits 10 and explodes)
     *   [8, -8]        ->  [ ]
     *   [10, 2, -5]    ->  [10]         (-5 destroys 2, then explodes on 10)
     *
     * STEP 1 · BRUTE FORCE
     * Repeatedly scan for a right-mover immediately followed by a left-mover,
     * resolve that crash, and start scanning again.
     *
     * Brute force cost: O(n²) time
     */
    // Step 1 · brute force
    public static class AsteroidsBrute {
        public int[] asteroidCollision(int[] asteroids) {
            List<Integer> list = new ArrayList<>();
            for (int a : asteroids) list.add(a);
            boolean changed = true;
            while (changed) {                   // scan for a crash
                changed = false;
                for (int i = 0; i + 1 < list.size(); i++) {
                    int left = list.get(i), right = list.get(i + 1);
                    if (left > 0 && right < 0) {  // heading at each other
                        if (left > -right) {
                            list.remove(i + 1);
                        } else if (left < -right) {
                            list.remove(i);
                        } else {
                            list.remove(i + 1);
                            list.remove(i);
                        }
                        changed = true;
                        break;
                    }
                }
            }
            int[] res = new int[list.size()];
            for (int i = 0; i < res.length; i++) res[i] = list.get(i);
            return res;
        }
    }

    /**
     * STEP 2 · THE BETTER IDEA
     * Keep a stack of survivors. A right-mover (or any asteroid when the top
     * isn't moving right) just gets pushed. A left-mover crashes into right-
     * movers on top of the stack, one at a time: if the top is smaller, it
     * explodes and the fight continues; if they're equal, both go; if the top
     * is bigger, the newcomer explodes.
     */
    // Step 2 · the better way
    public static class Asteroids {
        public int[] asteroidCollision(int[] asteroids) {
            Deque<Integer> stack = new ArrayDeque<>();     // survivors so far
            for (int a : asteroids) {
                boolean alive = true;
                // a moves left and the top moves right: they crash
                while (alive && a < 0
                        && !stack.isEmpty() && stack.peek() > 0) {
                    int top = stack.peek();
                    if (top < -a) {
                        stack.pop();             // top explodes, a goes on
                    } else if (top == -a) {
                        stack.pop();                   // both explode
                        alive = false;
                    } else {
                        alive = false;                 // a explodes
                    }
                }
                if (alive) stack.push(a);
            }
            int[] res = new int[stack.size()];
            for (int i = res.length - 1; i >= 0; i--) res[i] = stack.pop();
            return res;
        }
    }

    /*
     * Time: O(n) · Space: O(n)
     *
     * WATCH OUT
     * - [-2, -1, 1, 2] has no collisions: left-movers on the left and right-
     *   movers on the right move apart.
     */

    // ====================================================================
    // 5. Find First and Last Position of Element
    // LeetCode 34 · Medium
    // Trick: first = lowerBound(t); last = lowerBound(t + 1) − 1  ·  O(log n)
    // ====================================================================

    /**
     * QUESTION
     * In a sorted array, return the first and last index of target, or [-1,
     * -1] if it isn't there. It must be O(log n).
     *
     * EXAMPLE
     *   [5, 7, 7, 8, 8, 10], 8  ->  [3, 4]
     *   [5, 7, 7, 8, 8, 10], 6  ->  [-1, -1]
     *
     * STEP 1 · BRUTE FORCE
     * Scan the whole array, remembering the first and the last match.
     *
     * Brute force cost: O(n) time
     */
    // Step 1 · brute force
    public static class SearchRangeLinear {
        public int[] searchRange(int[] nums, int target) {
            int first = -1, last = -1;
            for (int i = 0; i < nums.length; i++) {
                if (nums[i] == target) {
                    if (first == -1) first = i;
                    last = i;
                }
            }
            return new int[] { first, last };
        }
    }

    /**
     * STEP 2 · THE BETTER IDEA
     * Use the lower-bound search from Search Insert twice. The first position
     * is lowerBound(target). The last position is one before the first value
     * bigger than the target, which is lowerBound(target + 1) − 1.
     */
    // Step 2 · the better way
    public static class SearchRange {
        public int[] searchRange(int[] nums, int target) {
            int first = lowerBound(nums, target);  // first index >= target
            if (first == nums.length || nums[first] != target) {
                return new int[] { -1, -1 };              // not there at all
            }
            // just before bigger
            int last = lowerBound(nums, target + 1) - 1;
            return new int[] { first, last };
        }

        // first index whose value is >= x  (same as Search Insert)
        private int lowerBound(int[] nums, int x) {
            int lo = 0, hi = nums.length;
            while (lo < hi) {
                int mid = lo + (hi - lo) / 2;
                if (nums[mid] < x) lo = mid + 1;
                else hi = mid;
            }
            return lo;
        }
    }

    /*
     * Time: O(log n) · Space: O(1)
     *
     * WATCH OUT
     * - Check that nums[first] really is the target. Lower bound also returns
     *   a position when the target is missing.
     */

    // ====================================================================
    // 6. Search in Rotated Sorted Array
    // LeetCode 33 · Medium
    // Trick: one half is always sorted; check if target lies inside it  ·  O(log n)
    // ====================================================================

    /*
     * QUESTION
     * A sorted array of distinct numbers was rotated at an unknown point,
     * like [4,5,6,7,0,1,2]. Find target in O(log n), or return −1.
     *
     * EXAMPLE
     *   [4, 5, 6, 7, 0, 1, 2], 0  ->  4
     *   [4, 5, 6, 7, 0, 1, 2], 3  ->  -1
     *
     * STEP 1 · BRUTE FORCE
     * A plain linear scan from the left. It works, but it ignores the
     * sortedness.
     *
     * Brute force cost: O(n) time
     */
    // LinearSearch: see StackHeapSearchEasy (Binary Search)

    /**
     * STEP 2 · THE BETTER IDEA
     * Cut at mid, and one of the two halves is always properly sorted. Check
     * which one: if nums[lo] ≤ nums[mid], it's the left half. If the target
     * falls inside that sorted half's range, search there; otherwise search
     * the other half.
     *
     * DRY RUN (TARGET 0)
     *   lo 0 hi 6 mid 3 (7): left [4..7] sorted; 0 not in [4,7) -> lo = 4
     *   lo 4 hi 6 mid 5 (1): left [0..1] sorted; 0 in [0,1)    -> hi = 4
     *   lo 4 hi 4 mid 4 (0): found -> 4
     */
    // Step 2 · the better way
    public static class SearchRotated {
        public int search(int[] nums, int target) {
            int lo = 0, hi = nums.length - 1;
            while (lo <= hi) {
                int mid = lo + (hi - lo) / 2;
                if (nums[mid] == target) return mid;
                if (nums[lo] <= nums[mid]) {  // LEFT half is sorted
                    if (nums[lo] <= target && target < nums[mid]) {
                        hi = mid - 1;  // target is inside it
                    } else {
                        lo = mid + 1;
                    }
                } else {  // RIGHT half is sorted
                    if (nums[mid] < target && target <= nums[hi]) {
                        lo = mid + 1;
                    } else {
                        hi = mid - 1;
                    }
                }
            }
            return -1;
        }
    }

    /*
     * Time: O(log n) · Space: O(1)
     *
     * WATCH OUT
     * - Use <= in nums[lo] <= nums[mid]. When lo == mid (two elements left),
     *   the left "half" is just one element, and it counts as sorted.
     */

    // ====================================================================
    // 7. Time Based Key-Value Store
    // LeetCode 981 · Medium
    // Trick: per key: sorted timestamps; binary search last ts ≤ t  ·  O(log n) per get
    // ====================================================================

    /**
     * QUESTION
     * Build a store where set(key, value, timestamp) saves a value, and
     * get(key, t) returns the value with the largest timestamp ≤ t, or "" if
     * there isn't one. Timestamps for set always increase.
     *
     * EXAMPLE
     *   set(price, 100, 10)  set(price, 120, 20)  set(price, 90, 30)
     *   get(price, 25) -> "120"     get(price, 30) -> "90"     get(price, 5) -> ""
     *
     * STEP 1 · BRUTE FORCE
     * For each get, walk that key's history backwards from the newest until
     * you find a timestamp ≤ t.
     *
     * Brute force cost: O(n) per get in the worst case.
     */
    // Step 1 · brute force
    public static class TimeMapScan {
        private final Map<String, List<Integer>> times = new HashMap<>();
        private final Map<String, List<String>> values = new HashMap<>();

        public void set(String key, String value, int t) {
            times.putIfAbsent(key, new ArrayList<>());
            values.putIfAbsent(key, new ArrayList<>());
            times.get(key).add(t);                     // timestamps only grow
            values.get(key).add(value);
        }

        public String get(String key, int t) {
            List<Integer> ts = times.get(key);
            if (ts == null) return "";
            // walk back from the newest
            for (int i = ts.size() - 1; i >= 0; i--) {
                if (ts.get(i) <= t) return values.get(key).get(i);
            }
            return "";
        }
    }

    /**
     * STEP 2 · THE BETTER IDEA
     * Because timestamps arrive in increasing order, each key's list is
     * already sorted. So get can binary search for the last index whose
     * timestamp is ≤ t. Remember a candidate whenever ts[mid] ≤ t, then keep
     * looking to the right for a later one.
     */
    // Step 2 · the better way
    public static class TimeMap {
        private final Map<String, List<Integer>> times = new HashMap<>();
        private final Map<String, List<String>> values = new HashMap<>();

        public void set(String key, String value, int t) {
            times.putIfAbsent(key, new ArrayList<>());
            values.putIfAbsent(key, new ArrayList<>());
            times.get(key).add(t);  // stays sorted for free
            values.get(key).add(value);
        }

        public String get(String key, int t) {
            List<Integer> ts = times.get(key);
            if (ts == null) return "";
            int lo = 0, hi = ts.size() - 1, found = -1;
            while (lo <= hi) {  // LAST index with ts <= t
                int mid = lo + (hi - lo) / 2;
                if (ts.get(mid) <= t) {
                    found = mid;  // good; maybe a later one
                    lo = mid + 1;
                } else {
                    hi = mid - 1;
                }
            }
            return found == -1 ? "" : values.get(key).get(found);
        }
    }

    /*
     * Time: O(1) set, O(log n) get · Space: O(total sets)
     *
     * KARAT ANGLE
     * "What was the value as of time t?" (prices, configs, user status) is
     * this exact pattern. A TreeMap with floorEntry(t) is a valid one-line
     * alternative. Mention it.
     */

    // ====================================================================
    // 8. Koko Eating Bananas
    // LeetCode 875 · binary search on the answer · Medium
    // Trick: search the speed 1..max; works → hi = mid  ·  O(n log m)
    // ====================================================================

    /**
     * QUESTION
     * There are piles of bananas, and Koko eats at speed k bananas per hour.
     * Each hour she picks one pile and eats up to k from it. If the pile has
     * fewer, she finishes it and rests for the rest of the hour. Find the
     * minimum speed k that lets her finish everything within h hours.
     *
     * EXAMPLE
     *   piles = [3, 6, 7, 11], h = 8   ->  4
     *   at speed 4:  1 + 2 + 2 + 3 = 8 hours  (ok)
     *   at speed 3:  1 + 2 + 3 + 4 = 10 hours (too slow)
     *
     * STEP 1 · BRUTE FORCE
     * Try speeds 1, 2, 3, … and return the first one that finishes in time.
     *
     * Brute force cost: O(maxPile × n) time. With piles of 10⁹ bananas that's
     * billions of tries. Step 2 halves the range each time.
     */
    // Step 1 · brute force
    public static class KokoBrute {
        public int minEatingSpeed(int[] piles, int h) {
            for (int speed = 1; ; speed++) {           // try 1, 2, 3, ...
                long hours = 0;
                for (int p : piles) {
                    hours += p / speed;
                    if (p % speed != 0) hours++;
                }
                if (hours <= h) return speed;     // first speed that works
            }
        }
    }

    /**
     * STEP 2 · THE BETTER IDEA
     * A speed either works or it doesn't, and if speed k works, every faster
     * speed works too. So the answers look like no no no YES YES YES…, and
     * you binary search for the first YES.
     *
     * The range is 1 .. biggest pile: at the biggest pile's speed, every pile
     * takes exactly one hour.
     *
     * Hours at speed k = the sum of ceil(pile / k).
     *
     * DRY RUN (PILES [3, 6, 7, 11], H = 8)
     *   lo | hi | mid | hours(mid) | ≤ 8? | next
     *   1 | 11 | 6 | 1+1+2+2 = 6 | yes | hi = 6
     *   1 | 6 | 3 | 1+2+3+4 = 10 | no | lo = 4
     *   4 | 6 | 5 | 1+2+2+3 = 8 | yes | hi = 5
     *   4 | 5 | 4 | 1+2+2+3 = 8 | yes | hi = 4
     *
     * lo = hi = 4 → answer 4
     */
    // Step 2 · the better way
    public static class KokoBananas {
        public int minEatingSpeed(int[] piles, int h) {
            int lo = 1, hi = 0;
            // eating at the biggest pile's size is always fast enough
            for (int p : piles) hi = Math.max(hi, p);
            while (lo < hi) {
                int mid = lo + (hi - lo) / 2;
                if (hoursNeeded(piles, mid) <= h) {
                    hi = mid;           // fast enough: try slower (keep mid)
                } else {
                    lo = mid + 1;       // too slow: must be faster
                }
            }
            return lo;
        }

        private long hoursNeeded(int[] piles, int speed) {
            long hours = 0;
            for (int p : piles) {
                hours += p / speed;
                if (p % speed != 0) hours++;     // leftover takes one more
            }
            return hours;
        }
    }

    /*
     * Time: O(n log maxPile) · Space: O(1)
     *
     * WATCH OUT
     * - When mid works, set hi = mid, not mid − 1, because mid itself might
     *   be the answer. This version uses while (lo < hi).
     * - Add up the hours in a long: many big piles can overflow an int.
     * - Ceiling without floating point: p / k, plus 1 if there's a remainder.
     *
     * SAME TEMPLATE SOLVES
     * Capacity to Ship Packages Within D Days (1011), Split Array Largest Sum
     * (410), Minimum Days to Make Bouquets (1482).
     */

    // ====================================================================
    // 9. Capacity To Ship Packages Within D Days
    // LeetCode 1011 · binary search on the answer · Medium
    // Trick: search capacity in [max weight, total]; check days needed  ·  O(n log total)
    // ====================================================================

    /**
     * QUESTION
     * Packages must ship in order, and each day the ship carries packages up
     * to its capacity. What's the smallest capacity that ships everything
     * within days days?
     *
     * EXAMPLE
     *   weights 1..10, days = 5   ->  15
     *   (1-5 | 6,7 | 8 | 9 | 10)
     *
     * STEP 1 · BRUTE FORCE
     * Start at the heaviest package (anything smaller can't carry it) and try
     * every capacity upward until one fits in time.
     *
     * Brute force cost: O(n × total weight) time
     */
    // Step 1 · brute force
    public static class ShipCapacityBrute {
        public int shipWithinDays(int[] weights, int days) {
            int cap = 0;
            // must fit heaviest
            for (int w : weights) cap = Math.max(cap, w);
            while (daysNeeded(weights, cap) > days) {
                cap++;                                 // try every capacity
            }
            return cap;
        }

        private int daysNeeded(int[] weights, int cap) {
            int days = 1, load = 0;
            for (int w : weights) {
                if (load + w > cap) {                  // ship is full:
                    days++;                            // start the next day
                    load = 0;
                }
                load += w;
            }
            return days;
        }
    }

    /**
     * STEP 2 · THE BETTER IDEA
     * It's the same shape as Koko: if capacity C works, every bigger capacity
     * works too. So binary search over [heaviest, sum of all]. The check
     * simulates loading in order: start a new day whenever the next package
     * doesn't fit.
     */
    // Step 2 · the better way
    public static class ShipCapacity {
        public int shipWithinDays(int[] weights, int days) {
            int lo = 0, hi = 0;
            for (int w : weights) {
                lo = Math.max(lo, w);                  // smallest possible
                hi += w;  // everything in one day
            }
            while (lo < hi) {
                int mid = lo + (hi - lo) / 2;
                if (daysNeeded(weights, mid) <= days) {
                    hi = mid;                          // works: try smaller
                } else {
                    lo = mid + 1;                      // too small
                }
            }
            return lo;
        }

        private int daysNeeded(int[] weights, int cap) {
            int days = 1, load = 0;
            for (int w : weights) {
                if (load + w > cap) {
                    days++;
                    load = 0;
                }
                load += w;
            }
            return days;
        }
    }

    /*
     * Time: O(n log(total weight)) · Space: O(1)
     *
     * RECOGNISE THE FAMILY
     * "Find the minimum X such that something is possible," where bigger X is
     * always easier, is binary search on the answer: Koko, ship capacity,
     * split array, bouquets.
     */

    // ====================================================================
    // 10. Kth Largest Element in an Array
    // LeetCode 215 · Medium
    // Trick: min-heap of size k; peek  ·  O(n log k)
    // ====================================================================

    /**
     * QUESTION
     * Return the k-th largest number. Duplicates count: in [5, 5, 4], the 2nd
     * largest is 5.
     *
     * EXAMPLE
     *   [3, 2, 1, 5, 6, 4],          k = 2  ->  5
     *   [3, 2, 3, 1, 2, 4, 5, 5, 6], k = 4  ->  4
     *
     * STEP 1 · BRUTE FORCE
     * Sort a copy and take the k-th element from the end.
     *
     * Brute force cost: O(n log n) time. It's short, correct and a fine first
     * answer. Step 2 avoids sorting everything.
     */
    // Step 1 · brute force
    public static class KthLargestSort {
        public int findKthLargest(int[] nums, int k) {
            int[] a = nums.clone();
            Arrays.sort(a);                            // smallest ... largest
            return a[a.length - k];                    // k-th from the end
        }
    }

    /**
     * STEP 2 · THE BETTER IDEA
     * Keep a min-heap holding the k largest numbers seen so far. Whenever it
     * grows past k, remove the smallest. At the end, the smallest number in
     * the heap (peek) is the k-th largest.
     *
     * DRY RUN ([3, 2, 1, 5, 6, 4], K = 2)
     *   add | heap before trimming | trim? | heap after
     *   3 | [3] | – | [3]
     *   2 | [2, 3] | – | [2, 3]
     *   1 | [1, 2, 3] | poll 1 | [2, 3]
     *   5 | [2, 3, 5] | poll 2 | [3, 5]
     *   6 | [3, 5, 6] | poll 3 | [5, 6]
     *   4 | [4, 5, 6] | poll 4 | [5, 6]
     *
     * peek → 5
     */
    // Step 2 · the better way
    public static class KthLargest {
        public int findKthLargest(int[] nums, int k) {
            PriorityQueue<Integer> heap = new PriorityQueue<>();  // min-heap
            for (int n : nums) {
                heap.add(n);
                if (heap.size() > k) {
                    heap.poll();             // throw away the smallest
                }
            }
            return heap.peek();              // smallest of the top k
        }
    }

    /*
     * Time: O(n log k) · Space: O(k)
     *
     * WATCH OUT
     * - Java's PriorityQueue is a min-heap by default, which is exactly what
     *   you want here.
     * - Arrays.sort(nums); return nums[nums.length - k]; is O(n log n) and a
     *   fine first answer. Mention the heap as the improvement.
     *
     * KARAT LINK
     * "Top k" questions (most active users, most visited pages) combine Part
     * 1's counting map with this size-k heap.
     */

    // ====================================================================
    // 11. K Closest Points to Origin
    // LeetCode 973 · Medium
    // Trick: max-heap of size k by distance²; drop the farthest  ·  O(n log k)
    // ====================================================================

    /**
     * QUESTION
     * Return the k points closest to (0, 0), in any order.
     *
     * EXAMPLE
     *   [[3,3], [5,-1], [-2,4]], k = 2   ->  [[3,3], [-2,4]]
     *   distances²: 18, 26, 20
     *
     * STEP 1 · BRUTE FORCE
     * Sort all the points by distance and take the first k.
     *
     * Brute force cost: O(n log n) time
     */
    // Step 1 · brute force
    public static class KClosestSort {
        public int[][] kClosest(int[][] points, int k) {
            int[][] p = points.clone();
            Arrays.sort(p, (a, b) -> Integer.compare(dist(a), dist(b)));
            return Arrays.copyOf(p, k);                // the first k
        }

        private int dist(int[] p) {
            return p[0] * p[0] + p[1] * p[1];          // no sqrt needed
        }
    }

    /**
     * STEP 2 · THE BETTER IDEA
     * This is Kth Largest flipped around. Keep a max-heap of size k ordered
     * by distance, so the farthest kept point is on top. Add each point; if
     * the heap grows past k, drop the top. What remains are the k closest.
     */
    // Step 2 · the better way
    public static class KClosest {
        public int[][] kClosest(int[][] points, int k) {
            // max-heap by distance: the FARTHEST kept point is on top
            PriorityQueue<int[]> heap = new PriorityQueue<>(
                    (a, b) -> Integer.compare(dist(b), dist(a)));
            for (int[] p : points) {
                heap.add(p);
                if (heap.size() > k) heap.poll();      // drop the farthest
            }
            return heap.toArray(new int[0][]);
        }

        private int dist(int[] p) {
            return p[0] * p[0] + p[1] * p[1];
        }
    }

    /*
     * Time: O(n log k) · Space: O(k)
     *
     * WATCH OUT
     * - Compare squared distances (x² + y²). The square root doesn't change
     *   the order, so skip it.
     */

    // ====================================================================
    // 12. Find Median from Data Stream
    // LeetCode 295 · two heaps · Hard
    // Trick: max-heap (small half) + min-heap (large half), balanced  ·  O(log n) add, O(1) median
    // ====================================================================

    /**
     * QUESTION
     * Numbers arrive one at a time. Support addNum(x) and findMedian() at any
     * moment.
     *
     * EXAMPLE
     *   add 5 -> median 5     add 15 -> 10     add 1 -> 5     add 3 -> 4
     *
     * STEP 1 · BRUTE FORCE
     * Keep a sorted list and insert each number in its place.
     *
     * Brute force cost: O(n) per addNum (searching and shifting), O(1) per
     * median.
     */
    // Step 1 · brute force
    public static class MedianFinderSorted {
        private final List<Integer> nums = new ArrayList<>();  // kept sorted

        public void addNum(int x) {
            int i = 0;
            while (i < nums.size() && nums.get(i) < x) i++;  // find its place
            nums.add(i, x);                                    // O(n) shift
        }

        public double findMedian() {
            int n = nums.size();
            if (n % 2 == 1) return nums.get(n / 2);
            return (nums.get(n / 2 - 1) + nums.get(n / 2)) / 2.0;
        }
    }

    /**
     * STEP 2 · THE BETTER IDEA
     * Split the numbers into two halves:
     *
     * - small: a max-heap of the smaller half, with its largest on top;
     * - large: a min-heap of the larger half, with its smallest on top.
     *
     * Keep their sizes equal, or let small hold one extra. Then the median is
     * either small's top, or the average of both tops.
     *
     * To add x: push it into small, move small's biggest over to large (which
     * keeps every number in small ≤ every number in large), and if large is
     * now bigger, move its smallest back.
     *
     * DRY RUN
     *   add 5:  small [5]         large [ ]          median 5
     *   add 15: small [5]         large [15]         median (5+15)/2 = 10
     *   add 1:  small [5, 1]      large [15]         median 5
     *   add 3:  small [3, 1]      large [5, 15]      median (3+5)/2 = 4
     */
    // Step 2 · the better way
    public static class MedianFinder {
        // small = max-heap of the smaller half; large = min-heap of the rest
        private final PriorityQueue<Integer> small =
                new PriorityQueue<>(Collections.reverseOrder());
        private final PriorityQueue<Integer> large = new PriorityQueue<>();

        public void addNum(int x) {
            small.add(x);                      // 1) into the small half
            large.add(small.poll());           // 2) its biggest moves across
            if (large.size() > small.size()) { // 3) small keeps the extra one
                small.add(large.poll());
            }
        }

        public double findMedian() {
            // odd count
            if (small.size() > large.size()) return small.peek();
            return (small.peek() + large.peek()) / 2.0;  // even count
        }
    }

    /*
     * Time: O(log n) per add, O(1) per median · Space: O(n)
     *
     * KARAT ANGLE
     * "Running median of response times / prices" is a classic stream
     * question. Saying "two heaps" in the first 30 seconds signals you've
     * seen it.
     */

    // ====================================================================
    // 13. LRU Cache  ★ Karat
    // LeetCode 146 · design a small class · Medium
    // Trick: HashMap + doubly linked list with dummy head/tail  ·  O(1)
    // ====================================================================

    /**
     * QUESTION
     * Build a cache with a fixed capacity:
     *
     * - get(key) returns the value, or −1 if the key isn't there.
     * - put(key, value) inserts or updates. When the cache is full, it first
     *   removes the least recently used key.
     *
     * Both operations must be O(1).
     *
     * EXAMPLE
     *   cache = new LRUCache(2)
     *   put(1, 10)  put(2, 20)       order, newest first: 2, 1
     *   get(1)  -> 10                order: 1, 2         (1 was just used)
     *   put(3, 30)                   full -> evict 2     order: 3, 1
     *   get(2)  -> -1
     *   put(4, 40)                   full -> evict 1     order: 4, 3
     *   get(1) -> -1    get(3) -> 30    get(4) -> 40
     *
     * STEP 1 · BRUTE FORCE
     * Use a HashMap for the values plus an ArrayList of keys ordered by use
     * (oldest first). Each get or put removes the key from the list and
     * appends it at the end.
     *
     * Brute force cost: O(n) per operation, because list.remove(key) searches
     * and shifts the list. The problem asks for O(1), which is why Step 2
     * uses a linked list.
     */
    // Step 1 · brute force
    public static class LRUCacheList {
        private final int capacity;
        private final Map<Integer, Integer> values = new HashMap<>();
        // keys in order of use, oldest first
        private final List<Integer> order = new ArrayList<>();

        public LRUCacheList(int capacity) {
            this.capacity = capacity;
        }

        public int get(int key) {
            if (!values.containsKey(key)) return -1;
            order.remove(Integer.valueOf(key));        // O(n) search + shift
            order.add(key);                            // now the newest
            return values.get(key);
        }

        public void put(int key, int value) {
            if (values.containsKey(key)) {
                order.remove(Integer.valueOf(key));
            } else if (values.size() == capacity) {
                int oldest = order.remove(0);          // evict the oldest
                values.remove(oldest);
            }
            values.put(key, value);
            order.add(key);
        }
    }

    /**
     * STEP 2 · THE BETTER IDEA
     * Use two structures together:
     *
     * - a HashMap key → node, to find any key in O(1);
     * - a doubly linked list ordered by use, with the newest right after head
     *   and the oldest right before tail. Removing or moving a node is O(1)
     *   because each node knows its prev and next.
     *
     * The dummy head and tail nodes mean you never have to check for null at
     * the ends.
     *
     *   head <-> [newest] <-> ... <-> [oldest] <-> tail
     *
     *   get / put an existing key  ->  move that node to the front (after head)
     *   cache full                 ->  remove tail.prev (the oldest)
     *
     * THE VERSION INTERVIEWERS WANT TO SEE
     */
    // Step 2 · the better way
    public static class LRUCache {
        // one entry in a doubly linked list
        private static class Node {
            int key, value;
            Node prev, next;
            Node(int key, int value) {
                this.key = key;
                this.value = value;
            }
        }

        private final int capacity;
        private final Map<Integer, Node> map = new HashMap<>();
        private final Node head = new Node(0, 0);   // dummy: newest side
        private final Node tail = new Node(0, 0);   // dummy: oldest side

        public LRUCache(int capacity) {
            this.capacity = capacity;
            head.next = tail;
            tail.prev = head;
        }

        public int get(int key) {
            Node node = map.get(key);
            if (node == null) return -1;
            moveToFront(node);                 // just used = newest
            return node.value;
        }

        public void put(int key, int value) {
            Node node = map.get(key);
            if (node != null) {                // key exists: update it
                node.value = value;
                moveToFront(node);
                return;
            }
            if (map.size() == capacity) {      // full: remove the oldest
                Node oldest = tail.prev;
                unlink(oldest);
                map.remove(oldest.key);
            }
            Node fresh = new Node(key, value);
            map.put(key, fresh);
            addToFront(fresh);
        }

        private void moveToFront(Node n) {
            unlink(n);
            addToFront(n);
        }

        private void unlink(Node n) {          // take n out of the list
            n.prev.next = n.next;
            n.next.prev = n.prev;
        }

        private void addToFront(Node n) {      // put n right after head
            n.prev = head;
            n.next = head.next;
            head.next.prev = n;
            head.next = n;
        }
    }

    /**
     * THE JAVA SHORTCUT (MENTION IT, BUT EXPECT TO BUILD THE ONE ABOVE)
     */
    // Step 2 · the better way
    public static class LRUCacheSimple extends LinkedHashMap<Integer, Integer> {
        private final int capacity;

        public LRUCacheSimple(int capacity) {
            super(16, 0.75f, true);            // true = order by ACCESS
            this.capacity = capacity;
        }

        public int get(int key) {
            return super.getOrDefault(key, -1);
        }

        public void put(int key, int value) {
            super.put(key, value);
        }

        @Override
        protected boolean removeEldestEntry(Map.Entry<Integer, Integer> e) {
            return size() > capacity;          // too big: drop the oldest
        }
    }

    /*
     * Time: O(1) per operation · Space: O(capacity)
     *
     * WATCH OUT
     * - Store the key inside the node. When you evict tail.prev, you need its
     *   key to remove it from the map too.
     * - When updating an existing key, change its value and move it to the
     *   front. Don't create a second node.
     * - In addToFront, set the new node's own prev and next first, then
     *   rewire head.next.prev and head.next. The order matters.
     *
     * KARAT TIP
     * Say the plan in one sentence before coding: "A HashMap for O(1) lookup,
     * plus a doubly linked list for O(1) reordering, with the most recent at
     * the head." Then write unlink and addToFront, then get, then put.
     */

}
