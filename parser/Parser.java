package parser;

import java.util.ArrayList;
import java.util.Stack;
import java.util.regex.Pattern;

import exception.SyntaxException;
import scanner.*;

public class Parser {
	
	private static Parser instance;
	
	private final Pattern XSL_TAG = Pattern.compile("xsl:.+");
	private final Pattern CLOSE_XSL = Pattern.compile("/");
	private final int ATTR_LEN = 5;
	
	/**
	 * the core function for parser
	 * @param tokens
	 * @return the root of AST
	 * @throws SyntaxException 
	 */
	public ASTNode parse(ArrayList<Token> tokens) throws SyntaxException{
		int ptr = 0, cur_line;
		Stack<ASTNode> p_stack = new Stack<ASTNode>();
		p_stack.add(ASTNode.getRoot()); // the root will stay at the stack forever!
		while(ptr<tokens.size()){
			if(tokens.get(ptr).getContent().equals("<")){
				ptr++;
				cur_line = tokens.get(ptr).getLine_no();
				String first = tokens.get(ptr).getContent();
				String second = tokens.get(ptr+1)==null?null:tokens.get(ptr+1).getContent(); // need 2 lookahead
				// find all tokens that make up this tag
				ArrayList<Token> tag_tokens = new ArrayList<Token>();
				while(ptr<tokens.size()){
					if(tokens.get(ptr).getContent().equals(">"))
						break;
					else if(tokens.get(ptr).getLine_no()!=cur_line)
						throw new SyntaxException(cur_line); // line end before >
					tag_tokens.add(tokens.get(ptr));
					ptr++;
				}
				ptr++;
				// process the tokens found
				if(XSL_TAG.matcher(first).matches()){
					ASTNode cur = parseTag(tag_tokens);
					p_stack.peek().addChild(cur);
					if(!cur.isclosed())
						p_stack.push(cur);
				}else if(CLOSE_XSL.matcher(first).matches() && XSL_TAG.matcher(second).matches()){
					if(tag_tokens.size()!=2)
						throw new SyntaxException(cur_line);
					assert(second!=null);
					if(second.equals(p_stack.peek().getTag())){
						p_stack.pop();
					}else
						throw new SyntaxException(cur_line);
				}else{
					//TODO
					ASTNode cur = new ASTNode(Scanner.getScanner().getCodeByLineNo(cur_line));
					p_stack.peek().addChild(cur);
				}
			}else{
				ASTNode cur = new ASTNode(tokens.get(ptr).getContent());
				p_stack.peek().addChild(cur);
				ptr++;
			}
		}
		return ASTNode.getRoot();
	}
	// start point to <, end point to >
	private ASTNode parseTag(ArrayList<Token> tokens) throws SyntaxException{
		String s = tokens.toString();
		Token first = tokens.get(0);
		int ptr = 1;
		switch(first.getContent()){
			case "xsl:stylesheet":
				StyleSheetNode ssnode = new StyleSheetNode();
				while(ptr<tokens.size()){
					if(tokens.get(ptr).getContent().equals("version")){
						if(tokens.size()-ptr<ATTR_LEN)
							throw new SyntaxException(first.getLine_no());
						match(tokens.get(++ptr), "=");
						match(tokens.get(++ptr),"\"");
						ssnode.setVersion(tokens.get(++ptr).getContent());
						match(tokens.get(++ptr), "\"");
						ptr++;
					}else if(tokens.get(ptr).getContent().equals("xmlns:xsl")){
						if(tokens.size()-ptr<ATTR_LEN)
							throw new SyntaxException(first.getLine_no());
						match(tokens.get(++ptr), "=");
						match(tokens.get(++ptr),"\"");
						ssnode.setURL(tokens.get(++ptr).getContent());
						match(tokens.get(++ptr), "\"");
						ptr++;
					}
				}
				return ssnode;
			case "xsl:template":
				TemplateNode tnode = new TemplateNode();
				if(tokens.get(ptr).getContent().equals("match")){
					if(tokens.size()-ptr<ATTR_LEN)
						throw new SyntaxException(first.getLine_no());
					match(tokens.get(++ptr), "=");
					match(tokens.get(++ptr),"\"");
					tnode.setMatch(tokens.get(++ptr).getContent());
					match(tokens.get(++ptr), "\"");
					ptr++;
				}
				return tnode;
			case "xsl:for-each":
				ForNode fnode = new ForNode();
				if(tokens.get(ptr).getContent().equals("select")){
					if(tokens.size()-ptr<ATTR_LEN)
						throw new SyntaxException(first.getLine_no());
					match(tokens.get(++ptr), "=");
					match(tokens.get(++ptr),"\"");
					fnode.setSelect(tokens.get(++ptr).getContent());
					match(tokens.get(++ptr), "\"");
					ptr++;
				}
				return fnode;
			case "xsl:sort":
				SortNode snode = new SortNode();
				if(tokens.get(ptr).getContent().equals("select")){
					if(tokens.size()-ptr<ATTR_LEN)
						throw new SyntaxException(first.getLine_no());
					match(tokens.get(++ptr), "=");
					match(tokens.get(++ptr),"\"");
					snode.setSelect(tokens.get(++ptr).getContent());
					match(tokens.get(++ptr), "\"");
					ptr++;
				}
				// must be closed
				match(tokens.get(ptr), "/");
				snode.close();
				return snode;
			case "xsl:value-of":
				ValueNode vnode = new ValueNode();
				if(tokens.get(ptr).getContent().equals("select")){
					if(tokens.size()-ptr<ATTR_LEN)
						throw new SyntaxException(first.getLine_no());
					match(tokens.get(++ptr), "=");
					match(tokens.get(++ptr),"\"");
					vnode.setSelect(tokens.get(++ptr).getContent());
					match(tokens.get(++ptr), "\"");
					ptr++;
				}
				// must be closed
				match(tokens.get(ptr), "/");
				vnode.close();
				return vnode;
			case "xsl:if":
				IfNode inode = new IfNode();
				if(tokens.get(ptr).getContent().equals("test")){
					if(tokens.size()-ptr<ATTR_LEN)
						throw new SyntaxException(first.getLine_no());
					match(tokens.get(++ptr), "=");
					match(tokens.get(++ptr),"\"");
					inode.setTest(tokens.get(++ptr).getContent());
					match(tokens.get(++ptr), "\"");
					ptr++;
				}
				return inode;
			case "xsl:text":
				TextNode txtnode = new TextNode();
				if(ptr<tokens.size() && tokens.get(ptr).getContent().equals("disable-output-escaping")){
					if(tokens.size()-ptr<ATTR_LEN)
						throw new SyntaxException(first.getLine_no());
					match(tokens.get(++ptr), "=");
					match(tokens.get(++ptr),"\"");
					txtnode.setDOE(tokens.get(++ptr).getContent());
					match(tokens.get(++ptr), "\"");
					ptr++;
				}
				return txtnode;
			default:
				throw new SyntaxException(first.getLine_no());
		}
	}
	private void match(Token feed, String expected) throws SyntaxException {
		if(!feed.getContent().equals(expected))
			throw new SyntaxException(feed.getLine_no());
	}
	public static Parser getParser(){
		if(instance == null)
			return instance = new Parser();
		else
			return instance;
	}
}