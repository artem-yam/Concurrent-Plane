package com.epam.jtc.concurrentPlane;


import static com.epam.jtc.concurrentPlane.Plane.LOGGER;

public class MachineGun implements Runnable {

    private final static String SHOT = "Gun shot! ";

    private int fireRate;
    private Plane plane;

    MachineGun(int fireRate, Plane plane) {
        this.fireRate = fireRate;
        this.plane = plane;
    }

    @Override
    public void run() {
        double sleepTime = (double) 60000 / fireRate;
        long millis = (long) sleepTime;
        int nanos = (int) ((sleepTime - millis) * 1000000);

        while (!Thread.currentThread().isInterrupted()/*plane.canMachineGunShoot()*/) {
            try {

                plane.getSynchronizer().await();

                LOGGER.info(SHOT);
                //plane.getInfoOutput().showShot();

                Thread.sleep(millis, nanos);
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
        }
    }
}
