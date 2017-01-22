package parser;

import java.util.ArrayList;

import main.TinyCompiler;
import XML_parser.XMLTag;
import visitors.Visitor;

public class ValueNode extends ASTNode{

	private String select;
	public ValueNode() {
		super("xsl:value-of");
	}

	public void setSelect(String select){
		this.select = select;
	}
	
	public void translate(ArrayList<XMLTag> tags){
		assert(tags.size()>0);
		XMLTag tag = tags.get(0);
		
		ArrayList<XMLTag> targets = tag.getChildrenByPath(select);
		
		assert(targets.size()==1);
		assert(targets.get(0).getContent()!=null);
		
		TinyCompiler.getInstance().appendTarget(targets.get(0).getContent());
	}
}
