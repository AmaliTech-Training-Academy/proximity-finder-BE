package team.proximity.helpandsupport.FAQ;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record FAQRequest(
        @NotNull(message = "Question cannot be null")
        @NotBlank(message = "Question cannot be blank")
        @Size(min = 10, max = 500, message = "Question must be between 10 and 500 characters")
        String question,

        @NotNull(message = "Answer cannot be null")
        @NotBlank(message = "Answer cannot be blank")
        @Size(min = 10, max = 1000, message = "Answer must be between 10 and 1000 characters")
        String answer,

        @NotNull(message = "Group ID cannot be null")
        @Positive(message = "Group ID must be a positive number")
        Long groupId
) {}
