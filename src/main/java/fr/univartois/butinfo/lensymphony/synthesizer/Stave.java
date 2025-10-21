package fr.univartois.butinfo.lensymphony.synthesizer;

import fr.univartois.butinfo.lensymphony.notes.Note;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Stave implements Iterable<Note>{
	private List<Note> notes = new ArrayList<>();

	public void addNote(Note note) {
		notes.add(note);
	}

	@Override
	public Iterator<Note> iterator() {
		return notes.iterator();
	}
}
