package controllers;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JOptionPane;

import client.Listener;
import domain.Game;
import domain.Pieces;

public class ConnectController extends Observable implements Observer {

	private static ConnectController instance = null;

	private String ip;
	private int port;
	private Socket connection = null;
	private PrintStream out;

	private String nickname;
	private Pieces pieces;
	private Game jogo;

	private String myTeam = "";

	private ConnectController(){
		nickname = "";
		this.addObserver(GameFacade.getInstance());
	}

	public static ConnectController getInstance(){
		if(instance == null){
			instance = new ConnectController();
		}
		return instance;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getMyTeam() {
		return myTeam;
	}

	public void setMyTeam(String myTeam) {
		this.myTeam = myTeam;
	}

	public Socket getConnection() {
		return connection;
	}

	public void setConnection(String ip, int port) throws UnknownHostException, IOException {
		this.connection = new Socket(ip, port);
	}

	public void connectToServer(String ip, int port) throws IOException {
		setIp(ip);
		setPort(port);
		try {
			setConnection(ip, port);
			this.out = new PrintStream(this.connection.getOutputStream());
			Listener listener = new Listener(this.connection);
			Thread con = new Thread(listener);
			con.start();
		} catch(Exception e) {
			JOptionPane.showMessageDialog(null, "Servidor indisponível. Tente novamente!");
			this.disconnectToServer();
		}
	}

	public void disconnectToServer() throws IOException {
		this.connection.close();
		System.exit(1);
	}

	private String mountGameToSend(){
		pieces = Pieces.GetPieces();
		jogo = Game.getInstance();

		String game = getNickname() + " game ";
		game = game.concat(jogo.GetCurrentPlayer() + " ");
		game = game.concat(jogo.GetCurrentState() + " ");
		game = game.concat(jogo.IsWaitingForPlayer() == true ? "1 ":"0 ");
		game = game.concat(jogo.GetDiceValue() + " ");
		for(int [][] allpieces : pieces.GetAll()){
			for(int [] pieces : allpieces) {
				for(int piece : pieces) {
					game = game.concat(Integer.toString(piece) + " ");
				}
			}
		}
		return game;
	}

	public void sendGameToServer() {
		String game = mountGameToSend();
		out.println(game);
		out.flush();
		this.notifyObservers();
	}	

	public void sendNickname(String nickname) {
		out.println("player nickname " + nickname);
		out.flush();
		this.notifyObservers();
	}

	public void sendEndMessageToServer() {
		out.println("player " + nickname + " saiu");
		out.flush();
		this.notifyObservers();
	}

	public void exitByTimeout() {
		JOptionPane.showMessageDialog(null, "Tempo para conexão ao servidor esgotado. Tenter novamente!");
		this.notifyObservers();
		try {
			this.disconnectToServer();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void gameOver() {
		out.println("###");
		out.flush();
		this.notifyObservers();
	}

	@Override
	public void update(Observable o, Object arg) {
		String message = (String) arg;
		if(message.contains("team")) {
			String[] aux = message.split(" ");
			if(aux[1].equals(getNickname().trim())) {
				setMyTeam(aux[3].trim());
			}
		} else if(message.contains("end game")) {
			this.sendEndMessageToServer();
			try {
				this.disconnectToServer();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		this.setChanged();
		this.notifyObservers(message);
	}
}