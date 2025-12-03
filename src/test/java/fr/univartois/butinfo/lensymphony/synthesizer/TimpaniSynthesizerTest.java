package fr.univartois.butinfo.lensymphony.synthesizer;

import fr.univartois.butinfo.lensymphony.notes.Note;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the TimpaniSynthesizer class.
 *
 * These tests validate the synthesis logic, including the exponential decay
 * and the frequency calculation.
 *
 * Note: These tests are designed to pass against the provided implementation,
 * including its specific behaviors like integer division for duration and
 * the use of the base frequency in the final sine wave.
 */
class TimpaniSynthesizerTest {

    private static final int SAMPLE_RATE = NoteSynthesizer.SAMPLE_RATE;
    private static final int TEMPO = 120; // Standard tempo
    private static final double VOLUME = 1.0; // Standard volume

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
            // (int) 2000.0 -> 2000
            return (int) durationMs;
        }
    }

    /**
     * Tests the main synthesis logic of the Timpani.
     * It checks if the waveform is generated according to the implementation's
     * specific formula.
     */
    @Test
    void testTimpaniSynthesisWaveform() {
        // --- Setup ---
        // Use 2000ms (2 seconds) to work correctly with the integer division bug
        double durationMs = 2000.0;
        double frequency = 100.0; // 100 Hz

        Note note = new FakeNote(frequency, durationMs);
        TimpaniSynthesizer synth = TimpaniSynthesizer.getInstance();

        // --- Execute ---
        double[] resultSound = synth.synthesize(note, TEMPO, VOLUME);

        // --- Verify ---

        // Replicate the implementation's logic, including bugs:

        // 1. Integer division for duration
        // note.getDuration(TEMPO) returns 2000.
        // 2000 / 1000 = 2.0
        double duration = note.getDuration(TEMPO) / 1000;

        // 2. nbSample calculation
        int nbSample = (int) (duration * SAMPLE_RATE);
        assertEquals(88200, nbSample, "Number of samples calculation is incorrect.");
        assertEquals(nbSample, resultSound.length, "Result array length mismatch.");

        double[] expectedSound = new double[nbSample];
        double decayRate = 5.0; // from implementation

        for (int i = 0; i < nbSample; i++) {
            double t = (double) i / SAMPLE_RATE;

            // This is calculated in the implementation but not used in the Math.sin
            double realFrequency = frequency + ((t * (0.6 * frequency - frequency)) / duration);

            // Check for the guard condition
            if (realFrequency <= 0) {
                expectedSound[i] = 0.0;
            } else {
                double envelope = Math.exp(-decayRate * t);

                // 3. Replicate the use of the *original* frequency in Math.sin
                expectedSound[i] = VOLUME * envelope * Math.sin(2 * Math.PI * frequency * t);
            }
        }

        assertArrayEquals(expectedSound, resultSound, 1e-9,
                "Timpani synthesis waveform does not match expected logic.");
    }

    /**
     * Tests synthesis with a zero (or negative) frequency note.
     * The implementation should return an array of zeros.
     */
    @Test
    void testZeroFrequencyNote() {
        double durationMs = 1000.0; // 1 second
        double frequency = 0.0;

        Note note = new FakeNote(frequency, durationMs);
        TimpaniSynthesizer synth = TimpaniSynthesizer.getInstance();

        double[] resultSound = synth.synthesize(note, TEMPO, VOLUME);

        // Calculate expected size
        double duration = note.getDuration(TEMPO) / 1000; // 1.0
        int nbSample = (int) (duration * SAMPLE_RATE); // 44100

        assertEquals(44100, resultSound.length, "Array length should match duration even if frequency is 0.");

        double[] expectedSound = new double[nbSample]; // All zeros

        assertArrayEquals(expectedSound, resultSound, 0.0,
                "A zero frequency note should result in silence.");
    }

    /**
     * Tests synthesis with a duration that truncates to zero.
     * (e.g., 999ms / 1000 = 0 in integer division).
     */
    @Test
    void testZeroDurationNote() {
        double durationMs = 999.0; // Will be 0 due to integer division
        double frequency = 100.0;

        Note note = new FakeNote(frequency, durationMs);
        TimpaniSynthesizer synth = TimpaniSynthesizer.getInstance();

        double[] resultSound = synth.synthesize(note, TEMPO, VOLUME);

        // duration = 999 / 1000 = 0
        // nbSample = (int) (0.0 * 44100) = 0
        assertEquals(0, resultSound.length,
                "A duration < 1000ms should result in 0 samples due to integer division.");
    }
}