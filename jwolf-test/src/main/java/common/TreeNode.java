package common;


import lombok.ToString;

@ToString
public class TreeNode<T> {
    public T data;
    public TreeNode left;
    public TreeNode right;

    public TreeNode(T data) {
        this.data = data;
    }


}
