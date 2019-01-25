package me.cirq.util;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Unified access to getClass project paths, particularly for Maven projects
 */
public class ProjectPathHandler {
    private static final String MAVEN_SOURCE_PATH = Paths.get("src", "main", "java").toString();
    private static final String MAVEN_CLASS_PATH = Paths.get("target", "classes").toString();
    private static final String MAVEN_TEST_SOURCE_PATH = Paths.get("src", "test", "java").toString();
    private static final String MAVEN_TEST_CLASS_PATH = Paths.get("target", "test-classes").toString();

    private String projectPath;

    public ProjectPathHandler(){
        this(Paths.get("").toString());
    }

    public ProjectPathHandler(String projectPath){
        Path rootPath = Paths.get(projectPath).toAbsolutePath();
        this.projectPath = rootPath.toString();
    }

    public String getProjectPath(){
        return projectPath;
    }

    public String getProjectSourcePath(){
        Path sourcePath = Paths.get(projectPath, MAVEN_SOURCE_PATH);
        return sourcePath.toString();
    }

    public String getProjectClassPath(){
        Path classPath = Paths.get(projectPath, MAVEN_CLASS_PATH);
        return classPath.toString();
    }

    public String getProjectTestSourcePath(){
        Path sourcePath = Paths.get(projectPath, MAVEN_TEST_SOURCE_PATH);
        return sourcePath.toString();
    }

    public String getProjectTestClassPath(){
        Path classPath = Paths.get(projectPath, MAVEN_TEST_CLASS_PATH);
        return classPath.toString();
    }

    public String getInSourcePath(String packageName, String filename){
        String packagePath = packageName.replace(".", File.separator);
        return Paths.get(getProjectSourcePath(), packagePath, filename).toString();
    }

    public String getInClassPath(String packageName, String filename){
        String packagePath = packageName.replace(".", File.separator);
        return Paths.get(getProjectClassPath(), packagePath, filename).toString();
    }

    public String getInTestSourcePath(String packageName, String filename){
        String packagePath = packageName.replace(".", File.separator);
        return Paths.get(getProjectTestSourcePath(), packagePath, filename).toString();
    }

    public String getInTestClassPath(String packageName, String filename){
        String packagePath = packageName.replace(".", File.separator);
        return Paths.get(getProjectTestClassPath(), packagePath, filename).toString();
    }
}
