package com.example.testAi.subject;


import com.example.testAi.chat.ChatService;
import com.example.testAi.user.domain.member.entity.Member;
import com.example.testAi.user.global.rp.Rq;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SubjectService {

    private final SubjectRepository subjectRepository;
    private final ChatService chatService;
    private final Rq request;


    Optional<Subject> get(Long id) {
        return subjectRepository.findById(id);
    }


    List<Subject> getAll() {
        if (request.isLogin()) {
            return subjectRepository.findAllByMemberId(Sort.by(Sort.Order.desc("createdDate")), request.getMember().getId())
                    .stream().filter(s -> !(s.isDone())).toList();
        } else {
            return new ArrayList<>();
        }

    }

    List<Subject> getAllByPriority() {
        if (request.isLogin()) {
            List<Subject> subjects =  subjectRepository.findAllByMemberId(Sort.by(Sort.Order.asc("priority")), request.getMember().getId())
                    .stream().filter(s -> !(s.isDone())).toList();
            int priority = 1;

            for (Subject subject : subjects) {
                subject.setPriority(priority++);
            }
            subjectRepository.saveAll(subjects);
            return subjects;
        } else {
            return new ArrayList<>();
        }
    }


    List<Subject> getFavorite() {
        if (request.isLogin()) {
            return subjectRepository.findAllByMemberId(Sort.by(Sort.Order.desc("createdDate"))
                            , request.getMember().getId()).stream().filter(Subject::isFavorite).filter(s -> !(s.isDone()))
                    .toList();
        } else {
            return new ArrayList<>();
        }
    }


    List<Subject> getRoots() {
        if (request.isLogin()) {
            return subjectRepository.findAllByMemberId(Sort.by(Sort.Order.desc("createdDate"))
                            , request.getMember().getId()).stream().filter(subject -> subject.getParent() == null)
                    .filter(s -> !(s.isDone())).toList();
        } else {
            return new ArrayList<>();
        }
    }


    List<Subject> getDone() {
        if (request.isLogin()) {
            return subjectRepository.findAllByMemberId(Sort.by(Sort.Order.desc("createdDate"))
                    , request.getMember().getId()).stream().filter(Subject::isDone).toList();
        } else {
            return new ArrayList<>();
        }
    }


    void delFavorite(Long id) {
        if (request.isLogin()) {
            Member member = request.getMember();
            Subject subject = get(id).orElse(null);
            if (subject != null) {
                member.getFavorite().remove(subject);
            }
        }
    }


    void addFavorite(Long id) {
        if (request.isLogin()) {
            Member member = request.getMember();
            Subject subject = get(id).orElse(null);
            if (subject != null) {
                member.getFavorite().add(subject);
            }
        }
    }


    // 에러 발생시 빈 리스트 반환
    List<SubjectForm> divide(Long id) {
        //TODO : divide subjects

        Subject subject = get(id).orElse(null);

        if (subject == null || !request.isLogin()) {

            return new ArrayList<>();
        }
        var response = chatService.generate(subject.getSubject());

        Map<String, Object> map = ChatService.jsonConverter(response);
        if (map.get("Error") != null) {
            return new ArrayList<>();
        }

        List<SubjectForm> subjects = new ArrayList<>();

        int no = 1;
        for (Map<String, Object> m : (List<Map<String, Object>>) map.get("subjects")) {
            SubjectForm subjectForm = new SubjectForm();
            subjectForm.setNo(no++);
            subjectForm.setSubject((String) m.getOrDefault("subject", "Nothing"));
            subjectForm.setDescription((String) m.getOrDefault("description", "Nothing"));
            subjectForm.setExpectDate((Integer) m.getOrDefault("expect_date", 0));
            String priority = (String) m.getOrDefault("priority", "0");
            subjectForm.setPriority(Integer.parseInt(priority));
            subjects.add(subjectForm);
        }
        subjects.sort((o1, o2) -> o2.getPriority() - o1.getPriority());
        return subjects;
    }

    void save(Subject parent, List<SubjectForm> subjectForms) {
        //TODO : save subject
        if (parent == null || !request.isLogin()) {
            return;
        }
        Member member = request.getMember();

        int childrenExp = 0;

        for (SubjectForm subjectForm : subjectForms) {
            Subject subject = new Subject();
            subject.setSubject(subjectForm.getSubject());
            subject.setDescription(subjectForm.getDescription());
            subject.setExpiredDate(subjectForm.getExpectDate());
            childrenExp += subjectForm.getExpectDate();

            subject.setPriority(subjectForm.getPriority());
            subject.setCreatedDate(LocalDateTime.now());
            subject.setParent(parent);
            subject.setMember(member);
            subject.setDepth(parent.getDepth() + 1);
            subjectRepository.save(subject);
            parent.getChildren().add(subject);
        }
        int diff = childrenExp - parent.getExpiredDate();
        while (parent != null) {
            parent.setExpiredDate(parent.getExpiredDate() + diff);
            subjectRepository.save(parent);
            parent = parent.getParent();
        }
    }


    // 자식들은 cascade로 삭제되므로 부모만 삭제
    void delete(Long id) {
        //TODO : delete subject
        if (subjectRepository.existsById(id)) {
            Subject target = subjectRepository.findById(id).get();
            Subject parent = target.getParent();
            while (parent != null) {
                parent.setExpiredDate(parent.getExpiredDate() - target.getExpiredDate());
                subjectRepository.save(parent);
                parent = parent.getParent();
            }
            subjectRepository.deleteById(id);
        }
    }

    void add(Long id, String content) {
        if (!request.isLogin()) {
            return;
        }

        Subject subject = new Subject();
        subject.setSubject(content);
        subject.setDescription("사용자가 직접 입력한 내용입니다.");
        subject.setCreatedDate(LocalDateTime.now());
        subject.setExpiredDate(0);
        subject.setDepth(0);
        subject.setMember(request.getMember());

        if (id != null && subjectRepository.existsById(id)) {
            Subject parent = subjectRepository.findById(id).orElse(null);
            subject.setParent(parent);
            parent.getChildren().add(subject);
            subject.setDepth(parent.getDepth() + 1);
            int priority = parent.getChildren().size();
            subject.setPriority(priority);
            subjectRepository.save(parent);
        }

        subjectRepository.save(subject);
    }

    void done(Long id) {
        if (request.isLogin()) {
            Subject subject = get(id).orElse(null);

            if (subject != null && !subject.isDone()) {
                subject.setDone(true);
                subjectRepository.save(subject);
                Subject parent = subject;
                int expDate = subject.getExpiredDate();
                while (parent != null) {
                    parent.setExpiredDate(parent.getExpiredDate() - expDate);
                    subjectRepository.save(parent);
                    parent = parent.getParent();
                }
                if (subject.getParent() != null) {
                    subject.getParent().getChildren().remove(subject);
                    subjectRepository.save(subject.getParent());
                }
                doneChlidren(subject);
            }
        }
    }
    private void doneChlidren(Subject subject) {

        if (subject.getChildren().isEmpty()) {
        subject.setParent(null);
            return;
        }
        for (Subject child : subject.getChildren()) {
            child.setDone(true);
            subjectRepository.save(child);
            doneChlidren(child);
        }
        subject.children.clear();
        subjectRepository.save(subject);
    }


    boolean switchFavorite(Long id) {
        if (request.isLogin()) {
            Subject subject = get(id).orElse(null);
            if (subject != null) {
                subject.setFavorite(!subject.isFavorite());
                subjectRepository.save(subject);
                return subject.isFavorite();
            }
        }
        return false;
    }

    void modify (Long id, Integer expiredDate ,Integer priority) {
        if (request.isLogin()) {
            Subject subject = get(id).orElse(null);
            if (subject != null) {
                subject.setExpiredDate(expiredDate);
                subject.setPriority(priority);
                subjectRepository.save(subject);
            }
        }
    }

    void setParentExpiredDate(Long pid, Integer totalExpiredDate) {
        if (request.isLogin()) {
            Subject parent = get(pid).orElse(null);
            if (parent != null) {
                Integer diff = totalExpiredDate - parent.getExpiredDate();
                while (parent != null) {
                    parent.setExpiredDate(parent.getExpiredDate() + diff);
                    subjectRepository.save(parent);
                    parent = parent.getParent();
                }
            }
        }
    }
}