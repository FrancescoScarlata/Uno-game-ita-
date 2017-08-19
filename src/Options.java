import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class Options
{
	JRadioButton r1 = new JRadioButton("On");
	JRadioButton r2 = new JRadioButton("Off");
	JRadioButton r3 = new JRadioButton("2");
	JRadioButton r4 = new JRadioButton("3");
	JRadioButton r5 = new JRadioButton("4");
	JRadioButton r6 = new JRadioButton("Facile");
	JRadioButton r7 = new JRadioButton("Medio");
	JRadioButton r8 = new JRadioButton("Difficile");
	int m; //memorizza lo stato della musica
	int g; //memorizza il numero dei giocatori
	int d; //memorizza la difficolta'


	public void Options(final JFrame w, final String utente, final Music music, final boolean playback)
	{
		System.out.println("Entra in Options");
		final JWindow subw=new JWindow();

		//Impostazione dimensioni
		Dimension monitor= Toolkit.getDefaultToolkit().getScreenSize();
		if (monitor.getWidth()>1000)
		{
			subw.setSize(375,450);
		}
		else
		{
			subw.setSize(150,225);
		}
		subw.setLocationRelativeTo(null);
		subw.setVisible(true);

		//Creazione componenti
		JPanel p=new JPanel(); //Pannello principale
		p.setLayout(new GridLayout(10,1));
		p.setBackground(Color.GRAY);

		JLabel l1=new JLabel("Musica", JLabel.CENTER);
		l1.setFont(new Font("Arial", Font.PLAIN, 40));

		JPanel p2=new JPanel(); //Pannello musica
		p2.setLayout(new FlowLayout());

		JPanel p2b=new JPanel(); //Pannello tasto skip
		p2.setLayout(new FlowLayout());

		ButtonGroup musica = new ButtonGroup();
		musica.add(r1);
		musica.add(r2);

		JLabel l2=new JLabel("Numero giocatori", JLabel.CENTER);
		l2.setFont(new Font("Arial", Font.PLAIN, 40));

		JPanel p3=new JPanel(); //Pannello numero giocatori
		p3.setLayout(new FlowLayout());

		ButtonGroup giocatori = new ButtonGroup();
		giocatori.add(r3);
		giocatori.add(r4);
		giocatori.add(r5);

		JLabel l3=new JLabel("Livello difficolta'", JLabel.CENTER);
		l3.setFont(new Font("Arial", Font.PLAIN, 40));

		JPanel p4=new JPanel(); //Pannello difficoltà 
		p4.setLayout(new FlowLayout());

		JPanel p4b=new JPanel(); //Pannello nero per distaccare
		p4b.setBackground(Color.GRAY);

		ButtonGroup difficolta = new ButtonGroup();
		difficolta.add(r6);
		difficolta.add(r7);
		difficolta.add(r8);

		JPanel p5=new JPanel(); //Pannello pulsanti
		p5.setLayout(new FlowLayout());

		JPanel p6=new JPanel(); //Pannello pulsanti
		p6.setLayout(new FlowLayout());

		JButton skip=new JButton("Prossima traccia");
		JButton resetp=new JButton("Cancellazione progressi"); //elimina achievements e records
		JButton reseto=new JButton("Ripristina le impostazioni di default"); //ripristina le impostazioni originali
		JButton back=new JButton("Torna al menu' precedente");

		//Assegnazione valori caricati (letti dal file options.txt)
		try
		{
			FileInputStream fstream = new FileInputStream(((System.getenv("USERPROFILE")).toString()+"/AppData/Local/Uno/data/"+utente+"/options.txt"));
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			//Lettura opzioni
			m=Integer.parseInt(br.readLine());
			g=Integer.parseInt(br.readLine());
			d=Integer.parseInt(br.readLine());

			//Modifica componenti
			//musica
			if(m==1)
			{
				r1.setSelected(true);
			}else
			{
				r2.setSelected(true);
			}
			//n.giocatori
			switch(g)
			{
				case 1:{r3.setSelected(true);}
					break;
				case 2:{r4.setSelected(true);}
					break;
				case 3:{r5.setSelected(true);}
					break;
			}
			//difficolta'
			switch(d)
			{
				case 1:{r6.setSelected(true);}
					break;
				case 2:{r7.setSelected(true);}
					break;
				case 3:{r8.setSelected(true);}
					break;
			}
			in.close();
		}catch(IOException e)
		{
			System.out.println(e);
			System.out.println("Impossibile caricare le opzioni!");
		}
		System.out.println("Opzioni caricate con successo!");

		//Unione componenti
		subw.add(p);
		p.add(l1);
		p.add(p2);
		p.add(p2b);
		p.add(l2);
		p.add(p3);
		p.add(l3);
		p.add(p4);
		p.add(p4b);
		p.add(p5);
		p.add(p6);

		p2.add(r1);
		p2.add(r2);

		p2b.add(skip);

		p3.add(r3);
		p3.add(r4);
		p3.add(r5);

		p4.add(r6);
		p4.add(r7);
		p4.add(r8);

		p5.add(reseto);

		p6.add(resetp);
		p6.add(back);

		//Gestori
		RadioManager rm=new RadioManager(music,playback);
		r1.addItemListener(rm);
		r2.addItemListener(rm);
		r3.addItemListener(rm);
		r4.addItemListener(rm);
		r5.addItemListener(rm);
		r6.addItemListener(rm);
		r7.addItemListener(rm);
		r8.addItemListener(rm);

		//Gestore pulsanti
		skip.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				music.skip=true;
				music.playback=false;
			}
		});
		resetp.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				w.setAlwaysOnTop(false);
				subw.setAlwaysOnTop(false);
				if(JOptionPane.showConfirmDialog(null,"Sei sicuro di voler cancellare i tuoi progressi nel gioco?","Conferma",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION)
				{
					//Rimozione progressi utente
					try
					{
						if(new File((System.getenv("USERPROFILE")).toString()+"/AppData/Local/Uno/data/"+utente+"/records.txt").exists())
						{
							new File((System.getenv("USERPROFILE")).toString()+"/AppData/Local/Uno/data/"+utente+"/records.txt").delete();
							Records.creaNuovoRecords(utente, 27);
						}
						else
							Records.creaNuovoRecords(utente, 27);


						if(new File((System.getenv("USERPROFILE")).toString()+"/AppData/Local/Uno/data/"+utente+"/achi_score.txt").exists())
						{
							new File((System.getenv("USERPROFILE")).toString()+"/AppData/Local/Uno/data/"+utente+"/achi_score.txt").delete();
							Achievements.newAchievements(utente);
						}
						else
							Achievements.newAchievements(utente);


						System.out.println("Salvataggi rimossi con successo!");
						JOptionPane.showMessageDialog(null,"Salvataggi rimossi con successo!");

					}catch (Exception exc)
					{
						System.out.println("Errore: " + exc);
						JOptionPane.showMessageDialog(null,"Errore durante la rimozione dei salvataggi", "Errore!", JOptionPane.ERROR_MESSAGE);
					}
				}
				w.setAlwaysOnTop(true);
				subw.setAlwaysOnTop(true);
		}
		});
		reseto.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				w.setAlwaysOnTop(false);
				subw.setAlwaysOnTop(false);
				if(JOptionPane.showConfirmDialog(null,"Sei sicuro di voler ripristinare le impostazioni originali?","Conferma",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION)
				{
					//reset opzioni
					if(new File((System.getenv("USERPROFILE")).toString()+"/AppData/Local/Uno/data/"+utente+"/options.txt").exists())
					{
						new File((System.getenv("USERPROFILE")).toString()+"/AppData/Local/Uno/data/"+utente+"/options.txt").delete();
						if(newOptions(utente))
						{
							System.out.println("Reset opzioni eseguito con successo!");
							JOptionPane.showMessageDialog(null,"Reset opzioni eseguito con successo!");
						}
					}

				else
					if(newOptions(utente))
					{
						System.out.println("Reset opzioni eseguito con successo!");
						JOptionPane.showMessageDialog(null,"Reset opzioni eseguito con successo!");
					}

					//chiusura
					subw.dispose();
					w.setAlwaysOnTop(true);
				}
				w.setAlwaysOnTop(true);
				subw.setAlwaysOnTop(true);
			}
		});
		back.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				//salvataggio impostazioni
				saveSettings(utente);
				//chiusura
				subw.dispose();
				w.setAlwaysOnTop(true);
			}
		});

	}
	public static boolean newOptions(String utente)
	{
		try
		{
			new File((System.getenv("USERPROFILE")).toString()+"/AppData/Local/Uno/data/"+utente+"/options.txt").createNewFile();
			FileWriter fileout = new FileWriter((System.getenv("USERPROFILE")).toString()+"/AppData/Local/Uno/data/"+utente+"/options.txt");
			fileout.write(48+1);//Musica: On
			fileout.write(13);
			fileout.write(10);
			fileout.write(48+3);//Giocatori: 4
			fileout.write(13);
			fileout.write(10);
			fileout.write(48+1);//Difficolta' : Facile
			fileout.write(13);
			fileout.write(10);
			fileout.close();
			return true;
		}
		catch(IOException e)
		{
			System.out.println(e);
			System.out.println("Errore durante la creazione delle opzioni!");
			return false;
		}
	}
	private void saveSettings(String utente)
	{
		try
		{
			FileWriter fileout = new FileWriter((System.getenv("USERPROFILE")).toString()+"/AppData/Local/Uno/data/"+utente+"/options.txt");
			fileout.write(48+m);
			fileout.write(13);
			fileout.write(10);
			fileout.write(48+g);//Giocatori
			fileout.write(13);
			fileout.write(10);
			fileout.write(48+d);//Difficolta'
			fileout.write(13);
			fileout.write(10);
			fileout.close();
			System.out.println("Opzioni salvate con successo!");
		}
		catch(IOException e2)
		{
			System.out.println(e2);
			System.out.println("Errore durante il salvaraggio delle opzioni!");
		}
	}
	//Gestore radiobutton
	class RadioManager implements ItemListener
	{
		Music music;
		boolean playback;
		public RadioManager(Music music, boolean playback)
		{
			this.music=music;
			this.playback=playback;
		}
		public void itemStateChanged(ItemEvent e)
		{
			Object target = e.getItem();
			int status = e.getStateChange();
			if(target.equals(r1) && status == ItemEvent.SELECTED)//Musica On
			{
				m=1;
				music.playback = true;
				System.out.println("Musica On");
			}
			if(target.equals(r2) && status == ItemEvent.SELECTED)//Musica Off
			{
				m=0;
				music.playback = false;
				System.out.println("Musica Off");
			}
			if(target.equals(r3) && status == ItemEvent.SELECTED)//N.Giocatori:2
			{
				g=1;
				System.out.println("Numero giocatori: 2");
			}
			if(target.equals(r4) && status == ItemEvent.SELECTED)//N.Giocatori:3
			{
				g=2;
				System.out.println("Numero giocatori: 3");
			}
			if(target.equals(r5) && status == ItemEvent.SELECTED)//N.Giocatori:4
			{
				g=3;
				System.out.println("Numero giocatori: 4");
			}
			if(target.equals(r6) && status == ItemEvent.SELECTED)//Difficolta' : Facile
			{
				d=1;
				System.out.println("Difficolta': Facile");
			}
			if(target.equals(r7) && status == ItemEvent.SELECTED)//Difficolta' Media
			{
				d=2;
				System.out.println("Difficolta' Media");
			}
			if(target.equals(r8) && status == ItemEvent.SELECTED)//Difficolta' Difficile
			{
				d=3;
				System.out.println("Difficolta' Difficile");
			}
		}
	}
}
