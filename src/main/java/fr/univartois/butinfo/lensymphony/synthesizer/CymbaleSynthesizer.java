package fr.univartois.butinfo.lensymphony.synthesizer;

import fr.univartois.butinfo.lensymphony.notes.Note;
import java.util.Arrays;
import java.util.Random;

public final class CymbaleSynthesizer implements NoteSynthesizer {

    private static final double ATTACK_DEFAULT = 0.01; // seconds
    private static final double DECAY_DEFAULT = 0.2; // seconds

    private final double attack = ATTACK_DEFAULT;
    private final double decay = DECAY_DEFAULT;
    Random random = new Random();

    /**
     * Eager singleton instance.
     */
    private static final CymbaleSynthesizer INSTANCE = new CymbaleSynthesizer();

    /**
     * Private constructor for don't have other instances.
     */
    private CymbaleSynthesizer() {}


    /**
     * Returns the singleton instance of this synthesizer.
     *
     * @return the shared {@code CymbaleSynthesizer} instance
     */
    public static CymbaleSynthesizer getInstance() {
        return INSTANCE;
    }




    /**
     * Computes the envelope at time {@code t} and multiplies it by the supplied {@code volume}.
     *
     * @param t      time in seconds
     * @param volume global volume
     * @return envelope value multiplied by {@code volume}
     */
    public double cymbaleEnvelope(double t,double volume) {
        double newVolume;
        if(t>=0 && t<attack){
            newVolume = t/attack;
        }else if(t>=attack ){
            newVolume = Math.exp((attack - t)/decay);
        }else{
            newVolume =0;
        }
        return volume*newVolume;
    }


    /**
     * Synthesizes the provided {@link Note} into an array of audio samples.
     * <p>
     * The number of samples is determined by {@code note.getDuration(tempo)} and
     * the {@code SAMPLE_RATE} constant defined in {@link NoteSynthesizer}.
     * If the note frequency is zero or negative, an array sized for the duration is returned
     * but left filled with zeros.
     *
     * @param note   the note to synthesize
     * @param tempo  tempo in BPM (used to compute note duration)
     * @param volume overall volume applied to the signal
     * @return an array of audio samples (double values)
     */
    @Override
    public double[] synthesize(Note note, int tempo, double volume) {
        double frequency = note.getFrequency();
        double[] sounds = new double[(int) ((note.getDuration(tempo) / 1000.0) * SAMPLE_RATE)];

        if (frequency <= 0) {
            return new double[0];
        }


        for (int i = 0; i < sounds.length; i++) {
            double t = (double) i / SAMPLE_RATE;
            double envelope = cymbaleEnvelope(t, volume);
            double rdm = (random.nextDouble() * 2.0) - 1.0; // random value between -1.0 and 1.0
            double end = Math.sin(4000.0 * Math.PI * t); // sin(4000πt)

            sounds[i] = volume* envelope * rdm * end;
        }

        return sounds;



    }
}
