package javadaw;

/**
 * Klasa reprezentuje treść zadania tzn. tutaj znajdują się metody generujące
 * dźwięk z zadania pierwszego oraz wokoder.
 */
public class Effects {

    public void getOneSimpleEffect(double freqency, int k, boolean[] on,
            boolean wahwah, double wahwahParam) {
        SoundGenerator sg = new SoundGenerator();
        JavaSound javaSound = new JavaSound();
        javaSound.createSound();
        int[] tmp = {0};
        Mixer mixer = new Mixer();

        if (on[0]) {
            tmp = sg.generateSound(SoundGenerator.SINUS_WAVE,
                (int) (44100 * 2.0), freqency, k);
            on[0] = false;
        }
        else if(on[1]) {
            tmp = sg.generateSound(SoundGenerator.SQUARE_WAVE,
                (int) (44100 * 2.0), freqency, k);
            on[1] = false;
        }
        else if(on[2]) {
            tmp = sg.generateSound(SoundGenerator.TRIANGLE_WAVE,
                (int) (44100 * 2.0), freqency, k);
            on[2] = false;
        }
        else if(on[3]) {
            tmp = sg.generateSound(SoundGenerator.SAWTOOTH_WAVE,
                (int) (44100 * 2.0), freqency, k);
            on[3] = false;
        }
        mixer.putSignal(tmp);


        if(on[1]) {
            tmp = sg.generateSound(SoundGenerator.SQUARE_WAVE,
                (int) (44100 * 2.0), freqency, k);
        }
        if(on[2]) {
            tmp = sg.generateSound(SoundGenerator.TRIANGLE_WAVE,
                (int) (44100 * 2.0), freqency, k);
        }
        if(on[3]) {
            tmp = sg.generateSound(SoundGenerator.SAWTOOTH_WAVE,
                (int) (44100 * 2.0), freqency, k);
        }
        mixer.addSignal(tmp);

        if (wahwah) {
            tmp = this.wahwah(tmp, wahwahParam, freqency/2.0);
        }
        mixer.addSignal(tmp);

        Ampli ampli = new Ampli();
        javaSound.putIntData(ampli.normalizeSingal(mixer.getOutput()));
        javaSound.playSound();
    }

    public void getHelicopter() {
        SoundGenerator sg = new SoundGenerator();
        JavaSound javaSound = new JavaSound();
        javaSound.createSound();
        int[] tmp = {0};
        Mixer mixer1 = new Mixer();
        Mixer mixer2 = new Mixer();
        Mixer mixer = new Mixer();

        int[] noise = sg.generateSound(SoundGenerator.NOISE_WAVE,
                (int) (44100 * 2.0), 5000, 0);
        tmp = Filters.lowPassFillter(noise, 800, 2.0);
        tmp = sg.delaySoundCos(tmp);
        mixer.putSignal(tmp);

        tmp = sg.generateSound(SoundGenerator.NOISE_WAVE,
                (int) (44100 * 2.0), 5000, 0);
        tmp = Filters.lowPassFillter(noise, 800, 2.0);
        tmp = sg.delaySoundSin(tmp);
        mixer.addSignal(tmp);

        //tmp = sg.generateSound(SoundGenerator.SINUS_WAVE,
          //      (int) (44100 * 2.0), 100, 0);
        //mixer1.addSignal(tmp);

        //tmp = sg.generateSound(SoundGenerator.COSSINUS_WAVE,
         //       (int) (44100 * 2.0), 100, 0);
        //tmp = sg.delaySound(tmp, 50);
        //mixer2.addSignal(tmp);

        //mixer.putSignal(mixer1.getOutput());
        //mixer.addSignal(mixer2.getOutput());
        Ampli ampli = new Ampli();
        javaSound.putIntData(ampli.normalizeSingal(mixer.getOutput()));
        javaSound.playSound();
    }

    public void getSimpleEffect() {
        SoundGenerator sg = new SoundGenerator();
        JavaSound javaSound = new JavaSound();
        javaSound.createSound();
        int[] all = {0};

        int[] tmp = sg.generateSound(SoundGenerator.SAWTOOTH_WAVE,
                (int) (44100 * 0.4), 391.9, 55);
        Mixer mixer = new Mixer();
        mixer.putSignal(tmp);
        tmp = sg.generateSound(SoundGenerator.SQUARE_WAVE,
                (int) (44100 * 0.4), 195.95, 55);
        mixer.addSignal(tmp);
        tmp = this.wahwah(sg.generateSound(SoundGenerator.SQUARE_WAVE,
                (int) (44100 * 0.4), 783.8, 55), 2.0, 195.95);
        mixer.addSignal(tmp);
        Ampli ampli = new Ampli();
        all = ampli.normalizeSingal(mixer.getOutput());



        tmp = sg.generateSound(SoundGenerator.SAWTOOTH_WAVE,
                (int) (44100 * 0.4), 329.6, 45);
        mixer = new Mixer();
        mixer.putSignal(tmp);
        tmp = sg.generateSound(SoundGenerator.SQUARE_WAVE,
                (int) (44100 * 0.4), 164.8, 45);
        mixer.addSignal(tmp);
        tmp = this.wahwah(sg.generateSound(SoundGenerator.SQUARE_WAVE,
                (int) (44100 * 0.4), 659.2, 45), 2.0, 164.8);
        mixer.addSignal(tmp);
        ampli = new Ampli();
        tmp = ampli.normalizeSingal(mixer.getOutput());
        all = concat(all, tmp);


        tmp = sg.generateSound(SoundGenerator.SAWTOOTH_WAVE,
                (int) (44100 * 0.4), 329.6, 45);
        mixer = new Mixer();
        mixer.putSignal(tmp);
        tmp = sg.generateSound(SoundGenerator.SQUARE_WAVE,
                (int) (44100 * 0.4), 164.8, 45);
        mixer.addSignal(tmp);
        tmp = this.wahwah(sg.generateSound(SoundGenerator.SQUARE_WAVE,
                (int) (44100 * 0.4), 659.2, 45), 2.0, 164.8);
        mixer.addSignal(tmp);
        ampli = new Ampli();
        tmp = ampli.normalizeSingal(mixer.getOutput());
        all = concat(all, tmp);



        tmp = sg.generateSound(SoundGenerator.SAWTOOTH_WAVE,
                (int) (44100 * 0.4), 349.6, 55);
        mixer = new Mixer();
        mixer.putSignal(tmp);
        tmp = sg.generateSound(SoundGenerator.SQUARE_WAVE,
                (int) (44100 * 0.4), 174.8, 55);
        mixer.addSignal(tmp);
        tmp = this.wahwah(sg.generateSound(SoundGenerator.SQUARE_WAVE,
                (int) (44100 * 0.4), 699.2, 55), 2.0, 174.8);
        mixer.addSignal(tmp);
        ampli = new Ampli();
        tmp = ampli.normalizeSingal(mixer.getOutput());
        all = concat(all, tmp);



        tmp = sg.generateSound(SoundGenerator.SAWTOOTH_WAVE,
                (int) (44100 * 0.4), 293.7, 45);
        mixer = new Mixer();
        mixer.putSignal(tmp);
        tmp = sg.generateSound(SoundGenerator.SQUARE_WAVE,
                (int) (44100 * 0.4), 146.85, 45);
        mixer.addSignal(tmp);
        tmp = this.wahwah(sg.generateSound(SoundGenerator.SQUARE_WAVE,
                (int) (44100 * 0.4), 587.4, 55), 2.0, 146.85);
        mixer.addSignal(tmp);
        ampli = new Ampli();
        tmp = ampli.normalizeSingal(mixer.getOutput());
        all = concat(all, tmp);



        tmp = sg.generateSound(SoundGenerator.SAWTOOTH_WAVE,
                (int) (44100 * 0.4), 293.7, 55);
        mixer = new Mixer();
        mixer.putSignal(tmp);
        tmp = sg.generateSound(SoundGenerator.SQUARE_WAVE,
                (int) (44100 * 0.4), 146.85, 55);
        mixer.addSignal(tmp);
        tmp = this.wahwah(sg.generateSound(SoundGenerator.SQUARE_WAVE,
                (int) (44100 * 0.4), 587.4, 55), 2.0, 146.85);
        mixer.addSignal(tmp);
        ampli = new Ampli();
        tmp = ampli.normalizeSingal(mixer.getOutput());
        all = concat(all, tmp);



        tmp = sg.generateSound(SoundGenerator.SAWTOOTH_WAVE,
                (int) (44100 * 0.2), 261.6, 75);
        mixer = new Mixer();
        mixer.putSignal(tmp);
        tmp = sg.generateSound(SoundGenerator.SQUARE_WAVE,
                (int) (44100 * 0.4), 130.8, 75);
        mixer.addSignal(tmp);
        tmp = this.wahwah(sg.generateSound(SoundGenerator.SQUARE_WAVE,
                (int) (44100 * 0.4), 523.2, 55), 2.0, 130.8);
        mixer.addSignal(tmp);
        ampli = new Ampli();
        tmp = ampli.normalizeSingal(mixer.getOutput());
        all = concat(all, tmp);



        tmp = sg.generateSound(SoundGenerator.SAWTOOTH_WAVE,
                (int) (44100 * 0.2), 329.6, 55);
        mixer = new Mixer();
        mixer.putSignal(tmp);
        tmp = sg.generateSound(SoundGenerator.SQUARE_WAVE,
                (int) (44100 * 0.4), 164.8, 55);
        mixer.addSignal(tmp);
        tmp = this.wahwah(sg.generateSound(SoundGenerator.SQUARE_WAVE,
                (int) (44100 * 0.4), 659.2, 55), 2.0, 164.8);
        mixer.addSignal(tmp);
        ampli = new Ampli();
        tmp = ampli.normalizeSingal(mixer.getOutput());
        all = concat(all, tmp);


        
        tmp = sg.generateSound(SoundGenerator.SAWTOOTH_WAVE,
                (int) (44100 * 0.4), 391.9, 45);
        mixer = new Mixer();
        mixer.putSignal(tmp);
        tmp = sg.generateSound(SoundGenerator.SQUARE_WAVE,
                (int) (44100 * 0.4), 195.95, 45);
        mixer.addSignal(tmp);
        tmp = this.wahwah(sg.generateSound(SoundGenerator.SQUARE_WAVE,
                (int) (44100 * 0.4), 783.8, 55), 2.0, 195.95);
        mixer.addSignal(tmp);
        ampli = new Ampli();
        tmp = ampli.normalizeSingal(mixer.getOutput());
        all = concat(all, tmp);

        //all = this.wahwah(all, 2);

        javaSound.putIntData(all);
        javaSound.playSound();
        //javaSound.stopSound();

    }

    public void getVocoder() {
    }

    int[] concat(int[] a, int[] b) {
        int[] c = new int[a.length+b.length];
        System.arraycopy(a, 0, c, 0, a.length);
        System.arraycopy(b, 0, c, a.length, b.length);
        return c;
    }


    /*
     * lf czestetliwość dla LFO (Low Frequency)
     * lf powinno byćmałe max 20
     */
    public int[] wahwah(int[] x, double lf, double f) {

        double q = 2.0;
        int[] y = new int[x.length];

        double s = Math.sin(Math.PI * 2.0 * f / 44100.0);
        double c = Math.cos(Math.PI * 2.0 * f / 44100.0);
        double alfa = s / (2 * q);
        double r = 1 / (1 + alfa);

        double a0 = 0.5 * (1 + c) * r;
        double a1 = -(1 + c) * r;
        double a2 = a0;
        double b1 = -2.0 * c * r;
        double b2 = (1.0 - alfa) * r;

        //Pierwsze dwa są te same
        y[0] = x[0];
        y[1] = x[1];
        int[] lfo = new SoundGenerator().generateSound(SoundGenerator.SINUS_WAVE, x.length, lf, 1);
        for (int i = 2; i < x.length; i++) {
            //Teraz działanie LFO
            s = Math.sin(Math.PI * 2.0 * f * (Math.abs(lfo[i]/32767.0)) / 44100.0);
            Math.cos(Math.PI * 2.0 * f * (Math.abs(lfo[i]/32767.0)) / 44100.0);
            alfa = s / (2.0 * q);
            r = 1.0 / (1.0 + alfa);
            a0 = 0.5 * (1.0 + c) * r;
            a1 = -(1.0 + c) * r;
            a2 = a0;
            b1 = -2.0 * c * r;
            b2 = (1.0 - alfa) * r;
            //
            y[i] = (int) (a0 * x[i] + a1 * x[i - 1] + a2 * x[i - 2] - b1 * y[i - 1] - b2 * y[i - 2]);
        }
        y[0] = y[2];
        y[1] = y[2];

        return y;
    }

    public int[] tremolo(int[] x, int lf) {
        int[] y = new int[x.length];
        int[] lfo = new SoundGenerator().generateSound(SoundGenerator.SINUS_WAVE, x.length, lf, 1);
        for (int i = 0; i < y.length; i++) {
            y[i] = lfo[i] * x[i] / 32767;
        }
        return y;
    }
}
