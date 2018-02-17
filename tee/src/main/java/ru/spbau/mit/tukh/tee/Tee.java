package ru.spbau.mit.tukh.tee;

import java.io.*;
import java.util.ArrayList;

/**
 * Tee utility implementation.
 * Reads standard input and writes it to both standard output and one or more files.
 */
public class Tee {
    private ArrayList<String> files = new ArrayList<>();
    private boolean flagAppend;

    private static final String USAGE = "tee [ -a | --append] [ --help ] [ File ... ]\n\t-a | --append = append to file\n\t--help=show help and exit";
    static final int BUF_SIZE = 1024;

    /**
     * Main method, which does everything.
     *
     * @param args should content
     *             flag -a to append input to files
     *             flag --help to show help and exit.
     */
    public static void main(String[] args) {
        Tee tee = new Tee();

        try {
            tee.parseArguments(args);
        } catch (ArgumentParseException exception) {
            System.err.println(exception.getMessage());
            System.exit(1);
        }

        try {
            tee.printToFiles();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private void parseArguments(String[] args) throws ArgumentParseException {
        for (String arg : args) {
            if (arg.equals("-a")) {
                flagAppend = true;
            } else if (arg.charAt(0) == '-' && arg.length() > 1) {
                switch (arg) {
                    case "--help":
                        System.out.println(USAGE);
                        System.exit(0);
                    case "--append":
                        flagAppend = true;
                        break;
                    default:
                        throw new ArgumentParseException("Unsupported flag.\nUsage: " + USAGE);
                }
            } else {
                files.add(arg);
            }
        }
    }

    private void printToFiles() throws IOException {
        ArrayList<BufferedWriter> bufferedWriters = new ArrayList<>();

        for (String filename : files) {
            try {
                bufferedWriters.add(new BufferedWriter(new FileWriter(filename, flagAppend)));
            } catch (IOException e) {
                System.err.println("Error opening file " + filename + "\n");
            }
        }

        bufferedWriters.add(new BufferedWriter(new PrintWriter(System.out)));

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        int length;
        char[] buf = new char[BUF_SIZE];

        while ((length = bufferedReader.read(buf)) > 0) {
            for (BufferedWriter bufferedWriter : bufferedWriters) {
                bufferedWriter.write(buf, 0, length);
            }
        }

        for (BufferedWriter bufferedWriter : bufferedWriters) {
            bufferedWriter.close();
        }

        bufferedReader.close();
    }
}
