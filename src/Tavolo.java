//non ancora ultimato.  l'abbozzo di ci che dovrebbe fare il tavolo
import javax.swing.*;

public class Tavolo
{
	private Carta [] mazzor; //mazzo di riserva (dal quale pescare)
	private Carta [] mazzos; //mazzo di scarto
	private Giocatore [] giocatori;
	private char [] colore= new char[2];
	private boolean giro; //true indica il senso orario
	private int posmazzor; //indica la posizione nel mazzo di riserva
	private int posmazzos; //indica la posizione nel mazzo di scarto

	public Tavolo(String [] nomi)
	{

		// inizializzazione del mazzo di riserva
		 inizializzaMazzo();
		//mescolamento del mazzo
		mescola();
		inizializzaGiocatori(nomi);
		giro=true;
		mazzos= new Carta [108];

	}

	public Tavolo(String utente)
	{
		String [] nomi= {utente, "Giocatore2", "Giocatore3", "Giocatore4"} ;
		// inizializzazione del mazzo di riserva
		inizializzaMazzo();
		//mescolamento del mazzo
		mescola();
		// inizializzazione dei giocatori con i nomi di default
		inizializzaGiocatori(nomi);
		giro=true;
		mazzos= new Carta [108];

	}


	private void  inizializzaGiocatori(String [] nomi)
	{
		giocatori= new Giocatore [nomi.length];
		giocatori[0]= new Giocatore(false,nomi[0]);
		for (byte i=1; i<giocatori.length; i++)
				giocatori[i]= new Giocatore(true, nomi[i]);
		return ;

	}

	
	

	protected void inizializzaMazzo()
	{
		
		mazzor= new Carta [108];
		char [] colori= {'r', 'b', 'v', 'g'};
		
		String [] funzioni= {"Pesca_due", "Cambio_giro", "Stop", "Cambio_colore", "Pesca_quattro"};
		String x=""+System.getProperty("user.dir") + "\\img\\Carte" +"\\";
		
		int i=0;
		byte col=0;
		byte num;

		while( i<76)
		{
			num=0;
			mazzor[i]=new Carta(num, colori[col], x+(num)+colori[col]+".png", x+(num)+colori[col]+"o.png");
			num++;
			while (num<=9)
			{

				mazzor[++i]=new Carta(num,colori[col],x+num+colori[col]+".png", x+num+colori[col]+"o.png");
				mazzor[++i]=new Carta(num,colori[col], x+num+colori[col]+".png", x+num+colori[col]+"o.png");
				num++;
			}
			i++;
			col++;
		}

		col=0;

		while(i<100)
		{
			num=10;
			while (num<=12)
			{
				mazzor[i]=new Speciale(num,colori[col], x+funzioni[num-10]+colori[col]+".png", x+funzioni[num-10]+colori[col]+"o.png", funzioni[num-10] );
				mazzor[++i]=new Speciale(num,colori[col], x+funzioni[num-10]+colori[col]+".png",x+funzioni[num-10]+colori[col]+".png", funzioni[num-10] );

				i++;
				num++;
			}
			col++;
		}
		mazzor[i]=new Speciale((byte)13,'n', x+funzioni[3]+'n'+".png", x+funzioni[3]+'n'+"o.png", funzioni[3]);
		mazzor[i+1]=new Speciale((byte)13,'n', x+funzioni[3]+'n'+".png", x+funzioni[3]+'n'+"o.png", funzioni[3]);
		mazzor[i+2]=new Speciale((byte)13,'n', x+funzioni[3]+'n'+".png", x+funzioni[3]+'n'+"o.png", funzioni[3]);
		mazzor[i+3]=new Speciale((byte)13,'n', x+funzioni[3]+'n'+".png", x+funzioni[3]+'n'+"o.png", funzioni[3]);
		
		mazzor[i+4]=new Speciale((byte)14,'n', x+funzioni[4]+'n'+".png", x+funzioni[4]+'n'+"o.png", funzioni[4]);
		mazzor[i+5]=new Speciale((byte)14,'n', x+funzioni[4]+'n'+".png", x+funzioni[4]+'n'+"o.png", funzioni[4]);
		mazzor[i+6]=new Speciale((byte)14,'n', x+funzioni[4]+'n'+".png", x+funzioni[4]+'n'+"o.png" ,funzioni[4]);
		mazzor[i+7]=new Speciale((byte)14,'n', x+funzioni[4]+'n'+".png", x+funzioni[4]+'n'+"o.png",funzioni[4]);

		return;
		
	}


	public void mescola()
	{
		int x, y;
		Carta temp;
		for (int i=0; i<(5000+(Math.random()*2000)); i++)
		{
			temp=mazzor[x=(int)(Math.random()*mazzor.length)];
			mazzor[x]=mazzor[y=(int)(Math.random()*mazzor.length)];
			mazzor[y]=temp;
			//System.out.println(i);
			// scambia le carte un numero di volte pari ad un random
		}
		posmazzor=0;
		posmazzos=0;
	}

	public boolean isOver()
	{
		if (posmazzor>=mazzor.length)
			return true;
		else
			return false;
	}

	public void setCol(char c)
	{
		if(c=='V')
			c='v';
		if(c=='R')
			c='r';
		if(c=='G')
			c='g';
		if(c=='B')
			c='b';
		colore[0]=colore[1];
		colore[1]=c;
	}

	public char [] getColore()
	{
		return colore;
	}

	public void setGiro(boolean g)
	{
		giro=g;
	}
	public boolean getGiro()
	{
		return giro;
	}

	public Carta getUltimaCarta()
	{
		return mazzos[posmazzos-1];
	}

	public Giocatore getPlayer(int x)
	{
		return giocatori[x];
	}

	public Carta[] getMazzor()
	{
		return mazzor;
	}
	
	public Carta[] getMazzos()
	{
		return mazzos;
	}
	
	public int getPosMazzos()
	{
		return posmazzos;
	}
	
	public Carta pescata()
	{
		return mazzor[posmazzor++];
	}

	public void scartata(Carta scarto)
	{
		mazzos[posmazzos]=scarto;
		//JOptionPane.showMessageDialog(null, scarto.getImg()[0]);
		//System.out.println("La carta dovrebbe essere stata inserita");
		posmazzos++;
	}

	public void fineMazzor(Carta [] mazzo)
	{
		mazzor= mazzo;
		posmazzor=0;
		posmazzos=0;
	}
	
}
