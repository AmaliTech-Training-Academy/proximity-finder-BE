package team.proximity.management.mappers;

import team.proximity.management.dtos.DocumentDTO;
import team.proximity.management.model.Document;

import java.util.List;
import java.util.stream.Collectors;

public class DocumentMapper {
    public static DocumentDTO toDto(Document document) {
        return DocumentDTO.builder()
                .id(document.getId())
                .name(document.getUrl())
                .url(document.getUrl())
                .build();
    }

    public static List<DocumentDTO> toDtoList(List<Document> documents) {
        return documents.stream()
                .map(DocumentMapper::toDto)
                .collect(Collectors.toList());
    }
}

