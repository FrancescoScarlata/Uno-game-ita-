public class Giocatore
{
	private Mano mazzetto;
	private boolean bot;
	private byte numcarte;
	private String nome;
	private boolean uno;

	public Giocatore( boolean bot, String nome)
	{
		mazzetto= new Mano();
		this.bot= bot;
		numcarte=0;
		this.nome=nome;
		uno=false;
	}

	public byte getNumCarte()
	{
		return numcarte;
	}

	public boolean isBot()
	{
		return bot;
	}

	public String toString()
	{
		return nome;
	}

	public Mano getMano()
	{
		return mazzetto;
	}
	public boolean getUno()
	{
		return uno;
	}
	
	public void uno()
	{
		uno=!uno;
	}
	
	
	public Carta scarta(Carta x)
	{
		if (!mazzetto.isEmpty())
		{
			numcarte--;
			return mazzetto.discardC(x);
		}
		return null;
	}

	public void pesca(Carta pescata)
	{
		getMano().insertTail(pescata);
		numcarte++;
		return;
	}

	public void nuovaMano()
	{
		mazzetto.svuotaMano();
		numcarte=0;
	}
	
}