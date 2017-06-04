package kc;

import java.util.ArrayList;
import java.util.List;

public class TreeNode {

	int val;
	TreeNode left;
	TreeNode right;
	List<TreeNode> children;
	TreeNode(int x) {
		val = x;
	}
	
	@Override
	public String toString() {
		List<Integer> res = inorderTraversal(this);
		StringBuilder sb = new StringBuilder();
		for(Integer xx : res) {
			sb.append(xx);
			
		}
		return sb.toString();
	}
	
    public List<Integer> inorderTraversal(TreeNode root) {
    	List<Integer> res = new ArrayList<Integer>();
    	helper(root,res);
    	return res;
    }
    
    private void helper(TreeNode root, List<Integer> res) {
    	if(root == null) return;
    	
    	helper(root.left, res);
    	res.add(root.val);
    	helper(root.right, res);
    }
    
    public String levelOrderTraversal() {
    	BinaryTreeLevelOrderTraversal x = new BinaryTreeLevelOrderTraversal();
    	return x.levelOrder(this).toString();
    }
}
