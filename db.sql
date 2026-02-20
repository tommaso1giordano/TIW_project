-- MySQL dump 10.13  Distrib 8.0.31, for macos12 (x86_64)
--
-- Host: 127.0.0.1    Database: dblogintest
-- ------------------------------------------------------
-- Server version	8.0.32

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `appelli`
--

DROP TABLE IF EXISTS `appelli`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `appelli` (
  `id` int NOT NULL AUTO_INCREMENT,
  `id_scaglioni` int NOT NULL,
  `data` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  UNIQUE KEY `UNIQUE_INDEX` (`id_scaglioni`,`data`),
  CONSTRAINT `appelli_scaglioni` FOREIGN KEY (`id_scaglioni`) REFERENCES `scaglioni` (`id`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `appelli`
--

LOCK TABLES `appelli` WRITE;
/*!40000 ALTER TABLE `appelli` DISABLE KEYS */;
INSERT INTO `appelli` VALUES (1,1,'2023-01-09 00:00:00'),(2,1,'2023-02-04 00:00:00'),(4,2,'2023-05-21 00:00:00'),(5,2,'2023-06-14 00:00:00'),(3,3,'2021-06-29 00:00:00'),(6,3,'2021-07-04 00:00:00');
/*!40000 ALTER TABLE `appelli` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `docenti`
--

DROP TABLE IF EXISTS `docenti`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `docenti` (
  `matricola` int NOT NULL AUTO_INCREMENT,
  `nome` varchar(32) NOT NULL,
  `cognome` varchar(32) NOT NULL,
  `email` varchar(64) NOT NULL,
  `password` varchar(32) NOT NULL,
  PRIMARY KEY (`matricola`),
  UNIQUE KEY `matricola_UNIQUE` (`matricola`),
  UNIQUE KEY `email_UNIQUE` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=562937 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `docenti`
--

LOCK TABLES `docenti` WRITE;
/*!40000 ALTER TABLE `docenti` DISABLE KEYS */;
INSERT INTO `docenti` VALUES (192837,'Mauro','Novellini','mauro.novellini@polimi.it','192837'),(562936,'Sara','Pavesi','sara.pavesi@polimi.it','562936');
/*!40000 ALTER TABLE `docenti` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `esami`
--

DROP TABLE IF EXISTS `esami`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `esami` (
  `id_appello` int NOT NULL,
  `matricola_studente` int NOT NULL,
  `desc_esito` varchar(32) NOT NULL,
  `voto` varchar(32) NOT NULL DEFAULT '<vuoto>',
  PRIMARY KEY (`id_appello`,`matricola_studente`),
  KEY `esami_esiti_idx` (`desc_esito`),
  KEY `esami_studenti_idx` (`matricola_studente`),
  KEY `esami_appelli_idx` (`id_appello`),
  KEY `esami_voti_idx` (`voto`),
  CONSTRAINT `esami_appelli` FOREIGN KEY (`id_appello`) REFERENCES `appelli` (`id`) ON UPDATE CASCADE,
  CONSTRAINT `esami_esiti` FOREIGN KEY (`desc_esito`) REFERENCES `esiti` (`desc`) ON UPDATE CASCADE,
  CONSTRAINT `esami_studenti` FOREIGN KEY (`matricola_studente`) REFERENCES `studenti` (`matricola`) ON UPDATE CASCADE,
  CONSTRAINT `esami_voti` FOREIGN KEY (`voto`) REFERENCES `voti` (`desc`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `esami`
--

LOCK TABLES `esami` WRITE;
/*!40000 ALTER TABLE `esami` DISABLE KEYS */;
INSERT INTO `esami` VALUES (1,918472,'Non inserito','<vuoto>'),(1,937453,'Non inserito','<vuoto>'),(1,946385,'Inserito','Assente'),(1,956374,'Inserito','30'),(1,958381,'Pubblicato','20'),(1,978364,'Pubblicato','25'),(1,978645,'Inserito','Rimandato'),(1,987645,'Inserito','30 e lode'),(2,918472,'Non inserito','<vuoto>'),(2,937453,'Non inserito','<vuoto>'),(2,956374,'Non inserito','<vuoto>'),(2,978364,'Non inserito','<vuoto>'),(2,987645,'Non inserito','<vuoto>'),(3,918472,'Non inserito','<vuoto>'),(3,956374,'Inserito','<vuoto>'),(3,958381,'Verbalizzato','28'),(3,978364,'Inserito','<vuoto>'),(3,978645,'Non inserito','<vuoto>'),(4,918472,'Pubblicato','25'),(4,946385,'Inserito','Riprovato'),(4,974563,'Non inserito','<vuoto>'),(4,978364,'Non inserito','<vuoto>'),(4,978645,'Pubblicato','30 e lode'),(5,918472,'Non inserito','<vuoto>'),(5,937453,'Pubblicato','18'),(5,946385,'Non inserito','<vuoto>'),(5,956374,'Inserito','23'),(5,978364,'Inserito','Assente'),(5,987645,'Pubblicato','28');
/*!40000 ALTER TABLE `esami` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `esiti`
--

DROP TABLE IF EXISTS `esiti`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `esiti` (
  `desc` varchar(32) NOT NULL,
  PRIMARY KEY (`desc`),
  UNIQUE KEY `desc_UNIQUE` (`desc`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `esiti`
--

LOCK TABLES `esiti` WRITE;
/*!40000 ALTER TABLE `esiti` DISABLE KEYS */;
INSERT INTO `esiti` VALUES ('Inserito'),('Non inserito'),('Pubblicato'),('Rifiutato'),('Verbalizzato');
/*!40000 ALTER TABLE `esiti` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `insegnamenti`
--

DROP TABLE IF EXISTS `insegnamenti`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `insegnamenti` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nome` varchar(64) NOT NULL,
  `id_corso_laurea` int NOT NULL,
  `cfu` int NOT NULL,
  `semestre` int NOT NULL,
  UNIQUE KEY `id_UNIQUE` (`id`),
  UNIQUE KEY `nome_id_corso_laurea_UNIQUE` (`id_corso_laurea`,`nome`),
  CONSTRAINT `insegnamenti_appelli` FOREIGN KEY (`id_corso_laurea`) REFERENCES `lauree` (`id`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `insegnamenti`
--

LOCK TABLES `insegnamenti` WRITE;
/*!40000 ALTER TABLE `insegnamenti` DISABLE KEYS */;
INSERT INTO `insegnamenti` VALUES (1,'Analisi 2',1,10,1),(2,'Reti logiche',1,8,2),(3,'Nanofabbricazione',2,5,1);
/*!40000 ALTER TABLE `insegnamenti` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `iscrizioni`
--

DROP TABLE IF EXISTS `iscrizioni`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `iscrizioni` (
  `id` int NOT NULL AUTO_INCREMENT,
  `matricola_studente` int NOT NULL,
  `id_scaglione` int NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  UNIQUE KEY `UNIQUE_INDEX` (`matricola_studente`,`id_scaglione`),
  KEY `iscrizioni_scaglioni_idx` (`id_scaglione`),
  CONSTRAINT `iscrizioni_scaglioni` FOREIGN KEY (`id_scaglione`) REFERENCES `scaglioni` (`id`) ON UPDATE CASCADE,
  CONSTRAINT `iscrizioni_studenti` FOREIGN KEY (`matricola_studente`) REFERENCES `studenti` (`matricola`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `iscrizioni`
--

LOCK TABLES `iscrizioni` WRITE;
/*!40000 ALTER TABLE `iscrizioni` DISABLE KEYS */;
INSERT INTO `iscrizioni` VALUES (5,918472,1),(1,918472,3),(6,937453,1),(12,937453,3),(4,946385,1),(15,946385,2),(13,946385,3),(7,956374,1),(2,958381,1),(16,958381,2),(3,958381,3),(11,974563,2),(8,978364,1),(14,978364,3),(9,978645,1),(17,978645,2),(10,987645,1);
/*!40000 ALTER TABLE `iscrizioni` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lauree`
--

DROP TABLE IF EXISTS `lauree`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `lauree` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nome` varchar(32) NOT NULL,
  `tipo_laurea` varchar(64) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  UNIQUE KEY `nome_tipo_laurea_UNIQUE` (`nome`,`tipo_laurea`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lauree`
--

LOCK TABLES `lauree` WRITE;
/*!40000 ALTER TABLE `lauree` DISABLE KEYS */;
INSERT INTO `lauree` VALUES (2,'Ingegneria fisica','Triennale'),(1,'Ingegneria informatica','Triennale');
/*!40000 ALTER TABLE `lauree` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `scaglioni`
--

DROP TABLE IF EXISTS `scaglioni`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `scaglioni` (
  `id` int NOT NULL AUTO_INCREMENT,
  `id_insegnamento` int NOT NULL,
  `matricola_docente` int NOT NULL,
  `anno` year NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `idscaglioni_UNIQUE` (`id`),
  UNIQUE KEY `UNIQUE_INDEX` (`id_insegnamento`,`anno`,`matricola_docente`),
  KEY `scaglioni_docenti_idx` (`matricola_docente`),
  CONSTRAINT `scaglioni_docenti` FOREIGN KEY (`matricola_docente`) REFERENCES `docenti` (`matricola`) ON UPDATE CASCADE,
  CONSTRAINT `scaglioni_insegnamenti` FOREIGN KEY (`id_insegnamento`) REFERENCES `insegnamenti` (`id`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `scaglioni`
--

LOCK TABLES `scaglioni` WRITE;
/*!40000 ALTER TABLE `scaglioni` DISABLE KEYS */;
INSERT INTO `scaglioni` VALUES (2,1,562936,2021),(1,1,192837,2022),(3,2,192837,2020);
/*!40000 ALTER TABLE `scaglioni` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Temporary view structure for view `scaglioni_docente`
--

DROP TABLE IF EXISTS `scaglioni_docente`;
/*!50001 DROP VIEW IF EXISTS `scaglioni_docente`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `scaglioni_docente` AS SELECT 
 1 AS `matricola_docente`,
 1 AS `id_scaglione`,
 1 AS `id_insegnamento`,
 1 AS `nome_insegnamento`,
 1 AS `anno`,
 1 AS `semestre`*/;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `studenti`
--

DROP TABLE IF EXISTS `studenti`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `studenti` (
  `matricola` int NOT NULL AUTO_INCREMENT,
  `nome` varchar(32) NOT NULL,
  `cognome` varchar(32) NOT NULL,
  `email` varchar(64) NOT NULL,
  `id_corso_laurea` int NOT NULL,
  `password` varchar(32) NOT NULL,
  PRIMARY KEY (`matricola`),
  UNIQUE KEY `matricola_UNIQUE` (`matricola`),
  UNIQUE KEY `email_UNIQUE` (`email`),
  KEY `studenti_lauree_idx` (`id_corso_laurea`),
  CONSTRAINT `studenti_lauree` FOREIGN KEY (`id_corso_laurea`) REFERENCES `lauree` (`id`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=987646 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `studenti`
--

LOCK TABLES `studenti` WRITE;
/*!40000 ALTER TABLE `studenti` DISABLE KEYS */;
INSERT INTO `studenti` VALUES (918472,'Daniele','Rossi','daniele.rossi@mail.polimi.it',1,'918472'),(937453,'Matteo','Bianchi','matteo.bianchi@mail.polimi.it',1,'937453'),(946385,'Giulia','Carlini','giulia.carlini@mail.polimi.it',1,'946385'),(956374,'Aurora','Levoni','aurora.pedroni@mail.polimi.it',1,'956374'),(958381,'Luca','Olivieri','luca.olivieri@mail.polimi.it',1,'958381'),(974563,'Adriano','Olivetti','adriano.olivetti@mail.polimi.it',1,'974563'),(978364,'Sara','Verdi','sara.verdi@mail.polimi.it',1,'978364'),(978645,'Marina','Pedroni','marina.pedroni@mail.polimi.it',1,'978645'),(987645,'Francesca','Baccolini','francesca.baccolini@mail.polimi.it',1,'987645');
/*!40000 ALTER TABLE `studenti` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `studenti_verbale`
--

DROP TABLE IF EXISTS `studenti_verbale`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `studenti_verbale` (
  `id_verbale` int NOT NULL,
  `matricola_studente` int NOT NULL,
  PRIMARY KEY (`id_verbale`,`matricola_studente`),
  KEY `studenti_key_idx` (`matricola_studente`),
  CONSTRAINT `studenti_key` FOREIGN KEY (`matricola_studente`) REFERENCES `studenti` (`matricola`) ON UPDATE CASCADE,
  CONSTRAINT `verbali_key` FOREIGN KEY (`id_verbale`) REFERENCES `verbali` (`id`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `studenti_verbale`
--

LOCK TABLES `studenti_verbale` WRITE;
/*!40000 ALTER TABLE `studenti_verbale` DISABLE KEYS */;
/*!40000 ALTER TABLE `studenti_verbale` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `verbali`
--

DROP TABLE IF EXISTS `verbali`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `verbali` (
  `id` int NOT NULL AUTO_INCREMENT,
  `id_appello` int NOT NULL,
  `datetime` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  KEY `verbali_appelli_idx` (`id_appello`),
  CONSTRAINT `verbali_appelli` FOREIGN KEY (`id_appello`) REFERENCES `appelli` (`id`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `verbali`
--

LOCK TABLES `verbali` WRITE;
/*!40000 ALTER TABLE `verbali` DISABLE KEYS */;
/*!40000 ALTER TABLE `verbali` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Temporary view structure for view `verbali_view`
--

DROP TABLE IF EXISTS `verbali_view`;
/*!50001 DROP VIEW IF EXISTS `verbali_view`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `verbali_view` AS SELECT 
 1 AS `id`,
 1 AS `id_appello`,
 1 AS `datetime`,
 1 AS `nome`,
 1 AS `cognome`,
 1 AS `matricola`,
 1 AS `voto`*/;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `voti`
--

DROP TABLE IF EXISTS `voti`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `voti` (
  `desc` varchar(32) NOT NULL,
  PRIMARY KEY (`desc`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `voti`
--

LOCK TABLES `voti` WRITE;
/*!40000 ALTER TABLE `voti` DISABLE KEYS */;
INSERT INTO `voti` VALUES ('<vuoto>'),('18'),('19'),('20'),('21'),('22'),('23'),('24'),('25'),('26'),('27'),('28'),('29'),('30'),('30 e lode'),('Assente'),('Rimandato'),('Riprovato');
/*!40000 ALTER TABLE `voti` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Final view structure for view `scaglioni_docente`
--

/*!50001 DROP VIEW IF EXISTS `scaglioni_docente`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `scaglioni_docente` (`matricola_docente`,`id_scaglione`,`id_insegnamento`,`nome_insegnamento`,`anno`,`semestre`) AS select `docenti`.`matricola` AS `matricola_docente`,`scaglioni`.`id` AS `id`,`scaglioni`.`id_insegnamento` AS `id_insegnamento`,`insegnamenti`.`nome` AS `nome`,`scaglioni`.`anno` AS `anno`,`insegnamenti`.`semestre` AS `semestre` from ((`docenti` join `scaglioni` on((`docenti`.`matricola` = `scaglioni`.`matricola_docente`))) join `insegnamenti` on((`insegnamenti`.`id` = `scaglioni`.`id_insegnamento`))) order by `docenti`.`matricola`,`insegnamenti`.`nome` desc */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `verbali_view`
--

/*!50001 DROP VIEW IF EXISTS `verbali_view`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `verbali_view` (`id`,`id_appello`,`datetime`,`nome`,`cognome`,`matricola`,`voto`) AS select `verbali`.`id` AS `id`,`verbali`.`id_appello` AS `id_appello`,`verbali`.`datetime` AS `datetime`,`studenti`.`nome` AS `nome`,`studenti`.`cognome` AS `cognome`,`studenti`.`matricola` AS `matricola`,`esami`.`voto` AS `voto` from (((`verbali` join `studenti_verbale` on((`verbali`.`id` = `studenti_verbale`.`id_verbale`))) join `studenti` on((`studenti_verbale`.`matricola_studente` = `studenti`.`matricola`))) join `esami` on(((`esami`.`id_appello` = `verbali`.`id_appello`) and (`esami`.`matricola_studente` = `studenti_verbale`.`matricola_studente`)))) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-06-13 18:35:13
