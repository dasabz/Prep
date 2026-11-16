package karat.graphs;

import java.util.*;

/**
 * Part 4 · Graph Basics · Medium & Hard (19 problems).
 *
 * Explanations, dry runs and pitfalls: pdfs/Karat-4b-Medium-Graph-Basics.pdf
 * Each problem: Step 1 = brute force, Step 2 = the better way.
 * Every example is checked in src/test/java/karat/graphs/GraphsTests.java
 */
public final class GraphsMedium {

    private GraphsMedium() { }

    // ====================================================================
    // 1. Keys and Rooms
    // LeetCode 841 · Medium
    // Trick: DFS from room 0 following keys; count rooms opened  ·  O(rooms + keys)
    // ====================================================================

    /**
     * QUESTION
     * Room 0 is open. Every room holds keys to other rooms. Can you get into
     * every room?
     *
     * EXAMPLE
     *   [[1], [2], [3], []]          ->  true     (0 -> 1 -> 2 -> 3)
     *   [[1,3], [3,0,1], [2], [0]]   ->  false    (nobody holds key 2)
     *
     * STEP 1 · BRUTE FORCE
     * "Keep looping over the rooms you can open until nothing new opens"
     * works, but it rescans rooms repeatedly. This is plain reachability, so
     * go straight to DFS.
     *
     * STEP 2 · THE IDEA
     * It's a graph: rooms are nodes and keys are edges. Start a DFS (or BFS)
     * at room 0, follow every key to a room you haven't visited yet, and
     * count the rooms you open. Return whether that count equals the number
     * of rooms.
     */
    // Step 2 · the better way
    public static class KeysAndRooms {
        public boolean canVisitAllRooms(List<List<Integer>> rooms) {
            boolean[] visited = new boolean[rooms.size()];
            Deque<Integer> stack = new ArrayDeque<>();   // DFS with a stack
            stack.push(0);
            visited[0] = true;
            int opened = 1;
            while (!stack.isEmpty()) {
                int room = stack.pop();
                for (int key : rooms.get(room)) {        // keys found here
                    if (!visited[key]) {
                        visited[key] = true;
                        opened++;
                        stack.push(key);
                    }
                }
            }
            return opened == rooms.size();
        }
    }

    /*
     * Time: O(rooms + total keys) · Space: O(rooms)
     *
     * PATTERN
     * "Can I reach everything from here?" is a single traversal plus a count.
     * The same shape answers "is the graph connected?"
     */

    // ====================================================================
    // 2. Number of Provinces
    // LeetCode 547 · Medium
    // Trick: for each unvisited: count++ and DFS; or Union-Find  ·  O(n²)
    // ====================================================================

    /**
     * QUESTION
     * There are n cities. isConnected[i][j] = 1 means cities i and j are
     * directly connected. A province is a group of cities connected directly
     * or through other cities. Count the provinces.
     *
     * EXAMPLE
     *         0 1 2 3
     *     0 [ 1 1 0 0 ]
     *     1 [ 1 1 0 0 ]     ->  2 provinces: {0, 1} and {2, 3}
     *     2 [ 0 0 1 1 ]
     *     3 [ 0 0 1 1 ]
     *
     * STEP 1 · BRUTE FORCE
     * Both versions in Step 2 (DFS and Union-Find) are already the efficient
     * answers. There's no simpler correct approach worth writing.
     *
     * STEP 2 · THE IDEA (DFS)
     * It's Number of Islands on a graph. For every city you haven't visited
     * yet, add 1 to provinces and DFS to mark its whole group. Here the graph
     * is a matrix: the neighbours of city are every other where
     * g[city][other] == 1.
     *
     * DFS
     */
    // Step 2 · the better way
    public static class NumberOfProvinces {
        public int findCircleNum(int[][] isConnected) {
            int n = isConnected.length;
            boolean[] visited = new boolean[n];
            int provinces = 0;
            for (int city = 0; city < n; city++) {
                if (!visited[city]) {
                    provinces++;                   // a new group starts here
                    dfs(isConnected, city, visited);
                }
            }
            return provinces;
        }

        private void dfs(int[][] g, int city, boolean[] visited) {
            visited[city] = true;
            for (int other = 0; other < g.length; other++) {
                if (g[city][other] == 1 && !visited[other]) {
                    dfs(g, other, visited);
                }
            }
        }
    }

    /**
     * ALTERNATIVE: UNION-FIND (DISJOINT SET)
     * Start with n separate groups. For each connection, if the two cities
     * are in different groups, merge them and subtract 1 from the group
     * count. find(x) follows parent links up to the group's "boss". Two
     * cities are in the same group exactly when they have the same boss.
     *
     * UNION-FIND
     */
    // Step 2 · the better way
    public static class ProvincesUnionFind {
        private int[] parent;

        public int findCircleNum(int[][] isConnected) {
            int n = isConnected.length;
            parent = new int[n];
            for (int i = 0; i < n; i++) parent[i] = i;   // everyone alone
            int groups = n;
            for (int i = 0; i < n; i++) {
                for (int j = i + 1; j < n; j++) {
                    if (isConnected[i][j] == 1 && union(i, j)) {
                        groups--;                        // two groups merged
                    }
                }
            }
            return groups;
        }

        private int find(int x) {             // the "boss" of x's group
            while (parent[x] != x) {
                parent[x] = parent[parent[x]];  // shortcut: path halving
                x = parent[x];
            }
            return x;
        }

        private boolean union(int a, int b) { // true if they were apart
            int bossA = find(a), bossB = find(b);
            if (bossA == bossB) return false;
            parent[bossA] = bossB;
            return true;
        }
    }

    /*
     * Time: O(n²) for both, since you read the whole matrix · Space: O(n)
     *
     * WATCH OUT
     * - In Union-Find, compare the bosses (find(a) == find(b)), not a and b
     *   themselves.
     * - The matrix has 1s on the diagonal (each city is connected to itself).
     *   Both versions handle that.
     *
     * WHEN TO REACH FOR UNION-FIND
     * When connections arrive one at a time ("after each new link, how many
     * groups are there?"), or when you'll ask "are a and b connected?" many
     * times.
     */

    // ====================================================================
    // 3. Number of Operations to Make Network Connected
    // LeetCode 1319 · Medium
    // Trick: need ≥ n−1 cables; answer = groups − 1 (Union-Find)  ·  O(n + e)
    // ====================================================================

    /**
     * QUESTION
     * n computers are joined by cables. You may unplug a cable and reconnect
     * it between any two computers. What's the fewest moves to connect
     * everything? Return −1 if it's impossible.
     *
     * EXAMPLE
     *   n = 4, cables = [[0,1], [0,2], [1,2]]   ->  1
     *   (0-1-2 has a spare cable; move it to connect 3)
     *
     * STEP 1 · BRUTE FORCE
     * Trying every possible way to move cables explodes fast. Two facts make
     * it simple instead (see Step 2), and both are easy to say out loud.
     *
     * STEP 2 · THE IDEA
     * 1. Connecting n computers needs at least n − 1 cables. Fewer than that
     *    → −1.
     * 2. If there are enough cables, there's always a spare one inside some
     *    group. Each move joins two groups, so the answer is groups − 1.
     *
     * Count the groups with Union-Find (or DFS, as in Number of Provinces).
     */
    // Step 2 · the better way
    public static class NetworkConnected {
        private int[] parent;

        public int makeConnected(int n, int[][] connections) {
            if (connections.length < n - 1) return -1;  // too few cables
            parent = new int[n];
            for (int i = 0; i < n; i++) parent[i] = i;
            int groups = n;
            for (int[] c : connections) {
                if (union(c[0], c[1])) groups--;
            }
            return groups - 1;          // each move joins two groups
        }

        private int find(int x) {
            while (parent[x] != x) {
                parent[x] = parent[parent[x]];
                x = parent[x];
            }
            return x;
        }

        private boolean union(int a, int b) {
            int ra = find(a), rb = find(b);
            if (ra == rb) return false;               // already together
            parent[ra] = rb;
            return true;
        }
    }

    /*
     * Time: O(n + e) (Union-Find with path halving is nearly linear) · Space:
     * O(n)
     */

    // ====================================================================
    // 4. Redundant Connection
    // LeetCode 684 · Medium
    // Trick: Union-Find: first edge whose ends are already joined  ·  O(n)
    // ====================================================================

    /**
     * QUESTION
     * A tree with nodes 1..n had one extra edge added, creating a cycle.
     * Return an edge you can remove to get a tree back. If there are several,
     * return the one that appears last in the input.
     *
     * EXAMPLE
     *   [[1,2], [1,3], [2,3]]   ->  [2,3]
     *
     * STEP 1 · BRUTE FORCE
     * Try removing each edge, last first. Rebuild the graph without it and
     * BFS to check that everything is still connected. The first edge that
     * passes is the answer.
     *
     * Brute force cost: O(n²) time, with a full rebuild and BFS per edge.
     */
    // Step 1 · brute force
    public static class RedundantBrute {
        public int[] findRedundantConnection(int[][] edges) {
            int n = edges.length;                     // nodes are 1..n
            for (int skip = n - 1; skip >= 0; skip--) {   // last edge first
                if (connectedWithout(edges, skip, n)) return edges[skip];
            }
            return new int[0];
        }

        // rebuild the graph without one edge and BFS from node 1
        private boolean connectedWithout(int[][] edges, int skip, int n) {
            List<List<Integer>> g = new ArrayList<>();
            for (int i = 0; i <= n; i++) g.add(new ArrayList<>());
            for (int i = 0; i < edges.length; i++) {
                if (i == skip) continue;
                g.get(edges[i][0]).add(edges[i][1]);
                g.get(edges[i][1]).add(edges[i][0]);
            }
            boolean[] seen = new boolean[n + 1];
            Queue<Integer> queue = new LinkedList<>();
            queue.add(1);
            seen[1] = true;
            int count = 1;
            while (!queue.isEmpty()) {
                for (int y : g.get(queue.poll())) {
                    if (!seen[y]) {
                        seen[y] = true;
                        count++;
                        queue.add(y);
                    }
                }
            }
            return count == n;                        // still all connected?
        }
    }

    /**
     * STEP 2 · THE BETTER IDEA
     * Add the edges one at a time with Union-Find. An edge whose two ends are
     * already in the same group closes a cycle, so it's the extra one.
     * Because you process edges in order, the first such edge you meet is the
     * right answer.
     *
     * DRY RUN
     *   [1,2] -> join {1,2}
     *   [1,3] -> join {1,2,3}
     *   [2,3] -> 2 and 3 already in one group  ->  return [2,3]
     */
    // Step 2 · the better way
    public static class RedundantConnection {
        private int[] parent;

        public int[] findRedundantConnection(int[][] edges) {
            parent = new int[edges.length + 1];
            for (int i = 0; i < parent.length; i++) parent[i] = i;
            for (int[] e : edges) {
                // already in the same group -> this edge closes a cycle
                if (!union(e[0], e[1])) return e;
            }
            return new int[0];
        }

        private int find(int x) {
            while (parent[x] != x) {
                parent[x] = parent[parent[x]];
                x = parent[x];
            }
            return x;
        }

        private boolean union(int a, int b) {
            int ra = find(a), rb = find(b);
            if (ra == rb) return false;
            parent[ra] = rb;
            return true;
        }
    }

    /*
     * Time: O(n) (nearly) · Space: O(n)
     *
     * WHEN UNION-FIND SHINES
     * "Edges arrive one by one; detect the first cycle" or "how many groups
     * after each edge" are the textbook Union-Find signals.
     */

    // ====================================================================
    // 5. Is Graph Bipartite?
    // LeetCode 785 · Medium
    // Trick: BFS colouring: neighbours get the opposite colour  ·  O(V + E)
    // ====================================================================

    /**
     * QUESTION
     * Can you split the nodes into two teams so that every edge connects
     * people on different teams?
     *
     * EXAMPLE
     *   0 - 1
     *   |   |     square 0-1-2-3  ->  true   (teams {0,2} and {1,3})
     *   3 - 2
     *
     *   add the diagonal 0-2      ->  false  (0 and 2 must differ, but share a team)
     *
     * STEP 1 · BRUTE FORCE
     * Try every possible split into two teams: 2ⁿ splits, each checked
     * against every edge. That's exponential, so say it, then do Step 2.
     *
     * STEP 2 · THE IDEA
     * Colour as you go. Give the first node colour 1. BFS outward, giving
     * each uncoloured neighbour the opposite colour. If you ever find a
     * neighbour that already has the same colour as the current node, it's
     * impossible. Start a fresh BFS from every uncoloured node, because the
     * graph may be in pieces.
     */
    // Step 2 · the better way
    public static class Bipartite {
        public boolean isBipartite(int[][] graph) {
            int n = graph.length;
            int[] color = new int[n];        // 0 = none, 1 / -1 = two teams
            for (int start = 0; start < n; start++) {
                if (color[start] != 0) continue;     // graph can be in pieces
                color[start] = 1;
                Queue<Integer> queue = new LinkedList<>();
                queue.add(start);
                while (!queue.isEmpty()) {
                    int node = queue.poll();
                    for (int next : graph[node]) {
                        if (color[next] == 0) {
                            color[next] = -color[node];   // other team
                            queue.add(next);
                        } else if (color[next] == color[node]) {
                            return false;                 // same team: clash
                        }
                    }
                }
            }
            return true;
        }
    }

    /*
     * Time: O(V + E) · Space: O(V)
     *
     * WATCH OUT
     * - Don't forget the outer loop over all nodes. A disconnected graph has
     *   several parts that each need colouring.
     */

    // ====================================================================
    // 6. Clone Graph
    // LeetCode 133 · Medium
    // Trick: map original → copy; store the copy BEFORE recursing  ·  O(V + E)
    // ====================================================================

    /**
     * QUESTION
     * Make a deep copy of a connected undirected graph: brand-new nodes with
     * the same values and the same connections.
     *
     * EXAMPLE
     *   1 -- 2
     *   |    |      ->  an identical graph made of new Node objects
     *   4 -- 3
     *
     * STEP 1 · BRUTE FORCE
     * There's no slower trick worth showing; the danger is infinite loops.
     * Copying neighbours naively goes 1 → 2 → 1 → 2 … forever, because the
     * graph has cycles.
     *
     * STEP 2 · THE IDEA
     * Keep a map from original node → its copy. When you visit a node: if
     * it's already in the map, return that copy. Otherwise create the copy,
     * put it in the map first, and then recurse into the neighbours. Storing
     * it before recursing is what breaks the cycles.
     */
    // Step 2 · the better way
    // LeetCode gives you this Node class:
    public static class Node {
        int val;
        List<Node> neighbors = new ArrayList<>();
        Node(int val) {
            this.val = val;
        }
    }

    public static class CloneGraph {
        private Map<Node, Node> copies = new HashMap<>();  // original -> copy

        public Node cloneGraph(Node node) {
            if (node == null) return null;
            if (copies.containsKey(node)) return copies.get(node);  // made it
            Node copy = new Node(node.val);
            copies.put(node, copy);     // store BEFORE recursing (cycles)
            for (Node nb : node.neighbors) {
                copy.neighbors.add(cloneGraph(nb));
            }
            return copy;
        }
    }

    /*
     * Time: O(V + E) · Space: O(V)
     *
     * WATCH OUT
     * - If you put the node in the map after the recursion, the cycle 1 → 2 →
     *   1 creates a second copy of 1. The order matters.
     */

    // ====================================================================
    // 7. All Paths From Source to Target
    // LeetCode 797 · Medium
    // Trick: DFS + path list; save a copy at the target  ·  O(2ⁿ·n)
    // ====================================================================

    /**
     * QUESTION
     * You're given a directed graph with no cycles (a DAG). Nodes are 0..n−1,
     * and graph[i] lists the nodes that i points to. Return every path from
     * node 0 to node n−1.
     *
     * EXAMPLE
     *   graph = [[1,2], [3], [3], []]
     *
     *   0 -> 1 -> 3
     *   0 -> 2 -> 3          ->  [[0,1,3], [0,2,3]]
     *
     * STEP 1 · BRUTE FORCE
     * The number of paths itself can be exponential, so any method that lists
     * them all is exponential. DFS with backtracking is the brute force and
     * the answer.
     *
     * STEP 2 · THE IDEA
     * Use DFS with a path list: add a node on the way in and remove it on the
     * way back (backtracking). When you reach n−1, save a copy of the path.
     * Because there are no cycles, you can't loop forever, so you don't need
     * a visited set.
     *
     * DRY RUN
     *   path [0] -> go 1 -> [0,1] -> go 3 -> [0,1,3]  save
     *               back to [0,1] -> back to [0]
     *            -> go 2 -> [0,2] -> go 3 -> [0,2,3]  save
     */
    // Step 2 · the better way
    public static class AllPaths {
        public List<List<Integer>> allPathsSourceTarget(int[][] graph) {
            List<List<Integer>> result = new ArrayList<>();
            List<Integer> path = new ArrayList<>();
            path.add(0);
            dfs(graph, 0, path, result);
            return result;
        }

        private void dfs(int[][] graph, int node, List<Integer> path,
                         List<List<Integer>> result) {
            if (node == graph.length - 1) {          // reached the target
                result.add(new ArrayList<>(path));   // save a copy
                return;
            }
            for (int next : graph[node]) {
                path.add(next);
                dfs(graph, next, path, result);
                path.remove(path.size() - 1);        // backtrack
            }
        }
    }

    /*
     * Time: O(2ⁿ × n) in the worst case, which is the number of paths ·
     * Space: O(n) recursion
     *
     * WATCH OUT
     * - Save new ArrayList<>(path), not path itself.
     * - If the graph could have cycles, keep an onPath set and skip any node
     *   already on the current path.
     *
     * KARAT LINK
     * Middle Course Part 2 (problem 16) walks every path in exactly this way.
     */

    // ====================================================================
    // 8. Course Schedule
    // LeetCode 207 · Medium
    // Trick: Kahn: start from inDegree 0; taken == n?  ·  O(V + E)
    // ====================================================================

    /**
     * QUESTION
     * Courses are numbered 0..n−1. [a, b] means you must take b before a. Can
     * you finish every course? In other words: is there no cycle?
     *
     * EXAMPLE
     *   2 courses, [[1,0]]           ->  true     (take 0, then 1)
     *   2 courses, [[1,0], [0,1]]    ->  false    (1 needs 0 and 0 needs 1: a cycle)
     *
     * STEP 1 · BRUTE FORCE
     * Simulate a student. Each round, scan for any course not taken yet whose
     * prerequisites are all done, and take it. If a round finds nothing to
     * take while courses remain, there's a cycle.
     *
     * Brute force cost: O(V² × E) time, since every round rescans every
     * course and every prerequisite pair. Step 2 does the same thing with a
     * queue and in-degrees.
     */
    // Step 1 · brute force
    public static class CourseScheduleBrute {
        public boolean canFinish(int numCourses, int[][] prerequisites) {
            boolean[] taken = new boolean[numCourses];
            for (int round = 0; round < numCourses; round++) {
                int pick = -1;
                for (int c = 0; c < numCourses && pick == -1; c++) {
                    if (taken[c]) continue;
                    boolean ready = true;
                    for (int[] p : prerequisites) {   // all prereqs taken?
                        if (p[0] == c && !taken[p[1]]) ready = false;
                    }
                    if (ready) pick = c;
                }
                if (pick == -1) return false;         // stuck: a cycle
                taken[pick] = true;
            }
            return true;
        }
    }

    /**
     * STEP 2 · THE BETTER IDEA: KAHN'S ALGORITHM (TOPOLOGICAL SORT)
     * 1. Draw an arrow pre → course. inDegree[course] = how many
     *    prerequisites are still not done.
     * 2. Put every course with inDegree 0 (no prerequisites) in a queue.
     * 3. Take a course from the queue. For each course it unlocks, subtract 1
     *    from its inDegree; when that reaches 0, it's ready, so add it to the
     *    queue.
     * 4. If you took every course, return true. Courses stuck in a cycle
     *    never reach 0.
     *
     * DRY RUN (4 COURSES, [[1,0],[2,1],[3,2],[1,3]])
     *   arrows: 0->1, 1->2, 2->3, 3->1          (1 -> 2 -> 3 -> 1 is a cycle)
     *   inDegree: 0:0  1:2  2:1  3:1
     *   queue [0] -> take 0 -> inDegree[1] = 1 (still waiting for 3)
     *   queue empty. taken = 1, not 4  ->  false
     */
    // Step 2 · the better way
    public static class CourseSchedule {
        public boolean canFinish(int numCourses, int[][] prerequisites) {
            List<List<Integer>> graph = new ArrayList<>();
            for (int i = 0; i < numCourses; i++) graph.add(new ArrayList<>());
            int[] inDegree = new int[numCourses];    // prereqs still needed
            for (int[] p : prerequisites) {
                int course = p[0], pre = p[1];
                graph.get(pre).add(course);          // edge: pre -> course
                inDegree[course]++;
            }
            Queue<Integer> queue = new LinkedList<>();
            for (int c = 0; c < numCourses; c++) {
                if (inDegree[c] == 0) queue.add(c);  // can take right away
            }
            int taken = 0;
            while (!queue.isEmpty()) {
                int c = queue.poll();
                taken++;
                for (int next : graph.get(c)) {
                    inDegree[next]--;                // one prereq done
                    if (inDegree[next] == 0) queue.add(next);
                }
            }
            return taken == numCourses;              // false = a cycle
        }
    }

    /*
     * Time: O(V + E) · Space: O(V + E)
     *
     * WATCH OUT
     * - Arrow direction: [a, b] means b → a. Read the problem twice;
     *   reversing it gives wrong answers on uneven examples.
     */

    // ====================================================================
    // 9. Course Schedule II
    // LeetCode 210 · Medium
    // Trick: same as 5, write the order as you take  ·  O(V + E)
    // ====================================================================

    /**
     * QUESTION
     * Same input as before, but return an order in which you can take every
     * course. Any valid order is fine. Return an empty array if it's
     * impossible.
     *
     * EXAMPLE
     *   4 courses, [[1,0], [2,0], [3,1], [3,2]]   ->  [0, 1, 2, 3]
     *                                                 ([0, 2, 1, 3] is also valid)
     *
     * STEP 1 · BRUTE FORCE
     * Use the same student simulation as Course Schedule, writing down each
     * course as you take it. Step 2 is again Kahn's algorithm.
     *
     * STEP 2 · THE IDEA
     * It's exactly Kahn's algorithm from Course Schedule (problem 8). Write
     * each course into order at the moment you take it.
     *
     * DRY RUN
     *   step | take | inDegree after | queue
     *   start | – | 0:0 1:1 2:1 3:2 | [0]
     *   1 | 0 | 1:0 2:0 | [1, 2]
     *   2 | 1 | 3:1 | [2]
     *   3 | 2 | 3:0 | [3]
     *   4 | 3 | – | [ ]
     *
     * order = [0, 1, 2, 3]
     */
    // Step 2 · the better way
    public static class CourseScheduleII {
        public int[] findOrder(int numCourses, int[][] prerequisites) {
            List<List<Integer>> graph = new ArrayList<>();
            for (int i = 0; i < numCourses; i++) graph.add(new ArrayList<>());
            int[] inDegree = new int[numCourses];
            for (int[] p : prerequisites) {
                graph.get(p[1]).add(p[0]);           // p[1] before p[0]
                inDegree[p[0]]++;
            }
            Queue<Integer> queue = new LinkedList<>();
            for (int c = 0; c < numCourses; c++) {
                if (inDegree[c] == 0) queue.add(c);
            }
            int[] order = new int[numCourses];
            int idx = 0;
            while (!queue.isEmpty()) {
                int c = queue.poll();
                order[idx++] = c;                    // take it now
                for (int next : graph.get(c)) {
                    inDegree[next]--;
                    if (inDegree[next] == 0) queue.add(next);
                }
            }
            return idx == numCourses ? order : new int[0];
        }
    }

    /*
     * Time: O(V + E) · Space: O(V + E)
     *
     * WATCH OUT
     * - If idx < numCourses when the queue empties, there's a cycle, so
     *   return new int[0].
     */

    // ====================================================================
    // 10. Course Schedule IV
    // LeetCode 1462 · Medium
    // Trick: precompute reach[a][b] with a BFS from every course  ·  O(V × (V + E) + Q)
    // ====================================================================

    /**
     * QUESTION
     * Prerequisite pairs [a, b] mean a comes before b, directly or through a
     * chain. For each query [u, v], answer: must u come before v?
     *
     * EXAMPLE
     *   n = 3, prerequisites = [[1,2], [1,0], [2,0]]
     *   queries [1,0] -> true    [1,2] -> true    [0,1] -> false
     *
     * STEP 1 · BRUTE FORCE
     * For each query, run a BFS from u and see whether it reaches v.
     *
     * Brute force cost: O(Q × (V + E)) time, which is painful when there are
     * many queries.
     */
    // Step 1 · brute force
    public static class CourseScheduleIVBrute {
        public List<Boolean> checkIfPrerequisite(int n, int[][] pre,
                                                 int[][] queries) {
            List<List<Integer>> next = new ArrayList<>();
            for (int i = 0; i < n; i++) next.add(new ArrayList<>());
            for (int[] p : pre) next.get(p[0]).add(p[1]);  // p[0] first
            List<Boolean> answers = new ArrayList<>();
            for (int[] q : queries) {
                answers.add(reaches(next, q[0], q[1]));  // BFS per query
            }
            return answers;
        }

        private boolean reaches(List<List<Integer>> next, int from, int to) {
            boolean[] seen = new boolean[next.size()];
            Queue<Integer> queue = new LinkedList<>();
            queue.add(from);
            seen[from] = true;
            while (!queue.isEmpty()) {
                int c = queue.poll();
                if (c == to) return true;
                for (int d : next.get(c)) {
                    if (!seen[d]) {
                        seen[d] = true;
                        queue.add(d);
                    }
                }
            }
            return false;
        }
    }

    /**
     * STEP 2 · THE BETTER IDEA
     * Precompute everything once. Run a BFS from every course and record
     * reach[start][x] = true for each course it reaches. Then every query is
     * a single O(1) lookup.
     */
    // Step 2 · the better way
    public static class CourseScheduleIV {
        public List<Boolean> checkIfPrerequisite(int n, int[][] pre,
                                                 int[][] queries) {
            List<List<Integer>> next = new ArrayList<>();
            for (int i = 0; i < n; i++) next.add(new ArrayList<>());
            for (int[] p : pre) next.get(p[0]).add(p[1]);
            // reach[a][b] = true if a must come before b (precomputed once)
            boolean[][] reach = new boolean[n][n];
            for (int start = 0; start < n; start++) {
                Queue<Integer> queue = new LinkedList<>();
                queue.add(start);
                while (!queue.isEmpty()) {
                    for (int d : next.get(queue.poll())) {
                        if (!reach[start][d]) {
                            reach[start][d] = true;
                            queue.add(d);
                        }
                    }
                }
            }
            List<Boolean> answers = new ArrayList<>();
            for (int[] q : queries) answers.add(reach[q[0]][q[1]]);  // O(1)
            return answers;
        }
    }

    /*
     * Time: O(V × (V + E) + Q) · Space: O(V²)
     *
     * KARAT LINK
     * "Is X an ancestor of Y?" asked many times is the same question as this.
     * When an interviewer says "now assume there are lots of queries", the
     * answer is precompute.
     */

    // ====================================================================
    // 11. Find All Possible Recipes from Given Supplies
    // LeetCode 2115 · Medium
    // Trick: Kahn's algorithm: items you HAVE unlock recipes  ·  O(recipes + ingredients)
    // ====================================================================

    /**
     * QUESTION
     * Each recipe needs a list of ingredients, and an ingredient can itself
     * be another recipe. You start with some supplies. Which recipes can you
     * make?
     *
     * EXAMPLE
     *   bread    needs yeast, flour
     *   sandwich needs bread, meat
     *   burger   needs sandwich, meat, bread
     *   supplies: yeast, flour, meat     ->  [bread, sandwich, burger]
     *
     * STEP 1 · BRUTE FORCE
     * Sweep repeatedly. Go through all the recipes, cook any whose
     * ingredients you now have, add it to your supplies, and repeat until a
     * full sweep cooks nothing new.
     *
     * Brute force cost: O(R² × I) time, because each sweep rechecks every
     * recipe.
     */
    // Step 1 · brute force
    public static class RecipesBrute {
        public List<String> findAllRecipes(String[] recipes,
                List<List<String>> ingredients, String[] supplies) {
            Set<String> have = new HashSet<>(Arrays.asList(supplies));
            List<String> made = new ArrayList<>();
            boolean changed = true;
            while (changed) {                          // sweep until stuck
                changed = false;
                for (int i = 0; i < recipes.length; i++) {
                    if (have.contains(recipes[i])) continue;
                    if (have.containsAll(ingredients.get(i))) {
                        have.add(recipes[i]);          // we can cook it now
                        made.add(recipes[i]);
                        changed = true;
                    }
                }
            }
            return made;
        }
    }

    /**
     * STEP 2 · THE BETTER IDEA
     * This is Course Schedule in disguise. For each recipe, count how many
     * ingredients are still missing. Put every supply in a queue. When you
     * take an item off the queue, every recipe that uses it has one fewer
     * missing ingredient; when a recipe's count reaches 0, you can make it,
     * so it goes into the queue as a new item too.
     */
    // Step 2 · the better way
    public static class Recipes {
        public List<String> findAllRecipes(String[] recipes,
                List<List<String>> ingredients, String[] supplies) {
            // item -> recipes that need it;  recipe -> items still missing
            Map<String, List<String>> usedIn = new HashMap<>();
            Map<String, Integer> missing = new HashMap<>();
            for (int i = 0; i < recipes.length; i++) {
                missing.put(recipes[i], ingredients.get(i).size());
                for (String item : ingredients.get(i)) {
                    usedIn.putIfAbsent(item, new ArrayList<>());
                    usedIn.get(item).add(recipes[i]);
                }
            }
            Queue<String> queue = new LinkedList<>(Arrays.asList(supplies));
            List<String> made = new ArrayList<>();
            while (!queue.isEmpty()) {                 // things we now HAVE
                String item = queue.poll();
                List<String> needs = usedIn.getOrDefault(item, List.of());
                for (String r : needs) {
                    int left = missing.get(r) - 1;
                    missing.put(r, left);
                    if (left == 0) {                   // all items ready
                        made.add(r);
                        queue.add(r);                  // r is an item now too
                    }
                }
            }
            return made;
        }
    }

    /*
     * Time: O(R + total ingredients) · Space: O(R + total ingredients)
     *
     * SPOT THE PATTERN
     * "X can only happen after all of its requirements" means in-degree
     * counting plus a queue, whether it's courses, recipes, build steps or
     * deployments.
     */

    // ====================================================================
    // 12. Find Eventual Safe States
    // LeetCode 802 · Medium
    // Trick: DFS with 3 states: 0 new, 1 on path/unsafe, 2 safe  ·  O(V + E)
    // ====================================================================

    /**
     * QUESTION
     * In a directed graph, a node is safe if every path starting from it ends
     * at a node with no outgoing edges. In other words, it can never fall
     * into a cycle. Return all safe nodes in increasing order.
     *
     * EXAMPLE
     *   graph = [[1,2],[2,3],[5],[0],[5],[],[]]   ->  [2, 4, 5, 6]
     *   (0 -> 1 -> 3 -> 0 is a cycle, so 0, 1 and 3 are unsafe)
     *
     * STEP 1 · BRUTE FORCE
     * For each node, explore every path from it looking for a cycle, without
     * remembering anything between nodes. The same parts of the graph get re-
     * explored again and again.
     *
     * STEP 2 · THE IDEA
     * Do one DFS with three states per node:
     *
     * - 0 = not visited yet
     * - 1 = on the current path. Meeting a 1 again means a cycle, and the
     *   node stays 1 = unsafe.
     * - 2 = proven safe, because every edge out of it leads to safe nodes.
     *
     * Each node is resolved once and remembered.
     */
    // Step 2 · the better way
    public static class SafeStates {
        // 0 = not visited, 1 = on the current path (or unsafe), 2 = safe
        public List<Integer> eventualSafeNodes(int[][] graph) {
            int[] state = new int[graph.length];
            List<Integer> safe = new ArrayList<>();
            for (int i = 0; i < graph.length; i++) {
                if (isSafe(graph, i, state)) safe.add(i);
            }
            return safe;
        }

        private boolean isSafe(int[][] g, int node, int[] state) {
            if (state[node] != 0) return state[node] == 2;  // 1 = cycle
            state[node] = 1;                     // we're walking through it
            for (int next : g[node]) {
                if (!isSafe(g, next, state)) return false;  // stays 1: unsafe
            }
            state[node] = 2;                     // every path out is safe
            return true;
        }
    }

    /*
     * Time: O(V + E) · Space: O(V)
     *
     * SAME 3-STATE TRICK
     * It's also the DFS way to detect a cycle in Course Schedule. Kahn's
     * algorithm and 3-state DFS are interchangeable; know one well.
     */

    // ====================================================================
    // 13. Parent–Child Ancestors (3 parts)  ★ Karat
    // A Karat question asked in parts, back to back · Medium
    // Trick: P1 count parents; P2 child→parents + BFS up; P3 by generations  ·  O(n)
    // ====================================================================

    /**
     * QUESTION
     * You get pairs [parent, child] describing a family. A person can have 0,
     * 1 or 2 parents.
     *
     * - Part 1: return two lists: people with zero parents, and people with
     *   exactly one parent.
     * - Part 2: given two people, do they share at least one ancestor?
     * - Part 3: given a person, return their earliest ancestor, the one
     *   farthest up. If there's a tie, return any (this code returns the
     *   smallest id). If the person has no parents, return −1.
     *
     * EXAMPLE FAMILY
     *   pairs (parent -> child):
     *     10->4   4->5   5->7   5->6
     *      1->3   2->3   3->6   6->8
     *
     *   person | parents
     *   3 | 1, 2
     *   4 | 10
     *   5 | 4
     *   6 | 3, 5
     *   7 | 5
     *   8 | 6
     *   1, 2, 10 | none
     *
     *   Part 1:  zero parents -> [1, 2, 10]      one parent -> [4, 5, 7, 8]
     *   Part 2:  (7, 8) -> true   (both descend from 5)      (3, 7) -> false
     *   Part 3:  earliest(8) -> 10     earliest(3) -> 1     earliest(10) -> -1
     *
     * STEP 1 · BRUTE FORCE
     * Part 2 brute force: for each ancestor of a, walk up from b again to
     * check whether it's an ancestor of b too. That repeats the same climb
     * many times: O(n²). Step 2 collects each person's ancestors once into a
     * set and intersects them.
     *
     * STEP 2 · THE IDEA
     * Part 1: count parents per person (in-degree). It's the same code as
     * Zero or One Losses in Part 1 of this series.
     *
     * Part 2: build a child → parents map. These are reversed arrows, so you
     * can walk up. BFS up from a to collect all of its ancestors in a set,
     * then BFS up from b and check whether any of b's ancestors is in that
     * set.
     *
     * Part 3: climb one generation at a time: {node}, then its parents, then
     * their parents, and so on. The earliest ancestor is in the last non-
     * empty generation.
     *
     * DRY RUN, PART 3, FOR PERSON 8
     *   generation 0: {8}
     *   generation 1: {6}            answer = 6
     *   generation 2: {3, 5}         answer = 3
     *   generation 3: {1, 2, 4}      answer = 1
     *   generation 4: {10}           answer = 10
     *   generation 5: { }   stop  ->  10
     */
    // Step 2 · the better way
    public static class KaratAncestors {
        // ---------- Part 1: people with 0 parents and exactly 1 parent
        public List<List<Integer>> zeroAndOneParent(int[][] pairs) {
            Map<Integer, Integer> parentCount = new TreeMap<>();  // sorted
            for (int[] p : pairs) {
                int parent = p[0], child = p[1];
                parentCount.putIfAbsent(parent, 0);
                int count = parentCount.getOrDefault(child, 0);
                parentCount.put(child, count + 1);
            }
            List<Integer> zero = new ArrayList<>();
            List<Integer> one = new ArrayList<>();
            for (int person : parentCount.keySet()) {
                if (parentCount.get(person) == 0) zero.add(person);
                if (parentCount.get(person) == 1) one.add(person);
            }
            return Arrays.asList(zero, one);
        }

        // helper: child -> list of its parents
        private Map<Integer, List<Integer>> parentsOf(int[][] pairs) {
            Map<Integer, List<Integer>> parents = new HashMap<>();
            for (int[] p : pairs) {
                parents.putIfAbsent(p[1], new ArrayList<>());
                parents.get(p[1]).add(p[0]);
            }
            return parents;
        }

        // helper: every ancestor of node (parents, grandparents, ...)
        private Set<Integer> ancestors(Map<Integer, List<Integer>> parents,
                                       int node) {
            Set<Integer> found = new HashSet<>();
            Queue<Integer> queue = new LinkedList<>();
            queue.add(node);
            while (!queue.isEmpty()) {
                int cur = queue.poll();
                for (int p : parents.getOrDefault(cur, new ArrayList<>())) {
                    if (found.add(p)) queue.add(p);   // new ancestor
                }
            }
            return found;
        }

        // ---------- Part 2: do a and b share at least one ancestor?
        public boolean hasCommonAncestor(int[][] pairs, int a, int b) {
            Map<Integer, List<Integer>> parents = parentsOf(pairs);
            Set<Integer> fromA = ancestors(parents, a);
            for (int x : ancestors(parents, b)) {
                if (fromA.contains(x)) return true;
            }
            return false;
        }

        // ---------- Part 3: the ancestor farthest up (-1 if no parents).
        // On a tie, return the smallest id.
        public int earliestAncestor(int[][] pairs, int node) {
            Map<Integer, List<Integer>> parents = parentsOf(pairs);
            Set<Integer> generation = new HashSet<>();
            generation.add(node);
            int answer = -1;
            while (true) {                       // climb one generation
                Set<Integer> older = new HashSet<>();
                for (int person : generation) {
                    older.addAll(parents.getOrDefault(person,
                                                      new ArrayList<>()));
                }
                if (older.isEmpty()) break;      // nobody further up
                answer = Collections.min(older); // best so far
                generation = older;
            }
            return answer;
        }
    }

    /*
     * Time: Part 1 O(n log n); Parts 2 and 3 O(n) per question · Space: O(n)
     *
     * WATCH OUT
     * - Part 2: is a person their own ancestor? Usually not, which is why
     *   this code starts from the parents. Ask the interviewer.
     * - found.add(p) returns false for an ancestor you've already reached by
     *   another route, so a shared ancestor isn't processed twice.
     * - Part 3 ties: ask which one they want. "Smallest id" is a safe
     *   default.
     *
     * KARAT TIP
     * All three parts come one after another in about 40 minutes. Write the
     * parentsOf and ancestors helpers properly in Part 2, because Part 3
     * reuses parentsOf for free.
     */

    // ====================================================================
    // 14. All Ancestors of a Node in a DAG
    // LeetCode 2192 · Karat's Ancestors question, for every node · Medium
    // Trick: walk parents up per node; or topological order + merge sets  ·  O(n × (n + e))
    // ====================================================================

    /**
     * QUESTION
     * Given a directed acyclic graph with nodes 0..n−1, return the sorted
     * list of ancestors for every node.
     *
     * EXAMPLE
     *   edges: 0->3 0->4 1->3 2->4 2->7 3->5 3->6 3->7 4->6
     *   node 3: [0, 1]      node 6: [0, 1, 2, 3, 4]      node 7: [0, 1, 2, 3]
     *
     * STEP 1 · BRUTE FORCE
     * This is exactly the Karat Ancestors helper, run once per node: reverse
     * the arrows (child → parents) and walk up from each node, collecting
     * into a TreeSet so the list comes out sorted.
     *
     * Brute force cost: O(n × (n + e)) time. Every node re-walks everything
     * above it.
     */
    // Step 1 · brute force
    public static class AllAncestorsBrute {
        public List<List<Integer>> getAncestors(int n, int[][] edges) {
            List<List<Integer>> parents = new ArrayList<>();
            for (int i = 0; i < n; i++) parents.add(new ArrayList<>());
            // child -> its parents (arrows reversed)
            for (int[] e : edges) parents.get(e[1]).add(e[0]);
            List<List<Integer>> result = new ArrayList<>();
            for (int i = 0; i < n; i++) {          // walk up from EVERY node
                Set<Integer> found = new TreeSet<>(); // sorted, no repeats
                Deque<Integer> stack = new ArrayDeque<>();
                stack.push(i);
                while (!stack.isEmpty()) {
                    for (int p : parents.get(stack.pop())) {
                        if (found.add(p)) stack.push(p);
                    }
                }
                result.add(new ArrayList<>(found));
            }
            return result;
        }
    }

    /**
     * STEP 2 · THE BETTER IDEA
     * Reuse work. Process the nodes in topological order (Kahn's algorithm).
     * When you take a node off the queue, all of its ancestors are already
     * known, so pass them down: each child gets node + ancestors(node). Every
     * set is built once, from its parents' finished sets.
     */
    // Step 2 · the better way
    public static class AllAncestors {
        public List<List<Integer>> getAncestors(int n, int[][] edges) {
            List<List<Integer>> children = new ArrayList<>();
            List<Set<Integer>> anc = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                children.add(new ArrayList<>());
                anc.add(new TreeSet<>());
            }
            int[] inDegree = new int[n];
            for (int[] e : edges) {
                children.get(e[0]).add(e[1]);
                inDegree[e[1]]++;
            }
            Queue<Integer> queue = new LinkedList<>();   // topological order
            for (int i = 0; i < n; i++) {
                if (inDegree[i] == 0) queue.add(i);
            }
            while (!queue.isEmpty()) {
                int node = queue.poll();             // its set is final now
                for (int child : children.get(node)) {
                    anc.get(child).add(node);              // the parent
                    anc.get(child).addAll(anc.get(node));  // + its ancestors
                    inDegree[child]--;
                    if (inDegree[child] == 0) queue.add(child);
                }
            }
            List<List<Integer>> result = new ArrayList<>();
            for (Set<Integer> s : anc) result.add(new ArrayList<>(s));
            return result;
        }
    }

    /*
     * Time: O(n × e) set merging in the worst case, with no repeated walks ·
     * Space: O(n²)
     *
     * KARAT LINK
     * Karat's Ancestors question (problem 13) asks this for one person at a
     * time. If they ask "what if we need it for everyone?", this is the
     * answer.
     */

    // ====================================================================
    // 15. Loud and Rich
    // LeetCode 851 · Medium
    // Trick: memoised DFS up the "richer than" edges  ·  O(n + e)
    // ====================================================================

    /**
     * QUESTION
     * [a, b] means a is richer than b, and quiet[i] is how quiet person i is.
     * For each person x, return the quietest person among everyone who is at
     * least as rich as x, including x.
     *
     * EXAMPLE
     *   richer = [[1,0],[2,1],[3,1],[3,7],[4,3],[5,3],[6,3]]
     *   quiet  = [3, 2, 5, 4, 6, 1, 7, 0]
     *   answer = [5, 5, 2, 5, 4, 5, 6, 7]
     *
     * STEP 1 · BRUTE FORCE
     * For each person, run a full DFS up the "richer than" edges, looking at
     * everyone above them, and keep the quietest.
     *
     * Brute force cost: O(n × (n + e)) time
     */
    // Step 1 · brute force
    public static class LoudRichBrute {
        public int[] loudAndRich(int[][] richer, int[] quiet) {
            int n = quiet.length;
            List<List<Integer>> richerThan = new ArrayList<>();
            for (int i = 0; i < n; i++) richerThan.add(new ArrayList<>());
            // r[0] is richer than r[1]
            for (int[] r : richer) richerThan.get(r[1]).add(r[0]);
            int[] ans = new int[n];
            for (int x = 0; x < n; x++) {          // full search per person
                int best = x;
                boolean[] seen = new boolean[n];
                Deque<Integer> stack = new ArrayDeque<>();
                stack.push(x);
                seen[x] = true;
                while (!stack.isEmpty()) {
                    int p = stack.pop();
                    if (quiet[p] < quiet[best]) best = p;
                    for (int q : richerThan.get(p)) {
                        if (!seen[q]) {
                            seen[q] = true;
                            stack.push(q);
                        }
                    }
                }
                ans[x] = best;
            }
            return ans;
        }
    }

    /**
     * STEP 2 · THE BETTER IDEA
     * answer[x] is the quietest of x itself and answer[r] for each person r
     * directly richer than x, because answer[r] already covers everyone above
     * r. So solve it with a DFS that remembers each answer (memoisation).
     * Every person is solved exactly once.
     */
    // Step 2 · the better way
    public static class LoudRich {
        private List<List<Integer>> richerThan;
        private int[] quiet, ans;

        public int[] loudAndRich(int[][] richer, int[] quiet) {
            int n = quiet.length;
            this.quiet = quiet;
            richerThan = new ArrayList<>();
            for (int i = 0; i < n; i++) richerThan.add(new ArrayList<>());
            for (int[] r : richer) richerThan.get(r[1]).add(r[0]);
            ans = new int[n];
            Arrays.fill(ans, -1);                      // -1 = not solved yet
            for (int x = 0; x < n; x++) solve(x);
            return ans;
        }

        // quietest person among x and everyone richer than x (memoised)
        private int solve(int x) {
            if (ans[x] != -1) return ans[x];
            int best = x;
            for (int r : richerThan.get(x)) {
                int candidate = solve(r);              // reuse r's answer
                if (quiet[candidate] < quiet[best]) best = candidate;
            }
            ans[x] = best;
            return best;
        }
    }

    /*
     * Time: O(n + e) · Space: O(n + e)
     *
     * KARAT LINK
     * Same shape as Karat's ancestors questions: "something about everyone
     * above me". Memoising the answer per person turns n separate searches
     * into one pass.
     */

    // ====================================================================
    // 16. Middle Course (2 parts)  ★ Karat
    // A Karat question asked in parts · Medium
    // Trick: start = never a "next"; index (size − 1) / 2; DFS all paths  ·  O(n)
    // ====================================================================

    /**
     * QUESTION
     * You get pairs [prerequisite, course].
     *
     * - Part 1: the courses form one straight chain. Return the course in the
     *   middle of it. If there's an even number of courses, return the one
     *   just before the middle.
     * - Part 2: tracks can split, and there can be several starting courses.
     *   For every complete path from a start to an end, find its middle
     *   course. Return all the different middle courses.
     *
     * EXAMPLE, PART 1
     *   [["Java","Spring"], ["Basics","Java"], ["Spring","Kafka"], ["Kafka","Microservices"]]
     *
     *   chain: Basics -> Java -> Spring -> Kafka -> Microservices   ->  "Spring"
     *   without Microservices: Basics -> Java -> Spring -> Kafka    ->  "Java"
     *
     * EXAMPLE, PART 2
     *   Basics -> Java -> Spring -> Kafka          middle: Java
     *   Basics -> Python -> Django                 middle: Python
     *   Basics -> Python -> ML -> LLMs             middle: Python
     *   SQL -> Spring -> Kafka                     middle: Spring
     *
     *   answer: {Java, Python, Spring}
     *
     * STEP 1 · BRUTE FORCE
     * The input is small and the work is a single walk (Part 1) or one DFS
     * over the paths (Part 2), so there's no meaningfully slower version.
     * Focus on finding the start correctly.
     *
     * STEP 2 · THE IDEA
     * Part 1: the start is the one course that is never anyone's next course.
     * Build a map course → next course, walk from the start to get the chain
     * in order, and pick index (size − 1) / 2.
     *
     * Part 2: build a map course → list of next courses. From every start,
     * DFS down to every end (a course with no next), keeping the current
     * path, just like All Paths (problem 7). At each end, add the path's
     * middle to a set.
     *
     * Why (size − 1) / 2? For size 5 it gives index 2, the exact middle. For
     * size 4 it gives index 1, the left of the two middles.
     */
    // Step 2 · the better way
    public static class KaratMiddleCourse {
        // ---------- Part 1: courses form ONE straight chain
        public String middleCourse(String[][] pairs) {
            Map<String, String> next = new HashMap<>();  // course -> next
            Set<String> hasPrereq = new HashSet<>();
            for (String[] p : pairs) {
                next.put(p[0], p[1]);
                hasPrereq.add(p[1]);
            }
            String start = null;                 // the course nobody needs
            for (String c : next.keySet()) {
                if (!hasPrereq.contains(c)) start = c;
            }
            List<String> order = new ArrayList<>();
            for (String c = start; c != null; c = next.get(c)) {
                order.add(c);                    // walk the chain
            }
            return order.get((order.size() - 1) / 2);   // even: left middle
        }

        // ---------- Part 2: courses can branch; middle of EVERY path
        public Set<String> allMiddleCourses(String[][] pairs) {
            Map<String, List<String>> graph = new HashMap<>();
            Set<String> hasPrereq = new HashSet<>();
            for (String[] p : pairs) {
                graph.putIfAbsent(p[0], new ArrayList<>());
                graph.get(p[0]).add(p[1]);
                hasPrereq.add(p[1]);
            }
            Set<String> middles = new TreeSet<>();
            for (String c : graph.keySet()) {
                if (!hasPrereq.contains(c)) {    // every starting course
                    List<String> path = new ArrayList<>();
                    path.add(c);
                    walk(graph, c, path, middles);
                }
            }
            return middles;
        }

        private void walk(Map<String, List<String>> graph, String course,
                          List<String> path, Set<String> middles) {
            List<String> nexts = graph.get(course);
            if (nexts == null) {                 // end of one full path
                middles.add(path.get((path.size() - 1) / 2));
                return;
            }
            for (String n : nexts) {
                path.add(n);
                walk(graph, n, path, middles);
                path.remove(path.size() - 1);    // backtrack
            }
        }
    }

    /*
     * Time: Part 1 O(n); Part 2 O(number of paths × path length) · Space:
     * O(n)
     *
     * WATCH OUT
     * - Pairs can arrive in any order. Never assume the first pair is the
     *   start of the chain.
     * - Use a Set for the Part 2 answers, because two paths can share a
     *   middle course. A TreeSet keeps the output sorted and easy to check.
     */

    // ====================================================================
    // 17. Minimum Genetic Mutation
    // LeetCode 433 · Medium
    // Trick: BFS over strings; neighbours = change 1 letter to A/C/G/T  ·  O(bank × L × 4)
    // ====================================================================

    /**
     * QUESTION
     * A gene is an 8-letter string of A, C, G, T. One mutation changes one
     * letter, and every intermediate gene must be in bank. What's the fewest
     * mutations from start to end? Return −1 if it's impossible.
     *
     * EXAMPLE
     *   start AACCGGTT, end AAACGGTA, bank [AACCGGTA, AACCGCTA, AAACGGTA]   ->  2
     *   AACCGGTT -> AACCGGTA -> AAACGGTA
     *
     * STEP 1 · BRUTE FORCE
     * DFS through every chain of mutations and keep the shortest. That's
     * exponential, and "fewest steps" means BFS.
     *
     * STEP 2 · THE IDEA
     * It's BFS where the nodes are strings. A string's neighbours come from
     * changing one position to each of A/C/G/T and keeping the results that
     * are in the bank and not seen yet. Count BFS levels until you pop end.
     */
    // Step 2 · the better way
    public static class GeneticMutation {
        public int minMutation(String start, String end, String[] bank) {
            Set<String> valid = new HashSet<>(Arrays.asList(bank));
            if (!valid.contains(end)) return -1;
            char[] letters = {'A', 'C', 'G', 'T'};
            Queue<String> queue = new LinkedList<>();
            Set<String> seen = new HashSet<>();
            queue.add(start);
            seen.add(start);
            int steps = 0;
            while (!queue.isEmpty()) {
                int size = queue.size();               // one BFS level
                for (int i = 0; i < size; i++) {
                    String cur = queue.poll();
                    if (cur.equals(end)) return steps;
                    char[] chars = cur.toCharArray();
                    for (int p = 0; p < chars.length; p++) {  // each spot
                        char old = chars[p];
                        for (char ch : letters) {              // each letter
                            chars[p] = ch;
                            String next = new String(chars);
                            if (valid.contains(next) && seen.add(next)) {
                                queue.add(next);
                            }
                        }
                        chars[p] = old;                        // put it back
                    }
                }
                steps++;
            }
            return -1;
        }
    }

    /*
     * Time: O(bank × L × 4) · Space: O(bank)
     *
     * WATCH OUT
     * - If end isn't in the bank, return −1 immediately.
     */

    // ====================================================================
    // 18. Word Ladder
    // LeetCode 127 · Hard
    // Trick: BFS; neighbours by trying a–z at each position  ·  O(n × L × 26)
    // ====================================================================

    /**
     * QUESTION
     * Change begin into end one letter at a time. Every word along the way
     * must be in the word list. Return the number of words in the shortest
     * chain (counting begin), or 0 if there's no chain.
     *
     * EXAMPLE
     *   cold -> cord -> card -> ward -> warm      ->  5
     *   list: cord, card, ward, warm, worm, word, wore, core
     *
     * STEP 1 · BRUTE FORCE
     * BFS, but find neighbours by scanning the whole word list and checking
     * which words differ by exactly one letter.
     *
     * Brute force cost: O(n² × L) time, because every word is compared with
     * every other word.
     */
    // Step 1 · brute force
    public static class WordLadderScan {
        public int ladderLength(String begin, String end,
                                List<String> words) {
            Queue<String> queue = new LinkedList<>();
            Set<String> seen = new HashSet<>();
            queue.add(begin);
            seen.add(begin);
            int length = 1;                            // words in the chain
            while (!queue.isEmpty()) {
                int size = queue.size();
                for (int i = 0; i < size; i++) {
                    String cur = queue.poll();
                    if (cur.equals(end)) return length;
                    for (String w : words) {           // scan the WHOLE list
                        if (!seen.contains(w) && oneApart(cur, w)) {
                            seen.add(w);
                            queue.add(w);
                        }
                    }
                }
                length++;
            }
            return 0;
        }

        private boolean oneApart(String a, String b) {
            int diff = 0;
            for (int i = 0; i < a.length(); i++) {
                if (a.charAt(i) != b.charAt(i)) diff++;
            }
            return diff == 1;
        }
    }

    /**
     * STEP 2 · THE BETTER IDEA
     * Generate the neighbours instead of searching for them. For each
     * position, try all 26 letters and check the resulting word against a
     * HashSet in O(1). Remove a word from the set when you queue it, so it's
     * never queued twice. That works as your visited set.
     *
     * DRY RUN
     *   level 1: cold
     *   level 2: cord              (c-o-l-d: change l -> r)
     *   level 3: card, word, core
     *   level 4: ward, worm, wore
     *   level 5: warm   ->  return 5
     */
    // Step 2 · the better way
    public static class WordLadder {
        public int ladderLength(String begin, String end,
                                List<String> words) {
            Set<String> unused = new HashSet<>(words);
            if (!unused.contains(end)) return 0;
            Queue<String> queue = new LinkedList<>();
            queue.add(begin);
            int length = 1;
            while (!queue.isEmpty()) {
                int size = queue.size();
                for (int i = 0; i < size; i++) {
                    String cur = queue.poll();
                    if (cur.equals(end)) return length;
                    char[] chars = cur.toCharArray();
                    for (int p = 0; p < chars.length; p++) {
                        char old = chars[p];
                        for (char ch = 'a'; ch <= 'z'; ch++) {  // 26 tries
                            chars[p] = ch;
                            String next = new String(chars);
                            if (unused.remove(next)) {  // in list, 1st time
                                queue.add(next);
                            }
                        }
                        chars[p] = old;
                    }
                }
                length++;
            }
            return 0;
        }
    }

    /*
     * Time: O(n × L × 26) · Space: O(n × L)
     *
     * WATCH OUT
     * - The answer counts words, not moves. That's why length starts at 1.
     * - unused.remove(next) returns true only the first time, which acts as a
     *   combined "is it in the list?" and "seen it?" check.
     */

    // ====================================================================
    // 19. Network Delay Time
    // LeetCode 743 · Dijkstra · Medium
    // Trick: Dijkstra: min-heap of (node, dist); skip stale entries  ·  O(E log V)
    // ====================================================================

    /**
     * QUESTION
     * A signal starts at node k. times[i] = [u, v, w] means it takes w time
     * to travel from u to v. How long until every node has the signal? Return
     * −1 if some node never gets it.
     *
     * EXAMPLE
     *   edges: 1->2 (4), 1->3 (1), 3->2 (1), 2->4 (1)      start 1   ->  3
     *   best routes: 3 = 1, 2 = 1+1 = 2 (via 3, not the direct 4), 4 = 2+1 = 3
     *
     * STEP 1 · BRUTE FORCE
     * Bellman–Ford. Repeat n − 1 times: go over every edge and lower dist[v]
     * if dist[u] + w is smaller. After n − 1 rounds, every shortest path has
     * been found.
     *
     * Brute force cost: O(V × E) time. Simple, and it also works with
     * negative edges, but it's slow.
     */
    // Step 1 · brute force
    public static class NetworkDelayBellman {
        public int networkDelayTime(int[][] times, int n, int k) {
            int INF = Integer.MAX_VALUE;
            int[] dist = new int[n + 1];
            Arrays.fill(dist, INF);
            dist[k] = 0;
            for (int round = 1; round < n; round++) {  // n - 1 rounds
                for (int[] t : times) {                // relax EVERY edge
                    int u = t[0], v = t[1], w = t[2];
                    if (dist[u] != INF && dist[u] + w < dist[v]) {
                        dist[v] = dist[u] + w;
                    }
                }
            }
            int worst = 0;
            for (int i = 1; i <= n; i++) {
                if (dist[i] == INF) return -1;         // never reached
                worst = Math.max(worst, dist[i]);
            }
            return worst;
        }
    }

    /**
     * STEP 2 · THE BETTER IDEA
     * Dijkstra. Always expand the node that's currently closest, using a min-
     * heap of {node, distance}. When you pop a node, its distance is final.
     * Push a neighbour whenever you find a shorter way to it. The heap may
     * hold old, larger entries for a node; skip them when they're popped.
     *
     * DRY RUN (EXAMPLE)
     *   pop 1 (0)  -> push 2 (4), 3 (1)
     *   pop 3 (1)  -> 2 via 3 = 2 < 4, push 2 (2)
     *   pop 2 (2)  -> push 4 (3)
     *   pop 4 (3)  -> no edges out
     *   pop 2 (4)  -> stale (4 > 2), skip.   max distance = 3
     */
    // Step 2 · the better way
    public static class NetworkDelay {
        public int networkDelayTime(int[][] times, int n, int k) {
            List<List<int[]>> g = new ArrayList<>();    // node -> {to, time}
            for (int i = 0; i <= n; i++) g.add(new ArrayList<>());
            for (int[] t : times) g.get(t[0]).add(new int[] { t[1], t[2] });

            int[] dist = new int[n + 1];
            Arrays.fill(dist, Integer.MAX_VALUE);
            dist[k] = 0;
            // heap of {node, distance}, closest first
            PriorityQueue<int[]> heap = new PriorityQueue<>(
                    (a, b) -> Integer.compare(a[1], b[1]));
            heap.add(new int[] { k, 0 });
            while (!heap.isEmpty()) {
                int[] top = heap.poll();
                int u = top[0], d = top[1];
                if (d > dist[u]) continue;              // stale entry: skip
                for (int[] e : g.get(u)) {
                    int v = e[0], nd = d + e[1];
                    if (nd < dist[v]) {                 // found a faster way
                        dist[v] = nd;
                        heap.add(new int[] { v, nd });
                    }
                }
            }
            int worst = 0;
            for (int i = 1; i <= n; i++) {
                if (dist[i] == Integer.MAX_VALUE) return -1;
                worst = Math.max(worst, dist[i]);
            }
            return worst;
        }
    }

    /*
     * Time: O(E log V) · Space: O(V + E)
     *
     * WATCH OUT
     * - Dijkstra needs non-negative weights. With negative edges, use
     *   Bellman–Ford.
     * - The if (d > dist[u]) continue; line is what makes skipping stale
     *   entries safe and fast.
     */

}
