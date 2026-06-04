package com.aep.PortalCidadao.enums;

import lombok.Getter;

@Getter
public enum Status {

    ABERTO("Aberto"),
    TRIAGEM("Em Triagem"),
    EM_EXECUCAO("Em Execução"),
    RESOLVIDO("Resolvido"),
    ENCERRADO("Encerrado");

    private final String descricao;

    Status(String descricao) {
        this.descricao = descricao;
    }

}