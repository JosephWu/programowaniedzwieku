
package javadaw;

import java.util.Random;

/**
 *
 */
public class SoundGenerator {

    public static final int SINUS_WAVE = 1;
    public static final int SQUARE_WAVE = 2;
    public static final int SAWTOOTH_WAVE = 3;
    public static final int TRIANGLE_WAVE = 4;
    public static final int NOISE_WAVE = 5;

    public SoundGenerator() {
    }

    /*
     * Funkcja do generowania różnych typów sygnału:
     * @param 1-sinus, 2-prostokąt, 3-piła, 4-trójkąt
     */
    public int[] generateSound(int type, int length, int frequency, int n) {
        int[] val = new int[length];
        double[] tmpVals = new double[length];
        double max = 0;
        Random rand = new Random();
        for (int t = 0; t < length; t++) {
            double tmp = 0;
            switch (type) {
                case SoundGenerator.SINUS_WAVE:
                    tmp = (Math.sin(2.0*Math.PI*(double)t*(double)frequency/44100.0));
                    break;
                case SoundGenerator.SQUARE_WAVE:
                    for (int k = 1; k < n; k++) {
                        tmp += (Math.sin((2.0*k-1)*2.0*Math.PI*(double)frequency*(double)t/44100.0)/(2.0*k-1));
                    }
                    break;
                case SoundGenerator.SAWTOOTH_WAVE:
                    for (int k = 1; k < n; k++) {
                        tmp += ((Math.sin(2.0*(double)k*Math.PI*(double)frequency*(double)t/44100.0))/(double)k);
                    }
                    break;
                case SoundGenerator.TRIANGLE_WAVE:
                    for (int k = 0; k < n; k++) {
                        tmp += ((Math.pow(-1, k)*(Math.sin(2.0*Math.PI*(2.0*k+1)*(double)frequency*(double)t/44100.0))/((2.0*k+1)*(2.0*k+1))));
                    }
                    break;
                case SoundGenerator.NOISE_WAVE:
                    val[t] = rand.nextInt(65535) - 32767;
                    break;
            }
            if (type != SoundGenerator.NOISE_WAVE) {
                tmpVals[t] = tmp;
                if (tmp > max)
                    max = tmp;
            }
        }
        if (type != SoundGenerator.NOISE_WAVE)
            for (int t = 0; t < length; t++) {
                val[t] = (int) (tmpVals[t] / max * 32767.0);
            }
        return val;
    }
}
