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
        }else if(index == size){
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
}
