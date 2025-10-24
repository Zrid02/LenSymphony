package fr.univartois.butinfo.lensymphony.synthesizer;

import fr.univartois.butinfo.lensymphony.notes.Note;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the CymbaleSynthesizer class.
 *
 * Testing is focused on the deterministic parts of the synthesizer:
 * - The cymbaleEnvelope() method, which is public and deterministic.
 * - Edge cases like zero frequency or zero duration notes.
 *
 * Direct testing of the synthesize() output values is not possible
 * due to the use of java.util.Random.
 */
class CymbaleSynthesizerTest {

    private static final int SAMPLE_RATE = NoteSynthesizer.SAMPLE_RATE;
    private static final int TEMPO = 120; // Standard tempo
    private static final double VOLUME = 1.0; // Standard volume

    // Constants from the implementation
    private static final double ATTACK_DEFAULT = 0.01; // 10ms
    private static final double DECAY_DEFAULT = 0.2; // 200ms

    /**
     * A simple fake Note implementation for testing.
     * It returns the duration as an int, mimicking truncation.
     */
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

    /**
     * Calculates the expected number of samples using the same logic
     * as the synthesizer implementation (double division).
     */
    private int getExpectedNbSamples(Note note, int tempo) {
        // Implementation uses: (int) ((note.getDuration(tempo) / 1000.0) * SAMPLE_RATE)
        double durationSeconds = note.getDuration(tempo) / 1000.0;
        return (int) (durationSeconds * SAMPLE_RATE);
    }

    /**
     * Tests the public cymbaleEnvelope() method directly,
     * as this contains the core (and testable) logic.
     */
    @Test
    void testCymbaleEnvelopeLogic() {
        CymbaleSynthesizer synth = CymbaleSynthesizer.getInstance();
        double volume = 0.8; // Use a non-1.0 volume

        // 1. Test in the middle of the attack phase (t < 0.01)
        double t_attack = 0.005; // 5ms
        // expected = volume * (t / attack) = 0.8 * (0.005 / 0.01) = 0.8 * 0.5 = 0.4
        double expected_attack = volume * (t_attack / ATTACK_DEFAULT);
        assertEquals(expected_attack, synth.cymbaleEnvelope(t_attack, volume), 1e-9,
                "Envelope value in attack phase is incorrect.");

        // 2. Test at the exact peak (t == attack)
        double t_peak = ATTACK_DEFAULT; // 10ms
        // expected = volume * Math.exp((attack - t) / decay) = 0.8 * Math.exp(0) = 0.8
        double expected_peak = volume * Math.exp((ATTACK_DEFAULT - t_peak) / DECAY_DEFAULT);
        assertEquals(expected_peak, synth.cymbaleEnvelope(t_peak, volume), 1e-9,
                "Envelope value at peak (start of decay) is incorrect.");
        assertEquals(volume, expected_peak, 1e-9); // Should be full volume

        // 3. Test in the middle of the decay phase (t > 0.01)
        double t_decay = 0.05; // 50ms
        // expected = volume * Math.exp((0.01 - 0.05) / 0.2) = 0.8 * Math.exp(-0.04 / 0.2) = 0.8 * Math.exp(-0.2)
        double expected_decay = volume * Math.exp((ATTACK_DEFAULT - t_decay) / DECAY_DEFAULT);
        assertEquals(expected_decay, synth.cymbaleEnvelope(t_decay, volume), 1e-9,
                "Envelope value in decay phase is incorrect.");
    }

    /**
     * Tests the synthesize method with a zero frequency note.
     * The implementation should return an array of zeros, but of the correct length.
     */
    @Test
    void testZeroFrequencyNote() {
        double durationMs = 100.0; // 100ms
        double frequency = 0.0;

        Note note = new FakeNote(frequency, durationMs);
        CymbaleSynthesizer synth = CymbaleSynthesizer.getInstance();

        int nbSample = getExpectedNbSamples(note, TEMPO); // (int)(0.1 * 44100) = 4410

        double[] resultSound = synth.synthesize(note, TEMPO, VOLUME);

        // Check length
        assertEquals(nbSample, resultSound.length,
                "Array length should match duration even if frequency is 0.");

        // Check content (should be all zeros)
        double[] expectedSound = new double[nbSample];
        assertArrayEquals(expectedSound, resultSound, 0.0,
                "A zero frequency note should result in silence.");
    }

    /**
     * Tests synthesis with a duration of zero.
     * This should result in an empty array (0 samples).
     */
    @Test
    void testZeroDurationNote() {
        double durationMs = 0.0;
        double frequency = 440.0;

        Note note = new FakeNote(frequency, durationMs);
        CymbaleSynthesizer synth = CymbaleSynthesizer.getInstance();

        double[] resultSound = synth.synthesize(note, TEMPO, VOLUME);

        // nbSample = (int)(0.0 * 44100) = 0
        assertEquals(0, resultSound.length,
                "A zero duration note should result in 0 samples.");
    }

    /**
     * A "smoke test" for the synthesize method with a valid note.
     * We cannot check the values, but we can check that it runs
     * and returns an array of the correct (non-zero) length.
     */
    @Test
    void testSynthesizeReturnsCorrectLength() {
        double durationMs = 50.0; // 50ms
        double frequency = 440.0;

        Note note = new FakeNote(frequency, durationMs);
        CymbaleSynthesizer synth = CymbaleSynthesizer.getInstance();

        int nbSample = getExpectedNbSamples(note, TEMPO); // (int)(0.05 * 44100) = 2205

        double[] resultSound = synth.synthesize(note, TEMPO, VOLUME);

        assertEquals(nbSample, resultSound.length,
                "Synthesize method did not return an array of the expected length.");
    }
}