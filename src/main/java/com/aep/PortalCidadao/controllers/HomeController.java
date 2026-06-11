package com.aep.PortalCidadao.controllers;

import com.aep.PortalCidadao.models.UserModel;
import com.aep.PortalCidadao.services.SolicitacaoService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @Autowired
    private SolicitacaoService solicitacaoService;

    @GetMapping("/")
    public String home(Model model, HttpSession session) {
        model.addAttribute("totalSolicitacoes", solicitacaoService.listar().size());

        UserModel usuario = (UserModel) session.getAttribute("usuarioLogado");
        model.addAttribute("usuarioLogado", usuario);
        model.addAttribute("isAdmin", usuario != null && usuario.isAdmin());
        return "index";
    }
}