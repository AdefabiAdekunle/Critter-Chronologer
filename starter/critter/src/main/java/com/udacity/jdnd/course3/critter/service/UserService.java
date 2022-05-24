package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.data.Customer;
import com.udacity.jdnd.course3.critter.data.Employee;
import com.udacity.jdnd.course3.critter.data.Pet;
import com.udacity.jdnd.course3.critter.repository.CustomerRepository;
import com.udacity.jdnd.course3.critter.repository.EmployeeRepository;
import com.udacity.jdnd.course3.critter.repository.PetRepository;
import com.udacity.jdnd.course3.critter.user.EmployeeSkill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;

@Service
@Transactional
public class UserService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private PetRepository petRepository;

    public Customer saveCustomer(Customer customer, List<Long> petId){
        List<Pet> pets= new ArrayList<>();
        if(petId!= null && !petId.isEmpty()){
            for(Long id : petId){
                pets.add(petRepository.getOne(id));
            }
            customer.setPets(pets);
           return  customerRepository.save(customer);
        }
        return customerRepository.save(customer);

    }

    public List<Customer> getAllCustomers(){
        return  customerRepository.findAll();
    }

    public Customer getOwnerByPet(long petId){
        return petRepository.getOne(petId).getCustomer();
    }

    public Employee saveEmployee(Employee employee){
        return employeeRepository.save(employee);
    }

    public Employee getEmployee(long employeeId){
        return employeeRepository.getOne(employeeId);
    }

    public void setAvailability(Set<DayOfWeek> dayOfWeeks,long employeeId){
        Optional<Employee> employee = employeeRepository.findById(employeeId);
        Employee employee1 = employee.get();
        employee1.setDaysAvailable(dayOfWeeks);
        employeeRepository.save(employee1);
    }

    public List<Employee> findEmployeesForService(LocalDate date, Set<EmployeeSkill> skills){
        List <Employee> employeeList =employeeRepository.findAllByDaysAvailableContaining(date.getDayOfWeek());
//        System.out.println(employeeList.size());
//        System.out.println(employeeList.get(0).toString());
        List<Employee> newEmployeeList = new ArrayList<>();
        for(Employee employee:employeeList){
            if(employee.getSkills().containsAll(skills)){
                newEmployeeList.add(employee);
            }
        }
        return newEmployeeList;
    }
}
