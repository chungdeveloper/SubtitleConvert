/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package audiotool;

/**
 *
 * @author shoot
 */
public class Note {

    private long stickTime;
    private String name;
    private int noteState;
    private int octave;
    private int key;
    private int velocity;

    public Note(long time, String name, int state, int oct, int key, int velocity) {
        stickTime = time;
        this.name = name;
        noteState = state;
        octave = oct;
        this.key = key;
        this.velocity = velocity;
    }

    Note() {

    }

    /**
     * @return the stickTime
     */
    public long getStickTime() {
        return stickTime;
    }

    /**
     * @param stickTime the stickTime to set
     */
    public void setStickTime(long stickTime) {
        this.stickTime = stickTime;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name + getOctave();
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the noteState
     */
    public int getNoteState() {
        return noteState;
    }

    /**
     * @param noteState the noteState to set
     */
    public void setNoteState(int noteState) {
        this.noteState = noteState;
    }

    /**
     * @return the octave
     */
    public int getOctave() {
        return octave;
    }

    /**
     * @param octave the octave to set
     */
    public void setOctave(int octave) {
        this.octave = octave;
    }

    /**
     * @return the key
     */
    public int getKey() {
        return key;
    }

    /**
     * @param key the key to set
     */
    public void setKey(int key) {
        this.key = key;
    }

    /**
     * @return the velocity
     */
    public int getVelocity() {
        return velocity;
    }

    /**
     * @param velocity the velocity to set
     */
    public void setVelocity(int velocity) {
        this.velocity = velocity;
    }
}
