import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Test{
	
	static int COUNT = 10;
	
	static void print2DUtil(AVLTree.IAVLNode root, int space)  
	{  
	    // Base case  
	    if (root == null)  
	        return;  
	  
	    // Increase distance between levels  
	    space += COUNT;  
	  
	    // Process right child first  
	    print2DUtil(root.getRight(), space);  
	  
	    // Print current node after space  
	    // count  
	    System.out.print("\n");  
	    for (int i = COUNT; i < space; i++)  
	        System.out.print(" ");  
	    System.out.print(root.getKey() + "\n");  
	  
	    // Process left child  
	    print2DUtil(root.getLeft(), space);  
	}  
	  
	// Wrapper over print2DUtil()  
	
	static void print2D(AVLTree.IAVLNode root)  
	{  
	    // Pass initial space count as 0  
	    print2DUtil(root, 0);  
	}
	
	public static void main(String[] args) {
		
		AVLTree tree = new AVLTree();
		for(int i = 20; i > 4; i--) {
			tree.insert(i, ""+i);
		}
		
//		for(int i = 20; i > 4; i--) {
//			tree.delete(i);
//		}
//		
		

		
//		AVLTree tree2 = new AVLTree();
//		for(int i = 0; i < 1; i++) {
//			tree2.insert(i, ""+i);
//		}
//
//		AVLTree tree3 = new AVLTree();
//		tree3.insert(2, ""+2);
		
		AVLTree[] splitted = tree.split(20);
		print2D(splitted[0].getRoot());
		System.out.println("************************");
		print2D(splitted[1].getRoot());
		
//		print2D(tree.getRoot());

//		System.out.println("*************");
//		print2D(tree2.getRoot());
//		
		
//		tree.join(tree3.getRoot(), tree2);
//		print2D(tree.getRoot());
		
//		AVLTree[] splitted = tree.split(10);
//		print2D(splitted[0].getRoot());
//		System.out.println("************************");
//		print2D(splitted[1].getRoot());

		
//		int exist = tree.insert(1, "omg");
//		System.out.println(exist);
//		int[] keysToArray = tree.keysToArray();
//		String[] infoToArray = tree.infoToArray();
//		System.out.println(Arrays.toString(keysToArray));
//		System.out.println(Arrays.toString(infoToArray));
//		System.out.println(tree.getRoot().getKey());
//		System.out.println(tree.min());
//		System.out.println(tree.max());
//		System.out.println(tree.size());
//		System.out.println(tree.empty());
//		System.out.println(tree.search(4));
//		System.out.println(tree.findPosition(tree.getRoot(), 5).getKey()); 
//		System.out.println(tree.findPosition(tree.getRoot(), 20).getKey()); 
		
	}



//	public boolean isBalanced() {
//		return isTreeBalanced(this.root);
//	}
//
//	public static boolean isTreeBalanced(IAVLNode root) {
//		if (root == null) {
//			return true;
//		}
//		boolean isNodeBalanced = isNodeBalanced(root);
//		if (!isNodeBalanced) {
//			System.out.println("Imbalanced Node!");
//			System.out.println("Key: " + root.getKey() + " Height: " + root.getHeight());
//			if (root.isRealNode()) {
//				System.out
//						.println("Right Key: " + root.getRight().getKey() + " Height: " + root.getRight().getHeight());
//				System.out.println("Left Key: " + root.getLeft().getKey() + " Height: " + root.getLeft().getHeight());
//			}
//		}
//		return isNodeBalanced && isTreeBalanced(root.getRight()) && isTreeBalanced(root.getLeft());
//	}
//
//	public static boolean isNodeBalanced(IAVLNode node) {
//		if (node.isRealNode()) {
//			return ((node.getHeight() - node.getRight().getHeight() == 1)
//					&& (node.getHeight() - node.getLeft().getHeight() == 1))
//					|| ((node.getHeight() - node.getRight().getHeight() == 1)
//							&& (node.getHeight() - node.getLeft().getHeight() == 2))
//					|| ((node.getHeight() - node.getRight().getHeight() == 2)
//							&& (node.getHeight() - node.getLeft().getHeight() == 1));
//
//		} else {
//			return ((node.getRight() == null) && (node.getLeft() == null));
//		}
//	}

}
