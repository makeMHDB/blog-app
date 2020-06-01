/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lt.bit.Blog.model;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author makeMH
 */
@Entity
@Table(name = "users_roles")
@NamedQueries({
    @NamedQuery(name = "UsersRoles.findAll", query = "SELECT u FROM UsersRoles u"),
    @NamedQuery(name = "UsersRoles.findByRole", query = "SELECT u FROM UsersRoles u WHERE u.role = :role"),
    @NamedQuery(name = "UsersRoles.findById", query = "SELECT u FROM UsersRoles u WHERE u.id = :id"),
    @NamedQuery(name = "UsersRoles.findAllGrouped", query = "SELECT u FROM UsersRoles u GROUP BY u.role")
})
public class UsersRoles implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "role")
    private String role;
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Users userId;

    public UsersRoles() {
    }

    public UsersRoles(Integer id) {
        this.id = id;
    }

    public UsersRoles(Integer id, String role) {
        this.id = id;
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Users getUserId() {
        return userId;
    }

    public void setUserId(Users userId) {
        this.userId = userId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UsersRoles)) {
            return false;
        }
        UsersRoles other = (UsersRoles) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "UsersRoles{" + "id=" + id + ", role=" + role + ", userId=" + userId + '}';
    }

}
