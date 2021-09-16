package hr.aspira.careapp.backend.service;

import hr.aspira.careapp.backend.model.entities.*;
import hr.aspira.careapp.backend.model.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class TestService {

    @Autowired
    private ResidentRepository residentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TherapyRepository therapyRepository;

    @Autowired
    private TherapyPlanRepository therapyPlanRepository;

    public void fillTestData() {
        Resident resident = new Resident();
        resident.setName("ante");
        resident.setLastName("antić");
        resident.setMobility(MobilityStatus.IMMOBILE);
        resident.setRoom(25);
        resident.setCitizenship("HR");
        resident.setOib("041604544");
        resident = residentRepository.save(resident);

        User user = new User();
        user.setName("mate");
        user.setLastName("matić");
        user.setNumber("+43 535 1671679");
        user = userRepository.save(user);

        Task task = new Task();
        task.setResident(resident);
        task.setUser(user);
        task.setIsDone(false);
        task.setDate(LocalDate.now());
        taskRepository.save(task);

        Therapy therapy = new Therapy();
        therapy.setName("Ibuprofen 200 mg");
        therapy.setQuantity(2);
        therapy = therapyRepository.save(therapy);

        TherapyPlan therapyPlan = new TherapyPlan();
        therapyPlan.setTherapy(therapy);
        therapyPlan.setResident(resident);
        therapyPlan.setIsDone(true);
        therapyPlan.setDate(LocalDate.now());
        therapyPlanRepository.save(therapyPlan);
    }
}
