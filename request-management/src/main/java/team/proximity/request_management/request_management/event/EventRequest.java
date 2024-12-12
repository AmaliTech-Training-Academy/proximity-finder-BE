package team.proximity.request_management.request_management.event;

public record EventRequest(

         String title,
         String startDate,
         String endDate,
         String startTime,
         String endTime,
         String description

) {}
