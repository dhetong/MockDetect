package mockpattern.handlers;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Statement;

public class APILocator extends ASTVisitor {
	private Set<String> charainfo;
	private Set<String> charainfofield;
	
	public APILocator(Set<String> chara, Set<String> charafield) {
		charainfo.addAll(chara);
		charainfofield.addAll(charafield);
	}
	
	public boolean visit(MethodDeclaration node) {
		return super.visit(node);
	}
	
	private void MethodScanner(MethodDeclaration node) {
		Block body = node.getBody();
		
		if(body != null) {
			for(Statement s : (List<Statement>) body.statements()) {
				if(isAPI(s)) {
					
				}
			}
		}
	}
	
	private boolean isAPI(Statement s) {
		boolean flag = false;
		for(String info:charainfo) {
			String tmp[] = info.split("-");
		}
		return flag;
	}
}
