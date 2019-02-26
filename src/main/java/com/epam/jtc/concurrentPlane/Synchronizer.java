package com.epam.jtc.concurrentPlane;

import com.epam.jtc.concurrentPlane.output.InfoOutput;

import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;

class Synchronizer {

    private static final int SHOT_DURATION = 10;

    private ReentrantReadWriteLock lock;
    private List<MachineGun> guns;
    private InfoOutput infoOutput;

    public Synchronizer(ReentrantReadWriteLock lock, List<MachineGun> guns,
                        InfoOutput infoOutput) {
        this.lock = lock;
        this.guns = guns;
        this.infoOutput = infoOutput;
    }

    public InfoOutput getInfoOutput() {
        return infoOutput;
    }

    public void rotate(Propeller propeller) {
        lock.writeLock().lock();
        try {
            propeller.updateBladesPosition();

            checkGunsShotOpportunity(propeller);
        } finally {
            lock.writeLock().unlock();
        }
    }

    private void checkGunsShotOpportunity(Propeller propeller) {
        for (MachineGun gun : guns) {
            gun.setCanShoot(propeller.checkGunShotAbility(gun));
            infoOutput.showCanShoot(guns.indexOf(gun), gun.isCanShoot());
        }
    }

    public void tryToShoot(MachineGun gun) {

        while (!gun.isCanShoot() || !lock.readLock().tryLock()) {
            if (Thread.currentThread().isInterrupted()) {
                return;
            }
        }
        try {
            infoOutput.showShot(guns.indexOf(gun), gun.isCanShoot());

            try {
                Thread.sleep(SHOT_DURATION);
            } catch (InterruptedException interruptedException) {
                Thread.currentThread().interrupt();
            }
        } finally {
            lock.readLock().unlock();
        }
    }
}
