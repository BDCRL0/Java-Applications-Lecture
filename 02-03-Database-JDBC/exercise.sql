CREATE DATABASE IF NOT EXISTS `exercise` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
USE `exercise`;

CREATE TABLE `students` (
  `Code` varchar(20) NOT NULL,
  `Firstname` varchar(50) NOT NULL,
  `Lastname` varchar(50) NOT NULL,
  `Email` varchar(50) NOT NULL,
  `Birthdate` date NOT NULL,
  `ProgramId` int(11) NOT NULL,
  `Credits` int(11) NOT NULL,
  PRIMARY KEY  (`Code`)
) ENGINE = MYISAM CHARACTER SET utf8 COLLATE utf8_general_ci;

INSERT INTO `students` (`Code`, `Firstname`, `Lastname`, `Email`, `Birthdate`, `ProgramId`, `Credits`) VALUES
('AAAABBB', 'Peter', 'Smith', 'aaa@gmail.com', '1990-02-23', 1, 10),
('CCCCBBB', 'John', 'Brown', 'bbb@gmai.hu', '1981-05-10', 1, 25),
('DDDFFFF', 'Jane', 'Taylor', 'ccc@gmai.com', '1988-08-01', 4, 30),
('QQQQBBB', 'Kate', 'Blake', 'ddd@gmai.hu', '1992-11-30', 2, 56),
('RRRRTTT', 'George', 'Green', 'eee@gmais.com', '1989-09-29', 3, 82);

CREATE TABLE `programs` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `type` int(11) NOT NULL DEFAULT 0,
  PRIMARY KEY  (`Id`)
) ENGINE = MYISAM CHARACTER SET utf8 COLLATE utf8_general_ci;


INSERT INTO `programs` (`Id`, `name`, `type`) VALUES
(1, 'Computer science', 2),
(2, 'Vehicle Engineering', 2),
(3, 'Tourism and Catering', 1),
(4, 'Business Administration', 1);

CREATE TABLE `courses` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `Name` varchar(50) NOT NULL,
  `Credit` int(11) NOT NULL DEFAULT 0,
  `Description` varchar(50),
  PRIMARY KEY (`id`)
) ENGINE = MYISAM CHARACTER SET utf8 COLLATE utf8_general_ci;


INSERT INTO `courses` (`Id`, `Name`, `Credit`, `Description`) VALUES
(1, 'Programming 1', 4, NULL),
(2, 'Programming 2', 5, NULL),
(3, 'Databases', 5, NULL),
(4, 'Visual programming', 2, NULL),
(5, 'Hotels', 3, NULL),
(6, 'Law', 5, NULL),
(7, 'History', 5, NULL);

CREATE TABLE `programcourse` (
  `ProgramId` int(11) NOT NULL DEFAULT 0,
  `CourseId` int(11) NOT NULL DEFAULT 0,
  PRIMARY KEY (`ProgramId`,`CourseId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


INSERT INTO `programcourse` (`ProgramId`, `CourseId`) VALUES
(1, 1),
(1, 2),
(1, 3),
(1, 4),
(2, 1),
(2, 3),
(3, 1),
(3, 5),
(3, 6),
(4, 6),
(4, 7);



