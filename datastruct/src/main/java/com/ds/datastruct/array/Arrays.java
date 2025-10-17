package com.ds.datastruct.array;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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
        squaresOfOrderedArrays(new int[]{-100, -8, 2, 4, 6, 9, 12});
//        minimumSubarrayLength(new int[]{5, 1, 3, 5, 10, 7, 4, 2, 2, 1}, 15);
//        spiralMatrix59(5);
//        List<Integer> list = spiralMatrix54(new int[][]{{1, 2, 3, 4}, {5, 6, 7, 8}, {9, 10, 11, 12}});
//        List<Integer> list = spiralMatrix54(new int[][]{{1, 2, 3, 4}, {5, 6, 7, 8}, {9, 10, 11, 12}, {13, 14, 15, 16}});
//        List<Integer> list = spiralMatrix54(new int[][]{{1,  2,  3}, {5,  6,  7}, {9, 10, 11},{12, 13, 14}});
//        List<Integer> list = spiralMatrix54(new int[][]{{1, 2, 3}, {5, 6, 7}, {9, 10, 11}});
//        list.forEach(System.out::println);
//        int[][] res = spiralMatrixNew59(5);
//        for (int[] re : res) {
//            System.out.println(java.util.Arrays.toString(re));
//        }
//        intervalSum();
//        int i = searchForInsertionPosition(new int[]{1, 2, 6, 7, 9}, 8);
//        System.out.println("i = " + i);
//        int[] res = findInitialAndEndPositions(new int[]{1, 2, 6, 7, 7, 9}, 7);
    }

    private static int[] findInitialAndEndPositions(int[] nums, int target) {
        //  {1, 2, 6, 7, 7, 9}, 7
        Integer index = binarySearch(nums, target);
        if (index == -1) {
            return new int[]{-1, -1};
        }
        int left = index;
        int right = index;
        while (left - 1 >= 0 && nums[left] == nums[left - 1]) {
            left--;
        }
        while (right + 1 < nums.length && nums[right] == nums[right + 1]) {
            right++;
        }
        return new int[]{left, right};
    }

    private static int searchForInsertionPosition(int[] nums, int target) {
        // 1, 2, 6, 7, 9, 10, 12, 19
        //          mid
        // L  m  R(P)
        int left = 0;
        int right = nums.length - 1;
        while (left <= right) {
            int mid = (left + right) >> 1;
            if (target > nums[mid]) {
                left = ++mid;
            } else if (target < nums[mid]) {
                right = --mid;
            } else {
                return mid;
            }
        }
        return right + 1;
    }

    private static void intervalSum() {
        Scanner scanner = new Scanner(System.in);

        int arrLength = scanner.nextInt();
        int[] arr = new int[arrLength];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = scanner.nextInt();
        }
        scanner.nextLine();
        String interval = scanner.nextLine();
        String[] s = interval.split(" ");
        int left = Integer.parseInt(s[0]), right = Integer.parseInt(s[1]);
        int sum = 0;
        for (int i = left; i <= right; i++) {
            sum += arr[i];
        }
        System.out.println(sum);
    }

    /**
     * 新思路，以四条边线作为边界条件
     */
    private static List<Integer> spiralMatrix54(int[][] matrix) {
        int left = 0, top = 0;
        int right = matrix[0].length - 1, bottom = matrix.length - 1;
        List<Integer> list = new ArrayList<>();
        while (left <= right && top <= bottom) {
            for (int j = left; j <= right; j++) {
                list.add(matrix[top][j]);
            }
            top++;

            for (int i = top; i <= bottom; i++) {
                list.add(matrix[i][right]);
            }
            right--;
            if (top <= bottom) {
                for (int j = right; j >= left; j--) {
                    list.add(matrix[bottom][j]);
                }
                bottom--;
            }

            if (left <= right) {
                for (int i = bottom; i >= top; i--) {
                    list.add(matrix[i][left]);
                }
                left++;
            }
        }
        return list;
    }

    private static List<Integer> spiralMatrix54chart(int[][] matrix) {
        //matrix是m*n
        /*
           1,  2,  3,  4
           5,  6,  7,  8
           9,  10, 11, 12
           13, 14, 15, 16

           1,  2,  3
           5,  6,  7
           9,  10, 11
           12, 13, 14
         */
//        int lengthi = matrix.length;
//        int widthj = matrix[0].length;
//        int offseti = 1;
//        int offsetj = 1;
//        int starti = 0, startj = 0;
//        List<Integer> list = new ArrayList<>();
//        while (list.size() < lengthi * widthj) {
//            for (; startj < widthj - offsetj; startj++) {
//                list.add(matrix[starti][startj]);
//            }
//            for (; starti < lengthi - offseti; starti++) {
//                list.add(matrix[starti][startj]);
//            }
//            for (; startj >= offsetj; startj--) {
//                list.add(matrix[starti][startj]);
//            }
//            for (; starti >= offseti; starti--) {
//                list.add(matrix[starti][startj]);
//            }
//            if (lengthi == widthj && offseti == offsetj && offsetj == (lengthi / 2) + 1 && lengthi % 2 == 1) {
//                list.add(matrix[starti][startj]);
//                break;
//            }
//            offseti++;
//            offsetj++;
//            starti++;
//            startj++;
//        }
//        System.out.println("list.size() = " + list.size());
//
//        return list;

        List<Integer> res = new ArrayList<>();
        if (matrix == null || matrix.length == 0) {
            return res;
        }

        int m = matrix.length;
        int n = matrix[0].length;
        int top = 0, bottom = m - 1;
        int left = 0, right = n - 1;

        while (top <= bottom && left <= right) {
            // 1️⃣ 从左到右
            for (int j = left; j <= right; j++) {
                res.add(matrix[top][j]);
            }
            top++;

            // 2️⃣ 从上到下
            for (int i = top; i <= bottom; i++) {
                res.add(matrix[i][right]);
            }
            right--;

            // 3️⃣ 从右到左（确保还有行）
            if (top <= bottom) {
                for (int j = right; j >= left; j--) {
                    res.add(matrix[bottom][j]);
                }
                bottom--;
            }

            // 4️⃣ 从下到上（确保还有列）
            if (left <= right) {
                for (int i = bottom; i >= top; i--) {
                    res.add(matrix[i][left]);
                }
                left++;
            }
        }

        return res;
    }

    private static int[][] spiralMatrixNew59(int n) {
        //matrix是n*n
        /**
         *      1    2   3   4
         *      12   13  14  5
         *      11   16  15  6
         *      10   9   8   7
         */
        int left = 0, top = 0, count = 1;
        int[][] matrix = new int[n][n];
        int right = matrix[0].length - 1, bottom = matrix.length - 1;
        while (left <= right && top <= bottom) {
            for (int j = left; j <= right; j++) {
                matrix[top][j] = count++;
            }
            top++;
            for (int i = top; i <= bottom; i++) {
                matrix[i][right] = count++;
            }
            right--;
            if (top <= bottom) {
                for (int j = right; j >= left; j--) {
                    matrix[bottom][j] = count++;
                }
                bottom--;
            }
            if (left <= right) {
                for (int i = bottom; i >= top; i--) {
                    matrix[i][left] = count++;
                }
                left++;
            }
        }
        return matrix;
    }

    private static int[][] spiralMatrix59(int n) {
        //matrix是n*n
        /**
         *      1    2   3   4
         *      12   13  14  5
         *      11   16  15  6
         *      10   9   8   7
         */

        int loop = 1;
        int offset = 1;
        int starti = 0, startj = 0;
        int count = 1;
        int[][] arr = new int[n][n];
        //每一条边都只处理起始边界
        while (loop <= n / 2) {
            //顶
            for (; startj < n - offset; startj++) {
                arr[starti][startj] = count++;
            }
            //右
            for (; starti < n - offset; starti++) {
                arr[starti][startj] = count++;
            }
            //下
            for (; startj >= offset; startj--) {
                arr[starti][startj] = count++;
            }
            //左
            for (; starti >= offset; starti--) {
                arr[starti][startj] = count++;
            }
            loop++;
            offset++;
            starti++;
            startj++;
        }
        if (n % 2 == 1) {
            arr[starti][startj] = count;
        }
        return arr;
    }

    private static int minimumSubarrayLength(int[] nums, int target) {
//        int sum = 0;
//        int start = 0;
//        int length = Integer.MAX_VALUE;
//        for (int right = 0; right < nums.length; right++) {
//            sum += nums[right];
//            while (sum >= target) {
//                //用于获取全局最小的连续数组长度，假如是length = right - start + 1
//                //这样的写法，只能获取最后一个连续数组大于target的长度，而非全局最小
//                length = Integer.min(length, right - start + 1);
//                sum -= nums[start++];
//            }
//        }
//        return length == Integer.MAX_VALUE ? 0 : length;

        int sum = 0;
        int start = 0;
        int end = 0;
        int length = Integer.MAX_VALUE;
        for (; end < nums.length; end++) {
            sum += nums[end];
            while (sum >= target) {
                sum -= nums[start++];
                length = Integer.min(length, end - start + 2);
            }
        }
        return length == Integer.MAX_VALUE ? 0 : length;
    }

    private static void squaresOfOrderedArrays(int[] nums) {
//        for (int i = 0; i < nums.length; i++) {
//            nums[i] = nums[i] * nums[i];
//        }
//        int[] arr = new int[nums.length];
//        int k = nums.length - 1;
//        for (int start = 0, end = nums.length - 1; start <= end; ) {
//            if (nums[start] > nums[end]) {
//                arr[k--] = nums[start];
//                start++;
//            } else {
//                arr[k--] = nums[end];
//                end--;
//            }
//        }

        for (int i = 0; i < nums.length; i++) {
            nums[i] = nums[i] * nums[i];
        }

        int letf = 0;
        int right = nums.length - 1;
        int p = nums.length - 1;
        int[] arr = new int[nums.length];
        while (letf <= right) {
            if (nums[letf] < nums[right]) {
                arr[p--] = nums[right--];
            } else {
                arr[p--] = nums[letf++];
            }
        }

        for (int re : arr) {
            System.out.println("re = " + re);
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

    public static int[] spiralArray(int[][] array) {
        int left = 0, top = 0, count = 0;

        if (array == null || array.length == 0) {
            return new int[]{};
        }

        int right = array[0].length - 1, bottom = array.length - 1;
        int[] res = new int[array[0].length * array.length];
        while (left <= right && top <= bottom) {
            for (int j = left; j <= right; j++) {
                res[count++] = array[top][j];
            }
            top++;
            for (int i = top; i <= bottom; i++) {
                res[count++] = array[i][right];
            }
            right--;

            if (top <= bottom) {
                for (int j = right; j >= left; j--) {
                    res[count++] = array[bottom][j];
                }
                bottom--;
            }

            if (left <= right) {
                for (int i = bottom; i >= top; i--) {
                    res[count++] = array[i][left];
                }
                left++;
            }
        }
        return res;
    }
}
