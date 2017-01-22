package XML_parser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import exception.SyntaxException;
import scanner.Scanner;
import scanner.Token;

public class XMLParser {

	private static XMLParser instance;
	private XMLParser(){}
	
	public XMLTag parse(ArrayList<Token> tokens) throws SyntaxException{
		int ptr = 0;
		Stack<XMLTag> p_stack = new Stack<XMLTag>();
		p_stack.push(XMLTag.getRoot());
		while(ptr<tokens.size()){
			Token first = tokens.get(ptr++);
			Token second = tokens.size()>ptr?tokens.get(ptr++):null;
			
			if(first.getContent().equals("<")){
				if(second!=null && second.getContent().equals("/")){
					Token third = tokens.size()>ptr?tokens.get(ptr++):null;
					if(third!=null && third.getContent().equals(p_stack.peek().getTag())){
						
						if(ptr>=tokens.size())
							throw new SyntaxException(third.getLine_no());
						match(tokens.get(ptr++), ">");
						
						p_stack.pop();
						
					}else
						throw new SyntaxException(second.getLine_no());
				}else if(second!=null){
					XMLTag tag = new XMLTag(second.getContent());
					match(tokens.get(ptr++), ">");
					p_stack.peek().addChild(tag);
					p_stack.push(tag);
				}else
					throw new SyntaxException(first.getLine_no());
			}else{
				ptr--;
				p_stack.peek().setContent(first.getContent());;
			}
		}
		return XMLTag.getRoot();
	}
	
	public void match(Token token, String expected) throws SyntaxException{
		if(!token.getContent().equals(expected))
			throw new SyntaxException(token.getLine_no());
	}
	
	public static XMLParser getInstance(){
		if(instance==null)
			return instance = new XMLParser();
		else 
			return instance;
	}
	
	public static void main(String[] args){
		if(args.length!=1)
			System.out.println("Need file-path only!");
		
		try {
			ArrayList<Token> tokens = Scanner.getScanner().scan(args[0]);
			System.out.println(tokens);
			XMLTag tag = XMLParser.getInstance().parse(tokens);
			tag.getChildrenByPath("/shop/part2/book");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
