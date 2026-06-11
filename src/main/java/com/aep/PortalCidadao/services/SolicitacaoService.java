package com.aep.PortalCidadao.services;

import com.aep.PortalCidadao.dtos.SolicitacaoDTO;
import com.aep.PortalCidadao.enums.Categoria;
import com.aep.PortalCidadao.enums.Status;
import com.aep.PortalCidadao.models.SolicitacaoModel;
import com.aep.PortalCidadao.models.UserModel;
import com.aep.PortalCidadao.repositories.SolicitacaoRepository;
import com.aep.PortalCidadao.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
public class SolicitacaoService {

    @Autowired
    private SolicitacaoRepository repository;

    @Autowired
    private UserRepository userRepository;

    @Value("${upload.dir:uploads}")
    private String uploadDir;

    // Criar solicitação
    @Transactional
    public SolicitacaoModel criar(SolicitacaoDTO dto,
                                  UserModel usuarioLogado,
                                  MultipartFile imagem) {
        if (dto.getCategoria() == null)
            throw new RuntimeException("Categoria obrigatória.");
        if (dto.getBairro() == null || dto.getBairro().isBlank())
            throw new RuntimeException("Bairro obrigatório.");
        if (dto.getDescricao() == null || dto.getDescricao().isBlank())
            throw new RuntimeException("Descrição obrigatória.");

        boolean anonimo = Boolean.TRUE.equals(dto.getAnonimo());

        if (anonimo && dto.getDescricao().trim().length() < 100)
            throw new RuntimeException(
                    "Solicitação anônima requer descrição com mínimo 100 caracteres.");

        UserModel usuario;
        if (anonimo) {
            usuario = UserModel.criarAnonimo();
            userRepository.save(usuario);
        } else if (usuarioLogado != null) {
            usuario = usuarioLogado;
        } else {
            validarDadosPessoais(dto);
            String cpfLimpo = dto.getCpf().replaceAll("\\D", "");
            usuario = new UserModel(dto.getNome(), cpfLimpo,
                    dto.getEmail(), dto.getTelefone(), null);
            userRepository.save(usuario);
        }

        SolicitacaoModel sol = new SolicitacaoModel(
                dto.getCategoria(), dto.getDescricao().trim(),
                dto.getBairro().trim(), usuario
        );

        if (imagem != null && !imagem.isEmpty())
            sol.setImagemPath(salvarImagem(imagem));

        return repository.save(sol);
    }

    public SolicitacaoModel acompanhar(String protocolo) {
        return repository.findByProtocolo(protocolo)
                .orElseThrow(() ->
                        new RuntimeException("Protocolo \"" + protocolo + "\" não encontrado."));
    }

    public List<SolicitacaoModel> listar() {
        return repository.findAllByOrderByDataCriacaoDesc();
    }

    public List<SolicitacaoModel> listarPorFiltro(String bairro, Categoria categoria) {
        return listar().stream()
                .filter(s -> bairro == null || bairro.isBlank()
                        || s.getBairro().equalsIgnoreCase(bairro))
                .filter(s -> categoria == null || s.getCategoria() == categoria)
                .toList();
    }

    @Transactional
    public void atualizarStatus(String protocolo, Status novoStatus,
                                String responsavel, String comentario) {
        SolicitacaoModel sol = acompanhar(protocolo);
        if (sol.isAtrasado()) {
            if (comentario == null || comentario.isBlank())
                throw new RuntimeException(
                        "Solicitação atrasada — justificativa obrigatória.");
            sol.setJustificativaAtraso(comentario);
        }
        sol.atualizarStatus(novoStatus, responsavel, comentario);
        repository.save(sol);
    }

    @Transactional
    public void editar(String protocolo, SolicitacaoDTO dto, MultipartFile imagem) {
        SolicitacaoModel sol = acompanhar(protocolo);
        if (dto.getCategoria() != null) sol.setCategoria(dto.getCategoria());
        if (dto.getBairro() != null && !dto.getBairro().isBlank())
            sol.setBairro(dto.getBairro().trim());
        if (dto.getDescricao() != null && !dto.getDescricao().isBlank())
            sol.setDescricao(dto.getDescricao().trim());
        if (imagem != null && !imagem.isEmpty())
            sol.setImagemPath(salvarImagem(imagem));
        repository.save(sol);
    }

    @Transactional
    public void excluir(String protocolo) {
        repository.delete(acompanhar(protocolo));
    }

    public boolean protocoloValido(String protocolo) {
        return protocolo != null && !protocolo.isBlank()
                && repository.findByProtocolo(protocolo).isPresent();
    }

    private String salvarImagem(MultipartFile file) {
        try {
            Path dir = Paths.get(uploadDir);
            Files.createDirectories(dir);
            String orig = file.getOriginalFilename();
            String ext  = (orig != null && orig.contains("."))
                    ? orig.substring(orig.lastIndexOf('.')) : "";
            String nome = UUID.randomUUID() + ext;
            Files.copy(file.getInputStream(), dir.resolve(nome));
            return nome;
        } catch (IOException e) {
            throw new RuntimeException("Erro ao salvar imagem: " + e.getMessage());
        }
    }

    private void validarDadosPessoais(SolicitacaoDTO dto) {
        if (dto.getNome() == null || dto.getNome().isBlank())
            throw new RuntimeException("Nome obrigatório.");
        String cpf = dto.getCpf() == null ? "" : dto.getCpf().replaceAll("\\D", "");
        if (cpf.length() != 11)
            throw new RuntimeException("CPF inválido — informe 11 dígitos.");
        if (dto.getTelefone() == null || dto.getTelefone().isBlank())
            throw new RuntimeException("Telefone obrigatório.");
        if (dto.getEmail() == null || !dto.getEmail().contains("@"))
            throw new RuntimeException("E-mail inválido.");
    }
}