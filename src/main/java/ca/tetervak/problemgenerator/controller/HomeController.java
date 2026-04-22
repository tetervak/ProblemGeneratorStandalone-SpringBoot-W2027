package ca.tetervak.problemgenerator.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
public class HomeController {

    HomeController() {
        log.trace("Home controller created");
    }

    @GetMapping({"/", "/index"})
    public String index() {
        log.trace("Home index page requested");
        return "home/home-index";
    }

    @GetMapping("/about")
    public String about() {
        log.trace("Home about page requested");
        return "home/home-about";
    }

}
