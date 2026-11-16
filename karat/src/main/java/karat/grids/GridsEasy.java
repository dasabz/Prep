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

    /**
     * QUESTION
     * An n×n matrix is valid if every row and every column contains all the
     * numbers 1 to n. Return true or false.
     *
     * EXAMPLE
     *   1 2 3                         1 2 3
     *   3 1 2   ->  true              2 3 1   ->  false
     *   2 3 1                         2 3 1        (column 0 is 1, 2, 2)
     *
     * STEP 1 · BRUTE FORCE
     * For every row and column, and for every value 1..n, search the row or
     * column for that value.
     *
     * Brute force cost: O(n³) time · O(1) space
     */
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

    /**
     * STEP 2 · THE BETTER IDEA
     * For each i, check row i and column i in the same loop: matrix[i][j]
     * walks across row i, and matrix[j][i] walks down column i. Put the
     * values into two HashSets. The row or column is valid if every value is
     * between 1 and n and the set ends up with n different values. n
     * different values, all in 1..n, can only be exactly 1..n.
     *
     * DRY RUN (THE FALSE EXAMPLE, I = 0)
     *   row 0:     1, 2, 3   ->  set {1,2,3}  size 3  ok
     *   column 0:  1, 2, 2   ->  set {1,2}    size 2  ->  return false
     */
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

    /*
     * Time: O(n²) · Space: O(n)
     *
     * WATCH OUT
     * - The range check matters when values can be anything. [[2,3],[3,2]]
     *   has two different values in every row, but 3 > n. LeetCode promises
     *   values in range; Karat often doesn't.
     *
     * KARAT VERSION
     * Karat asks exactly this ("each row and column must contain 1..N"). A
     * follow-up may add more rules, such as checking the diagonals too. Add
     * one more set per rule inside the same loop.
     */

    // ====================================================================
    // 2. Island Perimeter
    // LeetCode 463 · Easy
    // Trick: 4 × land − 2 × shared edges (look only down & right)  ·  O(R × C)
    // ====================================================================

    /**
     * QUESTION
     * A grid has exactly one island of 1s (no lakes inside it). Return the
     * length of the island's outline.
     *
     * EXAMPLE
     *   0 1 0 0
     *   1 1 1 0      ->  16
     *   0 1 0 0
     *   1 1 0 0
     *
     * STEP 1 · BRUTE FORCE
     * Check each land cell's four sides one by one. A side counts if it faces
     * water or the edge of the grid.
     *
     * Brute force cost: O(R × C). That's already linear, so this is a
     * perfectly good answer.
     */
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

    /**
     * STEP 2 · THE BETTER IDEA
     * Here's a counting shortcut that's nice to know. Every land cell brings
     * 4 sides, and every place where two land cells touch hides 2 sides (one
     * from each cell). So perimeter = 4 × land − 2 × shared. Look only down
     * and right so each shared edge is counted once.
     *
     * DRY RUN (EXAMPLE)
     *   land cells = 7  ->  7 × 4 = 28
     *   touching pairs (down/right) = 6  ->  6 × 2 = 12
     *   28 − 12 = 16
     */
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

    /*
     * Time: O(R × C) · Space: O(1)
     *
     * NO DFS NEEDED
     * Even though it's an "island" problem, you never need to walk it.
     * Counting is enough. Spotting when DFS isn't needed is part of the
     * skill.
     */

    // ====================================================================
    // 3. Flood Fill
    // LeetCode 733 · Easy
    // Trick: DFS; painting = visited; return early if same color  ·  O(R·C)
    // ====================================================================

    /**
     * QUESTION
     * An image is a grid of colors. Starting at (sr, sc), recolor that pixel,
     * and every pixel connected to it (up/down/left/right) that has the same
     * original color, to the new color. It's the paint-bucket tool.
     *
     * EXAMPLE (START (0,0), NEW COLOR 7)
     *   before       after
     *   1 1 0        7 7 0
     *   1 0 0   ->   7 0 0
     *   1 1 1        7 7 7
     *
     * STEP 1 · BRUTE FORCE
     * There's nothing slower worth writing: you must look at every connected
     * pixel at least once, and DFS does exactly that. The only alternative is
     * BFS with a queue, which avoids deep recursion on huge images. Mention
     * it if they ask about stack overflow.
     *
     * STEP 2 · THE IDEA
     * Use DFS from the start cell. If a cell is inside the grid and still has
     * the old color, paint it, then do the same for its four neighbours.
     * Painting it also marks it as visited, because it no longer has the old
     * color.
     *
     * DRY RUN
     *   (0,0)=1 paint -> down (1,0)=1 paint -> down (2,0)=1 paint
     *                 -> right (2,1)=1 paint -> right (2,2)=1 paint
     *   (0,0) right -> (0,1)=1 paint;  (1,1)=0 -> stop;  (0,2)=0 -> stop
     */
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

    /*
     * Time: O(rows × cols) · Space: O(rows × cols) recursion in the worst
     * case
     *
     * WATCH OUT
     * - If the new color equals the old one, return immediately. Otherwise
     *   painting changes nothing, cells never look visited, and the recursion
     *   runs until StackOverflowError.
     */

}
