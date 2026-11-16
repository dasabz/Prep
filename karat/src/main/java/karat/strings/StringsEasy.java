package karat.strings;

import java.util.*;

/**
 * Part 2 · String Parsing & Simulation · Easy (7 problems).
 *
 * Explanations, dry runs and pitfalls: pdfs/Karat-2a-Easy-String-Parsing-Simulation.pdf
 * Each problem: Step 1 = brute force, Step 2 = the better way.
 * Every example is checked in src/test/java/karat/strings/StringsTests.java
 */
public final class StringsEasy {

    private StringsEasy() { }

    // ====================================================================
    // 1. Valid Palindrome
    // LeetCode 125 · Easy
    // Trick: two pointers; skip non-alphanumerics; compare lower case  ·  O(n)
    // ====================================================================

    /**
     * QUESTION
     * Does the string read the same forwards and backwards? Ignore everything
     * that isn't a letter or a digit, and ignore upper/lower case.
     *
     * EXAMPLE
     *   "Never odd, or even!"   ->  true      (neveroddoreven)
     *   "race a car"            ->  false     (raceacar)
     *
     * STEP 1 · BRUTE FORCE
     * Build a cleaned copy (only letters and digits, lower case), then
     * compare it with its reverse.
     *
     * Brute force cost: O(n) time but O(n) extra space for the copies. Step 2
     * needs no copy at all.
     */
    // Step 1 · brute force
    public static class ValidPalindromeBrute {
        public boolean isPalindrome(String s) {
            StringBuilder clean = new StringBuilder();
            for (char c : s.toCharArray()) {
                if (Character.isLetterOrDigit(c)) {
                    clean.append(Character.toLowerCase(c));   // keep a-z, 0-9
                }
            }
            String forward = clean.toString();
            String backward = clean.reverse().toString();
            return forward.equals(backward);
        }
    }

    /**
     * STEP 2 · THE BETTER IDEA
     * Use two pointers: left starts at the beginning and right at the end.
     * Skip anything that isn't a letter or digit. Compare the two characters
     * in lower case. If they differ, return false. Otherwise move both
     * pointers inward.
     *
     * DRY RUN ("RACE A CAR")
     *   r a c e _ a _ c a r       (_ = space)
     *   l                 r       r == r  ok
     *     l             r         a == a  ok
     *       l         r           c == c  ok
     *         l     r             e vs a  ->  false
     * (On the last step right first lands on the space at index 6, skips it,
     * then compares e with a.)
     */
    // Step 2 · the better way
    public static class ValidPalindrome {
        public boolean isPalindrome(String s) {
            int left = 0, right = s.length() - 1;
            while (left < right) {
                char a = Character.toLowerCase(s.charAt(left));
                char b = Character.toLowerCase(s.charAt(right));
                if (!Character.isLetterOrDigit(a)) {
                    left++;                       // skip junk on the left
                } else if (!Character.isLetterOrDigit(b)) {
                    right--;                      // skip junk on the right
                } else if (a != b) {
                    return false;                 // mismatch
                } else {
                    left++;                       // match: move inward
                    right--;
                }
            }
            return true;
        }
    }

    /*
     * Time: O(n) · Space: O(1), no new string is built
     *
     * WATCH OUT
     * - Digits count: "0P" is not a palindrome.
     * - Skip one character per loop turn, then re-check left < right. That
     *   keeps both pointers inside the string.
     */

    // ====================================================================
    // 2. Valid Palindrome II
    // LeetCode 680 · Easy
    // Trick: two pointers; on first mismatch try skipping left OR right  ·  O(n)
    // ====================================================================

    /**
     * QUESTION
     * Can the string become a palindrome if you delete at most one character?
     *
     * EXAMPLE
     *   "abca"   ->  true     (delete 'b' or 'c')
     *   "abc"    ->  false
     *
     * STEP 1 · BRUTE FORCE
     * If the string is already a palindrome, you're done. Otherwise, try
     * deleting each character in turn and check whether what's left is a
     * palindrome.
     *
     * Brute force cost: O(n²) time, with n checks of O(n) each, plus a new
     * string every time.
     */
    // Step 1 · brute force
    public static class ValidPalindromeIIBrute {
        public boolean validPalindrome(String s) {
            if (isPal(s)) return true;
            for (int i = 0; i < s.length(); i++) {
                String t = s.substring(0, i) + s.substring(i + 1);  // drop i
                if (isPal(t)) return true;
            }
            return false;
        }

        private boolean isPal(String t) {
            int l = 0, r = t.length() - 1;
            while (l < r) {
                if (t.charAt(l) != t.charAt(r)) return false;
                l++;
                r--;
            }
            return true;
        }
    }

    /**
     * STEP 2 · THE BETTER IDEA
     * Walk two pointers inward. While the ends match, keep going. At the
     * first mismatch, the deleted character must be one of those two, so
     * check whether s[l+1..r] or s[l..r−1] is a palindrome. That's only two
     * checks, not n.
     *
     * DRY RUN ("ABCA")
     *   l=0 'a', r=3 'a'  match -> move in
     *   l=1 'b', r=2 'c'  mismatch -> try "c" (skip b) -> palindrome -> true
     */
    // Step 2 · the better way
    public static class ValidPalindromeII {
        public boolean validPalindrome(String s) {
            int l = 0, r = s.length() - 1;
            while (l < r) {
                if (s.charAt(l) != s.charAt(r)) {
                    // one delete allowed: skip the left OR the right char
                    return isPal(s, l + 1, r) || isPal(s, l, r - 1);
                }
                l++;
                r--;
            }
            return true;
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

    /*
     * Time: O(n) · Space: O(1)
     *
     * WATCH OUT
     * - You only get one deletion, so the two helper checks must not skip
     *   anything further.
     */

    // ====================================================================
    // 3. Reverse Vowels of a String
    // LeetCode 345 · Easy
    // Trick: two pointers stop on vowels and swap  ·  O(n)
    // ====================================================================

    /**
     * QUESTION
     * Reverse only the vowels (a, e, i, o, u, in either case) and leave every
     * other character where it is.
     *
     * EXAMPLE
     *   "karat code"  ->  "kerot cada"
     *   "IceCreAm"    ->  "AceCreIm"
     *
     * STEP 1 · BRUTE FORCE
     * Collect the vowels into a list, then rebuild the string, handing the
     * vowels back out from the end of the list.
     *
     * Brute force cost: O(n) time, but O(n) extra space for the list and the
     * new string.
     */
    // Step 1 · brute force
    public static class ReverseVowelsBrute {
        public String reverseVowels(String s) {
            List<Character> vowels = new ArrayList<>();
            for (char c : s.toCharArray()) {
                if (isVowel(c)) vowels.add(c);         // collect in order
            }
            StringBuilder sb = new StringBuilder();
            int k = vowels.size() - 1;         // hand out from the end
            for (char c : s.toCharArray()) {
                if (isVowel(c)) {
                    sb.append(vowels.get(k));
                    k--;
                } else {
                    sb.append(c);
                }
            }
            return sb.toString();
        }

        private boolean isVowel(char c) {
            return "aeiouAEIOU".indexOf(c) != -1;
        }
    }

    /**
     * STEP 2 · THE BETTER IDEA
     * Use two pointers on a char[]. Move l right until it hits a vowel and r
     * left until it hits a vowel, then swap them. It's the same skipping
     * pattern as Valid Palindrome.
     */
    // Step 2 · the better way
    public static class ReverseVowels {
        public String reverseVowels(String s) {
            char[] a = s.toCharArray();
            int l = 0, r = a.length - 1;
            while (l < r) {
                if (!isVowel(a[l])) {
                    l++;
                } else if (!isVowel(a[r])) {
                    r--;
                } else {
                    char tmp = a[l];                   // swap the two vowels
                    a[l] = a[r];
                    a[r] = tmp;
                    l++;
                    r--;
                }
            }
            return new String(a);
        }

        private boolean isVowel(char c) {
            return "aeiouAEIOU".indexOf(c) != -1;
        }
    }

    /*
     * Time: O(n) · Space: O(n) for the char array (Java strings can't be
     * changed in place)
     */

    // ====================================================================
    // 4. Longest Common Prefix
    // LeetCode 14 · Easy
    // Trick: check column i in every word; stop at first mismatch  ·  O(total)
    // ====================================================================

    /**
     * QUESTION
     * Find the longest starting text shared by all the words.
     *
     * EXAMPLE
     *   ["interview", "internet", "interval"]   ->  "inter"
     *   ["java", "kafka"]                       ->  ""
     *
     * STEP 1 · BRUTE FORCE
     * Start with the whole first word as the prefix. For each word, keep
     * chopping the last letter off the prefix until the word starts with it.
     *
     * Brute force cost: O(total letters × prefix length) in the worst case.
     * Short and easy to write; fine for small input.
     */
    // Step 1 · brute force
    public static class LongestCommonPrefixBrute {
        public String longestCommonPrefix(String[] words) {
            if (words.length == 0) return "";
            String prefix = words[0];                // start with all of it
            for (String w : words) {
                while (!w.startsWith(prefix)) {
                    // too long: chop off the last letter and try again
                    prefix = prefix.substring(0, prefix.length() - 1);
                }
            }
            return prefix;
        }
    }

    /**
     * STEP 2 · THE BETTER IDEA
     * Compare column by column. Take the letter at position i of the first
     * word and check that every other word has the same letter there. Stop as
     * soon as one word is too short or has a different letter. Everything
     * before i is the answer.
     *
     * DRY RUN
     *   col:  0 1 2 3 4 5
     *         i n t e r v i e w
     *         i n t e r n e t
     *         i n t e r v a l
     *         ✓ ✓ ✓ ✓ ✓ ✗       ->  stop at i = 5  ->  "inter"
     */
    // Step 2 · the better way
    public static class LongestCommonPrefix {
        public String longestCommonPrefix(String[] words) {
            if (words.length == 0) return "";
            String first = words[0];
            for (int i = 0; i < first.length(); i++) {
                char c = first.charAt(i);
                for (String w : words) {
                    // stop when a word is too short or the letter differs
                    if (i == w.length() || w.charAt(i) != c) {
                        return first.substring(0, i);
                    }
                }
            }
            return first;          // the whole first word is the prefix
        }
    }

    /*
     * Time: O(total letters) · Space: O(1)
     *
     * WATCH OUT
     * - Check i == w.length() before w.charAt(i), or a shorter word throws
     *   StringIndexOutOfBoundsException. Java's || stops at the first true
     *   condition, so the order matters.
     */

    // ====================================================================
    // 5. Add Strings
    // LeetCode 415 · Easy
    // Trick: right to left, digit + digit + carry; reverse at the end  ·  O(n)
    // ====================================================================

    /**
     * QUESTION
     * Add two non-negative numbers given as strings. You can't convert them
     * to integers, because they can be very long.
     *
     * EXAMPLE
     *   "456" + "77"   ->  "533"
     *   "999" + "1"    ->  "1000"
     *
     * STEP 1 · BRUTE FORCE
     * Convert both to long and add. This works for small inputs, but it
     * overflows past about 19 digits, and the problem forbids it. It's still
     * worth saying out loud, to show you know why it fails.
     *
     * Brute force cost: O(n), but wrong for long inputs.
     */
    // Step 1 · brute force
    public static class AddStringsParse {
        // looks easy, but long overflows past ~19 digits (and it's banned)
        public String addStrings(String a, String b) {
            long sum = Long.parseLong(a) + Long.parseLong(b);
            return String.valueOf(sum);
        }
    }

    /**
     * STEP 2 · THE BETTER IDEA
     * Do what you learned in school: start from the rightmost digits, add
     * them plus the carry, write sum % 10, and carry sum / 10. Keep going
     * while either string has digits left or there's still a carry. Append to
     * a StringBuilder and reverse it at the end.
     *
     * DRY RUN ("999" + "1")
     *   9 + 1 + 0 = 10 -> write 0, carry 1
     *   9 + 0 + 1 = 10 -> write 0, carry 1
     *   9 + 0 + 1 = 10 -> write 0, carry 1
     *   no digits, carry 1 -> write 1
     *   built "0001" -> reverse -> "1000"
     */
    // Step 2 · the better way
    public static class AddStrings {
        public String addStrings(String a, String b) {
            StringBuilder sb = new StringBuilder();
            int i = a.length() - 1, j = b.length() - 1, carry = 0;
            while (i >= 0 || j >= 0 || carry > 0) {
                int sum = carry;
                if (i >= 0) {
                    sum += a.charAt(i) - '0';
                    i--;
                }
                if (j >= 0) {
                    sum += b.charAt(j) - '0';
                    j--;
                }
                sb.append(sum % 10);                   // digit to write
                carry = sum / 10;                      // carry to next column
            }
            return sb.reverse().toString();            // built it backwards
        }
    }

    /*
     * Time: O(max(n, m)) · Space: O(max(n, m))
     *
     * WATCH OUT
     * - || carry > 0 in the loop condition is what produces the final 1 in
     *   999 + 1.
     */

    // ====================================================================
    // 6. Add Binary
    // LeetCode 67 · Easy
    // Trick: same as Add Strings, base 2  ·  O(n)
    // ====================================================================

    /**
     * QUESTION
     * Add two binary strings and return the sum as a binary string.
     *
     * EXAMPLE
     *   "1010" + "1011"  ->  "10101"
     *   "11"   + "1"     ->  "100"
     *
     * STEP 1 · BRUTE FORCE
     * Long.parseLong(a, 2), add, then Long.toBinaryString. It's the same trap
     * as before: it breaks past 63 bits.
     *
     * Brute force cost: O(n), but wrong for long inputs.
     */
    // Step 1 · brute force
    public static class AddBinaryParse {
        // same trap: fine for short input, overflows past 63 bits
        public String addBinary(String a, String b) {
            long sum = Long.parseLong(a, 2) + Long.parseLong(b, 2);
            return Long.toBinaryString(sum);
        }
    }

    /**
     * STEP 2 · THE BETTER IDEA
     * Use the Add Strings loop, but write sum % 2 and carry sum / 2.
     */
    // Step 2 · the better way
    public static class AddBinary {
        public String addBinary(String a, String b) {
            StringBuilder sb = new StringBuilder();
            int i = a.length() - 1, j = b.length() - 1, carry = 0;
            while (i >= 0 || j >= 0 || carry > 0) {
                int sum = carry;
                if (i >= 0) {
                    sum += a.charAt(i) - '0';
                    i--;
                }
                if (j >= 0) {
                    sum += b.charAt(j) - '0';
                    j--;
                }
                sb.append(sum % 2);                    // base 2 instead of 10
                carry = sum / 2;
            }
            return sb.reverse().toString();
        }
    }

    /*
     * Time: O(max(n, m)) · Space: O(max(n, m))
     *
     * PATTERN
     * Any "add two numbers stored as digits" problem (strings, arrays, linked
     * lists) uses this same carry loop.
     */

    // ====================================================================
    // 7. Roman to Integer
    // LeetCode 13 · Easy
    // Trick: smaller than next → subtract, else add  ·  O(n)
    // ====================================================================

    /**
     * QUESTION
     * Convert a Roman numeral to a number. I=1, V=5, X=10, L=50, C=100,
     * D=500, M=1000. A smaller symbol written before a bigger one is
     * subtracted: IV = 4, IX = 9, XC = 90, CM = 900.
     *
     * EXAMPLE
     *   "LVIII"    ->  58      (50 + 5 + 1 + 1 + 1)
     *   "MCMXCIV"  ->  1994    (1000 + 900 + 90 + 4)
     *
     * STEP 1 · BRUTE FORCE
     * Rewrite the six special pairs into plain additions (IV → IIII, CM →
     * DCCCC, …), then simply add up every symbol.
     *
     * Brute force cost: O(n) time, but it builds new strings and you must
     * remember all six pairs. Step 2 handles every case with one rule.
     */
    // Step 1 · brute force
    public static class RomanToIntegerBrute {
        public int romanToInt(String s) {
            // rewrite the 6 special pairs so every symbol just adds up
            s = s.replace("IV", "IIII").replace("IX", "VIIII");
            s = s.replace("XL", "XXXX").replace("XC", "LXXXX");
            s = s.replace("CD", "CCCC").replace("CM", "DCCCC");
            int total = 0;
            for (char c : s.toCharArray()) {
                switch (c) {
                    case 'I': total += 1; break;
                    case 'V': total += 5; break;
                    case 'X': total += 10; break;
                    case 'L': total += 50; break;
                    case 'C': total += 100; break;
                    case 'D': total += 500; break;
                    default:  total += 1000;          // 'M'
                }
            }
            return total;
        }
    }

    /**
     * STEP 2 · THE BETTER IDEA
     * Read left to right. If the current symbol is smaller than the next one,
     * subtract it. Otherwise add it. That single rule handles every special
     * case.
     *
     * DRY RUN ("MCMXCIV")
     *   symbol | value | next | smaller than next? | total
     *   M | 1000 | C(100) | no → +1000 | 1000
     *   C | 100 | M(1000) | yes → −100 | 900
     *   M | 1000 | X(10) | no → +1000 | 1900
     *   X | 10 | C(100) | yes → −10 | 1890
     *   C | 100 | I(1) | no → +100 | 1990
     *   I | 1 | V(5) | yes → −1 | 1989
     *   V | 5 | – | no → +5 | 1994
     */
    // Step 2 · the better way
    public static class RomanToInteger {
        public int romanToInt(String s) {
            Map<Character, Integer> value = new HashMap<>();
            value.put('I', 1);    value.put('V', 5);
            value.put('X', 10);   value.put('L', 50);
            value.put('C', 100);  value.put('D', 500);
            value.put('M', 1000);

            int total = 0;
            for (int i = 0; i < s.length(); i++) {
                int cur = value.get(s.charAt(i));
                boolean hasNext = i + 1 < s.length();
                if (hasNext && cur < value.get(s.charAt(i + 1))) {
                    total -= cur;        // smaller before bigger: IV, XC
                } else {
                    total += cur;
                }
            }
            return total;
        }
    }

    /*
     * Time: O(n) · Space: O(1)
     *
     * WATCH OUT
     * - Check i + 1 < s.length() before reading the next symbol.
     * - A switch works just as well as the map. Use whichever you can type
     *   faster.
     */

}
