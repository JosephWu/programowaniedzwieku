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

    private double frequency;

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
        frequency = oneSound.getFrequency();

    }

    @Override
    public void paint(Graphics g) {
        int offset = 30;
        Graphics2D g2 = (Graphics2D) g;
        for (int i = 0; i < this.values.size(); i++) {
            for (int j = this.values.get(i).length/2; j < this.values.get(i).length; j++) {
                double color = (((this.values.get(i)[j]-this.minValue) / (this.maxValue-this.minValue))) * 255.0;
                g2.setColor(new Color((int)color, (int)color, (int)color));
                g2.draw(new Line2D.Double(i+offset, j-this.values.get(i).length/2, i+offset, j-this.values.get(i).length/2));
            }
        }
        g2.setColor(new Color(0));
        g2.drawString("czas (próbki)", this.getWidth()-100, this.getHeight()-offset);
        g2.rotate(Math.PI/2.0);
        g2.drawString("częstotliwość", 0+10, 0-5);
        g2.rotate(-Math.PI/2.0);
        g2.drawString(String.valueOf(frequency/2.0), 5, 5);
        

    }

}
