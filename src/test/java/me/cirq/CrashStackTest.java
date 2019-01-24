package me.cirq;

import junit.framework.TestCase;
import me.cirq.entity.CrashStack;
import me.cirq.entity.SimpleFrame;
import me.cirq.util.ProjectPathHandler;
import org.junit.Test;

import java.io.IOException;
import java.util.Iterator;

public class CrashStackTest extends TestCase {
    private String filename;

    @Override
    protected void setUp() {
        ProjectPathHandler handler = new ProjectPathHandler();
        filename = handler.getInTestSourcePath("me.cirq.res", "simple_stack.txt");
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
