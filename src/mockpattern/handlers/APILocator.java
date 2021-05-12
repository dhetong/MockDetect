package mockpattern.handlers;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;

public class APILocator extends ASTVisitor {
	private Set<String> charainfo = new HashSet<String>();
	private Set<String> charainfofield = new HashSet<String>();
	
	public APILocator(Set<String> chara, Set<String> charafield) {
		charainfo.addAll(chara);
		charainfofield.addAll(charafield);
	}
	
	public boolean visit(MethodDeclaration node) {
		MethodScanner(node);
		return super.visit(node);
	}
	
	private void MethodScanner(MethodDeclaration node) {
		Block body = node.getBody();
		
//		HttpClientBuilder builder=HttpClientBuilder.create()
		
		if(body != null) {
			for(Statement s : (List<Statement>) body.statements()) {
				for(String info:charainfo) {
					String tmp[] = info.split("-");
					APINameCheck checker = new APINameCheck(tmp[2]);
					s.accept(checker);
					if(checker.isAPI) {
						if(s.getNodeType() == ASTNode.VARIABLE_DECLARATION_STATEMENT) {
							VarDecHander((VariableDeclarationStatement) s, tmp[3]);
							break;
						}
					}
				}
			}
		}
	}
	
	private void VarDecHander(VariableDeclarationStatement node, String classname) {
		String returntype = ((SimpleType) node.getType()).getName().toString();
		System.out.println(returntype);
	}
}
