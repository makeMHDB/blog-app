package lt.bit.Blog.repository;

import lt.bit.Blog.model.UsersInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersInfoRepository extends JpaRepository<UsersInfo, Integer>{
    
}
