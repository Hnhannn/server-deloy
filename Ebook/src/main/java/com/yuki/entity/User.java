package com.yuki.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.Builder;


/**
 * The persistent class for the Users database table.
 */
@Data
@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@Table(name = "Users")
@NamedQuery(name = "User.findAll", query = "SELECT u FROM User u")
public class User implements UserDetails {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userid")
    private int userID;

    @Column(name = "email")
    private String email;

    @Column(name = "fullname")
    private String fullName;

    @Column(name = "gender")
    private boolean gender;

    @Column(name = "password")
    @JsonIgnore
    private String password;

    @Column(name = "birthday", columnDefinition = "DATE DEFAULT '2000-01-01'")
    private LocalDate birthday;

    @Column(name = "phonenumber")
    private String phoneNumber;

    @Column(name = "role")
    private boolean role;

    @Column(name = "status")
    private String status;

    @Column(name = "userimage")
    private String userImage = "https://firebasestorage.googleapis.com/v0/b/ebookpage-836d8.appspot.com/o/file%2F564dba33-98f6-48d0-a0cc-6c4cc4b2c11a?alt=media&token=f3685c29-e343-4292-848c-02d9dc66a019";

    @Column(name = "username")
    private String username;

    @OneToMany(mappedBy = "user")
    // @JsonBackReference
    @JsonManagedReference
    private List<Address> addresses;

    @OneToMany(mappedBy = "user")
    // @JsonManagedReference
    @JsonBackReference
    private List<Likes> likes;

    @OneToMany(mappedBy = "user")
    @JsonManagedReference
    private List<SliderShow> sliderShows;

    @OneToMany(mappedBy = "user")
    // @JsonBackReference
    @JsonManagedReference
    private List<Order> orders;

    @OneToMany(mappedBy = "user")
    // @JsonManagedReference
    @JsonBackReference
    private List<Review> reviews;

    @OneToMany(mappedBy = "user")
    @JsonManagedReference
    private List<UserSubscription> userSubscription;

    public List<UserSubscription> getuserSubscription() {
        return userSubscription.stream()
                .filter(sub -> "Hoạt động".equals(sub.getStatus()))
                .collect(Collectors.toList());
    }

    @OneToMany(mappedBy = "user")
    @JsonBackReference
    // @JsonManagedReference
    private List<ReadBook> readBooks;

    public User(int userID) {
        this.userID = userID;
    }

    public User() {
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        if (role) {
            authorities.add(new SimpleGrantedAuthority("ADMIN"));
        } else {
            authorities.add(new SimpleGrantedAuthority("CLIENT"));
        }
        return authorities;
    }
}