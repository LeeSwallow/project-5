package com.example.testAi.subject;

import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/subject")
public class SubjectController {
    private final SubjectService subjectService;
    private final SubjectFormScope subjectFormScope;


    @GetMapping("main")
    public String getAllSubjects(Model model,
                                 @RequestParam(value = "taskSort", required = false, defaultValue = "recent") String taskSort) {
        if (!subjectFormScope.getPrevStep().equals(ControllerStep.DIVIDE)) {
            subjectFormScope.clear();
            subjectFormScope.setPrevStep(ControllerStep.START);
        } else {
            subjectFormScope.setPrevStep(ControllerStep.CHECK);
        }

        Long requestedId = subjectFormScope.getRequestedId();
        List<SubjectForm> subjectForms = new ArrayList<>(subjectFormScope.getSubjectForms());
        model.addAttribute("requestedId", requestedId);
        model.addAttribute("subjectForms", subjectForms);

        // taskSort에 따라 다르게 처리
        if (taskSort.equals("recent")) {
            model.addAttribute("subjects", subjectService.getAll());
        } else if (taskSort.equals("favorite")) {
            model.addAttribute("subjects", subjectService.getFavorite());
        } else if (taskSort.equals("tree")) {
            model.addAttribute("subjects", subjectService.getRoots());
        } else {
            model.addAttribute("subjects", subjectService.getDone());
        }

        return "main";
    }

    @PostMapping("main/divide")
    public String divideSubject(@RequestParam("id") Long id, RedirectAttributes model) {
        if (!subjectFormScope.getPrevStep().equals(ControllerStep.START)) {
            subjectFormScope.clear();
            subjectFormScope.setPrevStep(ControllerStep.NONE);
            return "redirect:/subject/main";
        } else {
            subjectFormScope.setPrevStep(ControllerStep.DIVIDE);
        }

        subjectFormScope.clear();
        subjectFormScope.setRequestedId(id);
        subjectFormScope.setSubjectForms(subjectService.divide(id));

        model.addFlashAttribute("requestedId", subjectFormScope.getRequestedId());
        model.addFlashAttribute("subjectForms", subjectFormScope.getSubjectForms());
        return "redirect:/subject/main";
    }

    @PostMapping("main/save")
    public String saveSubjects(@RequestParam("body") String body) {
        if (!subjectFormScope.getPrevStep().equals(ControllerStep.CHECK)) {
            subjectFormScope.clear();
            subjectFormScope.setPrevStep(ControllerStep.NONE);
            return "redirect:/subject/main";
        } else {
            subjectFormScope.setPrevStep(ControllerStep.SAVE);
        }

        JSONObject jsonObject = new JSONObject(body);
        HashSet<Integer> parsedSelect = jsonObject.getJSONArray("subjectNo").toList()
                .stream().map(Objects::toString).map(Integer::parseInt).collect(Collectors.toCollection(HashSet::new));

        List<SubjectForm> selectedForm = subjectFormScope.getSubjectForms().stream()
                .filter(subjectForm -> parsedSelect.contains(subjectForm.getNo()))
                .toList();

        Subject target = subjectService.get(subjectFormScope.getRequestedId()).orElse(null);
        subjectService.save(target, selectedForm);
        subjectFormScope.clear();
        return "redirect:/subject/main";
    }

    @PostMapping("main/add")
    public String addSubject(@RequestParam("taskInput") String taskInput) {
        subjectService.add(null, taskInput);
        return "redirect:/subject/main";
    }

    @PostMapping("main/delete")
    public String deleteSubject(@RequestParam("body") String body) {
        JSONObject jsonObject = new JSONObject(body);
        jsonObject.getJSONArray("tasks").toList().stream()
                .map(Objects::toString).map(Long::parseLong)
                .forEach(subjectService::delete);

        return "redirect:/subject/main";
    }

    @PostMapping("main/done")
    public String doneSubject(@RequestParam("body") String body) {
        JSONObject jsonObject = new JSONObject(body);
        List<Long> ids = jsonObject.getJSONArray("tasks").toList().stream()
                .map(Objects::toString).map(Long::parseLong).toList();

        ids.forEach(subjectService::done);

        return "redirect:/subject/main";
    }


    @GetMapping("/{sid}")
    public String getTargetSubjects(Model model, @PathVariable Long sid) {
        if (!subjectFormScope.getPrevStep().equals(ControllerStep.DIVIDE)) {
            subjectFormScope.clear();
            subjectFormScope.setPrevStep(ControllerStep.START);
        } else {
            subjectFormScope.setPrevStep(ControllerStep.CHECK);
        }

        Long requestedId = subjectFormScope.getRequestedId();
        List<SubjectForm> subjectForms = new ArrayList<>(subjectFormScope.getSubjectForms());
        Subject target = subjectService.get(sid).orElse(null);
        if (target == null) {
            return "redirect:/subject/main";
        }
        List<Subject> subjects = target.getChildren().stream()
                .filter(subject -> !subject.isDone())
                .sorted(Comparator.comparing(Subject::getPriority))
                .collect(Collectors.toList());

        model.addAttribute("requestedId", requestedId);
        model.addAttribute("subjectForms", subjectForms);
        model.addAttribute("target", target);
        model.addAttribute("subjects", subjects);

        return "target";
    }

    @PostMapping("{sid}/divide")
    public String divideTargetSubject(@RequestParam("id") Long id, RedirectAttributes model, @PathVariable Long sid) {
        if (!subjectFormScope.getPrevStep().equals(ControllerStep.START)) {
            subjectFormScope.clear();
            subjectFormScope.setPrevStep(ControllerStep.START);
            return String.format("redirect:/subject/%d", sid);
        } else {
            subjectFormScope.setPrevStep(ControllerStep.DIVIDE);
        }
        subjectFormScope.clear();
        subjectFormScope.setRequestedId(id);
        subjectFormScope.setSubjectForms(subjectService.divide(id));

        model.addFlashAttribute("requestedId", subjectFormScope.getRequestedId());
        model.addFlashAttribute("subjectForms", subjectFormScope.getSubjectForms());
        return String.format("redirect:/subject/%d", sid);
    }

    @PostMapping("{sid}/save")
    public String saveTargetSubjects(@RequestParam("body") String body, @PathVariable Long sid) {
        if (!subjectFormScope.getPrevStep().equals(ControllerStep.CHECK)) {
            subjectFormScope.clear();
            subjectFormScope.setPrevStep(ControllerStep.NONE);
            return String.format("redirect:/subject/%d", sid);
        } else {
            subjectFormScope.setPrevStep(ControllerStep.SAVE);
        }

        JSONObject jsonObject = new JSONObject(body);
        HashSet<Integer> parsedSelect = jsonObject.getJSONArray("subjectNo").toList()
                .stream().map(Objects::toString).map(Integer::parseInt).collect(Collectors.toCollection(HashSet::new));

        List<SubjectForm> selectedForm = subjectFormScope.getSubjectForms().stream()
                .filter(subjectForm -> parsedSelect.contains(subjectForm.getNo()))
                .toList();

        Subject target = subjectService.get(subjectFormScope.getRequestedId()).orElse(null);
        subjectService.save(target, selectedForm);
        subjectFormScope.clear();
        return String.format("redirect:/subject/%d", sid);
    }

    @PostMapping("{sid}/add")
    public String addTargetSubject(@RequestParam("taskInput") String taskInput, @PathVariable Long sid) {
        subjectService.add(sid, taskInput);
        return String.format("redirect:/subject/%d", sid);
    }

//
//    @PostMapping("{sid}/delete")
//    public String deleteTargetSubject(@RequestParam("body") String body, @PathVariable Long sid) {
//        JSONObject jsonObject = new JSONObject(body);
//
//        List<Long> idList = jsonObject.getJSONArray("tasks").toList().stream()
//                .map(Objects::toString).map(Long::parseLong).toList();
//
//        Subject target = subjectService.get(sid).orElse(null);
//        Subject parent = (target == null) ? null : target.getParent();
//
//        idList.forEach(subjectService::delete);
//
//        if (subjectService.get(sid).isPresent()) {
//            if (subjectService.get(sid).get().getChildren().isEmpty())
//                subjectService.delete(sid);
//            else
//                return String.format("redirect:/subject/%d", sid);
//        }
//        if (parent != null) {
//            return String.format("redirect:/subject/%d", parent.getId());
//        }
//        return "redirect:/subject/main";
//    }
//

    @PostMapping("{sid}/done")
    public String doneTargetSubject(@RequestParam("body") String body, @PathVariable Long sid) {
        JSONObject jsonObject = new JSONObject(body);

        List<Long> idList = jsonObject.getJSONArray("tasks").toList().stream()
                .map(Objects::toString).map(Long::parseLong).toList();

        Subject target = subjectService.get(sid).orElse(null);
        Subject parent = (target == null) ? null : target.getParent();

        for (Long id : idList) {
            subjectService.done(id);
        }


        if (target != null) {
            for (Subject child : target.getChildren()) {
                if (!child.isDone()) {
                    return String.format("redirect:/subject/%d", sid);
                }
            }
            subjectService.done(sid);
            if (parent != null) {
                return String.format("redirect:/subject/%d", parent.getId());
            }
        }
        return "redirect:/subject/main";
    }

    @PostMapping("{sid}/modify")
    public String modifyTargetSubject(@RequestParam("body") String body, @PathVariable Long sid) {

        JSONObject jsonObject = new JSONObject(body);
        List<Map<String, Object>> tasks = jsonObject.getJSONArray("tasks").toList().stream()
                .map(task -> (Map<String, Object>) task)
                .collect(Collectors.toList());

        Integer totalExpiredDate = 0;
        for (Map<String, Object> task : tasks) {
            Long listId = Long.parseLong(task.get("listId").toString());
            Integer expiredDate = Integer.parseInt(task.get("expiredDate").toString());
            Integer priority = Integer.parseInt(task.get("priority").toString());
            totalExpiredDate += expiredDate;
            subjectService.modify(listId, expiredDate, priority);
        }
        subjectService.setParentExpiredDate(sid, totalExpiredDate);
        return String.format("redirect:/subject/%d", sid);
    }


    @GetMapping("/main/favorite/{subjectId}")
    @ResponseBody
    public String switchFavorite(@PathVariable("subjectId") Long subjectId) {
        boolean isFavorite = subjectService.switchFavorite(subjectId);
        return Boolean.toString(isFavorite);
    }
}