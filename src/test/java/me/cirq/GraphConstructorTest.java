package me.cirq;

import junit.framework.TestCase;
import me.cirq.entity.CrashStack;
import me.cirq.entity.SimpleFrame;
import me.cirq.util.ProjectPathHandler;
import org.junit.Ignore;
import org.junit.Test;
import soot.SootMethod;

import java.io.IOException;
import java.util.Iterator;

public class GraphConstructorTest extends TestCase {
    private GraphConstructor grapher = GraphConstructor.getInstance();

    private String getFileName(String filename){
        ProjectPathHandler handler = new ProjectPathHandler();
        return handler.getInTestSourcePath("me.cirq.res", filename);
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
