package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import controllers.ConnectController;
import controllers.GameFacade;
import controllers.Main;
import interfaces.ObservadoIF;
import interfaces.ObservadorIF;

@SuppressWarnings("serial")
public class ButtonsPanel extends JPanel implements ObservadorIF {

	private static ButtonsPanel instance = null;
	private int ponto;
	private int x0 = 15 * ponto;
	private JButton novoJogo = new JButton("Novo Jogo");
	private JButton carregar = new JButton("Carregar Jogo");
	private JButton salvar = new JButton("Salvar");
	private JButton lancarDado = new JButton("Lançar Dado");
	private JLabel jogadorAtual = new JLabel();
	private JLabel dice = new JLabel();
	private JLabel estadoAtual = new JLabel();

	private ButtonsPanel(){
		this.ponto = Main.ponto;
		GameFacade.getInstance().add(this);
		SetButtons();
		SetLabels();
		SetDice();
		SetCurrentPlayerLabel();
		StartListeners();
		SetConfigurations();		
	}

	public static ButtonsPanel GetButtonsPanel(){
		if(instance == null)
			instance = new ButtonsPanel();
		return instance;
	}

	private URL GetDiceImageByNumber(int n) {
		switch(n) {
		case 1:	
			return getClass().getResource("/images/1.png");
		case 2:	
			return getClass().getResource("/images/2.png");
		case 3:	
			return getClass().getResource("/images/3.png");
		case 4:	
			return getClass().getResource("/images/4.png");
		case 5:	
			return getClass().getResource("/images/5.png");
		case 6:	
			return getClass().getResource("/images/6.png");
		case 7:	
			return getClass().getResource("/images/6.png");
		}
		return null;
	}

	private void SetButtons(){
		int i = 0;
		for(JButton jb : new JButton []{ novoJogo, carregar, salvar}) {
			jb.setSize(4 * ponto, ponto);
			jb.setLocation(x0 + ponto / 2, i + ponto);
			this.add(jb);
			jb.setEnabled(false);
			i += 3 * ponto / 2;
		}	
		lancarDado.setSize(4 * ponto, ponto);
		lancarDado.setLocation(x0 + ponto / 2, 19 * ponto / 2);
		this.add(lancarDado);
		if(ConnectController.getInstance().getMyTeam().equals(GameFacade.getInstance().GetCurrentPlayerText().toLowerCase())) {
			lancarDado.setEnabled(true);
		} else {
			lancarDado.setEnabled(false);
		}
	}

	private void SetLabels() {
		JLabel jl = new JLabel("À Jogar:", SwingConstants.CENTER);
		jl.setFont(new Font("Courier New",Font.BOLD,20) );
		jl.setSize(4 * ponto, ponto);
		jl.setLocation(x0 + ponto / 2, 11 * ponto / 2);
		this.add(jl);

		estadoAtual.setHorizontalAlignment(JLabel.CENTER);
		estadoAtual.setVerticalAlignment(JLabel.TOP);
		estadoAtual.setFont(new Font("Courier New",Font.PLAIN,15) );
		estadoAtual.setSize(5 * ponto, 3 * ponto);
		estadoAtual.setLocation(x0, 22 * ponto / 2);
		this.add(estadoAtual);
	}

	private void SetDice() {
		dice.setLocation(x0 + 2 * ponto, 16 * ponto / 2);
		this.add(dice);	
	}

	private void SetCurrentPlayerLabel() {
		jogadorAtual = new JLabel();
		jogadorAtual.setHorizontalAlignment(JLabel.CENTER);
		jogadorAtual.setFont(new Font("Courier New",Font.BOLD,20) );
		jogadorAtual.setSize(4 * ponto, ponto);
		jogadorAtual.setLocation(x0 + ponto / 2, 13 * ponto / 2);
		this.add(jogadorAtual);
	}

	private void StartListeners() {

		lancarDado.addActionListener( new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Thread t = new Thread(new Runnable() { 
					public void run() { 			
						GameFacade.getInstance().StartNewRound();
						ConnectController.getInstance().sendGameToServer();
						GameFacade.getInstance().RollDice();	
						ConnectController.getInstance().sendGameToServer();
						GameFacade.getInstance().MovePiece();	
						ConnectController.getInstance().sendGameToServer();
					}});
				t.start();
			}

		});
	}

	private void SetConfigurations() {
		this.setBackground(Color.lightGray);
		this.setLayout(null);
		this.setSize(5 * ponto, 15 * ponto);
		this.setLocation(15 * ponto, 0);
		this.setVisible(true);
	}

	private void RefreshDice(int diceValue) {
		if(diceValue <= 7 && diceValue > 0) {
			dice.setIcon(new ImageIcon(GetDiceImageByNumber(diceValue)));
			try {
				Thread.sleep(90);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} else {
			dice.setIcon(new ImageIcon());
		}
		dice.setSize(dice.getIcon().getIconWidth(), dice.getIcon().getIconHeight());
	}

	// OBSERVADOR DE JOGOFACADE
	@Override
	public void notify(ObservadoIF observado) {
		int diceValue = (int) observado.get(1);
		RefreshDice(diceValue);

		Color foreground = (Color) observado.get(2);
		jogadorAtual.setForeground(foreground);

		String currentPlayerText = (String) observado.get(3);
		jogadorAtual.setText(currentPlayerText);

		String currentStateText = (String) observado.get(4);
		estadoAtual.setText(currentStateText);

		SetButtons();

		revalidate();
		repaint();
	}
}