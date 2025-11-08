package com.ds.datastruct.tree;

import lombok.ToString;

import java.util.*;

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
//        List<Integer> res = rightSideView(head);
//        res.forEach(System.out::println);
        int res = minDepth(head);
        System.out.println("res = " + res);
    }

    public static void main2(String[] args) {
        Node head = new Node(1);
        Node node2 = new Node(2);
        Node node3 = new Node(3);
        Node node4 = new Node(4);
        Node node5 = new Node(5);
        Node node6 = new Node(6);
        Node node7 = new Node(7);
        Node node8 = new Node(8);
        Node node9 = new Node(9);

        head.left = node2;
        head.right = node3;

        node2.left = node4;
        node2.right = node5;

        node3.left = node6;
        node3.right = node7;
        Node res = connect(head);
        System.out.println("res = " + res);
    }

    public static int minDepth(TreeNode root) {
        if (root == null) {
            return 0;
        }
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);

        int deep = 0;
        while (!queue.isEmpty()) {
            int size = queue.size();
            deep++;
            while (size-- > 0) {
                TreeNode cur = queue.poll();
                if (cur.left == null && cur.right == null) {
                    return deep;
                }
                if (cur.left != null) {
                    queue.offer(cur.left);
                }
                if (cur.right != null) {
                    queue.offer(cur.right);
                }
            }
        }
        return deep;
    }

    public static Node connect(Node root) {
        if (root == null) {
            return new Node();
        }
        Queue<Node> queue = new LinkedList<>();
        queue.offer(root);

        while (!queue.isEmpty()) {
            int size = queue.size();
            Node cur = queue.poll();
            if (cur.left != null) {
                queue.offer(cur.left);
            }
            if (cur.right != null) {
                queue.offer(cur.right);
            }
            while (size-- > 1) {
                Node next = queue.poll();
                if (next.left != null) {
                    queue.offer(next.left);
                }
                if (next.right != null) {
                    queue.offer(next.right);
                }
                cur.next = next;
                cur = next;
            }

        }
        return root;
    }

    public static List<Integer> rightSideView(TreeNode root) {
        List<Integer> res = new ArrayList<>();

        if (root == null) {
            return res;
        }
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);

        while (!queue.isEmpty()) {
            int size = queue.size();
            List<Integer> cur = new ArrayList<>();
            while (size-- > 0) {
                TreeNode node = queue.poll();
                cur.add(node.val);
                if (node.left != null) {
                    queue.offer(node.left);
                }
                if (node.right != null) {
                    queue.offer(node.right);
                }
            }
//            res.add(cur.get(cur.size() - 1));
        }
//        Collections.reverse(res);
        return res;
    }

    public static List<List<Integer>> levelOrder(TreeNode root) {
        List<List<Integer>> res = new ArrayList<>();

        if (root == null) {
            return res;
        }
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);

        while (!queue.isEmpty()) {
            int size = queue.size();
            List<Integer> cur = new ArrayList<>();
            while (size-- > 0) {
                TreeNode node = queue.poll();
                cur.add(node.val);
                if (node.left != null) {
                    queue.offer(node.left);
                }
                if (node.right != null) {
                    queue.offer(node.right);
                }
            }
            res.add(cur);
        }
//        Collections.reverse(res);
        return res;
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

    @ToString
    static class Node {
        public int val;
        public Node left;
        public Node right;
        public Node next;

        public Node() {
        }

        public Node(int _val) {
            val = _val;
        }

        public Node(int _val, Node _left, Node _right, Node _next) {
            val = _val;
            left = _left;
            right = _right;
            next = _next;
        }
    }
}
