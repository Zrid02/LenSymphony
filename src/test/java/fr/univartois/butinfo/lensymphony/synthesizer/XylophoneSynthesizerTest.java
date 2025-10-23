package fr.univartois.butinfo.lensymphony.synthesizer;

import fr.univartois.butinfo.lensymphony.notes.Note;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class XylophoneSynthesizerTest {

    @Test
    public void synthesizeUsesNoteFrequency() {
        Note note = new Note() {
            @Override
            public double getFrequency() { return 330.0; }
            @Override
            public int getDuration(int tempo) { return 400; } // ms
        };

        int tempo = 100;
        double volume = 0.8;
        XylophoneSynthesizer xs = new XylophoneSynthesizer(8, 440.0);

        double[] samples = xs.synthesize(note, tempo, volume);

        int expected = (int) (note.getDuration(tempo) / 1000.0 * 44100);
        assertEquals(expected, samples.length, "Le nombre d'échantillons doit être correct");

        // Vérifie qu'il y a des échantillons non nuls
        boolean anyNonZero = false;
        for (double s : samples) { if (Math.abs(s) > 1e-12) { anyNonZero = true; break; } }
        assertTrue(anyNonZero, "Au moins un échantillon doit être non nul");
    }

    @Test
    public void synthesizeFallsBackToBaseFrequencyWhenNoteFrequencyNonPositive() {
        Note silentFreqNote = new Note() {
            @Override
            public double getFrequency() { return 0.0; }
            @Override
            public int getDuration(int tempo) { return 200; } // ms
        };

        XylophoneSynthesizer xs = new XylophoneSynthesizer(4, 440.0);
        double[] samples = xs.synthesize(silentFreqNote, 120, 0.5);

        int expected = (int) (silentFreqNote.getDuration(120) / 1000.0 * 44100);
        assertEquals(expected, samples.length, "Le fallback sur baseFrequency doit produire le bon nombre d'échantillons");
    }
}