package lt.bit.Blog.repository;

import lt.bit.Blog.model.Categories;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriesRepository extends JpaRepository<Categories, Integer>{
    
}
