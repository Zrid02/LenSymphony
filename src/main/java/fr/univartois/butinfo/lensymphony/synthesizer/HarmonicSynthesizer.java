/**
 * LenSymphony - A simple music synthesizer library developed in Lens, France.
 * Copyright (c) 2025 Romain Wallon - Université d'Artois.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to
 * deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or
 * sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE
 * USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package fr.univartois.butinfo.lensymphony.synthesizer;

import fr.univartois.butinfo.lensymphony.notes.Note;

/**
 * A decorator for {@link NoteSynthesizer} that adds harmonics to enrich the sound.
 * <p>
 * This synthesizer applies the formula with n harmonics:
 * <pre>
 * s(t) = (V/n) * Σ(i=1 to n) sin(2π * i * f * t) / √i
 * </pre>
 * where:
 * <ul>
 *   <li>V is the volume</li>
 *   <li>n is the number of harmonics</li>
 *   <li>f is the fundamental frequency</li>
 *   <li>t is the time</li>
 * </ul>
 * <p>
 * Adding harmonics produces a richer, more natural sound compared to a pure sine wave.
 * The more harmonics included, the more complex and instrument-like the tone becomes.
 *
 * @author Romain Wallon
 *
 * @version 0.1.0
 */
public class HarmonicSynthesizer extends NoteSynthesizerDecorator {

    /**
     * The number of harmonics to generate (including the fundamental frequency).
     */
    private final int numberOfHarmonics;

    /**
     * Creates a new harmonic synthesizer decorator.
     *
     * @param synthesizer       The base synthesizer to decorate (must not be {@code null}).
     * @param numberOfHarmonics The number of harmonics to include (must be &gt;= 1).
     *
     * @throws NullPointerException     If {@code synthesizer} is {@code null}.
     * @throws IllegalArgumentException If {@code numberOfHarmonics} is less than 1.
     */
    public HarmonicSynthesizer(NoteSynthesizer synthesizer, int numberOfHarmonics) {
        super(synthesizer);
        if (numberOfHarmonics < 1) {
            throw new IllegalArgumentException("numberOfHarmonics must be >= 1");
        }
        this.numberOfHarmonics = numberOfHarmonics;
    }

    /**
     * Synthesizes the given note with harmonics to create a richer sound.
     * <p>
     * If the note's frequency is 0 or negative (representing silence), delegates to
     * the base synthesizer. Otherwise, applies the harmonic synthesis formula.
     *
     * @param note   The note to synthesize.
     * @param tempo  The tempo in beats per minute (BPM).
     * @param volume The volume level for the note (0.0 to 1.0).
     *
     * @return An array of audio samples with harmonics applied.
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

            for (int harmonic = 2; harmonic <= numberOfHarmonics; harmonic++) {
                value += Math.sin(2 * Math.PI * harmonic * frequency * t) / Math.sqrt(harmonic);
            }

            sounds[i] = (volume / numberOfHarmonics) * value;
        }

        return sounds;
    }
}


