package fr.univartois.butinfo.lensymphony.synthesizer;

import fr.univartois.butinfo.lensymphony.notes.Note;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the ADSRSynthesizer class.
 *
 * These tests validate that the ADSR envelope is applied correctly to a base sound.
 * It uses PureSound as the base synthesizer to verify the envelope's effect.
 *
 * @author (Your Name)
 */
class ADSRTest {

    // Default sample rate from the NoteSynthesizer interface
    private static final int SAMPLE_RATE = NoteSynthesizer.SAMPLE_RATE;
    private static final int TEMPO = 120; // Standard tempo for consistent duration

    /**
     * A simple fake Note implementation for testing.
     * It provides a fixed frequency and a duration in milliseconds.
     * Crucially, getDuration(tempo) returns an int, mimicking the truncation behavior.
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
            // This cast (e.g., (int)200.0) is the critical part that matches
            // the truncation behavior of the real implementation.
            return (int) durationMs;
        }
    }

    /**
     * Calculates the expected number of samples using the same logic
     * as the synthesizer implementation to ensure tests are synchronized.
     *
     * @param note The note to calculate samples for.
     * @param tempo The tempo.
     * @return The expected number of samples.
     */
    private int getExpectedNbSamples(Note note, int tempo) {
        double durationSeconds = note.getDuration(tempo) / 1000.0;
        return (int) (durationSeconds * SAMPLE_RATE);
    }

    /**
     * Tests all phases of the ADSR envelope (Attack, Decay, Sustain, Release).
     *
     * This test creates a 200ms note and applies an envelope:
     * - Attack: 20ms
     * - Decay: 30ms (to 0.5 sustain)
     * - Sustain: 0.5 level
     * - Release: 40ms
     *
     * It verifies that the resulting sound array matches the expected calculations,
     * including the "double volume" application present in the implementation.
     */
    @Test
    void testFullADSRWaveform() {
        // --- Setup ---
        double volume = 0.8; // Use a non-1.0 volume to test double application
        double frequency = 10.0; // A simple frequency
        double durationMs = 200.0; // 200ms duration

        // ADSR Parameters (in ms)
        double attack = 20.0;
        double decay = 30.0;
        double sustain = 0.5; // Sustain level (0.0 to 1.0)
        double release = 40.0;

        Note note = new FakeNote(frequency, durationMs);
        NoteSynthesizer baseSynth = new PureSound(); // Use PureSound as the base

        // Create the synthesizer to be tested
        ADSRSynthesizer adsrSynth = new ADSRSynthesizer(baseSynth, attack, decay, sustain, release);

        // --- Execute ---
        double[] resultSound = adsrSynth.synthesize(note, TEMPO, volume);

        // --- Verify ---

        // 1. Get the expected base sound (which already has volume applied once)
        double[] baseSound = baseSynth.synthesize(note, TEMPO, volume);

        // 2. Calculate the expected output array
        int noteDuration = note.getDuration(TEMPO); // 200
        int nbSamples = baseSound.length;
        double[] expectedSound = new double[nbSamples];

        // Manually re-implement the ADSR logic from the class for verification
        for (int i = 0; i < nbSamples; i++) {
            // Map sample index 'i' back to time 't' in milliseconds
            // This matches the logic: t = (double) noteDuration * i / n
            double t = (double) noteDuration * i / nbSamples;

            double newVolume; // This is the 0.0-1.0 envelope level

            // Re-implementation of adsrEnvelope's logic
            if (t >= 0 && t < attack) {
                newVolume = t / attack; // Attack phase
            } else if (t >= attack && t < attack + decay) {
                newVolume = 1.0 - ((t - attack) / decay) * (1.0 - sustain); // Decay phase
            } else if (t >= attack + decay && t < noteDuration - release) {
                newVolume = sustain; // Sustain phase
            } else if (t >= noteDuration - release && t < noteDuration) {
                newVolume = sustain * (1.0 - ((t - (noteDuration - release)) / release)); // Release phase
            } else {
                newVolume = 0.0; // Outside the note duration
            }

            // This is the value returned by adsrEnvelope(t, ..., volume)
            // It includes the volume multiplication.
            double envelopeValue = newVolume * volume;

            // This is the final operation in synthesize()
            // baseSound[i] already contains (volume * sin(...))
            // The result is (volume * sin(...)) * (newVolume * volume)
            expectedSound[i] = baseSound[i] * envelopeValue;
        }

        // 3. Assert that the calculated result matches the expected result
        assertArrayEquals(expectedSound, resultSound, 1e-9,
                "ADSR envelope was not applied correctly.");
    }

    /**
     * Tests that a note with zero duration (due to truncation or input)
     * results in an empty sound array.
     */
    @Test
    void testZeroDurationNote() {
        double volume = 1.0;
        double durationMs = 0.0; // Zero duration

        Note note = new FakeNote(440.0, durationMs);
        NoteSynthesizer baseSynth = new PureSound();
        ADSRSynthesizer adsrSynth = new ADSRSynthesizer(baseSynth, 10, 10, 0.5, 10);

        double[] resultSound = adsrSynth.synthesize(note, TEMPO, volume);

        // Both baseSynth and adsrSynth should produce 0 samples
        assertEquals(0, resultSound.length, "Zero duration note should produce zero samples.");
    }
}