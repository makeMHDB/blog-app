/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lt.bit.Blog.controller;

import java.util.ArrayList;
import java.util.HashSet;
import javax.transaction.Transactional;
import javax.validation.Valid;
import lt.bit.Blog.model.Users;
import lt.bit.Blog.model.UsersInfo;
import lt.bit.Blog.model.UsersRoles;
import lt.bit.Blog.repository.UsersInfoRepository;
import lt.bit.Blog.repository.UsersRepository;
import lt.bit.Blog.repository.UsersRolesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author makeMH
 */
@Controller
public class RegisterController {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private UsersRolesRepository usersRolesRepository;
    
    @Autowired
    private UsersInfoRepository usersInfoRepository;

    @Autowired
    private PasswordEncoder pe;

    @RequestMapping(path = "/register", method = RequestMethod.GET)
    public String registerPage(Model model, Authentication authentication) {
        if (authentication == null) {
            model.addAttribute("user", new Users());
            return "register";
        } else {
            return "redirect:/";
        }
    }

    @RequestMapping("/completeRegistration")
    @Transactional
    public String completeRegistration(@Valid @ModelAttribute("user") Users user, BindingResult result, Model model) {
        Users existingUser = null;
        existingUser = usersRepository.findByUsername(user.getUsername());
        if (result.hasErrors() || existingUser != null) {
            model.addAttribute("user", user);
            model.addAttribute("existingUser", existingUser);
            return "register";
        }
        Users newUser = new Users();
        newUser.setId(null);
        newUser.setUsername(user.getUsername());
        newUser.setPassword(pe.encode(user.getPassword()));
        newUser.setEnabled(1);
        newUser.setInfoCompleted(0);
        usersRepository.save(newUser);

        UsersInfo userInfo = new UsersInfo();
        userInfo.setUsersId(newUser);
        usersInfoRepository.save(userInfo);
        
        
        newUser = usersRepository.findByUsername(user.getUsername());
        UsersRoles userRole = new UsersRoles();
        userRole.setRole("USER");
        userRole.setUserId(newUser);
        usersRolesRepository.save(userRole);

        newUser.setUsersInfoList(new ArrayList<UsersInfo>());
        newUser.getUsersInfoList().add(userInfo);
        newUser.setUsersRolesSet(new HashSet<UsersRoles>());
        newUser.getUsersRolesSet().add(userRole);
        usersRepository.save(newUser);

        return "redirect:/login";
    }
}
