import java.io.*;
import java.util.*;

//The class that takes the puzzle as an input buffer and stores it in
//abstract data structure in order to transform it to give its solution.
public class FillInPuzzle {
	private static final int overlapFlag = 0;
	private static final int wrongLengthFlag = -1;
	private static final int filledFlag = 1;
	int m_noOfRows, m_noOfColumns, m_noOfWords, m_choices;
	
	//Two dimensional array to store the solved puzzle.
	char m_puzzleGridState[][];
	
	List m_wordsList = new ArrayList<String>();
	
	//One dimensional string array to store all the 
	//words of the puzzle.
	String m_wordsArray[];
	
	//Memoization list to store the previous bad choices in
	//the same level.
	List m_repeatedWordsMemoizationList = new ArrayList<String>();
	
	//HashMap to store the details of the puzzle from the given buffer.
	Map<Integer,FillIns> m_gridDetails= new HashMap<Integer,FillIns>();
	
	//HashMap that stores every single bad choices the code has made for
	//every grid line.
	Map<Integer, ArrayList<String>> m_triedWordsMemoizationMap = new HashMap<Integer,ArrayList<String>>();
	FillInPuzzle(){
		m_noOfRows = 0;
		m_noOfColumns = 0;
		m_noOfWords = 0;
		m_choices = 0;
	}
	
	//The object of this private nested class stores the details of
	//individual word of the puzzle.
	private class FillIns{
		int colNo;
		int rowNo;
		int wordLength;
		char direction;
		
		FillIns(int colNo, int rowNo, int wordLength, char direction){
			this.colNo = colNo;
			this.rowNo = rowNo;
			this.wordLength = wordLength;
			this.direction = direction;
		}
	}
	
	
	/* 
	 *Initializing the space state of the puzzle grid. Every block is
	 *initialized with '0'.
	 *
	*/
	private void initializePuzzleState(){
		try{
			for(int i = 0; i < m_noOfRows; i+=1){
				for(int j = 0; j < m_noOfColumns; j+=1){
					m_puzzleGridState[i][j] = '0';
				}
			}
		}catch(Exception e){e.printStackTrace();}
	}
	
	/*
	 * The state of blocks that will contain the alphabets is changed
	 *to '1'.
	 *	 *
	 */
	private void changePuzzleState(){
		FillIns obj;
		try{
			for(int i:m_gridDetails.keySet()){
				obj = m_gridDetails.get(i);
				char order = obj.direction;
				int length = obj.wordLength;
				
				int col = obj.colNo;
				int row = (m_noOfRows - 1) - obj.rowNo;
				
				switch(order){
				
				//For horizontal words.
				case('h'):
					for(int m = 0; m < length; m+=1){
						m_puzzleGridState[row][col++] = '1';
						}
				break;
				
				//For vertical words.
				case('v'):
					for(int m = 0; m < length; m+=1){
						m_puzzleGridState[row++][col] = '1';
						}
				break;
				default:
					break;
				}
			}
		}
		catch(Exception e){e.printStackTrace();}
	}
	
	/*
	 * The method to check whether or not the grid contains a word.
	 */
	private boolean checkGridState(int mapKey){
		boolean isFilled = true;
		try{
			FillIns obj = m_gridDetails.get(mapKey);
			int col = obj.colNo;
			int row = (m_noOfRows - 1) - obj.rowNo;
			char order = obj.direction;
			int length = obj.wordLength;
			switch(order){
			//For horizontal words.
			case('h'):
			for(int m = 0; m < length; m+=1){
				if(m_puzzleGridState[row][col++] == '1'){
					isFilled = false;
				}
			}
			break;
			
			//For vertical words.
			case('v'):
				for(int m = 0; m < length; m+=1){
					if(m_puzzleGridState[row++][col] == '1'){
						isFilled = false;
					}
				}
				break;
			}
		}
		catch(Exception e){e.printStackTrace();}
		return isFilled;
	}
	
	/*
	 * The following method accepts the description of the puzzle 
	 * as a buffer stream and parses it to get all the information,
	 * and then stores that information into various data structure.
	 */
	boolean loadPuzzle(BufferedReader stream){
		boolean puzzleLoaded = false;
		try{
			String str;
			int i = 0;
			while((str = stream.readLine()) != null){
				
				//Retrieving the number of columns and rows, and the
				//words in the puzzle.
				if(0 == i){
					String arr[] = str.split(" ");
					m_noOfColumns = Integer.parseInt(arr[0]);
					m_noOfRows = Integer.parseInt(arr[1]);
					m_noOfWords = Integer.parseInt(arr[2]);
					m_puzzleGridState = new char[m_noOfRows][m_noOfColumns];
					m_wordsArray = new String[m_noOfWords];
					initializePuzzleState();
				}
				
				//Retrieving the starting coordinate of a grid along
				//with the size of the word and the direction.
				else if (i>0 && i<=m_noOfWords){
					String[] temp = new String[4];
					temp = str.split(" ");
					m_gridDetails.put(i-1, 
							new FillIns(Integer.parseInt(temp[0]), Integer.parseInt(temp[1]), 
									Integer.parseInt(temp[2]), temp[3].charAt(0)));
					m_triedWordsMemoizationMap.put(i-1, new ArrayList<String>());
				}
				
				//Retrieving the words and storing them to an ArrayList.
				else{
					m_wordsList.add(str);
				}
				i+=1;
			}
			
			changePuzzleState();
			//Converting the array list to a string array.
			m_wordsArray = (String[]) m_wordsList.toArray(m_wordsArray); 
			puzzleLoaded = true;				
		}
		catch(Exception e){e.printStackTrace();}
		return puzzleLoaded;
	}
	
	/*
	 * The following method accepts the grid index as the map key
	 * and word index of the string array, and fills the word to the 
	 * grid.
	 */	
	private int fillIn(int mapKey, int wordsArrayIndex){
		int hasFilled = filledFlag;
		try{
			FillIns obj = m_gridDetails.get(mapKey);
			String str = m_wordsArray[wordsArrayIndex];
			int col = obj.colNo;
			int row = (m_noOfRows - 1) - obj.rowNo;

			char order = obj.direction;
			int length = obj.wordLength;
			
			//In case the word's length is less than the grid length.
			if(str.length() != obj.wordLength){
				hasFilled = wrongLengthFlag;
			}			
			else{	
				char[] wordAlphabets = new char[length];
				wordAlphabets = str.toCharArray();
				
				//Check whether the grid is already filled with a bad
				//choice.
				boolean isFilled = checkGridState(mapKey);
				switch(order){
				//For horizontal word.
				case('h'):
				//If the grid is already filled with a bad choice.
				if(isFilled){
					if(!m_triedWordsMemoizationMap.get(mapKey+1).isEmpty()){
						m_triedWordsMemoizationMap.get(mapKey+1).clear();
					}
					
					//Checking for the history of bad choices for this 
					//particular grid.
					
					//If this choice has been made before.
					if(m_triedWordsMemoizationMap.get(mapKey).contains(str)){
						hasFilled = overlapFlag;
					}
					
					//If this choice is new.
					else{
						char temp[] = new char[length];
						for(int m = 0; m < length; m+=1){
							temp[m] = m_puzzleGridState[row][col];
							m_puzzleGridState[row][col++] = wordAlphabets[m];
						}
						String tempStr = new String(temp);
						
						//Adding the new choices to the memoization table.
						m_triedWordsMemoizationMap.get(mapKey).add(tempStr);
						m_triedWordsMemoizationMap.get(mapKey).add(str);
					}
				}
				
				//If the grid is unexplored.
				else{
					for(int m = 0; m < length; m+=1){
						if(m_puzzleGridState[row][col] == '1'){
							m_puzzleGridState[row][col++] = wordAlphabets[m];
						}
						else{
							if(m_puzzleGridState[row][col] == wordAlphabets[m]){
								m_puzzleGridState[row][col++] = wordAlphabets[m];
							}
							else{
								col = obj.colNo;
								for(int l = 0; l < m; l+=1){
									m_puzzleGridState[row][col++] = '1';
									}
								hasFilled = overlapFlag;
								break;
							}
						}
					}
				}
				break;
				
				//For vertical word.
				case('v'):
					//If the grid is already filled with a bad choice.
					if(isFilled){
						if(!m_triedWordsMemoizationMap.get(mapKey+1).isEmpty()){
							m_triedWordsMemoizationMap.get(mapKey+1).clear();
						}
						
						//Checking for the history of bad choices for this 
						//particular grid.
						
						//If this choice has been made before.
						if(m_triedWordsMemoizationMap.get(mapKey).contains(str)){
							hasFilled = overlapFlag;
						}
						else{
							char temp[] = new char[length];
							for(int m = 0; m < length; m+=1){
								m_puzzleGridState[row++][col] = wordAlphabets[m];
							}
							String tempStr = new String(temp);
							
							//Adding the new choices to the memoization table.
							m_triedWordsMemoizationMap.get(mapKey).add(tempStr);
							m_triedWordsMemoizationMap.get(mapKey).add(str);
						}
						
					}
				
					//If the grid is unexplored.
					else{
						for(int m = 0; m < length; m+=1){
							if(m_puzzleGridState[row][col] == '1'){
								m_puzzleGridState[row++][col] = wordAlphabets[m];
							}
							else{
								if(m_puzzleGridState[row][col] == wordAlphabets[m]){
									m_puzzleGridState[row++][col] = wordAlphabets[m];
								}
								else{
									row = (m_noOfRows - 1) - obj.rowNo;
									for(int l = 0; l < m; l+=1){
										m_puzzleGridState[row++][col] = '1';
										}
									hasFilled = overlapFlag;
									break;
								}
							}
						}
					}			
				break;
				default:
					break;
				}
			}			
		}
		catch(Exception e){e.printStackTrace();}
		return hasFilled;
	}
	
	/*
	 * Method that actually transforms the puzzle and store the solution.
	 * The method accepts the grid index and the index of the word array.
	 * */
	private boolean solvePuzzle(int mapKey, int wordsArrayIndex){
		m_choices+=1;
		int hasFilled;
		boolean isPuzzleSolved = true;
		
		//The anchor condition for the recursive code.
		//If the entire puzzle is solved successfully.
		if(wordsArrayIndex == m_noOfWords && mapKey == m_noOfWords){
			return true;
		}		
		
		else{
			//Trying to fill in the word in the given grid index.
			hasFilled = fillIn(mapKey, wordsArrayIndex);
			
			//If the word is successfully filled. This sub-problem is 
			//solved, recursively jump of the next grid with the next word.
			if(filledFlag == hasFilled){
				m_repeatedWordsMemoizationList.clear();
				solvePuzzle(mapKey+1,wordsArrayIndex+1);
				m_repeatedWordsMemoizationList.clear();
			}			
			
			//If the word was a bad guess.
			if(filledFlag != hasFilled){
				//Trying to put another word into the same grid.
				if(!m_repeatedWordsMemoizationList.contains(m_wordsArray[wordsArrayIndex])){					
					m_repeatedWordsMemoizationList.add(m_wordsArray[wordsArrayIndex]);
					rotateWordsArray(wordsArrayIndex);
					solvePuzzle(mapKey,wordsArrayIndex);
				}
				
				//All words were bad choices for this grid indicating
				//that a bad choice has been made in the lower level.
				
				//Backtracking
				else{							
					rotateWordsArray(wordsArrayIndex - 1);
					m_repeatedWordsMemoizationList.clear();
					solvePuzzle(mapKey-1,wordsArrayIndex - 1);
					}	
				}
			}
	
		return isPuzzleSolved;
	}
	
	/*
	 * The wrapper function that is called by the user. It
	 * calls the solvePuzzle method defined above to actually solve 
	 * the puzzle.
	 *  */
	boolean solve(){
		boolean isSolved = false;
		try{
			isSolved = solvePuzzle(0,0);
		}
		catch(Exception e){e.printStackTrace();}
		return isSolved;
	}
	
	/*
	 * Method that prints the solution of the puzzle into 
	 * the output stream.
	 * */
	void print(PrintWriter outstream){		
		try{
			for(int i = 0; i < m_noOfRows; i+=1){
				for(int j = 0; j < m_noOfColumns; j+=1){
					if(m_puzzleGridState[i][j] != '0')
						outstream.write(m_puzzleGridState[i][j]+" ");
					else
						outstream.write("  ");
				}
				outstream.write("\n");
			}
			outstream.close();
		}
		catch(Exception e){e.printStackTrace();}
	}
	
	/*
	 * This method returns the number of choices made and
	 * the number of times backtracking was done.
	 * */
	int choices(){
		return m_choices;
	}
	
	/*
	 * The method to display the state space after the puzzle is loaded
	 * into the data structure.
	 * */
	void displayLoadedPuzzle(){
		System.out.print("\nLoaded Puzzle\n");
		for(int i:m_gridDetails.keySet()){
			System.out.print("\n"+m_gridDetails.get(i).colNo);
			System.out.print(" "+m_gridDetails.get(i).rowNo);
			System.out.print(" "+m_gridDetails.get(i).wordLength);
			System.out.print(" "+m_gridDetails.get(i).direction);
		}
		
		for(int i = 0; i< m_wordsArray.length; i+=1){
			System.out.print("\n"+m_wordsArray[i]);
		}		
	}
	
	/*
	 * The method to display the state space after the puzzle is
	 * successfully solved.
	 * */
	void displaySolvedPuzzle(){		
		System.out.print("\n\nSolved Puzzle\n");
		for(int i = 0; i < m_noOfRows; i+=1){
			System.out.println();
			for(int j = 0; j < m_noOfColumns; j+=1){
				if(m_puzzleGridState[i][j] != '0')
					System.out.print(m_puzzleGridState[i][j]+" ");
				else
					System.out.print("  ");
			}
		}
	}
	
	/*
	 * This method rotates the state of the array from the given index.
	 * This is done so the the previously made bad choices can be pushed
	 * to the end of the array.
	 * */
	void rotateWordsArray(int index){
		String temp = m_wordsArray[index];
		int i = index;
		for(; i < m_noOfWords-1; i+=1){
			m_wordsArray[i] = m_wordsArray[i+1];			
		}
		m_wordsArray[i] = temp;
	}
}
