public class Test {
	
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
		// TODO Auto-generated method stub
		AVLTree tree = new AVLTree();
		tree.insert(1, "omg");
		tree.insert(2, "lol");
		tree.insert(3, "pls");
		tree.insert(4, "pls");
		tree.insert(5, "pls");
		tree.insert(5, "pls");
		tree.insert(6, "pls");
		tree.insert(7, "pls");
		tree.insert(8, "pls");
		tree.insert(9, "pls");
		tree.insert(10, "pls");
		print2D(tree.getRoot());
	}

}