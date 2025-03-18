package com.unbeaten.Practon;

import com.unbeaten.Practon.models.Person;
import com.unbeaten.Practon.models.Skill;
import com.unbeaten.Practon.models.Address;
import com.unbeaten.Practon.repositories.PersonRepository;
import com.unbeaten.Practon.repositories.SkillRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class PractonApplication {

    public static void main(String[] args) {
        SpringApplication.run(PractonApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(PersonRepository personRepo, SkillRepository skillRepo) {
        return args -> {
            // Creating Address objects
            Address address1 = new Address("123 Main St", "Springfield", "IL", "USA", "62701");
            Address address2 = new Address("456 Elm St", "Metropolis", "NY", "USA", "10001");
            Address address3 = new Address("789 Oak S", "Gotham", "NJ", "USA", "07001");

            // Creating multiple Person objects with addresses
            List<Person> people = List.of(
                    new Person("Mike", "Mugisha", "mugisha@gmail.com", address1),
                    new Person("John", "Doe", "john.doe@example.com", address2),
                    new Person("Alice", "Smith", "alice.smith@example.com", address3));

            // Save all Person objects
            personRepo.saveAll(people);

            // Creating Skill objects and associating them with Persons
            Skill skill1 = new Skill("Java Programming", "Proficient in Java", people.get(0));
            Skill skill2 = new Skill("Database Management", "Experienced with SQL databases", people.get(1));
            Skill skill3 = new Skill("Web Development", "Skilled in HTML, CSS, and JavaScript", people.get(2));

            // Save all Skill objects
            skillRepo.saveAll(List.of(skill1, skill2, skill3));
        };
    }
}
