package com.aep.PortalCidadao.controllers;

import com.aep.PortalCidadao.dtos.SolicitacaoDTO;
import com.aep.PortalCidadao.enums.Categoria;
import com.aep.PortalCidadao.models.SolicitacaoModel;
import com.aep.PortalCidadao.models.UserModel;
import com.aep.PortalCidadao.services.SolicitacaoService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/solicitacoes")
public class SolicitacaoController {

    @Autowired
    private SolicitacaoService solicitacaoService;

    // Lista pública
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("solicitacoes", solicitacaoService.listar());
        return "solicitacoes/lista";
    }

    // Formulário nova solicitação
    @GetMapping("/nova")
    public String novaSolicitacao(Model model, HttpSession session) {
        model.addAttribute("categorias", Categoria.values());
        model.addAttribute("dto", new SolicitacaoDTO());
        model.addAttribute("usuarioLogado",
                (UserModel) session.getAttribute("usuarioLogado"));
        return "solicitacoes/nova";
    }

    // POST criar — redireciona para tela de confirmação com pop-up
    @PostMapping("/criar")
    public String criar(@ModelAttribute SolicitacaoDTO dto,
                        @RequestParam(value = "imagem", required = false) MultipartFile imagem,
                        HttpSession session, Model model) {
        try {
            UserModel logado = (UserModel) session.getAttribute("usuarioLogado");
            SolicitacaoModel sol = solicitacaoService.criar(dto, logado, imagem);
            model.addAttribute("protocolo",  sol.getProtocolo());
            model.addAttribute("solicitacao", sol);
            return "solicitacoes/confirmacao";
        } catch (RuntimeException e) {
            model.addAttribute("erro", e.getMessage());
            model.addAttribute("categorias", Categoria.values());
            model.addAttribute("dto", dto);
            model.addAttribute("usuarioLogado",
                    (UserModel) session.getAttribute("usuarioLogado"));
            return "solicitacoes/nova";
        }
    }

    // ADICIONADO: GET /consultar?protocolo=... (tela de busca do cidadão)
    @GetMapping("/consultar")
    public String consultar(@RequestParam(required = false) String protocolo,
                            Model model) {
        model.addAttribute("protocolo", protocolo);
        if (protocolo != null && !protocolo.isBlank()) {
            try {
                model.addAttribute("solicitacao",
                        solicitacaoService.acompanhar(protocolo.trim().toUpperCase()));
            } catch (RuntimeException e) {
                model.addAttribute("erro", e.getMessage());
            }
        }
        return "solicitacoes/consultar";
    }

    // Detalhe por URL direta
    @GetMapping("/{protocolo}")
    public String detalhe(@PathVariable String protocolo, Model model) {
        try {
            model.addAttribute("solicitacao", solicitacaoService.acompanhar(protocolo));
            return "solicitacoes/detalhes";
        } catch (RuntimeException e) {
            model.addAttribute("erro", e.getMessage());
            return "solicitacoes/consultar";
        }
    }
}