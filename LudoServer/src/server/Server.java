package server;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import javax.swing.Timer;

import domain.Client;

public class Server implements Observer, ActionListener{

	private static Server instance = null;
	private ArrayList<Socket> clientsSocket;
	private ArrayList<Client> clients;
	private ServerSocket server;
	private Thread t = new Thread();
	private Timer countTime;

	private Server() throws IOException{
		this.clientsSocket = new ArrayList<Socket>();
		this.clients = new ArrayList<Client>();
		this.countTime = new Timer(120000, this);
	}

	public static Server getInstance() throws IOException{
		if(instance == null){
			instance = new Server();
		}
		return instance;
	}

	public void connectServer(int port) throws IOException{
		this.server = new ServerSocket(port);		
	}

	public void runServer(){		
		t = new Thread(){
			public void run(){
				while(clientsSocket.size() < 4){
					Socket client;
					try {
						client = server.accept();						
						clientsSocket.add(client);
						System.out.println("Novo cliente conectado.");
						Listener listener = new Listener(client);
						Thread clientListen = new Thread(listener);
						clientListen.start();
					} catch (IOException e) {
						e.printStackTrace();
					}

				} 
				return;
			}
		};
		t.start();
	}

	public void sendMessageToClients(Socket client, String message) throws IOException {
		PrintStream out = new PrintStream(client.getOutputStream());			
		out.println(message);			
		out.flush();
	}

	public void disconnectServer() throws IOException{
		server.close();
	}

	@Override
	public void update(Observable obs, Object obj) {
		Listener listener = (Listener) obs;
		String message = (String) obj;
		if(message.contains("nickname válido")) {
			String currentPlayerTeam = "";
			switch(((clients.size() + 1) % 4) - 1) {
			case 0:
				currentPlayerTeam = "vermelho";
				break;
			case 1:
				currentPlayerTeam = "verde";
				break;
			case 2:
				currentPlayerTeam = "amarelo";
				break;
			case -1:
				currentPlayerTeam = "azul";
				break;
			}
			Client c = new Client(listener.getClient(), message.substring(message.lastIndexOf(" "), message.length()), currentPlayerTeam);

			this.clients.add(c);
			System.out.println("Total de clientes: " + clients.size());

			if(clients.size() % 4 == 1) {
				countTime.start();
			} else if(clients.size() % 4 == 0) {
				countTime.stop();
				for(Client client : clients) {
					try {
						this.sendMessageToClients(client.getConnection(), "iniciar jogo");
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				try {
					this.sendMessageToClients(clients.get(3).getConnection(), "azul");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			this.clientsSocket.remove(listener.getClient());

			for(Client client : clients) {
				try {
					if(!c.getNickname().equals(" ") && clients.size() < 4) {
						this.sendMessageToClients(client.getConnection(), "player "+ c.getNickname().trim() +" team " + currentPlayerTeam + " entrou");
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		} else {			
			for(Client client : clients) {
				try {
					this.sendMessageToClients(client.getConnection(), message);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		for(Client client : clients) {
			try {
				this.sendMessageToClients(client.getConnection(), "timeout");
				System.exit(1);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}	
	}
}