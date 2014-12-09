import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;

/**
 * 
 */

/**
 * @author Dany
 *
 */
public class ConstructSudoku {

	static HashMap<Integer, ArrayList<String>> propertiesPrimeMap=null;
	static HashMap<Integer, ArrayList<String>> propertiesOddMap=null;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub


		new ConstructSudoku().constructProblem();
	}

	public void constructProblem()
	{
		String[] pInput = {"014","023","037","069","088","125", "143" ,"211" ,"263" ,"306" ,"342", "357" ,"404" ,"427" ,"461" ,"483" ,"535" ,"544" ,"589" ,"622" ,"673" ,"745" ,"764" ,"805" ,"824" ,"851" ,"862" ,"876"};

		HintsByPrimeNonPrimeHeuristic jClientObj = new HintsByPrimeNonPrimeHeuristic();
		HintsByOddEvenHeuristic jOddEvenObj = new HintsByOddEvenHeuristic();
		propertiesPrimeMap = jClientObj.getNumProperties();
		propertiesOddMap = jOddEvenObj.getNumProperties();
		
		SudokuSolver solver = new SudokuSolver();
		int[][] matrix = parseProblem(pInput);


		writeMatrix(matrix);

		if (SudokuSolver.solve(0,0,matrix))    // solves in place
		{
			writeMatrixByHints(matrix);
			System.out.println("   FINAL RESULTS");
			writeMatrix(matrix);
		}
		else 
			System.out.println("NONE");
	}

	static int[][] parseProblem(String[] input) {
		int[][] problem = new int[9][9]; // default 0 vals
		for (int n = 0; n < input.length; ++n) {
			int i = Integer.parseInt(input[n].substring(0,1));   
			int j = Integer.parseInt(input[n].substring(1,2));   
			int val = Integer.parseInt(input[n].substring(2,3)); 
			problem[i][j] = val;
		}
		return problem;
	}

	static void writeMatrixByHints(int[][] solution) {    	
		Scanner scanner = new Scanner(System.in);
		System.out.println("================================================================");

		boolean ansPrinted = false;
		for (int i = 0; i < 9; ++i) {
			if (i % 3 == 0)
				//System.out.println(" -----------------------");
				for (int j = 0; j < 9; ++j) {
					//if (j % 3 == 0) System.out.print("| ");
					String val = solution[i][j] == 0? " ": Integer.toString(solution[i][j]);
					int res = Integer.parseInt(val);
					//System.out.print("["+i+","+j+"]:"+res);
					ArrayList<String> primeProperties = new ArrayList<String>();
					ArrayList<String> oddProperties = new ArrayList<String>();

					if( propertiesPrimeMap.containsKey(res) )
					{
						primeProperties = propertiesPrimeMap.get(res);
					}
					
					if( propertiesOddMap.containsKey(res) )
					{
						oddProperties = propertiesOddMap.get(res);
					}

					System.out.println("Enter the Answer for ["+i+","+j+"]:");
					int num = scanner.nextInt();
					if(num == res)
					{
						System.out.println("    CORRECT ANSWER");
					}else
					{
						System.out.println("    Wrong Answer. Let me give some hints");
						System.out.println("    ------------------------------------");
						System.out.println("    PRIME HEIURISTIC\t\t\t|\t\tODD EVEN HEURISTIC");

						
						Iterator<String> primItr = primeProperties.iterator();
						Iterator<String> oddItr = oddProperties.iterator();
						String primeProp="",oddProp="";
						while(primItr.hasNext()||oddItr.hasNext())
						{
							if(primItr.hasNext())
								primeProp = primItr.next();
							if(oddItr.hasNext())
								oddProp = oddItr.next();
							
							if(num != res)
							{
								System.out.println("    "+primeProp+"\t\t|\t\t"+oddProp);
								num = scanner.nextInt();
							}else
							{
								System.out.println("    CORRECT ANSWER");
								ansPrinted = true;
								break;
							}
							
							primeProp = "";
							oddProp = "";
						}

						/*for(String prop : primeProperties)
						{
							if(num != res)
							{
								System.out.println("    "+prop);
								num = scanner.nextInt();
							}else
							{
								System.out.println("    CORRECT ANSWER");
								ansPrinted = true;
								break;
							}
						}*/


						if(num !=res)
						{
							System.out.println("    CORRECT ANSWER IS "+res);

						}
						else if(!ansPrinted)
						{
							System.out.println("    CORRECT ANSWER");
						}
					}
					ansPrinted = false;
				}
		}
		System.out.println("================================================================");
	}

	static void writeMatrix(int[][] solution) {
		for (int i = 0; i < 9; ++i) {
			if (i % 3 == 0)
				System.out.println(" -----------------------");
			for (int j = 0; j < 9; ++j) {
				if (j % 3 == 0) System.out.print("| ");
				System.out.print(solution[i][j] == 0
						? " "
								: Integer.toString(solution[i][j]));

				System.out.print(" ");
			}
			System.out.println("|");
		}
		System.out.println(" -----------------------");
	}

}
