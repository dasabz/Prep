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

    // ====================================================================
    // 2. Rotate Image
    // LeetCode 48 · Medium
    // Trick: transpose, then reverse each row  ·  O(n²)
    // ====================================================================

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

    // ====================================================================
    // 3. Set Matrix Zeroes
    // LeetCode 73 · Medium
    // Trick: first remember zero rows/cols, then write  ·  O(R × C)
    // ====================================================================

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

    // ====================================================================
    // 4. Game of Life
    // LeetCode 289 · Medium
    // Trick: in-place states: 2 = live→dead, 3 = dead→live  ·  O(R × C)
    // ====================================================================

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

    // ====================================================================
    // 5. Number of Islands
    // LeetCode 200 · Medium
    // Trick: on a '1': count++ then sink the whole island  ·  O(R·C)
    // ====================================================================

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

    // ====================================================================
    // 6. Max Area of Island
    // LeetCode 695 · Medium
    // Trick: sink() returns the size: 1 + four recursive calls  ·  O(R × C)
    // ====================================================================

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

    // ====================================================================
    // 7. Number of Enclaves
    // LeetCode 1020 · Medium
    // Trick: sink everything connected to the border; count what's left  ·  O(R × C)
    // ====================================================================

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

    // ====================================================================
    // 8. Surrounded Regions
    // LeetCode 130 · Medium
    // Trick: mark border-linked 'O' as safe; flip the rest  ·  O(R × C)
    // ====================================================================

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

    // ====================================================================
    // 9. Pacific Atlantic Water Flow
    // LeetCode 417 · Medium
    // Trick: climb UPHILL from each ocean; answer = cells reached by both  ·  O(R × C)
    // ====================================================================

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

    // ====================================================================
    // 10. Rotting Oranges
    // LeetCode 994 · Medium
    // Trick: all rotten in queue first; one BFS level = 1 minute  ·  O(R·C)
    // ====================================================================

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

    // ====================================================================
    // 11. 01 Matrix
    // LeetCode 542 · Medium
    // Trick: multi-source BFS from ALL zeros at once  ·  O(R × C)
    // ====================================================================

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

    // ====================================================================
    // 12. Shortest Path in Binary Matrix
    // LeetCode 1091 · Medium
    // Trick: BFS; store length in queue; mark when adding  ·  O(n²)
    // ====================================================================

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

    // ====================================================================
    // 13. Nearest Exit from Entrance in Maze
    // LeetCode 1926 · Medium
    // Trick: BFS from the entrance; first border cell (≠ entrance) wins  ·  O(R × C)
    // ====================================================================

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

    // ====================================================================
    // 14. Word Search  ★ Karat
    // LeetCode 79 · Medium
    // Trick: mark '#', try 4 ways, restore the letter  ·  O(R·C·4^L)
    // ====================================================================

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

    // ====================================================================
    // 15. Subsets
    // LeetCode 78 · Medium
    // Trick: record every call; loop from start; add a copy  ·  O(n·2ⁿ)
    // ====================================================================

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

    // ====================================================================
    // 16. Permutations
    // LeetCode 46 · Medium
    // Trick: backtracking with a used[] array  ·  O(n × n!)
    // ====================================================================

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

    // ====================================================================
    // 17. Combination Sum
    // LeetCode 39 · Medium
    // Trick: backtrack from start, reuse allowed (recurse with i); sort + break  ·  exponential
    // ====================================================================

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

    // ====================================================================
    // 18. Letter Combinations of a Phone Number
    // LeetCode 17 · Medium
    // Trick: depth = digit index; loop over that digit's letters  ·  O(4ⁿ × n)
    // ====================================================================

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

    // ====================================================================
    // 19. Find Rectangles (3 parts)  ★ Karat
    // A Karat question asked in parts · Part 3 is Number of Islands · Medium
    // Trick: first unused 0 = top-left; walk right & down; mark  ·  O(R·C)
    // ====================================================================

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

}
