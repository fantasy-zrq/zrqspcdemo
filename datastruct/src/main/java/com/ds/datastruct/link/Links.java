package com.ds.datastruct.link;

import lombok.ToString;

/**
 * @author zrq
 * @time 2025/10/13 11:09
 * @description
 */
public class Links {
    public static void main(String[] args) {
        ListNode linkList = generateLinkList();
//        ListNode head = removeElements(linkList, 2);
        ListNode head1 = virtualHeadNode(linkList, 2);
        System.out.println("head1 = " + head1);
    }

    private static ListNode virtualHeadNode(ListNode head, int val) {
        ListNode dummyHead = new ListNode(-1);
        dummyHead.next = head;
        ListNode current = dummyHead;
        while (current.next != null) {
            if (current.next.val == val) {
                current.next = current.next.next;
            } else {
                current = current.next;
            }
        }
        //这里不返回head，因为head头节点可能是需要删除的
        return dummyHead.next;
    }

    public static ListNode removeElements(ListNode head, int val) {
        //假如头节点是目标，则让其直接出队
        while (head != null && head.val == val) {
            head = head.next;
        }
        //拿到新头节点的引用,这个头节点一定不会是目标节点
        ListNode current = head;
        while (current != null && current.next != null) {
            if (current.next.val == val) {
                current.next = current.next.next;
            } else {
                current = current.next;
            }
        }
        return head;
    }


    @ToString
    static class ListNode {
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
}
