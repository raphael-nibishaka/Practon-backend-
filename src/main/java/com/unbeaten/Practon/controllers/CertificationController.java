package com.unbeaten.Practon.controllers;

import com.unbeaten.Practon.models.Certification;
import com.unbeaten.Practon.models.Person;
import com.unbeaten.Practon.models.Skill;
import com.unbeaten.Practon.services.CertificationService;
import com.unbeaten.Practon.services.PersonService;
import com.unbeaten.Practon.services.SkillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/certifications")
public class CertificationController {

    @Autowired
    private CertificationService certificationService;

    @Autowired
    private PersonService personService;

    @Autowired
    private SkillService skillService;

    // Get all certifications
    @GetMapping
    public ResponseEntity<List<Certification>> getAllCertifications() {
        return ResponseEntity.ok(certificationService.findAllCertifications());
    }

    // Get certification by ID
    @GetMapping("/{id}")
    public ResponseEntity<Certification> getCertificationById(@PathVariable Integer id) {
        Certification certification = certificationService.findCertificationById(id);
        if (certification != null) {
            return ResponseEntity.ok(certification);
        }
        return ResponseEntity.notFound().build();
    }

    // Get certifications by owner ID
    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<List<Certification>> getCertificationsByOwnerId(@PathVariable Integer ownerId) {
        return ResponseEntity.ok(certificationService.findCertificationsByOwnerId(ownerId));
    }

    // Get certifications by skill ID
    @GetMapping("/skill/{skillId}")
    public ResponseEntity<List<Certification>> getCertificationsBySkillId(@PathVariable Integer skillId) {
        return ResponseEntity.ok(certificationService.findCertificationsBySkillId(skillId));
    }

    // Create new certification
    @PostMapping
    public ResponseEntity<?> createCertification(@RequestBody Map<String, Object> certificationData) {
        try {
            // Validate required fields
            if (!certificationData.containsKey("name") || !certificationData.containsKey("issuingOrganization")
                    || !certificationData.containsKey("dateIssued") || !certificationData.containsKey("owner")
                    || !certificationData.containsKey("skill")) {
                return ResponseEntity.badRequest().body("Missing required fields");
            }

            Certification certification = new Certification();
            certification.setName((String) certificationData.get("name"));
            certification.setIssuingOrganization((String) certificationData.get("issuingOrganization"));
            certification.setDateIssued((String) certificationData.get("dateIssued"));

            // Handle owner data
            if (certificationData.get("owner") instanceof Map) {
                Map<String, Object> ownerData = (Map<String, Object>) certificationData.get("owner");
                if (!ownerData.containsKey("id")) {
                    return ResponseEntity.badRequest().body("Owner ID is required");
                }
                Integer ownerId = (Integer) ownerData.get("id");
                Person owner = personService.findPersonById(ownerId);
                if (owner == null) {
                    return ResponseEntity.badRequest().body("Owner not found with ID: " + ownerId);
                }
                certification.setOwner(owner);
            } else {
                return ResponseEntity.badRequest().body("Invalid owner data format");
            }

            // Handle skill data
            if (certificationData.get("skill") instanceof Map) {
                Map<String, Object> skillData = (Map<String, Object>) certificationData.get("skill");
                if (!skillData.containsKey("id")) {
                    return ResponseEntity.badRequest().body("Skill ID is required");
                }
                Integer skillId = (Integer) skillData.get("id");
                Skill skill = skillService.findSkillById(skillId);
                if (skill == null) {
                    return ResponseEntity.badRequest().body("Skill not found with ID: " + skillId);
                }
                certification.setSkill(skill);
            } else {
                return ResponseEntity.badRequest().body("Invalid skill data format");
            }

            certificationService.addCertification(certification);
            return ResponseEntity.ok(certification);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error creating certification: " + e.getMessage());
        }
    }

    // Update certification
    @PutMapping("/{id}")
    public ResponseEntity<Certification> updateCertification(@PathVariable Integer id,
            @RequestBody Certification certification) {
        Certification existingCertification = certificationService.findCertificationById(id);
        if (existingCertification != null) {
            certification.setId(id);
            certificationService.updateCertification(certification);
            return ResponseEntity.ok(certification);
        }
        return ResponseEntity.notFound().build();
    }

    // Delete certification
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCertification(@PathVariable Integer id) {
        Certification certification = certificationService.findCertificationById(id);
        if (certification != null) {
            certificationService.deleteCertification(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
