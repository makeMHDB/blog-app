package lt.bit.Blog.repository;

import lt.bit.Blog.model.Likes;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikesRepository extends JpaRepository<Likes, Integer>{
    
}
