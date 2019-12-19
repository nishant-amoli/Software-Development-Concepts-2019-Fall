import java.util.*;

/*
 * This class accepts the coordinates to add on the map and
 * also takes the current and destination coordinate points
 * to print the coordinates following the shortest path between them.
 */
public class HalifaxMap {
	
	private ArrayList <Coordinate> cartesianSystem;
	private HashMap <Integer, ArrayList<IndexCostNode>> roadMapObj;
	private ArrayList <Vertex> path;
	private ArrayList <Coordinate> shortestPath;
	private static final int setPermanent = 1;
	private static final int isTemporary = 0;

	//Class designed to store the coordinates into an object.
	private class Coordinate{
		int x, y;
		Coordinate(int x, int y){
			this.x = x;
			this.y = y;
		}
	}
	
	//Class designed to store the coordinate names and the traversing cost into an object.
	private class IndexCostNode{
		int index;
		int cost;
		IndexCostNode(int index, int cost){
			this.index = index;
			this.cost = cost;
		}
	}
	
	//Class designed to store the information of a point while creating the 
	//shortest path.
	private class Vertex{
		int destinationPoint, cost,routePoint, routeState;
		Vertex(int destinationPoint,int cost,int routePoint,int routeState){
			this.destinationPoint = destinationPoint;
			this.cost = cost;
			this.routePoint = routePoint;
			this.routeState = routeState;			
		}
	}
	
	//Method that takes the coordinates from the user and store them
	//into the Cartesian system.
	boolean newIntersection(int x, int y){
		boolean acknowledged = false;
		try{
			
			//Adding the first point to the Cartesian system.
			if(null == cartesianSystem){
				cartesianSystem = new ArrayList<Coordinate>();
				cartesianSystem.add(new Coordinate(x,y));
				acknowledged = true;
			}
			//Adding a point to the existing Cartesian system.
			else{
				//Checking whether the coordinates already exist
				//in the Cartesian system.
				if(checkCoordinates(x,y)){
					throw new CoordinatesExistInSystemException(x,y);										
				}
				
				//Adding new point in the Cartesian system.
				else{
					cartesianSystem.add(new Coordinate(x,y));
					acknowledged = true;
				}
				
			}
			
		}
		catch(CoordinatesExistInSystemException e){e.printStackTrace();}
		catch(Exception e){e.printStackTrace();}
		
		return acknowledged;
	}
	
	//Method that accepts the coordinates of two points and create an edge 
	//between them.
	boolean defineRoad(int x1, int y1, int x2, int y2){
		boolean isNew = false;
		int indexPointOne, indexPointTwo;
		try{
			
			if(cartesianSystem == null){
				throw new EmptyCartesianPlaneException();				
			}
			else{
				indexPointOne = getIndex(x1,y1);
				indexPointTwo = getIndex(x2,y2);
				
				//Check whether the points exist in the Cartesian System.
				if(!(checkCoordinates(x1,y1) && checkCoordinates(x2,y2))){
					throw new CoordinatesDontExistInSystemException();				
				}
				
				else if(indexPointOne == indexPointTwo){
					throw new SameInitialAndDestinationPoint();
				}
				//Check whether the road has already been constructed between the given points. 
				else if(doesRoadExist(x1, y1, x2, y2)){
					throw new RoadAlreadyDefinedException(x1, y1, x2, y2);				
				}
				
				//Construct the road between the two given points.
				else{				
					//In case the map containing the adjacency list is empty.
					if(null == roadMapObj){
						roadMapObj = new HashMap<Integer, ArrayList<IndexCostNode>>();
						roadMapObj.put(indexPointOne, new ArrayList<IndexCostNode>());
						roadMapObj.put(indexPointTwo, new ArrayList<IndexCostNode>());
						roadMapObj.get(indexPointOne).add(new IndexCostNode(
								indexPointTwo,calculateDistance(x1,y1,x2,y2)));
						roadMapObj.get(indexPointTwo).add(new IndexCostNode(
								indexPointOne,calculateDistance(x1,y1,x2,y2)));					
					}
					
					//If the map containing the adjacency list already exists.
					else{
						
						//If both the points have been mapped before.
						if(roadMapObj.containsKey(indexPointOne) && roadMapObj.containsKey(indexPointTwo)){
							roadMapObj.get(indexPointOne).add(new IndexCostNode(
									indexPointTwo,calculateDistance(x1,y1,x2,y2)));
							roadMapObj.get(indexPointTwo).add(new IndexCostNode(
									indexPointOne,calculateDistance(x1,y1,x2,y2)));	
						}
						
						//if just indexPointOne has been mapped before.
						else if(roadMapObj.containsKey(indexPointOne)){
							roadMapObj.get(indexPointOne).add(new IndexCostNode(
									indexPointTwo,calculateDistance(x1,y1,x2,y2)));
							
							roadMapObj.put(indexPointTwo, new ArrayList<IndexCostNode>());
							roadMapObj.get(indexPointTwo).add(new IndexCostNode(
									indexPointOne,calculateDistance(x1,y1,x2,y2)));	
							
						}
						
						//if just indexPointTwo has been mapped before.
						else if(roadMapObj.containsKey(indexPointTwo)){
							roadMapObj.get(indexPointTwo).add(new IndexCostNode(
									indexPointOne,calculateDistance(x1,y1,x2,y2)));
							
							roadMapObj.put(indexPointOne, new ArrayList<IndexCostNode>());
							roadMapObj.get(indexPointOne).add(new IndexCostNode(
									indexPointTwo,calculateDistance(x1,y1,x2,y2)));	
						}
						
						//if none of the points have been mapped before.
						else{
							roadMapObj.put(indexPointOne, new ArrayList<IndexCostNode>());
							roadMapObj.put(indexPointTwo, new ArrayList<IndexCostNode>());
							roadMapObj.get(indexPointOne).add(new IndexCostNode(
									indexPointTwo,calculateDistance(x1,y1,x2,y2)));
							roadMapObj.get(indexPointTwo).add(new IndexCostNode(
									indexPointOne,calculateDistance(x1,y1,x2,y2)));	
						}
						
					}
					
									
				}
			}
			
		}
		catch(EmptyCartesianPlaneException e){e.printStackTrace();}
		catch(CoordinatesDontExistInSystemException e){e.printStackTrace();}
		catch(SameInitialAndDestinationPoint e){e.printStackTrace();}
		catch(RoadAlreadyDefinedException e){e.printStackTrace();}
		catch(Exception e){e.printStackTrace();}
		return isNew;
	}
	
	//Method that lets the user to input the coordinates of two points
	//and print the shortest path between them.
	void navigate(int x1, int y1, int x2, int y2){
		try{
			if(cartesianSystem == null){
				throw new EmptyCartesianPlaneException();
			}
			else{
				
				// If the user tries to navigate the coordinates that don't exist in the map.
				if( !(checkCoordinates(x1,y1) && checkCoordinates(x2,y2))){
					throw new CoordinatesDontExistInSystemException();
				}
				
				
				
				else if(getIndex(x1,y1) == getIndex(x2,y2)){
					throw new SameInitialAndDestinationPoint();
				}
				else if(roadMapObj == null){
					System.out.print("\nNo path.\n");
				}
				
				else if (roadMapObj.containsKey(getIndex(x1,y1)) == false){
					System.out.print("\nNo path.\n");
				}
				
				else{
					int pointOneIndex = getIndex(x1,y1);
					int pointTwoIndex = getIndex(x2,y2);
					createPath(x1,y1);				
					for(int i = 0; i < cartesianSystem.size()-1; i+=1){
						Vertex lowestCostNode = getLowestCostNode();
						if(lowestCostNode == null){
							break;
						}
						modifyPath(lowestCostNode, pointOneIndex);
					}	
					printShortestPath(x1, y1, x2, y2);				
				}
			}
		}
		catch(EmptyCartesianPlaneException e){e.printStackTrace();}
		catch(SameInitialAndDestinationPoint e){e.printStackTrace();}
		catch(CoordinatesDontExistInSystemException e){e.printStackTrace();}
		catch(Exception e){e.printStackTrace();}		
	}
	
	//Method to calculate the distance between the two points.
	private int calculateDistance(int x1, int y1, int x2, int y2){
		double distance = 0;
		try{
			//Distance formula to calculate the shortest distance
			//between the two points.
			distance = Math.sqrt(Math.pow((x2 - x1), 2)+ 
					Math.pow((y2 - y1), 2));
			
			distance = Math.round(distance);			
		}
		catch(Exception e){e.printStackTrace();}
		return (int)distance;
	} 
	
	//Method to check whether the coordinates exist inside the
	//Cartesian system.
	private boolean checkCoordinates (int x, int y){
		boolean doesExist = false;
		try{
			for(Coordinate obj:cartesianSystem){
				if(obj.x == x && obj.y == y){
					doesExist = true;
				}								
			}
		}
		catch(Exception e){e.printStackTrace();}
		return doesExist;
	}
	
	//Method to print the coordinates added by the user.
	void printCoordinates(){
		try{
			if(null != cartesianSystem){
				for(Coordinate obj:cartesianSystem){
					System.out.print("("+obj.x+","+obj.y+")\t");
				}				
			}
		}
		catch(Exception e){e.printStackTrace();}
	}
	
	//Method that takes the coordinates and returns their index
	//in the list of Cartesian system.
	int getIndex(int x, int y){
		int index = -1;
		try{
			int countIndex = 0;
			for(Coordinate obj:cartesianSystem){
				if(obj.x == x && obj.y == y){
					index = countIndex;
				}
				countIndex += 1;
			}
		}
		catch(Exception e){e.printStackTrace();}
		return index;		
	}
	
	//Method that takes the index of the Cartesian system list and 
	//returns an object containing the index point's coordinates.
	private Coordinate getCoordinates(int index){
		int countIndex = 0;
		Coordinate coordinateObj = null;
		try{
			for(Coordinate obj:cartesianSystem){
				if(index == countIndex){
					coordinateObj = obj;
				}						
				countIndex += 1;
			}
		}
		catch(Exception e){e.printStackTrace();}
		return coordinateObj;
	}
	
	//Method to print the adjacency list. The nodes will be in the form of 
	//name of the points in the Cartesian system.
	void printAdjacencyListIndices(){
		try{
			if(roadMapObj != null){
				for(int key:roadMapObj.keySet()){
					System.out.print("\n"+key + " :\t");
					for(IndexCostNode obj:roadMapObj.get(key)){
						System.out.print(obj.index+" Cost : "+obj.cost+"\t");
					}
					
				}
			}			
		}
		catch(Exception e){e.printStackTrace();}
	}
	
	//Method to print the adjacency list. The nodes will be in the form of 
	//coordinates of the points in the Cartesian system.
	void printAdjacencyListCoordinates(){
		Coordinate ob;
		try{
			if(roadMapObj != null){
				for(int key:roadMapObj.keySet()){
					ob = getCoordinates(key);
					System.out.print("\n("+ob.x+","+ob.y+") :\t");
					for(IndexCostNode obj:roadMapObj.get(key)){
						ob = getCoordinates(obj.index);
						System.out.print("("+ob.x+","+ob.y+") Cost : "+obj.cost+"\t");
					}					
				}
			}
			
		}
		catch(Exception e){e.printStackTrace();}
	}
	
	//Method to check the existence of the edge between two Cartesian points.
	private boolean doesRoadExist(int x1, int y1, int x2, int y2){
		boolean roadExists = false;
		try{
			if(null != roadMapObj){
				int indexPointOne = getIndex(x1, y1);
				int indexPointTwo = getIndex(x2, y2);
				if(roadMapObj.containsKey(indexPointOne)){
					for(IndexCostNode obj:roadMapObj.get(indexPointOne)){
						if(obj.index == indexPointTwo){
							roadExists = true;
						}
					}
				}				
			}			
		}
		catch(Exception e){e.printStackTrace();}
		return roadExists;
	}
	
	//Method to create the shortest path (a part of 
	//Djikstra's algorithm).
	private void createPath(int x1, int y1){
		try{
			int startingIndex = getIndex(x1, y1);
			
			if(roadMapObj == null){
				System.out.print("\nRoad Not Defined!\n");
			}
			else if(!(roadMapObj.containsKey(startingIndex))){
				System.out.print("\nRoad Not Defined!\n");
			}
			else{
				path = new ArrayList<Vertex>();
				for(IndexCostNode obj:roadMapObj.get(startingIndex)){
					path.add(new Vertex(obj.index, obj.cost, startingIndex, isTemporary));
				
			}
			}
		}
		catch(Exception e){e.printStackTrace();}		
	}
	
	//Method to modify the path in case a new shortest path has
	//been discovered (a part of Djikstra's algorithm).
	private void modifyPath(Vertex lowestCostNode, int startingIndex){
		try{
			for(IndexCostNode obj:roadMapObj.get(lowestCostNode.destinationPoint)){
				int isPresent = 0;
				for(Vertex ob:path){
					if(ob.destinationPoint == obj.index){
						
						if(ob.cost > (obj.cost + lowestCostNode.cost)){
							ob.routePoint = lowestCostNode.destinationPoint;
							ob.cost = (obj.cost + lowestCostNode.cost);
						}
							
						isPresent = 1;
						break;
					}	
				}
				if(isPresent == 0 && obj.index != startingIndex){
					path.add(new Vertex(obj.index, lowestCostNode.cost+obj.cost, lowestCostNode.destinationPoint, isTemporary));
				}				
			}
			lowestCostNode.routeState = setPermanent;
		}
		catch(Exception e){e.printStackTrace();}
	}
	
	//Method to determine the lowest cost route (a part of 
	//Djikstra's algorithm).
	private Vertex getLowestCostNode(){
		Vertex lowestCostNode = null;
		int minCost = 9999;
		try{
			for(Vertex obj:path){
				if((obj.cost < minCost) && (obj.routeState == isTemporary)){
					lowestCostNode = obj;
				}
			}			
		}
		catch(Exception e){e.printStackTrace();}
		return lowestCostNode;
	}
	
	//Method to check whether the coordinates are present inside
	//the shortest path.
	private boolean checkCoordinatesInPath(int x, int y){
		boolean doesExist = false;
		try{
		int index = getIndex(x,y);	
		for(Vertex obj:path){
			if(obj.destinationPoint == index){
				doesExist = true;
			}
		}
		}
		catch(Exception e){e.printStackTrace();}	
		return doesExist;
	}
	
	//The method called by Navigate method to print the short path
	//discovered between the given two points.
	private void printShortestPath(int x1, int y1, int x2, int y2){
		try{
			
			boolean doesPathExist = checkCoordinatesInPath(x2,y2);
			if(doesPathExist != true){
				System.out.println();
				System.out.print("No Path.");
				System.out.println();
			}
			
			else{
				shortestPath = new ArrayList<Coordinate>();
				Vertex iterator = null;
				int destinationIndex = getIndex(x2,y2);
				int startingIndex = getIndex(x1, y1);
				for(Vertex obj:path){
					if(obj.destinationPoint == destinationIndex){
						iterator = obj;
					}
				}
				int stopIteration  = 0;
				while(iterator.destinationPoint != startingIndex && stopIteration == 0){
					
					for(Vertex obj:path){
						if(obj.destinationPoint == iterator.routePoint){
							shortestPath.add(getCoordinates(iterator.destinationPoint));
							iterator = obj;
						}
						if(iterator.routePoint == startingIndex){
							stopIteration = 1;
						}
				}
				
			}
				if(iterator.destinationPoint != startingIndex){
					shortestPath.add(getCoordinates(iterator.destinationPoint));
				}
				shortestPath.add(getCoordinates(startingIndex));				
				
				System.out.print("\n\nShortest path:\n\n");
				
				int index = shortestPath.size() - 1;
				
				Coordinate obj;
				for(; index >= 0; index-=1){
					obj = shortestPath.get(index);
					System.out.print(obj.x+"\t"+obj.y+"\n");
				}
			}	
			
		}
		catch(Exception e){e.printStackTrace();}
	}
}

/*
 * The following are the exceptions thrown if the user tries to
 * use illegal operations while using HalifaxMap class.
 */

//Exception thrown when user tries to add existing coordinates
//to the Cartesian system.
class CoordinatesExistInSystemException extends Exception{
	int x, y;
	CoordinatesExistInSystemException(int x, int y){
		this.x = x;
		this.y = y;
	}
	public String toString(){
		return "\nThe Cartesian system already contains the co-ordinates ("+
	x +","+y+"). Operation failed.\n";
	}
}
//Exception thrown when user tries to define roads before the coordinates
//the coordinates are introduced into the Cartesian system.
class EmptyCartesianPlaneException extends Exception{
	public String toString(){
		return "\nThe Cartesian system doesn't contain any"+
	" co-ordinates. Operation failed.\n";
	}
}
//Exception thrown when user tries to define roads on the coordinates
//that are not present in the existing Cartesian system.
class CoordinatesDontExistInSystemException extends Exception{
	public String toString(){
		return "\nThe Cartesian system doesn't contain the input"+
	" co-ordinates. Operation failed.\n";
	}
}

//Exception thrown when user tries to enter the same coordinates to find
//the shortest path.
class SameInitialAndDestinationPoint extends Exception{
	public String toString(){
		return "\nInitial and destination points can't be same.\n";
	}
}

//Exception thrown when user tries to define the road that has
//already been defined.
class RoadAlreadyDefinedException extends Exception{
	int x1, y1, x2, y2;
	RoadAlreadyDefinedException(int x1, int y1, int x2, int y2){
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
	}
	public String toString(){
		return "\nRoad already been defined between ("+x1+","+y1+") and "
				+ "("+x2+","+y2+"). Operation failed.\n";
	}
}

