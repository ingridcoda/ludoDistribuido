package controllers;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import server.Server;

public class Main {

	private static int port;
	
	public static int getPort() {
		return port;
	}

	public static void setPort(int port) {
		Main.port = port;
	}

	public static void main(String[] args) throws Exception {
		JLabel lblMessage = new JLabel("Porta do Servidor:");
		JTextField txtPort = new JTextField("5500");
		Object[] texts = {lblMessage, txtPort};  
		JOptionPane.showMessageDialog(null, texts);
		setPort(Integer.parseInt(txtPort.getText()));
		Server.getInstance().connectServer(getPort());
		Server.getInstance().runServer();
	}
}
