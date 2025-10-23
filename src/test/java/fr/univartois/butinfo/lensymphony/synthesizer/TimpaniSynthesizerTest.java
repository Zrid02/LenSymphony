package fr.univartois.butinfo.lensymphony.synthesizer;

import fr.univartois.butinfo.lensymphony.notes.Note;
import fr.univartois.butinfo.lensymphony.notes.NoteFactory;
import fr.univartois.butinfo.lensymphony.notes.NotePitch;
import fr.univartois.butinfo.lensymphony.notes.NoteValue;
import fr.univartois.butinfo.lensymphony.notes.PitchClass;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests simples pour la classe TimpaniSynthesizer.
 */
public class TimpaniSynthesizerTest {

    private TimpaniSynthesizer timpani;
    private Note note;

    @BeforeEach
    void setUp() {
        timpani = TimpaniSynthesizer.getInstance();

        // Création d'une note de test : Do3 (C3)
        NoteFactory factory = NoteFactory.getInstance();
        note = factory.createNote(
                NotePitch.of(PitchClass.C, 3),
                NoteValue.QUARTER
        );
    }

    @Test
    void testSingletonInstance() {
        TimpaniSynthesizer t1 = TimpaniSynthesizer.getInstance();
        TimpaniSynthesizer t2 = TimpaniSynthesizer.getInstance();
        assertSame(t1, t2, "TimpaniSynthesizer doit être un singleton");
    }

    @Test
    void testSynthesizeNotNull() {
        double[] sound = timpani.synthesize(note, 120, 1.0);
        assertNotNull(sound, "Le son ne doit pas être null");
        assertTrue(sound.length > 0, "Le son doit contenir des échantillons");
    }

    @Test
    void testSynthesizeFrequencyGlissando() {
        double[] sound = timpani.synthesize(note, 120, 1.0);
        double start = sound[0];
        double end = sound[sound.length - 1];

        assertTrue(end < start,
                "La fréquence doit diminuer au fil du temps (glissando descendant)");
    }

    @Test
    void testSamplesAreInExpectedRange() {
        double[] sound = timpani.synthesize(note, 120, 1.0);

        for (double s : sound) {
            assertTrue(s > 0, "Les fréquences doivent être positives");
        }
    }
}
