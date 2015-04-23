package SimpleExample;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.net.Socket;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.JTabbedPane;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import java.awt.FlowLayout;

public class ClientGui {

	private JLabel label;
	private Socket socket;
	private boolean firstImage = true;
	private JFrame frame;

	/**
	 * Launch the application.
	 */
	

	/**
	 * Create the application.
	 */
	public ClientGui() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		
		frame.getContentPane().setBackground(Color.DARK_GRAY);
		frame.setAlwaysOnTop(true);
		frame.setBounds(100, 100, 864, 486);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		frame.getContentPane().add(tabbedPane, BorderLayout.CENTER);
		
		JPanel MainPanel = new JPanel();
		MainPanel.setBackground(Color.DARK_GRAY);
		tabbedPane.addTab("Main Panel", null, MainPanel, null);
		tabbedPane.setEnabledAt(0, true);
		MainPanel.setLayout(new BorderLayout(0, 0));
		
		JLabel movieScreen = new JLabel();
		movieScreen.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				System.out.println("Musklick paus kanske?");
			}
		});
		MainPanel.add(movieScreen, BorderLayout.CENTER);
		movieScreen.setHorizontalAlignment(SwingConstants.CENTER);
		this.label = movieScreen;
		
		JPanel buttonPanel = new JPanel();
		MainPanel.add(buttonPanel, BorderLayout.SOUTH);
		buttonPanel.setBackground(Color.GRAY);
		buttonPanel.setLayout(new BorderLayout(0, 0));
		
		JPanel actionPanel = new JPanel();
		actionPanel.setBackground(Color.GRAY);
		buttonPanel.add(actionPanel);
		actionPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JButton btnPlay = new JButton("Play");
		actionPanel.add(btnPlay);
		
		JButton btnPause = new JButton("Pause");
		actionPanel.add(btnPause);
		btnPause.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Pause pressed");
			}
		});
		btnPlay.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Play pressed");
			}
		});
		
		JPanel exitPanel = new JPanel();
		exitPanel.setBackground(Color.GRAY);
		buttonPanel.add(exitPanel, BorderLayout.EAST);
		
		JButton btnExit = new JButton("Exit");
		exitPanel.add(btnExit);
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Exit pressed");
				System.exit(0);
			}
		});
		
		JPanel SelectPanel = new JPanel();
		SelectPanel.setBackground(Color.DARK_GRAY);
		tabbedPane.addTab("Select Panel", null, SelectPanel, null);
		String	listData[] =
			{
				"Star Wars - Force Awakens",
				"Taken 4",
				"James Bond",
				"Pumping Iron"
			};
		SelectPanel.setLayout(new BorderLayout(0, 0));
		JList list = new JList(listData);
		list.setBorder(UIManager.getBorder("List.focusCellHighlightBorder"));
		list.setBackground(Color.DARK_GRAY);
		list.setForeground(Color.WHITE);
		SelectPanel.add(list);
		
		JPanel selectButtonPanel = new JPanel();
		selectButtonPanel.setBackground(Color.GRAY);
		SelectPanel.add(selectButtonPanel, BorderLayout.SOUTH);
		
		JButton btnSelect = new JButton("Select");
		selectButtonPanel.add(btnSelect);
		tabbedPane.setEnabledAt(1, true);
		
		frame.setVisible(true);
	}
	public void setImage(BufferedImage image){
		if(firstImage){
			frame.setBounds(100, 100, image.getWidth(), image.getHeight()+120);
			frame.setLocationRelativeTo(null);
			firstImage = false;
		}
		label.setIcon(new ImageIcon(image));
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}
}