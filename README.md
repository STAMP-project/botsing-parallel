# Botsing parallel

This repository provides bash scripts to run several instances of botsing-reproduction in parallel.

## Install

You can install it in your Maven local repository with this command:

```
mvn clean install
```

## Simple Usage

After creation of botsing-parallel-jar-with-dependencies.jar JAR file, you can run botsing-parallel  

```
java -jar botsing-parallel-jar-with-dependencies.jar -project_cp /home/ubuntu/ExRunner-bash/bins/lang/20b/bin -crash_log /home/ubuntu/ExRunner-bash/crashes/lang/LANG-20b/LANG-20b.log -target_frame 3 -N 2 -X 4
```

* `project_cp` is the classpath of the project and all its dependencies.
* `crash_log` is the parameter to tell Botsing where is the log file to analyze.
* `target_frame` is the parameter to tell Botsing how many lines of the stacktrace to replicate

To have more information on the parameters that you can use, please refer to the [Botsing reproduction](https://stamp-project.github.io/botsing/pages/crashreproduction.html).