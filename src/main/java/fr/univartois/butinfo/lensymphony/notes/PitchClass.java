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
 * The PitchClass enumeration represents the twelve pitch classes for musical notes.
 * Basically, it represents the possible names for a note, independently of its octave.
 *
 * @author Romain Wallon
 *
 * @version 0.1.0
 */
public enum PitchClass {

    /**
     * The pitch class for C notes.
     */
    C,

    /**
     * The pitch class for C#/Db notes.
     */
    C_SHARP_D_FLAT,

    /**
     * The pitch class for D notes.
     */
    D,

    /**
     * The pitch class for D#/Eb notes.
     */
    D_SHARP_E_FLAT,

    /**
     * The pitch class for E notes.
     */
    E,

    /**
     * The pitch class for F notes.
     */
    F,

    /**
     * The pitch class for F#/Gb notes.
     */
    F_SHARP_G_FLAT,

    /**
     * The pitch class for G notes.
     */
    G,

    /**
     * The pitch class for G#/Ab notes.
     */
    G_SHARP_A_FLAT,

    /**
     * The pitch class for A notes.
     */
    A,

    /**
     * The pitch class for A#/Bb notes.
     */
    A_SHARP_B_FLAT,

    /**
     * The pitch class for B notes.
     */
    B;

    /**
     * Returns the PitchClass corresponding to the given name.
     * The name is case-insensitive and can be either the sharp or flat representation of
     * the note.
     *
     * @param name The name of the pitch class (e.g., {@code "C"}, {@code "C#"},
     *        {@code "Db"}, etc.).
     *
     * @return The PitchClass corresponding to the given name.
     */
    public static PitchClass fromName(String name) {
        return switch (name.toUpperCase()) {
            case "C" -> C;
            case "C#", "Db" -> C_SHARP_D_FLAT;
            case "D" -> D;
            case "D#", "Eb" -> D_SHARP_E_FLAT;
            case "E" -> E;
            case "F" -> F;
            case "F#", "Gb" -> F_SHARP_G_FLAT;
            case "G" -> G;
            case "G#", "Ab" -> G_SHARP_A_FLAT;
            case "A" -> A;
            case "A#", "Bb" -> A_SHARP_B_FLAT;
            case "B" -> B;
            default -> throw new IllegalArgumentException("No PitchClass with name " + name);
        };
    }

}
