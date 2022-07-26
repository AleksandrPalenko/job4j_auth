package ru.job4j.domain;

import javax.persistence.*;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.*;

@Entity
@Table(name = "employees")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private Long inn;
    private Date created = new Date(System.currentTimeMillis());

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "employee_id")
    private List<Person> accountsList = new ArrayList<>();

    public Employee() {
    }

    public static Employee of(int id, String name, List<Person> accountsList) {
        Employee em = new Employee();
        em.id = id;
        em.name = name;
        em.accountsList = accountsList;
        em.created = new Date(System.currentTimeMillis());
        return em;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getInn() {
        return inn;
    }

    public void setInn(Long inn) {
        this.inn = inn;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public List<Person> getAccountsList() {
        return accountsList;
    }

    public void setAccountsList(List<Person> accountsList) {
        this.accountsList = accountsList;
    }

    public void addAccount(Person person) {
        accountsList.add(person);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Employee employee = (Employee) o;
        return id == employee.id && name.equals(employee.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "Employee{"
                + "id=" + id
                + ", name='" + name + '\''
                + '}';
    }

}
