package karat.grids;

import java.util.*;
import karat.T;
import karat.grids.GridsEasy.*;
import karat.grids.GridsMedium.*;

/** Runs every example from Part 3's PDFs against the code. */
public class GridsTests {
    public static void main(String[] args) {
        Examples1.run();
        Examples2.run();
        Examples3.run();
        Examples4.run();
        T.done("Grids");
    }

    static class Examples1 {
        static char[][] cg(String... rows) {
            char[][] g = new char[rows.length][];
            for (int i = 0; i < rows.length; i++) g[i] = rows[i].toCharArray();
            return g;
        }

        static String rects(List<int[]> list) {
            StringBuilder sb = new StringBuilder();
            for (int[] a : list) sb.append(Arrays.toString(a));
            return sb.toString();
        }

        static void run() {
            AllNumbersInRowsCols an = new AllNumbersInRowsCols();
            T.check("valid1", an.checkValid(new int[][] {{1, 2, 3}, {3, 1, 2}, {2, 3, 1}}), true);
            T.check("valid2", an.checkValid(new int[][] {{1, 2, 3}, {2, 3, 1}, {2, 3, 1}}), false);
            T.check("valid3", an.checkValid(new int[][] {{1, 1, 1}, {1, 2, 3}, {1, 2, 3}}), false);
            T.check("valid4", an.checkValid(new int[][] {{2, 3}, {3, 2}}), false);

            FloodFill ff = new FloodFill();
            T.check("flood1", ff.floodFill(new int[][] {{1, 1, 0}, {1, 0, 0}, {1, 1, 1}}, 0, 0, 7),
                    new int[][] {{7, 7, 0}, {7, 0, 0}, {7, 7, 7}});
            T.check("flood2", ff.floodFill(new int[][] {{5, 5}, {5, 5}}, 0, 0, 5), new int[][] {{5, 5}, {5, 5}});

            NumberOfIslands ni = new NumberOfIslands();
            T.check("islands1", ni.numIslands(cg("11000", "11000", "00100", "00011")), 3);
            T.check("islands2", ni.numIslands(cg("1110", "1101", "1100", "0000")), 2);
            T.check("islands3", ni.numIslands(cg("101", "010", "101")), 5);

            RottingOranges ro = new RottingOranges();
            T.check("rot1", ro.orangesRotting(new int[][] {{2, 1, 1}, {1, 1, 0}, {0, 1, 1}}), 4);
            T.check("rot2", ro.orangesRotting(new int[][] {{2, 1, 1}, {0, 1, 1}, {1, 0, 1}}), -1);
            T.check("rot3", ro.orangesRotting(new int[][] {{0, 2}}), 0);
            T.check("rot4", ro.orangesRotting(new int[][] {{2, 1, 0, 1, 2}}), 1);

            ShortestPathBinaryMatrix sp = new ShortestPathBinaryMatrix();
            T.check("sp1", sp.shortestPathBinaryMatrix(new int[][] {{0, 1}, {1, 0}}), 2);
            T.check("sp2", sp.shortestPathBinaryMatrix(new int[][] {{0, 0, 0}, {1, 1, 0}, {1, 1, 0}}), 4);
            T.check("sp3", sp.shortestPathBinaryMatrix(new int[][] {{1, 0, 0}, {1, 1, 0}, {1, 1, 0}}), -1);
            T.check("sp4", sp.shortestPathBinaryMatrix(new int[][] {{0}}), 1);

            WordSearch ws = new WordSearch();
            T.check("ws1", ws.exist(cg("CATS", "ODEX", "PSAW"), "CODE"), true);
            T.check("ws2", ws.exist(cg("CATS", "ODEX", "PSAW"), "CAT"), true);
            T.check("ws3", ws.exist(cg("CATS", "ODEX", "PSAW"), "COC"), false);
            T.check("ws4", ws.exist(cg("CATS", "ODEX", "PSAW"), "TEA"), true);
            T.check("ws5", ws.exist(cg("CATS", "ODEX", "PSAW"), "SEX"), false);
            T.check("ws6", ws.exist(cg("CATS", "ODEX", "PSAW"), "ADE"), true);

            Subsets ss = new Subsets();
            T.check("subsets1", ss.subsets(new int[] {1, 2, 3}),
                    Arrays.asList(Arrays.asList(), Arrays.asList(1), Arrays.asList(1, 2), Arrays.asList(1, 2, 3),
                            Arrays.asList(1, 3), Arrays.asList(2), Arrays.asList(2, 3), Arrays.asList(3)));
            T.check("subsets2", ss.subsets(new int[] {}).size(), 1);

            FindRectangles fr = new FindRectangles();
            int[][] img = {
                {1, 1, 1, 1, 1, 1},
                {1, 0, 0, 1, 1, 1},
                {1, 0, 0, 1, 0, 1},
                {1, 1, 1, 1, 0, 1},
                {0, 1, 1, 1, 1, 1},
            };
            T.check("rect1", rects(fr.findRectangles(img)), "[1, 1, 2, 2][2, 4, 3, 4][4, 0, 4, 0]");
            int[][] one = { {1, 1, 1}, {1, 0, 0}, {1, 0, 0} };
            T.check("rect2", rects(fr.findRectangles(one)), "[1, 1, 2, 2]");

            FindShapes fs = new FindShapes();
            int[][] shapes = {
                {0, 1, 1, 0},
                {0, 0, 1, 0},
                {1, 1, 1, 1},
            };
            List<List<int[]>> got = fs.findShapes(shapes);
            T.check("shapes count", got.size(), 2);
            T.check("shape0 size", got.get(0).size(), 3);
            T.check("shape1 size", got.get(1).size(), 2);
        }
    }

    static class Examples2 {
        static int[][] copy(int[][] g) {
            int[][] c = new int[g.length][];
            for (int i = 0; i < g.length; i++) c[i] = g[i].clone();
            return c;
        }

        static void run() {
            RowsColsBrute rc = new RowsColsBrute();
            T.check("rcB1", rc.checkValid(new int[][] {{1, 2, 3}, {3, 1, 2}, {2, 3, 1}}), true);
            T.check("rcB2", rc.checkValid(new int[][] {{1, 2, 3}, {2, 3, 1}, {2, 3, 1}}), false);
            T.check("rcB3", rc.checkValid(new int[][] {{2, 3}, {3, 2}}), false);

            RottingSimulate rs = new RottingSimulate();
            T.check("rotS1", rs.orangesRotting(copy(new int[][] {{2, 1, 1}, {1, 1, 0}, {0, 1, 1}})), 4);
            T.check("rotS2", rs.orangesRotting(copy(new int[][] {{2, 1, 1}, {0, 1, 1}, {1, 0, 1}})), -1);
            T.check("rotS3", rs.orangesRotting(copy(new int[][] {{0, 2}})), 0);

            ShortestPathDFS sp = new ShortestPathDFS();
            T.check("spD1", sp.shortestPathBinaryMatrix(new int[][] {{0, 1}, {1, 0}}), 2);
            T.check("spD2", sp.shortestPathBinaryMatrix(new int[][] {{0, 0, 0}, {1, 1, 0}, {1, 1, 0}}), 4);
            T.check("spD3", sp.shortestPathBinaryMatrix(new int[][] {{1, 0, 0}, {1, 1, 0}, {1, 1, 0}}), -1);
            T.check("spD4", sp.shortestPathBinaryMatrix(new int[][] {{0, 0, 0}, {0, 0, 0}, {0, 0, 0}}), 3);

            List<List<Integer>> bm = new SubsetsBitmask().subsets(new int[] {1, 2, 3});
            T.check("bm size", bm.size(), 8);
            T.check("bm has", bm.contains(Arrays.asList(1, 3)) && bm.contains(Arrays.asList()) && bm.contains(Arrays.asList(1, 2, 3)), true);

            int[][] one = { {1, 1, 1, 1}, {1, 0, 0, 1}, {1, 0, 0, 1}, {1, 1, 1, 1} };
            T.check("oneRect", new OneRectangleSimple().findRectangle(one), new int[] {1, 1, 2, 2});
            int[][] edge = { {1, 1, 1}, {1, 1, 0} };
            T.check("oneRect2", new OneRectangleSimple().findRectangle(edge), new int[] {1, 2, 1, 2});
        }
    }

    static class Examples3 {
        static int[][] copy(int[][] g) {
            int[][] c = new int[g.length][];
            for (int i = 0; i < g.length; i++) c[i] = g[i].clone();
            return c;
        }

        static char[][] cg(String... rows) {
            char[][] g = new char[rows.length][];
            for (int i = 0; i < rows.length; i++) g[i] = rows[i].toCharArray();
            return g;
        }

        static void run() {
            int[][] island = {{0, 1, 0, 0}, {1, 1, 1, 0}, {0, 1, 0, 0}, {1, 1, 0, 0}};
            T.check("perimS", new IslandPerimeterSides().islandPerimeter(island), 16);
            T.check("perim1", new IslandPerimeter().islandPerimeter(island), 16);
            T.check("perim2", new IslandPerimeter().islandPerimeter(new int[][] {{1}}), 4);
            T.check("perim3", new IslandPerimeter().islandPerimeter(new int[][] {{1, 1}, {1, 1}}), 8);

            int[][] areaGrid = {{1, 1, 0, 0, 0}, {1, 1, 0, 0, 0}, {0, 0, 0, 1, 1}, {0, 0, 0, 1, 1}, {0, 0, 0, 0, 1}};
            T.check("areaB", new MaxAreaBrute().maxAreaOfIsland(copy(areaGrid)), 5);
            T.check("area1", new MaxAreaIsland().maxAreaOfIsland(copy(areaGrid)), 5);
            T.check("area2", new MaxAreaIsland().maxAreaOfIsland(new int[][] {{0, 0}}), 0);

            int[][] enc = {{0, 0, 0, 0}, {1, 0, 1, 0}, {0, 1, 1, 0}, {0, 0, 0, 0}};
            T.check("encB", new EnclavesBrute().numEnclaves(copy(enc)), 3);
            T.check("enc1", new Enclaves().numEnclaves(copy(enc)), 3);
            int[][] enc2 = {{0, 1, 1, 0}, {0, 0, 1, 0}, {0, 0, 1, 0}, {0, 0, 0, 0}};
            T.check("encB2", new EnclavesBrute().numEnclaves(copy(enc2)), 0);
            T.check("enc2", new Enclaves().numEnclaves(copy(enc2)), 0);

            char[][] b = cg("XXXX", "XOOX", "XXOX", "XOXX");
            new SurroundedRegions().solve(b);
            T.check("sur1", b, cg("XXXX", "XXXX", "XXXX", "XOXX"));
            char[][] b2 = cg("OO", "OO");
            new SurroundedRegions().solve(b2);
            T.check("sur2", b2, cg("OO", "OO"));

            int[][] h = {{1, 2, 2, 3, 5}, {3, 2, 3, 4, 4}, {2, 4, 5, 3, 1}, {6, 7, 1, 4, 5}, {5, 1, 1, 2, 4}};
            String want = "[[0, 4], [1, 3], [1, 4], [2, 2], [3, 0], [3, 1], [4, 0]]";
            T.check("paB", new PacificAtlanticBrute().pacificAtlantic(h), want);
            T.check("pa1", new PacificAtlantic().pacificAtlantic(h), want);
            int[][] small = {{1, 2, 3}, {8, 9, 4}, {7, 6, 5}};
            T.check("pa small", new PacificAtlantic().pacificAtlantic(small).toString(),
                    new PacificAtlanticBrute().pacificAtlantic(small).toString());

            int[][] m01 = {{0, 0, 0}, {0, 1, 0}, {1, 1, 1}};
            T.check("01B", new ZeroOneMatrixBrute().updateMatrix(m01), new int[][] {{0, 0, 0}, {0, 1, 0}, {1, 2, 1}});
            T.check("01", new ZeroOneMatrix().updateMatrix(m01), new int[][] {{0, 0, 0}, {0, 1, 0}, {1, 2, 1}});
            int[][] m02 = {{1, 1, 1}, {1, 1, 1}, {1, 1, 0}};
            T.check("01b", new ZeroOneMatrix().updateMatrix(m02), new int[][] {{4, 3, 2}, {3, 2, 1}, {2, 1, 0}});

            T.check("exit1", new NearestExit().nearestExit(cg("++.+", "...+", "+++."), new int[] {1, 2}), 1);
            T.check("exit2", new NearestExit().nearestExit(cg("+++", "...", "+++"), new int[] {1, 0}), 2);
            T.check("exit3", new NearestExit().nearestExit(cg(".+"), new int[] {0, 0}), -1);
        }
    }

    static class Examples4 {
        static int[][] copy(int[][] g) {
            int[][] c = new int[g.length][];
            for (int i = 0; i < g.length; i++) c[i] = g[i].clone();
            return c;
        }

        static void run() {
            int[][] m = {{1, 2, 3, 4}, {5, 6, 7, 8}, {9, 10, 11, 12}};
            String want = "[1, 2, 3, 4, 8, 12, 11, 10, 9, 5, 6, 7]";
            T.check("spV", new SpiralVisited().spiralOrder(m), want);
            T.check("sp1", new Spiral().spiralOrder(m), want);
            T.check("sp2", new Spiral().spiralOrder(new int[][] {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}}), "[1, 2, 3, 6, 9, 8, 7, 4, 5]");
            T.check("sp3", new Spiral().spiralOrder(new int[][] {{1}, {2}, {3}}), "[1, 2, 3]");
            T.check("spV3", new SpiralVisited().spiralOrder(new int[][] {{1}, {2}, {3}}), "[1, 2, 3]");

            int[][] r1 = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
            int[][] rot = {{7, 4, 1}, {8, 5, 2}, {9, 6, 3}};
            int[][] a = copy(r1); new RotateCopy().rotate(a);
            T.check("rotC", a, rot);
            int[][] b = copy(r1); new RotateInPlace().rotate(b);
            T.check("rotI", b, rot);
            int[][] b4 = {{5, 1, 9, 11}, {2, 4, 8, 10}, {13, 3, 6, 7}, {15, 14, 12, 16}};
            new RotateInPlace().rotate(b4);
            T.check("rotI4", b4, new int[][] {{15, 13, 2, 5}, {14, 3, 4, 1}, {12, 6, 8, 9}, {16, 7, 10, 11}});

            int[][] z = {{1, 1, 1}, {1, 0, 1}, {1, 1, 1}};
            int[][] zw = {{1, 0, 1}, {0, 0, 0}, {1, 0, 1}};
            int[][] z1 = copy(z); new SetZeroesCopy().setZeroes(z1);
            T.check("zeroC", z1, zw);
            int[][] z2 = copy(z); new SetZeroes().setZeroes(z2);
            T.check("zero1", z2, zw);
            int[][] z3 = {{0, 1, 2, 0}, {3, 4, 5, 2}, {1, 3, 1, 5}};
            new SetZeroes().setZeroes(z3);
            T.check("zero2", z3, new int[][] {{0, 0, 0, 0}, {0, 4, 5, 0}, {0, 3, 1, 0}});

            int[][] life = {{0, 1, 0}, {0, 0, 1}, {1, 1, 1}, {0, 0, 0}};
            int[][] lifeNext = {{0, 0, 0}, {1, 0, 1}, {0, 1, 1}, {0, 1, 0}};
            int[][] l1 = copy(life); new GameOfLifeCopy().gameOfLife(l1);
            T.check("lifeC", l1, lifeNext);
            int[][] l2 = copy(life); new GameOfLife().gameOfLife(l2);
            T.check("life1", l2, lifeNext);
            int[][] blinker = {{0, 1, 0}, {0, 1, 0}, {0, 1, 0}};
            new GameOfLife().gameOfLife(blinker);
            T.check("life2", blinker, new int[][] {{0, 0, 0}, {1, 1, 1}, {0, 0, 0}});

            T.check("perm", new Permutations().permute(new int[] {1, 2, 3}),
                    "[[1, 2, 3], [1, 3, 2], [2, 1, 3], [2, 3, 1], [3, 1, 2], [3, 2, 1]]");
            T.check("perm1", new Permutations().permute(new int[] {7}).size(), 1);

            T.check("csB", new CombinationSumBrute().combinationSum(new int[] {2, 3, 6, 7}, 7), "[[2, 2, 3], [7]]");
            T.check("cs1", new CombinationSum().combinationSum(new int[] {2, 3, 6, 7}, 7), "[[2, 2, 3], [7]]");
            T.check("cs2", new CombinationSum().combinationSum(new int[] {5, 3, 2}, 8), "[[2, 2, 2, 2], [2, 3, 3], [3, 5]]");
            T.check("cs3", new CombinationSum().combinationSum(new int[] {2}, 1), "[]");

            T.check("lc1", new LetterCombinations().letterCombinations("23"),
                    "[ad, ae, af, bd, be, bf, cd, ce, cf]");
            T.check("lc2", new LetterCombinations().letterCombinations(""), "[]");
            T.check("lc3", new LetterCombinations().letterCombinations("7").size(), 4);
        }
    }

}
