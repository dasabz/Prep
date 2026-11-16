package karat.grids;

import java.util.*;

/**
 * Part 3 · Grid DFS/BFS & Backtracking · Medium (19 problems).
 *
 * Explanations, dry runs and pitfalls: pdfs/Karat-3b-Medium-Grid-DFS-BFS-Backtracking.pdf
 * Each problem: Step 1 = brute force, Step 2 = the better way.
 * Every example is checked in src/test/java/karat/grids/GridsTests.java
 */
public final class GridsMedium {

    private GridsMedium() { }

    // ====================================================================
    // 1. Spiral Matrix
    // LeetCode 54 · Medium
    // Trick: four shrinking walls: top, bottom, left, right  ·  O(R × C)
    // ====================================================================

    /**
     * QUESTION
     * Return every element of the matrix in clockwise spiral order, starting
     * at the top-left.
     *
     * EXAMPLE
     *    1  2  3  4
     *    5  6  7  8      ->  1 2 3 4 8 12 11 10 9 5 6 7
     *    9 10 11 12
     *
     * STEP 1 · BRUTE FORCE
     * Walk like a robot. Move in the current direction (right, down, left,
     * up), and when the next cell is outside the grid or already visited,
     * turn right. A seen grid remembers the visited cells.
     *
     * Brute force cost: O(R × C) time, plus O(R × C) extra space for seen.
     */
    // Step 1 · brute force
    public static class SpiralVisited {
        public List<Integer> spiralOrder(int[][] m) {
            int rows = m.length, cols = m[0].length;
            boolean[][] seen = new boolean[rows][cols];
            int[][] dirs = { {0, 1}, {1, 0}, {0, -1}, {-1, 0} };  // R D L U
            List<Integer> out = new ArrayList<>();
            int r = 0, c = 0, d = 0;
            for (int k = 0; k < rows * cols; k++) {
                out.add(m[r][c]);
                seen[r][c] = true;
                int nr = r + dirs[d][0], nc = c + dirs[d][1];
                boolean blocked = nr < 0 || nc < 0 || nr >= rows
                        || nc >= cols || seen[nr][nc];
                if (blocked) {
                    d = (d + 1) % 4;                   // turn right
                    nr = r + dirs[d][0];
                    nc = c + dirs[d][1];
                }
                r = nr;
                c = nc;
            }
            return out;
        }
    }

    /**
     * STEP 2 · THE BETTER IDEA
     * Keep four walls: top, bottom, left, right. Go along the top row and
     * then move top down; go down the right column and move right in; go back
     * along the bottom row and move bottom up; go up the left column and move
     * left in. Repeat until the walls cross. No seen grid needed.
     *
     * DRY RUN (3 × 4 EXAMPLE)
     *   top row 0, cols 0..3      -> 1 2 3 4        top = 1
     *   right col 3, rows 1..2    -> 8 12           right = 2
     *   bottom row 2, cols 2..0   -> 11 10 9        bottom = 1
     *   left col 0, rows 1..1     -> 5              left = 1
     *   top row 1, cols 1..2      -> 6 7            top = 2 > bottom -> stop
     */
    // Step 2 · the better way
    public static class Spiral {
        public List<Integer> spiralOrder(int[][] m) {
            List<Integer> out = new ArrayList<>();
            int top = 0, bottom = m.length - 1;
            int left = 0, right = m[0].length - 1;
            while (top <= bottom && left <= right) {
                // top row, left to right
                for (int c = left; c <= right; c++) out.add(m[top][c]);
                top++;
                // right column, top to bottom
                for (int r = top; r <= bottom; r++) out.add(m[r][right]);
                right--;
                if (top <= bottom) {                   // bottom row, R to L
                    for (int c = right; c >= left; c--) out.add(m[bottom][c]);
                    bottom--;
                }
                if (left <= right) {                   // left column, up
                    for (int r = bottom; r >= top; r--) out.add(m[r][left]);
                    left++;
                }
            }
            return out;
        }
    }

    /*
     * Time: O(R × C) · Space: O(1) extra
     *
     * WATCH OUT
     * - The two if checks inside the loop matter. Without them, a single
     *   remaining row or column gets printed twice. Test a 3 × 1 matrix.
     */

    // ====================================================================
    // 2. Rotate Image
    // LeetCode 48 · Medium
    // Trick: transpose, then reverse each row  ·  O(n²)
    // ====================================================================

    /**
     * QUESTION
     * Rotate an n × n matrix 90° clockwise, in place.
     *
     * EXAMPLE
     *   1 2 3        7 4 1
     *   4 5 6   ->   8 5 2
     *   7 8 9        9 6 3
     *
     * STEP 1 · BRUTE FORCE
     * Fill a new matrix: the value at (r, c) moves to (c, n − 1 − r). Then
     * copy it back.
     *
     * Brute force cost: O(n²) time but O(n²) extra space, and the problem
     * says in place.
     */
    // Step 1 · brute force
    public static class RotateCopy {
        public void rotate(int[][] m) {
            int n = m.length;
            int[][] copy = new int[n][n];
            for (int r = 0; r < n; r++) {
                for (int c = 0; c < n; c++) {
                    copy[c][n - 1 - r] = m[r][c];   // row r -> col n-1-r
                }
            }
            for (int r = 0; r < n; r++) {
                for (int c = 0; c < n; c++) {
                    m[r][c] = copy[r][c];              // write it back
                }
            }
        }
    }

    /**
     * STEP 2 · THE BETTER IDEA
     * A 90° clockwise turn is two simple flips:
     *
     * 1. Transpose: swap m[r][c] with m[c][r] (mirror across the diagonal).
     * 2. Reverse each row (mirror left to right).
     *
     * Both are just swaps, so no extra matrix is needed.
     *
     * DRY RUN
     *   start        transpose     reverse rows
     *   1 2 3        1 4 7         7 4 1
     *   4 5 6   ->   2 5 8   ->    8 5 2
     *   7 8 9        3 6 9         9 6 3
     */
    // Step 2 · the better way
    public static class RotateInPlace {
        public void rotate(int[][] m) {
            int n = m.length;
            for (int r = 0; r < n; r++) {              // 1) transpose
                for (int c = r + 1; c < n; c++) {
                    int t = m[r][c];
                    m[r][c] = m[c][r];
                    m[c][r] = t;
                }
            }
            for (int[] row : m) {                      // 2) mirror each row
                int l = 0, rt = n - 1;
                while (l < rt) {
                    int t = row[l];
                    row[l] = row[rt];
                    row[rt] = t;
                    l++;
                    rt--;
                }
            }
        }
    }

    /*
     * Time: O(n²) · Space: O(1)
     *
     * WATCH OUT
     * - In the transpose, start the inner loop at c = r + 1. Starting at 0
     *   swaps every pair twice and puts everything back where it was.
     * - For counter-clockwise: transpose, then reverse each column instead.
     */

    // ====================================================================
    // 3. Set Matrix Zeroes
    // LeetCode 73 · Medium
    // Trick: first remember zero rows/cols, then write  ·  O(R × C)
    // ====================================================================

    /**
     * QUESTION
     * If a cell is 0, set its entire row and column to 0. Do it in place.
     *
     * EXAMPLE
     *   1 1 1        1 0 1
     *   1 0 1   ->   0 0 0
     *   1 1 1        1 0 1
     *
     * STEP 1 · BRUTE FORCE
     * Keep a copy of the original. For every 0 in the copy, zero out that
     * whole row and column in the real matrix.
     *
     * Brute force cost: O(R × C × (R + C)) time and O(R × C) extra space.
     */
    // Step 1 · brute force
    public static class SetZeroesCopy {
        public void setZeroes(int[][] m) {
            int rows = m.length, cols = m[0].length;
            int[][] orig = new int[rows][];
            for (int r = 0; r < rows; r++) orig[r] = m[r].clone();
            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < cols; c++) {
                    if (orig[r][c] != 0) continue;     // look at the ORIGINAL
                    for (int k = 0; k < cols; k++) m[r][k] = 0;   // row
                    for (int k = 0; k < rows; k++) m[k][c] = 0;   // column
                }
            }
        }
    }

    /**
     * STEP 2 · THE BETTER IDEA
     * Remember first, write later. One pass marks which rows and which
     * columns contain a 0, in two boolean arrays. A second pass sets a cell
     * to 0 if its row or its column is marked.
     */
    // Step 2 · the better way
    public static class SetZeroes {
        public void setZeroes(int[][] m) {
            int rows = m.length, cols = m[0].length;
            boolean[] zeroRow = new boolean[rows];
            boolean[] zeroCol = new boolean[cols];
            for (int r = 0; r < rows; r++) {           // 1) just remember
                for (int c = 0; c < cols; c++) {
                    if (m[r][c] == 0) {
                        zeroRow[r] = true;
                        zeroCol[c] = true;
                    }
                }
            }
            for (int r = 0; r < rows; r++) {           // 2) then write
                for (int c = 0; c < cols; c++) {
                    if (zeroRow[r] || zeroCol[c]) m[r][c] = 0;
                }
            }
        }
    }

    /*
     * Time: O(R × C) · Space: O(R + C)
     *
     * WATCH OUT
     * - Never zero cells while you're still scanning. A 0 you just wrote
     *   would look like an original 0 and wipe out more rows.
     *
     * IF THEY PUSH FOR O(1) SPACE
     * Use the matrix's own first row and first column as the marker arrays,
     * plus one extra boolean to remember whether the first column itself had
     * a 0. It's the same idea with fiddlier bookkeeping. Say it; only code it
     * if asked.
     */

    // ====================================================================
    // 4. Game of Life
    // LeetCode 289 · Medium
    // Trick: in-place states: 2 = live→dead, 3 = dead→live  ·  O(R × C)
    // ====================================================================

    /**
     * QUESTION
     * Every cell is alive (1) or dead (0). All cells update at the same time,
     * based on their 8 neighbours:
     *
     * - a live cell with fewer than 2 or more than 3 live neighbours dies;
     * - a live cell with 2 or 3 live neighbours lives on;
     * - a dead cell with exactly 3 live neighbours comes alive.
     *
     * Update the board in place.
     *
     * EXAMPLE
     *   0 1 0        0 0 0
     *   0 0 1   ->   1 0 1
     *   1 1 1        0 1 1
     *   0 0 0        0 1 0
     *
     * STEP 1 · BRUTE FORCE
     * Copy the board. Count neighbours on the old copy and write the results
     * into the real board.
     *
     * Brute force cost: O(R × C) time · O(R × C) extra space
     */
    // Step 1 · brute force
    public static class GameOfLifeCopy {
        public void gameOfLife(int[][] board) {
            int rows = board.length, cols = board[0].length;
            int[][] old = new int[rows][];
            for (int r = 0; r < rows; r++) old[r] = board[r].clone();
            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < cols; c++) {
                    int live = liveAround(old, r, c);  // count on the COPY
                    if (old[r][c] == 1 && (live < 2 || live > 3)) {
                        board[r][c] = 0;               // dies
                    } else if (old[r][c] == 0 && live == 3) {
                        board[r][c] = 1;               // is born
                    }
                }
            }
        }

        private int liveAround(int[][] b, int r, int c) {
            int live = 0;
            for (int dr = -1; dr <= 1; dr++) {
                for (int dc = -1; dc <= 1; dc++) {
                    if (dr == 0 && dc == 0) continue;  // not yourself
                    int nr = r + dr, nc = c + dc;
                    boolean inside = nr >= 0 && nc >= 0
                            && nr < b.length && nc < b[0].length;
                    if (inside && b[nr][nc] == 1) live++;
                }
            }
            return live;
        }
    }

    /**
     * STEP 2 · THE BETTER IDEA
     * Avoid the copy by using two extra states that remember both the old and
     * the new value:
     *
     * - 2 = was alive, now dead
     * - 3 = was dead, now alive
     *
     * When counting neighbours, treat 1 and 2 as "was alive". After the pass,
     * turn 2 into 0 and 3 into 1.
     */
    // Step 2 · the better way
    public static class GameOfLife {
        // in place.  2 = was alive, now dead.   3 = was dead, now alive.
        public void gameOfLife(int[][] board) {
            int rows = board.length, cols = board[0].length;
            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < cols; c++) {
                    int live = wasAliveAround(board, r, c);
                    if (board[r][c] == 1 && (live < 2 || live > 3)) {
                        board[r][c] = 2;
                    } else if (board[r][c] == 0 && live == 3) {
                        board[r][c] = 3;
                    }
                }
            }
            for (int r = 0; r < rows; r++) {          // decode the new states
                for (int c = 0; c < cols; c++) {
                    if (board[r][c] == 2) board[r][c] = 0;
                    else if (board[r][c] == 3) board[r][c] = 1;
                }
            }
        }

        private int wasAliveAround(int[][] b, int r, int c) {
            int live = 0;
            for (int dr = -1; dr <= 1; dr++) {
                for (int dc = -1; dc <= 1; dc++) {
                    if (dr == 0 && dc == 0) continue;
                    int nr = r + dr, nc = c + dc;
                    boolean inside = nr >= 0 && nc >= 0
                            && nr < b.length && nc < b[0].length;
                    if (!inside) continue;
                    if (b[nr][nc] == 1 || b[nr][nc] == 2) live++;  // was live
                }
            }
            return live;
        }
    }

    /*
     * Time: O(R × C) · Space: O(1)
     *
     * WATCH OUT
     * - The update is simultaneous. If you write plain 0/1 while scanning,
     *   later cells see the new values and the result is wrong. That's the
     *   whole point of the copy or the extra states.
     *
     * KARAT ANGLE
     * "Simulate one step of a grid with neighbour rules" is a classic warm-
     * up. Write a small countNeighbours helper first; follow-up parts usually
     * change only the rules.
     */

    // ====================================================================
    // 5. Number of Islands
    // LeetCode 200 · Medium
    // Trick: on a '1': count++ then sink the whole island  ·  O(R·C)
    // ====================================================================

    /**
     * QUESTION
     * In a grid of '1' (land) and '0' (water), count the islands: groups of
     * land connected up, down, left or right.
     *
     * EXAMPLE
     *   1 1 0 0 0
     *   1 1 0 0 0     ->  3 islands
     *   0 0 1 0 0
     *   0 0 0 1 1
     *
     * STEP 1 · BRUTE FORCE
     * Scanning every cell plus one DFS per island already touches each cell a
     * constant number of times, so it is the efficient answer. Alternatives
     * (BFS, Union-Find) have the same cost.
     *
     * STEP 2 · THE IDEA
     * Scan every cell. When you hit a '1', you've found a new island: add 1
     * to count, then sink the whole island with DFS (turn its 1s into 0s) so
     * its other cells don't get counted again.
     *
     * DRY RUN
     *   (0,0) is 1  ->  count = 1, sink {(0,0),(0,1),(1,0),(1,1)}
     *   (2,2) is 1  ->  count = 2, sink {(2,2)}
     *   (3,3) is 1  ->  count = 3, sink {(3,3),(3,4)}
     *   every other cell is now 0
     */
    // Step 2 · the better way
    public static class NumberOfIslands {
        public int numIslands(char[][] grid) {
            int count = 0;
            for (int r = 0; r < grid.length; r++) {
                for (int c = 0; c < grid[0].length; c++) {
                    if (grid[r][c] == '1') {
                        count++;              // found a new island
                        sink(grid, r, c);     // erase it: never count twice
                    }
                }
            }
            return count;
        }

        private void sink(char[][] g, int r, int c) {
            if (r < 0 || c < 0 || r >= g.length || c >= g[0].length) return;
            if (g[r][c] != '1') return;       // water or already sunk
            g[r][c] = '0';
            sink(g, r + 1, c);
            sink(g, r - 1, c);
            sink(g, r, c + 1);
            sink(g, r, c - 1);
        }
    }

    /*
     * Time: O(rows × cols), since each cell is sunk at most once · Space:
     * O(rows × cols) recursion
     *
     * WATCH OUT
     * - Diagonal cells are not connected unless the problem says so.
     * - If you're not allowed to change the input, use a separate boolean[][]
     *   seen.
     *
     * KARAT LINK
     * This is the engine behind Find Rectangles Part 3 (problem 19): same
     * scan and same DFS, but you collect the cells instead of counting them.
     */

    // ====================================================================
    // 6. Max Area of Island
    // LeetCode 695 · Medium
    // Trick: sink() returns the size: 1 + four recursive calls  ·  O(R × C)
    // ====================================================================

    /**
     * QUESTION
     * Return the size of the largest island of 1s (connected
     * up/down/left/right), or 0 if there's none.
     *
     * EXAMPLE
     *   1 1 0 0 0
     *   1 1 0 0 0
     *   0 0 0 1 1      ->  5
     *   0 0 0 1 1
     *   0 0 0 0 1
     *
     * STEP 1 · BRUTE FORCE
     * From every land cell, flood its island with a fresh seen grid and
     * measure it. The same island gets re-measured once for each of its
     * cells.
     *
     * Brute force cost: O((R × C)²) time in the worst case
     */
    // Step 1 · brute force
    public static class MaxAreaBrute {
        public int maxAreaOfIsland(int[][] grid) {
            int rows = grid.length, cols = grid[0].length;
            int best = 0;
            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < cols; c++) {
                    if (grid[r][c] == 1) {
                        // fresh 'seen' EVERY time: islands get re-measured
                        boolean[][] seen = new boolean[rows][cols];
                        best = Math.max(best, area(grid, r, c, seen));
                    }
                }
            }
            return best;
        }

        private int area(int[][] g, int r, int c, boolean[][] seen) {
            if (r < 0 || c < 0 || r >= g.length || c >= g[0].length) return 0;
            if (g[r][c] == 0 || seen[r][c]) return 0;
            seen[r][c] = true;
            return 1 + area(g, r + 1, c, seen) + area(g, r - 1, c, seen)
                     + area(g, r, c + 1, seen) + area(g, r, c - 1, seen);
        }
    }

    /**
     * STEP 2 · THE BETTER IDEA
     * It's Number of Islands with one change: make sink return the size of
     * what it erased, as 1 + sink(down) + sink(up) + sink(right) +
     * sink(left). Each cell is erased once, so it's counted once.
     */
    // Step 2 · the better way
    public static class MaxAreaIsland {
        public int maxAreaOfIsland(int[][] grid) {
            int best = 0;
            for (int r = 0; r < grid.length; r++) {
                for (int c = 0; c < grid[0].length; c++) {
                    if (grid[r][c] == 1) {
                        best = Math.max(best, sink(grid, r, c));
                    }
                }
            }
            return best;
        }

        // erases the island and returns how many cells it had
        private int sink(int[][] g, int r, int c) {
            if (r < 0 || c < 0 || r >= g.length || c >= g[0].length) return 0;
            if (g[r][c] != 1) return 0;
            g[r][c] = 0;                          // erased = counted once
            return 1 + sink(g, r + 1, c) + sink(g, r - 1, c)
                     + sink(g, r, c + 1) + sink(g, r, c - 1);
        }
    }

    /*
     * Time: O(R × C) · Space: O(R × C) recursion
     */

    // ====================================================================
    // 7. Number of Enclaves
    // LeetCode 1020 · Medium
    // Trick: sink everything connected to the border; count what's left  ·  O(R × C)
    // ====================================================================

    /**
     * QUESTION
     * Count the land cells (1s) that can't walk off the grid: no path of land
     * cells connects them to the border.
     *
     * EXAMPLE
     *   0 0 0 0
     *   1 0 1 0      ->  3      (the 1 at the left edge escapes; the other three are trapped)
     *   0 1 1 0
     *   0 0 0 0
     *
     * STEP 1 · BRUTE FORCE
     * From each land cell, run a BFS and ask whether it can reach the border.
     *
     * Brute force cost: O((R × C)²) time
     */
    // Step 1 · brute force
    public static class EnclavesBrute {
        public int numEnclaves(int[][] grid) {
            int count = 0;
            for (int r = 0; r < grid.length; r++) {
                for (int c = 0; c < grid[0].length; c++) {
                    if (grid[r][c] == 1 && !canEscape(grid, r, c)) count++;
                }
            }
            return count;
        }

        // BFS from one land cell: can it walk to the border?
        private boolean canEscape(int[][] g, int sr, int sc) {
            int rows = g.length, cols = g[0].length;
            boolean[][] seen = new boolean[rows][cols];
            Queue<int[]> queue = new LinkedList<>();
            queue.add(new int[] { sr, sc });
            seen[sr][sc] = true;
            int[][] dirs = { {1, 0}, {-1, 0}, {0, 1}, {0, -1} };
            while (!queue.isEmpty()) {
                int[] cur = queue.poll();
                int r = cur[0], c = cur[1];
                if (r == 0 || c == 0 || r == rows - 1 || c == cols - 1) {
                    return true;                  // reached the border
                }
                for (int[] d : dirs) {
                    int nr = r + d[0], nc = c + d[1];
                    if (g[nr][nc] == 1 && !seen[nr][nc]) {
                        seen[nr][nc] = true;
                        queue.add(new int[] { nr, nc });
                    }
                }
            }
            return false;
        }
    }

    /**
     * STEP 2 · THE BETTER IDEA
     * Flip the question. Instead of asking "can this cell escape?" for every
     * cell, start from the border and sink every land cell you can reach from
     * it. Whatever land is left is trapped, so just count it.
     */
    // Step 2 · the better way
    public static class Enclaves {
        public int numEnclaves(int[][] grid) {
            int rows = grid.length, cols = grid[0].length;
            // 1) sink every land cell that touches or links to the border
            for (int r = 0; r < rows; r++) {
                sink(grid, r, 0);
                sink(grid, r, cols - 1);
            }
            for (int c = 0; c < cols; c++) {
                sink(grid, 0, c);
                sink(grid, rows - 1, c);
            }
            // 2) any land still left can't escape
            int count = 0;
            for (int[] row : grid) {
                for (int v : row) {
                    if (v == 1) count++;
                }
            }
            return count;
        }

        private void sink(int[][] g, int r, int c) {
            if (r < 0 || c < 0 || r >= g.length || c >= g[0].length) return;
            if (g[r][c] != 1) return;
            g[r][c] = 0;
            sink(g, r + 1, c);
            sink(g, r - 1, c);
            sink(g, r, c + 1);
            sink(g, r, c - 1);
        }
    }

    /*
     * Time: O(R × C) · Space: O(R × C) recursion
     *
     * PATTERN: WORK FROM THE BORDER INWARD
     * "Which cells can reach the edge?" is almost always solved by starting
     * at the edge. The next two problems (Surrounded Regions, Pacific
     * Atlantic) use the same trick.
     */

    // ====================================================================
    // 8. Surrounded Regions
    // LeetCode 130 · Medium
    // Trick: mark border-linked 'O' as safe; flip the rest  ·  O(R × C)
    // ====================================================================

    /**
     * QUESTION
     * In a board of 'X' and 'O', flip every region of 'O' that is completely
     * surrounded by 'X' to 'X'. An 'O' region that touches the border is
     * safe.
     *
     * EXAMPLE
     *   X X X X        X X X X
     *   X O O X   ->   X X X X
     *   X X O X        X X X X
     *   X O X X        X O X X     (bottom O touches the border: safe)
     *
     * STEP 1 · BRUTE FORCE
     * For each 'O' region, BFS it and check whether any cell touches the
     * border; flip it if none does. With a shared visited array this is O(R ×
     * C) but awkward: you have to collect each region before deciding. Step 2
     * is simpler.
     *
     * STEP 2 · THE IDEA
     * Work from the border inward, like Number of Enclaves:
     *
     * 1. DFS from every border 'O' and mark everything it reaches as safe
     *    ('S').
     * 2. Sweep the board: any 'O' still left is surrounded, so make it 'X'.
     *    Every 'S' goes back to 'O'.
     */
    // Step 2 · the better way
    public static class SurroundedRegions {
        public void solve(char[][] board) {
            int rows = board.length, cols = board[0].length;
            // 1) every 'O' linked to the border is safe: mark it 'S'
            for (int r = 0; r < rows; r++) {
                markSafe(board, r, 0);
                markSafe(board, r, cols - 1);
            }
            for (int c = 0; c < cols; c++) {
                markSafe(board, 0, c);
                markSafe(board, rows - 1, c);
            }
            // 2) leftover 'O' is surrounded -> 'X';  'S' goes back to 'O'
            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < cols; c++) {
                    if (board[r][c] == 'O') board[r][c] = 'X';
                    else if (board[r][c] == 'S') board[r][c] = 'O';
                }
            }
        }

        private void markSafe(char[][] b, int r, int c) {
            if (r < 0 || c < 0 || r >= b.length || c >= b[0].length) return;
            if (b[r][c] != 'O') return;
            b[r][c] = 'S';
            markSafe(b, r + 1, c);
            markSafe(b, r - 1, c);
            markSafe(b, r, c + 1);
            markSafe(b, r, c - 1);
        }
    }

    /*
     * Time: O(R × C) · Space: O(R × C) recursion
     *
     * WATCH OUT
     * - The temporary 'S' marker is what keeps the DFS from looping forever.
     *   It changes the cell so the != 'O' check stops it.
     */

    // ====================================================================
    // 9. Pacific Atlantic Water Flow
    // LeetCode 417 · Medium
    // Trick: climb UPHILL from each ocean; answer = cells reached by both  ·  O(R × C)
    // ====================================================================

    /**
     * QUESTION
     * Water flows from a cell to a neighbour whose height is less than or
     * equal to it. The Pacific touches the top and left edges; the Atlantic
     * touches the bottom and right edges. Return every cell from which water
     * can reach both oceans.
     *
     * EXAMPLE
     *   heights:      answer cells (row, col):
     *   1 2 3         (0,2) (1,0) (1,1) (1,2)
     *   8 9 4         (2,0) (2,1) (2,2)
     *   7 6 5
     *
     * STEP 1 · BRUTE FORCE
     * From every cell, let water run downhill with DFS and note which ocean
     * edges it touches.
     *
     * Brute force cost: O((R × C)²) time
     */
    // Step 1 · brute force
    public static class PacificAtlanticBrute {
        public List<List<Integer>> pacificAtlantic(int[][] h) {
            int rows = h.length, cols = h[0].length;
            List<List<Integer>> result = new ArrayList<>();
            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < cols; c++) {
                    boolean[] reach = new boolean[2];   // [pacific, atlantic]
                    flow(h, r, c, new boolean[rows][cols], reach);
                    if (reach[0] && reach[1]) result.add(Arrays.asList(r, c));
                }
            }
            return result;
        }

        // let water run DOWNHILL from (r, c); note which oceans it touches
        private void flow(int[][] h, int r, int c, boolean[][] seen,
                          boolean[] reach) {
            seen[r][c] = true;
            if (r == 0 || c == 0) reach[0] = true;           // pacific side
            int lastR = h.length - 1, lastC = h[0].length - 1;
            if (r == lastR || c == lastC) reach[1] = true;   // atlantic side
            int[][] dirs = { {1, 0}, {-1, 0}, {0, 1}, {0, -1} };
            for (int[] d : dirs) {
                int nr = r + d[0], nc = c + d[1];
                if (nr >= 0 && nc >= 0 && nr < h.length && nc < h[0].length
                        && !seen[nr][nc] && h[nr][nc] <= h[r][c]) {
                    flow(h, nr, nc, seen, reach);
                }
            }
        }
    }

    /**
     * STEP 2 · THE BETTER IDEA
     * Reverse the flow. Start at each ocean and walk uphill (to neighbours
     * that are greater than or equal). Every cell you reach could send water
     * back down to that ocean. Do it once from the Pacific edges and once
     * from the Atlantic edges; the answer is the cells marked in both.
     */
    // Step 2 · the better way
    public static class PacificAtlantic {
        public List<List<Integer>> pacificAtlantic(int[][] h) {
            int rows = h.length, cols = h[0].length;
            boolean[][] pac = new boolean[rows][cols];
            boolean[][] atl = new boolean[rows][cols];
            for (int r = 0; r < rows; r++) {
                climb(h, r, 0, pac);                // left edge = pacific
                climb(h, r, cols - 1, atl);         // right edge = atlantic
            }
            for (int c = 0; c < cols; c++) {
                climb(h, 0, c, pac);                // top edge = pacific
                climb(h, rows - 1, c, atl);         // bottom edge = atlantic
            }
            List<List<Integer>> result = new ArrayList<>();
            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < cols; c++) {
                    if (pac[r][c] && atl[r][c]) {
                        result.add(Arrays.asList(r, c));   // reaches both
                    }
                }
            }
            return result;
        }

        // walk UPHILL from the ocean: water could flow back down this way
        private void climb(int[][] h, int r, int c, boolean[][] reach) {
            if (reach[r][c]) return;
            reach[r][c] = true;
            int[][] dirs = { {1, 0}, {-1, 0}, {0, 1}, {0, -1} };
            for (int[] d : dirs) {
                int nr = r + d[0], nc = c + d[1];
                if (nr >= 0 && nc >= 0 && nr < h.length && nc < h[0].length
                        && h[nr][nc] >= h[r][c]) {
                    climb(h, nr, nc, reach);
                }
            }
        }
    }

    /*
     * Time: O(R × C) · Space: O(R × C)
     *
     * WATCH OUT
     * - When walking backwards the comparison flips: you move to h[next] >=
     *   h[current].
     * - Corner cells touch both oceans: (0, last column) and (last row, 0).
     */

    // ====================================================================
    // 10. Rotting Oranges
    // LeetCode 994 · Medium
    // Trick: all rotten in queue first; one BFS level = 1 minute  ·  O(R·C)
    // ====================================================================

    /**
     * QUESTION
     * 0 = empty, 1 = fresh orange, 2 = rotten orange. Every minute, each
     * rotten orange rots its fresh neighbours (up/down/left/right). How many
     * minutes until no fresh orange is left? Return −1 if some can never rot.
     *
     * EXAMPLE
     *   2 1 1
     *   1 1 0     ->  4 minutes
     *   0 1 1
     *
     * STEP 1 · BRUTE FORCE
     * Simulate minute by minute. Each minute, scan the whole grid for fresh
     * oranges next to a rotten one, rot them all at once, and repeat until
     * nothing changes.
     *
     * Brute force cost: O((R × C)²) time in the worst case, since each minute
     * rescans the whole grid. Step 2 only touches each orange once.
     */
    // Step 1 · brute force
    public static class RottingSimulate {
        public int orangesRotting(int[][] grid) {
            int rows = grid.length, cols = grid[0].length;
            int minutes = 0;
            while (true) {
                // which fresh oranges touch a rotten one RIGHT NOW?
                List<int[]> toRot = new ArrayList<>();
                for (int r = 0; r < rows; r++) {
                    for (int c = 0; c < cols; c++) {
                        if (grid[r][c] == 1 && touchesRotten(grid, r, c)) {
                            toRot.add(new int[] { r, c });
                        }
                    }
                }
                if (toRot.isEmpty()) break;            // nothing changed
                for (int[] p : toRot) grid[p[0]][p[1]] = 2;
                minutes++;
            }
            for (int[] row : grid) {
                for (int v : row) {
                    if (v == 1) return -1;             // someone never rots
                }
            }
            return minutes;
        }

        private boolean touchesRotten(int[][] g, int r, int c) {
            int[][] dirs = { {1, 0}, {-1, 0}, {0, 1}, {0, -1} };
            for (int[] d : dirs) {
                int nr = r + d[0], nc = c + d[1];
                if (nr >= 0 && nc >= 0 && nr < g.length && nc < g[0].length
                        && g[nr][nc] == 2) {
                    return true;
                }
            }
            return false;
        }
    }

    /**
     * STEP 2 · THE BETTER IDEA
     * Use multi-source BFS. Put all the rotten oranges in the queue at the
     * start, then process the queue one level at a time: one level is one
     * minute. Count the fresh oranges at the start and subtract 1 each time
     * one rots. If any fresh ones are left at the end, return −1.
     *
     * DRY RUN (ONE GRID PER MINUTE)
     *   min 0    min 1    min 2    min 3    min 4
     *   2 1 1    2 2 1    2 2 2    2 2 2    2 2 2
     *   1 1 0    2 1 0    2 2 0    2 2 0    2 2 0
     *   0 1 1    0 1 1    0 1 1    0 2 1    0 2 2
     */
    // Step 2 · the better way
    public static class RottingOranges {
        public int orangesRotting(int[][] grid) {
            int rows = grid.length, cols = grid[0].length;
            Queue<int[]> queue = new LinkedList<>();
            int fresh = 0;
            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < cols; c++) {
                    if (grid[r][c] == 2) queue.add(new int[] { r, c });
                    if (grid[r][c] == 1) fresh++;
                }
            }
            int[][] dirs = { {1, 0}, {-1, 0}, {0, 1}, {0, -1} };
            int minutes = 0;
            while (!queue.isEmpty() && fresh > 0) {
                int size = queue.size();          // everything rotten NOW
                for (int i = 0; i < size; i++) {
                    int[] cell = queue.poll();
                    for (int[] d : dirs) {
                        int nr = cell[0] + d[0], nc = cell[1] + d[1];
                        if (nr >= 0 && nc >= 0 && nr < rows && nc < cols
                                && grid[nr][nc] == 1) {
                            grid[nr][nc] = 2;     // rot the neighbour
                            fresh--;
                            queue.add(new int[] { nr, nc });
                        }
                    }
                }
                minutes++;                        // one full level = 1 min
            }
            return fresh == 0 ? minutes : -1;
        }
    }

    /*
     * Time: O(rows × cols) · Space: O(rows × cols)
     *
     * WATCH OUT
     * - Read int size = queue.size() before the inner loop. That freezes the
     *   oranges for this minute, so ones added during the loop wait until the
     *   next minute.
     * - The loop condition fresh > 0 stops you counting one extra empty
     *   minute at the end.
     * - If there are no fresh oranges at the start, the answer is 0.
     */

    // ====================================================================
    // 11. 01 Matrix
    // LeetCode 542 · Medium
    // Trick: multi-source BFS from ALL zeros at once  ·  O(R × C)
    // ====================================================================

    /**
     * QUESTION
     * For every cell, return the distance (up/down/left/right steps) to the
     * nearest 0.
     *
     * EXAMPLE
     *   0 0 0        0 0 0
     *   0 1 0   ->   0 1 0
     *   1 1 1        1 2 1
     *
     * STEP 1 · BRUTE FORCE
     * Run a separate BFS from every cell until it finds a 0.
     *
     * Brute force cost: O((R × C)²) time
     */
    // Step 1 · brute force
    public static class ZeroOneMatrixBrute {
        public int[][] updateMatrix(int[][] mat) {
            int rows = mat.length, cols = mat[0].length;
            int[][] dist = new int[rows][cols];
            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < cols; c++) {
                    if (mat[r][c] == 1) dist[r][c] = nearestZero(mat, r, c);
                }
            }
            return dist;
        }

        // a separate BFS from EVERY cell
        private int nearestZero(int[][] m, int sr, int sc) {
            boolean[][] seen = new boolean[m.length][m[0].length];
            Queue<int[]> queue = new LinkedList<>();
            queue.add(new int[] { sr, sc, 0 });
            seen[sr][sc] = true;
            int[][] dirs = { {1, 0}, {-1, 0}, {0, 1}, {0, -1} };
            while (!queue.isEmpty()) {
                int[] cur = queue.poll();
                if (m[cur[0]][cur[1]] == 0) return cur[2];
                for (int[] d : dirs) {
                    int nr = cur[0] + d[0], nc = cur[1] + d[1];
                    boolean inside = nr >= 0 && nc >= 0
                            && nr < m.length && nc < m[0].length;
                    if (inside && !seen[nr][nc]) {
                        seen[nr][nc] = true;
                        queue.add(new int[] { nr, nc, cur[2] + 1 });
                    }
                }
            }
            return -1;
        }
    }

    /**
     * STEP 2 · THE BETTER IDEA
     * Turn it around, as with Rotting Oranges: put every 0 into the queue at
     * the start with distance 0. BFS then spreads outward one layer at a
     * time, and the first time it reaches a cell is that cell's shortest
     * distance. Mark unknown cells as −1 so each is set only once.
     *
     * DRY RUN (EXAMPLE)
     *   start queue: all 5 zeros
     *   layer 1: (1,1), (2,0), (2,2) get 1
     *   layer 2: (2,1) gets 2  (its neighbours (1,1)/(2,0)/(2,2) are 1)
     */
    // Step 2 · the better way
    public static class ZeroOneMatrix {
        public int[][] updateMatrix(int[][] mat) {
            int rows = mat.length, cols = mat[0].length;
            int[][] dist = new int[rows][cols];
            Queue<int[]> queue = new LinkedList<>();
            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < cols; c++) {
                    if (mat[r][c] == 0) {
                        queue.add(new int[] { r, c });   // ALL zeros start
                    } else {
                        dist[r][c] = -1;                 // -1 = not known yet
                    }
                }
            }
            int[][] dirs = { {1, 0}, {-1, 0}, {0, 1}, {0, -1} };
            while (!queue.isEmpty()) {
                int[] cur = queue.poll();
                for (int[] d : dirs) {
                    int nr = cur[0] + d[0], nc = cur[1] + d[1];
                    if (nr >= 0 && nc >= 0 && nr < rows && nc < cols
                            && dist[nr][nc] == -1) {
                        dist[nr][nc] = dist[cur[0]][cur[1]] + 1;
                        queue.add(new int[] { nr, nc });
                    }
                }
            }
            return dist;
        }
    }

    /*
     * Time: O(R × C) · Space: O(R × C)
     *
     * PATTERN
     * "Distance from the nearest X" means multi-source BFS: start from all
     * the X's together.
     */

    // ====================================================================
    // 12. Shortest Path in Binary Matrix
    // LeetCode 1091 · Medium
    // Trick: BFS; store length in queue; mark when adding  ·  O(n²)
    // ====================================================================

    /**
     * QUESTION
     * In an n×n grid, 0 is open and 1 is blocked. Find the shortest path from
     * the top-left to the bottom-right, moving in 8 directions (diagonals
     * included). The path length is the number of cells on it. Return −1 if
     * there's no path.
     *
     * EXAMPLE
     *   0 0 0
     *   1 1 0     ->  4      path: (0,0) (0,1) (1,2) (2,2)
     *   1 1 0
     *
     * STEP 1 · BRUTE FORCE
     * Use DFS to try every possible path, backtracking as you go, and
     * remember the shortest one that reaches the target.
     *
     * Brute force cost: exponential time. Fine on a 3×3 grid and hopeless on
     * 50×50. This is exactly why "shortest" means BFS.
     */
    // Step 1 · brute force
    public static class ShortestPathDFS {
        private int best;

        public int shortestPathBinaryMatrix(int[][] grid) {
            int n = grid.length;
            if (grid[0][0] == 1 || grid[n - 1][n - 1] == 1) return -1;
            best = Integer.MAX_VALUE;
            boolean[][] onPath = new boolean[n][n];
            dfs(grid, 0, 0, 1, onPath);
            return best == Integer.MAX_VALUE ? -1 : best;
        }

        // try EVERY path, remember the shortest one that reaches the end
        private void dfs(int[][] g, int r, int c, int len, boolean[][] on) {
            int n = g.length;
            if (r == n - 1 && c == n - 1) {
                best = Math.min(best, len);
                return;
            }
            on[r][c] = true;
            for (int dr = -1; dr <= 1; dr++) {
                for (int dc = -1; dc <= 1; dc++) {
                    int nr = r + dr, nc = c + dc;
                    if (nr >= 0 && nc >= 0 && nr < n && nc < n
                            && g[nr][nc] == 0 && !on[nr][nc]) {
                        dfs(g, nr, nc, len + 1, on);
                    }
                }
            }
            on[r][c] = false;                          // backtrack
        }
    }

    /**
     * STEP 2 · THE BETTER IDEA
     * "Shortest" on a grid with no weights means BFS. BFS explores in rings:
     * every cell 1 step away, then every cell 2 steps away, and so on. The
     * first time you reach the target is the shortest way there. Store the
     * path length with each cell in the queue.
     *
     * DRY RUN
     *   pop (0,0,len 1) -> open neighbours: (0,1)      -> add with len 2
     *   pop (0,1,len 2) -> (0,2), (1,2)                -> add with len 3
     *   pop (0,2,len 3) -> (1,2) already marked        -> nothing new
     *   pop (1,2,len 3) -> (2,2)                       -> add with len 4
     *   pop (2,2,len 4) -> it's the target             -> return 4
     */
    // Step 2 · the better way
    public static class ShortestPathBinaryMatrix {
        public int shortestPathBinaryMatrix(int[][] grid) {
            int n = grid.length;
            if (grid[0][0] == 1 || grid[n - 1][n - 1] == 1) return -1;

            Queue<int[]> queue = new LinkedList<>();
            queue.add(new int[] { 0, 0, 1 });     // row, col, path length
            grid[0][0] = 1;                       // mark visited
            while (!queue.isEmpty()) {
                int[] cur = queue.poll();
                int r = cur[0], c = cur[1], len = cur[2];
                if (r == n - 1 && c == n - 1) return len;
                // all 8 neighbours: dr and dc each in {-1, 0, 1}
                for (int dr = -1; dr <= 1; dr++) {
                    for (int dc = -1; dc <= 1; dc++) {
                        int nr = r + dr, nc = c + dc;
                        if (nr >= 0 && nc >= 0 && nr < n && nc < n
                                && grid[nr][nc] == 0) {
                            grid[nr][nc] = 1;     // mark when ADDED
                            queue.add(new int[] { nr, nc, len + 1 });
                        }
                    }
                }
            }
            return -1;
        }
    }

    /*
     * Time: O(n²) · Space: O(n²)
     *
     * WATCH OUT
     * - Mark a cell visited when you add it to the queue, not when you pop
     *   it. Otherwise the same cell gets added many times.
     * - Check the start and end cells first. If either is 1, return −1.
     * - For 8 directions, two loops over −1..1 save you typing an 8-row
     *   array.
     */

    // ====================================================================
    // 13. Nearest Exit from Entrance in Maze
    // LeetCode 1926 · Medium
    // Trick: BFS from the entrance; first border cell (≠ entrance) wins  ·  O(R × C)
    // ====================================================================

    /**
     * QUESTION
     * In a maze of '.' (open) and '+' (wall), start at entrance. An exit is
     * any open cell on the border, other than the entrance itself. Return the
     * fewest steps to an exit, or −1.
     *
     * EXAMPLE
     *   + + . +
     *   . . . +       entrance (1,2)  ->  1     (step up to (0,2), a border cell)
     *   + + + .
     *
     * STEP 1 · BRUTE FORCE
     * DFS over every path, keeping the shortest. That's exponential. "Fewest
     * steps" in a grid always means BFS, so go straight to Step 2.
     *
     * STEP 2 · THE IDEA
     * Run a BFS from the entrance, storing the step count with each cell. The
     * first border cell you discover is the nearest exit. Mark the entrance
     * as visited first, so it's never counted as an exit.
     */
    // Step 2 · the better way
    public static class NearestExit {
        public int nearestExit(char[][] maze, int[] entrance) {
            int rows = maze.length, cols = maze[0].length;
            Queue<int[]> queue = new LinkedList<>();
            // queue holds {row, col, steps}
            queue.add(new int[] { entrance[0], entrance[1], 0 });
            maze[entrance[0]][entrance[1]] = '+';     // mark visited
            int[][] dirs = { {1, 0}, {-1, 0}, {0, 1}, {0, -1} };
            while (!queue.isEmpty()) {
                int[] cur = queue.poll();
                for (int[] d : dirs) {
                    int nr = cur[0] + d[0], nc = cur[1] + d[1];
                    if (nr < 0 || nc < 0 || nr >= rows || nc >= cols) {
                        continue;                      // outside
                    }
                    if (maze[nr][nc] != '.') continue; // wall or visited
                    boolean border = nr == 0 || nc == 0
                            || nr == rows - 1 || nc == cols - 1;
                    if (border) return cur[2] + 1;     // first exit = nearest
                    maze[nr][nc] = '+';
                    queue.add(new int[] { nr, nc, cur[2] + 1 });
                }
            }
            return -1;
        }
    }

    /*
     * Time: O(R × C) · Space: O(R × C)
     *
     * WATCH OUT
     * - Check for an exit when you discover a neighbour (step + 1), not when
     *   you pop it. That saves a level and keeps the entrance from counting.
     */

    // ====================================================================
    // 14. Word Search  ★ Karat
    // LeetCode 79 · Medium
    // Trick: mark '#', try 4 ways, restore the letter  ·  O(R·C·4^L)
    // ====================================================================

    /**
     * QUESTION
     * Does the word appear in the grid as a chain of neighbouring cells
     * (up/down/left/right)? A cell can't be used twice in the same word.
     *
     * EXAMPLE
     *   C A T S
     *   O D E X       "CODE" -> true     C(0,0) O(1,0) D(1,1) E(1,2)
     *   P S A W       "COC"  -> false    (would have to reuse the C)
     *
     * STEP 1 · BRUTE FORCE
     * Backtracking is the brute force here: you genuinely have to try the
     * paths. The only speed-up is pruning. For example, before searching,
     * check that the board has enough of each letter the word needs, and
     * return false early if it doesn't.
     *
     * STEP 2 · THE IDEA
     * Backtracking. Try every cell as a starting point. When a cell matches
     * word[i], mark it as used ('#'), try all four neighbours for word[i+1],
     * then put the letter back before returning so other paths can still use
     * the cell.
     *
     * DRY RUN ("CODE")
     *   start (0,0) 'C' matches i=0 -> mark '#'
     *     down (1,0) 'O' matches i=1 -> mark
     *       down (2,0) 'P'  no
     *       up   (0,0) '#'  no (used)
     *       right (1,1) 'D' matches i=2 -> mark
     *         right (1,2) 'E' matches i=3 -> mark
     *           next call: i = 4 = word length  ->  true
     */
    // Step 2 · the better way
    public static class WordSearch {
        public boolean exist(char[][] board, String word) {
            for (int r = 0; r < board.length; r++) {
                for (int c = 0; c < board[0].length; c++) {
                    if (match(board, word, r, c, 0)) return true;
                }
            }
            return false;
        }

        // Can word[i..] be matched starting at cell (r, c)?
        private boolean match(char[][] b, String word, int r, int c, int i) {
            if (i == word.length()) return true;          // matched it all
            if (r < 0 || c < 0 || r >= b.length || c >= b[0].length) {
                return false;
            }
            if (b[r][c] != word.charAt(i)) return false;

            char saved = b[r][c];
            b[r][c] = '#';                                // mark as used
            boolean found = match(b, word, r + 1, c, i + 1)
                         || match(b, word, r - 1, c, i + 1)
                         || match(b, word, r, c + 1, i + 1)
                         || match(b, word, r, c - 1, i + 1);
            b[r][c] = saved;                              // undo: backtrack
            return found;
        }
    }

    /*
     * Time: O(rows × cols × 4^L), L = word length · Space: O(L) recursion
     *
     * WATCH OUT
     * - Restore the cell (b[r][c] = saved) on every path, including when
     *   nothing was found.
     * - Check i == word.length() before the bounds check. The last letter
     *   might be on the edge, and the next call steps outside the grid.
     *
     * KARAT VERSION
     * Karat usually wants the path back as a list of coordinates. Keep a List
     * path: add (r, c) when you mark a cell and remove the last entry when
     * you un-mark it. When i == word.length(), copy the path. Another common
     * part: "Which words from this list are in the grid?" Call exist once for
     * each word.
     */

    // ====================================================================
    // 15. Subsets
    // LeetCode 78 · Medium
    // Trick: record every call; loop from start; add a copy  ·  O(n·2ⁿ)
    // ====================================================================

    /**
     * QUESTION
     * Return every subset of a list of different numbers, including the empty
     * one.
     *
     * EXAMPLE
     *   [1, 2, 3]  ->  [], [1], [1,2], [1,2,3], [1,3], [2], [2,3], [3]
     *
     * STEP 1 · BRUTE FORCE
     * A direct, non-recursive way: every subset is a yes/no choice per
     * number, which is exactly the bits of a number from 0 to 2ⁿ − 1. Bit i
     * set means you take nums[i].
     *
     * Brute force cost: O(n × 2ⁿ), the same as Step 2, because every method
     * has to produce all 2ⁿ subsets. Learn backtracking anyway: it extends to
     * permutations and combinations, and bitmasks don't.
     */
    // Step 1 · brute force
    public static class SubsetsBitmask {
        public List<List<Integer>> subsets(int[] nums) {
            int n = nums.length;
            List<List<Integer>> result = new ArrayList<>();
            // each number 0 .. 2^n - 1 is one yes/no pattern
            for (int mask = 0; mask < (1 << n); mask++) {
                List<Integer> subset = new ArrayList<>();
                for (int i = 0; i < n; i++) {
                    if ((mask & (1 << i)) != 0) {      // bit i on = take it
                        subset.add(nums[i]);
                    }
                }
                result.add(subset);
            }
            return result;
        }
    }

    /**
     * STEP 2 · THE BETTER IDEA
     * The backtracking template is choose → explore → un-choose. At every
     * call, the current list is a valid subset, so record it. Then try adding
     * each remaining number, only from start onwards. That stops you going
     * backwards and creating duplicates like [2, 1].
     *
     * DRY RUN (THE RECURSION TREE)
     *   []
     *   ├── [1]
     *   │   ├── [1,2]
     *   │   │   └── [1,2,3]
     *   │   └── [1,3]
     *   ├── [2]
     *   │   └── [2,3]
     *   └── [3]
     */
    // Step 2 · the better way
    public static class Subsets {
        public List<List<Integer>> subsets(int[] nums) {
            List<List<Integer>> result = new ArrayList<>();
            build(nums, 0, new ArrayList<>(), result);
            return result;
        }

        private void build(int[] nums, int start, List<Integer> current,
                           List<List<Integer>> result) {
            result.add(new ArrayList<>(current));     // every step = a subset
            for (int i = start; i < nums.length; i++) {
                current.add(nums[i]);                 // 1) choose
                build(nums, i + 1, current, result);  // 2) explore
                current.remove(current.size() - 1);   // 3) un-choose
            }
        }
    }

    /*
     * Time: O(n × 2ⁿ) · Space: O(n) recursion, plus the output
     *
     * WATCH OUT
     * - Write result.add(new ArrayList<>(current)), which adds a copy. If you
     *   add current itself, every entry points to the same list, and that
     *   list is empty by the end.
     *
     * ONE TEMPLATE, MANY PROBLEMS
     * - Permutations (46): use a boolean[] used array instead of start.
     * - Combination Sum (39): a number may repeat, so recurse with i instead
     *   of i + 1.
     * - Letter Combinations (17): at depth d, loop over the letters of digit
     *   d.
     */

    // ====================================================================
    // 16. Permutations
    // LeetCode 46 · Medium
    // Trick: backtracking with a used[] array  ·  O(n × n!)
    // ====================================================================

    /**
     * QUESTION
     * Return every ordering of a list of distinct numbers.
     *
     * EXAMPLE
     *   [1, 2, 3]  ->  [1,2,3] [1,3,2] [2,1,3] [2,3,1] [3,1,2] [3,2,1]
     *
     * STEP 1 · BRUTE FORCE
     * There are n! answers, so every method takes at least O(n × n!). No
     * shortcut exists. The real choice is a clean way to generate them, which
     * is the backtracking template.
     *
     * STEP 2 · THE IDEA
     * Use the backtracking template from Subsets, with one change: any unused
     * number can go next, not just the ones after start. Keep a boolean[]
     * used, set it when you choose a number, and clear it when you un-choose.
     *
     * DRY RUN (TREE FOR [1, 2, 3], FIRST BRANCH)
     *   [] -> [1] -> [1,2] -> [1,2,3] save
     *             -> [1,3] -> [1,3,2] save
     *      -> [2] -> ...
     */
    // Step 2 · the better way
    public static class Permutations {
        public List<List<Integer>> permute(int[] nums) {
            List<List<Integer>> result = new ArrayList<>();
            build(nums, new boolean[nums.length], new ArrayList<>(), result);
            return result;
        }

        private void build(int[] nums, boolean[] used, List<Integer> cur,
                           List<List<Integer>> result) {
            if (cur.size() == nums.length) {           // a full arrangement
                result.add(new ArrayList<>(cur));
                return;
            }
            for (int i = 0; i < nums.length; i++) {
                if (used[i]) continue;
                used[i] = true;                        // 1) choose
                cur.add(nums[i]);
                build(nums, used, cur, result);        // 2) explore
                cur.remove(cur.size() - 1);            // 3) un-choose
                used[i] = false;
            }
        }
    }

    /*
     * Time: O(n × n!) · Space: O(n) recursion, plus the output
     *
     * WATCH OUT
     * - Undo both changes on the way back: remove from cur and set used[i] =
     *   false.
     */

    // ====================================================================
    // 17. Combination Sum
    // LeetCode 39 · Medium
    // Trick: backtrack from start, reuse allowed (recurse with i); sort + break  ·  exponential
    // ====================================================================

    /**
     * QUESTION
     * Given distinct positive numbers, return every combination that adds up
     * to target. A number may be used more than once. [2,2,3] and [3,2,2]
     * count as the same combination.
     *
     * EXAMPLE
     *   [2, 3, 6, 7], target 7   ->  [2,2,3], [7]
     *   [2, 3, 5],    target 8   ->  [2,2,2,2], [2,3,3], [3,5]
     *
     * STEP 1 · BRUTE FORCE
     * Backtrack: try each candidate from start onward, recurse with i (not i
     * + 1) so it can be reused, and stop a branch once the remaining amount
     * goes below 0.
     *
     * Brute force cost: exponential, and it keeps exploring candidates that
     * are already too big.
     */
    // Step 1 · brute force
    public static class CombinationSumBrute {
        public List<List<Integer>> combinationSum(int[] cand, int target) {
            List<List<Integer>> result = new ArrayList<>();
            build(cand, 0, target, new ArrayList<>(), result);
            return result;
        }

        private void build(int[] cand, int start, int left, List<Integer> cur,
                           List<List<Integer>> result) {
            if (left == 0) {
                result.add(new ArrayList<>(cur));
                return;
            }
            if (left < 0) return;                      // overshot: give up
            for (int i = start; i < cand.length; i++) {
                cur.add(cand[i]);
                build(cand, i, left - cand[i], cur, result);  // i: reuse ok
                cur.remove(cur.size() - 1);
            }
        }
    }

    /**
     * STEP 2 · THE BETTER IDEA
     * Sort first, then prune. Once the candidates are in increasing order,
     * the moment c[i] is bigger than what's left, every later candidate is
     * bigger too, so you can break out of the whole loop instead of trying
     * each one.
     */
    // Step 2 · the better way
    public static class CombinationSum {
        public List<List<Integer>> combinationSum(int[] cand, int target) {
            int[] c = cand.clone();
            Arrays.sort(c);                            // small numbers first
            List<List<Integer>> result = new ArrayList<>();
            build(c, 0, target, new ArrayList<>(), result);
            return result;
        }

        private void build(int[] c, int start, int left, List<Integer> cur,
                           List<List<Integer>> result) {
            if (left == 0) {
                result.add(new ArrayList<>(cur));
                return;
            }
            for (int i = start; i < c.length; i++) {
                if (c[i] > left) break;          // the rest are bigger too
                cur.add(c[i]);
                build(c, i, left - c[i], cur, result);
                cur.remove(cur.size() - 1);
            }
        }
    }

    /*
     * Time: exponential in the worst case, but pruning cuts it hugely ·
     * Space: O(target / smallest) recursion
     *
     * WATCH OUT
     * - Recurse with i to allow reuse. Use i + 1 for the "each number once"
     *   variant (Combination Sum II, LeetCode 40).
     * - Starting the loop at start (not 0) is what prevents [2,3,2]-style
     *   duplicates.
     */

    // ====================================================================
    // 18. Letter Combinations of a Phone Number
    // LeetCode 17 · Medium
    // Trick: depth = digit index; loop over that digit's letters  ·  O(4ⁿ × n)
    // ====================================================================

    /**
     * QUESTION
     * Each digit 2–9 maps to letters as on a phone keypad. Return every
     * letter string the digits could spell.
     *
     * EXAMPLE
     *   "23"  ->  ad ae af bd be bf cd ce cf
     *
     * STEP 1 · BRUTE FORCE
     * The output itself has up to 4ⁿ strings, so every method is at least
     * that big. Backtracking is the clean way to produce them; an iterative
     * "grow a list one digit at a time" version is the same cost.
     *
     * STEP 2 · THE IDEA
     * Recursion depth i = which digit you're filling. At depth i, loop over
     * that digit's letters: append one, recurse to i + 1, then remove it.
     * When i equals the number of digits, save the string.
     */
    // Step 2 · the better way
    public static class LetterCombinations {
        private static final String[] KEYS = {
            "", "", "abc", "def", "ghi", "jkl", "mno", "pqrs", "tuv", "wxyz"
        };

        public List<String> letterCombinations(String digits) {
            List<String> result = new ArrayList<>();
            if (digits.isEmpty()) return result;
            build(digits, 0, new StringBuilder(), result);
            return result;
        }

        private void build(String digits, int i, StringBuilder cur,
                           List<String> result) {
            if (i == digits.length()) {               // one letter per digit
                result.add(cur.toString());
                return;
            }
            for (char ch : KEYS[digits.charAt(i) - '0'].toCharArray()) {
                cur.append(ch);                        // choose
                build(digits, i + 1, cur, result);     // explore
                cur.deleteCharAt(cur.length() - 1);    // un-choose
            }
        }
    }

    /*
     * Time: O(4ⁿ × n) · Space: O(n) recursion, plus the output
     *
     * WATCH OUT
     * - Empty input returns an empty list, not [""].
     */

    // ====================================================================
    // 19. Find Rectangles (3 parts)  ★ Karat
    // A Karat question asked in parts · Part 3 is Number of Islands · Medium
    // Trick: first unused 0 = top-left; walk right & down; mark  ·  O(R·C)
    // ====================================================================

    /**
     * QUESTION
     * An image is a grid of 1 (white) and 0 (black).
     *
     * - Part 1: there is exactly one rectangle of 0s. Return its top-left and
     *   bottom-right corners.
     * - Part 2: there can be several rectangles of 0s, and they never touch.
     *   Return all of them.
     * - Part 3: the black shapes can be any shape, not just rectangles.
     *   Return the cells of each shape.
     *
     * EXAMPLE
     *        c0 c1 c2 c3 c4 c5
     *   r0    1  1  1  1  1  1
     *   r1    1  0  0  1  1  1
     *   r2    1  0  0  1  0  1
     *   r3    1  1  1  1  0  1
     *   r4    0  1  1  1  1  1
     *
     *   Part 2  ->  [1,1]-[2,2]   [2,4]-[3,4]   [4,0]-[4,0]
     *
     * STEP 1 · BRUTE FORCE
     * For Part 1 only (exactly one rectangle), there's a lovely shortcut: the
     * first 0 you meet in reading order is the top-left corner, and the last
     * 0 is the bottom-right corner.
     *
     * Brute force cost: O(R × C) time · O(1) space. It breaks as soon as
     * there are two rectangles, which is exactly what Part 2 adds. Say it,
     * then move on.
     */
    // Step 1 · brute force
    public static class OneRectangleSimple {
        // Part 1 only: exactly ONE rectangle of 0s
        public int[] findRectangle(int[][] grid) {
            int[] first = null, last = null;
            for (int r = 0; r < grid.length; r++) {
                for (int c = 0; c < grid[0].length; c++) {
                    if (grid[r][c] == 0) {
                        if (first == null) first = new int[] { r, c };
                        last = new int[] { r, c };    // keeps moving
                    }
                }
            }
            // first 0 = top-left, last 0 = bottom-right
            return new int[] { first[0], first[1], last[0], last[1] };
        }
    }

    /**
     * STEP 2 · THE BETTER IDEA (PARTS 1 AND 2)
     * Scan row by row, left to right. The first 0 you meet that isn't part of
     * a rectangle you've already found must be a top-left corner. From there,
     * walk right while the cells are 0 to find the right edge, and walk down
     * while the cells are 0 to find the bottom edge. Mark every cell of that
     * rectangle as used so you don't start again from inside it.
     *
     * Part 1 is the same code: just return the first rectangle.
     *
     * DRY RUN
     *   (1,1) first unused 0 -> right: (1,2)=0, (1,3)=1 stop -> right = 2
     *                           down:  (2,1)=0, (3,1)=1 stop -> bottom = 2
     *                           ->  [1,1,2,2], mark 4 cells
     *   (2,4) next unused 0  -> right: (2,5)=1 -> right = 4
     *                           down:  (3,4)=0, (4,4)=1 -> bottom = 3
     *                           ->  [2,4,3,4]
     *   (4,0) next unused 0  -> a 1x1 rectangle  ->  [4,0,4,0]
     *
     * PARTS 1 AND 2
     */
    // Step 2 · the better way
    public static class FindRectangles {
        // Each rectangle of 0s -> {topRow, leftCol, bottomRow, rightCol}
        public List<int[]> findRectangles(int[][] grid) {
            int rows = grid.length, cols = grid[0].length;
            boolean[][] used = new boolean[rows][cols];
            List<int[]> result = new ArrayList<>();

            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < cols; c++) {
                    if (grid[r][c] == 0 && !used[r][c]) {
                        // first unseen 0 in reading order = top-left corner
                        int right = c;
                        while (right + 1 < cols && grid[r][right + 1] == 0) {
                            right++;                       // walk right
                        }
                        int bottom = r;
                        while (bottom + 1 < rows
                                && grid[bottom + 1][c] == 0) {
                            bottom++;                      // walk down
                        }
                        for (int i = r; i <= bottom; i++) {
                            for (int j = c; j <= right; j++) {
                                used[i][j] = true;         // don't recount
                            }
                        }
                        result.add(new int[] { r, c, bottom, right });
                    }
                }
            }
            return result;
        }
    }

    /**
     * PART 3 (ANY SHAPE = NUMBER OF ISLANDS THAT COLLECTS CELLS)
     */
    // Step 2 · the better way
    public static class FindShapes {
        // Part 3: shapes of 0s can be any form. Return each shape's cells.
        public List<List<int[]>> findShapes(int[][] grid) {
            List<List<int[]>> shapes = new ArrayList<>();
            boolean[][] seen = new boolean[grid.length][grid[0].length];
            for (int r = 0; r < grid.length; r++) {
                for (int c = 0; c < grid[0].length; c++) {
                    if (grid[r][c] == 0 && !seen[r][c]) {
                        List<int[]> cells = new ArrayList<>();
                        collect(grid, r, c, seen, cells);   // like islands
                        shapes.add(cells);
                    }
                }
            }
            return shapes;
        }

        private void collect(int[][] g, int r, int c, boolean[][] seen,
                             List<int[]> cells) {
            if (r < 0 || c < 0 || r >= g.length || c >= g[0].length) return;
            if (g[r][c] != 0 || seen[r][c]) return;
            seen[r][c] = true;
            cells.add(new int[] { r, c });
            collect(g, r + 1, c, seen, cells);
            collect(g, r - 1, c, seen, cells);
            collect(g, r, c + 1, seen, cells);
            collect(g, r, c - 1, seen, cells);
        }
    }

    /*
     * Time: O(rows × cols) for every part · Space: O(rows × cols)
     *
     * WATCH OUT
     * - Test a 1×1 rectangle and a rectangle touching the edge of the image
     *   (the [4,0] case).
     * - Ask which output format they want: [top, left, bottom, right], two
     *   coordinate pairs, or [row, col, width, height]. It's a small change,
     *   but get it right.
     */

}
