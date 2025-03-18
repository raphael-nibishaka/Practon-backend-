package com.unbeaten.Practon.repositories;

import com.unbeaten.Practon.models.Certification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CertificationRepository extends JpaRepository<Certification, Integer> {
    List<Certification> findByOwnerId(Integer ownerId);
}
