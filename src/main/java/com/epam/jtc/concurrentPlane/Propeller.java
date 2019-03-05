package com.epam.jtc.concurrentPlane;

import com.epam.jtc.concurrentPlane.output.InfoOutput;

import java.util.concurrent.CountDownLatch;

public class Propeller implements Runnable {

    private static final double MINUTE_IN_MILLIS = 60000;
    private static final double MILLIS_IN_NANOS = 1000000;

    private static final int FULL_CIRCLE = 360;
    private static final int HALF_CIRCLE = 360 / 2;
    private static final double ROTATION_STEP_MULTIPLIER = 0.5;
    private static final int ROTATION_DURATION = 10;

    private static final int MIN_BLADES_COUNT = 3;
    private static final int MIN_BLADE_WIDTH = 20;
    private static final int MIN_ROTATION_SPEED = 1000;

    private final int bladesCount;
    private final int bladesWidth;
    private final double distanceBetweenBlades;
    private int rotationSpeed;
    private Synchronizer planeEquipmentSynchronizer;
    private CountDownLatch planeWorkTimeSynchronizer;
    private InfoOutput infoOutput;

    private double rotationStep;
    private double[] bladesPositions;

    public Propeller(int propellerRotationSpeed, int propellerBladesCount,
                     int propellerBladeWidth,
                     Synchronizer planeEquipmentSynchronizer,
                     CountDownLatch planeWorkTimeSynchronizer,
                     InfoOutput infoOutput) {
        this.planeEquipmentSynchronizer = planeEquipmentSynchronizer;
        this.planeWorkTimeSynchronizer = planeWorkTimeSynchronizer;
        this.infoOutput = infoOutput;

        if (propellerRotationSpeed < MIN_ROTATION_SPEED) {
            propellerRotationSpeed = MIN_ROTATION_SPEED;
        }

        if (propellerBladesCount < MIN_BLADES_COUNT) {
            propellerBladesCount = MIN_BLADES_COUNT;
        }

        if (propellerBladeWidth < MIN_BLADE_WIDTH) {
            propellerBladeWidth = MIN_BLADE_WIDTH;
        }

        if (propellerBladesCount * propellerBladeWidth < HALF_CIRCLE) {
            this.bladesCount = propellerBladesCount;
        } else {
            this.bladesCount = HALF_CIRCLE / propellerBladeWidth;

            infoOutput.showPropellerBladesCountExcess(propellerBladesCount,
                    bladesCount);
        }

        this.bladesWidth = propellerBladeWidth;

        this.rotationSpeed = propellerRotationSpeed;

        this.distanceBetweenBlades =
                (double) FULL_CIRCLE / bladesCount - bladesWidth;

        rotationStep = bladesWidth * ROTATION_STEP_MULTIPLIER;
        getBladesStartPositions();

    }

    public void setPlaneEquipmentSynchronizer(
            Synchronizer planeEquipmentSynchronizer) {
        this.planeEquipmentSynchronizer = planeEquipmentSynchronizer;
    }

    public int getBladesWidth() {
        return bladesWidth;
    }

    public double[] getBladesPositions() {
        return bladesPositions;
    }

    private void getBladesStartPositions() {
        bladesPositions = new double[bladesCount];

        for (int i = 0; i < bladesPositions.length; i++) {
            bladesPositions[i] = (distanceBetweenBlades + bladesWidth) * i;
        }
    }

    private void rotate() {
        try {
            infoOutput.showRotation();

            updateBladesPositions();

            Thread.sleep(ROTATION_DURATION);

            infoOutput.showRotationStop();
        } catch (InterruptedException interruptedException) {
            Thread.currentThread().interrupt();
        }
    }

    private void updateBladesPositions() {
        for (int i = 0; i < bladesPositions.length; i++) {

            bladesPositions[i] = bladesPositions[i] + rotationStep;

            if (bladesPositions[i] >= FULL_CIRCLE) {
                bladesPositions[i] -= FULL_CIRCLE;
            }
        }
    }

    @Override
    public void run() {
        try {
            double sleepTime = MINUTE_IN_MILLIS * rotationStep /
                    (FULL_CIRCLE * rotationSpeed);
            long millis = (long) sleepTime;
            int nanos = (int) ((sleepTime - millis) * MILLIS_IN_NANOS);

            while (!Thread.currentThread().isInterrupted()) {
                try {
                    planeEquipmentSynchronizer.getRotationAccess();

                    try {
                        rotate();
                    } finally {
                        planeEquipmentSynchronizer.stopRotation();
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

