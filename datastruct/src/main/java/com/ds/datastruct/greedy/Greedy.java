package com.ds.datastruct.greedy;

import java.util.Arrays;

/**
 * @author zrq
 * @time 2025/11/26 09:24
 * @description
 */
public class Greedy {
    public static void main(String[] args) {
        int[][] arr = {
                {1, 2},
                {1, 4},
                {5, 6},
                {7, 8},
                {9, 10},
                {11, 12}
        };
        Arrays.sort(arr, (ar1, ar2) -> {
            if (ar1[0] == ar2[0]) {
                return ar1[1] - ar2[1];
            }else{
                return ar1[0] - ar2[0];
            }
        });
        for (int[] ints : arr) {
            System.out.println("ints = " + Arrays.toString(ints));
        }
    }


    public int largestSumAfterKNegations(int[] nums, int k) {
        if (nums.length == 0) {
            return 0;
        }
        nums = Arrays.stream(nums).boxed().sorted((n1, n2) -> Math.abs(n2) - Math.abs(n1)).mapToInt(Integer::intValue).toArray();
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] < 0 && k > 0) {
                nums[i] = -nums[i];
            }
        }
        if (k % 2 == 1) {
            nums[nums.length - 1] = -nums[nums.length - 1];
        }
        return Arrays.stream(nums).sum();
    }

    public int findContentChildren(int[] g, int[] s) {
        int res = 0;
        int index = s.length - 1;
        Arrays.sort(g);
        Arrays.sort(s);
        for (int i = g.length - 1; i >= 0; i--) {
            while (index >= 0 && s[index] >= g[i]) {
                res++;
                index--;
            }
        }
        return res;
    }

    public int maxSubArray(int[] nums) {

        int windowSum = 0;
        int maxSum = nums[0];
        for (int num : nums) {
            // 扩展右指针
            windowSum += num;
            // 更新最大和
            maxSum = Math.max(maxSum, windowSum);

            // 如果当前和为负，窗口无意义，重置窗口
            if (windowSum < 0) {
                windowSum = 0;
            }
        }
        return maxSum;
    }
}
