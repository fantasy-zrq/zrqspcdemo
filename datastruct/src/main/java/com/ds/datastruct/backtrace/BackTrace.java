package com.ds.datastruct.backtrace;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * @author zrq
 * @time 2025/11/21 10:20
 * @description
 */
public class BackTrace {
    public static void main(String[] args) {
    }

    List<String> restoreIpAddressesResult = new ArrayList<>();
    StringBuilder sb;

    public List<String> restoreIpAddresses(String s) {
        if (s.isEmpty() || s.isBlank()) {
            return restoreIpAddressesResult;
        }
        sb = new StringBuilder(s);
        m5(s, 0, 0);
        return restoreIpAddressesResult;
    }

    private void m5(String s, int startIndex, int dotCount) {
        if (dotCount == 3) {
            if (isValidIp(startIndex, sb.length() - 1)) {
                restoreIpAddressesResult.add(sb.toString());
            }
        }
        for (int i = startIndex; i < s.length(); i++) {
            if (isValidIp(startIndex, i)) {
                sb.insert(i + 1, ".");
                m5(s, i + 2, dotCount + 1);
                sb.deleteCharAt(i + 1);
            } else {
                break;
            }
        }
    }

    private boolean isValidIp(int startIndex, int endIndex) {
        if (startIndex > endIndex) {
            return false;
        }
        if (sb.charAt(startIndex) == '0' && startIndex != endIndex) {
            return false;
        }
        int num = 0;
        for (int i = startIndex; i <= endIndex; i++) {
            int digit = sb.charAt(i) - '0';
            num = num * 10 + digit;
            if (num > 255)
                return false;
        }
        return true;
    }

    List<List<String>> partitionResult = new ArrayList<>();
    LinkedList<String> partitionList = new LinkedList<>();

    public List<List<String>> partition(String s) {
        if (s.isEmpty() || s.isBlank()) {
            return partitionResult;
        }
        m4(s, 0);
        return partitionResult;
    }

    private void m4(String s, int index) {
        if (index == s.length()) {
            partitionResult.add(new ArrayList<>(partitionList));
            return;
        }
        for (int i = index; i < s.length(); i++) {
            if (isPalindrome(s, index, i)) {
                partitionList.add(s.substring(index, i + 1)); // 使用 substring
                m4(s, i + 1);
                partitionList.removeLast();
            }
        }
    }

    private boolean isPalindrome(String s, int index, int end) {
        while (index < end) {
            if (s.charAt(index) != s.charAt(end)) return false;
            index++;
            end--;
        }
        return true;
    }

    List<List<Integer>> combinationSum2List = new ArrayList<>();
    LinkedList<Integer> tempSum2 = new LinkedList<>();
    Boolean[] used;
    Integer sum2 = 0;

    public List<List<Integer>> combinationSum2(int[] candidates, int target) {
        if (candidates == null) {
            return combinationList;
        }
        used = new Boolean[candidates.length];
        Arrays.fill(used, false);
        Arrays.sort(candidates);
        m3(candidates, target, 0);
        return combinationList;
    }

    private void m3(int[] candidates, int target, int index) {
        if (sum2 == target) {
            combinationSum2List.add(new ArrayList<>(tempSum2));
        }
        for (int i = index; i < candidates.length; i++) {
            if (sum2 + candidates[i] > target) {
                break;
            }
            if (i > 0 && candidates[i] == candidates[i - 1] && !used[i - 1]) {
                continue;
            }
            tempSum2.add(candidates[i]);
            sum2 += candidates[i];
            used[i] = true;
            m3(candidates, target, i + 1);
            used[i] = false;
            sum2 -= candidates[i];
            tempSum2.removeLast();
        }
    }

    List<List<Integer>> combinationList = new ArrayList<>();
    LinkedList<Integer> temp = new LinkedList<>();
    Integer sum = 0;

    public List<List<Integer>> combinationSum(int[] candidates, int target) {
        if (candidates == null) {
            return combinationList;
        }
        Arrays.sort(candidates);
        m2(candidates, target, 0);
        return combinationList;
    }

    private void m2(int[] candidates, int target, int index) {
        if (sum > target) {
            return;
        }
        if (sum == target) {
            combinationList.add(new ArrayList<>(temp));
            return;
        }
        for (int i = index; i < candidates.length; i++) {
            if (sum + candidates[i] > target) {
                break;
            }
            temp.add(candidates[i]);
            sum += candidates[i];
            m2(candidates, target, i);
            sum -= candidates[i];
            temp.removeLast();
        }
    }

    List<String> result = new ArrayList<>();
    StringBuilder builder = new StringBuilder();

    public List<String> letterCombinations(String digits) {
        if (digits == null || digits.isEmpty()) {
            return result;
        }
        String[] map = new String[]{"", "", "abc", "def", "ghi", "jkl", "mno", "pqrs", "tuv", "wxyz"};
        m1(map, digits, 0);
        return result;
    }

    private void m1(String[] map, String digits, int index) {
        if (builder.length() == digits.length()) {
            result.add(builder.toString());
            return;
        }
        String str = map[digits.charAt(index) - '0'];
        for (int i = 0; i < str.length(); i++) {
            builder.append(str.charAt(i));
            m1(map, digits, index + 1);
            builder.deleteCharAt(builder.length() - 1);
        }
    }

    public List<List<Integer>> combinationSum3(int k, int n) {
        backTracingSum3(n, k, 0, 1);
        return res;
    }

    private void backTracingSum3(int n, int k, int sum, int startIndex) {
        if (sum > n) {
            return;
        }

        if (path.size() == k) {
            if (sum == n) {
                res.add(new ArrayList<>(path));
            }
        }

        for (int i = startIndex; i < n - (k - path.size()) + 1; i++) {
            sum += i;
            path.add(i);
            backTracingSum3(n, k, sum, i + 1);
            sum -= i;
            path.removeLast();
        }
    }

    List<List<Integer>> res = new ArrayList<>();
    LinkedList<Integer> path = new LinkedList<>();

    public List<List<Integer>> combine(int n, int k) {
        backTracing(n, k, 1);
        return res;
    }

    private void backTracing(int n, int k, int startIndex) {
        if (path.size() == k) {
            res.add(new ArrayList<>(path));
            return;
        }
        for (int i = startIndex; i <= n - (k - path.size()) + 1; i++) {
            path.add(i);
            backTracing(n, k, i + 1);
            path.removeLast();
        }
    }
}
