package com.unbeaten.Practon.repositories;

import com.unbeaten.Practon.models.Skill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Repository
public interface SkillRepository extends JpaRepository<Skill, Integer> {
    // Custom query to find skills by name
    List<Skill> findByName(String name);

    // Example of a custom query using JPQL
    @Query("SELECT s FROM Skill s WHERE s.description LIKE %:keyword%")
    List<Skill> findByDescriptionContaining(@Param("keyword") String keyword);
}
