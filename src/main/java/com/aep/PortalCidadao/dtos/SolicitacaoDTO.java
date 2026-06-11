package com.aep.PortalCidadao.dtos;

import com.aep.PortalCidadao.enums.Categoria;
import lombok.Data;

@Data
public class SolicitacaoDTO {

    private String  nome;
    private String  cpf;
    private String  email;
    private String  telefone;
    private Boolean anonimo = false;
    private Categoria categoria;
    private String    bairro;
    private String    descricao;
}
