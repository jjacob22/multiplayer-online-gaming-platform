# confourREADME.MD
``` 

   _,.----.     _,.---._    .-._        .-._           ,----.    _,.----.  ,--.--------.             _,---.     _,.---._                             
 .' .' -   \  ,-.' , -  `. /==/ \  .-._/==/ \  .-._ ,-.--` , \ .' .' -   \/==/,  -   , -\         .-`.' ,  \  ,-.' , -  `.  .--.-. .-.-. .-.,.---.   
/==/  ,  ,-' /==/_,  ,  - \|==|, \/ /, /==|, \/ /, /==|-  _.-`/==/  ,  ,-'\==\.-.  - ,-./        /==/_  _.-' /==/_,  ,  - \/==/ -|/=/  |/==/  `   \  
|==|-   |  .|==|   .=.     |==|-  \|  ||==|-  \|  ||==|   `.-.|==|-   |  . `--`\==\- \          /==/-  '..-.|==|   .=.     |==| ,||=| -|==|-, .=., | 
|==|_   `-' \==|_ : ;=:  - |==| ,  | -||==| ,  | -/==/_ ,    /|==|_   `-' \     \==\_ \         |==|_ ,    /|==|_ : ;=:  - |==|- | =/  |==|   '='  / 
|==|   _  , |==| , '='     |==| -   _ ||==| -   _ |==|    .-' |==|   _  , |     |==|- |         |==|   .--' |==| , '='     |==|,  \/ - |==|- ,   .'  
\==\.       /\==\ -    ,_ /|==|  /\ , ||==|  /\ , |==|_  ,`-._\==\.       /     |==|, |         |==|-  |     \==\ -    ,_ /|==|-   ,   /==|_  . ,'.  
 `-.`.___.-'  '.='. -   .' /==/, | |- |/==/, | |- /==/ ,     / `-.`.___.-'      /==/ -/         /==/   \      '.='. -   .' /==/ , _  .'/==/  /\ ,  ) 
                `--`--''   `--`./  `--``--`./  `--`--`-----``                   `--`--`         `--`---'        `--`--''   `--`..---'  `--`-`--`--'  

```
## Overview
Connect 4 is a two player game where the players take turns dropping colored discs into a vertical grid.
## Rules
### 1. Setup
1. Empty 7x6 board is generated.
2. Random player starts. 
3. Color of pieces are typically Red and Yellow.
### 2. Movement/GameLoop
1. Players will take turns placing pieces in columns.
2. Pieces will drop to the lowest available row.
3. Pieces that collide will stack above each other.
### 3. End of Game
1. If a player connects four pieces of their color, in a horizontal, vertical or diagonal line they win.
2. If the board fills and no player has connected four pieces game is considered a draw.
3. A player resigns from the game granting the victory to the opponent.
## Contributors
See `team.md` for full list of contributors.

