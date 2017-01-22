package parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import XML_parser.XMLTag;
import visitors.Visitor;

public class SortNode extends ASTNode{

	public String select;
	public SortNode() {
		super("xsl:sort");
	}

	public void setSelect(String select){
		this.select = select;
	}
	
	public void translate(ArrayList<XMLTag> tags){
		assert(tags.size()>0);
		
		int size = tags.size();
		
		Map<XMLTag, XMLTag> map = new HashMap<XMLTag, XMLTag>();
		for(int ptr = 0; ptr<size; ptr++){
			XMLTag tag = tags.get(ptr);
			ArrayList<XMLTag> measure = tag.getChildrenByPath(select);
			assert(measure.size()==1);
			assert(measure.get(0).getContent()!=null);
			map.put(measure.get(0),tag);
		}
		
		XMLTag[] measures = new XMLTag[size];
		measures = map.keySet().toArray(measures);
		Arrays.sort(measures);
		
		for(int i = 0; i<size; i++){
			tags.set(i, map.get(measures[i]));
		}
		
		for(ASTNode node: children){
			node.translate(tags);
		}
	}
	
	private int str2int(String s){
		int result = 0;
		for(int i = 0; i<s.length(); i++){
			result += s.charAt(s.length()-i-1)*Math.pow(10, i);
		}
		return result;
	}
}