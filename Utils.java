import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.lang.management.ThreadMXBean;
import java.text.DecimalFormat;
import java.lang.management.MemoryUsage;
import java.lang.management.MemoryMXBean;



public class Utils {
    private static final OperatingSystemMXBean osMxBean = ManagementFactory.getOperatingSystemMXBean();
    private static final ThreadMXBean threadMxBean = ManagementFactory.getThreadMXBean();
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#0.00");
    private static final long START_TIME = System.currentTimeMillis();

    private static final MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();


    public static String getCPUUsage() {
        double cpuUsage = 0.0;
        long prevCpuTime = threadMxBean.getCurrentThreadCpuTime();
        long prevUpTime = START_TIME;
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            // Ignore
        }
        long currCpuTime = threadMxBean.getCurrentThreadCpuTime();
        long currUpTime = System.currentTimeMillis();
        double elapsedCpuTime = currCpuTime - prevCpuTime;
        double elapsedTime = currUpTime - prevUpTime;
        cpuUsage = elapsedCpuTime / (elapsedTime * osMxBean.getAvailableProcessors()) * 100;
        return DECIMAL_FORMAT.format(cpuUsage) + " %";
    }

    public static String getMemoryUsage() {
        MemoryUsage heapMemoryUsage = memoryMXBean.getHeapMemoryUsage();
        double usedMemory = (double) heapMemoryUsage.getUsed() / (1024 * 1024 * 1024);
        double maxMemory = (double) heapMemoryUsage.getMax() / (1024 * 1024 * 1024);
        String message = DECIMAL_FORMAT.format(usedMemory) + " / " + DECIMAL_FORMAT.format(maxMemory) + " GB";
        return message;
    }


}
