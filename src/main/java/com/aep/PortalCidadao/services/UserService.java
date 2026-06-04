package com.aep.PortalCidadao.services;

import com.aep.PortalCidadao.models.UserModel;
import com.aep.PortalCidadao.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public UserModel salvar(UserModel usuario) {
        return userRepository.save(usuario);
    }

    public List<UserModel> listar() {
        return userRepository.findAll();
    }

    public UserModel buscarPorId(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Usuário não encontrado"));
    }

    public void excluir(Long id) {
        userRepository.deleteById(id);
    }
}
