package com.ds.datastruct.link;

import lombok.ToString;

/**
 * @author zrq
 * @time 2025/10/13 16:15
 * @description
 */
@ToString
public class MyLinkedList {

    public ListNode dummyHead;
    public int size;

    @ToString
    static class ListNode {
        public int val;
        public MyLinkedList.ListNode next;

        public ListNode(int val) {
            this.val = val;
        }

        public ListNode(int val, MyLinkedList.ListNode next) {
            this.val = val;
            this.next = next;
        }
    }

    public MyLinkedList() {
        dummyHead = new ListNode(-99);
    }

    public int get(int index) {
        if (index < 0 || index > size - 1) {
            return -1;
        }
        ListNode current = dummyHead;
        while (index > 0) {
            current = current.next;
            index--;
        }
        return current.next.val;
    }

    public void addAtHead(int val) {
        ListNode headNode = new ListNode(val);
        ListNode next = dummyHead.next;
        dummyHead.next = headNode;
        headNode.next = next;
        size++;
    }

    public void addAtTail(int val) {
        ListNode tailNode = new ListNode(val);
        ListNode current = dummyHead;
        while (current.next != null) {
            current = current.next;
        }
        current.next = tailNode;
        size++;
    }

    //index= 2 size=3
    public void addAtIndex(int index, int val) {
        if (index < 0 || index > size) {
            return;
        } else if (index == size) {
            addAtTail(val);
            return;
        }
        ListNode addNode = new ListNode(val);
        ListNode current = dummyHead;
        while (index > 0) {
            current = current.next;
            index--;
        }
        ListNode next = current.next;
        current.next = addNode;
        addNode.next = next;
        size++;
    }

    public void deleteAtIndex(int index) {
        if (index < 0 || index > size - 1) {
            return;
        }
        ListNode current = dummyHead;
        while (index > 0) {
            current = current.next;
            index--;
        }
        current.next = current.next.next;
        size--;
    }

    public static MyLinkedList.ListNode reverseList(MyLinkedList.ListNode head) {
        // 1->2->3
        System.out.println("head = " + head);
        ListNode pre = null;
        //current、head都是长度为3的链表
        ListNode current = head;
        System.out.println("current = " + current);
        ListNode next;
        while (current != null) {
            System.out.println("current = " + current);
            //next是长度为2的链表 2->3
            next = current.next;
            System.out.println("next = " + next);
            //执行current.next = pre;第一个节点就从链表中移除了
            //此时current是一个单链表 1
            current.next = pre;
            System.out.println("current.next = " + current.next);
            //给pre这个链表添加了一个新节点1，就是current
            pre = current;
            System.out.println("pre = " + pre);
            current = next;
        }
        return pre;
    }

    public static ListNode reverse(ListNode head) {
        ListNode pre = null;
        ListNode current = head;
       while (current != null) {
            ListNode next = current.next;
            current.next = pre;
            pre = current;
            current = next;
        }
        return pre;
    }

    public static void main(String[] args) {
        ListNode listNode = generateLinkList();
        ListNode reverse = reverse(listNode);
        System.out.println("reverse = " + reverse);
        System.out.println("listNode = " + listNode);
    }

    public static MyLinkedList.ListNode generateLinkList() {
        MyLinkedList.ListNode node1 = new MyLinkedList.ListNode(1);
        MyLinkedList.ListNode node2 = new MyLinkedList.ListNode(2);
        MyLinkedList.ListNode node3 = new MyLinkedList.ListNode(3);
        MyLinkedList.ListNode node4 = new MyLinkedList.ListNode(4);
        MyLinkedList.ListNode node5 = new MyLinkedList.ListNode(5);

        node1.next = node2;
        node2.next = node3;
        node3.next = node4;
        node4.next = node5;
        return node1;
    }
}
