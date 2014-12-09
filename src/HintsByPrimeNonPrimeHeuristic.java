import com.hp.hpl.jena.sparql.util.*;
import com.hp.hpl.jena.rdf.model.*;
import com.hp.hpl.jena.query.*;
import com.hp.hpl.jena.*;

//import com.hp.hpl.jena.sparql.util.IndentedLineBuffer;
//import com.hp.hpl.jena.sparql.util.IndentedWriter;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;


public class HintsByPrimeNonPrimeHeuristic {


	/**
	 * Method to write the model to standard out in the specified format  (RDF/XML , N3, N- Triples)
	 * 
	 * @param model
	 * @param format
	 */
	private static void printModel(Model model, String format) {

		assert model != null : "The model is not initialised"; 
		model.write(System.out, format);
	}


	/**
	 * Method to load and make RDF file as Jena model for later processing
	 * @param filePath
	 * @return
	 * @throws Exception
	 */
	private static Model readModelFromFile(String filePath) throws Exception {

		//Create an empty model
		Model model = ModelFactory.createDefaultModel();

		//Read the RDF/XML file
		InputStream in = new FileInputStream(new File(filePath));
		model.read(in, "");

		return model;
	}


	/**
	 * Method to execute the SPARQLE query
	 * 
	 * @param queryString
	 * @param model
	 * @return
	 */
	private static HashMap<Integer, ArrayList<String>> doSelectQuery(String queryString, Model model) {

		long time = -1;
		HashMap<Integer, ArrayList<String>> numMap;

		QueryExecution qexec = null;
		Query query = QueryFactory.create(queryString) ;

		//Parse the query and print it in stdout
		//System.out.println("The following query is being performed...\n");
		//query.serialize(new IndentedWriter(System.out,true)) ;
		//System.out.println() ;


		// Select the model where the query is going to be performed
		if (model == null) {
			//System.out.println("Executing the query with RDF dataset provided by FROM and FROM NAMED");
			qexec = QueryExecutionFactory.create(query);
		}
		else {
			//System.out.println("Executing the query over the specified model");
			qexec = QueryExecutionFactory.create(query, model);
		}

		try {

			ArrayList<String> numProperties = new ArrayList<String>();
			numMap = new HashMap<Integer, ArrayList<String>>();
			
			// Assumption: it's a SELECT query.
			long tic = System.currentTimeMillis();
			ResultSet results = qexec.execSelect() ;
			long toc = System.currentTimeMillis();
			time = toc - tic;
			//System.out.println("These are the results of the query...\n");
			//ResultSetFormatter.out(System.out, results, query);

			while(results.hasNext())
			{
				QuerySolution row=results.nextSolution();
				String strC = row.get("C").toString(); 
				String strD = row.get("D").toString();
				String strE = row.get("E").toString();
				
			
				if(numMap.containsKey(Integer.parseInt(strC)))
				{
					ArrayList<String> properties = numMap.get(Integer.parseInt(strC));
					if(properties==null)
						properties = new ArrayList<String>();
					properties.add(strE);
					numMap.put(Integer.parseInt(strC), properties);
				}else
				{
					ArrayList<String> properties = new ArrayList<String>();
					properties.add(strE);
					numMap.put(Integer.parseInt(strC), properties);
				}
				
				if(numMap.containsKey(Integer.parseInt(strD)))
				{
					ArrayList<String> properties = numMap.get(Integer.parseInt(strD));
					if(properties==null)
						properties = new ArrayList<String>();
					properties.add(strE);
					numMap.put(Integer.parseInt(strD), properties);

				}else
				{
					ArrayList<String> properties = new ArrayList<String>();
					properties.add(strE);
					numMap.put(Integer.parseInt(strD), properties);
				}
				
				
				//System.out.print("C "+strC+" ");
				//System.out.print("D "+strD+" ");
				//System.out.print("E "+strE+" ");

				//System.out.println();

			}

		}
		finally
		{
			// QueryExecution objects should be closed to free any system resources 
			qexec.close() ;
		}

		return numMap;
	}


	/**
	 * Method used to read the text from the SPARQLE query text file 
	 * 
	 * @param f
	 * @return
	 * @throws Exception
	 */
	private static String extractText(File f) throws Exception {

		StringBuilder contents = new StringBuilder();

		try {
			BufferedReader input = new BufferedReader(new FileReader(f));
			try {
				String line = null;
				while ((line = input.readLine()) != null) {
					contents.append(line);
					contents.append(System.getProperty("line.separator"));
				}
			} finally {
				input.close();
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		return contents.toString().trim();
	}


	public HashMap<Integer, ArrayList<String>> getNumProperties()
	{

		try {

			/*if ( (args.length != 1) && (args.length != 2) ) {
		System.out.println("Parameters: file-with-query [file-with-rdf-data]");
		System.exit(0);
	    }*/

			File queryStringFile = new File("sparkle_prime_nonprime");
			String queryString = extractText(queryStringFile);

			Model model = null;

			{
				model = readModelFromFile("RDF_PRIME_NONPRIME");
				//System.out.println("The model has " + model.size() + " statements");
			}

			// Execute the query in the model and show the results
			HashMap<Integer, ArrayList<String>> nMap = doSelectQuery(queryString, model);
			//System.out.println("\nThe query took "  + "msec\n");
			
			/*for(int n:nMap.keySet())
			{
				ArrayList<String> properties = nMap.get(n);
				System.out.print(n+" - ");
				for(String s : properties)
				{
					System.out.print(s);
				}
				System.out.println();
			}*/
			
			
			return nMap;

		}
		catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	
	}
	
	/**
	 * Main Method
	 * @param args
	 */
	public static void main(String[] args)
	{
		
		new HintsByPrimeNonPrimeHeuristic().getNumProperties();
	}
}
