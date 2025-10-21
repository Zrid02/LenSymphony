package fr.univartois.butinfo.lensymphony.notes;

import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class ScoreTest {


	@Test
	void constructorSetsInstrument() {
		List<Note> notes = new ArrayList<>();
		Score stave = new Score(Instruments.XYLOPHONE,notes);
		assertEquals(Instruments.XYLOPHONE, stave.getInstrument());
	}

	@Test
	void iteratorIsEmptyWhenNoNotes() {
		List<Note> notes = new ArrayList<>();
		Score stave = new Score(Instruments.SNARE_DRUM, notes);
		Iterator<Note> it = stave.iterator();
		assertFalse(it.hasNext());
	}

	@Test
	void addNote() {
		List<Note> notes = new ArrayList<>();
		Score stave = new Score(Instruments.TIMPANI,notes);
		PitchedNote n1 = new PitchedNote(NotePitch.of(PitchClass.C, 5), NoteValue.EIGHTH);
		PitchedNote n2 = new PitchedNote(NotePitch.of(PitchClass.D, 5), NoteValue.WHOLE);

		stave.addNote(n1);
		stave.addNote(n2);

		List<Note> collected = new ArrayList<>();
		stave.iterator().forEachRemaining(collected::add);

		assertEquals(2, collected.size());
		assertSame(n1, collected.get(0));
		assertSame(n2, collected.get(1));
	}
}
