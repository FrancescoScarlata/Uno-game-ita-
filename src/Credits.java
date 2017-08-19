import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.awt.event.*;

class Credits
{
	static String[] creditsTexts = new String[19];
	public void Credits(final JFrame w)
	{
		LoadCredits(creditsTexts);
		final int W=500;
		System.out.println("Entra in Crediti");
		final JWindow subw=new JWindow();

		//Impostazione dimensioni - Magari si puo' fare un metodo apparte
		Dimension monitor= Toolkit.getDefaultToolkit().getScreenSize();
		
		if (monitor.getWidth()>1000)
			subw.setSize(500,650);
		else
			subw.setSize(500,300);
		
		subw.setLocationRelativeTo(null);
		subw.setVisible(true);

		JPanel jpanel_global=new JPanel(); //Pannello MAIN
		jpanel_global.setLayout(null);
		subw.add(jpanel_global);
		jpanel_global.setBackground(Color.darkGray);

		JLabel titolo=new JLabel("CREDITI", JLabel.CENTER);
		titolo.setFont(new Font("Arial", Font.PLAIN, 30));
		jpanel_global.add(titolo);
		titolo.setBounds(W/2-300/2,-130,300,300);
		titolo.setForeground(Color.white);

		//Label con le scritte
		//Francesco
		JLabel l1=new JLabel(creditsTexts[0], JLabel.CENTER);
		l1.setFont(new Font("Arial", Font.PLAIN, 16));
		jpanel_global.add(l1);
		l1.setBounds(W/2-360/2,-97,360,300);
		l1.setForeground(Color.white);

		JLabel l1_2=new JLabel(creditsTexts[1], JLabel.CENTER);
		l1_2.setFont(new Font("Arial", Font.PLAIN, 20));
		jpanel_global.add(l1_2);
		l1_2.setBounds(W/2-360/2,-78,360,300);
		l1_2.setForeground(Color.red);

		//Salvo
		JLabel l2=new JLabel(creditsTexts[2], JLabel.CENTER);
		l2.setFont(new Font("Arial", Font.PLAIN, 16));
		jpanel_global.add(l2);
		l2.setBounds(W/2-360/2,-47,360,300);
		l2.setForeground(Color.white);

		JLabel l2_2=new JLabel(creditsTexts[3], JLabel.CENTER);
		l2_2.setFont(new Font("Arial", Font.PLAIN, 20));
		jpanel_global.add(l2_2);
		l2_2.setBounds(W/2-360/2,-29,360,300);
		l2_2.setForeground(Color.red);

		//Stefano
		JLabel l3=new JLabel(creditsTexts[4], JLabel.CENTER);
		l3.setFont(new Font("Arial", Font.PLAIN, 16));
		jpanel_global.add(l3);
		l3.setBounds(W/2-360/2,3,360,300);
		l3.setForeground(Color.white);

		JLabel l3_3=new JLabel(creditsTexts[5], JLabel.CENTER);
		l3_3.setFont(new Font("Arial", Font.PLAIN, 20));
		jpanel_global.add(l3_3);
		l3_3.setBounds(W/2-360/2,21,360,300);
		l3_3.setForeground(Color.red);

		//COLONNA SONORA
		JLabel l4=new JLabel(creditsTexts[6], JLabel.CENTER);
		l4.setFont(new Font("Arial", Font.PLAIN, 16));
		jpanel_global.add(l4);
		l4.setBounds(W/2-360/2,50,360,300);
		l4.setForeground(Color.white);

		JLabel l_c_s_1=new JLabel(creditsTexts[7], JLabel.CENTER);
		l_c_s_1.setFont(new Font("Arial", Font.PLAIN, 20));
		jpanel_global.add(l_c_s_1);
		l_c_s_1.setBounds(W/2-360/2,67,360,300);
		l_c_s_1.setForeground(Color.red);

		JLabel l_c_s_2=new JLabel(creditsTexts[8], JLabel.CENTER);
		l_c_s_2.setFont(new Font("Arial", Font.PLAIN, 20));
		jpanel_global.add(l_c_s_2);
		l_c_s_2.setBounds(W/2-360/2,85,360,300);
		l_c_s_2.setForeground(Color.red);

		JLabel l_c_s_3=new JLabel(creditsTexts[9], JLabel.CENTER);
		l_c_s_3.setFont(new Font("Arial", Font.PLAIN, 20));
		jpanel_global.add(l_c_s_3);
		l_c_s_3.setBounds(W/2-360/2,101,360,300);
		l_c_s_3.setForeground(Color.red);

		JLabel l_c_s_4=new JLabel(creditsTexts[10], JLabel.CENTER);
		l_c_s_4.setFont(new Font("Arial", Font.PLAIN, 20));
		jpanel_global.add(l_c_s_4);
		l_c_s_4.setBounds(W/2-360/2,117,360,300);
		l_c_s_4.setForeground(Color.red);

		JLabel l_k_1=new JLabel(creditsTexts[11], JLabel.CENTER);
		l_k_1.setFont(new Font("Arial", Font.PLAIN, 16));
		jpanel_global.add(l_k_1);
		l_k_1.setBounds(W/2-360/2,133,360,300);
		l_k_1.setForeground(Color.white);

		JLabel l_k_2=new JLabel(creditsTexts[12], JLabel.CENTER);
		l_k_2.setFont(new Font("Arial", Font.PLAIN, 20));
		jpanel_global.add(l_k_2);
		l_k_2.setBounds(W/2-360/2,152,360,300);
		l_k_2.setForeground(Color.blue.darker());

		JLabel l_k_3=new JLabel(creditsTexts[13], JLabel.CENTER);
		l_k_3.setFont(new Font("Arial", Font.PLAIN, 16));
		jpanel_global.add(l_k_3);
		l_k_3.setBounds(W/2-360/2,168,360,300);
		l_k_3.setForeground(Color.white);

		JLabel l_s_1=new JLabel(creditsTexts[14], JLabel.CENTER);
		l_s_1.setFont(new Font("Arial", Font.PLAIN, 16));
		jpanel_global.add(l_s_1);
		l_s_1.setBounds(W/2-360/2,193,360,300);
		l_s_1.setForeground(Color.white);

		JLabel l_s_2=new JLabel(creditsTexts[15], JLabel.CENTER);
		l_s_2.setFont(new Font("Arial", Font.PLAIN, 16));
		jpanel_global.add(l_s_2);
		l_s_2.setBounds(W/2-360/2,209,360,300);
		l_s_2.setForeground(Color.white);

		JLabel l_s_3=new JLabel(creditsTexts[16], JLabel.CENTER);
		l_s_3.setFont(new Font("Arial", Font.PLAIN, 16));
		jpanel_global.add(l_s_3);
		l_s_3.setBounds(W/2-360/2,225,360,300);
		l_s_3.setForeground(Color.red);

		JLabel l_s_4=new JLabel(creditsTexts[17], JLabel.CENTER);
		l_s_4.setFont(new Font("Arial", Font.PLAIN, 16));
		jpanel_global.add(l_s_4);
		l_s_4.setBounds(W/2-360/2,241,360,300);
		l_s_4.setForeground(Color.red);

		JLabel l_s_5=new JLabel(creditsTexts[18], JLabel.CENTER);
		l_s_5.setFont(new Font("Arial", Font.PLAIN, 16));
		jpanel_global.add(l_s_5);
		l_s_5.setBounds(W/2-360/2,257,360,300);
		l_s_5.setForeground(Color.red);

		ImageIcon x = new ImageIcon();

		String d="";
		 d+="./img/title.png";

		 x = new ImageIcon(d);
		 x.setImage(x.getImage().getScaledInstance( 320, 176,Image.SCALE_DEFAULT));

		 JLabel title_1 = new JLabel(x);
		 d="";

		 jpanel_global.add(title_1);
		 title_1.setBounds(W/2-360/2,360,360,300);

		 JButton back=new JButton("Torna indietro");

                jpanel_global.add(back); //aggiungo il bottone
		back.setBounds(175, 615, 150, 20);

		//Gestore eventi

		back.addActionListener(new ActionListener() //gestisce direttamente l'evento per evitare richieste superflue
		{
			public void actionPerformed(ActionEvent e)
			{
				subw.dispose();
				w.setAlwaysOnTop(true);
			}
		});


	}

	static void LoadCredits(String [] CT)
	{
		CT[0] ="Algoritmo di gioco e gestione Records a cura di ";
		CT[1] ="Francesco Scarlata";
		CT[2] ="Grafica di gioco e gestione Opzioni a cura di ";
		CT[3] ="Salvatore Manfredi";
		CT[4] ="Gestione Trofei e Crediti a cura di ";
		CT[5] ="Stefano Zimmitti";
		CT[6] ="Colonna sonora:";
		CT[7] ="Murray Gold (Doctor Who Soundtrack)";
		CT[8] ="Benny Goodman";
		CT[9] ="Buddy De Franco";
		CT[10] ="Olivier Franc";
		CT[11] ="E un ringraziamento speciale va a";
		CT[12] ="Kevin Munari";
		CT[13] ="che ci ha aiutato a ripassare le regole";
		CT[14] ="Vorremmo inoltre ricordare con piacere";
		CT[15] ="(in ordine di scomparsa)";
		CT[16] ="CC Timido";
		CT[17] ="Il pirata e le sue carte fantasma";
		CT[18] ="L'anziano Mr.Game";
		return;
	}
}