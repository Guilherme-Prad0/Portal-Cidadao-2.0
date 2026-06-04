package com.aep.PortalCidadao.enums;

import lombok.Getter;

@Getter
public enum Prioridade {

    BAIXA("Baixa"),
    MEDIA("Média"),
    ALTA("Alta");

    private final String descricao;

    Prioridade(String descricao) {
        this.descricao = descricao;
    }

}