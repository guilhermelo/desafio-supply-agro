INSERT INTO fazenda (id, nome, cnpj, cidade, estado, logradouro) VALUES ('e38e7b36-f52f-4048-ad9d-fa32e35c20c1', 'Fazenda 1', '27.320.669/0001-17', 'Assis', 'SP', 'Rua 1');
INSERT INTO fazenda (id, nome, cnpj, cidade, estado, logradouro) VALUES ('d1b0e996-7141-4c40-bf23-0c0895a4ffad', 'Fazenda 2', '19.116.649/0001-09', 'Assis', 'SP', 'Rua 1');
INSERT INTO fazenda (id, nome, cnpj, cidade, estado, logradouro) VALUES ('599b2a61-03e4-4738-a9b3-dc02de03ccff', 'Fazenda 3', '23.349.318/0001-88', 'Assis', 'SP', 'Rua 1');

INSERT INTO talhao (id, codigo, area_em_hectare, safra, estimativa_produtividade, fazenda_id) VALUES ('63d81d90-c185-4dc0-844b-3d881157eff9', 'TAL1', 100.00, 2021, 500.00, 'e38e7b36-f52f-4048-ad9d-fa32e35c20c1');
INSERT INTO talhao (id, codigo, area_em_hectare, safra, estimativa_produtividade, fazenda_id) VALUES ('51e375de-3054-499b-8002-ba94db711681', 'TAL1', 50.00, 2021, 250.00, 'd1b0e996-7141-4c40-bf23-0c0895a4ffad');
INSERT INTO talhao (id, codigo, area_em_hectare, safra, estimativa_produtividade, fazenda_id) VALUES ('21538c43-d842-492b-8f30-c32aaaaf1ba9', 'TAL2', 80.00, 2021, 460.00, 'd1b0e996-7141-4c40-bf23-0c0895a4ffad');
INSERT INTO talhao (id, codigo, area_em_hectare, safra, estimativa_produtividade, fazenda_id) VALUES ('2aad741d-f58f-4dd3-a14c-4bc831b30a91', 'TAL2', 200.00, 2021, 900.00, 'e38e7b36-f52f-4048-ad9d-fa32e35c20c1');
INSERT INTO talhao (id, codigo, area_em_hectare, safra, estimativa_produtividade, fazenda_id) VALUES ('af5c8568-19d9-4baa-8e84-856e6fd9177c', 'TAL1', 300.00, 2021, 950.00, '599b2a61-03e4-4738-a9b3-dc02de03ccff');
INSERT INTO talhao (id, codigo, area_em_hectare, safra, estimativa_produtividade, fazenda_id) VALUES ('e5b3d4f6-023f-4bf5-ad54-9e6449138aed', 'TAL2', 260.00, 2021, 800.00, '599b2a61-03e4-4738-a9b3-dc02de03ccff');

INSERT INTO plantio (id, data, area_plantada_em_hectare, variedade, talhao_id) VALUES ('54de9035-05e3-4c0b-810f-1a6e5662f14d', '2020-06-20 10:00:24.555-03', 95.00, 'Soja', '63d81d90-c185-4dc0-844b-3d881157eff9');
INSERT INTO plantio (id, data, area_plantada_em_hectare, variedade, talhao_id) VALUES ('8eec7f1e-4eab-4ebc-b70a-8f801cd6e1c1', '2020-06-25 10:00:24.555-03', 195.00, 'Milho', '2aad741d-f58f-4dd3-a14c-4bc831b30a91');
INSERT INTO plantio (id, data, area_plantada_em_hectare, variedade, talhao_id) VALUES ('b1ad87c8-774d-49e1-b97d-5a5ed7abab6f', '2020-07-03 10:00:24.555-03', 40.00, 'Milho', '51e375de-3054-499b-8002-ba94db711681');
INSERT INTO plantio (id, data, area_plantada_em_hectare, variedade, talhao_id) VALUES ('03cae21f-9501-4531-b767-b3a32000ecc2', '2020-07-14 10:00:24.555-03', 75.00, 'Milho', '21538c43-d842-492b-8f30-c32aaaaf1ba9');
INSERT INTO plantio (id, data, area_plantada_em_hectare, variedade, talhao_id) VALUES ('49681aaa-dba9-4cee-800a-cf6cfd213605', '2020-01-18 10:00:24.555-03', 300.00, 'Feijao', 'af5c8568-19d9-4baa-8e84-856e6fd9177c');

INSERT INTO colheita (id, data, area_colhida_em_hectare, peso_colhido_em_kg, talhao_id) VALUES ('98282fb9-34d9-4da1-a60b-f86ab22d82a4', '2021-01-05 08:30:00.495-03', 80.00, 458.00, '21538c43-d842-492b-8f30-c32aaaaf1ba9');
INSERT INTO colheita (id, data, area_colhida_em_hectare, peso_colhido_em_kg, talhao_id) VALUES ('bc28968f-08bd-4acd-9d0d-8409b9524263', '2021-01-10 08:30:00.495-03', 199.00, 895.00, '2aad741d-f58f-4dd3-a14c-4bc831b30a91');
INSERT INTO colheita (id, data, area_colhida_em_hectare, peso_colhido_em_kg, talhao_id) VALUES ('71f4bae8-1242-4800-932f-676cca289fbe', '2021-01-15 08:30:00.495-03', 49.00, 247.00, '51e375de-3054-499b-8002-ba94db711681');
INSERT INTO colheita (id, data, area_colhida_em_hectare, peso_colhido_em_kg, talhao_id) VALUES ('b223d512-549b-4334-bd03-32cf0fadcd6f', '2021-01-20 08:30:00.495-03', 99.00, 498.00, '63d81d90-c185-4dc0-844b-3d881157eff9');
INSERT INTO colheita (id, data, area_colhida_em_hectare, peso_colhido_em_kg, talhao_id) VALUES ('06deb3e2-581c-4706-9504-8a67df76b761', '2021-01-24 08:30:00.495-03', 300.00, 950.00, 'af5c8568-19d9-4baa-8e84-856e6fd9177c');

INSERT INTO evento (id, data, area_em_hectare, tipo, talhao_id) VALUES ('e2dba13e-d8a2-4ad0-a5ef-b161e122bcd6', '2021-01-23 10:57:24.555-03', 95.00, 'PLANTIO', '63d81d90-c185-4dc0-844b-3d881157eff9');
INSERT INTO evento (id, data, area_em_hectare, tipo, talhao_id) VALUES ('2e0dbfc6-a28b-4eb0-80e4-bca5f1bfa8c0', '2021-01-23 10:57:24.555-03', 195.00, 'PLANTIO', '2aad741d-f58f-4dd3-a14c-4bc831b30a91');
INSERT INTO evento (id, data, area_em_hectare, tipo, talhao_id) VALUES ('94e41ae2-f641-464c-a2c4-397c35df816e', '2021-01-23 10:57:24.555-03', 40.00, 'PLANTIO', '51e375de-3054-499b-8002-ba94db711681');
INSERT INTO evento (id, data, area_em_hectare, tipo, talhao_id) VALUES ('00f080d7-3819-4404-8c0d-ef2b4ecdad46', '2021-01-23 10:57:24.555-03', 75.00, 'PLANTIO', '21538c43-d842-492b-8f30-c32aaaaf1ba9');
INSERT INTO evento (id, data, area_em_hectare, tipo, talhao_id) VALUES ('50f0fa97-742f-420a-a751-a185e0b2015e', '2021-01-23 10:57:24.555-03', 300.00, 'PLANTIO', 'af5c8568-19d9-4baa-8e84-856e6fd9177c');
INSERT INTO evento (id, data, area_em_hectare, tipo, talhao_id) VALUES ('e1e2915b-8811-40ae-abab-e605bbfc5135', '2021-01-23 11:49:48.495-03', 80.00, 'COLHEITA', '21538c43-d842-492b-8f30-c32aaaaf1ba9');
INSERT INTO evento (id, data, area_em_hectare, tipo, talhao_id) VALUES ('95b6d29b-ef9a-4e27-a29b-4f772a4b839a', '2021-01-23 11:49:48.495-03', 199.00, 'COLHEITA', '2aad741d-f58f-4dd3-a14c-4bc831b30a91');
INSERT INTO evento (id, data, area_em_hectare, tipo, talhao_id) VALUES ('5c81124a-c57a-417d-a854-ea09ae3158bc', '2021-01-23 11:49:48.495-03', 49.00, 'COLHEITA', '51e375de-3054-499b-8002-ba94db711681');
INSERT INTO evento (id, data, area_em_hectare, tipo, talhao_id) VALUES ('712f5467-eafb-480b-894f-d0a2e06c3179', '2021-01-23 11:49:48.495-03', 99.00, 'COLHEITA', '63d81d90-c185-4dc0-844b-3d881157eff9');
INSERT INTO evento (id, data, area_em_hectare, tipo, talhao_id) VALUES ('ecc3a07c-7fe5-43aa-a318-d01cfe1148d2', '2021-01-23 11:49:48.495-03', 300.00, 'COLHEITA', 'af5c8568-19d9-4baa-8e84-856e6fd9177c');



