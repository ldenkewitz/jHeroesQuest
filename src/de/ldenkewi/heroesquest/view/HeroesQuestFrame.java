package de.ldenkewi.heroesquest.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.border.LineBorder;

import com.sun.java.swing.plaf.windows.WindowsLookAndFeel;

import de.ldenkewi.heroesquest.controll.ViewCtrl;
import de.ldenkewi.heroesquest.view.listeners.HeroesQuestActionListener;

/**
 * Main JFrame that includes the 3D JPanel {@link HeroesQuestCanvas}.
 * @author Lars Denkewitz
 * @version from 14/04/2009
 */
public class HeroesQuestFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	
	private static final String RESSOURCES_JPG_DOWN 	 = ViewCtrl.RESSOURCES_GRAPHICS_ICONS + "down.jpg";
	private static final String RESSOURCES_JPG_RIGHT 	 = ViewCtrl.RESSOURCES_GRAPHICS_ICONS + "right.jpg";
	private static final String RESSOURCES_JPG_NEXT_TURN = ViewCtrl.RESSOURCES_GRAPHICS_ICONS + "btnNextTurn.jpg";
	private static final String RESSOURCES_JPG_LEFT 	 = ViewCtrl.RESSOURCES_GRAPHICS_ICONS + "left.jpg";
	private static final String RESSOURCES_JPG_UP 		 = ViewCtrl.RESSOURCES_GRAPHICS_ICONS + "up.jpg";
	private static final String RESSOURCES_JPG_ATK 		 = ViewCtrl.RESSOURCES_GRAPHICS_ICONS + "btnAtk.jpg";
	private static final String RESSOURCES_JPG_MOVE 	 = ViewCtrl.RESSOURCES_GRAPHICS_ICONS + "btnMove.jpg";
	private static final String RESSOURCES_JPG_NEXT_FIG  = ViewCtrl.RESSOURCES_GRAPHICS_ICONS + "btnNextFig.jpg";
	private static final String RESSOURCES_JPG_PREV_FIG  = ViewCtrl.RESSOURCES_GRAPHICS_ICONS + "btnPrevFig.jpg";
	
	// components frame
	private JPanel 		pnlInfo, pnlFunction;
	private JMenuBar 	menuBar;
	private JMenu 		fileMenu, helpMenu;
	private JMenuItem 	newMItem, exitMItem, optionMItem, aboutMItem, loadMItem, saveMItem;
	
	// components infoLbl
	private JLabel 	lblMap, lblPlayer, lblRound, lblRoundNr;
	
	// components functionLbl
	private JLabel 	lblFigure, lblLabel1, lblLabel2, lblLabel3, lblLabel4, lblLabel5, lblLabel6,
					lblFigurePic, lblHealthPoints, lblAtkDice, lblDefDice, lblAtkPoints, lblMovePoints;
	private JButton btnPrevFigur, btnNextFigur, btnMove, btnAttack, btnUp, btnLeft, 
					btnRight, btnDown, btnNextTurn;

	public HeroesQuestFrame() {
		// Initialize window
		super("- Heroes Quest ( v0.15 ) -");
		{try {UIManager.setLookAndFeel(new WindowsLookAndFeel());} catch (UnsupportedLookAndFeelException e) {}}
		this.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		this.setMinimumSize(new java.awt.Dimension(1024, 768));
		this.setResizable(false);
		this.getContentPane().setLayout(null);
		initComponents();
		setListeners();
		this.setVisible(true);
		setComponentsEnabled(false);
		setButtonsRequestFocusEnabled(false);
	}

	private void initComponents() {
		
		menuBar		= initMenuBar();
		pnlInfo 	= initInfoPnl();
		pnlFunction = initFunctionPnl();

		setJMenuBar(menuBar);
		add(pnlInfo);
		add(pnlFunction);
	}
	
	private JMenuBar initMenuBar() {
		JMenuBar menuBar1 = new JMenuBar();
		fileMenu 	= new JMenu();
		helpMenu 	= new JMenu();
		newMItem  	= new JMenuItem();
		optionMItem	= new JMenuItem();
		loadMItem 	= new JMenuItem();
		saveMItem 	= new JMenuItem();
		exitMItem 	= new JMenuItem();
		aboutMItem 	= new JMenuItem();
		
		fileMenu.setText("Datei");
		helpMenu.setText("Hilfe");

		newMItem.setText("Neu");
		newMItem.setActionCommand("HeroesQuestUINew");

		optionMItem.setText("Optionen");
		optionMItem.setActionCommand("HeroesQuestUIOptions");
		
		loadMItem.setText("Laden");
		loadMItem.setActionCommand("HeroesQuestUILoad");
		
		saveMItem.setText("Speichern");
		saveMItem.setActionCommand("HeroesQuestUISave");
		saveMItem.setEnabled(false);
		
		exitMItem.setText("Beenden");
		exitMItem.setActionCommand("HeroesQuestUIExit");

		aboutMItem.setText("Über");
		aboutMItem.setActionCommand("HeroesQuestUIAbout");
		
		JSeparator menuSeparator = new JSeparator();
		menuSeparator.setForeground(Color.LIGHT_GRAY);
		JSeparator menuSeparator2 = new JSeparator();
		menuSeparator.setForeground(Color.LIGHT_GRAY);
		fileMenu.add(newMItem);
		fileMenu.add(loadMItem);
		fileMenu.add(saveMItem);
		fileMenu.add(menuSeparator);
		fileMenu.add(optionMItem);
		fileMenu.add(menuSeparator2);
		fileMenu.add(exitMItem);
		helpMenu.add(aboutMItem);
		menuBar1.add(fileMenu);
		menuBar1.add(helpMenu);
		
		return menuBar1;
	}

	private JPanel initInfoPnl() {
		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setBounds(5, 5, this.getWidth() - 210, 50);
		panel.setBorder(new LineBorder(Color.GRAY));
		
		Font myFont = new Font("Old English Text MT", Font.LAYOUT_RIGHT_TO_LEFT, 24);
		
		lblPlayer = new JLabel("Spielername");
		lblPlayer.setFont(myFont);
		lblPlayer.setBounds(20, 5, 200, 40);
		lblMap = new JLabel(super.getTitle(), SwingConstants.CENTER);
		lblMap.setFont(myFont);
		lblMap.setBounds(235, 5, 400, 40);
		lblMap.setLocation(panel.getWidth()/2-lblMap.getWidth()/2, 5);
		lblRound = new JLabel("Runde:");
		lblRound.setFont(myFont);
		lblRound.setBounds(panel.getWidth()-125, 5, 100, 40);
		lblRoundNr = new JLabel("1", SwingConstants.CENTER);
		lblRoundNr.setFont(myFont);
		lblRoundNr.setBounds(panel.getWidth()-45, 5, 40, 40);
		
		panel.add(lblPlayer);
		panel.add(lblMap);
		panel.add(lblRound);
		panel.add(lblRoundNr);
		
		return panel;
	}

	private JPanel initFunctionPnl() {
		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setBounds(this.getWidth() - 200, 5, 190, this.getHeight() - 60);
		panel.setBorder(new LineBorder(Color.GRAY));
			
		Font myFont = new Font("Old English Text MT", Font.BOLD, 16);
		Font myFont2 = new Font("Old English Text MT", Font.BOLD, 22);
		
		btnPrevFigur = new JButton(new ImageIcon(RESSOURCES_JPG_PREV_FIG));
		btnPrevFigur.setBounds(10, 50, 80, 20);
		lblFigure = new JLabel("Figur1", SwingConstants.CENTER);
		lblFigure.setFont(new Font("Old English Text MT", Font.BOLD, 22));
		lblFigure.setBounds(0, 5, panel.getWidth(), 40);
		btnNextFigur = new JButton(new ImageIcon(RESSOURCES_JPG_NEXT_FIG));
		btnNextFigur.setBounds(panel.getWidth()-90, 50, 80, 20);
		JSeparator separator1 = new JSeparator();
		separator1.setBounds(5, 90, panel.getWidth() - 10, 10);
		separator1.setBackground(Color.LIGHT_GRAY);
		
		lblLabel1 = new JLabel("Körperkraft:");
		lblLabel1.setFont(myFont);
		lblLabel1.setBounds(10, 100, 150, 40);
		lblHealthPoints = new JLabel("1", SwingConstants.RIGHT);
		lblHealthPoints.setFont(myFont2);
		lblHealthPoints.setBounds(panel.getWidth()-31, 100, 25, 40);
		
		lblLabel2 = new JLabel("Angriffswürfe:");
		lblLabel2.setFont(myFont);
		lblLabel2.setBounds(10, 140, 150, 40);
		lblAtkDice = new JLabel("1", SwingConstants.RIGHT);
		lblAtkDice.setFont(myFont2);
		lblAtkDice.setBounds(panel.getWidth()-31, 140, 25, 40);
		
		lblLabel3 = new JLabel("Verteidigungswürfe:");
		lblLabel3.setFont(myFont);
		lblLabel3.setBounds(10, 180, 150, 40);
		lblDefDice = new JLabel("1", SwingConstants.RIGHT);
		lblDefDice.setFont(myFont2);
		lblDefDice.setBounds(panel.getWidth()-31, 180, 25, 40);
		
		lblLabel4 = new JLabel("Anzahl Angriffe:");
		lblLabel4.setFont(myFont);
		lblLabel4.setBounds(10, 220, 150, 40);
		lblAtkPoints = new JLabel("1", SwingConstants.RIGHT);
		lblAtkPoints.setFont(myFont2);
		lblAtkPoints.setBounds(panel.getWidth()-31, 220, 25, 40);
		
		lblLabel5 = new JLabel("Bewegungspunkte:");
		lblLabel5.setFont(myFont);
		lblLabel5.setBounds(10, 260, 150, 40);
		lblMovePoints = new JLabel("1", SwingConstants.RIGHT);
		lblMovePoints.setFont(myFont2);
		lblMovePoints.setBounds(panel.getWidth()-31, 260, 25, 40);
		JSeparator separator2 = new JSeparator();
		separator2.setBounds(5, 307, panel.getWidth() - 10, 10);
		separator2.setBackground(Color.LIGHT_GRAY);
		
		btnMove = new JButton(new ImageIcon(RESSOURCES_JPG_MOVE));
		btnMove.setFont(myFont2);
		btnMove.setBounds(25, 330, panel.getWidth() - 50, 30);
		btnAttack = new JButton(new ImageIcon(RESSOURCES_JPG_ATK));
		btnAttack.setFont(myFont2);
		btnAttack.setBounds(25, 370, panel.getWidth() - 50, 30);
		JSeparator separator3 = new JSeparator();
		separator3.setBounds(5, 420, panel.getWidth() - 10, 10);
		separator3.setBackground(Color.LIGHT_GRAY);
		
		lblLabel6 = new JLabel("Blickrichtung", SwingConstants.CENTER);
		lblLabel6.setFont(myFont2);
		lblLabel6.setBounds(0, 430, panel.getWidth(), 40);
		btnUp = new JButton(new ImageIcon(RESSOURCES_JPG_UP));
		btnUp.setBounds(panel.getWidth() / 2-40, 475, 80, 20);
		btnLeft = new JButton(new ImageIcon(RESSOURCES_JPG_LEFT));
		btnLeft.setBounds( 10, 520, 20, 80);
		btnRight = new JButton(new ImageIcon(RESSOURCES_JPG_RIGHT));
		btnRight.setBounds(panel.getWidth() -30, 515, 20, 80);
		lblFigurePic = new JLabel();
		lblFigurePic.setBounds(panel.getWidth() / 2 - 50, 505, 100, 100);
		btnDown = new JButton(new ImageIcon(RESSOURCES_JPG_DOWN));
		btnDown.setBounds(panel.getWidth() / 2-40, 615, 80, 20);
		JSeparator separator4 = new JSeparator();
		separator4.setBounds(5, 650, panel.getWidth() - 10, 10);
		separator4.setBackground(Color.LIGHT_GRAY);
		
		btnNextTurn = new JButton(new ImageIcon(RESSOURCES_JPG_NEXT_TURN));
		btnNextTurn.setFont(myFont2);
		btnNextTurn.setBounds(panel.getWidth() / 2-80, 665, 160, 30);
		
		btnPrevFigur.setActionCommand("PreviousFigure");
		btnNextFigur.setActionCommand("NextFigure");
		btnMove.setActionCommand("Move");
		btnAttack.setActionCommand("Attack");
		btnUp.setActionCommand("LookUp");
		btnLeft.setActionCommand("LookLeft");
		btnRight.setActionCommand("LookRight");
		btnDown.setActionCommand("LookDown");
		btnNextTurn.setActionCommand("NextTurn");
		
		panel.add(btnPrevFigur);
		panel.add(lblFigure);
		panel.add(btnNextFigur);
		panel.add(separator1);
		
		panel.add(lblLabel1);
		panel.add(lblHealthPoints);
		panel.add(lblLabel2);
		panel.add(lblAtkDice);
		panel.add(lblLabel3);
		panel.add(lblDefDice);
		panel.add(lblLabel4);
		panel.add(lblAtkPoints);
		panel.add(lblLabel5);
		panel.add(lblMovePoints);
		panel.add(separator2);
		
		panel.add(btnMove);
		panel.add(btnAttack);
		panel.add(separator3);
		
		panel.add(lblLabel6);
		panel.add(btnUp);
		panel.add(btnLeft);
		panel.add(btnRight);
		panel.add(lblFigurePic);
		panel.add(btnDown);
		panel.add(separator4);
		
		panel.add(btnNextTurn);
		
		return panel;
	}
	
	
	private void setListeners() {
		ActionListener listener = new HeroesQuestActionListener(this);
		
		newMItem.addActionListener(listener);
		optionMItem.addActionListener(listener);
		saveMItem.addActionListener(listener);
		loadMItem.addActionListener(listener);
		exitMItem.addActionListener(listener);
		aboutMItem.addActionListener(listener);

		btnPrevFigur.addActionListener(listener);
		btnNextFigur.addActionListener(listener);
		btnMove.addActionListener(listener);
		btnAttack.addActionListener(listener);
		btnUp.addActionListener(listener);
		btnLeft.addActionListener(listener);
		btnRight.addActionListener(listener);
		btnDown.addActionListener(listener);
		btnNextTurn.addActionListener(listener);
	}
	
	/**
	 * Enables/Disables the setRequestFocusEnabled() method from all buttons.
	 * @param bool
	 */
	private void setButtonsRequestFocusEnabled(boolean bool) {
		btnPrevFigur.setRequestFocusEnabled(bool);
		btnNextFigur.setRequestFocusEnabled(bool);
		btnMove.setRequestFocusEnabled(bool);
		btnAttack.setRequestFocusEnabled(bool);
		btnUp.setRequestFocusEnabled(bool);
		btnLeft.setRequestFocusEnabled(bool);
		btnRight.setRequestFocusEnabled(bool);
		btnDown.setRequestFocusEnabled(bool);
		btnNextTurn.setRequestFocusEnabled(bool);
	}
	
	/** Disables or enables the components on the game field.
	 * @param bool
	 */
	public void setComponentsEnabled(boolean bool) {
		btnPrevFigur.setEnabled(bool);
		btnNextFigur.setEnabled(bool);
		btnMove.setEnabled(bool);
		btnAttack.setEnabled(bool);
		btnUp.setEnabled(bool);
		btnLeft.setEnabled(bool);
		btnRight.setEnabled(bool);
		btnDown.setEnabled(bool);
		btnNextTurn.setEnabled(bool);
		saveMItem.setEnabled(bool);
	}
	
	public int getMaxCanvasWidth() {
		return this.getWidth() - pnlFunction.getWidth() - 15 ; 
	}
	
	public int getMaxCanvasHeight() {
		return this.getHeight() - pnlInfo.getHeight() - 60; 
	}
	
	/**
	 * Sets the text of the attribute {@link #lblMap}.
	 * @param text the lblMap-text to set
	 */
	public void setLblMap(String text) {
		this.lblMap.setText(text);
	}

	/** Returns the value of the attribute {@link #lblPlayer}.
	 * @return the lblPlayer
	 */
	public String getLblPlayer() {
		return lblPlayer.getText();
	}

	/** Sets the value of the attribute {@link #lblPlayer}.
	 * @param lblPlayer the lblPlayer to set
	 */
	public void setLblPlayer(String text) {
		this.lblPlayer.setText(text);
	}

	/** Returns the value of the attribute {@link #lblRoundNr}.
	 * @return the lblRoundNr
	 */
	public String getLblRoundNr() {
		return lblRoundNr.getText();
	}

	/** Sets the text of the attribute {@link #lblRoundNr}.
	 * @param text the lblRoundNr.text to set
	 */
	public void setLblRoundNr(String nr) {
		this.lblRoundNr.setText(nr);
	}

	/** Sets the text of the attribute {@link #lblFigure}.
	 * @param text the lblFigure.text to set
	 */
	public void setLblFigure(String text) {
		this.lblFigure.setText(text);
	}

	/** Sets the text of the attribute {@link #lblHealthPoints}.
	 * @param text the lblHealthPoints.text to set
	 */
	public void setLblHealthPoints(String text) {
		this.lblHealthPoints.setText(text);
	}

	/** Sets the text of the attribute {@link #lblAtkDice}.
	 * @param text the lblAtkDice.text to set
	 */
	public void setLblAtkDice(String text) {
		this.lblAtkDice.setText(text);
	}

	/** Sets the text of the attribute {@link #lblDefDice}.
	 * @param text the lblDefDice.text to set
	 */
	public void setLblDefDice(String text) {
		this.lblDefDice.setText(text);
	}

	/** Sets the text of the attribute {@link #lblAtkPoints}.
	 * @param text the lblAtkPoints.text to set
	 */
	public void setLblAtkPoints(String text) {
		this.lblAtkPoints.setText(text);
	}

	/** Sets the text of the attribute {@link #lblMovePoints}.
	 * @param text the lblMovePoints.text to set
	 */
	public void setLblMovePoints(String text) {
		this.lblMovePoints.setText(text);
	}
	
	/** Sets the value of the attribute {@link #lblFigurePic}.
	 * @param lblFigurePic the lblFigurePic to set
	 */
	public void setLblFigurePic(String textureName) {
		this.lblFigurePic.setIcon(new ImageIcon(textureName)) ;
	}
}
