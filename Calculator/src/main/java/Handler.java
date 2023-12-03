package main.java;

public interface Handler<T, S> {

    S handle(BinaryRequest<T> request);

    void setNext(Handler<T, S> handler);
}
