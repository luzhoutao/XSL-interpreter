package scanner;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * @author luzhoutao
 * This is the ad hoc scanner for scanning the xsl file, split the file string to a strem of tokens.
 */
public class Scanner {

	private static Scanner instance;
	private ArrayList<String> code = new ArrayList<String>();
	private List<Character> delims = Arrays.asList('<','>','?','=','/','"',' ','\t');
	private List<Character> blank = Arrays.asList(' ','\t');
	
	/*
	 * By hard-coded control block
	 * @return tokens: the list of separated tokens
	 */
	public ArrayList<Token> scan(String filename) throws IOException{
		code.clear();
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filename)));
		ArrayList<Token> tokens = new ArrayList<Token>();
		
		String curLine;
		int line_no = 1;
		StringBuffer tok_buf = new StringBuffer(80); // to buffer the characters of current identifier
		while((curLine = br.readLine())!=null){
			code.add(curLine);
			int index = 0;
			while(index<curLine.length()){
				char curChar = curLine.charAt(index);
				
				if(isDelim(curChar)){
					
					if(tok_buf.length()!=0){
						tokens.add(new Token(tok_buf.toString(), line_no));
						tok_buf.setLength(0);// clear the identifier buffer
					}
					
					if(!isBlank(curChar))
						tokens.add(new Token(String.valueOf(curChar),line_no)); // store the terminal symbol
					
					if(curChar == '"')
						index = dealQuotation(index,curLine,tokens,line_no);
				}else
					tok_buf.append(curChar);
				index++;
			}
			//to store the possible left identifier
			if(tok_buf.length()!=0)
				tokens.add(new Token(tok_buf.toString(), line_no));
		}
		//System.out.println(tokens);
		return tokens;
	}

	/*
	 * @param index: the start quotation index
	 * @param curLine: the current line being processed
	 * @param tokens: the arraylist used to store the tokens
	 * @return curTail: the index of end quotation, if not exsit, the index of start one
	 */
	private int dealQuotation(int index, String curLine, ArrayList<Token> tokens, int line_no) {
		int head = index; // the char start "
		
		index++;
		while(index < curLine.length()){
			if(curLine.charAt(index)=='"')
				break;
			index++;
		}
		if(index>head){
			tokens.add(new Token(curLine.substring(head,index).replaceAll("\"", ""), line_no));
			tokens.add(new Token("\"", line_no));
		}
		return index;
	}

	/*
	 * @return true: c is a blank, either tab or space
	 * 			false: otherwise
	 */
	private boolean isBlank(char c){
		return blank.contains(c);
	}
	
	/*
	 *  @return true: s is a delimiter
	 *  		false: otherwise
	 */
	private boolean isDelim(char s){
		return delims.contains(s);
	}
	
	public static Scanner getScanner(){
		if (instance==null)
			return instance = new Scanner();
		else 
			return instance;
	}
	public String getCodeByLineNo(int line_no){
		if(line_no>code.size())
			return null;
		return this.code.get(line_no-1);
	}
	public static void main(String[] args){
		if(args.length!=1)
			System.out.println("Need file-path only!");
		
		try {
			List<Token> tokens = new Scanner().scan(args[0]);
			System.out.println(tokens);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
