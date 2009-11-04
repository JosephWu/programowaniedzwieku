/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package javadaw;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import javax.swing.JPanel;

/**
 *
 * @author pioras
 */
public class SpectrogramPanel extends JPanel {

    ArrayList<double[]> values;
    private double maxValue;
    private double minValue;

    private OneSound oneSound;

    public SpectrogramPanel(ArrayList<double[]> values, OneSound oneSound) {
        this.oneSound = oneSound;
        this.values = values;
        this.maxValue = 0.0;
        this.minValue = 0.0;
        
        for (int i = 0; i < this.values.size(); i++) {
            for (int j = 0; j < this.values.get(i).length; j++) {
                double val = this.values.get(i)[j];
                val = Math.log10(val);
                this.values.get(i)[j] = val;
                if (this.maxValue < val) {
                    this.maxValue = val;
                } else if (this.minValue > val)
                    this.minValue = val;
            }
        }
    }

    @Override
    public void paint(Graphics g) {
        int offset = 70;
        Graphics2D g2 = (Graphics2D) g;
        for (int i = 0; i < this.values.size(); i++) {
            for (int j = this.values.get(i).length/2; j < this.values.get(i).length; j++) {
                double color = (((this.values.get(i)[j]-this.minValue) / (this.maxValue-this.minValue))) * 255.0;
                g2.setColor(new Color((int)color, (int)color, (int)color));
                g2.draw(new Line2D.Double(i+offset, j-this.values.get(i).length/2+10, i+offset, j-this.values.get(i).length/2+10));
            }
        }
        g2.setColor(new Color(0));
        g2.drawString("czas (próbki)", this.values.size()+offset-this.values.size()/2, 256+40);
        g2.drawString(String.valueOf(oneSound.getSamplesLength()), this.values.size()+offset, 256+20);
        g2.rotate(Math.PI/2.0);
        g2.drawString("częstotliwość", 0+5, 0-5);
        g2.rotate(-Math.PI/2.0);
        g2.drawString(String.valueOf(oneSound.getFrequency()/2.0), 25, 10);
        g2.drawString(String.valueOf(oneSound.getFrequency()/4.0), 25, 128+10);
        g2.drawString(String.valueOf("0.0"), 25, 256+10);
        

    }

}
