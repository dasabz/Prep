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

    // ====================================================================
    // 2. Number of Provinces
    // LeetCode 547 · Medium
    // Trick: for each unvisited: count++ and DFS; or Union-Find  ·  O(n²)
    // ====================================================================

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

    // ====================================================================
    // 3. Number of Operations to Make Network Connected
    // LeetCode 1319 · Medium
    // Trick: need ≥ n−1 cables; answer = groups − 1 (Union-Find)  ·  O(n + e)
    // ====================================================================

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

    // ====================================================================
    // 4. Redundant Connection
    // LeetCode 684 · Medium
    // Trick: Union-Find: first edge whose ends are already joined  ·  O(n)
    // ====================================================================

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

    // ====================================================================
    // 5. Is Graph Bipartite?
    // LeetCode 785 · Medium
    // Trick: BFS colouring: neighbours get the opposite colour  ·  O(V + E)
    // ====================================================================

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

    // ====================================================================
    // 6. Clone Graph
    // LeetCode 133 · Medium
    // Trick: map original → copy; store the copy BEFORE recursing  ·  O(V + E)
    // ====================================================================

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

    // ====================================================================
    // 7. All Paths From Source to Target
    // LeetCode 797 · Medium
    // Trick: DFS + path list; save a copy at the target  ·  O(2ⁿ·n)
    // ====================================================================

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

    // ====================================================================
    // 8. Course Schedule
    // LeetCode 207 · Medium
    // Trick: Kahn: start from inDegree 0; taken == n?  ·  O(V + E)
    // ====================================================================

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

    // ====================================================================
    // 9. Course Schedule II
    // LeetCode 210 · Medium
    // Trick: same as 5, write the order as you take  ·  O(V + E)
    // ====================================================================

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

    // ====================================================================
    // 10. Course Schedule IV
    // LeetCode 1462 · Medium
    // Trick: precompute reach[a][b] with a BFS from every course  ·  O(V × (V + E) + Q)
    // ====================================================================

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

    // ====================================================================
    // 11. Find All Possible Recipes from Given Supplies
    // LeetCode 2115 · Medium
    // Trick: Kahn's algorithm: items you HAVE unlock recipes  ·  O(recipes + ingredients)
    // ====================================================================

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

    // ====================================================================
    // 12. Find Eventual Safe States
    // LeetCode 802 · Medium
    // Trick: DFS with 3 states: 0 new, 1 on path/unsafe, 2 safe  ·  O(V + E)
    // ====================================================================

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

    // ====================================================================
    // 13. Parent–Child Ancestors (3 parts)  ★ Karat
    // A Karat question asked in parts, back to back · Medium
    // Trick: P1 count parents; P2 child→parents + BFS up; P3 by generations  ·  O(n)
    // ====================================================================

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

    // ====================================================================
    // 14. All Ancestors of a Node in a DAG
    // LeetCode 2192 · Karat's Ancestors question, for every node · Medium
    // Trick: walk parents up per node; or topological order + merge sets  ·  O(n × (n + e))
    // ====================================================================

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

    // ====================================================================
    // 15. Loud and Rich
    // LeetCode 851 · Medium
    // Trick: memoised DFS up the "richer than" edges  ·  O(n + e)
    // ====================================================================

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

    // ====================================================================
    // 16. Middle Course (2 parts)  ★ Karat
    // A Karat question asked in parts · Medium
    // Trick: start = never a "next"; index (size − 1) / 2; DFS all paths  ·  O(n)
    // ====================================================================

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

    // ====================================================================
    // 17. Minimum Genetic Mutation
    // LeetCode 433 · Medium
    // Trick: BFS over strings; neighbours = change 1 letter to A/C/G/T  ·  O(bank × L × 4)
    // ====================================================================

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

    // ====================================================================
    // 18. Word Ladder
    // LeetCode 127 · Hard
    // Trick: BFS; neighbours by trying a–z at each position  ·  O(n × L × 26)
    // ====================================================================

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

    // ====================================================================
    // 19. Network Delay Time
    // LeetCode 743 · Dijkstra · Medium
    // Trick: Dijkstra: min-heap of (node, dist); skip stale entries  ·  O(E log V)
    // ====================================================================

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

}
