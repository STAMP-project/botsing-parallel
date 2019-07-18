# Botsing parallel

This repository provides the components to run an arbitrary number of parallel instances of botsing-reproduction. The mentioned components are contained in a jar file called botsing-parallel-jar-with-dependencies.jar.

## Install

You can install it in your Maven local repository with this command:

```
mvn clean install
```

## Simple Usage

After creation of botsing-parallel-jar-with-dependencies.jar JAR file, you can run botsing-parallel with all required parameters: 

* `project_cp` is the classpath of the project under test and all its dependencies (e.g. `ExRunner-bash/bins/lang/20b/bin`).
* `crash_log` is the parameter to tell Botsing where is the log file to analyze (e.g. `ExRunner-bash/crashes/lang/LANG-20b/LANG-20b.log`).
* `target_frame` is the max level of the target frame. The botsing-parallel reproduces from the lower frames of the stack trace until the target_frame. 
* `N` is the number of botsing-reproduction instances running in parallel. 
* `X` is the number of times to reproduce each frame of the stack trace.


An example of usage can be: 

```
java -jar botsing-parallel-jar-with-dependencies.jar -project_cp ~/bin -crash_log LANG-20b.log -target_frame 3 -N 2 -X 4
```

You can use others parameters; by default they are:

* population = 100
* search_budget = 1800
* max_recursion = 30
* test_dir = crash-reproduction-tests
* no_runtime_dependency = false
* global_timeout = 1800
* catch_undeclared_exceptions = false

but you can overwrite them just appending in the command line `-D <property=value>`: for example:

```
java -jar botsing-parallel-jar-with-dependencies.jar -project_cp ~/bin -crash_log LANG-20b.log -target_frame 3 -N 2 -X 4 -Dtest_dir=lang-test-20b
```

Please note that all results will be stored in the `test_dir` folder; in the botsing-parallel task, you can have a lot of botsing-reproduction results (it depends on `target_frames` value) which use the same `test_dir` folder, so to avoid to overwrite the results for different `target_frames`, it always appends at the end of `test_dir` parameter the `-<target_frame>`. For example if you are using `target_frame = 3`, all results are in these directories:

* crash-reproduction-tests-1 (reproduction for target_frame = 1),
* crash-reproduction-tests-2 (reproduction for target_frame = 2)
* and crash-reproduction-tests-3 (reproduction for target_frame = 3).

To have more information on the parameters that you can use, please refer to the [Botsing reproduction](https://stamp-project.github.io/botsing/pages/crashreproduction.html).
