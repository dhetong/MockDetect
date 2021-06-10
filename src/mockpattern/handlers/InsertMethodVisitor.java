package mockpattern.handlers;

import java.io.File;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jface.text.Document;

public class InsertMethodVisitor extends ASTVisitor{
	private Document document;
	private File file;
	private LogInsert loginsert;
}
