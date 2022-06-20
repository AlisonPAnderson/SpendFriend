package net.alisonanderson.ashleypeebles.spendfriendbackend.exceptions;

public class NonSufficentFundsException  extends Exception{

    public NonSufficentFundsException() {
        super("You do not have the necessary funds to complete this transaction");
    }
}
