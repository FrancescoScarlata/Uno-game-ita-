import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;

class GameUtil
{
	public static boolean checkResults(int [] punt)
	{
		for (int i=0; i<punt.length; i++)
			if(punt[i]>=500)
				return false;
		return true;
	}

	public static boolean checkPlayersCards(int [] op, Tavolo t)
	{
			for (int i=0; i<(op[1]+1); i++)
				if (t.getPlayer(i).getNumCarte()==0)
					return false;
		return true;
	}

	public static char gestioneColori()
	{
		switch((int)(Math.random()*4))
		{
			case 0: return 'g';
			case 1: return 'b';
			case 2: return 'v';
			case 3: return 'r';
		}
		return 'g';
	}

	public static void gestioneUno(Tavolo t, int indice, int diff)
	{
		if (t.getPlayer(indice).getNumCarte()==1)
		{
			if (!t.getPlayer(indice).getUno())
			{
				switch (diff)
				{

					case 1:
					{
						if ((Math.random()*10+1)>4)
						{
							t.getPlayer(indice).uno();
							JOptionPane.showMessageDialog(null, ""+t.getPlayer(indice).toString()+" dice : Uno!");
						}
						return;
					}
					case 2:
					{
						if ((Math.random()*10+1)>5)
						{
							t.getPlayer(indice).uno();
							JOptionPane.showMessageDialog(null, ""+t.getPlayer(indice).toString()+" dice : Uno!");

						}
						return;
					}
					case 3:
					{
						t.getPlayer(indice).uno();
						JOptionPane.showMessageDialog(null, ""+t.getPlayer(indice).toString()+" dice : Uno!");

						return;
					}
				}
			}
		}
		else
			if (t.getPlayer(indice).getUno())
				t.getPlayer(indice).uno();
	}

	public static void gestioneFineMazzo(Tavolo t)
	{
		Carta [] mazzoprov=new Carta [t.getPosMazzos()];
		// cosa si dovrebbe fare:
		// prendere le carte del mazzo di scarto fino alla carta precedente all'ultima carta
		for (int i=0; i<t.getPosMazzos(); i++)
		{
			mazzoprov[i]=t.getMazzos()[i];
			mazzoprov[i].setNext(null);
		}

		//tenere l'ultima carta del mazzo di scarto
		Carta lastcard= t.getUltimaCarta();


		// mescolare le carte del nuovo mazzo creato
		int x, y;
		Carta temp;
		for (int i=0; i<(1000+(Math.random()*2000)); i++)
		{
			temp=mazzoprov[x=(int)(Math.random()*mazzoprov.length)];
			mazzoprov[x]=mazzoprov[y=(int)(Math.random()*mazzoprov.length)];
			mazzoprov[y]=temp;
		}
		// prendere le carte che servono da questo nuovo mazzo
		//trovare un modo in cui prendere le carte da questo nuovo mazzo piuttosto che dal mazzo precedente
		t.fineMazzor(mazzoprov);
		t.scartata(lastcard);
		System.out.println("\n E' finito il mazzo!!! \n");
	}

	public static int [] punteggioTotalizzato(Tavolo t, int [] gioc, int [] rec, int indice)
	{
		int cont=0;
		//vittoria
		if (t.getPlayer(indice).getNumCarte()==0)
		{
			for (int i=0; i<gioc.length; i++)
				if(i!=indice)
				{
					Carta aux=t.getPlayer(i).getMano().getHead();
					for (;aux!=null; aux=aux.getNext())
					switch (aux.getNumero())
					{
						case 0: cont+=aux.getNumero(); break;
						case 1:cont+=aux.getNumero(); break;
						case 2:cont+=aux.getNumero(); break;
						case 3:cont+=aux.getNumero(); break;
						case 4:cont+=aux.getNumero(); break;
						case 5:cont+=aux.getNumero(); break;
						case 6:cont+=aux.getNumero(); break;
						case 7:cont+=aux.getNumero(); break;
						case 8:cont+=aux.getNumero(); break;
						case 9:cont+=aux.getNumero(); break;
						case 10: cont+=20; break;
						case 11:cont+=20; break;
						case 12:cont+=20; break;
						case 13:cont+=50; break;
						case 14:cont+=50; break;
					}
				}
		}
		else
			return gioc;

		if (indice==0)
			JOptionPane.showMessageDialog(null,"Complimenti hai totalizzato: "+cont+" punti!");

		gioc[indice]+=cont;
		if (indice==0)
		{
			if(rec[6]<cont)
				rec[6]=cont;
			if (rec[7]==0)
				rec[7]=cont;
				else if(rec[7]>cont)
					rec[7]=cont;

		}
		cont=0;


		return gioc;
	}

	public static int [] readOptions(int [] op, String utente)
	{
		try
		{

			int n;
			FileReader filetxt = new FileReader((System.getenv("USERPROFILE")).toString()+"/AppData/Local/Uno/data/"+utente+"/options.txt");
			for (int i=0; (i<op.length)&&((n=filetxt.read()) !=-1) ; i++)
			{
				while (n==13||n==10)
				{

					n=filetxt.read();
				}
				//System.out.println(n+" ; " );
				op[i]=n-48;
			}
		}
		catch(IOException e)
		{

		}
		return op;
	}

	public static void writeRecords(int [] records, String utente)
	{
		try
		{
			byte x;
			FileWriter fileout = new FileWriter((System.getenv("USERPROFILE")).toString()+"/AppData/Local/Uno/data/"+utente+"/record.txt");
			for( int i=0; i<records.length; i++)
			{
				int record=records[i];

				for (byte y=3; y>=0; y--)
				{


					x=(byte)((record/Math.pow(10,y))+48);
					record-=(x-48)*Math.pow(10, y);

					fileout.write(x);
				}
				fileout.write(13);
				fileout.write(10);
				System.out.println();
			}
			fileout.close();
		}
		catch(IOException e)
		{
			System.out.println(e);
			System.out.println("Non riesco a scrivere!!!");
		}

	}

}

