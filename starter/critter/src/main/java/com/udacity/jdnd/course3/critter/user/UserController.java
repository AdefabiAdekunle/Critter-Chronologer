package com.udacity.jdnd.course3.critter.user;

import com.udacity.jdnd.course3.critter.data.Customer;
import com.udacity.jdnd.course3.critter.data.Employee;
import com.udacity.jdnd.course3.critter.data.Pet;
import com.udacity.jdnd.course3.critter.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Handles web requests related to Users.
 *
 * Includes requests for both customers and employees. Splitting this into separate user and customer controllers
 * would be fine too, though that is not part of the required scope for this class.
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;


    @PostMapping("/customer")
    public CustomerDTO saveCustomer(@RequestBody CustomerDTO customerDTO){
        List<Long> petId= customerDTO.getPetIds();
        try{
            Customer customer =userService.saveCustomer(convertCustomerDTOtoCustomer(customerDTO),petId);
            customerDTO.setId(customer.getId());
            return customerDTO;
        }
        catch (Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Customer could not be saved", e);
        }
    }

    @GetMapping("/customer")
    public List<CustomerDTO> getAllCustomers(){
        try{
           return convertCustomersToCustomerDTOs(userService.getAllCustomers());
        }
        catch(Exception e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"No record of Customers",e);
        }

    }

    @GetMapping("/customer/pet/{petId}")
    public CustomerDTO getOwnerByPet(@PathVariable long petId){

        try{
           return convertCustomerToCustomerDTO(userService.getOwnerByPet(petId));
        }
        catch(Exception e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Customer not found",e);
        }

    }

    @PostMapping("/employee")
    public EmployeeDTO saveEmployee(@RequestBody EmployeeDTO employeeDTO) {

        try {
            Employee employee= userService.saveEmployee(convertEmployeeDTOtoEmployee(employeeDTO));
            employeeDTO.setId(employee.getId());
            return employeeDTO;
        }
        catch(Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Employee could not be saved", e);
        }

    }

    @GetMapping("/employee/{employeeId}")
    public EmployeeDTO getEmployee(@PathVariable long employeeId) {
        try{
            Employee employee= userService.getEmployee(employeeId);
            EmployeeDTO employeeDTO = convertEmployeeToEmployeeDTO(employee);
            employeeDTO.setId(employee.getId());
            return employeeDTO;
        }
        catch (Exception e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Employee not found",e);
        }

    }

    @PutMapping("/employee/{employeeId}")
    public void setAvailability(@RequestBody Set<DayOfWeek> daysAvailable, @PathVariable long employeeId) {
        try {
            userService.setAvailability(daysAvailable,employeeId);
        }
        catch (Exception e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Employee not found",e);
        }
    }

    @GetMapping("/employee/availability")
    public List<EmployeeDTO> findEmployeesForService(@RequestBody EmployeeRequestDTO employeeDTO) {

        try {
            LocalDate date = employeeDTO.getDate();
            Set<EmployeeSkill> skills= employeeDTO.getSkills();
            return convertEmployersToEmployerDTOs(userService.findEmployeesForService(date,skills));
        }
        catch (Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Employees not found",e);
        }
    }

                                           //DTO'S
    private static Customer convertCustomerDTOtoCustomer(CustomerDTO customerDTO){
        Customer customer = new Customer();
        BeanUtils.copyProperties(customerDTO,customer);
        return customer;
    }
    private static List<CustomerDTO> convertCustomersToCustomerDTOs(List<Customer> customers){
        List<CustomerDTO> customerDTOList= new ArrayList<>();
        CustomerDTO customerDTO;
        List<Long> petIds;
        List<Pet> pets;
        for(Customer customer: customers){
           petIds=new ArrayList<>();
           pets=customer.getPets();
           if(pets!=null){
               for(Pet pet: pets){
                   petIds.add(pet.getId());
               }
           }

          customerDTO= new CustomerDTO();

            BeanUtils.copyProperties(customer,customerDTO);
            customerDTO.setPetIds(petIds);
            customerDTOList.add(customerDTO);
        }
        return customerDTOList;
    }


    private static CustomerDTO convertCustomerToCustomerDTO(Customer customer){
        List<Long> petIds=new ArrayList<>();
        CustomerDTO customerDTO= new CustomerDTO();
        BeanUtils.copyProperties(customer,customerDTO);
        List<Pet> pets=customer.getPets();
        for(Pet pet: pets){
            petIds.add(pet.getId());
        }
        customerDTO.setPetIds(petIds);
        return  customerDTO;
    }

    private static Employee convertEmployeeDTOtoEmployee(EmployeeDTO employeeDTO){
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDTO,employee);
        return employee;
    }

    private static EmployeeDTO convertEmployeeToEmployeeDTO(Employee employee){
        EmployeeDTO employeeDTO = new EmployeeDTO();
        BeanUtils.copyProperties(employee,employeeDTO);
        return employeeDTO;
    }

    private static List<EmployeeDTO> convertEmployersToEmployerDTOs(List<Employee> employees){
        List<EmployeeDTO> employeeDTOS = new ArrayList<>();
        EmployeeDTO employeeDTO;
        for (Employee employee: employees){
            employeeDTO = new EmployeeDTO();
            BeanUtils.copyProperties(employee,employeeDTO);
            employeeDTOS.add(employeeDTO);
        }

        return employeeDTOS;
    }
}
