package com.epam.jtc.concurrentPlane;


public class MachineGun implements Runnable {

    //private final static String SHOT = "Gun shot! ";

    private int fireRate;
    private Plane plane;

    MachineGun(int fireRate, Plane plane) {
        this.fireRate = fireRate;
        this.plane = plane;
    }

    @Override
    public void run() {
        double sleepTime = (double) 60000 / fireRate;
        long millis = (long) sleepTime;
        int nanos = (int) ((sleepTime - millis) * 1000000);

        while (!Thread.currentThread()
                .isInterrupted()/*plane.canMachineGunShoot()*/) {
            try {


                while (!plane.canMachineGunShoot()) {
                }

                plane.getSynchronizer().lock();
                try {
                    plane.getInfoOutput().showShot();
                    plane.setCanMachineGunShoot(false);

                    // plane.getSynchronizer().await();

                    //LOGGER.info(SHOT);


                /*plane.getInfoOutput().showShot();
                plane.setCanMachineGunShoot(false);

                plane.getSynchronizer().unlock();*/

                    Thread.sleep(millis, nanos);

                    plane.setCanMachineGunShoot(true);

                } finally {
                    plane.getSynchronizer().unlock();
                }
            } catch (InterruptedException interruptedException) {
                // e.printStackTrace();
                Thread.currentThread().interrupt();
            }
        }
    }
}
