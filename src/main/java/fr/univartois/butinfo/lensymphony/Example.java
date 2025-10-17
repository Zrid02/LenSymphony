/**
 * LenSymphony - A simple music synthesizer library developed in Lens, France.
 * Copyright (c) 2025 Romain Wallon - Université d'Artois.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to
 * deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or
 * sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE
 * USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package fr.univartois.butinfo.lensymphony;

import java.util.List;

import fr.univartois.butinfo.lensymphony.notes.AbstractNoteFactory;
import fr.univartois.butinfo.lensymphony.notes.Note;
import fr.univartois.butinfo.lensymphony.notes.NotePitch;
import fr.univartois.butinfo.lensymphony.notes.NoteValue;
import fr.univartois.butinfo.lensymphony.notes.PitchClass;
import fr.univartois.butinfo.lensymphony.synthesizer.MusicSynthesizer;
import fr.univartois.butinfo.lensymphony.synthesizer.NoteSynthesizer;

/**
 * The Example class provides a simple example of how to synthesize and play music using
 * the library.
 *
 * @author Romain Wallon
 *
 * @version 0.1.0
 */
public final class Example {

    /**
     * The note factory used to create notes.
     * TODO: You have to set it with your own implementation.
     */
    private static AbstractNoteFactory noteFactory = null;

    /**
     * The note synthesizer used to synthesize notes.
     * TODO: You have to set it with your own implementation.
     */
    private static NoteSynthesizer noteSynthesizer = null;

    /**
     * Disables instantiation.
     */
    private Example() {
        throw new AssertionError("No Example instances for you!");
    }

    /**
     * Executes the example, and plays a simple melody.
     *
     * @param args The command line arguments (not used).
     *
     * @throws Exception If an error occurs during synthesis or playback.
     */
    public static void main(String[] args) throws Exception {
        // Defining the melody to play.
        List<Note> notes = List.of(
                noteFactory.createNote(NotePitch.of(PitchClass.C, 5), NoteValue.EIGHTH),
                noteFactory.createNote(NotePitch.of(PitchClass.C, 5), NoteValue.EIGHTH),
                noteFactory.createNote(NotePitch.of(PitchClass.C, 5), NoteValue.EIGHTH),
                noteFactory.createNote(NotePitch.of(PitchClass.D, 5), NoteValue.EIGHTH),
                noteFactory.createNote(NotePitch.of(PitchClass.E, 5), NoteValue.QUARTER),
                noteFactory.createNote(NotePitch.of(PitchClass.D, 5), NoteValue.QUARTER),
                noteFactory.createNote(NotePitch.of(PitchClass.C, 5), NoteValue.EIGHTH),
                noteFactory.createNote(NotePitch.of(PitchClass.E, 5), NoteValue.EIGHTH),
                noteFactory.createNote(NotePitch.of(PitchClass.D, 5), NoteValue.EIGHTH),
                noteFactory.createNote(NotePitch.of(PitchClass.D, 5), NoteValue.EIGHTH),
                noteFactory.createNote(NotePitch.of(PitchClass.C, 5), NoteValue.QUARTER),
                noteFactory.createRest(NoteValue.QUARTER),

                noteFactory.createNote(NotePitch.of(PitchClass.C, 5), NoteValue.EIGHTH),
                noteFactory.createNote(NotePitch.of(PitchClass.C, 5), NoteValue.EIGHTH),
                noteFactory.createNote(NotePitch.of(PitchClass.C, 5), NoteValue.EIGHTH),
                noteFactory.createNote(NotePitch.of(PitchClass.D, 5), NoteValue.EIGHTH),
                noteFactory.createNote(NotePitch.of(PitchClass.E, 5), NoteValue.QUARTER),
                noteFactory.createNote(NotePitch.of(PitchClass.D, 5), NoteValue.QUARTER),
                noteFactory.createNote(NotePitch.of(PitchClass.C, 5), NoteValue.EIGHTH),
                noteFactory.createNote(NotePitch.of(PitchClass.E, 5), NoteValue.EIGHTH),
                noteFactory.createNote(NotePitch.of(PitchClass.D, 5), NoteValue.EIGHTH),
                noteFactory.createNote(NotePitch.of(PitchClass.D, 5), NoteValue.EIGHTH),
                noteFactory.createNote(NotePitch.of(PitchClass.C, 5), NoteValue.QUARTER),
                noteFactory.createRest(NoteValue.QUARTER),

                noteFactory.createNote(NotePitch.of(PitchClass.D, 5), NoteValue.EIGHTH),
                noteFactory.createNote(NotePitch.of(PitchClass.D, 5), NoteValue.EIGHTH),
                noteFactory.createNote(NotePitch.of(PitchClass.D, 5), NoteValue.EIGHTH),
                noteFactory.createNote(NotePitch.of(PitchClass.D, 5), NoteValue.EIGHTH),
                noteFactory.createNote(NotePitch.of(PitchClass.A, 4), NoteValue.QUARTER),
                noteFactory.createNote(NotePitch.of(PitchClass.A, 4), NoteValue.QUARTER),
                noteFactory.createNote(NotePitch.of(PitchClass.D, 5), NoteValue.EIGHTH),
                noteFactory.createNote(NotePitch.of(PitchClass.C, 5), NoteValue.EIGHTH),
                noteFactory.createNote(NotePitch.of(PitchClass.B, 4), NoteValue.EIGHTH),
                noteFactory.createNote(NotePitch.of(PitchClass.A, 4), NoteValue.EIGHTH),
                noteFactory.createNote(NotePitch.of(PitchClass.G, 4), NoteValue.HALF));

        // Synthesizing and playing the melody.
        MusicSynthesizer musicSynthetizer = new MusicSynthesizer(100, notes, noteSynthesizer);
        musicSynthetizer.synthesize();
        musicSynthetizer.play();
    }

}
