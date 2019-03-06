package com.epam.jtc.concurrentPlane;

import com.epam.jtc.concurrentPlane.output.InfoOutput;

import java.util.concurrent.locks.ReentrantReadWriteLock;

class Synchronizer {

    private ReentrantReadWriteLock lock;

    private InfoOutput infoOutput;

    private double bladesWidth;
    private double[] bladesPositions;

    public Synchronizer(
            InfoOutput infoOutput) {
        this.infoOutput = infoOutput;

        this.lock = new ReentrantReadWriteLock();
    }

    public InfoOutput getInfoOutput() {
        return infoOutput;
    }

    public void getRotationAccess() {
        lock.writeLock().lock();
    }

    public void setBlades(double bladesWidth, double[] bladesPositions) {
        this.bladesWidth = bladesWidth;
        this.bladesPositions = bladesPositions;
    }

    public void stopRotation(double[] bladesPositions) {
        this.bladesPositions = bladesPositions;

        lock.writeLock().unlock();
    }

    public void getShootingAccess(int gunPosition)
            throws InterruptedException {
        boolean canShoot = false;
        boolean cantShootMessageShowed = false;

        while (!canShoot) {

            if (Thread.currentThread().isInterrupted()) {
                throw new InterruptedException();
            }

            lock.readLock().lock();

            try {
                canShoot = !isGunShotBlocked(gunPosition);
            } finally {
                if (!canShoot) {
                    if (!cantShootMessageShowed) {
                        infoOutput.showCanShoot(false);
                        cantShootMessageShowed = true;
                    }
                    lock.readLock().unlock();
                }
            }
        }

        infoOutput.showCanShoot(true);
    }

    public void stopShooting() {
        lock.readLock().unlock();
    }

    public boolean isGunShotBlocked(int gunPosition) {
        boolean isBlocked = false;

        for (double blade : bladesPositions) {
            if (gunPosition >= blade &&
                    gunPosition <=
                            blade + bladesWidth) {
                isBlocked = true;
                break;
            }

        }

        return isBlocked;
    }

}
