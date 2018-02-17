package ru.spbau.mit.tukh.tee;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TeeTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();
    private String[] arguments;
    private ByteArrayOutputStream outContent;

    private void setupStreamsAndArguments(String input, String[] arguments) {
        outContent = new ByteArrayOutputStream();
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        System.setOut(new PrintStream(outContent));
        this.arguments = arguments;
    }

    @Test
    public void testMainWithoutFiles() {
        String testText = "2 3 9\n239";
        setupStreamsAndArguments(testText, new String[]{"-a"});
        Tee.main(arguments);
        assertEquals(testText, outContent.toString());
    }

    @Test
    public void testMainWithFileAndAppend() throws IOException {
        String testText = "2 3 9\n239\n";
        String filename = "src/test/resources/test.out";
        setupStreamsAndArguments(testText, new String[]{filename});
        Tee.main(arguments);
        assertEquals(testText, outContent.toString());
        assertTrue(compareFileContentWithString(filename, testText));

        setupStreamsAndArguments(testText, new String[]{filename});
        Tee.main(new String[]{"-a", filename});
        assertTrue(compareFileContentWithString(filename, testText + testText));
    }

    private boolean compareFileContentWithString(String filename, String string) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(filename));

        int length;
        char[] buf = new char[Tee.BUF_SIZE];
        StringBuilder stringBuilder = new StringBuilder();

        while ((length = bufferedReader.read(buf)) > 0) {
            for (int i = 0; i < length; i++) {
                stringBuilder.append(buf[i]);
            }
        }

        return string.equals(stringBuilder.toString());
    }
}