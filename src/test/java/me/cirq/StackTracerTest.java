package me.cirq;

import me.cirq.util.ProjectPathHandler;
import org.junit.Test;

import java.io.IOException;

public class StackTracerTest {
    private ProjectPathHandler handler = new ProjectPathHandler();

    @Test
    public void testStackTrace() throws IOException {
        String stackText = handler.getInTestSourcePath("me.cirq.res", "simple_stack.txt");
        StackTracer st = new StackTracer(stackText);
        st.analysis();
    }
}
