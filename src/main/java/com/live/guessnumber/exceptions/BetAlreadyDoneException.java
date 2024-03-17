package com.live.guessnumber.exceptions;


public class BetAlreadyDoneException extends Exception {
    public BetAlreadyDoneException(String message) {
        super(message);
    }
}