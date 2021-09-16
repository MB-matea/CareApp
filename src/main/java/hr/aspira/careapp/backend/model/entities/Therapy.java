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
public class Therapy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer therapyId;

    @Column
    private String name;

    @Column
    private Integer quantity;

    @OneToMany(mappedBy = "therapy")
    private List<TherapyPlan> therapyPlans = new ArrayList<>();

    @ManyToOne
    private Resident resident;
}
