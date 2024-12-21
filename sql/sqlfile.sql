CREATE DATABASE sampleproj;
USE sampleproj;

CREATE TABLE players (
    playerid INT PRIMARY KEY,
    name VARCHAR(100),
    jersey INT,
    agentid INT,
    teamid INT

);

CREATE TABLE teams (
    teamid INT PRIMARY KEY,
    name VARCHAR(100),
    coachid INT,
    champid INT
);

CREATE TABLE coach (
    coachid INT PRIMARY KEY,
    name VARCHAR(100),
    age INT
);

CREATE TABLE agent (
    agentid INT PRIMARY KEY,
    name VARCHAR(100)
    );

CREATE TABLE Championship (
    champid INT PRIMARY KEY,
    name VARCHAR(100),
    year INT
);

CREATE TABLE games (
    gameid INT PRIMARY KEY,
    team1 INT,
    team2 INT,
    champid INT,
    date DATE,
    motm INT REFERENCES players(playerid) ON DELETE SET NULL,
    team1name VARCHAR(100),
    team2name VARCHAR(100),
    motmname VARCHAR(100),
    winnerid INT,
    winnername VARCHAR(100)

);


ALTER TABLE players
ADD FOREIGN KEY (teamid) REFERENCES teams(teamid) ON DELETE SET NULL;



ALTER TABLE players
ADD FOREIGN KEY (agentid) REFERENCES agent(agentid) ON DELETE SET NULL;

ALTER TABLE teams
ADD FOREIGN KEY (coachid) REFERENCES coach(coachid) ON DELETE SET NULL;

ALTER TABLE teams
ADD FOREIGN KEY (champid) REFERENCES Championship(champid) ON DELETE SET NULL;

ALTER TABLE games
ADD FOREIGN KEY (team1) REFERENCES teams(teamid) ON DELETE SET NULL;

ALTER TABLE games
ADD FOREIGN KEY (team2) REFERENCES teams(teamid) ON DELETE SET NULL;

ALTER TABLE games
ADD FOREIGN KEY (champid) REFERENCES Championship(champid) ON DELETE SET NULL;

ALTER TABLE games
ADD team1name VARCHAR(100) REFERENCES teams(name) ON DELETE SET NULL;

ALTER TABLE games
ADD team2name VARCHAR(100) REFERENCES teams(name) ON DELETE SET NULL;

ALTER TABLE games
ADD motmname VARCHAR(100) REFERENCES players(name) ON DELETE SET NULL;

ALTER TABLE games
ADD winnerid INT REFERENCES teams(teamid) ON DELETE SET NULL;

ALTER TABLE games
ADD FOREIGN KEY (motm) REFERENCES players(playerid) ON DELETE SET NULL;

ALTER TABLE games
ADD FOREIGN KEY (winnerid) REFERENCES teams(teamid) ON DELETE SET NULL;


ALTER TABLE games
ADD FOREIGN KEY (motmname) REFERENCES players(name) ON DELETE SET NULL;

ALTER TABLE games
ADD winnername VARCHAR(100) REFERENCES teams(name) ON DELETE SET NULL;

ALTER TABLE players
ADD salary INT;


ALTER TABLE teams
ADD INDEX name_index (name);

ALTER TABLE games
ADD FOREIGN KEY (team1name) REFERENCES teams(name) ON DELETE SET NULL;
ALTER TABLE games
ADD FOREIGN KEY (team2name) REFERENCES teams(name) ON DELETE SET NULL;

ALTER TABLE players
ADD FOREIGN KEY (agentid) REFERENCES agent(agentid) ON DELETE SET NULL;

ALTER TABLE teams
ADD FOREIGN KEY (coachid) REFERENCES coach(coachid) ON DELETE SET NULL;

ALTER TABLE players DROP CONSTRAINT agentid;

INSERT INTO players (playerid, name, jersey, agentid, teamid)
VALUES (1, 'Messi', 10, NULL, NULL),
(2, 'Pedri', 8, NULL, NULL),
(3, 'Ramos', 4, NULL, NULL),
(4, 'Varane', 5, NULL, NULL),
(5, 'Lewandowski', 9, NULL, NULL),
(6, 'De Bruyne', 17, NULL, NULL),
(7, 'Neymar', 10, NULL, NULL),
(8, 'Mbappe', 7, NULL, NULL),
(9, 'Benzema', 9, NULL, NULL),
(10, 'Hazard', 11, NULL, NULL);



INSERT INTO teams (teamid, name, coachid, champid)
VALUES (1, 'Barcelona', NULL, NULL),
(2, 'Real Madrid', NULL, NULL),
(3, 'Bayern Munich', NULL, NULL),
(4, 'Manchester City', NULL, NULL),
(5, 'PSG', NULL, NULL);

INSERT INTO agent (agentid, name) 
VALUES (1, 'Mino Raiola'),
(2, 'Jorge Mendes'),
(3, 'FAbrizio ROmano');

INSERT INTO coach (coachid, name, age)
VALUES (1, 'XAVI', 40),
(2, 'CONTE', 48),
(3, 'tuchel', 56),
(4, 'Pep Guardiola', 50),
(5, 'luis enrique', 49);

INSERT INTO Championship (champid, name, year)
VALUES (1, 'La Liga', 2024),
(2, 'Bundesliga', 2024),
(3, 'Premier League', 2024),
(4, 'Ligue 1', 2024);

INSERT INTO games (gameid, team1, team2, champid, date, motm, team1name, team2name, motmname, winnerid, winnername)
VALUES (1, 1, 2, 1, '2024-01-01', 1, 'Barcelona', 'Real Madrid', 'Messi', 1, 'Barcelona'),
(2, 3, 4, 2, '2024-01-01', 3, 'Bayern Munich', 'Manchester City', 'Ramos', 3, 'Bayern Munich'),
(3, 5, 1, 3, '2024-01-01', 5, 'PSG', 'Barcelona', 'Lewandowski', 5, 'PSG'),
(4, 2, 5, 4, '2024-01-01', 6, 'Real Madrid', 'PSG', 'De Bruyne', 2, 'Real Madrid');



UPDATE players
SET teamid = 1
WHERE playerid = 1;

UPDATE players
SET teamid = 1
WHERE playerid = 2;

UPDATE players
SET teamid = 2
WHERE playerid = 3;

UPDATE players
SET teamid = 2
WHERE playerid = 4;

UPDATE players
SET teamid = 3
WHERE playerid = 5;

UPDATE players
SET teamid = 4
WHERE playerid = 6;

UPDATE players
SET teamid = 3
WHERE playerid = 7;

UPDATE players
SET teamid = 4
WHERE playerid = 8;

UPDATE players
SET teamid = 2
WHERE playerid = 9;

UPDATE players
SET teamid = 4
WHERE playerid = 10;

UPDATE teams
SET coachid = 1
WHERE teamid = 1;

UPDATE teams
SET coachid = 2
WHERE teamid = 2;

UPDATE teams
SET coachid = 3
WHERE teamid = 3;

UPDATE teams
SET coachid = 4
WHERE teamid = 4;

UPDATE teams
SET coachid = 5
WHERE teamid = 5;

UPDATE teams
SET champid = 1
WHERE teamid = 1;

UPDATE teams
SET champid = 1
WHERE teamid = 2;

UPDATE teams
SET champid = 2
WHERE teamid = 3;

UPDATE teams
SET champid = 3
WHERE teamid = 4;

UPDATE teams
SET champid = 4
WHERE teamid = 5;

UPDATE games
SET team1 = 1
WHERE gameid = 1;

UPDATE games
SET team2 = 2
WHERE gameid = 1;

UPDATE games
SET team1 = 3
WHERE gameid = 2;

UPDATE games
SET team2 = 4
WHERE gameid = 2;

UPDATE games
SET team1 = 5
WHERE gameid = 3;

UPDATE games
SET team2 = 1
WHERE gameid = 3;

UPDATE games
SET team1 = 2
WHERE gameid = 4;

UPDATE games
SET team2 = 5
WHERE gameid = 4;

UPDATE games
SET motm = 1
WHERE gameid = 1;

UPDATE games
SET motm = 2
WHERE gameid = 2;

UPDATE games
SET motm = 3
WHERE gameid = 3;

UPDATE games
SET motm = 4
WHERE gameid = 4;

UPDATE games
SET team1name = 'Barcelona'
WHERE gameid = 1;

UPDATE games
SET team2name = 'Real Madrid'
WHERE gameid = 1;

UPDATE games
SET team1name = 'Bayern Munich'
WHERE gameid = 2;

UPDATE games
SET team2name = 'Manchester City'
WHERE gameid = 2;

UPDATE games
SET team1name = 'PSG'
WHERE gameid = 3;

UPDATE games
SET team2name = 'Barcelona'
WHERE gameid = 3;

UPDATE games
SET team1name = 'Real Madrid'
WHERE gameid = 4;

UPDATE games
SET team2name = 'PSG'
WHERE gameid = 4;

UPDATE games
SET motmname = 'Pedri'
WHERE gameid = 1;

UPDATE games
SET motmname = 'Messi'
WHERE gameid = 2;

UPDATE games
SET motmname = 'valverde'
WHERE gameid = 3;

UPDATE games

SET motmname = 'griezmann'
WHERE gameid = 4;



UPDATE games    
SET winnerid = 1
WHERE gameid = 1;

UPDATE games
SET winnerid = 3
WHERE gameid = 2;

UPDATE games
SET winnerid = 5
WHERE gameid = 3;

UPDATE games
SET winnerid = 2
WHERE gameid = 4;

UPDATE games
SET winnername = 'Barcelona'
WHERE gameid = 1;

UPDATE games
SET winnername = 'Bayern Munich'
WHERE gameid = 2;

UPDATE games
SET winnername = 'PSG'
WHERE gameid = 3;

UPDATE games
SET winnername = 'Real Madrid'
WHERE gameid = 4;

UPDATE players
SET salary = 1000000
WHERE playerid = 1;

UPDATE players
SET salary = 2000000
WHERE playerid = 2;

UPDATE players
SET salary = 3000000
WHERE playerid = 3;

UPDATE players
SET salary = 4000000
WHERE playerid = 4;

UPDATE players
SET salary = 5000000
WHERE playerid = 5;

UPDATE players
SET salary = 6000000
WHERE playerid = 6;

UPDATE players
SET salary = 7000000
WHERE playerid = 7;

UPDATE players
SET salary = 8000000
WHERE playerid = 8;

UPDATE players
SET salary = 9000000
WHERE playerid = 9;

UPDATE players
SET salary = 10000000
WHERE playerid = 10;

UPDATE players
SET agentid = 1
WHERE playerid = 1;

UPDATE players
SET agentid = 2
WHERE playerid = 2;

UPDATE players
SET agentid = 3
WHERE playerid = 3;

UPDATE players
SET agentid = 2
WHERE playerid = 4;

UPDATE players
SET agentid = 3
WHERE playerid = 5;

UPDATE players
SET agentid = 3
WHERE playerid = 6;

UPDATE players
SET agentid = 1
WHERE playerid = 7;

UPDATE players
SET agentid = 1
WHERE playerid = 8;

UPDATE players
SET agentid = 2
WHERE playerid = 9;

UPDATE players
SET agentid = 2
WHERE playerid = 10;



SELECT * FROM players;  
SELECT * FROM teams;
SELECT * FROM coach;
SELECT * FROM agent;
SELECT * FROM Championship;
SELECT * FROM games;

DELETE FROM players
WHERE playerid = 10;

SELECT * FROM players;  
SELECT * FROM teams;
SELECT * FROM coach;
SELECT * FROM agent;
SELECT * FROM Championship;
SELECT * FROM games;



SELECT teams.name, players.name
FROM teams
INNER JOIN players
ON teams.teamid = players.teamid;

UPDATE TABLE players SET agentid = NULL;