package br.com.github.product.service.dto;

public class MessageRequest {

    private String text;

    public MessageRequest() {
    }

    public MessageRequest(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }


    @Override
    public String toString() {
        return "MessageRequest{" +
               "text='" + text + '\'' +
               '}';
    }
}
