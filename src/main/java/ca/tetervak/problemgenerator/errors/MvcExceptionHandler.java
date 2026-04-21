package ca.tetervak.problemgenerator.errors;

import ca.tetervak.problemgenerator.domain.AlgebraProblemCategory;
import ca.tetervak.problemgenerator.domain.DifficultyLevel;
import ca.tetervak.problemgenerator.model.RequestForm;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@ControllerAdvice
public class MvcExceptionHandler {

    @ExceptionHandler(NoResourceFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND) // Ensures 404 statuses are set
    public Object handleNoResourceFoundException(NoResourceFoundException ex) {
        String path = ex.getResourcePath();
        ModelAndView mav = new ModelAndView("error/error-404");
        mav.addObject("method", ex.getHttpMethod());
        mav.addObject("path", path);
        return mav;
    }

    @ExceptionHandler(Exception.class)
    public ModelAndView handleException(Exception ex) {
        ModelAndView mav = new ModelAndView("error/general-error");
        mav.addObject("exception", ex.getClass().getSimpleName());
        mav.addObject("message", ex.getMessage());
        return mav;
    }
}
