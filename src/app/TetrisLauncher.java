
package app;

import BGM.PlayMusic;
import model.TetrisBoard;
import controller.TetrisController;
import view.TetrisView;

/**
 * @Shaun Rain
 * 2014
 */
public class TetrisLauncher {
	public static void main(String[] args) {
		TetrisView view = new TetrisView();
		TetrisController controller = new TetrisController();
		TetrisBoard board = new TetrisBoard(20,10);
		PlayMusic pm = new PlayMusic("src/CLANNAD.wav");
		
		view.setController(controller);
		
		controller.setView(view);
		controller.setBoard(board);
		
		controller.update();
		view.start();
		
		while(true){
			if(pm.isAlive()==false){
				pm = new PlayMusic("src/CLANNAD.wav");
				pm.start();
			}
		}
		
	}

}
