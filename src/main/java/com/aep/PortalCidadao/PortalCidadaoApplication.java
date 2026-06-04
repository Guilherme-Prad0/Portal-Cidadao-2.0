package com.aep.PortalCidadao;

import com.aep.PortalCidadao.models.UserModel;
import com.aep.PortalCidadao.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class PortalCidadaoApplication {

	public static void main(String[] args) {
		SpringApplication.run(PortalCidadaoApplication.class, args);
	}

    @Bean
    CommandLineRunner init(UserRepository userRepository) {
        return args -> {

            UserModel user = new UserModel();

            user.setNome("João Silva");
            user.setCpf("12345678901");
            user.setEmail("joao@email.com");
            user.setTelefone("44999999999");
            user.setAnonimo(false);

            userRepository.save(user);
        };
    }

}
