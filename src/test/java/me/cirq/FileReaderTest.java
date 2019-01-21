package me.cirq;

import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Iterator;

public class FileReaderTest extends TestCase {
    private static String[] lines = {"system", "out", "println", "hello_world"};

    private String filename;

    @Override
    protected void setUp() {
        ProjectPathHandler handler = new ProjectPathHandler();
        String testPath = handler.getProjectTestSourcePath();
        String packagePath = "me.cirq.res".replace(".", File.separator);
        testPath = Paths.get(testPath, packagePath).toString();
        filename = Paths.get(testPath, "hello_world.txt").toString();
    }

    @Test
    public void testReadText() throws IOException {
        String text = FileReader.readAllText(filename);
        String realContent = String.join("\n", lines);
        assertEquals(realContent, text);
    }

    @Test
    public void testReadByte() throws IOException {
        byte[] bytes = FileReader.readAllByte(filename);
        byte[] realContent = String.join("\n", lines).getBytes();
        Assert.assertArrayEquals(realContent, bytes);
    }

    @Test
    public void testReadLine() throws IOException {
        Iterator<String> it = FileReader.readAllLine(filename).iterator();
        for(String realContent: lines){
            String str = it.next();
            assertEquals(realContent, str);
        }
    }
}
