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

    // ====================================================================
    // 2. Valid Palindrome II
    // LeetCode 680 · Easy
    // Trick: two pointers; on first mismatch try skipping left OR right  ·  O(n)
    // ====================================================================

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

    // ====================================================================
    // 3. Reverse Vowels of a String
    // LeetCode 345 · Easy
    // Trick: two pointers stop on vowels and swap  ·  O(n)
    // ====================================================================

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

    // ====================================================================
    // 4. Longest Common Prefix
    // LeetCode 14 · Easy
    // Trick: check column i in every word; stop at first mismatch  ·  O(total)
    // ====================================================================

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

    // ====================================================================
    // 5. Add Strings
    // LeetCode 415 · Easy
    // Trick: right to left, digit + digit + carry; reverse at the end  ·  O(n)
    // ====================================================================

    // Step 1 · brute force
    public static class AddStringsParse {
        // looks easy, but long overflows past ~19 digits (and it's banned)
        public String addStrings(String a, String b) {
            long sum = Long.parseLong(a) + Long.parseLong(b);
            return String.valueOf(sum);
        }
    }

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

    // ====================================================================
    // 6. Add Binary
    // LeetCode 67 · Easy
    // Trick: same as Add Strings, base 2  ·  O(n)
    // ====================================================================

    // Step 1 · brute force
    public static class AddBinaryParse {
        // same trap: fine for short input, overflows past 63 bits
        public String addBinary(String a, String b) {
            long sum = Long.parseLong(a, 2) + Long.parseLong(b, 2);
            return Long.toBinaryString(sum);
        }
    }

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

    // ====================================================================
    // 7. Roman to Integer
    // LeetCode 13 · Easy
    // Trick: smaller than next → subtract, else add  ·  O(n)
    // ====================================================================

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

}
