package com.client.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

import com.client.DTO.BoardSpaceDTO;
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
    private static final Integer RADIO_BUTTON_X=10;
    private static final Integer RADIO_BUTTON_Y=60;
    private List<JButton> challengeButtons;
    
    public LobbyPanel(UI ui, Client player){
	this.ui = ui;
	challengeButtons = new ArrayList<JButton>();
	dropdown = new JComboBox<PlayerDTO>();
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
	new Thread(new ChallengeListener()).start();
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
		
		    for(PlayerDTO playerDTO:listOfPlayers){
			dropdown.addItem(playerDTO);
		    }
		}catch(Exception ex){
		    ex.printStackTrace();
		}
	    }
	}
    }
    
    private class ButtonListener implements ActionListener{

	@Override
	public void actionPerformed(ActionEvent e) {
	    System.out.println("Sending challenge request to: " + String.valueOf(((PlayerDTO)dropdown.getSelectedItem()).getId()));
	    Response response = player.sendRequest(new Request(ContentType.CHALLENGE, MessageType.POST, ResponseType.YES_NO, String.valueOf(((PlayerDTO)dropdown.getSelectedItem()).getId())));
	    if(response != null){
	    if(response.getContentType().equals(ContentType.BOARD)){
		List<BoardSpaceDTO> boardSpaces = new ArrayList<BoardSpaceDTO>();
		
		try {
		    boardSpaces = new ObjectMapper().readValue(response.getPayload(),new TypeReference<List<BoardSpaceDTO>>(){});
		} catch (IOException e1) {
		    e1.printStackTrace();
		}
		
		ui.setActivePanel(new GamePanel(ui,player,boardSpaces));
	    }
	    }
	}
	
    }
    private class RadioButtonListener implements ActionListener{

	@Override
	public void actionPerformed(ActionEvent arg0) {
	    // TODO Auto-generated method stub
	    
	}
	
    }
    
    private class AcceptChallengeListener implements ActionListener{
	private Request request;
	
	public AcceptChallengeListener(Request request){
	    this.request = request;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
	    ChallengeButton button = (ChallengeButton) e.getSource();
	    String requestId = button.getRequestId();
	    player.sendResponse(requestId, ContentType.CHALLENGE, MessageType.POST, "Y");
	    List<BoardSpaceDTO> spaces = new ArrayList<BoardSpaceDTO>();
	    
	    try {
		spaces = new ObjectMapper().readValue(request.getPayload(), new TypeReference<List<BoardSpaceDTO>>(){});
	    } catch (Exception e1) {
		e1.printStackTrace();
	    }
	    ui.setActivePanel(new GamePanel(ui, player, spaces ));
	}
	
    }
    private class ChallengeListener implements Runnable{

	@Override
	public void run() {
	    
	    while(true){
		List<Request> requests = player.getChallengeRequests();
		
		//remove old buttons
		for(JButton radioButton:challengeButtons){
		    remove(radioButton);
		}
		
		
		int counter = 1;
		
		if(requests != null){		

		for(Request request:requests){
		    
		    ChallengeButton challenge = new ChallengeButton("Accept Challenge #" + counter, request.getId());
		    challenge.setSize(200,30);
		    challenge.setLocation(RADIO_BUTTON_X, RADIO_BUTTON_Y + (40 * counter));
		    challenge.addActionListener(new AcceptChallengeListener(request));
		    add(challenge);
		    counter++;
		}
		revalidate();
		repaint();
		setVisible(true);
		}
		try {
		    Thread.sleep(1000);
		} catch (InterruptedException e) {
		    e.printStackTrace();
		}
	    }
	}
    }
    
    private class ChallengeButton extends JButton{
	private String requestId;
	public String getRequestId(){return requestId;}
	public ChallengeButton(String title, String requestId) {
	    super(title);
	    this.requestId = requestId;
	}
    }
}
