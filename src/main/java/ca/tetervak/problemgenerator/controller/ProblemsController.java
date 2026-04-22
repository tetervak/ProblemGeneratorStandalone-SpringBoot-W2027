package ca.tetervak.problemgenerator.controller;

import ca.tetervak.problemgenerator.domain.*;
import ca.tetervak.problemgenerator.model.CompareForm;
import ca.tetervak.problemgenerator.model.RequestForm;
import ca.tetervak.problemgenerator.repository.AlgebraProblemRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/problems")
@Slf4j
public class ProblemsController {

    private final AlgebraProblemRepository problemRepository;

    ProblemsController(
            @NonNull AlgebraProblemRepository problemRepository
    ) {
        this.problemRepository = problemRepository;
        log.trace("Problems controller created");
    }

    @GetMapping({"", "/"})
    public String problemsIndex() {
        log.trace("Problems index page requested");
        return "problems/problems-index";
    }

    @GetMapping("/counts")
    public ModelAndView problemsCounts() {
        log.trace("Problems counts page requested");
        ModelAndView mav = new ModelAndView("problems/problems-counts");
        CountsByCategoriesAndLevels counts = problemRepository.getAlgebraProblemCounts();
        mav.addObject("counts", counts);
        return mav;
    }

    @GetMapping("/form")
    public String problemRequestForm(
            @ModelAttribute RequestForm requestForm,
            Model model
    ) {
        log.trace("Problems form page requested");
        model.addAttribute("requestForm", requestForm);
        return "problems/form/form-index";
    }

    @GetMapping("/form/confirm")
    public String problemRequestFormConfirm(
            @Valid @ModelAttribute RequestForm requestForm,
            BindingResult bindingResult,
            Model model
    ) {
        log.trace("Problems form confirm page requested");
        log.debug("Request form: {}", requestForm);
        if (bindingResult.hasErrors()) {
            log.trace("Form validation failed");
            log.debug("Errors: {}", bindingResult.getAllErrors());
            return "problems/form/form-index";
        } else {
            model.addAttribute("requestForm", requestForm);
            return "problems/form/form-confirm";
        }
    }

    @GetMapping("/categories")
    public String problemsCategories() {
        log.trace("Problems categories page requested");
        return "problems/categories/categories-index";
    }

    @GetMapping("/categories/{category}")
    public ModelAndView problemsSpecificCategory(
            @PathVariable String category
    ) {
        log.trace("Problems specific category page requested");
        log.debug("Category: {}", category);
        ModelAndView mav = new ModelAndView("problems/categories/specific-category");
        mav.addObject("category", category);
        CountsByLevels countsByLevels = problemRepository.getAlgebraProblemCountsByCategory(
                AlgebraProblemCategory.fromString(category)
        );
        mav.addObject("countsByLevels", countsByLevels);
        return mav;
    }

    @GetMapping("/generator")
    public ModelAndView problemsGenerator(
            @RequestParam(defaultValue = "addition") String category,
            @RequestParam(defaultValue = "beginner") String level,
            @RequestParam(defaultValue = "5") int number
    ) {
        log.trace("Problems generator page requested");
        log.debug("Category: {}, Level: {}, Number: {}", category, level, number);
        if (number < 1 || number > 10) {
            throw new IllegalArgumentException("Number must be between 1 and 10");
        }
        ModelAndView mav = new ModelAndView("problems/generator/generated-problems");
        mav.addObject("category", category);
        mav.addObject("level", level);
        List<AlgebraProblem> list = problemRepository
                .getRandomAlgebraProblemList(
                        AlgebraProblemCategory.fromString(category),
                        DifficultyLevel.fromString(level),
                        number
                );

        mav.addObject("problems", list);
        return mav;
    }

    @GetMapping("/categories/{category}/compare-levels")
    public String problemsCompareLevelsInput(
            @PathVariable String category,
            @CookieValue(name = "compare-number", required = false, defaultValue = "5") Integer number,
            @ModelAttribute CompareForm compareForm,
            Model model,
            HttpServletResponse response
    ) {
        log.trace("Problems compare levels page requested");
        log.debug("Category: {}, Number: {}", category, number);
        log.debug("Compare form: {}", compareForm);
        if (compareForm.getNumber() >= 1 && compareForm.getNumber() <= 10) {
            if (compareForm.getNumber() != number) {
                Cookie cookie = new Cookie(
                        "compare-number", String.valueOf(compareForm.getNumber()));
                cookie.setMaxAge(60 * 60 * 24);
                cookie.setPath("/problems/categories/");
                cookie.setHttpOnly(true);
                cookie.setAttribute("SameSite", "Strict");
                response.addCookie(cookie);
                log.debug("Cookie set: compare-number: {}", cookie.getValue());
            }
        } else {
            if(compareForm.getNumber() == 0) {
                if (number >= 1 && number <= 10) {
                    compareForm.setNumber(number);
                } else {
                    compareForm.setNumber(5);
                }
            } else {
                log.warn("Number is not between 1 and 10");
                throw new IllegalArgumentException("Number must be between 1 and 10");
            }
        }

        model.addAttribute("compareForm", compareForm);
        List<AlgebraProblem> leftList = problemRepository.getRandomAlgebraProblemList(
                AlgebraProblemCategory.fromString(category),
                DifficultyLevel.fromString(compareForm.getLeft()),
                compareForm.getNumber()
        );
        List<AlgebraProblem> rightList = problemRepository.getRandomAlgebraProblemList(
                AlgebraProblemCategory.fromString(category),
                DifficultyLevel.fromString(compareForm.getRight()),
                compareForm.getNumber()
        );
        model.addAttribute("category", category);
        model.addAttribute("leftList", leftList);
        model.addAttribute("rightList", rightList);
        return "problems/categories/compare-levels";
    }

    @ModelAttribute("categories")
    public String[] getCategories() {
        log.trace("Getting categories");
        return AlgebraProblemCategory.getStringArray();
    }

    @ModelAttribute("levels")
    public String[] getLevels() {
        log.trace("Getting levels");
        return DifficultyLevel.getStringArray();
    }
}
