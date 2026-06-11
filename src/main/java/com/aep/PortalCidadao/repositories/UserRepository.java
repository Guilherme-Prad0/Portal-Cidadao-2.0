package com.aep.PortalCidadao.repositories;

import com.aep.PortalCidadao.models.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserModel, Long> {

    // ADICIONADO: necessário para login por e-mail
    Optional<UserModel> findByEmail(String email);

    // ADICIONADO: evita cadastros duplicados
    boolean existsByEmail(String email);

}
