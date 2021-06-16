package mockpattern.handlers;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.text.edits.MalformedTreeException;
import org.eclipse.text.edits.TextEdit;

public class LogInsert {
	private boolean isFirst = true;
	
	public void fileLogInsert(CompilationUnit cunit,Document document,File file, Set<String> charainfo) {
		AST ast = cunit.getAST();
		ASTRewrite rewriter = ASTRewrite.create(ast);
		
		List<MethodDeclaration> methodDeclarations = MethodDeclarationFinder.perform(cunit);
		
		int adj = 1;
		
		for(MethodDeclaration methodDeclaration : methodDeclarations) {
			System.out.println("Method name:\t" + methodDeclaration.getName().getIdentifier());
			Block block = methodDeclaration.getBody();
			if(block == null || block.statements().size()==0)
				continue;
			
			for(int index = 0;index < block.statements().size();index++) {
				Statement s = (Statement) block.statements().get(index);
				if(s.getNodeType() != ASTNode.VARIABLE_DECLARATION_STATEMENT)
					continue;
				for(String info:charainfo) {
					String tmp[] = info.split("-");
					APINameCheck checker = new APINameCheck(tmp[2]);
					s.accept(checker);
					if(checker.isAPI) {
						if(isFirst) {
							isFirst = false;
							
							VariableDeclarationFragment fileFragment = ast.newVariableDeclarationFragment();
							SimpleName v_file = ast.newSimpleName("writeFile");
							
							File writeFile = new File("G:\\FileSave\\dataFile\\write.csv");
						}
						int type = ((VariableDeclarationStatement) s).getType().getNodeType();
						if(type == ASTNode.PRIMITIVE_TYPE) {
							VariableDeclarationFragment frag_tmp = 
									(VariableDeclarationFragment) ((VariableDeclarationStatement)s).fragments().get(0);
							SimpleName var_tmp = (SimpleName) frag_tmp.getName();
							
							MethodInvocation methodInvocation = ast.newMethodInvocation();
							QualifiedName qName = ast.newQualifiedName(ast.newSimpleName("System"), ast.newSimpleName("out"));
							methodInvocation.setExpression(qName);
							methodInvocation.setName(ast.newSimpleName("println"));
							SimpleName v_name = ast.newSimpleName(var_tmp.toString());
							methodInvocation.arguments().add(v_name);
							ExpressionStatement printstatement = ast.newExpressionStatement(methodInvocation);
							
							ListRewrite listRewrite = rewriter.getListRewrite(block, Block.STATEMENTS_PROPERTY);
							listRewrite.insertAt(printstatement, index+adj, null);
							
							adj = adj+1;
							
							break;
						}
					}
				}
			}
		}
		TextEdit edits = rewriter.rewriteAST(document,null);
		try {
			edits.apply(document);
			FileUtils.write(file, document.get());
		} catch (MalformedTreeException | BadLocationException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
