package com.userJoin.model;

public class ResultMode<T> {

    private Head head;

    private T data;

    public Head getHead() {
        return head;
    }

    public void setHead(Head head) {
        this.head = head;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public ResultMode(long status, String message, T data) {

        Head head = new Head();
        head.setStatus(status);
        head.setMessage(message);
        this.head = head;
        this.data = data;
    }

    public static <T> ResultMode<T> success(T result){
        return new ResultMode<>(0, "处理成功", result);
    }

    public static <T> ResultMode<String> fail(){
        return new ResultMode<>(-99, "处理失败", "");
    }

    public static <T> ResultMode<String> FAIL_AUTH(){
        return new ResultMode<>(90,"未授权", null);
    }

    private class Head {

        private long status;

        private String message;

        public long getStatus() {
            return status;
        }

        public void setStatus(long status) {
            this.status = status;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
