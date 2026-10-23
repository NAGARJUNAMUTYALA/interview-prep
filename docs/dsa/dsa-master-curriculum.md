# Master DSA Curriculum: 150 Curated High-Frequency Problems

**Target Roles:** Mid-to-Senior Java Software Engineers  
**Structure:** 16 Core Algorithmic Patterns, progressive difficulty (Easy ➔ Medium ➔ Hard)  

---

## Pattern 1: Arrays, Strings & Hashing (Problems 1–15)

| # | Problem | Difficulty | Key Pattern / Insight | Time | Space | Status |
|---|---|---|---|---|---|---|
| 1 | Two Sum | Easy | Hash Map lookup for complement `target - num` | $O(n)$ | $O(n)$ | `COMPLETED` |
| 2 | Contains Duplicate | Easy | Hash Set insertion or frequency map | $O(n)$ | $O(n)$ | `TODO` |
| 3 | Valid Anagram | Easy | 26-character frequency array | $O(n)$ | $O(1)$ | `TODO` |
| 4 | Group Anagrams | Medium | Sorted string or character frequency tuple as Map key | $O(n \cdot k)$ | $O(n \cdot k)$ | `TODO` |
| 5 | Top K Frequent Elements | Medium | Frequency Map + Min-Heap (size $k$) or Bucket Sort | $O(n \log k)$ | $O(n)$ | `TODO` |
| 6 | Product of Array Except Self | Medium | Prefix products from left, running suffix product from right | $O(n)$ | $O(1)$ | `TODO` |
| 7 | Longest Consecutive Sequence | Medium | Hash Set lookup; only explore sequence start (`!set.contains(x-1)`) | $O(n)$ | $O(n)$ | `TODO` |
| 8 | Encode and Decode Strings | Medium | Delimiter with length prefix (`4#code`) | $O(n)$ | $O(1)$ | `TODO` |
| 9 | Subarray Sum Equals K | Medium | Prefix sum array with cumulative frequency map | $O(n)$ | $O(n)$ | `TODO` |
| 10 | First Missing Positive | Hard | In-place cyclic sort into index `A[i] - 1` | $O(n)$ | $O(1)$ | `TODO` |
| 11 | Valid Sudoku | Medium | 3 Sets for row, column, and 3x3 sub-box | $O(1)$ | $O(1)$ | `TODO` |
| 12 | Rotate Image (90° Matrix) | Medium | Transpose matrix along diagonal, then reverse each row | $O(n^2)$ | $O(1)$ | `TODO` |
| 13 | Spiral Matrix | Medium | 4 boundaries (`top, bottom, left, right`) shrinking inward | $O(m \cdot n)$ | $O(1)$ | `TODO` |
| 14 | Set Matrix Zeroes | Medium | Use first row and first column as flag markers | $O(m \cdot n)$ | $O(1)$ | `TODO` |
| 15 | Majority Element (> n/2) | Easy | Boyer-Moore Voting Algorithm | $O(n)$ | $O(1)$ | `TODO` |

---

## Pattern 2: Two Pointers (Problems 16–25)

| # | Problem | Difficulty | Key Pattern / Insight | Time | Space | Status |
|---|---|---|---|---|---|---|
| 16 | Valid Palindrome | Easy | Inward two pointers skipping non-alphanumeric chars | $O(n)$ | $O(1)$ | `TODO` |
| 17 | Two Sum II (Sorted Input) | Medium | Left and right pointers moving based on sum comparison | $O(n)$ | $O(1)$ | `COMPLETED` |
| 18 | 3Sum (Zero Sum Triplet) | Medium | Sort array, fix first number, two pointers on remainder | $O(n^2)$ | $O(1)$ | `TODO` |
| 19 | Container With Most Water | Medium | Two pointers at ends, move the pointer with smaller height | $O(n)$ | $O(1)$ | `TODO` |
| 20 | Trapping Rain Water | Hard | Two pointers tracking `leftMax` and `rightMax` | $O(n)$ | $O(1)$ | `TODO` |
| 21 | Move Zeroes | Easy | Fast and slow pointer swap | $O(n)$ | $O(1)$ | `TODO` |
| 22 | Remove Duplicates from Sorted | Easy | Slow pointer writing distinct elements | $O(n)$ | $O(1)$ | `TODO` |
| 23 | Sort Colors (Dutch Flag) | Medium | 3 pointers (`low, mid, high`) partitioning 0s, 1s, and 2s | $O(n)$ | $O(1)$ | `TODO` |
| 24 | 4Sum | Medium | Sort array, two nested loops, two pointers for inner pair | $O(n^3)$ | $O(1)$ | `TODO` |
| 25 | Backspace String Compare | Easy | Traverse from right to left with backspace count | $O(n)$ | $O(1)$ | `TODO` |

---

## Pattern 3: Sliding Window (Problems 26–35)

| # | Problem | Difficulty | Key Pattern / Insight | Time | Space | Status |
|---|---|---|---|---|---|---|
| 26 | Best Time to Buy & Sell Stock | Easy | Track minimum price seen so far, maximize difference | $O(n)$ | $O(1)$ | `TODO` |
| 27 | Longest Substring Without Repeats | Medium | Sliding window with HashMap storing last seen indices | $O(n)$ | $O(\min(m, n))$ | `COMPLETED` |
| 28 | Longest Repeating Char Replacement | Medium | Window where `windowLength - maxFreq <= k` | $O(n)$ | $O(1)$ | `TODO` |
| 29 | Permutation in String | Medium | Fixed size window matching character frequency counts | $O(n)$ | $O(1)$ | `TODO` |
| 30 | Minimum Window Substring | Hard | Variable window contracting until valid condition breaks | $O(m + n)$ | $O(1)$ | `TODO` |
| 31 | Sliding Window Maximum | Hard | Monotonic Decreasing Deque storing array indices | $O(n)$ | $O(k)$ | `TODO` |
| 32 | Minimum Size Subarray Sum | Medium | Expanding right pointer, contracting left when `sum >= target` | $O(n)$ | $O(1)$ | `TODO` |
| 33 | Fruit Into Baskets (Max 2 types) | Medium | Window maintaining at most 2 distinct keys in Map | $O(n)$ | $O(1)$ | `TODO` |
| 34 | Subarrays with K Different Integers | Hard | `atMost(k) - atMost(k - 1)` sliding window trick | $O(n)$ | $O(n)$ | `TODO` |
| 35 | Max Consecutive Ones III | Medium | Window allowing at most `k` zeroes | $O(n)$ | $O(1)$ | `TODO` |

---

## Pattern 4: Stack & Monotonic Stack (Problems 36–45)

| # | Problem | Difficulty | Key Pattern / Insight | Time | Space | Status |
|---|---|---|---|---|---|---|
| 36 | Valid Parentheses | Easy | Stack matching opening brackets with closed | $O(n)$ | $O(n)$ | `TODO` |
| 37 | Min Stack | Medium | Auxiliary stack or paired values tracking running min | $O(1)$ ops | $O(n)$ | `TODO` |
| 38 | Evaluate Reverse Polish Notation | Medium | Stack operand evaluation | $O(n)$ | $O(n)$ | `TODO` |
| 39 | Daily Temperatures | Medium | Monotonic decreasing stack storing unresolved days | $O(n)$ | $O(n)$ | `TODO` |
| 40 | Largest Rectangle in Histogram | Hard | Monotonic stack finding nearest smaller left and right | $O(n)$ | $O(n)$ | `TODO` |
| 41 | Next Greater Element I | Easy | Monotonic stack + Hash Map mapping element to next greater | $O(n)$ | $O(n)$ | `COMPLETED` |
| 42 | Next Greater Element II (Circular)| Medium | Iterate array twice (`2n`) with monotonic stack | $O(n)$ | $O(n)$ | `TODO` |
| 43 | Simplify Path (Unix Canonical) | Medium | Stack pushing directories, popping on `".."` | $O(n)$ | $O(n)$ | `TODO` |
| 44 | Online Stock Span | Medium | Monotonic stack storing pairs of `(price, span)` | $O(1)$ amort | $O(n)$ | `TODO` |
| 45 | Basic Calculator (with Parentheses)| Hard | Stack preserving sign and running results on `(` and `)` | $O(n)$ | $O(n)$ | `TODO` |

---

## Pattern 5: Binary Search (Problems 46–55)

| # | Problem | Difficulty | Key Pattern / Insight | Time | Space | Status |
|---|---|---|---|---|---|---|
| 46 | Binary Search (Standard) | Easy | Classic `low + (high - low)/2` | $O(\log n)$ | $O(1)$ | `TODO` |
| 47 | Search a 2D Matrix | Medium | Treat $m \times n$ matrix as virtual 1D array (`row = mid / n`) | $O(\log(m \cdot n))$ | $O(1)$ | `TODO` |
| 48 | Koko Eating Bananas | Medium | Binary search on solution range `[1, max(piles)]` | $O(n \log(\max))$ | $O(1)$ | `TODO` |
| 49 | Find Minimum in Rotated Sorted Array | Medium | Check if `nums[mid] > nums[high]` to locate inflection | $O(\log n)$ | $O(1)$ | `TODO` |
| 50 | Search in Rotated Sorted Array | Medium | Determine which half is sorted, then check boundaries | $O(\log n)$ | $O(1)$ | `COMPLETED` |
| 51 | Find Peak Element | Medium | Move towards higher neighboring element | $O(\log n)$ | $O(1)$ | `TODO` |
| 52 | Capacity to Ship Packages in D Days | Medium | Binary search on capacity range `[max(weights), sum]` | $O(n \log(\text{sum}))$ | $O(1)$ | `TODO` |
| 53 | Median of Two Sorted Arrays | Hard | Partition both arrays such that left half $\le$ right half | $O(\log(\min(m, n)))$| $O(1)$ | `TODO` |
| 54 | First and Last Position of Element | Medium | Run two binary searches (one for lower bound, one upper) | $O(\log n)$ | $O(1)$ | `TODO` |
| 55 | Split Array Largest Sum | Hard | Binary search on largest sum with greedy validator | $O(n \log(\text{sum}))$ | $O(1)$ | `TODO` |

---

## Pattern 6: Linked Lists (Problems 56–65)

| # | Problem | Difficulty | Key Pattern / Insight | Time | Space | Status |
|---|---|---|---|---|---|---|
| 56 | Reverse Linked List | Easy | Iterative three-pointer swap (`prev, curr, next`) | $O(n)$ | $O(1)$ | `TODO` |
| 57 | Merge Two Sorted Lists | Easy | Dummy head node with pointer advancing smaller value | $O(m + n)$ | $O(1)$ | `TODO` |
| 58 | Reorder List | Medium | Find middle, reverse second half, weave two halves | $O(n)$ | $O(1)$ | `TODO` |
| 59 | Remove Nth Node From End | Medium | Two pointers with $N$-step lead; dummy node | $O(n)$ | $O(1)$ | `TODO` |
| 60 | Copy List with Random Pointer | Medium | Weave cloned nodes next to originals, assign randoms, split | $O(n)$ | $O(1)$ | `TODO` |
| 61 | Add Two Numbers | Medium | Traverse both lists with carry propagation | $O(\max(m, n))$ | $O(1)$ | `TODO` |
| 62 | Linked List Cycle | Easy | Floyd's Cycle Detection (Tortoise and Hare) | $O(n)$ | $O(1)$ | `TODO` |
| 63 | Find Cycle Start Node | Medium | When pointers meet, reset slow to head; move both 1 step | $O(n)$ | $O(1)$ | `TODO` |
| 64 | Merge K Sorted Lists | Hard | PriorityQueue (Min-Heap) storing heads of $K$ lists | $O(n \log k)$ | $O(k)$ | `TODO` |
| 65 | Reverse Nodes in k-Group | Hard | Count $K$ nodes ahead, reverse group, recursively wire | $O(n)$ | $O(1)$ | `TODO` |

---

## Pattern 7: Trees & Binary Search Trees (Problems 66–80)

| # | Problem | Difficulty | Key Pattern / Insight | Time | Space | Status |
|---|---|---|---|---|---|---|
| 66 | Invert Binary Tree | Easy | Postorder or preorder swap left and right subtrees | $O(n)$ | $O(h)$ | `TODO` |
| 67 | Maximum Depth of Binary Tree | Easy | DFS `1 + Math.max(left, right)` | $O(n)$ | $O(h)$ | `TODO` |
| 68 | Diameter of Binary Tree | Easy | Postorder DFS updating global max `leftHeight + rightHeight` | $O(n)$ | $O(h)$ | `TODO` |
| 69 | Balanced Binary Tree | Easy | Return height if balanced; return -1 if unbalanced | $O(n)$ | $O(h)$ | `TODO` |
| 70 | Same Tree | Easy | Structural and value equality comparison | $O(n)$ | $O(h)$ | `TODO` |
| 71 | Subtree of Another Tree | Easy | Serialized traversal or recursive identical tree check | $O(m \cdot n)$ | $O(h)$ | `TODO` |
| 72 | Lowest Common Ancestor (BST) | Medium | Follow BST property: if both values $<$ root, go left | $O(h)$ | $O(1)$ | `TODO` |
| 73 | Lowest Common Ancestor (Binary Tree)| Medium | DFS returning matched nodes; if both left & right non-null, root is LCA | $O(n)$ | $O(h)$ | `TODO` |
| 74 | Binary Tree Level Order Traversal | Medium | Queue BFS with level-size loop | $O(n)$ | $O(n)$ | `COMPLETED` |
| 75 | Binary Tree Right Side View | Medium | BFS queue (last element in level) or DFS (visit right first) | $O(n)$ | $O(n)$ | `TODO` |
| 76 | Count Good Nodes in Binary Tree | Medium | DFS passing down maximum value along current path | $O(n)$ | $O(h)$ | `TODO` |
| 77 | Validate Binary Search Tree | Medium | DFS with valid range `(minVal, maxVal)` | $O(n)$ | $O(h)$ | `TODO` |
| 78 | Kth Smallest Element in a BST | Medium | Inorder traversal yields sorted order | $O(h + k)$ | $O(h)$ | `TODO` |
| 79 | Construct Binary Tree from Pre & In | Medium | Preorder root partitions Inorder into left & right subtrees | $O(n)$ | $O(n)$ | `TODO` |
| 80 | Binary Tree Maximum Path Sum | Hard | Postorder DFS: max gain from branch; update global max with split | $O(n)$ | $O(h)$ | `TODO` |

---

## Pattern 8: Heaps & Priority Queues (Problems 81–90)

| # | Problem | Difficulty | Key Pattern / Insight | Time | Space | Status |
|---|---|---|---|---|---|---|
| 81 | Kth Largest Element in an Array | Medium | Min-Heap of size $K$ or QuickSelect algorithm | $O(n \log k)$ / $O(n)$ | $O(k)$ | `TODO` |
| 82 | Last Stone Weight | Easy | Max-Heap simulation | $O(n \log n)$ | $O(n)$ | `TODO` |
| 83 | K Closest Points to Origin | Medium | Max-Heap of size $K$ based on Euclidean distance | $O(n \log k)$ | $O(k)$ | `TODO` |
| 84 | Task Scheduler | Medium | Max-Heap of frequencies with cooldown wait queue | $O(n)$ | $O(1)$ | `TODO` |
| 85 | Find Median from Data Stream | Hard | Two Heaps: Max-Heap (lower half) & Min-Heap (upper half) | $O(\log n)$ add | $O(n)$ | `TODO` |
| 86 | Reorganize String | Medium | Max-Heap placing most frequent characters on alternating indices | $O(n \log A)$ | $O(A)$ | `TODO` |
| 87 | Design Twitter | Medium | User follow graph + Min-Heap merging 10 most recent tweets | $O(k \log N)$ | $O(N)$ | `TODO` |
| 88 | Minimum Cost to Connect Sticks | Medium | Min-Heap greedy combination of two smallest elements | $O(n \log n)$ | $O(n)$ | `TODO` |
| 89 | Seat Reservation Manager | Medium | Min-Heap tracking smallest unreserved seat numbers | $O(\log n)$ | $O(n)$ | `TODO` |
| 90 | Smallest Range Covering K Lists | Hard | Min-Heap with one pointer per list tracking running max | $O(n \log k)$ | $O(k)$ | `TODO` |

---

## Pattern 9: Tries (Prefix Trees) (Problems 91–95)

| # | Problem | Difficulty | Key Pattern / Insight | Time | Space | Status |
|---|---|---|---|---|---|---|
| 91 | Implement Trie (Prefix Tree) | Medium | Node with 26-element array and `isEndOfWord` flag | $O(L)$ per op | $O(N \cdot L)$ | `TODO` |
| 92 | Design Add and Search Words | Medium | Trie traversal with wildcard `'.'` triggering backtracking DFS | $O(L)$ / $O(26^L)$ | $O(N \cdot L)$ | `TODO` |
| 93 | Word Search II | Hard | Backtracking DFS over matrix guided by Trie prefix pruning | $O(M \cdot N \cdot 4^L)$ | $O(\text{words})$ | `TODO` |
| 94 | Maximum XOR of Two Numbers | Medium | Bitwise Trie inserting 32-bit binary representations | $O(n \cdot 32)$ | $O(n)$ | `TODO` |
| 95 | Replace Words (Dictionary Roots) | Medium | Trie prefix lookup replacing words with shortest root | $O(N \cdot L)$ | $O(\text{dict})$ | `TODO` |

---

## Pattern 10: Graphs (BFS, DFS, Topological Sort) (Problems 96–110)

| # | Problem | Difficulty | Key Pattern / Insight | Time | Space | Status |
|---|---|---|---|---|---|---|
| 96 | Number of Islands | Medium | Grid DFS/BFS marking visited land cells `1 ➔ 0` | $O(m \cdot n)$ | $O(m \cdot n)$ | `TODO` |
| 97 | Clone Graph | Medium | HashMap mapping original node ➔ cloned node + DFS/BFS | $O(V + E)$ | $O(V)$ | `TODO` |
| 98 | Max Area of Island | Medium | Grid DFS summing connected component area | $O(m \cdot n)$ | $O(m \cdot n)$ | `TODO` |
| 99 | Pacific Atlantic Water Flow | Medium | Reverse BFS/DFS from ocean borders inward | $O(m \cdot n)$ | $O(m \cdot n)$ | `TODO` |
| 100| Surrounded Regions | Medium | Reverse DFS from border `'O'`s, convert remaining `'O'` to `'X'` | $O(m \cdot n)$ | $O(m \cdot n)$ | `TODO` |
| 101| Rotting Oranges | Medium | Multi-source BFS tracking elapsed minutes | $O(m \cdot n)$ | $O(m \cdot n)$ | `TODO` |
| 102| Course Schedule (Cycle Detection) | Medium | Kahn's Algorithm (in-degree array + Queue) or DFS cycle detect | $O(V + E)$ | $O(V + E)$ | `TODO` |
| 103| Course Schedule II (Topo Order) | Medium | Kahn's Algorithm recording processed nodes | $O(V + E)$ | $O(V + E)$ | `TODO` |
| 104| Graph Valid Tree | Medium | Union-Find: $n-1$ edges and no cycle | $O(V \cdot \alpha(V))$ | $O(V)$ | `TODO` |
| 105| Number of Connected Components | Medium | Disjoint Set Union (Union-Find) with path compression | $O(V + E \cdot \alpha(V))$| $O(V)$ | `TODO` |
| 106| Redundant Connection | Medium | Union-Find: edge connecting already united components is redundant | $O(n \cdot \alpha(n))$ | $O(n)$ | `TODO` |
| 107| Word Ladder | Hard | Bidirectional BFS transforming words differing by 1 character | $O(M^2 \cdot N)$ | $O(M \cdot N)$ | `TODO` |
| 108| Network Delay Time (Dijkstra) | Medium | Dijkstra's shortest path with Min-Heap | $O(E \log V)$ | $O(V + E)$ | `TODO` |
| 109| Cheapest Flights Within K Stops | Medium | Bellman-Ford / Modified Dijkstra tracking stops | $O(K \cdot E)$ | $O(V)$ | `TODO` |
| 110| Alien Dictionary | Hard | Build character dependency graph, perform Topological Sort | $O(C)$ | $O(1)$ | `TODO` |

---

## Pattern 11: Dynamic Programming (1D & 2D) (Problems 111–135)

| # | Problem | Difficulty | Key Pattern / Insight | Time | Space | Status |
|---|---|---|---|---|---|---|
| 111| Climbing Stairs | Easy | Fibonacci DP: `dp[i] = dp[i-1] + dp[i-2]` | $O(n)$ | $O(1)$ | `TODO` |
| 112| Min Cost Climbing Stairs | Easy | `min(cost[i-1] + dp[i-1], cost[i-2] + dp[i-2])` | $O(n)$ | $O(1)$ | `TODO` |
| 113| House Robber | Medium | `dp[i] = Math.max(dp[i-1], dp[i-2] + nums[i])` | $O(n)$ | $O(1)$ | `TODO` |
| 114| House Robber II (Circular Houses)| Medium | Run House Robber on `[0, n-2]` and `[1, n-1]`; take max | $O(n)$ | $O(1)$ | `TODO` |
| 115| Longest Palindromic Substring | Medium | Expand around centers ($2n - 1$ centers) | $O(n^2)$ | $O(1)$ | `TODO` |
| 116| Palindromic Substrings (Count) | Medium | Expand around center counting valid expansions | $O(n^2)$ | $O(1)$ | `TODO` |
| 117| Decode Ways | Medium | DP checking 1-digit `[1..9]` and 2-digit `[10..26]` validity | $O(n)$ | $O(1)$ | `TODO` |
| 118| Coin Change | Medium | Unbounded Knapsack: `dp[i] = min(dp[i - coin] + 1)` | $O(A \cdot C)$ | $O(A)$ | `COMPLETED` |
| 119| Maximum Product Subarray | Medium | Track both running `max` and `min` due to negative flips | $O(n)$ | $O(1)$ | `TODO` |
| 120| Word Break | Medium | `dp[i]` is true if `dp[j] && dict.contains(s[j..i])` | $O(n^3)$ | $O(n)$ | `TODO` |
| 121| Longest Increasing Subsequence | Medium | DP with patience binary search | $O(n \log n)$ | $O(n)$ | `TODO` |
| 122| Partition Equal Subset Sum | Medium | 0/1 Knapsack with target sum = `total / 2` | $O(n \cdot S)$ | $O(S)$ | `TODO` |
| 123| Unique Paths | Medium | 2D Grid DP: `dp[r][c] = dp[r-1][c] + dp[r][c-1]` | $O(m \cdot n)$ | $O(n)$ | `TODO` |
| 124| Longest Common Subsequence | Medium | 2D DP: if match `1 + dp[i-1][j-1]`, else `max(left, top)` | $O(m \cdot n)$ | $O(n)$ | `TODO` |
| 125| Best Time to Buy Stock with Cooldown| Medium | State machine DP: `Held, Sold, Reset` | $O(n)$ | $O(1)$ | `TODO` |
| 126| Coin Change II (Ways to Make) | Medium | Outer loop over coins, inner loop over amounts | $O(A \cdot C)$ | $O(A)$ | `TODO` |
| 127| Target Sum | Medium | Transform to 0/1 Knapsack subset sum | $O(n \cdot S)$ | $O(S)$ | `TODO` |
| 128| Interleaving String | Medium | 2D DP matching characters from $s_1$ or $s_2$ | $O(m \cdot n)$ | $O(n)$ | `TODO` |
| 129| Edit Distance (Levenshtein) | Medium | 2D DP: Insert, Delete, Replace costs | $O(m \cdot n)$ | $O(n)$ | `TODO` |
| 130| Burst Balloons | Hard | Interval DP `dp[left][right]`: iterate over last burst balloon | $O(n^3)$ | $O(n^2)$ | `TODO` |
| 131| Regular Expression Matching | Hard | 2D DP handling `.` and `*` wildcard transitions | $O(m \cdot n)$ | $O(m \cdot n)$ | `TODO` |
| 132| Distinct Subsequences | Hard | 2D DP counting alignments of string $T$ in $S$ | $O(m \cdot n)$ | $O(n)$ | `TODO` |
| 133| Maximum Subarray (Kadane's) | Easy | `currentSum = Math.max(num, currentSum + num)` | $O(n)$ | $O(1)$ | `TODO` |
| 134| Maximal Square | Medium | `dp[r][c] = 1 + min(top, left, diagonal)` | $O(m \cdot n)$ | $O(n)$ | `TODO` |
| 135| Longest Palindromic Subsequence | Medium | 2D Interval DP expanding from inner substrings | $O(n^2)$ | $O(n^2)$ | `TODO` |

---

## Pattern 12: Intervals & Greedy (Problems 136–150)

| # | Problem | Difficulty | Key Pattern / Insight | Time | Space | Status |
|---|---|---|---|---|---|---|
| 136| Insert Interval | Medium | Merge into 3 phases: before, overlapping, after | $O(n)$ | $O(n)$ | `TODO` |
| 137| Merge Intervals | Medium | Sort by start time; merge overlapping intervals | $O(n \log n)$ | $O(n)$ | `TODO` |
| 138| Non-overlapping Intervals | Medium | Sort by end time; greedily retain intervals that finish earliest | $O(n \log n)$ | $O(1)$ | `TODO` |
| 139| Meeting Rooms | Easy | Sort by start time; check if `intervals[i][0] < intervals[i-1][1]` | $O(n \log n)$ | $O(1)$ | `TODO` |
| 140| Meeting Rooms II | Medium | Min-Heap storing meeting end times or two pointer timeline | $O(n \log n)$ | $O(n)$ | `TODO` |
| 141| Minimum Number of Arrows to Burst | Medium | Greedy interval overlap: shoot at earliest end point | $O(n \log n)$ | $O(1)$ | `TODO` |
| 142| Jump Game | Medium | Track furthest reachable index | $O(n)$ | $O(1)$ | `TODO` |
| 143| Jump Game II | Medium | BFS levels or greedy window updating current jump boundary | $O(n)$ | $O(1)$ | `TODO` |
| 144| Gas Station (Circular Tour) | Medium | If `totalGas >= totalCost`, start at index after lowest deficit | $O(n)$ | $O(1)$ | `TODO` |
| 145| Hand of Straights | Medium | TreeMap counting frequencies; greedily form groups of $W$ | $O(n \log n)$ | $O(n)$ | `TODO` |
| 146| Single Number | Easy | Bitwise XOR: $a \oplus a = 0$ | $O(n)$ | $O(1)$ | `TODO` |
| 147| Number of 1 Bits (Hamming Weight) | Easy | Brian Kernighan: `n & (n - 1)` clears lowest set bit | $O(\text{set bits})$ | $O(1)$ | `TODO` |
| 148| Counting Bits | Easy | DP bit relation: `dp[i] = dp[i >> 1] + (i & 1)` | $O(n)$ | $O(n)$ | `TODO` |
| 149| Reverse Bits | Easy | Bit shifting and masking 32 times | $O(1)$ | $O(1)$ | `TODO` |
| 150| Missing Number | Easy | XOR with all indices `0..n` or Gauss sum formula | $O(n)$ | $O(1)$ | `TODO` |
