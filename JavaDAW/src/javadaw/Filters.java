package javadaw;

/**
 * Klasa reprezentuje filtry górno, dolno i pasmowo przepustowe.
 */
public class Filters {

    /**
     *
     * @param x sygnał
     * @param f częstotliwość odcięcia
     * @param q współczynnik rezonansu
     * @return
     */
    public static int[] lowPassFillter(int[] x, double f, double q) {
        int[] y = new int[x.length];
        double s = Math.sin(Math.PI * 2.0 * f / 44100.0);
        double c = Math.cos(Math.PI * 2.0 * f / 44100.0);
        double alfa = s / (2.0 * q);
        double r = 1.0 / (1.0 + alfa);

        double a0 = 0.5 * (1.0 - c) * r;
        double a1 = (1.0 - c) * r;
        double a2 = a0;
        double b1 = -2.0 * c * r;
        double b2 = (1.0 - alfa) * r;

        //Pierwsze dwa są te same
        y[0] = x[0];
        y[1] = x[1];
        for (int i = 2; i < x.length; i++) {
            y[i] = (int) (a0 * x[i] + a1 * x[i - 1] + a2 * x[i - 2] - b1 * y[i - 1] - b2 * y[i - 2]);
        }
        //Pierwsze dwa są te same
        y[0] = y[2];
        y[1] = y[2];
        return y;
    }

    /**
     *
     * @param x sygnał
     * @param f częstotliwość odcięcia
     * @param q współczynnik rezonansu
     * @return
     */
    public static int[] highPassFillter(int[] x, double f, double q) {
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
        y[0] = y[2];
        y[1] = y[2];
        return y;
    }

    /**
     *
     * @param x sygnał
     * @param f częstotliwość odcięcia
     * @param q współczynnik rezonansu
     * @return
     */
    public static int[] passFillter(int[] x, double f, double q) {
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
        y[0] = y[2];
        y[1] = y[2];
        return y;
    }
}
