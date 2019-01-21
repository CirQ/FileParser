package me.cirq;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;

public class FileReader {
    public static String readAllText(String filename) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(filename));
        return new String(bytes, StandardCharsets.UTF_8);
    }

    public static byte[] readAllByte(String filename) throws IOException {
        return Files.readAllBytes(Paths.get(filename));
    }

    public static Iterable<String> readAllLine(String filename) throws IOException {
        File f = new File(filename);
        if(!f.exists())
            throw new FileNotFoundException(f.toString());
        return new LineByLineFileIterable(f);
    }

    private static class LineByLineFileIterable implements Iterable<String> {
        private File file;
        LineByLineFileIterable(File file) {
            this.file = file;
        }

        @Override
        public Iterator<String> iterator() {
            return new LineByLineFileIterator(file);
        }
    }

    private static class LineByLineFileIterator implements Iterator<String>{
        private BufferedReader reader;
        LineByLineFileIterator(File file) {
            try {
                reader = new BufferedReader(new java.io.FileReader(file));
            } catch (FileNotFoundException e){
                e.printStackTrace();
            }
        }

        @Override
        public boolean hasNext() {
            try{
                reader.mark(1);
                if(reader.read() < 0)
                    return false;
                reader.reset();
                return true;
            } catch (IOException e) {
                return false;
            }
        }

        @Override
        public String next() {
            try{
                return reader.readLine();
            } catch (IOException e){
                return null;
            }
        }
    }
}
