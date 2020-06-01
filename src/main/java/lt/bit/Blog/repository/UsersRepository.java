/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lt.bit.Blog.repository;

import lt.bit.Blog.model.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author makeMH
 */
public interface UsersRepository extends JpaRepository<Users, Integer> {

    @Query(name = "Users.findByUsername")
    public Users findByUsername(@Param("username") String userName);
    
    @Query(name = "Users.filterById")
    public Page<Users> filterById(@Param("id") Integer id, Pageable pageable);
   
    @Query(name = "Users.filterByUsername")
    public Page<Users> filterByUsername(@Param("username") String username, Pageable pageable);
    
    @Query(name = "Users.filterByEnabled")
    public Page<Users> filterByEnabled(@Param("enabled") Integer enabled, Pageable pageable);

}
