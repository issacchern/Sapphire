# Project Sapphire

![alt tag] (https://github.com/issacchern/Sapphire/blob/master/images/logo.jpg)

Sapphire is an eclipse plugin software that records the changes under Java Model in eclipse and exports the recorded data into SQLite database. This plugin is intended for research and educational purposes.  

# Installation

1. Click [here](https://github.com/issacchern/Sapphire/blob/master/edu.buffalo.cse.sapphire_1.2.2.1.jar?raw=true) to download the Sapphire plugin.
2. Drop the Sapphire jar file that you downloaded earlier into eclipse/dropins folder.
3. Download or use your text editor to create a .sapphire file as toggle file and put it in your project/.settings folder.
![alt tag](https://github.com/issacchern/Sapphire/blob/master/images/part1.gif)


That's it! Feel free to download the source project if you are interested. 


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

If for some reasons the plugin is not installed properly, please check the log file in the ../yourworkspace/.metadata/.log . If the issue is somewhat solvable, such as requiring other software installation, then please do what is necessary. If the problem still exists, please send me an email at issac.chua12@gmail.com. Thank you!



# License and Copyright

Sapphire is a free plugin software, licensed under the terms of the Eclipse Public License, version 1.0.  The license is available at http://www.eclipse.org/legal/epl-v10.html  
Sapphire uses sqlite-jdbc from xerial as referenced library, which follows Apache License 2.0. The license is available at http://www.apache.org/licenses/  

© 2015 University at Buffalo. All rights reserved. 
