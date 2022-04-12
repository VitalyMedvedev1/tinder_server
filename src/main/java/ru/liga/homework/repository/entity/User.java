package ru.liga.homework.repository.entity;

import lombok.*;
import ru.liga.homework.type.Gender;
import ru.liga.homework.type.LoveSearch;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users", schema = "otpmm")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SequenceGenerator(name = "users_s", schema = "otpmm", sequenceName = "users_s", allocationSize = 1)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_s")
    private Long id;

    @EqualsAndHashCode.Include
    private Long usertgid;
    private String name;
    private String password;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "form_title")
    private String formTitle;

    @Column(name = "love_search")
    @Enumerated(EnumType.STRING)
    private LoveSearch loveSearch;

    @Column(name = "form_description")
    private String formDescription;

    @Column(name = "form_filename")
    private String formFileName;

    @ManyToMany
    @JoinTable(name = "users_like", schema = "otpmm",
            joinColumns = {@JoinColumn(name = "user_id_who")},
            inverseJoinColumns = {@JoinColumn(name = "user_id_that")})
    private Set<User> likes = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "users_like", schema = "otpmm",
            joinColumns = {@JoinColumn(name = "user_id_that")},
            inverseJoinColumns = {@JoinColumn(name = "user_id_who")})
    private Set<User> likeBy = new HashSet<>();

}