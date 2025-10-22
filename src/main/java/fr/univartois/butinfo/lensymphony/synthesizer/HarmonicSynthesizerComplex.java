package fr.univartois.butinfo.lensymphony.synthesizer;


import fr.univartois.butinfo.lensymphony.notes.Note;

import java.util.function.BiFunction;
import java.util.function.IntUnaryOperator;

public class HarmonicSynthesizerComplex extends NoteSynthesizerDecorator{

    /**
     * The number of harmonics to generate (including the fundamental frequency).
     */


    private final int numberOfHarmonics;


    /**
     * The function to determine harmonic index based on harmonic ordinal.
     */
    private final IntUnaryOperator h; // function for harmonic index


    /**
     * The function to determine amplitude scaling based on harmonic ordinal and time.
     */
    private final BiFunction<Integer, Double, Double> a; // function for amplitude scaling


    /**
        * Creates a new harmonic synthesizer decorator with custom harmonic index and amplitude functions.
     * @param synthesizer the underlying synthesizer to decorate
     * @param numberOfHarmonics the number of harmonics to generate
     * @param h function to obtain the harmonic index
     * @param a function to obtain the amplitude of a harmonic given its ordinal and time
     * @throws IllegalArgumentException if {@code numberOfHarmonics < 1}
     */
    public HarmonicSynthesizerComplex(NoteSynthesizer synthesizer,int numberOfHarmonics, IntUnaryOperator h, BiFunction<Integer, Double, Double> a) {
        super(synthesizer);
        if (numberOfHarmonics < 1) {
            throw new IllegalArgumentException("numberOfHarmonics must be >= 1");
        }
        this.numberOfHarmonics = numberOfHarmonics;
        this.h = h;
        this.a = a;
    }


    /**
     * Synthesizes the given note with harmonics to create a richer sound.
     * @param note   The note to synthesize.
     * @param tempo  The tempo in beats per minute (BPM).
     * @param volume The volume level for the note (0.0 to 1.0).
     *
     * @return
     */
    @Override
    public double[] synthesize(Note note, int tempo, double volume) {
        double frequency = note.getFrequency();

        if (frequency <= 0) {
            return new double[0];
        }

        double duration = note.getDuration(tempo) / 1000.0;
        int nbSample = (int) (duration * SAMPLE_RATE);
        double[] sounds = super.synthesize(note, tempo, volume);


        for (int i = 0; i < nbSample; i++) {
            double t = (double) i / SAMPLE_RATE;
            double value = 0.0;

            for (int harmonic = 1; harmonic <= numberOfHarmonics; harmonic++) {
                int indexHarmonic = h.applyAsInt(harmonic); // get harmonic index using function h
                double amplitude = a.apply(harmonic, t); // get amplitude using function a
                value += amplitude * Math.sin(2 * Math.PI * indexHarmonic * frequency * t);

            }

            sounds[i] = (1. / numberOfHarmonics) * value;
        }

        return sounds;
    }





}
