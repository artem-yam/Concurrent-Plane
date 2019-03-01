package com.epam.jtc.concurrentPlane;

import java.util.concurrent.CountDownLatch;

public class MachineGun implements Runnable {

    private static final double MINUTE_IN_MILLIS = 60000;
    private static final double MILLIS_IN_NANOS = 1000000;

    private static final int SHOT_DURATION = 10;

    private static final int MIN_FIRE_RATE = 500;

    private final int fireRate;
    private int positionRelativeToPropeller;
    private boolean isBlocked = true;
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

    public boolean isBlocked() {
        return isBlocked;
    }

    public void setBlocked(boolean blocked) {
        isBlocked = blocked;
    }

    private void shoot() {
        try {
            planeEquipmentSynchronizer.getInfoOutput().showShot(
                    planeEquipmentSynchronizer.getGuns().indexOf(this),
                    isBlocked());

            Thread.sleep(SHOT_DURATION);
        } catch (InterruptedException interruptedException) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public void run() {

        double sleepTime = MINUTE_IN_MILLIS / fireRate - SHOT_DURATION;
        long millis = (long) sleepTime;
        int nanos = (int) ((sleepTime - millis) *
                MILLIS_IN_NANOS);

        while (!Thread.currentThread().isInterrupted()) {
            try {
                boolean canShoot = false;

                do {
                    //planeEquipmentSynchronizer.updateGunBlocked(this);
                   /* isBlocked = planeEquipmentSynchronizer.getPropeller()
                            .isGunShotBlocked(this);*/

                    if (Thread.currentThread().isInterrupted()) {
                        return;
                    }
                } while (!planeEquipmentSynchronizer.canShoot(this));

                do {
                    planeEquipmentSynchronizer.getShootingAccess();

                    try {
                        if (!isBlocked) {
                            canShoot = true;
                            shoot();
                        }
                    } finally {
                        planeEquipmentSynchronizer.stopShooting();
                    }

                } while (!canShoot);

                Thread.sleep(millis, nanos);
            } catch (InterruptedException interruptedException) {
                Thread.currentThread().interrupt();
            }
        }

        planeWorkTimeSynchronizer.countDown();
    }
}
