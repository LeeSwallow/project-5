package com.example.testAi.subject;

import com.example.testAi.user.global.rp.Rq;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import java.util.ArrayList;
import java.util.List;

@Component
@Getter
@Setter
@SessionScope
public class SubjectFormScope {
    private List<SubjectForm> subjectForms = new ArrayList<>();
    private Long requestedId = -1L;
    private Enum<ControllerStep> prevStep = ControllerStep.NONE;

    public void clear() {
        subjectForms.clear();
        requestedId = -1L;
    }
}