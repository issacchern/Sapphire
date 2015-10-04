# Project Sapphire

![alt tag] (https://github.com/issacchern/Sapphire/blob/master/images/logo.jpg)

Sapphire is a free eclipse plugin software that records the changes under JavaModel in eclipse and exports the recorded data to text files and SQLite database. This plugin is intended for educational and teaching purposes.  

# Installation

1. Click Download Zip button at the bottom right, extract the folder to your workspace.
2. Run eclipse navigation and navigate to File> Import...
3. Select Existing Projects into Workspace and browse for the folder you extracted earlier.
4. Once the project is created, open the project and double click plugin.xml.
5. Select Overview tab and click Export Wizard at the right end corner. 
6. Click finish. Extract the jar file from the Zip file and drop it in the dropins folder inside the eclipse folder.
7. You are all set!

You could also use the pre-built jar file from the downloaded zip folder and put the jar file in eclipse's dropin folder.

# How to use

In order to use the plugin, you will need to copy the .sapphire file from the downloaded folder (or create one .sapphire file if you like) to the .settings folder of your project. The .sapphire file is used as to enable toggle preventing every project being recorded unintendedly. 

NOTE: This plugin will not run as expected if you drop the .sapphire file while using eclipse. You have to restart eclipse to take effect. 

# Description

The objective of creating this plugin is to study the process of how beginner students learn writing codes instead of obtaining the final result. An analogy would be that English teacher asking students to write a few drafts before submitting their essays. There are a lot of ways to complete a coding assignment, probably by working hard by themselves or seeking help from others, or even getting codes online or from friends. For whatever method is, we wouldn't know what kind of struggles students have been through to get their assignments done by analyzing their submitted codes. Even though we use plagiarism detector nowadays, but there is no way to know if the source is authentic and original. Students can always change the structure flow of the code and make some unecessary changes to bypass the detection, but that's not our main concern here. We want students to learn, and we are looking for a way to teach beginner students to learn coding efficiently. We will be collecting the data input from students and analyzing the data to learn the general patterns and styles of coding. 


# Sample Output

This plugin will create .db file for SQLite database in the project folder. I use SQLiteBrowser for viewing the data. Snapshots below (captured in SQLiteBrowser) are the main four tables of the database.

- Main Event Table:

![alt tag] (https://github.com/issacchern/Sapphire/blob/master/images/sqlite3.JPG)

- CompilationUnit Table:

![alt tag] (https://github.com/issacchern/Sapphire/blob/master/images/sqlite1.JPG)

- Error Table: 

![alt tag] (https://github.com/issacchern/Sapphire/blob/master/images/sqlite2.JPG)

- Source File Table:

![alt tag] (https://github.com/issacchern/Sapphire/blob/master/images/sqlite4.JPG)



This plugin will also create two text files in the same project folder, .PROJECT_NAME.RECORDING and .PROJECT_NAME_source.RECORDING file. These two files are identical to the data in database.

Sample .PublicTest.RECORDING file:

```
2015.10.04 at 14:12:48 EDT [PLUGIN INITIALIZED]
2015.10.04 at 14:12:48 EDT [PACKAGE_FRAGMENT ADDED]: samplepackage
2015.10.04 at 14:12:58 EDT [COMPILATION_UNIT ADDED]: SampleClass1.java
2015.10.04 at 14:12:59 EDT [COMPILATION_UNIT CHANGED TO samplepackage.SampleClass1]
2015.10.04 at 14:12:59 EDT [SOURCE FILE INITIALIZED] 
2015.10.04 at 14:13:04 EDT [+] int a;
2015.10.04 at 14:13:04 EDT [LINE ADDED (samplepackage.SampleClass1)]: 5 $ [FIELD_DECLARATION] (int) | (a) | 
2015.10.04 at 14:13:07 EDT [+] int b;
2015.10.04 at 14:13:07 EDT [LINE ADDED (samplepackage.SampleClass1)]: 6 $ [FIELD_DECLARATION] (int) | (b) | 
2015.10.04 at 14:13:12 EDT [+] public void method(){
2015.10.04 at 14:13:12 EDT [+] }
2015.10.04 at 14:13:12 EDT [LINE ADDED (samplepackage.SampleClass1)]: 8 $ [METHOD_DECLARATION] keyword (public) | constructor (false) | (void) | (method) | 
2015.10.04 at 14:13:12 EDT [LINE ADDED (samplepackage.SampleClass1)]: 8 $ OPEN [METHOD_DECLARATION]
2015.10.04 at 14:13:15 EDT [ERROR DETECTED(samplepackage.SampleClass1)]: 9 * Syntax error, insert "VariableDeclarators" to complete LocalVariableDeclaration
2015.10.04 at 14:13:15 EDT [ERROR DETECTED(samplepackage.SampleClass1)]: 9 * Syntax error, insert ";" to complete BlockStatements
2015.10.04 at 14:13:15 EDT [ERROR DETECTED(samplepackage.SampleClass1)]: 9 * x cannot be resolved to a type
2015.10.04 at 14:13:19 EDT [+] int a=0;
2015.10.04 at 14:13:19 EDT [LINE ADDED (samplepackage.SampleClass1)]: 9 $ [VARIABLE_DECLARATION_STATEMENT] (int) | (a) | (0) | 
2015.10.04 at 14:13:19 EDT [LINE ADDED (samplepackage.SampleClass1)]: 9 $ CLOSE [METHOD_DECLARATION]
2015.10.04 at 14:13:21 EDT [-] int b;
2015.10.04 at 14:13:21 EDT [LINE REMOVED (samplepackage.SampleClass1)]: 6 $ [FIELD_DECLARATION] (int) | (b) | 
2015.10.04 at 14:13:30 EDT [+] public SampleClass1(){
2015.10.04 at 14:13:30 EDT [+] }
2015.10.04 at 14:13:30 EDT [LINE ADDED (samplepackage.SampleClass1)]: 11 $ [METHOD_DECLARATION] keyword (public) | constructor (true) | (SampleClass1) | 
2015.10.04 at 14:13:32 EDT [ERROR DETECTED(samplepackage.SampleClass1)]: 12 * Syntax error, insert "new ClassType ( )" to complete Expression
2015.10.04 at 14:13:32 EDT [ERROR DETECTED(samplepackage.SampleClass1)]: 12 * Syntax error, insert ";" to complete BlockStatements
2015.10.04 at 14:13:37 EDT [+] System.out.println();
2015.10.04 at 14:13:37 EDT [LINE ADDED (samplepackage.SampleClass1)]: 12 $ [QUALIFIED_NAME] (System) | (out) | (println) | 
2015.10.04 at 14:13:42 EDT [+] System.out.println("Hello World");
2015.10.04 at 14:13:42 EDT [-] System.out.println();
2015.10.04 at 14:13:42 EDT [LINE ADDED (samplepackage.SampleClass1)]: 12 $ [QUALIFIED_NAME] (System) | (out) | (println) | ("Hello World") | 
2015.10.04 at 14:13:42 EDT [LINE REMOVED (samplepackage.SampleClass1)]: 12 $ [QUALIFIED_NAME] (System) | (out) | (println) | 
2015.10.04 at 14:13:50 EDT [+] method();
2015.10.04 at 14:13:50 EDT [LINE ADDED (samplepackage.SampleClass1)]: 12 $ [METHOD_INVOCATION] (method) | 
2015.10.04 at 14:14:03 EDT [ERROR DETECTED(samplepackage.SampleClass1)]: 12 * Type mismatch: cannot convert from void to boolean
2015.10.04 at 14:14:08 EDT [ERROR DETECTED(samplepackage.SampleClass1)]: 7 * This method must return a result of type boolean
2015.10.04 at 14:14:12 EDT [+] public boolean method(){
2015.10.04 at 14:14:12 EDT [+] return true;
2015.10.04 at 14:14:12 EDT [+] boolean x=method();
2015.10.04 at 14:14:12 EDT [-] public void method(){
2015.10.04 at 14:14:12 EDT [-] method();
2015.10.04 at 14:14:12 EDT [LINE ADDED (samplepackage.SampleClass1)]: 7 $ [METHOD_DECLARATION] keyword (public) | constructor (false) | (boolean) | (method) | 
2015.10.04 at 14:14:12 EDT [LINE ADDED (samplepackage.SampleClass1)]: 10 $ [RETURN_STATEMENT] booleanValue (true) | 
2015.10.04 at 14:14:12 EDT [LINE ADDED (samplepackage.SampleClass1)]: 14 $ [VARIABLE_DECLARATION_STATEMENT] (boolean) | (x) | (method) | 
2015.10.04 at 14:14:12 EDT [LINE REMOVED (samplepackage.SampleClass1)]: 7 $ [METHOD_DECLARATION] keyword (public) | constructor (false) | (void) | (method) | 
2015.10.04 at 14:14:12 EDT [LINE REMOVED (samplepackage.SampleClass1)]: 12 $ [METHOD_INVOCATION] (method) | 
2015.10.04 at 14:14:17 EDT [+] boolean enabled=method();
2015.10.04 at 14:14:17 EDT [-] boolean x=method();
2015.10.04 at 14:14:17 EDT [LINE ADDED (samplepackage.SampleClass1)]: 14 $ [VARIABLE_DECLARATION_STATEMENT] (boolean) | (enabled) | (method) | 
2015.10.04 at 14:14:17 EDT [LINE REMOVED (samplepackage.SampleClass1)]: 14 $ [VARIABLE_DECLARATION_STATEMENT] (boolean) | (x) | (method) | 
2015.10.04 at 14:14:30 EDT [COMPILATION_UNIT ADDED]: SampleClass2.java
2015.10.04 at 14:14:30 EDT [COMPILATION_UNIT CHANGED TO samplepackage.SampleClass2]
2015.10.04 at 14:14:30 EDT [SOURCE FILE INITIALIZED] 
2015.10.04 at 14:14:40 EDT [ERROR DETECTED(samplepackage.SampleClass2)]: 5 * Syntax error, insert "Identifier (" to complete MethodHeaderName
2015.10.04 at 14:14:40 EDT [ERROR DETECTED(samplepackage.SampleClass2)]: 5 * Syntax error, insert ")" to complete MethodDeclaration
2015.10.04 at 14:14:40 EDT [ERROR DETECTED(samplepackage.SampleClass2)]: 5 * Syntax error, insert ";" to complete ClassBodyDeclarations
2015.10.04 at 14:14:47 EDT [ERROR DETECTED(samplepackage.SampleClass2)]: 5 * ArrayList cannot be resolved to a type
2015.10.04 at 14:14:47 EDT [ERROR DETECTED(samplepackage.SampleClass2)]: 5 * Syntax error, insert "( )" to complete Expression
2015.10.04 at 14:14:47 EDT [ERROR DETECTED(samplepackage.SampleClass2)]: 5 * Syntax error, insert ";" to complete ClassBodyDeclarations
2015.10.04 at 14:14:49 EDT [ERROR DETECTED(samplepackage.SampleClass2)]: 5 * ArrayList cannot be resolved to a type
2015.10.04 at 14:14:49 EDT [ERROR DETECTED(samplepackage.SampleClass2)]: 5 * ArrayList cannot be resolved to a type
2015.10.04 at 14:14:50 EDT [ERROR DETECTED(samplepackage.SampleClass2)]: 5 * ArrayList cannot be resolved to a type
2015.10.04 at 14:14:50 EDT [ERROR DETECTED(samplepackage.SampleClass2)]: 5 * ArrayList cannot be resolved to a type
2015.10.04 at 14:14:54 EDT [+] import java.util.ArrayList;
2015.10.04 at 14:14:54 EDT [+] ArrayList<String> array=new ArrayList<String>();
2015.10.04 at 14:14:54 EDT [LINE ADDED (samplepackage.SampleClass2)]: 3 $ [IMPORT_DECLARATION] static (false) | (java) | (util) | (ArrayList) | onDemand (false) | 
2015.10.04 at 14:14:54 EDT [LINE ADDED (samplepackage.SampleClass2)]: 7 $ [SIMPLE_TYPE] (ArrayList) | (String) | (array) | (ArrayList) | (String) | 
2015.10.04 at 14:14:59 EDT [LINE ADDED (samplepackage.SampleClass2)]: 9 # [LINE_COMMENT] // this class is 
2015.10.04 at 14:15:01 EDT [LINE ADDED (samplepackage.SampleClass2)]: 9 # [LINE_COMMENT] // this class is going to be 
2015.10.04 at 14:15:01 EDT [LINE REMOVED (samplepackage.SampleClass2)]: 9 # [LINE_COMMENT] // this class is 
2015.10.04 at 14:15:03 EDT [LINE ADDED (samplepackage.SampleClass2)]: 9 # [LINE_COMMENT] // this class is going to be awesome
2015.10.04 at 14:15:03 EDT [LINE REMOVED (samplepackage.SampleClass2)]: 9 # [LINE_COMMENT] // this class is going to be 
2015.10.04 at 14:15:08 EDT [COMPILATION_UNIT CHANGED TO samplepackage.SampleClass1]
2015.10.04 at 14:15:20 EDT [COMPILATION_UNIT REMOVED]: SampleClass2.java
2015.10.04 at 14:15:31 EDT [+] System.out.println("Class is removed! ");
2015.10.04 at 14:15:31 EDT [-] System.out.println("Hello World");
2015.10.04 at 14:15:31 EDT [LINE ADDED (samplepackage.SampleClass1)]: 15 $ [QUALIFIED_NAME] (System) | (out) | (println) | ("Class is removed! ") | 
2015.10.04 at 14:15:31 EDT [LINE REMOVED (samplepackage.SampleClass1)]: 15 $ [QUALIFIED_NAME] (System) | (out) | (println) | ("Hello World") | 
2015.10.04 at 14:15:41 EDT [PLUGIN CLOSED]

```

Sample .PublicTest_source.RECORDING file: 

```

2015.10.04 at 14:12:59 EDT [samplepackage.SampleClass1] 
---------------------------------------------------
package samplepackage;
public class SampleClass1 {
}

2015.10.04 at 14:14:30 EDT [samplepackage.SampleClass2] 
---------------------------------------------------
package samplepackage;
public class SampleClass2 {
}

2015.10.04 at 14:21:17 EDT [samplepackage.SampleClass1] 
---------------------------------------------------

package samplepackage;
public class SampleClass1 {
  int a;
  public boolean method(){
    int a=0;
    return true;
  }
  public SampleClass1(){
    boolean enabled=method();
    System.out.println("Class is removed! ");
  }
}


```

# Issues with Installation

If for some reasons the plugin is not installed properly, please check the log file in the ../yourworkspace/.metadata/.log . If the issue is somewhat solvable, such as requiring other software installation, then please do what is necessary. If the problem still exists, please send me an email to issac.chua12@gmail.com. Thank you!



# License and Copyright

Licensed under the terms of the Eclipse Public License, version 1.0.  The license is available at http://www.eclipse.org/legal/epl-v10.html

© 2015 University at Buffalo. All rights reserved. 
