package mockpattern.handlers;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.MethodDeclaration;

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
}
