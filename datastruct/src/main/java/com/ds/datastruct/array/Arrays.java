package com.ds.datastruct.array;

import lombok.extern.slf4j.Slf4j;

/**
 * @author zrq
 * @time 2025/9/30 10:37
 * @description
 */
@Slf4j
public class Arrays {

    public static void main(String[] args) {
//        System.out.println(binarySearch(new int[]{1, 5, 7, 9, 11, 78, 89}, 9));
//        System.out.println(removeElements(new int[]{1, 5, 7, 9, 9, 9, 89}, 9));
//        squaresOfOrderedArrays(new int[]{-5, -1, 7, 9, 9, 9, 89});
        squaresOfOrderedArrays(new int[]{-10, -8, 2, 4, 6, 9, 12});
    }

    private static void squaresOfOrderedArrays(int[] nums) {
        for (int i = 0; i < nums.length; i++) {
            nums[i] = nums[i] * nums[i];
        }
        int[] arr = new int[nums.length];
        int k = nums.length - 1;
        for (int start = 0, end = nums.length - 1; start <= end; ) {
            if (nums[start] > nums[end]) {
                arr[k--] = nums[start];
                start++;
            } else {
                arr[k--] = nums[end];
                end--;
            }
        }
    }

    public static int removeElements(int[] nums, int val) {
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] == val) {
                nums[i] = -1;
            }
        }
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] == -1) {
                for (int j = i + 1; j < nums.length; j++) {
                    if (nums[j] != -1) {
                        nums[i] = nums[j];
                        nums[j] = -1;
                        break;
                    }
                }
            }
        }
        int size = 0;
        for (Integer integer : nums) {
            if (integer != -1) {
                size++;
            }
        }
        return size;
    }

    public static int removeElementsByDoublePointer(int[] nums, int val) {
        //fast代表需要插入数组的元素
        //slow代表需要更新的位置，也代表数组的长度
        int slow = 0;
        for (int fast = 0; fast < nums.length; fast++) {
            if (nums[fast] != val) {
                nums[slow] = nums[fast];
                slow++;
            }
        }
        return slow;
    }

    public static Integer binarySearch(int[] arr, int target) {
        int left = 0;
        int right = arr.length - 1;
        while (left <= right) {
            int mid = (right + left) >> 1;
            if (arr[mid] < target) {
                left = mid + 1;
            } else if (target < arr[mid]) {
                right = mid - 1;
            } else {
                return mid;
            }
        }
        return -1;
    }
}
