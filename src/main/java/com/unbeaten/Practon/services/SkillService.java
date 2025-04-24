package com.unbeaten.Practon.services;

import com.unbeaten.Practon.models.Skill;
import com.unbeaten.Practon.repositories.SkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SkillService {

    @Autowired
    private SkillRepository skillRepository;

    public void addSkill(Skill skill) {
        skillRepository.save(skill);
    }

    public List<Skill> findAllSkills() {
        return skillRepository.findAll();
    }

    public Skill findSkillById(Integer id) {
        return skillRepository.findById(id).orElse(null);
    }

    public List<Skill> findSkillsByOwnerId(Integer ownerId) {
        return skillRepository.findByOwnerId(ownerId);
    }

    public List<Skill> findSkillsByName(String name) {
        return skillRepository.findByName(name);
    }

    public List<Skill> findSkillsByDescriptionContaining(String keyword) {
        return skillRepository.findByDescriptionContaining(keyword);
    }

    public void updateSkill(Skill skill) {
        skillRepository.save(skill);
    }

    public void deleteSkill(Integer id) {
        skillRepository.deleteById(id);
    }
}
