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
		charainfo.addAll(chara);
		charainfofield.addAll(charafield);
	}
	
	public boolean visit(MethodDeclaration node) {
		MethodScanner(node);
		return super.visit(node);
	}
	
	private void MethodScanner(MethodDeclaration node) {
		Block body = node.getBody();
		
		if(body != null) {
			for(Statement s : (List<Statement>) body.statements()) {
				for(String info:charainfo) {
					String tmp[] = info.split("-");
					APINameCheck checker = new APINameCheck(tmp[2]);
					s.accept(checker);
					if(checker.isAPI) {
						System.out.println(tmp[2]);
						System.out.println(s.toString());
					}
				}
			}
		}
	}
}
