package parser;

import java.util.ArrayList;

import XML_parser.XMLTag;
import visitors.Visitor;

public class IfNode extends ASTNode {

	private String test;
	public IfNode() {
		super("xsl:if");
		// TODO Auto-generated constructor stub
	}

	public void setTest(String test){
		this.test = test;
	}
	
	public void translate(ArrayList<XMLTag> tags){
		assert(tags.size()>0);
		XMLTag tag = tags.get(0);
		
		if(!test(tag))
			return;
		
		for(ASTNode node: children){
			node.translate(tags);
		}
	}
	
	private boolean test(XMLTag tag){
		String[] sa = test.split(" ");
		ArrayList<XMLTag> ta = tag.getChildrenByPath(sa[0]);
		assert(ta.size()==1);
		double num = Double.parseDouble(sa[2]);
		double con = Double.parseDouble(ta.get(0).getContent());
		if(sa[1].equals("&gt;"))
			return con>num;
		else if(sa[1].equals("&lt;"))
			return con<num;
		return false;
	}
}
