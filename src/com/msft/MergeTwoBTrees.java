package com.msft;
class TreeNode {
      int val;
      TreeNode left;
      TreeNode right;
      TreeNode() {}
      TreeNode(int val) { this.val = val; }
      TreeNode(int val, TreeNode left, TreeNode right) {
          this.val = val;
          this.left = left;
          this.right = right;
      }
}
public class MergeTwoBTrees {
    public TreeNode mergeTrees(TreeNode root1, TreeNode root2) {
        if(root1 == null && root2 == null) return null;
        TreeNode res = new TreeNode();
        res.val = ((root1!=null)?(root1.val):(0)) + ((root2!=null)?(root2.val):(0));

        TreeNode r1_left = (root1!=null)?(root1.left):(null);
        TreeNode r1_right =  (root1!=null)?(root1.right):(null);
        TreeNode r2_left =  (root2!=null)?(root2.left):(null);
        TreeNode r2_right =  (root2!=null)?(root2.right):(null);
        if(r1_left != null || r2_left != null) {
            res.left = mergeTrees(r1_left, r2_left);
        }
        if(r1_right != null || r2_right != null) {
            res.right = mergeTrees(r1_right, r2_right);
        }
        return res;
    }
}
