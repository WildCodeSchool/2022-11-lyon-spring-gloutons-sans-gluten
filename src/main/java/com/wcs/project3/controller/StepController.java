package com.wcs.project3.controller;

import com.wcs.project3.entity.Step;
import com.wcs.project3.repository.RecipeRepository;
import com.wcs.project3.repository.StepRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class StepController {
    @Autowired
    StepRepository stepRepository;
    @Autowired
    RecipeRepository recipeRepository;

    @GetMapping("/steps")
    public List<Step> getAll(){return stepRepository.findAll();}

    @GetMapping("/steps/{id}")
    public Step getStep(@PathVariable Long id){return stepRepository.findById(id).get();}

    @PutMapping("/steps/{id}")
    public Step updateStep(@PathVariable Long id, @RequestBody Step step){
        Step stepToUpdate = stepRepository.findById(id).get();
        stepToUpdate.setTitle(step.getTitle());
        stepToUpdate.setStepNumber(step.getStepNumber());
        stepToUpdate.setDescription(step.getDescription());
        return stepRepository.save(stepToUpdate);
    }

    @DeleteMapping("/steps/{id}")
    public Boolean deleteStep(@PathVariable Long id){
        stepRepository.deleteById(id);
        return true;
    }
}

