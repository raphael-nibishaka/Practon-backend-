package com.unbeaten.Practon.repositories;

import com.unbeaten.Practon.models.Certification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Repository
public interface CertificationRepository extends JpaRepository<Certification, Integer> {
    List<Certification> findByOwnerId(Integer ownerId);

    @Query("SELECT c FROM Certification c WHERE c.skill.id = :skillId")
    List<Certification> findBySkillId(@Param("skillId") Integer skillId);
}
