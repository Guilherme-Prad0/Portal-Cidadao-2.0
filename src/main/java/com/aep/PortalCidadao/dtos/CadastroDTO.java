package com.aep.PortalCidadao.dtos;

import lombok.Data;

@Data
public class CadastroDTO {
    private String nome;
    private String cpf;
    private String email;
    private String telefone;
    private String senha;
    private String confirmaSenha;
}
