package org.acme;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Sort;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Path("/professores")
public class ProfessorResource {

    @Context
    UriInfo uriInfo;

    private ProfessorRepresentation rep(Professor p) {
        return ProfessorRepresentation.from(p, uriInfo);
    }

    private List<ProfessorRepresentation> repList(List<Professor> professores) {
        return professores.stream().map(this::rep).collect(Collectors.toList());
    }

    @GET
    @Operation(
            summary = "Retorna todos os professores",
            description = "Retorna uma lista de professores por padrão no formato JSON"
    )
    @APIResponse(
            responseCode = "200",
            description = "OK",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Professor.class, type = SchemaType.ARRAY)
            )
    )
    public Response getAll() {
        return Response.ok(repList(Professor.listAll())).build();
    }

    @GET
    @Path("{id}")
    public Response getById(
            @Parameter(description = "Id do professor a ser pesquisado", required = true)
            @PathParam("id") long id
    ) {
        Professor entity = Professor.findById(id);
        if (entity == null) {
            return Response.status(404).build();
        }
        return Response.ok(rep(entity)).build();
    }

    @GET
    @Path("/search")
    public Response search(
            @Parameter(description = "Query de busca por nome ou email")
            @QueryParam("q") String q,
            @Parameter(description = "Campo de ordenação da lista de retorno")
            @QueryParam("sort") @DefaultValue("id") String sort,
            @Parameter(description = "Direção da ordenação ascendente/descendente")
            @QueryParam("direction") @DefaultValue("asc") String direction,
            @QueryParam("page") @DefaultValue("1") int page,
            @QueryParam("size") @DefaultValue("10") int size
    ) {
        Set<String> allowed = Set.of("id", "nome", "email");
        if (!allowed.contains(sort)) {
            sort = "id";
        }

        Sort sortObj = Sort.by(
                sort,
                "desc".equalsIgnoreCase(direction) ? Sort.Direction.Descending : Sort.Direction.Ascending
        );

        int effectivePage = page <= 1 ? 0 : page - 1;

        PanacheQuery<Professor> query = (q == null || q.isBlank())
                ? Professor.findAll(sortObj)
                : Professor.find("lower(nome) like ?1 or lower(email) like ?1",
                sortObj,
                "%" + q.toLowerCase() + "%");

        long totalElements = query.count();
        long totalPages = (long) Math.ceil((double) totalElements / size);

        List<Professor> professores = query.page(effectivePage, size).list();

        SearchProfessorResponse response = SearchProfessorResponse.from(
                professores, uriInfo, q, sort, direction, page, size, totalElements, totalPages
        );

        return Response.ok(response).build();
    }

    @POST
    @RequestBody(
            required = true,
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Professor.class)
            )
    )
    @APIResponse(
            responseCode = "201",
            description = "Created",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Professor.class)
            )
    )
    @APIResponse(
            responseCode = "400",
            description = "Bad Request",
            content = @Content(
                    mediaType = "text/plain",
                    schema = @Schema(implementation = String.class)
            )
    )
    @Transactional
    public Response insert(@Valid Professor professor) {
        Professor.persist(professor);
        return Response.status(201).entity(rep(professor)).build();
    }

    @DELETE
    @Transactional
    @Path("{id}")
    public Response delete(
            @PathParam("id") long id
    ) {
        Professor entity = Professor.findById(id);
        if (entity == null) {
            return Response.status(404).build();
        }
        Professor.deleteById(id);
        return Response.noContent().build();
    }

    @PUT
    @Transactional
    @Path("{id}")
    public Response update(
            @PathParam("id") long id,
            @Valid Professor newProfessor
    ) {
        Professor entity = Professor.findById(id);
        if (entity == null) {
            return Response.status(404).build();
        }

        entity.nome = newProfessor.nome;
        entity.email = newProfessor.email;

        return Response.status(200).entity(rep(entity)).build();
    }
}












