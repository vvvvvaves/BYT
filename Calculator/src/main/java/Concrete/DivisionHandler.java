package main.java.Concrete;

import main.java.BaseHandler;
import main.java.BinaryRequest;

public class DivisionHandler extends BaseHandler<Double, Double> {
    @Override
    public Double handle(BinaryRequest<Double> request) {
        if (request.getOperation().equals("/")) {
            return request.getElement1() / request.getElement2();
        } else if (super.next != null) {
            return next.handle(request);
        } else {
            return null;
        }
    }
}
