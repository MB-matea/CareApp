package hr.aspira.careapp.backend.controller;

import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import hr.aspira.careapp.backend.common.LocalDateDeserializer;
import hr.aspira.careapp.backend.model.entities.IndependenceStatus;
import hr.aspira.careapp.backend.model.entities.MobilityStatus;
import hr.aspira.careapp.backend.model.repositories.ResidentRepository;
import hr.aspira.careapp.backend.model.repositories.TaskRepository;
import hr.aspira.careapp.backend.model.repositories.TherapyPlanRepository;
import hr.aspira.careapp.backend.model.repositories.TherapyRepository;
import lombok.extern.slf4j.Slf4j;
import org.openapitools.api.ResidentsApi;
import org.openapitools.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
@Controller
public class ResidentsController implements ResidentsApi {
    @Autowired
    private ResidentRepository residentRepository;

    @Autowired
    private TherapyRepository therapyRepository;

    @Autowired
    private TherapyPlanRepository therapyPlanRepository;

    @Autowired
    private TaskRepository taskRepository;

    private final ObjectMapper objectMapper;

    public ResidentsController(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        SimpleModule module = new SimpleModule();
        module.addDeserializer(LocalDate.class, new LocalDateDeserializer());
        this.objectMapper.registerModule(module);
    }

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

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX")
    @Override
    public ResponseEntity<ReturnId> residentsPost(CreateResidentRequestBody residentRequestBody) {
        hr.aspira.careapp.backend.model.entities.Resident residentNew = new hr.aspira.careapp.backend.model.entities.Resident();

        List<hr.aspira.careapp.backend.model.entities.Therapy> therapyList = new ArrayList<>();

        residentNew.setName(residentRequestBody.getResident().getName());
        residentNew.setLastName(residentRequestBody.getResident().getLastName());
        residentNew.setIdCard(residentRequestBody.getResident().getIdCard());
        residentNew.setCitizenship(residentRequestBody.getResident().getCitizenship());
        residentNew.setContactAddress(residentRequestBody.getResident().getContactAddress());
        residentNew.setContactEmail(residentRequestBody.getResident().getContactEmail());
        residentNew.setContactName(residentRequestBody.getResident().getContactName());
        residentNew.setContactNumber(residentRequestBody.getResident().getContactNumber());
        residentNew.setContactRelationship(residentRequestBody.getResident().getContactRelationship());
        residentNew.setDateOfBirth(residentRequestBody.getResident().getDateOfBirth());
        residentNew.setPlaceOfBirth(residentRequestBody.getResident().getPlaceOfBirth());
        residentNew.setNote(residentRequestBody.getResident().getNote());
        residentNew.setOib(residentRequestBody.getResident().getOib());
        residentNew.setRoom(residentRequestBody.getResident().getRoom());;
        residentNew.setNacionality(residentRequestBody.getResident().getNationality());
        residentNew.setIndependence(IndependenceStatus.valueOf(residentRequestBody.getResident().getIndependence().toString()));
        residentNew.setMobility(MobilityStatus.valueOf(residentRequestBody.getResident().getMobility().toString()));

        List<Therapy> requestTherapyList = residentRequestBody.getTherapy();

        for (Therapy therapy : requestTherapyList){
            hr.aspira.careapp.backend.model.entities.Therapy therapyNew = new hr.aspira.careapp.backend.model.entities.Therapy();

            therapyNew.setName(therapy.getName());
            therapyNew.setQuantity(therapy.getQuantity());
            therapyNew.setResident(residentNew);
            therapyList.add(therapyNew);
        }

        residentNew.setTherapies(therapyList);

        residentRepository.save(residentNew);
        therapyRepository.save(residentNew.getTherapies().get(0));

        ReturnId response = new ReturnId();
        response.setId(residentNew.getId());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @Override
    public ResponseEntity<Void> residentsResidentIdDelete(Integer residentId) {
        Optional<hr.aspira.careapp.backend.model.entities.Resident> residents = residentRepository.findById(residentId);
        if(residents.isPresent()){
            hr.aspira.careapp.backend.model.entities.Resident resident = residents.get();
            List<hr.aspira.careapp.backend.model.entities.Therapy> therapies = resident.getTherapies();
            List<hr.aspira.careapp.backend.model.entities.TherapyPlan> therapyPlans = resident.getTherapyPlans();
            List<hr.aspira.careapp.backend.model.entities.Task> tasks = resident.getTasks();

            for (hr.aspira.careapp.backend.model.entities.TherapyPlan therapyPlan : therapyPlans){
                therapyPlanRepository.delete(therapyPlan);
            }

            for (hr.aspira.careapp.backend.model.entities.Therapy therapy : therapies){
                therapyRepository.delete(therapy);
            }

            for (hr.aspira.careapp.backend.model.entities.Task task : tasks){
                taskRepository.delete(task);
            }

            residentRepository.delete(resident);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<GetSpecificResidentResponseBody> residentsResidentIdGet(Integer residentId, @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX") LocalDate date) {
        log.info(String.valueOf(date));

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
        Optional<hr.aspira.careapp.backend.model.entities.Resident> residentsNew = residentRepository.findById(residentId);
        hr.aspira.careapp.backend.model.entities.Resident residentNew = residentsNew.get();

        residentNew.setName(resident.getName());
        residentNew.setLastName(resident.getLastName());
        residentNew.setIdCard(resident.getIdCard());
        residentNew.setCitizenship(resident.getCitizenship());
        residentNew.setContactAddress(resident.getContactAddress());
        residentNew.setContactEmail(resident.getContactEmail());
        residentNew.setContactName(resident.getContactName());
        residentNew.setContactNumber(resident.getContactNumber());
        residentNew.setContactRelationship(resident.getContactRelationship());
        residentNew.setDateOfBirth(resident.getDateOfBirth());
        residentNew.setPlaceOfBirth(resident.getPlaceOfBirth());
        residentNew.setNote(resident.getNote());
        residentNew.setOib(resident.getOib());
        residentNew.setRoom(resident.getRoom());;
        residentNew.setNacionality(resident.getNationality());
        residentNew.setIndependence(IndependenceStatus.valueOf(resident.getIndependence().toString()));
        residentNew.setMobility(MobilityStatus.valueOf(resident.getMobility().toString()));

        residentRepository.save(residentNew);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
