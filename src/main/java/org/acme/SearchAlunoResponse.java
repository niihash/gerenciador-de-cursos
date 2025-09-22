package org.acme;

import jakarta.ws.rs.core.UriInfo;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchAlunoResponse {

    public List<AlunoRepresentation> alunos;
    public PaginationMetadata pagination;
    public Map<String, String> _links;

    public SearchAlunoResponse() {
    }

    public static SearchAlunoResponse from(
            List<Aluno> alunos, UriInfo uriInfo, String query, String sort, String direction, int page, int size,
            long totalElements, long totalPages
    ) {
        SearchAlunoResponse response = new SearchAlunoResponse();

        response.alunos = alunos.stream()
                .map(aluno -> AlunoRepresentation.from(aluno, uriInfo))
                .toList();

        response.pagination = new PaginationMetadata();
        response.pagination.page = page;
        response.pagination.size = size;
        response.pagination.totalElements = totalElements;
        response.pagination.totalPages = totalPages;

        response._links = buildLinks(uriInfo, query, sort, direction, page, size, totalPages);

        return response;
    }

    private static Map<String, String> buildLinks(
            UriInfo uriInfo, String query, String sort,
            String direction, int page, int size, long totalPages
    ) {
        Map<String, String> links = new HashMap<>();
        URI baseUri = uriInfo.getBaseUri();
        String baseUrl = baseUri + "alunos/search";

        StringBuilder params = new StringBuilder();
        if (query != null && !query.isBlank()) {
            params.append("q=").append(query).append("&");
        }
        params.append("sort=").append(sort)
                .append("&direction=").append(direction)
                .append("&size=").append(size);

        // Self link
        links.put("self", baseUrl + "?" + params + "&page=" + page);

        // First page
        links.put("first", baseUrl + "?" + params + "&page=1");

        // Last page
        links.put("last", baseUrl + "?" + params + "&page=" + totalPages);

        // Previous page
        if (page > 1) {
            links.put("prev", baseUrl + "?" + params + "&page=" + (page - 1));
        }

        // Next page
        if (page < totalPages) {
            links.put("next", baseUrl + "?" + params + "&page=" + (page + 1));
        }

        // Other useful links
        links.put("alunos", baseUri + "alunos");

        return links;
    }

    public static class PaginationMetadata {
        public int page;
        public int size;
        public long totalElements;
        public long totalPages;
    }
}
