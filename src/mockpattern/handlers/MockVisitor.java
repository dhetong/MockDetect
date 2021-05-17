package mockpattern.handlers;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Statement;

public class MockVisitor extends ASTVisitor {
	public Set<String> FieldInfoList = new HashSet<String>();
	public Set<String> MethodInfoList = new HashSet<String>();
	
	public MockVisitor() {
	}
	
	private void FieldClassInfo(FieldDeclaration node) {
		Pattern PatternWhen = Pattern.compile("mock\\(");
		Matcher matcher = PatternWhen.matcher(node.toString());
		if(matcher.find()) {
			FragmentVisitor fvisitor = new FragmentVisitor();
			node.accept(fvisitor);
			String tmp = fvisitor.mockvar;
			tmp = tmp + "-" + fvisitor.mockclass;
			if(FieldInfoList.contains(tmp)) {
			}
			else {
				FieldInfoList.add(tmp);
			}
		}
	}
	
	private void StatementHandler(Statement s) {
		if(s.getNodeType() == Statement.EXPRESSION_STATEMENT) {
			System.out.println(s.toString());
//			FragmentVisitor fvisitor = new FragmentVisitor();
//			s.accept(fvisitor);
		}
	}
	
	private void MethodClassInfo(MethodDeclaration node) {
		Block body = node.getBody();
		
		if(body != null) {
			Set<String> mockinfo = new HashSet<String>();
			
			for(Statement s : (List<Statement>) body.statements()) {
				if(s.getNodeType() == Statement.EXPRESSION_STATEMENT || s.getNodeType() == Statement.VARIABLE_DECLARATION_STATEMENT) {
					Pattern PatternMock = Pattern.compile("mock\\(");
					Matcher matcher = PatternMock.matcher(s.toString());
					if(matcher.find()) {
						if(s.getNodeType() == Statement.VARIABLE_DECLARATION_STATEMENT) {
							FragmentVisitor fvisitor = new FragmentVisitor();
							s.accept(fvisitor);
							if(fvisitor.mockvar != null && fvisitor.mockclass != null) {
								String tmp = node.getName().toString();
								tmp = tmp + "-" + fvisitor.mockvar;
								tmp = tmp + "-" + fvisitor.mockclass;
								if(MethodInfoList.contains(tmp)) {
								}
								else {
									MethodInfoList.add(tmp);
								}
							}
						}
						if(s.getNodeType() == Statement.EXPRESSION_STATEMENT) {
							if(((ExpressionStatement) s).getExpression().getNodeType() == Statement.ASSIGNMENT) {
								AssignmentVisitor avisitor = new AssignmentVisitor();
								s.accept(avisitor);
								if(avisitor.mockvar != null && avisitor.mockclass != null) {
									String tmp = node.getName().toString();
									tmp = tmp + "-" + avisitor.mockvar;
									tmp = tmp + "-" + avisitor.mockclass;
									if(MethodInfoList.contains(tmp)) {
									}
									else {
										MethodInfoList.add(tmp);
									}
								}
							}
							else {
								if(s.toString().contains("doReturn")) {
//									MethodInvocationVisitorTypeA mvisitor = new MethodInvocationVisitorTypeA(1);
//									s.accept(mvisitor);
//									mockinfo.add(mvisitor.mockvar + "-" + mvisitor.mockmethod + "-" + mvisitor.mockclass);
								}
								else if(s.toString().contains("thenReturn")) {
//									MethodInvocationVisitorTypeB mvisitor = new MethodInvocationVisitorTypeB(1);
//									s.accept(mvisitor);
//									mockinfo.add(mvisitor.mockvar + "-" + mvisitor.mockmethod + "-" + mvisitor.mockclass);
								}
								else {
									//ignore the mock object which are used in expression with other APIs
									//TODO: extract mock object from inline method
								}
							}
						}
					}
					
					Pattern PatternSpy = Pattern.compile("spy\\(");
					Matcher matcherspy = PatternSpy.matcher(s.toString());
					if(matcherspy.find()) {
					}
				}
			}
			//search for mock objects created by mocking pattern
//			if(!mockinfo.isEmpty()) {
//				for(Statement s : (List<Statement>) body.statements()) {
//					for(String info : mockinfo) {
//						String[] list = info.split("-");
//						if(s.toString().contains(list[0]) && s.toString().contains(list[1])) {
//							System.out.println(s.toString());
//							break;
//						}
//					}
//				}
//			}
		}
	}
	
	public boolean visit(FieldDeclaration node) {
		FieldClassInfo(node);
		return super.visit(node);
	}
	
	public boolean visit(MethodDeclaration node) {
		MethodClassInfo(node);
		return super.visit(node);
	}
}
