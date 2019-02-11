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

            if (newPositions[i] >= 360) {
                newPositions[i] -= 360;
            }

        }

        return newPositions;
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

                canShoot = false;

                break;
            }
        }

        if (canShoot) {
            plane.getSynchronizers()
                    .get(plane.getMachineGuns().indexOf(gun)).countDown();

        }

        return canShoot;
    }

    @Override
    public void run() {
        double rotationStep = (double) bladesWidth / 2;

        double[] bladesPositions = getBladesStartPositions();


        double sleepTime = 1000 / ((double) rotationSpeed / rotationStep * 6);
        long millis = (long) sleepTime;
        int nanos = (int) ((sleepTime - millis) * 1000000);


        while (!Thread.currentThread()
                .isInterrupted()) {
            try {
                /*plane.getInfoOutput()
                        .showPropellerBladesPositions(bladesPositions);*/

                plane.getLock().lock();
                try {

                    for (MachineGun gun : plane.getMachineGuns()) {


                        plane.getInfoOutput()
                                .showCanShoot(
                                        plane.getMachineGuns().indexOf(gun),
                                        checkMachineGunShotOpportunity(gun,
                                                bladesPositions));


                    }

                } finally {
                    plane.getLock().unlock();
                }


                bladesPositions = updateBladesPosition(bladesPositions,
                        rotationStep);


                Thread.sleep(millis, nanos);
            } catch (InterruptedException interruptedException) {
                Thread.currentThread().interrupt();
            }
        }
    }
}

