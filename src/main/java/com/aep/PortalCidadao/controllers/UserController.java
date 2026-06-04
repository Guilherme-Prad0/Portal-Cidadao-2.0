package com.aep.PortalCidadao.controllers;

import com.aep.PortalCidadao.models.UserModel;
import com.aep.PortalCidadao.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public String listar( Model model) {
        model.addAttribute("usuarios", userService.listar());
        return "usuarios/lista";
    }

    @GetMapping("/novo")
    public String novoUsuario(Model model) {
        model.addAttribute("usuario", new UserModel());
        return "usuarios/novo";
    }

    @PostMapping("/salvar")
    public String salvar(@ModelAttribute UserModel usuario) {
        userService.salvar(usuario);
        return "redirect:/usuarios";
    }

    @GetMapping("/{id}")
    public String visualizar(@PathVariable Long id, Model model) {
        UserModel usuario = userService.buscarPorId(id);
        model.addAttribute("usuario", usuario);
        return "usuarios/detalhes";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        UserModel usuario = userService.buscarPorId(id);
        model.addAttribute("usuario", usuario);
        return "usuarios/editar";
    }

    @PostMapping("/atualizar/{id}")
    public String atualizar(@PathVariable Long id, @ModelAttribute UserModel usuario) {
        usuario.setId(id);
        userService.salvar(usuario);
        return "redirect:/usuarios";
    }

    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id) {
        userService.excluir(id);
        return "redirect:/usuarios";
    }
}