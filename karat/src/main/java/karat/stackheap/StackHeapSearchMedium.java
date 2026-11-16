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

    // ====================================================================
    // 2. Min Stack
    // LeetCode 155 · design · Medium
    // Trick: second stack holding "minimum so far" alongside each push  ·  O(1) per op
    // ====================================================================

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

    // ====================================================================
    // 3. Evaluate Reverse Polish Notation
    // LeetCode 150 · Medium
    // Trick: numbers push; an operator pops b then a, pushes a op b  ·  O(n)
    // ====================================================================

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

    // ====================================================================
    // 4. Asteroid Collision
    // LeetCode 735 · Medium
    // Trick: stack of survivors; a left-mover fights right-movers on top  ·  O(n)
    // ====================================================================

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

    // ====================================================================
    // 5. Find First and Last Position of Element
    // LeetCode 34 · Medium
    // Trick: first = lowerBound(t); last = lowerBound(t + 1) − 1  ·  O(log n)
    // ====================================================================

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

    // ====================================================================
    // 6. Search in Rotated Sorted Array
    // LeetCode 33 · Medium
    // Trick: one half is always sorted; check if target lies inside it  ·  O(log n)
    // ====================================================================

    // LinearSearch: see StackHeapSearchEasy (Binary Search)

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

    // ====================================================================
    // 7. Time Based Key-Value Store
    // LeetCode 981 · Medium
    // Trick: per key: sorted timestamps; binary search last ts ≤ t  ·  O(log n) per get
    // ====================================================================

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

    // ====================================================================
    // 8. Koko Eating Bananas
    // LeetCode 875 · binary search on the answer · Medium
    // Trick: search the speed 1..max; works → hi = mid  ·  O(n log m)
    // ====================================================================

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

    // ====================================================================
    // 9. Capacity To Ship Packages Within D Days
    // LeetCode 1011 · binary search on the answer · Medium
    // Trick: search capacity in [max weight, total]; check days needed  ·  O(n log total)
    // ====================================================================

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

    // ====================================================================
    // 10. Kth Largest Element in an Array
    // LeetCode 215 · Medium
    // Trick: min-heap of size k; peek  ·  O(n log k)
    // ====================================================================

    // Step 1 · brute force
    public static class KthLargestSort {
        public int findKthLargest(int[] nums, int k) {
            int[] a = nums.clone();
            Arrays.sort(a);                            // smallest ... largest
            return a[a.length - k];                    // k-th from the end
        }
    }

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

    // ====================================================================
    // 11. K Closest Points to Origin
    // LeetCode 973 · Medium
    // Trick: max-heap of size k by distance²; drop the farthest  ·  O(n log k)
    // ====================================================================

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

    // ====================================================================
    // 12. Find Median from Data Stream
    // LeetCode 295 · two heaps · Hard
    // Trick: max-heap (small half) + min-heap (large half), balanced  ·  O(log n) add, O(1) median
    // ====================================================================

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

    // ====================================================================
    // 13. LRU Cache  ★ Karat
    // LeetCode 146 · design a small class · Medium
    // Trick: HashMap + doubly linked list with dummy head/tail  ·  O(1)
    // ====================================================================

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

}
