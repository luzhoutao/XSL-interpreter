package main;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import XML_parser.XMLParser;
import XML_parser.XMLTag;
import parser.ASTNode;
import parser.Parser;
import exception.SyntaxException;
import scanner.Scanner;
import scanner.Token;

public class TinyCompiler {
	
	private String target_code = "";
	
	private static TinyCompiler compiler;
	private TinyCompiler(){}
	
	public void appendTarget(String s){
		this.target_code += s;
	}
	public static TinyCompiler getInstance(){
		if(compiler == null)
			return compiler = new TinyCompiler();
		else 
			return compiler;
	}
	private void output(String filename)
	{
		try {
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename)));
			bw.write(this.target_code.substring(1,target_code.length()-1));
			bw.close();
			this.target_code = "";
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args){
		String[] filename = {"XSL-1.txt", "XSL-2.txt", "XSL-3.txt"};
		for(int i = 0; i<filename.length; i++){
			XMLTag.getRoot().clear();
			ASTNode.getRoot().clear();
			
			String name = filename[i];
			ArrayList<Token> tokens;
			try {
				tokens = Scanner.getScanner().scan(name);
				ASTNode root = Parser.getParser().parse(tokens);
				
				tokens = Scanner.getScanner().scan("XML.txt");
				XMLTag tag = XMLParser.getInstance().parse(tokens);
				
				ArrayList<XMLTag> tags = new ArrayList<XMLTag>();
				tags.add(tag);
				root.translate(tags);
				TinyCompiler.getInstance().output("my_result_"+(i+1)+".txt");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
}
