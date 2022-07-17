package br.com.github.product.service.dto;

import java.io.Serializable;

public class CategoryDTO implements Serializable {

    private static final long serialVersionUID = 7758606879589113004L;

    private Long id;

    private String description;

    private String cod;

    private Boolean status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

}
