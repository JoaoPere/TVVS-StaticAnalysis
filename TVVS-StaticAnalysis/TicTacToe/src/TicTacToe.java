public class TicTacToe
{	
	private String name = "Tic Tac Toe";
	
    public static void main( String[] args )
    {
        TicTacToeView view = new TicTacToeView();
        TicTacToeModel model = new TicTacToeModel();
        @SuppressWarnings("unused")
        TicTacToeController controller = new TicTacToeController( view, model );
        view.setVisible( true );
        String hard = "Hard";
        String medium = "Medium";
        String easy = "Easy";
    }
}