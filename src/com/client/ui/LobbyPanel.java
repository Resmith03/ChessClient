package com.client.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

import com.client.models.board.BoardSpace;
import com.client.models.player.Player;
import com.client.models.transport.Client;
import com.client.models.transport.ContentType;
import com.client.models.transport.MessageType;
import com.client.models.transport.Request;
import com.client.models.transport.Response;
import com.client.models.transport.ResponseType;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class LobbyPanel extends JPanel {

    private static final long serialVersionUID = -5503442854405966926L;
    private JLabel logo;
    private Client player;
    private JComboBox<Player> dropdown;
    private JButton challenge;
    private JLabel instructions;
    private UI ui;
    private static final Integer RADIO_BUTTON_X = 10;
    private static final Integer RADIO_BUTTON_Y = 60;
    private List<JButton> challengeButtons;

    public LobbyPanel(UI ui, Client player) {
	this.ui = ui;
	challengeButtons = new ArrayList<JButton>();
	dropdown = new JComboBox<Player>();
	dropdown.addPopupMenuListener(new PopupListener());
	dropdown.setSize(310, 20);
	dropdown.setLocation(10, 40);
	challenge = new JButton("Challenge Player");
	challenge.setSize(180, 20);
	challenge.setLocation(330, 40);
	challenge.addActionListener(new ButtonListener());
	instructions = new JLabel(
		"Select a player from the dropdown and press challenge to challenge the player to a game of chess.");
	instructions.setSize(800, 20);
	instructions.setLocation(10, 70);
	add(instructions);
	add(challenge);
	add(dropdown);
	this.player = player;
	setPanelDefaults();
	addLogo();
	new Thread(new ChallengeListener()).start();
    }
    
    private void setPanelDefaults() {
	setOpaque(true);
	setBackground(Color.WHITE);
	setLayout(null);
    }

    private void addLogo() {
	logo = new JLabel("Frostburg State University - Chess");
	logo.setSize(400, 20);
	logo.setFont(new Font(logo.getFont().getName(), Font.ITALIC, 18));
	logo.setLocation(10, 10);
	add(logo);
    }

    private class PopupListener implements PopupMenuListener {
	private ObjectMapper mapper;

	public PopupListener() {
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

	    Request request = new Request(player.getSocket().getLocalSocketAddress().toString(), ContentType.NONE, MessageType.GET, ResponseType.PLAYER_LIST, "");
	    Response response = player.sendRequest(request);
	    if (response != null) {
		try {
		    List<Player> listOfPlayers = mapper.readValue(response.getPayload(),
			    new TypeReference<List<Player>>() {
			    });
		    dropdown.removeAllItems();

		    for (Player curPlayer : listOfPlayers) {
			if(!curPlayer.getUsername().equals(player.getSocket().getLocalSocketAddress().toString())){
			    dropdown.addItem(curPlayer);
			}
			
		    }
		} catch (Exception ex) {
		    ex.printStackTrace();
		}
	    }
	}
    }

    private class ButtonListener implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {

	    Response response = player.sendRequest(new Request(player.getSocket().getLocalSocketAddress().toString(),ContentType.CHALLENGE, MessageType.POST,
		    ResponseType.YES_NO, String.valueOf(((Player) dropdown.getSelectedItem()).getUsername())));
	    if (response != null) {
		if(response.getContentType() == ContentType.BOARD){
		    List<BoardSpace> spaces = new ArrayList<BoardSpace>();
		    
		    try {
			spaces = new ObjectMapper().readValue(response.getPayload(), new TypeReference<List<BoardSpace>>() {});
		    } catch (Exception ex) {
			ex.printStackTrace();
		    }
		    
		    GamePanel panel = new GamePanel(player, spaces);
		    panel.setBackground(Color.BLACK);
		    player.setGamePanel(panel);
		    ui.setActivePanel(panel);
		    ui.setTitle(ui.getTitle() + " - BLACK PLAYER");
		}
	    }
	}

    }

    private class AcceptChallengeListener implements ActionListener {
	private Request request;

	public AcceptChallengeListener(Request request) {
	    this.request = request;
	}

	@Override
	public void actionPerformed(ActionEvent event) {
	    ChallengeButton button = (ChallengeButton) event.getSource();
	    String requestId = button.getRequestId();
	    player.sendResponse(requestId, ContentType.CHALLENGE, MessageType.POST, "Y");
	    GamePanel panel = new GamePanel(player, new ArrayList<BoardSpace>());
	    player.setGamePanel(panel);
	    panel.setBackground(Color.WHITE);
	    ui.setTitle(ui.getTitle() + " - WHITE PLAYER");
	    ui.setActivePanel(panel);
	}
    }

    private class ChallengeListener implements Runnable {

	@Override
	public void run() {

	    while (true) {
		List<Request> requests = player.getChallengeRequests();

		// remove old buttons
		for (JButton radioButton : challengeButtons) {
		    remove(radioButton);
		}

		int counter = 1;

		if (requests != null) {

		    for (Request request : requests) {

			ChallengeButton challengeButton = new ChallengeButton("Accept Challenge #" + counter, request.getId());
			challengeButton.setSize(200, 30);
			challengeButton.setLocation(RADIO_BUTTON_X, RADIO_BUTTON_Y + (40 * counter));
			challengeButton.addActionListener(new AcceptChallengeListener(request));
			add(challengeButton);
			counter++;
		    }

		    revalidate();
		    repaint();
		    setVisible(true);
		}
		try {
		    Thread.sleep(10);
		} catch (InterruptedException e) {
		    e.printStackTrace();
		}
	    }
	}
    }

    private class ChallengeButton extends JButton {
	private static final long serialVersionUID = 1L;
	private String requestId;

	public String getRequestId() {
	    return requestId;
	}

	public ChallengeButton(String title, String requestId) {
	    super(title);
	    this.requestId = requestId;
	}
    }
}
