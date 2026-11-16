package karat.graphs;

import java.util.*;

/**
 * Part 4 · Graph Basics · Easy (3 problems).
 *
 * Explanations, dry runs and pitfalls: pdfs/Karat-4a-Easy-Graph-Basics.pdf
 * Each problem: Step 1 = brute force, Step 2 = the better way.
 * Every example is checked in src/test/java/karat/graphs/GraphsTests.java
 */
public final class GraphsEasy {

    private GraphsEasy() { }

    // ====================================================================
    // 1. Find the Town Judge
    // LeetCode 997 · Easy
    // Trick: out = 0 and in = n − 1  ·  O(n + t)
    // ====================================================================

    /**
     * QUESTION
     * People are numbered 1 to n. [a, b] means a trusts b. The judge trusts
     * nobody, and everybody else trusts the judge. Return the judge's number,
     * or −1 if there's no judge.
     *
     * EXAMPLE
     *   n = 4, trust = [[1,2], [3,2], [4,2], [1,3]]   ->  2
     *   person 2 trusts nobody and is trusted by 1, 3 and 4 (= n - 1 people)
     *
     * STEP 1 · BRUTE FORCE
     * For each person, scan every trust pair to see whether they trust anyone
     * and how many people trust them.
     *
     * Brute force cost: O(n × t) time · O(1) space
     */
    // Step 1 · brute force
    public static class TownJudgeBrute {
        public int findJudge(int n, int[][] trust) {
            for (int p = 1; p <= n; p++) {
                boolean trustsSomeone = false;
                int trustedBy = 0;
                for (int[] t : trust) {               // scan ALL pairs for p
                    if (t[0] == p) trustsSomeone = true;
                    if (t[1] == p) trustedBy++;
                }
                if (!trustsSomeone && trustedBy == n - 1) return p;
            }
            return -1;
        }
    }

    /**
     * STEP 2 · THE BETTER IDEA
     * This is pure degree counting. Keep two arrays: trustsOthers[p] for
     * arrows going out, and trustedBy[p] for arrows coming in. The judge is
     * the person with out = 0 and in = n − 1.
     *
     * DRY RUN
     *   person | trusts others (out) | trusted by (in) | judge?
     *   1 | 2 | 0 | no
     *   2 | 0 | 3 | yes
     *   3 | 1 | 1 | no
     *   4 | 1 | 0 | no
     */
    // Step 2 · the better way
    public static class TownJudge {
        public int findJudge(int n, int[][] trust) {
            int[] trustsOthers = new int[n + 1];   // out-degree (people 1..n)
            int[] trustedBy = new int[n + 1];      // in-degree
            for (int[] t : trust) {
                trustsOthers[t[0]]++;              // t[0] trusts t[1]
                trustedBy[t[1]]++;
            }
            for (int p = 1; p <= n; p++) {
                if (trustsOthers[p] == 0 && trustedBy[p] == n - 1) {
                    return p;
                }
            }
            return -1;
        }
    }

    /*
     * Time: O(n + t) · Space: O(n)
     *
     * WATCH OUT
     * - People are numbered 1..n, so the arrays need size n + 1.
     * - n = 1 with no trust pairs: person 1 is the judge, because 0 = n − 1.
     *
     * KARAT LINK
     * Counting arrows into each node is also Part 1 of the Ancestors question
     * (problem 13 in GraphsMedium).
     */

    // ====================================================================
    // 2. Find Center of Star Graph
    // LeetCode 1791 · Easy
    // Trick: the centre is the node shared by the first two edges  ·  O(1)
    // ====================================================================

    /**
     * QUESTION
     * A star graph has one centre node connected to every other node, and no
     * other edges. Given the edges, return the centre.
     *
     * EXAMPLE
     *   edges = [[4,2], [1,2], [2,3]]   ->  2
     *
     * STEP 1 · BRUTE FORCE
     * Count degrees like Town Judge: the centre is the node that appears in
     * every edge (degree = number of edges).
     *
     * Brute force cost: O(n) time · O(n) space. Correct, and the natural
     * first answer.
     */
    // Step 1 · brute force
    public static class StarCenterCount {
        public int findCenter(int[][] edges) {
            Map<Integer, Integer> degree = new HashMap<>();
            for (int[] e : edges) {
                degree.put(e[0], degree.getOrDefault(e[0], 0) + 1);
                degree.put(e[1], degree.getOrDefault(e[1], 0) + 1);
            }
            for (int node : degree.keySet()) {
                if (degree.get(node) == edges.length) return node;  // in all
            }
            return -1;
        }
    }

    /**
     * STEP 2 · THE BETTER IDEA
     * The centre is in every edge, so it must be in the first two. Whichever
     * node those two edges share is the answer, and you never read the rest.
     */
    // Step 2 · the better way
    public static class StarCenter {
        public int findCenter(int[][] edges) {
            int[] a = edges[0], b = edges[1];
            // the centre is in EVERY edge, so it's the node these two share
            if (a[0] == b[0] || a[0] == b[1]) return a[0];
            return a[1];
        }
    }

    /*
     * Time: O(1) · Space: O(1)
     *
     * LESSON
     * Use the problem's guarantees. "It's definitely a star" is what makes
     * the O(1) answer valid.
     */

    // ====================================================================
    // 3. Find if Path Exists in Graph
    // LeetCode 1971 · Easy
    // Trick: adjacency list (both ways) + BFS  ·  O(n + e)
    // ====================================================================

    /**
     * QUESTION
     * There are n nodes (0..n−1) and some undirected edges. Is there a path
     * from src to dst?
     *
     * EXAMPLE
     *   n = 6, edges = [[0,1], [1,2], [3,4], [4,5]]
     *
     *   0 --- 1 --- 2        3 --- 4 --- 5
     *
     *   src 0, dst 2  ->  true          src 0, dst 5  ->  false
     *
     * STEP 1 · BRUTE FORCE
     * No adjacency list: start with src marked as reached, then keep sweeping
     * all the edges. Whenever an edge has one end reached and the other not,
     * mark both. Stop when a whole sweep changes nothing.
     *
     * Brute force cost: O(n × e) time in the worst case (up to n sweeps).
     * Step 2 visits each node and edge once.
     */
    // Step 1 · brute force
    public static class PathExistsBrute {
        public boolean validPath(int n, int[][] edges, int src, int dst) {
            boolean[] reach = new boolean[n];
            reach[src] = true;
            boolean changed = true;
            while (changed) {                         // keep sweeping edges
                changed = false;
                for (int[] e : edges) {
                    if (reach[e[0]] != reach[e[1]]) { // one side reached only
                        reach[e[0]] = true;
                        reach[e[1]] = true;
                        changed = true;
                    }
                }
            }
            return reach[dst];
        }
    }

    /**
     * STEP 2 · THE BETTER IDEA
     * Build an adjacency list (node → its neighbours), then BFS from src. If
     * you ever pop dst, return true. The visited array stops you going round
     * in circles.
     *
     * DRY RUN (SRC 0, DST 2)
     *   queue [0]   pop 0 -> neighbours 1        -> queue [1]
     *               pop 1 -> neighbours 0 (seen), 2 -> queue [2]
     *               pop 2 -> it's dst           -> true
     */
    // Step 2 · the better way
    public static class PathExists {
        public boolean validPath(int n, int[][] edges, int src, int dst) {
            // 1) build the adjacency list: node -> its neighbours
            List<List<Integer>> graph = new ArrayList<>();
            for (int i = 0; i < n; i++) graph.add(new ArrayList<>());
            for (int[] e : edges) {
                graph.get(e[0]).add(e[1]);
                graph.get(e[1]).add(e[0]);         // undirected: both ways
            }
            // 2) BFS from src
            boolean[] visited = new boolean[n];
            Queue<Integer> queue = new LinkedList<>();
            queue.add(src);
            visited[src] = true;
            while (!queue.isEmpty()) {
                int node = queue.poll();
                if (node == dst) return true;
                for (int next : graph.get(node)) {
                    if (!visited[next]) {
                        visited[next] = true;
                        queue.add(next);
                    }
                }
            }
            return false;
        }
    }

    /*
     * Time: O(n + e) · Space: O(n + e)
     *
     * WATCH OUT
     * - Undirected means you add both directions.
     * - Mark nodes visited when you add them to the queue.
     * - When src == dst, the first node you pop is already dst, so the answer
     *   is true.
     */

}
