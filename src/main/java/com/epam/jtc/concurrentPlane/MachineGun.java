package com.epam.jtc.concurrentPlane;


public class MachineGun implements Runnable {

    private final static String SHOT = "Gun shot! ";

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

        while (!Thread.currentThread().isInterrupted()) {
            try {

                while (plane.getSynchronizer().getCount() > 0 ||
                        !plane.getLock().tryLock()) {
                    //plane.getLock().tryLock();
                }

//                plane.getSynchronizer().await();
//                plane.getLock().lock();

                /*while (!plane.getLock().tryLock()) {
                }*/
                //   if (plane.getLock().tryLock()) {

                try {
                    //    plane.getSynchronizer().await();


                    plane.getInfoOutput().showShot(
                            String.valueOf(
                                    plane.getSynchronizer().getCount()));


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
