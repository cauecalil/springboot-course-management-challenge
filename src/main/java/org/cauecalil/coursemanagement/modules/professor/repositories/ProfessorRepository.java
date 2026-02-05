package org.cauecalil.coursemanagement.modules.professor.repositories;

import org.cauecalil.coursemanagement.modules.professor.entities.ProfessorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ProfessorRepository extends JpaRepository<ProfessorEntity, UUID> {
    boolean existsByEmail(String email);
    Optional<ProfessorEntity> findByEmail(String email);
}
