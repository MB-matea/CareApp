package hr.aspira.careapp.backend.model.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@ToString
@Slf4j
@NoArgsConstructor
public class TherapyPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    private Resident resident;

    @ManyToOne
    private Therapy therapy;

    @Column
    private LocalDate date;

    @Column
    private Boolean isDone;
}
