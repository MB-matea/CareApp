package hr.aspira.careapp.backend.controller;

import hr.aspira.careapp.backend.model.entities.IndependenceStatus;
import hr.aspira.careapp.backend.model.repositories.ResidentRepository;
import hr.aspira.careapp.backend.model.repositories.TherapyRepository;
import org.openapitools.api.ResidentsApi;
import org.openapitools.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

@Controller
public class ResidentsController implements ResidentsApi {
    @Autowired
    private ResidentRepository residentRepository;

    @Autowired
    private TherapyRepository therapyRepository;

    @Override
    public ResponseEntity<GetAllResidentsResponseBody> residentsGet() {
        List<hr.aspira.careapp.backend.model.entities.Resident> residents = residentRepository.findAll();
        List<GetAllResidentsResponseBodyResidents> residentsReturned = new ArrayList<>();

        for(hr.aspira.careapp.backend.model.entities.Resident resident : residents){
            GetAllResidentsResponseBodyResidents residentReturned = new GetAllResidentsResponseBodyResidents();

            residentReturned.setResidentId(resident.getId());
            residentReturned.setName(resident.getName());
            residentReturned.setLastName(resident.getLastName());
            residentReturned.setRoom(resident.getRoom());

            residentsReturned.add(residentReturned);
        }

        GetAllResidentsResponseBody response = new GetAllResidentsResponseBody();
        response.setResidents(residentsReturned);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ReturnId> residentsPost(Resident resident) {
        return ResidentsApi.super.residentsPost(resident);
    }

    @Override
    public ResponseEntity<Void> residentsResidentIdDelete(Integer residentId) {
        return ResidentsApi.super.residentsResidentIdDelete(residentId);
    }

    @Override
    public ResponseEntity<GetSpecificResidentResponseBody> residentsResidentIdGet(Integer residentId, LocalDate date) {
        Optional<hr.aspira.careapp.backend.model.entities.Resident> residents = residentRepository.findById(residentId);

        if(!residents.isPresent()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        // RESIDENT

        hr.aspira.careapp.backend.model.entities.Resident resident = residents.get();

        Resident residentReturned = new Resident();

        residentReturned.setResidentId(residentId);
        residentReturned.setName(resident.getName());
        residentReturned.setLastName(resident.getLastName());
        residentReturned.setIdCard(resident.getIdCard());
        residentReturned.setCitizenship(resident.getCitizenship());
        residentReturned.setContactAddress(resident.getContactAddress());
        residentReturned.setContactEmail(resident.getContactEmail());
        residentReturned.setContactName(resident.getContactName());
        residentReturned.setContactNumber(resident.getContactNumber());
        residentReturned.setContactRelationship(resident.getContactRelationship());
        residentReturned.setDateOfBirth(resident.getDateOfBirth());
        residentReturned.setPlaceOfBirth(resident.getPlaceOfBirth());
        residentReturned.setNote(resident.getNote());
        residentReturned.setOib(resident.getOib());
        residentReturned.setRoom(resident.getRoom());;
        residentReturned.setNationality(resident.getNacionality());
        residentReturned.setIndependence(Resident.IndependenceEnum.valueOf(resident.getIndependence().toString()));
        residentReturned.setMobility(Resident.MobilityEnum.valueOf(resident.getMobility().toString()));

        // THERAPIES

        List<hr.aspira.careapp.backend.model.entities.Therapy> therapies = resident.getTherapies();
        List<Therapy> therapiesReturned = new ArrayList<>();

        for(hr.aspira.careapp.backend.model.entities.Therapy therapy : therapies){
            Therapy therapyReturned = new Therapy();

            therapyReturned.setId(therapy.getId());
            therapyReturned.setName(therapy.getName());
            therapyReturned.setQuantity(therapy.getQuantity());

            therapiesReturned.add(therapyReturned);
        }

        // THERAPY_PLANS

        List<hr.aspira.careapp.backend.model.entities.TherapyPlan> therapyPlans = resident.getTherapyPlans();
        List<TherapyPlan> therapyPlansReturned = new ArrayList<>();

        for(hr.aspira.careapp.backend.model.entities.TherapyPlan therapyPlan : therapyPlans){

            if(therapyPlan.getDate().isEqual(date)){
                TherapyPlan therapyPlanReturned = new TherapyPlan();

                therapyPlanReturned.setId(therapyPlan.getId());
                therapyPlanReturned.setDate(therapyPlan.getDate());
                therapyPlanReturned.setIsDone(therapyPlan.getIsDone());

                therapyPlansReturned.add(therapyPlanReturned);
            }

        }

        // RESPONSE

        GetSpecificResidentResponseBody response = new GetSpecificResidentResponseBody();
        response.setResident(residentReturned);
        response.setTherapies(therapiesReturned);
        response.setTherapyPlans(therapyPlansReturned);

        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @Override
    public ResponseEntity<Void> residentsResidentIdPut(Integer residentId, Resident resident) {
        return ResidentsApi.super.residentsResidentIdPut(residentId, resident);
    }
}
