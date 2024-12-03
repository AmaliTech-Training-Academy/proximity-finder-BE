package team.proximity.helpandsupport.contactsupport;

public record ContactSupportRequest(

        String email,
        String subject,
        String message

) {
}
