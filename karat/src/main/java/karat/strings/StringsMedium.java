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

    // ====================================================================
    // 2. String to Integer (atoi)
    // LeetCode 8 · Medium
    // Trick: spaces → sign → digits → stop; clamp to int range  ·  O(n)
    // ====================================================================

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

    // ====================================================================
    // 3. Reverse Words in a String
    // LeetCode 151 · Medium
    // Trick: trim() + split("\\s+"), then append backwards  ·  O(n)
    // ====================================================================

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

    // ====================================================================
    // 4. Compare Version Numbers
    // LeetCode 165 · Medium
    // Trick: split on \\.; compare as ints; missing part = 0  ·  O(n)
    // ====================================================================

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

    // ====================================================================
    // 5. Simplify Path
    // LeetCode 71 · Medium
    // Trick: split on "/"; skip "" and "."; ".." pops; else push  ·  O(n)
    // ====================================================================

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

    // ====================================================================
    // 6. Reorder Data in Log Files
    // LeetCode 937 · Medium
    // Trick: split at first space; sort letters by (content, id)  ·  O(n log n)
    // ====================================================================

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

    // ====================================================================
    // 7. String Compression
    // LeetCode 443 · Medium
    // Trick: read pointer finds runs; write pointer writes char + count  ·  O(n)
    // ====================================================================

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

    // ====================================================================
    // 8. Count and Say
    // LeetCode 38 · Medium
    // Trick: run-length encode the previous string, n − 1 times  ·  O(total length)
    // ====================================================================

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

    // ====================================================================
    // 9. Zigzag Conversion
    // LeetCode 6 · Medium
    // Trick: one StringBuilder per row; bounce direction at top/bottom  ·  O(n)
    // ====================================================================

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

    // ====================================================================
    // 10. Multiply Strings
    // LeetCode 43 · Medium
    // Trick: digit i × digit j lands in slots i+j and i+j+1  ·  O(n × m)
    // ====================================================================

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

    // ====================================================================
    // 11. Decode String
    // LeetCode 394 · Medium
    // Trick: at [ push (count, text); at ] pop and repeat  ·  O(output)
    // ====================================================================

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

    // ====================================================================
    // 12. Basic Calculator II
    // LeetCode 227 · the * and / version · Medium
    // Trick: apply the op BEFORE each number; * and / change the last term  ·  O(n)
    // ====================================================================

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

    // ====================================================================
    // 13. Longest Palindromic Substring
    // LeetCode 5 · Medium
    // Trick: expand around every centre (letter and gap)  ·  O(n²)
    // ====================================================================

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

    // ====================================================================
    // 14. Basic Calculator  ★ Karat
    // LeetCode 224 · Karat asks this in parts · Hard
    // Trick: result, sign, num; at ( push result & sign  ·  O(n)
    // ====================================================================

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

    // ====================================================================
    // 15. Word Wrap → Text Justification  ★ Karat
    // LeetCode 68 · Karat asks wrap first, then justify · Hard
    // Trick: greedy fit counting gaps; each = sp/gaps, extras go left  ·  O(chars)
    // ====================================================================

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

}
