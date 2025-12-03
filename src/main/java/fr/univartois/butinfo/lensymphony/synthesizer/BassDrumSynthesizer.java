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
 * The BassDrumSynthesizer class provides a strategy for synthesizing bass drum sounds.
 * <p>
 * A bass drum produces a deep, low-frequency sound that decays exponentially.
 * The frequency varies linearly from a starting frequency (fstart) to an ending
 * frequency (fend) over the duration defined by the decay rate.
 * <p>
 * The frequency at time t is calculated as:
 * <pre>
 * f(t) = fstart + t · (fend - fstart) / D
 * </pre>
 * where D is the decay rate.
 * <p>
 * The synthesized sound combines exponential decay with a sine wave:
 * <pre>
 * s(t) = V · exp(-decayRate · t) · sin(2π · f(t) · t)
 * </pre>
 * where V is the volume.
 * <p>
 * This implementation follows the Strategy design pattern by implementing
 * the {@link NoteSynthesizer} interface.
 *
 * @author Rabhi Nessim
 *
 * @version 0.1.0
 */
public class BassDrumSynthesizer implements NoteSynthesizer {

    /**
     * The starting frequency of the bass drum sound in Hz (default: 60 Hz).
     */
    private final double startFrequency = 60.0;

    /**
     * The ending frequency of the bass drum sound in Hz (default: 40 Hz).
     */
    private final double endFrequency = 40.0;

    /**
     * The decay rate for the exponential envelope (default: 5.0).
     * A higher value means faster decay (shorter sound).
     */
    private final double decayRate = 5.0;

    private static final BassDrumSynthesizer INSTANCE = new BassDrumSynthesizer();

    /**
     * Creates a new instance of BassDrumSynthesizer with default parameters.
     *
     * @return A new BassDrumSynthesizer instance.
     */
    public static BassDrumSynthesizer getInstance(){
        return INSTANCE;
    }

    /**
     * Creates a new bass drum synthesizer with default parameters.
     * <ul>
     *   <li>Start frequency: 60 Hz</li>
     *   <li>End frequency: 40 Hz</li>
     *   <li>Decay rate: 5.0</li>
     * </ul>
     */
    private BassDrumSynthesizer() {

    }

    /**
     * Creates a new bass drum synthesizer with custom parameters.
     *
     * @param startFrequency The starting frequency in Hz (must be &gt; 0).
     * @param endFrequency   The ending frequency in Hz (must be &gt; 0).
     * @param decayRate      The decay rate for exponential envelope (must be &gt; 0).
     *                       Higher values produce faster decay.
     *
     * @throws IllegalArgumentException If any parameter is not positive.
     */




    /**
     * Synthesizes a bass drum sound for the given note.
     * <p>
     * The frequency is calculated based on the tempo and decay rate, then
     * the sound is generated with exponential decay applied to a sine wave.
     * <p>
     * The synthesis uses the formula:
     * <pre>
     * frequency = fstart + tempo · (fend - fstart) / decayRate
     * s(t) = V · exp(-decayRate · t) · sin(2π · frequency · t)
     * </pre>
     *
     * @param note   The note to synthesize (duration is used, pitch is ignored for bass drum).
     * @param tempo  The tempo in beats per minute (BPM).
     * @param volume The volume level for the note (typically between 0.0 and 1.0).
     *
     * @return An array of audio samples representing the bass drum sound.
     *         Returns an empty array if the calculated frequency is not positive.
     */

    @Override
    public double[] synthesize(Note note, int tempo, double volume) {

        // Calculate duration in seconds
        double duration = note.getDuration(tempo) / 1000.0;
        int nbSample = (int) (duration * SAMPLE_RATE);
        double[] sounds = new double[nbSample];

        // Generate each sample with exponential decay
        for (int i = 0; i < nbSample; i++) {
            // Current time in seconds
            double t = (double) i / SAMPLE_RATE;

            // Calculate variable frequency: f(t) = fstart + t · (fend - fstart) / D
            double frequency = startFrequency + (t * (endFrequency - startFrequency) / duration);

            if (frequency <= 0) {
                 sounds[i] = 0;
                 continue;
            }

            // Calculate exponential decay envelope: exp(-decayRate · t)
            double envelope = Math.exp(-decayRate * t);

            // Calculate signal: s(t) = V · exp(-decayRate·t) · sin(2π · f(t) · t)
            sounds[i] = volume * envelope * Math.sin(2 * Math.PI * frequency * t);
        }

        return sounds;
    }

}