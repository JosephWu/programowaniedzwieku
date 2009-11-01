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

    public SpectrogramPanel(ArrayList<double[]> values) {
        this.values = values;
        this.maxValue = 0.0;
        for (int i = 0; i < this.values.size(); i++) {
            for (int j = 0; j < this.values.get(i).length; j++) {
                if (this.maxValue < this.values.get(i)[j]) {
                    this.maxValue = this.values.get(i)[j];
                }
            }
        }
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        for (int i = 0; i < this.values.size(); i++) {
            for (int j = 0; j < this.values.get(i).length; j++) {
                int color = (int) ((this.values.get(i)[j] / this.maxValue) * 255);
                g2.setColor(new Color(color, color, color));
                g2.draw(new Line2D.Double(i, j, i, j));
            }
        }
        //g2.drawString("Maksymalna wartość: " + this.maxValue,
          //      20f, (float) ((this.maxValue + 10.0) / diver + 30.0));

    }

}
