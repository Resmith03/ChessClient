package com.client.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

import com.client.DTO.PlayerDTO;
import com.client.app.Client;
import com.client.models.ContentType;
import com.client.models.MessageType;
import com.client.models.Request;
import com.client.models.Response;
import com.client.models.ResponseType;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class LobbyPanel extends JPanel{

    private static final long serialVersionUID = -5503442854405966926L;
    private JLabel logo;
    private Client player;
    private JComboBox<PlayerDTO> dropdown;
    private JButton challenge;
    private boolean active;
    private JLabel instructions;
    private UI ui;
    
    public LobbyPanel(UI ui, Client player){
	this.ui = ui;
	dropdown = new JComboBox<PlayerDTO>();
	dropdown.addActionListener(new DropdownListener());
	dropdown.addPopupMenuListener(new PopupListener());
	dropdown.setSize(310,20);
	dropdown.setLocation(10, 40);
	challenge = new JButton("Challenge Player");
	challenge.setSize(180, 20);
	challenge.setLocation(330,40);
	challenge.addActionListener(new ButtonListener());
	instructions = new JLabel("Select a player from the dropdown and press challenge to challenge the player to a game of chess.");
	instructions.setSize(800,20);
	instructions.setLocation(10,70);
	add(instructions);
	add(challenge);
	add(dropdown);
	active = true;
	this.player = player;
	setPanelDefaults();
	addLogo();
    }
    
    public void setInactive(){
	active = false;
    }

    private void setPanelDefaults(){
	setOpaque(true);
	setBackground(Color.WHITE);
	setLayout(null);
    }
    
    private void addLogo(){
	logo = new JLabel("Frostburg State University - Chess");
	logo.setSize(400,20);
	logo.setFont(new Font(logo.getFont().getName(), Font.ITALIC, 18));
	logo.setLocation(10,10); 
	add(logo);
    }
    
    private class PopupListener implements PopupMenuListener{
	private ObjectMapper mapper;
	
	public PopupListener(){
	    mapper = new ObjectMapper();
	}
	@Override
	public void popupMenuCanceled(PopupMenuEvent e) {
	    // TODO Auto-generated method stub
	    
	}

	@Override
	public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
	    // TODO Auto-generated method stub
	    
	}

	@Override
	public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
	    
	    Request request = new Request(ContentType.NONE, MessageType.GET, ResponseType.PLAYER_LIST, "");
	    Response response = player.sendRequest(request);
	    if(response != null){
		try{
		    List<PlayerDTO> listOfPlayers = mapper.readValue(response.getPayload(), new TypeReference<List<PlayerDTO>>(){});
		    dropdown.removeAllItems();
		
		    for(PlayerDTO player:listOfPlayers){
			dropdown.addItem(player);
		    }
		}catch(Exception ex){
		    ex.printStackTrace();
		}
	    }
	}
	
    }
    private class DropdownListener implements ActionListener{
	@Override
	public void actionPerformed(ActionEvent event) {
	    
	}
    }
    
    private class ButtonListener implements ActionListener{

	@Override
	public void actionPerformed(ActionEvent e) {
	    //Response response = player.sendRequest(new Request(ContentType.PLAYER, MessageType.POST, ResponseType.YES_NO, String.valueOf(((PlayerDTO)dropdown.getSelectedItem()).getId())));
	    if(/*response.getPayload().equalsIgnoreCase("Y")*/true){
		ui.setActivePanel(new GamePanel(ui,player));
	    }
	}
	
    }
}
