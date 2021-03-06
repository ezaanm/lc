package kc;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class BinaryTreeRightSideView {

    public List<Integer> rightSideView(TreeNode root) {
        List<Integer> res = new ArrayList<Integer>();
        if(root == null) return res;
        helper(root, 1, 0, res);
        return res;
    }
    
    private int helper(TreeNode root, int curLevel, int maxReached, List<Integer> res) {
    	if(root.left == null && root.right == null) {
    		if(curLevel > maxReached) {
    			res.add(root.val);
    			maxReached = curLevel;
    		}
    		return maxReached;
    	}
    	
    	if(maxReached >= curLevel) {
    		if(root.right != null) {
    			maxReached = helper(root.right,curLevel+1,maxReached, res);
    		}
    		if(root.left != null) {
    			maxReached = helper(root.left, curLevel+1, maxReached, res);
    		}
    	} else {
    		res.add(root.val);
    		if(root.right != null) {
    			maxReached = helper(root.right, curLevel+1, curLevel, res);
    		}
    		if(root.left != null) {
    			maxReached = helper(root.left, curLevel+1, maxReached, res);
    		}
    	}
    	
    	return maxReached;
    }
    
    public List<Integer> rightSideView2(TreeNode root) {
        List<Integer> res = new ArrayList<Integer>();
        Queue<TreeNode> parents = new LinkedList<TreeNode>();
        if(root == null) return res;
        parents.add(root);
        res.add(root.val);
        while(!parents.isEmpty()) {
            Queue<TreeNode> children = new LinkedList<TreeNode>();
            while(!parents.isEmpty()) {
                TreeNode p = parents.poll();
                if(p.right != null) {
                    children.add(p.right);
                }
                
                if(p.left != null) {
                    children.add(p.left);
                }
            }
            if(!children.isEmpty()) {
                res.add(children.peek().val);
            }
            parents = children;
        }
        return res;
    }
    
    
    public static void main(String[] args) {
    	BinaryTreeRightSideView x = new BinaryTreeRightSideView();
    	TreeNode a = new TreeNode(1);
    	TreeNode b = new TreeNode(2);
    	TreeNode c = new TreeNode(3);
    	TreeNode d = new TreeNode(4);
    	TreeNode e = new TreeNode(5);
    	
    	a.left = b;
    	a.right = c;
    	b.right = e;
    	e.right = d;
    	//c.right = d;
//    	System.out.println(a.levelOrderTraversal());
//    	System.out.println(x.rightSideView(a));
//    
    	System.out.println(0xFFFFFFFF & 3);
	}
    
}
