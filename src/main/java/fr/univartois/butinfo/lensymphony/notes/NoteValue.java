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

package fr.univartois.butinfo.lensymphony.notes;

/**
 * The NoteValue enumeration represents standard musical note values, from whole to 256th
 * notes.
 * Each note value is associated with its fraction of a whole note, which can be used to
 * calculate the duration of the note given a tempo in beats per minute (BPM).
 * It is also associated with a string type for an easier mapping from MusicXML or other
 * formats.
 *
 * @author Romain Wallon
 *
 * @version 0.1.0
 */
public enum NoteValue {

    /**
     * The whole note value.
     */
    WHOLE(1.0, "whole"),

    /**
     * The half note value.
     */
    HALF(0.5, "half"),

    /**
     * The quarter note value.
     */
    QUARTER(0.25, "quarter"),

    /**
     * The eighth note value.
     */
    EIGHTH(0.125, "eighth"),

    /**
     * The sixteenth note value.
     */
    SIXTEENTH(0.0625, "16th"),

    /**
     * The thirty-second note value.
     */
    THIRTY_SECOND(0.03125, "32nd"),

    /**
     * The sixty-fourth note value.
     */
    SIXTY_FOURTH(0.015625, "64th"),

    /**
     * The one hundred twenty-eighth note value.
     */
    ONE_HUNDRED_TWENTY_EIGHTH(0.0078125, "128th"),

    /**
     * The two hundred fifty-sixth note value.
     */
    TWO_HUNDRED_FIFTY_SIXTH(0.00390625, "256th");

    /**
     * The fraction of a whole note that this value represents.
     */
    private final double fractionOfWhole;

    /**
     * The string type associated with this note value.
     */
    private final String type;

    /**
     * Creates a new NoteValue.
     *
     * @param fractionOfWhole The fraction of a whole note that the value represents.
     * @param type The string type associated with the note value.
     */
    NoteValue(double fractionOfWhole, String type) {
        this.fractionOfWhole = fractionOfWhole;
        this.type = type;
    }

    /**
     * Gives the duration (in milliseconds) of a note having this value, given a tempo in
     * beats per minute (BPM).
     *
     * @param tempo The tempo in beats per minute (BPM).
     *
     * @return The duration of the note in milliseconds.
     */
    public int duration(int tempo) {
        double wholeNoteDuration = 60000.0 * 4 / tempo;
        return (int) (wholeNoteDuration * fractionOfWhole);
    }

    /**
     * Returns the NoteValue corresponding to the given string type.
     *
     * @param type The string type of the note value.
     *
     * @return The NoteValue corresponding to the given string type.
     *
     * @throws IllegalArgumentException If no NoteValue with the given type exists.
     */
    public static NoteValue fromString(String type) {
        for (NoteValue value : NoteValue.values()) {
            if (value.type.equalsIgnoreCase(type)) {
                return value;
            }
        }
        throw new IllegalArgumentException("No NoteValue with type " + type);
    }

}
