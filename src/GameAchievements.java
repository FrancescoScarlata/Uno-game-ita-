import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;

class GameAchievements {
	
	static void LoadDataAchi (byte[] av,String ut) // LETTURA DEGLI ACHIEVEMENT
	{
		System.out.println("Achi caricati");
		try
		{
			String path_file = (System.getenv("USERPROFILE")).toString()+"/AppData/Local/Uno/data/" +ut+ "/"+ "achi_score.txt";

			FileReader fr = new FileReader(path_file);
			BufferedReader br = new BufferedReader(fr);
			int i=0;
			String s;
			while((s = br.readLine()) != null)
			{
				av[i] =(byte) Integer.parseInt(s);
				//System.out.println(av[i]);
				i++;
			}
			fr.close();

	        }
		 catch(IOException e)
		{
			System.out.println(e);
			System.out.println("Non riesco a leggere!!!");
		}

		return;
	}
	
	// Ogni volta che succede qualcosa va a scrivere direttamente su disco
	public static void AchievementDealer(byte[] av, String ut)
	{
		System.out.println("Sono entrato nel metodo per eliminare/caricare il nuovo achi");
		String path_file = (System.getenv("USERPROFILE")).toString()+"/AppData/Local/Uno/data/" +ut+ "/achi_score.txt";
		File daEliminare = new File(path_file); //Referenzia oggetto file da percorso

		if(daEliminare.exists()) //se esiste...
                 {
			 /*
			if(daEliminare.delete()) //prova a eliminarlo...
				System.out.println("File eliminato!"); //e conferma...
			 */
		 }

		String newline = System.getProperty("line.separator");
		try
		{
			FileWriter fileout = new FileWriter((System.getenv("USERPROFILE")).toString()+"/AppData/Local/Uno/data/"+ut+"/"+"achi_score.txt");
			for (int j=0; j<10; j++)
			{
				fileout.write( av[j] + newline);
			}
			
			fileout.close();
		}
		catch(IOException e)
		{
			System.out.println(e);
		}
		return;
	}
	
	// Aggiorna tutti i valori in base all'indice che controlla. Quindi achivalue e' l'insieme di valori degli achievements e index l'indice della parte a cui si sta riferendo
	public static byte[] updateAchievement(byte[] achivalue, int index, String utente)
	{
		switch(index)
		{
			case 0:
				achivalue[0]++;
				if (achivalue[0] == 12)
				{
					System.out.println("Achievement ottenuto");
					JOptionPane.showMessageDialog(null, "COMPLIMENTI hai ottenuto l'achievement < < Caso disperato > >");
				}
				AchievementDealer(achivalue,utente);
				return achivalue;
				
			case 1: 
				achivalue[1]++;
				if (achivalue[1] == 25)
				{
					System.out.println("Achievement ottenuto");
					JOptionPane.showMessageDialog(null, "COMPLIMENTI hai ottenuto l'achievement < < Winning Spree > >");
				}
				AchievementDealer(achivalue,utente);
				return achivalue;
			
			case 2: // - prima partita
				achivalue[2] =(byte) 1;
				System.out.println("Achievement ottenuto");
				AchievementDealer(achivalue,utente);
				JOptionPane.showMessageDialog(null, "COMPLIMENTI hai ottenuto l'achievement < < Novizio > >");
				return achivalue;
			
			case 3: 
				achivalue[3]++;
				if (achivalue[3] == 14)
				{
					System.out.println("Achievement ottenuto");
					JOptionPane.showMessageDialog(null, "COMPLIMENTI hai ottenuto l'achievement < < Sempre in tempo per non arrugginirsi > >");
				}
				AchievementDealer(achivalue,utente);
				return achivalue;
				
			case 4 :
				achivalue[4]++;
 				if (achivalue[4] == 50)
 				{
	 				System.out.println("Achievement ottenuto");
	 				JOptionPane.showMessageDialog(null, "COMPLIMENTI hai ottenuto l'achievement < < L'esercizio e' la via! > >");
				}
 				AchievementDealer(achivalue,utente);
				return achivalue;
			
			case 5: 
				achivalue[5]++;
				if (achivalue[5] == 1)
				{
					System.out.println("Achievement ottenuto");
					JOptionPane.showMessageDialog(null, "COMPLIMENTI hai ottenuto l'achievement < < Batti il Trio! > >");
				}
				AchievementDealer(achivalue,utente);
				return achivalue;
			
			case 6: 	
				achivalue[6]++;
				if (achivalue[6] == 10)
				{
					System.out.println("Achievement ottenuto");
					JOptionPane.showMessageDialog(null, "COMPLIMENTI hai ottenuto l'achievement < < Distruzione!!! > >");
				}
				AchievementDealer(achivalue,utente);
				return achivalue;
			
			case 7:
				achivalue[7]++;
				if (achivalue[7] == 100)
				{
					System.out.println("Achievement ottenuto");
					JOptionPane.showMessageDialog(null, "COMPLIMENTI hai ottenuto l'achievement < < Ci sono cose che noi umani non possiamo immaginare > >");
				}
				AchievementDealer(achivalue,utente);
				return achivalue;
			
			case 8: // ha chiamato 100 Uno!
				achivalue[8]++;
				if (achivalue[8] == 100)
				{
					System.out.println("Achievement ottenuto");
					JOptionPane.showMessageDialog(null, "COMPLIMENTI hai ottenuto l'achievement < < Mr.UNO > >");
				}
				AchievementDealer(achivalue,utente);
				return achivalue;
		
			case 9:
				achivalue[9]++;
				if (achivalue[9] == 10)
				{
					System.out.println("Achievement ottenuto");
					JOptionPane.showMessageDialog(null, "COMPLIMENTI hai ottenuto l'achievement < < Fulmine > >");
				}
				AchievementDealer(achivalue,utente);
				return achivalue;
				
			default : break;
		}
		
		return achivalue;
	}
	
	
}