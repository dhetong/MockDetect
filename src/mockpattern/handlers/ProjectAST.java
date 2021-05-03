package mockpattern.handlers;

import java.util.HashSet;

import org.eclipse.core.runtime.*;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.search.IJavaSearchScope;
import org.eclipse.jdt.core.search.SearchEngine;
import org.eclipse.jdt.internal.corext.callhierarchy.MethodWrapper;

public class ProjectAST {
	private IJavaProject javaProject;
	
	public ProjectAST(IJavaProject javaProject){
		this.javaProject = javaProject;
	}
}
