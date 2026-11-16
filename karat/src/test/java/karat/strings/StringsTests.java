package karat.strings;

import java.util.*;
import karat.T;
import karat.strings.StringsEasy.*;
import karat.strings.StringsMedium.*;

/** Runs every example from Part 2's PDFs against the code. */
public class StringsTests {
    public static void main(String[] args) {
        Examples1.run();
        Examples2.run();
        Examples3.run();
        Examples4.run();
        T.done("Strings");
    }

    static class Examples1 {
        static void run() {
            ValidPalindrome vp = new ValidPalindrome();
            T.check("pal1", vp.isPalindrome("Never odd, or even!"), true);
            T.check("pal2", vp.isPalindrome("race a car"), false);
            T.check("pal3", vp.isPalindrome(" "), true);
            T.check("pal4", vp.isPalindrome("0P"), false);

            LongestCommonPrefix lcp = new LongestCommonPrefix();
            T.check("lcp1", lcp.longestCommonPrefix(new String[] {"interview", "internet", "interval"}), "inter");
            T.check("lcp2", lcp.longestCommonPrefix(new String[] {"java", "kafka"}), "");
            T.check("lcp3", lcp.longestCommonPrefix(new String[] {"ab", "a"}), "a");
            T.check("lcp4", lcp.longestCommonPrefix(new String[] {"same", "same"}), "same");

            RomanToInteger ri = new RomanToInteger();
            T.check("roman1", ri.romanToInt("MCMXCIV"), 1994);
            T.check("roman2", ri.romanToInt("LVIII"), 58);
            T.check("roman3", ri.romanToInt("XLII"), 42);
            T.check("roman4", ri.romanToInt("MMXXVI"), 2026);

            ReverseWords rw = new ReverseWords();
            T.check("rev1", rw.reverseWords("  karat   loves clean code "), "code clean loves karat");
            T.check("rev2", rw.reverseWords("one"), "one");

            ReorderLogs rl = new ReorderLogs();
            T.check("logs1", rl.reorderLogFiles(new String[] {"a1 9 2 3", "g1 act car", "zo4 4 7", "ab1 off key dog", "a8 act zoo"}),
                    new String[] {"g1 act car", "a8 act zoo", "ab1 off key dog", "a1 9 2 3", "zo4 4 7"});
            T.check("logs2", rl.reorderLogFiles(new String[] {"web2 GET home", "db1 504 13", "web1 GET home", "db2 200 1"}),
                    new String[] {"web1 GET home", "web2 GET home", "db1 504 13", "db2 200 1"});

            DecodeString ds = new DecodeString();
            T.check("decode1", ds.decodeString("3[a2[c]]"), "accaccacc");
            T.check("decode2", ds.decodeString("2[ab]3[x]y"), "ababxxxy");
            T.check("decode3", ds.decodeString("10[z]"), "zzzzzzzzzz");
            T.check("decode4", ds.decodeString("2[k1[ab]]"), "kabkab");

            BasicCalculator bc = new BasicCalculator();
            T.check("calc1", bc.calculate("1 + 1"), 2);
            T.check("calc2", bc.calculate(" 2-1 + 2 "), 3);
            T.check("calc3", bc.calculate("(1+(4+5+2)-3)+(6+8)"), 23);
            T.check("calc4", bc.calculate("-(2+3)"), -5);
            T.check("calc5", bc.calculate("10 - (4 - 2) + 7"), 15);
            T.check("calc6", bc.calculate("2-(5-(1+1))"), -1);

            WordWrap ww = new WordWrap();
            T.check("wrap1", ww.wrap(new String[] {"I", "love", "clean", "and", "simple", "code."}, 13),
                    Arrays.asList("I love clean", "and simple", "code."));
            T.check("wrap2", ww.wrap(new String[] {"aaaa", "bb", "cc"}, 4), Arrays.asList("aaaa", "bb", "cc"));

            TextJustify tj = new TextJustify();
            T.check("just1", tj.fullJustify(new String[] {"I", "love", "clean", "and", "simple", "code."}, 13),
                    Arrays.asList("I  love clean", "and    simple", "code.        "));
            T.check("just2", tj.fullJustify(new String[] {"This", "is", "an", "example", "of", "text", "justification."}, 16),
                    Arrays.asList("This    is    an", "example  of text", "justification.  "));
            T.check("just3", tj.fullJustify(new String[] {"What", "must", "be", "acknowledgment", "shall", "be"}, 16),
                    Arrays.asList("What   must   be", "acknowledgment  ", "shall be        "));
        }
    }

    static class Examples2 {
        static void run() {
            ValidPalindromeBrute vp = new ValidPalindromeBrute();
            T.check("palB1", vp.isPalindrome("Never odd, or even!"), true);
            T.check("palB2", vp.isPalindrome("race a car"), false);
            T.check("palB3", vp.isPalindrome("0P"), false);

            LongestCommonPrefixBrute lcp = new LongestCommonPrefixBrute();
            T.check("lcpB1", lcp.longestCommonPrefix(new String[] {"interview", "internet", "interval"}), "inter");
            T.check("lcpB2", lcp.longestCommonPrefix(new String[] {"java", "kafka"}), "");
            T.check("lcpB3", lcp.longestCommonPrefix(new String[] {"ab", "a"}), "a");

            RomanToIntegerBrute ri = new RomanToIntegerBrute();
            T.check("romanB1", ri.romanToInt("MCMXCIV"), 1994);
            T.check("romanB2", ri.romanToInt("LVIII"), 58);
            T.check("romanB3", ri.romanToInt("MMXXVI"), 2026);
            T.check("romanB4", ri.romanToInt("XLIX"), 49);

            DecodeStringBrute ds = new DecodeStringBrute();
            T.check("decB1", ds.decodeString("3[a2[c]]"), "accaccacc");
            T.check("decB2", ds.decodeString("2[ab]3[x]y"), "ababxxxy");
            T.check("decB3", ds.decodeString("10[z]"), "zzzzzzzzzz");
            T.check("decB4", ds.decodeString("2[k1[ab]]"), "kabkab");

            BasicCalculatorBrute bc = new BasicCalculatorBrute();
            T.check("calcB1", bc.calculate(" 2-1 + 2 "), 3);
            T.check("calcB2", bc.calculate("(1+(4+5+2)-3)+(6+8)"), 23);
            T.check("calcB3", bc.calculate("-(2+3)"), -5);
            T.check("calcB4", bc.calculate("10 - (4 - 2) + 7"), 15);
            T.check("calcB5", bc.calculate("2-(5-(1+1))"), -1);
            T.check("calcB6", bc.calculate("1-(2-5)"), 4);
        }
    }

    static class Examples3 {
        static void run() {
            T.check("vp2B1", new ValidPalindromeIIBrute().validPalindrome("abca"), true);
            T.check("vp2B2", new ValidPalindromeIIBrute().validPalindrome("abc"), false);
            T.check("vp2_1", new ValidPalindromeII().validPalindrome("abca"), true);
            T.check("vp2_2", new ValidPalindromeII().validPalindrome("abc"), false);
            T.check("vp2_3", new ValidPalindromeII().validPalindrome("deeee"), true);
            T.check("vp2_4", new ValidPalindromeII().validPalindrome("cbbcc"), true);
            T.check("vp2_5", new ValidPalindromeII().validPalindrome("eceec"), true);

            T.check("rvB", new ReverseVowelsBrute().reverseVowels("karat code"), "kerot cada");
            T.check("rv1", new ReverseVowels().reverseVowels("karat code"), "kerot cada");
            T.check("rv2", new ReverseVowels().reverseVowels("IceCreAm"), "AceCreIm");

            T.check("addP", new AddStringsParse().addStrings("456", "77"), "533");
            T.check("add1", new AddStrings().addStrings("456", "77"), "533");
            T.check("add2", new AddStrings().addStrings("999", "1"), "1000");
            T.check("add3", new AddStrings().addStrings("99999999999999999999", "1"), "100000000000000000000");

            T.check("binP", new AddBinaryParse().addBinary("1010", "1011"), "10101");
            T.check("bin1", new AddBinary().addBinary("1010", "1011"), "10101");
            T.check("bin2", new AddBinary().addBinary("11", "1"), "100");
            T.check("bin3", new AddBinary().addBinary("0", "0"), "0");

            T.check("i2rT", new IntegerToRomanTables().intToRoman(1994), "MCMXCIV");
            T.check("i2rT2", new IntegerToRomanTables().intToRoman(3749), "MMMDCCXLIX");
            T.check("i2r1", new IntegerToRoman().intToRoman(1994), "MCMXCIV");
            T.check("i2r2", new IntegerToRoman().intToRoman(58), "LVIII");
            T.check("i2r3", new IntegerToRoman().intToRoman(3749), "MMMDCCXLIX");

            Atoi at = new Atoi();
            T.check("atoi1", at.myAtoi("42"), 42);
            T.check("atoi2", at.myAtoi("   -042"), -42);
            T.check("atoi3", at.myAtoi("1337c0d3"), 1337);
            T.check("atoi4", at.myAtoi("0-1"), 0);
            T.check("atoi5", at.myAtoi("words 987"), 0);
            T.check("atoi6", at.myAtoi("-91283472332"), Integer.MIN_VALUE);
            T.check("atoi7", at.myAtoi("2147483648"), Integer.MAX_VALUE);
            T.check("atoi8", at.myAtoi(""), 0);

            CompareVersion cv = new CompareVersion();
            T.check("cv1", cv.compareVersion("1.2", "1.10"), -1);
            T.check("cv2", cv.compareVersion("1.01", "1.001"), 0);
            T.check("cv3", cv.compareVersion("1.0", "1.0.0.0"), 0);
            T.check("cv4", cv.compareVersion("2.1", "2.0.9"), 1);
        }
    }

    static class Examples4 {
        static String comp(char[] in, boolean brute) {
            char[] c = in.clone();
            int n = brute ? new StringCompressionBrute().compress(c) : new StringCompression().compress(c);
            return new String(c, 0, n);
        }

        static void run() {
            SimplifyPath sp = new SimplifyPath();
            T.check("path1", sp.simplifyPath("/home/"), "/home");
            T.check("path2", sp.simplifyPath("/home//foo/"), "/home/foo");
            T.check("path3", sp.simplifyPath("/a/./b/../../c/"), "/c");
            T.check("path4", sp.simplifyPath("/../"), "/");
            T.check("path5", sp.simplifyPath("/.../a/../b/c/../d/./"), "/.../b/d");

            T.check("compB", comp("aabccc".toCharArray(), true), "a2bc3");
            T.check("comp1", comp("aabccc".toCharArray(), false), "a2bc3");
            T.check("comp2", comp("abbbbbbbbbbbb".toCharArray(), false), "ab12");
            T.check("comp3", comp("a".toCharArray(), false), "a");

            CountAndSay cs = new CountAndSay();
            T.check("cas1", cs.countAndSay(1), "1");
            T.check("cas4", cs.countAndSay(4), "1211");
            T.check("cas6", cs.countAndSay(6), "312211");

            T.check("zigG", new ZigzagGrid().convert("PAYPALISHIRING", 3), "PAHNAPLSIIGYIR");
            T.check("zigG4", new ZigzagGrid().convert("PAYPALISHIRING", 4), "PINALSIGYAHRPI");
            T.check("zig1", new Zigzag().convert("PAYPALISHIRING", 3), "PAHNAPLSIIGYIR");
            T.check("zig2", new Zigzag().convert("PAYPALISHIRING", 4), "PINALSIGYAHRPI");
            T.check("zig3", new Zigzag().convert("AB", 1), "AB");

            T.check("mulB", new MultiplyStringsBrute().multiply("123", "456"), "56088");
            T.check("mul1", new MultiplyStrings().multiply("123", "456"), "56088");
            T.check("mul2", new MultiplyStrings().multiply("0", "52"), "0");
            T.check("mul3", new MultiplyStrings().multiply("99", "99"), "9801");
            T.check("mulB2", new MultiplyStringsBrute().multiply("99", "99"), "9801");

            T.check("c2S1", new CalculatorIIStack().calculate("3+2*2"), 7);
            T.check("c2S2", new CalculatorIIStack().calculate(" 3+5 / 2 "), 5);
            T.check("c2_1", new CalculatorII().calculate("3+2*2"), 7);
            T.check("c2_2", new CalculatorII().calculate(" 3/2 "), 1);
            T.check("c2_3", new CalculatorII().calculate(" 3+5 / 2 "), 5);
            T.check("c2_4", new CalculatorII().calculate("2*3-4/2+1"), 5);
            T.check("c2_5", new CalculatorII().calculate("1-5/2"), -1);
            T.check("c2_6", new CalculatorII().calculate("42"), 42);

            T.check("lpsB", new LongestPalSubstringBrute().longestPalindrome("babad"), "bab");
            T.check("lps1", new LongestPalSubstring().longestPalindrome("babad"), "bab");
            T.check("lps2", new LongestPalSubstring().longestPalindrome("cbbd"), "bb");
            T.check("lps3", new LongestPalSubstring().longestPalindrome("forgeeksskeegfor"), "geeksskeeg");
            T.check("lps4", new LongestPalSubstring().longestPalindrome("a"), "a");
        }
    }

}
