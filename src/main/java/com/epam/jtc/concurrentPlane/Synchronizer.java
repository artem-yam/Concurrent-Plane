package com.epam.jtc.concurrentPlane;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.ReentrantReadWriteLock;

class Synchronizer {

    private ReentrantReadWriteLock lock;

    private Propeller propeller;
    private List<MachineGun> guns;
    private List<CountDownLatch> sync = new ArrayList<>();

    public Synchronizer(Propeller propeller, List<MachineGun> guns) {
        this.guns = guns;
        this.propeller = propeller;

        this.lock = new ReentrantReadWriteLock();

        for (int i = 0; i < guns.size(); i++) {
            sync.add(new CountDownLatch(0));
        }
    }

    public void setPropeller(Propeller propeller) {
        this.propeller = propeller;
    }

    public List<MachineGun> getGuns() {
        return guns;
    }

    public ReentrantReadWriteLock getLock() {
        return lock;
    }

    /* public List<CountDownLatch> getSync() {
        return sync;
    }

    public void decreaseLatch(int latchIndex) {
        sync.get(latchIndex).countDown();
    }*/

    public void resetLatch(int latchIndex) {
        sync.set(latchIndex, new CountDownLatch(1));
    }

    public void awaitLatch(int latchIndex) {
        try {
            sync.get(latchIndex).await();

            lock.readLock().lock();
        } catch (InterruptedException interruptedException) {
            Thread.currentThread().interrupt();
        }
    }

    public void getRotationAccess() {
        lock.writeLock().lock();
    }

    public void stopRotation() {
        checkGunsBlock();

        lock.writeLock().unlock();
    }

    private void checkGunsBlock() {
        for (MachineGun gun : guns) {
            int gunIndex = guns.indexOf(gun);
            if (sync.get(gunIndex).getCount() > 0 &&
                    (!isGunShotBlocked(gun.getPositionRelativeToPropeller()))) {
                sync.get(gunIndex).countDown();
            }
        }
    }

    public boolean canShoot(int gunPosition) {
        return !isGunShotBlocked(gunPosition) &&
                lock.readLock().tryLock();
    }

    public void shotLock(int gunPosition) {

    /*    boolean successfulLock = false;

        while (!successfulLock) {
            if (Thread.currentThread().isInterrupted()) {
                return;
            }

            lock.readLock().lock();

            try {

                if (!isGunShotBlocked(gunPosition)) {
                    successfulLock = true;
                } else {
                    // lock.readLock().unlock();
                }
            } finally {
                lock.readLock().unlock();
            }
        }

        lock.readLock().lock();
*/
        boolean canShoot = false;

        while (!canShoot) {
            if (Thread.currentThread().isInterrupted()) {
                return;
            }
            canShoot = canShoot(gunPosition);
        }

        // lock.readLock().lock();
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
