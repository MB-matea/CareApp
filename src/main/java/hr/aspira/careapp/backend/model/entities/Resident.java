package hr.aspira.careapp.backend.model.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
@Slf4j
@NoArgsConstructor
public class Resident {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String name;

    @Column
    private String lastName;

    @Column
    private String oib;

    @Column
    private Integer room;

    @Column
    private LocalDate dateOfBirth;

    @Column
    private String placeOfBirth;

    @Column
    private String citizenship;

    @Column
    private String nacionality;

    @Column
    private String idCard;

    @Column
    private String contactName;

    @Column
    private String contactRelationship;

    @Column
    private String contactNumber;

    @Column
    private String contactEmail;

    @Column
    private String contactAddress;

    @Column
    @Enumerated(EnumType.STRING)
    private MobilityStatus mobility;

    @Column
    @Enumerated(EnumType.STRING)
    private IndependenceStatus independence;

    @Column
    private String note;

    @OneToMany(mappedBy = "resident")
    private List<Task> tasks = new ArrayList<>();

    @OneToMany(mappedBy = "resident")
    private List<TherapyPlan> therapyPlans = new ArrayList<>();

    @OneToMany(mappedBy = "resident")
    private  List<Therapy> therapies = new ArrayList<>();
}
