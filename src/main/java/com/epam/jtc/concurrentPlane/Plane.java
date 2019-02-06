package com.epam.jtc.concurrentPlane;

import com.epam.jtc.concurrentPlane.Output.ConsoleInfoOutput;
import com.epam.jtc.concurrentPlane.Output.InfoOutput;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Plane implements Runnable {

    private static final int APPLICATION_WORK_TIME = 1000;
    private static final int DEFAULT_PROPELLER_ROTATION_SPEED = 1200;
    private static final int DEFAULT_PROPELLER_BLADES_COUNT = 5;
    private static final int DEFAULT_PROPELLER_BLADE_WIDTH = 20;
    private static final int DEFAULT_FIRE_RATE = 1500;
    private static Logger logger = Logger
            .getLogger(Plane.class);
    private InfoOutput infoOutput = new ConsoleInfoOutput();
    private volatile List<CountDownLatch> synchronizers = new ArrayList<>();
    private volatile Lock lock = new ReentrantLock();
    private Propeller propeller;
    private List<MachineGun> machineGuns = new ArrayList<>();


    public Plane(int propellerRotationSpeed, int propellerBladesCount,
                 int propellerBladesWidth, int gunsCount, int gunsFireRate) {

        propeller = new Propeller(propellerRotationSpeed, propellerBladesCount,
                propellerBladesWidth, this);

        for (int i = 0; i < gunsCount; i++) {
            synchronizers.add(new CountDownLatch(1));
            machineGuns.add(new MachineGun(gunsFireRate, this,
                    i * 360 / gunsCount));
        }


    }

    public static void main(String[] args) {

        Plane plane = new Plane(DEFAULT_PROPELLER_ROTATION_SPEED,
                DEFAULT_PROPELLER_BLADES_COUNT, DEFAULT_PROPELLER_BLADE_WIDTH,
                2,
                DEFAULT_FIRE_RATE);

        new Thread(plane).start();

    }

    InfoOutput getInfoOutput() {
        return infoOutput;
    }

    public List<MachineGun> getMachineGuns() {
        return machineGuns;
    }

    List<CountDownLatch> getSynchronizers() {
        return synchronizers;
    }

    void resetSynchronizer(CountDownLatch synchronizer) {
        synchronizers
                .set(synchronizers.indexOf(synchronizer),
                        new CountDownLatch(1));
    }

    Lock getLock() {
        return lock;
    }

    @Override
    public void run() {

        Thread propellerThread = new Thread(propeller);
        List<Thread> machineGunThreads = new ArrayList<>();

        for (MachineGun gun : machineGuns) {
            machineGunThreads.add(new Thread(gun));
        }

        propellerThread.start();

        for (Thread gunThread : machineGunThreads) {
            gunThread.start();
        }

        try {
            Thread.sleep(APPLICATION_WORK_TIME);
        } catch (InterruptedException interruptedException) {
            Thread.currentThread().interrupt();
        } finally {
            propellerThread.interrupt();
            for (Thread gunThread : machineGunThreads) {
                gunThread.interrupt();
            }
        }

    }
}
