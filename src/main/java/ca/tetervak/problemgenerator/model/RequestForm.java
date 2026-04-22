package ca.tetervak.problemgenerator.model;

import ca.tetervak.problemgenerator.model.validation.ValidCategory;
import ca.tetervak.problemgenerator.model.validation.ValidLevel;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@ToString
public class RequestForm {

    @Min(value = 1, message = "{number-min-message}")
    @Max(value = 10, message = "{number-max-message}")
    private int number = 5;

    @ValidCategory(message = "{category.unknown}")
    private String category = "unknown";

    @ValidLevel(message = "{level.unknown}")
    private String level = "unknown";
}
