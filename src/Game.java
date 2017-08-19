import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;

class Gioco extends JLayeredPane
{
	//variabili del gioco
	protected boolean salta=false; //indica la volonta di saltare il turno
	protected boolean pescare=true; //indica se nel proprio turno si puo pescare o meno (utilizzato per non fare pescare + di una volta
	protected byte cont=0; //contatore che gestisce le operazioni delle carte "azioni"
	protected boolean turnoutente=false;
	protected char colore='n'; //indica il colore del tavolo

	//dimensioni
	private Dimension monitor= Toolkit.getDefaultToolkit().getScreenSize();
	private final int M_X=(int)monitor.getWidth(); //larghezza monitor
	private final int M_Y=(int)monitor.getHeight(); //altezza monitor
	private final int L_X=109; //larghezza etichetta
	private final int L_Y=180; //altezza etichetta
	private final int P_X=180; //larghezza pannelli
	private final int P_Y=700; //altezza pannelli
	private final int GAP=15; //spazio tra mazzo e scarto

	//componenti
	//il pannello principale
	private ImagePanel backingPanel = new ImagePanel();
	//panelli dei giocatori
	 protected JPanel pNorth=new JPanel(new GridBagLayout());
	protected JPanel pEast=new JPanel();
	protected JPanel pSouth=new JPanel(new GridBagLayout());
	protected JPanel pWest=new JPanel();
	//label con il numero di carte di ciascun giocatore
	protected JLabel [] numcarte;
	protected JLabel []nomigioc;
	//colore del tavolo
	protected JButton color= new JButton();
	protected JLabel lap= new JLabel();
	//pannelli con del mazzo di riserva e del mazzo "scarti"
	private ImagePanel pDeck=new ImagePanel();
	private ImagePanel pDiscard=new ImagePanel();
	//componente utile per "rallentare" i giocatori "non umani"
	protected Thread thread= new Thread();
	//pannello dei bottoni
	private JPanel pButtons=new JPanel();
	//pannello con i colori

	protected static Tavolo table;
	//creazione dell'array records
	protected int [] records= new int [26];

	//creazione dell'array opzioni
	protected int [] options=new int [3];

	//Variabili usate per gestire achievement
	static byte[] achivalue = new byte[10]; 


	public Gioco(final JFrame w, final JWindow subw, final String utente)
	{
		//modifiche grafiche
		backingPanel.setLayout(null);
		backingPanel.setImage("./img/bg_game.jpg");
		pEast.setLayout(new BoxLayout(pEast, BoxLayout.Y_AXIS));
		pWest.setLayout(new BoxLayout(pWest, BoxLayout.Y_AXIS));
		pDeck.setLayout(new GridBagLayout());
		pDiscard.setLayout(new GridBagLayout());
		pButtons.setLayout(null);

		backingPanel.setSize(monitor);

		subw.setSize(M_X,M_Y);

		//lettura dei records
		records=Records.readPunt(utente, 27);
		//lettura delle opzioni
		options=GameUtil.readOptions(options, utente);

		//musica
		System.out.println("Musica: "+ options[0]);
		//giocatori
		System.out.println("Numero giocatori: "+(options[1]+1));
		//livello difficolta
		System.out.println("Difficolta' " +options[2]);
		
		int cont_round = 0;
		for (int i =0; i<records.length;i++)
			System.out.println(records[i] +" ... ");

		//setta l'immagine del pannello delle carte di riserva
		pDeck.setImage(""+System.getProperty("user.dir") + "\\img\\Carte" +"\\"+"backn.png");

		//creazione giocatori
		String [] nomi= new String[options[1]+1];
		nomi[0]=utente;

		//scelta dell'utente sul nome dei giocatori
		if(JOptionPane.showConfirmDialog(null,"Vuoi scegliere i nomi dei tuoi avversari?","Preferenze",
				JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION)
		{
			for (byte i=1; i<nomi.length; i++)
				nomi[i]=JOptionPane.showInputDialog("Inserisci il nome dell'avversario nr."+(i));
			table= new Tavolo(nomi);
		}
		else
			table= new Tavolo(utente);

		for (byte i=0; i<nomi.length; i++)
			nomi[i]=table.getPlayer(i).toString();

		//CARICAMENTO DATI ACHIEVEMENT 
		GameAchievements.LoadDataAchi(achivalue, utente);
		
		//gestione achievement numero -2 : hai iniziato la prima partita
		if (achivalue[2] != 1)
			achivalue=GameAchievements.updateAchievement(achivalue, 2, utente);

		numcarte= new JLabel[options[1]];
		nomigioc=new JLabel[options[1]];

		for (byte i=0; i<(options[1]); i++)
		{
			String x= "Numero di carte : "+table.getPlayer(i+1).getNumCarte();
			numcarte[i]= new JLabel(x);
			nomigioc[i]=new JLabel(table.getPlayer(i+1).toString());
		}
		
		//creazione di un'array per inserire i punteggi del giocatori
		int [] score= new int [nomi.length];
		for (int i=0; i<score.length; i++)
			score[i]=0;

		numcarte[0].setBounds((int)monitor.getWidth()/2-50, 200,150,30);
		nomigioc[0].setBounds((int)monitor.getWidth()/2-20, 200-30,100,30);
		if (options[1]==3)
		{
			numcarte[1].setBounds((int)monitor.getWidth()/10*7+80, (int)monitor.getHeight()/2,150,30);
			nomigioc[1].setBounds((int)monitor.getWidth()/10*7+100, (int)monitor.getHeight()/2-30,100,30);
			numcarte[2].setBounds(200,(int)monitor.getHeight()/2,150,50);
			nomigioc[2].setBounds(200+20,(int)monitor.getHeight()/2-30,100,30);
		}
		else if (options[1]==2)
		{
			numcarte[1].setBounds((int)monitor.getWidth()/10*7+80, (int)monitor.getHeight()/2,100,30);
			nomigioc[1].setBounds((int)monitor.getWidth()/10*7+100, (int)monitor.getHeight()/2-30,100,30);
		}

		color.setBounds((int)monitor.getWidth()/2+150, 400,50,50);
		lap.setBounds((int)monitor.getWidth()/2+150, 350,50,50);
		//backingPanel.setBackground(Color.gray);

		//pulsanti
		JButton one=new JButton("Uno!");
		one.setBackground(Color.white);
		one.setForeground(Color.black);
		
		JButton accuse=new JButton("Accusa!");
		accuse.setBackground(Color.white);
		accuse.setForeground(Color.black);
		
		JButton skip=new JButton("Passa il turno");
		skip.setBackground(Color.white);
		skip.setForeground(Color.black);
		
		JButton exit=new JButton("Abbandona la partita");
		exit.setBackground(Color.white);
		exit.setForeground(Color.black);

		//pulsanti di pButton
		one.setBounds(0,0,150,40);
		accuse.setBounds(150,0,170,40);
		skip.setBounds(0,40,150,40);
		exit.setBounds(150,40,170,40);
		pButtons.add(one);
		pButtons.add(accuse);
		pButtons.add(skip);
		pButtons.add(exit);

		pButtons.setBackground(Color.darkGray);

		pNorth.setOpaque(false);
		pEast.setOpaque(false);
		pWest.setOpaque(false);
		pSouth.setOpaque(false);
		pDeck.setOpaque(false);
		pDiscard.setOpaque(false);

		//ascoltatori
		//pulsanti buttons
		one.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				//se non hai detto "uno" dillo, altrimenti l'hai gia' fatto
				if(!table.getPlayer(0).getUno())
				{
					if (achivalue[8] != 100)
						achivalue=GameAchievements.updateAchievement(achivalue, 8, utente);
					table.getPlayer(0).uno();
					records[19]++;
				}
			}
		});
		accuse.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				String acc;
				do
				{
					acc= JOptionPane.showInputDialog("scegli che tipo di accusa vuoi portare avanti: \n a) accusa di bluff \n b) accusa di negligenza");
					if (acc.charAt(0)!='a'&&acc.charAt(0)!='A'&&acc.charAt(0)!='b'&&acc.charAt(0)!='B'||acc.equals("")||acc.equals(" "))
						JOptionPane.showMessageDialog(null,"Puoi scegliere solo tra a) e b)!");
				}while (acc.charAt(0)!='a'&&acc.charAt(0)!='A'&&acc.charAt(0)!='b'&&acc.charAt(0)!='B'||acc.equals("")||acc.equals(" "));
				if(acc.charAt(0)=='a'||acc.charAt(0)=='A')
				{
					if (table.getUltimaCarta().getNumero()==14&&cont!=0)
					{
						if(JOptionPane.showConfirmDialog(null,"Sei sicuro di volere accusare? La penalita'...", "Accusa", JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION)
						{
							if(table.getGiro())
							{
								gestioneAccusa(table,(byte)0,options[1],cont);
								cont=0;
							}

							else
							{
								gestioneAccusa(table,(byte)0,(byte) 1,cont);
								cont=0;
							}
						}
					}
					else
						JOptionPane.showMessageDialog(null,"Non puoi accusare se non c'e'  una probabile infrazione!");
				}
				else
				{
					if(table.getGiro())
					{
						if(table.getPlayer(options[1]).getNumCarte()==1&&!table.getPlayer(options[1]).getUno())
						{
							JOptionPane.showMessageDialog(null, "Hai ragione! "+table.getPlayer(options[1]).toString()+" doveva dire \"Uno! \" e non l'ha detto! Complimenti!\n"+table.getPlayer(options[1]).toString()+" prendera' due carte dal mazzo per negligenza!");
							pescaDue(table,(byte)options[1], (byte)1);
							records[19]++;
						}
						else
							JOptionPane.showMessageDialog(null, "Mi dispiace ma l'ha detto! "+table.getPlayer(options[1]).toString()+ " non prende penalita'");

					}
					else
					{
						if(table.getPlayer(1).getNumCarte()==1&&!table.getPlayer(1).getUno())
						{
							JOptionPane.showMessageDialog(null, "Hai ragione! "+table.getPlayer(1).toString()+" doveva dire \"Uno! \" e non l'ha detto! Complimenti!\n"+table.getPlayer(1).toString()+" prendera' due carte dal mazzo per negligenza!");
							pescaDue(table,(byte)options[1], (byte)1);
						}
						else
							JOptionPane.showMessageDialog(null, "Mi dispiace ma l'ha detto! "+table.getPlayer(1).toString()+ " non prende penalita");
					}
				}
			}
		});
		skip.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				if (!pescare){
					
					salta=true;
				}
				else
					JOptionPane.showMessageDialog(null,"Non puoi passare se prima non peschi!");
			}
		});

		exit.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				if(JOptionPane.showConfirmDialog(null,"Sei sicuro di voler tornare indietro? Tutti i progressi non salvati verranno persi.","Uscita",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION)
				{
					//chiusura
					subw.dispose();
					w.setVisible(true);
					w.setAlwaysOnTop(true);
				}
			}
		});

		//inizializzazione sfondo pannelli

		//posizionamento pannelli
		pNorth.setBounds(M_X/2-P_Y/2,0,P_Y,P_X);
		pEast.setBounds(M_X-P_X,M_Y/2-P_Y/2+200,P_X,P_Y);
		pSouth.setBounds(M_X/2-P_Y/2,M_Y-P_X,P_Y,P_X);
		pWest.setBounds(0,M_Y/2-P_Y/2+200,P_X,P_Y);
		pDeck.setBounds(M_X/2-L_X-GAP,M_Y/2-L_Y/2,L_X,L_Y);
		pDiscard.setBounds(M_X/2+GAP,M_Y/2-L_Y/2,L_X,L_Y);
		pButtons.setBounds(M_X-400,M_Y-130,320,80);

		//agganciamento componenti
		backingPanel.add(pNorth);
		backingPanel.add(pEast);
		backingPanel.add(pSouth);
		backingPanel.add(pWest);
		backingPanel.add(pDeck);
		backingPanel.add(pDiscard);
		for (byte i=0; i<(options[1]); i++)
		{
			backingPanel.add(numcarte[i]);
			backingPanel.add(nomigioc[i]);
		}
		backingPanel.add(color);
		backingPanel.add(lap);
		backingPanel.add(pButtons);


		setPreferredSize(monitor);
		add(backingPanel, JLayeredPane.DEFAULT_LAYER);
		subw.add(this);

		MyMouseAdapter myMouseAdapter = new MyMouseAdapter(table,subw);
		addMouseListener(myMouseAdapter);
		addMouseMotionListener(myMouseAdapter);


		JLabel deck= new JLabel();
		ImageIcon img= new ImageIcon(""+System.getProperty("user.dir") + "\\img\\Carte" +"\\"+"backn.png");
		img.setImage(img.getImage().getScaledInstance(107, 179,Image.SCALE_DEFAULT));
		deck.setIcon(img);
		pDeck.add(deck);
		subw.setVisible(true);


		//giro del gioco
		while(GameUtil.checkResults(score))
		{
			img= new ImageIcon(""+System.getProperty("user.dir") + "\\img" +"\\"+"right.PNG");
			img.setImage(img.getImage().getScaledInstance(50,50, Image.SCALE_DEFAULT));
			lap.setIcon(img);
			System.out.println("Sta iniziando un round!");

			//il mazziere da' le 7 carte ai giocatori
			//gestisce in base al numero di giocatori
			Carta card;

			for (int i=0; i<7; i++)
			{
				for (int p=0; p<(options[1]+1); p++)
				{
					//gestione pescata
					card=table.pescata();
					table.getPlayer(p).pesca(card);
					updatePanels(table,p);
					subw.setVisible(true);
				}
			}
			//prima carta scartata
			table.scartata(table.pescata());
			gestisciScarto(table);

			//gestione della prima carta - si potrebbe fare un metodo apparte?
			while(table.getUltimaCarta().getNumero()==14)
			{
				card=table.pescata();
				table.scartata(card);
				gestisciScarto(table);
			}
			if(table.getUltimaCarta().getColore()=='n')
				table.setCol(GameUtil.gestioneColori());
			else
			{
				table.setCol(table.getUltimaCarta().getColore());
				coloraColor(table);
			}
			if(table.getUltimaCarta().getNumero()==10||table.getUltimaCarta().getNumero()==11||table.getUltimaCarta().getNumero()==12)
			{
				cont++;
				if(table.getUltimaCarta().getNumero()==11)
				{
					table.setGiro(!table.getGiro());
					if(table.getGiro())
					{
						img= new ImageIcon(""+System.getProperty("user.dir") + "\\img" +"\\"+"right.PNG");
						img.setImage(img.getImage().getScaledInstance(50,50, Image.SCALE_DEFAULT));
						lap.setIcon(img);
					}
					else
					{
						img= new ImageIcon(""+System.getProperty("user.dir") + "\\img" +"\\"+"left.PNG");
						img.setImage(img.getImage().getScaledInstance(50,50, Image.SCALE_DEFAULT));
						lap.setIcon(img);
					}
				}
			}

			subw.setVisible(true);

			byte i=(byte)(Math.random()*options[1]+1);

			while (GameUtil.checkPlayersCards(options, table))
			{
				if (table.getPlayer(i).isBot())
				{
					cont=botMove(table, i, cont, options);
					//se e' finito il mazzo
					if (cont<0)
					{
						//gestione fine mazzo
						System.out.println("qualcosa non va");

						break;
					}
					setVisible(true);
					subw.setVisible(true);
				}
				else
				{
					System.out.print("Inizio turno : "+i);
					//System.out.print(cont+" ; ");
					subw.setVisible(true);
					//per ora l'utente e' gestito in maniera automatica
					if (table.getPlayer(0).getUno()==true&& table.getPlayer(0).getNumCarte() >1)
						table.getPlayer(0).uno();
					
					if(table.getUltimaCarta().getNumero()==12&&cont!=0)
					{
						salta=true;
						JOptionPane.showMessageDialog(null, "Sei Fermo");
						cont=0;
						records[21]++;
					}
					else
					{
						JOptionPane.showMessageDialog(null, "Tocca a te!");
						turnoutente=true;
					}
					do //se non ha finito il turno
					{
						if(salta)
							System.out.print("lalalala");
						//l'utente sta giocando
						System.out.print("");
					}while (!salta);
					System.out.print("Fine turno");
					salta=false;
					pescare=true;
					turnoutente=false;
				}

				//gestione del cambio turno nei round
				if (table.getGiro())
				{
					i++;
					if (i>(options[1]))
						i=0;
				}
				else
				{
					i--;
					if (i<0)
						i=(byte)(options[1]);
				}
				try
				{
					thread.sleep(1000);
				}
				catch (InterruptedException e)
				{
					System.out.println("E che ce vojo fa'!");
				}
			}

			//contatore rimesso a zero
			cont=0;

			if (table.getPlayer(0).getNumCarte()==0)
			{
				records[5]++;
				JOptionPane.showMessageDialog(null,"Hai vinto questo round! Attento a non perdere!");
				// gestione achievement - 7 - ha vinto una partita 100 volte
				if (achivalue[7] != 100)
 					achivalue=GameAchievements.updateAchievement(achivalue, 7, utente);
				
				
                                cont_round++;
				if (cont_round == 2)
				{
					// gestione achievement - 1 - ha fatto 25 volte un winning spree
				 	if (achivalue[1] != 25)
					{
						achivalue=GameAchievements.updateAchievement(achivalue, 1, utente);
					}
				}
			}
			else
			{
				JOptionPane.showMessageDialog(null,"Mi spiace, hai perso questo round ma potresti ancora rifarti! :)");
				records[4]++;
				cont_round = 0;
			}

			String punteggi="";
			for (int p=0; p<(options[1]+1);p++)
				punteggi+=""+table.getPlayer(p).toString()+"               ";
			punteggi+="\n";
			for (int p=0; p<(options[1]+1);p++)
			{
				score=GameUtil.punteggioTotalizzato(table, score, records,p);
				punteggi+= " "+score[p]+"           ";
				for (int space=0; space<table.getPlayer(p).toString().length(); space++)
					punteggi+= "  ";
			}
			
			JOptionPane.showMessageDialog(null,punteggi, "Punteggio",JOptionPane.INFORMATION_MESSAGE );

			if(GameUtil.checkResults(score))
			{
				for (int p=0; p<nomi.length; p++)
				{
					table.getPlayer(p).nuovaMano();
					switch (p)
					{
						case 0:
							pSouth.removeAll();
							break;
						case 1:
							pNorth.removeAll();
							break;

						case 2:
							pEast.removeAll();
							break;
						case 3:
							pWest.removeAll();
							break;
					}
				}
				table.inizializzaMazzo();
				table.mescola();
				table.setGiro(true);
			}

		}

		if (score[0]>=500) // Da richiamare da altro metodo
		{
			if (achivalue[4] != 50)
 			{
 				GameAchievements.updateAchievement(achivalue, 4, utente);
 			}
			//achievement - 0 - // Da richiamare da altro metodo
			for (int contS = 0;contS<score.length;contS++)
		 	{
				if (score[contS] >= 400)
				{
					if (achivalue[0] != 12)
					{
						achivalue=GameAchievements.updateAchievement(achivalue,0,utente);
					}
					break;
				}


			}
			 // Da richiamare da altro metodo - non fa esattamente la cosa esatta
			int contatore_prov=0;
			for (int i = 0;i<nomi.length;i++)
			{
			    if (nomi[i] == "Stefano"){contatore_prov++;}
			}

			for (int i = 0;i<nomi.length;i++)
			{
			    if (nomi[i] == "Salvo"){contatore_prov++;}
			}

			for (int i = 0;i<nomi.length;i++)
			{
			    if (nomi[i] == "Francesco"){contatore_prov++;}
			}

			if (contatore_prov == 3)
			{
				if (achivalue[5] != 1) //achievement - 5 - i nomi degli sfidanti sono stefano, salvo e francesco
				{
					achivalue= GameAchievements.updateAchievement(achivalue, 5, utente);
				}
			}
			
			if (options[2] == 3)
			{
				if (achivalue[6] != 10) //achievement - 6 - vinci 10 partite in modalita' difficile
				{
					achivalue= GameAchievements.updateAchievement(achivalue, 6, utente);
				}
			}

			
			if (cont_round<=3)
			{
				if (achivalue[9] != 10) //achievement - 9 - ha finito 10 volte una partita in meno di 3 match
				{
					achivalue= GameAchievements.updateAchievement(achivalue,9,utente);
				}
			}
			  //possibile richiamare da altro metodo
			records[1]++;
			if(records[2]<score[0])
				records[2]=score[0];
			if (records[3]>score[0])
				records[3]=score[0];
		}
		else
		{
			if (achivalue[4] != 50) //possibile richiamare da altro metodo
			{
				achivalue= GameAchievements.updateAchievement(achivalue,4,utente);
			}
			records[0]++;

			GameUtil.writeRecords(records,utente);
		}
	}

	// crea un MouseAdapter personalizzato (un MouseAdapter permette di gestire tutti gli input del mouse come click, rotella etc.)
	private class MyMouseAdapter extends MouseAdapter
	{
		private JLabel dragLabel = null; //variabile che indichera'il label selezionato
		private int dragLabelWidthDiv2;
		private int dragLabelHeightDiv2;
		private JPanel source = null; //variabile che indichera' il pannello cliccato
		private String path=""; //conterra' il path dell'immagine della carta
		private Tavolo t;
		private JWindow subw;

		public MyMouseAdapter(Tavolo t,JWindow subw)
		{
			this.t=t;
			this.subw=subw;

		}

		public void mousePressed(MouseEvent me)
		{
			Point click=me.getPoint();
			if(backingPanel.getComponentAt(click) instanceof JPanel)
				source = (JPanel) backingPanel.getComponentAt(click);
			else
				return;
			Component[] components2 = source.getComponents();
			JComponent[] components=new JComponent[components2.length];
			for(int i=0; i<components2.length; i++)
				components[i]= (JComponent)components2[i];

			//controlla se il pannello cliccato (se valido) contiene dei componenti
			if (components.length == 0||source==pNorth||source==pEast||source==pWest||source==pDiscard)
				return;

			// il problema e' qui : guardera'  sempre la prima.

			int l=0;
			if(source==pSouth)
			{
				boolean flag=true;
				for( ; l<components.length&&flag; l++)
				{
					int startPanel=((JLabel)components[l]).getX(); //da dove inizia il componente
					int panelLength=((JLabel)components[l]).getX()+((JLabel)components[l]).getWidth(); // dove dovrebbe finire il componente
					int gap=M_X-(M_X-panelLength)/components.length; //la lunghezza del monitor-(lung_monitor-(coord_label+la sua ampiezza))/numcomponenti
					int realX=Math.abs((int)click.getX()-(M_X/2-P_Y/2)); //toglie la distanza alle coordinate in x

					int startPanel2=((JLabel)components[l]).getY(); //da dove inizia il componente (asse y)
					int panelLength2=((JLabel)components[l]).getY()+((JLabel)components[l]).getHeight(); // dove dovrebbe finire il componente (asse y)
					int gap2=M_Y-((M_Y-panelLength2)/components.length); //la larghezza del monitor-(larg_monitor-(larg_label+la sua larghezza))/2
					int realY=Math.abs((int)click.getY()-gap2); //toglie la distanza alle coordinate in y

					//condizione per vedere dov'e' il cursore
					if((realX>=((JLabel)components[l]).getX())&&(realX<=(((JLabel)components[l]).getX()+((JLabel)components[l]).getWidth())))
					{
							dragLabel = (JLabel) components[l];
							flag=false;
					}

				}
				//se esce dal for senza aver preso niente
				if (dragLabel==null)
					dragLabel = (JLabel) components[0];
			}
			else if(source==pDeck)
					if(pescare)
						dragLabel = (JLabel) components[0];

			if (dragLabel!=null)
				source.remove(dragLabel);
			else
				return;
			source.revalidate();
			source.repaint();

			dragLabelWidthDiv2 = dragLabel.getWidth() / 2;
			dragLabelHeightDiv2 = dragLabel.getHeight() / 2;

			//imposta le coordinate in modo che il puntatore compaia esattamente al centro del label (durante il click)
			int x = me.getPoint().x - dragLabelWidthDiv2;
			int y = me.getPoint().y - dragLabelHeightDiv2;
			dragLabel.setLocation(x, y);

			//imposta la label nel livello "drag" cio' mette la label in primo piano
			add(dragLabel, JLayeredPane.DRAG_LAYER);
			repaint();
		}

		public void mouseDragged(MouseEvent me)
		{
			//se non e' stato selezionato alcun label esci dall'evento
			if (dragLabel == null)
				return;
			//altrimenti continua a impostare le coordinate in modo da far comparire il label al centro del puntatore
			int x = me.getPoint().x - dragLabelWidthDiv2;
			int y = me.getPoint().y - dragLabelHeightDiv2;
			dragLabel.setLocation(x, y);
			repaint();
		}

		public void mouseReleased(MouseEvent me)
		{
			//esce se non stiamo trascinando alcun label
			if (dragLabel == null)
				return;

			//altrimenti rimuovi il label dal livello "drag"
			remove(dragLabel);
			//e calcola il pannello in cui dovra' essere posizionato
			Point point=me.getPoint();
			Component destination = backingPanel.getComponentAt(point);

			if(destination!=null&&!(destination instanceof JLabel)&&!(destination instanceof JButton))
			{
				//se si tenta di scartare la carta che si stava pescando
				if(source==pDeck&&(JPanel)destination==pDiscard)
				{
					source.add(dragLabel);
					source.revalidate();
					repaint();
					dragLabel = null;
					return;
				}

				//se si pesca
				if(source==pDeck&&(JPanel)destination==pSouth)
				{
					if(turnoutente)
					{
						if ((t.getUltimaCarta().getNumero()==14||t.getUltimaCarta().getNumero()==10)&&cont!=0)
						{
							if(JOptionPane.showConfirmDialog(null,"Sei sicuro di non poter fare nient'altro?","pescata imminente",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION)
							{
								records[10]++;
								pescaDue(t,(byte)0,cont);
								cont=0;
								source.add(dragLabel);
								source.revalidate();
								dragLabel=null;
								return;
							}
							else
							{
								source.add(dragLabel);
								source.revalidate();
								dragLabel=null;
								return;
							}
						}
						if(pescare)
						{
							if (!t.isOver())
							{
								t.getPlayer(0).pesca(t.pescata());
								updatePanels(t,0);
							}
							else
							{
								JOptionPane.showMessageDialog(null,"Attendi che il mazzo sia inserito nuovamente!");
								GameUtil.gestioneFineMazzo(t);
								t.getPlayer(0).pesca(t.pescata());
								updatePanels(t,0);
							}
							pescare=false;
						}
						else
						{
							JOptionPane.showMessageDialog(null,"Spiacente, ma puoi pescare una sola volta");
							source.add(dragLabel);
							source.revalidate();
						}
						destination.revalidate();
					}
					else
					{
						JOptionPane.showMessageDialog(null,"Non e' il tuo turno! attendi!");
						source.add(dragLabel);
						source.revalidate();
						dragLabel=null;
						return;
					}
				}

				if(source==pDeck)
				{
						source.add(dragLabel);
						source.revalidate();
					repaint();
					dragLabel = null;
					return;
				}

				//se si scarta
				if(source==pSouth&&(JPanel)destination==pDiscard)
				{
					if(turnoutente)
					{
						Carta card;
						card =t.getPlayer(0).getMano().getHead();
						for (; card!=null; card=card.getNext())
						{
							if (card.lab.equals(dragLabel))
							{
								System.out.println(cont);
								if ((t.getUltimaCarta().getNumero()==14||t.getUltimaCarta().getNumero()==10)&&cont!=0)
								{
									if (card.getNumero()!=14&&card.getNumero()!=10)
									{
										if(JOptionPane.showConfirmDialog(null,"Sei sicuro di non poter buttare nient'altro?","pescata imminente",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION)
										{
											records[10]++;
											pescaDue(t,(byte)0,cont);
											cont=0;
											source.add(dragLabel);
											source.revalidate();
											repaint();
											dragLabel = null;
											return;
										}
										else
										{
											source.add(dragLabel);
											source.revalidate();
											repaint();
											dragLabel = null;
											return;
										}
									}
									else
									{
										if(card.getNumero()==10&&card.getColore()==t.getColore()[1]&&t.getUltimaCarta().getNumero()==14)
										{
											pDiscard.removeAll();

											t.scartata(t.getPlayer(0).scarta(card));
											pDiscard.setImage(card.getImg()[0]);
											updatePanels(t,0);
											t.setCol(card.getColore());
											//gestione dell'aggiornamento del colore attuale
											coloraColor(t);
											destination.revalidate();
											repaint();
											dragLabel = null;
											records[9]++;
											salta=true;
											cont+=2;
											return;
										}
										if(card.getNumero()==10&&t.getUltimaCarta().getNumero()==10)
										{
											pDiscard.removeAll();

											t.scartata(t.getPlayer(0).scarta(card));
											pDiscard.setImage(card.getImg()[0]);
											updatePanels(t,0);

											t.setCol(card.getColore());
											//gestione dell'aggiornamento del colore attuale
											coloraColor(t);
											destination.revalidate();
											repaint();
											dragLabel = null;
											records[9]++;
											salta=true;
											cont++;
											return;
										}
										if(card.getNumero()==14)
										{
											pDiscard.removeAll();

											t.scartata(t.getPlayer(0).scarta(card));
											pDiscard.setImage(card.getImg()[0]);
											updatePanels(t,0);
											String x;
											do
											{
												x=JOptionPane.showInputDialog("Scegli il colore desiderato tra: \n verde \n rosso \n giallo \n blu");
												System.out.println(x);
												if(x.charAt(0)!='v'&&x.charAt(0)!='V'&&x.charAt(0)!='r'&&x.charAt(0)!='R'&&x.charAt(0)!='g'&&x.charAt(0)!='G'&&x.charAt(0)!='b'&&x.charAt(0)!='B')
													JOptionPane.showMessageDialog(null, "Riprova!");
											} while(x.charAt(0)!='v'&&x.charAt(0)!='V'&&x.charAt(0)!='r'&&x.charAt(0)!='R'&&x.charAt(0)!='g'&&x.charAt(0)!='G'&&x.charAt(0)!='b'&&x.charAt(0)!='B'||x.equals(""));


											//gestione dell'aggiornamento del colore attuale
											colore=x.charAt(0);
											t.setCol(colore);
											coloraColor(t);
											colore='n';
											records[13]++;
											Carta cartina =t.getPlayer(0).getMano().getHead();
											byte c=0;
											for (;cartina!=null; cartina=cartina.getNext())
												if( cartina.getColore()==t.getColore()[0])
													c++;
											if (c!=0)
												records[12]++;
											destination.revalidate();
											repaint();
											dragLabel = null;
											salta=true;
											cont+=2;
											return;
										}
									}
								}
								else
								{
									if(card.getNumero()==13)
									{
										pDiscard.removeAll();

										t.scartata(t.getPlayer(0).scarta(card));
										pDiscard.setImage(card.getImg()[0]);
										updatePanels(t,0);

										String x;
										do
										{
											x=JOptionPane.showInputDialog("Scegli il colore desiderato tra: \n verde \n rosso \n giallo \n blu");
											System.out.println(x);
											if(x.charAt(0)!='v'&&x.charAt(0)!='V'&&x.charAt(0)!='r'&&x.charAt(0)!='R'&&x.charAt(0)!='g'&&x.charAt(0)!='G'&&x.charAt(0)!='b'&&x.charAt(0)!='B')
												JOptionPane.showMessageDialog(null, "Riprova!");
										} while(x.charAt(0)!='v'&&x.charAt(0)!='V'&&x.charAt(0)!='r'&&x.charAt(0)!='R'&&x.charAt(0)!='g'&&x.charAt(0)!='G'&&x.charAt(0)!='b'&&x.charAt(0)!='B'||x.equals(""));
										//gestione dell'aggiornamento del colore attuale
										colore=x.charAt(0);
										t.setCol(colore);
										coloraColor(t);
										colore='n';
										records[11]++;
										destination.revalidate();
										repaint();
										dragLabel = null;
										salta=true;
										dragLabel=null;
										return;
									}

									if(card.getNumero()==14)
									{
										t.scartata(t.getPlayer(0).scarta(card));
										pDiscard.setImage(card.getImg()[0]);
										updatePanels(t,0);

										//pulsante che fa scegliere il colore

										String x;
										do
										{
											x=JOptionPane.showInputDialog("Scegli il colore desiderato tra: \n verde \n rosso \n giallo \n blu");
											System.out.println(x);
											if(x.charAt(0)!='v'&&x.charAt(0)!='V'&&x.charAt(0)!='r'&&x.charAt(0)!='R'&&x.charAt(0)!='g'&&x.charAt(0)!='G'&&x.charAt(0)!='b'&&x.charAt(0)!='B')
												JOptionPane.showMessageDialog(null, "Riprova!");
										} while(x.charAt(0)!='v'&&x.charAt(0)!='V'&&x.charAt(0)!='r'&&x.charAt(0)!='R'&&x.charAt(0)!='g'&&x.charAt(0)!='G'&&x.charAt(0)!='b'&&x.charAt(0)!='B'||x.equals(""));
										//gestione dell'aggiornamento del colore attuale
										colore=x.charAt(0);
										t.setCol(colore);
										coloraColor(t);
										colore='n';

										destination.revalidate();
										repaint();
											dragLabel=null;
										salta=true;
										records[13]++;
										Carta cartina =t.getPlayer(0).getMano().getHead();
										byte c=0;
										for (;cartina!=null; cartina=cartina.getNext())
											if( cartina.getColore()==t.getColore()[0])
												c++;
										if (c!=0)
											records[12]++;
										cont+=2;
										return;
										//gestione del p4
									}

									if( card.getColore()==t.getColore()[1]||card.getNumero()==t.getUltimaCarta().getNumero())
									{
										pDiscard.removeAll();


										if(card.getNumero()==10||card.getNumero()==12)
										{
											if (card.getNumero()==10)
												records[9]++;
											else
											{
												records[20]++;
												if (t.getUltimaCarta().getNumero()==12)
													records[22]++;
											}
											cont++;
										}
										if(card.getNumero()==11)
										{
											if (t.getUltimaCarta().getNumero()==11)
												records[16]++;
											ImageIcon img;
											t.setGiro(!t.getGiro());
											if(t.getGiro())
											{
												img= new ImageIcon(System.getProperty("user.dir") + "\\img" +"\\"+"right.PNG");
												img.setImage(img.getImage().getScaledInstance(50,50, Image.SCALE_DEFAULT));
												lap.setIcon(img);
											}
											else
											{
												img= new ImageIcon(System.getProperty("user.dir") + "\\img" +"\\"+"left.PNG");
												img.setImage(img.getImage().getScaledInstance(50,50, Image.SCALE_DEFAULT));
												lap.setIcon(img);
											}
											records[15]++;

										}
										if(card.getColore()==t.getColore()[1])
											records[24]++;
										else
											records[23]++;
										t.scartata(t.getPlayer(0).scarta(card));
										pDiscard.setImage(card.getImg()[0]);
										updatePanels(t,0);

										t.setCol(card.getColore());
										//gestione dell'aggiornamento del colore attuale
										coloraColor(t);
										source.revalidate();
										destination.revalidate();
										repaint();
										dragLabel = null;

										salta=true;

										return;
									}

								}
							}
						}
						//se la carta non viene trovata nel for torna dov'era
						source.add(dragLabel);
						source.revalidate();
						repaint();
						dragLabel=null;
						System.out.println(cont);
						return;
					}
					else
					{
						JOptionPane.showMessageDialog(null,"Non e' il tuo turno! attendi!");
						source.add(dragLabel);
						source.revalidate();
						dragLabel=null;
						return;
					}
				}
				if(source==(JPanel)pSouth)
				{
					source.add(dragLabel);
					source.revalidate();
				}
			}
			else
			{
				source.add(dragLabel);
				source.revalidate();
			}
			//salva le modifiche
			repaint();
			dragLabel = null;
			path="";
			return;
		}
	}

	// gestione della mossa del bot (tutto cio' che il bot fa e' qui) - da controllare e smembrare
	public byte botMove(Tavolo t ,byte indice, byte contatore, int [] opt)
	{

		if((opt[2]<3)&&(Math.random()*100>91)&&t.getPlayer(indice).getNumCarte()>15)
		{
			wft(t,indice,opt[1]);
			records[25]++;
		}
		boolean flag=true;

		//gestione uno
		int p;
		if (t.getGiro()) //se il giro e' orario
		{
			if ((indice -1)>=0) //se e' un giocatore (quindi tra un numero tra 0 a 3)
				p=indice-1;
			else
				p=opt[1];

			if (t.getPlayer(p).getNumCarte()==1&&!t.getPlayer(p).getUno())
			{
				//Sembra fattibile fare un metodo esterno qui
				if ( opt[2]==1&&((Math.random()*10+1)>6))
				{
					//gestione negligenza
					if (p==0)
					{
						records[17]++;
						JOptionPane.showMessageDialog(null,"Sei stato negligente!");
					}
					pescaDue(t, (byte) p,(byte) 1);
				}
				if ( opt[2]==2&&((Math.random()*10+1)>5))
				{
					//gestione negligenza
					if (p==0)
					{
						records[17]++;
						JOptionPane.showMessageDialog(null,"Sei stato negligente!");
					}
					pescaDue(t, (byte) p, (byte)1);
				}
				if (opt[2]==3)
				{
					//gestione negligenza
					if (p==0)
					{
						records[17]++;
						JOptionPane.showMessageDialog(null,"Sei stato negligente!");
					}
					pescaDue(t, (byte) p, (byte)1);
				}
			}
		}
		else
		{
			if((indice+1)>opt[1])
				p=0;
			else
				p=indice+1;

			if (t.getPlayer(p).getNumCarte()==1&&!t.getPlayer(p).getUno())
			{
				//Sembra fattibile fare un metodo esterno qui
				if ( opt[2]==1&&((Math.random()*10+1)>4))
				{
					//gestione negligenza
					if (p==0)
					{
						records[17]++;
						JOptionPane.showMessageDialog(null,"Sei stato negligente!");
					}
					pescaDue(t, (byte) p, (byte)1);
				}
				if ( opt[2]==2&&((Math.random()*10+1)>5))
				{
					//gestione negligenza
					if (p==0)
					{
						records[17]++;
						JOptionPane.showMessageDialog(null,"Sei stato negligente!");
					}
					pescaDue(t, (byte) p, (byte)1);
				}
				if (opt[2]==3)
				{
					//gestione negligenza
					if (p==0)
					{
						records[17]++;
						JOptionPane.showMessageDialog(null,"Sei stato negligente!");
					}
					pescaDue(t, (byte) p, (byte)1);
				}
			}
		}

		//caso in cui la carta sia stop
		if(( (t.getUltimaCarta().getNumero()==(byte)12))&& (contatore!=0))
		{
			return 0;
		}

		//caso in cui la carta sia pesca due
		if(t.getUltimaCarta().getNumero()==10&&contatore!=0)
		{
			Carta card= t.getPlayer(indice).getMano().getHead();
			for ( ; card != null; card=card.getNext() )
			{
				if (card.getNumero()==10||card.getNumero()==14)
				{

					if (card.getNumero()==14)
					{
						if (opt[2]!=1)
						{
							byte c=0;
							Carta card2= t.getPlayer(indice).getMano().getHead();
							for ( ; card2.getNext() != null ; card2=card2.getNext() )
								if (card2.getColore()==t.getColore()[1])
									c++;
							if (c==0)
							{
								t.scartata(t.getPlayer(indice).scarta(card));
								contatore+=(byte)2;
								t.setCol(GameUtil.gestioneColori());
								gestisciScarto(t,indice,card);
								GameUtil.gestioneUno(t, indice, opt[2]);
								return contatore;
							}
							else if((opt[2]==2)&& ((Math.random()*10)+1)>4)
							{
								t.scartata(t.getPlayer(indice).scarta(card));
								contatore+=(byte)2;
								t.setCol(GameUtil.gestioneColori());
								gestisciScarto(t,indice,card);
								GameUtil.gestioneUno(t, indice, opt[2]);
								return contatore;
							}
						}
						else
						{
							t.scartata(t.getPlayer(indice).scarta(card));
							contatore+=(byte)2;
							t.setCol(GameUtil.gestioneColori());
							gestisciScarto(t,indice,card);
							GameUtil.gestioneUno(t, indice, opt[2]);
							return contatore;
						}
					}
					else
					{
						t.scartata(t.getPlayer(indice).scarta(card));
						t.setCol(card.getColore());
						gestisciScarto(t,indice,card);
						contatore++;
						GameUtil.gestioneUno(t, indice, opt[2]);
						return contatore;
					}
				}
			}
			//se si esce dal for significa che non ci sono possibilita'  e bisogna pescare
			pescaDue(t, indice, contatore);

		}

		//caso in cui ci sia sul tavolo almeno un +4
		if(t.getUltimaCarta().getNumero()==14&& contatore!=0)
		{

			byte c=0;

			//gestione "scopri bluff"
			contatore= gestioneBluffP4(t, indice, contatore, opt);

			if (contatore!=0)
			{
				Carta card= t.getPlayer(indice).getMano().getHead();
				for ( ; card != null && flag ; card=card.getNext() )
					if (card.getNumero()==14)
						flag=!flag;
				if (!flag)
				{
					if( opt[2]!=1)
					{
						Carta card2= t.getPlayer(indice).getMano().getHead();
						for ( ; card2.getNext() != null ; card2=card2.getNext() )
							if (card2.getColore()==t.getColore()[1])
								c++;
						if (c==0)
						{
							card= t.getPlayer(indice).getMano().getHead();
							for ( ; card != null && flag ; card=card.getNext() )
								if (card.getNumero()==14)
								{
									t.scartata(t.getPlayer(indice).scarta(card));
									contatore+=(byte)2;
									t.setCol(GameUtil.gestioneColori());
									gestisciScarto(t,indice,card);
									GameUtil.gestioneUno(t, indice, opt[2]);
									return contatore;

								}
						}
						else
							if((opt[2]==2)&& ((Math.random()*10)+1)>6)
							{
								card= t.getPlayer(indice).getMano().getHead();
								for ( ; card != null && flag ; card=card.getNext() )
									if (card.getNumero()==14)
									{
										t.scartata(t.getPlayer(indice).scarta(card));
										contatore+=(byte)2;
										t.setCol(GameUtil.gestioneColori());
										gestisciScarto(t,indice,card);
										GameUtil.gestioneUno(t, indice, opt[2]);
										return contatore;
									}
							}
							else
							{
								pescaDue(t, indice, contatore);
								contatore=0;
								this.cont=0;
							}
					}
					else
					{
						card= t.getPlayer(indice).getMano().getHead();
						for ( ; card != null && flag ; card=card.getNext() )
							if (card.getNumero()==14)
							{
								t.scartata(t.getPlayer(indice).scarta(card));
								contatore+=(byte)2;
								t.setCol(GameUtil.gestioneColori());
								gestisciScarto(t,indice,card);
								GameUtil.gestioneUno(t, indice, opt[2]);
								return contatore;

							}
					}
				}
				else
				{
					card= t.getPlayer(indice).getMano().getHead();
					for ( ; card != null && flag ; card=card.getNext() )
					if (card.getNumero()==10&&t.getColore()[1]==card.getColore())
					{
						t.scartata(t.getPlayer(indice).scarta(card));
						gestisciScarto(t,indice,card);
						contatore++;
						GameUtil.gestioneUno(t, indice, opt[2]);
						return contatore;
					}
					else
					{
						pescaDue(t, indice, contatore);
						contatore=0;
						this.cont=0;
					}
				}
			}
		}

		// Sarebbe meglio fare un metodo a parte che viene richiamato in base alla difficolta'
		switch (opt[2])
		{
			case 1: //difficolta'  facile
			{
				//percentuale di negligenza Uno! : 60% gestire
				//percentuale bluff 100%
				//controllo mano rispetto alla carta scartata
				byte x;
				if(( x= scartaColore(t,indice,contatore, opt[2]))!=-1)
					return x;
				if(( x= scartaNumero(t,indice,contatore, opt[2]))!=-1)
					return x;

				//se non ci sono possibilita' pesco e controllo sulla carta
				if (x==-1)
				{
					Carta card= t.getPlayer(indice).getMano().getHead();
					for ( ; card != null ; card=card.getNext() )
					{
						if (card.getNumero()==13||card.getNumero()==14)
						{
							if( card.getNumero()==13)
							{
								t.scartata(t.getPlayer(indice).scarta(card));
								t.setCol(GameUtil.gestioneColori());
								gestisciScarto(t,indice,card);
								GameUtil.gestioneUno(t, indice, opt[2]);
								return 0;
							}

							if( card.getNumero()==14)
							{
								t.scartata(t.getPlayer(indice).scarta(card));
								t.setCol(GameUtil.gestioneColori());
								gestisciScarto(t,indice,card);
								contatore+= (byte)2;
								GameUtil.gestioneUno(t, indice, opt[2]);
								return contatore;
							}
						}
					}
				}
				if ((x=controlloPescata(t, indice, contatore, opt[2]))==-1)
				//se non posso buttare la carta pescata non faccio niente
				{
				// passo il turno
				//ulteriori guai per Uno
				}
				else
					return x;

				return 0;
			}

			case 2:
			{
				//diff media/normale

				//percentuale di bluff e'  40%
				//percentuale di negligenza Uno! : 50%



				// 1) controllo cambia colore
				byte numcc=0;
				Carta card= t.getPlayer(indice).getMano().getHead();
				for ( ; card != null ; card=card.getNext() )
					if (card.getNumero()==13)
						numcc++;

				//2) controllo per colore
				byte numcol=0;
				card= t.getPlayer(indice).getMano().getHead();
				for ( ; card != null ; card=card.getNext() )
					if (card.getColore()==t.getColore()[1])
						numcol++;


				//3) controllo se posso pesca quattro
				byte nump4=0;
				card= t.getPlayer(indice).getMano().getHead();
				for ( ; card != null ; card=card.getNext() )
					if (card.getNumero()==14)
						nump4++;

				//4) controllo per numero
				byte numnum=0;
				card= t.getPlayer(indice).getMano().getHead();
				for ( ; card != null ; card=card.getNext() )
					if (card.getNumero()==t.getUltimaCarta().getNumero())
						numnum++;

				// se ogni avversario ha + di 5 carte utilizzo prima 2 o 4 e dopo 1 o 3 (se possibile)
				// altrimenti un avversario ha meno di 5 carte utilizzo prima 1 o 3 (se possibile) e dopo 2 o 4

				flag= false;
				int c=0;
				for (int gioc=0; gioc<opt[1]; gioc++)
				{
					if(gioc==indice)
						gioc++;
					if(t.getPlayer(gioc).getNumCarte()>5)
						flag=true;
				}
				if (flag)
				{
					if (numcol!=0&&numcol>2)
						return scartaColore(t, indice, contatore, opt[2]);

					if (numnum!=0)
						return scartaNumero(t, indice, contatore, opt[2]);

					if (numcc!=0)
					{
						card= t.getPlayer(indice).getMano().getHead();
						for ( ; card != null ; card=card.getNext() )
						{
							if( card.getNumero()==13)
							{
								t.scartata(t.getPlayer(indice).scarta(card));
								t.setCol(GameUtil.gestioneColori());
								gestisciScarto(t,indice,card);
								GameUtil.gestioneUno(t, indice, opt[2]);
								return 0;
							}

						}
					}

					if (nump4!=0)
					{
						Carta card2= t.getPlayer(indice).getMano().getHead();
						for ( ; card2.getNext() != null ; card2=card2.getNext() )
							if (card2.getColore()==t.getColore()[1])
								c++;
							if (c==0)
							{
								card= t.getPlayer(indice).getMano().getHead();
								for ( ; card != null ; card=card.getNext() )
									if (card.getNumero()==14)
									{
										t.scartata(t.getPlayer(indice).scarta(card));
										contatore+=(byte)2;
										t.setCol(GameUtil.gestioneColori());
										gestisciScarto(t,indice,card);
										GameUtil.gestioneUno(t, indice, opt[2]);
										return contatore;
									}

							}
							else if(((Math.random()*10)+1)>6)
							{
								card= t.getPlayer(indice).getMano().getHead();
								for ( ; card != null ; card=card.getNext() )
									if (card.getNumero()==14)
									{
										t.scartata(t.getPlayer(indice).scarta(card));
										contatore+=(byte)2;
										t.setCol(GameUtil.gestioneColori());
										gestisciScarto(t,indice,card);
										GameUtil.gestioneUno(t, indice, opt[2]);
										return contatore;
									}
							}
					}

				}
				else
				{
					if (nump4!=0)
					{
						Carta card2= t.getPlayer(indice).getMano().getHead();
						for ( ; card2.getNext() != null ; card2=card2.getNext() )
							if (card2.getColore()==t.getColore()[1])
									c++;
						if (c==0)
						{
							card= t.getPlayer(indice).getMano().getHead();
							for ( ; card != null ; card=card.getNext() )
								if (card.getNumero()==14)
								{
									t.scartata(t.getPlayer(indice).scarta(card));
									contatore+=(byte)2;
									t.setCol(GameUtil.gestioneColori());
									gestisciScarto(t,indice,card);
									GameUtil.gestioneUno(t, indice, opt[2]);
									return contatore;

								}
						}
						else if(((Math.random()*10)+1)>6)
						{
							card= t.getPlayer(indice).getMano().getHead();
							for ( ; card != null ; card=card.getNext() )
								if (card.getNumero()==14)
								{
									t.scartata(t.getPlayer(indice).scarta(card));
									contatore+=(byte)2;
									t.setCol(GameUtil.gestioneColori());
									gestisciScarto(t,indice,card);
									GameUtil.gestioneUno(t, indice, opt[2]);
									return contatore;
								}


						}
					}

					if (numcc!=0)
					{
						card= t.getPlayer(indice).getMano().getHead();
						for ( ; card != null ; card=card.getNext() )
						{
							if( card.getNumero()==13)
							{
								t.scartata(t.getPlayer(indice).scarta(card));
								t.setCol(GameUtil.gestioneColori());
								gestisciScarto(t,indice,card);
								GameUtil.gestioneUno(t, indice, opt[2]);
								return 0;
							}

						}
					}

					if (numcol!=0)
					{
						return scartaColore(t,indice,contatore, opt[2]);
					}

					if (numnum!=0)
					{
						return scartaNumero(t,indice,contatore, opt[2]);
					}

				}
					//5) pesco e controllo della carta pescata normalmente come in precedenza

				byte x;
				if ((x=controlloPescata(t, indice, contatore, opt[2]))==-1)
					return 0;
				else
					return x;
			}

			case 3:
			{
				//difficolta' difficile

				//probabilita negligenza: 0%
				//probabilita'  bluff 10%

				// 1) controllo cambia colore
				byte numcc=0;
				Carta card= t.getPlayer(indice).getMano().getHead();
				for ( ; card != null ; card=card.getNext() )
					if (card.getNumero()==13)
						numcc++;

				//2) controllo per colore
				byte numcol=0;
				card= t.getPlayer(indice).getMano().getHead();
				for ( ; card != null ; card=card.getNext() )
					if (card.getColore()==t.getColore()[1])
						numcol++;


				//3) controllo se posso pesca quattro
				byte nump4=0;
				card= t.getPlayer(indice).getMano().getHead();
				for ( ; card != null ; card=card.getNext() )
					if (card.getNumero()==14)
						nump4++;

				//4) controllo per numero
				byte numnum=0;
				card= t.getPlayer(indice).getMano().getHead();
				for ( ; card != null ; card=card.getNext() )
					if (card.getNumero()==t.getUltimaCarta().getNumero())
						numnum++;

				// se ogni avversario ha + di 5 carte utilizzo prima 2 o 4 e dopo 1 o 3 (se possibile)
				// altrimenti un avversario ha meno di 5 carte utilizzo prima 1 o 3 (se possibile) e dopo 2 o 4

				flag= false;
				int c=0;
				for (int gioc=0; gioc<opt[1]; gioc++)
				{
					if(gioc==indice)
						gioc++;
					if(t.getPlayer(indice).getNumCarte()>5)
						flag=true;
				}
				if (flag)
				{
					if ((numcol!=0)&&numcol>numnum)
						return scartaColore(t, indice, contatore, opt[2]);
					else
						if (numnum!=0)
							return scartaNumero(t, indice, contatore, opt[2]);

					if (numcc!=0)
					{
						card= t.getPlayer(indice).getMano().getHead();
						for ( ; card != null ; card=card.getNext() )
						{
							if( card.getNumero()==13)
							{
								t.scartata(t.getPlayer(indice).scarta(card));
								t.setCol(GameUtil.gestioneColori());
								gestisciScarto(t,indice,card);
								GameUtil.gestioneUno(t, indice, opt[2]);
								return 0;
							}

						}
					}
					if (nump4!=0)
					{
						Carta card2= t.getPlayer(indice).getMano().getHead();
						for ( ; card2.getNext() != null ; card2=card2.getNext() )
						if (card2.getColore()==t.getColore()[1])
							c++;
						if (c==0)
						{
							card= t.getPlayer(indice).getMano().getHead();
							for ( ; card != null ; card=card.getNext() )
								if (card.getNumero()==14)
								{
									t.scartata(t.getPlayer(indice).scarta(card));
									contatore+=(byte)2;
									t.setCol(GameUtil.gestioneColori());
									gestisciScarto(t,indice,card);
									GameUtil.gestioneUno(t, indice, opt[2]);
									return contatore;
								}

						}
						// se tutti hanno + di 5 carte non ha senso rischiare
					}

				}
				else
				{
					if (nump4!=0)
					{
						Carta card2= t.getPlayer(indice).getMano().getHead();
						for ( ; card2.getNext() != null ; card2=card2.getNext() )
							if (card2.getColore()==t.getColore()[1])
								c++;
						if (c==0)
						{
							card= t.getPlayer(indice).getMano().getHead();
							for ( ; card != null ; card=card.getNext() )
								if (card.getNumero()==14)
								{
									t.scartata(t.getPlayer(indice).scarta(card));
									contatore+=(byte)2;
									t.setCol(GameUtil.gestioneColori());
									gestisciScarto(t,indice,card);
									GameUtil.gestioneUno(t, indice, opt[2]);
									return contatore;
								}

						}
						else if(((Math.random()*10)+1)>9)
						{
							card= t.getPlayer(indice).getMano().getHead();
							for ( ; card != null ; card=card.getNext() )
								if (card.getNumero()==14)
								{
									t.scartata(t.getPlayer(indice).scarta(card));
									contatore+=(byte)2;
									t.setCol(GameUtil.gestioneColori());
									gestisciScarto(t,indice,card);
									GameUtil.gestioneUno(t, indice, opt[2]);
									return contatore;
								}
						}

					}
						//controllo se ci sono pesca 2
					if (numcol!=0)
					{
						card= t.getPlayer(indice).getMano().getHead();
						for ( ; card != null && flag ; card=card.getNext() )
							if (card.getNumero()==10&&card.getColore()==t.getColore()[1])
							{
								t.scartata(t.getPlayer(indice).scarta(card));
								gestisciScarto(t,indice,card);
								contatore++;
								GameUtil.gestioneUno(t, indice, opt[2]);
								return contatore;
							}
					}

					if (numcc!=0)
					{
						card= t.getPlayer(indice).getMano().getHead();
						for ( ; card != null ; card=card.getNext() )
						{
							if( card.getNumero()==13)
							{
								t.scartata(t.getPlayer(indice).scarta(card));
								t.setCol(GameUtil.gestioneColori());
								gestisciScarto(t,indice,card);
								GameUtil.gestioneUno(t, indice, opt[2]);
								return 0;
							}
						}
					}

					if (numcol!=0&&numcol>numnum)
					{
						return scartaColore(t,indice,contatore, opt[2]);
					}
					else
						if (numnum!=0)
						{
							return scartaNumero(t,indice,contatore, opt[2]);
						}

				}

				//5) pesco e controllo della carta pescata normalmente come in precedenza

				byte x;
				if ((x=controlloPescata(t, indice, contatore, opt[2]))==-1)
					return 0;
				else
					return x;
			}
		}

		return -3;
	}
	// prende in input il tavolo di gioco, l'indice del giocatore, il contatore che dice quante carte bisogna pescare e le inserisce nel giocatore specificato
	public byte pescaDue(Tavolo t, byte indice,byte contatore)
	{
		Carta card;
		if(!t.isOver())
		{
			if(indice==0)
				JOptionPane.showMessageDialog(null,"Hai preso  "+contatore*2+ " carte!");
			contatore*=2;
			for (; contatore>0;contatore--)
				if(!t.isOver())
				{
					card=t.pescata();
					t.getPlayer(indice).pesca(card);
					updatePanels(t,indice);
				}
				else
				{
					//gestione fine mazzo da fare
					GameUtil.gestioneFineMazzo(t);
					return pescaDue(t,indice,contatore);
				}
			this.cont=0;
			return 0;
		}
		else
		{
			//gestione fine mazzo da fare
			GameUtil.gestioneFineMazzo(t);
			return pescaDue(t,indice,contatore);
		}

	}

	// il giocatore scarta una carta dalla sua mano in base al colore del tavolo
	public byte scartaColore(Tavolo t, byte indice, byte contatore, int diff)
	{
		Carta card= t.getPlayer(indice).getMano().getHead();

		for ( ; card != null ; card=card.getNext() )
		{
			if (card.getColore()==t.getColore()[1])
			{
				t.scartata(t.getPlayer(indice).scarta(card));
				t.setCol(card.getColore());
				gestisciScarto(t,indice,card);
				GameUtil.gestioneUno(t, indice, diff);
				if (card.getNumero()==10||card.getNumero()==11||card.getNumero()==12)
				{
					if (card.getNumero()==11)
					{
						ImageIcon img;
						t.setGiro(!t.getGiro());
						if(t.getGiro())
						{
							img= new ImageIcon(""+System.getProperty("user.dir") + "\\img" +"\\"+"right.PNG");
							img.setImage(img.getImage().getScaledInstance(50,50, Image.SCALE_DEFAULT));
							lap.setIcon(img);
						}
						else
						{
							img= new ImageIcon(""+System.getProperty("user.dir") + "\\img" +"\\"+"left.PNG");
							img.setImage(img.getImage().getScaledInstance(50,50, Image.SCALE_DEFAULT));
							lap.setIcon(img);
						}
					}
					else
						contatore++;

					return contatore;
				}
				else
				{
					return 0;
				}
			}
		}
		return -1;
	}
	// il giocatore scarta una carta dalla sua mano in base al numero dell'ultima carta che risiede mazzo "scarti"
	public byte scartaNumero(Tavolo t, byte indice, byte contatore, int diff)
	{
		Carta card= t.getPlayer(indice).getMano().getHead();

		for ( ; card != null ; card=card.getNext() )
		{
			if ((card.getNumero()<10)&&card.getNumero()==t.getUltimaCarta().getNumero())
			{
				t.scartata(t.getPlayer(indice).scarta(card));
				t.setCol(card.getColore());
				gestisciScarto(t,indice,card);
				GameUtil.gestioneUno(t, indice, diff);
				return 0;
			}
			if ((card.getNumero()==10||card.getNumero()==11||card.getNumero()==12)
				&&card.getNumero()==t.getUltimaCarta().getNumero())
			{
				t.scartata(t.getPlayer(indice).scarta(card));
				t.setCol(card.getColore());
				gestisciScarto(t,indice,card);
				if (card.getNumero()==11)
				{
					ImageIcon img;
					t.setGiro(!t.getGiro());
					if(t.getGiro())
					{
						img= new ImageIcon(""+System.getProperty("user.dir") + "\\img" +"\\"+"right.PNG");
						img.setImage(img.getImage().getScaledInstance(50,50, Image.SCALE_DEFAULT));
						lap.setIcon(img);
					}
					else
					{
						img= new ImageIcon(""+System.getProperty("user.dir") + "\\img" +"\\"+"left.PNG");
						img.setImage(img.getImage().getScaledInstance(50,50, Image.SCALE_DEFAULT));
						lap.setIcon(img);
					}
				}
				else
					contatore++;
				GameUtil.gestioneUno(t, indice, diff);
				return contatore;
			}
			if (card.getNumero()==13||card.getNumero()==14)
				if( card.getNumero()==13)
				{
					t.scartata(t.getPlayer(indice).scarta(card));
					t.setCol(GameUtil.gestioneColori());
					gestisciScarto(t,indice,card);
					GameUtil.gestioneUno(t, indice, diff);
					return 0;
				}
				else
				{
					t.scartata(t.getPlayer(indice).scarta(card));
					t.setCol(GameUtil.gestioneColori());
					gestisciScarto(t,indice,card);
					contatore+= (byte)2;
					GameUtil.gestioneUno(t, indice, diff);
					return contatore;
				}

		}
		return -1;
	}

	//il giocatore pesca e controlla se puo' buttare la carta pescata
	public byte controlloPescata(Tavolo t, byte indice, byte contatore, int diff)
	{
		Carta card;
		if (!t.isOver())
		{
			card=t.pescata();
			t.getPlayer(indice).pesca(card);
			updatePanels(t,indice);

			if ( card.getColore()==t.getColore()[1])
			{
				if (card.getNumero()==10||card.getNumero()==11||card.getNumero()==12)
				{
					if (card.getNumero()==11)
					{
						ImageIcon img;
						t.setGiro(!t.getGiro());
						if(t.getGiro())
						{
							img= new ImageIcon(""+System.getProperty("user.dir") + "\\img" +"\\"+"right.PNG");
							img.setImage(img.getImage().getScaledInstance(50,50, Image.SCALE_DEFAULT));
							lap.setIcon(img);
						}
						else
						{
							img= new ImageIcon(""+System.getProperty("user.dir") + "\\img" +"\\"+"left.PNG");
							img.setImage(img.getImage().getScaledInstance(50,50, Image.SCALE_DEFAULT));
							lap.setIcon(img);
						}
					}
					else
						contatore++;
					t.scartata(t.getPlayer(indice).scarta(card));
					gestisciScarto(t,indice,card);
					GameUtil.gestioneUno(t, indice, diff);

					return contatore;
				}
				else
				{
					t.scartata(t.getPlayer(indice).scarta(card));
					gestisciScarto(t,indice,card);
					GameUtil.gestioneUno(t, indice, diff);
					return 0;
				}
			}
			else
			{
				if ((card.getNumero()<10)&& card.getNumero()==t.getUltimaCarta().getNumero())
				{
					t.scartata(t.getPlayer(indice).scarta(card));
					gestisciScarto(t,indice,card);
					GameUtil.gestioneUno(t, indice, diff);
					return 0;
				}
				if (card.getNumero()==10||card.getNumero()==11||card.getNumero()==12&&
					card.getNumero()==t.getUltimaCarta().getNumero())
				{
					t.scartata(t.getPlayer(indice).scarta(card));
					t.setCol(card.getColore());
					gestisciScarto(t,indice,card);
					GameUtil.gestioneUno(t, indice, diff);
					if (card.getNumero()==11)
					{
						ImageIcon img;
						t.setGiro(!t.getGiro());
						if(t.getGiro())
						{
							img= new ImageIcon(""+System.getProperty("user.dir") + "\\img" +"\\"+"right.PNG");
							img.setImage(img.getImage().getScaledInstance(50,50, Image.SCALE_DEFAULT));
							lap.setIcon(img);
						}
						else
						{
							img= new ImageIcon(""+System.getProperty("user.dir") + "\\img" +"\\"+"left.PNG");
							img.setImage(img.getImage().getScaledInstance(50,50, Image.SCALE_DEFAULT));
							lap.setIcon(img);
						}
					}
					else
						contatore++;
					return contatore;
				}
				if (card.getNumero()==13||card.getNumero()==14)
					if( card.getNumero()==13)
					{
						t.scartata(t.getPlayer(indice).scarta(card));
						t.setCol(GameUtil.gestioneColori());
						gestisciScarto(t,indice,card);
						GameUtil.gestioneUno(t, indice, diff);
						return 0;
					}
					else
					{

						t.scartata(t.getPlayer(indice).scarta(card));
						t.setCol(GameUtil.gestioneColori());
						gestisciScarto(t,indice,card);
						contatore+= (byte)2;
						GameUtil.gestioneUno(t, indice, diff);
						return contatore;
					}

			}
			return -1;
		}
		else
		{
			GameUtil.gestioneFineMazzo(t);
			return controlloPescata(t, indice, contatore, diff);
		}
	}
	//gestisce lo scarto della carta rispetto al mazziere (vale a dire la prima carta scartata)
	public void gestisciScarto(Tavolo t)
	{
		pDiscard.removeAll();
		pDiscard.setImage(t.getUltimaCarta().getImg()[0]);
		//gestione dell'aggiornamento del colore attuale
		coloraColor(t);
		repaint();
		revalidate();
	}
	//gestisce lo scarto della carta rispetto al giocatore (vale a dire quando il giocatore deve scartare un carta)
	public void gestisciScarto(Tavolo t, byte indice, Carta card)
	{
		pDiscard.removeAll();
		ImageIcon img;
		switch(indice)
		{
			case 0:
				pSouth.remove(card.lab);
				break;
			case 1:
				pNorth.remove(card.lab);
				break;
			case 2:
				pEast.remove(card.lab);
				break;
			case 3:
				pWest.remove(card.lab);
				break;
		}

		pDiscard.setImage(card.getImg()[0]);
		updatePanels(t,indice);
		coloraColor(t);
		repaint();
	}

	// aggiorna il pannello del giocatore specificato in base al numero delle sue carte
	public void updatePanels(Tavolo t,int i)
	{
		ImageIcon img;
		int width,height;

		if (t.getPlayer(i).getNumCarte()>4)
		{
			width=(int)(Math.abs(100/t.getPlayer(i).getNumCarte()*4));

			height=(int)(Math.abs((170/t.getPlayer(i).getNumCarte()*6/1.2)));
		}
		else
		{
			//dimensione standard per una lista con 4 o meno carte
			width=100;
			height=170;
		}

		switch (i)
		{
			case 0:
			{
				pSouth.removeAll();
				Carta card= t.getPlayer(i).getMano().getHead();

				for (; card!=null; card=card.getNext())
				{
					card.lab.setSize(width,height);
					//rimetto l'immagine perche' non riesco a prenderla
					img= new ImageIcon(card.getImg()[0]);
					img.setImage(img.getImage().getScaledInstance(width,height , Image.SCALE_DEFAULT));
					card.lab.setIcon(img);
					pSouth.add(card.lab);
				}
				break;
			}
			case 1:
			{
				pNorth.removeAll();
				Carta card= t.getPlayer(i).getMano().getHead();

				for (; card!=null; card=card.getNext())
				{
					card.lab.setSize(width,height);

					//rimetto l'immagine perche' non riesco a prenderla
					img= new ImageIcon(""+System.getProperty("user.dir") + "\\img\\Carte" +"\\"+"backn.png");
					img.setImage(img.getImage().getScaledInstance( width,height, Image.SCALE_DEFAULT));
					card.lab.setIcon(img);
					pNorth.add(card.lab);
				}

				break;
			}
			case 2:
			{
				pEast.removeAll();
				Carta card= t.getPlayer(i).getMano().getHead();

				for (; card!=null; card=card.getNext())
				{
					card.lab.setSize(width,height);

					//rimetto l'immagine perche' non riesco a prenderla
					img= new ImageIcon(""+System.getProperty("user.dir") + "\\img\\Carte" +"\\"+"backe.png");
					img.setImage(img.getImage().getScaledInstance( height,width, Image.SCALE_DEFAULT));
					card.lab.setIcon(img);
					pEast.add(card.lab);
				}
				break;
			}
			case 3:
			{
				pWest.removeAll();
				Carta card= t.getPlayer(i).getMano().getHead();

				for (; card!=null; card=card.getNext())
				{
					card.lab.setSize(width,height);

					img= new ImageIcon(""+System.getProperty("user.dir") + "\\img\\Carte" +"\\"+"backo.png");
					img.setImage(img.getImage().getScaledInstance(height,width, Image.SCALE_DEFAULT));
					card.lab.setIcon(img);
					pWest.add(card.lab);
				}
				break;
			}
		}
		if (i!=0)
			numcarte[i-1].setText("Numero di carte : "+t.getPlayer(i).getNumCarte());
		repaint();
	}

	// guarda i commenti piu' in basso
	public byte gestioneBluffP4(Tavolo t, int indice, int cont, int [] opt)
	{
		//si arriva qui se il giocatore prima ha tirato un +4
		//se si accusa non si puo' continuare dal punto in cui si era. bisogna prendersi le carte nel caso si abbia sbagliato o dare le carte nel caso il bluff sia riuscito
		//controlliamo chi e' il precedente
		int p;
		if (t.getGiro()) //se il giro e' orario
		{
			if ((indice -1)>=0) //se e' un giocatore (quindi tra un numero tra 0 a 3)
				p=indice-1;
			else
				p=opt[1];

			// differenziamo i casi per difficolta'
			switch (opt[2])
			{
				case 1:
				{
					//se l'accusato ha + di 5 carte
					if ((Math.random()*10+1)>6&&t.getPlayer(p).getNumCarte()>5)
					{
						JOptionPane.showMessageDialog(null,t.getPlayer(indice).toString()+" dice: Ti accuso di bluff!");
						//facciamo vedere al giocatore le carte dell'accusato e ne controlliamo il contenuto
						return gestioneAccusa(t,indice, p,cont);
						//se l'accusa c'e' stata allora il cont e' 0

					}

					return (byte) cont;
				}
				case 2:
				{
					//se l'accusato ha + di 7 carte
					if ((Math.random()*10+1)>5&&t.getPlayer(p).getNumCarte()>7)
					{
						JOptionPane.showMessageDialog(null,t.getPlayer(indice).toString()+" dice: Ti accuso di bluff!");
						//facciamo vedere al giocatore le carte dell'accusato e ne controlliamo il contenuto
						return gestioneAccusa(t,indice, p, cont);
						//se l'accusa c'e' stata allora il cont e' 0

					}
					return (byte) cont;
				}
				case 3:
				{
					//se l'accusato ha + di 10 carte
					if(t.getPlayer(p).getNumCarte()>10)
					{
						JOptionPane.showMessageDialog(null,t.getPlayer(indice).toString()+" dice: Ti accuso di bluff!");
						//facciamo vedere al giocatore le carte dell'accusato e ne controlliamo il contenuto
						return gestioneAccusa(t,indice, p, cont);
						//se l'accusa c'e'stata allora il cont e' 0

					}
					return (byte) cont;
				}
			}
		}
		else
		{
			if ((indice +1)>=opt[1]) //se e' un giocatore (quindi tra un numero tra 0 a 3)
				p=0;
			else
				p=indice +1;
			// differenziamo i casi per difficolta'
			switch (opt[2])
			{
				case 1:
				{
					//se l'accusato ha + di 5 carte
					if ((Math.random()*10+1)>6&&t.getPlayer(p).getNumCarte()>5)
					{
						JOptionPane.showMessageDialog(null,t.getPlayer(indice).toString()+" dice: Ti accuso di bluff!");
						//facciamo vedere al giocatore le carte dell'accusato e ne controlliamo il contenuto
						return gestioneAccusa(t,indice, p, cont);
						//se l'accusa c'e' stata allora il cont e' 0

					}
					return (byte) cont;
				}
				case 2:
				{
					//se l'accusato ha + di 7 carte
					if ((Math.random()*10+1)>5&&t.getPlayer(p).getNumCarte()>7)
					{
						JOptionPane.showMessageDialog(null,""+t.getPlayer(indice).toString()+" dice: Ti accuso di bluff!");
						//facciamo vedere al giocatore le carte dell'accusato e ne controlliamo il contenuto
						return gestioneAccusa(t,indice, p, cont);
						//se l'accusa c'e'stata allora il cont e' 0

					}
					return (byte) cont;
				}
				case 3:
				{
					//se l'accusato ha + di 10 carte
					if(t.getPlayer(p).getNumCarte()>10)
					{
						JOptionPane.showMessageDialog(null,t.getPlayer(indice).toString()+" dice: Ti accuso di bluff!");
						//facciamo vedere al giocatore le carte dell'accusato e ne controlliamo il contenuto
						return gestioneAccusa(t,indice, p, cont);
						//se l'accusa c'e' stata allora il cont e' 0

					}
					return (byte) cont;
				}
			}
		}
		return (byte)cont;
	}
	// metodo che gestisce se un giocatore ha o meno avuto ragione nell'accusare l'avversario
	public byte gestioneAccusa(Tavolo t, int indice, int p, int cont)
	{
		//button con il colore precedente
		JButton prevcol =new JButton();
		prevcol.setBounds((int)monitor.getWidth()/2+150+50, 400,50,50);
		backingPanel.add(prevcol);
		repaint();
		revalidate();
		//gestione della colorazione
		if (t.getColore()[0]=='r')
			prevcol.setBackground(Color.red);
		if (t.getColore()[0]=='g')
			prevcol.setBackground(Color.yellow);
		if (t.getColore()[0]=='b')
			prevcol.setBackground(Color.blue);
		if (t.getColore()[0]=='v')
			prevcol.setBackground(Color.green);

		//metodo che fa vedere le carte dell'avversario
		svelaCarte(t,p);
		Carta card= t.getPlayer(p).getMano().getHead();
		for ( ; card != null ; card=card.getNext() )
		{
			//System.out.println("Il giocatore "+t.getPlayer(p).toString()+" ha la seguente carta : "+card.toString());
			if (card.getColore()==t.getColore()[0])
			{
				//se la carta c'e'
				pescaDue(t, (byte)p,(byte)(cont));
				JOptionPane.showMessageDialog(null,"L'accusato ha bluffato davvero! Penalty!!" );
				//gestione achievement - 3 - hai fatto un'accusa fondata x 14 volte
				if (achivalue[3] != 14)
				{
					achivalue= GameAchievements.updateAchievement(achivalue, 3,t.getPlayer(0).toString());
				}
				if (p==0)
					if (records[12]!=0)
					{
						records[12]--;
						records[14]++;
					}
				this.cont=0;
				prevcol.setVisible(false);
				return (byte)cont;
			}
		}

		//carte con lo stesso colore non ci sono
		pescaDue(t, (byte)indice, (byte)(cont+1));
		JOptionPane.showMessageDialog(null,"L'accusato non ha bluffato! Penalty per l'accusatore!!");
		prevcol.setVisible(false);
		this.cont=0;
		return (byte)cont;
	}

	//metodo che colora di volta in volta il button con il colore odierno del tavolo
	public void coloraColor(Tavolo t)
	{
		if (t.getColore()[1]=='r')
			color.setBackground(Color.red);
		if (t.getColore()[1]=='g')
			color.setBackground(Color.yellow);
		if (t.getColore()[1]=='b')
			color.setBackground(Color.blue);
		if (t.getColore()[1]=='v')
			color.setBackground(Color.green);
		repaint();
		revalidate();
		color.revalidate();
	}

	//metodo che gestisce la scoperta delle carte e la sua ricopertura
	public void svelaCarte(Tavolo t, int i)
	{
		ImageIcon img;
		int width,height;

		if (t.getPlayer(i).getNumCarte()>4)
		{
			width=100/t.getPlayer(i).getNumCarte()*4;

			height=(int)(170/t.getPlayer(i).getNumCarte()*6/1.2);
		}
		else
		{
			width=100;

			height=170;
		}

		switch (i)
		{
			//se sei l'utente vedi gia' le tue carte XD
			case 0:
					break;
			case 1:
			{
				pNorth.removeAll();
				Carta card= t.getPlayer(i).getMano().getHead();

				for (; card!=null; card=card.getNext())
				{
					card.lab.setSize(width,height);
					//rimetto l'immagine perche' non riesco a prenderla

					img= new ImageIcon(card.getImg()[0]);

					img.setImage(img.getImage().getScaledInstance( width,height, Image.SCALE_DEFAULT));
					card.lab.setIcon(img);
					pNorth.add(card.lab);
				}

				pNorth.revalidate();
				repaint();

				JOptionPane.showMessageDialog(null,""+t.getPlayer(i).toString()+ " dice : Ecco le carte! Possiamo continuare?");

				pNorth.removeAll();

				for (; card!=null; card=card.getNext())
				{
					card.lab.setSize(width,height);
					//rimetto l'immagine perche' non riesco a prenderla

					img= new ImageIcon((System.getenv("USERPROFILE")).toString()+"/Dropbox/Uno/Source/img/Carte/backn.png");

					img.setImage(img.getImage().getScaledInstance( width,height, Image.SCALE_DEFAULT));
					card.lab.setIcon(img);
					pNorth.add(card.lab);
					pNorth.revalidate();
					pNorth.repaint();
				}

				pNorth.revalidate();
				pNorth.repaint();
			}
			break;
			case 2:
			{
				Carta card= t.getPlayer(i).getMano().getHead();

				for (; card!=null; card=card.getNext())
				{
					card.lab.setSize(height,width);
					//rimetto l'immagine perche'  non riesco a prenderla

					img= new ImageIcon(card.getImg()[1]);

					img.setImage(img.getImage().getScaledInstance( height,width, Image.SCALE_DEFAULT));
					card.lab.setIcon(img);
					pEast.add(card.lab);
					pEast.revalidate();
					repaint();


				}

				//messaggio per fermare le carte comparse
				JOptionPane.showMessageDialog(null,""+t.getPlayer(i).toString()+ " dice : Ecco le carte! Possiamo continuare?");

				pEast.removeAll();
				card= t.getPlayer(i).getMano().getHead();

				for (; card!=null; card=card.getNext())
				{
					card.lab.setSize(width,height);
					//rimetto l'immagine perche'  non riesco a prenderla

					img= new ImageIcon(""+System.getProperty("user.dir") + "\\img\\Carte" +"\\"+"backe.png");

					img.setImage(img.getImage().getScaledInstance( height,width, Image.SCALE_DEFAULT));
					card.lab.setIcon(img);
					pEast.add(card.lab);
					pEast.revalidate();
					repaint();

				}
				pEast.revalidate();
				repaint();
			}
			break;
			case 3:
			{
				Carta card= t.getPlayer(i).getMano().getHead();
				pWest.removeAll();

				for (; card!=null; card=card.getNext())
				{
					card.lab.setSize(height,width);
					//rimetto l'immagine perche' non riesco a prenderla

					img= new ImageIcon(card.getImg()[1]);

					img.setImage(img.getImage().getScaledInstance( height,width, Image.SCALE_DEFAULT));
					card.lab.setIcon(img);
					pWest.add(card.lab);
					pWest.revalidate();
					repaint();
				}
				pWest.revalidate();
				repaint();

				JOptionPane.showMessageDialog(null,""+t.getPlayer(i).toString()+ " dice : Ecco le carte! Possiamo continuare?");

				pWest.removeAll();
				card= t.getPlayer(i).getMano().getHead();

				for (; card!=null; card=card.getNext())
				{
					card.lab.setSize(width,height);

					img= new ImageIcon(""+System.getProperty("user.dir") + "\\img\\Carte" +"\\"+"backo.png");
					img.setImage(img.getImage().getScaledInstance(height,width, Image.SCALE_DEFAULT));
					card.lab.setIcon(img);
					pWest.add(card.lab);
					pWest.revalidate();
					repaint();
				}

				pWest.revalidate();
				repaint();
			}
			break;
		}
		return;
	}
	// Si pu¨° spostare in un altro file
	public void wft(Tavolo t, int i, int numgioc)
	{
		JOptionPane.showMessageDialog(null, t.getPlayer(i).toString()+ " says : \n"+"What the hell! more of 15 fuc..ing cards! I can't believe it! Ass Holes, have you used some tricks, right?");
		if (i+1<numgioc)
			JOptionPane.showMessageDialog(null, t.getPlayer(i+1).toString()+ " says : \n Come on man, keep calm and continue to play... we are not a jokers, we are your friends!");
		else
			JOptionPane.showMessageDialog(null, t.getPlayer(i-1).toString()+ " says : \n Come on man, keep calm and continue to play... we are not a jokers, we are your friends!");
		JOptionPane.showMessageDialog(null, "Nicola Tesla says : what the fu...! I'm dead and you "+(numgioc+1)+" play with this stupid and orrible game?! Go to study!");
		String wth="";
		for (int p=0; p<numgioc-1;p++)
			wth+=""+ t.getPlayer(p).toString()+",";
		JOptionPane.showMessageDialog(null, wth+" "+t.getPlayer(numgioc-1).toString()+" and "+t.getPlayer(numgioc).toString()+ " say : what the ... ?");
		JOptionPane.showMessageDialog(null, "Someone says : \n Ehy Jimmy");
		JOptionPane.showMessageDialog(null, wth+" "+t.getPlayer(numgioc-1).toString()+" and "+t.getPlayer(numgioc).toString()+ " say : what the ... ? who are you a how did you come here?");
		JOptionPane.showMessageDialog(null, "K. Munari  says : \n Calma raga, li ho invitati io per una partita!");
		JOptionPane.showMessageDialog(null, wth+" "+t.getPlayer(numgioc-1).toString()+"  and "+t.getPlayer(numgioc).toString()+ " say : \n Ma noi stavamo gia' giocando!!");
		JOptionPane.showMessageDialog(null, "K. Munari  says : \n Va bene, scusate l'intrusione... Buona giocata!");
		JOptionPane.showMessageDialog(null, "Tesla says: \n go to study! Uhm... tomorrow I'll come again to play here, ok? and if you won't be here...");
		JOptionPane.showMessageDialog(null, "K. Munari says: \n Come on Niky, we'll play together tomorrow, don't worry!");
		JOptionPane.showMessageDialog(null, "Someone says : \n Bye Jimmy");
		JOptionPane.showMessageDialog(null, ""+t.getPlayer(0).toString()+" says : \n So... could we go back in this game, please? this topic is so strange! Don't you think?");
		JOptionPane.showMessageDialog(null, ""+t.getPlayer(i).toString()+" says : \n Uhm... ok, I'm agree with you... let's continue to play... ");
	}
}
	public class Game
	{
		public Game ()
		{}
		public void go(final JFrame w,String utente)
		{
			final JWindow subw=new JWindow();
			w.setAlwaysOnTop(false);
			//aggiunge al window il layeredPane
			w.setVisible(false);
			subw.getContentPane().add(new Gioco(w,subw,utente));

			JButton back=new JButton("Torna al menu'");
			back.setBackground(Color.WHITE);
			back.setBounds(50, 50, 150,40);

			subw.getLayeredPane().add(back);
			back.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					if(JOptionPane.showConfirmDialog(null,"Sei sicuro di voler tornare indietro? Tutti i progressi non salvati verranno persi.","Uscita",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION)
					{
						subw.dispose();
						w.setVisible(true);
						w.setAlwaysOnTop(true);
					}
					else
					{
						w.setAlwaysOnTop(false);
						subw.setAlwaysOnTop(true);
					}
				}
			});

			subw.pack();//imposta le dimensioni del window in base alle dimensioni del componente che contiene
			subw.setLocationRelativeTo(null);//centra la finestra
			w.setVisible(true);
		}
	}
