package ru.job4j.controller;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import ru.job4j.domain.Employee;
import ru.job4j.domain.Person;
import ru.job4j.repository.EmployeeRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/employees")
public class EmployeeController {

    private final RestTemplate restTemplate;
    private final EmployeeRepository employeeRepository;

    private static final String API = "http://localhost:8080/person/";
    private static final String API_ID = "http://localhost:8080/person/{id}";

    public EmployeeController(RestTemplate restTemplate, EmployeeRepository employeeRepository) {
        this.restTemplate = restTemplate;
        this.employeeRepository = employeeRepository;
    }

    @GetMapping("/")
    public List<Employee> getAllEmployees() {
        List<Employee> rsl = new ArrayList<>(employeeRepository.findAll());
        List<Person> personAccounts = restTemplate.exchange(
                API,
                HttpMethod.GET, null, new ParameterizedTypeReference<List<Person>>() {
                }
        ).getBody();
        for (Employee employee : rsl) {
            employee.setAccountsList(personAccounts.stream()
                    .filter(person -> person.getEmployeeId() == employee.getId())
                    .collect(Collectors.toList()));
        }
        return rsl;
    }

    @PostMapping("/{employeeId}/person")
    public ResponseEntity<Person> createAccountForEmployee(@PathVariable int employeeId, @RequestBody Person person) {
        Employee byId = employeeRepository.findById(employeeId).orElse(null);
        if (byId == null) {
            return new ResponseEntity<>(
                    null,
                    HttpStatus.NOT_FOUND
            );
        }
        Person rsl = restTemplate.postForObject(API, person, Person.class);
        byId.addAccount(rsl);
        employeeRepository.save(byId);
        return new ResponseEntity<>(rsl, HttpStatus.CREATED);
    }

    @PutMapping("/{employeeId}/person")
    public ResponseEntity<Void> update(@PathVariable int employeeId, @RequestBody Person account) {
        Employee byId = employeeRepository.findById(employeeId).orElse(null);
        if (byId == null) {
            return new ResponseEntity<>(
                    null,
                    HttpStatus.NOT_FOUND
            );
        }
        if(byId.getId() == account.getEmployeeId()) {
            restTemplate.put(API_ID, account);
            byId.addAccount(account);
            employeeRepository.save(byId);
        }
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        Employee byId = employeeRepository.findById(id).orElse(null);
        if (byId == null) {
            return new ResponseEntity<>(
                    null,
                    HttpStatus.NOT_FOUND
            );
        }
        restTemplate.delete(API_ID, id);
        return ResponseEntity.ok().build();
    }
}
