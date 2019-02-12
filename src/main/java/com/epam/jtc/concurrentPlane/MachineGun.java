package com.epam.jtc.concurrentPlane;


public class MachineGun implements Runnable {

    private int fireRate;
    private int positionRelativeToPropeller;
    private volatile boolean canShoot = false;
    private SynchronizingObject synchronizingObject;

    MachineGun(int fireRate, SynchronizingObject synchronizingObject,
            int positionRelativeToPropeller) {
        this.fireRate = fireRate;

        this.synchronizingObject = synchronizingObject;

        this.positionRelativeToPropeller = positionRelativeToPropeller;
    }

    int getPositionRelativeToPropeller() {
        return positionRelativeToPropeller;
    }

    boolean isCanShoot() {
        return canShoot;
    }

    void setCanShoot(boolean canShoot) {
        this.canShoot = canShoot;
    }

    @Override
    public void run() {

        double sleepTime = (double) 60000 / fireRate;
        long millis = (long) sleepTime;
        int nanos = (int) ((sleepTime - millis) * 1000000);

        while (!Thread.currentThread().isInterrupted()) {
            try {

                synchronizingObject.tryToShoot(this);

                Thread.sleep(millis, nanos);
            } catch (InterruptedException interruptedException) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
