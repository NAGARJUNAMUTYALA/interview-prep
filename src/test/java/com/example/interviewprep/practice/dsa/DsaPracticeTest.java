package com.example.interviewprep.practice.dsa;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Core Data Structures & Algorithms Practice: High-Frequency Patterns")
class DsaPracticeTest {

    // ─── Pattern 1: Two Pointers ────────────────────────────────────────────────

    @Test
    @DisplayName("Two Pointers: Two Sum on sorted array")
    void testTwoSumSorted() {
        int[] numbers = {2, 7, 11, 15};
        int target = 9;

        int left = 0, right = numbers.length - 1;
        int[] result = new int[2];
        while (left < right) {
            int sum = numbers[left] + numbers[right];
            if (sum == target) {
                result[0] = left;
                result[1] = right;
                break;
            } else if (sum < target) {
                left++;
            } else {
                right--;
            }
        }

        assertArrayEquals(new int[]{0, 1}, result);
    }

    // ─── Pattern 2: Sliding Window ──────────────────────────────────────────────

    @Test
    @DisplayName("Sliding Window: Longest Substring Without Repeating Characters")
    void testLongestSubstringWithoutRepeating() {
        String s = "abcabcbb";
        Map<Character, Integer> lastSeen = new HashMap<>();
        int maxLength = 0;
        int windowStart = 0;

        for (int windowEnd = 0; windowEnd < s.length(); windowEnd++) {
            char rightChar = s.charAt(windowEnd);
            if (lastSeen.containsKey(rightChar)) {
                windowStart = Math.max(windowStart, lastSeen.get(rightChar) + 1);
            }
            lastSeen.put(rightChar, windowEnd);
            maxLength = Math.max(maxLength, windowEnd - windowStart + 1);
        }

        assertEquals(3, maxLength); // "abc"
    }

    // ─── Pattern 3: Monotonic Stack ─────────────────────────────────────────────

    @Test
    @DisplayName("Monotonic Stack: Next Greater Element")
    void testNextGreaterElement() {
        int[] nums = {4, 5, 2, 25};
        int[] nextGreater = new int[nums.length];
        Arrays.fill(nextGreater, -1);
        Deque<Integer> stack = new ArrayDeque<>(); // stores indices

        for (int i = 0; i < nums.length; i++) {
            while (!stack.isEmpty() && nums[stack.peek()] < nums[i]) {
                nextGreater[stack.pop()] = nums[i];
            }
            stack.push(i);
        }

        assertArrayEquals(new int[]{5, 25, 25, -1}, nextGreater);
    }

    // ─── Pattern 4: Binary Search in Rotated Sorted Array ───────────────────────

    @Test
    @DisplayName("Binary Search: Search in Rotated Sorted Array")
    void testSearchInRotatedSortedArray() {
        int[] nums = {4, 5, 6, 7, 0, 1, 2};
        int target = 0;

        int low = 0, high = nums.length - 1;
        int foundIndex = -1;

        while (low <= high) {
            int mid = low + (high - low) / 2;
            if (nums[mid] == target) {
                foundIndex = mid;
                break;
            }
            // Check if left half is sorted
            if (nums[low] <= nums[mid]) {
                if (target >= nums[low] && target < nums[mid]) {
                    high = mid - 1;
                } else {
                    low = mid + 1;
                }
            } else { // Right half is sorted
                if (target > nums[mid] && target <= nums[high]) {
                    low = mid + 1;
                } else {
                    high = mid - 1;
                }
            }
        }

        assertEquals(4, foundIndex);
    }

    // ─── Pattern 5: Tree BFS (Level Order Traversal) ─────────────────────────────

    static class TreeNode {
        int val;
        TreeNode left, right;
        TreeNode(int val) { this.val = val; }
    }

    @Test
    @DisplayName("Tree BFS: Level Order Traversal")
    void testLevelOrderTraversal() {
        TreeNode root = new TreeNode(3);
        root.left = new TreeNode(9);
        root.right = new TreeNode(20);
        root.right.left = new TreeNode(15);
        root.right.right = new TreeNode(7);

        List<List<Integer>> levels = new ArrayList<>();
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);

        while (!queue.isEmpty()) {
            int levelSize = queue.size();
            List<Integer> currentLevel = new ArrayList<>();
            for (int i = 0; i < levelSize; i++) {
                TreeNode current = queue.poll();
                currentLevel.add(current.val);
                if (current.left != null) queue.offer(current.left);
                if (current.right != null) queue.offer(current.right);
            }
            levels.add(currentLevel);
        }

        assertEquals(3, levels.size());
        assertEquals(List.of(3), levels.get(0));
        assertEquals(List.of(9, 20), levels.get(1));
        assertEquals(List.of(15, 7), levels.get(2));
    }

    // ─── Pattern 6: Dynamic Programming (Coin Change) ───────────────────────────

    @Test
    @DisplayName("Dynamic Programming: Minimum Coins for Target Amount")
    void testCoinChange() {
        int[] coins = {1, 2, 5};
        int amount = 11;

        int[] dp = new int[amount + 1];
        Arrays.fill(dp, amount + 1);
        dp[0] = 0;

        for (int i = 1; i <= amount; i++) {
            for (int coin : coins) {
                if (i - coin >= 0) {
                    dp[i] = Math.min(dp[i], dp[i - coin] + 1);
                }
            }
        }

        int minCoins = dp[amount] > amount ? -1 : dp[amount];
        assertEquals(3, minCoins, "11 = 5 + 5 + 1 (3 coins)");
    }
}
