package model.history;

import model.Shape;

public class HistoryEvent {
    private final HistoryHandler redo, undo;

    public HistoryEvent(HistoryHandler redo, HistoryHandler undo) {
        this.redo = redo;
        this.undo = undo;
    }

    public void undo() {
        this.undo.handle();
    }

    public void redo() {
        this.redo.handle();
    }
}
