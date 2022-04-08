package ru.liga.homework.db.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users", schema = "otpmm")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SequenceGenerator(name = "users_s", schema = "otpmm", sequenceName = "users_s", allocationSize = 1)
public class User {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_s")
    private Long id;

    @Column(name = "usertgid")
    private Long usertgid;

    @Column(name = "name")
    private String name;

    @Column(name = "password")
    private String password;

    @Column(name = "gender")
    private String gender;

    @Column(name = "header")
    private String header;

    @Column(name = "lookingfor")
    private String lookingFor;

    @Column(name = "description")
    private String description;

    @Column(name = "form_file_name")
    private String formFileName;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "users_like", schema = "otpmm",
            joinColumns = {@JoinColumn(name = "user_id_who")},
            inverseJoinColumns = {@JoinColumn(name = "user_id_that")})
    private Set<User> likes = new HashSet<>();

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "users_like", schema = "otpmm",
            joinColumns = {@JoinColumn(name = "user_id_that")},
            inverseJoinColumns = {@JoinColumn(name = "user_id_who")})
    private Set<User> likeBy = new HashSet<>();
}