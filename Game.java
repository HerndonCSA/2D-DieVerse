import mapobjects.ImageObject;
import mapobjects.MapObject;
import mapobjects.Material;
import mapobjects.Type;
import mapobjects.shapes.Rectangle;
import mapobjects.shapes.Square;
import weapons.AK47;
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

    private final PlayerManager playerManager = PlayerManager.getInstance();

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

        MapObject bgM2 = new ImageObject(new Point (0, 50), Type.BACKGROUND, "images/castleBg2M2.png");

        MapObject castleFloor = new ImageObject(new Point (150, 340), Type.SOLID, "images/castleBrickRowGroundM2.png");
        MapObject dungeonFloor = new ImageObject(new Point (0, 732), Type.SOLID, "images/dungeonGroundM2.png");
        MapObject leftWallM2 = new ImageObject(new Point (0, 0), Type.SOLID, "images/wallM2.png");
        MapObject rightWallM2 = new ImageObject(new Point (992, 0), Type.SOLID, "images/wallM2.png");
        MapObject castleCeiling = new ImageObject(new Point (0, 0), Type.BACKGROUND, "images/castleBrickRowM2.png");
        MapObject dungeonBgM2 = new ImageObject(new Point (0, 390), Type.BACKGROUND, "images/dungeonBgM2.png");
        MapObject spawnM2P2 = new ImageObject(new Point (866, 663), Type.SOLID, "images/spawnM2.png");
        MapObject spawnM2P1 = new ImageObject(new Point (8, 662), Type.SOLID, "images/spawnM2.png");
        MapObject dlbM2 = new ImageObject(new Point (100, 665), Type.SOLID, "images/dungeonBlockM2.png");
        MapObject dmbM2 = new ImageObject(new Point (430, 665), Type.SOLID, "images/dungeonBlockM2.png");
        MapObject drbM2 = new ImageObject(new Point (770, 665), Type.SOLID, "images/rightBlockM2.png");
        MapObject platform1M2 = new ImageObject(new Point (255, 610), Type.SOLID, "images/platformM2.png");
        MapObject platform2M2 = new ImageObject(new Point (620, 610), Type.SOLID, "images/platformM2.png");
        MapObject platform3M2 = new ImageObject(new Point (108, 525), Type.SOLID, "images/platformM2.png");
        MapObject platform4M2 = new ImageObject(new Point (769, 525), Type.SOLID, "images/platformM2.png");
        MapObject platform5M2 = new ImageObject(new Point (-50, 445), Type.SOLID, "images/platformM2.png");
        MapObject platform6M2 = new ImageObject(new Point (920, 445), Type.SOLID, "images/platformM2.png");

        MapObject pillarPlatform1M2 = new ImageObject(new Point (100, 388), Type.SOLID, "images/pillarPlatformM2.png");
        MapObject pillarPlatform2M2 = new ImageObject(new Point (840, 388), Type.SOLID, "images/pillarPlatformM2.png");

        // use listOf method to add all the shapes to the map

        gameMap = new Map(List.of(bgM2, dungeonBgM2, castleFloor, dungeonFloor, castleCeiling, dlbM2, spawnM2P1, pillarPlatform1M2, pillarPlatform2M2,
                spawnM2P2, dmbM2, drbM2, platform1M2, platform2M2, platform3M2, platform4M2, platform5M2, platform6M2, leftWallM2, rightWallM2));

        playerOne = new Player(new Point(500, 0), "images/Player1.jpg", 0, gameMap, new Pistol());
        playerTwo = new Player(new Point(200, 300), "images/Player2.jpg", 1, gameMap, new AK47());
        playerManager.addPlayer(playerOne);
        playerManager.addPlayer(playerTwo);
//
        keyManager = new KeyManager();
//
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
		playerOne.render(graphics);
		playerTwo.render(graphics);
        // tag game: draw tag score at top left corner below debug in large font
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
