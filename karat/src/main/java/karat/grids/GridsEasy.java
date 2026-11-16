package karat.grids;

import java.util.*;

/**
 * Part 3 · Grid DFS/BFS & Backtracking · Easy (3 problems).
 *
 * Explanations, dry runs and pitfalls: pdfs/Karat-3a-Easy-Grid-DFS-BFS-Backtracking.pdf
 * Each problem: Step 1 = brute force, Step 2 = the better way.
 * Every example is checked in src/test/java/karat/grids/GridsTests.java
 */
public final class GridsEasy {

    private GridsEasy() { }

    // ====================================================================
    // 1. Every Row and Column Contains All Numbers  ★ Karat
    // LeetCode 2133 · Easy
    // Trick: row i and column i in one loop; two sets + range check  ·  O(n²)
    // ====================================================================

    // Step 1 · brute force
    public static class RowsColsBrute {
        public boolean checkValid(int[][] matrix) {
            int n = matrix.length;
            for (int i = 0; i < n; i++) {
                for (int v = 1; v <= n; v++) {          // every needed value
                    boolean inRow = false, inCol = false;
                    for (int j = 0; j < n; j++) {       // search for it
                        if (matrix[i][j] == v) inRow = true;
                        if (matrix[j][i] == v) inCol = true;
                    }
                    if (!inRow || !inCol) return false;
                }
            }
            return true;
        }
    }

    // Step 2 · the better way
    public static class AllNumbersInRowsCols {
        public boolean checkValid(int[][] matrix) {
            int n = matrix.length;
            for (int i = 0; i < n; i++) {
                Set<Integer> row = new HashSet<>();
                Set<Integer> col = new HashSet<>();
                for (int j = 0; j < n; j++) {
                    int a = matrix[i][j];     // row i, going across
                    int b = matrix[j][i];     // column i, going down
                    if (a < 1 || a > n || b < 1 || b > n) return false;
                    row.add(a);
                    col.add(b);
                }
                // n different values, all in 1..n  ->  exactly 1..n
                if (row.size() != n || col.size() != n) return false;
            }
            return true;
        }
    }

    // ====================================================================
    // 2. Island Perimeter
    // LeetCode 463 · Easy
    // Trick: 4 × land − 2 × shared edges (look only down & right)  ·  O(R × C)
    // ====================================================================

    // Step 1 · brute force
    public static class IslandPerimeterSides {
        public int islandPerimeter(int[][] grid) {
            int rows = grid.length, cols = grid[0].length;
            int perimeter = 0;
            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < cols; c++) {
                    if (grid[r][c] == 0) continue;
                    // up, down, left, right: a side counts if it faces
                    // water or the edge of the grid
                    if (r == 0 || grid[r - 1][c] == 0) perimeter++;
                    if (r == rows - 1 || grid[r + 1][c] == 0) perimeter++;
                    if (c == 0 || grid[r][c - 1] == 0) perimeter++;
                    if (c == cols - 1 || grid[r][c + 1] == 0) perimeter++;
                }
            }
            return perimeter;
        }
    }

    // Step 2 · the better way
    public static class IslandPerimeter {
        public int islandPerimeter(int[][] grid) {
            int rows = grid.length, cols = grid[0].length;
            int land = 0, shared = 0;
            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < cols; c++) {
                    if (grid[r][c] != 1) continue;
                    land++;
                    // look only down and right, so each pair counts once
                    if (r + 1 < rows && grid[r + 1][c] == 1) shared++;
                    if (c + 1 < cols && grid[r][c + 1] == 1) shared++;
                }
            }
            return land * 4 - shared * 2;   // a shared edge hides 2 sides
        }
    }

    // ====================================================================
    // 3. Flood Fill
    // LeetCode 733 · Easy
    // Trick: DFS; painting = visited; return early if same color  ·  O(R·C)
    // ====================================================================

    // Step 2 · the better way
    public static class FloodFill {
        public int[][] floodFill(int[][] image, int sr, int sc, int color) {
            int old = image[sr][sc];
            if (old != color) {               // same color: nothing to do
                paint(image, sr, sc, old, color);
            }
            return image;
        }

        private void paint(int[][] img, int r, int c, int old, int color) {
            if (r < 0 || c < 0 || r >= img.length || c >= img[0].length) {
                return;                       // outside the grid
            }
            if (img[r][c] != old) return;     // different color, stop
            img[r][c] = color;                // paint (also marks visited)
            paint(img, r + 1, c, old, color); // down
            paint(img, r - 1, c, old, color); // up
            paint(img, r, c + 1, old, color); // right
            paint(img, r, c - 1, old, color); // left
        }
    }

}
