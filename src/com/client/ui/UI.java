package com.client.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.client.app.Client;

public class UI extends JFrame{
    private JPanel panel;
    private JLabel label;
    private JButton button;
    private JTextArea console;
    private JScrollPane consoleScroll;
    private JTextField input;
    private Client client;
    
    public void addMessage(String message){
	console.append(message + "\n");
	console.setCaretPosition(console.getDocument().getLength());
    }
    
    public UI(){	    
	this.setTitle("Frostburg State University - Chess");
	this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	this.setSize(800,800);
	this.setLocationRelativeTo(null);
	this.setResizable(false);
	this.add(getMainPanel());
	this.setVisible(true);
    }
    
    public void setClient(Client client){
	this.client = client;
    }
    private JPanel getMainPanel(){
	panel = new JPanel();
	panel.setOpaque(true);
	panel.setBackground(Color.WHITE);
	panel.setLayout(null);
	
	label = new JLabel("Frostburg State University - Chess");
	label.setSize(400,20);
	label.setFont(new Font(label.getFont().getName(), Font.ITALIC, 18));
	label.setLocation(10,10);
	panel.add(label);
	
	input = new JTextField(10);
	input.setLocation(90, 760);
	input.setSize(200,30);
	panel.add(input);
	
	button = new JButton("Send");
	button.addActionListener(new ButtonListener());
	button.setSize(75,30);
	button.setLocation(10,760);
	panel.add(button);
	
	console = new JTextArea();
	console.setFont(new Font("monospaced", Font.CENTER_BASELINE, 20));
	console.setEditable(false);
	JScrollPane consoleScroll = new JScrollPane(console, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	consoleScroll.setSize(800,700);
	consoleScroll.setLocation(0,50);
	panel.add(consoleScroll);
	return panel;
    }
    
    private class ButtonListener implements ActionListener{
	@Override
	public void actionPerformed(ActionEvent arg0) {
	    client.getWriter().println(input.getText());
	    input.setText("");
	}
    }
}
