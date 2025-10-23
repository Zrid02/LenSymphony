package fr.univartois.butinfo.lensymphony.synthesizer;

import fr.univartois.butinfo.lensymphony.notes.Note;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TriangleSynthesizerTest {

    @Test
    public void synthesizeProducesExpectedLengthAndNonZero() {
        Note note = new Note() {
            @Override
            public double getFrequency() { return 440.0; }
            @Override
            public int getDuration(int tempo) { return 500; } // ms
        };

        int tempo = 120;
        double volume = 0.7;
        TriangleSynthesizer ts = TriangleSynthesizer.getInstance();

        double[] samples = ts.synthesize(note, tempo, volume);

        int expected = (int) (note.getDuration(tempo) / 1000.0 * 44100);
        assertEquals(expected, samples.length, "Le nombre d'échantillons doit correspondre à la durée");

        boolean anyNonZero = false;
        for (double s : samples) {
            if (Math.abs(s) > 1e-12) { anyNonZero = true; break; }
        }
        assertTrue(anyNonZero, "Au moins un échantillon doit être non nul");
    }
}