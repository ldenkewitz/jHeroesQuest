package de.ldenkewi.heroesquest.view;

import java.awt.Font;

import javax.swing.*;

import de.ldenkewi.heroesquest.controll.ViewCtrl;

/**
 * JDialog that is used to load a new game.
 * @author Lars Denkewitz
 * @version from 14/04/2009
 */
public class NewGameDialog extends JDialog {
	private static final long serialVersionUID = 1L;
	private JTextField txtMap, txtPlayer1, txtPlayer2;
	private JLabel lblMap, lblPlayer1, lblPlayer2;
	private JButton btnChoose, btnCancel, btnOk;
	private JSeparator separator1;

	private ViewCtrl viewCtrl;

	public NewGameDialog(int locX, int locY, ViewCtrl ctrl, HeroesQuestFrame owner) {
		super(owner, true);
		this.setLocation(locX + 70, locY + 50);
		this.setSize(345, 225);
		this.setResizable(false);
		this.setTitle("Neues Spiel");
		this.getContentPane().setLayout(null);
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.viewCtrl = ctrl;

		init();
		setListeners();
		// setVisible(true);
	}

	private void init() {
		lblPlayer1 = new JLabel("Name Spieler 1:");
		lblPlayer1.setBounds(20, 20, 140, 20);

		txtPlayer1 = new JTextField();
		txtPlayer1.setBounds(160, 20, 160, 20);

		lblPlayer2 = new JLabel("Name Spieler 2:");
		lblPlayer2.setBounds(20, 50, 140, 20);

		txtPlayer2 = new JTextField();
		txtPlayer2.setBounds(160, 50, 160, 20);

		lblMap = new JLabel("Karte:");
		lblMap.setBounds(20, 80, 140, 20);

		txtMap = new JTextField();
		txtMap.setEditable(false);
		txtMap.setHorizontalAlignment(JTextField.CENTER);
		txtMap.setFont(new Font("Tahoma", 1, 11));
		txtMap.setBounds(20, 100, 160, 20);

		btnChoose = new JButton("Durchsuchen");
		btnChoose.setActionCommand("NewGameUIChoose");
		btnChoose.setBounds(200, 100, 120, 20);

		separator1 = new JSeparator();
		separator1.setBounds(5, 140, this.getWidth() - 15, 10);

		btnOk = new JButton("OK");
		btnOk.setActionCommand("NewGameUIOk");
		btnOk.setBounds(20, 160, 100, 20);

		btnCancel = new JButton("Abbrechen");
		btnCancel.setActionCommand("NewGameUICancel");
		btnCancel.setBounds(220, 160, 100, 20);

		add(lblPlayer1);
		add(txtPlayer1);
		add(lblPlayer2);
		add(txtPlayer2);
		add(lblMap);
		add(txtMap);
		add(btnChoose);
		add(separator1);
		add(btnOk);
		add(btnCancel);
	}

	private void setListeners() {
		btnChoose.addActionListener(viewCtrl);
		btnOk.addActionListener(viewCtrl);
		btnCancel.addActionListener(viewCtrl);

	}

	/**
	 * Returns the value of the text {@link #txtMap}.
	 * 
	 * @return the text of txtMap
	 */
	public String getTxtMap() {
		return txtMap.getText();
	}

	/**
	 * Sets the value of the text {@link #txtMap}.
	 * 
	 * @param txtMap
	 *            the text to set
	 */
	public void setTxtMap(String txtMap) {
		this.txtMap.setText(txtMap);
	}

	/**
	 * Returns the value of the attribute {@link #txtPlayer1}.
	 * 
	 * @return the txtPlayer1
	 */
	public String getTxtPlayer1() {
		return txtPlayer1.getText();
	}

	/**
	 * Returns the value of the attribute {@link #txtPlayer2}.
	 * 
	 * @return the txtPlayer2
	 */
	public String getTxtPlayer2() {
		return txtPlayer2.getText();
	}
}
