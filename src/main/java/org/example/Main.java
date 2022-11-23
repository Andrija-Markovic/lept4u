package org.example;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        FileProcessor fileProcessor;

        if (args.length == 0) {
            fileProcessor = new FileProcessor();
        } else {
            fileProcessor = new FileProcessor(args[0]);
        }

        fileProcessor.process();
    }
}
