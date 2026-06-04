package com.aep.PortalCidadao.controllers;

import com.aep.PortalCidadao.enums.Categoria;
import com.aep.PortalCidadao.enums.Status;
import com.aep.PortalCidadao.models.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import com.aep.PortalCidadao.models.SolicitacaoModel;
import com.aep.PortalCidadao.services.SolicitacaoService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/solicitacoes")
public class SolicitacaoController {

    @Autowired
    private SolicitacaoService solicitacaoService;


    @GetMapping
    public String listar(Model model) {
        model.addAttribute("solicitacoes",
                solicitacaoService.listar());

        return "solicitacoes/lista";
    }

    @GetMapping("/nova")
    public String novaSolicitacao(Model model) {

        model.addAttribute("categorias", Categoria.values());
        model.addAttribute("usuario", new UserModel());

        return "solicitacoes/nova";
    }

    @PostMapping("/criar")
    public String criar(@RequestParam Categoria categoria,
                        @RequestParam String descricao,
                        @RequestParam String bairro,
                        @ModelAttribute UserModel usuario) {

        SolicitacaoModel solicitacao =
                solicitacaoService.criar(
                        categoria,
                        descricao,
                        bairro,
                        usuario
                );

        return "redirect:/solicitacoes/" +
                solicitacao.getProtocolo();
    }

    @GetMapping("/{protocolo}")
    public String acompanhar(@PathVariable String protocolo,
                             Model model) {

        SolicitacaoModel solicitacao =
                solicitacaoService.acompanhar(protocolo);

        model.addAttribute("solicitacao", solicitacao);

        return "solicitacoes/detalhes";
    }

    @PostMapping("/{protocolo}/status")
    public String atualizarStatus(@PathVariable String protocolo,
                                  @RequestParam Status status,
                                  @RequestParam String responsavel,
                                  @RequestParam String comentario) {

        solicitacaoService.atualizarStatus(
                protocolo,
                status,
                responsavel,
                comentario
        );

        return "redirect:/solicitacoes/" + protocolo;
    }
}