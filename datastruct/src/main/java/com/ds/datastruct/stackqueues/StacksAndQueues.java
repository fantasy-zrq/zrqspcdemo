package com.ds.datastruct.stackqueues;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Stack;

/**
 * @author zrq
 * @time 2025/10/30 09:37
 * @description
 */
public class StacksAndQueues {

    public static void main(String[] args) {
        boolean res = isValid("()[]){}))");
        System.out.println("res = " + res);
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
