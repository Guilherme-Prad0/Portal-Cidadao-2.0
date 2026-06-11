package com.aep.PortalCidadao.controllers;

import com.aep.PortalCidadao.dtos.SolicitacaoDTO;
import com.aep.PortalCidadao.enums.Categoria;
import com.aep.PortalCidadao.enums.Role;
import com.aep.PortalCidadao.enums.Status;
import com.aep.PortalCidadao.models.SolicitacaoModel;
import com.aep.PortalCidadao.models.UserModel;
import com.aep.PortalCidadao.services.SolicitacaoService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private SolicitacaoService solicitacaoService;

    private boolean isAdmin(HttpSession session) {
        UserModel u = (UserModel) session.getAttribute("usuarioLogado");
        return u != null && u.getRole() == Role.ADMIN;
    }

    private String semAcesso() {
        return "redirect:/login?erro=Acesso+restrito+ao+administrador";
    }

    @GetMapping
    public String painel(Model model, HttpSession session) {
        if (!isAdmin(session)) return semAcesso();
        List<SolicitacaoModel> lista = solicitacaoService.listar();
        model.addAttribute("solicitacoes", lista);
        model.addAttribute("categorias",   Categoria.values());
        model.addAttribute("statusList",   Status.values());
        return "admin/painel";
    }

    @GetMapping("/filtro")
    public String filtrar(@RequestParam(required = false) String bairro,
                          @RequestParam(required = false) Categoria categoria,
                          Model model, HttpSession session) {
        if (!isAdmin(session)) return semAcesso();
        model.addAttribute("solicitacoes",
                solicitacaoService.listarPorFiltro(bairro, categoria));
        model.addAttribute("categorias", Categoria.values());
        model.addAttribute("statusList", Status.values());
        return "admin/painel";
    }

    @GetMapping("/solicitacao/{protocolo}")
    public String visualizar(@PathVariable String protocolo,
                             Model model, HttpSession session) {
        if (!isAdmin(session)) return semAcesso();
        try {
            model.addAttribute("solicitacao", solicitacaoService.acompanhar(protocolo));
            model.addAttribute("statusList",  Status.values());
            model.addAttribute("categorias",  Categoria.values());
            return "admin/detalhes";
        } catch (RuntimeException e) {
            model.addAttribute("erro", e.getMessage());
            return painel(model, session);
        }
    }

    @PostMapping("/solicitacao/{protocolo}/status")
    public String atualizarStatus(@PathVariable String protocolo,
                                  @RequestParam Status status,
                                  @RequestParam String responsavel,
                                  @RequestParam(required = false) String comentario,
                                  HttpSession session, Model model) {
        if (!isAdmin(session)) return semAcesso();
        try {
            solicitacaoService.atualizarStatus(protocolo, status, responsavel, comentario);
            return "redirect:/admin/solicitacao/" + protocolo;
        } catch (RuntimeException e) {
            model.addAttribute("erro", e.getMessage());
            return visualizar(protocolo, model, session);
        }
    }

    @PostMapping("/solicitacao/{protocolo}/editar")
    public String editar(@PathVariable String protocolo,
                         @ModelAttribute SolicitacaoDTO dto,
                         @RequestParam(value = "imagem", required = false) MultipartFile imagem,
                         HttpSession session, Model model) {
        if (!isAdmin(session)) return semAcesso();
        try {
            solicitacaoService.editar(protocolo, dto, imagem);
            return "redirect:/admin/solicitacao/" + protocolo;
        } catch (RuntimeException e) {
            model.addAttribute("erro", e.getMessage());
            return visualizar(protocolo, model, session);
        }
    }

    @PostMapping("/solicitacao/{protocolo}/excluir")
    public String excluir(@PathVariable String protocolo, HttpSession session) {
        if (!isAdmin(session)) return semAcesso();
        solicitacaoService.excluir(protocolo);
        return "redirect:/admin";
    }
}