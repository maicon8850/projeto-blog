package br.com.montreal.projeto_blog.dto;

import javax.validation.constraints.NotBlank;

public class TemaDTO {

    @NotBlank
    private String descricao;

    public TemaDTO() {}

    public TemaDTO(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}