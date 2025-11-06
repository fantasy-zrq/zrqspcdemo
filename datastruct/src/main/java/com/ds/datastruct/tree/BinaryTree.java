package com.ds.datastruct.tree;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * @author zrq
 * @time 2025/11/6 10:33
 * @description
 */
public class BinaryTree {
    public static void main(String[] args) {
        TreeNode head = new TreeNode(1);
        TreeNode node1 = new TreeNode(2);
        TreeNode node2 = new TreeNode(3);
        head.right = node1;
        node1.left = node2;
        List<Integer> res = iterativeMethod(head);
        res.forEach(System.out::println);
    }

    public static List<Integer> iterativeMethod(TreeNode root) {
        List<Integer> res = new ArrayList<>();
        Stack<TreeNode> stack = new Stack<>();
        if (root == null) {
            return res;
        }
        TreeNode cur = root;
        while (cur != null || !stack.isEmpty()) {
            if (cur != null) {
                stack.push(cur);
                cur = cur.left;
            } else {
                cur = stack.pop();
                res.add(cur.val);
                cur = cur.right;
            }
        }
        return res;
    }

    public static List<Integer> preorderTraversal(TreeNode root) {
        List<Integer> res = new ArrayList<>();
        recursion(root, res);
        return res;
    }

    private static void recursion(TreeNode root, List<Integer> res) {
        if (root == null) {
            return;
        }
        recursion(root.left, res);
        recursion(root.right, res);
        res.add(root.val);
    }

    static class TreeNode {
        public int val;
        public TreeNode left;
        public TreeNode right;

        public TreeNode(int val) {
            this.val = val;
        }

        public TreeNode(TreeNode left, int val, TreeNode right) {
            this.left = left;
            this.val = val;
            this.right = right;
        }
    }
}
