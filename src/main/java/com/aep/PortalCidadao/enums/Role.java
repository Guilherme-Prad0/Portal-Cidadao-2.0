package com.aep.PortalCidadao.enums;

import lombok.Getter;

@Getter
public enum Role {
    USER("User"),
    ADMIN("Admin");

    private final String descricao;

    Role(String descricao) {
        this.descricao = descricao;
    }
}
