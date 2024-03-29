package com.epam.jtc.concurrentPlane;

import com.epam.jtc.concurrentPlane.output.InfoOutput;
import com.epam.jtc.concurrentPlane.output.LoggerInfoOutput;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class Plane implements Runnable {

    private static final int FULL_CIRCLE = 360;
    private static final int ZERO = 0;
    private static final String GUN_THREAD_NAME = "%d %s";

    private static final int PROPELLER_ROTATION_SPEED = 1200;
    private static final int PROPELLER_BLADES_COUNT = 5;
    private static final int PROPELLER_BLADE_WIDTH = 20;
    private static final int FIRE_RATE = 1500;
    private static final int GUNS_COUNT = 3;

    private static final int APPLICATION_WORK_TIME = 1000;
    private static final int GUNS_MAX_COUNT = 6;

    private Propeller propeller;
    private List<MachineGun> machineGuns;
    private volatile CountDownLatch workTimeSynchronizer;
    private InfoOutput infoOutput = new LoggerInfoOutput();

    public Plane(int propellerRotationSpeed, int propellerBladesCount,
                 int propellerBladesWidth, int gunsCount, int gunsFireRate) {

        if (gunsCount <= GUNS_MAX_COUNT) {
            if (gunsCount <= ZERO) {
                gunsCount = ZERO;
                infoOutput.showZeroGunsWarning();
            }
        } else {
            infoOutput.showWrongGunsCountWarning(gunsCount, GUNS_MAX_COUNT);
            gunsCount = GUNS_MAX_COUNT;
        }

        machineGuns = new ArrayList<>(gunsCount);

        workTimeSynchronizer = new CountDownLatch(gunsCount + 1);

        Synchronizer equipmentSynchronizer = new Synchronizer(infoOutput);

        for (int i = 0; i < gunsCount; i++) {
            machineGuns.add(new MachineGun(gunsFireRate, equipmentSynchronizer,
                    i * FULL_CIRCLE / gunsCount,
                    workTimeSynchronizer, infoOutput));
        }

        propeller = new Propeller(propellerRotationSpeed, propellerBladesCount,
                propellerBladesWidth, equipmentSynchronizer,
                workTimeSynchronizer, infoOutput);

        propeller.setPlaneEquipmentSynchronizer(equipmentSynchronizer);
        for (MachineGun gun : machineGuns) {
            gun.setPlaneEquipmentSynchronizer(equipmentSynchronizer);
        }
    }

    public static void main(String[] args) {
        new Plane(PROPELLER_ROTATION_SPEED, PROPELLER_BLADES_COUNT,
                PROPELLER_BLADE_WIDTH, GUNS_COUNT, FIRE_RATE).launch();
    }

    public void launch() {
        new Thread(this).start();
    }

    @Override
    public void run() {

        Thread propellerThread = new Thread(propeller);
        propellerThread.setName(propeller.getClass().getSimpleName());

        List<Thread> machineGunThreads = new ArrayList<>(machineGuns.size());

        for (MachineGun gun : machineGuns) {
            Thread gunsThread = new Thread(gun);
            gunsThread.setName(
                    String.format(GUN_THREAD_NAME, machineGuns.indexOf(gun),
                            gun.getClass().getSimpleName()));

            machineGunThreads.add(gunsThread);
            gunsThread.start();
        }

        propellerThread.start();

        try {
            Thread.sleep(APPLICATION_WORK_TIME);

            propellerThread.interrupt();
            for (Thread gunThread : machineGunThreads) {
                gunThread.interrupt();
            }

            workTimeSynchronizer.await();

            infoOutput.showPlaneStop();
        } catch (InterruptedException interruptedException) {
            Thread.currentThread().interrupt();
        }
    }
}
