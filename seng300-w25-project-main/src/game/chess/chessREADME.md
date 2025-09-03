# chessREADME.MD
``` 

          _____                    _____                    _____                    _____                    _____          
         /\    \                  /\    \                  /\    \                  /\    \                  /\    \         
        /::\    \                /::\____\                /::\    \                /::\    \                /::\    \        
       /::::\    \              /:::/    /               /::::\    \              /::::\    \              /::::\    \       
      /::::::\    \            /:::/    /               /::::::\    \            /::::::\    \            /::::::\    \      
     /:::/\:::\    \          /:::/    /               /:::/\:::\    \          /:::/\:::\    \          /:::/\:::\    \     
    /:::/  \:::\    \        /:::/____/               /:::/__\:::\    \        /:::/__\:::\    \        /:::/__\:::\    \    
   /:::/    \:::\    \      /::::\    \              /::::\   \:::\    \       \:::\   \:::\    \       \:::\   \:::\    \   
  /:::/    / \:::\    \    /::::::\    \   _____    /::::::\   \:::\    \    ___\:::\   \:::\    \    ___\:::\   \:::\    \  
 /:::/    /   \:::\    \  /:::/\:::\    \ /\    \  /:::/\:::\   \:::\    \  /\   \:::\   \:::\    \  /\   \:::\   \:::\    \ 
/:::/____/     \:::\____\/:::/  \:::\    /::\____\/:::/__\:::\   \:::\____\/::\   \:::\   \:::\____\/::\   \:::\   \:::\____\
\:::\    \      \::/    /\::/    \:::\  /:::/    /\:::\   \:::\   \::/    /\:::\   \:::\   \::/    /\:::\   \:::\   \::/    /
 \:::\    \      \/____/  \/____/ \:::\/:::/    /  \:::\   \:::\   \/____/  \:::\   \:::\   \/____/  \:::\   \:::\   \/____/ 
  \:::\    \                       \::::::/    /    \:::\   \:::\    \       \:::\   \:::\    \       \:::\   \:::\    \     
   \:::\    \                       \::::/    /      \:::\   \:::\____\       \:::\   \:::\____\       \:::\   \:::\____\    
    \:::\    \                      /:::/    /        \:::\   \::/    /        \:::\  /:::/    /        \:::\  /:::/    /    
     \:::\    \                    /:::/    /          \:::\   \/____/          \:::\/:::/    /          \:::\/:::/    /     
      \:::\    \                  /:::/    /            \:::\    \               \::::::/    /            \::::::/    /      
       \:::\____\                /:::/    /              \:::\____\               \::::/    /              \::::/    /       
        \::/    /                \::/    /                \::/    /                \::/    /                \::/    /        
         \/____/                  \/____/                  \/____/                  \/____/                  \/____/         
                                                                                                                             
```
## Overview
This is the Readme for the game of Chess, it includes the rules, general program layout and major contacts for developers.
Chess is a two player strategy game with the objective of putting your opponent in `chekmate`.
## Rules
### 1. Setup
1. ♜ | Rooks are placed on the outside corners, right and left edge.
2. ♞ | Knights are placed immediately inside the rooks.
3. ♝ |Bishops are placed immediately inside the knights.
4. ♛ |The queen is placed on the central square of the same color of that of the piece: white queen on the white square and black queen on the black square.
5. ♚ | The king takes the vacant spot next to the queen.
6. ♟ | Pawns are placed one square in front of all the other pieces
7. White Player Starts (Color of player assignment TBD)
```
8 |♜|♞|♝|♛|♚|♝|♞|♜| #: Represents Black Tile 
7 |♟|♟|♟|♟|♟|♟|♟|♟| _: Represents White Tile
6                        Filled in Pieces are conisdered Black
5                        Clear Pieces are considered White   
4                       
3 
2 |♙|♙|♙|♙|♙|♙|♙|♙|
1 |♖|♘|♗|♕|♔|♗|♘|♖|
   a  b  c d  e f  g  h 
```

### 2. Movement/GameLoop
Players take turns move 1 piece.
For movement to happen the piece needs to be un obstructed.
If a Piece collides with another it is considered the piece that started the collision "Takes" the collided piece.
1. ♜ | Rooks move any number of tiles vertically or horizontally. Rooks are also involved in the special move `castling`.
2. ♞ | Knights move in an L shape pattern and can hop over other pieces.
3. ♝ | Bishops move any number of tiles diagonally.
4. ♛ | Queen moves any number of tiles diagonally, horizontally or vertically. 
5. ♚ | The king moves exactly one tile adjacent to it. The King also involved in the special move `castling`
6. ♟ | Pawns can move 2 tiles forward on the first turn, then they can only move 1 tile forward every turn. Pawns can only attack on diagonals. Pawns are also involved in 2 special moves `en passant` and `promotion`.
#### Advanced Movement
#### Castling:
Castling Consists of moving the king two squares towards a rook, then placing the rook on the other side of the king adjacent to it. Castling can only be done if
1. The king and rook involved in castling must not have moved.
2. There must be no pieces between the king and the rook.
3. The king cannot be under fire.
4. The castling rook must be on the same rank as the king.

#### Promotion
If a player advances a pawn to the eighth rank, the pawn is promoted(converted) to a queen, rook, bishop, or knight.

#### En Passant
When a pawn advances two squares on its initial move and ends the turn adjacent to an enemy pawn on the same rank, it may be captured en passant by the enemy pawn as if it had moved only one square. This capture is legal only on the move immediately following the pawn's advance. The diagrams demonstrate an instance of this: if the white pawn moves from a2 to a4, the black pawn on b4 can capture it en passant, moving from b4 to a3, and the white pawn on a4 is removed from the board.


### 3. End of Game
####  Check 
   1. If the king is under fire (In the range of another piece's movement) the king is considered in check
   2. Check has to be resolved before any other piece can be moved (Block the line of sight to king or move king)
####  Checkmate 
If a players king is placed in `check` and no legal move able to be made, that player is considered to be in `checkmate`. The checkmated player loses and the game ends.

#### Resignation
Players may resign at any time thus ending the game.

#### Draws
The game ends in a draw if any of these conditions occur:
1. The player to move is not in check and has no legal move. This is considered a stalemate.
2. The game reaches a dead position (No player has the pieces to successfully checkmate the other).
3. Both players agree to a draw.

### Contributors
See `\team.md` for full list of contributors.