public class TicTacToeModel
{
    private char      playerToMove;                // who's move is it?
    private boolean   computerIsOpponent;          // is opponent the computer?
    private boolean   computerIsDifficult;         // easy[0], hard[1]
    private char[][]  gameBoard = new char[3][3];  // game board
    private int       moveCount;                   // counts the total moves played in game
    private boolean   gameIsComplete;              // monitor game completion state
    private char      gameWinner;                  // who won the game?
    private WinPath   winPath = new WinPath();     // how was the 3 in a row made?

    // Class WinPath
    // A win path is the path on the game board followed to make 3 in a row.
    public class WinPath
    {
        private int startRow;  // path starts in row
        private int startCol;  // path starts in column
        private int endRow;    // path ends in row
        private int endCol;    // path ends in column

        public int getStartRow() { return startRow; }
        public int getStartCol() { return startCol; }
        public int getEndRow()   { return endRow; }
        public int getEndCol()   { return endCol; }
        public void setPath( int startRow, int startCol, int endRow, int endCol )
        {
            this.startRow = startRow;
            this.startCol = startCol;
            this.endRow = endRow;
            this.endCol = endCol;
        }
    } // end class WinPath

    //--------------------------//
    //    GETTERS & SETTERS     //
    //--------------------------//

    public char[][] getGameBoard()        { return gameBoard; }
    public boolean  computerIsOpponent()  { return computerIsOpponent; }
    public boolean  computerIsDifficult() { return computerIsDifficult; }
    public char     getPlayerToMove()     { return playerToMove; }
    public boolean  gameIsComplete()      { return gameIsComplete; }
    public char     getGameWinner()       { return gameWinner; }
    public WinPath  getWinPath()          { return winPath; }

    public void setComputerIsOpponent ( boolean trueForComputerMode )
    {
        computerIsOpponent = trueForComputerMode;
    }

    public void setComputerIsDifficult ( boolean trueForDifficult )
    {
        computerIsDifficult = trueForDifficult;
    }

    //-------------------//
    //    GAME SETUP     //
    //-------------------//

    // Tic Tac Toe model constructor
    // Sets up the default game state to 2 player
    public TicTacToeModel()
    {
        startNewGame( false );
    }

    // Starts a new game with the current opponent mode.
    public void startNewGame()
    {
        playerToMove = 'x';  // x always first
        moveCount = 0;
        gameWinner = ' ';
        gameIsComplete = false;
        resetGameBoard();
    }

    // Sets the opponent mode and starts a new game.
    public void startNewGame( boolean trueForComputerMode )
    {
        computerIsOpponent = trueForComputerMode;
        startNewGame();
    }

    // Sets the game board to its default empty state.
    private void resetGameBoard()
    {
        for ( int i = 0; i < 3; i++ ) {
            for ( int j = 0; j < 3; j++ ) {
                gameBoard[i][j] = ' ';
            }
        }
    }

    //------------------------//
    //    BASIC GAME PLAY     //
    //------------------------//

    // Returns true if a square is already played.
    public boolean squareHasBeenPlayed( int row, int col )
    {
        return gameBoard[row][col] != 'x' && gameBoard[row][col] != 'o' ? false : true;
    }

    // Makes a move in a game board square.
    public void makeMoveInSquare( int row, int col )
    {
        gameBoard[row][col] = playerToMove;           // make the move in the game board model

        if ( playerToMove == 'x' )                    // update the player to move
            playerToMove = 'o';
        else if ( playerToMove == 'o' )
            playerToMove = 'x';

        performWinCheck();                            // check for a win
        ++moveCount;                                  // count the move          
        if ( moveCount == 9 ) gameIsComplete = true;  // set the game to complete if # of moves is 9
    }

    // Sets gameIsComplete to true if winning sequence is found.
    public void performWinCheck()
    {
        if ( rowWins() || colWins() || diagWins() ) gameIsComplete = true;
    }

    // Returns true if a winning row is found.
    // Sets the winner and the win path.
    private boolean rowWins()
    {
        for ( int i = 0; i < 3; i++ ) {
            int xCount = 0, oCount = 0;
            for ( int j = 0; j < 3; j++ ) {
                if ( gameBoard[i][j] == 'x' ) ++xCount;
                if ( gameBoard[i][j] == 'o' ) ++oCount;
            }
            if ( xCount == 3 || oCount == 3 ) {
                if ( xCount == 3 ) gameWinner = 'x';
                if ( oCount == 3 ) gameWinner = 'o';
                winPath.setPath( i, 0, i, 2);
                return true;
            }
        }
        return false;
    } // end rowWins()

    // Returns true if a winning column is found.
    // Sets the winner and the win path.
    private boolean colWins()
    {
        for ( int i = 0; i < 3; i++ ) {
            int xCount = 0, oCount = 0;
            for ( int j = 0; j < 3; j++ ) {
                if ( gameBoard[j][i] == 'x' ) ++xCount;
                if ( gameBoard[j][i] == 'o' ) ++oCount;
            }
            if ( xCount == 3 || oCount == 3 ) {
                if ( xCount == 3 ) gameWinner = 'x';
                if ( oCount == 3 ) gameWinner = 'o';
                winPath.setPath( 0, i, 2, i);
                return true;
            }
        }
        return false;
    } // end colWins()

    // Returns true if a winning diagonal is found.
    // Sets the winner and the win path.
    private boolean diagWins()
    {
        if ( gameBoard[0][0] == 'x' && gameBoard[1][1] == 'x' && gameBoard[2][2] == 'x' ) {
            gameWinner = 'x';
            winPath.setPath( 0, 0, 2, 2 );
            return true;
        } else if  ( gameBoard[2][0] == 'x' && gameBoard[1][1] == 'x' && gameBoard[0][2] == 'x' ) {
            gameWinner = 'x';
            winPath.setPath( 0, 2, 2, 0 );
            return true;
        } else if  ( gameBoard[0][0] == 'o' && gameBoard[1][1] == 'o' && gameBoard[2][2] == 'o' ) {
            gameWinner = 'o';
            winPath.setPath( 0, 0, 2, 2 );
            return true;
        } else if  ( gameBoard[2][0] == 'o' && gameBoard[1][1] == 'o' && gameBoard[0][2] == 'o' ) {
            gameWinner = 'o';
            winPath.setPath( 0, 2, 2, 0 );
            return true;
        } else {
            return false;
        }
    } // end diagWins()
}  // end class TicTacToeModel
