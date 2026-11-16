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

    // ====================================================================
    // 2. Find Center of Star Graph
    // LeetCode 1791 · Easy
    // Trick: the centre is the node shared by the first two edges  ·  O(1)
    // ====================================================================

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

    // Step 2 · the better way
    public static class StarCenter {
        public int findCenter(int[][] edges) {
            int[] a = edges[0], b = edges[1];
            // the centre is in EVERY edge, so it's the node these two share
            if (a[0] == b[0] || a[0] == b[1]) return a[0];
            return a[1];
        }
    }

    // ====================================================================
    // 3. Find if Path Exists in Graph
    // LeetCode 1971 · Easy
    // Trick: adjacency list (both ways) + BFS  ·  O(n + e)
    // ====================================================================

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

}
