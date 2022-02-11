package algorithm;

import common.TreeNode;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Stack;

/**
 * 二叉树的构建，前中后序遍历，分层遍历,dfs,bfs
 *
 * @author majun
 * @version 1.0
 * @since 2022-02-08 22:33
 */
public class T060BinaryTree {
    /**
     * 先序遍历逆过程创建二叉树
     *
     * @param inputList
     * @return
     */
    public static TreeNode preOrderCreateBinaryTree(LinkedList<Integer> inputList) {
        TreeNode node = null;
        if (inputList == null || inputList.isEmpty()) {
            return null;
        }
        Integer data = inputList.removeFirst();
        if (data != null) {
            node = new TreeNode(data);
            node.left = preOrderCreateBinaryTree(inputList);
            node.right = preOrderCreateBinaryTree(inputList);

        }
        return node;

    }

    // 用递归的方法进行先序遍历
    public static void qinaxuDigui(TreeNode treeNode) {
        System.out.print("->" + treeNode.data);
        if (treeNode.left != null) {
            qinaxuDigui(treeNode.left);
        }
        if (treeNode.right != null) {
            qinaxuDigui(treeNode.right);
        }
    }

    // 用递归的方法进行中序遍历
    public static void zhongxuDigui(TreeNode treeNode) {
        if (treeNode.left != null) {
            zhongxuDigui(treeNode.left);
        }
        System.out.print("->" + treeNode.data);
        if (treeNode.right != null) {
            zhongxuDigui(treeNode.right);
        }
    }

    // 用递归的方法进行后序遍历
    public static void houxuDigui(TreeNode treeNode) {
        if (treeNode.left != null) {
            houxuDigui(treeNode.left);
        }
        if (treeNode.right != null) {
            houxuDigui(treeNode.right);
        }
        System.out.print("->" + treeNode.data);
    }

    // 用非递归的方法进行先序遍历
    public static void qinaxuNonDigui(TreeNode treeNode) {
        Stack<TreeNode> stack = new Stack<TreeNode>();
        while (treeNode != null || !stack.isEmpty()) {
            while (treeNode != null) {
                System.out.print("->" + treeNode.data);
                stack.push(treeNode);
                treeNode = treeNode.left;
            }
            if (!stack.isEmpty()) {
                treeNode = stack.pop();
                treeNode = treeNode.right;
            }
        }
    }

    // 用非递归的方法进行中序遍历,略

    // 用非递归的方法进行后序遍历,略

    //深度优先遍历，就是先序遍历
    public static void dfs(TreeNode treeNode) {
        Stack<TreeNode<Integer>> stack = new Stack();
        stack.push(treeNode);
        TreeNode<Integer> node = null;
        while (!stack.empty()) {
            node = stack.pop();
            System.out.print("->" + node.data);
            if (node.right != null) {
                stack.push(node.right);//先将右子树压栈
            }
            if (node.left != null) {
                stack.push(node.left);//再将左子树压栈
            }
        }
    }

    //广度优先遍历，就是分层遍历
    public static void bfs(TreeNode treeNode) {
        LinkedList<TreeNode<Integer>> queue = new LinkedList();
        queue.offer(treeNode);
        TreeNode<Integer> node = null;
        while (!queue.isEmpty()) {
            node = queue.poll();
            System.out.print("->" + node.data);
            if (node.left != null) {
                queue.offer(node.left);//先将左子树入队
            }
            if (node.right != null) {
                queue.offer(node.right);//再将右子树入队
            }
        }
    }

    public static void main(String[] args) {
        LinkedList<Integer> list = new LinkedList<>(Arrays.asList(1, 2, null, 4, null, null, 3, 5, null, null, 6, null, null));
        /**
         * 左、右孩子如果没有用null代替，以便构造二叉树
         *
         *          1                        1
         *       /    \                   /    \
         *      2      3       ==>       2      3
         *     / \    /  \                \    / \
         *    N   4  5   6                 4  5   6
         *       / | / | / |
         *      N  N N N N N
         */
        TreeNode root = preOrderCreateBinaryTree(list);
        System.out.println(root);
        System.out.println("\n前序递归遍历");
        qinaxuDigui(root);
        System.out.println("\n中序递归遍历");
        zhongxuDigui(root);
        System.out.println("\n后序递归遍历");
        houxuDigui(root);
        System.out.println("\n前序非递归遍历");
        qinaxuNonDigui(root);
        System.out.println("\ndfs");
        dfs(root);
        System.out.println("\nbfs");
        bfs(root);

    }
}
