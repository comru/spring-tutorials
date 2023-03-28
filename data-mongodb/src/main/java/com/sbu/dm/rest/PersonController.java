package com.sbu.dm.rest;

import com.sbu.dm.domain.Person;
import com.sbu.dm.repository.PersonRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/persons")
@RequiredArgsConstructor
public class PersonController {

    private final PersonRepository personRepository;

    @DeleteMapping("/{id}")
    public void deleteOne(@PathVariable("id") String hexString) {
        personRepository.deleteById(new ObjectId(hexString));
    }

    @GetMapping("/many")
    public List<Person> getMany(@RequestParam("ids") List<String> hexStrings) {
        List<ObjectId> ids = hexStrings.stream().map(ObjectId::new).toList();
        return personRepository.findAllById(ids);
    }

    @GetMapping
    public List<Person> getList(@ParameterObject Pageable pageable) {
        return personRepository.findAll(pageable).getContent();
    }

    @GetMapping("/{id}")
    public Optional<Person> getOne(@PathVariable("id") String hexString) {
        return personRepository.findById(new ObjectId(hexString));
    }

    @PostMapping
    public Person create(@RequestBody @Valid Person entity) {
        return personRepository.save(entity);
    }
}

