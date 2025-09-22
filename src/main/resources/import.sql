-- This file allow to write SQL commands that will be emitted in test and dev.
-- The commands are commented as their support depends of the database
-- insert into myentity (id, field) values(1, 'field-1');
-- insert into myentity (id, field) values(2, 'field-2');
-- insert into myentity (id, field) values(3, 'field-3');
-- alter sequence myentity_seq restart with 4;

insert into book (id, titulo, autor, editora, anoLancamento, estaDisponivel) values (1, 'Dom Casmurro', 'Machado de Assis', 'Editora Record', 1899, true);
insert into book (id, titulo, autor, editora, anoLancamento, estaDisponivel) values (2, 'O Alquimista', 'Paulo Coelho', 'Editora Rocco', 1988, true);
insert into book (id, titulo, autor, editora, anoLancamento, estaDisponivel) values (3, 'Capitães da Areia', 'Jorge Amado', 'Companhia das Letras', 1937, false);
insert into book (id, titulo, autor, editora, anoLancamento, estaDisponivel) values (4, 'Grande Sertão: Veredas', 'Guimarães Rosa', 'Nova Fronteira', 1956, true);

alter sequence book_seq restart with 5;

insert into professor (id, nome, email) values (1, 'Alpha', 'alpha@gmail.com');
insert into professor (id, nome, email) values (2, 'Beta', 'beta@gmail.com');
insert into professor (id, nome, email) values (3, 'Charlie', 'charlie@gmail.com');
insert into professor (id, nome, email) values (4, 'Delta', 'delta@gmail.com');
insert into professor (id, nome, email) values (5, 'Echo', 'echo@gmail.com');

alter sequence professor_seq restart with 6;

insert into aluno (id, nome, email, sexo) values (1, 'Ana Clara', 'ana.clara@gmail.com', 'FEMININO');
insert into aluno (id, nome, email, sexo) values (2, 'Bruno Silva', 'bruno.silva@gmail.com', 'MASCULINO');
insert into aluno (id, nome, email, sexo) values (3, 'Carla Mendes', 'carla.mendes@gmail.com', 'FEMININO');
insert into aluno (id, nome, email, sexo) values (4, 'Diego Rocha', 'diego.rocha@gmail.com', 'MASCULINO');
insert into aluno (id, nome, email, sexo) values (5, 'Eduarda Lima', 'eduarda.lima@gmail.com', 'FEMININO');

alter sequence aluno_seq restart with 6;

insert into disciplina (id, nome, descricao, cargaHoraria, professor_id)
values (1, 'Matemática', 'Estudo dos números, álgebra e geometria.', 60, 1);
insert into disciplina (id, nome, descricao, cargaHoraria, professor_id)
values (2, 'Física', 'Princípios fundamentais da natureza e do universo.', 60, 2);
insert into disciplina (id, nome, descricao, cargaHoraria, professor_id)
values (3, 'Química', 'Estudo das substâncias, reações e propriedades da matéria.', 60, 3);
insert into disciplina (id, nome, descricao, cargaHoraria, professor_id)
values (4, 'História', 'Análise dos eventos e processos históricos.', 40, 4);
insert into disciplina (id, nome, descricao, cargaHoraria, professor_id)
values (5, 'Literatura', 'Leitura e interpretação de obras literárias.', 40, 5);

alter sequence disciplina_seq restart with 6;

insert into curso (id, nome, descricao, cargaHoraria, coordenador_id)
values (1, 'Ciência da Computação', 'Curso voltado para algoritmos, programação e sistemas.', 3200, 1);
insert into curso (id, nome, descricao, cargaHoraria, coordenador_id)
values (2, 'Engenharia de Software', 'Foco no desenvolvimento, modelagem e manutenção de sistemas.', 3600, 2);
insert into curso (id, nome, descricao, cargaHoraria, coordenador_id)
values (3, 'Sistemas de Informação', 'Voltado à gestão da informação e desenvolvimento de sistemas.', 3200, 3);
insert into curso (id, nome, descricao, cargaHoraria, coordenador_id)
values (4, 'Análise e Desenvolvimento de Sistemas', 'Curso prático voltado ao desenvolvimento de software.', 2800, 4);
insert into curso (id, nome, descricao, cargaHoraria, coordenador_id)
values (5, 'Redes de Computadores', 'Curso voltado à infraestrutura e comunicação de dados.', 3000, 5);

alter sequence curso_seq restart with 6;

-- Ana → cursos 1 e 2
insert into aluno_curso (aluno_id, curso_id) values (1, 1);
insert into aluno_curso (aluno_id, curso_id) values (1, 2);

-- Bruno → cursos 2 e 3
insert into aluno_curso (aluno_id, curso_id) values (2, 2);
insert into aluno_curso (aluno_id, curso_id) values (2, 3);

-- Carla → cursos 3 e 4
insert into aluno_curso (aluno_id, curso_id) values (3, 3);
insert into aluno_curso (aluno_id, curso_id) values (3, 4);

-- Diego → cursos 4 e 5
insert into aluno_curso (aluno_id, curso_id) values (4, 4);
insert into aluno_curso (aluno_id, curso_id) values (4, 5);

-- Eduarda → cursos 1 e 5
insert into aluno_curso (aluno_id, curso_id) values (5, 1);
insert into aluno_curso (aluno_id, curso_id) values (5, 5);

