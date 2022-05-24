package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.data.Customer;
import com.udacity.jdnd.course3.critter.data.Pet;
import com.udacity.jdnd.course3.critter.pet.PetDTO;
import com.udacity.jdnd.course3.critter.repository.CustomerRepository;
import com.udacity.jdnd.course3.critter.repository.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class PetService {

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private CustomerRepository customerRepository;


    public Pet savePet(Pet pet,long ownerId){
        Customer customer = customerRepository.getOne(ownerId);
        List<Pet> pets = customer.getPets();
        pet.setCustomer(customer);
        pet = petRepository.save(pet);
        //pets.add(pet);
        if(pets==null){
            pets=new ArrayList<>();
        }
        pets.add(pet);
        customer.setPets(pets);
        customerRepository.save(customer);
        return pet;
    }

    public Pet getPet(long petId){
        return petRepository.getOne(petId);
    }
    public List<Pet> getPets(){
        return petRepository.findAll();
    }

    public List<Pet> getPetsByOwner(long ownerId){
        Customer customer=customerRepository.getOne(ownerId);
        //System.out.println(" The size of the list is "+ petRepository.findAllByCustomerId(ownerId).size());
        //return petRepository.findAllByCustomerId(ownerId);
        return customer.getPets();
    }
}
