// java
package fr.univartois.butinfo.lensymphony.synthesizer;

import fr.univartois.butinfo.lensymphony.notes.Note;
import org.junit.jupiter.api.Test;

import java.util.function.BiFunction;
import java.util.function.IntUnaryOperator;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Additional tests for HarmonicSynthesizerComplex without Mockito.
 */
class HarmonicSynthesizerComplexAdditionalTest {

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
    }

    // Fake base synthesizer that returns a zero-filled array of the expected size
    private static class FakeBaseSynthesizer implements NoteSynthesizer {
        @Override
        public double[] synthesize(Note note, int tempo, double volume) {
            double durationSeconds = note.getDuration(tempo) / 1000.0;
            int nbSample = (int) (durationSeconds * SAMPLE_RATE);
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
            for (int k = 1; k <= nbHarmonics; k++) {
                sum += Math.sin(2 * Math.PI * k * frequency * t);
            }
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
            for (int k = 1; k <= nbHarmonics; k++) {
                sum += t * Math.sin(2 * Math.PI * k * frequency * t);
            }
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
        assertArrayEquals(expected, result, 0.0, "All-zero amplitude should produce silence");
    }
}
