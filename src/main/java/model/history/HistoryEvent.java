package model.history;
/**
 * Class for handling history events
 */
public class HistoryEvent {
    /**
     * Redo and undo handlers
     */
    private final HistoryHandler redo, undo;
    /**
     * Constructor for the history event
     * @param redo redo handler
     * @param undo undo handler
     */
    public HistoryEvent(HistoryHandler redo, HistoryHandler undo) {
        this.redo = redo;
        this.undo = undo;
    }
    /**
     * Handles the history event
     */
    public void undo() {
        this.undo.handle();
    }
    /**
     * Handles the history event
     */
    public void redo() {
        this.redo.handle();
    }
}
