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

	public WaitingPlayersFrame(String color) 
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
		
		switch(color) {
		case "vermelho":
			this.waitingPlayersPanel.setBackground(new Color(220,20,60));
			break;
		case "verde":
			this.waitingPlayersPanel.setBackground(new Color (60,179,113));
			break;
		case "amarelo":
			this.waitingPlayersPanel.setBackground(new Color(255,215,0));
			break;
		case "azul":
			this.waitingPlayersPanel.setBackground(new Color(100,149,237));
			break;
		}
		
		this.name = ConnectController.getInstance().getNickname();
	
		this.label = new JLabel("Jogador(a) " + this.name + ", seu time é " + color + ". Aguardando adversários...");		
		this.label.setLocation(0, this.dimension.height / 5);
		this.label.setSize(this.dimension.width, 40);
		this.label.setOpaque(false);
		this.label.setFont(new Font("Helvetica", 0, 30));
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
