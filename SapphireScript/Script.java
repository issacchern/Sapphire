import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Scanner;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class Script {

	public static ArrayList<File> arrayFile = new ArrayList<File>();
	public static ArrayList<File> arrayFileAll = new ArrayList<File>();

	public static ArrayList<File> findFile(String studentName,File file, String course, String keyword) {	
		File[] list = file.listFiles();
		if(list!=null)
			for (File fil : list){
				if(fil.getAbsolutePath().contains(course)){
					if (fil.isDirectory()){
						findFile(studentName,fil, course, keyword);
					} else if (studentName.equalsIgnoreCase(fil.getName())){

						if(fil.getAbsolutePath().contains(keyword)){
							arrayFile.add(fil);
						}
					}
				}
			}    
		return arrayFile;
	}


	public static ArrayList<File> findFile(File file, String course, String keyword) {	
		File[] list = file.listFiles();
		if(list!=null)
			for (File fil : list){
				if(fil.getAbsolutePath().contains(course)){
					if (fil.isDirectory()){
						findFile(fil, course, keyword);
					} else if(fil.getName().contains("jar") && fil.getAbsolutePath().contains(keyword)){
						arrayFileAll.add(fil);	 	
					}
				}
			}    
		return arrayFileAll;
	}

	public static void main (String args[]) throws IOException{

		System.out.println("==== Welcome to Sapphire Script! ===");
		while(true){
			try {
				String line, name, id;
				boolean printNone = true;
				System.out.println("> Please choose mode: ");
				System.out.println("   1. Select all");
				System.out.println("   2. Select consented only");
				Scanner scanner = new Scanner(System.in);
				String mode = scanner.nextLine();

				if(Integer.parseInt(mode) == 1) {
					System.out.println("> Specify project location: ");
					String directory = scanner.nextLine();
					System.out.println("> Specify output location: ");
					String outputFolder = scanner.nextLine();
					System.out.println("> Select course(s): ");
					System.out.println("   1. CSE 115");
					System.out.println("   2. CSE 116");
					String courses = scanner.nextLine();	  
					String course = "";
					if(Integer.parseInt(courses) == 1){
						course = "115";
					} else if(Integer.parseInt(courses) == 2){
						course = "116";
					} else{
						course = "";
					}
					System.out.println("> Output file name:");
					System.out.println("   1. Student name");
					System.out.println("   2. Anonymous");
					String outputName = scanner.nextLine();

					boolean isAnonymous = false;
					int k = 1;

					if(Integer.parseInt(outputName) == 2) {
						isAnonymous = true;
					}

					// this is going to be the folder name as well
					boolean isLabAvailable = false;
					String labs;
					String [] lab;

					do{
						System.out.println("> Specify lab assignments (use ',' to seperate):");	    	
						labs = scanner.nextLine().replaceAll(" ", "");
						lab = labs.split(",");
						if(lab.length > 0 && lab[0].length() != 0){
							isLabAvailable = true;
						} else{
							System.out.println("Invalid input!");
						}

					} while(!isLabAvailable);

					for(int i = 0; i < lab.length; i++){
						arrayFile.clear();
						arrayFileAll.clear();
						arrayFileAll = findFile(new File(directory), course,lab[i]);
						System.out.println("In " + lab[i]);
						
						if(arrayFileAll.size() > 0){

							boolean isFirstTime = true;
							for(int j = 0; j <arrayFileAll.size(); j++){
								String tempName = "";
								if(isFirstTime) tempName = arrayFileAll.get(j).getName();
								File theDir = new File(outputFolder);
								if (!theDir.exists()){
									theDir.mkdir();
								}

								File theDirSub = new File(outputFolder + "/" + lab[i]);
								if (!theDirSub.exists()){
									theDirSub.mkdir();
								}

								String finalPath = theDirSub.getAbsolutePath() + "/" +  arrayFileAll.get(j).getName() + ".db";

								JarFile jar = new JarFile(arrayFileAll.get(j));
								Enumeration enumEntries = jar.entries();
								while(enumEntries.hasMoreElements()){
									JarEntry file = (JarEntry) enumEntries.nextElement();						    	    	
									if(file.getName().contains(".sqlite.db")){
										System.out.println("FOUND .sqlite.db IN " + arrayFileAll.get(j).getAbsolutePath());
										printNone = false;
										InputStream is = jar.getInputStream(file);
										if(isAnonymous){
											finalPath = theDirSub.getAbsolutePath() + "/" + "USER" + k + ".db";
											if(isFirstTime){
												isFirstTime = false;

											} else{
												if(!arrayFileAll.get(j).getName().equals(tempName)){
													k++;
												}		
											}

											tempName = arrayFileAll.get(j).getName();
										}

										FileOutputStream fos = new FileOutputStream(finalPath);
										while(is.available() > 0){
											fos.write(is.read());
										}
										fos.close();
										is.close();
										break;
									}	
								}
							}
						}
					}

					if(printNone){
						System.out.println("No .sqlite.db found! Try again? (y/n)");
					} else{
						System.out.println("Complete! Do you wish to try again? (y/n)");
					}

					String printLast = scanner.nextLine();
					if(printLast.equals("y") || printLast.equals("Y") ){

					}
					else{
						System.out.println("\nDone! Program is terminated.");
						break;
					}
				}

				else if (Integer.parseInt(mode) == 2){
					System.out.println("> Please locate your student_list text file: ");
					String student_str = scanner.nextLine();
					BufferedReader br = new BufferedReader(new FileReader(student_str));
					System.out.println("> Specify project location: ");
					String directory = scanner.nextLine();
					System.out.println("> Specify output location: ");
					String outputFolder = scanner.nextLine();
					System.out.println("> Select course(s): ");
					System.out.println("   1. CSE 115");
					System.out.println("   2. CSE 116");
					String courses = scanner.nextLine();	  
					String course = "";
					if(Integer.parseInt(courses) == 1){
						course = "115";
					} else if(Integer.parseInt(courses) == 2){
						course = "116";
					} else{
						course = "";
					}
					// this is going to be the folder name as well
					boolean isLabAvailable = false;
					String labs;
					String [] lab;

					do{
						System.out.println("> Specify lab assignments (use ',' to seperate):");	    	
						labs = scanner.nextLine().replaceAll(" ", "");
						lab = labs.split(",");
						if(lab.length > 0 && lab[0].length() != 0){
							isLabAvailable = true;
						} else{
							System.out.println("Invalid input!");
						}

					} while(!isLabAvailable);


					while ((line = br.readLine()) != null) {

						id = line.substring(0, line.indexOf(" ")) ;
						name = line.substring(line.indexOf(" ") + 1, line.length()) + ".jar";

						for(int i = 0; i < lab.length ; i++){
							arrayFile.clear();
							arrayFileAll.clear();
							arrayFile = findFile(name,new File(directory), course,lab[i]);

							if(arrayFile.size() > 0){

								File theLastFile = arrayFile.get(arrayFile.size() - 1);
								File theDir = new File(outputFolder);
								if (!theDir.exists()){
									theDir.mkdir();
								}

								File theDirSub = new File(outputFolder + "/" + lab[i]);
								if (!theDirSub.exists()){
									theDirSub.mkdir();
								}

								String finalPath = theDirSub.getAbsolutePath() + "/" + "USER" + id + ".db";
								JarFile jar = new JarFile(theLastFile);
								Enumeration enumEntries = jar.entries();
								while(enumEntries.hasMoreElements()){
									JarEntry file = (JarEntry) enumEntries.nextElement();						    	    	
									if(file.getName().contains(".sqlite.db")){
										System.out.println("FOUND .sqlite.db IN " + theLastFile.getAbsolutePath());
										printNone = false;
										InputStream is = jar.getInputStream(file);
										FileOutputStream fos = new FileOutputStream(finalPath);
										while(is.available() > 0){
											fos.write(is.read());
										}
										fos.close();
										is.close();
										break;
									}	
								}
							}		
						}
					}

					if(printNone){
						System.out.println("> No .sqlite.db found! Try again? (y/n)");
					} else{
						System.out.println("> Complete! Do you wish to try again? (y/n)");
					}

					String printLast = scanner.nextLine();
					if(printLast.equals("y") || printLast.equals("Y") ){

					}
					else{
						System.out.println("\nDone! Program is terminated.");
						break;
					}
				} else{
					System.out.println("Invalid option!");
				}
			} catch(NumberFormatException e){
				System.out.println("Invalid option!");

			}
			catch (Exception e){
				System.out.println("INPUT ERROR: " + e);
			}		
		}

	}
}
