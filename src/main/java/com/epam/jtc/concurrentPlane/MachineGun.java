package com.epam.jtc.concurrentPlane;


public class MachineGun implements Runnable {

    private final static String SHOT = "Gun shot! ";

    private int fireRate;
    private int positionRelativeToPropeller;
    private Plane plane;

    MachineGun(int fireRate, Plane plane, int positionRelativeToPropeller) {
        this.fireRate = fireRate;
        this.plane = plane;
        this.positionRelativeToPropeller = positionRelativeToPropeller;
    }

    public int getPositionRelativeToPropeller() {
        return positionRelativeToPropeller;
    }

    @Override
    public void run() {
        double sleepTime = (double) 60000 / fireRate;
        long millis = (long) sleepTime;
        int nanos = (int) ((sleepTime - millis) * 1000000);

        while (!Thread.currentThread().isInterrupted()) {
            try {


                while (plane.getSynchronizers()
                        .get(plane.getMachineGuns().indexOf(this)).getCount() >
                        0 ||
                        !plane.getLock().tryLock()) {
                }

                try {


                    plane.getInfoOutput()
                            .showShot(plane.getMachineGuns().indexOf(this),
                                    String.valueOf(
                                            plane.getSynchronizers()
                                                    .get(plane.getMachineGuns()
                                                            .indexOf(this))
                                                    .getCount()));


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
