package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import controllers.ConnectController;

@SuppressWarnings("serial")
public class WaitingPlayersFrame extends JFrame {

	private Toolkit tk;

	private Dimension dimension;
	private JPanel waitingPlayersPanel;
	private JLabel label;
	private String name;

	public WaitingPlayersFrame() 
	{
		this.tk = Toolkit.getDefaultToolkit();
		Dimension dim = tk.getScreenSize();
		int width = dim.width;
		int height = dim.height;
		this.dimension = new Dimension(width / 2, height / 2);

		this.setSize(this.dimension);

		this.waitingPlayersPanel = new JPanel();
		this.waitingPlayersPanel.setLayout(null);
		this.waitingPlayersPanel.setSize(this.dimension);
		this.waitingPlayersPanel.setBackground(Color.WHITE);

		this.name = ConnectController.getInstance().getNickname();

		String myTeam = "";
		if(ConnectController.getInstance().getMyTeam() != null) myTeam = ConnectController.getInstance().getMyTeam();		
		this.label = new JLabel("Jogador(a) " + this.name + ", seu time é " + myTeam + ". Aguardando adversários...");		
		this.label.setLocation(0, this.dimension.height / 5);
		this.label.setSize(this.dimension.width, 40);
		this.label.setOpaque(false);
		this.label.setFont(new Font("Helvetica", 0, 16));
		this.label.setHorizontalAlignment(SwingConstants.CENTER);
		this.waitingPlayersPanel.add(this.label);

		this.getContentPane().add(this.waitingPlayersPanel);

		int x = width / 4;
		int y = height / 4;

		this.setLocation(x, y);		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
	}
}
