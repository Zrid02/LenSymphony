# LenSymphony - A simple music synthesizer library developed in Lens

This project provides a *Java* library for representing and synthesizing
musical pieces.
It allows users to define musical notes, silences, and staffs, and to
synthesize the sound of a complete musical piece using virtual instruments.

## Overview

This library is composed of the following classes:

```plantuml
hide empty members

' -------------------- '
' Note representations '
' -------------------- '

enum NoteValue {
    + {static} WHOLE
    + {static} HALF
    + {static} QUARTER
    + {static} EIGHTH
    + {static} SIXTEENTH
    + {static} THIRTY_SECOND
    + {static} SIXTY_FOURTH
    + {static} ONE_HUNDRED_TWENTY_EIGHTH
    + {static} TWO_HUNDRED_FIFTY_SIXTH
    - fractionOfWhole
    - type

    ~ NoteValue(fractionOfWhole: double, type: String)
    + duration(tempo: int): int
    + {static} fromString(type: String): NoteValue
}

enum PitchClass {
    + {static} C
    + {static} C_SHARP_D_FLAT
    + {static} D
    + {static} D_SHARP_E_FLAT
    + {static} E
    + {static} F
    + {static} F_SHARP_G_FLAT
    + {static} G
    + {static} G_SHARP_A_FLAT
    + {static} A
    + {static} A_SHARP_B_FLAT
    + {static} B

    + {static} fromName(name: String): PitchClass
}

class NotePitch {
    - {static} NB_OCTAVES: int
    - {static} NOTE_PITCHES: Map<PitchClass, NotePitch[]>
    - pitchClass: PitchClass
    - octave: int
    - frequency: double

    - NotePitch(pitchClass: PitchClass, octave: int, frequency: double)
    + {static} of(pitchClass: PitchClass, octave: int): NotePitch
    + {static} of(pitchClass: PitchClass, octave: int, alteration: int): NotePitch
    + alter(alteration: int): NotePitch
    + flat(): NotePitch
    + sharp(): NotePitch
    + frequency(): double
}

interface Note {
    + {abstract} getFrequency(): double
    + {abstract} getDuration(tempo: int): int
}

interface AbstractNoteFactory {
    + {abstract} createRest(value: NoteValue): Note
    + {abstract} createNote(pitch: NotePitch, value: NoteValue): Note
    + {abstract} createDottedNote(note: Note): Note
    + {abstract} createFermataOn(note: Note): Note
    + {abstract} createTiedNotes(notes: Note[]): Note
    + {abstract} createTiedNotes(notes: List<Note>): Note
}

NotePitch o-- PitchClass
AbstractNoteFactory --> Note : << creates >>

' ------- '
' Parsing '
' ------- '

class MusicXMLSaxParser {
    + MusicXMLSaxParser(noteFactory: AbstractNoteFactory)
    + getTempo(): int
    + getParts(): Map<String,List<Note>>
    + getNotes(partId: String): List<Note>
}

MusicXMLSaxParser --> AbstractNoteFactory : << uses >>

' --------------- '
' Sound synthesis '
' --------------- '

interface NoteSynthesizer {
    + {static} SAMPLE_RATE: int
    + {abstract} synthesize(note: Note, tempo: int, volume: double): double[]
}

class PureSound implements NoteSynthesizer{
    + {static} SAMPLE_RATE: int
    + synthesize(note: Note, tempo: int, volume: oduble): double[]
}

interface MusicSynthesizer {
    + {abstract} synthesize(): void
    + {abstract} getSamples(): double[]
    + {abstract} getAudioData(): byte[]
    + {abstract} play(): void
    + {abstract} save(filename: String): void
}

class SimpleMusicSynthesizer implements MusicSynthesizer {
    - {static} DEFAULT_VOLUME: double
    - notes: Iterable<Note>
    - synthesizer: NoteSynthesizer
    - tempo: int
    - samples: double[]

    + SimpleMusicSynthesizer(tempo: int, notes: Iterable<Note>, synthetizer: NoteSynthesizer)
    + synthesize(): void
    + getSamples(): double[]
}

SimpleMusicSynthesizer o-- "*" Note
SimpleMusicSynthesizer o-- "1" NoteSynthesizer

' ------------ '
' Main classes '
' ------------ '

class Example {
    - {static} noteFactory: AbstractNoteFactory
    - {static} noteSynthesizer: NoteSynthesizer
    + {static} main(args: String[]): void
}

class LenSymphony {
    - {static} noteFactory: AbstractNoteFactory
    - {static} noteSynthesizer: NoteSynthesizer
    + {static} main(args: String[]): void
}

Example --> AbstractNoteFactory : << uses >>
Example --> MusicXMLSaxParser : << uses >>
Example --> NoteSynthesizer : << uses >>
LenSymphony --> AbstractNoteFactory : << uses >>
LenSymphony --> MusicXMLSaxParser : << uses >>
LenSymphony --> NoteSynthesizer : << uses >>
```

## Feature list

| Features                                               | Design Pattern(s) (?)       | Author(s) |
|--------------------------------------------------------|-----------------------------|-----------|
| Representation of a note's pitch (name + octave)       |                             |           |
| Representation of a note/silence value                 |                             |           |
| Representation of a musical note                       |                             |           |
| Representation of a silence                            |                             |           |
| Representation of a point on a note                    |                             |           |
| Representation of a tie between notes                  |                             |           |
| Representation of a staff                              |                             |           |
| Traversal of notes/silences in a staff                 |                             |           |
| Representation of a musical piece                      |                             |           |
| Creation of musical elements (notes, silences)         |                             |           |
| Generation of the "pure" sound for a note              |                             |           |
| Addition of harmonics to the sound of a note           |                             |           |
| Application of an ADSR envelope to the sound of a note |                             |           |
| Application of a vibrato to the sound of a note        |                             |           |
| Addition of random noise to the sound of a note        |                             |           |
| Synthesis of the bass drum sound                       |                             |           |
| Synthesis of the snare drum sound                      |                             |           |
| Synthesis of the cymbal sound                          |                             |           |
| Synthesis of the triangle sound                        |                             |           |
| Synthesis of the timpani sound                         |                             |           |
| Synthesis of the xylophone sound                       |                             |           |
| Definition of virtual instruments                      |                             |           |
| Synthesis of the ensemble piece sound                  |                             |           |
| Command line management                                |                             |           |

## Team

This project has been developed by:

- Your Name Here
- Your Name Here
- Your Name Here
- Your Name Here
