package com.unbeaten.Practon;

import com.unbeaten.Practon.models.Person;
import com.unbeaten.Practon.models.Skill;
import com.unbeaten.Practon.models.Address;
import com.unbeaten.Practon.models.Certification;
import com.unbeaten.Practon.models.PaymentDetails;
import com.unbeaten.Practon.models.Payment;
import com.unbeaten.Practon.repositories.PersonRepository;
import com.unbeaten.Practon.repositories.SkillRepository;
import com.unbeaten.Practon.repositories.CertificationRepository;
import com.unbeaten.Practon.repositories.PaymentRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootApplication
public class PractonApplication {

    public static void main(String[] args) {
        SpringApplication.run(PractonApplication.class, args);
    }

    @Bean
    @Transactional
    CommandLineRunner commandLineRunner(PersonRepository personRepo, SkillRepository skillRepo,
            CertificationRepository certRepo, PaymentRepository paymentRepo) {
        return args -> {


        };
    }
}
