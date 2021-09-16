package hr.aspira.careapp.backend.model.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
@Slf4j
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;

    @Column
    private String name;

    @Column
    private String lastName;

    @Column
    private String userName;

    @Column
    private String password;

    @Column
    private Boolean isAdmin;

    @Column
    private String address;

    @Column
    private String number;

    @Column
    private String email;

    @OneToMany(mappedBy = "user")
    private List<Task> tasks = new ArrayList<>();
}
