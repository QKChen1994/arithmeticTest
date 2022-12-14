package com.cqk.arithmetic.tree;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Stack;

/**
 * 二叉树遍历
 *
 * @author cqk
 */
public class TraversalOfBinaryTree {

    public static void main(String[] args) {
        LinkedList<Integer> inputList = new LinkedList<>(
                Arrays.asList(new Integer[]{3, 2, 9, null, null, 10, null,
                        null, 8, null, 4})
        );
        TreeNode treeNode = createBinaryTree(inputList);
        System.out.println(" 前序遍历：");
        preOrderTraversal(treeNode);
        System.out.println(" 非递归前序遍历：");
        preOrderTraversalOfStack(treeNode);
        System.out.println(" 中序遍历：");
        inOrderTraversal(treeNode);
        System.out.println(" 非递归中序遍历：");
        inOrderTraversalOfStack(treeNode);
        System.out.println(" 后序遍历：");
        postOrderTraversal(treeNode);
        System.out.println(" 非递归后序遍历：");
        postOrderTraversalOfStack(treeNode);
    }

    /**
     * 构建二叉树
     *
     * @param inputList 输入序列
     */
    public static TreeNode createBinaryTree(LinkedList<Integer> inputList) {
        TreeNode node = null;
        if (inputList == null || inputList.isEmpty()) {
            return null;
        }
        Integer data = inputList.removeFirst();
        if (data != null) {
            node = new TreeNode(data);
            node.leftChild = createBinaryTree(inputList);
            node.rightChild = createBinaryTree(inputList);
        }
        return node;
    }

    /**
     * 20. * 二叉树前序遍历
     * 2 * @param node 二叉树节点
     * 2
     */
    public static void preOrderTraversal(TreeNode node) {
        if (node == null) {
            return;
        }
        System.out.println(node.data);
        preOrderTraversal(node.leftChild);
        preOrderTraversal(node.rightChild);
    }

    /**
     * 二叉树中序遍历
     *
     * @param node 二叉树节点
     */
    public static void inOrderTraversal(TreeNode node) {
        if (node == null) {
            return;
        }
        inOrderTraversal(node.leftChild);
        System.out.println(node.data);
        inOrderTraversal(node.rightChild);
    }


    /**
     * 二叉树后序遍历
     *
     * @param node 二叉树节点
     */
    public static void postOrderTraversal(TreeNode node) {
        if (node == null) {
            return;
        }
        postOrderTraversal(node.leftChild);
        postOrderTraversal(node.rightChild);
        System.out.println(node.data);
    }


    /**
     * 二叉树非递归前序遍历
     *
     * @param node 二叉树节点
     */
    private static void preOrderTraversalOfStack(TreeNode node) {
        Stack<TreeNode> stack = new Stack<>();
        TreeNode nodeCopy = node;
        while (nodeCopy != null || !stack.isEmpty()) {
            //迭代访问节点的左孩子，并入栈
            while (nodeCopy != null) {
                System.out.println(nodeCopy.data);
                stack.add(nodeCopy);
                nodeCopy = nodeCopy.leftChild;
            }
            //如果节点没有左孩子，则弹出栈顶节点，访问节点右孩子
            if (!stack.isEmpty()) {
                nodeCopy = stack.pop();
                nodeCopy = nodeCopy.rightChild;
            }
        }
    }


    /**
     * 二叉树非递归中序遍历
     *
     * @param node 二叉树节点
     */
    private static void inOrderTraversalOfStack(TreeNode node) {
        Stack<TreeNode> stack = new Stack<>();
        TreeNode nodeCopy = node;
        while (nodeCopy != null || !stack.isEmpty()) {
            //迭代访问节点的左孩子，并入栈
            while (nodeCopy != null) {
                stack.add(nodeCopy);
                nodeCopy = nodeCopy.leftChild;
            }
            //如果节点没有左孩子，则弹出栈顶节点，访问节点右孩子
            if (!stack.isEmpty()) {
                nodeCopy = stack.pop();
                System.out.println(nodeCopy.data);
                nodeCopy = nodeCopy.rightChild;
            }
        }
    }

    /**
     * 二叉树非递归后序遍历
     *
     * 1、先遍历左孩子入栈，判断栈顶元素是否有右孩子，
     * 2、有-入栈 循环右孩子；
     * 3、没有-打印，记录最近访问过，并跳过寻找左孩子，用它的父节点循环
     *
     * @param node 二叉树节点
     */
    private static void postOrderTraversalOfStack(TreeNode node) {
        Stack<TreeNode> stack = new Stack<>();
        TreeNode nodeCopy = node;
        TreeNode lastVisitNode = null;
        while (nodeCopy != null || !stack.isEmpty()) {
            //迭代访问节点的左孩子，并入栈; 此节点不为刚访问节点
            while (nodeCopy != null) {
                stack.add(nodeCopy);
                nodeCopy = nodeCopy.leftChild;
            }
            // 提取栈顶节点
            nodeCopy = stack.pop();
            //节点没有左孩子，判断栈顶节点是否没有右孩子，或者右孩子和最近访问的node是一个，
            if (nodeCopy.rightChild == null || nodeCopy.rightChild.equals(lastVisitNode)) {
                // 如果是，则认为这是一个叶子节点，打印，并记录最近访问节点为此节点。节点设置为null，下一个循环走此节点的父节点。
                System.out.println(nodeCopy.data);
                lastVisitNode = nodeCopy;
                nodeCopy = null;
            }else {
                // 如果不是，则入栈，下一个循环访问右孩子的下属节点，
                stack.add(nodeCopy);
                nodeCopy = nodeCopy.rightChild;
            }
        }
    }

    /**
     * 6 * 二叉树节点
     * 6
     */
    private static class TreeNode {
        int data;
        TreeNode leftChild;
        TreeNode rightChild;

        TreeNode(int data) {
            this.data = data;
        }
    }

}
