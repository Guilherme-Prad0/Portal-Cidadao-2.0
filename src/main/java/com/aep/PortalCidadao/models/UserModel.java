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

    // ADICIONADO: senha em plain-text (sem Spring Security)
    private String senha;

    // ADICIONADO: papel do usuário
    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "usuario")
    private List<SolicitacaoModel> solicitacoes;

    // Construtor original mantido para compatibilidade
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

    // Construtor para cadastro com senha
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

    // Factory: usuário anônimo
    public static UserModel criarAnonimo() {
        UserModel u = new UserModel();
        u.nome    = "ANONIMO";
        u.anonimo = true;
        u.role    = Role.USER;
        return u;
    }

    // Factory: administrador
    public static UserModel criarAdmin(String nome, String email, String senha) {
        UserModel u = new UserModel();
        u.nome    = nome;
        u.email   = email;
        u.senha   = senha;
        u.anonimo = false;
        u.role    = Role.ADMIN;
        return u;
    }

    public String getCpfMascarado() {
        if (cpf == null || cpf.length() < 2) return "N/A";
        return "***.***.***-" + cpf.substring(cpf.length() - 2);
    }

    public boolean isAdmin() {
        return Role.ADMIN == role;
    }
}