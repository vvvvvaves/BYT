package main.java;

public abstract class BinaryRequest<T> {
    private T element1;
    private T element2;
    private String operation;

    public BinaryRequest(T element1, T element2, String operation) {
        this.element1 = element1;
        this.element2 = element2;
        this.operation = operation;
    }

    public T getElement1() {
        return element1;
    }

    public T getElement2() {
        return element2;
    }

    public String getOperation() {
        return operation;
    }
}
