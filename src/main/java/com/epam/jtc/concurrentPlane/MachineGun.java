package com.epam.jtc.concurrentPlane;

import java.util.concurrent.CountDownLatch;

public class MachineGun implements Runnable {

    private static final double MINUTE_IN_MILLIS = 60000;
    private static final double MILLIS_IN_NANOS = 1000000;

    private static final int MIN_FIRE_RATE = 500;

    private final int fireRate;
    private int positionRelativeToPropeller;
    private boolean canShoot;
    private Synchronizer planeEquipmentSynchronizer;
    private CountDownLatch planeWorkTimeSynchronizer;

    public MachineGun(int fireRate, Synchronizer planeEquipmentSynchronizer,
                      int positionRelativeToPropeller,
                      CountDownLatch planeWorkTimeSynchronizer) {

        if (fireRate < MIN_FIRE_RATE) {
            fireRate = MIN_FIRE_RATE;
        }

        this.fireRate = fireRate;

        this.planeEquipmentSynchronizer = planeEquipmentSynchronizer;

        this.positionRelativeToPropeller = positionRelativeToPropeller;

        this.planeWorkTimeSynchronizer = planeWorkTimeSynchronizer;
    }

    public int getPositionRelativeToPropeller() {
        return positionRelativeToPropeller;
    }

    public boolean isCanShoot() {
        return canShoot;
    }

    public void setCanShoot(boolean canShoot) {
        this.canShoot = canShoot;
    }

    @Override
    public void run() {

        double sleepTime = MINUTE_IN_MILLIS / fireRate;
        long millis = (long) sleepTime;
        int nanos = (int) ((sleepTime - millis) *
                MILLIS_IN_NANOS);

        while (!Thread.currentThread().isInterrupted()) {
            try {

                planeEquipmentSynchronizer.tryToShoot(this);

                Thread.sleep(millis, nanos);
            } catch (InterruptedException interruptedException) {
                Thread.currentThread().interrupt();
            }
        }

        planeWorkTimeSynchronizer.countDown();
    }
}
