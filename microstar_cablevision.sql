-- phpMyAdmin SQL Dump
-- version 5.0.2
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Apr 09, 2022 at 03:19 AM
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

-- --------------------------------------------------------

--
-- Table structure for table `complaint`
--

CREATE TABLE `complaint` (
  `complaintID` int(11) NOT NULL,
  `complaintType` varchar(20) NOT NULL,
  `complaintDetails` varchar(100) NOT NULL,
  `status` char(1) NOT NULL DEFAULT 'U',
  `customerID` varchar(10) NOT NULL,
  `staffID` varchar(10) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `complaint`
--

INSERT INTO `complaint` (`complaintID`, `complaintType`, `complaintDetails`, `status`, `customerID`, `staffID`) VALUES
(1, 'Internet', 'Internet not working', 'U', 'B4955', NULL),
(2, 'Cable ', 'Cable not working', 'U', 'C3726', 'X074'),
(3, 'Payment', 'Payment not showing up on System', 'U', 'J6889', 'E135'),
(4, 'Other', 'I have been receiving emails from MicroStar that my account information is compromised. The email as', 'U', 'Z9359', NULL),
(5, 'Internet', 'Internet is very slow in my area', 'U', 'J6889', 'K928'),
(6, 'Cable', 'Cable has been down in my area for the past 5 days', 'U', 'C3726', NULL),
(7, 'Payment', 'I made payments for this month but they were not showing up on the system', 'U', 'P4386', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `customer`
--

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

CREATE TABLE `livechat` (
  `liveChatID` int(11) NOT NULL,
  `customerID` varchar(10) NOT NULL,
  `staffID` varchar(10) NOT NULL,
  `message` varchar(1000) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `livechat`
--

INSERT INTO `livechat` (`liveChatID`, `customerID`, `staffID`, `message`) VALUES
(1, 'B4955', 'E135', 'Hello, Good afternoon'),
(2, 'C3726', 'X074', 'Hello'),
(3, 'J6889', 'K928', 'You\'re Welcome'),
(4, 'Z9359', 'U386', 'Give me a sec'),
(5, 'C3726', 'U386', 'Thank you'),
(6, 'P4386', 'N319', 'Hi'),
(7, 'B4955', 'N319', 'Hi');

-- --------------------------------------------------------

--
-- Table structure for table `payment`
--

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

CREATE TABLE `response` (
  `responseID` int(11) NOT NULL,
  `complaintID` int(11) NOT NULL,
  `proposedDateOfVisit` datetime DEFAULT NULL,
  `responseDetails` varchar(100) NOT NULL,
  `responseDateTime` datetime NOT NULL,
  `staffID` varchar(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `response`
--

INSERT INTO `response` (`responseID`, `complaintID`, `proposedDateOfVisit`, `responseDetails`, `responseDateTime`, `staffID`) VALUES
(1, 1, '2022-04-21 21:59:09', 'soon fix', '2022-04-08 01:55:27', 'X074'),
(2, 2, NULL, 'We will soon contact you and send a technician', '2022-04-08 14:28:19', 'U386'),
(3, 3, NULL, 'A technician will soon contact you', '2022-04-07 11:38:19', 'F102'),
(4, 4, NULL, 'I would like to advise you that the email you have received is a phishing email. Please ignore it', '2022-04-01 15:34:09', 'F102'),
(5, 5, '2022-04-13 12:28:19', 'Will contact you soon', '2022-04-05 11:11:19', 'K928'),
(6, 6, NULL, 'We will soon rectify that issue', '2022-04-02 13:13:23', 'N319'),
(7, 7, NULL, 'Will soon rectify', '2022-04-06 09:28:19', 'U386');

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
  MODIFY `complaintID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT for table `livechat`
--
ALTER TABLE `livechat`
  MODIFY `liveChatID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT for table `response`
--
ALTER TABLE `response`
  MODIFY `responseID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

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
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
