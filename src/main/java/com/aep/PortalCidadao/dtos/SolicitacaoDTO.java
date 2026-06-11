package com.aep.PortalCidadao.dtos;

import com.aep.PortalCidadao.enums.Categoria;
import lombok.Data;

@Data
public class SolicitacaoDTO {

    // Dados pessoais — usados quando não logado e não anônimo
    private String  nome;
    private String  cpf;
    private String  email;
    private String  telefone;
    private Boolean anonimo = false;

    // Ocorrência — obrigatórios para todos
    private Categoria categoria;
    private String    bairro;
    private String    descricao;
}
