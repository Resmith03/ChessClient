package com.client.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.client.models.transport.Client;

public class ConnectionPanel extends JPanel{
    private UI ui;
    private Client client;
    private JLabel serverLabel;
    private JLabel portLabel;
    private JTextField txtServerAddress;
    private JTextField txtServerPort;
    private JButton connect;
    private JTextArea logs;
    private JScrollPane logScrollPane;
    private JLabel logLabel;
    private JLabel serverDefaults;
    
    public ConnectionPanel(UI ui, Client client){
	this.ui=ui;
	this.client=client;
	setSize(800,800);
	setLocation(0,0);
	serverLabel = getServerLabel();
	portLabel = getPortLabel();
	setLayout(null);
	txtServerAddress = getServerAddressField();
	txtServerPort = getServerPortField();
	connect = getConnectButton();
	logs = getLogField();
	logScrollPane = getScrollPane(logs);
	logLabel = getLogLabel();
	serverDefaults = getServerDefaults();
	add(serverDefaults);
	add(logLabel);
	add(logScrollPane);
	add(serverLabel);
	add(portLabel);
	add(txtServerAddress);
	add(txtServerPort);
	add(connect);
	
	setVisible(true);
    }

    private JLabel getServerDefaults() {
	JLabel label = new JLabel("Example (SERVER:localhost and PORT:1321)");
	label.setSize(400,20);
	label.setLocation(50,45);
	
	return label;
    }

    private JLabel getLogLabel() {
	JLabel label = new JLabel("Connection Logs");
	label.setSize(300,20);
	label.setLocation(50,100);
	return label;
    }

    private JScrollPane getScrollPane(JTextArea logs) {
	JScrollPane pane = new JScrollPane(logs);
	pane.setSize(700,500);
	pane.setLocation(50,125);
	return pane;
    }

    private JTextArea getLogField() {
	JTextArea area = new JTextArea();
	area.setSize(700,500);
	return area;
    }

    private JLabel getPortLabel() {
	JLabel label = new JLabel("PORT:");
	label.setSize(100,20);
	label.setLocation(320,20);
	return label;
    }

    private JButton getConnectButton() {
	JButton button = new JButton("Connect");
	button.setSize(100,20);
	button.setLocation(650,20);
	button.addActionListener(new ButtonListener());
	return button;
    }

    private JTextField getServerPortField() {
	JTextField field = new JTextField();
	field.setLocation(365,20);
	field.setSize(100,20);
	return field;
    }

    private JTextField getServerAddressField() {
	JTextField field = new JTextField();
	field.setLocation(110,20);
	field.setSize(200,20);
	return field;
    }

    private JLabel getServerLabel() {
	JLabel label = new JLabel("SERVER:");
	label.setSize(100,20);
	label.setLocation(50,20);
	return label;
    }
    
    private class ButtonListener implements ActionListener{

	@Override
	public void actionPerformed(ActionEvent arg0) {
	    try{
		String serverAddress = txtServerAddress.getText();
		String serverPort = txtServerPort.getText();
		Integer port = Integer.parseInt(serverPort);
		
		client.connect(serverAddress, port);
		ui.setActivePanel(new LobbyPanel(ui,client));
	    }catch(Exception ex){
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		ex.printStackTrace(pw);
		
		logs.setText(sw.toString());
	    }
	}
	
    }
}
