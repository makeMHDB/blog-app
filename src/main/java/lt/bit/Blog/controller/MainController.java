package lt.bit.Blog.controller;

import javax.servlet.http.HttpServletRequest;
import lt.bit.Blog.model.Users;
import lt.bit.Blog.repository.BlogsRepository;
import lt.bit.Blog.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/")
public class MainController {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private BlogsRepository blogsRepository;

    @RequestMapping(method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView mainPage(HttpServletRequest request, Authentication authentication) {
        ModelAndView mav = new ModelAndView("index");
        if (authentication != null) {
            String name = authentication.getName();
            Users user = usersRepository.findByUsername(name);
            mav.addObject("user", user);
            mav.addObject("auth", authentication);
            if (!user.isInfoCompleted()) {
                return new ModelAndView("redirect:/userCP", "user", user);
            }
        }

        int page = 0;
        int size = 10;

        if (request.getParameter("page") != null && !request.getParameter("page").isEmpty()) {
            page = Integer.parseInt(request.getParameter("page")) - 1;
        }

        if (request.getParameter("size") != null && !request.getParameter("size").isEmpty()) {
            size = Integer.parseInt(request.getParameter("size"));
        }
        mav.addObject("blogs", blogsRepository.findAll(PageRequest.of(page, size, Sort.by("date").descending().and(Sort.by("id").descending()))));
        return mav;
    }

    @RequestMapping("accessDenied")
    public ModelAndView userCPPage(Authentication authentication) {
        ModelAndView mav = new ModelAndView("accessDenied");
        Users user = usersRepository.findByUsername(authentication.getName());
        mav.addObject("user", user);
        mav.addObject("auth", authentication);
        return mav;
    }
}
