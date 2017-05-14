package com.client.ui;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

import com.client.models.board.BoardSpace;
import com.client.models.board.GamePiece;
import com.client.models.board.Location;
import com.client.models.board.Move;
import com.client.models.transport.Client;
import com.client.models.transport.ContentType;
import com.client.models.transport.MessageType;
import com.client.models.transport.Request;
import com.client.models.transport.Response;
import com.client.models.transport.ResponseType;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class GamePanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private Client client;
    private Map<JToggleButton, Location> buttonLocations;
    private List<JToggleButton> selected = new ArrayList<JToggleButton>();
    private boolean clearing = false;

    private String getImageBySymbol(String symbol) {

	String url = "";
	if (symbol != null) {
	    switch (symbol) {
	    case "R":
		url = "rook_white.png";
		break;
	    case "r":
		url = "rook.png";
		break;
	    case "N":
		url = "knight_white.png";
		break;
	    case "n":
		url = "knight.png";
		break;
	    case "B":
		url = "bishop_white.png";
		break;
	    case "b":
		url = "bishop.png";
		break;
	    case "P":
		url = "pawn_white.png";
		break;
	    case "p":
		url = "pawn.png";
		break;
	    case "K":
		url = "king_white.png";
		break;
	    case "k":
		url = "king.png";
		break;
	    case "Q":
		url = "queen_white.png";
		break;
	    case "q":
		url = "queen.png";
	    }
	}
	return url;
    }

    public GamePanel(Client client, List<BoardSpace> spaces) {
	buttonLocations = new HashMap<JToggleButton, Location>();
	this.client = client;
	addTitle();
	setPanelProperties();
	updateBoard(spaces);
    }

    private void addTitle() {
	JLabel label = new JLabel("Frostburg State University: Chess");
	label.setSize(300, 20);
	label.setLocation(50, 10);
	add(label);
    }

    private void setPanelProperties() {
	setOpaque(true);
	setBackground(Color.WHITE);
	setLayout(null);
	setSize(800, 800);
    }

    public void updateBoard(List<BoardSpace> spaces) {
	JPanel grid = new JPanel(new GridLayout(8, 8));
	grid.setSize(696, 696);
	grid.setLocation(50, 50);
	grid.setBackground(Color.LIGHT_GRAY);
	grid.setOpaque(true);

	try {
	    for (int y = 0; y < 8; y++) {
		for (int x = 0; x < 8; x++) {

		    JToggleButton button = new JToggleButton();

		    button.addItemListener(new BoardSpaceListener());
		    button.setSize(87, 87);
		    button.setLocation(x * 87, y * 87);

		    if (spaces != null) {
			for (BoardSpace space : spaces) {
			    if (space.getLocation().getX() == x && space.getLocation().getY() == y) {
				GamePiece piece = space.getGamePiece();

				if (piece != null) {
				    button.setIcon(new ImageIcon(
					    getClass().getResource(this.getImageBySymbol(piece.getSymbol()))));
				}
				break;
			    }
			}
		    }

		    if ((x + y) % 2 == 0) {
			button.setBackground(Color.GRAY);
		    } else {
			button.setBackground(Color.LIGHT_GRAY);
		    }

		    buttonLocations.put(button, new Location(x, y));
		    grid.add(button);
		}
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	}
	
	removeAll();
	add(grid);
	revalidate();
	repaint();
	setVisible(true);
    }

    private void lockBoard() {
	setEnabled(false);
    }

    private void unlockBoard() {
	setEnabled(true);
    }

    private class BoardSpaceListener implements ItemListener {
	private ObjectMapper mapper;

	public BoardSpaceListener() {
	    mapper = new ObjectMapper();
	}

	@Override
	public void itemStateChanged(ItemEvent ev) {
	    if (clearing) {
		return;
	    }

	    if (ev.getStateChange() == ItemEvent.SELECTED) {

		selected.add((JToggleButton) ev.getSource());

		if (selected.size() > 1) {
		    lockBoard();
		    clearing = true;

		    JToggleButton fromButton = selected.get(0);
		    JToggleButton toButton = selected.get(1);
		    Location from = buttonLocations.get(fromButton);
		    Location to = buttonLocations.get(toButton);
		    Move move = new Move(from, to);
		    String moveString = "";
		    fromButton.setSelected(false);
		    toButton.setSelected(false);
		    try {
			moveString = mapper.writeValueAsString(move);

			Response response = client.sendRequest(
				new Request(client.getSocket().getLocalSocketAddress().toString(),ContentType.MOVE, MessageType.POST, ResponseType.BOARD, moveString));

			if (response != null) {
			    if (response.getContentType().equals(ContentType.BOARD)) {
				List<BoardSpace> spaces = mapper.readValue(response.getPayload(),
					new TypeReference<List<BoardSpace>>() {});
				
				buttonLocations.clear();
				updateBoard(spaces);
			    }
			}
		    } catch (Exception ex) {
			ex.printStackTrace();
		    }

		    selected.clear();
		    unlockBoard();
		} else if (ev.getStateChange() == ItemEvent.DESELECTED) {
		    selected.remove((JToggleButton) ev.getSource());
		}

		clearing = false;
	    }
	}
    }
}
