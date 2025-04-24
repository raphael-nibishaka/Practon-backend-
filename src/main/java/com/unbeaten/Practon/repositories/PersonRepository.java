package com.unbeaten.Practon.repositories;

import com.unbeaten.Practon.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PersonRepository extends JpaRepository<Person, Integer> {



    // Basic Finders
    Optional<Person> findByEmail(String email);
    List<Person> findByFirstname(String firstname);
    List<Person> findByLastname(String lastname);
    List<Person> findByFirstnameAndLastname(String firstname, String lastname);
    List<Person> findByFirstnameOrLastname(String firstname, String lastname);
    List<Person> findByFirstnameIgnoreCase(String firstname);
    List<Person> findByLastnameIgnoreCase(String lastname);
    Optional<Person> findByEmailIgnoreCase(String email);
    List<Person> findByFirstnameContaining(String keyword);
    List<Person> findByLastnameContaining(String keyword);

    // Sorting
    List<Person> findByOrderByFirstnameAsc();
    List<Person> findByOrderByFirstnameDesc();
    List<Person> findByOrderByLastnameAsc();
    List<Person> findByOrderByLastnameDesc();
    List<Person> findByOrderByEmailAsc();
    List<Person> findByOrderByEmailDesc();

    // Custom Queries
    @Query("SELECT p FROM Person p WHERE p.email LIKE %:domain%")
    List<Person> findByEmailDomain(@Param("domain") String domain);

    @Query("SELECT p FROM Person p WHERE LOWER(p.firstname) = LOWER(:name) OR LOWER(p.lastname) = LOWER(:name)")
    List<Person> findByNameIgnoreCase(@Param("name") String name);


    @Query("SELECT p FROM Person p ORDER BY p.firstname ASC")
    List<Person> findAllSortedByFirstname();

    @Query("SELECT p FROM Person p ORDER BY p.lastname ASC")
    List<Person> findAllSortedByLastname();

    @Query("SELECT p FROM Person p WHERE p.firstname LIKE %:name% OR p.lastname LIKE %:name%")
    List<Person> searchByName(@Param("name") String name);

    @Query("SELECT p FROM Person p WHERE p.email LIKE CONCAT('%', :email, '%')")
    List<Person> searchByEmail(@Param("email") String email);

    @Query("SELECT p FROM Person p WHERE p.email LIKE :domain%")
    List<Person> findByEmailStartingWith(@Param("domain") String domain);

    // Deletion
    void deleteByEmail(String email);
    void deleteByFirstname(String firstname);
    void deleteByLastname(String lastname);

    // Exists Checks
    boolean existsByEmail(String email);
    boolean existsByFirstname(String firstname);
    boolean existsByLastname(String lastname);

    // First / Top Queries
    Optional<Person> findFirstByOrderByFirstnameAsc();
    Optional<Person> findFirstByOrderByLastnameAsc();
    Optional<Person> findTopByOrderByFirstnameDesc();
    Optional<Person> findTopByOrderByLastnameDesc();

    // Limited Results
    List<Person> findTop3ByOrderByFirstnameAsc();
    List<Person> findTop5ByOrderByLastnameAsc();
    List<Person> findFirst10ByOrderByFirstnameAsc();
    List<Person> findTop10ByOrderByLastnameDesc();

    // Pagination Support
    List<Person> findByFirstname(String firstname, Pageable pageable);
    List<Person> findByLastname(String lastname, Pageable pageable);
    List<Person> findByEmailContaining(String email, Pageable pageable);

    // Count Queries
    long countByEmail(String email);
    long countByFirstname(String firstname);
    long countByLastname(String lastname);
    long countByFirstnameContaining(String keyword);
    long countByLastnameContaining(String keyword);
    long countByEmailContaining(String keyword);

    // Distinct Queries
    List<Person> findDistinctByFirstname(String firstname);
    List<Person> findDistinctByLastname(String lastname);
    List<Person> findDistinctByEmail(String email);
    List<Person> findDistinctByFirstnameIgnoreCase(String firstname);
    List<Person> findDistinctByLastnameIgnoreCase(String lastname);

    // Boolean Queries
    List<Person> findByFirstnameNot(String firstname);
    List<Person> findByLastnameNot(String lastname);
    List<Person> findByEmailNot(String email);
    List<Person> findByFirstnameNotLike(String pattern);
    List<Person> findByLastnameNotLike(String pattern);

    // Case Insensitive Searches
    List<Person> findByFirstnameIgnoreCaseAndLastnameIgnoreCase(String firstname, String lastname);

    List<Person> findByFirstnameStartingWithIgnoreCase(String prefix);
    List<Person> findByLastnameEndingWithIgnoreCase(String suffix);

    // Complex Filtering
    List<Person> findByFirstnameContainingAndLastnameContaining(String firstname, String lastname);
    List<Person> findByFirstnameStartingWithAndLastnameEndingWith(String firstname, String lastname);
    List<Person> findByFirstnameNotContaining(String keyword);
    List<Person> findByLastnameNotContaining(String keyword);
    List<Person> findByEmailContainingAndFirstnameContaining(String email, String firstname);
    List<Person> findByEmailEndingWithAndLastnameStartingWith(String email, String lastname);

    // Named Queries
    @Query("SELECT p FROM Person p WHERE p.firstname = :name OR p.lastname = :name")
    List<Person> findByFirstnameOrLastnameQuery(@Param("name") String name);

    @Query("SELECT p FROM Person p WHERE LOWER(p.email) = LOWER(:email)")
    Optional<Person> findByEmailIgnoreCaseQuery(@Param("email") String email);

    @Query("SELECT p FROM Person p WHERE p.email LIKE :emailPattern%")
    List<Person> findByEmailStartingWithQuery(@Param("emailPattern") String emailPattern);

    @Query("SELECT p FROM Person p WHERE p.firstname LIKE :prefix%")
    List<Person> findByFirstnameStartingWithQuery(@Param("prefix") String prefix);

    // Fetching Specific Fields
    @Query("SELECT p.firstname FROM Person p WHERE p.email = :email")
    String findFirstnameByEmail(@Param("email") String email);

    @Query("SELECT p.lastname FROM Person p WHERE p.email = :email")
    String findLastnameByEmail(@Param("email") String email);

    @Query("SELECT p.email FROM Person p WHERE p.firstname = :firstname AND p.lastname = :lastname")
    String findEmailByFullname(@Param("firstname") String firstname, @Param("lastname") String lastname);
}
