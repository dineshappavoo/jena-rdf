/**
 * 
 */

/**
 * @author Dany
 *
 */
public class ConstructSudoku {

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
		
		SudokuSolver solver = new SudokuSolver();
		int[][] matrix = parseProblem(pInput);
		
		
		writeMatrix(matrix);
		if (SudokuSolver.solve(0,0,matrix))    // solves in place
		{
		    writeMatrix(matrix);
		    writeMatrixByHints(matrix);
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
		    	for (int i = 0; i < 9; ++i) {
		    	    if (i % 3 == 0)
		    		System.out.println(" -----------------------");
		    	    for (int j = 0; j < 9; ++j) {
		    		if (j % 3 == 0) System.out.print("| ");
		    		String val = solution[i][j] == 0? " ": Integer.toString(solution[i][j]);
		    		System.out.print("["+i+","+j+"]:"+val);
		    		//System.out.print(val);
		    		
		    		System.out.print(" ");
		    	    }
		    	    System.out.println("|");
		    	}
		    	System.out.println(" -----------------------");
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
