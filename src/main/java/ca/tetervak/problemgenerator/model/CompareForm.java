package ca.tetervak.problemgenerator.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@ToString
public class CompareForm {
    private String left = "beginner";
    private String right = "easy";
    private int number = 0;
}
