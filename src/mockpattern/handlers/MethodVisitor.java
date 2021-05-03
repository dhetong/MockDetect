package mockpattern.handlers;

import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.EnhancedForStatement;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.internal.corext.callhierarchy.MethodWrapper;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.regex.*;

public class MethodVisitor extends ASTVisitor {
	ProjectAST javaAST;
	List<String> ClassName = new ArrayList<>();
	Set<String> MethodName = new HashSet<String>();
	
	Set<String> InfoList = new HashSet<String>();
	//MethodName format: Class||Method	
	
	public MethodVisitor(IJavaProject javaProject){
		javaAST = new ProjectAST(javaProject);
	}
	
	public List<String> GetClassNames() {
		return ClassName;
	}
	
	public void MethodFinder(MethodDeclaration node){
		Block body = node.getBody();

		if(body != null) {
			for(Statement s : (List<Statement>) body.statements()) {
				if(s.getNodeType() == Statement.EXPRESSION_STATEMENT) {
					Pattern PatternWhen = Pattern.compile("when\\(([^}]*)\\)\\.");
					Matcher matcher = PatternWhen.matcher(s.toString());
					if(matcher.find()) {
						if(s.toString().contains("doReturn")) {
							MethodInvocationVisitorTypeA visitor = new MethodInvocationVisitorTypeA(0);
							s.accept(visitor);
							String info = node.getName().toString();
							info = info + "-" + visitor.varname + "-" + visitor.method;
							if(!info.isEmpty()) {
								InfoList.add(info);
							}
						}
						if(s.toString().contains("thenReturn")) {
							MethodInvocationVisitorTypeB visitor = new MethodInvocationVisitorTypeB(0);
							s.accept(visitor);
							String info = node.getName().toString();
							info = info + "-" + visitor.varname + "-" + visitor.method;
							if(!info.isEmpty()) {
								InfoList.add(info);
							}
						}
					}
				}
			}
		}
	}
	
	public boolean visit(MethodDeclaration node) {
		MethodFinder(node);
		return super.visit(node);
	}

}

