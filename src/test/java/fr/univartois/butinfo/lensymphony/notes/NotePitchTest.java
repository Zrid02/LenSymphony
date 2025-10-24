package fr.univartois.butinfo.lensymphony.notes;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the NotePitch class.
 * This test validates all frequency calculation, caching, and alteration logic.
 */
class NotePitchTest {

    @Test
    void testA4ReferenceFrequency() {
        // A4 is the reference frequency
        NotePitch a4 = NotePitch.of(PitchClass.A, 4);
        assertEquals(440.0, a4.frequency(), 1e-9, "A4 frequency must be exactly 440.0 Hz.");
    }

    @Test
    void testC4FrequencyCalculation() {
        // C4 is 9 semitones below A4
        NotePitch c4 = NotePitch.of(PitchClass.C, 4);

        // This is the first time C4 is accessed, so it hits the creation branch
        // int a = 9 (A)
        // realOrdinal = 0 (C)
        // realOctave = 4
        // (0 - 9 + (4 - 4) * 12) / 12.0 = -9 / 12.0
        double expectedFreq = 440.0 * Math.pow(2.0, -9.0 / 12.0); // Approx 261.625...

        assertEquals(expectedFreq, c4.frequency(), 1e-9, "C4 frequency calculation incorrect.");
    }

    @Test
    void testCaching() {
        // Call `of` for C4
        NotePitch c4_firstCall = NotePitch.of(PitchClass.C, 4);
        // Call it again
        NotePitch c4_secondCall = NotePitch.of(PitchClass.C, 4);

        // It should return the *exact same* instance, not a new one
        assertSame(c4_firstCall, c4_secondCall, "NotePitch instances should be cached and reused.");
    }

    @Test
    void testAlteration() {
        // Test C4.alter(1) -> C#4
        NotePitch c4 = NotePitch.of(PitchClass.C, 4);
        NotePitch cSharp4 = c4.alter(1);

        NotePitch expectedPitch = NotePitch.of(PitchClass.C_SHARP, 4);
        assertSame(expectedPitch, cSharp4, "C4.alter(1) should return the C_SHARP 4 instance.");
    }

    @Test
    void testSharp() {
        // Test G4.sharp() -> G#4
        NotePitch g4 = NotePitch.of(PitchClass.G, 4);
        NotePitch gSharp4 = g4.sharp(); // alter(1)

        NotePitch expectedPitch = NotePitch.of(PitchClass.G_SHARP, 4);
        assertSame(expectedPitch, gSharp4, "sharp() method failed.");
    }

    @Test
    void testFlat() {
        // Test A4.flat() -> G#4
        NotePitch a4 = NotePitch.of(PitchClass.A, 4);
        NotePitch gSharp4 = a4.flat(); // alter(-1)

        NotePitch expectedPitch = NotePitch.of(PitchClass.G_SHARP, 4);
        assertSame(expectedPitch, gSharp4, "flat() method failed.");
    }

    @Test
    void testOctaveWrapSharp() {
        // B4.sharp() should wrap to C5
        NotePitch b4 = NotePitch.of(PitchClass.B, 4);
        NotePitch c5 = b4.sharp();

        // Manually check frequency
        // B4 is 2 semitones above A4. C5 is 3 semitones above A4.
        double expectedFreq = 440.0 * Math.pow(2.0, 3.0 / 12.0); // Approx 523.25

        assertEquals(expectedFreq, c5.frequency(), 1e-9, "B4.sharp() frequency is incorrect.");
        assertSame(NotePitch.of(PitchClass.C, 5), c5, "B4.sharp() should be the C5 instance.");
    }

    @Test
    void testOctaveWrapFlat() {
        // C4.flat() should wrap to B3
        NotePitch c4 = NotePitch.of(PitchClass.C, 4);
        NotePitch b3 = c4.flat();

        // C4 is 9 semitones below A4. B3 is 10 semitones below A4.
        double expectedFreq = 440.0 * Math.pow(2.0, -10.0 / 12.0); // Approx 246.94

        assertEquals(expectedFreq, b3.frequency(), 1e-9, "C4.flat() frequency is incorrect.");
        assertSame(NotePitch.of(PitchClass.B, 3), b3, "C4.flat() should be the B3 instance.");
    }

    @Test
    void testPitchOutOfRangeLow() {
        // C0.flat() -> B-1. This should throw.
        // realOrdinal = -1, realOctave = 0
        // -> realOctave = -1, realOrdinal = 11
        // -> if (realOctave < 0) -> throw
        Exception e = assertThrows(IllegalArgumentException.class, () -> {
            NotePitch.of(PitchClass.C, 0).flat();
        });
        assertEquals("Pitch is too low or too high", e.getMessage());
    }

    @Test
    void testPitchOutOfRangeHigh() {
        // B8.sharp() -> C9. This should throw (NB_OCTAVES = 9, so 0-8 is valid)
        // realOrdinal = 12, realOctave = 8
        // -> realOctave = 9, realOrdinal = 0
        // -> if (NB_OCTAVES <= realOctave) -> throw
        Exception e = assertThrows(IllegalArgumentException.class, () -> {
            NotePitch.of(PitchClass.B, 8).sharp();
        });
        assertEquals("Pitch is too low or too high", e.getMessage());
    }
}