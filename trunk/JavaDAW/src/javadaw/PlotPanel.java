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
public class PlotPanel extends JPanel {

    double[] values;
    private double maxValue;
    private double minValue;

    private double frequency;

    private OneSound oneSound;

    public PlotPanel(double[] values, OneSound oneSound) {
        this.oneSound = oneSound;
        this.values = values;
        this.maxValue = -1;
        this.minValue = 1;
        
        for (int i = 0; i < this.values.length; i++) {
if(values[i]>maxValue)
    maxValue=values[i];
if(values[i]<minValue)
    minValue=values[i];
            }
        

    }

    @Override
    public void paint(Graphics g) {
        int offset = 0;
        int centerLine=250;
        int scale=50;
        Graphics2D g2 = (Graphics2D) g;
        int step=this.values.length/600;
        int j=0;
        for (int i = 0; i < this.values.length; i+=step) {

                //double color = (((this.values.get(i)[j]-this.minValue) / (this.maxValue-this.minValue))) * 255.0;
                g2.setColor(Color.red);
                g2.draw(new Line2D.Double(j+offset, centerLine, j+offset, 250+values[i]*scale));
                j++;
        }
        g2.setColor(new Color(0));
        g2.drawString("czas (próbki)", this.getWidth()-100, this.getHeight()-offset);
        g2.rotate(Math.PI/2.0);
        g2.drawString("częstotliwość", 0+10, 0-5);
        g2.rotate(-Math.PI/2.0);
        g2.drawString(String.valueOf(frequency/2.0), 5, 5);
        

    }

}
