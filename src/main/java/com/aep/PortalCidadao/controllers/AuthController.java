package com.aep.PortalCidadao.controllers;

import com.aep.PortalCidadao.dtos.CadastroDTO;
import com.aep.PortalCidadao.models.UserModel;
import com.aep.PortalCidadao.services.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String loginPage(@RequestParam(required = false) String erro, Model model) {
        if (erro != null) model.addAttribute("erro", erro);
        model.addAttribute("cadastroDTO", new CadastroDTO());
        return "auth/login";
    }

    //login com conta
    @PostMapping("/login")
    public String loginCidadao(@RequestParam String email,
                               @RequestParam String senha,
                               HttpSession session) {
        try {
            UserModel user = userService.autenticarCidadao(email, senha);
            session.setAttribute("usuarioLogado", user);
            return "redirect:/";
        } catch (RuntimeException e) {
            return "redirect:/login?erro=" + enc(e.getMessage());
        }
    }

    //anonimo
    @GetMapping("/login/anonimo")
    public String entrarAnonimo(HttpSession session) {
        session.setAttribute("acessoAnonimo", true);
        return "redirect:/";
    }

    //cadastro
    @PostMapping("/cadastro")
    public String cadastrar(@ModelAttribute CadastroDTO dto,
                            HttpSession session, Model model) {
        try {
            UserModel user = userService.cadastrar(dto);
            session.setAttribute("usuarioLogado", user);
            return "redirect:/";
        } catch (RuntimeException e) {
            model.addAttribute("erro", e.getMessage());
            model.addAttribute("cadastroDTO", dto);
            return "auth/login";
        }
    }

    //admin=login
    @PostMapping("/admin/login")
    public String loginAdmin(@RequestParam String email,
                             @RequestParam String senha,
                             HttpSession session) {
        try {
            UserModel admin = userService.autenticarAdmin(email, senha);
            session.setAttribute("usuarioLogado", admin);
            session.setAttribute("isAdmin", true);
            return "redirect:/admin";
        } catch (RuntimeException e) {
            return "redirect:/login?erro=" + enc(e.getMessage());
        }
    }

    //logout
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    private String enc(String msg) {
        return msg == null ? "" : msg.replace(" ", "+").replace("\"", "");
    }
}