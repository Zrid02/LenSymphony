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

package fr.univartois.butinfo.lensymphony.musicxml;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Consumer;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import fr.univartois.butinfo.lensymphony.notes.AbstractNoteFactory;
import fr.univartois.butinfo.lensymphony.notes.Note;
import fr.univartois.butinfo.lensymphony.notes.NotePitch;
import fr.univartois.butinfo.lensymphony.notes.NoteValue;
import fr.univartois.butinfo.lensymphony.notes.PitchClass;

/**
 * The MusicXMLSaxParser is a SAX parser for MusicXML files.
 * It extracts all the information needed to play the music, (e.g., notes, tempo, etc.).
 * It is neither a complete nor a robust MusicXML parser: its sole purpose is to provide a
 * sufficiently rich representation of a piece of music to be played by the synthesizer.
 *
 * @author Romain Wallon
 *
 * @version 0.1.0
 */
public final class MusicXMLSaxParser extends DefaultHandler {

    /**
     * The factory to create the notes of the parsed music.
     */
    private final AbstractNoteFactory noteFactory;

    /**
     * The number of beats per measure.
     */
    private int beats;

    /**
     * The tempo of the music (in beats per minute).
     */
    private int tempo = 60;

    /**
     * The map associating each part (given by its ID) to the list of notes in that part.
     */
    private Map<String, List<Note>> parts = new TreeMap<>();

    /**
     * The identifier of the current part being parsed (used during parsing).
     */
    private String currentPartId = null;

    /**
     * The current chromatic transposition (used during parsing).
     */
    private int currentChromaticTransposition = 0;

    /**
     * The list of notes parsed from the MusicXML file.
     */
    private List<Note> notes;

    /**
     * The current tie being parsed (used during parsing).
     */
    private List<Note> currentTie;

    /**
     * Whether a tie has been started and not ended (used during parsing).
     */
    private boolean inTie;

    /**
     * The pitch class of the current note (used during parsing).
     */
    private PitchClass currentPitchClass = null;

    /**
     * The alteration of the current note (used during parsing).
     */
    private int currentAlter = 0;

    /**
     * The octave of the current note (used during parsing).
     */
    private int currentOctave = -1;

    /**
     * The pitch of the current note (used during parsing).
     */
    private NotePitch currentPitch = null;

    /**
     * The value of the current note (used during parsing).
     */
    private NoteValue currentValue = null;

    /**
     * The current note (used during parsing).
     */
    private Note currentNote = null;

    /**
     * The identifier of the staff of the current note (used during parsing).
     */
    private int currentStaff = 0;

    /**
     * The text buffer used to accumulate character data between XML tags.
     */
    private StringBuilder textBuffer = new StringBuilder();

    /**
     * The map of handlers for the start of XML elements.
     */
    private Map<String, Consumer<Attributes>> startElementHandlers = Map.of(
            "sound", this::startSound,
            "part", this::startPart,
            "tie", this::startTie,
            "rest", this::startRest,
            "note", this::startNote);

    /**
     * The map of handlers for the end of XML elements.
     */
    private Map<String, Runnable> endElementHandlers = Map.ofEntries(
            Map.entry("beats", this::endBeats),
            Map.entry("chromatic", this::endChromatic),
            Map.entry("step", this::endStep),
            Map.entry("display-step", this::endStep),
            Map.entry("alter", this::endAlter),
            Map.entry("octave", this::endOctave),
            Map.entry("display-octave", this::endOctave),
            Map.entry("pitch", this::endPitch),
            Map.entry("unpitched", this::endPitch),
            Map.entry("type", this::endType),
            Map.entry("dot", this::endDot),
            Map.entry("fermata", this::endFermata),
            Map.entry("staff", this::endStaff),
            Map.entry("note", this::endNote));

    /**
     * Creates a new MusicXMLSaxParser.
     *
     * @param noteFactory The factory to create the notes of the parsed music.
     */
    public MusicXMLSaxParser(AbstractNoteFactory noteFactory) {
        this.noteFactory = noteFactory;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.xml.sax.helpers.DefaultHandler#startElement(java.lang.String,
     * java.lang.String, java.lang.String, org.xml.sax.Attributes)
     */
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        textBuffer.setLength(0);

        if (startElementHandlers.containsKey(qName)) {
            startElementHandlers.get(qName).accept(attributes);
        }
    }

    /**
     * Reads the tempo from the {@code sound} element.
     *
     * @param attributes The attributes of the {@code sound} element.
     */
    private void startSound(Attributes attributes) {
        String tempoAttr = attributes.getValue("tempo");
        if (tempoAttr != null) {
            tempo = Integer.parseInt(tempoAttr);
        }
    }

    /**
     * Initializes the state for a new {@code part} element.
     *
     * @param attributes The attributes of the {@code part} element.
     */
    private void startPart(Attributes attributes) {
        currentPartId = attributes.getValue("id");
        currentChromaticTransposition = 0;
        notes = null;
    }

    /**
     * Initializes the state for a new {@code tie} element.
     *
     * @param attributes The attributes of the {@code tie} element.
     */
    private void startTie(Attributes attributes) {
        if ("start".equals(attributes.getValue("type"))) {
            // Starting a new tie, or continuing an existing one.
            if (currentTie == null) {
                currentTie = new ArrayList<>();
            }
            inTie = true;

        } else if ("stop".equals(attributes.getValue("type"))) {
            // Ending the current tie.
            inTie = false;

        }
    }

    /**
     * Adds a full rest when a {@code rest} element is started with its {@code measure}
     * attribute set to {@code "yes"}.
     *
     * @param attributes The attributes of the {@code rest} element.
     */
    private void startRest(Attributes attributes) {
        if ("yes".equals(attributes.getValue("measure"))) {
            currentNote = noteFactory.createTiedNotes(
                    Collections.nCopies(beats, noteFactory.createRest(NoteValue.QUARTER)));
        }
    }

    /**
     * Initializes the state for a new {@code note} element.
     *
     * @param attributes The attributes of the {@code note} element.
     */
    private void startNote(Attributes attributes) {
        currentPitchClass = null;
        currentAlter = 0;
        currentOctave = -1;
        currentPitch = null;
        currentValue = null;
        currentStaff = 0;
        currentNote = null;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.xml.sax.helpers.DefaultHandler#characters(char[], int, int)
     */
    @Override
    public void characters(char[] ch, int start, int length) {
        textBuffer.append(ch, start, length);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.xml.sax.helpers.DefaultHandler#endElement(java.lang.String,
     * java.lang.String, java.lang.String)
     */
    @Override
    public void endElement(String uri, String localName, String qName) {
        if (endElementHandlers.containsKey(qName)) {
            endElementHandlers.get(qName).run();
        }
    }

    /**
     * Extracts the number of beats per measure from the content of a {@code beats}
     * element.
     */
    private void endBeats() {
        String text = textBuffer.toString().trim();
        beats = Integer.parseInt(text);
    }

    /**
     * Extracts the chromatic transposition from the content of a {@code chromatic}
     * element.
     */
    private void endChromatic() {
        String text = textBuffer.toString().trim();
        currentChromaticTransposition = Integer.parseInt(text);
    }

    /**
     * Extracts the pitch class from the content of a {@code step} element.
     */
    private void endStep() {
        String text = textBuffer.toString().trim();
        currentPitchClass = PitchClass.valueOf(text.toUpperCase());
    }

    /**
     * Extracts an alteration from the content of an {@code alter} element.
     */
    private void endAlter() {
        String text = textBuffer.toString().trim();
        currentAlter = Integer.parseInt(text);
    }

    /**
     * Extracts the octave from the content of an {@code octave} element.
     */
    private void endOctave() {
        String text = textBuffer.toString().trim();
        currentOctave = Integer.parseInt(text);
    }

    /**
     * Finalizes the pitch of the current note when a {@code pitch} element is closed.
     */
    private void endPitch() {
        currentPitch = NotePitch.of(currentPitchClass, currentOctave,
                currentChromaticTransposition + currentAlter);
    }

    /**
     * Extracts the note value from the content of a {@code type} element.
     */
    private void endType() {
        String text = textBuffer.toString().trim();
        currentValue = NoteValue.fromString(text);

        if (currentPitch == null) {
            currentNote = noteFactory.createRest(currentValue);
        } else {
            currentNote = noteFactory.createNote(currentPitch, currentValue);
        }
    }

    /**
     * Adds a dot to the current note when a {@code dot} element is closed.
     */
    private void endDot() {
        currentNote = noteFactory.createDottedNote(currentNote);
    }

    /**
     * Adds a fermata to the current note when a {@code fermata} element is closed.
     */
    private void endFermata() {
        currentNote = noteFactory.createFermataOn(currentNote);
    }

    /**
     * Extracts the staff identifier from the content of a {@code staff} element.
     */
    private void endStaff() {
        String text = textBuffer.toString().trim();
        currentStaff = Integer.parseInt(text);
        String staffId = currentPartId + "." + currentStaff;
        notes = parts.computeIfAbsent(staffId, k -> new ArrayList<>());
    }

    /**
     * Finalizes the current note and adds it to the list of notes when a {@code note}
     * element is closed.
     */
    private void endNote() {
        if (inTie) {
            // The note is not added yet: the tie is not over.
            currentTie.add(currentNote);
            return;
        }

        if (currentTie != null) {
            // The tie has just been ended.
            // The note, is added to the tie, and a tied note is created.
            currentTie.add(currentNote);
            currentNote = noteFactory.createTiedNotes(currentTie);
            currentTie = null;
        }

        // Adding the note to the list of notes.
        if (notes == null) {
            // No staff defined yet: using a default one.
            notes = parts.computeIfAbsent(currentPartId, k -> new ArrayList<>());
        }
        notes.add(currentNote);
    }

    /**
     * Gives the tempo of the music (in beats per minute) read from the MusicXML file.
     *
     * @return The tempo of the music (in beats per minute).
     */
    public int getTempo() {
        return tempo;
    }

    /**
     * Gives the map associating each part (given by its ID) to the list of notes in that
     * part.
     *
     * @return The map of the parts.
     */
    public Map<String, List<Note>> getParts() {
        return parts;
    }

    /**
     * Gives the list of notes for a given part.
     *
     * @param partId The ID of the part.
     *
     * @return The list of notes for the given part.
     */
    public List<Note> getNotes(String partId) {
        return parts.get(partId);
    }

    /**
     * Gives the list of notes parsed from the MusicXML file.
     *
     * @return The list of parsed notes.
     */
    public List<Note> getNotes() {
        return notes;
    }

}
