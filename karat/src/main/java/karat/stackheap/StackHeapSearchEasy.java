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

    // ====================================================================
    // 2. Remove All Adjacent Duplicates in String
    // LeetCode 1047 · Easy
    // Trick: StringBuilder as a stack; same as top → pop, else push  ·  O(n)
    // ====================================================================

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

    // ====================================================================
    // 3. Next Greater Element I
    // LeetCode 496 · monotonic stack · Easy
    // Trick: one pass over nums2 with a stack → map value → next greater  ·  O(n + m)
    // ====================================================================

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

    // ====================================================================
    // 4. Binary Search
    // LeetCode 704 · Easy
    // Trick: lo <= hi; move past mid; lo + (hi−lo)/2  ·  O(log n)
    // ====================================================================

    // Step 1 · brute force
    public static class LinearSearch {
        public int search(int[] nums, int target) {
            for (int i = 0; i < nums.length; i++) {
                if (nums[i] == target) return i;       // check one by one
            }
            return -1;
        }
    }

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

    // ====================================================================
    // 5. Search Insert Position
    // LeetCode 35 · lower bound · Easy
    // Trick: lo..hi = [0, n]; nums[mid] < target → lo = mid+1 else hi = mid  ·  O(log n)
    // ====================================================================

    // Step 1 · brute force
    public static class SearchInsertLinear {
        public int searchInsert(int[] nums, int target) {
            for (int i = 0; i < nums.length; i++) {
                if (nums[i] >= target) return i;       // first spot that fits
            }
            return nums.length;  // bigger than everything
        }
    }

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

    // ====================================================================
    // 6. First Bad Version
    // LeetCode 278 · Easy
    // Trick: binary search for the first true of isBadVersion  ·  O(log n)
    // ====================================================================

    // Step 2 · the better way
    // LeetCode gives you isBadVersion(); this stand-in lets us run it
    public static class VersionControl {
        int firstBad;

        boolean isBadVersion(int version) {
            return version >= firstBad;
        }
    }

    // Step 1 · brute force
    public static class FirstBadLinear extends VersionControl {
        public int firstBadVersion(int n) {
            for (int v = 1; v <= n; v++) {
                if (isBadVersion(v)) return v;  // ask about every version
            }
            return n;
        }
    }

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

    // ====================================================================
    // 7. Last Stone Weight
    // LeetCode 1046 · max-heap · Easy
    // Trick: max-heap; smash two heaviest; push the difference  ·  O(n log n)
    // ====================================================================

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

}
