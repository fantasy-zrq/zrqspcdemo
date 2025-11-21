package com.ds.datastruct.backtrace;

import java.util.ArrayList;
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

    public List<List<Integer>> combinationSum3(int k, int n) {
        backTracingSum3(n, k, 0, 1);
        return res;
    }

    private void backTracingSum3(int n, int k, int sum, int startIndex) {
        if (sum > n) {
            return;
        }

        if (path.size() == k ) {
            if(sum == n){
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
