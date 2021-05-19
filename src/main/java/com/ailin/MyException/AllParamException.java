package com.ailin.MyException;

public class AllParamException extends RuntimeException {

    private String code;
    private String message;

    public AllParamException(){
        super();
    }

    public AllParamException(String code, String message) {
        super();
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
