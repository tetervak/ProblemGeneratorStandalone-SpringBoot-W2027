package ca.tetervak.problemgenerator.config;

import ca.tetervak.problemgenerator.domain.AlgebraProblemFactory;
import ca.tetervak.problemgenerator.domain.AlgebraProblemListFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Random;

@Configuration
@Slf4j
public class ProblemGeneratorConfig {

    @Bean
    Random random() {
        log.trace("Creating random number generator");
        return new Random();
    }

    @Bean
    AlgebraProblemFactory algebraProblemFactory() {
        log.trace("Creating algebra problem factory");
        return new AlgebraProblemFactory();
    }

    @Bean
    AlgebraProblemListFactory algebraProblemListFactory() {
        log.trace("Creating algebra problem list factory");
        return new AlgebraProblemListFactory();
    }
}
