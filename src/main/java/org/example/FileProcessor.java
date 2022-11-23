package org.example;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileProcessor {

    private static String RESOURCES_FOLDER_NAME = "/resources";
    private static String INPUT_FOLDER_NAME = "input";
    private static String OUTPUT_FOLDER_NAME = "output";
    private static String DESKEWED_IMAGE_PREFIX = "\\deskewed_";
    private static String SUPPORTED_IMAGE_FORMAT1 = "jpg";
    private static String SUPPORTED_IMAGE_FORMAT2 = "jpeg";
    private static String SUPPORTED_IMAGE_FORMAT3 = "png";
    private static String OUTPUT_IMAGE_FORMAT = "jpg";

    private String pathToIOFolder;
    private File ioFolder;

    public FileProcessor() {
        try {
            System.out.println(FileProcessor.class
                    .getProtectionDomain()
                    .getCodeSource()
                    .getLocation()
                    .toURI()
                    .getPath());
        } catch (URISyntaxException ex) {
            System.out.println("Could not get the executing code's path!");
        }
        this.pathToIOFolder = "C:/Users/Andrija/dev/java/lept4j-test";
        this.pathToIOFolder += RESOURCES_FOLDER_NAME;
    }

    public FileProcessor(String pathToIOFolder) {
        this.pathToIOFolder = pathToIOFolder;
    }

    public void process() {
        ioFolder = new File(pathToIOFolder);
        Map<File, String[]> ioFileMapping = new HashMap<>();

        for (File subFolder : ioFolder.listFiles()) {
            if (subFolder.isDirectory() && subFolder.getName().toLowerCase().trim().equals(INPUT_FOLDER_NAME)) {
                try (Stream<Path> subPaths = Files.walk(Paths.get(subFolder.getAbsolutePath()))) {
                    List<File> inputImages = subPaths
                            .filter(Files::isRegularFile)
                            .map(Path::toFile)
                            .filter(file -> file.getName().contains(SUPPORTED_IMAGE_FORMAT1) ||
                                    file.getName().contains(SUPPORTED_IMAGE_FORMAT2) ||
                                    file.getName().contains(SUPPORTED_IMAGE_FORMAT3))
                            .collect(Collectors.toList());

                    ioFileMapping = createIOFileMapping(inputImages);
                } catch (IOException ex) {
                    System.out.println("Could not read sub folders!");
                }
            }
        }

        if (!ioFileMapping.isEmpty()) {
            deSkewAndOCRImages(ioFileMapping);
        } else {
            System.out.println("No input images were found");
        }
    }

    private Map<File, String[]> createIOFileMapping(List<File> inputImages) {
        Map<File, String[]> ioFileMapping = new HashMap<>();
        String[] outputFileNames = new String[2];

        for (File inputImage : inputImages) {
            String imgName = inputImage.getName();
            String outputDirPath = inputImage.getParent().split(INPUT_FOLDER_NAME)[0] + OUTPUT_FOLDER_NAME;

            outputFileNames[0] = outputDirPath + DESKEWED_IMAGE_PREFIX + imgName;

            String imgNameWoExt = imgName.substring(0, imgName.lastIndexOf('.'));

            outputFileNames[1] = outputDirPath + DESKEWED_IMAGE_PREFIX + imgNameWoExt + ".txt";

            ioFileMapping.put(inputImage, outputFileNames);
        }

        return ioFileMapping;
    }

    private void deSkewAndOCRImages(Map<File, String[]> ioImageMapping) {
        try {
            for (Map.Entry<File, String[]> ioImagePair : ioImageMapping.entrySet()) {
                BufferedImage buffImg = ImageIO.read(ioImagePair.getKey());

                BufferedImage deSkewedImage = Leptonica.deSkewImage(buffImg);

                ImageIO.write(
                        deSkewedImage,
                        OUTPUT_IMAGE_FORMAT,
                        new File(ioImagePair.getValue()[0])
                );

//                readAndSaveTextFile(Tess.extractText(deSkewedImage),
//                        ioImagePair.getValue()[1]);
            }
        } catch (IOException e) {
            System.out.println("Could not read the file as an image");
        }
    }

    private void readAndSaveTextFile(String textToSave, String outputTxtFilePath) {
        if (createTextFile(outputTxtFilePath)) {
            try {
                FileWriter myWriter = new FileWriter(outputTxtFilePath);
                myWriter.write(textToSave);
                myWriter.close();
                System.out.println("Successfully wrote to the file.");
            } catch (IOException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
        }
    }

    private boolean createTextFile(String outputTxtFilePath) {
        try {
            File outputTxtFile = new File(outputTxtFilePath);
            if (outputTxtFile.createNewFile()) {
                System.out.println("File created: " + outputTxtFile.getName());
            } else {
                System.out.println("File already exists.");
            }

            return true;
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();

            return false;
        }
    }
}
