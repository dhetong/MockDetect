package mockpattern.handlers;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Statement;

public class APILocator extends ASTVisitor {
	private Set<String> charainfo = new HashSet<String>();
	private Set<String> charainfofield = new HashSet<String>();
	
	public APILocator(Set<String> chara, Set<String> charafield) {
		for(String tmp:chara) {
			charainfo.add(tmp);
		}
	}
	
	public boolean visit(MethodDeclaration node) {
		return super.visit(node);
	}
	
	private void MethodScanner(MethodDeclaration node) {
		Block body = node.getBody();
		
		if(body != null) {
			for(Statement s : (List<Statement>) body.statements()) {
				for(String info:charainfo) {
					String tmp[] = info.split("-");
					if(isAPI(s,tmp[2])) {
						System.out.println(s.toString());
					}
				}
			}
		}
	}
	
	private boolean isAPI(Statement s, String key_method) {
		boolean flag = false;
		if(s.toString().contains(key_method)) {
			flag = true;
		}
		return flag;
	}
}
