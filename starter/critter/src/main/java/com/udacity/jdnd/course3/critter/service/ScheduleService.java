package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.data.Employee;
import com.udacity.jdnd.course3.critter.data.Pet;
import com.udacity.jdnd.course3.critter.data.Schedule;
import com.udacity.jdnd.course3.critter.repository.EmployeeRepository;
import com.udacity.jdnd.course3.critter.repository.PetRepository;
import com.udacity.jdnd.course3.critter.repository.ScheduleRepository;
import com.udacity.jdnd.course3.critter.schedule.ScheduleDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ScheduleService {
    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private PetRepository petRepository;

    public Schedule createSchedule(Schedule schedule, List<Long> employeeIds, List<Long> petIds){
        List<Pet> pets= new ArrayList<>();
        List<Employee> employeeList= new ArrayList<>();
        if(petIds!= null && !petIds.isEmpty()){
            for(Long id : petIds){
                pets.add(petRepository.getOne(id));
            }
            schedule.setPets(pets);
        }
        if(employeeIds!= null && !employeeIds.isEmpty()){
            for(Long id : employeeIds){
                employeeList.add(employeeRepository.getOne(id));
            }
            schedule.setEmployee(employeeList);
        }
        return scheduleRepository.save(schedule);
    }


    public List<Schedule> getAllSchedules() {
       return scheduleRepository.findAll();
    }

    public List<Schedule> getScheduleForPet( long petId) {
        Pet pet = petRepository.getOne(petId);
        return scheduleRepository.findAllByPetsContaining(pet);
    }

    public List<Schedule> getScheduleForEmployee( long employeeId) {
        Employee employee = employeeRepository.getOne(employeeId);
        return scheduleRepository.findAllByEmployeeContaining(employee);
    }

    public List<Schedule> getScheduleForCustomer(long customerId) {
        List<Pet> pets= petRepository.findAllByCustomerId(customerId);
        List<Schedule> schedules =scheduleRepository.findAll();
        List<Schedule> schedules2= new ArrayList<>();
        for(Schedule schedule:schedules){

            if(schedule.getPets().containsAll(pets)){
                schedules2.add(schedule);
            }
        }
        return schedules2;
        //return scheduleRepository.findAllByPetsIn(pets);
    }
}
