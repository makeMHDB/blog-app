/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lt.bit.Blog.controller;

import javax.transaction.Transactional;
import lt.bit.Blog.model.Users;
import lt.bit.Blog.model.UsersInfo;
import lt.bit.Blog.repository.UsersInfoRepository;
import lt.bit.Blog.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author makeMH
 */
@Controller
@RequestMapping("/userCP")
public class UserCPController {

    @Autowired
    private UsersInfoRepository usersInfoRepository;

    @Autowired
    private UsersRepository usersRepository;

    @GetMapping
    public ModelAndView userCPPage(Authentication authentication) {
        ModelAndView mav = new ModelAndView("userCP");
        Users user = usersRepository.findByUsername(authentication.getName());
        mav.addObject("usersInfo", user.getUsersInfoList().get(0));
        mav.addObject("user", user);
        mav.addObject("auth", authentication);
        return mav;
    }

    @RequestMapping(path = "/saveInfo", method = RequestMethod.POST)
    @Transactional
    public String saveInfo(@ModelAttribute("usersInfo") UsersInfo userInfo, @RequestParam(name = "userId") String id) {
        Integer userId = Integer.parseInt(id);
        Users user = usersRepository.getOne(userId);
        UsersInfo existingUI = user.getUsersInfoList().get(0);

        existingUI.setFirstName(userInfo.getFirstName());
        existingUI.setLastName(userInfo.getLastName());
        existingUI.setEmail(userInfo.getEmail());
        existingUI.setAddress(userInfo.getAddress());
        existingUI.setCity(userInfo.getCity());
        existingUI.setCountry(userInfo.getCountry());
        existingUI.setPhoneNumber(userInfo.getPhoneNumber());

        if (!user.isInfoCompleted()) {
            user.setInfoCompleted(1);
        }

        usersInfoRepository.save(existingUI);
        return "redirect:/";
    }

}
