/**
 * This file is part of Sapphire.
 * 
 * Sapphire is a free plugin software, licensed under the terms of the 
 * Eclipse Public License, version 1.0.  The license is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Sapphire uses sqlite-jdbc from xerial as referenced library, 
 * which follows Apache License 2.0. The license is available at 
 * http://www.apache.org/licenses/
 * 
 * Sapphire © 2015 University at Buffalo. All rights reserved.  
 */

package edu.buffalo.cse.Sapphire;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import static org.eclipse.jdt.core.IJavaElement.JAVA_MODEL;
import static org.eclipse.jdt.core.IJavaElement.JAVA_PROJECT;
import static org.eclipse.jdt.core.IJavaElement.COMPILATION_UNIT;
import static org.eclipse.jdt.core.IJavaElement.PACKAGE_FRAGMENT;
import static org.eclipse.jdt.core.IJavaElement.PACKAGE_FRAGMENT_ROOT;
import static org.eclipse.jdt.core.dom.ASTNode.IMPORT_DECLARATION;

import org.eclipse.jdt.core.ElementChangedEvent;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IElementChangedListener;
import org.eclipse.jdt.core.IJavaElementDelta;
import org.eclipse.jdt.core.compiler.IProblem;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ChildListPropertyDescriptor;
import org.eclipse.jdt.core.dom.ChildPropertyDescriptor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.SimplePropertyDescriptor;

import edu.buffalo.cse.Sapphire.diff_match_patch.Diff;


/**
 * This class handles all the changes to the JavaModel, recording all of
 * the changes that the user has made into a text file. 
 * 
 * NOTE: If you add the .sapphire file while using eclipse, chances 
 * are the plugin will not be installed. You will have to restart 
 * eclipse to run the plugin.
 * 
 * @author Chern Yee Chua
 */
public class JavaModelListenerNew implements IElementChangedListener{
	
	private static SQLiteHelper sqlh;
	private static String fileNameAndLocation = "";
	private static String className = "";
	private static String long2 = "";
	private static String previousJavaProjectName = "";
	private static String projectAndClassName = "";
	private static boolean visitJavaModel = false;
	private static boolean enableRecording = false;
	private static int lastLineNumber = 0;
	private static int lineNumberCheck = 0;
	private static HashMap<String, String> mapSource = new HashMap<String, String>(); 
	private static HashMap<String, String> mapHolder = new HashMap<String, String>();
	private static HashMap<Integer, String> mapValue; // for node type
	private static IElementChangedListener _listener = new JavaModelListenerNew();
	private static String sourceTemp = "";
	private static CompilationUnit astRoot;
	private ICompilationUnit cu;
	private static String source = "";
	private static List<String> stringArray = new ArrayList<String>();
	private static List<String> stringArrayContent = new ArrayList<String>();
	private static List<Integer> stringArrayNumber = new ArrayList<Integer>();
	
	private static String str = "";
	private static String strTemp = "";
	private static String str2 = "";
	private static String strTemp2 = "";
	private static ArrayList<String> errorMessageArray = new ArrayList<String>(); 
	private static ArrayList<Integer> errorNumberArray = new ArrayList<Integer>();
	private static ArrayList<String> errorArray = new ArrayList<String>();
	
	private static final int NUMBER_CHARACTER = 100;
	private static String current_state = "";
	
	private static final int DURATION = 2500;
	private String current_time= "";
	private String past_time = "" ;
	private static String srcHolder = "";

	
	/**
	 * The constructor initializes necessary functions and variables.
	 * @author Chern Yee Chua
	 */
	
	public JavaModelListenerNew(){

		// initialize sqlite helper class
		sqlh = new SQLiteHelper();
		// initialize the past time for the interval time
		past_time = new SimpleDateFormat("yyyy.MM.dd 'at' HH:mm:ss z").format(Calendar.getInstance().getTime());
		
        mapValue = new HashMap<Integer , String>();
        mapValue.put(1, "[ANONYMOUS_CLASS_DECLARATION]");
        mapValue.put(2, "[ARRAY_ACCESS]");
        mapValue.put(3, "[ARRAY_CREATION]");
        mapValue.put(4, "[ARRAY_INITIALIZER]");
        mapValue.put(5, "[ARRAY_TYPE]");
        mapValue.put(6, "[ASSERT_STATEMENT]");
        mapValue.put(7, "[ASSIGNMENT]");
        mapValue.put(8, "[BLOCK]");
        mapValue.put(9, "[BOOLEAN_LITERAL]");
        mapValue.put(10, "[BREAK_STATEMENT]");
        mapValue.put(11, "[CAST_EXPRESSION]");
        mapValue.put(12, "[CATCH_CLAUSE]");
        mapValue.put(13, "[CHARACTER_LITERAL]");
        mapValue.put(14, "CLASS_INSTANCE_CREATION]");
        mapValue.put(15, "[COMPILATION_UNIT]");
        mapValue.put(16, "[CONDITIONAL_EXPRESSION]");
        mapValue.put(17, "[CONSTRUCTOR_INVOCATION]");
        mapValue.put(18, "[CONTINUE_STATEMENT]");
        mapValue.put(19, "[DO_STATEMENT]");
        mapValue.put(20, "[EMPTY_STATEMENT]");
        mapValue.put(21, "[EXPRESSION_STATEMENT]");
        mapValue.put(22, "[FIELD_ACCESS]");
        mapValue.put(23, "[FIELD_DECLARATION]");
        mapValue.put(24, "[FOR_STATEMENT]");
        mapValue.put(25, "[IF_STATEMENT]");
        mapValue.put(26, "[IMPORT_DECLARATION]");
        mapValue.put(27, "[INFIX_EXPRESSION]");
        mapValue.put(28, "[INITIALIZER]");
        mapValue.put(29, "[JAVADOC]");
        mapValue.put(30, "[LABELED_STATEMENT]");
        mapValue.put(31, "[METHOD_DECLARATION]");
        mapValue.put(32, "[METHOD_INVOCATION]");
        mapValue.put(33, "[NULL_LITERAL]");
        mapValue.put(34, "[NUMBER_LITERAL]");
        mapValue.put(35, "[PACKAGE_DECLARATION]");
        mapValue.put(36, "[PARENTHESIZED_EXPRESSION]");
        mapValue.put(37, "[POSTFIX_EXPRESSION]");
        mapValue.put(38, "[PREFIX_EXPRESSION]");
        mapValue.put(39, "[PRIMITIVE_TYPE]");
        mapValue.put(40, "[QUALIFIED_NAME]");
        mapValue.put(41, "[RETURN_STATEMENT]");
        mapValue.put(42, "[SIMPLE_NAME]");
        mapValue.put(43, "[SIMPLE_TYPE]");
        mapValue.put(44, "[SINGLE_VARIABLE_DECLARATION]");
        mapValue.put(45, "[STRING_LITERAL]");
        mapValue.put(46, "[SUPER_CONSTRUCTOR_INVOCATION]");
        mapValue.put(47, "[SUPER_FIELD_ACCESS]");
        mapValue.put(48, "[SUPER_METHOD_INVOCATION]");
        mapValue.put(49, "[SWITCH_CASE]");
        mapValue.put(50, "[SWITCH_STATEMENT]");
        mapValue.put(51, "[SYNCHRONIZED_STATEMENT]");
        mapValue.put(52, "[THIS_EXPRESSION]");
        mapValue.put(53, "[THROW_STATEMENT]");
        mapValue.put(54, "[TRY_STATEMENT]");
        mapValue.put(55, "[TYPE_DECLARATION]");
        mapValue.put(56, "[TYPE_DECLARATION_STATEMENT]");
        mapValue.put(57, "[TYPE_LITERAL]");
        mapValue.put(58, "[VARIABLE_DECLARATION_EXPRESSION]");
        mapValue.put(59, "[VARIABLE_DECLARATION_FRAGMENT]");
        mapValue.put(60, "[VARIABLE_DECLARATION_STATEMENT]");
        mapValue.put(61, "[WHILE_STATEMENT]");
        mapValue.put(62, "[INSTANCEOF_EXPRESSION]");
        mapValue.put(63, "[LINE_COMMENT]");
        mapValue.put(64, "[BLOCK_COMMENT]");
        mapValue.put(65, "[TAG_ELEMENT]");
        mapValue.put(66, "[TEXT_ELEMENT]");
        mapValue.put(67, "[MEMBER_REF]");
        mapValue.put(68, "[METHOD_REF]");
        mapValue.put(69, "[METHOD_REF_PARAMETER]");
        mapValue.put(70, "[ENHANCED_FOR_STATEMENT]");
        mapValue.put(71, "[ENUM_DECLARATION]");
        mapValue.put(72, "[ENUM_CONSTANT_DECLARATION]");
        mapValue.put(73, "[TYPE_PARAMETER]");
        mapValue.put(74, "[PARAMETERIZED_TYPE]");
        mapValue.put(75, "[QUALIFIED_TYPE]");
        mapValue.put(76, "[WILDCARD_TYPE]");
        mapValue.put(77, "[NORMAL_ANNOTATION]");
        mapValue.put(78, "[MARKER_ANNOTATION]");
        mapValue.put(79, "[SINGLE_MEMBER_ANNOTATION]");
        mapValue.put(80, "[MEMBER_VALUE_PAIR]");
        mapValue.put(81, "[ANNOTATION_TYPE_DECLARATION]");    
	}


	/**
	 * This is the main function of all changes under JavaModel. Depending 
	 * on the event type, there are two types of event: those under JavaModel 
	 * but not under CompilationUnit and those under CompilationUnit.
	 * 
	 * @author Chern Yee Chua
	 */
	
	@Override
	public void elementChanged(ElementChangedEvent event) {	
			
		if(event.getDelta().getElement().getElementType() == JAVA_MODEL){
			traverseAndPrintJavaModel(event.getDelta());
		}
		else{
			
			if(visitJavaModel == false){	
				traverseCompilationUnit(event);							
			}		
			else{
				visitJavaModel = false;
			}
		}	
	}


	/**
	 * This function checks if there is any error in the CompilationUnit.
	 * 
	 * @author Chern Yee Chua
	 */	
	
	public boolean error_checking(ElementChangedEvent event){
		
		// prevent null exception
		if(event.getDelta().getCompilationUnitAST() == null){
			return false;
		}
		
		if (event.getDelta().getCompilationUnitAST().getProblems().length > 0){
			boolean true_or_false = true;
			
			
			for (IProblem problem : (event.getDelta().getCompilationUnitAST().getProblems())){
				if (problem.isError()){
					true_or_false = false;
					
					int number = problem.getSourceLineNumber();
					String strnumber = problem.getSourceLineNumber() + " * " + problem.getMessage();
					String str = "" + problem.getMessage();
					
					errorNumberArray.add(number);
					errorArray.add(strnumber);
					errorMessageArray.add(str);
					
				}
			}
		
			return true_or_false;				
		}
		
		else{
			return true;	
		}
	}
	

	
	public void checkSource(ElementChangedEvent event){
		
		if(enableRecording){
			
			try{
				
				Calendar cal = Calendar.getInstance();
		        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd 'at' HH:mm:ss z");
				
		        current_time = sdf.format(cal.getTime());
				
				Date date1 = sdf.parse(current_time);
				Date date2 = sdf.parse(past_time);
				if( date1.getTime() - date2.getTime() > DURATION){
					past_time = current_time;
					printSource(event);
				}
				else{
					past_time = current_time;
				}
	
				
			} catch(Exception e){
				e.printStackTrace();
			}
	
		}	
	}
	
	
	public void printSource(ElementChangedEvent event){
		
		try{
			if(sourceTemp.equals("")){
				
				sourceTemp = ((ICompilationUnit)event.getDelta().getElement()).getSource();
				
				error_checking(event);
				
				String[] lines = sourceTemp.split("\r\n|\r|\n");
				strTemp = "";
				strTemp2 = "";
				int j = 0;
				for (int i = 0; i < lines.length ; i++){
					if(lines[i].matches("^.*[^a-zA-Z0-9 ].*$") && !lines[i].matches("^\\s*$")){
						j = i + 1;
						
						strTemp2 = strTemp2 + lines[i] + "\n";
						if(j < 10){
							strTemp = strTemp + " 000" + j + " " + lines[i] + "\n";
						} else if( j < 100){
							strTemp = strTemp + " 00" + j + " " + lines[i] + "\n";						
						} else if( j < 1000){
							strTemp = strTemp + " 0" + j + " " + lines[i] + "\n";
						} else {
							strTemp = strTemp + " " + j + " " + lines[i] + "\n";					
						}					
					}
				}
				
				String errorLine = "";
				for(int i = 0; i < errorArray.size(); i++){
					errorLine = errorLine + errorArray.get(i) + "\n";
				}
				
				
				sqlh.sqlMain(fileNameAndLocation, "Source File Initialized", className);				
				sqlh.sqlSource(fileNameAndLocation, className, errorLine, "Source file initialized",strTemp.replaceAll("  " ,"").replaceAll("\t", "   "));
				//srcHolder = strTemp.replaceAll("  " ,"").replaceAll("\t", "   ");
				current_state = "main";
				mapHolder.put(projectAndClassName, strTemp2);
				arrayClear();
				
				
			} else{
				/* regenerate strTemp and strTemp2 */
				{
				String[] lines = sourceTemp.split("\r\n|\r|\n");
				strTemp = "";
				strTemp2 = "";
				int j = 0;
				for (int i = 0; i < lines.length ; i++){
					if(lines[i].matches("^.*[^a-zA-Z0-9 ].*$") && !lines[i].matches("^\\s*$")){
						j = i + 1;
						
						strTemp2 = strTemp2 + lines[i] + "\n";
						if(j < 10){
							strTemp = strTemp + " 000" + j + " " + lines[i] + "\n";
						} else if( j < 100){
							strTemp = strTemp + " 00" + j + " " + lines[i] + "\n";						
						} else if( j < 1000){
							strTemp = strTemp + " 0" + j + " " + lines[i] + "\n";
						} else {
							strTemp = strTemp + " " + j + " " + lines[i] + "\n";					
						}					
					}
				}
				}
				/* ---------------------------------*/
				
				
				
				error_checking(event);
				cu = (ICompilationUnit) event.getDelta().getElement();
				source = cu.getSource();
				ASTParser parser = ASTParser.newParser(AST.JLS8);
				parser.setSource(cu);
				parser.setKind(ASTParser.K_COMPILATION_UNIT);
				parser.setResolveBindings(true);
				astRoot = (CompilationUnit) parser.createAST(null);
				
				long2 = "";	
				lineNumberCheck = 0;			
				print(astRoot);
				
				if(lineNumberCheck != 0 || long2.length() != 0){
					// add the last piece of element
					stringArrayNumber.add(lineNumberCheck);
					if(lineNumberCheck < 10){
						stringArray.add( "000" + lineNumberCheck + " " + long2); 	
					} else if(lineNumberCheck <100){
						stringArray.add( "00" + lineNumberCheck + " " + long2);
					} else if(lineNumberCheck < 1000){
						stringArray.add( "0" + lineNumberCheck + " " + long2);
					} else{
						stringArray.add(lineNumberCheck + " " + long2);
					}
					stringArrayContent.add(long2);
				}
				
				
				// this is after removing empty lines
				String[] lines = source.split("\r\n|\r|\n");
				str = "";
				str2 = "";
				
				ArrayList<String> array = new ArrayList<String>();
				for (int i = 0; i < lines.length ; i++){
					if(lines[i].matches("^.*[^a-zA-Z0-9 ].*$") && !lines[i].matches("^\\s*$")){
					
						int j = i + 1;
						str2 = str2 + lines[i] + "\n";
						if(j < 10){
							str = str + " 000" + j + " " + lines[i] + "\n";
							array.add(" 000" + j + " " + lines[i]);
							//System.out.println("   " + j + " " + lines[i]);	
						} else if( j < 100){
							str = str + " 00" + j + " " + lines[i] + "\n";
							array.add(" 00" + j + " " + lines[i]);
							//System.out.println("  " + j + " " + lines[i]);
			
						} else if( j < 1000){
							str = str + " 0" + j + " " + lines[i] + "\n";	
							array.add(" 0" + j + " " + lines[i]);
							//System.out.println(" " + j + " " + lines[i]);
							
						} else {
							str = str + " " + j + " " + lines[i] + "\n";	
							array.add(" " + j + " " + lines[i]);
							//System.out.println("" + j + " " + lines[i]);
						}			
					}		
				}
				
				

				if(mapHolder.containsKey(projectAndClassName)){
					srcHolder =  mapHolder.get(projectAndClassName);
				}
				
				
				
		    	if(str2.length() - srcHolder.length() > NUMBER_CHARACTER || srcHolder.length() - str2.length() > NUMBER_CHARACTER){
		    				    	
			    	String errorLine = "";
			    	for(int i = 0; i < errorArray.size(); i++){
			    		errorLine = errorLine + errorArray.get(i) + "\n";
			    	}
			    	
			    	String strX = "";
			    	
			    	diff_match_patch dmpX = new diff_match_patch();
					LinkedList<Diff> dfX = dmpX.diff_lines_only(srcHolder,str2);
					
					ArrayList<String> a1X = new ArrayList<String>();
			    	ArrayList<String> a2X = new ArrayList<String>();
			    	
					for(int i = 0; i < dfX.size(); i++){
						String df_string = dfX.get(i).toString();
						String temp_df_string = df_string.substring(9,df_string.length() - 1).trim();
						
			    		if(df_string.substring(0, 6).equals("INSERT") && df_string.length() > 8 ){
			    			a1X.add(temp_df_string);	
			    		}
			    		else if(df_string.substring(0, 6).equals("DELETE") && df_string.length() > 8 ){
			    			a2X.add(temp_df_string);
			    		}
			    	}
					
					
					
			    	ArrayList<String> a1tempX = new ArrayList<String>();

			    	for(int i = 0; i < a1X.size(); i++){					
						String[] lines1 = a1X.get(i).split("\r\n|\r|\n");
						for (String line : lines1) {	
							if(line.length() > 0){
								
								if(line.charAt(0) == ' '){
									a1tempX.add(line.substring(1));
								} else{
									a1tempX.add(line);
								}
							}
						}	
					}
			    	
			    	ArrayList<String> a2tempX = new ArrayList<String>();

			    	for(int i = 0; i < a2X.size(); i++){
						String[] lines1 = a2X.get(i).split("\r\n|\r|\n");
						for (String line : lines1) {	
							if(line.length() > 0){	
								if(line.charAt(0) == ' '){
									a2tempX.add(line.substring(1));
								} else{
									a2tempX.add(line);
								}
							}
						}
					}
			    	
			    	for(int i = 0 ; i < a1tempX.size(); i++){
			    		strX = strX + "+ " + a1tempX.get(i).replaceAll("\t", "") + "\n";
			    	}
			    	
			    	for(int i = 0 ; i < a2tempX.size(); i++){
			    		strX = strX + "- " + a2tempX.get(i).replaceAll("\t", "") + "\n";
			    	}
					
					
					
			    	
			    	sqlh.sqlMain(fileNameAndLocation, "Source File Generated", className);				
					sqlh.sqlSource(fileNameAndLocation, className, errorLine, strX ,str.replaceAll("  " ,"").replaceAll("\t", "   "));
					current_state = "main";
					
					mapHolder.replace(projectAndClassName, str2);
		    	}
		    	
		    	
				
				diff_match_patch dmp = new diff_match_patch();
				LinkedList<Diff> df = dmp.diff_lines_only(strTemp,str);
				
				ArrayList<String> a1 = new ArrayList<String>();
		    	ArrayList<String> a2 = new ArrayList<String>();
		    	
				for(int i = 0; i < df.size(); i++){
					String df_string = df.get(i).toString();
					String temp_df_string = df_string.substring(9,df_string.length() - 1).trim();
					
		    		if(df_string.substring(0, 6).equals("INSERT") && df_string.length() > 8 ){
		    			a1.add(temp_df_string);	
		    		}
		    		else if(df_string.substring(0, 6).equals("DELETE") && df_string.length() > 8 ){
		    			a2.add(temp_df_string);
		    		}
		    	}
				
				diff_match_patch dmp2 = new diff_match_patch();
				LinkedList<Diff> df2 = dmp2.diff_lines_only(strTemp2,str2);
				
				ArrayList<String> a12 = new ArrayList<String>();
		    	ArrayList<String> a22 = new ArrayList<String>();
				
		    	for(int i = 0; i < df2.size(); i++){
					String df_string = df2.get(i).toString();
					String temp_df_string = df_string.substring(9,df_string.length() - 1).trim();
					
		    		if(df_string.substring(0, 6).equals("INSERT") && df_string.length() > 8 ){
		    			a12.add(temp_df_string);
		    		}
		    		else if(df_string.substring(0, 6).equals("DELETE") && df_string.length() > 8 ){
		    			a22.add(temp_df_string);
		    		}
		    	}
		    	
		    	ArrayList<String> a1temp = new ArrayList<String>();
		    	ArrayList<String> a12temp = new ArrayList<String>();
		    	
		    	for(int i = 0; i < a1.size(); i++){					
					String[] lines1 = a1.get(i).split("\r\n|\r|\n");
					for (String line : lines1) {	
						if(line.length() > 0){
							
							if(line.charAt(0) == ' '){
								a1temp.add(line.substring(1));
							} else{
								a1temp.add(line);
							}
						}
					}	
				}
		    	    	
		    	for(int i = 0; i < a12.size(); i++){
					String[] lines1 = a12.get(i).split("\r\n|\r|\n");
					for (String line : lines1) {	
						if(line.length() > 0){
							if(line.charAt(0) == ' '){
								a12temp.add(line.substring(1));
							} else{
								a12temp.add(line);
							}
						}
					}
				}	    	
		    				    
		    	ArrayList<String> a2temp = new ArrayList<String>();
		    	ArrayList<String> a22temp = new ArrayList<String>();

		    	for(int i = 0; i < a2.size(); i++){
					String[] lines1 = a2.get(i).split("\r\n|\r|\n");
					for (String line : lines1) {	
						if(line.length() > 0){	
							if(line.charAt(0) == ' '){
								a2temp.add(line.substring(1));
							} else{
								a2temp.add(line);
							}
						}
					}
				}
		    	    	
		    	for(int i = 0; i < a22.size(); i++){
					String[] lines1 = a22.get(i).split("\r\n|\r|\n");
					for (String line : lines1) {	
						if(line.length() > 0){
							if(line.charAt(0) == ' '){
								a22temp.add(line.substring(1));
							} else{
								a22temp.add(line);
							}
						}
					}	
				}	    	
		    			    	
		    	String line_diff = "";
		    	for(int i = 0; i < a12temp.size(); i++){
		    		line_diff = line_diff + "+" + a1temp.get(i).replaceAll("\t", "") + "\n";
		    	}
		    	   	
		    	for(int i = 0; i < a22temp.size(); i++){
		    		line_diff = line_diff + "-" + a2temp.get(i).replaceAll("\t", "") + "\n";
		    	}
		    	
		    	String line_description = "";
		    	
		    	// only take care when line added is 1, or line changed is 1
		    	if(a12temp.size() == 1){

		    		int line = Integer.parseInt(a1temp.get(0).substring(0, 4));		
		    		if(!a12temp.get(0).replaceAll("\t", "").equals("}")){    			
		    			for(int j = 0; j < array.size(); j++){    			
			    			int line2 = Integer.parseInt(array.get(j).substring(1, 5));
			    			if(line2 > line){    			
			    				String addons = ""; 				 				
			    				if(errorNumberArray.contains(line)){
			    					addons = "[" + errorMessageArray.get( errorNumberArray.indexOf(line)) + "]";
			    				}
			    				
			    				else if(stringArrayNumber.contains(line)){
			    					addons = stringArrayContent.get(stringArrayNumber.indexOf(line));
			    				} else if(a1temp.get(0).contains("//")){	
			    					addons = "[LINE_COMMENT]";
			    				} else if (a1temp.get(0).contains("/**")){
			    					addons = "[JAVA_DOC]";
			    				} else if(a1temp.get(0).contains("/*")){
			    					addons = "[BLOCK_COMMENT]";
			    				}
			    				
			    				// in case it's the first line
			    				if(j > 1){
			    					line_description = line_description + array.get(j-2).replaceAll("\t", "") + "\n";
			    				}
			    				line_description = line_description + "+"+ a1temp.get(0).replaceAll("\t", "") + " " + addons + "\n";  							
			    				if(a12temp.size() == a22temp.size()){
			    					line_description = line_description + "-"+ a2temp.get(0).replaceAll("\t", "") + "\n";
			    				}
			    				line_description = line_description + array.get(j).replaceAll("\t", "") + "\n"; 				

			    				break;
			    			}
			    			
			    		}
		    			
		    		}		
		    		
		    	} else {
		    		
		    		for(int i = 0; i < a12temp.size(); i++){
		    			int line = Integer.parseInt(a1temp.get(i).substring(0, 4));
			    		
			    		if(!a12temp.get(0).replaceAll("\t", "").equals("}") || !a12temp.get(0).replaceAll("\t", "").equals("{")){
			    			
			    			for(int j = 0; j < array.size(); j++){
				    			
				    			int line2 = Integer.parseInt(array.get(j).substring(1, 5));
				    			if(line2 > line){
				    			
				    				String addons = ""; 				
				    				
				    				if(errorNumberArray.contains(line)){
				    					addons = "[" + errorMessageArray.get( errorNumberArray.indexOf(line)) + "]";
				    				}
				    				
				    				else if(stringArrayNumber.contains(line)){
				    					addons = stringArrayContent.get(stringArrayNumber.indexOf(line));
				    				}
				    				
				    				line_description = line_description + "+"+ a1temp.get(i).replaceAll("\t", "") + " " + addons + "\n";
				    				break;
				    			}
				    		}		
			    		}   			
		    		}
		    	} 




		    	if(!current_state.equals("compilationUnit")){
		    		sqlh.sqlMain(fileNameAndLocation, "CompilationUnit Edited", className);		
		    	}
		    	
		    	
		    	if(line_diff.length() > 2 ){
		    		sqlh.sqlEdit(fileNameAndLocation, className, line_diff, line_description); 	
		    	}
		    	
				
		    	arrayClear();
		    	
		    	sourceTemp = source;
				strTemp = str;
				strTemp2 = str2;
				current_state = "compilationUnit";

			}
			
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
		
	/**
	 * This functions handles bunch of cases, for example, enable/disable 
	 * recording, checking if still working on same java file/project, 
	 * checking specific error, and etc.
	 * 
	 * @author Chern Yee Chua
	 */
	
	public void traverseCompilationUnit(ElementChangedEvent event){
		try{
			if(event.getDelta().getElement().getJavaProject() != null){
				/*
				 * When the plugin starts, the previousJavaProjectName will be empty.
				 * This if_statement is expected to run only once when the eclipse starts.
				 */
				
				if(previousJavaProjectName.equals("")){
					// get the name of project and location
					previousJavaProjectName = event.getDelta().getElement().getJavaProject().getProject().getName();
					fileNameAndLocation = event.getDelta().getElement().getJavaProject().getProject().getLocation().toString();
					
					// check whether the plugin should record the data
					File checkFile = new File(fileNameAndLocation + "/.settings/.sapphire");			
					enableRecording = checkFile.exists();
					
					if(enableRecording){
						sqlh.sqlMain(fileNameAndLocation, "Plugin Initialized", previousJavaProjectName);
						current_state = "main";
						
					}
					
					/*
					 * If the element changed event type is COMPILATION_UNIT,
					 * we check whether there is any error in the editor. If there
					 * is error, then we skip the printing AST part.
					 */

					if(event.getDelta().getElement().getElementType() == COMPILATION_UNIT){
						
						if(event.getDelta().getCompilationUnitAST() != null && 
								event.getDelta().getCompilationUnitAST().getTypeRoot() != null &&
								event.getDelta().getCompilationUnitAST().getTypeRoot().findPrimaryType() != null){
							
							className = event.getDelta().getCompilationUnitAST().getTypeRoot().findPrimaryType().getFullyQualifiedName();
							projectAndClassName = previousJavaProjectName + "." + className;
														
							if(mapSource.containsKey(projectAndClassName)){
								sourceTemp =  mapSource.get(projectAndClassName);
								
								checkSource(event);
								mapSource.replace(projectAndClassName, sourceTemp);
							}
							else{
								sourceTemp = "";
								checkSource(event);
								mapSource.put(projectAndClassName, sourceTemp);	
							}	 
						}
					}
				}
				
				else{
					/*
					 * When the user is still working on the same Java project, we check whether  
					 * the COMPILATION_UNIT is valid and also check for switching java file cases.
					 */
				
					if(event.getDelta().getElement().getJavaProject().getProject().getName().equals(previousJavaProjectName)){

						if(event.getDelta().getElement().getElementType() == COMPILATION_UNIT){
							
							// if the COMPILATION_UNIT is normal
							if(event.getDelta().getCompilationUnitAST() != null && 
									event.getDelta().getCompilationUnitAST().getTypeRoot() != null &&
									event.getDelta().getCompilationUnitAST().getTypeRoot().findPrimaryType() != null){
														
								// this if_statement checks whether the user is working on the same java file or not
								if(event.getDelta().getCompilationUnitAST().getTypeRoot().findPrimaryType().getFullyQualifiedName().
										equals(className)){
									className = event.getDelta().getCompilationUnitAST().getTypeRoot().findPrimaryType().getFullyQualifiedName();				
									projectAndClassName = previousJavaProjectName + "." + className;
									
									if(mapSource.containsKey(projectAndClassName)){
										sourceTemp = mapSource.get(projectAndClassName);
										checkSource(event);
										mapSource.replace(projectAndClassName, sourceTemp);
									}
									else{
										sourceTemp = "";
										checkSource(event);
										mapSource.put(projectAndClassName, sourceTemp);
									}			

								}
								
								// the user switches to another java class file
								else{	
						        
							        className = event.getDelta().getCompilationUnitAST().getTypeRoot().findPrimaryType().getFullyQualifiedName();
							        
							        if(enableRecording){
										sqlh.sqlMain(fileNameAndLocation, "CompilationUnit Changed", className);
										// prevent unnecessary error messages
										current_state = "main";
							        }
									
													
									projectAndClassName = previousJavaProjectName + "." + className;
									
									if(mapSource.containsKey(projectAndClassName)){
										sourceTemp = mapSource.get(projectAndClassName);
										checkSource(event);
										mapSource.replace(projectAndClassName, sourceTemp);									
									}
									else{
										sourceTemp = "";
										checkSource(event);
										mapSource.put(projectAndClassName, sourceTemp);
									}	

									
								}	
							}
							
							// this else_statement is triggered when the COMPILATION_UNIT is either empty or 
							// class type name is invalid 
							else{		
								checkSource(event);			
							}
						}
					}
					
					/*
					 * This is the case when user switches to another Java Project. 
					 * Close previous file writer and reinitialize new file writer (check if .sapphire exists)
					 * Procedure is same as the previous case.
					 */
					else{
				        if(enableRecording){
							sqlh.sqlMain(fileNameAndLocation, "Plugin Closed", previousJavaProjectName);
							current_state = "main";
				        }

						previousJavaProjectName = event.getDelta().getElement().getJavaProject().getProject().getName();
						fileNameAndLocation = event.getDelta().getElement().getJavaProject().getProject().getLocation().toString();
		
						File checkFile = new File(fileNameAndLocation + "/.settings/.sapphire");
						enableRecording = checkFile.exists();
						
						if(enableRecording){  
							sqlh.sqlMain(fileNameAndLocation, "Plugin Initialized", previousJavaProjectName);
							current_state = "main";
							sourceTemp = "";
						}
						
						if(event.getDelta().getElement().getElementType() == COMPILATION_UNIT){
							
							// the normal case
							if(event.getDelta().getCompilationUnitAST() != null && 
									event.getDelta().getCompilationUnitAST().getTypeRoot() != null &&
									event.getDelta().getCompilationUnitAST().getTypeRoot().findPrimaryType() != null){
								className = event.getDelta().getCompilationUnitAST().getTypeRoot().findPrimaryType().getFullyQualifiedName();				
								projectAndClassName = previousJavaProjectName + "." + className;
								

								if(mapSource.containsKey(projectAndClassName)){
									sourceTemp = mapSource.get(projectAndClassName);
									checkSource(event);
									mapSource.replace(projectAndClassName, sourceTemp);	
								}
								else{
									sourceTemp = "";
									checkSource(event);
									mapSource.put(projectAndClassName, sourceTemp);
								}	

							}
							
							// the crazy case
							else{															
								checkSource(event);										
														
							}
						}
					}
				}
			}
					
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static IElementChangedListener getListener() {
		return _listener;
	}
	
	/**
	 * Traverse all the affected JavaElementDelta children and print out
	 * what has changed, excluding CompilationUnit. 
	 * 
	 * @author Chern Yee Chua
	 */
	
	public void traverseAndPrintJavaModel(IJavaElementDelta delta){
        try{
        	switch (delta.getKind()) {
            case IJavaElementDelta.ADDED:  	
            	
            	if(delta.getElement().getElementType() == JAVA_PROJECT || 
            			delta.getElement().getElementType() == JAVA_MODEL ||
            			delta.getElement().getElementType() == PACKAGE_FRAGMENT ||
            			delta.getElement().getElementType() == PACKAGE_FRAGMENT_ROOT ||
            			delta.getElement().getElementType() == COMPILATION_UNIT ){
            		
    		        String projectName = delta.getElement().getJavaProject().getProject().getName();
    		        
    		        if(previousJavaProjectName.equals("")){
    		        	previousJavaProjectName = delta.getElement().getJavaProject().getProject().getName();
    					fileNameAndLocation = delta.getElement().getJavaProject().getProject().getLocation().toString();
    					
    					File checkFile = new File(fileNameAndLocation + "/.settings/.sapphire");
    					enableRecording = checkFile.exists();
    					if(enableRecording){	  
    						sqlh.sqlMain(fileNameAndLocation, "Plugin Initialized", previousJavaProjectName);
    						current_state = "main";
    					}
    		        }
    		        
    		        else{
    		        	if(projectName.equals(previousJavaProjectName)){	
    		        		// do nothing, complete the switch at the end
    		        	}
    		        	
    		        	else{  		
    						if(enableRecording){
								sqlh.sqlMain(fileNameAndLocation, "Plugin Closed", previousJavaProjectName);
								current_state = "main";
							}
							
							previousJavaProjectName = delta.getElement().getJavaProject().getProject().getName();
							fileNameAndLocation = delta.getElement().getJavaProject().getProject().getLocation().toString();


							File checkFile = new File(fileNameAndLocation + "/.settings/.sapphire");
							enableRecording = checkFile.exists();
							if(enableRecording){
								sqlh.sqlMain(fileNameAndLocation, "Plugin Initialized", previousJavaProjectName);
								current_state = "main";
							} 
    		        	}
    		        }
    		        
    		        
    		        if(enableRecording){
    		        	switch(delta.getElement().getElementType()){
                		case 1: 	 
                			sqlh.sqlMain(fileNameAndLocation, "JavaModel Added", delta.getElement().getElementName());
                			break;       		
                		case 2:         			
                			sqlh.sqlMain(fileNameAndLocation, "JavaProject Added", delta.getElement().getElementName());
                			break;       			
                		case 3:         			
                			sqlh.sqlMain(fileNameAndLocation, "PackageFragmentRoot Added", delta.getElement().getElementName());
                			break;       			
                		case 4: 
                			sqlh.sqlMain(fileNameAndLocation, "PackageFragment Added", delta.getElement().getElementName());
                			break;        			
                		case 5: 
                			sqlh.sqlMain(fileNameAndLocation, "CompilationUnit Added", delta.getElement().getElementName());
                			break;
                		default: 
            
                		}	
    		        	
    		        	current_state = "main";
    		        	
    		        }

            		
            		visitJavaModel = true;
            	}
                
                break;
                                
            case IJavaElementDelta.REMOVED:
            	if(delta.getElement().getElementType() == JAVA_PROJECT || 
    			delta.getElement().getElementType() == JAVA_MODEL ||
    			delta.getElement().getElementType() == PACKAGE_FRAGMENT ||
    			delta.getElement().getElementType() == PACKAGE_FRAGMENT_ROOT ||
    			delta.getElement().getElementType() == COMPILATION_UNIT ){
            		     
    		        String projectName = delta.getElement().getJavaProject().getProject().getName();	    
    		        if(projectName.equals(previousJavaProjectName)){	
    	        		// do nothing now... 
    	        	}
    	        	
    	        	else{
    					if(enableRecording){
							sqlh.sqlMain(fileNameAndLocation, "Plugin Closed", "previousJavaProjectName");
							current_state = "main";
						}
						
						if(delta.getElement().getJavaProject() != null &&
							delta.getElement().getJavaProject().getProject() != null &&
							delta.getElement().getJavaProject().getProject().getLocation() != null){
							previousJavaProjectName = delta.getElement().getJavaProject().getProject().getName();
							fileNameAndLocation = delta.getElement().getJavaProject().getProject().getLocation().toString();
							
							File checkFile = new File(fileNameAndLocation + "/.settings/.sapphire");
							enableRecording = checkFile.exists();
							if(enableRecording){  
								sqlh.sqlMain(fileNameAndLocation, "Plugin Initialized", "previousJavaProjectName");
								current_state = "main";
							}		
						} 
    	        	}	 
    		        
    		        if(enableRecording){
    		        	switch(delta.getElement().getElementType()){
                		case 1:  
                			sqlh.sqlMain(fileNameAndLocation, "JavaModel Removed", delta.getElement().getElementName());
                			break;       			
                		case 2: 
                			sqlh.sqlMain(fileNameAndLocation, "JavaProject Removed", delta.getElement().getElementName());
                			break;	       			
                		case 3: 
                			sqlh.sqlMain(fileNameAndLocation, "PackageFragmentRoot Removed", delta.getElement().getElementName());
                			break;        			
                		case 4: 
                			sqlh.sqlMain(fileNameAndLocation, "PackageFragment Removed", delta.getElement().getElementName());
                			break;       			
                		case 5: 
                			sqlh.sqlMain(fileNameAndLocation, "CompilationUnit Removed", delta.getElement().getElementName());
                			break;

                		default: 
                		}	
    		        	current_state = "main";
    		        }

            		visitJavaModel = true;
            	}

                break;
            }
            
            IJavaElementDelta[] children = delta.getAffectedChildren();
            for (int i = 0; i < children.length; i++) {
            	traverseAndPrintJavaModel(children[i]);
            }
        	
        }catch(Exception e){
        	e.printStackTrace();
        }
	}
	
	/**
	 * This method traverses the root ASTNode in the parameter and calls
	 * printStatement to store the information of the child node.
	 * 
	 * @author Chern Yee Chua
	 */
	
	public static void print(ASTNode node) {
	
		 List properties= node.structuralPropertiesForType();
		 for (Iterator iterator= properties.iterator(); iterator.hasNext();) {
			 Object descriptor= iterator.next();
			 if (descriptor instanceof SimplePropertyDescriptor) {
				 SimplePropertyDescriptor simple= (SimplePropertyDescriptor)descriptor;
				 Object value= node.getStructuralProperty(simple);		
				 int lineNumber = astRoot.getLineNumber(node.getStartPosition());
				 
				 lastLineNumber = lineNumber;
				 printStatement(lineNumber, node);

			 } else if (descriptor instanceof ChildPropertyDescriptor) {
				 ChildPropertyDescriptor child= (ChildPropertyDescriptor)descriptor;
				 ASTNode childNode= (ASTNode)node.getStructuralProperty(child);
				 if (childNode != null){
					 
					 print(childNode);
				 }
			 } else {
				 ChildListPropertyDescriptor list= (ChildListPropertyDescriptor)descriptor;
				 print((List)node.getStructuralProperty(list));
			 }
		 }
	}
	
	public static void print(List nodes) {
		for (Iterator iterator= nodes.iterator(); iterator.hasNext();) {
			print( (ASTNode) iterator.next() );
		}
	}


	public static void printStatement(int lineNumber, ASTNode node){
		
		String str;
		if(node.getNodeType() == IMPORT_DECLARATION){
			str = mapValue.get(node.getNodeType());
		}
		else{
			str = mapValue.get(node.getParent().getNodeType());
		}
		

		if(lineNumberCheck == 0){
			long2 = str;	
			lineNumberCheck = lineNumber;
		 }
		else{
			if(lineNumber != lineNumberCheck){		
				 
				stringArrayNumber.add(lineNumberCheck);
				if(lineNumberCheck < 10){
					stringArray.add( "000" + lineNumberCheck + " " + long2); 	
				} else if(lineNumberCheck <100){
					stringArray.add( "00" + lineNumberCheck + " " + long2);
				} else if(lineNumberCheck < 1000){
					stringArray.add( "0" + lineNumberCheck + " " + long2);
				} else{
					stringArray.add(lineNumberCheck + " " + long2);
				}
				stringArrayContent.add(long2);
				

				long2 = str;	
				lineNumberCheck = lineNumber;			 
			}
			
		}		
	}

	
	
	public void arrayClear(){
		stringArrayNumber.clear();
		stringArray.clear();
		stringArrayContent.clear();
		errorMessageArray.clear();
		errorNumberArray.clear();
		errorArray.clear();
	}


	public static void endPlugin() {
		if(enableRecording){
        	try {
				sqlh.sqlMain(fileNameAndLocation, "Plugin Closed", previousJavaProjectName);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
        }
		
	}

}