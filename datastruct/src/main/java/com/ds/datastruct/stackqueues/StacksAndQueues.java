package com.ds.datastruct.stackqueues;

import java.util.*;

/**
 * @author zrq
 * @time 2025/10/30 09:37
 * @description
 */
public class StacksAndQueues {

    public static void main(String[] args) {
//        boolean res = isValid("()[]){}))");
//        System.out.println("res = " + res);
//        String res = removeDuplicates("abbaca");
//        System.out.println("res = " + res);
//        int res = evalRPN(new String[]{"2", "1", "+", "3", "*"});
//        System.out.println("res = " + res);
//        int[] res = maxSlidingWindow(new int[]{1, 3, -1, -3, 5, 3, 6, 7}, 3);
//        for (int re : res) {
//            System.out.println("re = " + re);
//        }
        int[] res = topKFrequent(new int[]{4, 1, -1, 2, -1, 2, 3}, 2);
        for (int re : res) {
            System.out.println("re = " + re);
        }
    }

    public static int[] topKFrequent(int[] nums, int k) {
        Map<Integer, Integer> map = new HashMap<>(10 * 10 * 10 * 10 * 10);
        for (int num : nums) {
            if (map.containsKey(num)) {
                map.put(num, map.get(num) + 1);
                continue;
            }
            map.put(num, 1);
        }
        return map.entrySet()
                .stream()
                .sorted((e1, e2) -> e2.getValue() - e1.getValue())
                .limit(k)
                .map(Map.Entry::getKey)
                .mapToInt(Integer::intValue)
                .toArray();
    }

    public static int[] maxSlidingWindow(int[] nums, int k) {
        Deque<Integer> deque = new LinkedList<>();
        List<Integer> res = new ArrayList<>(nums.length - k + 1);
        for (int i = 0; i < k; i++) {
            while (!deque.isEmpty() && nums[deque.peekLast()] < nums[i]) {
                deque.pollLast();
            }
            deque.addLast(i);
        }
        res.add(nums[deque.peekFirst()]);
        for (int i = k; i < nums.length; i++) {
            while (!deque.isEmpty() && i - deque.peek() + 1 > k) {
                deque.pollFirst();
            }
            while (!deque.isEmpty() && nums[deque.peekLast()] < nums[i]) {
                deque.pollLast();
            }
            deque.addLast(i);
            res.add(nums[deque.peekFirst()]);
        }
        return res.stream().mapToInt(Integer::intValue).toArray();
    }

    public static int evalRPN(String[] tokens) {
        Stack<Integer> stack = new Stack<>();

        for (String s : tokens) {
            if ("+".equals(s)) {
                int add = stack.pop() + stack.pop();
                stack.push(add);
            } else if ("-".equals(s)) {
                Integer first = stack.pop();
                int sub = stack.pop() - first;
                stack.push(sub);
            } else if ("*".equals(s)) {
                int add = stack.pop() * stack.pop();
                stack.push(add);
            } else if ("/".equals(s)) {
                Integer first = stack.pop();
                int divide = stack.pop() / first;
                stack.push(divide);
            } else {
                stack.push(Integer.valueOf(s));
            }
        }
        return stack.pop();
    }

    public static String removeDuplicates(String s) {
        Stack<Character> stack = new Stack<>();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            if (stack.isEmpty() || stack.peek() != s.charAt(i)) {
                stack.push(s.charAt(i));
            } else {
                stack.pop();
            }
        }
        while (!stack.isEmpty()) {
            builder.append(stack.pop());
        }
        return builder.reverse().toString();
    }

    public static boolean isValid(String s) {
        Stack<Character> stack = new Stack<>();
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '(') {
                stack.push(')');
            } else if (s.charAt(i) == '[') {
                stack.push(']');
            } else if (s.charAt(i) == '{') {
                stack.push('}');
            } else if (stack.isEmpty() || stack.peek() != s.charAt(i)) {
                return false;
            } else {
                stack.pop();
            }
        }
        return stack.isEmpty();
    }

    Stack<Integer> stackIn;
    Stack<Integer> stackOut;

    public StacksAndQueues() {
        this.stackIn = new Stack<>();
        this.stackOut = new Stack<>();
    }

    public Integer peek() {
        filling();
        return stackOut.peek();
    }

    public Integer pop() {
        filling();
        return stackOut.pop();
    }

    public void push(int x) {
        stackIn.push(x);
    }

    public boolean empty() {
        return stackOut.isEmpty() && stackIn.isEmpty();
    }

    public void filling() {
        if (!stackOut.isEmpty()) {
            return;
        }
        while (!stackIn.isEmpty()) {
            stackOut.push(stackIn.pop());
        }
    }

    static class MyStack {

        private Deque<Integer> queue;

        public MyStack() {
            queue = new ArrayDeque<>();
        }

        public void push(int x) {
            queue.push(x);
        }

        public int pop() {
            return queue.pop();
        }

        public int top() {
            return queue.peek();
        }

        public boolean empty() {
            return queue.isEmpty();
        }
    }
}
