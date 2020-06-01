/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lt.bit.Blog.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import lt.bit.Blog.model.Users;
import lt.bit.Blog.model.UsersRoles;
import lt.bit.Blog.repository.UsersInfoRepository;
import lt.bit.Blog.repository.UsersRepository;
import lt.bit.Blog.repository.UsersRolesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author makeMH
 */
@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private UsersRolesRepository usersRolesRepository;

    @Autowired
    private UsersInfoRepository usersInfoRepository;

    @RequestMapping
    public ModelAndView adminPage(Authentication authentication) {
        ModelAndView mav = new ModelAndView("admin/adminPage");
        Users user = usersRepository.findByUsername(authentication.getName());
        mav.addObject("user", user);
        mav.addObject("auth", authentication);
        return mav;
    }

    @RequestMapping("/users")
    public ModelAndView usersPage(HttpServletRequest request, Authentication authentication) {
        ModelAndView mav = new ModelAndView("admin/usersPage");
        Users user = usersRepository.findByUsername(authentication.getName());
        mav.addObject("user", user);
        mav.addObject("auth", authentication);

        int page = 0;
        int size = 10;

        if (request.getParameter("page") != null && !request.getParameter("page").isEmpty()) {
            page = Integer.parseInt(request.getParameter("page")) - 1;
        }

        if (request.getParameter("size") != null && !request.getParameter("size").isEmpty()) {
            size = Integer.parseInt(request.getParameter("size"));
        }
        Page<Users> usersList = null;
        if (request.getParameter("id") != null && request.getParameter("id").length() > 0) {
            try {
                Integer idFilter = Integer.parseInt(request.getParameter("id"));
                usersList = usersRepository.filterById(idFilter, PageRequest.of(page, size, (Sort.by("id").ascending())));
            } catch (NumberFormatException e) {
                return new ModelAndView("redirect:/admin/users");
            }
        } else if (request.getParameter("username") != null && request.getParameter("username").length() > 0) {
            usersList = usersRepository.filterByUsername(request.getParameter("username"), PageRequest.of(page, size, (Sort.by("id").ascending())));
        } else if (request.getParameter("role") != null && request.getParameter("role").length() > 0) {
            List<Users> allUsers = usersRepository.findAll();
            List<Users> usersWithRole = new ArrayList<>();
            for (Users u : allUsers) {
                if (u.getAuthorities().iterator().next().getAuthority().equals(request.getParameter("role"))
                        || u.getAuthorities().iterator().next().getAuthority().toLowerCase().equals(request.getParameter("role"))) {
                    usersWithRole.add(u);
                }
            }
            usersList = new PageImpl<Users>(usersWithRole, PageRequest.of(page, size, (Sort.by("id").ascending())), usersWithRole.size());
        } else if (request.getParameter("enabled") != null && request.getParameter("enabled").length() > 0) {
            try {
                Integer enabledFilter = Integer.parseInt(request.getParameter("enabled"));
                usersList = usersRepository.filterByEnabled(enabledFilter, PageRequest.of(page, size, (Sort.by("id").ascending())));
            } catch (NumberFormatException e) {
                return new ModelAndView("redirect:/admin/users");
            }
        } else {
            usersList = usersRepository.findAll(PageRequest.of(page, size, (Sort.by("id").ascending())));
        }
        mav.addObject("usersList", usersList);
        return mav;
    }

    @RequestMapping("/users/{id}")
    public ModelAndView userPage(Authentication authentication, @PathVariable("id") String idParam) {
        ModelAndView mav = new ModelAndView("admin/userPage");
        Users user = usersRepository.findByUsername(authentication.getName());
        try {
            Integer userMenuId = Integer.parseInt(idParam);
            Optional<Users> userMenuO = usersRepository.findById(userMenuId);
            if (userMenuO.isPresent()) {
                Users userMenu = userMenuO.get();
                mav.addObject("userMenu", userMenu);
            } else {
                return new ModelAndView("redirect:/admin/users");
            }
        } catch (NumberFormatException e) {
            return new ModelAndView("redirect:/admin/users");
        }
        mav.addObject("roles", usersRolesRepository.findAllGrouped());
        mav.addObject("user", user);
        mav.addObject("auth", authentication);
        return mav;
    }

    @RequestMapping(path = "/users/{id}/save", method = RequestMethod.POST)
    @Transactional
    public ModelAndView saveUser(Authentication authentication, @PathVariable("id") String idParam,
            @RequestParam("role") String role, @RequestParam("enabled") String enabledParam) {
        try {
            Integer userMenuId = Integer.parseInt(idParam);
            Integer enabled = Integer.parseInt(enabledParam);
            Optional<Users> existingUserO = usersRepository.findById(userMenuId);
            if (existingUserO.isPresent()) {
                Users existingUser = existingUserO.get();
                UsersRoles existingUserRole = existingUser.getUsersRolesSet().iterator().next();
                existingUserRole.setRole(role);
                existingUser.setEnabled(enabled);
                usersRepository.save(existingUser);
                usersRolesRepository.save(existingUserRole);
            }
        } catch (NumberFormatException e) {
            return new ModelAndView("redirect:/admin/users");
        }
        return new ModelAndView("redirect:/admin/users");
    }
}
