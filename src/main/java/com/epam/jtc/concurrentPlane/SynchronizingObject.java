package com.epam.jtc.concurrentPlane;

import com.epam.jtc.concurrentPlane.output.InfoOutput;

import java.util.List;
import java.util.concurrent.locks.Lock;

public class SynchronizingObject {

    private Lock lock;
    private List<MachineGun> guns;
    private InfoOutput infoOutput;


    public SynchronizingObject(Lock lock, List<MachineGun> guns,
                               InfoOutput infoOutput) {
        this.lock = lock;
        this.guns = guns;
        this.infoOutput = infoOutput;
    }


    public Lock getLock() {
        return lock;
    }

    public List<MachineGun> getGuns() {
        return guns;
    }

    public InfoOutput getInfoOutput() {
        return infoOutput;
    }

    public void checkMachineGunsShotOpportunity(double[] bladesPositions,
                                                int bladesWidth) {
        lock.lock();

        try {

            for (MachineGun gun : guns) {

                boolean canShoot = true;

                for (double blade : bladesPositions) {
                    if (gun.getPositionRelativeToPropeller() >= blade &&
                            gun.getPositionRelativeToPropeller() <=
                                    blade + bladesWidth) {

                        gun.setCanShoot(false);

                        canShoot = gun.isCanShoot();

                        break;
                    }
                }

                if (canShoot) {
                    gun.setCanShoot(true);
                }

                infoOutput.showCanShoot(
                        guns.indexOf(gun), gun.isCanShoot());
            }

        } finally {
            lock.unlock();
        }

    }

    public void tryToShoot(MachineGun gun) {

       /* while (!gun.isCanShoot() ||
                !lock.tryLock()) {
        }*/

        lock.lock();
        try {

            infoOutput.showShot(
                    guns.indexOf(gun),
                    String.valueOf(gun.isCanShoot()));

        } finally {
            lock.unlock();
        }

    }
}
