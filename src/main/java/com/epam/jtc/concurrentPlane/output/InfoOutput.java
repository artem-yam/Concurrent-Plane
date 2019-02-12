package com.epam.jtc.concurrentPlane.output;

public interface InfoOutput {

    void showShot(int gunIndex, boolean canShoot);

    void showCanShoot(int gunIndex, boolean canShoot);

    void showGunsCountExcess(int enteredCount, int maxCount);

    void showPropellerBladesCountExcess(int enteredCount, int maxCount);

    void showPropellerBladesPositions(double... blades);

}
