package mockpattern.handlers;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Statement;

public class MockVisitorField extends ASTVisitor {
	public ProjectAST javaAST;
	Set<String> InfoList = new HashSet<String>();
	
	public MockVisitorField(IJavaProject javaProject) {
		javaAST = new ProjectAST(javaProject);
	}
	
	private void ClassInfo(FieldDeclaration node) {
		Pattern PatternWhen = Pattern.compile("mock\\(");
		Matcher matcher = PatternWhen.matcher(node.toString());
		if(matcher.find()) {
			FragmentVisitor fvisitor = new FragmentVisitor();
			node.accept(fvisitor);
			String tmp = fvisitor.mockvar;
			tmp = tmp + "-" + fvisitor.mockclass;
			if(InfoList.contains(tmp)) {
			}
			else {
				InfoList.add(tmp);
			}
		}
	}
	
	public boolean visit(FieldDeclaration node) {
		ClassInfo(node);
		return super.visit(node);
	}
}
