package ru.liga.homework.db.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users", schema = "public")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
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

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "usersLikely", joinColumns = {@JoinColumn(name = "user1")},
            inverseJoinColumns = {@JoinColumn(name = "User2")})
    private Set<User> likeBy = new HashSet<>();

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name="usersLikely",
            joinColumns={@JoinColumn(name="user2")},
            inverseJoinColumns={@JoinColumn(name="user1")})
    private Set<User> likes = new HashSet<>();
}