# Karat Coding Prep: 156 problems in 7 patterns

Pattern-based prep for the Karat technical interview: the problems and close variants candidates report from Karat rounds, plus the core LeetCode problems for each pattern.

Every problem has **Step 1, the brute force** (the obvious way, with its cost) and **Step 2, the better way**, written as simple, commented Java. Every example from the PDFs is run as a test.

## Layout

```
src/main/java/karat/<pattern>/<Pattern>Easy.java     easy problems
src/main/java/karat/<pattern>/<Pattern>Medium.java   medium (+ hard)
src/test/java/karat/<pattern>/<Pattern>Tests.java    all examples
pdfs/                                               study guides
```

Inside each class, every problem is a block of nested classes: `TwoSumBrute` (step 1) then `TwoSum` (step 2), with a header comment giving the LeetCode number and the one trick to remember.

## Run the tests

Needs JDK 11+ only (no Maven, no libraries).

```bash
./run-tests.sh
```

## Contents

| # | Pattern | Easy | Medium (+ Hard) | PDFs |
|---|---|---|---|---|
| 1 | Hash Map, Counting & Sorting | [HashMapEasy](src/main/java/karat/hashmap/HashMapEasy.java) (17) | [HashMapMedium](src/main/java/karat/hashmap/HashMapMedium.java) (13) | [easy](pdfs/Karat-1a-Easy-HashMap-Counting-Sorting.pdf) · [medium](pdfs/Karat-1b-Medium-HashMap-Counting-Sorting.pdf) |
| 2 | String Parsing & Simulation | [StringsEasy](src/main/java/karat/strings/StringsEasy.java) (7) | [StringsMedium](src/main/java/karat/strings/StringsMedium.java) (15) | [easy](pdfs/Karat-2a-Easy-String-Parsing-Simulation.pdf) · [medium](pdfs/Karat-2b-Medium-String-Parsing-Simulation.pdf) |
| 3 | Grid DFS/BFS & Backtracking | [GridsEasy](src/main/java/karat/grids/GridsEasy.java) (3) | [GridsMedium](src/main/java/karat/grids/GridsMedium.java) (19) | [easy](pdfs/Karat-3a-Easy-Grid-DFS-BFS-Backtracking.pdf) · [medium](pdfs/Karat-3b-Medium-Grid-DFS-BFS-Backtracking.pdf) |
| 4 | Graph Basics | [GraphsEasy](src/main/java/karat/graphs/GraphsEasy.java) (3) | [GraphsMedium](src/main/java/karat/graphs/GraphsMedium.java) (19) | [easy](pdfs/Karat-4a-Easy-Graph-Basics.pdf) · [medium](pdfs/Karat-4b-Medium-Graph-Basics.pdf) |
| 5 | Time Windows & Intervals | [IntervalsEasy](src/main/java/karat/intervals/IntervalsEasy.java) (3) | [IntervalsMedium](src/main/java/karat/intervals/IntervalsMedium.java) (17) | [easy](pdfs/Karat-5a-Easy-Time-Windows-Intervals.pdf) · [medium](pdfs/Karat-5b-Medium-Time-Windows-Intervals.pdf) |
| 6 | Light DP & Greedy | [DpGreedyEasy](src/main/java/karat/dp/DpGreedyEasy.java) (4) | [DpGreedyMedium](src/main/java/karat/dp/DpGreedyMedium.java) (16) | [easy](pdfs/Karat-6a-Easy-Light-DP-Greedy.pdf) · [medium](pdfs/Karat-6b-Medium-Light-DP-Greedy.pdf) |
| 7 | Stack, Heap & Binary Search | [StackHeapSearchEasy](src/main/java/karat/stackheap/StackHeapSearchEasy.java) (7) | [StackHeapSearchMedium](src/main/java/karat/stackheap/StackHeapSearchMedium.java) (13) | [easy](pdfs/Karat-7a-Easy-Stack-Heap-Binary-Search.pdf) · [medium](pdfs/Karat-7b-Medium-Stack-Heap-Binary-Search.pdf) |

**Total: 156 problems.** ★ = reported by candidates (directly or as a close variant) in Karat interviews.

## Problem list

### 1. Hash Map, Counting & Sorting

**Easy** · `HashMapEasy`

| # | Problem | LC | Classes |
|---|---|---|---|
| 1 | Two Sum | 1 | `TwoSumBrute`, `TwoSum` |
| 2 | Contains Duplicate | 217 | `ContainsDuplicateBrute`, `ContainsDuplicate` |
| 3 | Valid Anagram | 242 | `ValidAnagramSort`, `ValidAnagram` |
| 4 | Majority Element | 169 | `MajorityElementBrute`, `MajorityElement`, `MajorityVote` |
| 5 | Ransom Note ★ | 383 | `RansomNoteBrute`, `RansomNote` |
| 6 | First Unique Character in a String | 387 | `FirstUniqueCharBrute`, `FirstUniqueChar` |
| 7 | Isomorphic Strings | 205 | `IsomorphicBrute`, `Isomorphic` |
| 8 | Word Pattern | 290 | `WordPatternBrute`, `WordPattern` |
| 9 | Find Words That Can Be Formed by Characters ★ | 1160 | `FormWordsBrute`, `FormWords` |
| 10 | Intersection of Two Arrays II | 350 | `IntersectBrute`, `Intersect` |
| 11 | Contains Duplicate II | 219 | `NearbyDuplicateBrute`, `NearbyDuplicate` |
| 12 | Happy Number | 202 | `HappyNumberBrute`, `HappyNumber` |
| 13 | Uncommon Words from Two Sentences | 884 | `UncommonWordsBrute`, `UncommonWords` |
| 14 | Most Common Word | 819 | `MostCommonWordBrute`, `MostCommonWord` |
| 15 | Longest Palindrome (build one) | 409 | `LongestPalindromeBrute`, `LongestPalindromeBuild` |
| 16 | Minimum Index Sum of Two Lists | 599 | `MinIndexSumBrute`, `MinIndexSum` |
| 17 | Sort Array by Increasing Frequency | 1636 | `FrequencySortBrute`, `FrequencySortIncreasing` |

**Medium** · `HashMapMedium`

| # | Problem | LC | Classes |
|---|---|---|---|
| 1 | Group Anagrams | 49 | `GroupAnagramsBrute`, `GroupAnagrams` |
| 2 | Top K Frequent Elements | 347 | `TopKFrequentBrute`, `TopKFrequent` |
| 3 | Custom Sort String | 791 | `CustomSortBrute`, `CustomSort` |
| 4 | Top K Frequent Words | 692 | `TopKWordsSort`, `TopKWordsHeap` |
| 5 | Sort Characters By Frequency | 451 | `SortByFrequencyBrute`, `SortByFrequency` |
| 6 | Bulls and Cows | 299 | `BullsCowsBrute`, `BullsCows` |
| 7 | Find Players With Zero or One Losses ★ | 2225 | `ZeroOrOneLossesBrute`, `ZeroOrOneLosses` |
| 8 | Subdomain Visit Count ★ | 811 | `SubdomainVisitsBrute`, `SubdomainVisits` |
| 9 | Repeated DNA Sequences | 187 | `RepeatedDnaBrute`, `RepeatedDna` |
| 10 | Longest Consecutive Sequence | 128 | `LongestConsecutiveSort`, `LongestConsecutive` |
| 11 | Subarray Sum Equals K | 560 | `SubarraySumBrute`, `SubarraySum` |
| 12 | Brick Wall | 554 | `BrickWallBrute`, `BrickWall` |
| 13 | Valid Sudoku ★ | 36 | `ValidSudokuPasses`, `ValidSudoku` |

### 2. String Parsing & Simulation

**Easy** · `StringsEasy`

| # | Problem | LC | Classes |
|---|---|---|---|
| 1 | Valid Palindrome | 125 | `ValidPalindromeBrute`, `ValidPalindrome` |
| 2 | Valid Palindrome II | 680 | `ValidPalindromeIIBrute`, `ValidPalindromeII` |
| 3 | Reverse Vowels of a String | 345 | `ReverseVowelsBrute`, `ReverseVowels` |
| 4 | Longest Common Prefix | 14 | `LongestCommonPrefixBrute`, `LongestCommonPrefix` |
| 5 | Add Strings | 415 | `AddStringsParse`, `AddStrings` |
| 6 | Add Binary | 67 | `AddBinaryParse`, `AddBinary` |
| 7 | Roman to Integer | 13 | `RomanToIntegerBrute`, `RomanToInteger` |

**Medium & Hard** · `StringsMedium`

| # | Problem | LC | Classes |
|---|---|---|---|
| 1 | Integer to Roman | 12 | `IntegerToRomanTables`, `IntegerToRoman` |
| 2 | String to Integer (atoi) | 8 | `Atoi` |
| 3 | Reverse Words in a String | 151 | `ReverseWords` |
| 4 | Compare Version Numbers | 165 | `CompareVersion` |
| 5 | Simplify Path | 71 | `SimplifyPath` |
| 6 | Reorder Data in Log Files | 937 | `ReorderLogs` |
| 7 | String Compression | 443 | `StringCompressionBrute`, `StringCompression` |
| 8 | Count and Say | 38 | `CountAndSay` |
| 9 | Zigzag Conversion | 6 | `ZigzagGrid`, `Zigzag` |
| 10 | Multiply Strings | 43 | `MultiplyStringsBrute`, `MultiplyStrings` |
| 11 | Decode String | 394 | `DecodeStringBrute`, `DecodeString` |
| 12 | Basic Calculator II | 227 | `CalculatorIIStack`, `CalculatorII` |
| 13 | Longest Palindromic Substring | 5 | `LongestPalSubstringBrute`, `LongestPalSubstring` |
| 14 | Basic Calculator ★ | 224 | `BasicCalculatorBrute`, `BasicCalculator` |
| 15 | Word Wrap → Text Justification ★ | 68 | `WordWrap`, `TextJustify` |

### 3. Grid DFS/BFS & Backtracking

**Easy** · `GridsEasy`

| # | Problem | LC | Classes |
|---|---|---|---|
| 1 | Every Row and Column Contains All Numbers ★ | 2133 | `RowsColsBrute`, `AllNumbersInRowsCols` |
| 2 | Island Perimeter | 463 | `IslandPerimeterSides`, `IslandPerimeter` |
| 3 | Flood Fill | 733 | `FloodFill` |

**Medium** · `GridsMedium`

| # | Problem | LC | Classes |
|---|---|---|---|
| 1 | Spiral Matrix | 54 | `SpiralVisited`, `Spiral` |
| 2 | Rotate Image | 48 | `RotateCopy`, `RotateInPlace` |
| 3 | Set Matrix Zeroes | 73 | `SetZeroesCopy`, `SetZeroes` |
| 4 | Game of Life | 289 | `GameOfLifeCopy`, `GameOfLife` |
| 5 | Number of Islands | 200 | `NumberOfIslands` |
| 6 | Max Area of Island | 695 | `MaxAreaBrute`, `MaxAreaIsland` |
| 7 | Number of Enclaves | 1020 | `EnclavesBrute`, `Enclaves` |
| 8 | Surrounded Regions | 130 | `SurroundedRegions` |
| 9 | Pacific Atlantic Water Flow | 417 | `PacificAtlanticBrute`, `PacificAtlantic` |
| 10 | Rotting Oranges | 994 | `RottingSimulate`, `RottingOranges` |
| 11 | 01 Matrix | 542 | `ZeroOneMatrixBrute`, `ZeroOneMatrix` |
| 12 | Shortest Path in Binary Matrix | 1091 | `ShortestPathDFS`, `ShortestPathBinaryMatrix` |
| 13 | Nearest Exit from Entrance in Maze | 1926 | `NearestExit` |
| 14 | Word Search ★ | 79 | `WordSearch` |
| 15 | Subsets | 78 | `SubsetsBitmask`, `Subsets` |
| 16 | Permutations | 46 | `Permutations` |
| 17 | Combination Sum | 39 | `CombinationSumBrute`, `CombinationSum` |
| 18 | Letter Combinations of a Phone Number | 17 | `LetterCombinations` |
| 19 | Find Rectangles (3 parts) ★ | Karat | `OneRectangleSimple`, `FindRectangles`, `FindShapes` |

### 4. Graph Basics

**Easy** · `GraphsEasy`

| # | Problem | LC | Classes |
|---|---|---|---|
| 1 | Find the Town Judge | 997 | `TownJudgeBrute`, `TownJudge` |
| 2 | Find Center of Star Graph | 1791 | `StarCenterCount`, `StarCenter` |
| 3 | Find if Path Exists in Graph | 1971 | `PathExistsBrute`, `PathExists` |

**Medium & Hard** · `GraphsMedium`

| # | Problem | LC | Classes |
|---|---|---|---|
| 1 | Keys and Rooms | 841 | `KeysAndRooms` |
| 2 | Number of Provinces | 547 | `NumberOfProvinces`, `ProvincesUnionFind` |
| 3 | Number of Operations to Make Network Connected | 1319 | `NetworkConnected` |
| 4 | Redundant Connection | 684 | `RedundantBrute`, `RedundantConnection` |
| 5 | Is Graph Bipartite? | 785 | `Bipartite` |
| 6 | Clone Graph | 133 | `CloneGraph` |
| 7 | All Paths From Source to Target | 797 | `AllPaths` |
| 8 | Course Schedule | 207 | `CourseScheduleBrute`, `CourseSchedule` |
| 9 | Course Schedule II | 210 | `CourseScheduleII` |
| 10 | Course Schedule IV | 1462 | `CourseScheduleIVBrute`, `CourseScheduleIV` |
| 11 | Find All Possible Recipes from Given Supplies | 2115 | `RecipesBrute`, `Recipes` |
| 12 | Find Eventual Safe States | 802 | `SafeStates` |
| 13 | Parent–Child Ancestors (3 parts) ★ | Karat | `KaratAncestors` |
| 14 | All Ancestors of a Node in a DAG | 2192 | `AllAncestorsBrute`, `AllAncestors` |
| 15 | Loud and Rich | 851 | `LoudRichBrute`, `LoudRich` |
| 16 | Middle Course (2 parts) ★ | Karat | `KaratMiddleCourse` |
| 17 | Minimum Genetic Mutation | 433 | `GeneticMutation` |
| 18 | Word Ladder | 127 | `WordLadderScan`, `WordLadder` |
| 19 | Network Delay Time | 743 | `NetworkDelayBellman`, `NetworkDelay` |

### 5. Time Windows & Intervals

**Easy** · `IntervalsEasy`

| # | Problem | LC | Classes |
|---|---|---|---|
| 1 | Summary Ranges | 228 | `SummaryRanges` |
| 2 | Teemo Attacking | 495 | `TeemoBrute`, `Teemo` |
| 3 | Maximum Average Subarray I | 643 | `MaxAverageBrute`, `MaxAverage` |

**Medium** · `IntervalsMedium`

| # | Problem | LC | Classes |
|---|---|---|---|
| 1 | Merge Intervals | 56 | `MergeIntervalsBrute`, `MergeIntervals` |
| 2 | Insert Interval | 57 | `InsertIntervalMerge`, `InsertInterval` |
| 3 | Non-overlapping Intervals | 435 | `NonOverlapping` |
| 4 | Minimum Number of Arrows to Burst Balloons | 452 | `MinArrows` |
| 5 | Interval List Intersections | 986 | `IntersectionsBrute`, `Intersections` |
| 6 | Meeting Fits & Free Slots (2 parts) ★ | Karat | `FreeSlotsMinutes`, `KaratFreeTime` |
| 7 | My Calendar I | 729 | `MyCalendarList`, `MyCalendar` |
| 8 | Minimum Meeting Rooms ★ | 253 | `MinRoomsBrute`, `MinRooms` |
| 9 | Car Pooling | 1094 | `CarPoolingBrute`, `CarPooling` |
| 10 | Longest Substring Without Repeating Characters | 3 | `LongestUniqueBrute`, `LongestUniqueSubstring` |
| 11 | Minimum Size Subarray Sum | 209 | `MinSubArrayBrute`, `MinSubArray` |
| 12 | Max Consecutive Ones III | 1004 | `LongestOnesBrute`, `LongestOnes` |
| 13 | Permutation in String | 567 | `PermutationInStringBrute`, `PermutationInString` |
| 14 | Find All Anagrams in a String | 438 | `FindAnagramsBrute`, `FindAnagrams` |
| 15 | Alert Using Same Key-Card 3+ Times in One Hour ★ | 1604 | `KeyCardBrute`, `KeyCardAlert` |
| 16 | High-Access Employees ★ | 2933 | `HighAccessEmployees` |
| 17 | Badge Records (2 parts) ★ | Karat | `KaratBadgeRecords`, `KaratBadgeTimes` |

### 6. Light DP & Greedy

**Easy** · `DpGreedyEasy`

| # | Problem | LC | Classes |
|---|---|---|---|
| 1 | Climbing Stairs | 70 | `ClimbingStairsRecursive`, `ClimbingStairs` |
| 2 | Min Cost Climbing Stairs | 746 | `MinCostStairsRecursive`, `MinCostStairs` |
| 3 | Best Time to Buy and Sell Stock | 121 | `StockBrute`, `BestTimeToBuySell` |
| 4 | Assign Cookies | 455 | `AssignCookiesScan`, `AssignCookies` |

**Medium** · `DpGreedyMedium`

| # | Problem | LC | Classes |
|---|---|---|---|
| 1 | Best Time to Buy and Sell Stock II | 122 | `StockII` |
| 2 | House Robber | 198 | `HouseRobberRecursive`, `HouseRobber` |
| 3 | House Robber II | 213 | `HouseRobberII` |
| 4 | Maximum Subarray (Kadane) | 53 | `MaxSubarrayBrute`, `MaxSubarray` |
| 5 | Jump Game ★ | 55 | `JumpGameDP`, `JumpGame` |
| 6 | Jump Game II | 45 | `JumpGameIIDP`, `JumpGameII` |
| 7 | Partition Labels | 763 | `PartitionLabelsScan`, `PartitionLabels` |
| 8 | Unique Paths | 62 | `UniquePathsRecursive`, `UniquePaths` |
| 9 | Minimum Path Sum | 64 | `MinPathSumRecursive`, `MinPathSum` |
| 10 | Maximal Square ★ | 221 | `MaximalSquareBrute`, `MaximalSquare` |
| 11 | Coin Change | 322 | `CoinChangeRecursive`, `CoinChange` |
| 12 | Decode Ways | 91 | `DecodeWaysRecursive`, `DecodeWays` |
| 13 | Word Break | 139 | `WordBreakRecursive`, `WordBreak` |
| 14 | Longest Increasing Subsequence | 300 | `LISQuadratic`, `LISFast` |
| 15 | Longest Common Browsing History ★ | 718 | `CommonHistoryBrute`, `LongestCommonHistory` |
| 16 | Longest Common Subsequence | 1143 | `LCSRecursive`, `LCS` |

### 7. Stack, Heap & Binary Search

**Easy** · `StackHeapSearchEasy`

| # | Problem | LC | Classes |
|---|---|---|---|
| 1 | Valid Parentheses | 20 | `ParenthesesReplace`, `ValidParentheses` |
| 2 | Remove All Adjacent Duplicates in String | 1047 | `RemoveDuplicatesBrute`, `RemoveDuplicates` |
| 3 | Next Greater Element I | 496 | `NextGreaterBrute`, `NextGreater` |
| 4 | Binary Search | 704 | `LinearSearch`, `BinarySearch` |
| 5 | Search Insert Position | 35 | `SearchInsertLinear`, `SearchInsert` |
| 6 | First Bad Version | 278 | `VersionControl`, `FirstBadLinear`, `FirstBadVersion` |
| 7 | Last Stone Weight | 1046 | `LastStoneSort`, `LastStone` |

**Medium & Hard** · `StackHeapSearchMedium`

| # | Problem | LC | Classes |
|---|---|---|---|
| 1 | Daily Temperatures | 739 | `DailyTempsBrute`, `DailyTemperatures` |
| 2 | Min Stack | 155 | `MinStackScan`, `MinStack` |
| 3 | Evaluate Reverse Polish Notation | 150 | `EvalRPN` |
| 4 | Asteroid Collision | 735 | `AsteroidsBrute`, `Asteroids` |
| 5 | Find First and Last Position of Element | 34 | `SearchRangeLinear`, `SearchRange` |
| 6 | Search in Rotated Sorted Array | 33 | `SearchRotated` |
| 7 | Time Based Key-Value Store | 981 | `TimeMapScan`, `TimeMap` |
| 8 | Koko Eating Bananas | 875 | `KokoBrute`, `KokoBananas` |
| 9 | Capacity To Ship Packages Within D Days | 1011 | `ShipCapacityBrute`, `ShipCapacity` |
| 10 | Kth Largest Element in an Array | 215 | `KthLargestSort`, `KthLargest` |
| 11 | K Closest Points to Origin | 973 | `KClosestSort`, `KClosest` |
| 12 | Find Median from Data Stream | 295 | `MedianFinderSorted`, `MedianFinder` |
| 13 | LRU Cache ★ | 146 | `LRUCacheList`, `LRUCache`, `LRUCacheSimple` |

## Suggested order

1. For each pattern, read the **Easy** PDF, then re-solve the Easy problems without looking.
2. Move to the **Medium** PDF. The ★ Karat multi-part questions are the ones to over-prepare.
3. Finish with timed, 40-minute, 3-part mock sessions.
