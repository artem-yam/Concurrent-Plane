package com.epam.jtc.concurrentPlane;

import static com.epam.jtc.concurrentPlane.Plane.LOGGER;

public class Propeller implements Runnable {

    private final static String SHOOTING_BLOCKED = "Shooting blocked";
    private final static String SHOOTING_ALLOWED = "Shooting allowed";

    private final int bladesCount;
    private final int bladesWidth;
    private final double distanceBetweenBlades;
    private int rotationSpeed;
    private Plane plane;

    Propeller(int propellerRotationSpeed, int propellerBladesCount,
            int propellerBladeWidth, Plane plane) {
        this.rotationSpeed = propellerRotationSpeed;

        if (propellerBladesCount * propellerBladeWidth >= 180) {
            this.bladesCount = 180 / propellerBladeWidth;
        } else {
            this.bladesCount = propellerBladesCount;
        }

        this.bladesWidth = propellerBladeWidth;

        this.distanceBetweenBlades = (double) 360 / bladesCount - bladesWidth;
        this.plane = plane;
    }


    private double[] getBladesStartPositions(double rotationUntilNextBlade) {
        double[] bladesPositions = new double[bladesCount];
        for (int i = 0; i < bladesPositions.length; i++) {
            bladesPositions[i] = rotationUntilNextBlade * i;
        }

        return bladesPositions;
    }

    private double updateBladesPosition(double[] bladesPosition,
            double rotationStep, double rotationUntilNextBlock) {
        for (int i = 0; i < bladesPosition.length; i++) {

            bladesPosition[i] += rotationStep;

            if (bladesPosition[i] >= 360) {
                bladesPosition[i] -= 360;
            }

        }

        return rotationUntilNextBlock - rotationStep;
    }


    private double checkShotOpportunityChange(double rotationUntilNextBlock) {
        if (rotationUntilNextBlock <= 0) {
            rotationUntilNextBlock += distanceBetweenBlades + bladesWidth;

            plane.resetSynchronizer();

            LOGGER.info(SHOOTING_BLOCKED);
            //  plane.getInfoOutput().showCanShoot(false);
        } else if (rotationUntilNextBlock <= distanceBetweenBlades &&
                plane.getSynchronizer().getCount() > 0) {
            plane.getSynchronizer().countDown();


            LOGGER.info(SHOOTING_ALLOWED);
            // plane.getInfoOutput().showCanShoot(true);
        }


        return rotationUntilNextBlock;
    }

    @Override
    public void run() {
        double rotationStep = (double) bladesWidth / 2;
        double rotationUntilNextBlock = distanceBetweenBlades + bladesWidth;


        double[] bladesPositions = getBladesStartPositions(
                rotationUntilNextBlock);


        double sleepTime = 1000 / ((double) rotationSpeed / rotationStep * 6);
        long millis = (long) sleepTime;
        int nanos = (int) ((sleepTime - millis) * 1000000);


        // plane.getInfoOutput().showPropellerBladesPositions(bladesPositions);
        LOGGER.info(SHOOTING_BLOCKED);
        //  plane.getInfoOutput().showCanShoot(false);


        while (!Thread.currentThread()
                      .isInterrupted()/*plane.isPropellerActive()*/) {
            try {

                rotationUntilNextBlock = updateBladesPosition(bladesPositions,
                        rotationStep, rotationUntilNextBlock);

                //   plane.getInfoOutput().showPropellerBladesPositions(
                //            bladesPositions);

                rotationUntilNextBlock = checkShotOpportunityChange(
                        rotationUntilNextBlock);

                Thread.sleep(millis, nanos);
            } catch (InterruptedException e) {
                //TODO
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
        }
    }
}

