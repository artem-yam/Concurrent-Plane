package com.epam.jtc.concurrentPlane.Output;

public interface InfoOutput {

    void showShot(int gunIndex, String qwe);

    void showCanShoot(int gunIndex, boolean canShoot);

    void showPropellerBladesPositions(double... blades);
    
}
