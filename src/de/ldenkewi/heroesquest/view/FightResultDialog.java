package de.ldenkewi.heroesquest.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.*;

import de.ldenkewi.heroesquest.controll.ViewCtrl;
import de.ldenkewi.heroesquest.model.Figure;

/**
 * JDialog that is used to show the result of a fight.
 * @author Lars Denkewitz
 * @version from 14/04/2009
 */
public class FightResultDialog extends JDialog {
	private static final long serialVersionUID = 1L;

	private static final String RESSOURCES_JPG_SHIELD 	= ViewCtrl.RESSOURCES_GRAPHICS_ICONS + "shield.jpg";
	private static final String RESSOURCES_JPG_SKULL 	= ViewCtrl.RESSOURCES_GRAPHICS_ICONS + "skull.jpg";
	private static final String RESSOURCES_JPG_FIGHTRESULT = ViewCtrl.RESSOURCES_GRAPHICS_RANDOM + "fightresult.jpg";
	private static final String RESSOURCES_JPG_BTN_OK 	= ViewCtrl.RESSOURCES_GRAPHICS_ICONS + "btnOK.jpg";
	
	private ArrayList<JLabel> listLbl;
	private JButton btnOk;
	private JLabel lblBack, lblAtkF, lblDefF, lblLabel1, lblResult;
	
	public FightResultDialog(int locX, int locY, HeroesQuestFrame owner,Figure atkFigure, Figure defFigure, int atkPoints, int defPoints) {
		super(owner, true);
		this.setSize(503, 178);
		this.setResizable(false);
		this.setTitle("Kampfbericht");
		this.setLocation(locX + 70, locY + 50);
//		this.setIconImage(Toolkit.getDefaultToolkit().getImage("graphics/icons/sword.gif"));
		this.getContentPane().setLayout(null);
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		
		listLbl = new ArrayList<JLabel>();
		
		init(atkFigure, defFigure, atkPoints, defPoints);
	}
	
	public void init(Figure atkFigure, Figure defFigure, int atkPoints, int defPoints) {
		int healt, result = atkPoints - defPoints;
		if(result < 0)
			result = 0;
		
		healt = defFigure.getHealthPoints()-result;
		if(healt < 0)
			healt = 0;
		
		Font myFont = new Font("Old English Text MT", Font.PLAIN, 22);
		
		btnOk = new JButton(new ImageIcon(RESSOURCES_JPG_BTN_OK));
		btnOk.setBounds(this.getWidth() / 2 - 50, this.getHeight() - 67, 100, 20);
		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		
		lblBack = new JLabel(new ImageIcon(RESSOURCES_JPG_FIGHTRESULT));
		lblBack.setBounds(0, 0, 500, 155);
		
		lblAtkF = new JLabel(atkFigure.getFigureMapSetName()+ " :");
		lblAtkF.setFont(myFont);
		lblAtkF.setBounds(30, 25, 100, 30);
		lblAtkF.setForeground(Color.WHITE);
		
		lblDefF = new JLabel(defFigure.getFigureMapSetName()+ " :");
		lblDefF.setFont(myFont);
		lblDefF.setBounds(30, 60, 100, 30);
		lblDefF.setForeground(Color.WHITE);
		
		for(int i = 0; i<atkPoints; i++) {
			JLabel lbl = new JLabel(new ImageIcon(RESSOURCES_JPG_SKULL));
			lbl.setBounds(120 + i*35, lblAtkF.getY()+3, 24, 24);
			listLbl.add(lbl);
		}
		
		for(int i = 0; i<defPoints; i++) {
			JLabel lbl = new JLabel(new ImageIcon(RESSOURCES_JPG_SHIELD));
			lbl.setBounds(120 + i*35, lblDefF.getY()+3, 24, 24);
			listLbl.add(lbl);
		}
		
		lblLabel1 = new JLabel("Resultat :");
		lblLabel1.setBounds(290, lblAtkF.getY(), 100, 30);
		lblLabel1.setFont(myFont);
		lblLabel1.setForeground(Color.WHITE);
		
		lblResult = new JLabel();
		lblResult.setBounds(300, 38, 300, 80);
		lblResult.setFont(new Font("Old English Text MT", Font.PLAIN, 16));
		lblResult.setForeground(Color.WHITE);
		lblResult.setText("<html>Leben von <B>"+defFigure.getFigureMapSetName()+"</B>: <B>"+String.valueOf(defFigure.getHealthPoints())+
				"</B><p/>verlorenes Leben: <B>"+String.valueOf(result)+
				"</B><p/>Lebenspunkte übrig :<B>"+String.valueOf(healt)+"</B>/<html>");
		
		for (JLabel lbl : listLbl) {
			add(lbl);
		}

		add(btnOk);
		add(lblAtkF);
		add(lblDefF);
		add(lblLabel1);
		add(lblResult);
		
		add(lblBack);
	}
}
