package domain;

import java.net.Socket;

public class Client {
	
	private Socket connection;
	private String nickname;
	private String playerTeam;
	
	public Client(Socket con, String name, String team) {
		this.connection = con;
		this.nickname = name;
		this.playerTeam = team;
	}

	public Socket getConnection() {
		return connection;
	}

	public void setConnection(Socket connection) {
		this.connection = connection;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getPlayerTeam() {
		return playerTeam;
	}

	public void setPlayerTeam(String playerTeam) {
		this.playerTeam = playerTeam;
	}
}
