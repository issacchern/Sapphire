/**

 * This file is part of Sapphire.
 * 
 * Sapphire is a free plugin software, licensed under the terms of the 
 * Eclipse Public License, version 1.0.  The license is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Sapphire © 2015 University at Buffalo. All rights reserved.  
 */

package edu.buffalo.cse.Sapphire;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

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
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.BlockComment;
import org.eclipse.jdt.core.dom.ChildListPropertyDescriptor;
import org.eclipse.jdt.core.dom.ChildPropertyDescriptor;
import org.eclipse.jdt.core.dom.Comment;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Javadoc;
import org.eclipse.jdt.core.dom.LineComment;
import org.eclipse.jdt.core.dom.SimplePropertyDescriptor;

import edu.buffalo.cse.Sapphire.diff_match_patch.Diff;


/**
 * This class handles all the changes to the JavaModel, recording all of
 * the changes that the user has made into a text file. 
 * 
 * @author Chern Yee Chua
 */
public class JavaModelListener implements IElementChangedListener{
	
	private static String fileNameAndLocation =  "main_recorder";
	private static String className = "";
	private static String long2 = "";
	private static String previousJavaProjectName = "";
	private static String projectAndClassName = "";
	private static boolean isTempLarger = false;
	private static boolean visitJavaModel = false;
	private static boolean enableRecording = false;
	private static int lastLineNumber = 0;
	private static int lineNumberCheck = 0;
	private static HashMap<String, String> mapComment = new HashMap<String, String>(); // for comment blocks
	private static HashMap<String, ASTNode> mapAST = new HashMap<String, ASTNode>(); // for ASTNode
	private static HashMap<Integer, String> mapValue; // for node type
	private FileWriter fwriter;
	private static IElementChangedListener _listener = new JavaModelListener();
	private ICompilationUnit cuTemp;	
	private static String sourceTemp;
	private static CompilationUnit astRootTemp;
	private ICompilationUnit cu;
	private static String source;
	private static CompilationUnit astRoot;
	private static List<String> stringArray = new ArrayList<String>();
	private static List<String> stringArrayContent = new ArrayList<String>();
	private static List<Integer> stringArrayNumber = new ArrayList<Integer>();
	private static List<String> stringArrayTemp = new ArrayList<String>();
	private static List<String> stringArrayTempContent = new ArrayList<String>();
	private static List<Integer> stringArrayTempNumber = new ArrayList<Integer>();
	private static List<String> commentList = new ArrayList<String>();
	private static List<String> commentListContent = new ArrayList<String>();
	private static List<String> commentListTemp = new ArrayList<String>();
	private static List<String> commentListTempContent = new ArrayList<String>();

	
	/**
	 * The constructor records how many times the plugin has triggered into
	 * a textfile located at the same directory of eclipse folder.
	 * It also initializes the HashMap corresponding to the AST node type.
	 * 
	 * @author Chern Yee Chua
	 */
	
	public JavaModelListener(){

		/*
		try
		{
			File newFile = new File(fileNameAndLocation);	
			fwriter = new FileWriter(newFile, true);
			Calendar cal = Calendar.getInstance();
	        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd 'at' HH:mm:ss z");
			fwriter.write(sdf.format(cal.getTime()) + " [LISTENER INITIALIZATION] \n");
			fwriter.flush();
			fwriter.close();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}	
		*/
		
		astRootTemp = null;
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
	
	
	public boolean error_checking(ElementChangedEvent event){
		
		if (event.getDelta().getCompilationUnitAST().getProblems().length > 0){
			boolean true_or_false = true;
			
			for (IProblem problem : (event.getDelta().getCompilationUnitAST().getProblems())){
				if (problem.isError()){
					true_or_false = false;
					if(enableRecording){
						String str = problem.getSourceLineNumber() + " * " + problem.getMessage();
						try {
							Calendar cal = Calendar.getInstance();
					        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd 'at' HH:mm:ss z");
				        	fwriter.write(sdf.format(cal.getTime()) + " [ERROR DETECTED(" + className + ")]: " +
				        			str + "\n");
							fwriter.flush();		
							
						} catch (IOException e) {
							e.printStackTrace();
						}	
					}
				}
			}

			return true_or_false;				
		}
		
		else{
			return true;	
		}
	}
	
	
	/**
	 * Method parse() takes string source of CompilationUnit and store the visiting
	 * node into ArrayList. Since Comment node is not part of the ASTNode structure,
	 * visiting those Comment nodes (LineComment, BlockComment and Javadoc) are 
	 * neccessary.
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
	
	
	@SuppressWarnings("unchecked")
	public static void parse(final String strA) {
		ASTParser parserA = ASTParser.newParser(AST.JLS8);
		parserA.setSource(strA.toCharArray());
		parserA.setKind(ASTParser.K_COMPILATION_UNIT);
		final CompilationUnit cuA = (CompilationUnit) parserA.createAST(null);
 
		for (Comment comment : (List<Comment>) cuA.getCommentList()) {
			comment.accept(new ASTVisitor(){
				
				public boolean visit(LineComment node) {
					int start = node.getStartPosition();
					int end = start + node.getLength();
					String comment = strA.substring(start, end);	
					int lineNumber;
					 
					if(isTempLarger){
						lineNumber = astRootTemp.getLineNumber(node.getStartPosition());
					}
					else{
						lineNumber = astRoot.getLineNumber(node.getStartPosition());
					}
					
					if(isTempLarger){
						if(!commentListTemp.contains(lineNumber + " # " + "[LINE_COMMENT] " + comment)){
							commentListTemp.add(lineNumber + " # " + "[LINE_COMMENT] " + comment);
							commentListTempContent.add( "[LINE_COMMENT] " + comment); 	 
						}
					}
					else{
						if(!commentList.contains(lineNumber + " # " + "[LINE_COMMENT] " + comment)){
							commentList.add(lineNumber + " # " + "[LINE_COMMENT] " + comment);
							commentListContent.add("[LINE_COMMENT] " + comment);	 
						}
					}

					return true;
				}
			 
				public boolean visit(BlockComment node) {
					int start = node.getStartPosition();
					int end = start + node.getLength();
					String comment = strA.substring(start, end);
					int lineNumber;
					 
					if(isTempLarger){
						 lineNumber = astRootTemp.getLineNumber(node.getStartPosition());
					}
					else{
						lineNumber = astRoot.getLineNumber(node.getStartPosition());
					}
					 
					if(isTempLarger){
						if(!commentListTemp.contains(lineNumber + " # " + "[BLOCK_COMMENT] \n" + comment)){
							commentListTemp.add(lineNumber + " # " + "[BLOCK_COMMENT] \n" + comment);
							commentListTempContent.add("[BLOCK_COMMENT] \n" + comment); 		 
						}
					}
					else{
						if(!commentList.contains(lineNumber + " # " + "[BLOCK_COMMENT] \n" + comment)){
							commentList.add(lineNumber + " # " + "[BLOCK_COMMENT] \n" + comment);
							commentListContent.add("[BLOCK_COMMENT] \n" + comment); 
						}
					}
					return true;
				}
				
				public boolean visit(Javadoc node){
					
					int start = node.getStartPosition();
					int end = start + node.getLength();
					String comment = strA.substring(start, end);
					int lineNumber;

					if(isTempLarger){
						 lineNumber = astRootTemp.getLineNumber(node.getStartPosition());
					}
					else{
						lineNumber = astRoot.getLineNumber(node.getStartPosition());
					}
					 
					if(isTempLarger){
						if(!commentListTemp.contains(lineNumber + " # " + "[JAVADOC] \n" + comment)){
							commentListTemp.add(lineNumber + " # " + "[JAVADOC] \n" + comment);
							commentListTempContent.add("[JAVADOC] \n" + comment); 
						}
					}
					else{
						if(!commentList.contains(lineNumber + " # " + "[JAVADOC] \n" + comment)){
							commentList.add(lineNumber + " # " + "[JAVADOC] \n" + comment);
							commentListContent.add("[JAVADOC] \n" + comment); 			 
						}	
					}
					return true;
				}			
			});
		}
	}
	
	
	/**
	 * This is the major part of listening to the CompilationUnit changes.
	 * First visitation of the CompilationUnit will print out the structure
	 * of the file. Then, this method will return what is being added or removed
	 * to the file. 
	 * If the CompilationUnit is switched to another CompilationUnit or another
	 * Java Project, a whole new structure of that CompilationUnit will be printed out. 
	 * 
	 * @author Chern Yee Chua
	 */
	
	public void printAST(ElementChangedEvent event){
		
		try{
			// When the user switch java file or project, cuTemp is expected to be null.
			if(astRootTemp == null && enableRecording){			
				cuTemp = (ICompilationUnit) event.getDelta().getElement();
				sourceTemp = cuTemp.getSource();
				mapComment.put(projectAndClassName, sourceTemp);
				ASTParser parserTemp = ASTParser.newParser(AST.JLS8);
				parserTemp.setSource(cuTemp); 
				parserTemp.setKind(ASTParser.K_COMPILATION_UNIT);
				parserTemp.setResolveBindings(true);
				astRootTemp = (CompilationUnit) parserTemp.createAST(null);
				Calendar cal = Calendar.getInstance();
		        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd 'at' HH:mm:ss z");
		        
		        // parse the astRootTemp
		        isTempLarger = true;
				long2 = "";
				lineNumberCheck = 0;
				print(astRootTemp);
				
				if(lineNumberCheck == 0 || long2.length() == 0){
				}
				else{
					stringArrayTemp.add(lineNumberCheck + " $ " + long2); 
				}

				fwriter.write(sdf.format(cal.getTime()) + " [SOURCE FILE INITIALIZED] \n");
				fwriter.flush();
				

				// write to another recorded file specifically for the source file only
				File newFileInitial = new File(fileNameAndLocation + "/." + previousJavaProjectName + "_source" +".RECORDING");
				FileWriter fwriterInitial = new FileWriter(newFileInitial, true);
				fwriterInitial.write(sdf.format(cal.getTime()) + " [" + className + "] \n--------------------"
						+ "-------------------------------\n" 
						+ astRootTemp.toString() + "\n"); 
				fwriterInitial.flush();
				fwriterInitial.close();
				
				
				// handle comments and docs
				parse(mapComment.get(projectAndClassName));	
				
				for(int i = 0; i < commentListTemp.size(); i++){
					fwriter.write(sdf.format(cal.getTime()) +  " [LINE INITIALIZED (" + className + ")]: " + 
							commentListTemp.get(i) + "\n");
					fwriter.flush();
				}	
				
				arrayClear();
			}
			
			else{
				if(enableRecording){
					
					Calendar cal = Calendar.getInstance();
			        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd 'at' HH:mm:ss z");
					
					cu = (ICompilationUnit) event.getDelta().getElement();				
					source = cu.getSource();
					ASTParser parser = ASTParser.newParser(AST.JLS8);
					parser.setSource(cu);
					parser.setKind(ASTParser.K_COMPILATION_UNIT);
					parser.setResolveBindings(true);
					astRoot = (CompilationUnit) parser.createAST(null);
					
					
					// this section uses diff_match_patch algorithm to extract the differences of two ASTNode
					
					diff_match_patch dmp = new diff_match_patch();
					LinkedList<Diff> df = dmp.diff_lines_only(astRootTemp.toString(),astRoot.toString());
					
					ArrayList<String> a1 = new ArrayList<String>();
			    	ArrayList<String> a2 = new ArrayList<String>();
			    	
					for(int i = 0; i < df.size(); i++){
						String df_string = df.get(i).toString();
						String temp_df_string = df_string.substring(9,df_string.length() - 1).replaceAll("  ", "").
		    					replaceAll("\t","");
			    		if(df_string.substring(0, 6).equals("INSERT") && df_string.length() > 8 ){
			    			a1.add(temp_df_string);
			    			
			    		}
			    		else if(df_string.substring(0, 6).equals("DELETE") && df_string.length() > 8 ){
			    			a2.add(temp_df_string);
			    		}
			    	}
					
			    	for(int i = 0; i < a1.size();i++){	
			    		
			    		String[] lines = a1.get(i).split("\r\n|\r|\n");
						for (String line : lines) {					    
						    fwriter.write(sdf.format(cal.getTime()) +  " [+] " + 
									line + "\n");
							fwriter.flush();   
						}
			    			
			    	}
			    		    			    	
			    	for(int i = 0; i < a2.size();i++){		
			    		String[] lines = a2.get(i).split("\r\n|\r|\n");
						for (String line : lines) {
				    		fwriter.write(sdf.format(cal.getTime()) +  " [-] " + 
									line + "\n");
							fwriter.flush();		
						}    		    		
			    	}
			    	
					// end of diff_match_patch algorithm 
					
					// parse the astRootTemp
					isTempLarger = true;
					long2 = "";
					lineNumberCheck = 0;
					print(astRootTemp);
					
					if(lineNumberCheck == 0 || long2.length() == 0){
					}
					else{
						// add the last piece of element
						stringArrayTempNumber.add(lineNumberCheck);
						stringArrayTemp.add(lineNumberCheck + " $ " + long2); 
						stringArrayTempContent.add(long2);
					}
					
					// reset the data and parse astRoot
					long2 = "";	
					lineNumberCheck = 0;			
					isTempLarger = false; 
					print(astRoot);
					
					if(lineNumberCheck == 0 || long2.length() == 0){
					}
					else{
						// add the last piece of element
						stringArrayNumber.add(lineNumberCheck);
						stringArray.add(lineNumberCheck + " $ " + long2);
						stringArrayContent.add(long2);
					}
						
					// this algorithm marks the duplicate element with "duplicate" with the count at the end
					// NOTE: this is for current unit
					ArrayList<String> arr = new ArrayList<String>();	
					int cnt = 0;
					for(int i = 0 ; i < stringArray.size(); i++ ){
						int index = stringArray.get(i).indexOf('$');
						String stringSub = stringArray.get(i).substring(index);	
						
						if(stringSub.substring(2, 5).equals("OPE") || stringSub.substring(2,5).equals("CLO")){
							// don't mark those cases with "duplicates"
						}
						
						else{
							if(arr.contains(stringSub)){
								cnt++;
								stringArray.set(i, stringArray.get(i) + "(dup: " + cnt + ")");
								stringArrayContent.set(i, stringSub.substring(2) + "(dup: " + cnt + ")");
								arr.add(stringSub + "(dup: " + cnt + ")");
							}
							else{
								arr.add(stringSub);
							}	
						}
					}
					
					// this algorithm marks the duplicate element with "duplicate" with the count at the end
					// NOTE: this is for previous unit
					ArrayList<String> arrTemp = new ArrayList<String>();			
					int cntTemp = 0;
					for(int i = 0 ; i < stringArrayTemp.size(); i++ ){
						int index = stringArrayTemp.get(i).indexOf('$');
						String stringSub = stringArrayTemp.get(i).substring(index);	
						
						if(stringSub.substring(2, 5).equals("OPE") || stringSub.substring(2,5).equals("CLO")){
							// don't mark those cases with "duplicates"
						}
						
						else{
							if(arrTemp.contains(stringSub)){
								cntTemp++;
								stringArrayTemp.set(i, stringArrayTemp.get(i) + "(dup: " + cntTemp + ")");
								stringArrayTempContent.set(i, stringSub.substring(2) + "(dup: " + cntTemp + ")");
								arrTemp.add(stringSub + "(dup: " + cntTemp + ")");
							}
							else{
								arrTemp.add(stringSub);
							}	
						}
					}
					
					// clone another ArrayList for other usage
					List<String> copyStringArray = new ArrayList<String>();
					copyStringArray.addAll(stringArray);
					
					// clone another ArrayList for other usage
					List<String> copyStringArrayTemp = new ArrayList<String>();
					copyStringArrayTemp.addAll(stringArrayTemp);
					
					// remove duplicates in copyStringArray
					Set<String> set1 = new HashSet<String>();
					set1.addAll(stringArrayTemp);
					Set<String> set2 = new HashSet<String>();
					set2.addAll(stringArray);
					set2.removeAll(set1);
					
					for(String s: stringArrayTemp){
						copyStringArray.remove(s);
					}
													
					// create another ArrayList with the duplicates removed
					List<String> afterModificationAdded = new ArrayList<String>();
					afterModificationAdded.addAll(copyStringArray);
					
					// remove duplicates in copyStringArrayTemp
					Set<String> set12 = new HashSet<String>();
					set12.addAll(stringArray);
					Set<String> set22 = new HashSet<String>();
					set22.addAll(stringArrayTemp);
					set22.removeAll(set12);
					
					for(String s: stringArray){
						copyStringArrayTemp.remove(s);
					}

					// create another ArrayList with the duplicates removed
					List<String> afterModificationRemoved = new ArrayList<String>();
					afterModificationRemoved.addAll(copyStringArrayTemp);
					
					// get the ArrayList with only the new elements added 
					for(int i = 0 ; i < copyStringArray.size(); i++){
						int index = copyStringArray.get(i).indexOf('$');
						String stringSub = copyStringArray.get(i).substring(index); 
						for(int j = 0; j < stringArrayTempContent.size(); j++){
							if(stringArrayTempContent.get(j).equals(stringSub.substring(2))){
								afterModificationAdded.remove(copyStringArray.get(i)); 
								stringArrayTempContent.remove(stringArrayTempContent.get(j));
								break;
							}
						}
					}
					
					// get the ArrayList with only the new elements added 
					for(int i = 0 ; i < copyStringArrayTemp.size(); i++){
						int index = copyStringArrayTemp.get(i).indexOf('$');
						String stringSub = copyStringArrayTemp.get(i).substring(index); // the content 
						for(int j = 0; j < stringArrayContent.size(); j++){		
							if(stringArrayContent.get(j).equals(stringSub.substring(2))){
								afterModificationRemoved.remove(copyStringArrayTemp.get(i));
								stringArrayContent.remove(stringArrayContent.get(j));
								break;
							}											
						}								
					}
					
					// print out the time and the class name in certain amount of time
					
			        
			        // display what new element is added 
		        	for(int i = 0 ; i < afterModificationAdded.size(); i++){

		        		if(afterModificationAdded.get(i).contains("dup")){
		        			// remove dup words 
		        			int index_of = afterModificationAdded.get(i).indexOf("dup:");
		        			
		        			fwriter.write(sdf.format(cal.getTime()) +  " [LINE ADDED (" + className + ")]: " + 
							afterModificationAdded.get(i).substring(0, index_of - 1) + "\n");
							fwriter.flush();	
		        		}
		        		else{

		        			fwriter.write(sdf.format(cal.getTime()) +  " [LINE ADDED (" + className + ")]: " + 
							afterModificationAdded.get(i) + "\n");
							fwriter.flush();
		        			
		        		}
						
					}	
		        	
		        	// display what old element is removed
					for(int i = 0 ; i < afterModificationRemoved.size(); i++){
						
						// remove dup words
						if(afterModificationRemoved.get(i).contains("dup:")){
							int index_of = afterModificationRemoved.get(i).indexOf("dup:");
							
							fwriter.write(sdf.format(cal.getTime()) +  " [LINE REMOVED (" + className + ")]: " + 
									afterModificationRemoved.get(i).substring(0, index_of - 1) + "\n");
							fwriter.flush();
		        		}
		        		
		        		else{
		        			fwriter.write(sdf.format(cal.getTime()) +  " [LINE REMOVED (" + className + ")]: " + 
									afterModificationRemoved.get(i) + "\n");
							fwriter.flush();	
		        			
		        		}
						
					}

		
					// retrieve the comment elements
					isTempLarger = true;
					parse(mapComment.get(projectAndClassName));
					isTempLarger = false;
					parse(source);
					
					// clone ArrayList for other usage	
					ArrayList<String> afterCommentAdded = new ArrayList<String>();
					afterCommentAdded.addAll(commentList);
					ArrayList<String> afterCommentRemoved = new ArrayList<String>();
					afterCommentRemoved.addAll(commentListTemp);
					
					// get the ArrayList with only the new elements added 
					for(int i = 0 ; i < commentList.size(); i++){
						int index = commentList.get(i).indexOf('#');
						String stringSub = commentList.get(i).substring(index); // the content 
						for(int j = 0; j < commentListTempContent.size(); j++){
							if(commentListTempContent.get(j).equals(stringSub.substring(2))){
								afterCommentAdded.remove(commentList.get(i)); // you need to check again before you remove the element. 
								commentListTempContent.remove(commentListTempContent.get(j));
								break;
							}
						}
					}
					
					// get the ArrayList with only the new elements added 
					for(int i = 0 ; i < commentListTemp.size(); i++){
						int index = commentListTemp.get(i).indexOf('#');
						String stringSub = commentListTemp.get(i).substring(index); // the content 
						for(int j = 0; j < commentListContent.size(); j++){
							if(commentListContent.get(j).equals(stringSub.substring(2))){
								afterCommentRemoved.remove(commentListTemp.get(i));
								commentListContent.remove(commentListContent.get(j));
								break;
							}
						}
					}

					// display what new comment is added
					for(int i = 0 ; i < afterCommentAdded.size(); i++){
						fwriter.write(sdf.format(cal.getTime()) +  " [LINE ADDED (" + className + ")]: " + 
								afterCommentAdded.get(i) + "\n");
						fwriter.flush();
					}
					
					// display what old comment is removed
					for(int i = 0 ; i < afterCommentRemoved.size(); i++){
						fwriter.write(sdf.format(cal.getTime()) +  " [LINE REMOVED (" + className + ")]: " + 
								afterCommentRemoved.get(i) + "\n");   
						fwriter.flush();
					}
					
					// remove all elements in those lists 
					arrayClear();
				
					// set the current CompilationUnit as previous one for next elementChanged event 
					cuTemp = cu;			
					sourceTemp = cuTemp.getSource();
					mapComment.replace(projectAndClassName, sourceTemp);
					ASTParser parserTemp = ASTParser.newParser(AST.JLS8);
					parserTemp.setSource(cuTemp); 
					parserTemp.setKind(ASTParser.K_COMPILATION_UNIT);
					parserTemp.setResolveBindings(true);
					astRootTemp = (CompilationUnit) parserTemp.createAST(null);			
				}		
			} 
		}
		
		catch(Exception e){
			e.printStackTrace();
		}		
	}
	
	/**
	 * This method is called when the user switches to another Java
	 * Project, a new recording file corresponding to the Java Project 
	 * will be created and start recording the changes.
	 * 
	 * @author Chern Yee Chua
	 */
	
	public void reinitializeFile(String previousName){
		try
		{
			File newFile = new File(previousName);
			fwriter = new FileWriter(newFile, true);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}	
	}
	
	/**
	 * This method is triggered automatically when there is any 
	 * modification on JavaModel occurs. It checks whether the user
	 * is working on the same CompilationUnit or Java Project. Call
	 * reintializeFile method if the user switches to another Java
	 * Project. 
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
					Calendar cal = Calendar.getInstance();
			        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd 'at' HH:mm:ss z");
					previousJavaProjectName = event.getDelta().getElement().getJavaProject().getProject().getName();
					fileNameAndLocation = event.getDelta().getElement().getJavaProject().getProject().getLocation().toString();
					
					// check whether the plugin should record the data
					File checkFile = new File(fileNameAndLocation + "/.settings/.sapphire");			
					enableRecording = checkFile.exists();
					
					if(enableRecording){
						reinitializeFile(fileNameAndLocation + "/." + previousJavaProjectName + ".RECORDING");
						fwriter.write(sdf.format(cal.getTime()) + " [PLUGIN INITIALIZED]\n");
						fwriter.flush();	  
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
							
							boolean no_error = error_checking(event);

							if(no_error){
								
								/*
								 * Check if the project together with class name already exists in the system,
								 * if there exists, then the astRootTemp will be the previous one that we stored. 
								 * Else, it will initialize a new instance and put in the map.
								 * We expect the else statement will only be triggered the maximum number of 
								 * java classes in the project.
								 */
							
								if(mapAST.containsKey(projectAndClassName)){
									astRootTemp = (CompilationUnit) mapAST.get(projectAndClassName);
									
									printAST(event);
									mapAST.replace(projectAndClassName, astRootTemp);
								}
								else{
									astRootTemp = null;
									printAST(event);
									mapAST.put(projectAndClassName, astRootTemp);	
								}	
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
									
									boolean no_error = error_checking(event);

									if(no_error){
										if(mapAST.containsKey(projectAndClassName)){
											astRootTemp = (CompilationUnit) mapAST.get(projectAndClassName);
											printAST(event);
											mapAST.replace(projectAndClassName, astRootTemp);
										}
										else{
											astRootTemp = null;
											printAST(event);
											mapAST.put(projectAndClassName, astRootTemp);
										}		
									}

								}
								
								// the user switches to another java class file
								else{	
									Calendar cal = Calendar.getInstance();
							        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd 'at' HH:mm:ss z");
									fwriter.write(sdf.format(cal.getTime()) + " [COMPILATION_UNIT CHANGED TO " + 
											event.getDelta().getCompilationUnitAST().getTypeRoot().findPrimaryType().getFullyQualifiedName()
											+ "]\n");
									fwriter.flush();
									className = event.getDelta().getCompilationUnitAST().getTypeRoot().findPrimaryType().getFullyQualifiedName();				
									projectAndClassName = previousJavaProjectName + "." + className;
									
									boolean no_error = error_checking(event);

									if(no_error){
										if(mapAST.containsKey(projectAndClassName)){
											astRootTemp = (CompilationUnit) mapAST.get(projectAndClassName);
											printAST(event);
											mapAST.replace(projectAndClassName, astRootTemp);
											
										}
										else{
											astRootTemp = null;
											printAST(event);
											mapAST.put(projectAndClassName, astRootTemp);
										}	
									}
									
									
								}	
							}
							
							// this else_statement is triggered when the COMPILATION_UNIT is either empty or 
							// class type name is invalid 
							else{
								Calendar cal = Calendar.getInstance();
						        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd 'at' HH:mm:ss z");			
								ICompilationUnit cuCheck = (ICompilationUnit) event.getDelta().getElement();
								ASTParser parserCheck = ASTParser.newParser(AST.JLS8);
								parserCheck.setSource(cuCheck); 
								parserCheck.setKind(ASTParser.K_COMPILATION_UNIT);
								parserCheck.setResolveBindings(true);
								ASTNode astCheck = (CompilationUnit) parserCheck.createAST(null);
								
								if(astCheck.toString().length() == 0){								
									fwriter.write(sdf.format(cal.getTime()) + " [COMPILATION_UNIT ERROR]: EMPTY SOURCE FILE DETECTED\n");
									fwriter.flush();
									
									if(astRootTemp != null){
										isTempLarger = true;
										long2 = "";
										lineNumberCheck = 0;
										print(astRootTemp);
										stringArrayTemp.add(lineNumberCheck + " $ " + long2); 
																					
										for(int i = 0 ; i < stringArrayTemp.size(); i ++){
											fwriter.write(sdf.format(cal.getTime()) + " [LINE REMOVED (" + className + ")]: " + 
													stringArrayTemp.get(i) + "\n");
											fwriter.flush();
										}	
										stringArrayTemp.clear();	
									}
									
									className = "";
									astRootTemp = null; 

								} else{
									// we hope the user would not do something too crazy at this point
									fwriter.write(sdf.format(cal.getTime()) + " [COMPILATION_UNIT ERROR]: INVALID CLASS NAME OR MISSING CLASS TYPE\n");
									fwriter.flush();
									className = "";
									astRootTemp = null; 
								}
							}
						}
					}
					
					/*
					 * This is the case when user switches to another Java Project. 
					 * Close previous file writer and reinitialize new file writer (check if .sapphire exists)
					 * Procedure is same as the previous case.
					 */
					else{
						Calendar cal = Calendar.getInstance();
				        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd 'at' HH:mm:ss z");
				        if(enableRecording){
				        	fwriter.write(sdf.format(cal.getTime()) + " [PLUGIN CLOSED]\n");
							fwriter.flush();
							fwriter.close();	
				        	
				        }
						
						
						previousJavaProjectName = event.getDelta().getElement().getJavaProject().getProject().getName();
						fileNameAndLocation = event.getDelta().getElement().getJavaProject().getProject().getLocation().toString();
		
						File checkFile = new File(fileNameAndLocation + "/.settings/.sapphire");
						enableRecording = checkFile.exists();
						if(enableRecording){
							reinitializeFile(fileNameAndLocation + "/." + previousJavaProjectName + ".RECORDING");
							fwriter.write(sdf.format(cal.getTime()) + " [PLUGIN INITIALIZED]\n");
							fwriter.flush();	  
						}
						
						
						if(event.getDelta().getElement().getElementType() == COMPILATION_UNIT){
							
							// the normal case
							if(event.getDelta().getCompilationUnitAST() != null && 
									event.getDelta().getCompilationUnitAST().getTypeRoot() != null &&
									event.getDelta().getCompilationUnitAST().getTypeRoot().findPrimaryType() != null){
								className = event.getDelta().getCompilationUnitAST().getTypeRoot().findPrimaryType().getFullyQualifiedName();				
								projectAndClassName = previousJavaProjectName + "." + className;
								
								boolean no_error = error_checking(event);

								if(no_error){
									if(mapAST.containsKey(projectAndClassName)){
										astRootTemp = (CompilationUnit) mapAST.get(projectAndClassName);
										printAST(event);
										mapAST.replace(projectAndClassName, astRootTemp);	
									}
									else{
										astRootTemp = null;
										printAST(event);
										mapAST.put(projectAndClassName, astRootTemp);
									}		
								}
								
								
							}
							
							// the crazy case
							else{															
								ICompilationUnit cuCheck = (ICompilationUnit) event.getDelta().getElement();
								ASTParser parserCheck = ASTParser.newParser(AST.JLS8);
								parserCheck.setSource(cuCheck); 
								parserCheck.setKind(ASTParser.K_COMPILATION_UNIT);
								parserCheck.setResolveBindings(true);
								ASTNode astCheck = (CompilationUnit) parserCheck.createAST(null);
								
								if(astCheck.toString().length() == 0){						
									fwriter.write(sdf.format(cal.getTime()) + " [COMPILATION_UNIT ERROR]: EMPTY SOURCE FILE DETECTED\n");
									fwriter.flush();
									className = "";											
									astRootTemp = null;

								} else{
									fwriter.write(sdf.format(cal.getTime()) + " [COMPILATION_UNIT ERROR]: INVALID CLASS NAME OR MISSING CLASS TYPE\n");
									fwriter.flush();
									className = "";	
									astRootTemp = null;
								}
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
            		
            		Calendar cal = Calendar.getInstance();
    		        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd 'at' HH:mm:ss z");
    		        String projectName = delta.getElement().getJavaProject().getProject().getName();
    		        
    		        if(previousJavaProjectName.equals("")){
    		        	previousJavaProjectName = delta.getElement().getJavaProject().getProject().getName();
    					fileNameAndLocation = delta.getElement().getJavaProject().getProject().getLocation().toString();
    					
    					File checkFile = new File(fileNameAndLocation + "/.settings/.sapphire");
    					enableRecording = checkFile.exists();
    					if(enableRecording){
    						reinitializeFile(fileNameAndLocation + "/." + previousJavaProjectName + ".RECORDING");
    						fwriter.write(sdf.format(cal.getTime()) + " [PLUGIN INITIALIZED]\n");
    						fwriter.flush();	  
    					}
    		        }
    		        
    		        else{
    		        	if(projectName.equals(previousJavaProjectName)){	
    		        		// do nothing now... 
    		        	}
    		        	
    		        	else{
    		        		
    		        		
    						try {
    							if(enableRecording){
    								fwriter.write(sdf.format(cal.getTime()) + " [PLUGIN CLOSED]\n");
        							fwriter.flush();
        							fwriter.close();	
    							}
    							
    							previousJavaProjectName = delta.getElement().getJavaProject().getProject().getName();
    							fileNameAndLocation = delta.getElement().getJavaProject().getProject().getLocation().toString();


    							File checkFile = new File(fileNameAndLocation + "/.settings/.sapphire");
    							enableRecording = checkFile.exists();
    							if(enableRecording){
    								reinitializeFile(fileNameAndLocation + "/." + previousJavaProjectName + ".RECORDING");
        							fwriter.write(sdf.format(cal.getTime()) + " [PLUGIN INITIALIZED]\n");
        							fwriter.flush();	  
    							}
    							
    							
    							
    						} catch (IOException e) {
    							e.printStackTrace();
    						} 
    		        	}
    		        }

            		switch(delta.getElement().getElementType()){
            		case 1: 	 
            			fwriter.write(sdf.format(cal.getTime()) + " [JAVA_MODEL ADDED]: " + delta.getElement() + "\n"); 
            			fwriter.flush();
            			break;       		
            		case 2:         			
            			fwriter.write(sdf.format(cal.getTime()) + " [JAVA_PROJECT ADDED]: " + delta.getElement() + "\n");
            			fwriter.flush();
            			break;       			
            		case 3:         			
            			fwriter.write(sdf.format(cal.getTime()) + " [PACKAGE_FRAGMENT_ROOT ADDED]: " + delta.getElement() + "\n");
            			fwriter.flush();
            			break;       			
            		case 4: 
            			fwriter.write(sdf.format(cal.getTime()) + " [PACKAGE_FRAGMENT ADDED]: " + delta.getElement() + "\n"); 
            			fwriter.flush();
            			break;        			
            		case 5: 
            			fwriter.write(sdf.format(cal.getTime()) + " [COMPILATION_UNIT ADDED]: " + delta.getElement() + "\n"); 
            			fwriter.flush();
            			break;
            		default: 
        
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
            		
            		Calendar cal = Calendar.getInstance();
    		        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd 'at' HH:mm:ss z");	        
    		        String projectName = delta.getElement().getJavaProject().getProject().getName();	    

    		        if(projectName.equals(previousJavaProjectName)){	
    	        		// do nothing now... 
    	        	}
    	        	
    	        	else{
    	        		
    	        		
    					try {
    						if(enableRecording){
    							fwriter.write(sdf.format(cal.getTime()) + " [PLUGIN CLOSED]\n");
        						fwriter.flush();
        						fwriter.close();	
    						}
    						
    						if(delta.getElement().getJavaProject() != null &&
    							delta.getElement().getJavaProject().getProject() != null &&
    							delta.getElement().getJavaProject().getProject().getLocation() != null){
    							previousJavaProjectName = delta.getElement().getJavaProject().getProject().getName();
    							fileNameAndLocation = delta.getElement().getJavaProject().getProject().getLocation().toString();
    							
    							File checkFile = new File(fileNameAndLocation + "/.settings/.sapphire");
    							enableRecording = checkFile.exists();
    							if(enableRecording){
    								reinitializeFile(fileNameAndLocation + "/." + previousJavaProjectName + ".RECORDING");
        							fwriter.write(sdf.format(cal.getTime()) + " [PLUGIN INITIALIZED]\n");
        							fwriter.flush();	  
    							}
    							
    						}

    					} catch (IOException e) {
    						e.printStackTrace();
    					} 

    	        	}	 

            		switch(delta.getElement().getElementType()){
            		case 1:  
            			fwriter.write(sdf.format(cal.getTime()) + " [JAVA_MODEL REMOVED]: " + delta.getElement() + "\n"); 
            			fwriter.flush();
            			break;       			
            		case 2: 
            			fwriter.write(sdf.format(cal.getTime()) + " [JAVA_PROJECT REMOVED]: " + delta.getElement() + "\n");
            			fwriter.flush();
            			break;	       			
            		case 3: 
            			fwriter.write(sdf.format(cal.getTime()) + " [PACKAGE_FRAGMENT_ROOT REMOVED]: " + delta.getElement() + "\n");
            			fwriter.flush();
            			break;        			
            		case 4: 
            			fwriter.write(sdf.format(cal.getTime()) + " [PACKAGE_FRAGMENT REMOVED]: " + delta.getElement() + "\n"); 
            			fwriter.flush();
            			break;       			
            		case 5: 
            			fwriter.write(sdf.format(cal.getTime()) + " [COMPILATION_UNIT REMOVED]: " + delta.getElement() + "\n"); 
            			fwriter.flush();
            			break;

            		default: 
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
				 int lineNumber;
				 if(isTempLarger){
					 lineNumber = astRootTemp.getLineNumber(node.getStartPosition());
				 }
				 else{
					 lineNumber = astRoot.getLineNumber(node.getStartPosition());	
				 }

				
				 
				 lastLineNumber = lineNumber;
				 printStatement(lineNumber, simple, value, node);

			 } else if (descriptor instanceof ChildPropertyDescriptor) {
				 ChildPropertyDescriptor child= (ChildPropertyDescriptor)descriptor;
				 ASTNode childNode= (ASTNode)node.getStructuralProperty(child);
				 if (childNode != null){	
					 
					// print(childNode);
					 
					 

					 if(child.getId().equals("body") || child.getId().equals("thenStatement") 
							 || child.getId().equals("elseStatement")){
						 
						 String str = mapValue.get(childNode.getParent().getNodeType());
						 if(child.getId().equals("elseStatement")){
							 str = "[ELSE_STATEMENT]";
						 }
						 int lineNumber;
						 if(isTempLarger){
							 lineNumber = astRootTemp.getLineNumber(node.getStartPosition());
						 }
						 else{
							 lineNumber = astRoot.getLineNumber(node.getStartPosition());	
						 }	
						 if(lineNumberCheck != 0){
							 if(isTempLarger){
								 stringArrayTemp.add(lineNumberCheck + " $ " + long2);
								 stringArrayTempContent.add(long2);
								 lineNumberCheck = 0;
								 long2 = ""; 
								 stringArrayTemp.add(lineNumber + " $ OPEN " + str);
								 stringArrayTempContent.add("OPEN " + str);	 
							 }
							 else{
								 stringArray.add(lineNumberCheck + " $ " + long2);
								 stringArrayContent.add(long2);
								 lineNumberCheck = 0;
								 long2 = "";
								 stringArray.add(lineNumber + " $ OPEN " + str);
								 stringArrayContent.add("OPEN " + str);
							 }
						 }		 
						 print(childNode);
						 
						 if(lineNumberCheck != 0){
							 if(isTempLarger){
								 stringArrayTemp.add(lineNumberCheck + " $ " + long2);
								 stringArrayTempContent.add(long2);
								 lineNumberCheck = 0;
								 long2 = ""; 
								 stringArrayTemp.add(lastLineNumber + " $ CLOSE " + str);
								 stringArrayTempContent.add("CLOSE " + str);
							 }
							 else{
								 stringArray.add(lineNumberCheck + " $ " + long2);
								 stringArrayContent.add(long2);
								 lineNumberCheck = 0;
								 long2 = "";
								 stringArray.add(lastLineNumber + " $ CLOSE " + str);
								 stringArrayContent.add("CLOSE " + str);
							 }
						 }
					 }		 
					 else{
						 print(childNode);
					 }
					 
					 
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

	/**
	 * Method printStatement() records the AST node's description by stating the
	 * parent node's type and also the corresponding line number to the source file.
	 * The idea is to record the information line by line.
	 * 
	 * @author Chern Yee Chua
	 */

	public static void printStatement(int lineNumber, SimplePropertyDescriptor simple, Object value, ASTNode node){
		
		String str;
		String strValue;
		String strSimple;
		if(node.getNodeType() == IMPORT_DECLARATION){
			str = mapValue.get(node.getNodeType());
		}
		else{
			str = mapValue.get(node.getParent().getNodeType());
		}
		
		// this prevents error from javadoc
		if(value == null){
			strValue = "null";
		}
		else{
			strValue = value.toString();
		}
		
		strSimple = simple.getId();
			
		// remove the element name that is pretty much useless 
		if(strSimple.equals("primitiveTypeCode") || strSimple.equals("identifier") || strSimple.equals("escapedValue") ||
				strSimple.equals("operator") || strSimple.equals("token")){
			strSimple = "";
		}
		else{
			strSimple = strSimple + " ";
		}
		

		
		
		if(lineNumberCheck == 0){
			long2 += str + " " + strSimple + "(" + strValue + ") | ";	
			lineNumberCheck = lineNumber;
		 }
		else{
			if(lineNumber != lineNumberCheck){		
				 
				if(isTempLarger){
					stringArrayTempNumber.add(lineNumberCheck);
					stringArrayTemp.add(lineNumberCheck + " $ " + long2);
					stringArrayTempContent.add(long2);
				}
				else{
					stringArrayNumber.add(lineNumberCheck);
					stringArray.add(lineNumberCheck + " $ " + long2); 
					stringArrayContent.add(long2); 
				}	 
				long2 = ""; 
				long2 += str + " " + strSimple + "(" + strValue + ") | ";	
				lineNumberCheck = lineNumber;			 
			}
			else{
				long2 += strSimple + "(" + strValue + ") | ";	
			}
		}		
	}
	
	public void arrayClear(){
		stringArrayNumber.clear();
		stringArrayTempNumber.clear();
		stringArray.clear();
		stringArrayTemp.clear();
		stringArrayContent.clear();
		stringArrayTempContent.clear();
		commentList.clear();
		commentListTemp.clear();
		commentListContent.clear();
		commentListTempContent.clear();	
	}

}