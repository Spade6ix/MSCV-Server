-- phpMyAdmin SQL Dump
-- version 5.0.2
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Mar 24, 2022 at 05:49 AM
-- Server version: 10.4.13-MariaDB
-- PHP Version: 7.4.8

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `microstar_cablevision`
--
CREATE DATABASE IF NOT EXISTS `microstar_cablevision` DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci;
USE `microstar_cablevision`;

-- --------------------------------------------------------

--
-- Table structure for table `complaint`
--

DROP TABLE IF EXISTS `complaint`;
CREATE TABLE `complaint` (
  `complaintID` int(11) NOT NULL,
  `complaintType` varchar(20) NOT NULL,
  `complaintDetails` varchar(100) NOT NULL,
  `status` char(1) NOT NULL DEFAULT 'U',
  `customerID` varchar(10) NOT NULL,
  `staffID` varchar(10) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `customer`
--

DROP TABLE IF EXISTS `customer`;
CREATE TABLE `customer` (
  `customerID` varchar(10) NOT NULL,
  `password` varchar(20) NOT NULL,
  `firstName` varchar(50) NOT NULL,
  `lastName` varchar(50) NOT NULL,
  `address` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `customer`
--

INSERT INTO `customer` (`customerID`, `password`, `firstName`, `lastName`, `address`) VALUES
('B4955', 'agnus357', 'Elizabeth', 'Keene', 'Chapelton, Clarendon'),
('C3726', 'johnP90s', 'John ', 'Public', 'Papine, Kgn 6'),
('J6889', 'raymondR876', 'Raymond', 'Reddington', 'Santa Cruz, St. Elizabeth'),
('P4386', 'klaus44', 'Hope', 'Mikaelson', 'Spanish Town, St. Catherine'),
('Z9359', 'JVMGosling1991', 'Java ', 'Cascade', 'Falmouth, Trelawny');

-- --------------------------------------------------------

--
-- Table structure for table `customeremail`
--

DROP TABLE IF EXISTS `customeremail`;
CREATE TABLE `customeremail` (
  `email` varchar(50) NOT NULL,
  `customerID` varchar(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `customeremail`
--

INSERT INTO `customeremail` (`email`, `customerID`) VALUES
('Eliz_Keene@gmail.com', 'B4955'),
('JDK_main@jvm.com', 'Z9359'),
('jpublic@yahoo.com', 'C3726'),
('mkhope876@hotmail.com', 'P4386'),
('publiclyJohn@sql.com', 'C3726'),
('reddington.ray@gmail.com', 'J6889'),
('return0@jvm.com', 'Z9359');

-- --------------------------------------------------------

--
-- Table structure for table `customerphone`
--

DROP TABLE IF EXISTS `customerphone`;
CREATE TABLE `customerphone` (
  `phone` varchar(20) NOT NULL,
  `customerID` varchar(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `customerphone`
--

INSERT INTO `customerphone` (`phone`, `customerID`) VALUES
('(876)1113638', 'B4955'),
('(876)1741234', 'J6889'),
('(876)3315667', 'C3726'),
('(876)5976137', 'Z9359'),
('(876)6587554', 'P4386'),
('(876)6842384', 'Z9359'),
('(876)7563287', 'B4955'),
('(876)8589999', 'C3726');

-- --------------------------------------------------------

--
-- Table structure for table `employee`
--

DROP TABLE IF EXISTS `employee`;
CREATE TABLE `employee` (
  `staffID` varchar(10) NOT NULL,
  `password` varchar(20) NOT NULL,
  `firstName` varchar(50) NOT NULL,
  `lastName` varchar(50) NOT NULL,
  `job` char(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `employee`
--

INSERT INTO `employee` (`staffID`, `password`, `firstName`, `lastName`, `job`) VALUES
('E135', 'MySQL876', 'Jane', 'Python', 'T'),
('F102', 'WizzyWill6', 'William', 'Wizard', 'C'),
('K928', 'Log4jLucas', 'Lucas', 'Apache', 'T'),
('N319', 'jammyL376', 'James', 'Loop', 'C'),
('U386', 'IzzyI100', 'Isabella ', 'Interface', 'C'),
('X074', 'Sophia98', 'Sophia', 'Script', 'T');

-- --------------------------------------------------------

--
-- Table structure for table `livechat`
--

DROP TABLE IF EXISTS `livechat`;
CREATE TABLE `livechat` (
  `liveChatID` int(11) NOT NULL,
  `customerID` varchar(10) NOT NULL,
  `staffID` varchar(10) NOT NULL,
  `message` varchar(1000) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `payment`
--

DROP TABLE IF EXISTS `payment`;
CREATE TABLE `payment` (
  `paymentID` varchar(20) NOT NULL,
  `customerID` varchar(10) NOT NULL,
  `dateOfPayment` varchar(30) NOT NULL,
  `amountPaid` double NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `payment`
--

INSERT INTO `payment` (`paymentID`, `customerID`, `dateOfPayment`, `amountPaid`) VALUES
('P14868', 'J6889', 'March 2, 2022', 2000),
('P19865', 'P4386', 'February 21, 2022', 6000),
('P24765', 'C3726', 'February 20, 2022', 3000),
('P31248', 'Z9359', 'March 3, 2022', 9000),
('P37667', 'B4955', 'March 1, 2022', 5000),
('P46617', 'J6889', 'February 25, 2022', 6000),
('P54768', 'Z9359', 'March 2, 2022', 10000),
('P59947', 'P4386', 'March 2, 2022', 5000),
('P66847', 'B4955', 'February 23, 2022', 8000),
('P77845', 'B4955', 'March 5, 2022', 4000);

-- --------------------------------------------------------

--
-- Table structure for table `query`
--

DROP TABLE IF EXISTS `query`;
CREATE TABLE `query` (
  `customerID` varchar(10) NOT NULL,
  `paymentStatus` varchar(30) NOT NULL,
  `amountDue` double DEFAULT NULL,
  `dueDate` varchar(25) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `query`
--

INSERT INTO `query` (`customerID`, `paymentStatus`, `amountDue`, `dueDate`) VALUES
('B4955', 'No Payment Due', NULL, NULL),
('C3726', 'No Payment Due', NULL, NULL),
('J6889', 'Payment Due', 5000, 'March 31,2022'),
('P4386', 'Payment Due', 3000, 'March 31,2022'),
('Z9359', 'Payment Due', 1000, 'March 31,2022');

-- --------------------------------------------------------

--
-- Table structure for table `response`
--

DROP TABLE IF EXISTS `response`;
CREATE TABLE `response` (
  `responseID` int(11) NOT NULL,
  `complaintID` int(11) NOT NULL,
  `proposedDateOfVisit` datetime DEFAULT NULL,
  `responseDetails` varchar(100) NOT NULL,
  `responseDateTime` datetime NOT NULL,
  `staffID` varchar(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `complaint`
--
ALTER TABLE `complaint`
  ADD PRIMARY KEY (`complaintID`),
  ADD KEY `fk_Customer_Complaint` (`customerID`),
  ADD KEY `fk_Employee_Complaint` (`staffID`);

--
-- Indexes for table `customer`
--
ALTER TABLE `customer`
  ADD PRIMARY KEY (`customerID`);

--
-- Indexes for table `customeremail`
--
ALTER TABLE `customeremail`
  ADD PRIMARY KEY (`email`,`customerID`),
  ADD KEY `fk_Customer_Email` (`customerID`);

--
-- Indexes for table `customerphone`
--
ALTER TABLE `customerphone`
  ADD PRIMARY KEY (`phone`,`customerID`),
  ADD KEY `fk_Customer_Phone` (`customerID`);

--
-- Indexes for table `employee`
--
ALTER TABLE `employee`
  ADD PRIMARY KEY (`staffID`);

--
-- Indexes for table `livechat`
--
ALTER TABLE `livechat`
  ADD PRIMARY KEY (`liveChatID`),
  ADD KEY `fk_Customer_LiveChat` (`customerID`),
  ADD KEY `fk_Employee_LiveChat` (`staffID`);

--
-- Indexes for table `payment`
--
ALTER TABLE `payment`
  ADD PRIMARY KEY (`paymentID`),
  ADD KEY `fk_Customer_Payment` (`customerID`);

--
-- Indexes for table `query`
--
ALTER TABLE `query`
  ADD PRIMARY KEY (`customerID`);

--
-- Indexes for table `response`
--
ALTER TABLE `response`
  ADD PRIMARY KEY (`responseID`),
  ADD KEY `fk_Complaint_Response` (`complaintID`),
  ADD KEY `fk_Employee_Response` (`staffID`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `complaint`
--
ALTER TABLE `complaint`
  MODIFY `complaintID` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `livechat`
--
ALTER TABLE `livechat`
  MODIFY `liveChatID` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `response`
--
ALTER TABLE `response`
  MODIFY `responseID` int(11) NOT NULL AUTO_INCREMENT;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `complaint`
--
ALTER TABLE `complaint`
  ADD CONSTRAINT `fk_Customer_Complaint` FOREIGN KEY (`customerID`) REFERENCES `customer` (`customerID`),
  ADD CONSTRAINT `fk_Employee_Complaint` FOREIGN KEY (`staffID`) REFERENCES `employee` (`staffID`);

--
-- Constraints for table `customeremail`
--
ALTER TABLE `customeremail`
  ADD CONSTRAINT `fk_Customer_Email` FOREIGN KEY (`customerID`) REFERENCES `customer` (`customerID`);

--
-- Constraints for table `customerphone`
--
ALTER TABLE `customerphone`
  ADD CONSTRAINT `fk_Customer_Phone` FOREIGN KEY (`customerID`) REFERENCES `customer` (`customerID`);

--
-- Constraints for table `livechat`
--
ALTER TABLE `livechat`
  ADD CONSTRAINT `fk_Customer_LiveChat` FOREIGN KEY (`customerID`) REFERENCES `customer` (`customerID`),
  ADD CONSTRAINT `fk_Employee_LiveChat` FOREIGN KEY (`staffID`) REFERENCES `employee` (`staffID`);

--
-- Constraints for table `payment`
--
ALTER TABLE `payment`
  ADD CONSTRAINT `fk_Customer_Payment` FOREIGN KEY (`customerID`) REFERENCES `customer` (`customerID`);

--
-- Constraints for table `query`
--
ALTER TABLE `query`
  ADD CONSTRAINT `fk_Customer_Query` FOREIGN KEY (`customerID`) REFERENCES `customer` (`customerID`);

--
-- Constraints for table `response`
--
ALTER TABLE `response`
  ADD CONSTRAINT `fk_Complaint_Response` FOREIGN KEY (`complaintID`) REFERENCES `complaint` (`complaintID`),
  ADD CONSTRAINT `fk_Employee_Response` FOREIGN KEY (`staffID`) REFERENCES `employee` (`staffID`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
