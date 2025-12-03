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

import java.io.File;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import fr.univartois.butinfo.lensymphony.musicxml.MusicXMLSaxParser;
import fr.univartois.butinfo.lensymphony.notes.*;
import fr.univartois.butinfo.lensymphony.synthesizer.*;
import picocli.CommandLine;

/**
 * The LenSymphony class provides a simple application to synthesize and play music from a
 * MusicXML file.
 * This file must be provided as a command line argument to the application.
 *
 * @author Romain Wallon
 *
 * @version 0.1.0
 */
public final class LenSymphony {

    /**
     * The note factory used to create notes.
     * TODO: You have to set it with your own implementation.
     */
    private static AbstractNoteFactory noteFactory = NoteFactory.getInstance();

    /**
     * The note synthesizer used to synthesize notes.
     * TODO: You have to set it with your own implementation.
     */
    private static NoteSynthesizer noteSynthesizer = new PureSound();

    /**
     * Disables instantiation.
     */
    private LenSymphony() {
        throw new AssertionError("No LenSymphony instances for you!");
    }

    /**
     * The main method of the application.
     *
     * @param args The command line arguments, which must contain exactly the path to the
     *        MusicXML file to play.
     *
     * @throws Exception If any error occurs.
     */
    public static void main(String[] args) throws Exception {
        MusicCommandLine cmd = new MusicCommandLine();
        new CommandLine(cmd).execute(args);


            if (cmd.getInput() == null) {
                // The command line is invalid.
                throw new IllegalArgumentException("MusicXML file is required as single argument");
            }


            // Creating the SAX parser.
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();

            // Parsing the MusicXML file.
            MusicXMLSaxParser handler = new MusicXMLSaxParser(noteFactory);
            saxParser.parse(new File(cmd.getInput()), handler);

            // Creating a musical score from the parsed data.

            // Synthesizing and playing the music.
            MusicPiece musicPiece = new MusicPiece(handler.getTempo());

            Map<String, List<Note>> listePartitions = handler.getParts();


            List<String> voices = cmd.getVoices();
            if (voices != null) {
                for (String voice : voices) {
                    String[] split = voice.split(":");
                    String part = split[0];
                    String instrument = split[1].toUpperCase();
                    List<Note> notes = listePartitions.get(part);
                    if (notes == null) {
                        continue;
                    }
                    Score score = new Score(Instruments.valueOf(instrument), notes);
                    musicPiece.addScore(score);
                    listePartitions.remove(part);
                }
            }

            for (Map.Entry<String, List<Note>> entry : listePartitions.entrySet()) {
                List<Note> notes = entry.getValue();
                if (notes == null) {
                    continue;
                }
                Score score = new Score(Instruments.XYLOPHONE, notes); //Default instrument
                musicPiece.addScore(score);
            }

            MultipleScoreSynthesizer composite = new MultipleScoreSynthesizer();

            for (Score score : musicPiece.getScores()) {
                NoteSynthesizer ns = score.getInstrument().getSynthesizer();
                SimpleMusicSynthesizer sms = new SimpleMusicSynthesizer(musicPiece.getTempo(), score.getNotes(), ns,0.5);
                composite.add(sms);
            }

            composite.synthesize();
            if (cmd.getOutput() != null) {
                composite.save(cmd.getOutput());
            }
            if (cmd.isPlay()) {
                composite.play();
            }

    }

}
