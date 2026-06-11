package com.aep.PortalCidadao.repositories;

import com.aep.PortalCidadao.enums.Categoria;
import com.aep.PortalCidadao.models.SolicitacaoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface SolicitacaoRepository extends JpaRepository<SolicitacaoModel, Long> {

    // ALTERADO: retorna Optional para evitar NullPointerException
    Optional<SolicitacaoModel> findByProtocolo(String protocolo);

    List<SolicitacaoModel> findByBairroIgnoreCase(String bairro);

    List<SolicitacaoModel> findByCategoria(Categoria categoria);

    List<SolicitacaoModel> findByBairroIgnoreCaseAndCategoria(String bairro, Categoria categoria);

    // ADICIONADO: mais recentes primeiro em todas as listagens
    List<SolicitacaoModel> findAllByOrderByDataCriacaoDesc();

}
