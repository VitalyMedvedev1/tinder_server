package ru.liga.homework.db.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "USER_ATTACH", schema = "otpmm")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SequenceGenerator(name = "user_attach_s", schema = "otpmm", sequenceName = "user_attach_s", allocationSize = 1)
public class Attach {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_s")
    private Integer id;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "file_path")
    private String filePath;

    @Column(name = "description")
    private String description;
}