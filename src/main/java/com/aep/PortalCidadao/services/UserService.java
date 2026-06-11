package com.aep.PortalCidadao.services;

import com.aep.PortalCidadao.dtos.CadastroDTO;
import com.aep.PortalCidadao.enums.Role;
import com.aep.PortalCidadao.models.UserModel;
import com.aep.PortalCidadao.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public UserModel salvar(UserModel usuario) {
        return userRepository.save(usuario);
    }

    // ADICIONADO: cadastro de novo cidadão
    @Transactional
    public UserModel cadastrar(CadastroDTO dto) {
        if (userRepository.existsByEmail(dto.getEmail()))
            throw new RuntimeException("E-mail já cadastrado.");
        if (!dto.getSenha().equals(dto.getConfirmaSenha()))
            throw new RuntimeException("As senhas não coincidem.");

        String cpfLimpo = dto.getCpf() == null ? null
                : dto.getCpf().replaceAll("\\D", "");
        if (cpfLimpo != null && cpfLimpo.length() != 11)
            throw new RuntimeException("CPF inválido — informe 11 dígitos.");

        return userRepository.save(
                new UserModel(dto.getNome(), cpfLimpo, dto.getEmail(),
                        dto.getTelefone(), dto.getSenha())
        );
    }

    // ADICIONADO: login de cidadão por e-mail/senha
    public UserModel autenticarCidadao(String email, String senha) {
        UserModel user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("E-mail não encontrado."));
        if (!senha.equals(user.getSenha()))
            throw new RuntimeException("Senha incorreta.");
        if (user.getRole() != Role.USER)
            throw new RuntimeException("Use o acesso de administrador.");
        return user;
    }

    // ADICIONADO: login de administrador
    public UserModel autenticarAdmin(String email, String senha) {
        UserModel user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));
        if (!senha.equals(user.getSenha()))
            throw new RuntimeException("Senha incorreta.");
        if (user.getRole() != Role.ADMIN)
            throw new RuntimeException("Acesso negado — sem permissão de administrador.");
        return user;
    }

    public List<UserModel> listar() {
        return userRepository.findAll();
    }

    public UserModel buscarPorId(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));
    }

    @Transactional
    public void excluir(Long id) {
        userRepository.deleteById(id);
    }
}