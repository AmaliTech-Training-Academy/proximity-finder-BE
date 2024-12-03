package team.proximity.helpandsupport.FAQ;

public record FAQResponse(
         Long id,
         String question,
         String answer,
         String groupName,
         Long groupId
) {
}
