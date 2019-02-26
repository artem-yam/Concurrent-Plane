package com.epam.jtc.concurrentPlane;

import java.util.concurrent.CountDownLatch;

public class Propeller implements Runnable {

    private static final double MINUTE_IN_MILLIS = 60000;
    private static final double MILLIS_IN_NANOS = 1000000;

    private static final int FULL_CIRCLE = 360;
    private static final int HALF_CIRCLE = 360 / 2;
    private static final double ROTATION_STEP_MULTIPLIER = 0.5;

    private static final int MIN_BLADES_COUNT = 3;
    private static final int MIN_BLADE_WIDTH = 20;
    private static final int MIN_ROTATION_SPEED = 1000;

    private final int bladesCount;
    private final int bladesWidth;
    private final double distanceBetweenBlades;
    private int rotationSpeed;
    private Synchronizer planeEquipmentSynchronizer;
    private CountDownLatch planeWorkTimeSynchronizer;

    private double rotationStep;
    private double[] bladesPositions;

    public Propeller(int propellerRotationSpeed, int propellerBladesCount,
                     int propellerBladeWidth,
                     Synchronizer planeEquipmentSynchronizer,
                     CountDownLatch planeWorkTimeSynchronizer) {
        this.planeEquipmentSynchronizer = planeEquipmentSynchronizer;

        if (propellerRotationSpeed < MIN_ROTATION_SPEED) {
            propellerRotationSpeed = MIN_ROTATION_SPEED;
        }

        if (propellerBladesCount < MIN_BLADES_COUNT) {
            propellerBladesCount = MIN_BLADES_COUNT;
        }

        if (propellerBladeWidth < MIN_BLADE_WIDTH) {
            propellerBladeWidth = MIN_BLADE_WIDTH;
        }

        if (propellerBladesCount * propellerBladeWidth >= HALF_CIRCLE) {
            this.bladesCount = HALF_CIRCLE / propellerBladeWidth;

            planeEquipmentSynchronizer.getInfoOutput()
                    .showPropellerBladesCountExcess(
                            propellerBladesCount, bladesCount);

        } else {
            this.bladesCount = propellerBladesCount;
        }

        this.bladesWidth = propellerBladeWidth;

        this.rotationSpeed = propellerRotationSpeed;

        this.distanceBetweenBlades =
                (double) FULL_CIRCLE / bladesCount - bladesWidth;

        rotationStep = bladesWidth * ROTATION_STEP_MULTIPLIER;
        getBladesStartPositions();

        this.planeWorkTimeSynchronizer = planeWorkTimeSynchronizer;
    }

    private void getBladesStartPositions() {
        bladesPositions = new double[bladesCount];

        for (int i = 0; i < bladesPositions.length; i++) {
            bladesPositions[i] = (distanceBetweenBlades + bladesWidth) * i;
        }
    }

    public void updateBladesPosition() {
        for (int i = 0; i < bladesPositions.length; i++) {

            bladesPositions[i] = bladesPositions[i] + rotationStep;

            if (bladesPositions[i] >= FULL_CIRCLE) {
                bladesPositions[i] -= FULL_CIRCLE;
            }
        }
    }

    public boolean checkGunShotAbility(MachineGun gun) {
        boolean canShoot = true;

        for (double blade : bladesPositions) {
            if (gun.getPositionRelativeToPropeller() >= blade &&
                    gun.getPositionRelativeToPropeller() <=
                            blade + bladesWidth) {
                canShoot = false;
                break;
            }
        }

        return canShoot;
    }

    @Override
    public void run() {

        double sleepTime =
                MINUTE_IN_MILLIS * rotationStep / (FULL_CIRCLE * rotationSpeed);
        long millis = (long) sleepTime;
        int nanos = (int) ((sleepTime - millis) *
                MILLIS_IN_NANOS);

        while (!Thread.currentThread().isInterrupted()) {
            try {
                planeEquipmentSynchronizer.rotate(this);

                Thread.sleep(millis, nanos);
            } catch (InterruptedException interruptedException) {
                Thread.currentThread().interrupt();
            }
        }

        planeWorkTimeSynchronizer.countDown();
    }
}

