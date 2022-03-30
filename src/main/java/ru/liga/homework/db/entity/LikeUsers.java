package ru.liga.homework.db.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;


@Entity
@Table(name = "likeusers", schema = "public")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@SequenceGenerator(name = "like_s", sequenceName = "like_s", allocationSize = 1)
public class LikeUsers {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "like_s")
    private Long id;

    @Column(name = "usernamefirst")
    private String userNameFirst;

    @Column(name = "usernamesecond")
    private String userNameSecond;

    @Column(name = "userfirstflg")
    private Boolean userFirstFlg;

    @Column(name = "usersecondflg")
    private Boolean userSecondFlg;

}