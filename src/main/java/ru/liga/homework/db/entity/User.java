package ru.liga.homework.db.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users", schema = "public")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SequenceGenerator(name = "user_s", sequenceName = "user_s", allocationSize = 1)
public class User {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_s")
    private Long id;

    @Column(name = "username")
    private String username;

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

    @JsonIgnore
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "usersLikely", joinColumns = {@JoinColumn(name = "user1")},
            inverseJoinColumns = {@JoinColumn(name = "User2")})
    private Set<User> likeBy = new HashSet<>();

    @JsonIgnore
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name="usersLikely",
            joinColumns={@JoinColumn(name="user2")},
            inverseJoinColumns={@JoinColumn(name="user1")})
    private Set<User> likes = new HashSet<>();
}