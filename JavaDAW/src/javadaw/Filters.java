/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package javadaw;

/**
 *
 * @author Stach
 */
public class Filters {

    public int[] lowPassFillter(int[] x, double f, double q) {
        int[] y = new int[x.length];
        double s = Math.sin(Math.PI * 2 * f / 44100);
        double c = Math.cos(Math.PI * 2 * f / 44100);
        double alfa = s / (2 * q);
        double r = 1 / (1 + alfa);

        double a0 = 0.5 * (1 - c) * r;
        double a1 = (1 - c) * r;
        double a2 = a0;
        double b1 = -2 * c * r;
        double b2 = (1 - alfa) * r;

        //Pierwsze dwa są te same
        y[0] = x[0];
        y[1] = x[1];
        for (int i = 2; i < x.length; i++) {
            y[i] = (int) (a0 * x[i] + a1 * x[i - 1] + a2 * x[i - 2] - b1 * y[i - 1] - b2 * y[i - 2]);
        }

        return y;
    }

    public int[] highPassFillter(int[] x, double f, double q) {
        int[] y = new int[x.length];
        double s = Math.sin(Math.PI * 2 * f / 44100);
        double c = Math.cos(Math.PI * 2 * f / 44100);
        double alfa = s / (2 * q);
        double r = 1 / (1 + alfa);

        double a0 = 0.5 * (1 + c) * r;
        double a1 = -(1 + c) * r;
        double a2 = a0;
        double b1 = -2 * c * r;
        double b2 = (1 - alfa) * r;

        //Pierwsze dwa są te same
        y[0] = x[0];
        y[1] = x[1];
        for (int i = 2; i < x.length; i++) {
            y[i] = (int) (a0 * x[i] + a1 * x[i - 1] + a2 * x[i - 2] - b1 * y[i - 1] - b2 * y[i - 2]);
        }

        return y;
    }

    public int[] passFillter(int[] x, double f, double q) {
        int[] y = new int[x.length];
        double s = Math.sin(Math.PI * 2 * f / 44100);
        double c = Math.cos(Math.PI * 2 * f / 44100);
        double alfa = s / (2 * q);
        double r = 1 / (1 + alfa);

        double a0 = alfa * r;
        double a2 = -a0;
        double b1 = -2 * c * r;
        double b2 = (1 - alfa) * r;

        //Pierwsze dwa są te same
        y[0] = x[0];
        y[1] = x[1];
        for (int i = 2; i < x.length; i++) {
            y[i] = (int) (a0 * x[i] + a2 * x[i - 2] - b1 * y[i - 1] - b2 * y[i - 2]);
        }

        return y;
    }
}
