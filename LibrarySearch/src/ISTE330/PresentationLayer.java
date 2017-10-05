package ISTE330;

import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import javax.swing.*;
import java.awt.*;

/**
 * @author Willy Choi
 * @author Austin Levesque
 * @author Aurko Mallick
 * @author Nick Taber
 * 
 */

public class PresentationLayer extends JFrame{
	/*
	this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

    this.addWindowListener(new WindowListener() {
        @Override
        public void windowClosing(WindowEvent e) {
        	bl.closeConnection();
            System.exit(0);
        }
    }
	 */

	/**
	 * Sets the default JFrame
	 */
	public PresentationLayer(){


		bl = new BusinessLayer();

		/*btnHome = new JButton("Home");
		btnBack = new JButton("Back");
		btnHome.addActionListener(new actionListener());
		btnBack.addActionListener(new actionListener());
		*/

		createLoginPanel();
		//createResultsPanel();
		//createUsersPanel();
		//createLandingPanel();
		//createPaperPanel();
		//createAddPaperPanel();
		changePanel("LOGIN");
		//changePanel("LANDING");
		//changePanel("PAPER");
		//changePanel("ADDPAPER");
		//changePanel("RESULT");00000
	}

	/**
	 * Changes the current panel to the requested one
	 * 
	 * @param newPanel
	 * 			"RESULT" for result page
	 * 			"LOGIN" for login page
	 * 			"LANDING" for landing page
	 * 			"PAPER" for paper page
	 * 			"ADDPAPER" for add paper page
	 * 		
	 */
	private void changePanel(String newPanel){
		//Remove current panel
		if(currentPanel == "RESULT"){
			lastPanel = "RESULT";
			this.remove(resultPanel);
		}
		else if(currentPanel == "USERS"){
			lastPanel = "USERS";
			this.remove(usersPanel);
		}
		else if(currentPanel == "LOGIN"){
			lastPanel = "LOGIN";
			this.remove(loginPanel);
		}
		else if(currentPanel == "LANDING"){
			lastPanel = "LANDING";
			this.remove(landingPanel);
		}
		else if(currentPanel == "PAPER"){
			lastPanel = "PAPER";
			this.remove(paperPanel);
		}
		else if(currentPanel == "ADDPAPER"){
			lastPanel = "ADDPAPER";
			this.remove(addPaperPanel);
		}

		//Add new panel
		if(newPanel == "RESULT"){
			this.setContentPane(resultPanel);
			currentPanel = "RESULT";
		}
		else if(newPanel == "USERS"){
			this.setContentPane(usersPanel);
			currentPanel = "USERS";
		}
		else if(newPanel == "LOGIN"){
			this.setContentPane(loginPanel);
			currentPanel = "LOGIN";
		}
		else if(newPanel == "LANDING"){
			this.setContentPane(landingPanel);
			currentPanel = "LANDING";
		}
		else if(newPanel == "PAPER"){
			this.setContentPane(paperPanel);
			currentPanel = "PAPER";
		}
		else if(newPanel == "ADDPAPER"){
			this.setContentPane(addPaperPanel);
			currentPanel = "ADDPAPER";
		}
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		/*
		this.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				bl.closeConnection();
			}
		});
		 */
		this.setVisible(true);
		repaint();
		this.pack();
		this.setLocationRelativeTo(null);
	}

	/**
	 * Switches to last panel (for the back button)
	 * 
	 * @return
	 */
	private void lastPanel(){
		changePanel(lastPanel);
	}

	/**
	 * Returns the current panel
	 * 
	 * @return
	 */
	private String curPanel(){
		return currentPanel;
	}

	/**
	 * Generates the initial search results page
	 * 
	 */
	private void createResultsPanel(){
		btnResSearch = new JButton("Search");
		btnResAdd = new JButton("Add New Paper");
		btnResBack = new JButton("Back");
		btnResDel = new JButton("Delete Selected Paper");

		resultPanel = new JPanel(new BorderLayout());
		JPanel pnlCenter = new JPanel();

		//Labels
		JLabel lblPapers = new JLabel("Papers");
		JLabel lblSearch = new JLabel("Search for:");
		JLabel lblTitle = new JLabel("Title");
		JLabel lblAuth = new JLabel("Author");

		//TextFields
		txtResSearch = new JTextField(30);
		txtResSearch.setMaximumSize(txtResSearch.getPreferredSize());

		//Search Panel
		pnlResSearch = new JPanel();
		pnlResSearch.setLayout(new BoxLayout(pnlResSearch, BoxLayout.Y_AXIS));
		pnlResSearch.add(lblSearch);
		pnlResSearch.add(txtResSearch);

		JLabel lblRadioSearch = new JLabel("Search by:");
		radioTitle = new JRadioButton("Title");
		radioTitle.setSelected(true);
		radioAuthor = new JRadioButton("Author");
		radioKeyword = new JRadioButton("Keyword");
		ButtonGroup group = new ButtonGroup();
		group.add(radioTitle);
		group.add(radioAuthor);
		group.add(radioKeyword);

		pnlResSearch.add(lblRadioSearch);
		pnlResSearch.add(radioTitle);
		pnlResSearch.add(radioAuthor);
		pnlResSearch.add(radioKeyword);

		pnlResSearch.add(btnResSearch);
		pnlResSearch.add(btnResBack);
		pnlResSearch.add(btnResAdd);
		pnlResSearch.add(btnResDel);

		//Result Panel
		pnlResult = new JPanel();
		pnlResult.setLayout(new BoxLayout(pnlResult, BoxLayout.Y_AXIS));
		pnlResult.add(lblPapers);

		String[] columnNames = {"Last Name",
				"First Name",
				"Paper Title",
				"Serial Number"
		};

		ArrayList<ArrayList<String>> arrayList = bl.searchTable("", 0);
		String[][] data = convertArray(arrayList);
		paperTable = new JTable(data, columnNames); 

		paperTable.setPreferredScrollableViewportSize(new Dimension(500, 70));
		paperTable.setFillsViewportHeight(true);
		JScrollPane scrollPane = new JScrollPane(paperTable);

		//pnlResult.add(scrollPane, BorderLayout.CENTER);
		//pnlResult.add(scrollPane);

		//pnlCenter.setLayout(new GridLayout(0,2));
		//pnlCenter.add(pnlSearch);
		//pnlCenter.add(pnlResult);

		resultPanel.add(pnlResSearch,BorderLayout.WEST);
		resultPanel.add(scrollPane, BorderLayout.CENTER);

		btnResBack.addActionListener(new actionListener());
		btnResSearch.addActionListener(new actionListener());
		btnResAdd.addActionListener(new actionListener());
		btnResDel.addActionListener(new actionListener());
	}
	
	/**
	 * Generates the initial user search page
	 * 
	 */
	private void createUsersPanel(){
		btnUSearch = new JButton("Search");
		btnBack = new JButton("Back");
		//btnUAdd = new JButton("Add User");

		usersPanel = new JPanel(new BorderLayout());
		JPanel pnlCenter = new JPanel();

		//Labels
		JLabel lblSearch = new JLabel("Search for:");

		//TextFields
		txtUSearch = new JTextField(30);
		txtUSearch.setMaximumSize(txtUSearch.getPreferredSize());

		//Search Panel
		pnlUSearch = new JPanel();
		pnlUSearch.setLayout(new BoxLayout(pnlUSearch, BoxLayout.Y_AXIS));
		pnlUSearch.add(lblSearch);
		pnlUSearch.add(txtUSearch);

		pnlUSearch.add(btnUSearch);
		pnlUSearch.add(btnBack);

		// Users Panel
		pnlUsers = new JPanel();
		pnlUsers.setLayout(new BoxLayout(pnlResult, BoxLayout.Y_AXIS));

		String[] columnNames = {"Last Name",
				"First Name",
				"email"
		};

		ArrayList<ArrayList<String>> arrayList = bl.searchTable("", 1);
		String[][] data = convertArray(arrayList);
		userTable = new JTable(data, columnNames); 
		userTable.setEnabled(false);

		paperTable.setPreferredScrollableViewportSize(new Dimension(500, 70));
		paperTable.setFillsViewportHeight(true);
		JScrollPane scrollPane = new JScrollPane(userTable);

		//pnlResult.add(scrollPane, BorderLayout.CENTER);
		//pnlResult.add(scrollPane);

		//pnlCenter.setLayout(new GridLayout(0,2));
		//pnlCenter.add(pnlSearch);
		//pnlCenter.add(pnlResult);

		usersPanel.add(pnlUSearch,BorderLayout.WEST);
		usersPanel.add(scrollPane, BorderLayout.CENTER);

		btnBack.addActionListener(new actionListener());
		btnUSearch.addActionListener(new actionListener());
	}
	

	/**
	 * Generates the login page
	 * 
	 */
	private void createLoginPanel(){
		btnLogSubmit = new JButton("Submit");
		//btnLogFPass = new JButton("Forgot Password");

		loginPanel = new JPanel(new BorderLayout());
		JPanel pnlNorth = new JPanel();
		JPanel pnlSouth = new JPanel();
		JPanel pnlCenter = new JPanel();

		//Labels
		JLabel lblDatabase = new JLabel("Faculty Research Database");
		JLabel lblLogin = new JLabel("Login");
		JLabel lblUser = new JLabel("Email");
		JLabel lblPass = new JLabel("Password");

		//TextFields
		txtUser = new JTextField(20);
		txtPass = new JTextField(20);

		//North
		lblDatabase.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblLogin.setAlignmentX(Component.CENTER_ALIGNMENT);

		pnlNorth.setLayout(new BoxLayout(pnlNorth, BoxLayout.Y_AXIS));
		pnlNorth.add(lblDatabase);
		pnlNorth.add(lblLogin);

		//South
		pnlSouth.add(btnLogSubmit);
		//pnlSouth.add(btnLogFPass);

		//Center
		JPanel pnlUser = new JPanel();
		pnlUser.add(lblUser);
		pnlUser.add(txtUser);

		JPanel pnlPass = new JPanel();
		pnlPass.add(lblPass);
		pnlPass.add(txtPass);

		pnlCenter.setLayout(new BoxLayout(pnlCenter, BoxLayout.Y_AXIS));
		pnlCenter.add(pnlUser);
		pnlCenter.add(pnlPass);

		// Setup Main Frame
		loginPanel.add(pnlNorth, BorderLayout.NORTH);
		loginPanel.add(pnlSouth, BorderLayout.SOUTH);
		loginPanel.add(pnlCenter, BorderLayout.CENTER);

		//Add action listeners
		btnLogSubmit.addActionListener(new actionListener());
		//btnLogFPass.addActionListener(new actionListener());

	}

	/**
	 * Generates the initial paper page
	 * 
	 */
/*	private void createPaperPanel(){
		btnPapSearch = new JButton("Search");
		btnPapAdd = new JButton("Add New Paper");

		paperPanel = new JPanel(); //create Frame
		//JPanel pnlNorth = new JPanel(); // North quadrant 
		//JPanel pnlSouth = new JPanel(); // South quadrant
		//JPanel pnlEast = new JPanel(); // East quadrant
		//JPanel pnlWest = new JPanel(); // West quadrant
		JPanel pnlCenter = new JPanel(); // Center quadrant

		//Labels
		JLabel lblPapers = new JLabel("Papers");
		JLabel lblSearch = new JLabel("Search By");
		JLabel lblTitle = new JLabel("Title");
		JLabel lblAuth = new JLabel("Author");
		JLabel lblDesc = new JLabel("Brief Description");
		btnBack = new JButton("Back");

		//TextFields
		JTextField txtTitle = new JTextField(20);
		JTextField txtAuth = new JTextField(20);

		//TextArea
		JTextArea areatxtPaper = new JTextArea(10,10);

		//Search Panel
		JPanel pnlSearch = new JPanel();
		pnlSearch.setLayout(new BoxLayout(pnlSearch, BoxLayout.Y_AXIS));
		pnlSearch.add(lblTitle);
		pnlSearch.add(lblAuth);
		pnlSearch.add(lblDesc);
		pnlSearch.add(btnBack);

		//Result Panel
		JPanel pnlResult = new JPanel();
		pnlResult.setLayout(new BoxLayout(pnlResult, BoxLayout.Y_AXIS));
		pnlResult.add(lblPapers);

		//Scrollbar for TextArea
		JScrollPane scrollPane = new JScrollPane(areatxtPaper);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		pnlResult.add(scrollPane);

		//Add all panels together   
		pnlCenter.setLayout(new GridLayout(0,2));
		pnlCenter.add(pnlSearch);
		pnlCenter.add(pnlResult);

		/*
		  TableCellRenderer buttonRenderer = new JTableButtonRenderer();
		        table.getColumn("Blah3").setCellRenderer(buttonRenderer);
		 

		// Setup Main Frame
		paperPanel.setLayout(new BorderLayout());
		paperPanel.add(pnlCenter, BorderLayout.CENTER);

		btnPapSearch.addActionListener(new actionListener());
		btnPapAdd.addActionListener(new actionListener());
	} */

	/**
	 * Generates the initial landing page
	 * 
	 */
	private void createLandingPanel(){
		//btnLanInfo = new JButton("Edit Information");
		//btnLanAuth = new JButton("Browse by Author");
		btnLanPaper = new JButton("Browse by Paper");
		btnLanUsers = new JButton("Browse Users");
		btnLanAdd = new JButton("Add a Paper");

		//Initialize all swing objects.
		landingPanel = new JPanel(); //create Frame
		JPanel pnlNorth = new JPanel(); // North quadrant 
		pnlLanSouth = new JPanel(); // South quadrant
		pnlLanEast = new JPanel(); // East quadrant
		JPanel pnlWest = new JPanel(); // West quadrant
		JPanel pnlCenter = new JPanel(); // Center quadrant

		//User Panel
		pnlLanUser = new JPanel();
		pnlLanUser.setLayout(new BoxLayout(pnlLanUser, BoxLayout.Y_AXIS));

		//Author Panel
		JPanel pnlAuth = new JPanel();
		pnlAuth.setLayout(new BoxLayout(pnlAuth, BoxLayout.Y_AXIS));
		// pnlAuth.add(lblAuth);
		//pnlAuth.add(btnLanAuth);

		//JPanel pnlUser = new JPanel();
		pnlWest.add(pnlLanUser);
		pnlWest.add(pnlAuth);

		//Paper Panel
		JPanel pnlPaper = new JPanel();
		pnlPaper.setLayout(new BoxLayout(pnlPaper, BoxLayout.Y_AXIS));
		// pnlPaper.add(lblPaper);
		pnlPaper.add(btnLanPaper);
		
		// THIS SHOULD NOT BE HERE - MOVE IT SOMEWHERE ELSE
		pnlPaper.add(btnLanUsers);

		//AddPaper Panel
		pnlLanAdd = new JPanel();
		pnlLanAdd.setLayout(new BoxLayout(pnlLanAdd, BoxLayout.Y_AXIS));
		// pnlLanAdd.add(lblAdd);
		pnlLanAdd.add(btnLanAdd);

		pnlLanSouth.setLayout(new GridLayout(0,3));
		pnlLanSouth.add(pnlAuth);
		pnlLanSouth.add(pnlPaper);

		// Setup Main Frame
		landingPanel.setLayout(new BorderLayout());
		landingPanel.add(pnlLanSouth, BorderLayout.SOUTH);
		landingPanel.add(pnlWest, BorderLayout.WEST);
		landingPanel.add(pnlLanEast, BorderLayout.EAST);
		landingPanel.add(pnlCenter, BorderLayout.CENTER);

		//btnLanInfo.addActionListener(new actionListener());
		//btnLanAuth.addActionListener(new actionListener());
		btnLanPaper.addActionListener(new actionListener());
		btnLanAdd.addActionListener(new actionListener());
		btnLanUsers.addActionListener(new actionListener());
	}

	//move these with the other public things at the bottom
	private JTextArea txtAddFName, txtAddLName, txtAddEmail, txtAddPassword;
	private JButton btnAddSubmit;
	private JLabel lblAddFName,lblAddLName,lblAddEmail,lblAddPassword, lblAddUser;
	private JPanel pnlAddUser;
	private void addUserPanel(){
		pnlAddUser = new JPanel();

		txtAddFName = new JTextArea("First Name");
		txtAddLName = new JTextArea("Last Name");
		txtAddEmail = new JTextArea("Email");
		txtAddPassword = new JTextArea("Password");

		lblAddUser = new JLabel("Adding New User");
		lblAddFName = new JLabel("First Name");
		lblAddLName = new JLabel("Last Name");
		lblAddEmail = new JLabel("Email");
		lblAddPassword = new JLabel("Password");

		btnAddSubmit = new JButton("Submit");

		pnlAddUser.add(lblAddUser);
		pnlAddUser.add(lblAddFName);
		pnlAddUser.add(txtAddFName);
		pnlAddUser.add(lblAddLName);
		pnlAddUser.add(txtAddLName);
		pnlAddUser.add(lblAddEmail);
		pnlAddUser.add(txtAddEmail);
		pnlAddUser.add(lblAddPassword);
		pnlAddUser.add(txtAddPassword);
		pnlAddUser.add(btnAddSubmit);

		btnAddSubmit.addActionListener(new actionListener());
	}

	private JPanel pnlUser;
	private JLabel lblUsers;
	private void showUserPanel(){
		pnlUser = new JPanel();
		pnlUser.setLayout(new BoxLayout(pnlResult, BoxLayout.Y_AXIS));
		pnlUser.add(lblUsers);

		String[] columnNames = {"First Name",
				"Last Name",
				"Email"
		};

		ArrayList<ArrayList<String>> arrayList = bl.searchTable("", 0);
		String[][] data = convertArray(arrayList);
		paperTable = new JTable(data, columnNames); 

		paperTable.setPreferredScrollableViewportSize(new Dimension(500, 70));
		paperTable.setFillsViewportHeight(true);
		JScrollPane scrollPane = new JScrollPane(paperTable);

		pnlUser.add(scrollPane);
	}
	/**
	 * updates the tables with values
	 * @param arrayList values
	 * @param panelType 0 - papers, 1 - users
	 */
	private void updateTable(ArrayList<ArrayList<String>> arrayList, int panelType){

		ArrayList<String> columnNames = new ArrayList<String>();
		if(panelType == 0){
			columnNames.add("Last Name");
			columnNames.add("First Name");
			columnNames.add("Paper Title");
			columnNames.add("Serial Number");
			
		}
		else if(panelType == 1){
			columnNames.add("Last Name");
			columnNames.add("First Name");
			columnNames.add("email");
		}

		String[][] data = convertArray(arrayList);
		paperTable = new JTable(data, columnNames.toArray()); 
		paperTable.setPreferredScrollableViewportSize(new Dimension(500, 70));
		paperTable.setFillsViewportHeight(true);
		JScrollPane scrollPane = new JScrollPane(paperTable);

		if(panelType == 0){
			//Have to remove everything and recreate it to update
			//I tried multiple other ways only this one worked
			resultPanel.removeAll();
			resultPanel.add(pnlResSearch,BorderLayout.WEST);
			resultPanel.add(scrollPane, BorderLayout.CENTER);
			resultPanel.updateUI();
			resultPanel.repaint();
		}
		else if(panelType == 1){
			//Do the same as above except for the other panel
			usersPanel.removeAll();
			usersPanel.add(pnlUSearch, BorderLayout.WEST);
			usersPanel.add(scrollPane, BorderLayout.CENTER);
			usersPanel.updateUI();
			usersPanel.repaint();
		}
	}
	/**
	 * Function converts a 2d string arraylist into a form that is useable
	 * by JTable
	 * 
	 * @param arrayList
	 * @return
	 */
	private String[][] convertArray(ArrayList<ArrayList<String>> arrayList){
		//System.out.println("works");
		//System.out.println(arrayList.size());
		String[][] array = new String[arrayList.size()][arrayList.get(0).size()];

		for (int i = 0; i < arrayList.size(); i++) {
			ArrayList<String> row = arrayList.get(i);
			for (int a = 0; a < row.size(); a++) {
				array[i][a] = row.get(a);
				//System.out.println(row);
			}
			//System.out.println(row);
			//array[i] = row.toArray(new String[row.size()]);
		}
		return array;
	}

	/**
	 * Generates the initial paper page
	 * 
	 */
	private void createAddPaperPanel(){
		btnPapSave = new JButton("Save");
		btnPapCancel = new JButton("Cancel");

		addPaperPanel = new JPanel(); //create Frame
		JPanel pnlNorth = new JPanel(); // North quadrant 
		JPanel pnlSouth = new JPanel(); // South quadrant
		JPanel pnlEast = new JPanel(); // East quadrant
		JPanel pnlWest = new JPanel(); // West quadrant
		JPanel pnlCenter = new JPanel(); // Center quadrant

		//Labels
		JLabel lblTitle = new JLabel("Title");
		JLabel lblAuth = new JLabel("Author");
		JLabel lblDesc = new JLabel("Abstract");
		JLabel lblCit = new JLabel("Citation");
		JLabel lblKeywords = new JLabel("Keywords (separate by comma)");

		//TextFields
		txtTitle = new JTextField(30);
		txtAuth = new JTextField(30);
		txtDesc = new JTextField(50);
		txtCit = new JTextField(50);
		txtKeywords = new JTextField(50);

		//Search Panel
		JPanel pnlSearch = new JPanel();
		pnlSearch.setLayout(new BoxLayout(pnlSearch, BoxLayout.Y_AXIS));
		pnlSearch.add(lblTitle);
		pnlSearch.add(txtTitle);
		if(bl.getCurrentUser().get_userLevel() == 3){
			pnlSearch.add(lblAuth);
			pnlSearch.add(txtAuth);
		}
		pnlSearch.add(lblDesc);
		pnlSearch.add(txtDesc);
		pnlSearch.add(lblCit);
		pnlSearch.add(txtCit);
		pnlSearch.add(lblKeywords);
		pnlSearch.add(txtKeywords);
		pnlSearch.add(btnPapSave);
		pnlSearch.add(btnPapCancel);

		//Result Panel
		JPanel pnlResult = new JPanel();
		pnlResult.setLayout(new BoxLayout(pnlResult, BoxLayout.Y_AXIS));

		//Scrollbars for TextAreas
		//JScrollPane scrollPane1 = new JScrollPane(txtDesc);
		//scrollPane1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		//pnlSearch.add(scrollPane1);
		
		//JScrollPane scrollPane2 = new JScrollPane(txtKeywords);
		//scrollPane2.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		//pnlSearch.add(scrollPane2);

		//Add all panels together   
		//pnlCenter.setLayout(new GridLayout(0,2));
		//pnlCenter.add(pnlSearch);
		//pnlCenter.add(pnlResult);

		/*
		  TableCellRenderer buttonRenderer = new JTableButtonRenderer();
		        table.getColumn("Blah3").setCellRenderer(buttonRenderer);
		 */

		// Setup Main Frame
		addPaperPanel.setLayout(new BorderLayout());
		addPaperPanel.add(pnlSearch, BorderLayout.CENTER);

		btnPapSave.addActionListener(new actionListener());
		btnPapCancel.addActionListener(new actionListener());
	}

	/**
	 * ActionListener listens to button presses
	 * 
	 */
	class actionListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent ae){
			if(ae.getSource() == btnHome){
				changePanel("LANDING");
			}
			else if(ae.getSource() == btnResSearch){
				String resSearch = txtResSearch.getText();
				ArrayList<ArrayList<String>> searchResults;
				if(radioTitle.isSelected()){
					searchResults = bl.searchTable(resSearch, 2);
				}
				else if(radioAuthor.isSelected()){
					searchResults = bl.searchTable(resSearch, 3); 
				}
				else{
					searchResults = bl.searchTable(resSearch, 4);
				}	
				
				if(!searchResults.isEmpty()){
					updateTable(searchResults, 0);
				}else {
					updateTable(bl.searchTable(resSearch, 0), 0); 
					JOptionPane.showMessageDialog(null, "No results found.");

				}
				

			}
			else if(ae.getSource() == btnUSearch){
				String uSearch = txtUSearch.getText();
				ArrayList<ArrayList<String>> uSearchResults;
				uSearchResults = bl.searchTable(uSearch, 1);
				
				if(!uSearchResults.isEmpty()){
					updateTable(uSearchResults, 1);
				}else {
					updateTable(bl.searchTable(uSearch, 0), 0);
					JOptionPane.showMessageDialog(null, "No results found.");
				}
			}
			else if(ae.getSource() == btnBack){
				lastPanel();
			}
			else if(ae.getSource() == btnResBack){
				lastPanel();
			}
			else if(ae.getSource() == btnPapCancel){
				lastPanel();
			}
			else if(ae.getSource() == btnResAdd){
				changePanel("ADDPAPER");
			}
			else if(ae.getSource() == btnLogSubmit){
				String user = txtUser.getText();
				String pass = txtPass.getText();
				if(user.equals("") && pass.equals("")){
					setLanUser(0);
				}
				else if(bl.logIn(user, pass)){//Check if login is correct
					curUser = bl.getCurrentUser();
					int userlvl = curUser.get_userLevel();
					createResultsPanel();
					createUsersPanel();
					createLandingPanel();
					//createPaperPanel();
					createAddPaperPanel();
					setLanUser(userlvl);
				}
				else{
					String st="Invalid Username or Password";
					JOptionPane.showMessageDialog(null,st);
				}

			}
			else if(ae.getSource() == btnPapSearch){

			}
			else if(ae.getSource() == btnPapAdd){
				changePanel("ADDPAPER");
			}
			else if(ae.getSource() == btnLanAuth){
				changePanel("PAPER");
			}
			else if(ae.getSource() == btnLanPaper){
				changePanel("RESULT");
			}
			else if(ae.getSource() == btnLanUsers){
				changePanel("USERS");
			}
			else if(ae.getSource() == btnLanAdd){
				changePanel("ADDPAPER");
			}
			else if(ae.getSource() == btnPapSave){
				
				String title = txtTitle.getText();
				if(title.isEmpty()) {
					JOptionPane.showMessageDialog(null, "You must enter a paper title.");
				}
				else {
					if((bl.getCurrentUser().get_userLevel() == 3) && (txtAuth.getText().isEmpty())) {
						JOptionPane.showMessageDialog(null, "You must enter an author ID.");
					}
					else {
						String desc = txtDesc.getText();
						if(desc.isEmpty()) {
							JOptionPane.showMessageDialog(null, "You must enter a description.");
						}
						else {
							String cit = txtCit.getText();
							if(cit.isEmpty()){
								cit = "N/A";
							}
							String keywords = txtKeywords.getText();
							String[] keyArr = keywords.split(",");
							ArrayList<String> content = new ArrayList<String>();
							content.add(title);
							content.add(desc);
							content.add(cit);
							String author = "a";
							System.out.println(title + ", " + desc + " " + cit + author);
							if(bl.getCurrentUser().get_userLevel() == 3){
								author = txtAuth.getText();
								content.add(author);
							}
							ArrayList<String> pkeywords = new ArrayList<String>();
							// Add the keywords to array list
							for (int i = 0; i < keyArr.length; i++){
								pkeywords.add(keyArr[i]);
							}
							bl.addPaper(content, pkeywords);
						}
					}
				}
			}
			else if(ae.getSource() == btnResDel) {
				int row = paperTable.getSelectedRow();
				String idToDelete = (String) paperTable.getValueAt(row, 3);
				int dialogButton = JOptionPane.YES_NO_OPTION;
				int dialogResult = JOptionPane.showConfirmDialog(null, "Are you sure you would like to delete the selected paper? (Cannot be undone)", "Warning", dialogButton);
				if(dialogResult == JOptionPane.YES_OPTION) {
					bl.deletePaper(idToDelete);
					
					String resSearch = txtResSearch.getText();
					ArrayList<ArrayList<String>> searchResults;
					if(radioTitle.isSelected()){
						searchResults = bl.searchTable(resSearch, 2);
					}
					else if(radioAuthor.isSelected()){
						searchResults = bl.searchTable(resSearch, 3); 
					}
					else{
						searchResults = bl.searchTable(resSearch, 4);
					}	
					
					if(!searchResults.isEmpty()){
						updateTable(searchResults, 0);
					}else {
						updateTable(bl.searchTable(resSearch, 0), 0); 
						JOptionPane.showMessageDialog(null, "No results found.");

					}
				}
			}
			else if(ae.getSource() == btnAddSubmit){
				//String text = something.getText()
				//txtAddFName, txtAddLName, txtAddEmail, txtAddPassword
				//Check if email and password meet requirements
				//Add user
			}
		}
	}

	/**
	 * Function that changes the landing page based on the user level
	 * 
	 */
	private void setLanUser(int userlvl){
		//int userlvl = curUser.get_userLevel();

		if(userlvl == 0){
			JLabel lblName = new JLabel("Public");
			pnlLanUser.add(lblName);
			changePanel("LANDING");
		}
		else if(userlvl == 1){
			JLabel lblName = new JLabel("Name");
			JLabel lblEmail = new JLabel("Email");

			String fName = curUser.get_fName();
			String lName = curUser.get_lName();
			String fullName = fName + " " + lName;
			String email = curUser.get_email();

			JLabel txtName = new JLabel(fullName);
			JLabel txtEmail = new JLabel(email);
			pnlLanUser.add(lblName);
			pnlLanUser.add(txtName);
			pnlLanUser.add(lblEmail);
			pnlLanUser.add(txtEmail);
			//updateTable();
			changePanel("LANDING");
		}
		else if(userlvl == 2){
			JLabel lblName = new JLabel("Name");
			JLabel lblEmail = new JLabel("Email");

			String fName = curUser.get_fName();
			String lName = curUser.get_lName();
			String fullName = fName + " " + lName;
			String email = curUser.get_email();

			JLabel txtName = new JLabel(fullName);
			JLabel txtEmail = new JLabel(email);

			pnlLanUser.add(lblName);
			pnlLanUser.add(txtName);
			pnlLanUser.add(lblEmail);
			pnlLanUser.add(txtEmail);

			pnlLanSouth.add(pnlLanAdd);

			//updateTable();
			changePanel("LANDING");
		}
		else if(userlvl == 3){
			JLabel lblName = new JLabel("Admin");
			pnlLanUser.add(lblName);

			btnLanAddStudent = new JButton("Add Student");
			btnLanAddFaculty = new JButton("Add Faculty");

			pnlLanEast.add(btnLanAddStudent);
			pnlLanEast.add(btnLanAddFaculty);
			pnlLanSouth.add(pnlLanAdd);
			changePanel("LANDING");
		}
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		PresentationLayer pres = new PresentationLayer(); 
	}

	/**
	 * Instance Variables
	 */
	BusinessLayer bl;
	Users curUser = null;
	private String lastPanel, currentPanel;

	private JMenuBar mb;
	private JPanel resultPanel, usersPanel, loginPanel, landingPanel, paperPanel, addPaperPanel;
	//General Buttons
	private JButton btnHome, btnBack;
	//Result Page
	private JButton btnResSearch, btnResAdd, btnResDel, btnResBack;
	private JTextField txtResSearch;
	private JRadioButton radioTitle, radioAuthor, radioKeyword;
	private JPanel pnlResult, pnlResSearch;
	// Users Page
	private JButton btnUSearch;
	private JTextField txtUSearch;
	private JPanel pnlUsers, pnlUSearch;
	
	//Login Page
	private JButton btnLogSubmit; //, btnLogFPass;
	private JTextField txtUser, txtPass;
	//Paper Page
	private JButton btnPapSearch, btnPapAdd;
	private JTable paperTable, userTable;
	//Add Paper Page
	private JButton btnPapSave, btnPapCancel;
	private JTextField txtTitle, txtAuth, txtCit, txtDesc, txtKeywords;
	//Landing Page
	private JButton btnLanAuth, btnLanPaper, btnLanUsers, btnLanAdd, btnLanAddStudent, btnLanAddFaculty;
	private JPanel pnlLanUser, pnlLanAdd, pnlLanSouth, pnlLanEast;
}
