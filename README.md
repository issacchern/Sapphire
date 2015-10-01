# Project Sapphire

![alt tag] (https://github.com/issacchern/Sapphire/blob/master/logo.jpg)

Sapphire is a free eclipse plugin software that records the changes under JavaModel in eclipse and exports the recorded data to files. This plugin is intended for educational and teaching purposes.  

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

NOTE: This plugin will run automatically once installed.

# Description

The objective of creating this plugin is to study the process of how beginner students learn writing codes instead of obtaining the final result. An analogy would be that English teacher asking students to write a few drafts before submitting their essays. There are a lot of ways to complete a coding assignment, probably by working hard by themselves or seeking help from others, or even getting codes online or from friends. For whatever method is, we wouldn't know what kind of struggles students have been through to get their assignments done by analyzing their submitted codes. Even though we use plagiarism detector nowadays, but there is no way to know if the source is authentic and original. Students can always change the structure flow of the code and make some unecessary changes to bypass the detection, but that's not our main concern here. We want students to learn, and we are looking for a way to teach beginner students to learn coding efficiently. We will be collecting the data input from students and analyzing the data to learn the general patterns and styles of coding. 


# Sample Output

This plugin will create two recorded files in the same project folder, .PROJECT_NAME.RECORDING and .PROJECT_NAME_source.RECORDING file. The first one will be recording all the changed events while the second one will be printing the initial code in the Java editor once the plugin starts. 

Sample .PublicTest.RECORDING file:

```
2015.10.01 at 10:03:46 EDT [PLUGIN INITIALIZED]
2015.10.01 at 10:03:46 EDT [JAVA_PROJECT ADDED]: PublicTest (not open)
2015.10.01 at 10:03:47 EDT [PACKAGE_FRAGMENT_ROOT ADDED]: src (not open) [in PublicTest]
2015.10.01 at 10:04:53 EDT [COMPILATION_UNIT ADDED]: Class1.java (not open) [in publicpackage [in src [in PublicTest]]]
2015.10.01 at 10:04:55 EDT [COMPILATION_UNIT CHANGED TO publicpackage.Class1]
2015.10.01 at 10:04:55 EDT [SOURCE FILE INITIALIZED] 
2015.10.01 at 10:05:27 EDT [+] int x;
2015.10.01 at 10:05:27 EDT [+] int y;
2015.10.01 at 10:05:27 EDT [+] int z;
2015.10.01 at 10:05:27 EDT [LINE ADDED (publicpackage.Class1)]: 5 $ [FIELD_DECLARATION] (int) | (x) | 
2015.10.01 at 10:05:27 EDT [LINE ADDED (publicpackage.Class1)]: 6 $ [FIELD_DECLARATION] (int) | (y) | 
2015.10.01 at 10:05:27 EDT [LINE ADDED (publicpackage.Class1)]: 7 $ [FIELD_DECLARATION] (int) | (z) | 
2015.10.01 at 10:05:34 EDT [+] public Class1(){
2015.10.01 at 10:05:34 EDT [+] }
2015.10.01 at 10:05:34 EDT [LINE ADDED (publicpackage.Class1)]: 9 $ [METHOD_DECLARATION] keyword (public) | constructor (true) | (Class1) | 
2015.10.01 at 10:05:34 EDT [LINE ADDED (publicpackage.Class1)]: 9 $ OPEN [METHOD_DECLARATION]
2015.10.01 at 10:05:39 EDT [+] public void method(){
2015.10.01 at 10:05:39 EDT [+] }
2015.10.01 at 10:05:39 EDT [LINE ADDED (publicpackage.Class1)]: 13 $ [METHOD_DECLARATION] keyword (public) | constructor (false) | (void) | (method) | 
2015.10.01 at 10:05:54 EDT [+] public Class1(int x1,int x2){
2015.10.01 at 10:05:54 EDT [-] public Class1(){
2015.10.01 at 10:05:54 EDT [LINE ADDED (publicpackage.Class1)]: 9 $ [METHOD_DECLARATION] keyword (public) | constructor (true) | (Class1) | (int) | varargs (false) | (x1) | (int) | varargs (false) | (x2) | 
2015.10.01 at 10:05:54 EDT [LINE REMOVED (publicpackage.Class1)]: 9 $ [METHOD_DECLARATION] keyword (public) | constructor (true) | (Class1) | 
2015.10.01 at 10:06:02 EDT [ERROR DETECTED(publicpackage.Class1)]: 11 * Syntax error, insert "AssignmentOperator Expression" to complete Assignment
2015.10.01 at 10:06:02 EDT [ERROR DETECTED(publicpackage.Class1)]: 11 * Syntax error, insert ";" to complete Statement
2015.10.01 at 10:06:02 EDT [ERROR DETECTED(publicpackage.Class1)]: 11 * The left-hand side of an assignment must be a variable
2015.10.01 at 10:06:06 EDT [ERROR DETECTED(publicpackage.Class1)]: 11 * Syntax error, insert "AssignmentOperator Expression" to complete Assignment
2015.10.01 at 10:06:06 EDT [ERROR DETECTED(publicpackage.Class1)]: 11 * Syntax error, insert ";" to complete Statement
2015.10.01 at 10:06:08 EDT [ERROR DETECTED(publicpackage.Class1)]: 11 * Syntax error, insert "AssignmentOperator Expression" to complete Assignment
2015.10.01 at 10:06:08 EDT [ERROR DETECTED(publicpackage.Class1)]: 11 * Syntax error, insert ";" to complete Statement
2015.10.01 at 10:06:10 EDT [ERROR DETECTED(publicpackage.Class1)]: 11 * y1 cannot be resolved to a variable
2015.10.01 at 10:06:14 EDT [ERROR DETECTED(publicpackage.Class1)]: 11 * y1 cannot be resolved to a variable
2015.10.01 at 10:06:18 EDT [+] public Class1(int x1,int y1){
2015.10.01 at 10:06:18 EDT [+] this.x=x1;
2015.10.01 at 10:06:18 EDT [+] this.y=y1;
2015.10.01 at 10:06:18 EDT [-] public Class1(int x1,int x2){
2015.10.01 at 10:06:18 EDT [LINE ADDED (publicpackage.Class1)]: 9 $ [METHOD_DECLARATION] keyword (public) | constructor (true) | (Class1) | (int) | varargs (false) | (x1) | (int) | varargs (false) | (y1) | 

```

Sample .PublicTest_source.RECORDING file: 

```

2015.10.01 at 10:04:55 EDT [publicpackage.Class1] 
---------------------------------------------------
package publicpackage;
public class Class1 {
}

2015.10.01 at 10:11:16 EDT [publicpackage.Class1] 
---------------------------------------------------
package publicpackage;
public class Class1 {
  int x;
  int y;
  int z;
  public Class1(  int x1,  int y1){
    this.x=x1;
    this.y=y1;
  }
  public String method(  int a){
    String s;
    if (a > 5) {
      s="hello_world!";
    }
 else {
      s="hello_universe!";
    }
    return s;
  }
}

```




# License and Copyright

Licensed under the terms of the Eclipse Public License, version 1.0.  The license is available at http://www.eclipse.org/legal/epl-v10.html

© 2015 University at Buffalo. All rights reserved. 
