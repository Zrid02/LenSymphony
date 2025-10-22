/**
 * LenSymphony - A simple music synthesizer library developed in Lens, France.
 * Copyright (c) 2025 Romain Wallon - Université d'Artois.
 */

package fr.univartois.butinfo.lensymphony.synthesizer;

import fr.univartois.butinfo.lensymphony.notes.Note;
import java.util.Random;

/**
 * A decorator for {@link NoteSynthesizer} that adds white noise to simulate
 * wind instrument breath sounds.
 * <p>
 * Formula: s'(t) = s(t) + random(-b, b)
 * <p>
 * where b is the noise amplitude.
 */
public class WhiteNoiseSynthesizer extends SynthesizerDecorator {

    private final double noiseAmplitude;
    private final Random random;

    public WhiteNoiseSynthesizer(NoteSynthesizer synthesizer, double noiseAmplitude) {
        super(synthesizer);
        if (noiseAmplitude < 0) {
            throw new IllegalArgumentException("noiseAmplitude must be >= 0");
        }
        this.noiseAmplitude = noiseAmplitude;
        this.random = new Random();
    }

    @Override
    public double[] synthesize(Note note, int tempo, double volume) {
        double[] samples = super.synthesize(note, tempo, volume);

        for (int i = 0; i < samples.length; i++) {
            double noise = (random.nextDouble() * 2 - 1) * noiseAmplitude;
            samples[i] = samples[i] + noise;
        }

        return samples;
    }
}