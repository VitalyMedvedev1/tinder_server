package ru.liga.homework.repository.entity;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import ru.liga.homework.type.Gender;
import ru.liga.homework.type.LoveSearch;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "users", schema = "otpmm")
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
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "header")
    private String header;

    @Column(name = "lookingfor")
    @Enumerated(EnumType.STRING)
    private LoveSearch loveSearch;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id.equals(user.id) && usertgid.equals(user.usertgid) &&
                name.equals(user.name) && password.equals(user.password) &&
                gender.equals(user.gender) && Objects.equals(header, user.header) &&
                loveSearch.equals(user.loveSearch) && Objects.equals(formFileName, user.formFileName);
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + id.hashCode();
        result = 31 * result + usertgid.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + password.hashCode();
        result = 31 * result + gender.hashCode();
        result = 31 * result + header.hashCode();
        result = 31 * result + loveSearch.hashCode();
        result = 31 * result + formFileName.hashCode();
        return result;
    }
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

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFormFileName() {
        return formFileName;
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