package mockpattern.handlers;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.StringLiteral;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;
import org.eclipse.jdt.core.dom.ParameterizedType;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.SimpleName;

public class APILocator extends ASTVisitor {
	private Set<String> charainfo = new HashSet<String>();
	private Set<String> charainfofield = new HashSet<String>();
	
	public APILocator(Set<String> chara, Set<String> charafield) {
		charainfo.addAll(chara);
		charainfofield.addAll(charafield);
	}
	
	public boolean visit(MethodDeclaration node) {
		MethodScanner(node);
		return super.visit(node);
	}
	
	public void LogInsert(VariableDeclarationStatement node, MethodDeclaration method) {
		if(node.getType().getNodeType() == ASTNode.PRIMITIVE_TYPE) {
			VariableDeclarationFragment frag_tmp = (VariableDeclarationFragment) node.fragments().get(0);
			SimpleName var_tmp = (SimpleName) frag_tmp.getName();
			
			AST ast = node.getAST();
			ASTRewrite rewriter = ASTRewrite.create(ast);
			
			MethodInvocation methodInv = ast.newMethodInvocation();
			
			SimpleName nameSystem = ast.newSimpleName("System");  
	        SimpleName nameOut = ast.newSimpleName("out");  
	        SimpleName namePrintln = ast.newSimpleName("println");
	        
	        QualifiedName nameSystemOut = ast.newQualifiedName(nameSystem, nameOut);
	        methodInv.setExpression(nameSystemOut);  
	        methodInv.setName(namePrintln);
	        
	        SimpleName v_name = ast.newSimpleName(var_tmp.toString());	        
	        methodInv.arguments().add(v_name);
	        ExpressionStatement estatement = ast.newExpressionStatement(methodInv);
	        
	        ListRewrite listRewrite = rewriter.getListRewrite(method.getBody(), Block.STATEMENTS_PROPERTY);
	        listRewrite.insertAfter(estatement, node, null);
		}
	}
	
	private void MethodScanner(MethodDeclaration node) {
		Block body = node.getBody();
		
//		HttpClientBuilder builder=HttpClientBuilder.create()
		
		if(body != null) {
			for(Statement s : (List<Statement>) body.statements()) {
				for(String info:charainfo) {
					String tmp[] = info.split("-");
					APINameCheck checker = new APINameCheck(tmp[2]);
					s.accept(checker);
					if(checker.isAPI) {
						if(s.getNodeType() == ASTNode.VARIABLE_DECLARATION_STATEMENT) {
//							if(VarDecHander((VariableDeclarationStatement) s, tmp[3])) {
//								break;
//							}
							VarDecHander((VariableDeclarationStatement) s, tmp[3]);
							break;
						}
					}
				}
			}
		}
	}
	
	private boolean VarDecHander(VariableDeclarationStatement node, String classname) {
		boolean flag = false;
		//TODO:primitive type
//		long serverCount=util.getHBaseClusterInterface().getClusterMetrics().getLiveServerMetrics().size();
		//TODO:array type
//		byte[] prev=value.getValue(FAMILY_NAME,COLUMN_PREV);
		
//		byte status=(byte)in.read();
		
		if(node.getType().getNodeType() == ASTNode.SIMPLE_TYPE) {
//			VariableDeclarationFragment frag_tmp = (VariableDeclarationFragment) node.fragments().get(0);
//			SimpleName var_tmp = (SimpleName) frag_tmp.getName();
//			ITypeBinding binding_tmp = var_tmp.resolveTypeBinding();
//			System.out.println(binding_tmp);
//			System.out.println(var_tmp.toString());
		}
		else if(node.getType().getNodeType() == ASTNode.PARAMETERIZED_TYPE) {
//			String typename = ((SimpleType)((ParameterizedType) node.getType()).getType()).getName().toString();
//			String argname = ((SimpleType)((ParameterizedType) node.getType()).typeArguments().get(0)).getName().toString();
		}
		else if(node.getType().getNodeType() == ASTNode.PRIMITIVE_TYPE){
			VariableDeclarationFragment frag_tmp = (VariableDeclarationFragment) node.fragments().get(0);
			SimpleName var_tmp = (SimpleName) frag_tmp.getName();
			ITypeBinding binding_tmp = var_tmp.resolveTypeBinding();
			System.out.println(binding_tmp);
			System.out.println(var_tmp.toString());
		}
		else if(node.getType().getNodeType() == ASTNode.ARRAY_TYPE){
		}
		
		return flag;
	}
}
