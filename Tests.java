import static org.junit.Assert.*;

import org.junit.Test;


public class Tests{
	
	
	@Test
	public void testingOrderingOfValuesWithinNode() {
		Tree t = new Tree();
		
		t.insert(10);
		t.insert(9);
		t.insert(9); // Proves we aren't inserting a duplicate.
		
		assertEquals(2, t.size());
		assertEquals(false, t.insert(10)); // further proves we don't insert duplicates.
	}
	

	
   @Test
   public void singleNodeTree()
   {
      Tree t = new Tree();
      t.insert(9);
      
      assertEquals(1, t.size(9));
      assertEquals(0, t.size(8));
      assertEquals(0, t.size(10));
      
      t.insert(15);
      assertEquals(2, t.size(9));
      assertEquals(0, t.size(8));
      assertEquals(0, t.size(10));
      assertEquals(2, t.size(15));
      assertEquals(0, t.size(18));

      t = new Tree();
      t.insert(15);
      t.insert(9);
      assertEquals(2, t.size(9));
      assertEquals(0, t.size(8));
      assertEquals(0, t.size(10));
      assertEquals(2, t.size(15));
      assertEquals(0, t.size(18));
      
      assertEquals(9, t.get(0));
      assertEquals(15, t.get(1));

   }
   
   

   @Test
   public void manualTestOfTreeUsingIllegalNodeAndTree() {
	   Tree t = new Tree();
	   Tree.Node node = t.new Node();
	   
	   
	   node.insert(0);
	   node.insert(1);
	   node.insert(2);
	   assertEquals(3, node.size());
	   /*
	   node.addVal(4);
	   
	   node.children[0] = t.new Node(2, node);
	   node.children[0].children[0] = t.new Node(1, node.children[0]);
	   node.children[0].children[2] = t.new Node(3, node.children[0]);
	   
	   node.children[2] = t.new Node(6, node);
	   node.children[2].addVal(9);
	   node.children[2].children[0] = t.new Node(5, node.children[2]);
	   node.children[2].children[1] = t.new Node(7, node.children[2]);
	   node.children[2].children[2] = t.new Node(10, node.children[2]);
	

	   assertEquals(9, node.size());
	   node.insert(8);
	   node.insert(8); // IT WORKS :)
	   assertEquals(node.children[2].children[1], node.find(7));
	   assertEquals(node.children[0].children[2], node.get(1));
	   */
	   //Node found = ;
	  // assertEquals(node.children[0].children[0], t.find(node, 1));
	  // assertEquals(null, t.find(node, 1000));
	   
	 //  assertEquals(new Integer(1), t.find(node, 1).getVal(1));
	   
	  // assertEquals(node.rtChild, t.find(node, 8));
	  // assertEquals(node.rtChild.midChild, t.find(node, 7)); // IT FOUND IT!!!!
   }
   /*
   Illegal tests that require node to be separate public/protected class. Requires private methods to be public. Requires illegal tree constructor.
   Used for testing real 23 trees since I can't get my split to work.
  
   */
	
	/*

   @Test
   public void oneSplitLeft()
   {
      Tree t = new Tree();
      t.insert(9);
      t.insert(15);
      t.insert(1);
      
      assertEquals(3, t.size(9));
      assertEquals(1, t.size(15));
      assertEquals(0, t.size(17));
      assertEquals(0, t.size(11));

      assertEquals(1, t.size(1));
      assertEquals(0, t.size(0));
      assertEquals(0, t.size(3));

      assertEquals(1, t.get(0));
      assertEquals(9, t.get(1));
      assertEquals(15, t.get(2));
      
      assertEquals(3,t.size());
   }
   
   @Test
   public void oneSplitRight()
   {
      Tree t = new Tree();
      t.insert(1);
      t.insert(9);
      t.insert(15);
      
      assertEquals(3, t.size(9));
      assertEquals(1, t.size(15));
      assertEquals(0, t.size(17));
      assertEquals(0, t.size(11));

      assertEquals(1, t.size(1));
      assertEquals(0, t.size(0));
      assertEquals(0, t.size(3));
      
      assertEquals(1, t.get(0));
      assertEquals(9, t.get(1));
      assertEquals(15, t.get(2));
      assertEquals(3,t.size());


   }

   @Test
   public void oneSplitMiddle()
   {
      Tree t = new Tree();
      t.insert(1);
      t.insert(15);
      t.insert(9);
      
      assertEquals(3, t.size(9));
      assertEquals(1, t.size(15));
      assertEquals(0, t.size(17));
      assertEquals(0, t.size(11));

      assertEquals(1, t.size(1));
      assertEquals(0, t.size(0));
      assertEquals(0, t.size(3));
      
      assertEquals(1, t.get(0));
      assertEquals(9, t.get(1));
      assertEquals(15, t.get(2));
      assertEquals(3,t.size());


   }

   
   @Test
   public void testDuplicates()
   {
      Tree t = new Tree();
      t.insert(1);
      t.insert(9);
      t.insert(15);
      t.insert(13);
      t.insert(20);
      t.insert(7);
      t.insert(4);
      t.insert(1);
      t.insert(9);
      t.insert(15);
      t.insert(1);
      t.insert(9);
      t.insert(15);
      t.insert(13);
      t.insert(20);
      t.insert(7);
      t.insert(4);
      t.insert(13);
      t.insert(20);
      t.insert(7);
      t.insert(4);

      assertEquals(7, t.size(9));
      assertEquals(3, t.size(4));
      assertEquals(3, t.size(15));

      assertEquals(0, t.size(12));
      assertEquals(1, t.size(13));
      assertEquals(0, t.size(14));
      assertEquals(0, t.size(19));
      assertEquals(1, t.size(20));
      assertEquals(0, t.size(21));

      assertEquals(1, t.size(1));
      assertEquals(0, t.size(0));
      assertEquals(0, t.size(3));

      assertEquals(0, t.size(6));
      assertEquals(1, t.size(7));
      assertEquals(0, t.size(8));
      
      assertEquals(1, t.get(0));
      assertEquals(4, t.get(1));
      assertEquals(7, t.get(2));
      assertEquals(9, t.get(3));
      assertEquals(13, t.get(4));
      assertEquals(15, t.get(5));
      assertEquals(20, t.get(6));
      assertEquals(7,t.size());

      

   }

*/
}