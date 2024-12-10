package team.proximity.helpandsupport.FAQ;

import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Join;
import team.proximity.helpandsupport.FAQGROUP.FAQGroup;

import java.util.List;

public class FAQSpecifications {

     public static Specification<FAQ> belongsToGroupNames(List<String> groupNames) {
        return (root, query, criteriaBuilder) -> {
            Join<FAQ, FAQGroup> groupJoin = root.join("group");
            return groupJoin.get("name").in(groupNames);
        };
    }
}
