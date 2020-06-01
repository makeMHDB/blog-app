/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lt.bit.Blog.repository;

import java.util.List;
import lt.bit.Blog.model.Blogs;
import lt.bit.Blog.model.Comments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author makeMH
 */
public interface CommentsRepository extends JpaRepository<Comments, Integer> {

    @Query("SELECT c FROM Comments c WHERE c.blogId = :blogId")
    List<Comments> findByBlog(@Param("blogId") Blogs blog);

}
