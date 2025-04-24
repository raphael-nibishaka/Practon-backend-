package com.unbeaten.Practon.services;

import com.unbeaten.Practon.models.Person;
import com.unbeaten.Practon.repositories.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PersonService {

    @Autowired
    private PersonRepository personRepository;

    /**
     * Adds a new Person to the database.
     *
     * @param person The person object to be saved.
     */
    public void addPerson(Person person) {
        personRepository.save(person);
    }

    /**
     * Retrieves all persons from the database.
     *
     * @return List of all persons.
     */
    public List<Person> findAllPersons() {
        return personRepository.findAll();
    }


    // Add this method to your PersonService class
    public void updatePerson(Person person) {
        personRepository.save(person);
    }

    /**
     * Deletes a specific person from the database.
     *
     * @param person The person object to be deleted.
     */
    @Deprecated
    public void deletePerson(Person person) {
        personRepository.delete(person);
    }

    /**
     * Finds a person by their ID.
     *
     * @param id The ID of the person.
     * @return The person object if found, null otherwise.
     */
    public Person findPersonById(Integer id) {
        return personRepository.findById(id).orElse(null);
    }

    /**
     * Finds a person by their email.
     *
     * @param email The email address.
     * @return The person object if found, null otherwise.
     */
    public Person findPersonByEmail(String email) {
        return personRepository.findByEmail(email).orElse(null);
    }

    /**
     * Checks if a person with the given email exists.
     *
     * @param email The email address to check.
     * @return True if exists, false otherwise.
     */
    public boolean existsByEmail(String email) {
        return personRepository.findByEmail(email).isPresent();
    }

    /**
     * Returns an Optional<Person> by email (used in login scenarios).
     *
     * @param email The email address.
     * @return Optional<Person> result.
     */
    public Optional<Person> findByEmail(String email) {
        return personRepository.findByEmail(email);
    }
}
