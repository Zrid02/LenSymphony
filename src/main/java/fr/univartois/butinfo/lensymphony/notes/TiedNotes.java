package fr.univartois.butinfo.lensymphony.notes;

import java.util.List;

public class TiedNotes implements Note{

    /**
     * The list of notes that are tied together.
     */

    private final List<Note> notes;

    public TiedNotes(List<Note> notes) {
        this.notes = List.copyOf(notes);
    }

    /**
     * this function return the frequency of the first note in the tied notes
     *
     */

    @Override
    public double getFrequency() {
        if (notes.isEmpty()) {
            return 0;
        }
        return notes.getFirst().getFrequency();
    }

    /**
     * this function return the total duration of the tied notes
     *
     */

    public int getDuration(int tempo) {
        int totalDuration = 0;
        for (Note note : notes) {
            totalDuration = totalDuration + note.getDuration(tempo); // sum the duration of each note
        }
        return totalDuration;
    }
}
