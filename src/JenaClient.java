import com.hp.hpl.jena.sparql.util.*;
import com.hp.hpl.jena.rdf.model.*;
import com.hp.hpl.jena.query.*;
import com.hp.hpl.jena.*;
//import com.hp.hpl.jena.sparql.util.IndentedLineBuffer;
//import com.hp.hpl.jena.sparql.util.IndentedWriter;
import java.io.*;


public class JenaClient {


    // Write the model to standard out in the specified format (RDF/XML, N3, N-TRIPLES)
    private static void printModel(Model model, String format) {
	
	assert model != null : "The model is not initialised"; 
	model.write(System.out, format);
    }


    // Loads and RDF file as a Jena model for later processing
    private static Model readModelFromFile(String filePath) throws Exception {
	
	// Create an empty model
	Model model = ModelFactory.createDefaultModel();
	
	// Read the RDF/XML file
	InputStream in = new FileInputStream(new File(filePath));
	model.read(in, "");
				
	return model;
    }


    // Executes a SPARQL select query
    private static long doSelectQuery(String queryString, Model model) {
	
	long time = -1;

	QueryExecution qexec = null;
	Query query = QueryFactory.create(queryString) ;

	// Parse the query and print it in stdout
	System.out.println("The following query is being performed...\n");
     // query.serialize(new IndentedWriter(System.out,true)) ;
        System.out.println() ;


	// Select the model where the query is going to be performed
	if (model == null) {
	    System.out.println("Executing the query with RDF dataset provided by FROM and FROM NAMED");
	    qexec = QueryExecutionFactory.create(query);
	}
	else {
	    System.out.println("Executing the query over the specified model");
	    qexec = QueryExecutionFactory.create(query, model);
	}

	try {
            // Assumption: it's a SELECT query.
	    long tic = System.currentTimeMillis();
            ResultSet results = qexec.execSelect() ;
	    long toc = System.currentTimeMillis();
	    time = toc - tic;
	    System.out.println("These are the results of the query...\n");
	    ResultSetFormatter.out(System.out, results, query);
	    
	    while(results.hasNext())
	    {
	    	QuerySolution row=results.nextSolution();
	        String strC = row.get("c").toString(); 
	        String strB = row.get("B").toString();
	        String strT = row.get("T").toString();
	        //System.out.println("strC:"+strC+strB+strT);
	        n[count] = new Node(strB);
	        n[count].addh_value(Integer.parseInt(strC));
	        n[count].dur = Integer.parseInt(strT) ;
	        count++;
	    }
                       
        }
        finally
        {
            // QueryExecution objects should be closed to free any system resources 
            qexec.close() ;
        }

	return time;
    }


    // Used to read the text of a SPARQL query from a file
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


    public static void main(String[] args)
    {
	try {

	    /*if ( (args.length != 1) && (args.length != 2) ) {
		System.out.println("Parameters: file-with-query [file-with-rdf-data]");
		System.exit(0);
	    }*/

	    File queryStringFile = new File("sparkle");
	    String queryString = extractText(queryStringFile);

	    Model model = null;

	    {
		model = readModelFromFile("RDF_NUM");
		System.out.println("The model has " + model.size() + " statements");
	    }
	    
	    // Execute the query in the model and show the results
	    long time = doSelectQuery(queryString, model);
	    System.out.println("\nThe query took " + time + "msec\n");

	}
	catch(Exception e) {
	    e.printStackTrace();
	}
    }
}
