package embedded.radar.gui;

import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Synthesizer;
import javax.sound.midi.Instrument;


/**
 * Audio sound effects generation class
 * 
 * @author Karim Ashraf
 * 
 */

public class Audio {

    private static Audio singleton = null;
    private MidiChannel chan;

    /**
     * 
     * Returns Audio singleton instance.
     * 
     * @return Audio instance
     * @throws MidiUnavailableException
     */
    public static Audio getInstance() throws MidiUnavailableException {
        if (singleton == null) {
            singleton = new Audio();
        }
        return singleton;
    }

    /**
     * Constructor. chooses sine generator as the MIDI channel
     * 
     * @throws MidiUnavailableException
     */
    private Audio() throws MidiUnavailableException {
        Synthesizer synth = MidiSystem.getSynthesizer();
        synth.open();
        synth.loadAllInstruments(synth.getDefaultSoundbank());
        chan = synth.getChannels()[0];
        for (Instrument i : synth.getLoadedInstruments()) {
            if (i.getName().toLowerCase().trim().contains("piano")) { //60's E.Piano
                chan.programChange(i.getPatch().getProgram());
                // break;
            }
        }
       
    }

    /**
     * 
     * Plays alert sound
     * 
     */
    public void PlaySound() {
        try {
            new Thread() {
                @Override
                public void run() {
                    try {
                        for (int i = 0; i < 2; i++) {
                            chan.allNotesOff();
                            chan.noteOn(90, 90);
                            Thread.sleep(1000);
                            chan.allNotesOff();
                            Thread.sleep(500);
                        }
                    } catch (IllegalArgumentException e) {
                    } catch (InterruptedException e) {
                    }
                }
            }.start();
        } catch (IllegalThreadStateException e) {
        }
    }

    /**
     * Stops imediately current note
     */
    public void StopSound() {
        chan.allNotesOff();
    }

}