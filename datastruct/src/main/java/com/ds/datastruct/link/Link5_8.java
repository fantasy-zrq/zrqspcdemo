package com.ds.datastruct.link;

import lombok.ToString;

/**
 * @author zrq
 * @time 2025/10/15 10:30
 * @description
 */
public class Link5_8 {
    public static void main(String[] args) {
//        ListNode res = exchangeNodeInLinkedListPairwise(generateLinkList());
//        System.out.println("res = " + res);
//        ListNode head = removeNthFromEnd(generateLinkList(), 3);
//        ListNode[] intersectionNode = generateIntersectionNode();
//        ListNode node = getIntersectionNode(intersectionNode[0], intersectionNode[1]);
        ListNode node = generateCircularNode();
        ListNode circularNode = circularLinkedList(node);

    }

    private static ListNode circularLinkedList(ListNode head) {

        ListNode fast = head;
        ListNode slow = head;

        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
            if (fast == slow) {
                slow = head;
                while (true) {
                    if (slow == fast) {
                        return fast;
                    }
                    slow = slow.next;
                    fast = fast.next;
                }
            }
        }
        return null;
    }

    private static ListNode generateCircularNode() {
        ListNode node1 = new ListNode(1);
        ListNode node2 = new ListNode(2);
        ListNode node3 = new ListNode(3);
        ListNode node4 = new ListNode(4);
        ListNode node5 = new ListNode(5);
        ListNode node6 = new ListNode(6, node3);

        node1.next = node2;
        node2.next = node3;
        node3.next = node4;
        node4.next = node5;
        node5.next = node6;
        return node1;
    }

    public static ListNode getIntersectionNode(ListNode headA, ListNode headB) {
        int lengthA = 0;
        int lengthB = 0;
        ListNode curA = headA;
        ListNode curB = headB;

        while (curA != null) {
            curA = curA.next;
            lengthA++;
        }

        while (curB != null) {
            curB = curB.next;
            lengthB++;
        }

        curA = headA;
        curB = headB;

        if (lengthA < lengthB) {
            int temp;
            temp = lengthA;
            lengthA = lengthB;
            lengthB = temp;
            ListNode tem;
            tem = curA;
            curA = curB;
            curB = tem;
        }

        int gap = lengthA - lengthB;
        //gap = 2
        for (int i = 0; i < gap; i++) {
            curA = curA.next;
        }
        while (curA != null) {
            if (curA == curB) {
                return curA;
            }
            curA = curA.next;
            curB = curB.next;
        }

        return null;
    }

    private static ListNode removeNthFromEnd(ListNode head, int n) {
        ListNode dummyHead = new ListNode(-99, head);
        ListNode fast = dummyHead;
        ListNode slow = dummyHead;
        for (int i = 0; i <= n; i++) {
            fast = fast.next;
        }
        while (fast != null) {
            fast = fast.next;
            slow = slow.next;
        }
        slow.next = slow.next.next;
        return dummyHead.next;
    }

    private static ListNode exchangeNodeInLinkedListPairwise(ListNode head) {
        ListNode dummyHead = new ListNode(-99);
        dummyHead.next = head;
        ListNode current = dummyHead;
        while (current.next != null && current.next.next != null) {
            ListNode next = current.next;
            ListNode nextnextnext = current.next.next.next;
            current.next = current.next.next;
            current.next.next = next;
            next.next = nextnextnext;
            current = current.next.next;
        }
        return dummyHead.next;
    }

    public static ListNode generateLinkList() {
        ListNode node1 = new ListNode(1);
        ListNode node2 = new ListNode(2);
        ListNode node3 = new ListNode(3);
        ListNode node4 = new ListNode(4);
        ListNode node5 = new ListNode(5);

        node1.next = node2;
        node2.next = node3;
        node3.next = node4;
        node4.next = node5;
        return node1;
    }

    public static ListNode[] generateIntersectionNode() {
        ListNode node1 = new ListNode(1);
        ListNode node2 = new ListNode(2);
        ListNode node3 = new ListNode(3);
        ListNode node4 = new ListNode(4);
        ListNode node5 = new ListNode(5);

        node1.next = node2;
        node2.next = node3;
        node3.next = node4;
        node4.next = node5;

        ListNode node9 = new ListNode(9, node3);
        ListNode node10 = new ListNode(10, node9);
        ListNode node11 = new ListNode(11, node10);

        return new ListNode[]{node1, node11};
    }
}

@ToString
class ListNode {
    public int val;
    public ListNode next;

    public ListNode(int val) {
        this.val = val;
    }

    public ListNode(int val, ListNode next) {
        this.val = val;
        this.next = next;
    }
}
