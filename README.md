# Project Sapphire

![alt tag] (https://github.com/issacchern/Sapphire/blob/master/images/logo.jpg)

Sapphire is an eclipse plugin software that records the changes under Java Model in eclipse and exports the recorded data into SQLite database. This plugin is intended for research and educational purposes.  

# How to Use

1. Click [here](https://github.com/issacchern/Sapphire/blob/master/edu.buffalo.cse.sapphire_1.2.2.1.jar?raw=true) to download the Sapphire plugin.
2. Drop the Sapphire jar file that you downloaded earlier into eclipse/dropins folder.
3. Download or use your text editor to create a .sapphire file as toggle file and put it in your project/.settings folder.
![alt tag](https://github.com/issacchern/Sapphire/blob/master/images/part1.gif)

That's it! Feel free to download the source project if you are interested. 

NOTE: Plugin might not run as expected if you drop the .sapphire file while using eclipse. You have to restart eclipse to take effect. 


# Description

Sapphire plugin is like a real-time recording tool for eclipse. Whatever you write or do inside eclipse, it will be recorded into a  database file. The purpose of developing this plugin is to learn about the process of how beginner students start to write code. A good analogy would be that English teacher asks students to write few drafts before submitting their essays. We want to know what kind of mistakes or struggles that students have been through to get their assignments done. We care more about what students have learned throughout the course rather than just getting a good grade. By analyzing data that we collected from students, perhaps we can find a way to improve the teaching's quality and effectiveness at UB. 


# App Demo
![alt tag](https://github.com/issacchern/Sapphire/blob/master/images/part2.gif)




# Sample Output

This plugin will create a database file in the project folder. You can view the database file by using any third party database browser or tool. I use [Database Browser Portable](http://portableapps.com/apps/development/database_browser_portable) to view the file.

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
