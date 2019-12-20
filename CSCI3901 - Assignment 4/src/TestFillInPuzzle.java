import java.util.ArrayList;
import java.util.Arrays;
import java.io.*;
public class TestFillInPuzzle {

	public static void main(String[] args) throws FileNotFoundException {
		// TODO Auto-generated method stub
		FillInPuzzle obj = new FillInPuzzle();		
		String filename = "F:\\Dal MACS Courses\\CSCI 3901 - Software Development Concepts\\Assignments\\Assignment 4\\Sample Puzzle 2.txt";
		PrintWriter outstream = new PrintWriter("Solved Puzzle.txt");
		BufferedReader fileRead = new BufferedReader(new FileReader(filename));

		obj.loadPuzzle(fileRead);		
		//System.out.print("Col: "+obj.m_noOfColumns +" Row: "+obj.m_noOfRows+ " Words: "+obj.m_noOfWords);
		obj.displayLoadedPuzzle();
		obj.solve();
		obj.displaySolvedPuzzle();
		obj.print(outstream);
		System.out.println("\n\nChoices made: "+obj.choices());
	}
}