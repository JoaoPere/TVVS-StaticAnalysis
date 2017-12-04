//
//  File: TicTacToeController.java
//  Auth: Brian W. Howell
//  Date: 20 January 2014
//  Desc: Coordinates communication between the UI (view) and the model.
//

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton; // for casting as JButton

public class TicTacToeController
{
    //--------------------------------------------------------------------//
    //    COMPONENT LABELS                                                //   
    //    The controller is responsible for updating the views labels.    //
    //    Customization of the update messages can be done here.          // 
    //--------------------------------------------------------------------//

    private final String  STATUS_START        = "X moves to start the game";
    private final String  STATUS_CATS         = "Cat's game";
    private final String  STATUS_X_WINS       = "X wins the game!";
    private final String  STATUS_O_WINS       = "O wins the game!";
    private final String  STATUS_X_MOVES      = "X to move";
    private final String  STATUS_O_MOVES      = "O to move";
    private final String  STATUS_CP_MOVES     = "Computer is thinking...";

    //------------------------------//
    //    TIC-TAC-TOE CONTROLLER    //
    //------------------------------//

    private TicTacToeView  view;
    private TicTacToeModel model;

    // Tic Tac Toe controller constructor.
    // Provides controller with access to the model & view and adds view event listeners. 
    public TicTacToeController( TicTacToeView view, TicTacToeModel model )
    {
        this.view = view;  
        this.model = model;

        this.view.addGameBoardSquareButtonListener( new SquareListener() );
        this.view.addGameBoardSquareButtonHoverListener( new SquareHoverListener() );
        this.view.addNewGameButtonListener( new NewGameListener() );
    }

    //-----------------//
    //    LISTENERS    //
    //-----------------//

    // Class SquareListener.
    // Handles game board square clicks from the view.
    private class SquareListener implements ActionListener
    {
        // Used to prevent user moves while computer moves are in progress.
        private boolean blockMove = false;

        @Override // A Square has been clicked
        public void actionPerformed( ActionEvent e )
        {
            if ( !blockMove ) {
                String gameStatus;  // string will store game status to update view label.
                JButton square = (JButton) e.getSource();
                int row = (int) square.getClientProperty("row");  // store square identifiers
                int col = (int) square.getClientProperty("col");  // to pass to model

                // Prevent square interaction if game is complete or square has been played.
                if ( model.gameIsComplete() ) return;
                if ( model.squareHasBeenPlayed( row, col )) return;
            
                // Tell model to make the move since the square is empty.
                model.makeMoveInSquare( row, col );

                // Ask model who moves next so we can update the view's game status label.
                // This code block is only accessible when the user plays, so if we are in 
                // computer mode we know the computer moves next.
                if ( model.computerIsOpponent() ) {
                    gameStatus = STATUS_CP_MOVES;
                } else if ( model.getPlayerToMove() == 'x' ) {
                    gameStatus = STATUS_X_MOVES;
                } else {
                    gameStatus = STATUS_O_MOVES;
                }

                // Ask model if game is complete so we can update the game status for that scenario.
                if ( model.gameIsComplete() ) {
                    if ( model.getGameWinner() == ' ' ) gameStatus = STATUS_CATS;
                    if ( model.getGameWinner() == 'x' ) gameStatus = STATUS_X_WINS;
                    if ( model.getGameWinner() == 'o' ) gameStatus = STATUS_O_WINS;
                }

                // Update the view UI to display results of the move.
                view.updateGameStatusLabelText( gameStatus );
                view.updateGameBoardUI( model.getGameBoard() );
                performWinLineUpdates();

            } // end if (!blockMove)
        } // end SquareListener actionPerformed
 

        // Asks view to update it's winner line if the game has been won.
        private void performWinLineUpdates()
        {
            if ( model.gameIsComplete() && model.getGameWinner() != ' ' ) {
                int row1 = model.getWinPath().getStartRow();
                int col1 = model.getWinPath().getStartCol();
                int row2 = model.getWinPath().getEndRow();
                int col2 = model.getWinPath().getEndCol();
                view.updateWinnerLine( row1, col1, row2, col2 );
            }
        }

    } // end class SquareListener

    // Class SquareHoverListener
    // Controls game board square hover state. Hover states are prevented if 
    // the game is not in progress or a square has already been played.
    private class SquareHoverListener extends MouseAdapter {
        
        @Override 
        public void mouseEntered( MouseEvent e ) {
            JButton square = (JButton) e.getSource();
            int row = (int) square.getClientProperty("row");
            int col = (int) square.getClientProperty("col");
            if ( !model.gameIsComplete() && !model.squareHasBeenPlayed(row, col) ) {
                view.updateSquareUIForHoverState( row, col );
            }
        }

        @Override
        public void mouseExited( MouseEvent e ) {
            JButton square = (JButton) e.getSource();
            int row = (int) square.getClientProperty("row");
            int col = (int) square.getClientProperty("col");
            view.updateSquareUIForNormalState( row, col );
        }

    } // end class SquareHoverListener

    // Class NewGameListener.
    // Handles clicks of the Start New Game button.
    class NewGameListener implements ActionListener
    {
        @Override
        public void actionPerformed( ActionEvent e )
        {
            model.startNewGame();                            // reset model
            view.resetWinnerLine();                          // resets (hides) view winner line
            view.updateGameBoardUI( model.getGameBoard() );  // reset view gameboard
            view.updateGameStatusLabelText( STATUS_START );  // reset view game status label
        }

    } // end class NewGameButtonListener

    // Class OpponentModeListener.
    // Handles clicks of the Change Mode button.
    class OpponentModeListener implements ActionListener
    {
        @Override
        public void actionPerformed( ActionEvent e )
        {
            // Switch from 2 player to easy computer.
            if ( !model.computerIsOpponent() ) {
                model.setComputerIsOpponent( true );
                model.setComputerIsDifficult( false );
            } 
            // Switch from easy to hard computer.
            else if ( model.computerIsOpponent() && !model.computerIsDifficult() ) {
                model.setComputerIsDifficult( true );
            }
            // Switch back to 2 player.
            else
            {
                model.setComputerIsOpponent( false );
            }
            // Restart the game
            model.startNewGame();
            view.resetWinnerLine();
            view.updateGameBoardUI( model.getGameBoard() );
            view.updateGameStatusLabelText( STATUS_START );
        }

    } // end class NewGameButtonListener

}  // end class TicTacToeController
