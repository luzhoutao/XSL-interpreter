package visitors;

import parser.*;

public abstract class Visitor {

	public abstract void visitForNode(ForNode fnode);
	public abstract void visitIfNode(IfNode inode);
	public abstract void visitSortNode(ForNode snode);
	public abstract void visitStyleSheetNode(ForNode ssnode);
	public abstract void visitTemplateNode(ForNode tnode);
	public abstract void visitTextNode(ForNode tnode);
	public abstract void visitValueNode(ForNode vnode);
	
}
