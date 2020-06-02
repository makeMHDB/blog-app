package lt.bit.Blog.repository;

import lt.bit.Blog.model.Blogs;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlogsRepository extends JpaRepository<Blogs, Integer> {
}
