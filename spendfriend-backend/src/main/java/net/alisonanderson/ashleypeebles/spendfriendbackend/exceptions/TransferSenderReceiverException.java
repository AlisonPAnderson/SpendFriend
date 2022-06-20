package net.alisonanderson.ashleypeebles.spendfriendbackend.exceptions;

public class TransferSenderReceiverException extends Exception{
    public TransferSenderReceiverException() {
        super("You may not transfer funds to the same user ID.");
    }

}
