package com.epam.jtc.concurrentPlane;

public class Propeller implements Runnable {

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

    private double[] updateBladesPosition(double[] bladesPositions,
                                          double rotationStep) {
        for (int i = 0; i < bladesPositions.length; i++) {

            bladesPositions[i] += rotationStep;

            if (bladesPositions[i] >= 360) {
                bladesPositions[i] -= 360;
            }

        }

        return bladesPositions;
    }


    private boolean checkMachineGunShotOpportunity(MachineGun gun,
                                                   double[] bladesPositions) {
        boolean canShoot = true;

        for (double blade : bladesPositions) {
            if (gun.getPositionRelativeToPropeller() >= blade &&
                    gun.getPositionRelativeToPropeller() <=
                            blade + bladesWidth) {
                plane.resetSynchronizer(plane.getSynchronizers()
                        .get(plane.getMachineGuns().indexOf(gun)));

                plane.getInfoOutput().showCanShoot(plane.getMachineGuns().indexOf(gun), false);

                canShoot = false;
            }
        }

        if (canShoot) {
            plane.getSynchronizers()
                    .get(plane.getMachineGuns().indexOf(gun)).countDown();

            plane.getInfoOutput().showCanShoot(plane.getMachineGuns().indexOf(gun), true);

        }

        return canShoot;
    }

/*    private boolean checkShotOpportunityChange(MachineGun gun,
                                               double rotationUntilNextBlock) {
        boolean shotOpportunityChange;


        if (rotationUntilNextBlock <= 0) {

            plane.resetSynchronizer(plane.getSynchronizers()
                    .get(plane.getMachineGuns().indexOf(gun)));

            plane.getInfoOutput().showCanShoot(false);

            shotOpportunityChange = true;
        } else if (rotationUntilNextBlock <= distanceBetweenBlades &&
                plane.getSynchronizers()
                        .get(plane.getMachineGuns().indexOf(gun)).getCount() >
                        0) {
            plane.getSynchronizers()
                    .get(plane.getMachineGuns().indexOf(gun)).countDown();

            plane.getInfoOutput().showCanShoot(true);

            shotOpportunityChange = true;
        } else {
            shotOpportunityChange = false;
        }


        return shotOpportunityChange;
    }*/

    @Override
    public void run() {
        double rotationStep = (double) bladesWidth / 2;
        double rotationUntilNextBlock = distanceBetweenBlades + bladesWidth;


        double[] bladesPositions = getBladesStartPositions(
                rotationUntilNextBlock);


        double sleepTime = 1000 / ((double) rotationSpeed / rotationStep * 6);
        long millis = (long) sleepTime;
        int nanos = (int) ((sleepTime - millis) * 1000000);


        //plane.getInfoOutput().showPropellerBladesPositions(bladesPositions);
        //plane.getInfoOutput().showCanShoot(false);

        for (MachineGun gun : plane.getMachineGuns()) {

            checkMachineGunShotOpportunity(gun, bladesPositions);

        }


        while (!Thread.currentThread()
                .isInterrupted()) {
            try {

                updateBladesPosition(bladesPositions,
                        rotationStep);

                rotationUntilNextBlock -= rotationStep;

                // plane.getInfoOutput().showPropellerBladesPositions(
                //          bladesPositions);

                plane.getLock().lock();
                try {


                    for (MachineGun gun : plane.getMachineGuns()) {

                        checkMachineGunShotOpportunity(gun, bladesPositions);

                    /*    if (checkShotOpportunityChange(gun, bladesPositions,
                                rotationUntilNextBlock) &&
                                rotationUntilNextBlock <= 0) {
                            rotationUntilNextBlock +=
                                    distanceBetweenBlades + bladesWidth;
                        }*/

                    }

                } finally {
                    plane.getLock().unlock();
                }

                Thread.sleep(millis, nanos);
            } catch (InterruptedException interruptedException) {
                Thread.currentThread().interrupt();
            }
        }
    }
}

