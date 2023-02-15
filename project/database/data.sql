USE wineshop;

INSERT INTO user(Name, Surname, FiscalCode, Telephone, Address, Username, PswHash, Type) VALUES
('Federico', 'Brandini', 'BRNFRC02A25B034H', '3480387517', 'via 17 aprile, n. 1', 'aFedeBrando', 'd760688da522b4dc3350e6fb68961b0934f911c7d0ff337438cabf4608789ba94ce70b6601d7e08a279ef088716c4b1913b984513fea4c557d404d0598d4f2f1', 'Administrator'),
('Federico', 'Putamorsi', 'BRNFRC02A25B034H', '3480387517', 'via 17 aprile, n. 1', 'eFedePuta', 'd760688da522b4dc3350e6fb68961b0934f911c7d0ff337438cabf4608789ba94ce70b6601d7e08a279ef088716c4b1913b984513fea4c557d404d0598d4f2f1', 'Employee'),
('Paolo', 'Bonolis', 'BRNFRC02A25B034H', '3480387517', 'via 17 aprile, n. 1', 'ePaoBon', 'd760688da522b4dc3350e6fb68961b0934f911c7d0ff337438cabf4608789ba94ce70b6601d7e08a279ef088716c4b1913b984513fea4c557d404d0598d4f2f1', 'Employee'),
('Beppe', 'Vessicchio', 'BRNFRC02A25B034H', '3480387517', 'via 17 aprile, n. 1', 'eBeppeVess', 'd760688da522b4dc3350e6fb68961b0934f911c7d0ff337438cabf4608789ba94ce70b6601d7e08a279ef088716c4b1913b984513fea4c557d404d0598d4f2f1', 'Employee'),
('Maurizio', 'Costanzo', 'BRNFRC02A25B034H', '3480387517', 'via 17 aprile, n. 1', 'cMauCosta', 'd760688da522b4dc3350e6fb68961b0934f911c7d0ff337438cabf4608789ba94ce70b6601d7e08a279ef088716c4b1913b984513fea4c557d404d0598d4f2f1', 'Customer');

INSERT INTO wine(Name, Producer, Origin, Year, Notes, Vines, Price, AvailableQuantity) VALUES
('Chianti Classico', 'Badia a Coltibuono', 'Emilia Romagna', 2018, null, 'Garzon', 15.00, 150),
('Pinot Noir', 'Ochota Barrels', 'Trentino Alto Adige', 2019, 'Pregiato.', 'Garzon', 45.00, 36),
('Aleatico Alea Ludendo', 'Tenuta delle Ripalte', 'Trentino Alto Adige', 2017, null, 'Garzon', 26.00, 54),
('Amarone', 'Pieropan', 'Trentino Alto Adige', 2016, null, 'Garzon', 47.00, 50),
('Astra', "Domaine de L'écu", 'Trentino Alto Adige', 2018, 'Delicato.', 'Garzon', 30.00, 10),
('Barbaresco', 'Bruno Rocca', 'Trentino Alto Adige', 2016, null, 'Garzon', 37.00, 19),
('Sassicaia', 'Tenuta San Guido', 'Emilia Romagna', 2012, 'Pregiato.', 'Monte delle Vigne', 1187.00, 1),
('Barolo', 'Giacomo Conterno', 'Trentino Alto Adige', 2018, null, 'Garzon', 499.00, 20),
('Il Randagio', 'Tenuta di Carleone', 'Trentino Alto Adige', 2019, null, 'Garzon', 14.51, 278),
('Grotte Rosse', 'Salustri', 'Trentino Alto Adige', 2018, null, 'Garzon', 26.85, 112);

INSERT INTO Service VALUES
('S1', 'Supplier', 'Gioacchino', 'Esposito', '333 303 0006', 'via Aglio, 91/A'),
('C1', 'Courier', 'Armando', 'Tresca', '354 777 768', 'via Norma, 51');