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
//        String res = reverseWords("a good   example");
//        System.out.println("res = " + res);

//        int[] next = getNext("abcabd");
//        for (int i : next) {
//            System.out.println("i = " + i);
//        }
//
//        int index = strStr("mississippi", "issip");
//
//        System.out.println("index = " + index);
//        boolean res = repeatedSubstringPattern("aba");

        String s = reverseWords("a good   example");
        System.out.println("s = " + s);
    }

    public static boolean repeatedSubstringPattern(String s) {
        String string = s + s;
        int[] next = getNext(s);
        for (int i = 1, j = 0; i < string.length() - 1; i++) {
            while (j > 0 && string.charAt(i) != s.charAt(j)) {
                j = next[j - 1];
            }
            if(string.charAt(i) == s.charAt(j)){
                j++;
            }
            if(j == s.length()){
                return true;
            }
        }
        return false;
    }

    private static int strStr(String haystack, String needle) {
        int[] next = getNext(needle);
        for (int j = 0, i = 0; i < haystack.length(); i++) {
            while (j > 0 && haystack.charAt(i) != needle.charAt(j)) {
                j = next[j - 1];
            }
            if (haystack.charAt(i) == needle.charAt(j)) {
                j++;
            }
            if (j == needle.length()) {
                return i - j + 1;
            }
        }
        return -1;
    }

    private static int[] getNext(String str) {
        char[] charArray = str.toCharArray();
        int[] next = new int[charArray.length];
        next[0] = 0;
        for (int j = 0, i = 1; i < charArray.length; i++) {
            if (j > 0 && charArray[i] != charArray[j]) {
                j = next[j - 1];
            }
            if (charArray[i] == charArray[j]) {
                j++;
            }
            next[i] = j;
        }
        return next;
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
