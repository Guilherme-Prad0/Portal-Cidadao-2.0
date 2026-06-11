package com.aep.PortalCidadao.models;

import com.aep.PortalCidadao.enums.Status;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@Table(name = "HISTORICO_STATUS")
public class HistoricoStatusModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Status status;
    private String responsavel;

    @Column(length = 1000)
    private String comentario;
    private LocalDateTime data;

    @ManyToOne
    @JoinColumn(name = "solicitacao_id")
    private SolicitacaoModel solicitacao;

    public HistoricoStatusModel(Status status, String responsavel,
                                String comentario, SolicitacaoModel solicitacao) {
        this.status      = status;
        this.responsavel = responsavel;
        this.comentario  = comentario;
        this.solicitacao = solicitacao;
        this.data        = LocalDateTime.now();
    }
}