package com.aep.PortalCidadao.models;

import com.aep.PortalCidadao.enums.Role;
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
    private String senha;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "usuario")
    private List<SolicitacaoModel> solicitacoes;

    public UserModel(Long id, String nome, String cpf, String email,
                     String telefone, Boolean anonimo) {
        this.id      = id;
        this.anonimo = anonimo;
        this.role    = Role.USER;
        if (Boolean.TRUE.equals(anonimo)) {
            this.nome     = "ANONIMO";
            this.cpf      = null;
            this.email    = null;
            this.telefone = null;
        } else {
            this.nome     = nome;
            this.cpf      = cpf;
            this.email    = email;
            this.telefone = telefone;
        }
    }

    public UserModel(String nome, String cpf, String email,
                     String telefone, String senha) {
        this.nome     = nome;
        this.cpf      = cpf;
        this.email    = email;
        this.telefone = telefone;
        this.senha    = senha;
        this.anonimo  = false;
        this.role     = Role.USER;
    }

    public static UserModel criarAnonimo() {
        UserModel userAnonimo = new UserModel();
        userAnonimo.nome    = "ANONIMO";
        userAnonimo.anonimo = true;
        userAnonimo.role    = Role.USER;
        return userAnonimo;
    }

    public static UserModel criarAdmin(String nome, String email, String senha) {
        UserModel admin = new UserModel();
        admin.nome    = nome;
        admin.email   = email;
        admin.senha   = senha;
        admin.anonimo = false;
        admin.role    = Role.ADMIN;
        return admin;
    }

    public String getCpfMascarado() {
        if (cpf == null || cpf.length() < 2) return "N/A";
        return "***.***.***-" + cpf.substring(cpf.length() - 2);
    }

    public boolean isAdmin() {
        return Role.ADMIN == role;
    }
}