/**
 * Ce logiciel est distribué à des fins éducatives.
 *
 * Il est fourni "tel quel", sans garantie d’aucune sorte, explicite
 * ou implicite, notamment sans garantie de qualité marchande, d’adéquation
 * à un usage particulier et d’absence de contrefaçon.
 * En aucun cas, les auteurs ou titulaires du droit d’auteur ne seront
 * responsables de tout dommage, réclamation ou autre responsabilité, que ce
 * soit dans le cadre d’un contrat, d’un délit ou autre, en provenance de,
 * consécutif à ou en relation avec le logiciel ou son utilisation, ou avec
 * d’autres éléments du logiciel.
 *
 * (c) 2025 Romain Wallon - Université d'Artois.
 * Tous droits réservés.
 */

package fr.univartois.butinfo.lensymphony.synthesizer;

import static fr.univartois.butinfo.lensymphony.synthesizer.NoteSynthesizer.SAMPLE_RATE;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

/**
 * The IMusicSynthesizer interface defines the methods required to synthesize a sequence
 * of notes.
 *
 * @author Romain Wallon
 *
 * @version 0.1.0
 */
public interface IMusicSynthesizer {

    /**
     * Generates the audio samples for the sequence of notes, and stores them for later
     * playback or saving, encoded as a byte array.
     */
    void synthesize();

    /**
     * Returns the synthesized audio data as a byte array.
     * If the audio stream has not been synthesized yet, this method returns an empty
     * array.
     *
     * @return The synthesized audio data as a byte array.
     *
     * @see #synthesize()
     */
    byte[] getAudioData();

    /**
     * Plays the synthesized audio stream.
     * If the audio stream has not been synthesized yet, this method will not play
     * anything.
     *
     * @throws LineUnavailableException If an audio line cannot be opened.
     *
     * @see #synthesize()
     */
    default void play() throws LineUnavailableException {
        AudioFormat format = new AudioFormat(SAMPLE_RATE, Short.SIZE, 1, true, false);
        try (SourceDataLine line = AudioSystem.getSourceDataLine(format)) {
            line.open(format);
            line.start();
            byte[] audioData = getAudioData();
            line.write(audioData, 0, audioData.length);
            line.drain();
        }
    }

    /**
     * Saves the synthesized audio stream to a WAV file.
     * If the audio stream has not been synthesized yet, the resulting file will be empty.
     *
     * @param filename The name of the WAV file to save the audio stream to.
     *
     * @throws IOException If an I/O error occurs while writing the file.
     *
     * @see #synthesize()
     */
    default void save(String filename) throws IOException {
        AudioFormat format = new AudioFormat(SAMPLE_RATE, Short.SIZE, 1, true, false);
        byte[] audioData = getAudioData();
        ByteArrayInputStream bais = new ByteArrayInputStream(audioData);
        AudioInputStream ais = new AudioInputStream(bais, format, audioData.length);
        AudioSystem.write(ais, AudioFileFormat.Type.WAVE, new File(filename));
    }

}
