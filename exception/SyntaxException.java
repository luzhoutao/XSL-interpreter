package exception;

import scanner.Scanner;

public class SyntaxException extends Exception{

	String code;
	public SyntaxException(int line_no){
		this.code = Scanner.getScanner().getCodeByLineNo(line_no);
	}
	
}
