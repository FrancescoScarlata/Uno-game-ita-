import javax.swing.*;

public class Starter extends Thread
{
	private JFrame w;
	private String u;
    public Starter(JFrame w, String u)
    {
    	this.w=w;
    	this.u=u;
    }
    public void run()
	{
		//avviare il thread Game
		Game g=new Game();
		g.go(w,u);
	}
}