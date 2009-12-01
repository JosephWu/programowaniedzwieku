
package javadaw;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

/**
 *
 * @author pioras
 */
public class WaveDataPanel extends JPanel {

    int[] values;
    private int maxValue;

    private OneSound oneSound;

    BufferedImage scaledImage;
    BufferedImage image;

    int width;
    int height;

    public WaveDataPanel(int[] values, OneSound oneSound) {
        this.oneSound = oneSound;
        this.values = values;
        this.maxValue = 0;
        


        width = 658;
        height = 300;
/**
        scaledImage = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_BINARY);

        int maxNumber = (int) Math.pow(2.0, 8.0*oneSound.getBytesLength()/oneSound.getSamplesLength());
        image = new BufferedImage(oneSound.getSamplesLength(), maxNumber/64, BufferedImage.TYPE_BYTE_BINARY);
        Graphics2D graphics2Dimage = image.createGraphics();
        for (int i = 0; i < values.length-1; i++)
            graphics2Dimage.drawLine(i, (int) Math.log10(values[i]), i+1, (int) Math.log10(values[i]));

        Graphics2D graphics2D = scaledImage.createGraphics();
        //graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
          //      RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        graphics2D.drawImage(image, 0, 0, width, height, null);

        graphics2Dimage.dispose();
        graphics2D.dispose();
   **/
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
            //super.paint(g);
      //  g2.drawImage(scaledImage, 0, 0, this);
            // Draw left channel
            double scale = 0.5 * height / 32768;
            int xPrev = 0, yPrev = 0;
            double center = height / 2;
            for (int x = 0; x < width; x++)
            {
                int y = (int)(center - (values[values.length / width * x] * scale));
                if (x == 0)
                {
                    xPrev = 0;
                    yPrev = y;
                }
                else
                {
                    g2.drawLine(xPrev, yPrev, x, y);
                    xPrev = x;
                    yPrev = y;
                }
            }
    }

}
