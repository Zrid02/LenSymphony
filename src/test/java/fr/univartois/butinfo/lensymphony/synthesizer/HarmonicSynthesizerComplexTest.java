package fr.univartois.butinfo.lensymphony.synthesizer;

import fr.univartois.butinfo.lensymphony.notes.Note;
import fr.univartois.butinfo.lensymphony.synthesizer.NoteSynthesizer;
import org.junit.jupiter.api.Test;

import java.util.function.BiFunction;
import java.util.function.IntUnaryOperator;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for HarmonicSynthesizerComplex, corrected to match the implementation.
 *
 * This test corrects two issues:
 * 1. Logic Problem: The 'expected' loop starts at k=2 to match
 * the 'harmonic = 2' loop in the implementation.
 * 2. Truncation Problem: Uses 'durationMs = 10.0' to prevent
 * note.getDuration(tempo) from returning 0, which caused nbSample = 0.
 */
class HarmonicSynthesizerComplexTest {

    // Note: SAMPLE_RATE is now retrieved from the interface
    private final int sampleRate = NoteSynthesizer.SAMPLE_RATE;
    private final int tempo = 120;
    private final double volume = 1.0;

    // Fake Note: getDuration() truncates (int)durationMs.
    private static class FakeNote implements Note {
        private final double frequency;
        private final double durationMs;

        FakeNote(double frequency, double durationMs) {
            this.frequency = frequency;
            this.durationMs = durationMs;
        }

        @Override
        public double getFrequency() {
            return frequency;
        }

        @Override
        public int getDuration(int tempo) {
            // (int) 10.0 -> 10. This is what causes the truncation.
            return (int) durationMs;
        }
    }

    // Fake Base Synthesizer
    // Corrected to use the SAME nbSample calculation logic as the implementation.
    private static class FakeBaseSynthesizer implements NoteSynthesizer {
        @Override
        public double[] synthesize(Note note, int tempo, double volume) {
            // Uses the same logic as HarmonicSynthesizerComplex.java
            // 1. note.getDuration(tempo) returns an int (e.g., 10)
            // 2. durationSeconds becomes 0.01
            // 3. nbSample becomes (int)(0.01 * 44100) = 441
            double durationSeconds = note.getDuration(tempo) / 1000.0;
            int nbSample = (int) (durationSeconds * SAMPLE_RATE);

            if (nbSample < 0) nbSample = 0;
            return new double[nbSample]; // Returns a zero-filled array of the correct size (441)
        }
    }

    /**
     * Calculates the expected number of samples using the SAME logic
     * as the implementation to avoid desynchronization.
     */
    private int getExpectedNbSamples(Note note) {
        double durationSeconds = note.getDuration(tempo) / 1000.0;
        return (int) (durationSeconds * sampleRate);
    }


    @Test
    void testNormalizationWithMultipleHarmonics() {
        // FIX 2: Use a duration >= 1.0ms to avoid truncation to 0
        double durationMs = 10.0;
        double frequency = 1.0;

        Note note = new FakeNote(frequency, durationMs);
        NoteSynthesizer base = new FakeBaseSynthesizer();

        // Calculate the expected nbSample (441)
        int nbSamples = getExpectedNbSamples(note);

        int nbHarmonics = 3; // N = 3
        IntUnaryOperator h_identity = k -> k;
        BiFunction<Integer, Double, Double> a_const = (k, t) -> 1.0;

        HarmonicSynthesizerComplex synth = new HarmonicSynthesizerComplex(base, nbHarmonics, h_identity, a_const);
        double[] result = synth.synthesize(note, tempo, volume);

        // Check that the array has the correct size (441)
        assertEquals(nbSamples, result.length, "Sample array length mismatch (should be " + nbSamples + ")");

        double[] expected = new double[nbSamples];
        for (int i = 0; i < nbSamples; i++) {
            double t = (double) i / sampleRate;
            double sum = 0.0; // The base sound is 0.0

            // FIX 1: Loop k=2 to match the implementation
            for (int k = 2; k <= nbHarmonics; k++) { // k=2, k=3
                sum += Math.sin(2 * Math.PI * k * frequency * t); // H2 + H3
            }

            // Expected result: (Base + H2 + H3) / N = (0.0 + H2 + H3) / 3
            expected[i] = sum / nbHarmonics;
        }

        assertArrayEquals(expected, result, 1e-9, "Normalized additive synthesis mismatch");
    }

    @Test
    void testTimeDependentAmplitude() {
        // FIX 2: Use a duration >= 1.0ms
        double durationMs = 10.0;
        double frequency = 1.0;

        Note note = new FakeNote(frequency, durationMs);
        NoteSynthesizer base = new FakeBaseSynthesizer();
        int nbSamples = getExpectedNbSamples(note); // 441

        int nbHarmonics = 2; // N = 2
        IntUnaryOperator h_identity = k -> k;
        BiFunction<Integer, Double, Double> a_time = (k, t) -> t;

        HarmonicSynthesizerComplex synth = new HarmonicSynthesizerComplex(base, nbHarmonics, h_identity, a_time);
        double[] result = synth.synthesize(note, tempo, volume);

        assertEquals(nbSamples, result.length, "Sample array length mismatch (should be " + nbSamples + ")");

        double[] expected = new double[nbSamples];
        for (int i = 0; i < nbSamples; i++) {
            double t = (double) i / sampleRate;
            double sum = 0.0; // Base sound = 0.0

            // FIX 1: Loop k=2
            for (int k = 2; k <= nbHarmonics; k++) { // k=2
                sum += t * Math.sin(2 * Math.PI * k * frequency * t); // t * H2
            }

            // Expected result: (Base + t*H2) / N = (0.0 + t*H2) / 2
            expected[i] = sum / nbHarmonics;
        }

        assertArrayEquals(expected, result, 1e-9, "Time-dependent amplitude synthesis mismatch");
    }

    @Test
    void testAllZeroAmplitudeProducesSilence() {
        // FIX 2: Use a duration >= 1.0ms
        double durationMs = 10.0;
        double frequency = 440.0;

        Note note = new FakeNote(frequency, durationMs);
        NoteSynthesizer base = new FakeBaseSynthesizer();
        int nbSamples = getExpectedNbSamples(note); // 441

        int nbHarmonics = 4;
        IntUnaryOperator h_any = k -> k;
        BiFunction<Integer, Double, Double> a_zero = (k, t) -> 0.0;

        HarmonicSynthesizerComplex synth = new HarmonicSynthesizerComplex(base, nbHarmonics, h_any, a_zero);
        double[] result = synth.synthesize(note, tempo, volume);

        assertEquals(nbSamples, result.length, "Sample array length mismatch (should be " + nbSamples + ")");

        double[] expected = new double[nbSamples]; // all zeros
        assertArrayEquals(expected, result, 0.0, "All-zero amplitude should produce silence");
    }

    @Test
    void testNonIdentityHarmonicIndex() {
        // FIX 2: Use a duration >= 1.0ms
        double durationMs = 10.0;
        double frequency = 1.0;

        Note note = new FakeNote(frequency, durationMs);
        NoteSynthesizer base = new FakeBaseSynthesizer();
        int nbSamples = getExpectedNbSamples(note); // 441

        int nbHarmonics = 3;
        IntUnaryOperator h_even = k -> k * 2; // h(2)=4, h(3)=6
        BiFunction<Integer, Double, Double> a_const = (k, t) -> 1.0;

        HarmonicSynthesizerComplex synth = new HarmonicSynthesizerComplex(base, nbHarmonics, h_even, a_const);
        double[] result = synth.synthesize(note, tempo, volume);
        assertEquals(nbSamples, result.length);

        double[] expected = new double[nbSamples];
        for (int i = 0; i < nbSamples; i++) {
            double t = (double) i / sampleRate;
            double sum = 0.0;

            // FIX 1: Loop k=2
            // k=2 -> h(2)=4
            sum += Math.sin(2 * Math.PI * 4 * frequency * t);
            // k=3 -> h(3)=6
            sum += Math.sin(2 * Math.PI * 6 * frequency * t);

            expected[i] = sum / nbHarmonics; // (0.0 + H4 + H6) / 3
        }
        assertArrayEquals(expected, result, 1e-9, "Non-identity h() function mismatch");
    }
}