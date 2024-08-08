package com.example.testAi.subject;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {
    List<Subject> findAllByMemberId(Sort sort, Long memberId);
    List<Subject> findAll(Sort sort);
    void deleteById(Long id);
    boolean existsById(Long id);
}