package com.udacity.jdnd.course3.critter.pet;

import com.udacity.jdnd.course3.critter.data.Customer;
import com.udacity.jdnd.course3.critter.data.Pet;
import com.udacity.jdnd.course3.critter.service.PetService;
import com.udacity.jdnd.course3.critter.service.UserService;
import com.udacity.jdnd.course3.critter.user.CustomerDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles web requests related to Pets.
 */
@RestController
@RequestMapping("/pet")
public class PetController {

    @Autowired
    private PetService petService;

    @PostMapping
    public PetDTO savePet(@RequestBody PetDTO petDTO) {
        try{
            Pet pet = petService.savePet(convertPetDTOtoPet(petDTO),petDTO.getOwnerId());
            petDTO.setId(pet.getId());
            return petDTO;
        }
        catch(Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Pet could not be saved", e);
        }

    }

    @GetMapping("/{petId}")
    public PetDTO getPet(@PathVariable long petId) {
       try {
           Pet pet= petService.getPet(petId);
           PetDTO petDTO = convertPetToPetDTO(pet);
           petDTO.setOwnerId(pet.getCustomer().getId());
           return petDTO;
       }
       catch(Exception e){
           throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pet could not be found", e);
       }
    }

    @GetMapping
    public List<PetDTO> getPets(){
        try{
            return convertPetsToPetDTOs(petService.getPets());
        }
        catch(Exception e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"No record of Customers",e);
        }
    }

    @GetMapping("/owner/{ownerId}")
    public List<PetDTO> getPetsByOwner(@PathVariable long ownerId) {
        try {
            List<Pet> pets= petService.getPetsByOwner(ownerId);
            return convertPetsToPetDTOs(petService.getPets());
        }
        catch(Exception e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Owners record not found",e);
        }
    }

                          //DTO'S
    private static Pet convertPetDTOtoPet(PetDTO petDTO){
        Pet pet = new Pet();
        BeanUtils.copyProperties(petDTO,pet);
        return pet;
    }

    private static PetDTO convertPetToPetDTO(Pet pet){
        PetDTO petDTO = new PetDTO();
        BeanUtils.copyProperties(pet,petDTO);
        return petDTO;
    }
    private static List<PetDTO> convertPetsToPetDTOs(List<Pet> pets){
        List<PetDTO> petDTOList=new ArrayList<>();
        PetDTO petDTO;
        for(Pet pet:pets){
           petDTO= new PetDTO();
            BeanUtils.copyProperties(pet,petDTO);
            petDTO.setOwnerId(pet.getCustomer().getId());
            petDTOList.add(petDTO);
        }
        return petDTOList;
    }
}
