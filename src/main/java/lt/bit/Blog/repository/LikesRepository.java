/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lt.bit.Blog.repository;

import lt.bit.Blog.model.Likes;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author makeMH
 */
public interface LikesRepository extends JpaRepository<Likes, Integer>{
    
}
