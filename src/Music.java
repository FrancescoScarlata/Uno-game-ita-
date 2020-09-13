import java.io.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

public class Music extends Thread
{
	private AudioStream as;
	private AudioPlayer p;
	public boolean playback; //indica se la musica deve essere riprodotta o no
	public boolean skip; //serve per cambiare traccia
	private boolean firstOpen=true; //serve ad evitare il bug causato dalla disattivazione all'avvio della musica

	public Music(boolean playback)
	{
		this.playback=playback;
		setRandom(); //sceglie la prima canzone per l'avvio
	}

	public void run()
	{
 		while (true)
 		{
			 if (playback)
			 {
			 		startPlayback();
			 		firstOpen=false;
			 }else
			 {
			 	if(firstOpen) //sfrutta il flag per evitare l'eccezzione
			 	{
					 do
				 	{
				 		if(playback)
				 			System.out.println("Bug risolto!!");
					} while (!playback&&firstOpen);
					firstOpen=false;
			 	}else
				{
					if(skip==true)
					{
						p.player.stop(as);
						setRandom();
				 		skip=false;
						playback=true;
				 	}else
				 	{
				 		stopPlayback();
				 	}
				 }
			}
		}
	}

	public void startPlayback()
	{
		//setRandom();
		p.player.start(as);
		try
		{
			do
			{
			} while (as.available() > 0 && playback);
			return;
		} catch (IOException ex)
		{
			Logger.getLogger(Music.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public void stopPlayback()
	{
		p.player.stop(as);
		try
		{
			do
			{
			} while ((!playback==false)||(as.available() <= 0));
			return;
	 	} catch (IOException ex)
	 	{
	 		Logger.getLogger(Music.class.getName()).log(Level.SEVERE, null, ex);
	 	}
	}

	private File[] getTracks()
	{
		// We need the file in the music folder
		String dirPath= System.getProperty("user.dir");
		//System.out.println("dir: "+dirPath.substring(0,dirPath.length()-4) + "\\music");
		// To check if the cwd is in src or in Uno_ita
	 	File dir = new File(dirPath.substring(0,dirPath.length()-4) + "\\music");
		if(!dir.exists())
			dir= new File(dirPath + "\\music");
	 	File[] a = dir.listFiles();
	 	ArrayList<File> list = new ArrayList<File>();
	 	for (File f : a)
	 	{
	 		if (f.getName().substring(f.getName().length() - 3, f.getName().length()).equals("wav"))
	 		{
	 			list.add(f);
	 		}
	 	}
	 	File[] ret = new File[list.size()];
	 	for (int i = 0; i < list.size(); i++)
	 	{
	 		ret[i] = list.get(i);
	 	}
	 	return ret;
	}

	private void setRandom()
	{
		File[] files = getTracks();
		try
		{
	 		String f = files[(int) (Math.random() * (files.length - 1))].getAbsolutePath();
	 		System.out.println("In riproduzione: " + f);
	 		as = new AudioStream(new FileInputStream(f));
	 	} catch (IOException ex)
	 	{
	 		Logger.getLogger(Music.class.getName()).log(Level.SEVERE, null, ex);
	 	}
	}
	
	public static boolean chechMusic(String utente)
	{
		try
		{
			FileInputStream fstream = new FileInputStream(((System.getenv("USERPROFILE")).toString()+"/AppData/Local/Uno/data/"+utente+"/options.txt"));
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			//Lettura opzioni
			if(Integer.parseInt(br.readLine())==0)
				return false;
			else
				return true;
		}catch(IOException e)
		{
			System.out.println(e);
			System.out.println("Impossibile caricare le preferenze!");
		}
		System.out.println("Preferenze musicali caricate con successo!");
		return true;
	}
}