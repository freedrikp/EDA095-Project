package SimpleExample.client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.Socket;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

import SimpleExample.common.Configuration;

public class ClientGui {

	private JLabel label;
	private Socket socket;
	private boolean firstImage = true;
	private JFrame frame;
	private JProgressBar progressBar;
	private JLabel procent;
	private ClientImageBuffer cib;
	private boolean fullscreen = false;
	private int mouseClicks = 1;
	private JPanel movieScreenPanel;
	private JButton btnPlay;
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
	 * 
	 * @param progressBar
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

		JPanel movieScreenPanel = new JPanel();
		movieScreenPanel.setBackground(Color.DARK_GRAY);
		MainPanel.add(movieScreenPanel, BorderLayout.CENTER);
		movieScreenPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		JLabel movieScreen = new JLabel();
		movieScreenPanel.add(movieScreen);
		movieScreen.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					if (!fullscreen) {
						frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
					} else {
						frame.setExtendedState(JFrame.NORMAL);

					}
					fullscreen = !fullscreen;
				}
				// System.out.println("Musklick paus kanske?");
				if (mouseClicks % 2 == 1) {
					cib.setPlayNotPause(false);
					btnPlay.setText("Play");
					mouseClicks++;
				} else {
					cib.setPlayNotPause(true);
					btnPlay.setText("Pause");
					mouseClicks = 1;
				}
				// progressBar.setValue(progressBar.getValue() - 50);
				// System.gc();
			}
		});
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

		procent = new JLabel();
		procent.setForeground(Color.WHITE);
		actionPanel.add(procent);

		progressBar = new JProgressBar(0, Configuration.CLIENT_BUFFER_SIZE);
		actionPanel.add(progressBar);
		progressBar.setForeground(Color.LIGHT_GRAY);
		progressBar.setBackground(Color.DARK_GRAY);
		progressBar.setValue(0);

		btnPlay = new JButton("Play");
		actionPanel.add(btnPlay);
		
		btnPlay.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// System.out.println("Play pressed");
				if (cib.isPlaying()) {
					btnPlay.setText("Play");
				} else {
					btnPlay.setText("Pause");
				}
				cib.setPlayNotPause(!cib.isPlaying());
			}
		});

		JPanel exitPanel = new JPanel();
		exitPanel.setBackground(Color.GRAY);
		buttonPanel.add(exitPanel, BorderLayout.EAST);
		
		JButton btnConnect = new JButton("Connect");
		exitPanel.add(btnConnect);

		JButton btnExit = new JButton("Exit");
		exitPanel.add(btnExit);
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Exit pressed");
				try {
					socket.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				System.exit(0);
			}
		});

		JPanel SelectPanel = new JPanel();
		SelectPanel.setBackground(Color.DARK_GRAY);
		tabbedPane.addTab("Select Panel", null, SelectPanel, null);
		String listData[] = { "Star Wars - Force Awakens", "Taken 4",
				"James Bond", "Pumping Iron" };
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

	public void setImage(BufferedImage image) {
		if (firstImage) {
			frame.setBounds(100, 100, image.getWidth(), image.getHeight() + 125);
			frame.setLocationRelativeTo(null);
			cib.setPlayNotPause(true);
			firstImage = false;
		}
		if (fullscreen) {
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			int width = (int) screenSize.getWidth();
			int height = (int) (screenSize.getHeight() / 1.3);
			label.setSize(width, height);
			System.out.println("Size: " + label.getWidth() + " "
					+ label.getHeight());
			BufferedImage fullscreenImage = new BufferedImage(label.getWidth(),
					label.getHeight(), BufferedImage.TYPE_INT_RGB);
			Graphics2D g = fullscreenImage.createGraphics();
			g.drawImage(image, 0, 0, label.getWidth(), label.getHeight(), null);
			g.dispose();
			image = fullscreenImage;
		}
		label.setIcon(new ImageIcon(image));

	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	public void setImageBuffer(ClientImageBuffer cib) {
		this.cib = cib;
	}

	public void updateProgressBar(int bufferSize) {
		if (bufferSize <= Configuration.CLIENT_BUFFER_SIZE) {
			progressBar.setValue(bufferSize);
			progressBar.setVisible(true);
		}
		procent.setText(bufferSize + " frames");
	}
}
