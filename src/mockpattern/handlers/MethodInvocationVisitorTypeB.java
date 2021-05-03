package mockpattern.handlers;

import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.SimpleName;

public class MethodInvocationVisitorTypeB extends ASTVisitor {
	public String varname;
	public String method;
	public String valuereturn;
	
	public String mockvar;
	public String mockmethod;
	public String mockclass;
	
	private int f_type;
	
	public MethodInvocationVisitorTypeB(int type) {
		f_type = type;
	}
	
	public boolean visit(MethodInvocation node) {
		if(f_type == 0) {
			while(true) {
				if(node.getName().toString().equals("when")) {
					List whenarg = node.arguments();
					whenHandler(whenarg);
				
					break;
				}
				else {
					List returnarg = node.arguments();
				}
				node = (MethodInvocation) node.getExpression();
			}
		}
		else if(f_type == 1) {
			while(true) {
				if(node.getName().toString().equals("when")) {
					List whenarg = node.arguments();
					whenHandler(whenarg);
				
					break;
				}
				else {
					List returnarg = node.arguments();
					MethodInvocation mockmethod = (MethodInvocation) returnarg.get(0);
					this.mockclass = mockmethod.arguments().get(0).toString();
				}
				node = (MethodInvocation) node.getExpression();
			}
		}
		
		return false;
	}
	
	private void methodHandler(List args) {
	}
	
	private void whenHandler(List args) {
		if(args.size() > 0) {
			for(int i = 0;i < args.size();i++) {
				if(args.get(i) instanceof MethodInvocation) {
					MethodInvocation node = (MethodInvocation) args.get(i);
					List methodargs = node.arguments();
					if(f_type == 0) {
						this.varname = node.getExpression().toString();
						this.method = node.getName().toString();
					}
					else if(f_type == 1) {
						this.mockvar = node.getExpression().toString();
						this.mockmethod = node.getName().toString();
					}
				}
			}
		}
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
				}
			}
		}
		else {
			System.out.println("no parameter");
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

	private void printList(List arg) {
		if(arg.size() > 0) {
			for(int i = 0;i < arg.size();i++) {
				System.out.println(arg.get(i).toString());
			}
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
