[![Build Status](https://travis-ci.org/EvoSuite/evosuite.svg?branch=master)](https://travis-ci.org/EvoSuite/evosuite)
[![CircleCI](https://circleci.com/gh/EvoSuite/evosuite.svg?style=svg&circle-token=f00c8d84b9dcf7dae4a82438441823f3be9df090)](https://circleci.com/gh/EvoSuite/evosuite)

# Evosuite++
This project is a forked version of Evosuite. The original address of evosuite is here: https://github.com/EvoSuite/evosuite

In this project, we enhance Evosuite in terms of branch distance gradient recovering, object construction, smarter mutation, etc.
Here is the relevant publication:
- Yun Lin, Jun Sun, Gordon Fraser, Ziheng Xiu, Ting Liu, and Jin Song Dong. Recovering Fitness Gradients for Interprocedural Boolean Flags in Search-Based Testing (ISSTA 2020), 440--451. (Acknowledge*: Thanks Lyly Tran's contribution to set up our experiment despite her name is not list on the paper.)

You may refer to our website for more information on this project and how to run the experiment demonstrated in our paper: https://sites.google.com/view/evoipf/home

# Building EvoSuite

EvoSuite uses [Maven](https://maven.apache.org/).

First, ensure you have maven installed, to check, run

```mvn -v```

To build EvoSuite in Eclipse, make sure you have the [M2Eclipse](http://www.eclipse.org/m2e/) plugin installed, and import EvoSuite as Maven project. This will ensure that Eclipse correctly configure the Maven project.

# Building EvoSuite in Eclipse

In eclipse, we need to import Evosuite projects by **Import>>Maven>>Existing Maven Projects**. In general, we may import the following projects for compiling Evosuite:
* evosuite
* evosuite-client
* evosuite-master
* evosuite-runtime
* evosuite-shell

After importing all the above projects, we need to modify pom.xml in evosuite project as follows (here is an Eclipse bug, which makes the IDE fail to recognize correct Java home path even if we set the correct JDK path in Eclipse, see https://bugs.eclipse.org/bugs/show_bug.cgi?id=432992):
We find ```<tools-default>``` and replace its ```<exists>``` and ```<toolsjar>``` element with the file location inside
 project.
For example:
```
<id>tools-default</id>
  <activation>
    <activeByDefault>true</activeByDefault>
    <file>
      <exists>C:\Program Files\Java\jdk1.8.0_261\libs\tools.jar</exists>
    </file>
    </activation>
    <properties>
       <toolsjar>C:\Program Files\Java\jdk1.8.0_261\libs\tools.jar</toolsjar>
    </properties>
```

A more systematic way to resolve the problem can be referred here: 
add the following configuration in your eclipse.ini file before `-vmargs` option.
```
-vm
$YOUR_JDK_PATH$/jre/bin/server/jvm.dll
```
Then, right click the project >> Maven >> Ipdate Project ...
By this means, the problem can be fixed.

The "evosuite-master" project may have some compilation errors. In this case, we may include the ```target/generated
-sources/jaxb``` folder as build path.

# FAQ

1. If you encounter **com.sun** dependency issue:

    > Please replace the corresponding tools.jar with the absolute path of the jdk tools.jar and the error will go
                                                      away. 
