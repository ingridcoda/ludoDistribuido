package controllers;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JOptionPane;

import domain.Game;
import domain.GameSaver;
import domain.Pieces;
import interfaces.ObservadoIF;
import interfaces.ObservadorIF;
import view.BoardFrame;
import view.ConnectFrame;
import view.WaitingPlayersFrame;

public class GameFacade extends Observable implements Observer, ObservadoIF, ObservadorIF {

	private static GameFacade instance = null;
	Game game;
	GameSaver gameSaver;
	Pieces pieces;
	private List<ObservadorIF> lst = new ArrayList<ObservadorIF>();
	boolean rollDiceState;
	private BoardFrame gameWindow = null;
	private ConnectFrame connectWindow = null;
	private WaitingPlayersFrame waitingPlayersWindow = null;	

	private GameFacade(){
		game = Game.getInstance();
		gameSaver = GameSaver.GetGameSaver();
		pieces = Pieces.GetPieces();
		game.add(this);
		pieces.add(this);
	}

	public static GameFacade getInstance(){
		if(instance == null) {
			instance = new GameFacade();
		}
		return instance;
	}	

	public void StartNewRound(){
		SetRollDiceState();
		game.StartNewRound();
		ListIterator<ObservadorIF> li = lst.listIterator();
		while(li.hasNext()) {
			li.next().notify(this);
		}
		ConnectController.getInstance().sendGameToServer();
	}

	public void SetRollDiceState() {
		String text = "";
		switch(game.GetCurrentPlayer()) {
		case 0:
			text = "vermelho";
			break;
		case 1:
			text = "verde";
			break;
		case 2:
			text = "amarelo";
			break;
		case 3:
			text = "azul";
			break;
		}		
		rollDiceState = (text.equals(ConnectController.getInstance().getMyTeam()));
		ListIterator<ObservadorIF> li = lst.listIterator();
		while(li.hasNext()) {
			li.next().notify(this);
		}
	}

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	public Pieces getPieces() {
		return pieces;
	}

	public void setPieces(Pieces pieces) {
		this.pieces = pieces;
	}

	public void StartGame(){
		SetRollDiceState();
		game.StartGame();
	}

	public void RollDice(){
		game.RollDice();
		ListIterator<ObservadorIF> li = lst.listIterator();
		while(li.hasNext()) {
			li.next().notify(this);
		}
		ConnectController.getInstance().sendGameToServer();
	}

	public void MovePiece(){
		game.MovePiece();
		ListIterator<ObservadorIF> li = lst.listIterator();
		while(li.hasNext()) {
			li.next().notify(this);
		}
		ConnectController.getInstance().sendGameToServer();
	}

	public int GetDiceValue(){
		return game.GetDiceValue();
	}

	public void MouseClicked(MouseEvent e){
		if(ConnectController.getInstance().getMyTeam().equals(this.GetCurrentPlayerText().toLowerCase())) {
			game.MouseClicked(e);
			ConnectController.getInstance().sendGameToServer();
		}
	}

	public void connectPlayer()	{
		this.connectWindow = new ConnectFrame("Conectar ao servidor do Ludo");
		this.connectWindow.setVisible(true);
	}

	public void waitingPlayers(String color) {
		if(this.waitingPlayersWindow == null) {
			this.waitingPlayersWindow = new WaitingPlayersFrame(color);
			this.connectWindow.dispose();
			this.connectWindow = null;
			this.waitingPlayersWindow.setVisible(true);
		} else {
			this.waitingPlayersWindow.revalidate();
			this.waitingPlayersWindow.repaint();
		}
	}

	public void loadScreen() {
		if(this.gameWindow == null) {
			this.gameWindow = BoardFrame.getInstance();			
			if(this.connectWindow != null) {
				this.connectWindow.dispose();
				this.connectWindow = null;
				this.StartGame();
				this.gameWindow.setVisible(true);
			} else {
				this.waitingPlayersWindow.dispose();
				this.waitingPlayersWindow = null;
				this.StartGame();
				this.gameWindow.setVisible(true);
			}
		} 
		this.gameWindow.revalidate();
		this.gameWindow.repaint();
	}

	public Color GetCurrentPlayerForeground() {
		switch (game.GetCurrentPlayer()) {
		case 0:	
			return new Color(220,20,60);
		case 1:	
			return new Color (60,179,113);
		case 2:	
			return new Color(255,215,0);
		case 3:	
			return new Color(100,149,237);
		}
		return null;
	}

	public String GetCurrentPlayerText() {
		switch (game.GetCurrentPlayer()) {
		case 0:	
			return "Vermelho";
		case 1: 
			return "Verde";
		case 2: 
			return "Amarelo";
		case 3:	
			return "Azul";
		}	
		return null;
	}

	public String GetCurrentStateText() {
		switch (game.GetCurrentState()) {
		case 6: 
			return "";
		case 0:	
			return "<html>Esperando<br> lançamento.</html>";
		case 1:	
			return "<html>Escolha jogada<br> clicando na<br> peça desejada.</html>";
		case 2:	
			return "<html>Não há<br> jogadas<br> possíveis.</html>";
		case 3:	
			return "<html> Essa Jogada<br> não  é<br> possível, <br>escolha outra.";
		case 4:	
			return "<html> Ande 7 casas<br> (Dado = 6 e<br> não existem<br> peças na casa<br> inicial).</html>";
		case 5:	
			return "<html> Peça comida.<br> Ande 20<br> casas.</html>";
		}
		return null;
	}

	public void SaveGame() {
		gameSaver.SaveGame();
		JOptionPane.showMessageDialog(null, "Jogo Salvo");
	}

	public void LoadGame() {
		gameSaver.LoadGame();
		JOptionPane.showMessageDialog(null, "Jogo Carregado");
	}

	//OBSERVADO PELO BUTTONS PANEL E BOARDPANEL
	@Override
	public void add(ObservadorIF observador) {
		lst.add(observador);
	}

	@Override
	public void remove(ObservadorIF observador) {
		lst.remove(observador);
	}

	@Override
	public Object get(int i) {
		switch(i) {
		case 1:
			return game.GetDiceValue();
		case 2:
			return GetCurrentPlayerForeground();
		case 3:
			return GetCurrentPlayerText();
		case 4:
			return GetCurrentStateText();
		case 5:
			return rollDiceState;
		case 6:
			return pieces.GetAll();
		}
		return 0;
	}

	public void EndGame() {
		JOptionPane.showMessageDialog(null, "Fim de Jogo\n Ganhador: " + ConnectController.getInstance().getNickname() + " " + GetCurrentPlayerText() + "\n" + game.GetPoints());
		ConnectController.getInstance().sendEndMessageToServer();
		ConnectController.getInstance().gameOver();
		System.exit(0);
	}

	//OBSERVADOR DE JOGO E PIECES
	@Override
	public void notify(ObservadoIF observado) {
		ListIterator<ObservadorIF> li = lst.listIterator();
		while(li.hasNext()) {
			li.next().notify(this);			
		}
	}

	@Override
	public void update(Observable obs, Object obj) {	
		if(this.gameWindow != null) {
			this.gameWindow.buttonsPanel.repaint();
			this.gameWindow.boardPanel.repaint();
			this.gameWindow.repaint();
		}
	}

	public void updateGame(String message) {
		System.out.println("Mensagem updatedGame: " + message);
		Object[] v = game.updateGame(message);
		game = (Game)v[0];
		pieces = (Pieces)v[1];
		this.notifyObservers();
		this.notify(game);
		this.notify(pieces);
		BoardFrame.getInstance().buttonsPanel.repaint();
		BoardFrame.getInstance().boardPanel.repaint();
		BoardFrame.getInstance().repaint();
	}
}