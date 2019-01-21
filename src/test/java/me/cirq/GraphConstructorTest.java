package me.cirq;

import junit.framework.TestCase;
import org.junit.Ignore;
import org.junit.Test;
import soot.SootMethod;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Iterator;

public class GraphConstructorTest extends TestCase {
    private GraphConstructor grapher = GraphConstructor.getInstance();

    private String getFileName(String filename){
        ProjectPathHandler handler = new ProjectPathHandler();
        String testPath = handler.getProjectTestSourcePath();
        String packagePath = "me.cirq.res".replace(".", File.separator);
        testPath = Paths.get(testPath, packagePath).toString();
        return Paths.get(testPath, filename).toString();
    }

    @Test
    public void testSootClass() {
        String filename = getFileName("simple_stack.txt");
        CrashStack stack = null;
        try {
            stack = new CrashStack(filename);
        } catch (IOException e){
            e.printStackTrace();
            System.exit(-1);
        }
        Iterator<SimpleFrame> it = stack.iterator();
        it.next();
        SimpleFrame frame = it.next();
        SootMethod sm = grapher.get(frame);
        assertEquals("<me.cirq.subject.ClassC: void f12(int)>", sm.getSignature());
    }

    @Ignore
    public void testLargeSoot(){
        String filename = getFileName("hive_stack.txt");
        CrashStack stack = null;
        try {
            stack = new CrashStack(filename);
        } catch (IOException e){
            e.printStackTrace();
            System.exit(-1);
        }
        Iterator<SimpleFrame> it = stack.iterator();
        it.next();
        it.next();
        SimpleFrame frame = it.next();

        String anotherProjectPath = "C:\\Users\\dell\\Desktop\\tencent\\locator\\hadoop\\hadoop-yarn-project\\hadoop-yarn\\hadoop-yarn-server\\hadoop-yarn-server-resourcemanager";
        ProjectPathHandler anotherProject = new ProjectPathHandler(anotherProjectPath);
        GraphConstructor.addSootLoadPath(anotherProject.getProjectClassPath());
        SootMethod sm = grapher.get(frame);

        System.out.println(sm);
    }

}
