package org.example;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.FloatBuffer;
import javax.imageio.ImageIO;
import net.sourceforge.lept4j.Leptonica1;
import net.sourceforge.lept4j.Pix;
import net.sourceforge.lept4j.util.LeptUtils;

public class Main {

  private static int defaultRedSweep = 4;
  private static float sweepRange = 50;
  private static float defaultSweepDelta = 1;
  private static int defaultRedSearch = 2;
  private static int defaultThresh = 130;
  private static FloatBuffer defaultPAngle = null;
  private static FloatBuffer defaultPConf = null;

  public static void main(String[] args) throws IOException {
    BufferedImage image = ImageIO.read(
      new File(
        "C:\\Users\\Andrija\\dev\\java\\lept4j-test\\src\\main\\resources\\skewed.jpg"
      )
    );

    BufferedImage deSkewedImage = deskew(image);

    ImageIO.write(
      deSkewedImage,
      "jpg",
      new File(
        "C:\\Users\\Andrija\\dev\\java\\lept4j-test\\src\\main\\resources\\deskewed.jpg"
      )
    );
  }

  private static BufferedImage deskew(BufferedImage bufferedImage)
    throws IOException {
    Pix pixOriginal = LeptUtils.convertImageToPix(bufferedImage);

    Pix pixDeSkewed = Leptonica1.pixDeskewGeneral(
      pixOriginal,
      defaultRedSweep,
      sweepRange,
      defaultSweepDelta,
      defaultRedSearch,
      defaultThresh,
      defaultPAngle,
      defaultPConf
    );

    int skewAngle = Leptonica1.pixFindSkew(
      pixDeSkewed,
      defaultPAngle,
      defaultPConf
    );

    if (skewAngle != 0) {
      System.out.println("Skew angle is " + skewAngle);

      pixDeSkewed =
        Leptonica1.pixDeskewGeneral(
          pixDeSkewed,
          defaultRedSweep,
          skewAngle + 15,
          defaultSweepDelta,
          defaultRedSearch,
          defaultThresh,
          defaultPAngle,
          defaultPConf
        );
    }

    return LeptUtils.convertPixToImage(pixDeSkewed);
  }
}
