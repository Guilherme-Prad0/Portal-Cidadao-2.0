package com.aep.PortalCidadao.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@Table(name = "USERS")
public class UserModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String cpf;
    private String email;
    private String telefone;
    private Boolean anonimo;

    @OneToMany(mappedBy = "usuario")
    private List<SolicitacaoModel> solicitacoes;

    public UserModel(Long id, String nome, String cpf, String email, String telefone, Boolean anonimo) {
        this.id = id;
        this.anonimo = anonimo;

        if (Boolean.TRUE.equals(anonimo)) {
            this.nome = "ANONIMO";
            this.cpf = null;
            this.email = null;
            this.telefone = null;
        } else {
            this.nome = nome;
            this.cpf = cpf;
            this.email = email;
            this.telefone = telefone;
        }
    }

    public String getCpfMascarado() {
        if (cpf == null || cpf.length() < 2) {
            return "N/A";
        }
        return "***.***.***-" + cpf.substring(cpf.length() - 2);
    }

}
