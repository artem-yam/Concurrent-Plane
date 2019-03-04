package com.epam.jtc.concurrentPlane.output;

public interface InfoOutput {

    void showShot(int gunIndex, boolean isBlocked);

    void showShootingStop(int gunIndex);

    void showCanShoot(int gunIndex, boolean canShoot);

    void showWrongGunsCountWarning(int enteredCount, int maxCount);

    void showPropellerBladesCountExcess(int enteredCount, int maxCount);

    void showZeroGunsWarning();

    void showRotation();

    void showRotationStop();
}
