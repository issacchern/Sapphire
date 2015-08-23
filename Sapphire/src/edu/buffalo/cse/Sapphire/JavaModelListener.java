/**
 * This file is the main part of Project Sapphire.
 * 
 * Project Sapphire is an eclipse plugin research project 
 * under supervision of Prof. Carl Alphonce and Prof. Bina 
 * Ramamurthy from University at Buffalo, New York.
 *  
 * Sapphire is a free plugin software, licensed under the terms of the 
 * Eclipse Public License, version 1.0.  The license is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Sapphire © 2015 University at Buffalo. All rights reserved.  
 */


package edu.buffalo.cse.Sapphire;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static org.eclipse.jdt.core.IJavaElement.JAVA_MODEL;
import static org.eclipse.jdt.core.IJavaElement.JAVA_PROJECT;
import static org.eclipse.jdt.core.IJavaElement.COMPILATION_UNIT;
import static org.eclipse.jdt.core.IJavaElement.PACKAGE_FRAGMENT;
import static org.eclipse.jdt.core.IJavaElement.PACKAGE_FRAGMENT_ROOT;

import org.eclipse.jdt.core.ElementChangedEvent;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IElementChangedListener;
import org.eclipse.jdt.core.IJavaElementDelta;
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

/**
 * This class handles all the changes to the JavaModel, recording all of
 * the changes that the user has made into a text file. 
 * 
 * @author Chern Yee Chua
 */
public class JavaModelListener implements IElementChangedListener{
	
	//change these values to serve you needs 
	
	private static String fileNameAndLocation =  "main_recorder";

	private static String className = "";
	private static String long2 = "";
	
	private static String previousJavaProjectName = "";
	
	private static boolean isTempLarger = false;
	private static int lineNumberCheck = 0;
	
	private FileOutputStream fout;
	
	private static HashMap<Integer, String> mapValue;
	
	private static IElementChangedListener _listener = new JavaModelListener();
	
	private ICompilationUnit cuTemp;	
	private String sourceTemp;
	private static CompilationUnit astRootTemp;
	
	private ICompilationUnit cu;
	private String source;
	private static CompilationUnit astRoot;
	
	private static List<String> stringArray = new ArrayList<String>();
	private static List<Integer> stringArrayLineNumber = new ArrayList<Integer>();
	private static List<String> stringArrayContent = new ArrayList<String>();
	
	private static List<String> stringArrayTemp = new ArrayList<String>();
	private static List<Integer> stringArrayTempLineNumber = new ArrayList<Integer>();
	private static List<String> stringArrayTempContent = new ArrayList<String>();
	
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
		
		
		try
		{
			File newFile = new File(fileNameAndLocation);
			fout= new FileOutputStream(newFile, true);		
			MultiOutputStream multiOut= new MultiOutputStream(System.out, fout);	
			PrintStream stdout= new PrintStream(multiOut);	
			System.setOut(stdout);
			Calendar cal = Calendar.getInstance();
	        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd 'at' HH:mm:ss z");
			System.out.println(sdf.format(cal.getTime()) + " [LISTENER INITIALIZATION] ");
			fout.close();
		}
		catch (Exception ex)
		{
			System.out.println(ex);
		}	
		
		cuTemp = null;

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
	 * Method parse() takes string source of CompilationUnit and store the visiting
	 * node into ArrayList. Since Comment node is not part of the ASTNode structure,
	 * visiting those Comment nodes (LineComment, BlockComment and Javadoc) are 
	 * neccessary.
	 * 
	 * @author Chern Yee Chua
	 */
	
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
					int lineNumber = astRoot.getLineNumber(node.getStartPosition());
					int lineNumberTemp = astRootTemp.getLineNumber(node.getStartPosition());
					 
					if(isTempLarger){
						 lineNumber = lineNumberTemp;
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
					int lineNumber = astRoot.getLineNumber(node.getStartPosition());
					int lineNumberTemp = astRootTemp.getLineNumber(node.getStartPosition());
	 
					if(isTempLarger){
						lineNumber = lineNumberTemp;
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
					
					int lineNumber = astRoot.getLineNumber(node.getStartPosition());
					int lineNumberTemp = astRootTemp.getLineNumber(node.getStartPosition());
					 
					 
					if(isTempLarger){
						lineNumber = lineNumberTemp;
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
			if(cuTemp == null){				
				cuTemp = (ICompilationUnit) event.getDelta().getElement();
				sourceTemp = cuTemp.getSource();
				ASTParser parserTemp = ASTParser.newParser(AST.JLS8);
				parserTemp.setSource(cuTemp); 
				parserTemp.setKind(ASTParser.K_COMPILATION_UNIT);
				parserTemp.setResolveBindings(true);
				astRootTemp = (CompilationUnit) parserTemp.createAST(null);
				System.out.println(astRootTemp);
				className = event.getDelta().getCompilationUnitAST().getTypeRoot().findPrimaryType().getFullyQualifiedName();
			
			}
			
			else{
				
				// set the className
				className = event.getDelta().getCompilationUnitAST().getTypeRoot().findPrimaryType().getFullyQualifiedName();
				
				cu = (ICompilationUnit) event.getDelta().getElement();
				source = cu.getSource();
				ASTParser parser = ASTParser.newParser(AST.JLS8);
				parser.setSource(cu);
				parser.setKind(ASTParser.K_COMPILATION_UNIT);
				parser.setResolveBindings(true);
				astRoot = (CompilationUnit) parser.createAST(null);
				
				isTempLarger = true;
				long2 = "";
				lineNumberCheck = 0;
				print(astRootTemp);
				
				// add the last piece of element
				stringArrayTemp.add(lineNumberCheck + " $ " + long2); 
				stringArrayTempLineNumber.add(lineNumberCheck); 
				stringArrayTempContent.add(long2);
				
				// reset the data
				long2 = "";	
				lineNumberCheck = 0;			
				isTempLarger = false; 
				print(astRoot);
				
				// add the last piece of element
				stringArray.add(lineNumberCheck + " $ " + long2);
				stringArrayLineNumber.add(lineNumberCheck);
				stringArrayContent.add(long2);

				// this algorithm marks the duplicate element with "duplicate" with the count at the end
				// NOTE: this is for current unit
				ArrayList<String> arr = new ArrayList<String>();	
				int cnt = 0;
				for(int i = 0 ; i < stringArray.size(); i++ ){
					int index = stringArray.get(i).indexOf('$');
					String stringSub = stringArray.get(i).substring(index);	
					
					if(arr.contains(stringSub)){
						cnt++;
						stringArray.set(i, stringArray.get(i) + "(duplicate: " + cnt + " )");
						stringArrayContent.set(i, stringSub.substring(2) + "(duplicate: " + cnt + " )");
						arr.add(stringSub + "(duplicate: " + cnt + " )");
					}
					else{
						arr.add(stringSub);
					}

				}
				
				// this algorithm marks the duplicate element with "duplicate" with the count at the end
				// NOTE: this is for previous unit
				
				ArrayList<String> arrTemp = new ArrayList<String>();			
				int cntTemp = 0;
				for(int i = 0 ; i < stringArrayTemp.size(); i++ ){
					int index = stringArrayTemp.get(i).indexOf('$');
					String stringSub = stringArrayTemp.get(i).substring(index);	
					
					if(arrTemp.contains(stringSub)){
						cntTemp++;
						stringArrayTemp.set(i, stringArrayTemp.get(i) + "(duplicate: " + cntTemp + " )");
						stringArrayTempContent.set(i, stringSub.substring(2) + "(duplicate: " + cntTemp + " )");
						arrTemp.add(stringSub + "(duplicate: " + cntTemp + " )");
					}
					else{
						arrTemp.add(stringSub);
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
				Calendar cal = Calendar.getInstance();
		        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd 'at' HH:mm:ss z");

		        // display what new element is added 
				for(int i = 0 ; i < afterModificationAdded.size(); i++){
					System.out.println(sdf.format(cal.getTime()) + " [" + className + "]" + " [ADDED LINE] : " 
							+ afterModificationAdded.get(i));
				}
				
				// display what old element is removed
				for(int i = 0 ; i < afterModificationRemoved.size(); i++){
					System.out.println(sdf.format(cal.getTime()) + " [" + className + "]" + " [REMOVED LINE] : "
							+ afterModificationRemoved.get(i));
				}
				
				// retrieve the comment elements
				isTempLarger = true;
				parse(sourceTemp);
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

							afterCommentRemoved.remove(commentListTemp.get(i)); // you need to check again before you remove the element. 
							commentListContent.remove(commentListContent.get(j));
							break;
						}
					}
					
				}

				// display what new comment is added
				for(int i = 0 ; i < afterCommentAdded.size(); i++){
					System.out.println(sdf.format(cal.getTime()) + " [" + className + "] " +  "[ADDED LINE] : " + 
							afterCommentAdded.get(i));
				}
				
				// display what old comment is removed
				for(int i = 0 ; i < afterCommentRemoved.size(); i++){
					System.out.println(sdf.format(cal.getTime()) + " [" + className + "] " +  "[REMOVED LINE] : " 
							+ afterCommentRemoved.get(i));   
				}
				

		//		System.out.println("...");

				// remove all elements in those lists 
				stringArray.clear();
				stringArrayTemp.clear();
				stringArrayLineNumber.clear();
				stringArrayTempLineNumber.clear();
				stringArrayContent.clear();
				stringArrayTempContent.clear();
				commentList.clear();
				commentListTemp.clear();
				commentListContent.clear();
				commentListTempContent.clear();
				
				// set the current CompilationUnit as previous one for next elementChanged event 
				cuTemp = cu;			
				sourceTemp = cuTemp.getSource();
				ASTParser parserTemp = ASTParser.newParser(AST.JLS8);
				parserTemp.setSource(cuTemp); 
				parserTemp.setKind(ASTParser.K_COMPILATION_UNIT);
				parserTemp.setResolveBindings(true);
				astRootTemp = (CompilationUnit) parserTemp.createAST(null);
			} 
		}
		
		catch(Exception e){
			System.out.println(e);
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
			fout= new FileOutputStream(newFile, true);		
			MultiOutputStream multiOut= new MultiOutputStream(System.out, fout);	
			PrintStream stdout= new PrintStream(multiOut);	
			System.setOut(stdout);
			
		}
		catch (Exception ex)
		{
			System.out.println(ex);
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
	
	@Override
	public void elementChanged(ElementChangedEvent event) {	
		
		try{
			traverseAndPrint(event.getDelta());
			
			if(event != null && event.getDelta() != null && event.getDelta().getElement() != null &&
					event.getDelta().getCompilationUnitAST() != null && 
					event.getDelta().getCompilationUnitAST().getTypeRoot() != null &&
					event.getDelta().getCompilationUnitAST().getTypeRoot().findPrimaryType() != null &&
					event.getDelta().getElement().getJavaProject() != null){
				
				// this should only be visited once
				if(previousJavaProjectName.equals("")){
					previousJavaProjectName = event.getDelta().getElement().getJavaProject().getProject().getName();
					
					fileNameAndLocation = event.getDelta().getElement().getJavaProject().getProject().getLocation().toString();
					 
					reinitializeFile(fileNameAndLocation + "/." + previousJavaProjectName + ".RECORDING");
					
					Calendar cal = Calendar.getInstance();
			        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd 'at' HH:mm:ss z");
					System.out.println(sdf.format(cal.getTime()) + " [INITIALIZE PLUGIN]");

					
					if(event.getDelta().getElement().getElementType() == COMPILATION_UNIT){
						printAST(event);
					}
				}
				
				else{
					
					if(event.getDelta().getElement().getJavaProject().getProject().getName().equals(previousJavaProjectName)){
						if(event.getDelta().getElement().getElementType() == COMPILATION_UNIT){

							if(event.getDelta().getCompilationUnitAST().getTypeRoot().findPrimaryType().getFullyQualifiedName().
									equals(className)){
								printAST(event);		
							}
							
							else{
								Calendar cal = Calendar.getInstance();
						        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd 'at' HH:mm:ss z");
								System.out.println(sdf.format(cal.getTime()) + " [CHANGE COMPILATION_UNIT TO " + 
										event.getDelta().getCompilationUnitAST().getTypeRoot().findPrimaryType().getFullyQualifiedName()
										+ "]");
								cuTemp = null;
								printAST(event);
							}
										
						}	
					}
					
					else{
						Calendar cal = Calendar.getInstance();
				        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd 'at' HH:mm:ss z");
						System.out.println(sdf.format(cal.getTime()) + " [CLOSE PLUGIN]");
						
						fout.close(); // close the previous file stream
						
						previousJavaProjectName = event.getDelta().getElement().getJavaProject().getProject().getName();
						fileNameAndLocation = event.getDelta().getElement().getJavaProject().getProject().getLocation().toString();
						 
						// create new file stream
						reinitializeFile(fileNameAndLocation + "/." + previousJavaProjectName + ".RECORDING");
						
						System.out.println(sdf.format(cal.getTime()) + " [INITIALIZE PLUGIN]");
											
					}
				}
			}

			
		} catch(Exception e){
			System.out.println(e);
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
	
	public void traverseAndPrint(IJavaElementDelta delta) {
        switch (delta.getKind()) {
        case IJavaElementDelta.ADDED:
        	
        	if(delta.getElement().getElementType() == JAVA_PROJECT || 
        			delta.getElement().getElementType() == JAVA_MODEL ||
        			delta.getElement().getElementType() == PACKAGE_FRAGMENT ||
        			delta.getElement().getElementType() == PACKAGE_FRAGMENT_ROOT ||
        			delta.getElement().getElementType() == COMPILATION_UNIT){
        		
        		Calendar cal = Calendar.getInstance();
		        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd 'at' HH:mm:ss z");

        		switch(delta.getElement().getElementType()){
        		case 1: System.out.println(sdf.format(cal.getTime()) + " [ADD JAVA_MODEL]: " + delta.getElement()); break;
        		case 2: System.out.println(sdf.format(cal.getTime()) + " [ADD JAVA_PROJECT]: " + delta.getElement()); break;
        		case 3: System.out.println(sdf.format(cal.getTime()) + " [ADD PACKAGE_FRAGMENT_ROOT]: " + delta.getElement()); break;
        		case 4: System.out.println(sdf.format(cal.getTime()) + " [ADD PACKAGE_FRAGMENT]: " + delta.getElement()); break;
        		case 5: System.out.println(sdf.format(cal.getTime()) + " [ADD COMPILATION_UNIT]: " + delta.getElement()); break;
        		default: 
    
        		}
        	}
            
            break;
                            
        case IJavaElementDelta.REMOVED:
        	if(delta.getElement().getElementType() == JAVA_PROJECT || 
			delta.getElement().getElementType() == JAVA_MODEL ||
			delta.getElement().getElementType() == PACKAGE_FRAGMENT ||
			delta.getElement().getElementType() == PACKAGE_FRAGMENT_ROOT ||
			delta.getElement().getElementType() == COMPILATION_UNIT){
        		
        		Calendar cal = Calendar.getInstance();
		        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd 'at' HH:mm:ss z");
		        
        		switch(delta.getElement().getElementType()){
        		case 1: System.out.println(sdf.format(cal.getTime()) + " [REMOVE JAVA_MODEL]: " + delta.getElement()); break;
        		case 2: System.out.println(sdf.format(cal.getTime()) + " [REMOVE JAVA_PROJECT]: " + delta.getElement()); break;
        		case 3: System.out.println(sdf.format(cal.getTime()) + " [REMOVE PACKAGE_FRAGMENT_ROOT]: " + delta.getElement()); break;
        		case 4: System.out.println(sdf.format(cal.getTime()) + " [REMOVE PACKAGE_FRAGMENT]: " + delta.getElement()); break;
        		case 5: System.out.println(sdf.format(cal.getTime()) + " [REMOVE COMPILATION_UNIT]: " + delta.getElement()); break;
        		default: 
    
        		}
        	}

            break;

        // do nothing in particular     
        case IJavaElementDelta.CHANGED:
        
            break;

        }
        IJavaElementDelta[] children = delta.getAffectedChildren();
        for (int i = 0; i < children.length; i++) {
        	traverseAndPrint(children[i]);
        }
	}
	
	/**
	 * This method traverses the root ASTNode in the parameter and calls
	 * printStatement to store the information of the child node.
	 * 
	 * @author Chern Yee Chua
	 */

	
	public static void print(ASTNode node) {
		// return a list of structural property descriptor
		 List properties= node.structuralPropertiesForType();
		 for (Iterator iterator= properties.iterator(); iterator.hasNext();) {
			 Object descriptor= iterator.next();
			 if (descriptor instanceof SimplePropertyDescriptor) {
				 SimplePropertyDescriptor simple= (SimplePropertyDescriptor)descriptor;
				 Object value= node.getStructuralProperty(simple);		 

				 int lineNumber = astRoot.getLineNumber(node.getStartPosition());
				 int lineNumberTemp = astRootTemp.getLineNumber(node.getStartPosition());

				 if(isTempLarger){
					 lineNumber = lineNumberTemp;
				 }

				 printStatement(lineNumber, simple, value, node);
				 
			 } else if (descriptor instanceof ChildPropertyDescriptor) {
				 ChildPropertyDescriptor child= (ChildPropertyDescriptor)descriptor;
				 ASTNode childNode= (ASTNode)node.getStructuralProperty(child);
				 if (childNode != null) {
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
	
	
	/**
	 * Method printStatement() records the AST node's description by stating the
	 * parent node's type and also the corresponding line number to the source file.
	 * The idea is to record the information line by line.
	 * 
	 * @author Chern Yee Chua
	 */

	public static void printStatement(int lineNumber, SimplePropertyDescriptor simple, Object value, ASTNode node){
		
		String str = mapValue.get(node.getParent().getNodeType());
		String strValue = value.toString();
		String strSimple = simple.getId();
					
		// remove the element name that is pretty much useless 
		if(strSimple.equals("primitiveTypeCode") || strSimple.equals("identifier")){
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
					stringArrayTemp.add(lineNumberCheck + " $ " + long2);
					stringArrayTempLineNumber.add(lineNumberCheck);
					stringArrayTempContent.add(long2);
				}
				else{
					stringArray.add(lineNumberCheck + " $ " + long2); 
					stringArrayLineNumber.add(lineNumberCheck);
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

}