public class Launcher {
	public static final int WIDTH = 1000, HEIGHT = 800;
	public static void main(String[] args){
		Game game = new Game("Render Engine", WIDTH, HEIGHT, 60, false);
		game.start();
	}
}
