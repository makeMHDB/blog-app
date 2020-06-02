package lt.bit.Blog.repository;

import java.util.List;
import java.util.Set;
import lt.bit.Blog.model.UsersRoles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UsersRolesRepository extends JpaRepository<UsersRoles, Integer> {

    @Query(name = "UsersRoles.findByRole")
    Set<UsersRoles> findByRole(@Param("findByRole") String findByRole);
    
    @Query(name = "UsersRoles.findAllGrouped")
    List<UsersRoles> findAllGrouped();
}
