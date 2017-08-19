import javax.swing.*;

public class Carta
{
	private byte numero;
	private char colore;
	private String [] img;
	
	private Carta next;
	protected JLabel lab=new JLabel();
	//attributi per lista
	
	public Carta (byte num, char col, String imgv, String imgo)
	{
		img= new String[2];
		numero=num;
		colore=col;
		img[0]=imgv;
		img[1]=imgo;
		next=null;
	}
	
	public char getColore()
	{
		return colore;
	}
	
	public byte getNumero()
	{
		return numero;
	}
	
	public String [] getImg()
	{
		return img;
	}
	
	public Carta getNext()
	{
		return next;
	}
	
	public void setNext(Carta succ)
	{
		next=succ;
	}
	
	public String toString()
	{
		String x= "";
		return x+numero+colore;
	}
	
}

class Speciale extends Carta
{
	private String funzione;
	
	public Speciale(byte num, char col, String imgv, String imgo, String funz)
	{
		super(num,col,imgv,imgo);
		funzione=funz;
	}
	
	public String getFunzione()
	{
		return funzione;
	}
	
	public String toString()
	{
		return ""+funzione+getColore();
	}
	
	
}
