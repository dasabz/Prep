package karat.stackheap;

import java.util.*;
import karat.T;
import karat.stackheap.StackHeapSearchEasy.*;
import karat.stackheap.StackHeapSearchMedium.*;

/** Runs every example from Part 7's PDFs against the code. */
public class StackHeapSearchTests {
    public static void main(String[] args) {
        Examples1.run();
        Examples2.run();
        Examples3.run();
        Examples4.run();
        T.done("StackHeapSearch");
    }

    static class Examples1 {
        static void run() {
            ValidParentheses vp = new ValidParentheses();
            T.check("paren1", vp.isValid("([]{})"), true);
            T.check("paren2", vp.isValid("([)]"), false);
            T.check("paren3", vp.isValid("(("), false);
            T.check("paren4", vp.isValid("]"), false);
            T.check("paren5", vp.isValid(""), true);
            T.check("paren6", vp.isValid("{[()()]}"), true);

            DailyTemperatures dt = new DailyTemperatures();
            T.check("temps1", dt.dailyTemperatures(new int[] {73, 74, 75, 71, 69, 72, 76, 73}), new int[] {1, 1, 4, 2, 1, 1, 0, 0});
            T.check("temps2", dt.dailyTemperatures(new int[] {30, 31, 29, 33}), new int[] {1, 2, 1, 0});
            T.check("temps3", dt.dailyTemperatures(new int[] {40, 30, 20}), new int[] {0, 0, 0});

            BinarySearch bs = new BinarySearch();
            int[] sorted = {-3, 0, 4, 7, 9, 15};
            T.check("bs1", bs.search(sorted, 9), 4);
            T.check("bs2", bs.search(sorted, 5), -1);
            T.check("bs3", bs.search(sorted, -3), 0);
            T.check("bs4", bs.search(sorted, 15), 5);
            T.check("bs5", bs.search(new int[] {}, 1), -1);

            KokoBananas kb = new KokoBananas();
            T.check("koko1", kb.minEatingSpeed(new int[] {3, 6, 7, 11}, 8), 4);
            T.check("koko2", kb.minEatingSpeed(new int[] {30, 11, 23, 4, 20}, 5), 30);
            T.check("koko3", kb.minEatingSpeed(new int[] {30, 11, 23, 4, 20}, 6), 23);
            T.check("koko4", kb.minEatingSpeed(new int[] {1000000000}, 2), 500000000);
            T.check("koko5", kb.minEatingSpeed(new int[] {5, 8, 6}, 6), 4);

            KthLargest kl = new KthLargest();
            T.check("kth1", kl.findKthLargest(new int[] {3, 2, 1, 5, 6, 4}, 2), 5);
            T.check("kth2", kl.findKthLargest(new int[] {3, 2, 3, 1, 2, 4, 5, 5, 6}, 4), 4);
            T.check("kth3", kl.findKthLargest(new int[] {7}, 1), 7);

            LRUCache c = new LRUCache(2);
            c.put(1, 10);
            c.put(2, 20);
            T.check("lru get1", c.get(1), 10);     // 1 is now newest
            c.put(3, 30);                          // evicts 2
            T.check("lru get2", c.get(2), -1);
            c.put(4, 40);                          // evicts 1
            T.check("lru get1b", c.get(1), -1);
            T.check("lru get3", c.get(3), 30);
            T.check("lru get4", c.get(4), 40);
            c.put(3, 33);                          // update, 3 newest
            c.put(5, 50);                          // evicts 4
            T.check("lru get4b", c.get(4), -1);
            T.check("lru get3b", c.get(3), 33);

            LRUCacheSimple s = new LRUCacheSimple(2);
            s.put(1, 10);
            s.put(2, 20);
            T.check("lrus get1", s.get(1), 10);
            s.put(3, 30);
            T.check("lrus get2", s.get(2), -1);
            s.put(4, 40);
            T.check("lrus get1b", s.get(1), -1);
            T.check("lrus get3", s.get(3), 30);
            T.check("lrus get4", s.get(4), 40);
        }
    }

    static class Examples2 {
        static void run() {
            ParenthesesReplace pr = new ParenthesesReplace();
            T.check("parR1", pr.isValid("([]{})"), true);
            T.check("parR2", pr.isValid("([)]"), false);
            T.check("parR3", pr.isValid("{[()()]}"), true);
            T.check("parR4", pr.isValid("(("), false);

            T.check("tempB", new DailyTempsBrute().dailyTemperatures(new int[] {73, 74, 75, 71, 69, 72, 76, 73}),
                    new int[] {1, 1, 4, 2, 1, 1, 0, 0});

            T.check("lin1", new LinearSearch().search(new int[] {-3, 0, 4, 7, 9, 15}, 9), 4);
            T.check("lin2", new LinearSearch().search(new int[] {-3, 0, 4, 7, 9, 15}, 5), -1);

            T.check("kokoB1", new KokoBrute().minEatingSpeed(new int[] {3, 6, 7, 11}, 8), 4);
            T.check("kokoB2", new KokoBrute().minEatingSpeed(new int[] {30, 11, 23, 4, 20}, 6), 23);

            T.check("kthS", new KthLargestSort().findKthLargest(new int[] {3, 2, 3, 1, 2, 4, 5, 5, 6}, 4), 4);

            LRUCacheList c = new LRUCacheList(2);
            c.put(1, 10);
            c.put(2, 20);
            T.check("lruL1", c.get(1), 10);
            c.put(3, 30);
            T.check("lruL2", c.get(2), -1);
            c.put(4, 40);
            T.check("lruL3", c.get(1), -1);
            T.check("lruL4", c.get(3), 30);
            T.check("lruL5", c.get(4), 40);
            c.put(3, 33);
            c.put(5, 50);
            T.check("lruL6", c.get(4), -1);
            T.check("lruL7", c.get(3), 33);
        }
    }

    static class Examples3 {
        static void run() {
            T.check("dupB1", new RemoveDuplicatesBrute().removeDuplicates("abbaca"), "ca");
            T.check("dup1", new RemoveDuplicates().removeDuplicates("abbaca"), "ca");
            T.check("dup2", new RemoveDuplicates().removeDuplicates("azxxzy"), "ay");
            T.check("dup3", new RemoveDuplicates().removeDuplicates("aa"), "");

            int[] n1 = {4, 1, 2}, n2 = {1, 3, 4, 2};
            T.check("ngB", new NextGreaterBrute().nextGreaterElement(n1, n2), new int[] {-1, 3, -1});
            T.check("ng1", new NextGreater().nextGreaterElement(n1, n2), new int[] {-1, 3, -1});
            T.check("ng2", new NextGreater().nextGreaterElement(new int[] {2, 4}, new int[] {1, 2, 3, 4}), new int[] {3, -1});

            MinStackScan ms = new MinStackScan();
            ms.push(-2); ms.push(0); ms.push(-3);
            T.check("msS1", ms.getMin(), -3);
            ms.pop();
            T.check("msS2", ms.top(), 0);
            T.check("msS3", ms.getMin(), -2);
            MinStack m = new MinStack();
            m.push(-2); m.push(0); m.push(-3);
            T.check("ms1", m.getMin(), -3);
            m.pop();
            T.check("ms2", m.top(), 0);
            T.check("ms3", m.getMin(), -2);
            m.push(-2);
            m.pop();
            T.check("ms4", m.getMin(), -2);

            T.check("rpn1", new EvalRPN().evalRPN(new String[] {"2", "1", "+", "3", "*"}), 9);
            T.check("rpn2", new EvalRPN().evalRPN(new String[] {"4", "13", "5", "/", "+"}), 6);
            T.check("rpn3", new EvalRPN().evalRPN(new String[] {"10", "6", "9", "3", "+", "-11", "*", "/", "*", "17", "+", "5", "+"}), 22);

            T.check("astB1", new AsteroidsBrute().asteroidCollision(new int[] {5, 10, -5}), new int[] {5, 10});
            T.check("astB2", new AsteroidsBrute().asteroidCollision(new int[] {10, 2, -5}), new int[] {10});
            T.check("ast1", new Asteroids().asteroidCollision(new int[] {5, 10, -5}), new int[] {5, 10});
            T.check("ast2", new Asteroids().asteroidCollision(new int[] {8, -8}), new int[] {});
            T.check("ast3", new Asteroids().asteroidCollision(new int[] {10, 2, -5}), new int[] {10});
            T.check("ast4", new Asteroids().asteroidCollision(new int[] {-2, -1, 1, 2}), new int[] {-2, -1, 1, 2});
            T.check("ast5", new Asteroids().asteroidCollision(new int[] {1, -2, -2, -2}), new int[] {-2, -2, -2});
        }
    }

    static class Examples4 {
        static String sortedPts(int[][] p) {
            int[][] c = p.clone();
            Arrays.sort(c, (a, b) -> a[0] != b[0] ? Integer.compare(a[0], b[0]) : Integer.compare(a[1], b[1]));
            return Arrays.deepToString(c);
        }

        static void run() {
            int[] s = {1, 3, 5, 6};
            T.check("insL", new SearchInsertLinear().searchInsert(s, 2), 1);
            T.check("ins1", new SearchInsert().searchInsert(s, 5), 2);
            T.check("ins2", new SearchInsert().searchInsert(s, 2), 1);
            T.check("ins3", new SearchInsert().searchInsert(s, 7), 4);
            T.check("ins4", new SearchInsert().searchInsert(s, 0), 0);

            FirstBadLinear fl = new FirstBadLinear();
            fl.firstBad = 4;
            T.check("badL", fl.firstBadVersion(5), 4);
            FirstBadVersion fb = new FirstBadVersion();
            fb.firstBad = 4;
            T.check("bad1", fb.firstBadVersion(5), 4);
            fb.firstBad = 1;
            T.check("bad2", fb.firstBadVersion(1), 1);
            fb.firstBad = 1702766719;
            T.check("bad3", fb.firstBadVersion(2126753390), 1702766719);

            int[] r = {5, 7, 7, 8, 8, 10};
            T.check("rangeL", new SearchRangeLinear().searchRange(r, 8), new int[] {3, 4});
            T.check("range1", new SearchRange().searchRange(r, 8), new int[] {3, 4});
            T.check("range2", new SearchRange().searchRange(r, 6), new int[] {-1, -1});
            T.check("range3", new SearchRange().searchRange(new int[] {}, 0), new int[] {-1, -1});
            T.check("range4", new SearchRange().searchRange(new int[] {2, 2}, 2), new int[] {0, 1});

            int[] rot = {4, 5, 6, 7, 0, 1, 2};
            T.check("rotL", new LinearSearch().search(rot, 0), 4);
            T.check("rot1", new SearchRotated().search(rot, 0), 4);
            T.check("rot2", new SearchRotated().search(rot, 3), -1);
            T.check("rot3", new SearchRotated().search(new int[] {1}, 0), -1);
            T.check("rot4", new SearchRotated().search(new int[] {5, 1, 3}, 5), 0);
            T.check("rot5", new SearchRotated().search(new int[] {3, 1}, 1), 1);

            TimeMapScan ts = new TimeMapScan();
            ts.set("foo", "bar", 1);
            T.check("tmS1", ts.get("foo", 3), "bar");
            ts.set("foo", "bar2", 4);
            T.check("tmS2", ts.get("foo", 4), "bar2");
            T.check("tmS3", ts.get("foo", 0), "");
            TimeMap tm = new TimeMap();
            tm.set("price", "100", 10);
            tm.set("price", "120", 20);
            tm.set("price", "90", 30);
            T.check("tm1", tm.get("price", 25), "120");
            T.check("tm2", tm.get("price", 30), "90");
            T.check("tm3", tm.get("price", 5), "");
            T.check("tm4", tm.get("cost", 25), "");
            T.check("tm5", tm.get("price", 1000), "90");

            int[] wts = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
            T.check("shipB", new ShipCapacityBrute().shipWithinDays(wts, 5), 15);
            T.check("ship1", new ShipCapacity().shipWithinDays(wts, 5), 15);
            T.check("ship2", new ShipCapacity().shipWithinDays(new int[] {3, 2, 2, 4, 1, 4}, 3), 6);
            T.check("ship3", new ShipCapacity().shipWithinDays(new int[] {1, 2, 3, 1, 1}, 4), 3);

            T.check("stoneS", new LastStoneSort().lastStoneWeight(new int[] {2, 7, 4, 1, 8, 1}), 1);
            T.check("stone1", new LastStone().lastStoneWeight(new int[] {2, 7, 4, 1, 8, 1}), 1);
            T.check("stone2", new LastStone().lastStoneWeight(new int[] {3, 3}), 0);

            T.check("kcS", sortedPts(new KClosestSort().kClosest(new int[][] {{3, 3}, {5, -1}, {-2, 4}}, 2)), "[[-2, 4], [3, 3]]");
            T.check("kc1", sortedPts(new KClosest().kClosest(new int[][] {{3, 3}, {5, -1}, {-2, 4}}, 2)), "[[-2, 4], [3, 3]]");
            T.check("kc2", sortedPts(new KClosest().kClosest(new int[][] {{1, 3}, {-2, 2}}, 1)), "[[-2, 2]]");

            MedianFinderSorted ms = new MedianFinderSorted();
            ms.addNum(1); ms.addNum(2);
            T.check("medS1", ms.findMedian(), 1.5);
            ms.addNum(3);
            T.check("medS2", ms.findMedian(), 2.0);
            MedianFinder mf = new MedianFinder();
            mf.addNum(5);
            T.check("med1", mf.findMedian(), 5.0);
            mf.addNum(15);
            T.check("med2", mf.findMedian(), 10.0);
            mf.addNum(1);
            T.check("med3", mf.findMedian(), 5.0);
            mf.addNum(3);
            T.check("med4", mf.findMedian(), 4.0);
        }
    }

}
