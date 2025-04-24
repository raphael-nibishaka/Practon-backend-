package com.unbeaten.Practon.controllers;

import com.unbeaten.Practon.models.Skill;
import com.unbeaten.Practon.models.Person;
import com.unbeaten.Practon.services.SkillService;
import com.unbeaten.Practon.services.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:8098")
@RequestMapping("/skills")
public class SkillController {

    @Autowired
    private SkillService skillService;

    @Autowired
    private PersonService personService;

    // Get all skills
    @GetMapping
    public ResponseEntity<List<Skill>> getAllSkills() {
        return ResponseEntity.ok(skillService.findAllSkills());
    }

    // Get skill by ID
    @GetMapping("/{id}")
    public ResponseEntity<Skill> getSkillById(@PathVariable Integer id) {
        Skill skill = skillService.findSkillById(id);
        if (skill != null) {
            return ResponseEntity.ok(skill);
        }
        return ResponseEntity.notFound().build();
    }

    // Get skills by owner ID
    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<List<Skill>> getSkillsByOwnerId(@PathVariable Integer ownerId) {
        return ResponseEntity.ok(skillService.findSkillsByOwnerId(ownerId));
    }

    // Get skills by name
    @GetMapping("/name/{name}")
    public ResponseEntity<List<Skill>> getSkillsByName(@PathVariable String name) {
        return ResponseEntity.ok(skillService.findSkillsByName(name));
    }

    // Search skills by description
    @GetMapping("/search")
    public ResponseEntity<List<Skill>> searchSkillsByDescription(@RequestParam String keyword) {
        return ResponseEntity.ok(skillService.findSkillsByDescriptionContaining(keyword));
    }

    // Create new skill
    @PostMapping
    public ResponseEntity<Skill> createSkill(@RequestBody Map<String, Object> skillData) {
        try {
            Skill skill = new Skill();
            skill.setName((String) skillData.get("name"));
            skill.setDescription((String) skillData.get("description"));

            // Handle owner data
            if (skillData.containsKey("owner") && skillData.get("owner") instanceof Map) {
                Map<String, Object> ownerData = (Map<String, Object>) skillData.get("owner");
                if (ownerData.containsKey("id")) {
                    Integer ownerId = (Integer) ownerData.get("id");
                    Person owner = personService.findPersonById(ownerId);
                    if (owner != null) {
                        skill.setOwner(owner);
                    } else {
                        return ResponseEntity.badRequest().build();
                    }
                }
            }

            skillService.addSkill(skill);
            return ResponseEntity.ok(skill);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Update skill
    @PutMapping("/{id}")
    public ResponseEntity<Skill> updateSkill(@PathVariable Integer id, @RequestBody Skill skill) {
        Skill existingSkill = skillService.findSkillById(id);
        if (existingSkill != null) {
            skill.setId(id);
            skillService.updateSkill(skill);
            return ResponseEntity.ok(skill);
        }
        return ResponseEntity.notFound().build();
    }

    // Delete skill
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSkill(@PathVariable Integer id) {
        Skill skill = skillService.findSkillById(id);
        if (skill != null) {
            skillService.deleteSkill(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
