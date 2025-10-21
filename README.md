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

class PitchedNote {
    - pitch: NotePitch
    - value: NoteValue
    + PitchedNote(pitch: NotePitch, value: NoteValue)
    + getFrequency(): double
    + getDuration(tempo: int): int
    + pitch(): NotePitch
    + value(): NoteValue
    + equals(o: Object): boolean
    + hashCode(): int
    + toString(): String
}

PitchedNote ..|> Note
PitchedNote --> NotePitch : uses
PitchedNote --> NoteValue : uses

abstract class NoteDecorator {
    # note: Note
    # NoteDecorator(note: Note)
    + getFrequency(): double
    + getDuration(tempo: int): int
}

class DottedNote {
    + DottedNote(note: Note)
    + getDuration(tempo: int): int
}

NoteDecorator ..|> Note
NoteDecorator o--> Note : decorates
DottedNote --|> NoteDecorator

class Rest {
    - noteValue: NoteValue
    - dots: int
    - tiedNotes: List\<Note\>
    + Rest(noteValue: NoteValue)
    + Rest(noteValue: NoteValue, dots: int)
    + addDot(): void
    + addDots(n: int): void
    + tieWith(other: Note): Rest
    + getTiedNotes(): List\<Note\>
    + getFrequency(): double
    + getDuration(tempo: int): int
}

Rest ..|> Note
Rest --> NoteValue : uses

package "tests" {
  class PitchedNoteTest <<test>> {
    + a4Quarter_at120bpm()
    + c4Half_at90bpm()
    + e5Eighth_at120bpm()
  }
}

PitchedNoteTest ..> PitchedNote : tests

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

' Score (stave) representation
class Score {
  - notes: List\<Note\>
  - instrument: Instruments
  + Score(instrument: Instruments, notes: List\<Note\>)
  + getInstrument(): Instruments
  + addNote(note: Note): void
  + iterator(): Iterator\<Note\>
}
enum Instruments {
  + BASS_DRUM
  + SNARE_DRUM
  + CYMBAL
  + TRIANGLE
  + TIMPANI
  + XYLOPHONE
}

Score o-- "*" Note
Score --> Instruments : uses

package "tests" {
  class ScoreTest <<test>> {
    + constructorSetsInstrument()
    + iteratorIsEmptyWhenNoNotes()
    + addNote()
  }
}

ScoreTest ..> Score : tests

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
    + {static} SAMPLE_RATE: int = 44100
    + {abstract} synthesize(note: Note, tempo: int, volume: double): double[]
}

class PureSound {
    + synthesize(note: Note, tempo: int, volume: double): double[]
}

abstract class NoteSynthesizerDecorator {
    # synthesizer: NoteSynthesizer
    
    # NoteSynthesizerDecorator(synthesizer: NoteSynthesizer)
    + synthesize(note: Note, tempo: int, volume: double): double[]
}

class HarmonicSynthesizer {
    - numberOfHarmonics: int
    
    + HarmonicSynthesizer(synthesizer: NoteSynthesizer, numberOfHarmonics: int)
    + synthesize(note: Note, tempo: int, volume: double): double[]
}

' Relations Synthesizers
PureSound ..|> NoteSynthesizer

NoteSynthesizerDecorator ..|> NoteSynthesizer
NoteSynthesizerDecorator o--> NoteSynthesizer : decorates

HarmonicSynthesizer --|> NoteSynthesizerDecorator


class MusicPiece implements Iterable{
    - scores: ArrayList<Score>
    + addScore(score: Score ): void
    + iterator(): Iterator<Score>
}

MusicPiece --> "liste" Score
interface MusicSynthesizer {
    + {abstract} synthesize(): void
    + {abstract} getSamples(): double[]
    + {abstract} getAudioData(): byte[]
    + {abstract} play(): void
    + {abstract} save(filename: String): void
}

class SimpleMusicSynthesizer {
    - {static} DEFAULT_VOLUME: double
    - notes: Iterable<Note>
    - synthesizer: NoteSynthesizer
    - tempo: int
    - samples: double[]

    + SimpleMusicSynthesizer(tempo: int, notes: Iterable<Note>, synthesizer: NoteSynthesizer)
    + synthesize(): void
    + getSamples(): double[]
}

class MultipleScoreSynthesizer {
  + add(synth: MusicSynthesizer): void
  + synthesize(): void
  + getSamples(): double[]
}

MultipleScoreSynthesizer ..|> MusicSynthesizer
MultipleScoreSynthesizer o-- "*" MusicSynthesizer : combines

SimpleMusicSynthesizer ..|> MusicSynthesizer
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

class NoteFactory {
    - {static} INSTANCE: NoteFactory
    - NoteFactory()
    + {static} getInstance(): NoteFactory
    + createRest(value: NoteValue): Note
    + createNote(pitch: NotePitch, value: NoteValue): Note
    + createDottedNote(note: Note): Note
    + createFermataOn(note: Note): Note
    + createTiedNotes(notes: Note[]): Note
    + createTiedNotes(notes: List<Note>): Note
}

class TiedNotes implements Note {
    - notes: List<Note>
    + TiedNotes(notes: List<Note>)
    + getFrequency(): double
    + getDuration(tempo: int): int
}

class FermataNote {
    + FermataNote(note: Note)
    + getDuration(tempo: int): int
}

FermataNote --|> NoteDecorator


NoteFactory ..|> AbstractNoteFactory

Example --> AbstractNoteFactory : << uses >>
Example --> MusicXMLSaxParser : << uses >>
Example --> NoteSynthesizer : << uses >>
LenSymphony --> AbstractNoteFactory : << uses >>
LenSymphony --> MusicXMLSaxParser : << uses >>
LenSymphony --> NoteSynthesizer : << uses >>



note right of NoteDecorator
  Decorator pattern base class.
  Delegates all operations to
  the wrapped note by default.
end note

note right of DottedNote
  Concrete decorator that
  increases duration by 50%
  (multiplies by 1.5).
end note

note right of PitchedNote
  Concrete note implementation
  combining pitch and rhythmic value.
end note

```

## Feature list

| Features                                               | Design Pattern(s) (?) | Author(s)       |
|--------------------------------------------------------|-----------------------|-----------------|
| Representation of a note's pitch (name + octave)       | None                  |                 |
| Representation of a note/silence value                 | None                  | Dutkiewicz Tom  |
| Representation of a musical note                       | Decorator             | Rabhi Nessim    |
| Representation of a silence                            | Composite             | Dassonville Ugo |
| Representation of a point on a note                    | Decorator             | Rabhi Nessim    |
| Representation of a tie between notes                  | Composite             | Dutkiewicz Tom  |
| Representation of a staff                              | Composite             |                 |
| Traversal of notes/silences in a staff                 | Iterator              |                 |
| Representation of a musical piece                      | Composite             |                 |
| Creation of musical elements (notes, silences)         | Abstract Fabric       |                 |
| Generation of the "pure" sound for a note              | Strategy              | Mouille Antoine |
| Addition of harmonics to the sound of a note           | Decorator             | Rabhi Nessim    |
| Application of an ADSR envelope to the sound of a note | Decorator             |                 |
| Application of a vibrato to the sound of a note        | Decorator             |                 |
| Addition of random noise to the sound of a note        | Decorator             |                 |
| Synthesis of the bass drum sound                       | Strategy              |                 |
| Synthesis of the snare drum sound                      | Strategy              |                 |
| Synthesis of the cymbal sound                          | Strategy              |                 |
| Synthesis of the triangle sound                        | Strategy              |                 |
| Synthesis of the timpani sound                         | Strategy              |                 |
| Synthesis of the xylophone sound                       | Strategy              |                 |
| Definition of virtual instruments                      | Abstract Fabric       |                 |
| Synthesis of the ensemble piece sound                  | Composite             |                 |
| Command line management                                | Singleton             |                 |

## Team

This project has been developed by:

- Dassonville Ugo  
- Mouille Antoine
- Rabhi Nessim
- Dutkiewicz Tom