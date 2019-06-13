package eu.stamp.botsing;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

public class CommandLineParameters {

	public static final String D_OPT = "D";
	public static final String PROJECT_CP_OPT = "project_cp";
	public static final String TARGET_FRAME_OPT = "target_frame";
	public static final String CRASH_LOG_OPT = "crash_log";
	public static final String HELP_OPT = "help";

	public static final String N_OPT = "N";
	public static final String X_OPT = "X";

	// optional parameters
	public static final String POPULATION_OPT = "population";
	public static final String SEARCH_BUDGET_OPT = "search_budget";
	public static final String MAX_RECURSION = "max_recursion";
	public static final String TEST_DIR = "test_dir";
	public static final String NO_RUNTIME_DEPENDENCY = "no_runtime_dependency";
	public static final String GLOBAL_TIMEOUT = "global_timeout";
	public static final String CATCH_UNDECLARED_EXCEPTIONS = "catch_undeclared_exceptions";

	public static Options getCommandLineOptions() {
		Options options = new Options();

		// Properties
		options.addOption(Option.builder(D_OPT).numberOfArgs(2).argName("property=value").valueSeparator()
				.desc("use value for given property").build());

		// Classpath
		options.addOption(Option.builder(PROJECT_CP_OPT).hasArg()
				.desc("classpath of the project under test and all its dependencies").build());

		// Target frame
		options.addOption(Option.builder(TARGET_FRAME_OPT).hasArg().desc("Level of the target frame").build());

		// Stack trace file
		options.addOption(Option.builder(CRASH_LOG_OPT).hasArg().desc("File with the stack trace").build());

		// Number of parallel botsing reproduction
		options.addOption(Option.builder(N_OPT).hasArg().desc("Number of parallel botsing reproduction").build());

		// Number of times
		options.addOption(Option.builder(X_OPT).hasArg().desc("Number of times").build());

		// Help message
		options.addOption(Option.builder(HELP_OPT).desc("Prints this help message.").build());

		return options;
	}

}
