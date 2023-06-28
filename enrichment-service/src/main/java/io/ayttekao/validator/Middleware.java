package io.ayttekao.validator;

import io.ayttekao.model.Message;

public abstract class Middleware {
    private Middleware next;

    public static Middleware link(Middleware first, Middleware... chain) {
        Middleware head = first;
        for (Middleware nextInChain : chain) {
            head.next = nextInChain;
            head = nextInChain;
        }
        return first;
    }

    public abstract boolean check(Message message);

    protected boolean checkNext(Message message) {
        if (next == null) {
            return true;
        }
        return next.check(message);
    }
}