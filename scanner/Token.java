package scanner;

public class Token {
	String content;
	int line_no;
	
	public Token(String content, int line_no){
		this.content = content;
		this.line_no = line_no;
	}
	
	public String toString(){
		return this.content;
	}

	public String getContent() {
		return content;
	}

	public int getLine_no() {
		return line_no;
	}
}
