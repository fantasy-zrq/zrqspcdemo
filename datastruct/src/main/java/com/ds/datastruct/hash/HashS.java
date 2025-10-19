package com.ds.datastruct.hash;

import java.util.*;

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
//        int[] res = sumOfTwoNumbers(new int[]{3, 3}, 6);
//        System.out.println("res = " + Arrays.toString(res));
    }

    public static boolean canConstruct(String ransomNote, String magazine) {
        int[] nums = new int[26];
        for (byte asc : ransomNote.getBytes()) {
            nums[asc - 'a'] += 1;
        }

        for (byte asc : magazine.getBytes()) {
            if (nums[asc - 'a'] != 0) {
                nums[asc - 'a'] -= 1;
            }
        }

        for (int num : nums) {
            if (num > 0) {
                return false;
            }
        }
        return true;
    }

    private static int addingFourNumbersII(int[] nums1, int[] nums2, int[] nums3, int[] nums4) {
        Map<Integer, Integer> map = new HashMap<>();
        for (int i : nums1) {
            for (int j : nums2) {
                if (map.containsKey(i + j)) {
                    map.put(i + j, map.get(i + j) + 1);
                } else {
                    map.put(i + j, 1);
                }
            }
        }
        // 5  3
        int count = 0;
        for (int i : nums3) {
            for (int j : nums4) {
                if (map.containsKey(-(i + j))) {
                    count += map.get(-(i + j));
                }
            }
        }
        return count;
    }

    private static int[] sumOfTwoNumbers(int[] nums, int target) {
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            if (map.containsKey(target - nums[i])) {
                return new int[]{map.get(target - nums[i]), i};
            }
            map.put(nums[i], i);
        }
        return null;
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
