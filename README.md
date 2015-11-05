# Project Sapphire

![alt tag] (https://github.com/issacchern/Sapphire/blob/master/images/logo.jpg)

Sapphire is a free eclipse plugin software that records the changes under JavaModel in eclipse and exports the recorded data to SQLite database. This plugin is intended for educational and teaching purposes.  

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

This plugin will create .db file for SQLite database in the project folder. Snapshots below are the main tables of the database.

- Main Event Table:

![alt tag] (https://github.com/issacchern/Sapphire/blob/master/images/main.JPG)

- CompilationUnit Table:

![alt tag] (https://github.com/issacchern/Sapphire/blob/master/images/cu.JPG)


- Source File Table:

![alt tag] (https://github.com/issacchern/Sapphire/blob/master/images/source.JPG)



# Issues with Installation

If for some reasons the plugin is not installed properly, please check the log file in the ../yourworkspace/.metadata/.log . If the issue is somewhat solvable, such as requiring other software installation, then please do what is necessary. If the problem still exists, please send me an email to issac.chua12@gmail.com. Thank you!



# License and Copyright

Sapphire is a free plugin software, licensed under the terms of the Eclipse Public License, version 1.0.  The license is available at http://www.eclipse.org/legal/epl-v10.html  
Sapphire uses sqlite-jdbc from xerial as referenced library, which follows Apache License 2.0. The license is available at http://www.apache.org/licenses/  

© 2015 University at Buffalo. All rights reserved. 
