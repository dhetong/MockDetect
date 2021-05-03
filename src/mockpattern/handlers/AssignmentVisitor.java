package mockpattern.handlers;

import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.FieldAccess;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.SimpleName;

public class AssignmentVisitor extends ASTVisitor{
	public String mockvar;
	public String mockclass;
	
	public AssignmentVisitor() {
	}
	
	public boolean visit(Assignment node) {		
		if(node.getRightHandSide().getNodeType() == ASTNode.METHOD_INVOCATION) {
			MethodInvocation m = (MethodInvocation) node.getRightHandSide();
			if(node.getLeftHandSide().getNodeType() == ASTNode.SIMPLE_NAME) {
				SimpleName v = (SimpleName) node.getLeftHandSide();
				mockvar = v.toString();
			}
			else if(node.getLeftHandSide().getNodeType() == ASTNode.FIELD_ACCESS) {
				FieldAccess v = (FieldAccess) node.getLeftHandSide();
				mockvar = v.getName().toString();
			}
			List args = m.arguments();
			mockclass = args.get(0).toString();
		}
		else {
			//TODO:support inline function
//			System.out.println(node.toString());
		}
		return false;
	}
}
