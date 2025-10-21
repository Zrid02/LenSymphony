package fr.univartois.butinfo.lensymphony.synthesizer;

import fr.univartois.butinfo.lensymphony.notes.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class HarmonicSynthesizerTest {

    @Test
    void createHarmonicSynthesizer() {
        NoteSynthesizer base = new PureSound();
        NoteSynthesizer harmonic = new HarmonicSynthesizer(base, 5);

        assertNotNull(harmonic);
    }

    @Test
    void synthesizeWithHarmonics() {
        NoteSynthesizer harmonic = new HarmonicSynthesizer(new PureSound(), 5);
        Note note = new PitchedNote(NotePitch.of(PitchClass.A, 4), NoteValue.QUARTER);

        double[] samples = harmonic.synthesize(note, 120, 0.5);

        assertEquals(22050, samples.length); // 500ms * 44100 / 1000
    }

    @Test
    void differentFromPureSound() {
        NoteSynthesizer pure = new PureSound();
        NoteSynthesizer harmonic = new HarmonicSynthesizer(pure, 5);
        Note note = new PitchedNote(NotePitch.of(PitchClass.A, 4), NoteValue.QUARTER);

        double[] pureSamples = pure.synthesize(note, 120, 0.5);
        double[] harmonicSamples = harmonic.synthesize(note, 120, 0.5);

        assertNotEquals(pureSamples[1000], harmonicSamples[1000]);
    }

    @Test
    void nullSynthesizer_throwsException() {
        assertThrows(NullPointerException.class, () -> {
            new HarmonicSynthesizer(null, 5);
        });
    }

    @Test
    void invalidHarmonics_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            new HarmonicSynthesizer(new PureSound(), 0);
        });
    }
}