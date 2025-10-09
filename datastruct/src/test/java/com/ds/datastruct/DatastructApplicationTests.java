package com.ds.datastruct;

import org.junit.jupiter.api.Test;

class DatastructApplicationTests {

    @Test
    void contextLoads() {
//        int res = binarySearch(new int[]{1, 5, 7, 9, 11, 78, 89}, 89);
//        System.out.println("res = " + res);
//        int length = removeElements(new int[]{1, 5, 7, 9, 9, 9, 89}, 9);
//        System.out.println("length = " + length);
        squaresOfOrderedArrays(new int[]{-86, -51, 16, 51, 86});
    }

    void squaresOfOrderedArrays(int[] nums) {
        for (int i = 0; i < nums.length; i++) {
            nums[i] = nums[i] * nums[i];
        }
        int[] res = new int[nums.length];
        int reStart = 0;
        int end = nums.length - 1;
        //-8, -5, 3, 4, 8
        for (int start = 0; start <= end; ) {
            if (nums[start] < nums[end]) {
                res[reStart++] = nums[end--];
            } else {
                res[reStart++] = nums[start++];
            }
        }

        for (int re : res) {
            System.out.println("re = " + re);
        }
    }

    int removeElements(int[] nums, int target) {
        int faster = 0, slow = 0;
        for (int i = 0; i < nums.length; i++) {
            if (nums[faster] != target) {
                slow++;
            }
            faster++;
        }
        return slow;
    }

    int binarySearch(int[] nums, int target) {
        int left = 0;
        int right = nums.length - 1;
        while (left <= right) {
            int mid = (right + left) >> 1;
            if (nums[mid] > target) {
                right--;
            } else if (nums[mid] < target) {
                left++;
            } else {
                return mid;
            }
        }
        return -1;
    }


}
