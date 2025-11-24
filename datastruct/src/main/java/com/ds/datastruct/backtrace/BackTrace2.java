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
