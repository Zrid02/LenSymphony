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
    - fractionOfWhole: double
    - type: String

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

class FermataNote {
    + FermataNote(note: Note)
    + getDuration(tempo: int): int
}

class Rest {
    - noteValue: NoteValue
    - dots: int
    - tiedNotes: List<Note>
    
    + Rest(noteValue: NoteValue)
    + Rest(noteValue: NoteValue, dots: int)
    + addDot(): void
    + addDots(n: int): void
    + tieWith(other: Note): Rest
    + getTiedNotes(): List<Note>
    + getFrequency(): double
    + getDuration(tempo: int): int
}

class TiedNotes {
    - notes: List<Note>
    
    + TiedNotes(notes: List<Note>)
    + getFrequency(): double
    + getDuration(tempo: int): int
}

' Relations Notes
PitchedNote ..|> Note
PitchedNote --> NotePitch : uses
PitchedNote --> NoteValue : uses

NoteDecorator ..|> Note
NoteDecorator o--> Note : decorates
DottedNote --|> NoteDecorator
FermataNote --|> NoteDecorator

Rest ..|> Note
Rest --> NoteValue : uses

TiedNotes ..|> Note
TiedNotes o-- "*" Note : ties

NotePitch o-- PitchClass

' ------- '
' Factory '
' ------- '

interface AbstractNoteFactory {
    + {abstract} createRest(value: NoteValue): Note
    + {abstract} createNote(pitch: NotePitch, value: NoteValue): Note
    + {abstract} createDottedNote(note: Note): Note
    + {abstract} createFermataOn(note: Note): Note
    + {abstract} createTiedNotes(notes: Note[]): Note
    + {abstract} createTiedNotes(notes: List<Note>): Note
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

NoteFactory ..|> AbstractNoteFactory
AbstractNoteFactory ..> Note : << creates >>

' ------- '
' Parsing '
' ------- '

class MusicXMLSaxParser {
    - noteFactory: AbstractNoteFactory
    - tempo: int
    - parts: Map<String, List<Note>>
    
    + MusicXMLSaxParser(noteFactory: AbstractNoteFactory)
    + getTempo(): int
    + getParts(): Map<String, List<Note>>
    + getNotes(partId: String): List<Note>
}

MusicXMLSaxParser --> AbstractNoteFactory : << uses >>

' -------------- '
' Score and Instruments '
' -------------- '

enum Instruments {
    + BASS_DRUM
    + SNARE_DRUM
    + CYMBAL
    + TRIANGLE
    + TIMPANI
    + XYLOPHONE
    - synthesizer: NoteSynthesizer
    
    + getSynthesizer(): NoteSynthesizer
    ~ Instruments(synthesizer: NoteSynthesizer)
}

class Score {
    - notes: List<Note>
    - instrument: Instruments
    
    + Score(instrument: Instruments, notes: List<Note>)
    + getInstrument(): Instruments
    + addNote(note: Note): void
    + iterator(): Iterator<Note>
}

Score o-- "*" Note
Score --> Instruments : uses
Instruments --> NoteSynthesizer : uses

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

abstract class SynthesizerDecorator {
    # SynthesizerDecorator(synthesizer: NoteSynthesizer)
}

class HarmonicSynthesizer {
    - numberOfHarmonics: int
    
    + HarmonicSynthesizer(synthesizer: NoteSynthesizer, numberOfHarmonics: int)
    + synthesize(note: Note, tempo: int, volume: double): double[]
}

class WhiteNoiseSynthesizer {
    - noiseAmplitude: double
    - random: Random
    
    + WhiteNoiseSynthesizer(synthesizer: NoteSynthesizer, noiseAmplitude: double)
    + synthesize(note: Note, tempo: int, volume: double): double[]
}

class VibratoSynthesizer {
    - depth: double
    - speed: double
    
    + VibratoSynthesizer(synthesizer: NoteSynthesizer, depth: double, speed: double)
    + synthesize(note: Note, tempo: int, volume: double): double[]
}

' Relations Synthesizers
PureSound ..|> NoteSynthesizer

NoteSynthesizerDecorator ..|> NoteSynthesizer
NoteSynthesizerDecorator o--> NoteSynthesizer : decorates

SynthesizerDecorator --|> NoteSynthesizerDecorator

HarmonicSynthesizer --|> SynthesizerDecorator
WhiteNoiseSynthesizer --|> SynthesizerDecorator
VibratoSynthesizer --|> SynthesizerDecorator

' --------------- '
' Music synthesis '
' --------------- '

class MusicPiece {
    - scores: ArrayList<Score>
    - tempo: int
    
    + addScore(score: Score): void
    + iterator(): Iterator<Score>
    + getScores(): List<Score>
    + getTempo(): int
}

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
    + getAudioData(): byte[]
    + play(): void
    + save(filename: String): void
}

class MultipleScoreSynthesizer {
    - synthesizers: List<MusicSynthesizer>
    - samples: double[]
    
    + add(synth: MusicSynthesizer): void
    + synthesize(): void
    + getSamples(): double[]
    + getAudioData(): byte[]
    + play(): void
    + save(filename: String): void
}

MusicPiece ..|> Iterable
MusicPiece o-- "*" Score

SimpleMusicSynthesizer ..|> MusicSynthesizer
SimpleMusicSynthesizer o-- "*" Note : uses
SimpleMusicSynthesizer o-- "1" NoteSynthesizer : uses

MultipleScoreSynthesizer ..|> MusicSynthesizer
MultipleScoreSynthesizer o-- "*" MusicSynthesizer : combines

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

class HarmonicSynthesizerComplex {
  - numberOfHarmonics: int
  - h: IntUnaryOperator
  - a: BiFunction\<Integer, Double, Double\>
  + HarmonicSynthesizerComplex(synthesizer: NoteSynthesizer, numberOfHarmonics: int, h: IntUnaryOperator, a: BiFunction\<Integer, Double, Double\>)
  + synthesize(note: Note, tempo: int, volume: double): double[]
}

class ADSRSynthesizer extends NoteSynthesizerDecorator{
   - attack: double
   - decay: double
   - sustain: double
   - release: double
   + ADSRSynthesizer(synthesizer: NoteSynthesizer, attack: double, decay: double, sustain: double, release: double)
   + adsrEnvelope(note: Note, tempo: int): double[]
   + synthesize(note: Note, tempo: int, volume: double): double[]
   
}


HarmonicSynthesizerComplex --|> NoteSynthesizerDecorator



package "tests" <<Rectangle>> {
  class PitchedNoteTest <<test>> {
    + a4Quarter_at120bpm()
    + c4Half_at90bpm()
    + e5Eighth_at120bpm()
  }
  
  class ScoreTest <<test>> {
    + constructorSetsInstrument()
    + iteratorIsEmptyWhenNoNotes()
    + addNote()
  }
  
  class HarmonicSynthesizerTest <<test>> {
    + createHarmonicSynthesizer()
    + synthesizeWithHarmonics()
    + differentFromPureSound()
    + nullSynthesizer_throwsException()
    + invalidHarmonics_throwsException()
  }
  
  class WhiteNoiseSynthesizerTest <<test>> {
    + createWhiteNoiseSynthesizer()
    + synthesizeWithNoise()
    + differentFromPureSound()
    + nullSynthesizer_throwsException()
    + negativeAmplitude_throwsException()
  }
  
  class VibratoSynthesizerTest <<test>> {
    + createVibratoSynthesizer()
    + synthesizeWithVibrato()
    + differentFromPureSound()
    + nullSynthesizer_throwsException()
    + negativeDepth_throwsException()
    + combineEffects()
  }
  
  class ADSRTest <<test>> {
    - adsr : ADSRSynthesizer
    - mockSynth: NoteSynthesizer
    - note: Note
    + setUp(): void
    + testAttackPhase(): void
    + testDecayPhase(): void
    + testSustainPhase(): void
    + testReleasePhase(): void
    + testAfterNoteEnd(): void
    + testSynthesizeModifiesSignal(): void
  }
}

PitchedNoteTest ..> PitchedNote : tests
ScoreTest ..> Score : tests
HarmonicSynthesizerTest ..> HarmonicSynthesizer : tests
WhiteNoiseSynthesizerTest ..> WhiteNoiseSynthesizer : tests
VibratoSynthesizerTest ..> VibratoSynthesizer : tests

' --------------- '
' Design patterns '
' --------------- '

note right of NoteDecorator
  **Decorator Pattern** (Notes)
  ═══════════════════════════
  Base class for note decorators.
  Delegates to wrapped note.
  
  Subclasses: DottedNote, FermataNote
end note

note right of DottedNote
  **Concrete Decorator**
  ══════════════════════
  Increases duration by 50%
  Formula: duration × 1.5
  
  Frequency: unchanged
end note

note right of FermataNote
  **Concrete Decorator**
  ══════════════════════
  Extends note duration
  for expressive pauses.
end note

note right of PitchedNote
  **Concrete Component**
  ══════════════════════
  Base note implementation.
  
  Combines:
  • NotePitch → frequency
  • NoteValue → duration
end note

note right of NoteSynthesizerDecorator
  **Decorator Pattern** (Base)
  ═════════════════════════════
  Abstract base for all
  synthesizer decorators.
  
  Delegates to wrapped
  synthesizer by default.
end note

note right of SynthesizerDecorator
  **Intermediate Decorator**
  ══════════════════════════
  Extends NoteSynthesizerDecorator.
  
  Base class for concrete effect
  decorators (Harmonic, Noise, Vibrato).
end note

note bottom of HarmonicSynthesizer
  **Harmonic Effect**
  ═══════════════════
  Adds harmonics (2 to n):
  s(t) += (V/n) × Σ sin(2π·i·f·t) / √i
  
  Uses super.synthesize()
  for fundamental frequency.
end note

note bottom of WhiteNoiseSynthesizer
  **White Noise Effect**
  ══════════════════════
  Simulates wind instrument breath:
  s'(t) = s(t) + random(-b, b)
  
  Uses Random.nextDouble()
  to add noise to each sample.
end note

note bottom of VibratoSynthesizer
  **Vibrato Effect**
  ══════════════════
  Periodic pitch variation:
  ∇(t) = p · sin(2π · v · t)
  s'(t) = s(t) + ∇(t)
  
  p = depth, v = speed (Hz)
end note

note bottom of NoteFactory
  **Singleton Pattern**
  ═════════════════════
  Single instance via
  getInstance().
  
  Private constructor.
end note

note right of MultipleScoreSynthesizer
  **Composite Pattern**
  ═════════════════════
  Combines multiple MusicSynthesizer
  instances to synthesize multiple
  scores (polyphony).
  
  Mixes samples from all synthesizers.
end note

note right of Instruments
  **Enum with Strategy**
  ══════════════════════
  Each instrument has its own
  NoteSynthesizer for specific
  sound characteristics.
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
| Representation of a staff                              | Composite             | Ugo Dassonville |
| Traversal of notes/silences in a staff                 | Iterator              |                 |
| Representation of a musical piece                      | Composite             |                 |
| Creation of musical elements (notes, silences)         | Abstract Fabric       |                 |
| Generation of the "pure" sound for a note              | Strategy              | Mouille Antoine |
| Addition of harmonics to the sound of a note           | Decorator             | Rabhi Nessim    |
| Application of an ADSR envelope to the sound of a note | Decorator             |                 |
| Application of a vibrato to the sound of a note        | Decorator             |                 |
| Addition of random noise to the sound of a note        | Decorator             |                 |
| Synthesis of the bass drum sound                       | Strategy              | Antoine Mouille |
| Synthesis of the snare drum sound                      | Strategy              | Antoine Mouille |
| Synthesis of the cymbal sound                          | Strategy              | Antoine Mouille |
| Synthesis of the triangle sound                        | Strategy              | Antoine Mouille |
| Synthesis of the timpani sound                         | Strategy              | Antoine Mouille |
| Synthesis of the xylophone sound                       | Strategy              | Antoine Mouille |
| Definition of virtual instruments                      | Abstract Fabric       | Antoine/Ugo     |
| Synthesis of the ensemble piece sound                  | Composite             |                 |
| Command line management                                | Singleton             |                 |

## Team

This project has been developed by:

- Dassonville Ugo  
- Mouille Antoine
- Rabhi Nessim
- Dutkiewicz Tom