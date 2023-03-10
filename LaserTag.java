// SWING imports
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.text.StyledEditorKit.ForegroundAction;
// AWT imports
import java.awt.*;
import java.awt.event.*;
// SQL imports
import java.sql.*;
// Other imports
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.Timer;

public class LaserTag implements ActionListener 
{
    //Controller controller;

    // Team player names
    private static ArrayList<Player> redPlayers = new ArrayList<Player>();
    private static ArrayList<Player> greenPlayers = new ArrayList<Player>();

    // Variables
    private JTextField textField;
    final private static Font mainFont = new Font("Dialog", Font.PLAIN, 15); // Main text
    final private static Font welcomeFont = new Font("Dialog", Font.BOLD, 40); // Welcome label
    final private static Font instructFont = new Font("Dialog", Font.PLAIN, 20); // Instruction label

    // Laser Tag Constructor
    public LaserTag(JTextField textField, ArrayList<Player> redTeam, ArrayList<Player> greenTeam) 
    {
        this.textField = textField;
        this.redPlayers = redTeam;
        this.greenPlayers = greenTeam;
    }

    //MAIN FUNCTION ============================================================================
    public static void main(String[] args) throws InterruptedException
    {  
        // creates database connection object  
		Connection db = getConnection();
        
        // Create instance of JFrame
        JFrame frame = new JFrame("LASER TAG");
        
        //Open splash screen for 3 seconds and close
        JWindow splashScreen = createSplashScreen(frame);
        Thread.sleep(2000); //2 secs 
        splashScreen.setVisible(false);

        // Create frame and add layout
        createFrame(frame);
        JPanel playerEntryPanel = createPlayerEntry();
        JPanel actionDisplayPanel = createActionDisplay();
        setLayout(frame, playerEntryPanel, actionDisplayPanel);

        //Show frame
        frame.setVisible(true);
    }

    // METHODS =================================================================================================
    // Method that connects to server and returns connection
	public static Connection getConnection() {
        Connection conn = null;
		String url = "jdbc:postgresql://ec2-54-86-224-85.compute-1.amazonaws.com:5432/d7o8d02lik98h5?sslmode=require&user=uyxzxuqnymgnca&password=28ac4c9bcc607991c066ccdcb5bc72e1fac7f43dc34d02d0dd68262bc29db8a1";
		try {
			// Attempts to get connection from heroku
			conn = DriverManager.getConnection(url);
			
			// Checks to see if connection was successful
			if (conn != null) {
				System.out.println("Connected to database");
			} else {
				System.out.println("Failed to connect");
			}
		
		}
		// Checks for SQL Exceptions
		catch (SQLException e){
			e.printStackTrace(System.err);
		}
		// Returns the heroku Connection
		return conn;
	}
    
    // Method to create splash screen
    public static JWindow createSplashScreen(JFrame frame) throws InterruptedException
    {
        // Creating image
        Image originalImage = Toolkit.getDefaultToolkit().getImage("PhotonLogo.jpg");
        
        // Scale down image
        Image scaledImage = originalImage.getScaledInstance(660,308,Image.SCALE_SMOOTH);
        JLabel imageLabel = new JLabel(new ImageIcon(scaledImage));
        frame.add(imageLabel);

        // Creating window splashscreen
        JWindow splashScreen = new JWindow();
        splashScreen.add(imageLabel); // Upload image
        splashScreen.pack();
        splashScreen.setLocationRelativeTo(null);
        
        // Sizing and centering the screen
        splashScreen.setSize(660,308); 
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int centerX = (screenSize.width - splashScreen.getWidth())/2;
        int centerY = (screenSize.height - splashScreen.getHeight())/2;
        splashScreen.setLocation(centerX,centerY);
        splashScreen.setVisible(true);
    
        return splashScreen;
    }

    // Method to create frame for application
    public static void createFrame(JFrame frame) throws InterruptedException
    {
        // Loading JFrame window
        frame.setSize(1920, 1080); 
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.getContentPane().setBackground(Color.darkGray);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    // PLAYER ENTRY SCREEN ================================================================
    public static JPanel createPlayerEntry()
    {
        /******************************** Text Panel ********************************/
        // Create textLabel for welcome text
        JLabel welcomeLabel = new JLabel("Welcome to Photon Main");
        JLabel instructLabel = new JLabel("Enter in your ID and Codename, then get ready to play!");
        welcomeLabel.setFont(welcomeFont); // Set fonts
        instructLabel.setFont(instructFont);
        welcomeLabel.setForeground(Color.white); // Set text color
        instructLabel.setForeground(Color.white);
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER); // Center text
        instructLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Create textPanel and add elements to it
        JPanel textPanel = new JPanel();
        textPanel.setOpaque(false);
        textPanel.setLayout(new GridLayout(2, 1, 5, 5));
        textPanel.add(welcomeLabel);
        textPanel.add(instructLabel);
        textPanel.setBorder(BorderFactory.createEmptyBorder(50, 0, 50, 0)); // Add emptyBorder for space);

        /************************* Boxes Panel ****************************/
        // Vertical boxes to hold horizontal box for name and id
        Box vbox1name = Box.createVerticalBox();
        Box vbox1id = Box.createVerticalBox();
        Box vbox2name = Box.createVerticalBox();
        Box vbox2id = Box.createVerticalBox();
    
        // Add boxes for each teams' 15 players
        for (int i = 0; i < 15; i++)
        {
            if (i <= 8)
            {
                addHorizontalBox(vbox1name, "Player " + (i+1) + "     ID: ", redPlayers, greenPlayers);
                addHorizontalBox(vbox1id, "   Codename: ", redPlayers, greenPlayers);
                addHorizontalBox(vbox2name, "Player " + (i+1) + "     ID: ", redPlayers, greenPlayers);
                addHorizontalBox(vbox2id, "   Codename: ", redPlayers, greenPlayers);
            }
            else
            {
                addHorizontalBox(vbox1name, "Player " + (i+1) + "   ID: ", redPlayers, greenPlayers);
                addHorizontalBox(vbox1id, "   Codename: ", redPlayers, greenPlayers);
                addHorizontalBox(vbox2name, "Player " + (i+1) + "   ID: ", redPlayers, greenPlayers);
                addHorizontalBox(vbox2id, "   Codename: ", redPlayers, greenPlayers);
            }
            
        }
        
        // Horizontal boxes to hold the columns
        // Set as red, strut, and green boxes for panel segmentation
        Box hboxRed = Box.createHorizontalBox();
        Box hboxStrut = Box.createHorizontalBox();
        Box hboxGreen = Box.createHorizontalBox();
        hboxRed.add(vbox1name);
        hboxRed.add(vbox1id);
        hboxStrut.add(Box.createHorizontalStrut(10));
        hboxGreen.add(vbox2name);
        hboxGreen.add(vbox2id);
        
        // Add color to team panels
        JPanel redTeamPanel = new JPanel();
        redTeamPanel.add(hboxRed);
        redTeamPanel.setBackground(new Color(133, 3, 3));
        JPanel greenTeamPanel = new JPanel();
        greenTeamPanel.add(hboxGreen);
        greenTeamPanel.setBackground(new Color(14, 115, 2));

        // ACreate playerEntryPanel
        JPanel boxesPanel = new JPanel();
        boxesPanel.setOpaque(false);
        boxesPanel.add(redTeamPanel);
        boxesPanel.add(hboxStrut);
        boxesPanel.add(greenTeamPanel);


        /******************************** Button Panel ********************************/
        // Create start game button
        JButton button = new JButton("[F5] Start Game");
        button.setFont(mainFont);
        button.setPreferredSize(new Dimension(200, 50));

        // Add action listener to button
        button.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e) 
            {
                buttonMethod();
            }
        });
        // Make F5 key activate button
        button.addKeyListener(new KeyAdapter() 
        {
            public void keyPressed(KeyEvent e) 
            {
                System.out.println("1");
                if (e.getKeyCode() == KeyEvent.VK_F5) 
                {
                    buttonMethod();
                }
            }
        });

        // Create buttonPanel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.add(button);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 50, 0)); // Add emptyBorder for space

        /******************************** Player Entry Panel ********************************/
        JPanel playerEntryPanel = new JPanel();
        playerEntryPanel.setLayout(new BorderLayout());
        playerEntryPanel.setBackground(Color.darkGray);
        playerEntryPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        playerEntryPanel.add(textPanel, BorderLayout.NORTH);
        playerEntryPanel.add(boxesPanel, BorderLayout.CENTER);
        playerEntryPanel.add(buttonPanel, BorderLayout.SOUTH);

        return playerEntryPanel;
    }

    // ACTION DISPLAY SCREEN ================================================================================
    // Method to create actionDisplayPanel
    public static JPanel createActionDisplay()
    {
        /******************************** Teams Panel ********************************/
        // Red team: Create panels, boxes, and labels
        JPanel redTeam = new JPanel();
        Box vboxRedTeam = Box.createVerticalBox();
        Box hboxRedTeam = Box.createHorizontalBox();
        JLabel redTitleLabel = new JLabel("RED TEAM");
        // Set label attributes
        redTitleLabel.setFont(welcomeFont);
        redTitleLabel.setForeground(Color.red);
        redTitleLabel.setHorizontalAlignment(SwingConstants.LEFT); // Not working for some reason
        // Boxes
        hboxRedTeam.add(redTitleLabel);
        vboxRedTeam.add(hboxRedTeam);
        // set panel attributes
        redTeam.setPreferredSize(new Dimension(600, 500));
        redTeam.setBorder(BorderFactory.createLineBorder(Color.red));
        redTeam.setOpaque(false);
        redTeam.add(hboxRedTeam);
        redTeam.add(vboxRedTeam);
        // Loop to add player info as labels to team red team panel
            // Import data
            // Store data in label
            // Format label
            // Add label to box
        

        // Green team: Create panels, boxes, and labels
        JPanel greenTeam = new JPanel();
        Box vboxGreenTeam = Box.createVerticalBox();
        Box hboxGreenTeam = Box.createHorizontalBox();
        JLabel greenTitleLabel = new JLabel("GREEN TEAM");
        // Set label attributes
        greenTitleLabel.setFont(welcomeFont);
        greenTitleLabel.setForeground(Color.green);
        greenTitleLabel.setHorizontalAlignment(SwingConstants.LEFT); // Not working for some reason
        // Boxes
        hboxGreenTeam.add(greenTitleLabel);
        vboxGreenTeam.add(hboxGreenTeam);
        // Set panel attributes
        greenTeam.setPreferredSize(new Dimension(600, 500));
        greenTeam.setBorder(BorderFactory.createLineBorder(Color.green));
        greenTeam.setOpaque(false);
        greenTeam.add(hboxGreenTeam);
        greenTeam.add(vboxGreenTeam);
        // Loop to add player info as labels to team green team panel
            // Import data
            // Store data in label
            // Format label
            // Add label to box


        // Create teamsPanel and add elements
        JPanel teamsPanel = new JPanel();
        teamsPanel.setOpaque(false);
        teamsPanel.add(greenTeam);
        teamsPanel.add(redTeam);

        /******************************** Game Panel ********************************/
        JLabel gameTitleLabel = new JLabel("GAME ACTION");
        gameTitleLabel.setFont(welcomeFont);
        gameTitleLabel.setForeground(Color.pink);
        gameTitleLabel.setHorizontalAlignment(SwingConstants.LEFT);

        // Create gamePanel and add elements
        JPanel gamePanel = new JPanel();
        gamePanel.setPreferredSize(new Dimension(1200, 200));
        gamePanel.setBorder(BorderFactory.createLineBorder(Color.pink));
        gamePanel.setOpaque(false);
        gamePanel.add(gameTitleLabel);


        /******************************** Action Display Panel ********************************/
        JLabel actionLabel = new JLabel("Action Display");
        actionLabel.setFont(instructFont);
        actionLabel.setForeground(Color.white);
        actionLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Create actionDisplayPanel and add elements
        JPanel actionDisplayPanel = new JPanel();
        actionDisplayPanel.setLayout(new BorderLayout());
        actionDisplayPanel.setBackground(Color.darkGray);
        actionDisplayPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
        actionDisplayPanel.add(actionLabel, BorderLayout.NORTH);
        actionDisplayPanel.add(teamsPanel, BorderLayout.CENTER);
        actionDisplayPanel.add(gamePanel, BorderLayout.SOUTH);
        
        return actionDisplayPanel;
    }

    // Set panels from layout to card panel and add to frame
    public static void setLayout(JFrame frame, JPanel playerEntryPanel, JPanel actionDisplayPanel)
    {
        /******************************** Card Panel ********************************/
        JPanel cardPanel = new JPanel(new CardLayout());
        //cardPanel.add(playerEntryPanel, "Panel 1");
        cardPanel.add(actionDisplayPanel, "Panel 2");

        // Add cardPanel to frame
        frame.add(cardPanel);
    }

    // // Method to change screens
    // public static void changeCard(JPanel cardPanel)
    // {

    // }

    // Method to create and add a horizontal box to a vertical box
    private static JTextField addHorizontalBox(Box vbox, String labelText, ArrayList<Player> redPlayers, ArrayList<Player> greenPlayers)
    {
        // Create boxes
        Box hbox = Box.createHorizontalBox(); // Create box
        JLabel label = new JLabel(labelText); // Add label text
        label.setFont(mainFont);
        label.setForeground(Color.white); // Set text color to white
        hbox.add(label); // Add label
        hbox.add(Box.createHorizontalStrut(10)); // Add space for label
        JTextField textField = new JTextField(10); // Add text field
        // Add action listener to each text field
        LaserTag listener = new LaserTag(textField, redPlayers, greenPlayers); // Allows enter to be used to add players 
        textField.addActionListener(listener);                                 // from text fields into respective arrays
        hbox.add(textField); // Add textfield to hbox
        
        // Creates player objects and adds them to their respective arraylists
        if (labelText.contains("Red"))
        {
            // Creates player object with a textField and adds it to the redPlayer arraylist
            Player player = new Player(textField, null, labelText.substring(12), "Red");
            redPlayers.add(player);
        }
        else if (labelText.contains("Green"))
        {
            // Creates player object with a textField and adds it to the greenPlayer arraylist
            Player player = new Player(textField, null, labelText.substring(14), "Green");
            greenPlayers.add(player);
        }

        vbox.add(hbox); // Add hbox to vbox

        return textField;
    }

    // Method for when button is pressed
    public static void buttonMethod()
    {
        System.out.println("BUTTON METHOD");
        //changeCard();
        printTeams();
    }

    // Method that creates a countdown timer
    public static void countdownTimer()
    {
        Timer timer = new Timer();
        
    }

    // Method to print out array lists of players names
    public static void printTeams()
    {
        // Print out red team names and IDs
        System.out.println("--------------------------------------\n");
        System.out.println(" * Red Team Player Names: ");
        for (int i = 0; i < redPlayers.size(); i++)
        {
            System.out.println("\t * Player " + i + ": " + redPlayers.get(i) );
        }
        
        //Print out green team names and IDs
        System.out.println("\n--------------------------------------\n");
        System.out.println(" * Green Team Player Names: ");
        for (int i = 0; i < greenPlayers.size(); i++)
        {
            System.out.println("\t -> Player " + i + ": " + greenPlayers.get(i) );
        }
        
        System.out.println("\n");
    }

    // ACTION PERFORMED =========================================================================================================
    public void actionPerformed(ActionEvent e)
	{
        if (e.getSource() == textField) 
        {
            // Handle the event triggered by the Enter key being pressed
            String text = textField.getText();

            // String text = textField.getText();
            // if ( textField.getX() == 94 || textField.getX() == 102 )
            //     redPlayers.add(new Player(textField, text, null, "Red"));
            // else
            //     greenPlayers.add(new Player(textField, text, null, "Green"));
            // System.out.println("\n * textField.getLocationOnScreen() for " + text + " = " + textField.getLocationOnScreen());
        }
	}

}