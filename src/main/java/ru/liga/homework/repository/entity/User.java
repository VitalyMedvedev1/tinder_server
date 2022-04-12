package ru.liga.homework.repository.entity;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ru.liga.homework.type.Gender;
import ru.liga.homework.type.LoveSearch;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users", schema = "otpmm")
@AllArgsConstructor
@NoArgsConstructor
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

    @Column(name = "search_love")
    @Enumerated(EnumType.STRING)
    private LoveSearch loveSearch;

    @Column(name = "form_description")
    private String formDescription;

    @Column(name = "form_file_name")
    private String formFileName;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "users_like", schema = "otpmm",
            joinColumns = {@JoinColumn(name = "user_id_who")},
            inverseJoinColumns = {@JoinColumn(name = "user_id_that")})
    private Set<User> likes = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "users_like", schema = "otpmm",
            joinColumns = {@JoinColumn(name = "user_id_that")},
            inverseJoinColumns = {@JoinColumn(name = "user_id_who")})
    private Set<User> likeBy = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUsertgid() {
        return usertgid;
    }

    public void setUsertgid(Long usertgid) {
        this.usertgid = usertgid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public LoveSearch getLoveSearch() {
        return loveSearch;
    }

    public void setLoveSearch(LoveSearch loveSearch) {
        this.loveSearch = loveSearch;
    }

    public String getFormFileName() {
        return formFileName;
    }

    public String getFormTitle() {
        return formTitle;
    }

    public void setFormTitle(String formTitle) {
        this.formTitle = formTitle;
    }

    public String getFormDescription() {
        return formDescription;
    }

    public void setFormDescription(String formDescription) {
        this.formDescription = formDescription;
    }

    public void setFormFileName(String formFileName) {
        this.formFileName = formFileName;
    }

    public Set<User> getLikes() {
        return likes;
    }

    public void setLikes(Set<User> likes) {
        this.likes = likes;
    }

    public Set<User> getLikeBy() {
        return likeBy;
    }

    public void setLikeBy(Set<User> likeBy) {
        this.likeBy = likeBy;
    }
}