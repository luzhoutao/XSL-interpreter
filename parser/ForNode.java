package parser;

import java.util.ArrayList;
import java.util.stream.Collectors;

import XML_parser.XMLTag;

public class ForNode extends ASTNode{
	private String select;
	public ForNode() {
		super("xsl:for-each");
	}

	public void setSelect(String select){
		this.select = select;
	}
	
	public void translate(ArrayList<XMLTag> tags){
		assert(tags.size()>0);
		XMLTag tag = tags.get(0);
		ArrayList<XMLTag> targets;
		
		String condition = "";
		int start = select.indexOf('[');
		int end = select.indexOf(']');
		if(start>=0 && end>=0 && end-start>1){
			condition = select.substring(start+1, end);
			select = select.substring(0, start);
			
			targets = tag.getChildrenByPath(select);
			String[] cond_a = parseCondition(condition);
			targets = new ArrayList<XMLTag>(targets.stream().filter(e->{
				ArrayList<XMLTag> sub = e.getChildrenByPath(cond_a[0]);
				assert(sub.size()==1);
				assert(sub.get(0).getContent()!=null);
				return sub.get(0).getContent().equals(cond_a[1]);
			}).collect(Collectors.toList()));
		}else {
			targets =  tag.getChildrenByPath(select);
		}
		
		while(targets.size()>0){
			for(ASTNode node: children){
				node.translate(targets);
			}
			targets.remove(0);
		}
	}
	
	public String[] parseCondition(String cond){
		ArrayList<String> sa = new ArrayList<String>();
		for(String s:cond.split("=|'")){
			if(s.length()!=0)
				sa.add(s);
		}
		return sa.toArray(new String[2]);
	}
}
