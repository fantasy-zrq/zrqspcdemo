package com.ds.datastruct.greedy;

import java.util.Arrays;

/**
 * @author zrq
 * @time 2025/11/26 09:24
 * @description
 */
public class Greedy {
    public static void main(String[] args) {
    
    }
    public int findContentChildren(int[] g, int[] s) {
        int res = 0;
        int index = s.length-1;
        Arrays.sort(g);
        Arrays.sort(s);
        for(int i = g.length -1;i>=0;i--){
            while(index >= 0 && s[index] >= g[i]){
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
