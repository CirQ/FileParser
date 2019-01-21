package me.cirq;

import soot.Scene;
import soot.SootClass;
import soot.options.Options;

import java.io.File;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class ProjectFileHandler implements Config, Iterable<SootClass> {
    private static final String SOURCE_SUFFIX = ".java";
    private static final String CLASS_SUFFIX = ".class";

    static {
        Options.v().set_keep_line_number(true);
        String sootClassPath = Scene.v().getSootClassPath() + ";" + PATH_HANDLER.getProjectClassPath();
        Scene.v().setSootClassPath(sootClassPath);
    }

    private String packageName;
    private List<String> packageFileFrontier;

    public ProjectFileHandler(String packageName){
        initPackageFrontier(packageName);
    }

    private void initPackageFrontier(String packageName){
        this.packageName = packageName;
        packageFileFrontier = new LinkedList<>();

        // todo: to iterable
    }

    public static File getSourceFile(String fullname) {
        if(false){
            // todo: regex detect, fullname: ^((\w+)\.)*(\w+)$
            return null;
        }

        String filename = fullname.replaceAll("\\.", File.separator) + SOURCE_SUFFIX;
        String filepath = PATH_HANDLER.getProjectSourcePath() + filename;
        return new File(filepath);
    }

    public static File getClassFile(String fullname) {
        String filename = fullname.replaceAll("\\.", File.separator) + CLASS_SUFFIX;
        String filepath = PATH_HANDLER.getProjectClassPath() + filename;
        return new File(filepath);
    }

    public static SootClass getSootClass(String fullname) {
        SootClass sc = Scene.v().getSootClass(fullname);
        Scene.v().loadNecessaryClasses();
        return sc;
    }

    @Override
    public Iterator<SootClass> iterator() {
        return new Iterator<SootClass>(){
            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public SootClass next() {
                return null;
            }
        };
    }

}
