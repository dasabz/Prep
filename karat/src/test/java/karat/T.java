package karat;

import java.util.*;

// Tiny test helper shared by all P*.java files.
public class T {
    static int pass = 0, fail = 0;

    public static String s(Object o) {
        if (o instanceof int[]) return Arrays.toString((int[]) o);
        if (o instanceof int[][]) return Arrays.deepToString((int[][]) o);
        if (o instanceof char[][]) {
            StringBuilder sb = new StringBuilder();
            for (char[] row : (char[][]) o) sb.append(new String(row)).append('/');
            return sb.toString();
        }
        if (o instanceof Object[]) return Arrays.deepToString((Object[]) o);
        if (o instanceof List) {
            List<?> list = (List<?>) o;
            StringBuilder sb = new StringBuilder("[");
            for (int i = 0; i < list.size(); i++) {
                if (i > 0) sb.append(", ");
                sb.append(s(list.get(i)));
            }
            return sb.append("]").toString();
        }
        return String.valueOf(o);
    }

    public static void check(String name, Object got, Object want) {
        String g = s(got), w = s(want);
        if (g.equals(w)) {
            pass++;
        } else {
            fail++;
            System.out.println("FAIL " + name + "\n   got:  " + g + "\n   want: " + w);
        }
    }

    // Sort a list of lists (each inner list sorted too) for order-free compares.
    public static List<List<String>> norm(List<List<String>> groups) {
        List<List<String>> out = new ArrayList<>();
        for (List<String> g : groups) {
            List<String> copy = new ArrayList<>(g);
            Collections.sort(copy);
            out.add(copy);
        }
        out.sort((a, b) -> a.toString().compareTo(b.toString()));
        return out;
    }

    public static void done(String file) {
        System.out.println(file + ": " + pass + " passed, " + fail + " failed");
        if (fail > 0) System.exit(1);
    }
}
