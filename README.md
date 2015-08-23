# Project Sapphire

Sapphire is a free eclipse plugin software that captures all the changes under JavaModel and output to the file. 

# Installation

1. Click Download Zip button at the bottom right, extract the folder to your workspace.
2. Run eclipse navigation and navigate to File> Import...
3. Select Existing Projects into Workspace and browse for the folder you extracted earlier.
4. Once the project is created, open the project and double click plugin.xml.
5. Select Overview tab and click Export Wizard at the right end corner. 
6. Click finish. Extract the jar file from the Zip file and drop it in the dropins folder inside the eclipse folder.
7. You are all set!

# Description

This plugin is used for recording the changes that users made under JavaModel from time to time. The objective of developing this plugin is to help students in learning how to code efficiently. By analyzing the output file generated from the plugin, we can see how the students wrote the code and we will see the kind of common mistakes they made and what their struggles are while coding. Thereby, we can teach students the right way of coding and avoid making unnecessary mistakes.


NOTE: This plugin will run automatically once installed.

# Sample Output

Every Java Project will have its own recorded file. Here's a sample of recorded file. 

```
2015.08.22 at 23:55:31 EDT [INITIALIZE PLUGIN]
2015.08.22 at 23:59:45 EDT [issacPackage.IssacClass] [ADDED LINE] : 6 $ [FIELD_DECLARATION] (int) | (b) | 
2015.08.22 at 23:59:45 EDT [issacPackage.IssacClass] [ADDED LINE] : 7 $ [FIELD_DECLARATION] (int) | (c) | 
2015.08.22 at 23:59:45 EDT [issacPackage.IssacClass] [ADDED LINE] : 8 $ [FIELD_DECLARATION] (int) | (d) | 
2015.08.22 at 23:59:53 EDT [issacPackage.IssacClass] [ADDED LINE] : 10 $ [METHOD_DECLARATION] keyword (public) | constructor (false) | (void) | (method) | 
2015.08.22 at 23:59:56 EDT [issacPackage.IssacClass] [ADDED LINE] : 12 $ [VARIABLE_DECLARATION_STATEMENT] (int) | (e) | 
2015.08.22 at 23:59:56 EDT [issacPackage.IssacClass] [ADDED LINE] : 13 $ [VARIABLE_DECLARATION_STATEMENT] (int) | (f) | 
2015.08.23 at 00:00:11 EDT [issacPackage.IssacClass] [REMOVED LINE] : 6 $ [FIELD_DECLARATION] (int) | (b) | 
2015.08.23 at 00:00:11 EDT [issacPackage.IssacClass] [REMOVED LINE] : 7 $ [FIELD_DECLARATION] (int) | (c) | 
2015.08.23 at 00:00:11 EDT [issacPackage.IssacClass] [REMOVED LINE] : 8 $ [FIELD_DECLARATION] (int) | (d) | 
2015.08.23 at 00:00:13 EDT [issacPackage.IssacClass] [REMOVED LINE] : 9 $ [VARIABLE_DECLARATION_STATEMENT] (int) | (f) | 
2015.08.23 at 00:17:48 EDT [ADD COMPILATION_UNIT]: IssacClass2.java (not open) [in issacPackage [in src [in IssacProject]]]
2015.08.23 at 00:17:49 EDT [CHANGE COMPILATION_UNIT TO issacPackage.IssacClass2]
2015.08.23 at 00:18:48 EDT [ADD COMPILATION_UNIT]: IssacClassNew.java (not open) [in issacPackage [in src [in IssacProject]]]
2015.08.23 at 00:18:48 EDT [REMOVE COMPILATION_UNIT]: IssacClass.java (not open) [in issacPackage [in src [in IssacProject]]]
2015.08.23 at 00:28:58 EDT [ADD PACKAGE_FRAGMENT]: carlPackage (not open) [in src [in CarlProject]]
2015.08.23 at 00:29:53 EDT [ADD COMPILATION_UNIT]: CarlClass.java (not open) [in carlPackage [in src [in CarlProject]]]
2015.08.23 at 00:29:54 EDT [CLOSE PLUGIN]


```



# Licenses and Copyrights

Sapphire is a free plugin software, licensed under the terms of the Eclipse Public License, version 1.0.  The license is available at http://www.eclipse.org/legal/epl-v10.html

© 2015 University at Buffalo. All rights reserved. 
