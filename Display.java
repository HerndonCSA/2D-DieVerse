import java.awt.Canvas;
import java.awt.Dimension;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;

public class Display {

	private JFrame frame;
	private Canvas canvas;

	private final String title;
	private final int width;
	private final int height;
	private Game game;

	public Display(String title, int width, int height, Game game) throws IOException {
		this.title = title;
		this.width = width;
		this.height = height;
		this.game = game;
		createDisplay();
	}

	private void createDisplay() throws IOException {
		frame = new JFrame(title);
		frame.setSize(width, height);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		// icon is ./icon.png, set it

		canvas = new Canvas();
		canvas.setPreferredSize(new Dimension(width, height));
		canvas.setMaximumSize(new Dimension(width, height));
		canvas.setMinimumSize(new Dimension(width, height));

		frame.add(canvas);
		frame.pack();
	}

	public Canvas getCanvas(){
		return canvas;
	}
	public void setTitle(String title){
		frame.setTitle(title);
	}
	public JFrame getFrame(){
		return frame;
	}
}