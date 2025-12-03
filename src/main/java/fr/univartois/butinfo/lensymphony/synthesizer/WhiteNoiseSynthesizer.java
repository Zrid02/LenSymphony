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
import java.util.Random;

/**
 * A decorator for {@link NoteSynthesizer} that adds white noise to simulate
 * wind instrument breath sounds.
 * <p>
 * This synthesizer adds a random value between -b and +b to each sample:
 * <pre>
 * s'(t) = s(t) + random(-b, b)
 * </pre>
 * where b is the noise amplitude.
 * <p>
 * The white noise simulates the natural air breath sound characteristic of
 * wind instruments like flutes, clarinets, and saxophones.
 * <p>
 * This decorator extends {@link NoteSynthesizerDecorator} and follows the
 * Decorator design pattern to add effects to an existing synthesizer.
 *
 * @author Romain Wallon
 *
 * @version 0.1.0
 */
public class WhiteNoiseSynthesizer extends NoteSynthesizerDecorator {

    /**
     * The amplitude of the white noise (value b).
     * Random noise will be added in the range [-noiseAmplitude, +noiseAmplitude].
     */
    private final double noiseAmplitude;

    /**
     * Random number generator for producing white noise.
     */
    private final Random random;

    /**
     * Creates a new white noise synthesizer decorator.
     *
     * @param synthesizer     The base synthesizer to decorate (must not be {@code null}).
     * @param noiseAmplitude  The amplitude of the noise (must be &gt;= 0). This defines
     *                        the range of random values added to each sample: [-b, +b].
     *
     * @throws NullPointerException     If {@code synthesizer} is {@code null}.
     * @throws IllegalArgumentException If {@code noiseAmplitude} is negative.
     */
    public WhiteNoiseSynthesizer(NoteSynthesizer synthesizer, double noiseAmplitude) {
        super(synthesizer);
        if (noiseAmplitude < 0) {
            throw new IllegalArgumentException("noiseAmplitude must be >= 0");
        }
        this.noiseAmplitude = noiseAmplitude;
        this.random = new Random();
    }

    /**
     * Synthesizes the given note and adds white noise to simulate breath sound.
     * <p>
     * This method delegates to the wrapped synthesizer to generate the base sound,
     * then adds random noise to each sample using the formula:
     * <pre>
     * s'(t) = s(t) + random(-b, b)
     * </pre>
     * where b is the {@code noiseAmplitude}.
     * <p>
     * The white noise creates a more realistic wind instrument sound by simulating
     * the turbulent air flow through the instrument.
     *
     * @param note   The note to synthesize (must not be {@code null}).
     * @param tempo  The tempo in beats per minute (BPM).
     * @param volume The volume level for the note (typically between 0.0 and 1.0).
     *
     * @return An array of audio samples with white noise added. Each sample is the
     *         base synthesized value plus a random noise component.
     */
    @Override
    public double[] synthesize(Note note, int tempo, double volume) {
        double[] samples = super.synthesize(note, tempo, volume);


        for (int i = 0; i < samples.length; i++) {
            double noise = random.nextDouble(-noiseAmplitude, noiseAmplitude);
            samples[i] = samples[i] + noise;
        }

        return samples;
    }
}