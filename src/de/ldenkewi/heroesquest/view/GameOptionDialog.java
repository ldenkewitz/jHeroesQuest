package de.ldenkewi.heroesquest.view;

import java.awt.*;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import de.ldenkewi.heroesquest.controll.SoundCtrl;
import de.ldenkewi.heroesquest.controll.ViewCtrl;

/**
 * Dialog to configure the available settings like sound volume.
 * @author Lars Denkewitz
 */
public class GameOptionDialog extends JDialog {
	private static final long serialVersionUID = 1L;
	
	
	private static float volume = 0;
	private ViewCtrl 	viewCtrl;
	
	private JSlider 	volumeSlider;
	private JButton 	btnOk,  btnCancel;
	
	public GameOptionDialog(int locX, int locY, ViewCtrl ctrl, HeroesQuestFrame owner) {
			super(owner, true);
			this.setLocation(locX + 70, locY + 50);
//			this.setSize(345, 225);
			this.setResizable(false);
			this.setTitle("Optionen");
			this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			this.viewCtrl = ctrl;
			volume = SoundCtrl.getVolume();
			
			init();
			setListeners();
	}
	
	public void init() {
		// if there are more options, use tabs!
        
		GridBagLayout bag = new GridBagLayout();
		GridBagConstraints cons = new GridBagConstraints();
		setLayout(bag);
        
		volumeSlider = createSlider(SoundCtrl.getVolume());

		btnOk = new JButton("OK");
		btnOk.setActionCommand("GameOptionDialogOk");
		
		btnCancel = new JButton("Abbrechen");
		btnCancel.setActionCommand("GameOptionDialogCancel");
		
		cons.anchor = GridBagConstraints.NORTH;
		cons.fill = GridBagConstraints.BOTH;
		cons.gridwidth = GridBagConstraints.REMAINDER;
		bag.setConstraints(volumeSlider,cons);
		add(volumeSlider);
		
		cons.anchor = GridBagConstraints.SOUTHWEST;
		cons.fill = GridBagConstraints.BOTH;
		cons.gridwidth = GridBagConstraints.RELATIVE;
		bag.setConstraints(btnOk,cons);
		add(btnOk);
		
		cons.anchor = GridBagConstraints.SOUTHEAST;
		cons.fill = GridBagConstraints.NONE;
		cons.gridwidth = GridBagConstraints.REMAINDER;
		bag.setConstraints(btnCancel,cons);
		add(btnCancel);
		
		pack();
	}

	private void setListeners() {
		btnOk.addActionListener(viewCtrl);
		btnCancel.addActionListener(viewCtrl);
	}
	
	/**
	 * Returns a slider to set the volume from {@link #SoundCtrl}.
	 * @param volume that is currently set
	 * @return JSlider to set volume
	 */
	JSlider createSlider(final float volume) {
        final JSlider s = new JSlider(0, 1000);
        final float min = -80.0f;
        final float max = 6.0206f;
        final float width = max-min;
        s.setValue((int) ((volume - min)/width * 1000));

        java.util.Hashtable<Integer, JLabel> labels = new java.util.Hashtable<Integer, JLabel>(3);
        labels.put(new Integer(0), new JLabel("0%"));
        labels.put(new Integer(500), new JLabel("50%"));
        labels.put(new Integer(1000), new JLabel("100%"));
        s.setLabelTable(labels);
        s.setPaintLabels(true);

        s.setBorder(new TitledBorder("Volume" ));

        s.addChangeListener(new ChangeListener( ) {
                public void stateChanged(ChangeEvent e) {
                    int i = s.getValue( );
                    float f = min + (i*width/1000.0f);
                    GameOptionDialog.volume = f;
                }
            });
        return s;
    }

	/** Returns the value of the attribute {@link #volume}.
	 * @return the volume
	 */
	public static float getVolume() {
		return volume;
	}

}
