package javadaw;

/**
 * Klasa reprezentuje treść zadania tzn. tutaj znajdują się metody generujące
 * dźwięk z zadania pierwszego oraz wokoder.
 */
public class Effects {

    public void getSimpleEffect() {
        SoundGenerator sg = new SoundGenerator();
        JavaSound javaSound = new JavaSound();
        javaSound.createSound();

        int[] tmp = sg.generateSound(SoundGenerator.SQUARE_WAVE,
                (int) (44100 * 0.4), 392, 25);
        Mixer mixer = new Mixer();
        mixer.putSignal(tmp);
        tmp = sg.generateSound(SoundGenerator.SAWTOOTH_WAVE,
                (int) (44100 * 0.4), 392, 45);
        mixer.addSignal(tmp);
        tmp = this.wahwah(sg.generateSound(SoundGenerator.TRIANGLE_WAVE,
                (int) (44100 * 0.4), 392, 45), 10);
        mixer.addSignal(tmp);
        tmp = this.wahwah(sg.generateSound(SoundGenerator.TRIANGLE_WAVE,
                (int) (44100 * 0.4), 392, 45), 10);
        mixer.addSignal(tmp);
        tmp = this.wahwah(sg.generateSound(SoundGenerator.TRIANGLE_WAVE,
                (int) (44100 * 0.4), 392, 45), 10);
        mixer.addSignal(tmp);
        Ampli ampli = new Ampli();

        javaSound.putIntData(ampli.normalizeSingal(mixer.getOutput()));
        javaSound.playSound();

        tmp = sg.generateSound(SoundGenerator.SQUARE_WAVE,
                (int) (44100 * 0.4), 330, 5);
        mixer = new Mixer();
        mixer.putSignal(tmp);
        tmp = sg.generateSound(SoundGenerator.SAWTOOTH_WAVE,
                (int) (44100 * 0.4), 330, 45);
        mixer.addSignal(tmp);
        ampli = new Ampli();
        javaSound.putIntData(ampli.normalizeSingal(mixer.getOutput()));
        javaSound.playSound();

        tmp = sg.generateSound(SoundGenerator.SQUARE_WAVE,
                (int) (44100 * 0.4), 330, 25);
        mixer = new Mixer();
        mixer.putSignal(tmp);
        tmp = this.wahwah(sg.generateSound(SoundGenerator.TRIANGLE_WAVE,
                (int) (44100 * 0.4), 330, 45), 10);
        mixer.addSignal(tmp);
        ampli = new Ampli();
        javaSound.putIntData(ampli.normalizeSingal(mixer.getOutput()));
        javaSound.playSound();

        tmp = sg.generateSound(SoundGenerator.SQUARE_WAVE,
                (int) (44100 * 0.4), 350, 25);
        mixer = new Mixer();
        mixer.putSignal(tmp);
        tmp = sg.generateSound(SoundGenerator.SINUS_WAVE,
                (int) (44100 * 0.4), 350, 45);
        mixer.addSignal(tmp);
        tmp = this.wahwah(sg.generateSound(SoundGenerator.TRIANGLE_WAVE,
                (int) (44100 * 0.4), 392, 45), 10);
        mixer.addSignal(tmp);
        tmp = this.wahwah(sg.generateSound(SoundGenerator.TRIANGLE_WAVE,
                (int) (44100 * 0.4), 392, 45), 10);
        mixer.addSignal(tmp);
        ampli = new Ampli();
        javaSound.putIntData(ampli.normalizeSingal(mixer.getOutput()));
        javaSound.playSound();

        javaSound.putIntData(sg.generateSound(SoundGenerator.SQUARE_WAVE,
                (int) (44100 * 0.4), 294, 15));
        javaSound.playSound();

        javaSound.putIntData(sg.generateSound(SoundGenerator.SQUARE_WAVE,
                (int) (44100 * 0.4), 294, 25));
        javaSound.playSound();

        javaSound.putIntData(sg.generateSound(SoundGenerator.SQUARE_WAVE,
                (int) (44100 * 0.2), 262, 25));
        javaSound.playSound();

        javaSound.putIntData(sg.generateSound(SoundGenerator.SQUARE_WAVE,
                (int) (44100 * 0.2), 330, 25));
        javaSound.playSound();

        javaSound.putIntData(this.wahwah(sg.generateSound(SoundGenerator.TRIANGLE_WAVE,
                (int) (44100 * 0.4), 392, 45), 2));
        javaSound.playSound();

    }

    public void getVocoder() {
    }

    /*
     * lf czestetliwość dla LFO (Low Frequency)
     * lf powinno byćmałe max 20
     */
    public int[] wahwah(int[] x, int lf) {

        int f = 200;
        double q = 2;
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
        int[] lfo = new SoundGenerator().generateSound(SoundGenerator.SINUS_WAVE, x.length, lf, 1);
        for (int i = 2; i < x.length; i++) {
            //Teraz działanie LFO
            s = Math.sin(Math.PI * 2 * f * (Math.abs(lfo[i]/32767.0)) / 44100);
            Math.cos(Math.PI * 2 * f * (Math.abs(lfo[i]/32767.0)) / 44100);
            alfa = s / (2 * q);
            r = 1 / (1 + alfa);
            a0 = 0.5 * (1 + c) * r;
            a1 = -(1 + c) * r;
            a2 = a0;
            b1 = -2 * c * r;
            b2 = (1 - alfa) * r;
            //
            y[i] = (int) (a0 * x[i] + a1 * x[i - 1] + a2 * x[i - 2] - b1 * y[i - 1] - b2 * y[i - 2]);
        }
        return y;
    }
}
