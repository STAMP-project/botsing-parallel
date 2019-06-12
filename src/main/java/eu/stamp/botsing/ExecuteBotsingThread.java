package eu.stamp.botsing;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExecuteBotsingThread extends Thread {

	Integer frame;
	List<String> properties;

	private static final Logger LOG = LoggerFactory.getLogger(ExecuteBotsingThread.class);

	private static String TEST_DIR = "test_dir";

	private static List<Integer> framesCompleted = new ArrayList<Integer>();

	public static List<Integer> getFramesCompleted() {
		return framesCompleted;
	}

	public static void addFrameComplete(Integer value) {
		framesCompleted.add(value);
	}

	ExecuteBotsingThread(Integer frame, List<String> properties) {
		this.frame = frame;
		this.properties = properties;
	}

	@Override
	public void run() {
		try {
			LOG.info(Thread.currentThread().getName() + " Start. Current frame = " + frame);

			synchronized (frame) {// add synchronized clause

				if (!(getFramesCompleted().contains(frame))) { // run thread
					boolean success = executeJar(frame);

					if (success) {
						addFrameComplete(frame);
						LOG.info("Botsing parallel terminated with successful for frame " + frame);
					} else {
						LOG.info("Botsing parallel failed for frame " + frame);
					}

				} else {// jump frame is already executed
					LOG.info("Botsing parallel for frame " + frame + " already done!");
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private boolean executeJar(int frame) throws InterruptedException {
		LOG.info("Start execution: botsing-reproduction on frame " + frame);
		final String JAVA_CMD = System.getProperty("java.home") + File.separatorChar + "bin" + File.separatorChar
				+ "java";

		ArrayList<String> jarCommand = new ArrayList<String>();
		jarCommand.add(JAVA_CMD);
		jarCommand.add("-jar");

		File botsingReproductionJar = new File(Parallel.BOTSINF_JAR_PATH);
		jarCommand.add(botsingReproductionJar.getAbsolutePath());

		List<String> parallelProperties = getLocalProperties(properties);

		parallelProperties.add("-target_frame");
		parallelProperties.add(frame + "");

		jarCommand.addAll(parallelProperties);
		LOG.info("jarCommand :" + jarCommand);

		ProcessBuilder builder = new ProcessBuilder(jarCommand.toArray(new String[0]));
		// builder.directory(workDir.getAbsoluteFile());
		builder.redirectErrorStream(true);

		try {
			Process process = builder.start();
			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

			String line;
			while ((line = reader.readLine()) != null) {
				System.out.println(line);
			}

			int exitCode = process.waitFor();
			if (exitCode != 0)
				System.out.println("\nExited with error code : " + exitCode);

			return exitCode == 0;
		} catch (IOException e) {
			return false;
		}
	}

	private List<String> getLocalProperties(List<String> properties) {
		List<String> prop = new ArrayList<String>();
		prop.addAll(properties);

		for (int i = 0; i < prop.size(); i++) {
			if (prop.get(i).contains(TEST_DIR)) {
				prop.set(i, prop.get(i) + "-" + frame);
				break;
			}
		}
		return prop;
	}

}
