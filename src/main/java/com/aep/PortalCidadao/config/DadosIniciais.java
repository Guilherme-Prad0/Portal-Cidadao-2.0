package com.aep.PortalCidadao.config;

import com.aep.PortalCidadao.enums.Categoria;
import com.aep.PortalCidadao.enums.Status;
import com.aep.PortalCidadao.models.SolicitacaoModel;
import com.aep.PortalCidadao.models.UserModel;
import com.aep.PortalCidadao.repositories.SolicitacaoRepository;
import com.aep.PortalCidadao.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DadosIniciais {

    @Bean
    CommandLineRunner seed(UserRepository userRepo, SolicitacaoRepository solRepo) {
        return args -> {
            if (userRepo.count() > 0) return; // evita duplicatas ao reiniciar

            // ── Administrador ──
            UserModel admin = UserModel.criarAdmin(
                    "Administrador", "admin@prefeitura.gov.br", "admin123");
            userRepo.save(admin);

            // ── Cidadãos de exemplo ──
            UserModel joao = new UserModel(
                    "João Silva", "12345678901", "joao@email.com", "44999990001", "senha123");
            UserModel maria = new UserModel(
                    "Maria Souza", "98765432100", "maria@email.com", "44999990002", "senha123");
            UserModel anonimo = UserModel.criarAnonimo();
            userRepo.save(joao);
            userRepo.save(maria);
            userRepo.save(anonimo);

            // ── 5 protocolos com status e categorias variados ─────────────

            // 1. ABERTO
            SolicitacaoModel s1 = new SolicitacaoModel(
                    Categoria.BURACO,
                    "Buraco de aproximadamente 1 metro de diâmetro na Av. Brasil esquina com Rua das Flores. "
                            + "Risco para veículos e pedestres, especialmente à noite.",
                    "Centro", joao);
            solRepo.save(s1);

            // 2. EM_EXECUCAO
            SolicitacaoModel s2 = new SolicitacaoModel(
                    Categoria.ILUMINACAO,
                    "Três postes sem iluminação na Rua Ipê, entre os números 100 e 200. "
                            + "Área fica completamente escura após as 18h.",
                    "Jardim Alvorada", maria);
            s2.atualizarStatus(Status.TRIAGEM, "Central de Atendimento", "Registrado e encaminhado para análise.");
            s2.atualizarStatus(Status.EM_EXECUCAO, "Secretaria de Obras", "Equipe agendada para reparo na quinta-feira.");
            solRepo.save(s2);

            // 3. RESOLVIDO
            SolicitacaoModel s3 = new SolicitacaoModel(
                    Categoria.SAUDE,
                    "UBS do bairro está sem médico há duas semanas. Pacientes sendo orientados a procurar pronto-socorro.",
                    "Zona Norte", joao);
            s3.atualizarStatus(Status.TRIAGEM, "Secretaria de Saúde", "Situação confirmada pela regional.");
            s3.atualizarStatus(Status.EM_EXECUCAO, "Secretaria de Saúde", "Médico substituto alocado temporariamente.");
            s3.atualizarStatus(Status.RESOLVIDO, "Secretaria de Saúde", "Médico efetivo contratado e em atividade.");
            solRepo.save(s3);

            // 4. ENCERRADO
            SolicitacaoModel s4 = new SolicitacaoModel(
                    Categoria.LIMPEZA,
                    "Acúmulo de lixo em terreno baldio na Rua das Indústrias próximo ao número 450. "
                            + "Situação ocorre há mais de um mês e atrai animais. "
                            + "Moradores já solicitaram remoção diversas vezes sem retorno.",
                    "Parque Industrial", anonimo);
            s4.atualizarStatus(Status.TRIAGEM, "Limpeza Urbana", "Vistoria realizada.");
            s4.atualizarStatus(Status.EM_EXECUCAO, "Limpeza Urbana", "Equipe enviada para limpeza do terreno.");
            s4.atualizarStatus(Status.RESOLVIDO, "Limpeza Urbana", "Terreno limpo e proprietário notificado.");
            s4.atualizarStatus(Status.ENCERRADO, "Limpeza Urbana", "Caso encerrado após confirmação.");
            solRepo.save(s4);

            // 5. TRIAGEM
            SolicitacaoModel s5 = new SolicitacaoModel(
                    Categoria.SEGURANCA_ESCOLAR,
                    "Muro da Escola Municipal Rui Barbosa com trinca visível e risco de queda. "
                            + "Crianças transitam diariamente no local.",
                    "Vila Nova", maria);
            s5.atualizarStatus(Status.TRIAGEM, "Secretaria de Educação", "Engenheiro agendado para vistoria.");
            solRepo.save(s5);

            System.out.println("=================================================");
            System.out.println("  PORTAL CIDADÃO — seed inserido com sucesso");
            System.out.println("  Admin  : admin@prefeitura.gov.br / admin123");
            System.out.println("  Cidadão: joao@email.com / senha123");
            System.out.println("  Cidadão: maria@email.com / senha123");
            System.out.println("=================================================");
        };
    }
}