package com.epam.jtc.concurrentPlane.output;

public interface InfoOutput {

    void showShot(int gunIndex, boolean isBlocked);

    void showCanShoot(int gunIndex, boolean isBlocked);

    void showWrongGunsCountWarning(int enteredCount, int maxCount);

    void showPropellerBladesCountExcess(int enteredCount, int maxCount);

    void showZeroGunsWarning();
}
