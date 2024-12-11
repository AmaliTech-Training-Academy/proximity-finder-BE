package auth.proximity.authservice.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class PaginatedResponse<T> {
    private List<T> data;
    private PaginationMetadata pagination;

    public PaginatedResponse(List<T> data, PaginationMetadata pagination) {
        this.data = data;
        this.pagination = pagination;
    }

    @Data
    @NoArgsConstructor
    public static class PaginationMetadata {
        private int currentPage;
        private int totalPages;
        private long totalElements;
        private int pageSize;

        public PaginationMetadata(int currentPage, int totalPages, long totalElements, int pageSize) {
            this.currentPage = currentPage;
            this.totalPages = totalPages;
            this.totalElements = totalElements;
            this.pageSize = pageSize;
        }
    }
}
