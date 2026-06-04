package com.aep.PortalCidadao.models;

import com.aep.PortalCidadao.enums.Categoria;
import com.aep.PortalCidadao.enums.Prioridade;
import com.aep.PortalCidadao.enums.Status;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@Table(name = "SOLICITACOES")
public class SolicitacaoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String protocolo;

    @Enumerated(EnumType.STRING)
    private Categoria categoria;
    @Column(length = 2000)
    private String descricao;
    private String bairro;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private UserModel usuario;

    @Enumerated(EnumType.STRING)
    private Prioridade prioridade;

    @Enumerated(EnumType.STRING)
    private Status status;
    private LocalDateTime dataCriacao;
    private LocalDateTime prazo;
    private boolean atrasado;

    @Column(length = 1000)
    private String justificativaAtraso;

    @OneToMany(mappedBy = "solicitacao", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HistoricoStatusModel> historico = new ArrayList<>();

    public SolicitacaoModel(Categoria categoria, String descricao, String bairro, UserModel usuario) {
        this.protocolo = gerarProtocolo();
        this.categoria = categoria;
        this.descricao = descricao;
        this.bairro = bairro;
        this.usuario = usuario;
        this.status = Status.ABERTO;
        this.dataCriacao = LocalDateTime.now();
        this.prioridade = definirPrioridade();
        this.prazo = definirPrazo();
    }

    public void atualizarStatus(Status novoStatus, String responsavel, String comentario) {
        this.status = novoStatus;

        HistoricoStatusModel historicoStatus =
                new HistoricoStatusModel(
                        novoStatus,
                        responsavel,
                        comentario,
                        this
                );
        historico.add(historicoStatus);
    }

    private String gerarProtocolo() {
        int ano = LocalDate.now().getYear();
        return "SOL-" + ano + "-" + System.currentTimeMillis();
    }

    private Prioridade definirPrioridade() {

        if (categoria == Categoria.SAUDE ||
                categoria == Categoria.SEGURANCA_ESCOLAR) {
            return Prioridade.ALTA;
        }

        if (categoria == Categoria.ILUMINACAO ||
                categoria == Categoria.BURACO) {
            return Prioridade.MEDIA;
        }
        return Prioridade.BAIXA;
    }

    private LocalDateTime definirPrazo() {
        switch (prioridade) {
            case ALTA:
                return dataCriacao.plusDays(1);

            case MEDIA:
                return dataCriacao.plusDays(3);

            case BAIXA:
                return dataCriacao.plusDays(7);

            default:
                return dataCriacao.plusDays(5);
        }
    }

    public boolean isAtrasado() {
        return LocalDateTime.now().isAfter(prazo)
                && status != Status.ENCERRADO;
    }
}