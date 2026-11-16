package karat.strings;

import java.util.*;

/**
 * Part 2 · String Parsing & Simulation · Medium & Hard (15 problems).
 *
 * Explanations, dry runs and pitfalls: pdfs/Karat-2b-Medium-String-Parsing-Simulation.pdf
 * Each problem: Step 1 = brute force, Step 2 = the better way.
 * Every example is checked in src/test/java/karat/strings/StringsTests.java
 */
public final class StringsMedium {

    private StringsMedium() { }

    // ====================================================================
    // 1. Integer to Roman
    // LeetCode 12 · Medium
    // Trick: greedy over 13 values (incl. 900, 400, 90, 40, 9, 4)  ·  O(1)
    // ====================================================================

    /**
     * QUESTION
     * Convert a number from 1 to 3999 into Roman numerals.
     *
     * EXAMPLE
     *   1994  ->  "MCMXCIV"       (M + CM + XC + IV)
     *   3749  ->  "MMMDCCXLIX"
     *
     * STEP 1 · BRUTE FORCE
     * Handle each decimal digit with a lookup table: one table each for
     * thousands, hundreds, tens and ones. It's direct and correct, but you
     * have to type 4 tables correctly under pressure.
     *
     * Brute force cost: O(1), but lots of typing.
     */
    // Step 1 · brute force
    public static class IntegerToRomanTables {
        public String intToRoman(int num) {
            String[] th = {"", "M", "MM", "MMM"};
            String[] hu = {"", "C", "CC", "CCC", "CD",
                           "D", "DC", "DCC", "DCCC", "CM"};
            String[] te = {"", "X", "XX", "XXX", "XL",
                           "L", "LX", "LXX", "LXXX", "XC"};
            String[] on = {"", "I", "II", "III", "IV",
                           "V", "VI", "VII", "VIII", "IX"};
            // one lookup per digit: thousands, hundreds, tens, ones
            return th[num / 1000] + hu[num % 1000 / 100]
                 + te[num % 100 / 10] + on[num % 10];
        }
    }

    /**
     * STEP 2 · THE BETTER IDEA
     * Be greedy. Put the 13 "coins" in a list from largest to smallest,
     * including the six special pairs (900, 400, 90, 40, 9, 4). At each step,
     * use the biggest coin that still fits and subtract it.
     *
     * DRY RUN (1994)
     *   1994 -> biggest fit 1000 -> "M"      994 left
     *    994 -> biggest fit  900 -> "CM"      94 left   (900 is checked before 500)
     *     94 -> biggest fit   90 -> "XC"       4 left
     *      4 -> biggest fit    4 -> "IV"       0 left   ->  "MCMXCIV"
     */
    // Step 2 · the better way
    public static class IntegerToRoman {
        public String intToRoman(int num) {
            int[] values = {1000, 900, 500, 400, 100, 90, 50, 40,
                            10, 9, 5, 4, 1};
            String[] symbols = {"M", "CM", "D", "CD", "C", "XC", "L", "XL",
                                "X", "IX", "V", "IV", "I"};
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < values.length; i++) {
                while (num >= values[i]) {             // biggest that fits
                    sb.append(symbols[i]);
                    num -= values[i];
                }
            }
            return sb.toString();
        }
    }

    /*
     * Time: O(1) (at most 15 symbols) · Space: O(1)
     *
     * WATCH OUT
     * - The values must be in descending order, with the special pairs
     *   slotted in. Leave out 900 and you'll get DCCCC instead of CM.
     */

    // ====================================================================
    // 2. String to Integer (atoi)
    // LeetCode 8 · Medium
    // Trick: spaces → sign → digits → stop; clamp to int range  ·  O(n)
    // ====================================================================

    /**
     * QUESTION
     * Turn a string into an int like C's atoi:
     *
     * 1. Skip leading spaces.
     * 2. Read an optional + or −.
     * 3. Read digits until a non-digit appears.
     * 4. Clamp the result to the int range.
     *
     * Return 0 if there are no digits.
     *
     * EXAMPLE
     *   "   -042"       ->  -42
     *   "1337c0d3"      ->  1337
     *   "words 987"     ->  0
     *   "-91283472332"  ->  -2147483648   (clamped)
     *
     * STEP 1 · BRUTE FORCE
     * There's no slower algorithm here; it's pure careful parsing. The
     * tempting shortcut, Integer.parseInt inside a try/catch, fails on
     * "1337c0d3" and on overflow. Write the four steps explicitly instead.
     *
     * STEP 2 · THE IDEA
     * Walk with an index i through the four stages in order. Build the number
     * in a long, and check the int limits after every digit, so you clamp
     * before the long itself could overflow.
     */
    // Step 2 · the better way
    public static class Atoi {
        public int myAtoi(String s) {
            int i = 0, n = s.length();
            while (i < n && s.charAt(i) == ' ') i++;          // 1) spaces
            int sign = 1;
            if (i < n && (s.charAt(i) == '+' || s.charAt(i) == '-')) {
                if (s.charAt(i) == '-') sign = -1;           // 2) sign
                i++;
            }
            long num = 0;
            while (i < n && Character.isDigit(s.charAt(i))) { // 3) digits
                num = num * 10 + (s.charAt(i) - '0');
                if (sign * num > Integer.MAX_VALUE) return Integer.MAX_VALUE;
                if (sign * num < Integer.MIN_VALUE) return Integer.MIN_VALUE;
                i++;
            }
            return (int) (sign * num);                        // 4) stop
        }
    }

    /*
     * Time: O(n) · Space: O(1)
     *
     * WATCH OUT
     * - Check i < n before every charAt(i). Inputs like "" and "   " exist.
     * - Only one sign is allowed: "+-12" returns 0.
     *
     * KARAT ANGLE
     * Karat questions often start with "parse this input line". Writing the
     * stages in order, with a comment per stage, is exactly the habit they
     * score well.
     */

    // ====================================================================
    // 3. Reverse Words in a String
    // LeetCode 151 · Medium
    // Trick: trim() + split("\\s+"), then append backwards  ·  O(n)
    // ====================================================================

    /**
     * QUESTION
     * Reverse the order of the words. The input may have extra spaces at the
     * ends or between words. The output must have single spaces only.
     *
     * EXAMPLE
     *   "  karat   loves clean code "   ->  "code clean loves karat"
     *
     * STEP 1 · BRUTE FORCE
     * There's no slower version worth writing. trim() + split("\\s+") is
     * already O(n). If the interviewer says "no split", walk the string by
     * hand: skip spaces, collect letters into a word, and add each word to a
     * list.
     *
     * STEP 2 · THE IDEA
     * trim() removes the spaces at both ends. split("\\s+") splits on one or
     * more spaces, so the gaps don't create empty words. Then add the words
     * to a StringBuilder from last to first, with a single space between
     * them.
     *
     * DRY RUN
     *   trim       ->  "karat   loves clean code"
     *   split \s+  ->  ["karat", "loves", "clean", "code"]
     *   backwards  ->  "code" + " " + "clean" + " " + "loves" + " " + "karat"
     */
    // Step 2 · the better way
    public static class ReverseWords {
        public String reverseWords(String s) {
            // trim the ends, then split on one-or-more spaces
            String[] words = s.trim().split("\\s+");
            StringBuilder sb = new StringBuilder();
            for (int i = words.length - 1; i >= 0; i--) {
                sb.append(words[i]);
                if (i > 0) sb.append(' ');
            }
            return sb.toString();
        }
    }

    /*
     * Time: O(n) · Space: O(n)
     *
     * WATCH OUT
     * - split(" ") without trim() and \\s+ gives you empty strings "" for the
     *   extra spaces.
     * - In a regex, "\\s+" means one or more whitespace characters. The
     *   double backslash is how you write \s inside a Java string.
     */

    // ====================================================================
    // 4. Compare Version Numbers
    // LeetCode 165 · Medium
    // Trick: split on \\.; compare as ints; missing part = 0  ·  O(n)
    // ====================================================================

    /**
     * QUESTION
     * Compare two version strings like "1.2" and "1.10". Return −1, 0 or 1.
     * Leading zeros don't matter ("01" = "1"), and a missing part counts as 0
     * ("1.0" = "1").
     *
     * EXAMPLE
     *   "1.2"  vs "1.10"     ->  -1    (2 < 10)
     *   "1.01" vs "1.001"    ->   0
     *   "1.0"  vs "1.0.0.0"  ->   0
     *
     * STEP 1 · BRUTE FORCE
     * The tempting brute force, v1.compareTo(v2), is wrong. As text, "1.10" <
     * "1.9", because '1' < '9'. You must compare number by number, not
     * character by character.
     *
     * STEP 2 · THE IDEA
     * Split both on dots (split("\\."), since . is a regex). Walk up to the
     * longer length, parse each part as an int (or 0 if it's missing), and
     * return at the first difference.
     */
    // Step 2 · the better way
    public static class CompareVersion {
        public int compareVersion(String v1, String v2) {
            String[] a = v1.split("\\.");          // "." is regex: escape it
            String[] b = v2.split("\\.");
            int n = Math.max(a.length, b.length);
            for (int i = 0; i < n; i++) {
                // a missing part counts as 0 ("1.0" == "1")
                int x = i < a.length ? Integer.parseInt(a[i]) : 0;
                int y = i < b.length ? Integer.parseInt(b[i]) : 0;
                if (x != y) return x < y ? -1 : 1;
            }
            return 0;
        }
    }

    /*
     * Time: O(n + m) · Space: O(n + m)
     *
     * WATCH OUT
     * - split(".") returns an empty array, because . matches every character.
     *   Always escape it.
     */

    // ====================================================================
    // 5. Simplify Path
    // LeetCode 71 · Medium
    // Trick: split on "/"; skip "" and "."; ".." pops; else push  ·  O(n)
    // ====================================================================

    /**
     * QUESTION
     * Simplify a Unix-style absolute path. . means the current folder, ..
     * means go up one, repeated slashes count as one, and the result has no
     * trailing slash.
     *
     * EXAMPLE
     *   "/home//foo/"             ->  "/home/foo"
     *   "/a/./b/../../c/"         ->  "/c"
     *   "/../"                    ->  "/"
     *   "/.../a/../b/c/../d/./"   ->  "/.../b/d"    ("..." is a normal name)
     *
     * STEP 1 · BRUTE FORCE
     * Repeated string replacing ("//" → "/", remove "/./", remove "name/..")
     * is fragile: "..." is a valid folder name, and the order of the
     * replacements matters. Don't go there; split it instead.
     *
     * STEP 2 · THE IDEA
     * Split on / and treat the parts like a stack of folders. Skip empty
     * parts and .. For .., pop if the stack isn't empty. Anything else is a
     * folder name, so push it. Finally, join with /.
     *
     * DRY RUN ("/A/./B/../../C/")
     *   parts: "", a, ., b, .., .., c, ""
     *   a -> [a]   . -> skip   b -> [a, b]   .. -> [a]   .. -> [ ]   c -> [c]
     *   -> "/c"
     */
    // Step 2 · the better way
    public static class SimplifyPath {
        public String simplifyPath(String path) {
            Deque<String> folders = new ArrayDeque<>();   // used as a stack
            for (String part : path.split("/")) {
                if (part.isEmpty() || part.equals(".")) {
                    continue;                     // "//" or "/./"
                } else if (part.equals("..")) {
                    if (!folders.isEmpty()) folders.pollLast();  // go up one
                } else {
                    folders.addLast(part);            // a real folder name
                }
            }
            StringBuilder sb = new StringBuilder();
            for (String dir : folders) {              // first to last
                sb.append('/').append(dir);
            }
            return sb.length() == 0 ? "/" : sb.toString();
        }
    }

    /*
     * Time: O(n) · Space: O(n)
     *
     * WATCH OUT
     * - .. at the root does nothing, so check that the stack isn't empty
     *   before you pop.
     * - An empty result must be "/", not "".
     */

    // ====================================================================
    // 6. Reorder Data in Log Files
    // LeetCode 937 · Medium
    // Trick: split at first space; sort letters by (content, id)  ·  O(n log n)
    // ====================================================================

    /**
     * QUESTION
     * Each log line is "identifier content…". A letter-log has words as
     * content; a digit-log has numbers. Put all letter-logs first, sorted by
     * content (if the content ties, by identifier). Digit-logs go after them,
     * in their original order.
     *
     * EXAMPLE
     *   in:   "web2 GET home", "db1 504 13", "web1 GET home", "db2 200 1"
     *   out:  "web1 GET home", "web2 GET home", "db1 504 13", "db2 200 1"
     *
     * STEP 1 · BRUTE FORCE
     * The approach (split into two groups, sort one) is already the natural
     * one. A slower version would only be a worse sort. Put the effort into
     * the comparator instead.
     *
     * STEP 2 · THE IDEA
     * 1. Split each line at the first space into identifier and content.
     * 2. Put it in the letter list or the digit list, depending on the
     *    content's first character.
     * 3. Sort the letter list with a comparator: content first, then
     *    identifier.
     * 4. Return letters followed by digits. Don't sort the digit-logs.
     *
     * DRY RUN
     *   "web2 GET home"  ->  letter   (G is a letter)
     *   "db1 504 13"     ->  digit
     *   "web1 GET home"  ->  letter
     *   "db2 200 1"      ->  digit
     *   sort letters: same content "GET home" -> tie -> web1 < web2
     */
    // Step 2 · the better way
    public static class ReorderLogs {
        public String[] reorderLogFiles(String[] logs) {
            List<String> letterLogs = new ArrayList<>();
            List<String> digitLogs = new ArrayList<>();
            for (String log : logs) {
                String content = log.substring(log.indexOf(' ') + 1);
                if (Character.isDigit(content.charAt(0))) {
                    digitLogs.add(log);
                } else {
                    letterLogs.add(log);
                }
            }
            letterLogs.sort((a, b) -> {
                String idA = a.substring(0, a.indexOf(' '));
                String idB = b.substring(0, b.indexOf(' '));
                String restA = a.substring(a.indexOf(' ') + 1);
                String restB = b.substring(b.indexOf(' ') + 1);
                int cmp = restA.compareTo(restB);   // 1st: by content
                if (cmp != 0) return cmp;
                return idA.compareTo(idB);          // tie: by identifier
            });
            List<String> result = new ArrayList<>(letterLogs);
            result.addAll(digitLogs);               // digit logs keep order
            return result.toArray(new String[0]);
        }
    }

    /*
     * Time: O(n log n · L), where L is the line length · Space: O(n)
     *
     * WATCH OUT
     * - Split at the first space only (indexOf(' ')). The content itself
     *   contains spaces.
     * - Java's List.sort is stable: items that compare equal keep their
     *   original order. That's why we never sort the digit-logs; we just
     *   append them.
     *
     * KARAT ANGLE
     * Karat loves log-style input: badge logs, access logs, click logs. The
     * skill being tested is the same each time: split the line correctly,
     * decide what kind of record it is, then group or sort it.
     */

    // ====================================================================
    // 7. String Compression
    // LeetCode 443 · Medium
    // Trick: read pointer finds runs; write pointer writes char + count  ·  O(n)
    // ====================================================================

    /**
     * QUESTION
     * Compress a char[] in place: each run of the same character becomes the
     * character followed by its count (only when the count is more than 1).
     * Return the new length.
     *
     * EXAMPLE
     *   ['a','a','b','c','c','c']  ->  "a2bc3"   (return 5)
     *   ['a','b' + twelve b's]     ->  "ab12"
     *
     * STEP 1 · BRUTE FORCE
     * Build the compressed text in a separate StringBuilder, then copy it
     * back into the array.
     *
     * Brute force cost: O(n) time but O(n) extra space, and the problem asks
     * for O(1).
     */
    // Step 1 · brute force
    public static class StringCompressionBrute {
        public int compress(char[] chars) {
            StringBuilder sb = new StringBuilder();   // extra memory
            int i = 0;
            while (i < chars.length) {
                int j = i;
                while (j < chars.length && chars[j] == chars[i]) j++;
                sb.append(chars[i]);
                if (j - i > 1) sb.append(j - i);      // count only if > 1
                i = j;
            }
            for (int k = 0; k < sb.length(); k++) chars[k] = sb.charAt(k);
            return sb.length();
        }
    }

    /**
     * STEP 2 · THE BETTER IDEA
     * Use two pointers on the same array. read scans ahead to find the end of
     * each run. write puts down the character and then the digits of the
     * count. Because the compressed form is never longer than the original,
     * write can never overtake read.
     */
    // Step 2 · the better way
    public static class StringCompression {
        public int compress(char[] chars) {
            int write = 0, read = 0;
            while (read < chars.length) {
                char c = chars[read];
                int start = read;
                while (read < chars.length && chars[read] == c) read++;
                chars[write] = c;                     // write the letter
                write++;
                int count = read - start;
                if (count > 1) {
                    for (char d : String.valueOf(count).toCharArray()) {
                        chars[write] = d;             // then each digit
                        write++;
                    }
                }
            }
            return write;
        }
    }

    /*
     * Time: O(n) · Space: O(1)
     *
     * WATCH OUT
     * - A count like 12 is written as two characters, '1' and '2'. That's
     *   what String.valueOf(count) handles.
     */

    // ====================================================================
    // 8. Count and Say
    // LeetCode 38 · Medium
    // Trick: run-length encode the previous string, n − 1 times  ·  O(total length)
    // ====================================================================

    /**
     * QUESTION
     * Start with "1". Each next term reads the previous one aloud: "one 1" →
     * "11", then "two 1s" → "21", then "one 2, one 1" → "1211", and so on.
     * Return the n-th term.
     *
     * EXAMPLE
     *   1: 1     2: 11     3: 21     4: 1211     5: 111221     6: 312211
     *
     * STEP 1 · BRUTE FORCE
     * There's no formula shortcut. You have to simulate every step, so the
     * only real choice is how cleanly you write the run-length loop.
     *
     * STEP 2 · THE IDEA
     * For each step, scan the current string in runs. For each run of equal
     * digits, write the run's length and then the digit.
     *
     * DRY RUN (FROM "1211")
     *   runs: "1" | "2" | "11"
     *   -> one 1, one 2, two 1s  ->  "11" + "12" + "21" = "111221"
     */
    // Step 2 · the better way
    public static class CountAndSay {
        public String countAndSay(int n) {
            String s = "1";
            for (int step = 2; step <= n; step++) {
                StringBuilder next = new StringBuilder();
                int i = 0;
                while (i < s.length()) {
                    int j = i;
                    while (j < s.length() && s.charAt(j) == s.charAt(i)) j++;
                    next.append(j - i).append(s.charAt(i));  // "count, digit"
                    i = j;
                }
                s = next.toString();
            }
            return s;
        }
    }

    /*
     * Time: O(sum of all term lengths) · Space: O(length of the last term)
     *
     * PATTERN
     * "Group consecutive equal items" (the i/j run loop) shows up again in
     * String Compression and in log grouping. Learn it once.
     */

    // ====================================================================
    // 9. Zigzag Conversion
    // LeetCode 6 · Medium
    // Trick: one StringBuilder per row; bounce direction at top/bottom  ·  O(n)
    // ====================================================================

    /**
     * QUESTION
     * Write the string in a zigzag down and up across numRows rows, then read
     * it row by row.
     *
     * EXAMPLE (NUMROWS = 3)
     *   P   A   H   N
     *   A P L S I I G        ->  "PAHNAPLSIIGYIR"
     *   Y   I   R
     *
     * STEP 1 · BRUTE FORCE
     * Draw the zigzag for real in a 2-D char grid (going down a column, then
     * diagonally up and right), then read the grid row by row, skipping the
     * empty cells.
     *
     * Brute force cost: O(n × rows) space, mostly empty cells.
     */
    // Step 1 · brute force
    public static class ZigzagGrid {
        public String convert(String s, int numRows) {
            if (numRows == 1) return s;
            char[][] grid = new char[numRows][s.length()];   // mostly empty
            int row = 0, col = 0;
            boolean down = true;
            for (char c : s.toCharArray()) {
                grid[row][col] = c;
                if (row == 0) down = true;
                else if (row == numRows - 1) down = false;
                if (down) {
                    row++;                            // straight down
                } else {
                    row--;                     // diagonal: up-right
                    col++;
                }
            }
            StringBuilder sb = new StringBuilder();
            for (char[] r : grid) {
                for (char c : r) {
                    if (c != 0) sb.append(c);         // skip empty cells
                }
            }
            return sb.toString();
        }
    }

    /**
     * STEP 2 · THE BETTER IDEA
     * You don't need the columns at all, only which row each character lands
     * in. Keep one StringBuilder per row, plus a row index that moves down,
     * bounces at the bottom, moves up, and bounces at the top. Then glue the
     * rows together.
     *
     * DRY RUN (FIRST 6 CHARACTERS, 3 ROWS)
     *   P->row0  A->row1  Y->row2 (bounce)  P->row1  A->row0 (bounce)  L->row1
     */
    // Step 2 · the better way
    public static class Zigzag {
        public String convert(String s, int numRows) {
            if (numRows == 1) return s;
            List<StringBuilder> rows = new ArrayList<>();
            for (int i = 0; i < numRows; i++) rows.add(new StringBuilder());
            int row = 0;
            boolean down = true;
            for (char c : s.toCharArray()) {
                rows.get(row).append(c);
                if (row == 0) down = true;                 // bounce at top
                else if (row == numRows - 1) down = false; // bounce at bottom
                row += down ? 1 : -1;
            }
            StringBuilder sb = new StringBuilder();
            for (StringBuilder r : rows) sb.append(r);
            return sb.toString();
        }
    }

    /*
     * Time: O(n) · Space: O(n)
     *
     * WATCH OUT
     * - numRows == 1 must return s straight away. Otherwise the bounce logic
     *   tries to move to row 1, which doesn't exist.
     */

    // ====================================================================
    // 10. Multiply Strings
    // LeetCode 43 · Medium
    // Trick: digit i × digit j lands in slots i+j and i+j+1  ·  O(n × m)
    // ====================================================================

    /**
     * QUESTION
     * Multiply two non-negative numbers given as strings, without converting
     * them to integers.
     *
     * EXAMPLE
     *   "123" × "456"  ->  "56088"
     *   "99"  × "99"   ->  "9801"
     *
     * STEP 1 · BRUTE FORCE
     * Use the school method: multiply a by each digit of b, shift by adding
     * zeros, and add each partial product into a running total with Add
     * Strings.
     *
     * Brute force cost: O(n × m) digit work, plus lots of string building and
     * copying.
     */
    // Step 1 · brute force
    public static class MultiplyStringsBrute {
        public String multiply(String a, String b) {
            if (a.equals("0") || b.equals("0")) return "0";
            String total = "0";
            for (int j = b.length() - 1; j >= 0; j--) {     // each digit of b
                int d = b.charAt(j) - '0';
                StringBuilder part = new StringBuilder();   // a × d
                int carry = 0;
                for (int i = a.length() - 1; i >= 0; i--) {
                    int p = (a.charAt(i) - '0') * d + carry;
                    part.append(p % 10);
                    carry = p / 10;
                }
                if (carry > 0) part.append(carry);
                part.reverse();
                for (int z = 0; z < b.length() - 1 - j; z++) {
                    part.append('0');                       // shift left
                }
                total = add(total, part.toString());        // school method
            }
            return total;
        }

        private String add(String a, String b) {           // = Add Strings
            StringBuilder sb = new StringBuilder();
            int i = a.length() - 1, j = b.length() - 1, carry = 0;
            while (i >= 0 || j >= 0 || carry > 0) {
                int sum = carry;
                if (i >= 0) sum += a.charAt(i--) - '0';
                if (j >= 0) sum += b.charAt(j--) - '0';
                sb.append(sum % 10);
                carry = sum / 10;
            }
            return sb.reverse().toString();
        }
    }

    /**
     * STEP 2 · THE BETTER IDEA
     * Skip the partial strings entirely. The product of a[i] and b[j] always
     * lands in slots i + j and i + j + 1 of a result array of size n + m. Add
     * each product into pos[i + j + 1], keep that slot's last digit, and push
     * the carry into pos[i + j]. Strip leading zeros at the end.
     *
     * DRY RUN ("99" × "99", RESULT ARRAY HAS 4 SLOTS)
     *   i=1,j=1: 9*9=81 -> slot3=1, slot2+=8     [0, 0, 8, 1]
     *   i=1,j=0: 9*9+8=89 -> slot2=9, slot1+=8   [0, 8, 9, 1]
     *   i=0,j=1: 9*9+9=90 -> slot2=0, slot1+=9   [0,17, 0, 1]
     *   i=0,j=0: 9*9+17=98 -> slot1=8, slot0+=9  [9, 8, 0, 1]  ->  "9801"
     */
    // Step 2 · the better way
    public static class MultiplyStrings {
        public String multiply(String a, String b) {
            int[] pos = new int[a.length() + b.length()];   // answer digits
            for (int i = a.length() - 1; i >= 0; i--) {
                for (int j = b.length() - 1; j >= 0; j--) {
                    int mul = (a.charAt(i) - '0') * (b.charAt(j) - '0');
                    int sum = mul + pos[i + j + 1];         // add onto slot
                    pos[i + j + 1] = sum % 10;
                    pos[i + j] += sum / 10;                 // carry left
                }
            }
            StringBuilder sb = new StringBuilder();
            for (int d : pos) {
                if (sb.length() == 0 && d == 0) continue;   // leading zeros
                sb.append(d);
            }
            return sb.length() == 0 ? "0" : sb.toString();
        }
    }

    /*
     * Time: O(n × m) · Space: O(n + m)
     *
     * WATCH OUT
     * - If either input is "0", the array is all zeros, so return "0", not
     *   "".
     */

    // ====================================================================
    // 11. Decode String
    // LeetCode 394 · Medium
    // Trick: at [ push (count, text); at ] pop and repeat  ·  O(output)
    // ====================================================================

    /**
     * QUESTION
     * k[text] means "repeat text k times". The brackets can be nested.
     *
     * EXAMPLE
     *   "3[a2[c]]"    ->  "accaccacc"     (inner: 2[c] = cc  ->  3[acc])
     *   "2[ab]3[x]y"  ->  "ababxxxy"
     *
     * STEP 1 · BRUTE FORCE
     * Repeatedly find the innermost bracket pair (the first ] and the last [
     * before it), expand it, and paste the result back into the string until
     * no brackets are left.
     *
     * Brute force cost: O(output × nesting depth) time, because the whole
     * string is rebuilt after every expansion. Easy to reason about, but
     * slow.
     */
    // Step 1 · brute force
    public static class DecodeStringBrute {
        public String decodeString(String s) {
            while (s.contains("[")) {
                // innermost pair: the first ']' and the last '[' before it
                int close = s.indexOf(']');
                int open = s.lastIndexOf('[', close);
                int start = open;                      // walk back over digits
                while (start > 0 && Character.isDigit(s.charAt(start - 1))) {
                    start--;
                }
                int times = Integer.parseInt(s.substring(start, open));
                String inside = s.substring(open + 1, close);
                String expanded = inside.repeat(times);
                s = s.substring(0, start) + expanded + s.substring(close + 1);
            }
            return s;
        }
    }

    /**
     * STEP 2 · THE BETTER IDEA
     * Nested brackets call for a stack. When you see [, save what you've
     * built so far and the repeat count, then start fresh inside. When you
     * see ], repeat the inside part k times and glue it onto the text you
     * saved.
     *
     * DRY RUN ("3[A2[C]]")
     *   char | action | cur | stacks (counts / outer)
     *   3 | num = 3 | "" | –
     *   [ | push 3 and "" | "" | [3] / [""]
     *   a | append | "a" | [3] / [""]
     *   2 | num = 2 | "a" |
     *   [ | push 2 and "a" | "" | [3,2] / ["","a"]
     *   c | append | "c" |
     *   ] | pop 2, "a" → "a"+"cc" | "acc" | [3] / [""]
     *   ] | pop 3, "" → "acc"×3 | "accaccacc" | empty
     */
    // Step 2 · the better way
    public static class DecodeString {
        public String decodeString(String s) {
            Deque<Integer> counts = new ArrayDeque<>();
            Deque<StringBuilder> outer = new ArrayDeque<>();
            StringBuilder cur = new StringBuilder();
            int num = 0;
            for (char c : s.toCharArray()) {
                if (Character.isDigit(c)) {
                    num = num * 10 + (c - '0');     // numbers like "12"
                } else if (c == '[') {
                    counts.push(num);               // save repeat count
                    outer.push(cur);                // save text so far
                    cur = new StringBuilder();      // start fresh inside
                    num = 0;
                } else if (c == ']') {
                    int times = counts.pop();
                    StringBuilder prev = outer.pop();
                    for (int i = 0; i < times; i++) {
                        prev.append(cur);           // repeat inside part
                    }
                    cur = prev;
                } else {
                    cur.append(c);                  // normal letter
                }
            }
            return cur.toString();
        }
    }

    /*
     * Time: O(length of the output) · Space: O(output)
     *
     * WATCH OUT
     * - Repeat counts can have several digits ("10[z]"). Build them with num
     *   * 10 + digit.
     * - Reset num = 0 after you push it.
     */

    // ====================================================================
    // 12. Basic Calculator II
    // LeetCode 227 · the * and / version · Medium
    // Trick: apply the op BEFORE each number; * and / change the last term  ·  O(n)
    // ====================================================================

    /**
     * QUESTION
     * Evaluate an expression with non-negative integers, + − * / and spaces,
     * but no brackets. * and / come before + and −, and division truncates
     * toward zero.
     *
     * EXAMPLE
     *   "3+2*2"      ->  7
     *   " 3+5 / 2 "  ->  5
     *   "2*3-4/2+1"  ->  5
     *
     * STEP 1 · BRUTE FORCE
     * Use a stack of terms. Remember the operator that came before the
     * current number. On +, push the number; on −, push its negative; on * or
     * /, pop the last term, combine it with the number, and push the result.
     * At the end, add up the stack.
     *
     * Brute force cost: O(n) time, O(n) space for the stack.
     */
    // Step 1 · brute force
    public static class CalculatorIIStack {
        public int calculate(String s) {
            Deque<Integer> stack = new ArrayDeque<>();     // finished terms
            int num = 0;
            char op = '+';                                 // op BEFORE num
            for (int i = 0; i < s.length(); i++) {
                char c = s.charAt(i);
                if (Character.isDigit(c)) num = num * 10 + (c - '0');
                boolean end = (i == s.length() - 1);
                if ((!Character.isDigit(c) && c != ' ') || end) {
                    if (op == '+') stack.push(num);
                    else if (op == '-') stack.push(-num);
                    else if (op == '*') stack.push(stack.pop() * num);
                    else stack.push(stack.pop() / num);    // '/'
                    op = c;
                    num = 0;
                }
            }
            int result = 0;
            for (int term : stack) result += term;        // add everything up
            return result;
        }
    }

    /**
     * STEP 2 · THE BETTER IDEA
     * Only the last term can still change, because a later * or / affects
     * only that one term. So drop the stack and keep two numbers: result
     * (finished terms) and lastTerm (the open one). On + or −, add lastTerm
     * into result and start a new term. On * or /, update lastTerm in place.
     *
     * DRY RUN ("2*3-4/2+1")
     *   number | op before it | lastTerm | result
     *   2 | + | 2 | 0
     *   3 | * | 6 | 0
     *   4 | − | −4 | 6
     *   2 | / | −2 | 6
     *   1 | + | 1 | 4
     *   end |  |  | 4 + 1 = 5
     */
    // Step 2 · the better way
    public static class CalculatorII {
        public int calculate(String s) {
            int result = 0;      // sum of finished terms
            int lastTerm = 0;    // term still open to * and /
            int num = 0;
            char op = '+';
            for (int i = 0; i < s.length(); i++) {
                char c = s.charAt(i);
                if (Character.isDigit(c)) num = num * 10 + (c - '0');
                boolean end = (i == s.length() - 1);
                if ((!Character.isDigit(c) && c != ' ') || end) {
                    if (op == '+' || op == '-') {
                        result += lastTerm;               // close old term
                        lastTerm = (op == '+') ? num : -num;
                    } else if (op == '*') {
                        lastTerm = lastTerm * num;
                    } else {
                        lastTerm = lastTerm / num;        // '/'
                    }
                    op = c;
                    num = 0;
                }
            }
            return result + lastTerm;
        }
    }

    /*
     * Time: O(n) · Space: O(1)
     *
     * WATCH OUT
     * - Process the operator when you reach the next operator or the last
     *   character. That's what the end flag is for. Forget it and you'll lose
     *   the final number.
     *
     * KARAT LINK
     * This is the natural Part 3 after the + / − and brackets calculator in
     * this part: add * and / by treating the last term as "still open".
     */

    // ====================================================================
    // 13. Longest Palindromic Substring
    // LeetCode 5 · Medium
    // Trick: expand around every centre (letter and gap)  ·  O(n²)
    // ====================================================================

    /**
     * QUESTION
     * Return the longest substring that is a palindrome.
     *
     * EXAMPLE
     *   "babad"   ->  "bab"     ("aba" is also fine)
     *   "cbbd"    ->  "bb"
     *
     * STEP 1 · BRUTE FORCE
     * Check every substring with a palindrome check, keeping the longest.
     * There are O(n²) substrings, and each check costs O(n).
     *
     * Brute force cost: O(n³) time
     */
    // Step 1 · brute force
    public static class LongestPalSubstringBrute {
        public String longestPalindrome(String s) {
            String best = "";
            for (int i = 0; i < s.length(); i++) {
                for (int j = i; j < s.length(); j++) {     // every substring
                    if (j - i + 1 > best.length() && isPal(s, i, j)) {
                        best = s.substring(i, j + 1);
                    }
                }
            }
            return best;
        }

        private boolean isPal(String s, int l, int r) {
            while (l < r) {
                if (s.charAt(l) != s.charAt(r)) return false;
                l++;
                r--;
            }
            return true;
        }
    }

    /**
     * STEP 2 · THE BETTER IDEA
     * Every palindrome has a centre: either a letter (odd length, "aba") or
     * the gap between two letters (even length, "abba"). For each of the 2n −
     * 1 centres, expand outward while both ends match. Keep the longest
     * result.
     *
     * DRY RUN ("CBBD")
     *   centre 'c'      -> "c"   (len 1)
     *   gap c|b         -> no
     *   centre 'b'(1)   -> "b"
     *   gap b|b         -> "bb", then c vs d -> stop   (len 2)  best
     *   ...
     */
    // Step 2 · the better way
    public static class LongestPalSubstring {
        public String longestPalindrome(String s) {
            int bestStart = 0, bestLen = 0;
            for (int c = 0; c < s.length(); c++) {
                int[] odd = expand(s, c, c);           // centre on a letter
                int[] even = expand(s, c, c + 1);      // centre between two
                int[] better = odd[1] >= even[1] ? odd : even;
                if (better[1] > bestLen) {
                    bestStart = better[0];
                    bestLen = better[1];
                }
            }
            return s.substring(bestStart, bestStart + bestLen);
        }

        // grow outwards while both ends match; returns {start, length}
        private int[] expand(String s, int l, int r) {
            while (l >= 0 && r < s.length() && s.charAt(l) == s.charAt(r)) {
                l--;
                r++;
            }
            return new int[] { l + 1, r - l - 1 };
        }
    }

    /*
     * Time: O(n²) · Space: O(1)
     *
     * WATCH OUT
     * - Don't forget the even centres. Testing only odd centres misses "bb".
     * - expand returns once the ends no longer match, so the real palindrome
     *   is l + 1 .. r − 1, which is where {l + 1, r − l − 1} comes from.
     */

    // ====================================================================
    // 14. Basic Calculator  ★ Karat
    // LeetCode 224 · Karat asks this in parts · Hard
    // Trick: result, sign, num; at ( push result & sign  ·  O(n)
    // ====================================================================

    /**
     * QUESTION
     * Evaluate a string expression with non-negative integers, +, -,
     * parentheses and spaces. You can't use eval.
     *
     * EXAMPLE
     *   " 2-1 + 2 "               ->  3
     *   "10 - (4 - 2) + 7"        ->  15
     *   "(1+(4+5+2)-3)+(6+8)"     ->  23
     *
     * STEP 1 · BRUTE FORCE
     * Solve the innermost ( … ) first: evaluate the flat + − expression
     * inside it, paste the number back in, tidy up +-/--, and repeat until
     * there are no brackets.
     *
     * Brute force cost: O(n²) time, because the string is rebuilt for every
     * pair of brackets. It's a great first version to say out loud before the
     * one-pass stack solution.
     */
    // Step 1 · brute force
    public static class BasicCalculatorBrute {
        public int calculate(String s) {
            s = s.replace(" ", "");
            while (s.contains("(")) {
                // solve the innermost ( ... ) and paste the number back in
                int close = s.indexOf(')');
                int open = s.lastIndexOf('(', close);
                int inner = evalFlat(s.substring(open + 1, close));
                s = s.substring(0, open) + inner + s.substring(close + 1);
                s = s.replace("+-", "-").replace("--", "+");  // tidy signs
            }
            return evalFlat(s);
        }

        // only digits, + and - (no brackets)
        private int evalFlat(String s) {
            int result = 0, sign = 1, num = 0;
            for (char c : s.toCharArray()) {
                if (Character.isDigit(c)) {
                    num = num * 10 + (c - '0');
                } else {
                    result += sign * num;
                    num = 0;
                    sign = (c == '+') ? 1 : -1;
                }
            }
            return result + sign * num;
        }
    }

    /**
     * STEP 2 · THE BETTER IDEA (BUILD IT IN TWO STEPS, LIKE KARAT DOES)
     * Step 1: only + and −. Keep a running result and a sign (+1 or −1). When
     * you reach an operator, the number you just finished reading is
     * complete, so result += sign * num.
     *
     * Step 2: add parentheses. At (, push the current result and sign and
     * start a fresh calculation inside. At ), finish the inside value, then
     * combine it: result = savedResult + savedSign * inside.
     *
     * DRY RUN ("10 - (4 - 2) + 7")
     *   char | what happens | result | sign | stack
     *   1, 0 | num = 10 | 0 | +1 |
     *   − | result += 10, sign = −1 | 10 | −1 |
     *   ( | push 10, push −1; reset | 0 | +1 | [10, −1]
     *   4 | num = 4 | 0 | +1 |
     *   − | result += 4, sign = −1 | 4 | −1 |
     *   2 | num = 2 | 4 | −1 |
     *   ) | result = 4 − 2 = 2; then 10 + (−1)(2) | 8 |  | [ ]
     *   + | result += 0, sign = +1 | 8 | +1 |
     *   7 | num = 7 | 8 | +1 |
     *   end | 8 + 7 | 15 |  |
     */
    // Step 2 · the better way
    public static class BasicCalculator {
        public int calculate(String s) {
            int result = 0;   // total so far at the current bracket level
            int sign = 1;     // +1 or -1, applies to the next number
            int num = 0;      // the number we are reading
            Deque<Integer> stack = new ArrayDeque<>();

            for (char c : s.toCharArray()) {
                if (Character.isDigit(c)) {
                    num = num * 10 + (c - '0');
                } else if (c == '+' || c == '-') {
                    result += sign * num;        // finish previous number
                    num = 0;
                    sign = (c == '+') ? 1 : -1;
                } else if (c == '(') {
                    stack.push(result);          // save total outside (
                    stack.push(sign);            // save sign before (
                    result = 0;                  // start fresh inside
                    sign = 1;
                } else if (c == ')') {
                    result += sign * num;        // finish last number inside
                    num = 0;
                    int signBefore = stack.pop();
                    int totalBefore = stack.pop();
                    result = totalBefore + signBefore * result;
                }
                // spaces: do nothing
            }
            return result + sign * num;          // add the last number
        }
    }

    /*
     * Time: O(n) · Space: O(n) for nested parentheses
     *
     * WATCH OUT
     * - Don't forget the last number. It's still sitting in num when the loop
     *   ends, so return result + sign * num.
     * - A leading minus such as "-(2+3)" works: sign becomes −1 before the (,
     *   so the inside gets negated.
     * - Push in the order result, sign, and pop in reverse: sign first.
     *
     * KARAT-STYLE PARTS
     * - Part 1: + and − only, e.g. "6+9-12" → 3.
     * - Part 2: add parentheses (the code above).
     * - Part 3 (sometimes): variables. You get a map such as {e=8, y=7};
     *   substitute known values, and keep unknown ones as text in the result.
     *   Read letters the way you read digits, and look each one up in the
     *   map.
     */

    // ====================================================================
    // 15. Word Wrap → Text Justification  ★ Karat
    // LeetCode 68 · Karat asks wrap first, then justify · Hard
    // Trick: greedy fit counting gaps; each = sp/gaps, extras go left  ·  O(chars)
    // ====================================================================

    /**
     * QUESTION
     * Part 1: wrap. Fit words into lines of at most maxWidth characters, with
     * words separated by one space. Put as many words on each line as fit.
     *
     * Part 2: justify. Every line must be exactly maxWidth long. Spread the
     * spaces between words as evenly as possible; if they don't divide
     * evenly, the gaps on the left get the extra spaces. The last line, and
     * any line with a single word, is left-aligned and padded with spaces on
     * the right.
     *
     * EXAMPLE (MAXWIDTH = 13)
     *   words = ["I", "love", "clean", "and", "simple", "code."]
     *
     *   Part 1 wrap:          Part 2 justify (| marks the line ends):
     *   "I love clean"        |I  love clean|     3 spaces, 2 gaps -> 2 + 1
     *   "and simple"          |and    simple|     4 spaces, 1 gap
     *   "code."               |code.        |     last line: pad right
     *
     * STEP 1 · BRUTE FORCE
     * Greedy line filling is the natural approach, and nothing simpler works.
     * The skill is getting the spacing arithmetic right, so this problem goes
     * straight to Step 2.
     *
     * STEP 2 · THE IDEA
     * Choose the words for a line (greedy). Keep adding words while letters +
     * nextWord + (number of gaps) <= maxWidth. The gaps count because each
     * one needs at least one space.
     *
     * Spread the spaces. spaces = maxWidth − letters. Each gap gets spaces /
     * gaps, and the first spaces % gaps gaps get one extra.
     *
     * PART 1 CODE: WORD WRAP
     */
    // Step 2 · the better way
    public static class WordWrap {
        // Karat Part 1: greedy wrap, words joined by single spaces
        public List<String> wrap(String[] words, int maxWidth) {
            List<String> lines = new ArrayList<>();
            StringBuilder line = new StringBuilder();
            for (String w : words) {
                if (line.length() == 0) {
                    line.append(w);                       // first word
                } else if (line.length() + 1 + w.length() <= maxWidth) {
                    line.append(' ').append(w);           // still fits
                } else {
                    lines.add(line.toString());           // line is full
                    line = new StringBuilder(w);
                }
            }
            if (line.length() > 0) lines.add(line.toString());
            return lines;
        }
    }

    /**
     * DRY RUN FOR LINE 1 OF PART 2 ("I LOVE CLEAN")
     *   letters = 1 + 4 + 5 = 10,  gaps = 2,  spaces = 13 - 10 = 3
     *   each  = 3 / 2 = 1,   extra = 3 % 2 = 1
     *   gap 0 gets 1 + 1 = 2,  gap 1 gets 1   ->  "I" + "  " + "love" + " " + "clean"
     *
     * PART 2 CODE: FULL JUSTIFICATION
     */
    // Step 2 · the better way
    public static class TextJustify {
        public List<String> fullJustify(String[] words, int maxWidth) {
            List<String> lines = new ArrayList<>();
            int i = 0;
            while (i < words.length) {
                // 1) take as many words as fit: words[i .. j-1]
                int j = i;
                int letters = 0;
                while (j < words.length
                    && letters + words[j].length() + (j - i) <= maxWidth) {
                    letters += words[j].length();
                    j++;
                }
                int gaps = j - i - 1;
                boolean lastLine = (j == words.length);
                StringBuilder line = new StringBuilder();

                if (lastLine || gaps == 0) {
                    // 2a) left-justify: single spaces, pad the right end
                    for (int k = i; k < j; k++) {
                        line.append(words[k]);
                        if (k < j - 1) line.append(' ');
                    }
                    while (line.length() < maxWidth) line.append(' ');
                } else {
                    // 2b) spread spaces; leftmost gaps get the extras
                    int spaces = maxWidth - letters;
                    int each = spaces / gaps;
                    int extra = spaces % gaps;
                    for (int k = i; k < j; k++) {
                        line.append(words[k]);
                        if (k < j - 1) {
                            int n = each + (k - i < extra ? 1 : 0);
                            for (int s = 0; s < n; s++) line.append(' ');
                        }
                    }
                }
                lines.add(line.toString());
                i = j;                       // next line starts here
            }
            return lines;
        }
    }

    /*
     * Time: O(total characters) · Space: O(output)
     *
     * WATCH OUT
     * - Single-word lines have gaps = 0. Dividing by zero crashes, so handle
     *   them in the left-justify branch.
     * - The last line is always left-justified, even when it has several
     *   words.
     * - Test a word exactly maxWidth long: it should sit alone on its line
     *   with no padding.
     *
     * KARAT TIP
     * Karat's version sometimes joins words with '-' instead of spaces ("I--
     * love-clean"). It's the same logic with a different filler character.
     * Write Part 1 cleanly, because Part 2 reuses the same "which words fit
     * on this line" loop.
     */

}
