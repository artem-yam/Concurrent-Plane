package com.epam.jtc.concurrentPlane;

import com.epam.jtc.concurrentPlane.output.InfoOutput;

import java.util.concurrent.CountDownLatch;

public class MachineGun implements Runnable {

    private static final double MINUTE_IN_MILLIS = 60000;
    private static final double MILLIS_IN_NANOS = 1000000;

    private static final int SHOT_DURATION = 10;

    private static final int MIN_FIRE_RATE = 500;

    private final int fireRate;
    private int positionRelativeToPropeller;
    private Synchronizer planeEquipmentSynchronizer;
    private volatile CountDownLatch planeWorkTimeSynchronizer;

    private InfoOutput infoOutput;

    public MachineGun(int fireRate, Synchronizer planeEquipmentSynchronizer,
                      int positionRelativeToPropeller,
                      CountDownLatch planeWorkTimeSynchronizer,
                      InfoOutput infoOutput) {
        this.planeEquipmentSynchronizer = planeEquipmentSynchronizer;
        this.planeWorkTimeSynchronizer = planeWorkTimeSynchronizer;
        this.infoOutput = infoOutput;

        if (fireRate < MIN_FIRE_RATE) {
            fireRate = MIN_FIRE_RATE;
        }

        this.fireRate = fireRate;
        this.positionRelativeToPropeller = positionRelativeToPropeller;
    }

    public void setPlaneEquipmentSynchronizer(
            Synchronizer planeEquipmentSynchronizer) {
        this.planeEquipmentSynchronizer = planeEquipmentSynchronizer;
    }

    private void shoot() {
        try {
            infoOutput.showShot(
                    planeEquipmentSynchronizer
                            .isGunShotBlocked(positionRelativeToPropeller));

            Thread.sleep(SHOT_DURATION);

        } catch (InterruptedException interruptedException) {
            Thread.currentThread().interrupt();
        } finally {
            infoOutput.showShootingStop();
        }
    }

    @Override
    public void run() {
        try {
            double sleepTime = MINUTE_IN_MILLIS / fireRate - SHOT_DURATION;
            long millis = (long) sleepTime;
            int nanos = (int) ((sleepTime - millis) * MILLIS_IN_NANOS);

            while (!Thread.currentThread().isInterrupted()) {
                try {
                    planeEquipmentSynchronizer
                            .getShootingAccess(positionRelativeToPropeller);

                    try {
                        shoot();
                    } finally {
                        planeEquipmentSynchronizer.stopShooting();
                    }

                    Thread.sleep(millis, nanos);
                } catch (InterruptedException interruptedException) {
                    Thread.currentThread().interrupt();
                }
            }
        } finally {
            planeWorkTimeSynchronizer.countDown();
        }
    }
}
