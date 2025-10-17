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

import java.util.EnumMap;
import java.util.Map;

/**
 * The NotePitch class represents the pitch of a musical note, defined by its
 * {@link PitchClass} and its octave.
 * It corresponds to the exact frequency of the note.
 * Each NotePitch instance is unique for a given pitch class and octave combination.
 *
 * @author Romain Wallon
 *
 * @version 0.1.0
 */
public final class NotePitch {

    /**
     * The number of supported octaves.
     */
    private static final int NB_OCTAVES = 9;

    /**
     * The map storing the NotePitch instances for each pitch class and octave
     * combination.
     */
    private static final Map<PitchClass, NotePitch[]> NOTE_PITCHES = new EnumMap<>(PitchClass.class);

    /**
     * The pitch class of the note (C, D, E, etc.).
     */
    private PitchClass pitchClass;

    /**
     * The octave of the note (0 to 8).
     */
    private int octave;

    /**
     * The frequency of the note (in Hz), calculated from the pitch class and octave.
     */
    private double frequency;

    /**
     * Creates a new NotePitch.
     *
     * @param pitchClass The pitch class of the note (C, D, E, etc.).
     * @param octave The octave of the note (0 to 8).
     * @param frequency The frequency of the note (in Hz), calculated from the pitch class
     *        and octave.
     */
    private NotePitch(PitchClass pitchClass, int octave, double frequency) {
        this.pitchClass = pitchClass;
        this.octave = octave;
        this.frequency = frequency;
    }

    /**
     * Retrieves the NotePitch instance for the given pitch class and octave.
     *
     * @param pitchClass The pitch class of the note (C, D, E, etc.).
     * @param octave The octave of the note (0 to 8).
     *
     * @return The NotePitch instance for the given pitch class and octave.
     */
    public static NotePitch of(PitchClass pitchClass, int octave) {
        return of(pitchClass, octave, 0);
    }

    /**
     * Retrieves the NotePitch instance for the given pitch class, octave and alteration.
     *
     * @param pitchClass The pitch class of the note (C, D, E, etc.).
     * @param octave The octave of the note (0 to 8).
     * @param alteration The alteration of the note (number of semitones to add or remove).
     *
     * @return The NotePitch instance for the given pitch class and octave.
     */
    public static NotePitch of(PitchClass pitchClass, int octave, int alteration) {
        PitchClass[] classes = PitchClass.values();
        int realOrdinal = pitchClass.ordinal() + alteration;
        int realOctave = octave;

        // Adjusting the octave if the ordinal goes out of bounds.
        // In this case, the alteration has changed the octave.
        if (realOrdinal < 0) {
            realOctave--;
            realOrdinal += classes.length;

        } else if (realOrdinal >= classes.length) {
            realOctave++;
            realOrdinal -= classes.length;
        }
        PitchClass realPitchClass = classes[realOrdinal];

        // Validating the octave range.
        if ((realOctave < 0) || (NB_OCTAVES <= realOctave)) {
            throw new IllegalArgumentException("Pitch is too low or too high");
        }

        // Retrieving or creating the NotePitch instance.
        // The frequency is computed w.r.t. A4 = 440 Hz (using the equal temperament).
        NotePitch[] notes = NOTE_PITCHES.computeIfAbsent(realPitchClass, k -> new NotePitch[NB_OCTAVES]);
        if (notes[realOctave] == null) {
            int a = PitchClass.A.ordinal();
            double frequency = 440.0 * Math.pow(2, (realOrdinal - a + (realOctave - 4) * 12) / 12.0);
            notes[realOctave] = new NotePitch(realPitchClass, realOctave, frequency);
        }
        return notes[realOctave];
    }

    /**
     * Alters this pitch by the given number of semitones.
     *
     * @param alteration The number of semitones to add (positive) or remove (negative).
     *
     * @return The altered NotePitch instance.
     */
    public NotePitch alter(int alteration) {
        return NotePitch.of(pitchClass, octave, alteration);
    }

    /**
     * Alters this pitch down by one semitone.
     * This corresponds to a flat alteration.
     *
     * @return The altered NotePitch instance.
     *
     * @see #alter(int)
     */
    public NotePitch flat() {
        return alter(-1);
    }

    /**
     * Alters this pitch up by one semitone.
     * This corresponds to a sharp alteration.
     *
     * @return The altered NotePitch instance.
     *
     * @see #alter(int)
     */
    public NotePitch sharp() {
        return alter(1);
    }

    /**
     * Gives the frequency of this note pitch (in Hz).
     *
     * @return The frequency of this note pitch (in Hz).
     */
    public double frequency() {
        return frequency;
    }

}
