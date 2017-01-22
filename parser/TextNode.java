package parser;

import java.util.*;

import main.TinyCompiler;
import XML_parser.XMLTag;
import visitors.Visitor;

public class TextNode extends ASTNode {
	private String disable_output_escaping = "no";
	
	public TextNode() {
		super("xsl:text");
	}

	public void setDOE(String doe){
		this.disable_output_escaping = doe;
	}
	
	public void translate(ArrayList<XMLTag> tags){
		String src = this.children.get(0).getTag();
		String interpretation = "";
		if(disable_output_escaping.equals("no")){
			interpretation = src.replace("&#160;", " ").replace("&#xa;", "\n");
		}else{
			interpretation = src;
		}
		TinyCompiler.getInstance().appendTarget(interpretation);;
	}
}
