package com.unbeaten.Practon.services;

import com.unbeaten.Practon.models.Certification;
import com.unbeaten.Practon.repositories.CertificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CertificationService {

    @Autowired
    private CertificationRepository certificationRepository;

    public void addCertification(Certification certification) {
        certificationRepository.save(certification);
    }

    public List<Certification> findAllCertifications() {
        return certificationRepository.findAll();
    }

    public Certification findCertificationById(Integer id) {
        return certificationRepository.findById(id).orElse(null);
    }

    public List<Certification> findCertificationsByOwnerId(Integer ownerId) {
        return certificationRepository.findByOwnerId(ownerId);
    }

    public List<Certification> findCertificationsBySkillId(Integer skillId) {
        return certificationRepository.findBySkillId(skillId);
    }

    public void updateCertification(Certification certification) {
        certificationRepository.save(certification);
    }

    public void deleteCertification(Integer id) {
        certificationRepository.deleteById(id);
    }
}
