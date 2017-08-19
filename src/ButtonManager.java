import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;

class ButtonManager implements ActionListener
{
	private JFrame w; //finestra principale
	private JWindow subw; //finestra aggiuntiva
	private String u; //username
	private Music music; //player7
	private boolean playback;

	public ButtonManager(JFrame w, String u) //Achievements e Records
	{
		this.w=w;
		subw=null;
		this.u=u;
	}
	public ButtonManager(JFrame w, JWindow subw) //Credits
	{
		this.w=w;
		this.subw=subw;
		u=null;
	}
	public ButtonManager(JFrame w, String u, Music music, boolean playback) //Options e Game
	{
		this.w=w;
		subw=null;
		this.u=u;
		this.music=music;
		this.playback=playback;
	}

	public void actionPerformed(ActionEvent e)
	{
		if (e.getActionCommand().equals("Nuova Partita"))
		{
			w.setAlwaysOnTop(false);
			Starter s=new Starter(w,u);
			s.start();
		}

		if (e.getActionCommand().equals("Opzioni"))
		{
			w.setAlwaysOnTop(false);
			Options o=new Options();
			o.Options(w,u,music,playback);
		}

		if (e.getActionCommand().equals("Trofei"))
		{
			w.setAlwaysOnTop(false);
			Achievements a=new Achievements();
			a.Achievements(w,u);

		}

		if (e.getActionCommand().equals("Records"))
		{
			w.setAlwaysOnTop(false);
			Records r=new Records();
			r.records(w,u);

		}

		if (e.getActionCommand().equals("Crediti"))
		{
			w.setAlwaysOnTop(false);
			Credits c=new Credits();
			c.Credits(w);

		}

		if (e.getActionCommand().equals("Esci"))
		{
			w.setAlwaysOnTop(false);
			if(JOptionPane.showConfirmDialog(null,"Sei sicuro di voler uscire dal gioco?","Uscita",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION)
			{
				//Uscita
				System.out.println("Programma terminato con successo, registrare voto.");
				System.exit(0);
			}else
			{
				w.setAlwaysOnTop(true);
			}
		}
	}
}






