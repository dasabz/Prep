package karat.stackheap;

import java.util.*;

/**
 * Part 7 · Stack, Heap & Binary Search · Easy (7 problems).
 *
 * Explanations, dry runs and pitfalls: pdfs/Karat-7a-Easy-Stack-Heap-Binary-Search.pdf
 * Each problem: Step 1 = brute force, Step 2 = the better way.
 * Every example is checked in src/test/java/karat/stackheap/StackHeapSearchTests.java
 */
public final class StackHeapSearchEasy {

    private StackHeapSearchEasy() { }

    // ====================================================================
    // 1. Valid Parentheses
    // LeetCode 20 · Easy
    // Trick: push the expected closer; pop must match; empty at end  ·  O(n)
    // ====================================================================

    /**
     * QUESTION
     * The string contains only ( ) [ ] { }. It's valid if every opener is
     * closed by the same type, in the right order.
     *
     * EXAMPLE
     *   "([]{})"   ->  true
     *   "([)]"     ->  false    ( [ is closed by ) )
     *   "(("       ->  false    (never closed)
     *
     * STEP 1 · BRUTE FORCE
     * Keep deleting every (), [] and {} pair you can find, until the string
     * stops changing. A valid string disappears completely.
     *
     * Brute force cost: O(n²) time (up to n/2 rounds, each copying the
     * string). It's cute and easy to remember, but the stack is the real
     * answer.
     */
    // Step 1 · brute force
    public static class ParenthesesReplace {
        public boolean isValid(String s) {
            String prev;
            do {                                       // remove inner pairs
                prev = s;
                s = s.replace("()", "").replace("[]", "").replace("{}", "");
            } while (!s.equals(prev));            // until nothing changes
            return s.isEmpty();
        }
    }

    /**
     * STEP 2 · THE BETTER IDEA
     * Use a stack. On an opener, push the closer you'll need. On a closer, it
     * must match what you pop off the top. At the end, the stack must be
     * empty.
     *
     * DRY RUN ("([)]")
     *   char | action | stack (top on the right)
     *   ( | push ) | )
     *   [ | push ] | ) ]
     *   ) | pop ], which ≠ ) → false |
     */
    // Step 2 · the better way
    public static class ValidParentheses {
        public boolean isValid(String s) {
            Deque<Character> stack = new ArrayDeque<>();
            for (char c : s.toCharArray()) {
                if (c == '(') {
                    stack.push(')');          // remember the closer we need
                } else if (c == '[') {
                    stack.push(']');
                } else if (c == '{') {
                    stack.push('}');
                } else if (stack.isEmpty() || stack.pop() != c) {
                    return false;             // wrong or unexpected closer
                }
            }
            return stack.isEmpty();           // nothing left open
        }
    }

    /*
     * Time: O(n) · Space: O(n)
     *
     * WATCH OUT
     * - A closer when the stack is empty ("]") → false. Check isEmpty()
     *   before pop(), or it throws.
     * - Openers left over ("((") → false, caught by the final isEmpty().
     */

    // ====================================================================
    // 2. Remove All Adjacent Duplicates in String
    // LeetCode 1047 · Easy
    // Trick: StringBuilder as a stack; same as top → pop, else push  ·  O(n)
    // ====================================================================

    /**
     * QUESTION
     * Repeatedly delete two equal letters that sit next to each other until
     * no such pair is left. Return the final string.
     *
     * EXAMPLE
     *   "abbaca"  ->  "ca"     (remove "bb" -> "aaca", then remove "aa" -> "ca")
     *
     * STEP 1 · BRUTE FORCE
     * Scan for any adjacent equal pair, cut it out, and start scanning again.
     * Repeat until nothing changes.
     *
     * Brute force cost: O(n²) time, with a new string every time.
     */
    // Step 1 · brute force
    public static class RemoveDuplicatesBrute {
        public String removeDuplicates(String s) {
            boolean changed = true;
            while (changed) {                   // rescan after a removal
                changed = false;
                for (int i = 0; i + 1 < s.length(); i++) {
                    if (s.charAt(i) == s.charAt(i + 1)) {
                        s = s.substring(0, i) + s.substring(i + 2);
                        changed = true;
                        break;
                    }
                }
            }
            return s;
        }
    }

    /**
     * STEP 2 · THE BETTER IDEA
     * Use a stack, which a StringBuilder can act as. For each letter: if it
     * equals the letter on top, pop it, because the pair vanishes. Otherwise
     * push it. Removing a pair can expose a new pair underneath, and the
     * stack handles that automatically.
     *
     * DRY RUN ("ABBACA")
     *   a -> [a]   b -> [a,b]   b = top -> pop [a]   a = top -> pop [ ]
     *   c -> [c]   a -> [c,a]   ->  "ca"
     */
    // Step 2 · the better way
    public static class RemoveDuplicates {
        public String removeDuplicates(String s) {
            StringBuilder stack = new StringBuilder();     // used as a stack
            for (char c : s.toCharArray()) {
                int top = stack.length() - 1;
                if (top >= 0 && stack.charAt(top) == c) {
                    stack.deleteCharAt(top);         // pair: both vanish
                } else {
                    stack.append(c);
                }
            }
            return stack.toString();
        }
    }

    /*
     * Time: O(n) · Space: O(n)
     *
     * PATTERN
     * "Remove things that cancel out with their neighbour" (brackets,
     * duplicates, asteroids) means a stack.
     */

    // ====================================================================
    // 3. Next Greater Element I
    // LeetCode 496 · monotonic stack · Easy
    // Trick: one pass over nums2 with a stack → map value → next greater  ·  O(n + m)
    // ====================================================================

    /**
     * QUESTION
     * nums1 is a subset of nums2. For each value in nums1, find the first
     * bigger value to its right in nums2, or −1 if there isn't one.
     *
     * EXAMPLE
     *   nums1 = [4, 1, 2], nums2 = [1, 3, 4, 2]   ->  [-1, 3, -1]
     *
     * STEP 1 · BRUTE FORCE
     * For each value, find it in nums2, then scan right for the first bigger
     * value.
     *
     * Brute force cost: O(n × m) time
     */
    // Step 1 · brute force
    public static class NextGreaterBrute {
        public int[] nextGreaterElement(int[] nums1, int[] nums2) {
            int[] ans = new int[nums1.length];
            for (int i = 0; i < nums1.length; i++) {
                ans[i] = -1;
                int pos = 0;
                while (nums2[pos] != nums1[i]) pos++;      // find it in nums2
                for (int j = pos + 1; j < nums2.length; j++) {
                    if (nums2[j] > nums1[i]) {             // first bigger one
                        ans[i] = nums2[j];
                        break;
                    }
                }
            }
            return ans;
        }
    }

    /**
     * STEP 2 · THE BETTER IDEA
     * It's the Daily Temperatures stack, storing values instead of days. Walk
     * nums2 once, keeping a stack of values still waiting for something
     * bigger. When a bigger x arrives, it's the answer for every smaller
     * value on top, so pop them and record next[value] = x. Then answer nums1
     * from the map.
     */
    // Step 2 · the better way
    public static class NextGreater {
        public int[] nextGreaterElement(int[] nums1, int[] nums2) {
            // value -> its next greater value;  stack = values still waiting
            Map<Integer, Integer> next = new HashMap<>();
            Deque<Integer> stack = new ArrayDeque<>();
            for (int x : nums2) {
                while (!stack.isEmpty() && x > stack.peek()) {
                    next.put(stack.pop(), x);              // x answers them
                }
                stack.push(x);
            }
            int[] ans = new int[nums1.length];
            for (int i = 0; i < nums1.length; i++) {
                ans[i] = next.getOrDefault(nums1[i], -1);
            }
            return ans;
        }
    }

    /*
     * Time: O(n + m) · Space: O(m)
     */

    // ====================================================================
    // 4. Binary Search
    // LeetCode 704 · Easy
    // Trick: lo <= hi; move past mid; lo + (hi−lo)/2  ·  O(log n)
    // ====================================================================

    /**
     * QUESTION
     * Given a sorted array, return the index of target, or −1 if it isn't
     * there. It must run in O(log n).
     *
     * EXAMPLE
     *   [-3, 0, 4, 7, 9, 15], target 9  ->  4
     *   [-3, 0, 4, 7, 9, 15], target 5  ->  -1
     *
     * STEP 1 · BRUTE FORCE
     * Check the numbers one by one from the left.
     *
     * Brute force cost: O(n) time. It ignores the fact that the array is
     * sorted, which is the whole point of the problem.
     */
    // Step 1 · brute force
    public static class LinearSearch {
        public int search(int[] nums, int target) {
            for (int i = 0; i < nums.length; i++) {
                if (nums[i] == target) return i;       // check one by one
            }
            return -1;
        }
    }

    /**
     * STEP 2 · THE BETTER IDEA
     * Look at the middle. If it's the target, you're done. If the middle is
     * too small, the target can only be to the right, so lo = mid + 1.
     * Otherwise it's to the left, so hi = mid − 1. Each step halves the
     * range.
     *
     * DRY RUN
     *   target | lo | hi | mid | nums[mid] | action
     *   9 | 0 | 5 | 2 | 4 | 4 < 9 → lo = 3
     *   9 | 3 | 5 | 4 | 9 | found → 4
     *   5 | 0 | 5 | 2 | 4 | lo = 3
     *   5 | 3 | 5 | 4 | 9 | hi = 3
     *   5 | 3 | 3 | 3 | 7 | hi = 2
     *   5 |  |  |  |  | lo 3 > hi 2 → −1
     */
    // Step 2 · the better way
    public static class BinarySearch {
        public int search(int[] nums, int target) {
            int lo = 0, hi = nums.length - 1;
            while (lo <= hi) {
                int mid = lo + (hi - lo) / 2;       // avoids int overflow
                if (nums[mid] == target) {
                    return mid;
                } else if (nums[mid] < target) {
                    lo = mid + 1;                   // answer is to the right
                } else {
                    hi = mid - 1;                   // answer is to the left
                }
            }
            return -1;
        }
    }

    /*
     * Time: O(log n) · Space: O(1)
     *
     * WATCH OUT
     * - In this "find the exact value" version, loop while lo <= hi (note the
     *   =).
     * - Always move past mid (mid + 1 or mid − 1), or the loop can run
     *   forever.
     * - lo + (hi − lo) / 2 avoids the overflow that (lo + hi) / 2 can hit on
     *   huge arrays.
     */

    // ====================================================================
    // 5. Search Insert Position
    // LeetCode 35 · lower bound · Easy
    // Trick: lo..hi = [0, n]; nums[mid] < target → lo = mid+1 else hi = mid  ·  O(log n)
    // ====================================================================

    /**
     * QUESTION
     * In a sorted array, return the index of target, or the index where it
     * would be inserted to keep the array sorted.
     *
     * EXAMPLE
     *   [1, 3, 5, 6], 5 -> 2     2 -> 1     7 -> 4     0 -> 0
     *
     * STEP 1 · BRUTE FORCE
     * Walk from the left and return the first index whose value is ≥ target.
     *
     * Brute force cost: O(n) time
     */
    // Step 1 · brute force
    public static class SearchInsertLinear {
        public int searchInsert(int[] nums, int target) {
            for (int i = 0; i < nums.length; i++) {
                if (nums[i] >= target) return i;       // first spot that fits
            }
            return nums.length;  // bigger than everything
        }
    }

    /**
     * STEP 2 · THE BETTER IDEA
     * You want the first index whose value is ≥ target, usually called "lower
     * bound". Search over [0, n] (n is a valid answer). If nums[mid] <
     * target, the answer is to the right; otherwise mid itself might be the
     * answer, so keep it with hi = mid.
     *
     * DRY RUN ([1, 3, 5, 6], TARGET 2)
     *   lo 0 hi 4 mid 2: 5 >= 2 -> hi = 2
     *   lo 0 hi 2 mid 1: 3 >= 2 -> hi = 1
     *   lo 0 hi 1 mid 0: 1 <  2 -> lo = 1      lo == hi -> 1
     */
    // Step 2 · the better way
    public static class SearchInsert {
        public int searchInsert(int[] nums, int target) {
            int lo = 0, hi = nums.length;              // answer is in [0, n]
            while (lo < hi) {
                int mid = lo + (hi - lo) / 2;
                if (nums[mid] < target) {
                    lo = mid + 1;  // answer is right of mid
                } else {
                    hi = mid;  // mid could be the answer
                }
            }
            return lo;  // first index >= target
        }
    }

    /*
     * Time: O(log n) · Space: O(1)
     *
     * LEARN THIS TEMPLATE BY HEART
     * while (lo < hi) with hi = mid / lo = mid + 1 finds "the first position
     * where a condition becomes true". It's the core of the next four
     * problems.
     */

    // ====================================================================
    // 6. First Bad Version
    // LeetCode 278 · Easy
    // Trick: binary search for the first true of isBadVersion  ·  O(log n)
    // ====================================================================

    /**
     * QUESTION
     * Versions 1..n are good up to some point, and every version after that
     * is bad. Using the API isBadVersion(v), find the first bad one with as
     * few calls as possible.
     *
     * THE API YOU'RE GIVEN
     */
    // Step 2 · the better way
    // LeetCode gives you isBadVersion(); this stand-in lets us run it
    public static class VersionControl {
        int firstBad;

        boolean isBadVersion(int version) {
            return version >= firstBad;
        }
    }

    /**
     * EXAMPLE
     *   n = 5, first bad = 4:   good good good BAD BAD   ->  4
     *
     * STEP 1 · BRUTE FORCE
     * Ask about versions 1, 2, 3, … until one is bad.
     *
     * Brute force cost: O(n) API calls. With n = 2³¹ that's billions.
     */
    // Step 1 · brute force
    public static class FirstBadLinear extends VersionControl {
        public int firstBadVersion(int n) {
            for (int v = 1; v <= n; v++) {
                if (isBadVersion(v)) return v;  // ask about every version
            }
            return n;
        }
    }

    /**
     * STEP 2 · THE BETTER IDEA
     * The answers look like false false … true true, so binary search for the
     * first true. If mid is bad, the first bad is mid or earlier (hi = mid).
     * Otherwise it's after mid (lo = mid + 1).
     */
    // Step 2 · the better way
    public static class FirstBadVersion extends VersionControl {
        public int firstBadVersion(int n) {
            int lo = 1, hi = n;
            while (lo < hi) {
                int mid = lo + (hi - lo) / 2;         // NOT (lo + hi) / 2
                if (isBadVersion(mid)) {
                    hi = mid;  // first bad is mid or before
                } else {
                    lo = mid + 1;  // first bad is after mid
                }
            }
            return lo;
        }
    }

    /*
     * Time: O(log n) API calls · Space: O(1)
     *
     * WATCH OUT
     * - (lo + hi) / 2 overflows when both are near 2³¹. lo + (hi − lo) / 2
     *   doesn't. This is the classic place interviewers check for it.
     */

    // ====================================================================
    // 7. Last Stone Weight
    // LeetCode 1046 · max-heap · Easy
    // Trick: max-heap; smash two heaviest; push the difference  ·  O(n log n)
    // ====================================================================

    /**
     * QUESTION
     * Each turn, smash the two heaviest stones. If they're equal, both
     * disappear; otherwise the difference goes back in. Return the last
     * stone's weight, or 0 if none is left.
     *
     * EXAMPLE
     *   [2, 7, 4, 1, 8, 1]  ->  1
     *   8 vs 7 -> 1    4 vs 2 -> 2    2 vs 1 -> 1    1 vs 1 -> gone    -> 1
     *
     * STEP 1 · BRUTE FORCE
     * Re-sort the list every round and take the two largest.
     *
     * Brute force cost: O(n² log n) time
     */
    // Step 1 · brute force
    public static class LastStoneSort {
        public int lastStoneWeight(int[] stones) {
            List<Integer> list = new ArrayList<>();
            for (int s : stones) list.add(s);
            while (list.size() > 1) {
                Collections.sort(list);                // sort EVERY round
                int y = list.remove(list.size() - 1);  // heaviest
                int x = list.remove(list.size() - 1);  // second heaviest
                if (y != x) list.add(y - x);
            }
            return list.isEmpty() ? 0 : list.get(0);
        }
    }

    /**
     * STEP 2 · THE BETTER IDEA
     * A max-heap always gives the heaviest stone in O(log n). Java's
     * PriorityQueue is a min-heap, so pass Collections.reverseOrder() to flip
     * it.
     */
    // Step 2 · the better way
    public static class LastStone {
        public int lastStoneWeight(int[] stones) {
            PriorityQueue<Integer> heap =
                    // max-heap
                    new PriorityQueue<>(Collections.reverseOrder());
            for (int s : stones) heap.add(s);
            while (heap.size() > 1) {
                int y = heap.poll();                   // heaviest
                int x = heap.poll();                   // second heaviest
                if (y != x) heap.add(y - x);  // what's left goes back
            }
            return heap.isEmpty() ? 0 : heap.peek();
        }
    }

    /*
     * Time: O(n log n) · Space: O(n)
     */

}
