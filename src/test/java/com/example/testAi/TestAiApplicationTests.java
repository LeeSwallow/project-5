package com.example.testAi;

import com.example.testAi.subject.Subject;
import com.example.testAi.subject.SubjectRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

@SpringBootTest
class TestAiApplicationTests {

	@Autowired
	SubjectRepository subjectRepository;


	@Test
	void makeOneSubject() {
		Subject subject = new Subject();
		subject.setParent(null);
		subject.setSubject("스프링 부트 개발자 되기");
		subject.setDescription("사용자가 직접 입력한 목표입니다.");
		subject.setCreatedDate(LocalDateTime.now());
		subjectRepository.save(subject);
	}

	@Test
	void deleteAllSubjects() {
		subjectRepository.deleteAll();
	}

}