import javax.swing.*;
import java.awt.*;
import java.io.*;

public class Menu extends Thread
{
	private Music music;
	private boolean playback;
	private String utente;

	public Menu(Music music, boolean playback, String utente)
	{
		this.music=music;
		this.playback=playback;
		this.utente=utente;
	}
	public void run()
	{
		//Creazione Menu Principale
		JFrame w = new JFrame();
		w.setUndecorated(true);
 		w.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Dimension monitor= Toolkit.getDefaultToolkit().getScreenSize();
		if (monitor.getWidth()>1000)
		{
			w.setSize(500,600);
		}
		else
		{
			w.setSize(200,300);
		}
		w.setLocationRelativeTo(null);
		w.setIconImage(new ImageIcon("./img/logo.jpg").getImage());

		JPanel p=new JPanel();
		p.setBackground(Color.DARK_GRAY);

		JLabel title= new JLabel(new ImageIcon("./img/title.png"));

		JButton start= new JButton("Nuova Partita");
		start.setBackground(Color.RED);
		start.setForeground(Color.WHITE);

		JButton records= new JButton("Records");
		records.setBackground(Color.BLUE);
		records.setForeground(Color.WHITE);

		JButton options= new JButton("Opzioni");
		options.setBackground(Color.GREEN);

		JButton achievements= new JButton("Trofei");
		achievements.setBackground(Color.YELLOW);

		JButton credits= new JButton("Crediti");
		credits.setBackground(Color.LIGHT_GRAY);

		JButton exit= new JButton("Esci");
		exit.setBackground(Color.WHITE);

		//Layout
		w.setLayout(new BorderLayout());
		p.setLayout(null);

		if (monitor.getWidth()<1000)
		{
			title.setBounds(50,7,100,50);
			start.setBounds(50,120,100,50);
			records.setBounds(50,160,100,50 );
			options.setBounds(50,200,100,50);
			achievements.setBounds(50,240,100,50);
			credits.setBounds(50,270,100,50);
			exit.setBounds(50,300,100,50);
		}
		else
		{
			title.setBounds(125,15,250,200);
			start.setBounds(125,210,250,40);
			records.setBounds(125,270,250,40);
			options.setBounds(125,330,250,40);
			achievements.setBounds(125,390,250,40);
			credits.setBounds(125,450,250,40);
			exit.setBounds(125,510,250,40);
		}

		//aggiunta al pannello
		w.add(p,"Center");
		p.add(title);
		p.add(start);
		p.add(records);
		p.add(options);
		p.add(achievements);
		p.add(credits);
		p.add(exit);

		//Gestore eventi
		ButtonManager m=new ButtonManager(w,utente, music, playback);
		start.addActionListener(m);
		records.addActionListener(m);
		options.addActionListener(m);
		achievements.addActionListener(m);
		credits.addActionListener(m);
		exit.addActionListener(m);

		w.setVisible(true);
		playback=false;

	}
	
	public static void checkSavegame()
	{
		if(new File((System.getenv("USERPROFILE")).toString()+"/AppData/Local/Uno").exists()==false)
		{
			System.out.println("Cartella salvataggi non trovata!");
			try
			{
				//Creazione cartella gioco
				(new File((System.getenv("USERPROFILE")).toString()+"/AppData/Local/Uno")).mkdir();
				boolean success = (new File((System.getenv("USERPROFILE")).toString()+"/AppData/Local/Uno/data")).mkdir();
				if(success)
					System.out.println("Cartella salvataggi creata!");

			}catch (Exception exc)
			{
				System.out.println("Errore: " + exc);
				JOptionPane.showMessageDialog(null,"Errore durante il caricamento dei salvataggi!", "Errore!", JOptionPane.ERROR_MESSAGE);
			}
		}else
			System.out.println("Cartella salvataggi trovata!");
	}

	public static String createUser()
	{
		String utente="";

		while(utente==null||utente.equals("")||utente.equals(" "))
		{
			utente=JOptionPane.showInputDialog("Inserisci il tuo nome utente");

			if(utente==null)
			{
				JOptionPane.showMessageDialog(null, "E' necessario un nome utente per giocare", "Errore!", JOptionPane.ERROR_MESSAGE);
			}else if(utente.equals("")||utente.equals(" "))
				JOptionPane.showMessageDialog(null, "Inserisci un nome valido", "Errore!", JOptionPane.ERROR_MESSAGE);
		}

		try
		{
			if(new File((System.getenv("USERPROFILE")).toString()+"/AppData/Local/Uno/data/"+utente).exists()==false)
			{
				System.out.println("Cartella utente non trovata!");
				//Creazione cartella utente
				System.out.println("Sto scrivendo in "+(System.getenv("USERPROFILE")).toString());
				boolean success = (new File((System.getenv("USERPROFILE")).toString()+"/AppData/Local/Uno/data/"+utente)).mkdir();
				if(success)
					System.out.println("Cartella utente creata!");
				//Creazione file opzioni
				Options.newOptions(utente);

				//creazione di records.txt
				Records.creaNuovoRecords(utente, 26);

				//crezione achievements
				Achievements.newAchievements(utente);
			}else
				System.out.println("Cartella utente trovata!");
		} catch (Exception exc)
		{
		 System.out.println("Errore: " + exc);
		 JOptionPane.showMessageDialog(null,"Errore durante il caricamento dei salvataggi!", "Errore!", JOptionPane.ERROR_MESSAGE);
		}
		return utente;
	}
}