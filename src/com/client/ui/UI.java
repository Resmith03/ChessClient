package com.client.ui;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.client.models.transport.Client;

public class UI extends JFrame {
    
    private Client player;
    private JPanel activePanel;

    public UI(Client player) {
	setFrameDefaults();
	setActivePanel(new LobbyPanel(this, player));
	this.setVisible(true);
	this.player = player;
    }

    public void setActivePanel(JPanel panel) {
	activePanel = panel;
	this.getContentPane().removeAll();
	add(activePanel);
	repaint();
	setVisible(true);
    }

    private void setFrameDefaults() {
	setTitle("Frostburg State University - Chess");
	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	setSize(800, 800);
	setLocationRelativeTo(null);
	setResizable(false);
    }
}
