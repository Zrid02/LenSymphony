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

import fr.univartois.butinfo.lensymphony.notes.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link BassDrumSynthesizer}.
 *
 * @author Romain Wallon
 *
 * @version 0.1.0
 */
public class BassDrumSynthesizerTest {

    @Test
    void createBassDrumSynthesizer() {
        NoteSynthesizer bassDrum = new BassDrumSynthesizer();
        assertNotNull(bassDrum);
    }

    @Test
    void createBassDrumSynthesizerWithCustomParameters() {
        NoteSynthesizer bassDrum = new BassDrumSynthesizer(70.0, 35.0, 6.0);
        assertNotNull(bassDrum);
    }

    @Test
    void synthesizeBassDrum() {
        NoteSynthesizer bassDrum = new BassDrumSynthesizer();
        Note note = new PitchedNote(NotePitch.of(PitchClass.C, 2), NoteValue.QUARTER);

        double[] samples = bassDrum.synthesize(note, 120, 0.8);

        // Vérifier que des échantillons ont été générés
        assertTrue(samples.length > 0);

        // Durée attendue : 500ms pour une noire à 120 BPM
        assertEquals(22050, samples.length, "Duration should be 500ms at 44100 Hz");
    }

    @Test
    void exponentialDecay() {
        NoteSynthesizer bassDrum = new BassDrumSynthesizer();
        Note note = new PitchedNote(NotePitch.of(PitchClass.C, 2), NoteValue.QUARTER);

        double[] samples = bassDrum.synthesize(note, 120, 1.0);

        // Le son doit décroître exponentiellement
        // Les premiers échantillons doivent être plus forts que les derniers
        double firstSampleAbs = Math.abs(samples[100]);
        double lastSampleAbs = Math.abs(samples[samples.length - 100]);

        assertTrue(firstSampleAbs > lastSampleAbs,
                "Sound should decay exponentially (first: " + firstSampleAbs + ", last: " + lastSampleAbs + ")");
    }

    @Test
    void differentTemposProduceDifferentFrequencies() {
        NoteSynthesizer bassDrum = new BassDrumSynthesizer(60.0, 40.0, 5.0);
        Note note = new PitchedNote(NotePitch.of(PitchClass.C, 2), NoteValue.QUARTER);

        double[] samples60 = bassDrum.synthesize(note, 60, 1.0);
        double[] samples120 = bassDrum.synthesize(note, 120, 1.0);

        // Avec des tempos différents, les fréquences calculées sont différentes
        // donc les sons doivent être différents
        assertNotEquals(samples60[1000], samples120[1000], 0.01);
    }

    @Test
    void volumeAffectsAmplitude() {
        NoteSynthesizer bassDrum = new BassDrumSynthesizer();
        Note note = new PitchedNote(NotePitch.of(PitchClass.C, 2), NoteValue.QUARTER);

        double[] samplesLow = bassDrum.synthesize(note, 120, 0.5);
        double[] samplesHigh = bassDrum.synthesize(note, 120, 1.0);

        // Le volume élevé doit produire des amplitudes plus grandes
        double avgLow = 0;
        double avgHigh = 0;
        for (int i = 0; i < 100; i++) {
            avgLow += Math.abs(samplesLow[i]);
            avgHigh += Math.abs(samplesHigh[i]);
        }
        avgLow /= 100;
        avgHigh /= 100;

        assertTrue(avgHigh > avgLow, "Higher volume should produce higher amplitude");
    }

    @Test
    void differentDecayRates() {
        NoteSynthesizer slowDecay = new BassDrumSynthesizer(60.0, 40.0, 2.0);
        NoteSynthesizer fastDecay = new BassDrumSynthesizer(60.0, 40.0, 8.0);
        Note note = new PitchedNote(NotePitch.of(PitchClass.C, 2), NoteValue.HALF);

        double[] samplesSlow = slowDecay.synthesize(note, 120, 1.0);  // ✅ CORRIGÉ
        double[] samplesFast = fastDecay.synthesize(note, 120, 1.0);

        // À la fin du son, le decay lent doit avoir plus d'amplitude
        int endIndex = samplesSlow.length - 1000;
        double slowEnd = Math.abs(samplesSlow[endIndex]);
        double fastEnd = Math.abs(samplesFast[endIndex]);

        assertTrue(slowEnd > fastEnd,
                "Slower decay should have more amplitude at the end (slow: " + slowEnd + ", fast: " + fastEnd + ")");
    }

    @Test
    void invalidStartFrequency_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            new BassDrumSynthesizer(-60.0, 40.0, 5.0);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            new BassDrumSynthesizer(0, 40.0, 5.0);
        });
    }

    @Test
    void invalidEndFrequency_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            new BassDrumSynthesizer(60.0, 0, 5.0);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            new BassDrumSynthesizer(60.0, -40.0, 5.0);
        });
    }

    @Test
    void invalidDecayRate_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            new BassDrumSynthesizer(60.0, 40.0, -5.0);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            new BassDrumSynthesizer(60.0, 40.0, 0);
        });
    }

    @Test
    void differentFromPureSound() {
        NoteSynthesizer bassDrum = new BassDrumSynthesizer();
        NoteSynthesizer pure = new PureSound();
        Note note = new PitchedNote(NotePitch.of(PitchClass.C, 2), NoteValue.QUARTER);

        double[] bassDrumSamples = bassDrum.synthesize(note, 120, 0.5);
        double[] pureSamples = pure.synthesize(note, 120, 0.5);

        // Le son de grosse caisse avec decay doit être différent d'un son pur
        assertNotEquals(bassDrumSamples[1000], pureSamples[1000], 0.01);
    }

    @Test
    void longerNotesProduceMoreSamples() {
        NoteSynthesizer bassDrum = new BassDrumSynthesizer();
        Note quarter = new PitchedNote(NotePitch.of(PitchClass.C, 2), NoteValue.QUARTER);
        Note half = new PitchedNote(NotePitch.of(PitchClass.C, 2), NoteValue.HALF);

        double[] samplesQuarter = bassDrum.synthesize(quarter, 120, 1.0);
        double[] samplesHalf = bassDrum.synthesize(half, 120, 1.0);

        // Une blanche doit produire 2 fois plus d'échantillons qu'une noire
        assertEquals(samplesQuarter.length * 2, samplesHalf.length);
    }
}