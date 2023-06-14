package composer;

import java.io.File;

import analysis.SimulationResultsWriter;
import jmt.engine.simDispatcher.DispatcherJSIMschema;

public class Main {

	/* args: [0]: json specifications (input file)
	   		 [1]: dataset path (output file)
	   		 [2]: simulation duration (in sec)
	   		 [3]: alias 
	   		 [4]: global message size (ex:50.0 MB)
	*/
	public static void main(String[] args) throws Exception {
		if(args.length!=5) {
			System.out.println("Error: Invalid number of parameters.");
			System.out.println("Usage: java -jar <name>.jar <inputFolder> <outputFolder> <simulationDuration> <alias> <globalMessageSize>");
			System.exit(0);
		}
		String inputFile = args[0];
		String outputFile = args[1];
		int simulationDuration = Integer.parseInt(args[2]);
		String alias = args[3];
		double GLOBAL_MESSAGE_SIZE=Double.parseDouble(args[4])* 1048576;//converted to Bytes

		QueueingNetworkComposer composer = new QueueingNetworkComposer();
		System.out.println("Composing the queueing network ...");
		String jsimgFile = composer.composeNetwork(inputFile, simulationDuration,GLOBAL_MESSAGE_SIZE);
		System.out.println("Created simulation file.");
		System.out.println("Running the simulation ...");
		DispatcherJSIMschema djss = new DispatcherJSIMschema(jsimgFile);
		djss.setSimulationMaxDuration(simulationDuration* 1000L);
		djss.solveModel();
		File simResultFile = djss.getOutputFile();
		System.out.println("Simulation done.");
		
		System.out.println("Writing results to csv ...");
		SimulationResultsWriter writer = new SimulationResultsWriter();
		writer.readXML(simResultFile.getCanonicalPath());
		writer.writeToCsv(outputFile+"/output_"+alias+".csv");
		System.out.println("Done writing to csv.");
		
		System.out.println("Adding results to dataset");
		writer.addResultsToDataset(outputFile, alias);
		System.out.println("Done.");
		
	}

}
