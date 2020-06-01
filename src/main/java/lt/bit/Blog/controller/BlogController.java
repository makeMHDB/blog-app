/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lt.bit.Blog.controller;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import lt.bit.Blog.model.Blogs;
import lt.bit.Blog.model.Categories;
import lt.bit.Blog.model.Comments;
import lt.bit.Blog.model.Likes;
import lt.bit.Blog.model.Users;
import lt.bit.Blog.repository.BlogsRepository;
import lt.bit.Blog.repository.CategoriesRepository;
import lt.bit.Blog.repository.CommentsRepository;
import lt.bit.Blog.repository.LikesRepository;
import lt.bit.Blog.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/blog")
public class BlogController {

    @Autowired
    private BlogsRepository blogsRepository;
    @Autowired
    private CommentsRepository commentsRepository;
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private LikesRepository likesRepository;
    @Autowired
    private CategoriesRepository categoriesRepository;

    @RequestMapping("/{id}")
    public ModelAndView getBlog(@PathVariable("id") String blogIdParam, Authentication authentication) {
        ModelAndView mav = new ModelAndView("blog");
        Integer blogId;
        try {
            blogId = Integer.parseInt(blogIdParam);
        } catch (NumberFormatException e) {
            return new ModelAndView("redirect:../");
        }

        Optional<Blogs> blogO = blogsRepository.findById(blogId);
        if (!blogO.isPresent()) {
            return new ModelAndView("redirect:../");
        } else {
            Blogs blog = blogO.get();
            mav.addObject("blog", blog);

            List<Comments> commentsList = commentsRepository.findByBlog(blog);
            if (commentsList != null) {
                mav.addObject("commentsList", commentsList);
            }
        }

        if (authentication != null) {
            Users user = usersRepository.findByUsername(authentication.getName());
            mav.addObject("user", user);
            mav.addObject("auth", authentication);
            mav.addObject("userRole", user.getUsersRolesSet().iterator().next());
            if (hasUserLiked(user, blogId)) {
                mav.addObject("liked", true);
            }
        }

        return mav;
    }

    @RequestMapping(path = "/{id}/like", method = RequestMethod.POST)
    @Transactional
    public ModelAndView likeBlog(@PathVariable("id") String blogIdParam, Authentication authentication) {
        Integer blogId;
        Users user;
        try {
            blogId = Integer.parseInt(blogIdParam);
        } catch (NumberFormatException e) {
            return new ModelAndView("redirect:../");
        }

        Optional<Blogs> blogO = blogsRepository.findById(blogId);
        if (!blogO.isPresent()) {
            return new ModelAndView("redirect:../");
        } else {
            Blogs blog = blogO.get();
            user = usersRepository.findByUsername(authentication.getName());
            List<Likes> blogLikesList = blog.getLikesList();
            for (Likes likes : blogLikesList) {
                if (likes.getUserId().equals(user)) {
                    likesRepository.delete(likes);
                    likes.getBlogId().getLikesList().remove(likes);
                    return new ModelAndView("redirect:/blog/" + blog.getId());
                }
            }
            Likes like = new Likes();
            like.setBlogId(blog);
            like.setUserId(user);
            likesRepository.save(like);
            return new ModelAndView("redirect:/blog/" + blog.getId());
        }
    }

    @RequestMapping(path = "/{id}/comment", method = RequestMethod.POST)
    @Transactional
    public ModelAndView commentBlog(@PathVariable("id") String blogIdParam, Authentication authentication,
            @RequestParam("comment") String commentParam) {
        Integer blogId;
        Users user;
        try {
            blogId = Integer.parseInt(blogIdParam);
        } catch (NumberFormatException e) {
            return new ModelAndView("redirect:../");
        }

        Optional<Blogs> blogO = blogsRepository.findById(blogId);
        if (!blogO.isPresent()) {
            return new ModelAndView("redirect:../");
        } else {
            Blogs blog = blogO.get();
            user = usersRepository.findByUsername(authentication.getName());
            List<Comments> blogCommentsList = blog.getCommentsList();
            Comments comment = new Comments();
            if (blogCommentsList.contains(comment)) {
                return new ModelAndView("redirect:/blog/" + blog.getId());
            }
            comment.setBlogId(blog);
            comment.setCauthor(user);
            comment.setComment(commentParam);

            Date now = new Date();
            comment.setDate(now);

            commentsRepository.save(comment);
            return new ModelAndView("redirect:/blog/" + blog.getId());
        }
    }

    @RequestMapping(path = "/{id}/deleteComment/{cId}", method = RequestMethod.POST)
    @Transactional
    public ModelAndView deleteComment(@PathVariable("id") String blogIdParam,
            @PathVariable("cId") String commentIdParam, Authentication authentication) {
        Integer blogId;
        Integer commentId;
        Users user;
        try {
            blogId = Integer.parseInt(blogIdParam);
        } catch (NumberFormatException e) {
            return new ModelAndView("redirect:/");
        }
        try {
            commentId = Integer.parseInt(commentIdParam);
        } catch (NumberFormatException e) {
            return new ModelAndView("redirect:/");
        }

        user = usersRepository.findByUsername(authentication.getName());
        Optional<Comments> commentO = commentsRepository.findById(commentId);
        if (commentO.isPresent() && (user.getUsersRolesSet().iterator().next().getRole().equals("ADMIN") || commentO.get().getCauthor().equals(user))) {
            Comments comment = commentO.get();
            commentsRepository.delete(comment);
            Blogs blog = blogsRepository.getOne(blogId);
            return new ModelAndView("redirect:/blog/" + blog.getId());
        } else {
            return new ModelAndView("redirect:/");
        }
    }

    @RequestMapping("/newPost")
    public ModelAndView newBlog(Authentication authentication) {
        Users user = usersRepository.findByUsername(authentication.getName());
        List<Categories> categories = categoriesRepository.findAll();
        ModelAndView mav = new ModelAndView("newblog");

        mav.addObject("categories", categories);
        mav.addObject("user", user);
        mav.addObject("auth", authentication);
        return mav;
    }

    @RequestMapping(path = "/addPost", method = RequestMethod.POST)
    @Transactional
    public ModelAndView addPost(Authentication authentication, @RequestParam("title") String title,
            @RequestParam("categories") String categoryIdParam,
            @RequestParam("blogText") String blogText) {
        Users user = usersRepository.findByUsername(authentication.getName());
        Categories cat = categoriesRepository.getOne(Integer.parseInt(categoryIdParam));
        Blogs blog = new Blogs();
        blog.setTitle(title);
        blog.setText(blogText);
        blog.setCategoryId(cat);
        blog.setAuthor(user);
        blog.setDate(new Date());
        blogsRepository.save(blog);
        return new ModelAndView("redirect:/blog/" + blog.getId());
    }

    @RequestMapping(path = "/{id}/deleteBlog", method = RequestMethod.POST)
    @Transactional
    public ModelAndView deleteBlog(Authentication authentication, @PathVariable("id") String blogIdParam) {
        Integer blogId;
        Users user = usersRepository.findByUsername(authentication.getName());
        try {
            blogId = Integer.parseInt(blogIdParam);
        } catch (NumberFormatException e) {
            return new ModelAndView("redirect:/");
        }

        Optional<Blogs> blogO = blogsRepository.findById(blogId);
        if (blogO.isPresent()) {
            Blogs blog = blogO.get();
            if (blog.getAuthor().equals(user) || user.getUsersRolesSet().iterator().next().getRole().equals("ADMIN")) {
                blogsRepository.delete(blog);
            }
        }
        return new ModelAndView("redirect:/");
    }

    @RequestMapping(path = "/{id}/editBlog")
    public ModelAndView editBlog(Authentication authentication, @PathVariable("id") String blogIdParam) {
        ModelAndView mav = new ModelAndView("editBlog");
        Integer blogId;
        Users user;
        try {
            blogId = Integer.parseInt(blogIdParam);
        } catch (NumberFormatException e) {
            return new ModelAndView("redirect:/");
        }
        Optional<Blogs> blogO = blogsRepository.findById(blogId);
        if (!blogO.isPresent()) {
            return new ModelAndView("redirect:/");
        } else {
            Blogs blog = blogO.get();
            user = usersRepository.findByUsername(authentication.getName());
            List<Categories> categories = categoriesRepository.findAll();
            mav.addObject("categories", categories);
            mav.addObject("blog", blog);
            mav.addObject("user", user);
            mav.addObject("auth", authentication);
            return mav;
        }
    }

    @RequestMapping(path = "/{id}/savePost", method = RequestMethod.POST)
    @Transactional
    public ModelAndView savePost(Authentication authentication,
            @PathVariable("id") String blogIdParam,
            @RequestParam("title") String title,
            @RequestParam("categories") String categoryIdParam,
            @RequestParam("blogText") String blogText) {
        Integer blogId;
        Blogs existingBlog;
        try {
            blogId = Integer.parseInt(blogIdParam);
        } catch (NumberFormatException e) {
            return new ModelAndView("redirect:/");
        }
        Users user = usersRepository.findByUsername(authentication.getName());
        Categories cat = categoriesRepository.getOne(Integer.parseInt(categoryIdParam));
        Optional<Blogs> existingBlogO = blogsRepository.findById(blogId);
        if (!existingBlogO.isPresent()) {
            return new ModelAndView("redirect:/");
        }
        existingBlog = existingBlogO.get();
        existingBlog.setCategoryId(cat);
        existingBlog.setText(blogText);
        existingBlog.setTitle(title);
        blogsRepository.save(existingBlog);
        return new ModelAndView("redirect:/blog/" + existingBlog.getId());
    }

    public boolean hasUserLiked(Users user, Integer blogId) {
        Blogs blog = blogsRepository.getOne(blogId);
        List<Likes> blogLikesList = blog.getLikesList();
        for (Likes likes : blogLikesList) {
            if (likes.getUserId().equals(user)) {
                return true;
            }
        }
        return false;
    }

}
