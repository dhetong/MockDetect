package mockpattern.handlers;

import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

public class FragmentVisitor extends ASTVisitor{
	public String mockvar;
	public String mockclass;
	
	public FragmentVisitor() {
	}
	
	public boolean visit(VariableDeclarationFragment node) {
		if(node.getInitializer().getNodeType() == ASTNode.METHOD_INVOCATION) {
			MethodInvocation tmp = (MethodInvocation) node.getInitializer();
			if(tmp.getName().toString().contains("mock")) {
				mockvar = node.getName().toString();
				List args = tmp.arguments();
				mockclass = args.get(0).toString();
			}
			else {
				//there exist no example
//				System.out.println(node.toString());
			}
		}
		else {
			//TODO:support inline function
//			System.out.println(node.toString());
		}
		return false;
	}
}