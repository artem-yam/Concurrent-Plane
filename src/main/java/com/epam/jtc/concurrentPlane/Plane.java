package com.epam.jtc.concurrentPlane;

import com.epam.jtc.concurrentPlane.Output.ConsoleInfoOutput;
import com.epam.jtc.concurrentPlane.Output.InfoOutput;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Plane implements Runnable {

    private static final int APPLICATION_WORK_TIME = 1000;
    private static final int DEFAULT_PROPELLER_ROTATION_SPEED = 1200;
    private static final int DEFAULT_PROPELLER_BLADES_COUNT = 5;
    private static final int DEFAULT_PROPELLER_BLADE_WIDTH = 20;
    private static final int DEFAULT_FIRE_RATE = 1500;
    private InfoOutput infoOutput = new ConsoleInfoOutput();

    private volatile CountDownLatch synchronizer = new CountDownLatch(1);
    private volatile Lock lock = new ReentrantLock();

    private Propeller propeller;
    private MachineGun machineGun;


    public Plane(int propellerRotationSpeed, int propellerBladesCount,
                 int propellerBladesWidth, int gunFireRate) {

        propeller = new Propeller(propellerRotationSpeed, propellerBladesCount,
                propellerBladesWidth, this);
        machineGun = new MachineGun(gunFireRate, this);


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

    CountDownLatch getSynchronizer() {
        return synchronizer;
    }

    void resetSynchronizer() {
        synchronizer = new CountDownLatch(1);
    }

    Lock getLock() {
        return lock;
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
            Thread.currentThread().interrupt();
        } finally {
            propellerThread.interrupt();
            machineGunThread.interrupt();
        }

    }
}
