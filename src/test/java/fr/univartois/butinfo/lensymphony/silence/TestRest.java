package fr.univartois.butinfo.lensymphony.silence;

import fr.univartois.butinfo.lensymphony.notes.Note;
import fr.univartois.butinfo.lensymphony.notes.NoteValue;
import fr.univartois.butinfo.lensymphony.notes.Rest;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestRest {


	//FakeNote class to simulate notes since Note class was still in production
	private static class FakeNote implements Note {
		private final double frequency;
		private final int duration;

		FakeNote(double frequency, int duration) {
			this.frequency = frequency;
			this.duration = duration;
		}

		@Override
		public double getFrequency() {
			return frequency;
		}

		@Override
		public int getDuration(int tempo) {
			return duration;
		}
	}


	@Test
	void addDotsNegativeThrows() {
		NoteValue nv = NoteValue.WHOLE;
		Rest s = new Rest(nv);
		assertThrows(IllegalArgumentException.class, () -> s.addDots(-3));
	}

	@Test
	void tieWithNonSilenceThrows() {
		NoteValue nv = NoteValue.WHOLE;
		Rest s = new Rest(nv);
		Note soundNote = new FakeNote(440.0, 200);
		assertThrows(IllegalArgumentException.class, () -> s.tieWith(soundNote));
	}

	@Test
	void tieWithAddsAndReturnsThis() {
		NoteValue nv1 = NoteValue.WHOLE;
		NoteValue nv2 = NoteValue.WHOLE;
		Rest s1 = new Rest(nv1);
		Rest s2 = new Rest(nv2);
		Rest returned = s1.tieWith(s2);
		assertSame(s1, returned);
		List<Note> tied = s1.getTiedNotes();
		assertTrue(tied.contains(s2));
	}

	@Test
	void getTiedNotesIsUnmodifiable() {
		NoteValue nv = NoteValue.WHOLE;
		Rest s1 = new Rest(nv);
		Rest s2 = new Rest(nv);
		s1.tieWith(s2);
		List<Note> tied = s1.getTiedNotes();
		assertThrows(UnsupportedOperationException.class, () -> tied.add(s2));
	}

	@Test
	void getFrequencyIsZero() {
		NoteValue nv = NoteValue.WHOLE;
		Rest s = new Rest(nv);
		assertEquals(0.0, s.getFrequency());
	}

	@Test
	void getDurationTempoValidation() {
		NoteValue nv =NoteValue.WHOLE;
		Rest s = new Rest(nv);
		assertThrows(IllegalArgumentException.class, () -> s.getDuration(0));
		assertThrows(IllegalArgumentException.class, () -> s.getDuration(-10));
	}
}
