package mockpattern.handlers;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.MethodInvocation;

public class APINameCheck extends ASTVisitor {
	public boolean isAPI = false;
	String key_name;
	
	public APINameCheck(String m_name) {
		key_name = m_name;
	}
	
	public boolean visit(MethodInvocation node) {
		if(node.getName().toString().equals(key_name)) {
			//the binding is null
//			IMethodBinding minfo = node.resolveMethodBinding();
//			if(minfo == null) {
//				System.out.println("null");
//			}	
			isAPI = true;
		}
		return false;
	}
}
