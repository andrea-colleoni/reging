CREATE DATABASE  IF NOT EXISTS `reging` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `reging`;
-- MySQL dump 10.13  Distrib 5.7.12, for Win64 (x86_64)
--
-- Host: scm-pc01_pb1    Database: reging
-- ------------------------------------------------------
-- Server version	5.7.17-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `bancaore`
--

DROP TABLE IF EXISTS `bancaore`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `bancaore` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `dataAttribuzione` date NOT NULL,
  `quantitaMinuti` float NOT NULL,
  `tipoOra` varchar(255) DEFAULT NULL,
  `dipendente_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_iwbhbxrrgu784l5pab72kxob2` (`dipendente_id`),
  CONSTRAINT `FK_iwbhbxrrgu784l5pab72kxob2` FOREIGN KEY (`dipendente_id`) REFERENCES `dipendente` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=893 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `causale`
--

DROP TABLE IF EXISTS `causale`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `causale` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `descrizione` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `coppiatimbrate`
--

DROP TABLE IF EXISTS `coppiatimbrate`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `coppiatimbrate` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `giornata_id` bigint(20) DEFAULT NULL,
  `ingresso_id` int(11) DEFAULT NULL,
  `uscita_id` int(11) DEFAULT NULL,
  `sequenza` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_4l3t8l6nvkqa0g78skb1dhudo` (`giornata_id`),
  KEY `FK_nid938n0nkjdwe1opxgcyei8` (`ingresso_id`),
  KEY `FK_6x58xnpjcy00maxi55j2fvej` (`uscita_id`),
  CONSTRAINT `FK_4l3t8l6nvkqa0g78skb1dhudo` FOREIGN KEY (`giornata_id`) REFERENCES `giornata` (`id`),
  CONSTRAINT `FK_6x58xnpjcy00maxi55j2fvej` FOREIGN KEY (`uscita_id`) REFERENCES `timbrata` (`id`),
  CONSTRAINT `FK_nid938n0nkjdwe1opxgcyei8` FOREIGN KEY (`ingresso_id`) REFERENCES `timbrata` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2076 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `dipendente`
--

DROP TABLE IF EXISTS `dipendente`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `dipendente` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `codiceBadge` varchar(255) DEFAULT NULL,
  `cognome` varchar(255) DEFAULT NULL,
  `nome` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  `RUOLO_ID` bigint(20) DEFAULT NULL,
  `TURNO_ID` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_hk38kxbg226cvy88jmuwg48l6` (`codiceBadge`),
  UNIQUE KEY `UK_nw7fian7sy73u8n8nsn7adsil` (`username`),
  KEY `FK_rfrb8ulj2kdkv2mbc8h7pd7r` (`RUOLO_ID`),
  KEY `FK_db9ujd9sq0kq07gp5xascedm6` (`TURNO_ID`),
  CONSTRAINT `FK_db9ujd9sq0kq07gp5xascedm6` FOREIGN KEY (`TURNO_ID`) REFERENCES `turno` (`id`),
  CONSTRAINT `FK_rfrb8ulj2kdkv2mbc8h7pd7r` FOREIGN KEY (`RUOLO_ID`) REFERENCES `ruolo` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `giornata`
--

DROP TABLE IF EXISTS `giornata`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `giornata` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `dataRiferimento` date NOT NULL,
  `note` varchar(255) DEFAULT NULL,
  `dipendente_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_lvjakykk0gqyytr6yf98wbayx` (`dipendente_id`),
  CONSTRAINT `FK_lvjakykk0gqyytr6yf98wbayx` FOREIGN KEY (`dipendente_id`) REFERENCES `dipendente` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1040 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ruolo`
--

DROP TABLE IF EXISTS `ruolo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ruolo` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `nome` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `timbrata`
--

DROP TABLE IF EXISTS `timbrata`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `timbrata` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `causale` varchar(255) DEFAULT NULL,
  `codiceBadge` varchar(255) DEFAULT NULL,
  `dataOra` datetime NOT NULL,
  `fuoriOrarioTurno` bit(1) NOT NULL,
  `ip` varchar(255) DEFAULT NULL,
  `modificataManualmente` bit(1) NOT NULL,
  `tipoTimbrata` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4198 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `turno`
--

DROP TABLE IF EXISTS `turno`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `turno` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `descrizione` varchar(255) DEFAULT NULL,
  `discretizzazioneMinuti` int(11) NOT NULL,
  `durataOre` float NOT NULL,
  `feriale` bit(1) NOT NULL,
  `festivo` bit(1) NOT NULL,
  `finePausa` time DEFAULT NULL,
  `ingressoMax` time NOT NULL,
  `ingressoMin` time NOT NULL,
  `inizioPausa` time DEFAULT NULL,
  `minutiArrotondamentoDifetto` int(11) NOT NULL,
  `minutiArrotondamentoEccesso` int(11) NOT NULL,
  `nome` varchar(255) DEFAULT NULL,
  `preFestivo` bit(1) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping routines for database 'reging'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2017-03-17 15:41:55
