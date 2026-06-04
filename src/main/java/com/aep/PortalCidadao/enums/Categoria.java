package com.aep.PortalCidadao.enums;

import lombok.Getter;

@Getter
public enum Categoria {

    ILUMINACAO("Iluminação"),
    BURACO("Buraco"),
    LIMPEZA("Limpeza"),
    SAUDE("Saúde"),
    SEGURANCA_ESCOLAR("Segurança Escolar");

    private final String descricao;

    Categoria(String descricao) {
        this.descricao = descricao;
    }

}
