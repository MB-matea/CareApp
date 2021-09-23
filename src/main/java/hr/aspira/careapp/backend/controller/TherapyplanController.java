package hr.aspira.careapp.backend.controller;

import hr.aspira.careapp.backend.model.entities.TherapyPlan;
import hr.aspira.careapp.backend.model.repositories.TherapyPlanRepository;
import hr.aspira.careapp.backend.model.repositories.TherapyRepository;
import org.openapitools.api.TherapyplanApi;
import org.openapitools.model.InlineObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import java.util.Optional;

@Controller
public class TherapyplanController implements TherapyplanApi {

    @Autowired
    private TherapyPlanRepository therapyPlanRepository;

    @Override
    public ResponseEntity<Void> therapyplanTherapyplanIdPatch(Integer therapyplanId, InlineObject inlineObject) {
        Optional<TherapyPlan> therapyPlan = therapyPlanRepository.findById(therapyplanId);

        if(!therapyPlan.isPresent()){
                    return  new ResponseEntity<>(HttpStatus.NOT_FOUND);
                }

        hr.aspira.careapp.backend.model.entities.TherapyPlan therapyPlanNew = therapyPlan.get();

        therapyPlanNew.setIsDone(inlineObject.getIsDone());


        therapyPlanRepository.save(therapyPlanNew);

        return new ResponseEntity<>(HttpStatus.OK);

    }

}
