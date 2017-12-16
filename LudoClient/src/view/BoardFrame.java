package view;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Toolkit;

import javax.swing.JFrame;

import controllers.Main;

public class BoardFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private static BoardFrame instance;
	private int altura;
	private int largura;
	private Graphics2D g;
	public BoardPanel boardPanel;
	public ButtonsPanel buttonsPanel;

	private BoardFrame(){
		SetPanelTabuleiro();
		SetPanelBotoes();
		SetConfigurations();
	}

	public static BoardFrame getInstance() {
		if(instance == null) {
			instance = new BoardFrame();
		}
		return instance;
	}

	private void SetPanelTabuleiro() {
		boardPanel = BoardPanel.GetBoardPanel();
		boardPanel.paintComponents(g);
		this.getContentPane().add(boardPanel);
	}

	private void SetPanelBotoes(){
		buttonsPanel = ButtonsPanel.GetButtonsPanel();
		this.getContentPane().add(buttonsPanel);
	}

	private void SetConfigurations() {
		SetDefautSizes();
		SetBoundsAndLayout();		
		SetTitleAndSize();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}

	private void SetDefautSizes(){
		this.altura = Main.altura;
		this.largura = Main.largura;
	}

	private void SetBoundsAndLayout(){
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension screenSize = tk.getScreenSize();
		int sl = screenSize.width, sa = screenSize.height;
		int x = sl / 2 - largura / 2, y = sa / 2 - altura / 2;
		this.setBounds(x, y, largura, altura);
		this.setLayout(null);
	}

	private void SetTitleAndSize(){
		this.setSize(largura, altura);
		this.setTitle("Ludo");
		this.setResizable(false);
	}

}
