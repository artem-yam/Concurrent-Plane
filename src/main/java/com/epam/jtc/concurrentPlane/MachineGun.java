package com.epam.jtc.concurrentPlane;

public class MachineGun implements Runnable {

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

        while (plane.canMachineGunShoot()) {
            try {

                plane.getSynchronizer().await();

                plane.getInfoOutput().showShot();

                Thread.sleep(millis, nanos);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
