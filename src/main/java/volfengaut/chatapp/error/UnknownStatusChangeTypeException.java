package volfengaut.chatapp.error;

import volfengaut.chatapp.message.ChatterStatusChange;

public class UnknownStatusChangeTypeException extends RuntimeException {

    public UnknownStatusChangeTypeException(ChatterStatusChange statusChange) {
        super("Unknown type of chatter status change: " + statusChange + "!");
    }

}
