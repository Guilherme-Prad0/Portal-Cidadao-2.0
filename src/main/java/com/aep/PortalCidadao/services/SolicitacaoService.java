package com.aep.PortalCidadao.services;

import com.aep.PortalCidadao.enums.Categoria;
import com.aep.PortalCidadao.enums.Status;
import com.aep.PortalCidadao.models.SolicitacaoModel;
import com.aep.PortalCidadao.models.UserModel;
import com.aep.PortalCidadao.repositories.SolicitacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SolicitacaoService {

    @Autowired
    private SolicitacaoRepository repository;

    public SolicitacaoModel criar(Categoria categoria, String descricao, String bairro, UserModel usuario) {

        if (descricao == null || descricao.trim().isEmpty()) {
            throw new RuntimeException("Descrição obrigatória");
        }

        if (bairro == null || bairro.trim().isEmpty()) {
            throw new RuntimeException("Bairro obrigatório");
        }

        if (Boolean.TRUE.equals(usuario.getAnonimo())) {

            if (descricao.length() < 15) {
                throw new RuntimeException(
                        "Denúncia anônima precisa ser mais detalhada (mínimo 15 caracteres)"
                );
            }

        } else {

            if (usuario.getNome() == null || usuario.getNome().trim().isEmpty()) {
                throw new RuntimeException("Nome obrigatório");
            }

            if (usuario.getCpf() == null || usuario.getCpf().length() != 11) {
                throw new RuntimeException("CPF inválido");
            }

            if (usuario.getTelefone() == null || usuario.getTelefone().trim().isEmpty()) {
                throw new RuntimeException("Telefone obrigatório");
            }

            if (usuario.getEmail() == null || !usuario.getEmail().contains("@")) {
                throw new RuntimeException("Email inválido");
            }
        }

        SolicitacaoModel solicitacao =
                new SolicitacaoModel(
                        categoria,
                        descricao,
                        bairro,
                        usuario
                );
        return repository.save(solicitacao);
    }

    public void atualizarStatus(String protocolo, Status novoStatus, String responsavel, String comentario) {

        SolicitacaoModel solicitacao = repository.findByProtocolo(protocolo);

        if (solicitacao == null) {
            throw new RuntimeException("Solicitação não encontrada");
        }

        if (solicitacao.isAtrasado()) {
            if (comentario == null || comentario.trim().isEmpty()) {
                throw new RuntimeException(
                        "Solicitação atrasada! Justificativa obrigatória."
                );
            }
            solicitacao.setJustificativaAtraso(comentario);
        }

        solicitacao.atualizarStatus(novoStatus, responsavel, comentario);
        repository.save(solicitacao);
    }

    public List<SolicitacaoModel> listar() {
        return repository.findAll();
    }

    public SolicitacaoModel acompanhar(String protocolo) {
        SolicitacaoModel solicitacao =
                repository.findByProtocolo(protocolo);
        if (solicitacao == null) {
            throw new RuntimeException("Protocolo não encontrado");
        }
        return solicitacao;
    }

    public List<SolicitacaoModel> listarPorFiltro(String bairro, Categoria categoria) {

        List<SolicitacaoModel> solicitacoes = repository.findAll();
        return solicitacoes.stream()
                .filter(s -> bairro == null ||
                        bairro.isBlank() ||
                        s.getBairro().equalsIgnoreCase(bairro))
                .filter(s -> categoria == null ||
                        s.getCategoria() == categoria)
                .toList();
    }

    public boolean protocoloValido(String protocolo) {

        if (protocolo == null || protocolo.trim().isEmpty()) {
            return false;
        }
        return repository.findByProtocolo(protocolo) != null;
    }
}