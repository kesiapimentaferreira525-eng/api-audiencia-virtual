-- 1. Limpa as tabelas respeitando a ordem das chaves estrangeiras
DELETE FROM tb_audienciavirtual;
DELETE FROM tb_agenda;
DELETE FROM tb_parte;
DELETE FROM tb_usuario;

-- 2. Insere cinco usuários
INSERT INTO tb_usuario (id, nome, cpf, funcao) VALUES (1, 'João Silva', '12345678901', 'ADMIN');
INSERT INTO tb_usuario (id, nome, cpf, funcao) VALUES (2, 'Maria Souza', '98765432101', 'SERVIDOR');
INSERT INTO tb_usuario (id, nome, cpf, funcao) VALUES (3, 'Carlos Oliveira', '44455566677', 'PARTE');
INSERT INTO tb_usuario (id, nome, cpf, funcao) VALUES (4, 'Ana Oliveira', '33333333333', 'PARTE');
INSERT INTO tb_usuario (id, nome, cpf, funcao) VALUES (5, 'Pedro Costa', '44444444444', 'PARTE');

-- 3. Insere cinco partes
INSERT INTO tb_parte (id_usuario, numero_processo) VALUES (1, '0000001-00.2026.8.05.0001');
INSERT INTO tb_parte (id_usuario, numero_processo) VALUES (2, '0000002-00.2026.8.05.0001');
INSERT INTO tb_parte (id_usuario, numero_processo) VALUES (3, '0001234-56.2026.8.05.0000');
INSERT INTO tb_parte (id_usuario, numero_processo) VALUES (4, '0000004-00.2026.8.05.0001');
INSERT INTO tb_parte (id_usuario, numero_processo) VALUES (5, '0000005-00.2026.8.05.0001');

-- 4. Insere cinco agendas
INSERT INTO tb_agenda (id, parte_id) VALUES (1, 1);
INSERT INTO tb_agenda (id, parte_id) VALUES (2, 2);
INSERT INTO tb_agenda (id, parte_id) VALUES (3, 3);
INSERT INTO tb_agenda (id, parte_id) VALUES (4, 4);
INSERT INTO tb_agenda (id, parte_id) VALUES (5, 5);

-- 5. Insere a Audiência Virtual
INSERT INTO tb_audienciavirtual
    (id, agenda_id, email, data_audiencia, site_agendamento)
VALUES
    (1, 1, 'joao.silva@exemplo.com',
     '2026-10-12 09:00:00', 'Microsoft Teams');
INSERT INTO tb_audienciavirtual
    (id, agenda_id, email, data_audiencia, site_agendamento)
VALUES
    (2, 2, 'maria.souza@exemplo.com',
     '2026-10-13 14:00:00', 'Microsoft Teams');
INSERT INTO tb_audienciavirtual
    (id, agenda_id, email, data_audiencia, site_agendamento)
VALUES
    (3, 3, 'carlos.oliveira@exemplo.com',
     '2026-10-14 14:00:00', 'Microsoft Teams');
INSERT INTO tb_audienciavirtual
    (id, agenda_id, email, data_audiencia, site_agendamento)
VALUES
    (4, 4, 'ana.oliveira@exemplo.com',
     '2026-10-15 14:00:00', 'Microsoft Teams');
INSERT INTO tb_audienciavirtual
    (id, agenda_id, email, data_audiencia, site_agendamento)
VALUES
    (5, 5, 'pedro.costa@exemplo.com',
     '2026-10-16 14:00:00', 'Microsoft Teams');