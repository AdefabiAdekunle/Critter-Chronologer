package com.udacity.jdnd.course3.critter.schedule;

import com.udacity.jdnd.course3.critter.data.Customer;
import com.udacity.jdnd.course3.critter.data.Employee;
import com.udacity.jdnd.course3.critter.data.Pet;
import com.udacity.jdnd.course3.critter.data.Schedule;
import com.udacity.jdnd.course3.critter.pet.PetDTO;
import com.udacity.jdnd.course3.critter.service.ScheduleService;
import com.udacity.jdnd.course3.critter.user.CustomerDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles web requests related to Schedules.
 */
@RestController
@RequestMapping("/schedule")
public class ScheduleController {

    @Autowired
    private ScheduleService scheduleService;

    @PostMapping
    public ScheduleDTO createSchedule(@RequestBody ScheduleDTO scheduleDTO) {

        try {
            List<Long> employeeIds = scheduleDTO.getEmployeeIds();
            List<Long> petIds = scheduleDTO.getPetIds();
            Schedule schedule = scheduleService.createSchedule(convertScheduleDTOtoSchedule(scheduleDTO),
                    employeeIds,petIds);

            scheduleDTO.setId(schedule.getId());
            return scheduleDTO;
        }
        catch (Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Schedule cannot be created", e);
        }

    }

    @GetMapping
    public List<ScheduleDTO> getAllSchedules() {

        try{
            return convertSchedulesToScheduleDTOs(scheduleService.getAllSchedules());
        }
        catch(Exception e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"No record of Customers",e);
        }
    }

    @GetMapping("/pet/{petId}")
    public List<ScheduleDTO> getScheduleForPet(@PathVariable long petId) {
       try {
           return convertSchedulesToScheduleDTOs(scheduleService.getScheduleForPet(petId));
       }
       catch(Exception e){
           throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Pet not found",e);
       }
    }

    @GetMapping("/employee/{employeeId}")
    public List<ScheduleDTO> getScheduleForEmployee(@PathVariable long employeeId) {
        try {
            return convertSchedulesToScheduleDTOs(scheduleService.getScheduleForEmployee(employeeId));
        }
        catch(Exception e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Employee not found",e);
        }
    }

    @GetMapping("/customer/{customerId}")
    public List<ScheduleDTO> getScheduleForCustomer(@PathVariable long customerId) {

        try {
            return convertSchedulesToScheduleDTOs(scheduleService.getScheduleForCustomer(customerId));
        }
        catch(Exception e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Customer not found",e);
        }
    }

                                  //DTO'S
    private static Schedule convertScheduleDTOtoSchedule(ScheduleDTO scheduleDTO){
        Schedule schedule = new Schedule();
        BeanUtils.copyProperties(scheduleDTO,schedule);
        return schedule;
    }
    private static List<ScheduleDTO> convertSchedulesToScheduleDTOs(List<Schedule> schedules){
        List<ScheduleDTO> scheduleDTOList= new ArrayList<>();
        ScheduleDTO scheduleDTO;
        List<Long> petIds;
        List<Pet> pets;
        List<Long> employeeIds;
        List<Employee> employees;
        for(Schedule schedule: schedules){
            petIds=new ArrayList<>();
            pets=schedule.getPets();
            for(Pet pet: pets){
                petIds.add(pet.getId());
            }

            employeeIds=new ArrayList<>();
            employees=schedule.getEmployee();
            for(Employee employee: employees){
                employeeIds.add(employee.getId());
            }

            scheduleDTO = new ScheduleDTO();
            BeanUtils.copyProperties(schedule,scheduleDTO);
            scheduleDTO.setPetIds(petIds);
            scheduleDTO.setEmployeeIds(employeeIds);
            scheduleDTOList.add(scheduleDTO);
        }

        return scheduleDTOList;
    }
}
