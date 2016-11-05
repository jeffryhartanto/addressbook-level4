package seedu.taskscheduler.commons.events.ui;

import seedu.taskscheduler.commons.events.BaseEvent;

//@@author A0140007B

/**
 * Indicates a request to change command box text
 */
public class CommandBoxTextChangeRequestEvent extends BaseEvent {

    public final String text;

    public CommandBoxTextChangeRequestEvent(String text) {
        this.text = text;
    }
    
    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

}
