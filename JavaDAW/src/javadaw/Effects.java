
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
                (int)(44100*0.4), 392, 25);
        Mixer mixer = new Mixer();
        mixer.putSignal(tmp);
        tmp = sg.generateSound(SoundGenerator.SAWTOOTH_WAVE,
                (int)(44100*0.4), 392, 45);
        mixer.addSignal(tmp);
        Ampli ampli = new Ampli();

        javaSound.putIntData(ampli.normalizeSingal(mixer.getOutput()));
        javaSound.playSound();

        tmp = sg.generateSound(SoundGenerator.SQUARE_WAVE,
                (int)(44100*0.4), 330, 5);
        mixer = new Mixer();
        mixer.putSignal(tmp);
        tmp = sg.generateSound(SoundGenerator.SAWTOOTH_WAVE,
                (int)(44100*0.4), 330, 45);
        mixer.addSignal(tmp);
        ampli = new Ampli();
        javaSound.putIntData(ampli.normalizeSingal(tmp));
        javaSound.playSound();

        tmp = sg.generateSound(SoundGenerator.SQUARE_WAVE,
                (int)(44100*0.4), 330, 25);
        mixer = new Mixer();
        mixer.putSignal(tmp);
        tmp = sg.generateSound(SoundGenerator.SAWTOOTH_WAVE,
                (int)(44100*0.4), 330, 45);
        mixer.addSignal(tmp);
        ampli = new Ampli();
        javaSound.putIntData(ampli.normalizeSingal(tmp));
        javaSound.playSound();

        tmp = sg.generateSound(SoundGenerator.SQUARE_WAVE,
                (int)(44100*0.4), 350, 25);
        mixer = new Mixer();
        mixer.putSignal(tmp);
        tmp = sg.generateSound(SoundGenerator.SINUS_WAVE,
                (int)(44100*0.4), 350, 45);
        mixer.addSignal(tmp);
        ampli = new Ampli();
        javaSound.putIntData(ampli.normalizeSingal(tmp));
        javaSound.playSound();

        javaSound.putIntData(sg.generateSound(SoundGenerator.SQUARE_WAVE, 
                (int)(44100*0.4), 294, 15));
        javaSound.playSound();

        javaSound.putIntData(sg.generateSound(SoundGenerator.SQUARE_WAVE, 
                (int)(44100*0.4), 294, 25));
        javaSound.playSound();

        javaSound.putIntData(sg.generateSound(SoundGenerator.SQUARE_WAVE, 
                (int)(44100*0.2), 262, 25));
        javaSound.playSound();

        javaSound.putIntData(sg.generateSound(SoundGenerator.SQUARE_WAVE, 
                (int)(44100*0.2), 330, 25));
        javaSound.playSound();

        javaSound.putIntData(sg.generateSound(SoundGenerator.SQUARE_WAVE, 
                (int)(44100*0.4), 392, 25));
        javaSound.playSound();

    }

    public void getVocoder() {


    }

}
