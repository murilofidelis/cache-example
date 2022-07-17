package br.com.github.product.service.exception;

public class AppException extends RuntimeException {

    public AppException(String var) {
        super(var);
    }

    public AppException(String var, Throwable throwable) {
        super(var, throwable);
    }
}
