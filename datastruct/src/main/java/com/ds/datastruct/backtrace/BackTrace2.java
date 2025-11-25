package com.ds.datastruct.backtrace;

import java.util.*;

/**
 * @author zrq
 * @time 2025/11/23 11:14
 * @description
 */
public class BackTrace2 {
    public static void main(String[] args) {

    }

    List<List<String>> res6 = new ArrayList<>();

    public List<List<String>> solveNQueens(int n) {
        char[][] chessboard = new char[n][n];
        for (char[] chars : chessboard) {
            Arrays.fill(chars, '.');
        }
        backtrace6(chessboard, 0, n);
        return res6;
    }

    private void backtrace6(char[][] chessboard, int row, int n) {
        if (row == n) {
            List<String> list = new ArrayList<>();
            for (char[] c : chessboard) {
                list.add(String.copyValueOf(c));
            }
            res6.add(list);
            return;
        }
        for (int clo = 0; clo < n; clo++) {
            if (isValid(row, clo, chessboard, n)) {
                chessboard[row][clo] = 'Q';
                backtrace6(chessboard, row + 1, n);
                chessboard[row][clo] = '.';
            }
        }
    }

    private boolean isValid(int row, int clo, char[][] chessboard, int n) {
        for (int i = 0; i < row; i++) {
            if (chessboard[i][clo] == 'Q') {
                return false;
            }
        }
        for (int i = row - 1, j = clo - 1; i >= 0 && j >= 0; i--, j--) {
            if (chessboard[i][j] == 'Q') {
                return false;
            }
        }

        for (int i = row - 1, j = clo + 1; i >= 0 && j <= n - 1; i--, j++) {
            if (chessboard[i][j] == 'Q') {
                return false;
            }
        }
        return true;
    }

    List<List<Integer>> res5 = new ArrayList<>();
    LinkedList<Integer> path5 = new LinkedList<>();
    Boolean[] used5 = null;

    public List<List<Integer>> permuteUnique(int[] nums) {
        if (nums == null) {
            return res5;
        }
        backtrace5(nums);
        return res5;
    }

    private void backtrace5(int[] nums) {
        if (path5.size() == nums.length) {
            res5.add(new ArrayList<>(path5));
            return;
        }
        for (int i = 0; i < nums.length; i++) {
            if (i > 0 && nums[i] == nums[i - 1] && !used5[i - 1]) {
                continue;
            }
            if (!used5[i]) {
                path5.add(nums[i]);
                used5[i] = true;
                backtrace5(nums);
                used5[i] = false;
                path5.removeLast();
            }
        }
    }

    List<List<Integer>> res4 = new ArrayList<>();
    LinkedList<Integer> path4 = new LinkedList<>();
    Boolean[] used4 = null;

    public List<List<Integer>> permute(int[] nums) {
        if (nums == null) {
            return res4;
        }
        used4 = new Boolean[nums.length];
        backtrace4(nums);
        return res4;
    }

    private void backtrace4(int[] nums) {
        if (path4.size() == nums.length) {
            res4.add(new ArrayList<>(path4));
            return;
        }
        for (int i = 0; i < nums.length; i++) {
            if (used4[i]) {
                continue;
            }
            used4[i] = true;
            path4.add(nums[i]);
            backtrace4(nums);
            used4[i] = false;
            path4.removeLast();
        }
    }

    List<List<Integer>> res3 = new ArrayList<>();
    LinkedList<Integer> path3 = new LinkedList<>();

    public List<List<Integer>> findSubsequences(int[] nums) {
        if (nums == null) {
            return res3;
        }
        backtrace3(nums, 0);
        return res3;
    }

    private void backtrace3(int[] nums, int startIndex) {
        if (path3.size() >= 2) {
            res3.add(new ArrayList<>(path3));
        }
        Set<Integer> set = new HashSet<>();
        for (int i = startIndex; i < nums.length; i++) {
            if (set.contains(nums[i]) || !path3.isEmpty() && path3.get(path3.size() - 1) > nums[i]) {
                continue;
            }
            set.add(nums[i]);
            path3.add(nums[i]);
            backtrace3(nums, i + 1);
            path3.removeLast();
        }
    }

    List<List<Integer>> res2 = new ArrayList<>();
    LinkedList<Integer> path2 = new LinkedList<>();
    boolean[] used = null;

    public List<List<Integer>> subsetsWithDup(int[] nums) {
        if (nums == null) {
            return res2;
        }
        used = new boolean[nums.length];
        Arrays.fill(used, false);
        backtrace2(nums, 0);
        return res2;
    }

    private void backtrace2(int[] nums, int startIndex) {
        res2.add(new ArrayList<>(path2));
        if (startIndex == nums.length) {
            return;
        }
        for (int i = startIndex; i < nums.length; i++) {
            if (i > 0 && nums[i] == nums[i - 1] && !used[i]) {
                continue;
            }
            path2.add(nums[i]);
            used[i] = true;
            backtrace2(nums, i + 1);
            used[i] = false;
            path2.removeLast();
        }
    }

    List<List<Integer>> res1 = new ArrayList<>();
    LinkedList<Integer> path1 = new LinkedList<>();

    public List<List<Integer>> subsets(int[] nums) {
        if (nums == null) {
            return res1;
        }
        backtrace1(nums, 0);
        return res1;
    }

    private void backtrace1(int[] nums, int startIndex) {
        res1.add(new ArrayList<>(path1));
        if (startIndex >= nums.length) {
            return;
        }
        for (int i = startIndex; i < nums.length; i++) {
            path1.add(nums[i]);
            backtrace1(nums, i + 1);
            path1.removeLast();
        }
    }
}
