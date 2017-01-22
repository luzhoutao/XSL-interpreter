package parser;

import java.util.ArrayList;

import XML_parser.XMLTag;
import visitors.Visitor;

public class TemplateNode extends ASTNode {

	private String match;
	public TemplateNode() {
		super("xsl:template");
	}

	public void setMatch(String match){
		this.match = match;
	}
	
	public void translate(ArrayList<XMLTag> tags){
		assert(tags.size()>0);
		XMLTag tag = tags.get(0);
		
		ArrayList<XMLTag> targets = tag.getChildrenByPath(match);
		
		for(ASTNode node: children){
			node.translate(targets);
		}
	}
}
