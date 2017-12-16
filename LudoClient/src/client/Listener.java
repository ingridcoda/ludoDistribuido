package client;

import java.io.IOException;
import java.net.Socket;
import java.util.Observable;
import java.util.Scanner;

import controllers.ConnectController;
import controllers.GameFacade;
import view.BoardFrame;

public class Listener extends Observable implements Runnable {

	private Socket connection;
	private Scanner in;

	public Listener(Socket connection) {
		this.connection = connection;
	}

	@Override
	public void run() {
		this.addObserver(ConnectController.getInstance());
		try {
			this.in = new Scanner(this.connection.getInputStream());
			while(in.hasNextLine()){
				String message = in.nextLine();
				if(!message.equals("###")){
					if(message.contains("iniciar jogo")) {
						GameFacade.getInstance().loadScreen();
						this.setChanged();
						this.notifyObservers(message);
					} else if(message.equals("timeout")) {
						ConnectController.getInstance().exitByTimeout();
						this.setChanged();
						this.notifyObservers(message);
					} else if(message.contains("nickname inválido")) {
						GameFacade.getInstance().connectPlayer();
						this.setChanged();
						this.notifyObservers(message);
					} else if(message.contains("entrou")) {
						if(ConnectController.getInstance().getMyTeam().equals(" ")) {
							String[] aux = message.split(" ");
							ConnectController.getInstance().setMyTeam(aux[3]);
						}
						GameFacade.getInstance().waitingPlayers();
						this.setChanged();
						this.notifyObservers(message);
					} else if(message.contains("azul")){
						ConnectController.getInstance().setMyTeam("azul");
						this.setChanged();
						this.notifyObservers(message);
					}
					if(message.contains("game")) {
						String[] aux = message.split(" ");
						if(!aux[0].equals(ConnectController.getInstance().getNickname())) {
							GameFacade.getInstance().updateGame(message);
						}
						BoardFrame.getInstance().repaint();
					}
				}
			}
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
