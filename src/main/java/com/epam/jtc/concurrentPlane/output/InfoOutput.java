package com.epam.jtc.concurrentPlane.output;

public interface InfoOutput {

    void showShot(boolean isBlocked);

    void showShootingStop();

    void showCanShoot(boolean canShoot);

    void showGunWantToShoot();

    void showWrongGunsCountWarning(int enteredCount, int maxCount);

    void showPropellerBladesCountExcess(int enteredCount, int maxCount);

    void showZeroGunsWarning();

    void showRotation();

    void showRotationStop();

    void showPlaneStop();
}
