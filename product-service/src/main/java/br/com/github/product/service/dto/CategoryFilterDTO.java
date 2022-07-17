package br.com.github.product.service.dto;

import java.io.Serializable;

public class CategoryFilterDTO implements Serializable {

    private static final long serialVersionUID = 1265316421486018286L;

    private String description;

    private String cod;

    private boolean status;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCod() {
        return cod;
    }

    public void setCod(String cod) {
        this.cod = cod;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
