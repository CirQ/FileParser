package me.cirq.util;

import junit.framework.TestCase;
import org.junit.Test;

import java.io.File;

public class ProjectPathHandlerTest extends TestCase {
    private String sep = File.separator;
    private String root = System.getProperty("user.dir");

    private String pathJoin(String... strs){
        return String.join(sep, strs);
    }

    @Test
    public void testDefaultProjectPath(){
        String expected = root;
        String actual = new ProjectPathHandler().getProjectPath();
        assertEquals("default project path", expected, actual);
    }

    @Test
    public void testNamedProjectPath(){
        String path = "foo";
        String expected = pathJoin(root, path);
        String actual = new ProjectPathHandler(path).getProjectPath();
        assertEquals("named project path", expected, actual);
    }

    @Test
    public void testSourcePath(){
        String expected = pathJoin(root, "src", "main", "java");
        String actual = new ProjectPathHandler().getProjectSourcePath();
        assertEquals("source path", expected, actual);
    }

    @Test
    public void testClassPath() {
        String expected = pathJoin(root, "target", "classes");
        String actual = new ProjectPathHandler().getProjectClassPath();
        assertEquals("class path", expected, actual);
    }
}
