package karat.intervals;

import java.util.*;
import karat.T;
import karat.intervals.IntervalsEasy.*;
import karat.intervals.IntervalsMedium.*;

/** Runs every example from Part 5's PDFs against the code. */
public class IntervalsTests {
    public static void main(String[] args) {
        Examples1.run();
        Examples2.run();
        Examples3.run();
        Examples4.run();
        T.done("Intervals");
    }

    static class Examples1 {
        static void run() {
            MergeIntervals mi = new MergeIntervals();
            T.check("merge1", mi.merge(new int[][] {{8, 10}, {1, 3}, {2, 6}, {15, 18}}), new int[][] {{1, 6}, {8, 10}, {15, 18}});
            T.check("merge2", mi.merge(new int[][] {{1, 4}, {4, 5}}), new int[][] {{1, 5}});
            T.check("merge3", mi.merge(new int[][] {{1, 10}, {2, 3}, {4, 5}}), new int[][] {{1, 10}});
            T.check("merge4", mi.merge(new int[][] {{5, 7}}), new int[][] {{5, 7}});

            KaratFreeTime ft = new KaratFreeTime();
            int[][] meetings = {{1030, 1200}, {900, 1000}, {1500, 1600}, {1130, 1230}};
            T.check("fit1", ft.canSchedule(meetings, 1000, 1030), true);
            T.check("fit2", ft.canSchedule(meetings, 1145, 1300), false);
            T.check("fit3", ft.canSchedule(meetings, 1230, 1500), true);
            T.check("fit4", ft.canSchedule(meetings, 850, 905), false);
            T.check("free1", ft.freeSlots(meetings, 800, 1800),
                    Arrays.asList(new int[] {800, 900}, new int[] {1000, 1030}, new int[] {1230, 1500}, new int[] {1600, 1800}));
            T.check("free2", ft.freeSlots(new int[][] {{800, 1800}}, 800, 1800), Arrays.asList());

            LongestUniqueSubstring lu = new LongestUniqueSubstring();
            T.check("lus1", lu.lengthOfLongestSubstring("abcabcbb"), 3);
            T.check("lus2", lu.lengthOfLongestSubstring("bbbbb"), 1);
            T.check("lus3", lu.lengthOfLongestSubstring("pwwkew"), 3);
            T.check("lus4", lu.lengthOfLongestSubstring(""), 0);
            T.check("lus5", lu.lengthOfLongestSubstring("abba"), 2);
            T.check("lus6", lu.lengthOfLongestSubstring("karatcode"), 7);

            KaratBadgeRecords br = new KaratBadgeRecords();
            String[][] recs = {
                {"Asha", "enter"}, {"Ravi", "enter"}, {"Asha", "exit"}, {"Meera", "exit"}, {"Ravi", "enter"},
                {"Ravi", "exit"}, {"Kiran", "enter"}, {"Meera", "enter"}, {"Meera", "exit"}
            };
            T.check("badge1", br.mismatches(recs), Arrays.asList(Arrays.asList("Kiran", "Ravi"), Arrays.asList("Meera")));
            T.check("badge2", br.mismatches(new String[][] {{"A", "enter"}, {"A", "exit"}}),
                    Arrays.asList(Arrays.asList(), Arrays.asList()));

            KeyCardAlert ka = new KeyCardAlert();
            T.check("alert1", ka.alertNames(new String[] {"daniel", "daniel", "daniel", "luis", "luis", "luis", "luis"},
                    new String[] {"10:00", "10:40", "11:00", "09:00", "11:00", "13:00", "15:00"}), Arrays.asList("daniel"));
            T.check("alert2", ka.alertNames(new String[] {"asha", "asha", "asha", "ravi", "ravi", "ravi"},
                    new String[] {"12:01", "12:00", "18:00", "21:00", "21:20", "21:30"}), Arrays.asList("ravi"));
            T.check("alert3", ka.alertNames(new String[] {"x", "x", "x"}, new String[] {"23:00", "23:30", "23:59"}),
                    Arrays.asList("x"));

            HighAccessEmployees he = new HighAccessEmployees();
            List<String> got = he.findHighAccessEmployees(Arrays.asList(
                    Arrays.asList("a", "0549"), Arrays.asList("b", "0457"), Arrays.asList("a", "0532"),
                    Arrays.asList("a", "0621"), Arrays.asList("b", "0540")));
            T.check("high1", got, Arrays.asList("a"));
            List<String> got2 = he.findHighAccessEmployees(Arrays.asList(
                    Arrays.asList("x", "1000"), Arrays.asList("x", "1030"), Arrays.asList("x", "1100")));
            T.check("high2 boundary", got2, Arrays.asList());
            List<String> got3 = he.findHighAccessEmployees(Arrays.asList(
                    Arrays.asList("x", "1000"), Arrays.asList("x", "1030"), Arrays.asList("x", "1059")));
            T.check("high3", got3, Arrays.asList("x"));

            KaratBadgeTimes bt = new KaratBadgeTimes();
            String[][] times = {
                {"Asha", "800"}, {"Ravi", "930"}, {"Asha", "845"}, {"Asha", "855"}, {"Ravi", "1100"},
                {"Asha", "900"}, {"Asha", "1300"}, {"Ravi", "1215"}, {"Meera", "1400"}, {"Meera", "1430"}, {"Meera", "1459"}
            };
            T.check("btimes", bt.badgedOften(times).toString(), "{Asha=[800, 845, 855, 900], Meera=[1400, 1430, 1459]}");
        }
    }

    static class Examples2 {
        static void run() {
            MergeIntervalsBrute mb = new MergeIntervalsBrute();
            T.check("mergeB1", mb.merge(new int[][] {{8, 10}, {1, 3}, {2, 6}, {15, 18}}), new int[][] {{1, 6}, {8, 10}, {15, 18}});
            T.check("mergeB2", mb.merge(new int[][] {{1, 4}, {4, 5}}), new int[][] {{1, 5}});
            T.check("mergeB3", mb.merge(new int[][] {{1, 10}, {2, 3}, {4, 5}}), new int[][] {{1, 10}});
            T.check("mergeB4", mb.merge(new int[][] {{1, 2}, {5, 6}, {2, 5}}), new int[][] {{1, 6}});

            FreeSlotsMinutes fm = new FreeSlotsMinutes();
            int[][] meetings = {{1030, 1200}, {900, 1000}, {1500, 1600}, {1130, 1230}};
            T.check("freeM", fm.freeSlots(meetings, 800, 1800),
                    Arrays.asList(new int[] {800, 900}, new int[] {1000, 1030}, new int[] {1230, 1500}, new int[] {1600, 1800}));

            LongestUniqueBrute lu = new LongestUniqueBrute();
            T.check("lusB1", lu.lengthOfLongestSubstring("abcabcbb"), 3);
            T.check("lusB2", lu.lengthOfLongestSubstring("pwwkew"), 3);
            T.check("lusB3", lu.lengthOfLongestSubstring(""), 0);
            T.check("lusB4", lu.lengthOfLongestSubstring("abba"), 2);

            KeyCardBrute kb = new KeyCardBrute();
            T.check("alertB1", kb.alertNames(new String[] {"daniel", "daniel", "daniel", "luis", "luis", "luis", "luis"},
                    new String[] {"10:00", "10:40", "11:00", "09:00", "11:00", "13:00", "15:00"}), Arrays.asList("daniel"));
            T.check("alertB2", kb.alertNames(new String[] {"asha", "asha", "asha", "ravi", "ravi", "ravi"},
                    new String[] {"12:01", "12:00", "18:00", "21:00", "21:20", "21:30"}), Arrays.asList("ravi"));
        }
    }

    static class Examples3 {
        static void run() {
            T.check("sum1", new SummaryRanges().summaryRanges(new int[] {0, 1, 2, 4, 5, 7}), "[0->2, 4->5, 7]");
            T.check("sum2", new SummaryRanges().summaryRanges(new int[] {0, 2, 3, 4, 6, 8, 9}), "[0, 2->4, 6, 8->9]");
            T.check("sum3", new SummaryRanges().summaryRanges(new int[] {}), "[]");

            T.check("teemoB", new TeemoBrute().findPoisonedDuration(new int[] {1, 2, 6}, 3), 7);
            T.check("teemo1", new Teemo().findPoisonedDuration(new int[] {1, 2, 6}, 3), 7);
            T.check("teemo2", new Teemo().findPoisonedDuration(new int[] {1, 4}, 2), 4);
            T.check("teemo3", new Teemo().findPoisonedDuration(new int[] {1, 2}, 2), 3);

            int[] avg = {1, 12, -5, -6, 50, 3};
            T.check("avgB", new MaxAverageBrute().findMaxAverage(avg, 4), 12.75);
            T.check("avg1", new MaxAverage().findMaxAverage(avg, 4), 12.75);
            T.check("avg2", new MaxAverage().findMaxAverage(new int[] {5}, 1), 5.0);

            int[][] iv = {{1, 2}, {3, 5}, {6, 7}, {8, 10}, {12, 16}};
            T.check("insM", new InsertIntervalMerge().insert(iv, new int[] {4, 8}), new int[][] {{1, 2}, {3, 10}, {12, 16}});
            T.check("ins1", new InsertInterval().insert(iv, new int[] {4, 8}), new int[][] {{1, 2}, {3, 10}, {12, 16}});
            T.check("ins2", new InsertInterval().insert(new int[][] {{1, 3}, {6, 9}}, new int[] {2, 5}), new int[][] {{1, 5}, {6, 9}});
            T.check("ins3", new InsertInterval().insert(new int[][] {}, new int[] {5, 7}), new int[][] {{5, 7}});
            T.check("ins4", new InsertInterval().insert(new int[][] {{1, 5}}, new int[] {6, 8}), new int[][] {{1, 5}, {6, 8}});

            T.check("non1", new NonOverlapping().eraseOverlapIntervals(new int[][] {{1, 2}, {2, 3}, {3, 4}, {1, 3}}), 1);
            T.check("non2", new NonOverlapping().eraseOverlapIntervals(new int[][] {{1, 2}, {1, 2}, {1, 2}}), 2);
            T.check("non3", new NonOverlapping().eraseOverlapIntervals(new int[][] {{1, 2}, {2, 3}}), 0);

            T.check("arrow1", new MinArrows().findMinArrowShots(new int[][] {{10, 16}, {2, 8}, {1, 6}, {7, 12}}), 2);
            T.check("arrow2", new MinArrows().findMinArrowShots(new int[][] {{1, 2}, {3, 4}, {5, 6}, {7, 8}}), 4);
            T.check("arrow3", new MinArrows().findMinArrowShots(new int[][] {{1, 2}, {2, 3}, {3, 4}, {4, 5}}), 2);
            T.check("arrow4", new MinArrows().findMinArrowShots(new int[][] {{-2147483648, 2147483647}}), 1);

            int[][] a = {{0, 2}, {5, 10}, {13, 23}, {24, 25}};
            int[][] b = {{1, 5}, {8, 12}, {15, 24}, {25, 26}};
            int[][] want = {{1, 2}, {5, 5}, {8, 10}, {15, 23}, {24, 24}, {25, 25}};
            T.check("isecB", new IntersectionsBrute().intervalIntersection(a, b), want);
            T.check("isec1", new Intersections().intervalIntersection(a, b), want);
            T.check("isec2", new Intersections().intervalIntersection(new int[][] {{1, 3}}, new int[][] {}), new int[][] {});
        }
    }

    static class Examples4 {
        static void run() {
            MyCalendarList cl = new MyCalendarList();
            T.check("calL1", cl.book(10, 20), true);
            T.check("calL2", cl.book(15, 25), false);
            T.check("calL3", cl.book(20, 30), true);
            MyCalendar c = new MyCalendar();
            T.check("cal1", c.book(10, 20), true);
            T.check("cal2", c.book(15, 25), false);
            T.check("cal3", c.book(20, 30), true);
            T.check("cal4", c.book(5, 10), true);
            T.check("cal5", c.book(5, 11), false);
            T.check("cal6", c.book(25, 26), false);

            int[][] rooms = {{900, 1030}, {1000, 1100}, {1030, 1200}, {1100, 1130}, {1015, 1045}};
            T.check("roomsB", new MinRoomsBrute().minMeetingRooms(rooms), 3);
            T.check("rooms1", new MinRooms().minMeetingRooms(rooms), 3);
            T.check("rooms2", new MinRooms().minMeetingRooms(new int[][] {{0, 30}, {5, 10}, {15, 20}}), 2);
            T.check("rooms3", new MinRooms().minMeetingRooms(new int[][] {{7, 10}, {2, 4}}), 1);
            T.check("roomsB3", new MinRoomsBrute().minMeetingRooms(new int[][] {{0, 30}, {5, 10}, {15, 20}}), 2);

            int[][] trips = {{2, 1, 5}, {3, 3, 7}};
            T.check("carB1", new CarPoolingBrute().carPooling(trips, 4), false);
            T.check("carB2", new CarPoolingBrute().carPooling(trips, 5), true);
            T.check("car1", new CarPooling().carPooling(trips, 4), false);
            T.check("car2", new CarPooling().carPooling(trips, 5), true);
            T.check("car3", new CarPooling().carPooling(new int[][] {{2, 1, 5}, {3, 5, 7}}, 3), true);

            T.check("minB", new MinSubArrayBrute().minSubArrayLen(7, new int[] {2, 3, 1, 2, 4, 3}), 2);
            T.check("min1", new MinSubArray().minSubArrayLen(7, new int[] {2, 3, 1, 2, 4, 3}), 2);
            T.check("min2", new MinSubArray().minSubArrayLen(4, new int[] {1, 4, 4}), 1);
            T.check("min3", new MinSubArray().minSubArrayLen(11, new int[] {1, 1, 1, 1}), 0);

            int[] ones = {1, 1, 1, 0, 0, 0, 1, 1, 1, 1, 0};
            T.check("onesB", new LongestOnesBrute().longestOnes(ones, 2), 6);
            T.check("ones1", new LongestOnes().longestOnes(ones, 2), 6);
            T.check("ones2", new LongestOnes().longestOnes(new int[] {0, 0, 1, 1, 0, 0, 1, 1, 1, 0, 1, 1, 0, 0, 0, 1, 1, 1, 1}, 3), 10);
            T.check("ones3", new LongestOnes().longestOnes(new int[] {0, 0}, 0), 0);

            T.check("permB1", new PermutationInStringBrute().checkInclusion("ab", "eidbaooo"), true);
            T.check("permB2", new PermutationInStringBrute().checkInclusion("ab", "eidboaoo"), false);
            T.check("perm1", new PermutationInString().checkInclusion("ab", "eidbaooo"), true);
            T.check("perm2", new PermutationInString().checkInclusion("ab", "eidboaoo"), false);
            T.check("perm3", new PermutationInString().checkInclusion("abc", "ab"), false);

            T.check("anaB", new FindAnagramsBrute().findAnagrams("cbaebabacd", "abc"), "[0, 6]");
            T.check("ana1", new FindAnagrams().findAnagrams("cbaebabacd", "abc"), "[0, 6]");
            T.check("ana2", new FindAnagrams().findAnagrams("abab", "ab"), "[0, 1, 2]");
            T.check("ana3", new FindAnagrams().findAnagrams("a", "ab"), "[]");
        }
    }

}
