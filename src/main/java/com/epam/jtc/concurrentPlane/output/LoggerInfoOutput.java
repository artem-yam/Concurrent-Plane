package com.epam.jtc.concurrentPlane.output;

import org.apache.log4j.Logger;

public class LoggerInfoOutput implements InfoOutput {

    private static final String SHOT = "%s start shooting!";
    private static final String STOP_SHOOTING = "%s stop shooting!";
    private static final String SHOOTING_BLOCKED =
            "%s can't shoot. It blocked.";
    private static final String SHOOTING_ALLOWED = "%s can shoot ";
    private static final String PROPELLER_BLADES_COUNT_EXCESS =
            "Propeller blades count excess: %d. " +
                    "Propeller blades count will be set to %d.";
    private static final String WRONG_GUNS_COUNT =
            "Wrong guns count: %d. Guns count will be set to %d.";
    private static final String UNEXPECTED_SHOT =
            "Something went wrong! %s gun shot, but it wasn't enabled!";
    private static final String ZERO_GUNS = "Warning! No guns installed!";
    private static final String ROTATION = "Propeller start rotating!";
    private static final String ROTATION_STOP = "Propeller rotation stopped!";
    private static final String GUN_WANT_TO_SHOOT = "%s want to shoot.";
    private static final String PLANE_STOP = "Plane finished work!";

    private final Logger logger = Logger.getLogger(this.getClass());

    @Override
    public void showShot(boolean isBlocked) {
        if (!isBlocked) {
            logger.info(String.format(SHOT, Thread.currentThread().getName()));
        } else {
            logger.error(String.format(UNEXPECTED_SHOT,
                    Thread.currentThread().getName()));
        }
    }

    @Override
    public void showShootingStop() {
        logger.info(
                String.format(STOP_SHOOTING, Thread.currentThread().getName()));
    }

    @Override
    public void showCanShoot(boolean canShoot) {
        if (canShoot) {
            logger.info(String.format(SHOOTING_ALLOWED,
                    Thread.currentThread().getName()));
        } else {
            logger.info(String.format(SHOOTING_BLOCKED,
                    Thread.currentThread().getName()));
        }
    }

    @Override
    public void showGunWantToShoot() {
        logger.info(String.format(GUN_WANT_TO_SHOOT,
                Thread.currentThread().getName()));
    }

    @Override
    public void showWrongGunsCountWarning(int enteredCount, int newCount) {
        logger.warn(String.format(WRONG_GUNS_COUNT, enteredCount, newCount));
    }

    @Override
    public void showPropellerBladesCountExcess(int enteredCount, int maxCount) {
        logger.warn(String.format(PROPELLER_BLADES_COUNT_EXCESS, enteredCount,
                maxCount));
    }

    @Override
    public void showZeroGunsWarning() {
        logger.warn(ZERO_GUNS);
    }

    @Override
    public void showRotation() {
        logger.info(ROTATION);
    }

    @Override
    public void showRotationStop() {
        logger.info(ROTATION_STOP);
    }

    @Override
    public void showPlaneStop() {
        logger.info(PLANE_STOP);
    }
}
