package com.unbeaten.Practon.security;

import com.unbeaten.Practon.models.Person;
import com.unbeaten.Practon.services.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private PersonService personService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Person person = personService.findPersonByEmail(email);
        if (person == null) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }

        return new User(
                person.getEmail(),
                person.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + person.getRole().name()))
        );
    }
}
