package com.epam.jtc.concurrentPlane;

import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;

class Synchronizer {

    private ReentrantReadWriteLock lock;

    private Propeller propeller;
    private List<MachineGun> guns;

    public Synchronizer(Propeller propeller, List<MachineGun> guns) {
        this.guns = guns;
        this.propeller = propeller;

        this.lock = new ReentrantReadWriteLock();
    }

    public void setPropeller(Propeller propeller) {
        this.propeller = propeller;
    }

    public List<MachineGun> getGuns() {
        return guns;
    }

    public void getRotationAccess() {
        lock.writeLock().lock();
    }

    public void stopRotation() {
        lock.writeLock().unlock();
    }

    public boolean canShoot(int gunPosition) {
        return !isGunShotBlocked(gunPosition) && lock.readLock().tryLock();
    }

    public void stopShooting() {
        lock.readLock().unlock();
    }

    public boolean isGunShotBlocked(int gunPosition) {
        boolean isBlocked = false;

        for (double blade : propeller.getBladesPositions()) {
            if (gunPosition >= blade &&
                    gunPosition <= blade + propeller.getBladesWidth()) {
                isBlocked = true;
                break;
            }

        }

        return isBlocked;
    }

}
