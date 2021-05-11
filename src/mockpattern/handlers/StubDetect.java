package mockpattern.handlers;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

public class StubDetect extends AbstractHandler {
	List<String> ClassNameMock = new ArrayList<>();
	Set<String> MethodName = new HashSet<String>();	
	
	Set<String> charainfo = new HashSet<String>();
	Set<String> charainfofield = new HashSet<String>();

	@Override
	public Object execute(ExecutionEvent arg0) throws ExecutionException {
		// TODO Auto-generated method stub
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
//		System.out.println(workspace.toString());
		IWorkspaceRoot root = workspace.getRoot();
//		System.out.println(root.getLocation().toString());
		IProject[] projects = root.getProjects();
//		System.out.println(projects.length);
		
		DetectProjects(projects);
		
		System.out.println("DONE DETECTING");
		return null;
	}
	
	private void DetectProjects(IProject[] projects) {
		for(IProject project : projects) {
			System.out.println("DETECTING IN: " + project.getName());
			try {
				ProcessProject(project);
			} catch (CoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		for(IProject project : projects) {
			System.out.println("LOCATING IN: " + project.getName());
			try {
				LocateInProject(project);
			} catch (CoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	private void LocateInProject(IProject project) throws CoreException{
		IJavaProject javaProject = JavaCore.create(project);
		IPackageFragment[] packages = javaProject.getPackageFragments();
		System.out.println(packages.toString());
		
		for (IPackageFragment mypackage : packages) {
			if (mypackage.getKind() == IPackageFragmentRoot.K_SOURCE) {
				for (ICompilationUnit unit : mypackage.getCompilationUnits()) {
					CompilationUnit cunit = ASTBuilder(unit, javaProject);
					APILocator locator = new APILocator(charainfo, charainfofield);
					cunit.accept(locator);
				}
			}
		}
	}
	
	private void ProcessProject(IProject project) throws CoreException{
		IJavaProject javaProject = JavaCore.create(project);
		IPackageFragment[] packages = javaProject.getPackageFragments();
		System.out.println(packages.toString());
				
		for (IPackageFragment mypackage : packages){			
			if (mypackage.getKind() == IPackageFragmentRoot.K_SOURCE){
				for (ICompilationUnit unit : mypackage.getCompilationUnits()){
					CompilationUnit cunit = ASTBuilder(unit, javaProject);
					
					Map<String, String> methodvarmap = new HashMap<>();
					Map<String, String> fieldvarmap = new HashMap<>();
				    
				    MockVisitorMethod mvisitor = new MockVisitorMethod(javaProject);
				    cunit.accept(mvisitor);
				    if(!mvisitor.InfoList.isEmpty()) {
				    	for(String info:mvisitor.InfoList) {
				    		String[] tmp = info.split("-");
				    		String key_tmp = tmp[0] + "-" + tmp[1];
				    		String v_tmp = tmp[2];
				    		
				    		methodvarmap.put(key_tmp, v_tmp);
				    	}
				    }
				    
				    MockVisitorField fvisitor = new MockVisitorField(javaProject);
				    cunit.accept(fvisitor);
				    if(!fvisitor.InfoList.isEmpty()) {
				    	for(String info:fvisitor.InfoList) {
				    		String[] tmp = info.split("-");
				    		String key_tmp = tmp[0];
				    		String v_tmp = tmp[1];
				    		
				    		fieldvarmap.put(key_tmp, v_tmp);
				    	}
				    }
				    
				    MethodVisitor visitor = new MethodVisitor(javaProject);
				    cunit.accept(visitor);
				    if(!visitor.InfoList.isEmpty()) {
				    	for(String info:visitor.InfoList) {
				    		String[] tmp = info.split("-");
				    		String key_1 = tmp[0] + "-" + tmp[1];
				    		String key_2 = tmp[1];
				    		if(methodvarmap.containsKey(key_1)) {
				    			String info_tmp = tmp[0] + "-" + tmp[1] + "-" + tmp[2] + "-" + methodvarmap.get(key_1);
				    			if(!charainfo.contains(info_tmp)) {
				    				charainfo.add(info_tmp);
				    			}
				    		}
				    		else if(fieldvarmap.containsKey(key_2)) {
				    			String info_tmp = tmp[0] + "-" + tmp[1] + "-" + tmp[2] + "-" + fieldvarmap.get(key_2);
				    			if(!charainfofield.contains(info_tmp)) {
				    				charainfofield.add(info_tmp);
				    			}
				    		}
				    	}
				    }
				}
			}
		}
	}
	
	private CompilationUnit ASTBuilder(ICompilationUnit unit, IJavaProject javaProject) throws CoreException {
		ASTParser astParser = ASTParser.newParser(AST.JLS8);
		astParser.setResolveBindings(true);
		astParser.setKind(ASTParser.K_COMPILATION_UNIT);
		astParser.setBindingsRecovery(true);
		@SuppressWarnings("rawtypes")
		Map options = JavaCore.getOptions();
		astParser.setCompilerOptions(options);
		astParser.setSource(unit);
		astParser.setProject(javaProject);
		
		String unitName = "1.java";
		astParser.setUnitName(unitName);

		astParser.setEnvironment(null, null, null, true);
		CompilationUnit result = (CompilationUnit) (astParser.createAST(null));
        return result;
	}
}