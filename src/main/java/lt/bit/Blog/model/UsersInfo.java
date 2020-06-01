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
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 *
 * @author makeMH
 */
@Entity
@Table(name = "users_info")
@NamedQueries({
    @NamedQuery(name = "UsersInfo.findAll", query = "SELECT u FROM UsersInfo u"),
    @NamedQuery(name = "UsersInfo.findById", query = "SELECT u FROM UsersInfo u WHERE u.id = :id"),
    @NamedQuery(name = "UsersInfo.findByFirstName", query = "SELECT u FROM UsersInfo u WHERE u.firstName = :firstName"),
    @NamedQuery(name = "UsersInfo.findByLastName", query = "SELECT u FROM UsersInfo u WHERE u.lastName = :lastName"),
    @NamedQuery(name = "UsersInfo.findByEmail", query = "SELECT u FROM UsersInfo u WHERE u.email = :email"),
    @NamedQuery(name = "UsersInfo.findByCountry", query = "SELECT u FROM UsersInfo u WHERE u.country = :country"),
    @NamedQuery(name = "UsersInfo.findByCity", query = "SELECT u FROM UsersInfo u WHERE u.city = :city"),
    @NamedQuery(name = "UsersInfo.findByAddress", query = "SELECT u FROM UsersInfo u WHERE u.address = :address"),
    @NamedQuery(name = "UsersInfo.findByPhoneNumber", query = "SELECT u FROM UsersInfo u WHERE u.phoneNumber = :phoneNumber")})
public class UsersInfo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "first_name")
    private String firstName;
    @Basic(optional = false)
    @Column(name = "last_name")
    private String lastName;
    @Basic(optional = false)
    @Column(name = "email")
    private String email;
    @Basic(optional = false)
    @Column(name = "country")
    private String country;
    @Basic(optional = false)
    @Column(name = "city")
    private String city;
    @Basic(optional = false)
    @Column(name = "address")
    private String address;
    @Basic(optional = false)
    @Column(name = "phone_number")
    private String phoneNumber;
    @JoinColumn(name = "users_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Users usersId;

    public UsersInfo() {
    }

    public UsersInfo(Integer id) {
        this.id = id;
    }

    public UsersInfo(Integer id, String firstName, String lastName, String email, String country, String city, String address, String phoneNumber) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.country = country;
        this.city = city;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Users getUsersId() {
        return usersId;
    }

    public void setUsersId(Users usersId) {
        this.usersId = usersId;
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
        if (!(object instanceof UsersInfo)) {
            return false;
        }
        UsersInfo other = (UsersInfo) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "UsersInfo{" + "id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", email=" + email + ", country=" + country + ", city=" + city + ", address=" + address + ", phoneNumber=" + phoneNumber + ", usersId=" + usersId + '}';
    }

}
