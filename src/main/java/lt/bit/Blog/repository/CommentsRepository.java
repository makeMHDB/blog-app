package lt.bit.Blog.repository;

import java.util.List;
import lt.bit.Blog.model.Blogs;
import lt.bit.Blog.model.Comments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommentsRepository extends JpaRepository<Comments, Integer> {

    @Query("SELECT c FROM Comments c WHERE c.blogId = :blogId")
    List<Comments> findByBlog(@Param("blogId") Blogs blog);

}
