package com.ds.datastruct.backtrace;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author zrq
 * @time 2025/11/23 11:14
 * @description
 */
public class BackTrace2 {
    public static void main(String[] args) {

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
