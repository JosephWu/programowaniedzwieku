
package javadaw;

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

    public WaveDataPanel(int[] values, OneSound oneSound) {
        this.oneSound = oneSound;
        this.values = values;
        this.maxValue = 0;

        int width = 658;
        int height = 300;

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
        
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.drawImage(scaledImage, 0, 0, this);
    }

}
