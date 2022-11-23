package org.example;

import net.sourceforge.lept4j.Leptonica1;
import net.sourceforge.lept4j.Pix;
import net.sourceforge.lept4j.util.LeptUtils;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.FloatBuffer;

public final class Leptonica {
    private static int defaultRedSweep = 4;
    private static float sweepRange = 50;
    private static float defaultSweepDelta = 1;
    private static int defaultRedSearch = 2;
    private static int defaultThresh = 130;
    private static FloatBuffer defaultPAngle = null;
    private static FloatBuffer defaultPConf = null;

    public static BufferedImage deSkewImage(BufferedImage bufferedImage) throws IOException {
        final Pix pixOriginal = LeptUtils.convertImageToPix(bufferedImage);

        final Pix pixDeSkewed = Leptonica1.pixDeskewGeneral(
                pixOriginal,
                defaultRedSweep,
                sweepRange,
                defaultSweepDelta,
                defaultRedSearch,
                defaultThresh,
                defaultPAngle,
                defaultPConf
        );

        return LeptUtils.convertPixToImage(pixDeSkewed);
    }
}
