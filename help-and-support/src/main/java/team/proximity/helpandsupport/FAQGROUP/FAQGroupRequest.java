package team.proximity.helpandsupport.FAQGROUP;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record FAQGroupRequest(
        @NotNull(message = "FAQ group name cannot be null")
        @NotBlank(message = "FAQ group name cannot be blank")
        @Size(min = 1, max = 100, message = "FAQ group name must be between 1 and 100 characters")
        String name
) {}