import java.util.concurrent.CountDownLatch;

public class Threadlock {


    public static void main(String[] args) {
        System.out.println("main线程启动,等待子线程完成");
        System.out.println("一共3个子线程!");
        final CountDownLatch downLatch = new CountDownLatch(3);

        new Thread(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("1号线程完成！");
                downLatch.countDown();
            }
        }).start();

        new Thread(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(3100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("2号线程完成！");
                downLatch.countDown();
            }
        }).start();

        new Thread(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(3200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("3号线程完成！");
                downLatch.countDown();
            }
        }).start();

        try {
            downLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("等待完成！");
    }
}
