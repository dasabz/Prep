package karat.graphs;

import java.util.*;
import karat.T;
import karat.graphs.GraphsEasy.*;
import karat.graphs.GraphsMedium.*;

/** Runs every example from Part 4's PDFs against the code. */
public class GraphsTests {
    public static void main(String[] args) {
        Examples1.run();
        Examples2.run();
        Examples3.run();
        Examples4.run();
        T.done("Graphs");
    }

    static class Examples1 {
        static void run() {
            TownJudge tj = new TownJudge();
            T.check("judge1", tj.findJudge(3, new int[][] {{1, 3}, {2, 3}}), 3);
            T.check("judge2", tj.findJudge(3, new int[][] {{1, 3}, {2, 3}, {3, 1}}), -1);
            T.check("judge3", tj.findJudge(1, new int[][] {}), 1);
            T.check("judge4", tj.findJudge(4, new int[][] {{1, 2}, {3, 2}, {4, 2}, {1, 3}}), 2);

            PathExists pe = new PathExists();
            T.check("path1", pe.validPath(6, new int[][] {{0, 1}, {1, 2}, {3, 4}, {4, 5}}, 0, 2), true);
            T.check("path2", pe.validPath(6, new int[][] {{0, 1}, {1, 2}, {3, 4}, {4, 5}}, 0, 5), false);
            T.check("path3", pe.validPath(1, new int[][] {}, 0, 0), true);

            int[][] cities = {{1, 1, 0, 0}, {1, 1, 0, 0}, {0, 0, 1, 1}, {0, 0, 1, 1}};
            int[][] alone = {{1, 0, 0}, {0, 1, 0}, {0, 0, 1}};
            int[][] chain = {{1, 1, 0}, {1, 1, 1}, {0, 1, 1}};
            T.check("prov1", new NumberOfProvinces().findCircleNum(cities), 2);
            T.check("prov2", new NumberOfProvinces().findCircleNum(alone), 3);
            T.check("prov3", new NumberOfProvinces().findCircleNum(chain), 1);
            T.check("provUF1", new ProvincesUnionFind().findCircleNum(cities), 2);
            T.check("provUF2", new ProvincesUnionFind().findCircleNum(alone), 3);
            T.check("provUF3", new ProvincesUnionFind().findCircleNum(chain), 1);

            AllPaths ap = new AllPaths();
            T.check("allpaths1", ap.allPathsSourceTarget(new int[][] {{1, 2}, {3}, {3}, {}}),
                    Arrays.asList(Arrays.asList(0, 1, 3), Arrays.asList(0, 2, 3)));
            T.check("allpaths2", ap.allPathsSourceTarget(new int[][] {{1, 2, 3}, {3}, {1}, {}}),
                    Arrays.asList(Arrays.asList(0, 1, 3), Arrays.asList(0, 2, 1, 3), Arrays.asList(0, 3)));

            CourseSchedule cs = new CourseSchedule();
            T.check("cs1", cs.canFinish(2, new int[][] {{1, 0}}), true);
            T.check("cs2", cs.canFinish(2, new int[][] {{1, 0}, {0, 1}}), false);
            T.check("cs3", cs.canFinish(4, new int[][] {{1, 0}, {2, 1}, {3, 2}, {1, 3}}), false);
            T.check("cs4", cs.canFinish(3, new int[][] {}), true);

            CourseScheduleII cs2 = new CourseScheduleII();
            T.check("order1", cs2.findOrder(4, new int[][] {{1, 0}, {2, 0}, {3, 1}, {3, 2}}), new int[] {0, 1, 2, 3});
            T.check("order2", cs2.findOrder(2, new int[][] {{0, 1}, {1, 0}}), new int[] {});
            T.check("order3", cs2.findOrder(3, new int[][] {{0, 2}}), new int[] {1, 2, 0});

            KaratAncestors ka = new KaratAncestors();
            int[][] family = {{10, 4}, {4, 5}, {1, 3}, {2, 3}, {3, 6}, {5, 6}, {5, 7}, {6, 8}};
            T.check("anc p1", ka.zeroAndOneParent(family), Arrays.asList(Arrays.asList(1, 2, 10), Arrays.asList(4, 5, 7, 8)));
            T.check("anc p2a", ka.hasCommonAncestor(family, 7, 8), true);
            T.check("anc p2b", ka.hasCommonAncestor(family, 3, 7), false);
            T.check("anc p2c", ka.hasCommonAncestor(family, 3, 8), true);
            T.check("anc p2d", ka.hasCommonAncestor(family, 6, 7), true);
            T.check("anc p3a", ka.earliestAncestor(family, 8), 10);
            T.check("anc p3b", ka.earliestAncestor(family, 3), 1);
            T.check("anc p3c", ka.earliestAncestor(family, 10), -1);
            T.check("anc p3d", ka.earliestAncestor(family, 6), 10);
            T.check("anc p3e", ka.earliestAncestor(family, 7), 10);

            KaratMiddleCourse mc = new KaratMiddleCourse();
            String[][] chain5 = {{"Java", "Spring"}, {"Basics", "Java"}, {"Spring", "Kafka"}, {"Kafka", "Microservices"}};
            T.check("mid odd", mc.middleCourse(chain5), "Spring");
            String[][] chain4 = {{"Java", "Spring"}, {"Basics", "Java"}, {"Spring", "Kafka"}};
            T.check("mid even", mc.middleCourse(chain4), "Java");
            String[][] tree = {{"Basics", "Java"}, {"Java", "Spring"}, {"Spring", "Kafka"}, {"Basics", "Python"},
                    {"Python", "Django"}, {"Python", "ML"}, {"ML", "LLMs"}, {"SQL", "Spring"}};
            T.check("mid all", mc.allMiddleCourses(tree), new TreeSet<>(Arrays.asList("Java", "Python", "Spring")));
        }
    }

    static class Examples2 {
        static void run() {
            TownJudgeBrute tj = new TownJudgeBrute();
            T.check("judgeB1", tj.findJudge(3, new int[][] {{1, 3}, {2, 3}}), 3);
            T.check("judgeB2", tj.findJudge(3, new int[][] {{1, 3}, {2, 3}, {3, 1}}), -1);
            T.check("judgeB3", tj.findJudge(4, new int[][] {{1, 2}, {3, 2}, {4, 2}, {1, 3}}), 2);

            PathExistsBrute pe = new PathExistsBrute();
            T.check("pathB1", pe.validPath(6, new int[][] {{0, 1}, {1, 2}, {3, 4}, {4, 5}}, 0, 2), true);
            T.check("pathB2", pe.validPath(6, new int[][] {{0, 1}, {1, 2}, {3, 4}, {4, 5}}, 0, 5), false);
            T.check("pathB3", pe.validPath(4, new int[][] {{2, 3}, {1, 2}, {0, 1}}, 0, 3), true);

            CourseScheduleBrute cs = new CourseScheduleBrute();
            T.check("csB1", cs.canFinish(2, new int[][] {{1, 0}}), true);
            T.check("csB2", cs.canFinish(2, new int[][] {{1, 0}, {0, 1}}), false);
            T.check("csB3", cs.canFinish(4, new int[][] {{1, 0}, {2, 1}, {3, 2}, {1, 3}}), false);
            T.check("csB4", cs.canFinish(4, new int[][] {{1, 0}, {2, 0}, {3, 1}, {3, 2}}), true);
        }
    }

    static class Examples3 {
        static List<List<Integer>> rooms(int[]... r) {
            List<List<Integer>> out = new ArrayList<>();
            for (int[] x : r) {
                List<Integer> l = new ArrayList<>();
                for (int v : x) l.add(v);
                out.add(l);
            }
            return out;
        }

        // describe a graph: each node's value and its neighbours' values (BFS order)
        static String describe(Node start, Set<Node> objects) {
            StringBuilder sb = new StringBuilder();
            Set<Node> seen = new HashSet<>();
            Queue<Node> q = new LinkedList<>();
            q.add(start);
            seen.add(start);
            while (!q.isEmpty()) {
                Node x = q.poll();
                objects.add(x);
                sb.append(x.val).append(":");
                for (Node y : x.neighbors) {
                    sb.append(y.val).append(",");
                    if (seen.add(y)) q.add(y);
                }
                sb.append(" ");
            }
            return sb.toString();
        }

        static void run() {
            int[][] star = {{4, 2}, {1, 2}, {2, 3}};
            T.check("starC", new StarCenterCount().findCenter(star), 2);
            T.check("star1", new StarCenter().findCenter(star), 2);
            T.check("star2", new StarCenter().findCenter(new int[][] {{1, 2}, {5, 1}, {1, 3}, {1, 4}}), 1);

            T.check("rooms1", new KeysAndRooms().canVisitAllRooms(rooms(new int[] {1}, new int[] {2}, new int[] {3}, new int[] {})), true);
            T.check("rooms2", new KeysAndRooms().canVisitAllRooms(rooms(new int[] {1, 3}, new int[] {3, 0, 1}, new int[] {2}, new int[] {0})), false);

            T.check("net1", new NetworkConnected().makeConnected(4, new int[][] {{0, 1}, {0, 2}, {1, 2}}), 1);
            T.check("net2", new NetworkConnected().makeConnected(6, new int[][] {{0, 1}, {0, 2}, {0, 3}, {1, 2}, {1, 3}}), 2);
            T.check("net3", new NetworkConnected().makeConnected(6, new int[][] {{0, 1}, {0, 2}, {0, 3}, {1, 2}}), -1);

            int[][] red1 = {{1, 2}, {1, 3}, {2, 3}};
            int[][] red2 = {{1, 2}, {2, 3}, {3, 4}, {1, 4}, {1, 5}};
            T.check("redB1", new RedundantBrute().findRedundantConnection(red1), new int[] {2, 3});
            T.check("redB2", new RedundantBrute().findRedundantConnection(red2), new int[] {1, 4});
            T.check("red1", new RedundantConnection().findRedundantConnection(red1), new int[] {2, 3});
            T.check("red2", new RedundantConnection().findRedundantConnection(red2), new int[] {1, 4});

            T.check("bip1", new Bipartite().isBipartite(new int[][] {{1, 3}, {0, 2}, {1, 3}, {0, 2}}), true);
            T.check("bip2", new Bipartite().isBipartite(new int[][] {{1, 2, 3}, {0, 2}, {0, 1, 3}, {0, 2}}), false);
            T.check("bip3", new Bipartite().isBipartite(new int[][] {{}, {2}, {1}}), true);

            Node[] nodes = new Node[5];
            for (int i = 1; i <= 4; i++) nodes[i] = new Node(i);
            int[][] adj = {{}, {2, 4}, {1, 3}, {2, 4}, {1, 3}};
            for (int i = 1; i <= 4; i++) for (int j : adj[i]) nodes[i].neighbors.add(nodes[j]);
            Set<Node> origObjs = new HashSet<>(), copyObjs = new HashSet<>();
            String origDesc = describe(nodes[1], origObjs);
            Node copy = new CloneGraph().cloneGraph(nodes[1]);
            String copyDesc = describe(copy, copyObjs);
            T.check("clone shape", copyDesc, origDesc);
            copyObjs.retainAll(origObjs);
            T.check("clone new objects", copyObjs.isEmpty(), true);
            T.check("clone null", new CloneGraph().cloneGraph(null) == null, true);
        }
    }

    static class Examples4 {
        static void run() {
            int[][] dag = {{0, 3}, {0, 4}, {1, 3}, {2, 4}, {2, 7}, {3, 5}, {3, 6}, {3, 7}, {4, 6}};
            String want = "[[], [], [], [0, 1], [0, 2], [0, 1, 3], [0, 1, 2, 3, 4], [0, 1, 2, 3]]";
            T.check("ancB", new AllAncestorsBrute().getAncestors(8, dag), want);
            T.check("anc1", new AllAncestors().getAncestors(8, dag), want);

            int[][] pre = {{1, 2}, {1, 0}, {2, 0}};
            int[][] qs = {{1, 0}, {1, 2}, {0, 1}};
            T.check("cs4B", new CourseScheduleIVBrute().checkIfPrerequisite(3, pre, qs), "[true, true, false]");
            T.check("cs4", new CourseScheduleIV().checkIfPrerequisite(3, pre, qs), "[true, true, false]");
            T.check("cs4b", new CourseScheduleIV().checkIfPrerequisite(2, new int[][] {}, new int[][] {{1, 0}, {0, 1}}), "[false, false]");

            T.check("safe1", new SafeStates().eventualSafeNodes(new int[][] {{1, 2}, {2, 3}, {5}, {0}, {5}, {}, {}}), "[2, 4, 5, 6]");
            T.check("safe2", new SafeStates().eventualSafeNodes(new int[][] {{1, 2, 3, 4}, {1, 2}, {3, 4}, {0, 4}, {}}), "[4]");

            String[] rec = {"bread", "sandwich", "burger"};
            List<List<String>> ing = Arrays.asList(Arrays.asList("yeast", "flour"), Arrays.asList("bread", "meat"),
                    Arrays.asList("sandwich", "meat", "bread"));
            String[] sup = {"yeast", "flour", "meat"};
            T.check("recB", new RecipesBrute().findAllRecipes(rec, ing, sup), "[bread, sandwich, burger]");
            T.check("rec1", new Recipes().findAllRecipes(rec, ing, sup), "[bread, sandwich, burger]");
            T.check("rec2", new Recipes().findAllRecipes(new String[] {"bread"}, Arrays.asList(Arrays.asList("yeast", "flour")),
                    new String[] {"yeast"}), "[]");

            int[][] richer = {{1, 0}, {2, 1}, {3, 1}, {3, 7}, {4, 3}, {5, 3}, {6, 3}};
            int[] quiet = {3, 2, 5, 4, 6, 1, 7, 0};
            T.check("lrB", new LoudRichBrute().loudAndRich(richer, quiet), new int[] {5, 5, 2, 5, 4, 5, 6, 7});
            T.check("lr1", new LoudRich().loudAndRich(richer, quiet), new int[] {5, 5, 2, 5, 4, 5, 6, 7});

            T.check("gene1", new GeneticMutation().minMutation("AACCGGTT", "AACCGGTA", new String[] {"AACCGGTA"}), 1);
            T.check("gene2", new GeneticMutation().minMutation("AACCGGTT", "AAACGGTA",
                    new String[] {"AACCGGTA", "AACCGCTA", "AAACGGTA"}), 2);
            T.check("gene3", new GeneticMutation().minMutation("AAAAACCC", "AACCCCCC",
                    new String[] {"AAAACCCC", "AAACCCCC", "AACCCCCC"}), 3);
            T.check("gene4", new GeneticMutation().minMutation("AAAA", "CCCC", new String[] {"AAAC"}), -1);

            List<String> words = Arrays.asList("cord", "card", "ward", "warm", "worm", "word", "wore", "core");
            T.check("wlS", new WordLadderScan().ladderLength("cold", "warm", words), 5);
            T.check("wl1", new WordLadder().ladderLength("cold", "warm", words), 5);
            List<String> lc = Arrays.asList("hot", "dot", "dog", "lot", "log", "cog");
            T.check("wl2", new WordLadder().ladderLength("hit", "cog", lc), 5);
            T.check("wl3", new WordLadder().ladderLength("hit", "cog", Arrays.asList("hot", "dot", "dog", "lot", "log")), 0);
            T.check("wlS3", new WordLadderScan().ladderLength("hit", "cog", Arrays.asList("hot", "dot", "dog", "lot", "log")), 0);

            int[][] times = {{2, 1, 1}, {2, 3, 1}, {3, 4, 1}};
            T.check("ndB", new NetworkDelayBellman().networkDelayTime(times, 4, 2), 2);
            T.check("nd1", new NetworkDelay().networkDelayTime(times, 4, 2), 2);
            T.check("nd2", new NetworkDelay().networkDelayTime(new int[][] {{1, 2, 1}}, 2, 2), -1);
            int[][] t3 = {{1, 2, 4}, {1, 3, 1}, {3, 2, 1}, {2, 4, 1}};
            T.check("nd3", new NetworkDelay().networkDelayTime(t3, 4, 1), 3);
            T.check("ndB3", new NetworkDelayBellman().networkDelayTime(t3, 4, 1), 3);
        }
    }

}
