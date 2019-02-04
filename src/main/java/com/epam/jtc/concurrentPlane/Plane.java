package com.epam.jtc.concurrentPlane;

import com.epam.jtc.concurrentPlane.Output.ConsoleInfoOutput;
import com.epam.jtc.concurrentPlane.Output.InfoOutput;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Plane implements Runnable {

    //public static final Logger LOGGER = Logger.getLogger(
    //        Logger.class.getName());
    private static final int DEFAULT_PROPELLER_ROTATION_SPEED = 1200;
    private static final int DEFAULT_PROPELLER_BLADES_COUNT = 5;
    private static final int DEFAULT_PROPELLER_BLADE_WIDTH = 20;
    private static final int DEFAULT_FIRE_RATE = 1500;
    private static final int APPLICATION_WORK_TIME = 1000;
    private InfoOutput infoOutput = new ConsoleInfoOutput();
    // private CountDownLatch synchronizer = new CountDownLatch(1);
    private Lock synchronizer = new ReentrantLock();

    //private boolean isPropellerActive = true;
    private volatile boolean canMachineGunShoot = true;

    private Propeller propeller;
    private MachineGun machineGun;


    public Plane(int propellerRotationSpeed, int propellerBladesCount,
                 int propellerBladesWidth, int gunFireRate) {

       /* Thread propeller = new Thread(
                new Propeller(propellerRotationSpeed, propellerBladesCount,
                        propellerBladesWidth, this));
        Thread gun = new Thread(new MachineGun(gunFireRate, this));*/

        synchronizer.lock();

        propeller = new Propeller(propellerRotationSpeed, propellerBladesCount,
                propellerBladesWidth, this);
        machineGun = new MachineGun(gunFireRate, this);


       /* Thread propeller = new Propeller(propellerRotationSpeed,
       propellerBladesCount,
                        propellerBladesWidth, this);
        Thread gun = new MachineGun(gunFireRate, this);*/

      /*  propeller.start();
        gun.start();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        propeller.interrupt();
        gun.interrupt();*/

    }

    public static void main(String[] args) {

        Plane plane = new Plane(DEFAULT_PROPELLER_ROTATION_SPEED,
                DEFAULT_PROPELLER_BLADES_COUNT, DEFAULT_PROPELLER_BLADE_WIDTH,
                DEFAULT_FIRE_RATE);

        new Thread(plane).start();

    }

    InfoOutput getInfoOutput() {
        return infoOutput;
    }

    /*boolean isPropellerActive() {
        return isPropellerActive;
    }

    public void setPropellerActive(boolean propellerActive) {
        isPropellerActive = propellerActive;
    }
    */

    boolean canMachineGunShoot() {
        return canMachineGunShoot;
    }

    public void setCanMachineGunShoot(boolean canMachineGunShoot) {
        this.canMachineGunShoot = canMachineGunShoot;
    }




 /*   CountDownLatch getSynchronizer() {
        return synchronizer;
    }

    void resetSynchronizer() {
        synchronizer = new CountDownLatch(1);
    }*/

    Lock getSynchronizer() {
        return synchronizer;
    }

    @Override
    public void run() {

        Thread propellerThread = new Thread(propeller);
        Thread machineGunThread = new Thread(machineGun);

        propellerThread.start();
        machineGunThread.start();

        try {
            Thread.sleep(APPLICATION_WORK_TIME);
        } catch (InterruptedException interruptedException) {
            //e.printStackTrace();
            Thread.currentThread().interrupt();
        } finally {
            propellerThread.interrupt();
            machineGunThread.interrupt();
            // isPropellerActive = false;
            //  canMachineGunShoot = false;
        }

    }
}
