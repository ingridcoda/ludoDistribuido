package controllers;

import java.io.IOException;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class Main {

	public static int ponto = 30;
	public static int altura = 15 * ponto + 30; 
	public static int largura = 15 * ponto + 155; 

	public static void main(String[] args) throws IOException {		

		JLabel lblConection = new JLabel("Dados da Conexão:");
		JTextField txtIp = new JTextField("127.0.0.1");
		JTextField txtPort = new JTextField("5500");
		Object[] texts = {lblConection, txtIp, txtPort};  
		JOptionPane.showMessageDialog(null, texts);	

		GameFacade.getInstance().connectPlayer();

		ConnectController.getInstance().connectToServer(txtIp.getText(), Integer.parseInt(txtPort.getText()));

		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			public void run() {
				try {
					ConnectController.getInstance().disconnectToServer();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}));
	}
}

