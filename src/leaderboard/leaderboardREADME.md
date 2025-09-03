# Leaderboard
The leaderboard package is intended to interact with the database to 
retrieve and update player statistics for each game, and information about match results. The client then can interact
with the leaderboard classes to retrieve statistics and relevant
information for matchmaking or leaderboard display purposes (ELO ranking, games won, etc.).

## File Descriptions
This section lists all the files included in the package and provides information for each file such
as the file name, location, as well as the purpose of each file.

### ELOCalculator
- Filename: ELOCalculator.java <br>
- Location: `seng300-project/src/leaderboard`  <br>
- Description: Calculates the new ELO rankings of players after their match. Uses different K-factors for each game.

### GameStatistics
- Filename: GameStatistics.java <br>
- Location: `seng300-project/src/leaderboard`  <br>
- Description: Acts as a class to retrieve statistics for a player from the database, and set a players ELO ranking if needed.

### Leaderboard
- Filename: Leaderboard.java <br>
- Location: `seng300-project/src/leaderboard`  <br>
- Description: Sorts the leaderboard statistics to get the top players, or top among friends. 

### MatchInfo
- Filename: MatchInfo.java <br>
- Location: `seng300-project/src/leaderboard`  <br>
- Description: Retrieves from the database information about a match for players and a specific game.

## References

| Package Name        | Purpose                                   |
|---------------------|-------------------------------------------|
| `java.util`         | Collections, random numbers, etc.         |


## Contributors
- Martin Webster    (martin.webster@ucalgary.ca)      
- Vanja Salkauskas    (vanja.salkauskas@ucalgary.ca)         
- Cole Runions-Kahler (cole.runionskahler@ucalgary.ca)  