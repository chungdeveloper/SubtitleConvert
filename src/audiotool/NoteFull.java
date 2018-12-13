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
public class NoteFull {

    private Note startNote;
    private Note endNote;

    public NoteFull(Note start, Note end) {
        startNote = start;
        endNote = end;
    }

    /**
     * @return the startNote
     */
    public Note getStartNote() {
        return startNote;
    }

    /**
     * @param startNote the startNote to set
     */
    public void setStartNote(Note startNote) {
        this.startNote = startNote;
    }

    /**
     * @return the endNote
     */
    public Note getEndNote() {
        return endNote;
    }

    /**
     * @param endNote the endNote to set
     */
    public void setEndNote(Note endNote) {
        this.endNote = endNote;
    }

    public long getStartTime() {
        return startNote.getStickTime();
    }

    public long getEndTime() {
        return endNote.getStickTime();
    }

}
