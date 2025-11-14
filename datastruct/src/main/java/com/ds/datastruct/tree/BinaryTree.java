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
        TreeNode node2 = new TreeNode(2);
        TreeNode node3 = new TreeNode(3);
        TreeNode node4 = new TreeNode(4);
        TreeNode node5 = new TreeNode(5);
        TreeNode node6 = new TreeNode(6);
        TreeNode node7 = new TreeNode(7);
        head.left = node2;
        head.right = node3;
        node2.left = node4;
        node2.right = node5;
        node3.left = node6;
        node3.right = node7;
//        List<Integer> res = rightSideView(head);
//        res.forEach(System.out::println);
//        int res = minDepth(head);
//        System.out.println("res = " + res);
//        TreeNode res = invertTree(head);
//        System.out.println("res = " + res);

//        boolean res = isBalanced(head);
//        System.out.println("res = " + res);
        //              中左右                     左右中
//        inorder = [9,3,15,20,7], postorder = [9,15,7,20,3]
//        TreeNode res = buildTree(new int[]{9, 3, 15, 20, 7}, new int[]{9, 15, 7, 20, 3});
//        System.out.println("res = " + res);
    }

    Map<Integer, Integer> map = new HashMap<>();

    public TreeNode buildTree(int[] inorder, int[] postorder) {
        if (inorder.length == 0) {
            return null;
        }

        for (int i = 0; i < inorder.length; i++) {
            map.put(inorder[i], i);
        }
        return m3(postorder, 0, postorder.length, 0, inorder.length);
    }

    private TreeNode m3(int[] postorder, int postorderStart, int postorderEnd, int inorderStart, int inorderEnd) {
        if (postorderStart >= postorderEnd || inorderStart >= inorderEnd) {
            return null;
        }
        int midNodeVal = postorder[postorderEnd - 1];
        TreeNode treeNode = new TreeNode(midNodeVal);
        Integer midIndex = map.get(midNodeVal);
        int leftCount = midIndex - inorderStart;
        treeNode.left = m3(postorder, postorderStart, postorderStart + leftCount, inorderStart, midIndex);
        treeNode.right = m3(postorder, postorderStart + leftCount, postorderEnd - 1, midIndex + 1, inorderEnd);
        return treeNode;
    }

    public static boolean hasPathSum(TreeNode root, int targetSum) {
        if (root == null) {
            return true;
        }
        targetSum -= root.val;
        if (root.left == null && root.right == null) {
            return targetSum == 0;
        }
        if (root.left != null) {
            if (hasPathSum(root.left, targetSum)) {
                return true;
            }
        }
        if (root.right != null) {
            return hasPathSum(root.right, targetSum);
        }
        return false;
    }

    public int sumOfLeftLeaves(TreeNode root) {
        if (root == null) {
            return 0;
        }
        int leftValue = sumOfLeftLeaves(root.left);
        int rightValue = sumOfLeftLeaves(root.right);

        int midValue = 0;
        if (root.left != null && root.left.left == null && root.left.right == null) {
            midValue = root.left.val;
        }
        return midValue + leftValue + rightValue;
    }

    List<String> list = new ArrayList<>();

    public List<String> binaryTreePaths(TreeNode root) {
        m2(root, "");
        return list;
    }

    private void m2(TreeNode root, String s) {
        if (root == null) {
            return;
        }
        if (root.left == null && root.right == null) {
            list.add(s + root.val);
            return;
        }
        String s1 = s + root.val + "->";
        m2(root.left, s1);
        m2(root.right, s1);
    }

    public static boolean isBalanced(TreeNode root) {
        if (root == null) {
            return true;
        }
        return m1(root) != -1;
    }

    private static int m1(TreeNode root) {
        if (root == null) {
            return 0;
        }
        int left = m1(root.left);
        if (left == -1) {
            return -1;
        }
        int right = m1(root.right);
        if (right == -1) {
            return -1;
        }
        return Math.abs(left - right) > 1 ? -1 : Math.max(left, right) + 1;
    }

    public static int countNodes(TreeNode root) {
        if (root == null) {
            return 0;
        }
        int left = countNodes(root.left);
        int right = countNodes(root.right);
        return left + right + 1;
    }

    public static int minDepth(TreeNode root) {
        if (root == null) {
            return 0;
        }
        int left = minDepth(root.left);
        int right = minDepth(root.right);
        if (root.left == null && root.right != null) {
            return right + 1;
        }
        if (root.right == null && root.left != null) {
            return left + 1;
        }
        return Math.min(left, right) + 1;
    }

    public static int maxDepth(TreeNode root) {
        if (root == null) {
            return 0;
        }
        int leftDepth = maxDepth(root.left);
        int rightDepth = maxDepth(root.right);
        return Math.max(leftDepth, rightDepth) + 1;
    }

    public boolean isSubtree(TreeNode root, TreeNode subRoot) {
        if (root == null) {
            return false;
        } else if (compSubTree(root, subRoot)) {
            return true;
        }
        return isSubtree(root.left, subRoot) || isSubtree(root.right, subRoot);
    }

    private boolean compSubTree(TreeNode root, TreeNode subRoot) {
        if (root == null && subRoot == null) {
            return true;
        } else if (root == null || subRoot == null) {
            return false;
        } else if (root.val != subRoot.val) {
            return false;
        }
        return compSubTree(root.left, subRoot.left) && compSubTree(root.right, subRoot.right);
    }

    public boolean isSameTree(TreeNode p, TreeNode q) {
        return compSame(p, q);
    }

    private boolean compSame(TreeNode p, TreeNode q) {
        if (p == null && q == null) {
            return true;
        } else if (p == null && q != null) {
            return false;
        } else if (p != null && q == null) {
            return false;
        } else if (p.val != q.val) {
            return false;
        }
        boolean left = compSame(p.left, q.left);
        boolean right = compSame(p.right, q.right);
        return left && right;
    }

    public static boolean isSymmetric(TreeNode root) {
        return comp(root.left, root.right);
    }

    private static boolean comp(TreeNode left, TreeNode right) {
        if (left == null && right != null) {
            return false;
        }
        if (left != null && right == null) {
            return false;
        }

        if (left == null && right == null) {
            return true;
        }
        if (left.val != right.val) {
            return false;
        }

        boolean out = comp(left.left, right.right);
        boolean in = comp(left.right, right.left);
        return out && in;
    }

    //前序遍历
    public static TreeNode invertTree(TreeNode root) {
        if (root == null) {
            return root;
        }
        TreeNode temp;
        temp = root.left;
        root.left = root.right;
        root.right = temp;

        invertTree(root.left);
        invertTree(root.right);
        return root;
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

//    public static int minDepth(TreeNode root) {
//        if (root == null) {
//            return 0;
//        }
//        Queue<TreeNode> queue = new LinkedList<>();
//        queue.offer(root);
//
//        int deep = 0;
//        while (!queue.isEmpty()) {
//            int size = queue.size();
//            deep++;
//            while (size-- > 0) {
//                TreeNode cur = queue.poll();
//                if (cur.left == null && cur.right == null) {
//                    return deep;
//                }
//                if (cur.left != null) {
//                    queue.offer(cur.left);
//                }
//                if (cur.right != null) {
//                    queue.offer(cur.right);
//                }
//            }
//        }
//        return deep;
//    }

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

    @ToString
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
