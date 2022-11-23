package org.example;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import java.awt.image.BufferedImage;

public final class Tess {
    private static final Tesseract tesseract = new Tesseract();

    public static String extractText(BufferedImage image) {
        try {
            return tesseract.doOCR(image);
        } catch (TesseractException e) {
            return e.getMessage();
        }
    }
}
