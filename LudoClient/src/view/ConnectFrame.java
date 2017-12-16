package view;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;

@SuppressWarnings("serial")
public class ConnectFrame extends JFrame {
	private Toolkit tk;

	private Dimension dimension;

	private ConnectPanel connectPanel;

	public ConnectFrame(String title) {
		this.tk = Toolkit.getDefaultToolkit();
		Dimension dim = tk.getScreenSize();
		int width = dim.width;
		int height = dim.height;
		this.dimension = new Dimension(width / 2, height / 2);

		this.connectPanel = new ConnectPanel(this.dimension);

		this.setSize(this.dimension);
		this.getContentPane().add(this.connectPanel);

		int x = width / 4;
		int y = height / 4;

		this.setLocation(x, y);		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setTitle(title);
	}

}