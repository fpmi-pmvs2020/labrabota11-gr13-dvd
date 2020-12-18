CREATE TABLE People (
  Id		int		PRIMARY KEY AUTOINCREMENT,
  Login		text	NOT NULL,
  FIO		text	NOT NULL,
  Telephone	text	NOT NULL,
  Address	text	NOT NULL,
  BirthDate text	NOT NULL,
  RoleId 	int		NOT NULL
);

CREATE TABLE Duties (
  Id				int		PRIMARY KEY AUTOINCREMENT,
  DutyFrom			text	NOT NULL,
  DutyTo			text	NOT NULL,
  DutyTypeId		int		NOT NULL,
  MaxPeopleOnDuty	int		NOT NULL
);

CREATE TABLE DutyTypes (
  Id		int		PRIMARY KEY AUTOINCREMENT,
  TypeName	text	NOT NULL
);

CREATE TABLE PeopleOnDuty (
  Id			int		PRIMARY KEY AUTOINCREMENT,
  PersonId		int		NOT NULL,
  DutyId		int		NOT NULL,
  OnDutyFrom	text	NOT NULL,
  OnDutyTo		text	NOT NULL
);

CREATE TABLE Roles (
  Id	int		PRIMARY KEY AUTOINCREMENT,
  Name	text	NOT NULL
);

ALTER TABLE PeopleOnDuty ADD FOREIGN KEY (PersonId) REFERENCES People (Id);

ALTER TABLE PeopleOnDuty ADD FOREIGN KEY (DutyId) REFERENCES Duties (Id);

ALTER TABLE Duties ADD FOREIGN KEY (DutyTypeId) REFERENCES DutyTypes (Id);

ALTER TABLE People ADD FOREIGN KEY (RoleId) REFERENCES Roles (Id);
