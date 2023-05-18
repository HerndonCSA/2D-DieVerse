import mapobjects.ImageObject;
import mapobjects.Material;
import mapobjects.Type;
import mapobjects.shapes.Rectangle;
import mapobjects.shapes.Square;
import weapons.Pistol;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.io.IOException;
import java.util.List;
public class Game implements Runnable {
    private Display display;
    private KeyManager keyManager;
    private final int width;
    private final int height;
    private final String title;

    private BufferStrategy bufferStrategy;
    private Graphics2D graphics;

    private boolean running = false;
    private Thread gameThread;
    private Thread tickThread;
    // Variables for render thread
    private Thread renderThread;
    private int targetFPS;

    private Map gameMap;
    private Player playerOne;
    private Player playerTwo;
    private boolean debug;

    public Game(String title, int width, int height, int refresh_rate) {
        this.width = width;
        this.height = height;
        this.title = title;
        targetFPS = refresh_rate;
        this.debug = false;
    }

    public Game(String title, int width, int height, int refresh_rate, boolean debug) {
        this.width = width;
        this.height = height;
        this.title = title;
        targetFPS = refresh_rate;
        this.debug = debug;

    }

    private void init() throws IOException {
        display = new Display(title, width, height, this);

        Rectangle rectangleOne = new Rectangle(new Point(400, 400), Type.SOLID, Color.BLACK, 50, 90);
        ImageObject background = new ImageObject(new Point(0, 0), Type.BACKGROUND, "background.png");

        Square squareOne = new Square(new Point(500, 500), Type.SOLID, Color.BLACK, 50);
        // add ground, it is a rectangle that is 50 pixels wide and spans the entire width of the screen (use Launcher.WIDTH & Launcher.HEIGHT)
        // rectangle is (Point, type, height, width in the order btw)
        Rectangle ground = new Rectangle(new Point(0, Launcher.HEIGHT - 50), Type.SOLID, Color.BLACK, 50, Launcher.WIDTH);

        Square[] stares = new Square[15];
        for (int i = 0; i < stares.length; i++) {
            stares[i] = new Square(new Point(100 + i * 50, 100 + i * 50), Type.SOLID, Color.BLACK, 60);
        }
        // add all the shapes to the map

        Square[] stairs2 = new Square[5];
        // starts a little higher than halfway from the stairs, but faces the other way
        for (int i = 0; i < stairs2.length; i++) {
            stairs2[i] = new Square(new Point(100 + i * 50, 100 + i * 50), Type.SOLID, Color.BLACK, 60);
        }





        // use listOf method to add all the shapes to the map

        gameMap = new Map(List.of(background, ground, stares[0], stares[1], stares[2], stares[3], stares[4], stares[5], stares[6], stares[7], stares[8], stares[9], stares[10], stares[11], stares[12], stares[13], stares[14]
        , stairs2[0], stairs2[1], stairs2[2], stairs2[3], stairs2[4]));

        playerOne = new Player(new Point(500, 0), "Player.jpg", true, gameMap, new Pistol());
        playerTwo = new Player(new Point(700, 500), "Player.jpg", false, gameMap, new Pistol());

        keyManager = new KeyManager(playerOne, playerTwo);

        display.getCanvas().addKeyListener(keyManager);
    }

    private void tick() {
        // rn just update the color
        // move position of circle
        gameMap.tick();
		playerOne.tick();
		playerTwo.tick();

    }

    void debug(Graphics2D graphics) {
        graphics.setColor(Color.RED);
        graphics.drawString("FPS: " + fps, 0, 12);
        graphics.drawString("TPS: " + tps, 0, 27);
//        graphics.drawString("CPU: " + Utils.getCPUUsage(), 0, 42);
//        // memory, in gigs, used / total GB
//        graphics.drawString("Memory: " + Utils.getMemoryUsage(), 0, 57);
    }


    private void render() {
        bufferStrategy = display.getCanvas().getBufferStrategy();
        if (bufferStrategy == null) {
            display.getCanvas().createBufferStrategy(3);
            return;

        }


        graphics = (Graphics2D) bufferStrategy.getDrawGraphics();

        graphics.clearRect(0, 0, width, height);
        gameMap.render(graphics);
		playerOne.render(graphics, playerTwo);
		playerTwo.render(graphics, playerOne);
        if (debug) debug(graphics);
        // Show the buffer and dispose of the graphics object
        bufferStrategy.show();
        graphics.dispose();

    }

    private Color interpolateColors(Color start, Color end, float progress) {
        int r = (int) (start.getRed() * (1 - progress) + end.getRed() * progress);
        int g = (int) (start.getGreen() * (1 - progress) + end.getGreen() * progress);
        int b = (int) (start.getBlue() * (1 - progress) + end.getBlue() * progress);

        return new Color(r, g, b);
    }

    private int fps = 0;
    private int tps = 0;


    @Override
    public void run() {
        try {
            init();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        tickThread = new Thread(() -> {
            double timePerTick = 1_000_000_000.0 / 240;
            double delta = 0;
            long currentTime;
            long previousTime = System.nanoTime();
            long timer = 0;
            int deltaTPS = 0;

            while (running) {
                currentTime = System.nanoTime();
                delta += (currentTime - previousTime) / timePerTick;
                timer += currentTime - previousTime;
                previousTime = currentTime;

                if (delta >= 1) {
                    tick();
                    deltaTPS++; // Increase TPS count
                    delta--;
                }

                if (timer >= 1_000_000_000) {
                    tps = deltaTPS;
                    deltaTPS = 0;
                    timer = 0;
                }
            }
        });

        renderThread = new Thread(() -> {
            long frameStartTime;
            long frameEndTime;
            long frameDuration;
            long currentTime;
            long previousTime = System.nanoTime();
            long renderTimer = 0;
            int deltaFPS = 0;

            int minFps = 30;
            int maxFps = 60;
            long[] frameTimes = new long[maxFps];
            int frameTimeIndex = 0;

            while (running) {
                frameStartTime = System.nanoTime();
                render();
                frameEndTime = System.nanoTime();
                deltaFPS++; // Increase FPS count

                frameDuration = frameEndTime - frameStartTime;
                frameTimes[frameTimeIndex] = frameDuration;
                frameTimeIndex = (frameTimeIndex + 1) % maxFps;

                // Calculate the average frame time
                long totalFrameTime = 0;
                for (long frameTime : frameTimes) {
                    totalFrameTime += frameTime;
                }
                double averageFrameTime = (double) totalFrameTime / maxFps;
                double desiredFrameTime = 1_000_000_000.0 / targetFPS;

                if (averageFrameTime > desiredFrameTime && targetFPS > minFps) {
                    targetFPS--;
                } else if (averageFrameTime < desiredFrameTime && targetFPS < maxFps) {
                    targetFPS++;
                }

                long sleepTime = (long) (desiredFrameTime - frameDuration) / 1_000_000;
                if (sleepTime > 0) {
                    try {
                        Thread.sleep(sleepTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                currentTime = System.nanoTime();
                renderTimer += currentTime - previousTime;
                previousTime = currentTime;

                if (renderTimer >= 1_000_000_000) {
                    // log max and min fps

                    fps = deltaFPS;
                    deltaFPS = 0;
                    renderTimer = 0;
                }
            }
        });
        tickThread.start();
        renderThread.start();

        // Wait for the threads to finish

    }

    public synchronized void start() {
        if (running) {
            return;
        }
        running = true;
        gameThread = new Thread(this);
        gameThread.start();
    }


    public synchronized void stop() {
        // Print out the max and min fps
        System.out.println("Closing Game, good bye!");
        if (!running) {
            return;
        }
        running = false;
        try {
            gameThread.join();
            tickThread.join();
            renderThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
