package com.ds.datastruct.str;

/**
 * @author zrq
 * @time 2025/10/22 14:36
 * @description
 */
public class Strings {
    public static void main(String[] args) {
//        reverseString(new char[]{'z', 'r', 'q', 'h', 'h'});
//        String res = reverseStr("abcdefg", 2);
//        System.out.println("res = " + res);
        String res = reverseWords("a good   example");
        System.out.println("res = " + res);
    }

    public static String reverseWords(String s) {
        String[] split = s.trim().split(" ");

        int num = 0;
        for (String string : split) {
            if (!string.isBlank()) {
                num++;
            }
        }
        String[] arr = new String[num];
        int i = 0;
        for (String string : split) {
            if (!string.isBlank()) {
                arr[i++] = string;
            }
        }
        StringBuilder builder = new StringBuilder();
        for (int j = arr.length - 1; j >= 0; j--) {
            if (j == 0) {
                builder.append(arr[j]);
                break;
            }
            builder.append(arr[j]).append(" ");
        }
        return builder.toString();
    }

    public static void reverseString(char[] s) {
        int left = 0;
        int right = s.length - 1;
        while (left < right) {
            char temp;
            temp = s[left];
            s[left] = s[right];
            s[right] = temp;
            left++;
            right--;
        }
        for (char c : s) {
            System.out.println("c = " + c);
        }
    }

    public static String reverseStr(String s, int k) {
        char[] charArray = s.toCharArray();
        for (int i = 0; i < charArray.length; i += 2 * k) {
            if (charArray.length - i <= k) {
                reverse(charArray, i, charArray.length - 1);
            } else {
                reverse(charArray, i, i + k - 1);
            }
        }
        return new String(charArray);
    }

    private static void reverse(char[] charArray, int start, int end) {
        while (start < end) {
            char temp;
            temp = charArray[start];
            charArray[start] = charArray[end];
            charArray[end] = temp;
            start++;
            end--;
        }
    }
}
