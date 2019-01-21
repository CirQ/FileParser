package me.cirq;

import junit.framework.TestCase;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;

public class CrashStackTest extends TestCase {
    private String filename;

    @Override
    protected void setUp() {
        ProjectPathHandler handler = new ProjectPathHandler();
        String testPath = handler.getProjectTestSourcePath();
        String packagePath = "me.cirq.res".replace(".", File.separator);
        testPath = Paths.get(testPath, packagePath).toString();
        filename = Paths.get(testPath, "simple_stack.txt").toString();
    }

    @Test
    public void testMatch() throws IOException {
        CrashStack cs = new CrashStack(filename);

        assertEquals(cs.getException(), "java.lang.ArrayIndexOutOfBoundsException");
        assertEquals(cs.getMessage(), "32");

        Iterator<SimpleFrame> it = cs.iterator();

        SimpleFrame frame1 = it.next();
        assertEquals(frame1.getQualifiedName(), "me.cirq.subject.ClassD");
        assertEquals(frame1.getLineNumber(), 13);
        SimpleFrame frame2 = it.next();
        assertEquals(frame2.getQualifiedName(), "me.cirq.subject.ClassC");
        assertEquals(frame2.getLineNumber(), 10);
    }

}
