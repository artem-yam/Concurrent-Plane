package com.epam.jtc.concurrentPlane;

import com.epam.jtc.concurrentPlane.output.ConsoleInfoOutput;
import com.epam.jtc.concurrentPlane.output.InfoOutput;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Plane implements Runnable {


    private static final int DEFAULT_PROPELLER_ROTATION_SPEED = 1200;
    private static final int DEFAULT_PROPELLER_BLADES_COUNT = 5;
    private static final int DEFAULT_PROPELLER_BLADE_WIDTH = 20;
    private static final int DEFAULT_FIRE_RATE = 1500;
    private static final int DEFAULT_GUNS_COUNT = 3;


    private static final int APPLICATION_WORK_TIME = 1000;
    private static final int GUNS_MAX_COUNT = 6;
    private InfoOutput infoOutput = new ConsoleInfoOutput();
    //private List<Boolean> canGunsShoot = new ArrayList<>();
    private Lock lock = new ReentrantLock();
    private Propeller propeller;
    private List<MachineGun> machineGuns = new ArrayList<>();


    public Plane(int propellerRotationSpeed, int propellerBladesCount,
                 int propellerBladesWidth, int gunsCount, int gunsFireRate) {

        if (gunsCount > GUNS_MAX_COUNT) {
            infoOutput.showGunsCountExcess(gunsCount, GUNS_MAX_COUNT);
            gunsCount = GUNS_MAX_COUNT;
        }

        SynchronizingObject synchronizingObject =
                new SynchronizingObject(lock, machineGuns, infoOutput);


        for (int i = 0; i < gunsCount; i++) {
            //canGunsShoot.add(false);
            machineGuns.add(new MachineGun(gunsFireRate, synchronizingObject,
                    i * 360 / gunsCount));
        }


        propeller = new Propeller(propellerRotationSpeed, propellerBladesCount,
                propellerBladesWidth, synchronizingObject);

    }

    public static void main(String[] args) {

        Plane plane = new Plane(DEFAULT_PROPELLER_ROTATION_SPEED,
                DEFAULT_PROPELLER_BLADES_COUNT, DEFAULT_PROPELLER_BLADE_WIDTH,
                DEFAULT_GUNS_COUNT, DEFAULT_FIRE_RATE);

        new Thread(plane).start();

    }

    /*InfoOutput getInfoOutput() {
        return infoOutput;
    }

    List<MachineGun> getMachineGuns() {
        return machineGuns;
    }

    Lock getLock() {
        return lock;
    }*/

    @Override
    public void run() {

        try {
            Thread propellerThread = new Thread(propeller);
            propellerThread.setName("Propeller");


            List<Thread> machineGunThreads = new ArrayList<>();

            int i = 1;
            for (MachineGun gun : machineGuns) {

                Thread gunThread = new Thread(gun);
                gunThread.setName("Gun " + i++);

                machineGunThreads.add(gunThread);
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
        } finally {
            Thread.currentThread().interrupt();
        }

    }
}
