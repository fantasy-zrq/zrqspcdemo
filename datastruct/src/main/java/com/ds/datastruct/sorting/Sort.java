package com.ds.datastruct.sorting;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

/**
 * @author zrq
 * @time 2025/9/27 17:06
 * @description
 */
@Slf4j
public class Sort {
    public static void main(String[] args) {
//        bubbleSort(new int[]{6, 5, 7, 9, 1, 2, 311, 1, 8});
//        selectSort(new int[]{6, 5, 7, 9, 1, 2, 311, 1, 8});
//        insertSort(new int[]{6, 5, 7, 9, 1, 2, 311, 1, 8});
//        shellSort(new int[]{6, 5, 7, 9, 1, 2, 311, 1, 8, 56, 13});
        //                  1  2  7  1  6  5  311  9  8
        System.out.println(factorial(4));
    }

    public static void bubbleSort(int[] arr) {
//        for (int j = arr.length - 1; j > 0; j--) {
//            for (int i = 0; i < j; i++) {
//                if (arr[i] > arr[i + 1]) {
//                    int temp;
//                    temp = arr[i];
//                    arr[i] = arr[i + 1];
//                    arr[i + 1] = temp;
//                }
//            }
//        }
//        outPut(arr);

        for (int i = 1; i < arr.length; i++) {
            for (int j = 0; j < arr.length - i; j++) {
                if (arr[j] < arr[j + 1]) {
                    int temp;
                    temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                }
            }
        }
        outPut(arr);
    }

    public static void selectSort(int[] arr) {
        for (int i = 0; i < arr.length - 1; i++) {
            int min = arr[i];
            for (int j = i + 1; j < arr.length; j++) {
                if (min > arr[j]) {
                    int temp;
                    temp = arr[i];
                    arr[i] = arr[j];
                    arr[j] = temp;
                    min = arr[i];
                }
            }
        }
//        outPut(arr);
    }

    public static void insertSort(int[] arr) {
        //记录当前元素索引
        for (int i = 1; i < arr.length; i++) {
            //记录前一个元素索引
            for (int j = i - 1; j >= 0; j--) {
                if (arr[i] < arr[j]) {
                    int temp;
                    temp = arr[i];
                    arr[i] = arr[j];
                    arr[j] = temp;
                    //将i的值变为i的前一个值，来保证一致是与前一个值进比较
                    i = j;
                }
            }
        }
        outPut(arr);
    }

    public static void shellSort(int[] arr) {
        for (int gap = arr.length / 2; gap > 0; gap /= 2) {
            //每一组的需要排序的起始位置
            for (int i = gap; i < arr.length; i++) {
                //找出该组的所有元素，进行插入排序
                for (int j = i; j >= gap; j -= gap) {
                    if (arr[j] < arr[j - gap]) {
                        int temp;
                        temp = arr[j];
                        arr[j] = arr[j - gap];
                        arr[j - gap] = temp;
                    }
                }
            }
        }
        outPut(arr);
    }

    public static Integer factorial(int num) {
        if (num == 1) {
            return 1;
        }
        return num * factorial(num - 1);
    }

    private static void outPut(int[] arr) {
        Arrays.stream(arr).forEach(System.out::println);
    }
}
