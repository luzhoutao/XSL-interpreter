package parser;

import java.util.ArrayList;

import XML_parser.XMLTag;
import visitors.Visitor;

public class StyleSheetNode extends ASTNode {

	private String version;
	private String url;
	public StyleSheetNode() {
		super("xsl:stylesheet");
		// TODO Auto-generated constructor stub
	}

	public void setVersion(String version){
		this.version = version;
	}
	
	public void setURL(String url){
		this.url = url;
	}
}
