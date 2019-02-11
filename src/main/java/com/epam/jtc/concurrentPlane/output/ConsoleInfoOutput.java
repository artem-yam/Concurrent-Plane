package com.epam.jtc.concurrentPlane.output;

public class ConsoleInfoOutput implements InfoOutput {

    private static final String SHOT = "%d gun  shot! Synchronizer: %s \n";
    private static final String SHOOTING_BLOCKED = "%d gun Shooting blocked \n";
    private static final String SHOOTING_ALLOWED = "%d gun Can shoot \n";
    private static final String BLADE_POSITION = "Blade %d in position = %f\n";

    @Override
    public void showShot(int gunIndex, String qwe) {
        System.out.printf(SHOT, gunIndex, qwe);
        if (Integer.parseInt(qwe) > 0) {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public void showCanShoot(int gunIndex, boolean canShoot) {
        if (canShoot) {
            System.out.printf(SHOOTING_ALLOWED, gunIndex);
        } else {
            System.out.printf(SHOOTING_BLOCKED, gunIndex);
        }
    }

    @Override
    public void showPropellerBladesPositions(double... blades) {
        for (int i = 0; i < blades.length; i++) {
            System.out.format(BLADE_POSITION, i + 1, blades[i]);
        }
    }

}
