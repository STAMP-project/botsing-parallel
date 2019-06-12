package eu.stamp.botsing;

import static eu.stamp.botsing.CommandLineParameters.CRASH_LOG_OPT;
import static eu.stamp.botsing.CommandLineParameters.HELP_OPT;
import static eu.stamp.botsing.CommandLineParameters.N_OPT;
import static eu.stamp.botsing.CommandLineParameters.PROJECT_CP_OPT;
import static eu.stamp.botsing.CommandLineParameters.TARGET_FRAME_OPT;
import static eu.stamp.botsing.CommandLineParameters.X_OPT;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Parallel {

	private static final Logger LOG = LoggerFactory.getLogger(Parallel.class);
	private DefaultProperties defaultProperties = new DefaultProperties();
	List<String> properties = new ArrayList<String>();
	public static final String BOTSING_REPRODUCTION = "botsing-reproduction.jar";
	public final static String BOTSINF_JAR_PATH = BOTSING_REPRODUCTION;

	public static void main(String[] args) {
		Parallel parallel = new Parallel();
		parallel.parseCommandLine(args);
		System.exit(0);
	}

	public void parseCommandLine(String[] args) {

		// Parse commands according to the defined options
		Options options = CommandLineParameters.getCommandLineOptions();
		CommandLine commands = parseCommands(args, options);

		// If help option is provided
		if (commands.hasOption(HELP_OPT)) {
			printHelpMessage(options);
		} else if (!(commands.hasOption(PROJECT_CP_OPT) && commands.hasOption(CRASH_LOG_OPT)
				&& commands.hasOption(TARGET_FRAME_OPT))) {
			// Check the required options are there
			LOG.error("A mandatory option -{} -{} -{} is missing!", PROJECT_CP_OPT, CRASH_LOG_OPT, TARGET_FRAME_OPT);
			printHelpMessage(options);
		} else {// Otherwise, proceed to crash reproduction

			int n = Integer.parseInt(commands.getOptionValue(N_OPT));
			int x = Integer.parseInt(commands.getOptionValue(X_OPT));
			int target_frames = Integer.parseInt(commands.getOptionValue(TARGET_FRAME_OPT));

			addProperty(PROJECT_CP_OPT, commands.getOptionValue(PROJECT_CP_OPT));
			addProperty(CRASH_LOG_OPT, commands.getOptionValue(CRASH_LOG_OPT));
			addDefaultProperties();

			// TODO add new parameters

			// load botsing-reproduction jar file
			saveBotsingJar(BOTSINF_JAR_PATH);

			runReproductionInParallel(n, x, target_frames);
		}
	}

	private void runReproductionInParallel(int n, int x, int target_frames) {
		LOG.info("ReproductionInParallel: times = " + x + ", parallel = " + n + ", target_frames = " + target_frames);

		int total_frames[] = new int[x * target_frames];
		int size = total_frames.length;

		for (int i = 0; i < size; i++) {
			if ((i + 1) % target_frames > 0) {
				total_frames[i] = (i + 1) % target_frames;
			} else {
				total_frames[i] = target_frames;
			}
		}

		ExecutorService executor = Executors.newFixedThreadPool(n);

		System.out.println(Arrays.toString(total_frames));
		for (int i = 0; i < size; i++) {

			Thread worker = new ExecuteBotsingThread(total_frames[i], properties);
			executor.execute(worker);
		}
		executor.shutdown();

		while (!executor.isTerminated()) {
		}
		LOG.info("Finished all threads. Test completed!");
		LOG.info("List of frames completed: " + ExecuteBotsingThread.getFramesCompleted());
	}

	protected CommandLine parseCommands(String[] args, Options options) {
		CommandLineParser parser = new DefaultParser();
		CommandLine commands = null;
		try {
			commands = parser.parse(options, args);
		} catch (ParseException e) {
			LOG.error("Could not parse command line!", e);
			printHelpMessage(options);
			return null;
		}
		return commands;
	}

	private void printHelpMessage(Options options) {
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp(
				"java -jar botsing.jar -crash_log stacktrace.log -target_frame 2 -project_cp dep1.jar;dep2.jar  )",
				options);
	}

	private void addProperty(String name, String value) {
		if (value != null && value.length() > 0) {
			if (properties.contains("-" + name)) {
				int i = properties.indexOf("-" + name);
				// remove old value
				properties.remove(i + 1);
				// insert new value
				properties.add(i + 1, value);
			} else {
				// insert new parameter and value
				properties.add("-" + name);
				properties.add(value);
			}
		}
	}

	private void addDefaultProperties() {
		Properties properties = defaultProperties.getProperties();
		properties.forEach((key, value) -> addDProperty(key.toString(), value.toString()));
	}

	protected void addDProperty(String name, String value) {
		if (value != null && value.length() > 0) {
			properties.add("-D" + name + "=" + value);
		}
	}

	private void saveBotsingJar(String filename) {

		InputStream inputStream = getClass().getClassLoader().getResourceAsStream(BOTSING_REPRODUCTION);
		FileOutputStream fos = null;
		File file;

		try {
			// Specify the file path here
			file = new File(filename);
			fos = new FileOutputStream(file);

			if (!file.exists()) {
				file.createNewFile();
			}

			byte[] buffer = new byte[1024];
			int bytesRead;
			// read from is to buffer
			while ((bytesRead = inputStream.read(buffer)) != -1) {
				fos.write(buffer, 0, bytesRead);
			}
			inputStream.close();
			fos.flush();

			System.out.println("File Written Successfully");
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			try {
				if (fos != null) {
					fos.close();
				}
			} catch (IOException ioe) {
				System.out.println("Error in closing the Stream");
			}
		}
	}
}
