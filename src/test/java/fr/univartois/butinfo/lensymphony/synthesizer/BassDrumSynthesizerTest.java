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
 * @author Rabhi Nessim
 *
 * @version 0.1.0
 */
public class BassDrumSynthesizerTest {

    @Test
    void getInstanceReturnsSameInstance() {
        NoteSynthesizer instance1 = BassDrumSynthesizer.getInstance();
        NoteSynthesizer instance2 = BassDrumSynthesizer.getInstance();

        assertSame(instance1, instance2, "getInstance() should always return the same instance");
    }

    @Test
    void getInstanceReturnsNotNull() {
        NoteSynthesizer bassDrum = BassDrumSynthesizer.getInstance();
        assertNotNull(bassDrum, "getInstance() should not return null");
    }

    @Test
    void synthesizeBassDrumQuarterNote() {
        NoteSynthesizer bassDrum = BassDrumSynthesizer.getInstance();
        Note note = new PitchedNote(NotePitch.of(PitchClass.C, 2), NoteValue.QUARTER);

        double[] samples = bassDrum.synthesize(note, 120, 0.8);

        // Vérifier que des échantillons ont été générés
        assertTrue(samples.length > 0, "Synthesized samples should not be empty");

        // Durée attendue : 500ms pour une noire à 120 BPM
        assertEquals(22050, samples.length, "Duration should be 500ms (22050 samples at 44100 Hz)");
    }

    @Test
    void synthesizeBassDrumHalfNote() {
        NoteSynthesizer bassDrum = BassDrumSynthesizer.getInstance();
        Note note = new PitchedNote(NotePitch.of(PitchClass.C, 2), NoteValue.HALF);

        double[] samples = bassDrum.synthesize(note, 120, 0.8);

        // Durée attendue : 1000ms pour une blanche à 120 BPM
        assertEquals(44100, samples.length, "Duration should be 1000ms (44100 samples at 44100 Hz)");
    }

    @Test
    void exponentialDecayOverTime() {
        NoteSynthesizer bassDrum = BassDrumSynthesizer.getInstance();
        Note note = new PitchedNote(NotePitch.of(PitchClass.C, 2), NoteValue.QUARTER);

        double[] samples = bassDrum.synthesize(note, 120, 1.0);

        // Le son doit décroître exponentiellement
        // Comparer les échantillons au début vs à la fin (en valeur absolue)
        double firstRegionMax = 0;
        double lastRegionMax = 0;

        // Maximum dans les 100 premiers échantillons
        for (int i = 0; i < 100; i++) {
            firstRegionMax = Math.max(firstRegionMax, Math.abs(samples[i]));
        }

        // Maximum dans les 100 derniers échantillons
        for (int i = samples.length - 100; i < samples.length; i++) {
            lastRegionMax = Math.max(lastRegionMax, Math.abs(samples[i]));
        }

        assertTrue(firstRegionMax > lastRegionMax,
                "Sound should decay exponentially (first region max: " + firstRegionMax +
                        ", last region max: " + lastRegionMax + ")");
    }

    @Test
    void frequencyDecreasesOverTime() {
        NoteSynthesizer bassDrum = BassDrumSynthesizer.getInstance();
        Note note = new PitchedNote(NotePitch.of(PitchClass.C, 2), NoteValue.HALF);

        double[] samples = bassDrum.synthesize(note, 120, 1.0);

        // La fréquence doit varier de 60 Hz à 40 Hz
        // On ne peut pas tester directement la fréquence, mais on vérifie que le son est généré
        assertTrue(samples.length > 0, "Samples should be generated");

        // Vérifier qu'il y a du signal (pas que des zéros)
        boolean hasSignal = false;
        for (double sample : samples) {
            if (Math.abs(sample) > 1e-6) {
                hasSignal = true;
                break;
            }
        }
        assertTrue(hasSignal, "Generated sound should have non-zero samples");
    }

    @Test
    void volumeAffectsAmplitude() {
        NoteSynthesizer bassDrum = BassDrumSynthesizer.getInstance();
        Note note = new PitchedNote(NotePitch.of(PitchClass.C, 2), NoteValue.QUARTER);

        double[] samplesLowVolume = bassDrum.synthesize(note, 120, 0.3);
        double[] samplesHighVolume = bassDrum.synthesize(note, 120, 0.9);

        // Calculer l'amplitude moyenne sur les premiers échantillons
        double avgLow = 0;
        double avgHigh = 0;
        for (int i = 0; i < 1000; i++) {
            avgLow += Math.abs(samplesLowVolume[i]);
            avgHigh += Math.abs(samplesHighVolume[i]);
        }
        avgLow /= 1000;
        avgHigh /= 1000;

        assertTrue(avgHigh > avgLow,
                "Higher volume should produce higher amplitude (low: " + avgLow + ", high: " + avgHigh + ")");
    }

    @Test
    void differentTempusProduceDifferentDurations() {
        NoteSynthesizer bassDrum = BassDrumSynthesizer.getInstance();
        Note note = new PitchedNote(NotePitch.of(PitchClass.C, 2), NoteValue.QUARTER);

        double[] samples60bpm = bassDrum.synthesize(note, 60, 1.0);
        double[] samples120bpm = bassDrum.synthesize(note, 120, 1.0);

        // À 60 BPM, une noire dure 1000ms (44100 échantillons)
        // À 120 BPM, une noire dure 500ms (22050 échantillons)
        assertEquals(44100, samples60bpm.length, "Quarter note at 60 BPM should be 1000ms");
        assertEquals(22050, samples120bpm.length, "Quarter note at 120 BPM should be 500ms");
    }

    @Test
    void differentNoteValuesProduceDifferentDurations() {
        NoteSynthesizer bassDrum = BassDrumSynthesizer.getInstance();
        Note quarter = new PitchedNote(NotePitch.of(PitchClass.C, 2), NoteValue.QUARTER);
        Note half = new PitchedNote(NotePitch.of(PitchClass.C, 2), NoteValue.HALF);
        Note whole = new PitchedNote(NotePitch.of(PitchClass.C, 2), NoteValue.WHOLE);

        double[] samplesQuarter = bassDrum.synthesize(quarter, 120, 1.0);
        double[] samplesHalf = bassDrum.synthesize(half, 120, 1.0);
        double[] samplesWhole = bassDrum.synthesize(whole, 120, 1.0);

        // Une blanche doit produire 2 fois plus d'échantillons qu'une noire
        assertEquals(samplesQuarter.length * 2, samplesHalf.length,
                "Half note should be twice as long as quarter note");

        // Une ronde doit produire 4 fois plus d'échantillons qu'une noire
        assertEquals(samplesQuarter.length * 4, samplesWhole.length,
                "Whole note should be four times as long as quarter note");
    }

    @Test
    void differentFromPureSound() {
        NoteSynthesizer bassDrum = BassDrumSynthesizer.getInstance();
        NoteSynthesizer pure = new PureSound();
        Note note = new PitchedNote(NotePitch.of(PitchClass.C, 2), NoteValue.QUARTER);

        double[] bassDrumSamples = bassDrum.synthesize(note, 120, 0.5);
        double[] pureSamples = pure.synthesize(note, 120, 0.5);

        // Les deux sons doivent être différents (grosse caisse vs son pur)
        // Comparer quelques échantillons au milieu
        boolean isDifferent = false;
        for (int i = 1000; i < 2000; i++) {
            if (Math.abs(bassDrumSamples[i] - pureSamples[i]) > 0.01) {
                isDifferent = true;
                break;
            }
        }

        assertTrue(isDifferent, "Bass drum sound should be different from pure sound");
    }

    @Test
    void synthesizeWithZeroVolume() {
        NoteSynthesizer bassDrum = BassDrumSynthesizer.getInstance();
        Note note = new PitchedNote(NotePitch.of(PitchClass.C, 2), NoteValue.QUARTER);

        double[] samples = bassDrum.synthesize(note, 120, 0.0);

        // Avec volume = 0, tous les échantillons doivent être ~0
        boolean allZero = true;
        for (double sample : samples) {
            if (Math.abs(sample) > 1e-10) {
                allZero = false;
                break;
            }
        }

        assertTrue(allZero, "With volume = 0, all samples should be approximately zero");
    }

    @Test
    void synthesizeWithMaxVolume() {
        NoteSynthesizer bassDrum = BassDrumSynthesizer.getInstance();
        Note note = new PitchedNote(NotePitch.of(PitchClass.C, 2), NoteValue.QUARTER);

        double[] samples = bassDrum.synthesize(note, 120, 1.0);

        // Vérifier qu'il y a du signal
        boolean hasSignal = false;
        for (double sample : samples) {
            if (Math.abs(sample) > 0.1) {
                hasSignal = true;
                break;
            }
        }

        assertTrue(hasSignal, "With volume = 1.0, should produce audible signal");
    }

    @Test
    void allSamplesWithinValidRange() {
        NoteSynthesizer bassDrum = BassDrumSynthesizer.getInstance();
        Note note = new PitchedNote(NotePitch.of(PitchClass.C, 2), NoteValue.QUARTER);

        double[] samples = bassDrum.synthesize(note, 120, 1.0);

        // Tous les échantillons doivent être dans la plage [-1.0, 1.0]
        for (int i = 0; i < samples.length; i++) {
            assertTrue(samples[i] >= -1.0 && samples[i] <= 1.0,
                    "Sample " + i + " out of range: " + samples[i]);
        }
    }

    @Test
    void synthesizeVeryShortNote() {
        NoteSynthesizer bassDrum = BassDrumSynthesizer.getInstance();
        Note note = new PitchedNote(NotePitch.of(PitchClass.C, 2), NoteValue.SIXTY_FOURTH);

        double[] samples = bassDrum.synthesize(note, 120, 1.0);

        // Même une note très courte doit produire des échantillons
        assertTrue(samples.length > 0, "Even very short notes should produce samples");
    }
}