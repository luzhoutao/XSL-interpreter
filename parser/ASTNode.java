package parser;

import java.util.ArrayList;

import XML_parser.XMLTag;
import visitors.Visitor;

public class ASTNode 
{
	String tag;
	ArrayList<ASTNode> children = new ArrayList<ASTNode>();
	
	private boolean close = false;
	public ASTNode(String tag){
		this.tag = tag;
	}
	
	public void addChild(ASTNode node){
		this.children.add(node);
	}
	
	public void close(){
		this.close = true;
	}
	public boolean isclosed(){
		return this.close;
	}
	// the globally unique root
	private static ASTNode root;
	public static ASTNode getRoot(){
		if(root==null)
			return root = new ASTNode("ROOT");
		else
			return root;
	}

	public void clear(){
		this.children.clear();
	}
	public String getTag() {
		return tag;
	}
	
	public void translate(ArrayList<XMLTag> tags){
		for(ASTNode node: children){
			node.translate(tags);
		}
	}
	
	public void print(){
		System.out.println(tag+children.size());
		for(int i = 0; i<children.size();i++){
			children.get(i).print();
		}
	}
}