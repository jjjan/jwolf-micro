package algorithm.leetcode.editor.cn;//给你两个 非空 的链表，表示两个非负的整数。它们每位数字都是按照 逆序 的方式存储的，并且每个节点只能存储 一位 数字。
//
// 请你将两个数相加，并以相同形式返回一个表示和的链表。 
//
// 你可以假设除了数字 0 之外，这两个数都不会以 0 开头。 
//
// 
//
// 示例 1： 
//
// 
//输入：l1 = [2,4,3], l2 = [5,6,4]
//输出：[7,0,8]
//解释：342 + 465 = 807.
// 
//
// 示例 2： 
//
// 
//输入：l1 = [0], l2 = [0]
//输出：[0]
// 
//
// 示例 3： 
//
// 
//输入：l1 = [9,9,9,9,9,9,9], l2 = [9,9,9,9]
//输出：[8,9,9,9,0,0,0,1]
// 
//
// 
//
// 提示： 
//
// 
// 每个链表中的节点数在范围 [1, 100] 内 
// 0 <= Node.val <= 9 
// 题目数据保证列表表示的数字不含前导零 
// 
// Related Topics 递归 链表 数学 
// 👍 7648 👎 0

import algorithm.ListNode;

//leetcode submit region begin(Prohibit modification and deletion)


/**
 * Definition for singly-linked list.
 * public class ListNode {
 * int val;
 * ListNode next;
 * ListNode() {}
 * ListNode(int val) { this.val = val; }
 * ListNode(int val, ListNode next) { this.val = val; this.next = next; }
 * }
 */
class Solution2 {

    boolean isPlusOne = false; //是否进位

    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        ListNode head = new ListNode();
        add(l1, l2, head);
        return head;
    }

    public void add(ListNode l1, ListNode l2, ListNode node) {
        int v1 = l1 == null ? 0 : l1.val;
        int v2 = l2 == null ? 0 : l2.val;
        int sum = v1 + v2 + (isPlusOne ? 1 : 0);
        if (sum >= 10) {
            node.val = sum - 10;
            isPlusOne = true;
        } else {
            node.val = sum;
            isPlusOne = false;

        }
        //如果有一个链表不为null 且next也不为null时需要创建下一级Node
        if ((l1 != null && l1.next != null) || (l2 != null && l2.next != null)) {
            ListNode newNode = new ListNode();
            node.next = newNode;
            add(l1 == null ? null : l1.next, l2 == null ? null : l2.next, newNode);
        } else {
            //如果不满足下级node创建时，还要检查进位标记=1时要额外创建最后一个val=1的Node
            if (isPlusOne) {
                ListNode newNode = new ListNode(1);
                node.next = newNode;
            }
        }


    }

    public static void main(String[] args) {
        ListNode listNode1 = new ListNode(9);
        ListNode listNode2 = new ListNode(9);
        ListNode listNode3 = new ListNode(9);
        ListNode listNode11 = new ListNode(9);
        ListNode listNode22 = new ListNode(9);
        listNode1.next = listNode2;
        listNode2.next = listNode3;
        listNode11.next = listNode22;
        ListNode listNode = new Solution2().addTwoNumbers(listNode1, listNode11);
        System.out.println(listNode);
    }
}
//leetcode submit region end(Prohibit modification and deletion)
