import com.hp.hpl.jena.sparql.util.*;
import com.hp.hpl.jena.rdf.model.*;
import com.hp.hpl.jena.query.*;
import com.hp.hpl.jena.reasoner.*;
import com.hp.hpl.jena.reasoner.rulesys.*;
import com.hp.hpl.jena.vocabulary.ReasonerVocabulary;
import com.hp.hpl.jena.util.*;
import java.io.*;


public class Reasoning {


    private static Model reason(Model input) {
	
	// Register a namespace to be used in the rules
	String flUri = "http://www.snee.com/ns/flights#";
	PrintUtil.registerPrefix("fl", flUri);
	
	// Create an (RDF) specification of a hybrid reasoner which loads its rules from an external file.
	Model m = ModelFactory.createDefaultModel();
	Resource configuration =  m.createResource();
	configuration.addProperty(ReasonerVocabulary.PROPruleMode, "hybrid");
	configuration.addProperty(ReasonerVocabulary.PROPruleSet,  "file.rules");
	
	// Create an instance of such a reasoner
	Reasoner reasoner = GenericRuleReasonerFactory.theInstance().create(configuration);
	
	// Infere new knowledge on the input model, generating a new one
	InfModel infmodel = ModelFactory.createInfModel(reasoner, input);
	
	return infmodel;
    }


    private static Model readModelFromFile(String filePath) throws Exception {
	
	// Create an empty model
	Model model = ModelFactory.createDefaultModel();
	
	// Use the FileManager to find the input file
	InputStream in = new FileInputStream(new File(filePath));

	// Read the RDF/XML file
	model.read(in, "");
				
	return model;
    }

    
    private static long doSelectQuery(String queryString, Model model) {
	
	long time = -1;

	QueryExecution qexec = null;
	Query query = QueryFactory.create(queryString) ;

	// Parse the query and print it in stdout
	System.out.println("The following query is being performed...\n");
  //      query.serialize(new IndentedWriter(System.out,true)) ;
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
                       
        }
        finally
        {
            // QueryExecution objects should be closed to free any system resources 
            qexec.close() ;
        }

	return time;
    }

    
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

	    /*if ( args.length != 2 ) {
		System.out.println("Parameters: file-with-query file-with-rdf-data");
		System.exit(0);
	    }*/

	    File queryStringFile = new File("sparkle");
	    String queryString = extractText(queryStringFile);

	    Model model = readModelFromFile("RDF");
	    System.out.println("The model has " + model.size() + " statements");
	    // Do the reasoning
	    model = reason(model);
	    System.out.println("After inferencing the model has " + model.size() + " statements");

	    // Execute the query in the model and show the results
	    //long time = doSelectQuery(queryString, model);
	    //System.out.println("\nThe query took " + time + "msec\n");

	}
	catch(Exception e) {
	    e.printStackTrace();
	}
    }
}
