package server;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Scanner;

public class Listener extends Observable implements Runnable {

	private Socket client;
	private Scanner in;
	private ArrayList<String> clients; 

	public Listener(Socket client) throws IOException {
		this.client = client;
		this.addObserver(Server.getInstance());
		this.in = new Scanner(this.client.getInputStream());
		this.clients = new ArrayList<String>();
	}

	public Socket getClient() {
		return client;
	}

	public void setClient(Socket client) {
		this.client = client;
	}

	@Override
	public void run() {
		try {
			this.addObserver(Server.getInstance());

			while(in.hasNextLine()){
				String message = in.nextLine();
				System.out.println("Mensagem recebida " + message);
				if(!message.equals("###")){
					if(message.contains("nickname")) {
						String[] aux = message.split(" ");
						if(clients.contains(aux[2])) {
							this.setChanged();
							this.notifyObservers("nickname inválido");
						} else {
							clients.add(aux[2]);
							this.setChanged();
							this.notifyObservers("nickname válido "+ clients.get(clients.size()-1));
						}
					} else {
						this.setChanged();
						this.notifyObservers(message);
					}
				} else {
					try {
						this.client.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
					this.notifyObservers(message);
				}
			}
			
			in.close();
			System.exit(0);
			
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}
}