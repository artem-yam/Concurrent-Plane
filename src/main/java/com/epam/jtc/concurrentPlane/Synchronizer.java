package com.epam.jtc.concurrentPlane;

import com.epam.jtc.concurrentPlane.output.InfoOutput;

import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;

class Synchronizer {

    private ReentrantReadWriteLock lock;

    //private Propeller propeller;
    private List<MachineGun> guns;
    private InfoOutput infoOutput;

    public Synchronizer(ReentrantReadWriteLock lock, List<MachineGun> guns,
                        InfoOutput infoOutput/*, Propeller propeller*/) {
        this.lock = lock;
        this.guns = guns;
        this.infoOutput = infoOutput;
        // this.propeller = propeller;
    }

    public InfoOutput getInfoOutput() {
        return infoOutput;
    }

    /*public Propeller getPropeller() {
        return propeller;
    }

    public void setPropeller(Propeller propeller) {
        this.propeller = propeller;
    }*/

    public List<MachineGun> getGuns() {
        return guns;
    }

    /*public void updateGunBlocked(MachineGun gun) {
        gun.setBlocked(propeller
                .isGunShotBlocked(gun));
    }*/

   /* public void updateGunsBlocked() {
        for (MachineGun gun : guns) {
            gun.setBlocked(propeller.isGunShotBlocked(gun));

            infoOutput.showCanShoot(guns.indexOf(gun), gun.isBlocked());
        }
    }*/

  /*  public boolean canRotate() {
        return lock.writeLock().tryLock();
    }*/

    public void getRotationAccess() {
        lock.writeLock().lock();
    }

    public void stopRotation() {
        lock.writeLock().unlock();
    }

    public boolean canShoot(MachineGun gun) {
        //gun.setBlocked(propeller.isGunShotBlocked(gun));
        return !gun.isBlocked() && lock.readLock().tryLock();
    }


    public void getShootingAccess() {
        lock.readLock().lock();
    }

    public void stopShooting() {
        lock.readLock().unlock();
    }

}
