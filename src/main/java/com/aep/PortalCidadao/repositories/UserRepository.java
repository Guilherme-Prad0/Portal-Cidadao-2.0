package com.aep.PortalCidadao.repositories;

import com.aep.PortalCidadao.models.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserModel, Long> {

}
