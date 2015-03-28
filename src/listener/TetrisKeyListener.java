/**
 * 
 */
package listener;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import controller.TetrisController;

/**
 * @Shaun Rain
 * 2014
 */
public class TetrisKeyListener implements KeyListener {
	private TetrisController controller;
	
	public TetrisKeyListener(TetrisController controller) {
		this.controller = controller;
	}
	
	public void keyPressed(KeyEvent e) {

		controller.gameKeyControls(e.getKeyCode());
		
	}


	public void keyReleased(KeyEvent e) {
		// TODO �Զ����ɵķ������
		
	}

	public void keyTyped(KeyEvent e) {
		// TODO �Զ����ɵķ������
		
	}

}
