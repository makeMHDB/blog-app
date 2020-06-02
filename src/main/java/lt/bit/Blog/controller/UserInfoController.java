package lt.bit.Blog.controller;

import java.util.Optional;
import lt.bit.Blog.model.Users;
import lt.bit.Blog.model.UsersInfo;
import lt.bit.Blog.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/userInfo")
public class UserInfoController {

    @Autowired
    private UsersRepository usersRepository;

    @RequestMapping("/{id}")
    public ModelAndView userInfo(@PathVariable("id") String idParam, Authentication authentication) {
        Integer userInfoId;
        try {
            userInfoId = Integer.parseInt(idParam);
        } catch (NumberFormatException e) {
            return new ModelAndView("redirect:../");
        }

        ModelAndView mav = new ModelAndView("userInfo");

        Optional<Users> userInfoO = usersRepository.findById(userInfoId);
        if (!userInfoO.isPresent()) {
            return new ModelAndView("redirect:../");
        } else {
            UsersInfo userInfo = userInfoO.get().getUsersInfoList().get(0);
            mav.addObject("usersInfo", userInfo);
        }
        if (authentication != null) {
            Users user = usersRepository.findByUsername(authentication.getName());
            mav.addObject("user", user);
            mav.addObject("auth", authentication);
        }
        return mav;
    }
}
