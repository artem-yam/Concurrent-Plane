package com.epam.jtc.concurrentPlane;

import com.epam.jtc.concurrentPlane.output.InfoOutput;

import java.util.List;
import java.util.concurrent.locks.Lock;

class SynchronizingObject {

    private Lock lock;
    private List<MachineGun> guns;
    private InfoOutput infoOutput;


    SynchronizingObject(Lock lock, List<MachineGun> guns,
                        InfoOutput infoOutput) {
        this.lock = lock;
        this.guns = guns;
        this.infoOutput = infoOutput;
    }

    InfoOutput getInfoOutput() {
        return infoOutput;
    }

    void checkMachineGunsShotOpportunity(double[] bladesPositions,
                                         int bladesWidth) {

        lock.lock();

        try {

            for (MachineGun gun : guns) {


                boolean canShoot = true;

                for (double blade : bladesPositions) {
                    if (gun.getPositionRelativeToPropeller() >= blade &&
                            gun.getPositionRelativeToPropeller() <=
                                    blade + bladesWidth) {

                        canShoot = false;

                        break;
                    }
                }

                gun.setCanShoot(canShoot);

                infoOutput.showCanShoot(guns.indexOf(gun), gun.isCanShoot());


            }
        } finally {
            lock.unlock();
        }

    }

    void tryToShoot(MachineGun gun) {

        while (!gun.isCanShoot() || !lock.tryLock()) {
            if (Thread.currentThread().isInterrupted()) {
                return;
            }
        }


        try {

            infoOutput.showShot(guns.indexOf(gun), gun.isCanShoot());

        } finally {
            lock.unlock();
        }

    }
}
