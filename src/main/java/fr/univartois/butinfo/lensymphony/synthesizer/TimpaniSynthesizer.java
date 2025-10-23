package fr.univartois.butinfo.lensymphony.synthesizer;

import fr.univartois.butinfo.lensymphony.notes.Note;

public class TimpaniSynthesizer implements NoteSynthesizer{
    private static final TimpaniSynthesizer INSTANCE = new TimpaniSynthesizer();

    /**
     * thus us the constructor of the class
     */
    private TimpaniSynthesizer() {

    }

    /**
     * this method gives the instance of the classe
     * @return the instance of the class
     */
    public static TimpaniSynthesizer getInstance() {
        return INSTANCE;
    }

    /**
     * Computes the audio samples for a given note.
     *
     * @param note   The note to synthesize.
     * @param tempo  The tempo in beats per minute (BPM).
     * @param volume The volume level for the note (0.0 to 1.0).
     * @return An array of audio sample representing the synthesized note.
     */
    @Override
    public double[] synthesize(Note note, int tempo, double volume) {
        double frequency = note.getFrequency();
        double duration = note.getDuration(tempo)/1000;

        int nbSample = (int) (duration*SAMPLE_RATE);

        double[] sounds = new double[nbSample];

        for(int i=0;i<nbSample;i++){
            double t = (double) i/SAMPLE_RATE;
            sounds[i] = frequency+((t*(0.6*frequency-frequency))/duration);
        }

        return sounds;
    }
}
