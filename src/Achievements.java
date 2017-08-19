import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;

class Trofeo
{
	private String name;
	private String description;
	private byte progress;
	private byte total_count;

	public Trofeo( String name, String description, byte progress, byte tot_count)
	{
		this.name = name;
		this.description = description;
		this.progress = progress;
		this.total_count = tot_count;
	}

	public String getName(){return name;}
	public String getDescription(){return description;}
	public byte getProgress(){return progress;}
	public byte getTotal_count(){return total_count;}

	public void setProgress(byte p){this.progress = p;return;}

}

class Achievements
{

	static Trofeo[] trofeo = new Trofeo[10];
	static byte[] value_gain = new byte[10];
	static String path_file = "";

	public void Achievements(final JFrame w, String nomeutente)
	{
		InitiateArrayTrophies(trofeo, nomeutente);

		String file_path = (System.getenv("USERPROFILE")).toString()+"//AppData//Local//Uno//data//"+nomeutente+"//achi_details.txt";
		System.out.println(file_path);

		System.out.println("Entra in Achievements");
		final JWindow subw=new JWindow();

		//Impostazione dimensioni
		Dimension monitor= Toolkit.getDefaultToolkit().getScreenSize();
		if (monitor.getWidth()>700)
		{
			subw.setSize(680,700);
		}
		else
		{
			subw.setSize(650,650);
		}
		subw.setLocationRelativeTo(null);
		subw.setVisible(true);

		//----

		JPanel jpanel_global=new JPanel(); //Pannello MAIN
		jpanel_global.setLayout(null);

		//GESTIONE DEI PANNELLI
		//gestione label del titolo
		JLabel l1=new JLabel("Trofei", JLabel.CENTER);
		l1.setFont(new Font("Arial", Font.PLAIN, 30));
		jpanel_global.add(l1);
		l1.setBounds(250,-85,200,200); //x, y, largh, altez
		//---------------------------

		JPanel p5=new JPanel(); //Pannello pulsante uscita

		//parteSX
		String d ="";

		ImageIcon[] x= new ImageIcon[10];
		for (int i=0;i<10;i++)
		{
		 d+="./img/achi_img_collection/";
		 d+=Integer.toString(i) + ".png";
		 x[i] = new ImageIcon(d);
		 x[i].setImage(x[i].getImage().getScaledInstance( 50, 50,Image.SCALE_DEFAULT));
		 d="";
		}
		int y=-60;

		JLabel[] img_achi = new JLabel[10];
		for (int i=0;i<10;i++)
		{

			img_achi[i] = new JLabel(x[i]);
			jpanel_global.add(img_achi[i]);
			img_achi[i].setBounds(-60,y,250,250);
			y= y+ 65;
		}

		//parteDX
		JLabel achi_title[] = new JLabel[10];
		JLabel description[] = new JLabel[10];
	 	JProgressBar progress[] = new JProgressBar[10];
		JLabel earned_achi[] = new JLabel[10];
		int x1 =105;
		 
		int [] yC= new int [4];
		yC[0]= 30; //y del titolo
		yC[1]= 44; //y del description
		yC[2] = 50; //y del progress
		yC[3] = 75; //y della label earned
		for(int i=0;i<10;i++)
		{
			achi_title[i] = new JLabel(trofeo[i].getName());
			jpanel_global.add(achi_title[i]);
			achi_title[i].setBounds(x1,yC[0],450,50);
			achi_title[i].setForeground(Color.red);

			description[i] = new JLabel(trofeo[i].getDescription());
			jpanel_global.add(description[i]);
			description[i].setBounds(x1,yC[1],450,50);
			description[i].setForeground(Color.blue);

			progress[i] = new JProgressBar(0,trofeo[i].getTotal_count());
			System.out.println(trofeo[i].getProgress());
			progress[i].setValue(trofeo[i].getProgress());

			jpanel_global.add(progress[i]);

			progress[i].setBounds(x1,yC[2],450,33);
			progress[i].setForeground(Color.yellow);

			if(trofeo[i].getProgress() == trofeo[i].getTotal_count())
			{
			earned_achi[i] = new JLabel("OTTENUTO!");
			jpanel_global.add(earned_achi[i]);
			earned_achi[i].setBounds(570,yC[0],450,50);
			earned_achi[i].setForeground(Color.green);
			}
			else
			{
				earned_achi[i] = new JLabel("IN CORSO");
				jpanel_global.add(earned_achi[i]);
				earned_achi[i].setBounds(570,yC[2],450,50);
				earned_achi[i].setForeground(Color.red);
			}

			for( int j=0; j<yC.length; j++)
				yC[j]+=65;
			
		}

		//panel-ception
		subw.add(jpanel_global);

		//--------------
		JButton back=new JButton("Torna indietro");

		jpanel_global.add(back); //aggiungo il bottone
		back.setBounds(290, 670, 150, 20);

		//Gestore eventi

		back.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				//chiusura
				subw.dispose();
				w.setAlwaysOnTop(true);
			}
		});
	}

	private static void InitiateArrayTrophies (Trofeo [] t,String utente)
	{
		//inizializzazione trofei per la stampa a video
		t[0] = new Trofeo("Caso disperato", "Vinci una partita con uno degli avversari avente un punteggio superiore a 400", (byte)0, (byte)12);//-- DONE --
		t[1] = new Trofeo("Winning Spree", "Vinci 2 round consecutivi", (byte)0, (byte)25); //--DONE--
		t[2] = new Trofeo("Novizio", "Partecipa alla tua prima partita ad 'UNO'!", (byte)0, (byte)1); //-- DONE --
		t[3] = new Trofeo("Sempre in tempo per non arrugginirsi", "Scopri un giocatore che effettua un bluff!", (byte)0, (byte)14); //-- DONE --
		t[4] = new Trofeo("L'esercizio e'  la via", "Termina 50 Partite con successo!", (byte)0, (byte)50); //-- DONE --
		t[5] = new Trofeo("Il Trio", "Batti il trio delle meraviglie!", (byte)0, (byte)1);//--DONE--
		t[6] = new Trofeo("Distruzione!!!", "Vinci 10 partite a difficolta' DIFFICILE", (byte)0, (byte)10);
		t[7] = new Trofeo("Ci sono cose che noi umani non possiamo immaginare", "Vinci 100 ROUND!", (byte)0, (byte)100);//-- DONE --
		t[8] = new Trofeo("Mr Uno", "Dichiara UNO 100 volte.", (byte)0, (byte)100);// --DONE--
		t[9] = new Trofeo("Fulmine", "Vinci una partita in soli 3 round!", (byte)0, (byte)10);// -- DONE --

		try
		{
			path_file = (System.getenv("USERPROFILE")).toString()+"/AppData/Local/Uno/data/" +utente+ "/"+ "achi_score.txt";

			FileReader fr = new FileReader(path_file);
			BufferedReader br = new BufferedReader(fr);
			int i=0;
			String s;
			while((s = br.readLine()) != null)
			{
				value_gain[i] =(byte) Integer.parseInt(s);
				t[i].setProgress(value_gain[i]);
				System.out.println(value_gain[i]);
				i++;
			}
			fr.close();
		}
		catch(IOException e)
		{
			System.out.println(e);
			System.out.println("Non riesco a leggere!!!");
		}

	}
	
	public static void newAchievements(String utente)
	{
		String newline = System.getProperty("line.separator");
		try
			{
				FileWriter fileout = new FileWriter((System.getenv("USERPROFILE")).toString()+"/AppData/Local/Uno/data/"+utente+"/"+"achi_score.txt");
				for (int i=0; i<10; i++) {
					fileout.write( "0" + newline);
				}
					
				fileout.close();
			}
			catch(IOException e)
			{
				System.out.println(e);
				System.out.println("Non riesco a scrivere!!!");
			}

		return;
	}
}