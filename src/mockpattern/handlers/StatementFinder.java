package mockpattern.handlers;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.MethodDeclaration;

public class StatementFinder extends ASTVisitor{
	public boolean visit(MethodDeclaration node) {
		return true;
	}
}
