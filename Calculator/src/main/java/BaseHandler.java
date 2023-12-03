package main.java;

public abstract class BaseHandler<T, S> implements Handler<T, S>{

    protected Handler<T, S> next;

    @Override
    public void setNext(Handler<T, S> handler) {
        next = handler;
    }
}
