
/*
 * ListHierarchy class provides the functionality to add and search string 
 * elements in a sorted string list. 
 */
public class ListHierarchy {
	
	//An attribute of ListHierarchy of the type Coin through
	//which either RandomCoin or ArrayCoin will be invoked, based
	//on the user input.
	Coin coinFlipMethod;	
	
	/*
	 * The objects of type Node, that is the inner private class of
	 * ListHierarchy.
	 */
	
	//Sentinel will be the first element of the list and tail 
	//will be the last element element.
	Node sentinel, tail;
	
	/*
	 * The following nested class provides the structure for the elements of the 
	 * list.
	 */
	private class Node{
		String key;
		Node next, prev, up, down;
		Node(String key){
			this.key = key;
			next = null;
			prev = null;
			up = null;
			down = null;
		}
	}
	
	//ListHierarchy constructor that accepts an object of type
	//coin.
	public ListHierarchy( Coin flip ) {
		coinFlipMethod = flip;
		sentinel = new Node("");
		tail = null;
	}
	
	
	//addKey method will be called by add method whenever it is invoked
	//to add a new element in the sorted list.
	private boolean addKey(Node sent, Node addedNode,String key){
		boolean flag = false;
		
		try{
			//If the list is either empty or the element is to be added
			//in the ground level.
			if(sentinel.next == null || addedNode == null){
				//If the list is empty and it's the first element to be added.
				if(sentinel.next == null){
					sentinel.next = new Node(key);
					tail = sentinel.next;
					tail.prev = sentinel;
					addedNode = tail;
					flag = true;
					}
				
				//If the list isn't empty and the key is greater than any element
				//of the list.
				else if((key.compareToIgnoreCase(tail.key))>0)
				{
					tail.next = new Node(key);
					tail.next.prev = tail;
					tail = tail.next;
					addedNode = tail;
					flag = true;
					
				}
				
				//If the list isn't empty and the new element is to be 
				//added in the middle of the sorted list.
				else{
					Node current = sentinel.next;
					int lexicalOrderCheck;
					
					while (true){
						lexicalOrderCheck = key.compareToIgnoreCase(current.key);
						
						// If the key that is to be added is found to be smaller than
						// the current node's key.
						if(lexicalOrderCheck < 0){
							
							
								Node temp = new Node(key);
								temp.next = current;
								temp.prev = current.prev;
								current.prev = temp;
								temp.prev.next = temp;
								flag = true;
								addedNode = temp;
								break;
							
							}
						//If the key that is to be added is found to be greater than
						// the current node's key.
						else{
							current = current.next;
							if(current == null){
								break;
							}
							}
						}
					
					}
				
				// Invoking the random method in order to decide whether to
				// add the element to the next level or not.
				int coinFlipOutcome = coinFlipMethod.flip();
				if(coinFlipOutcome == 1){
					//Recursively calling the addKey function with the sentinel
					//node of the current level and the newly added node along with
					//the key.
					addKey(sentinel, addedNode, key);
				}
			}
			
			//Following block of code will run whenever the new level has be 
			//created or the element is to be added in the upper level.
			else{
				Node newSent = sent.up, newAddedNode = addedNode.up;
				
				// In case the upper level doesn't exist.
				if(newSent == null){
					newSent = new Node("");
					newSent.down = sent;
					sent.up = newSent;
					
					newAddedNode = new Node(key);
					newAddedNode.down = addedNode;
					addedNode.up = newAddedNode;
					
					newSent.next = newAddedNode;
					newAddedNode.prev = newSent;
					flag = true;
				}
				
				//If the upper level already exists.
				else{
					Node current = newSent;
					int lexicalOrderCheck;
					while(flag != true){
						lexicalOrderCheck = key.compareToIgnoreCase(current.key);
						
						// If the key that is to be added is found to be smaller than
						// the current node's key.
						if(lexicalOrderCheck < 0){
							
							
								Node temp = new Node(key);
								temp.next = current;
								temp.prev = current.prev;
								current.prev = temp;
								temp.prev.next = temp;
								
								newAddedNode = temp;
								newAddedNode.down = addedNode;
								flag = true;						
							}
						
						//If the key that is to be added is found to be greater than
						// the current node's key.
						else{
							if(current.next == null){
							Node temp = new Node(key);
							current.next = temp;
							temp.prev = current;
							
							newAddedNode = temp;
							newAddedNode.down = addedNode;
							flag = true;
							}
							current = current.next;
						}										
					}
				}
				
				// Invoking the random method in order to decide whether to
				// add the element to the next level or not.
				int coinFlipOutcome = coinFlipMethod.flip();				
				if(coinFlipOutcome == 1){
					//Recursively calling the addKey function with the sentinel
					//node of the current level and the newly added node along with
					//the key.
					addKey(newSent, newAddedNode, key);
				}
			}
			
		}catch(Exception e){e.printStackTrace();}
		
		return flag;		
	}
	
	
	//The following method will be called whenever the user wants to add an
	//element in the sorted string list. This is just a wrapper function that 
	//further calls addKey method that performs the actual add operation.
	public boolean add( String key ) {
		Node addedNode = null;
		boolean flag = false;
		try{
			
			//Checking whether the user entered null as the new element.
			if(key == null){
				throw new NullKeyException();
			}
			
			//Checking whether the user entered blank character as the new element.
			if(key.equals("")){
				throw new BlankKeyException();
			}
			
			//This condition will check whether the list is empty or not.
			if(sentinel.next == null){
				flag = addKey(sentinel, addedNode, key);				
			}
			
			//If the list is not empty.
			else{
				
				//Calling the find method to check whether the element is
				//already in the list.
				boolean isPresent = find(key);				
				if(isPresent){
					throw new AddingExistingElementException(key);
				}
				else{
					flag = addKey(sentinel, addedNode, key);
				}				
			}
			
		}
		catch(BlankKeyException e){e.printStackTrace();}
		catch(NullKeyException e){e.printStackTrace();}
		catch(AddingExistingElementException e){e.printStackTrace();}
		catch(Exception e){e.printStackTrace();}		
		
		return flag;
	}
	
	
	//The following method can be used to check whether an element is present in
	//the list or not. If the element is present it returns 'true', else 'false'.
	boolean find( String key ) {
		boolean flag = false;
		
		try {
			Node current = sentinel.next;
			
			if(current == null){
				throw new SearchingInEmptyList();
			}
				
			
			while (current != null){
				if( key.equalsIgnoreCase(current.key) ){
					flag = true;
					break;
				}
				current = current.next;
			}

			
		}
		catch(SearchingInEmptyList e){e.printStackTrace();}
		catch(Exception e){e.printStackTrace();}
		
				
		return flag;
	}
	
	//Method to print the entire structure on the output screen.
	private void printStructure(Node sent){
		
		if(sent == null)
			return;
		
		Node s = sent;
		Node current = s.next;
		System.out.print("\n\n\n");
		while(current!=null){
			System.out.print("-> "+current.key+"  ");
			current = current.next;			
		}
		printStructure(s.up);		
	}
	
	//The wrapper function display can be invoked in order to display the elements
	//of the structure. The actual display operation is done by displayStructure.
	public void print(){		
		printStructure(sentinel);		
	}	
}

/*
 * The following are the classes that handle the
 * runtime exceptions that otherwise might cause the 
 * program to crash.
 */

//This class will print an exception message if the 
//user tries to enter a duplicate element in the list.
class AddingExistingElementException extends Exception{
	String key;
	AddingExistingElementException(String key){
		this.key = key;		
	}
	public String toString(){
		return "\nList already contains the key '"+key+"'. Add operation failed.\n";
	}		
}

//This class will print an exception message if the 
//user tries to search an element in an empty list.
class SearchingInEmptyList extends Exception{
	public String toString(){
		return "\nList is empty. Find operation failed.\n";
	}
}

//This class will print an exception message if the 
//user tries to add a blank element in the list.
class BlankKeyException extends Exception{
	public String toString(){
		return "\nTrying to add a blank key. Add operation failed.\n";
	}
}

//This class will print an exception message if the 
//user tries to add NULL in the list.
class NullKeyException extends Exception{
	public String toString(){
		return "\nTrying to add null in the list. Add operation failed.\n";
	}
}

