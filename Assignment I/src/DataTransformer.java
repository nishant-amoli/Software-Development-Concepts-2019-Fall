import java.io.*;
import java.util.*;

public class DataTransformer{
		
	//A separate list that contains the name of all columns in the file. 
	//Every newly added column will be added to the list.
	ArrayList<String> columnNameList;
	
	//While invoking the calculate() method, the equation will be parsed and stored
	//in this string array.
	String equationDictionary[] = new String[5];
	
	//A hash map object to store the rows of the file.
	//The rows will be stored as the values.
	HashMap <Integer, List<String>> mapObj= new HashMap<Integer, List<String>>();
	
	/*
	 * Method to clear the data from the object.
	 * Returns true in case the object is empty after the operation.
	 */	
	boolean clear(){
		boolean isObjectEmpty = false;
		try{			
		if(mapObj.isEmpty()){
				throw new ClearingEmptyObjectException();			
		}
		mapObj.clear();
		isObjectEmpty = true;
		}
		catch(ClearingEmptyObjectException e)
		{
			e.printStackTrace();
		}
		return isObjectEmpty;
	}
	
	
	/*
	 * Method to read in the contents of the file to the object. Returns the 
	 * number of the data rows.
	 */
	Integer read(String filename){
		int rowCount = 0;
		try
		{
			if(filename == ""){
				try{
					throw new ReadingBlankFileNameException();
				}catch(Exception e){e.printStackTrace();}
				
			} else if(filename == null){
				try{
					throw new ReadingNullFileNameException();
				}catch(Exception e){e.printStackTrace();}
			} else {
				File fileObj = new File(filename);
				if(fileObj.exists()){
					BufferedReader fileRead = new BufferedReader(new FileReader(filename));
					String str;
					int i = 0;
					int noLinesCheck = -1;
					while((str = fileRead.readLine()) != null){
						noLinesCheck = 0;					
						mapObj.put(i, new ArrayList<String>(Arrays.asList(str.split("\t"))));
						
						if(rowCount == 0){
							columnNameList = new ArrayList<String>(Arrays.asList(str.split("\t")));
						}						
						rowCount += 1;
						i += 1;
					}	
					if(noLinesCheck == -1){
						System.out.print("\nThe given file contains 0 lines.\n");
					}
					fileRead.close();
				} else{
					System.out.print("\nFile doesn't exist. Read operation failed.\n");
				}						
			}
		}
		catch(Exception ex){
			System.out.println(ex.toString());
		}
		return rowCount;
	}
	
	
	/*
	 * The following method creates a new column with the given name. 
	 * Returns True if the new column is successfully created.
	 */
	boolean newColumn(String columnName){
		boolean flag = false;
		try
		{
			if(columnName == null){
				flag = false;
				throw new NullColumnNameException();
			}
			
			if(columnName.equals("")){
				flag = false;
				throw new InsertingBlankColumnNameException();
			}
			
			if(columnNameList == null){
				columnNameList = new ArrayList<String>();
			}
			
			//Checking whether any column with the same name exists.
			if(!mapObj.isEmpty()){
				for(String existedColumn: columnNameList){
					if(columnName.equalsIgnoreCase(existedColumn)){
						System.out.print("\nColumn '"+columnName+"' already exists!");
						flag = false;
					}
				}
			}
			
			//Adding a new column in case the object is empty.
			if(mapObj.isEmpty()){
				ArrayList<String> arr = new ArrayList<String>();
				arr.add(columnName);
				columnNameList.add(columnName);
				mapObj.put(0,arr);
				flag = true;
			}		
			
			
			for(int key : mapObj.keySet()){
				if(key == 0){
					mapObj.get(key).add(columnName);
					columnNameList.add(columnName);
					flag = true;
				}
					
				if(key > 0)
					mapObj.get(key).add("0");
			}		
		}
		catch(NullColumnNameException e){
			e.printStackTrace();
		}
		catch(InsertingBlankColumnNameException e){
			e.printStackTrace();
		}
		catch(Exception ex){
			System.out.println(ex.toString());
			//return false;
		}
		return flag;
	}
	
	
	/*
	 * The following methods take in an equation and apply it to the data in the object.
	 * Returns the number of rows that were calculated for the received equation.
	 */
	int calculate(String equation){
		int calculatedRowCount = 0;
		try
		{
			equationDictionary = equation.split(" ");
			
			int derivedColumnIndex = -1, operandColumnIndex1= -1, operandColumnIndex2= -1,
					index = -1;
			float derivedColumnValue = -1, operandColumnValue1 = -1, operandColumnValue2 = -1;
			for(String x : columnNameList){
				index += 1;
				if(x.equalsIgnoreCase(equationDictionary[0]))
					derivedColumnIndex = index;
				if(x.equalsIgnoreCase(equationDictionary[2]))
					operandColumnIndex1 = index;
				if(x.equalsIgnoreCase(equationDictionary[4]))
					operandColumnIndex2 = index;
				}		
				
			
			for(int key : mapObj.keySet()){
				if(key == 0)
					continue;
				
				index = -1;
				for(String x : mapObj.get(key)){
					index += 1;
					if(index == 0)
						continue;
					
					try{
						if(index == operandColumnIndex1){
							operandColumnValue1 = Float.parseFloat(x);
						}
						if(index == operandColumnIndex2){
							operandColumnValue2 = Float.parseFloat(x);
						}					
					} catch(Exception e){e.printStackTrace();};
					
				}
				
				if(operandColumnIndex1 == -1){
					operandColumnValue1 = Float.parseFloat(equationDictionary[2]);
				}
				if(operandColumnIndex2 == -1){
					operandColumnValue2 = Float.parseFloat(equationDictionary[4]);
				}
	
				
				switch(equationDictionary[3]){
					case "+" : 
						derivedColumnValue = operandColumnValue1 + operandColumnValue2;
						break;
					case "-" : 
						derivedColumnValue = operandColumnValue1 - operandColumnValue2;
						break;
					case "*" : 
						derivedColumnValue = operandColumnValue1 * operandColumnValue2;
						break;
					case "/" : 
						derivedColumnValue = operandColumnValue1 / operandColumnValue2;
						break;
				}
			
				index = -1;
				for(String x : mapObj.get(key)){
					index += 1;
					
					if(index == derivedColumnIndex){
						int temp;
						derivedColumnValue = Math.round(derivedColumnValue);
						temp = (int) derivedColumnValue;
						mapObj.get(key).set(index, Integer.toString(temp));
						calculatedRowCount += 1;
					}
				}				
			}
		}
		catch(Exception ex){
			System.out.println(ex.toString());
		}
		return calculatedRowCount;
	}
	
	
	/*
	 * Method to print the first 5 rows of the object.
	 */
	void top(){
		try
		{
			if(mapObj.isEmpty()){
				try{
					throw new ReadingEmptyObjectException();
				}
				catch (ReadingEmptyObjectException e){
					e.printStackTrace();		
				}
			}		
			int tempCount = 1;
			System.out.println();
			for(int key : mapObj.keySet()){
				//System.out.print("Key : "+key + " : ");
				for(String attr : mapObj.get(key)){
					System.out.print(attr);
					System.out.print("\t");
				}
				System.out.println();
				if(tempCount == 5)
					break;
				tempCount += 1;
			}
		}
		catch(Exception ex){
			System.out.println(ex.toString());
		}
	}
	
	
	/*
	 * Method to print all rows to the screen.
	 */
	void print(){
		try
		{
		if(mapObj.isEmpty()){
			try{
				throw new ReadingEmptyObjectException();
			}
			catch (ReadingEmptyObjectException e){
				e.printStackTrace();				
			}
		}
		System.out.println();

		for(int key : mapObj.keySet()){
			for(String attr : mapObj.get(key)){
				System.out.print(attr);
				System.out.print("\t");
			}
			System.out.println();
		}
		}catch(Exception ex){
			System.out.println(ex.toString());
		}
		
	}
	
	
	/*
	 * The following method writes the contents of the object to the given file. 
	 */
	int write(String fileName) throws IOException{
		int dataRowCount = 0;
		FileWriter fileWrite = new FileWriter(fileName);		
		
			if(mapObj.isEmpty()){
				try{
					throw new WritingEmptyObjectException();
				}
				catch (WritingEmptyObjectException e){
					e.printStackTrace();
					}
		}				
		

		for(int key : mapObj.keySet()){
			for(String attr : mapObj.get(key)){
				fileWrite.write(attr);
				fileWrite.write("\t");
			}
			fileWrite.write("\n");
			dataRowCount += 1;
		}		
		fileWrite.close();
		return dataRowCount;
	}
}

/*
 * Following are the exceptions that may arrive while calling
 * the methods inside DataTransformer class.
 * */

//Exception generated while trying to print an empty DataTransformer object.
class ReadingEmptyObjectException extends Exception{
	public String toString()
	{
		return "\nError. Trying to print an empty object.\n";
	}
}

//Exception generated while trying to write empty DataTransformer object to the file.
class WritingEmptyObjectException extends Exception{
	public String toString()
	{
		return "\nError. Trying to write from an empty object.\n";
	}
}

//Exception generated while trying to clear an empty DataTransformer object.
class ClearingEmptyObjectException extends Exception{
	public String toString()
	{
		return "\nError. Trying to invoke CLEAR method on an empty object.\n";
	}
}

//Exception generated while trying to invoke read() with a blank string.
class ReadingBlankFileNameException extends Exception{
	public String toString()
	{
		return "\nBlank file name. Read operation failed.\n";
	}
}

//Exception generated while trying to invoke read() with null.
class ReadingNullFileNameException extends Exception{
	public String toString()
	{
		return "\nNULL as the file name. Read operation failed.\n";
	}
}

//Exception generated while trying to insert a blank column name.
class InsertingBlankColumnNameException extends Exception{
	public String toString()
	{
		return "\nTrying to insert a blank column name. Add column operation failed.\n";
	}
}
//Exception generated while trying to invoke newColumn method with null.
class NullColumnNameException extends Exception{
	public String toString()
	{
		return "\nCalumn name can not be null. Add column operation failed.\n";
	}
}

