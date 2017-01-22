package XML_parser;

import java.util.ArrayList;

public class XMLTag implements Comparable{
	
	private String tag;
	private String content;
	private ArrayList<XMLTag> children = new ArrayList<XMLTag>();
	
	public XMLTag(String tag){
		this.tag = tag;
	}
	
	public String getTag(){
		return this.tag;
	}
	
	public void addChild(XMLTag node){
		this.children.add(node);
	}
	
	public void setContent(String content){
		this.content = content;
	}
	public ArrayList<XMLTag> getChildrenByPath(String path){
		if(path.charAt(0)=='/')
			path = path.substring(1, path.length());
		int index = path.indexOf("/");
		String name = index<0?path.substring(0, path.length()):path.substring(0, index);
		String rest = index<0?null:path.substring(index, path.length());
		
		ArrayList<XMLTag> selected = new ArrayList<XMLTag>();
		for(XMLTag child:this.children){
			if(child.tag.equals(name)){
				if(rest==null)
					selected.add(child);
				else
					selected.addAll(child.getChildrenByPath(rest));
			}
		}
		return selected;
	}
	private static XMLTag root;
	public static XMLTag getRoot(){
		if(root==null)
			return root = new XMLTag("ROOT");
		else 
			return root;
	}

	public String getContent() {
		return this.content;
	}

	@Override
	public int compareTo(Object o) {
		return this.content.compareTo(((XMLTag) o).content);
	}
	
	public void clear(){
		this.children.clear();
	}
}
