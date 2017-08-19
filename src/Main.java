
import java.io.*;
import javax.swing.*;

class Main
{
	public static void main(String args[])
	{
		//Controllo presenza salvataggi
		Menu.checkSavegame();
		//Creazione utente e generazione opzioni
		String utente=Menu.createUser();
		boolean playback=Music.chechMusic(utente);
		Music music = new Music(playback);
		Menu gioco=new Menu(music, playback, utente);

		//Avvio interfaccia grafica
		gioco.start();

		//Avvio musica :D
		music.start();
	}	
}
