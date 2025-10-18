package com.ds.datastruct.hash;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author zrq
 * @time 2025/10/18 14:27
 * @description
 */
public class HashS {
    public static void main(String[] args) {
//        boolean res = validAllophones("anagram", "nagaram");
//        System.out.println("res = " + res);
//        int[] res = intersectionOfTwoArrays(new int[]{1, 2, 2, 1}, new int[]{2, 2});
//        for (int re : res) {
//            System.out.println("re = " + re);
//        }
        boolean res = happyNumber(19);
        System.out.println(2147483648L / 1000000000);
    }

    private static boolean happyNumber(int n) {
        Set<Integer> set = new HashSet<>();
        while (n != 1 && !set.contains(n)) {
            set.add(n);
            int res = 0;
            while (n > 0) {
                int temp = n % 10;
                res += temp * temp;
                n /= 10;
            }
            n = res;
        }
        return n == 1;
    }

    private static int[] intersectionOfTwoArrays(int[] nums1, int[] nums2) {


//        HashSet<Integer> set1 = new HashSet<>();
//        HashSet<Integer> set2 = new HashSet<>();
//        for (int i : nums1) {
//            set1.add(i);
//        }
//
//        for (int i : nums2) {
//            if (set1.contains(i)) {
//                set2.add(i);
//            }
//        }
//
//        return set2.stream().mapToInt(Integer::intValue).toArray();
        int[] hash1 = new int[1001];
        int[] hash2 = new int[1001];

        for (int i : nums1) {
            hash1[i] += 1;
        }
        for (int i : nums2) {
            hash2[i] += 1;
        }

        List<Integer> res = new ArrayList<>();
        for (int i = 0; i < 1001; i++) {
            if (hash1[i] > 0 && hash2[i] > 0) {
                res.add(i);
            }
        }

        return res.stream().mapToInt(Integer::intValue).toArray();
    }


    private static boolean validAllophones(String s, String t) {
        if (s.length() < t.length()) {
            String temp;
            temp = s;
            s = t;
            t = temp;
        }
        int[] hash = new int[26];
        byte[] bytes = s.getBytes();
        for (byte aByte : bytes) {
            if (hash[aByte - 97] != 0) {
                hash[aByte - 97] += 1;
            } else {
                hash[aByte - 97] = 1;
            }
        }

        byte[] bytet = t.getBytes();
        for (byte b : bytet) {
            if (hash[b - 97] != 0) {
                hash[b - 97] -= 1;
            }
        }
        int cont = 0;
        for (int i : hash) {
            if (i == 0) {
                cont++;
            }
        }
        return cont == 26;
    }
}
