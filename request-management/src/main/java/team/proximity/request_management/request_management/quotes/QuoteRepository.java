package team.proximity.request_management.request_management.quotes;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface QuoteRepository extends JpaRepository<Quote, Long> {

    boolean existsByTitleAndAssignedProvider(String title,String assignedProvider);

    boolean existsByCreatedByAndTitleAndAssignedProvider(String createdBy, String description, String assignedProvider);

    Optional<Quote> findByQuoteIdAndAssignedProvider(Long id, String assignedTo);
    Optional<Quote> findByQuoteIdAndCreatedBy(Long quoteId, String createdBy);

    @Query("SELECT q FROM Quote q LEFT JOIN FETCH q.images LEFT JOIN FETCH q.decision WHERE q.createdBy = :createdBy")
    List<Quote> findByCreatedByWithDetails(@Param("createdBy") String createdBy);

    @Query("SELECT q FROM Quote q LEFT JOIN FETCH q.images LEFT JOIN FETCH q.decision WHERE q.assignedProvider = :assignedTo")
    List<Quote> findByAssignedToWithDetails(@Param("assignedTo") String assignedTo);
}
