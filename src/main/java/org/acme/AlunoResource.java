package org.acme;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Sort;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jdk.javadoc.doclet.Reporter;
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

@Path("/alunos")
public class AlunoResource {

    @Context
    UriInfo uriInfo;

    private AlunoRepresentation rep(Aluno a) {
        return AlunoRepresentation.from(a, uriInfo);
    }

    private List<AlunoRepresentation> repList(List<Aluno> alunos) {
        return alunos.stream().map(this::rep).collect(Collectors.toList());
    }

    @GET
    @Operation(
            summary = "Retorna todos os alunos",
            description = "Retorna uma lista de alunos por padrão no formato JSON"
    )
    @APIResponse(
            responseCode = "200",
            description = "OK",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Aluno.class, type = SchemaType.ARRAY)
            )
    )
    public Response getAll(){
        return Response.ok(repList(Aluno.listAll())).build();
    }

    @GET
    @Path("{id}")
    public Response getById(
            @Parameter(description = "Id do aluno a ser pesquisado", required = true)
            @PathParam("id") long id
    ) {
        Aluno entity = Aluno.findById(id);
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
        Set<String> allowed = Set.of("id", "nome", "email", "sexo");
        if (!allowed.contains(sort)) {
            sort = "id";
        }

        Sort sortObj = Sort.by(
                sort,
                "desc".equalsIgnoreCase(direction) ? Sort.Direction.Descending : Sort.Direction.Ascending
        );

        int effectivePage = page <= 1 ? 0 : page - 1;

        PanacheQuery<Aluno> query = (q == null || q.isBlank())
                ? Aluno.findAll(sortObj)
                : Aluno.find("lower(nome) like ?1 or lower(email) like ?1",
                sortObj,
                "%" + q.toLowerCase() + "%");

        long totalElements = query.count();
        long totalPages = (long) Math.ceil((double) totalElements / size);

        List<Aluno> alunos = query.page(effectivePage, size).list();

        SearchAlunoResponse response = SearchAlunoResponse.from(
                alunos, uriInfo, q, sort, direction, page, size, totalElements, totalPages
        );

        return Response.ok(response).build();
    }

    @POST
    @RequestBody(
            required = true,
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Aluno.class)
            )
    )
    @APIResponse(
            responseCode = "201",
            description = "Created",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Aluno.class)
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
    public Response insert(@Valid Aluno aluno) {
        Aluno.persist(aluno);
        return Response.status(201).entity(rep(aluno)).build();
    }

    @DELETE
    @Transactional
    @Path("{id}")
    public Response delete(
            @PathParam("id") long id
    ) {
        Aluno entity = Aluno.findById(id);
        if (entity == null) {
            return Response.status(404).build();
        }
        Aluno.deleteById(id);
        return Response.noContent().build();
    }

    @PUT
    @Transactional
    @Path("{id}")
    public Response update(
            @PathParam("id") long id,
            @Valid Aluno newAluno
    ) {
        Aluno entity = Aluno.findById(id);
        if (entity == null) {
            return Response.status(404).build();
        }

        entity.nome = newAluno.nome;
        entity.email = newAluno.email;
        entity.sexo = newAluno.sexo;

        return Response.status(200).entity(rep(entity)).build();
    }

    //Matricula a partir de aqui

    @POST
    @Path("{id}/cursos/{cursoId}")
    @Transactional
    public Response matricular(
            @PathParam("id") long alunoId,
            @PathParam(("cursoId")) long cursoId
    ){
        Aluno aluno = Aluno.findById(alunoId);
        if (aluno == null) {
            return Response.status(404).entity("Aluno não encontrado").build();
        }

        Curso curso = Curso.findById(cursoId);
        if (curso == null) {
            return Response.status(404).entity("Curso não encontrado").build();
        }

        if(aluno.cursos.contains(curso)) {
            return Response.status(409).entity("Aluno já está matriculado neste curso").build();
        }

        aluno.cursos.add(curso);
        return Response.status(200).entity(rep(aluno)).build();
    }

    @GET
    @Path("{id}/cursos")
    public Response listarCursos(
            @PathParam("id") long alunoId
    ){
        Aluno aluno = Aluno.findById(alunoId);
        if (aluno == null) {
            return Response.status(404).build();
        }

        return Response.ok(rep(aluno)).build();
    }

    @DELETE
    @Path("{id}/cursos/{cursoId}")
    @Transactional
    public Response desmatricular(
            @PathParam("id") long alunoId,
            @PathParam("cursoId") long cursoId) {

        Aluno aluno = Aluno.findById(alunoId);
        if (aluno == null) {
            return Response.status(404).entity("Aluno não encontrado").build();
        }

        Curso curso = Curso.findById(cursoId);
        if (curso == null) {
            return Response.status(404).entity("Curso não encontrado").build();
        }

        if (!aluno.cursos.contains(curso)) {
            return Response.status(400).entity("Aluno não está matriculado neste curso").build();
        }

        aluno.cursos.remove(curso);
        return Response.noContent().build();
    }
}
