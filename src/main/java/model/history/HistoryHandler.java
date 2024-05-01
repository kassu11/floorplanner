package model.history;
/**
 * Functional interface for handling history events
 */
@FunctionalInterface
public interface HistoryHandler {
    /**
     * Handles the history event
     */
    void handle();
}
