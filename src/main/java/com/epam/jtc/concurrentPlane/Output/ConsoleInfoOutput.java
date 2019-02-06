package com.epam.jtc.concurrentPlane.Output;

public class ConsoleInfoOutput implements InfoOutput {

    private final static String SHOT = "%d gun  shot! %d  Synchronizer: %s \n";
    private final static String SHOOTING_BLOCKED = "%d gun Shooting blocked \n";
    private final static String SHOOTING_ALLOWED = "%d gun Shooting allowed \n";
    private final static String BLADE_POSITION = "Blade %d in position = %f\n";
    private int shotsCount = 1;

    @Override
    public void showShot(int gunIndex, String qwe) {
        System.out.printf(SHOT, gunIndex, shotsCount++, qwe);
        if (Integer.parseInt(qwe) > 0) {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public void showCanShoot(int gunIndex, boolean canShoot) {
        if (canShoot) {
            System.out.printf(SHOOTING_ALLOWED, gunIndex);
        } else {
            System.err.printf(SHOOTING_BLOCKED, gunIndex);
        }
    }

    @Override
    public void showPropellerBladesPositions(double... blades) {
        for (int i = 0; i < blades.length; i++) {
            System.out.format(BLADE_POSITION, i + 1, blades[i]);
        }
    }

}
