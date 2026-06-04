package com.aep.PortalCidadao.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import com.aep.PortalCidadao.enums.Categoria;
import com.aep.PortalCidadao.enums.Status;
import com.aep.PortalCidadao.models.SolicitacaoModel;
import com.aep.PortalCidadao.services.SolicitacaoService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private SolicitacaoService solicitacaoService;

    @GetMapping
    public String painel(Model model) {

        model.addAttribute(
                "solicitacoes",
                solicitacaoService.listar()
        );

        model.addAttribute(
                "categorias",
                Categoria.values()
        );

        model.addAttribute(
                "statusList",
                Status.values()
        );

        return "admin/painel";
    }

    @GetMapping("/filtro")
    public String filtrar(
            @RequestParam(required = false) String bairro,
            @RequestParam(required = false) Categoria categoria,
            Model model) {

        List<SolicitacaoModel> solicitacoes =
                solicitacaoService.listarPorFiltro(
                        bairro,
                        categoria
                );

        model.addAttribute("solicitacoes", solicitacoes);
        model.addAttribute("categorias", Categoria.values());

        return "admin/painel";
    }

    @GetMapping("/solicitacao/{protocolo}")
    public String visualizarSolicitacao(@PathVariable String protocolo, Model model) {

        SolicitacaoModel solicitacao = solicitacaoService.acompanhar(protocolo);
        model.addAttribute("solicitacao", solicitacao);
        model.addAttribute("statusList", Status.values());
        return "admin/detalhes";
    }

    @PostMapping("/solicitacao/{protocolo}/status")
    public String atualizarStatus(@PathVariable String protocolo,
                                  @RequestParam Status status,
                                  @RequestParam String responsavel,
                                  @RequestParam String comentario) {

        solicitacaoService.atualizarStatus(protocolo, status,
                responsavel, comentario);

        return "redirect:/admin/solicitacao/" + protocolo;
    }
}