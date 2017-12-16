package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import controllers.ConnectController;

@SuppressWarnings("serial")
public class ConnectPanel extends JPanel implements ActionListener
{
	private JTextField playerName;
	private JLabel label;
	private JButton playButton;
	private String nickname;

	public ConnectPanel(Dimension dimension)
	{
		super();
		this.setLayout(null);

		this.setBackground(Color.WHITE);

		this.label = new JLabel("Insira Nickname:");
		this.label.setLocation(0, dimension.height/5);
		this.label.setSize(dimension.width, 40);
		this.label.setForeground(Color.BLACK);
		this.label.setOpaque(false);
		this.label.setFont(new Font("Helvetica", 0, 16));
		this.label.setHorizontalAlignment(SwingConstants.CENTER);
		this.add(this.label);

		this.playerName = new JTextField();
		this.playerName.setLocation(dimension.width/3, dimension.height/3);
		this.playerName.setSize(dimension.width/3, 40);
		this.playerName.setEditable(true);
		this.playerName.setBackground(Color.WHITE);
		this.playerName.setForeground(Color.BLACK);
		this.add(this.playerName);

		this.playButton = new JButton("Jogar");
		this.playButton.setContentAreaFilled(false);
		this.playButton.setOpaque(false);
		this.playButton.setForeground(Color.BLACK);
		this.playButton.setBackground(Color.WHITE);
		this.playButton.setBorderPainted(true);
		this.playButton.setLocation(dimension.width/2 - 40, dimension.height/2);
		this.playButton.setSize(dimension.width/8, 40);
		this.playButton.addActionListener(this);
		this.add(this.playButton);

	}

	@Override
	public void actionPerformed(ActionEvent e) 
	{
		this.nickname = this.playerName.getText();
		if(this.nickname != null && !this.nickname.isEmpty()) {
			ConnectController.getInstance().setNickname(this.nickname);
			ConnectController.getInstance().sendNickname(this.nickname);			
		} else {
			JOptionPane.showMessageDialog(null,
					"Você deve inserir o campo nickname para prosseguir",
					"Insira um nickname", JOptionPane.INFORMATION_MESSAGE);
		}	
	}
}