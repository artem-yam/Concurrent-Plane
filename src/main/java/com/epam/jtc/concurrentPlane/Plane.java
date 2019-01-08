package com.epam.jtc.concurrentPlane;

import com.epam.jtc.concurrentPlane.Output.ConsoleInfoOutput;
import com.epam.jtc.concurrentPlane.Output.InfoOutput;

import java.util.concurrent.CountDownLatch;

public class Plane {

    private static final int DEFAULT_PROPELLER_ROTATION_SPEED = 1200;
    private static final int DEFAULT_PROPELLER_BLADES_COUNT = 5;
    private static final int DEFAULT_PROPELLER_BLADE_WIDTH = 20;
    private static final int DEFAULT_FIRE_RATE = 1500;

    private InfoOutput infoOutput = new ConsoleInfoOutput();
    private CountDownLatch synchronizer = new CountDownLatch(1);
    private boolean isPropellerActive = true;
    private boolean canMachineGunShoot = true;


    public Plane(int propellerRotationSpeed, int propellerBladesCount,
            int propellerBladesWidth, int gunFireRate) {

        Thread engine = new Thread(
                new Propeller(propellerRotationSpeed, propellerBladesCount,
                        propellerBladesWidth, this));
        Thread machineGun = new Thread(new MachineGun(gunFireRate, this));

        engine.start();
        machineGun.start();

    }

    public static void main(String[] args) {

        Plane plane = new Plane(DEFAULT_PROPELLER_ROTATION_SPEED,
                DEFAULT_PROPELLER_BLADES_COUNT, DEFAULT_PROPELLER_BLADE_WIDTH,
                DEFAULT_FIRE_RATE);

    }

    public InfoOutput getInfoOutput() {
        return infoOutput;
    }

    boolean isPropellerActive() {
        return isPropellerActive;
    }

    public void setPropellerActive(boolean propellerActive) {
        isPropellerActive = propellerActive;
    }

    boolean canMachineGunShoot() {
        return canMachineGunShoot;
    }

    public void setCanMachineGunShoot(boolean canMachineGunShoot) {
        this.canMachineGunShoot = canMachineGunShoot;
    }

    public CountDownLatch getSynchronizer() {
        return synchronizer;
    }

    public void resetSynchronizer() {
        synchronizer = new CountDownLatch(1);
    }
}
