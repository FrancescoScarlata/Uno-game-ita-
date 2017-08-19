import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;
import javax.swing.table.*;

public class Records
{
	public void records(final JFrame w, String nomeutente)
	{
		//Dimensioni window
		Dimension monitor= Toolkit.getDefaultToolkit().getScreenSize();

		final JWindow subw=new JWindow(); //finestra utilizzata
		if (monitor.getWidth()>1000)
			subw.setSize(800,550);

		else
			subw.setSize(400,300);

		subw.setLocationRelativeTo(null);
		subw.setVisible(true);

				//array con le descrizioni
		String [] descrizioni={	"Totale partite perse", "Totale partite vinte", "Partita col piu' alto punteggio", "Partita col punteggio piu' basso",
						"Totale round persi", "Totale round vinti", "Round col piu' alto punteggio", "Round col punteggio piu' basso","",
						"Dona "+'"'+" Pesca Due"+'"', "Ricevi "+'"'+"Pesca Due"+'"','"'+"Cambio colore"+'"'+" effettuati", "Bluff "+'"'+"Pesca Quattro"+'"'+" riusciti", '"'+"Pesca Quattro"+'"'+" effettuati",
						"Bluff scoperti", "Inversioni effettuate", "Inversioni rinviate", "Penalita'"+'"'+"Uno!"+'"'+" subite", "Penalita' "+'"'+"Uno!"+'"'+" inviate",
						'"'+"Uno!"+'"'+" effettuati con successo", '"'+"Stop!"+'"'+" effettuati", '"'+"Stop!"+'"'+" subiti", '"'+"Stop!"+'"'+" rilanciati",
						"Carte scartate per numero", "Carte scartate per colore", "Strani episodi di gioco quotidiano","TOTALE PUNTEGGIO"};
	
				//array con le classifiche
		String [] classifiche= {"Caso disperato", "L'inverno e' arrivato", "Novizio", "Sempre in tempo per non arrugginirsi", "L'esercizio e' la via",
						"Fu cosi' che inizio' la stima", "Iniziato al gioco","Affiliato","Ci sono cose che noi umani non possiamo immaginare",
						"Giocatore di professione","Mr Uno", "Complimenti", "Baciamo le mani!","La forza sia con te",
						"Docente del gioco", "Top", "Super top" };
			
		int [] punteggi; //array per i punteggi letti da file

		DefaultTableModel data = new DefaultTableModel(); //crea un modello di tabella

		//crea l'intestazione
		data.addColumn("Descrizione");
		data.addColumn("Punteggio");
		data.addColumn("Classifica");
						
		punteggi=readPunt(nomeutente,descrizioni.length);
		int riga = data.getRowCount();
		data.setRowCount(riga+descrizioni.length+2); //modifica il numero di righe del modello di tabella

		for (;riga<descrizioni.length-1; riga++)
		{
			data.setValueAt(descrizioni[riga], riga, 0);//prima colonna
			if (riga!=8) //caso "limite" in cui la riga deve essere vuota
				data.setValueAt(punteggi[riga], riga, 1); //seconda colonna

			data.setValueAt(rank(punteggi[riga],classifiche,riga), riga, 2);	// terza colonna

		}

			riga= descrizioni.length-1;

			data.setValueAt(descrizioni[descrizioni.length-1], riga+2, 0); //inserisce il valore alla prima colonna dell'ultima riga
			setPunt(data,descrizioni);						//inserisce il valore alla seconda colonna dell'ultima riga
			rankPunt(Float.parseFloat(data.getValueAt(riga+2,1).toString()), data, riga, classifiche);	//inserisce il valore alla terza colonna dell'ultima riga

		JTable table = new JTable(data) //inserisce i dati in tabella
		{
			@Override public boolean isCellEditable(int row, int col) //setta a false la modifica delle celle
				{
					return false;
				}
		};
		
		table.setAutoscrolls(true);
		table.getTableHeader().setBackground(Color.blue); //colora lo sfondo dell'intestazione di blu
		table.getTableHeader().setForeground(Color.white); //colora il formato dell'intestazione di bianco

		table.getTableHeader().setReorderingAllowed(false); //setta il riordinamento della tabella a falso

		JScrollPane scroller = new JScrollPane(table,
			JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
			JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED); //crea lo scroll

		JViewport x=scroller.getViewport();
		x.setBackground(Color.DARK_GRAY); //colora lo sfondo della table in grigio scuro

		table.setBackground(Color.DARK_GRAY);//colora lo scroll in grigio scuro
		table.setForeground(Color.WHITE);//colora il testo all'interno dello scroll in bianco
		table.setGridColor(Color.white); //colora di bianco la griglia della table

		table.setDragEnabled(false);

		JButton back= new JButton("Torna indietro"); //crea il button per tornare indietro

		back.setBackground(Color.RED); //colora lo sfondo del button in rosso
		back.setForeground(Color.WHITE);//colora il testo all'interno del button in bianco

		if (monitor.getWidth()>1000)
			back.setBounds( 280,510,250,40);
		else
			back.setBounds( 50, 260,100 ,40);

		subw.add(back); // aggiunge back alla finestra
		subw.add(scroller); // aggiunge lo scroll alla finestra

		subw.setAlwaysOnTop(true);

		back.addActionListener(new ActionListener() //gestisce direttamente l'evento per evitare richieste superflue
		{
			public void actionPerformed(ActionEvent e)
			{

				subw.dispose();
				w.setAlwaysOnTop(true);
			}
		});




		return;
	}

	public void rankPunt(float punt, DefaultTableModel data, int riga, String [] classifiche ) // il metodo che restituisce il rango in base al punteggio
	{
		if (punt==0)
			data.setValueAt("Vuoto", riga+2, 2);

		if ((punt>0)&&(punt<50))
			data.setValueAt(classifiche[0], riga+2, 2);

		if ((punt>=50)&&(punt<100))
			data.setValueAt(classifiche[1], riga+2, 2);

		if ((punt>=100)&&(punt<150))
			data.setValueAt(classifiche[2], riga+2, 2);

		if ((punt>=150)&&(punt<250))
			data.setValueAt(classifiche[3], riga+2, 2);

		if( (punt>=250)&&(punt<500))
			data.setValueAt(classifiche[4], riga+2, 2);

		if ((punt>=500)&&(punt<750))
			data.setValueAt(classifiche[5], riga+2, 2);

		if ((punt>=750)&&(punt<1000))
			data.setValueAt(classifiche[6], riga+2, 2);

		if ((punt>=1000)&&(punt<1500))
			data.setValueAt(classifiche[7], riga+2, 2);

		if ((punt>=1500)&&(punt<2000))
			data.setValueAt(classifiche[8], riga+2, 2);

		if ((punt>=2000)&&(punt<2500))
			data.setValueAt(classifiche[9], riga+2, 2);

		if ((punt>=2500)&&(punt<3000))
			data.setValueAt(classifiche[10], riga+2, 2);

		if ((punt>=3000)&&(punt<5000))
			data.setValueAt(classifiche[11], riga+2, 2);

		if ((punt>=5000)&&(punt<7000))
			data.setValueAt(classifiche[12], riga+2, 2);

		if ((punt>=7000)&&(punt<9000))
			data.setValueAt(classifiche[13], riga+2, 2);

		if ((punt>=10000)&&(punt<15000))
			data.setValueAt(classifiche[14], riga+2, 2);

		if ((punt>=20000)&&(punt<50000))
			data.setValueAt(classifiche[15], riga+2, 2);

		if (punt>50000)
			data.setValueAt(classifiche[16], riga+2, 2);
		return;
	}

	public void setPunt(DefaultTableModel data, String [] desc) //  il metodo che inserisce in table il punteggio totale
	{
		byte riga;
		float punt=0;
		for (riga=0; riga<desc.length-1;riga++)

			switch(riga)
			{
				case 0:punt-=Integer.parseInt(data.getValueAt(riga,1).toString());
				break;
				case 1:punt+=Integer.parseInt(data.getValueAt(riga,1).toString());
				break;
				case 2:punt+=Integer.parseInt(data.getValueAt(riga,1).toString());
				break;
				case 3:punt-=Integer.parseInt(data.getValueAt(riga,1).toString());
				break;
				case 4:punt-=Integer.parseInt(data.getValueAt(riga,1).toString());
				break;
				case 5:punt+=Integer.parseInt(data.getValueAt(riga,1).toString());
				break;
				case 6:punt+=Integer.parseInt(data.getValueAt(riga,1).toString());
				break;
				case 7:punt-=Integer.parseInt(data.getValueAt(riga,1).toString());
				break;

				case 9:punt+=Integer.parseInt(data.getValueAt(riga,1).toString());
				break;
				case 10: punt-=Integer.parseInt(data.getValueAt(riga,1).toString());
				break;
				case 11: if (Integer.parseInt(data.getValueAt(riga-2,1).toString())>=Integer.parseInt(data.getValueAt(riga-1,1).toString()))
						punt++;
					else
						punt--;
					break;
				case 12: punt+=Integer.parseInt(data.getValueAt(riga,1).toString())*5;
				break;
				case 13: if (Integer.parseInt(data.getValueAt(riga-1,1).toString())>Integer.parseInt(data.getValueAt(riga+1,1).toString()))
						punt++;
					else
						punt--;
					break;
				case 14: punt-=Integer.parseInt(data.getValueAt(riga,1).toString())*2;
				break;
				case 15: punt+=Integer.parseInt(data.getValueAt(riga,1).toString())*2;
				break;
				case 16: punt+=Integer.parseInt(data.getValueAt(riga,1).toString())*3;
				break;
				case 17: punt-=Integer.parseInt(data.getValueAt(riga,1).toString())*5;
				break;
				case 18: punt+=Integer.parseInt(data.getValueAt(riga,1).toString())*5;
				break;
				case 19: punt+=Integer.parseInt(data.getValueAt(riga,1).toString())*2;
				break;
				case 20: punt+=Integer.parseInt(data.getValueAt(riga,1).toString());
				break;
				case 21: punt-=Integer.parseInt(data.getValueAt(riga,1).toString());
				break;
				case 22: punt+=Integer.parseInt(data.getValueAt(riga,1).toString())*3;
				break;
				case 23: punt+=Integer.parseInt(data.getValueAt(riga,1).toString())*(100/108);
				break;
				case 24: punt+=Integer.parseInt(data.getValueAt(riga,1).toString())*((108-25)/108);
				break;
				case 25: punt+=Integer.parseInt(data.getValueAt(riga,1).toString())*50;
				break;

			}

		data.setValueAt(punt, riga+2, 1);
			return;
	}

	public String rank(float score, String [] classifiche, int riga) //e'  il metodo che ritorna la stringa con il rango riguardo al suo punteggio
	{
		switch(riga)
		{
			case 0:
			{
				if (score==0)
					return "Vuoto";
				if ((score>0)&&(score<=10))
					return classifiche[15];
				if ((score>10)&&(score<25))
					return classifiche[10];
				if ((score>=25)&&(score<100))
				return classifiche[9];
				if ((score>=100)&&(score<250))
				return classifiche[6];
				if ((score>=250)&&(score<500))
					return classifiche[3];
				if ((score>=500)&&(score<1000))
					return classifiche[1];
				else
					return classifiche[0];
			}
			case 1:
			{
				if (score==0)
					return "Vuoto";
				if (((score)>0)&&((score)<=10))
					return classifiche[2];
				if ((score>10)&&(score<25))
					return classifiche[6];
				if ((score>=25)&&(score<100))
				return classifiche[7];
				if ((score>=100)&&(score<250))
				return classifiche[9];
				if ((score>=250)&&(score<500))
					return classifiche[11];
				if ((score>=500)&&(score<1000))
					return classifiche[14];
				else
					return classifiche[15];


			}
			case 2:
			{
				if (score==0)
					return "Vuoto";
				if (((score)>0)&&((score)<=10))
					return classifiche[2];
				if ((score>10)&&(score<25))
					return classifiche[6];
				if ((score>=25)&&(score<100))
				return classifiche[7];
				if ((score>=100)&&(score<250))
				return classifiche[9];
				if ((score>=250)&&(score<500))
					return classifiche[11];
				if ((score>=500)&&(score<1000))
					return classifiche[14];
				else
					return classifiche[15];


			}

			case 3:
			{
				if (score==0)
					return "Vuoto";
				if ((score>0)&&(score<=10))
					return classifiche[15];
				if ((score>10)&&(score<25))
					return classifiche[10];
				if ((score>=25)&&(score<100))
				return classifiche[9];
				if ((score>=100)&&(score<250))
				return classifiche[6];
				if ((score>=250)&&(score<500))
					return classifiche[3];
				if ((score>=500)&&(score<1000))
					return classifiche[1];
				else
					return classifiche[0];



			}
			case 4:
			{
				if (score==0)
					return "Vuoto";
				if ((score>0)&&(score<=10))
					return classifiche[15];
				if ((score>10)&&(score<25))
					return classifiche[10];
				if ((score>=25)&&(score<100))
				return classifiche[9];
				if ((score>=100)&&(score<250))
				return classifiche[6];
				if ((score>=250)&&(score<500))
					return classifiche[3];
				if ((score>=500)&&(score<1000))
					return classifiche[1];
				else
					return classifiche[0];
			}
			case 5:
			{
				if (score==0)
					return "Vuoto";
				if (((score)>0)&&((score)<=10))
					return classifiche[2];
				if ((score>10)&&(score<25))
					return classifiche[6];
				if ((score>=25)&&(score<100))
				return classifiche[7];
				if ((score>=100)&&(score<250))
				return classifiche[9];
				if ((score>=250)&&(score<500))
					return classifiche[11];
				if ((score>=500)&&(score<1000))
					return classifiche[14];
				else
					return classifiche[15];


			}
			case 6:
			{
				if (score==0)
					return "Vuoto";
				if (((score)>0)&&((score)<=10))
					return classifiche[2];
				if ((score>10)&&(score<25))
					return classifiche[6];
				if ((score>=25)&&(score<100))
				return classifiche[7];
				if ((score>=100)&&(score<250))
				return classifiche[9];
				if ((score>=250)&&(score<500))
					return classifiche[11];
				if ((score>=500)&&(score<1000))
					return classifiche[14];
				else
					return classifiche[15];


			}

			case 7:
			{
				if (score==0)
					return "Vuoto";
				if ((score>0)&&(score<=10))
					return classifiche[15];
				if ((score>10)&&(score<25))
					return classifiche[10];
				if ((score>=25)&&(score<100))
				return classifiche[9];
				if ((score>=100)&&(score<250))
				return classifiche[6];
				if ((score>=250)&&(score<500))
					return classifiche[3];
				if ((score>=500)&&(score<1000))
					return classifiche[1];
				else
					return classifiche[0];



			}
			/*
			case 8:
			{
				return "";
			}
			*/
			case 9:
			{
				if (score==0)
					return "Vuoto";
				if ((score>0)&&(score<=10))
					return classifiche[15];
				if ((score>10)&&(score<25))
					return classifiche[10];
				if ((score>=25)&&(score<100))
				return classifiche[9];
				if ((score>=100)&&(score<250))
				return classifiche[6];
				if ((score>=250)&&(score<500))
					return classifiche[3];
				if ((score>=500)&&(score<1000))
					return classifiche[1];
				else
					return classifiche[0];



			}

			case 10:
			{

				if (score==0)
					return "Vuoto";
				if ((score>0)&&(score<=10))
					return classifiche[2];
				if ((score>10)&&(score<25))
					return classifiche[4];
				if ((score>=25)&&(score<100))
				return classifiche[5];
				if ((score>=100)&&(score<250))
				return classifiche[7];
				if ((score>=250)&&(score<500))
					return classifiche[9];
				if ((score>=500)&&(score<1000))
					return classifiche[12];
				if ((score>=1000)&&(score<2000))
					return classifiche[14];
				else
					return classifiche[15];


			}

			case 11:
			{
				if (score==0)
					return "Vuoto";
				if ((score>0)&&(score<=10))
					return classifiche[2];
				if ((score>10)&&(score<25))
					return classifiche[4];
				if ((score>=25)&&(score<100))
				return classifiche[5];
				if ((score>=100)&&(score<250))
				return classifiche[7];
				if ((score>=250)&&(score<500))
					return classifiche[9];
				if ((score>=500)&&(score<1000))
					return classifiche[12];
				if ((score>=1000)&&(score<2000))
					return classifiche[14];
				else
					return classifiche[15];


			}
			case 12:
			{
				if (score==0)
					return "Vuoto";
				if ((score>0)&&(score<=10))
					return classifiche[15];
				if ((score>10)&&(score<25))
					return classifiche[10];
				if ((score>=25)&&(score<100))
				return classifiche[9];
				if ((score>=100)&&(score<250))
				return classifiche[6];
				if ((score>=250)&&(score<500))
					return classifiche[3];
				if ((score>=500)&&(score<1000))
					return classifiche[1];
				else
					return classifiche[0];



			}
			case 13:
			{
				if (score==0)
					return "Vuoto";
				if ((score>0)&&(score<=10))
					return classifiche[2];
				if ((score>10)&&(score<25))
					return classifiche[4];
				if ((score>=25)&&(score<100))
				return classifiche[5];
				if ((score>=100)&&(score<250))
				return classifiche[7];
				if ((score>=250)&&(score<500))
					return classifiche[9];
				if ((score>=500)&&(score<1000))
					return classifiche[12];
				if ((score>=1000)&&(score<2000))
					return classifiche[14];
				else
					return classifiche[15];



			}
			case 14:
			{
				if (score==0)
					return "Vuoto";
				if ((score>0)&&(score<=10))
					return classifiche[2];
				if ((score>10)&&(score<25))
					return classifiche[4];
				if ((score>=25)&&(score<100))
				return classifiche[5];
				if ((score>=100)&&(score<250))
				return classifiche[7];
				if ((score>=250)&&(score<500))
					return classifiche[9];
				if ((score>=500)&&(score<1000))
					return classifiche[12];
				if ((score>=1000)&&(score<2000))
					return classifiche[14];
				else
					return classifiche[15];


			}
			case 15:
			{
				if (score==0)
					return "Vuoto";
				if ((score>0)&&(score<=10))
					return classifiche[2];
				if ((score>10)&&(score<25))
					return classifiche[4];
				if ((score>=25)&&(score<100))
				return classifiche[5];
				if ((score>=100)&&(score<250))
				return classifiche[7];
				if ((score>=250)&&(score<500))
					return classifiche[9];
				if ((score>=500)&&(score<1000))
					return classifiche[12];
				if ((score>=1000)&&(score<2000))
					return classifiche[14];
				else
					return classifiche[15];


			}
			case 16:
			{
					if (score==0)
					return "Vuoto";
				if ((score>0)&&(score<=10))
					return classifiche[15];
				if ((score>10)&&(score<25))
					return classifiche[10];
				if ((score>=25)&&(score<100))
				return classifiche[9];
				if ((score>=100)&&(score<250))
				return classifiche[6];
				if ((score>=250)&&(score<500))
					return classifiche[3];
				if ((score>=500)&&(score<1000))
					return classifiche[1];
				else
					return classifiche[0];

			}
			case 17:
			{if (score==0)
					return "Vuoto";
				if ((score>0)&&(score<=10))
					return classifiche[2];
				if ((score>10)&&(score<25))
					return classifiche[4];
				if ((score>=25)&&(score<100))
				return classifiche[5];
				if ((score>=100)&&(score<250))
				return classifiche[7];
				if ((score>=250)&&(score<500))
					return classifiche[9];
				if ((score>=500)&&(score<1000))
					return classifiche[12];
				if ((score>=1000)&&(score<2000))
					return classifiche[14];
				else
					return classifiche[15];


			}
			case 18:
			{
				if (score==0)
					return "Vuoto";
				if ((score>0)&&(score<=10))
					return classifiche[2];
				if ((score>10)&&(score<25))
					return classifiche[4];
				if ((score>=25)&&(score<100))
				return classifiche[5];
				if ((score>=100)&&(score<250))
				return classifiche[7];
				if ((score>=250)&&(score<500))
					return classifiche[9];
				if ((score>=500)&&(score<1000))
					return classifiche[12];
				if ((score>=1000)&&(score<2000))
					return classifiche[14];
				else
					return classifiche[15];


			}

			case 19:
			{
				if (score==0)
					return "Vuoto";
				if ((score>0)&&(score<=10))
					return classifiche[2];
				if ((score>10)&&(score<25))
					return classifiche[4];
				if ((score>=25)&&(score<100))
				return classifiche[5];
				if ((score>=100)&&(score<250))
				return classifiche[7];
				if ((score>=250)&&(score<500))
					return classifiche[9];
				if ((score>=500)&&(score<1000))
					return classifiche[12];
				if ((score>=1000)&&(score<2000))
					return classifiche[14];
				else
					return classifiche[15];


			}

			case 20:
			{
				if (score==0)
					return "Vuoto";
				if ((score>0)&&(score<=10))
					return classifiche[0];
				if ((score>10)&&(score<25))
					return classifiche[1];
				if ((score>=25)&&(score<50))
				return classifiche[3];
				if ((score>=50)&&(score<100))
				return classifiche[6];
				if ((score>=100)&&(score<200))
					return classifiche[7];
				if ((score>=200)&&(score<400))
					return classifiche[8];
				if ((score>=400)&&(score<800))
					return classifiche[12];
				if ((score>=1200)&&(score<2400))
					return classifiche[13];
				if ((score>=2400)&&(score<4800))
					return classifiche[15];
				else
					return classifiche[16];



			}
			case 21:
			{
				if (score==0)
					return "Vuoto";
				if ((score>0)&&(score<=10))
					return classifiche[0];
				if ((score>10)&&(score<25))
					return classifiche[1];
				if ((score>=25)&&(score<50))
				return classifiche[3];
				if ((score>=50)&&(score<100))
				return classifiche[6];
				if ((score>=100)&&(score<200))
					return classifiche[7];
				if ((score>=200)&&(score<400))
					return classifiche[8];
				if ((score>=400)&&(score<800))
					return classifiche[12];
				if ((score>=1200)&&(score<2400))
					return classifiche[13];
				if ((score>=2400)&&(score<4800))
					return classifiche[15];
				else
					return classifiche[16];



			}
			case 22:
			{
				if (score==0)
					return "Vuoto";
				if ((score>0)&&(score<=10))
					return classifiche[0];
				if ((score>10)&&(score<25))
					return classifiche[1];
				if ((score>=25)&&(score<50))
				return classifiche[3];
				if ((score>=50)&&(score<100))
				return classifiche[6];
				if ((score>=100)&&(score<200))
					return classifiche[7];
				if ((score>=200)&&(score<400))
					return classifiche[8];
				if ((score>=400)&&(score<800))
					return classifiche[12];
				if ((score>=1200)&&(score<2400))
					return classifiche[13];
				if ((score>=2400)&&(score<4800))
					return classifiche[15];
				else
					return classifiche[16];



			}
			case 23:
			{
				if (score==0)
					return "Vuoto";
				if ((score>0)&&(score<=10))
					return classifiche[0];
				if ((score>10)&&(score<25))
					return classifiche[1];
				if ((score>=25)&&(score<50))
				return classifiche[3];
				if ((score>=50)&&(score<100))
				return classifiche[6];
				if ((score>=100)&&(score<200))
					return classifiche[7];
				if ((score>=200)&&(score<400))
					return classifiche[8];
				if ((score>=400)&&(score<800))
					return classifiche[12];
				if ((score>=1200)&&(score<2400))
					return classifiche[13];
				if ((score>=2400)&&(score<4800))
					return classifiche[15];
				else
					return classifiche[16];



			}
			case 24:
			{
				if (score==0)
					return "Vuoto";
				if ((score>0)&&(score<=10))
					return classifiche[0];
				if ((score>10)&&(score<25))
					return classifiche[1];
				if ((score>=25)&&(score<50))
				return classifiche[3];
				if ((score>=50)&&(score<100))
				return classifiche[6];
				if ((score>=100)&&(score<200))
					return classifiche[7];
				if ((score>=200)&&(score<400))
					return classifiche[8];
				if ((score>=400)&&(score<800))
					return classifiche[12];
				if ((score>=1200)&&(score<2400))
					return classifiche[13];
				if ((score>=2400)&&(score<4800))
					return classifiche[15];
				else
					return classifiche[16];



			}
			case 25:
			{
				if (score==0)
					return "Vuoto";
				if ((score>0)&&(score<=10))
					return "wth?";
				if ((score>10)&&(score<25))
					return "are you serious?";
				if ((score>=25)&&(score<50))
				return "A volte tutto puo' succedere...";
				if ((score>=50)&&(score<100))
				return "Credi nella forza, perche' credere nella realta' non conviene";
				if ((score>=100)&&(score<200))
					return "Se dio esiste ti voleva troppo bene... Stai attento!";
				if ((score>=200)&&(score<400))
					return classifiche[8];
				if ((score>=400)&&(score<800))
					return classifiche[12];
				if ((score>=1200)&&(score<2400))
					return classifiche[13];
				if ((score>=2400)&&(score<4800))
					return classifiche[15];
				else
					return classifiche[16];



			}



		}
		return "???";
	}

	public static int [] readPunt(String nomeutente,int lunghezza) //legge i punteggi da input e li inserisce in un array
	{
		
		int n;
		int [] desc= new int [lunghezza-1];
		try
		{
			boolean flag=true;
			FileReader filetxt = new FileReader((System.getenv("USERPROFILE")).toString()+"/AppData/Local/Uno/data/"+nomeutente+"/record.txt");
			for (int i=0; i<(desc.length-1)&&flag; i++)
			{
				for (byte x=3;((n=filetxt.read()) !=-1)&&(x>=0);x-- )
				{
					if (n!=13&&n!=10)
					{
						//legge la cifra e la somma alle altre per significatività
						desc[i]+=(n-48)*Math.pow(10,x);
						/*
						if (x==0)
							desc[i]+=(n-48);
							*/
					}
					else
					{
						//caso in cui il numero e' causato da spazi e invii
						//System.out.print(n+" ; ");
						//System.out.println(x+" ; ");
						x++;
					}

				}

				//System.out.println();


				if (n==-1)
				{
					flag=false;
					filetxt.close();

				}
			}


		}
		catch(FileNotFoundException e)
		{
			System.out.println(e);
			System.out.println("try again!");

			JOptionPane.showMessageDialog(null, "Non esiste l'utente desiderato", "Attenzione", JOptionPane.ERROR_MESSAGE);
			creaNuovoRecords(nomeutente, lunghezza);
		}
		catch(IOException e)
		{
			System.out.println(e);
			//System.exit(1);
		}

		return desc;

	}

	public static void creaNuovoRecords(String nomeutente, int length) //crea un nuovo records per l'utente chiesto
	{
		try
		{
			FileWriter fileout = new FileWriter((System.getenv("USERPROFILE")).toString()+"/AppData/Local/Uno/data/"+nomeutente+"/record.txt");
			for (byte x=0; x<length-1; x++)
			{
				for (byte y=0; y<4; y++)
					fileout.write(48);
				fileout.write(13);
				fileout.write(10);
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
