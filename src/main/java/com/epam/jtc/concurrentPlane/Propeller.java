package com.epam.jtc.concurrentPlane;

public class Propeller implements Runnable {


    private static final int FULL_CIRCLE = 360;
    private static final int HALF_CIRCLE = 360 / 2;

    private final int bladesCount;
    private final int bladesWidth;
    private final double distanceBetweenBlades;
    private int rotationSpeed;
    private SynchronizingObject synchronizingObject;

    Propeller(int propellerRotationSpeed, int propellerBladesCount,
              int propellerBladeWidth,
              SynchronizingObject synchronizingObject) {
        this.rotationSpeed = propellerRotationSpeed;

        if (propellerBladesCount * propellerBladeWidth >= HALF_CIRCLE) {
            this.bladesCount = HALF_CIRCLE / propellerBladeWidth;

            synchronizingObject.getInfoOutput().showPropellerBladesCountExcess(
                    propellerBladesCount, bladesCount);


        } else {
            this.bladesCount = propellerBladesCount;
        }

        this.bladesWidth = propellerBladeWidth;

        this.distanceBetweenBlades =
                (double) FULL_CIRCLE / bladesCount - bladesWidth;

        this.synchronizingObject = synchronizingObject;
    }


    private double[] getBladesStartPositions() {
        double[] bladesPositions = new double[bladesCount];
        for (int i = 0; i < bladesPositions.length; i++) {
            bladesPositions[i] = (distanceBetweenBlades + bladesWidth) * i;
        }

        return bladesPositions;
    }

    private double[] updateBladesPosition(double[] bladesPositions,
                                          double rotationStep) {
        double[] newPositions = new double[bladesPositions.length];

        for (int i = 0; i < bladesPositions.length; i++) {

            newPositions[i] = bladesPositions[i] + rotationStep;

            if (newPositions[i] >= FULL_CIRCLE) {
                newPositions[i] -= FULL_CIRCLE;
            }

        }

        return newPositions;
    }

    @Override
    public void run() {
        double rotationStep = (double) bladesWidth / 2;

        double[] bladesPositions = getBladesStartPositions();


        double sleepTime = 1000 / ((double) rotationSpeed / rotationStep * 6);
        long millis = (long) sleepTime;
        int nanos = (int) ((sleepTime - millis) * 1000000);


        while (!Thread.currentThread().isInterrupted()) {
            try {
                /*plane.getInfoOutput()
                        .showPropellerBladesPositions(bladesPositions);*/


                synchronizingObject.checkMachineGunsShotOpportunity(
                        bladesPositions, bladesWidth);


                bladesPositions = updateBladesPosition(bladesPositions,
                        rotationStep);


                Thread.sleep(millis, nanos);
            } catch (InterruptedException interruptedException) {
                Thread.currentThread().interrupt();
            }
        }
    }
}

