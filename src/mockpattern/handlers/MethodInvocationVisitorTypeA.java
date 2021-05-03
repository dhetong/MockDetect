package mockpattern.handlers;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.Statement;

public class MethodInvocationVisitorTypeA extends ASTVisitor {
	public String varname;
	public String method;
	public String valuereturn;
	
	public String mockvar;
	public String mockmethod;
	public String mockclass;
	
	private int f_type;
	
	public MethodInvocationVisitorTypeA(int type) {
		f_type = type;
	}
	
	public boolean visit(MethodInvocation node) {	
		if(f_type == 0) {
			if(node.getName().toString().equals("when")) {
				ExtractorTypeA(node);
			}
			else {
				ExtractorTypeB(node);
			}
		}
		else if(f_type == 1) {
			if(node.getName().toString().equals("when")) {
				MockExtractorTypeA(node);
			}
			else {
				MockExtractorTypeB(node);
			}
		}
		
		return false;
	}
	
	private boolean MockExtractorTypeA(MethodInvocation node) {
		MethodInvocation returnNode = (MethodInvocation) node.getExpression();
		List returnarg = returnNode.arguments();
		MethodInvocation mockmethod = (MethodInvocation) returnarg.get(0);
		this.mockclass = mockmethod.arguments().get(0).toString();
		return true;
	}
	
	private boolean MockExtractorTypeB(MethodInvocation node) {
		MethodInvocation methodNode = node;
		MethodInvocation whenNode = (MethodInvocation) node.getExpression();
		MethodInvocation returnNode = (MethodInvocation) whenNode.getExpression();
		List whenarg = whenNode.arguments();
		List returnarg = returnNode.arguments();
		MethodInvocation mockmethod = (MethodInvocation) returnarg.get(0);
		this.mockclass = mockmethod.arguments().get(0).toString();
		this.mockmethod = methodNode.getName().toString();
		this.mockvar = whenarg.get(0).toString();
		return true;
	}
	
	private boolean ExtractorTypeA(MethodInvocation node) {
		MethodInvocation whenNode = node;
		MethodInvocation returnNode = (MethodInvocation) node.getExpression();
		
		List whenarg = whenNode.arguments();
		List returnarg = returnNode.arguments();

		return true;
	}
	
	private boolean ExtractorTypeB(MethodInvocation node) {
		MethodInvocation methodNode = node;
		MethodInvocation whenNode = (MethodInvocation) node.getExpression();
		MethodInvocation returnNode = (MethodInvocation) whenNode.getExpression();
		
		List whenarg = whenNode.arguments();
		List returnarg = returnNode.arguments();
		List methodarg = methodNode.arguments();
		
//		System.out.println(whenarg.get(0).toString());
//		System.out.println(methodNode.getName().toString());
		
		this.method = methodNode.getName().toString();
		this.varname = whenarg.get(0).toString();
		
		return true;
	}
	
	private void methodHandler(List args) {
	}
	
	private void whenHandler(List args) {
	}
	
	private void returnHandler(List args) {
	}
	
	private void argsHandler(List args) {
		if(args.size() > 0) {
			for(int i = 0;i < args.size();i++) {
				if(args.get(i) instanceof MethodInvocation) {
					MethodInvocation node = (MethodInvocation) args.get(i);
					List methodargs = node.arguments();
					
					System.out.println(node.getName().toString());
					
					if(node.getExpression() != null) {
						System.out.println(node.getExpression().toString());
					}
					
					if(methodargs.size() > 0) {
						for(int j = 0;j < methodargs.size();j++) {
							System.out.println(methodargs.get(j).toString());
						}
					}
					else {
						System.out.println("no parameter");
					}
				}
				else {
					System.out.println(args.get(i).toString());
					if(args.get(i) instanceof SimpleName) {
						IBinding binding = ((SimpleName) args.get(i)).resolveBinding();
						bindingCheck(binding);
					}
				}
			}
		}
		else {
			System.out.println("no parameter:");
		}
		
	}
	
	private void bindingCheck(IBinding binding) {
		System.out.println("type:");
		if(binding == null) {
			System.out.println("null");
		}
		else {
			System.out.println("not null");
		}
	}
	
	public String getvarname() {
		return varname;
	}
	
	public String getmethod() {
		return method;
	}
	
	public String getvaluereturn() {
		return valuereturn;
	}
}
