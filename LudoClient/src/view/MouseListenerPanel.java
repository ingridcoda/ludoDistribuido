package view;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import controllers.GameFacade;

public class MouseListenerPanel	implements MouseListener {

	private static MouseListenerPanel instance = null;
	private GameFacade jogof;

	public MouseListenerPanel(){
		jogof = GameFacade.getInstance();
	}

	public static MouseListenerPanel GetMouseListener(){
		if(instance == null)
			instance = new MouseListenerPanel();
		return instance;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		jogof.MouseClicked(e);
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

}