import java.awt.image.BufferedImage;
import javax.swing.*;
import java.awt.Graphics;
import javax.imageio.*;
import java.io.*;

public class ImagePanel extends JPanel
{
	private BufferedImage image;

	public void setImage(String path)
	{
		try
		{
			image = ImageIO.read(new File(path));
		} catch (IOException ex)
		{
			System.out.println("C'e' qualcosa che non va in questo path :\n"+path);
			// gestire eccezione
		}
	}
	public void paintComponent(Graphics g)
	{
		g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
	}
}