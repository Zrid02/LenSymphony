package fr.univartois.butinfo.lensymphony.synthesizer;

import fr.univartois.butinfo.lensymphony.notes.Note;
import fr.univartois.butinfo.lensymphony.synthesizer.NoteSynthesizer;
import org.junit.jupiter.api.Test;

import java.util.function.BiFunction;
import java.util.function.IntUnaryOperator;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Additional tests for HarmonicSynthesizerComplex without Mockito.
 * These tests are adapted to match the *original* implementation which
 * adds harmonics H2...HN to the base sound (which is 0.0 here)
 * and normalizes by N.
 */
class HarmonicSynthesizerComplexTest {

    private final int sampleRate = NoteSynthesizer.SAMPLE_RATE;
    private final int tempo = 120;
    private final double volume = 1.0;

    // Simple fake Note implementation for tests
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
            return (int) durationMs;
        }

        // expose the precise duration (double) for test fakes to avoid truncation
        public double getDurationMsDouble() {
            return durationMs;
        }
    }

    // Fake base synthesizer that returns a zero-filled array of the expected size
    // This remains unchanged. The implementation will add H2..HN to this array of zeros.
    private static class FakeBaseSynthesizer implements NoteSynthesizer {
        @Override
        public double[] synthesize(Note note, int tempo, double volume) {
            double durationSeconds;
            // if we have the fake note, use the precise double duration to avoid truncation
            if (note instanceof FakeNote) {
                durationSeconds = ((FakeNote) note).getDurationMsDouble() / 1000.0;
            } else {
                durationSeconds = note.getDuration(tempo) / 1000.0;
            }
            int nbSample = (int) Math.round(durationSeconds * SAMPLE_RATE);
            if (nbSample < 0) nbSample = 0;
            return new double[nbSample];
        }
    }

    @Test
    void testNormalizationWithMultipleHarmonics() {
        int nbSamples = 5;
        double frequency = 1.0;
        double durationMs = (nbSamples * 1000.0) / sampleRate;

        Note note = new FakeNote(frequency, durationMs);
        NoteSynthesizer base = new FakeBaseSynthesizer();

        int nbHarmonics = 3;
        IntUnaryOperator h_identity = k -> k; // h(k)=k
        BiFunction<Integer, Double, Double> a_const = (k, t) -> 1.0; // a(k,t)=1

        HarmonicSynthesizerComplex synth = new HarmonicSynthesizerComplex(base, nbHarmonics, h_identity, a_const);

        double[] result = synth.synthesize(note, tempo, volume);

        assertEquals(nbSamples, result.length, "Sample array length mismatch");

        double[] expected = new double[nbSamples];
        for (int i = 0; i < nbSamples; i++) {
            double t = (double) i / sampleRate;
            double sum = 0.0;

            // *** MODIFICATION HERE ***
            // The implementation loops from 2, adding to the base sound (which is 0.0).
            for (int k = 2; k <= nbHarmonics; k++) { // k=2, 3
                sum += Math.sin(2 * Math.PI * k * frequency * t); // H2 + H3
            }
            // The result is (Base + H2 + H3) / N = (0.0 + H2 + H3) / 3
            expected[i] = sum / nbHarmonics;
        }

        assertArrayEquals(expected, result, 1e-9, "Normalized additive synthesis mismatch");
    }

    @Test
    void testTimeDependentAmplitude() {
        int nbSamples = 5;
        double frequency = 1.0;
        double durationMs = (nbSamples * 1000.0) / sampleRate;

        Note note = new FakeNote(frequency, durationMs);
        NoteSynthesizer base = new FakeBaseSynthesizer();

        int nbHarmonics = 2;
        IntUnaryOperator h_identity = k -> k;
        // amplitude = t (time in seconds) so amplitude varies with sample index
        BiFunction<Integer, Double, Double> a_time = (k, t) -> t;

        HarmonicSynthesizerComplex synth = new HarmonicSynthesizerComplex(base, nbHarmonics, h_identity, a_time);

        double[] result = synth.synthesize(note, tempo, volume);

        assertEquals(nbSamples, result.length, "Sample array length mismatch");

        double[] expected = new double[nbSamples];
        for (int i = 0; i < nbSamples; i++) {
            double t = (double) i / sampleRate;
            double sum = 0.0;

            // *** MODIFICATION HERE ***
            // The implementation loops from 2, adding to the base sound (which is 0.0).
            for (int k = 2; k <= nbHarmonics; k++) { // k=2 only
                sum += t * Math.sin(2 * Math.PI * k * frequency * t); // t * H2
            }
            // The result is (Base + t*H2) / N = (0.0 + t*H2) / 2
            expected[i] = sum / nbHarmonics;
        }

        assertArrayEquals(expected, result, 1e-9, "Time-dependent amplitude synthesis mismatch");
    }

    @Test
    void testAllZeroAmplitudeProducesSilence() {
        int nbSamples = 5;
        double frequency = 440.0; // arbitrary
        double durationMs = (nbSamples * 1000.0) / sampleRate;

        Note note = new FakeNote(frequency, durationMs);
        NoteSynthesizer base = new FakeBaseSynthesizer();

        int nbHarmonics = 4;
        IntUnaryOperator h_any = k -> k;
        BiFunction<Integer, Double, Double> a_zero = (k, t) -> 0.0;

        HarmonicSynthesizerComplex synth = new HarmonicSynthesizerComplex(base, nbHarmonics, h_any, a_zero);

        double[] result = synth.synthesize(note, tempo, volume);

        assertEquals(nbSamples, result.length, "Sample array length mismatch");

        double[] expected = new double[nbSamples]; // all zeros

        // This test passes without modification because (0.0 + 0.0 + 0.0) / 4 is still 0.0.
        assertArrayEquals(expected, result, 0.0, "All-zero amplitude should produce silence");
    }
}