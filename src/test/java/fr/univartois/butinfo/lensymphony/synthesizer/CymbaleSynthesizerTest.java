package fr.univartois.butinfo.lensymphony.synthesizer;

import fr.univartois.butinfo.lensymphony.notes.Instruments;
import fr.univartois.butinfo.lensymphony.notes.Note;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test class for CymbaleSynthesizer.
 * It checks the Singleton pattern, the atonal behavior, and
 * sound generation.
 */
class CymbaleSynthesizerTest {

    /**
     * A "Stub" class for Note, used only for testing.
     * It implements the Note interface with the minimum required
     * for the synthesizers to use it.
     */
    private static class StubNote implements Note {
        private final double frequency;
        private final int durationMs;

        public StubNote(double frequency, int durationMs) {
            this.frequency = frequency;
            this.durationMs = durationMs;
        }

        @Override
        public double getFrequency() {
            return frequency;
        }

        @Override
        public int getDuration(int tempo) {
            return durationMs;
        }

        // If the Note interface declares other methods,
        // they would need to be implemented here (even with an empty body).
    }


    /**
     * Test 1: Verifies that the Singleton pattern is respected.
     * Two calls to getInstance() must return the SAME instance.
     *
     */
    @Test
    void testSingletonInstanceIsUnique() {
        NoteSynthesizer instance1 = CymbaleSynthesizer.getInstance();
        NoteSynthesizer instance2 = CymbaleSynthesizer.getInstance();

        // assertSame checks that both variables point
        // to the same object in memory.
        assertSame(instance1, instance2, "getInstance() must always return the same instance.");
    }

    /**
     * Test 2: Verifies the atonal behavior (silence handling).
     * If the note has a frequency <= 0, the synthesizer must
     * return an empty array.
     */
    @Test
    void testSynthesizeReturnsEmptyArrayForSilence() {
        NoteSynthesizer synthesizer = CymbaleSynthesizer.getInstance();

        // Create a "silent" note (frequency of 0)
        Note silence = new StubNote(0.0, 1000); // 1 second of silence

        double[] samples = synthesizer.synthesize(silence, 120, 1.0);

        // Check that the sound array is empty
        assertEquals(0, samples.length, "An atonal instrument must not play during a silence.");
    }

    /**
     * Test 3: Verifies sound generation for a valid note.
     * For a normal note (frequency > 0), the synthesizer must
     * generate an array of the correct size and containing sound.
     */
    @Test
    void testSynthesizeGeneratesSoundForValidNote() {
        NoteSynthesizer synthesizer = CymbaleSynthesizer.getInstance();
        int durationMs = 500; // 0.5 seconds

        // A normal note (440 Hz)
        Note note = new StubNote(440.0, durationMs);

        double[] samples = synthesizer.synthesize(note, 120, 1.0);

        // 1. Check the array size
        // Nb Samples = duration (sec) * SAMPLE_RATE
        int expectedLength = (int) ((durationMs / 1000.0) * NoteSynthesizer.SAMPLE_RATE);
        assertEquals(expectedLength, samples.length, "The sample array size must match the note's duration.");

        // 2. Check that there is sound
        // Because of random(), we cannot predict the exact values.
        // So, we check that the array is not just filled with zeros.
        double sum = 0.0;
        for (double sample : samples) {
            sum += Math.abs(sample); // Sum of absolute values
        }

        assertTrue(sum > 0.0, "The generated sound must not be total silence (all zeros).");
    }

    /**
     * Test 4: Verifies integration with the Instruments enum.
     * The enum must use the singleton instance correctly.
     */
    @Test
    void testInstrumentsEnumUsesSingletonInstance() {
        // Get the synthesizer from the enum
        NoteSynthesizer fromEnum = Instruments.CYMBAL.getSynthesizer();

        // Get the synthesizer directly
        NoteSynthesizer fromSingleton = CymbaleSynthesizer.getInstance();

        // Check that it's the same object
        assertSame(fromSingleton, fromEnum, "Instruments.CYMBAL must use the CymbaleSynthesizer singleton.");
    }
}