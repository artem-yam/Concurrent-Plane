package com.epam.jtc.concurrentPlane.output;

import org.apache.log4j.Logger;

public class LoggerInfoOutput implements InfoOutput {

    private static final String SHOT = "%d gun shooting!";
    private static final String STOP_SHOOTING = "%d gun stopped shooting!";
    private static final String SHOOTING_BLOCKED = "%d gun Shooting disabled ";
    private static final String SHOOTING_ALLOWED = "%d gun Can shoot ";
    private static final String PROPELLER_BLADES_COUNT_EXCESS =
            "Propeller blades count excess: %d. " +
                    "Propeller blades count will be set to %d.";
    private static final String WRONG_GUNS_COUNT =
            "Wrong guns count: %d. Guns count will be set to %d.";
    private static final String UNEXPECTED_SHOT =
            "Something went wrong! %d gun shot, but it wasn't enabled!";
    private static final String ZERO_GUNS = "Warning! No guns installed!";
    private static final String ROTATION = "Propeller start rotating!";
    private static final String ROTATION_STOP = "Propeller rotation stopped!";

    private final Logger logger = Logger.getLogger(this.getClass());

    @Override
    public void showShot(int gunIndex, boolean isBlocked) {
        if (!isBlocked) {
            logger.info(String.format(SHOT, gunIndex));
        } else {
            logger.error(String.format(UNEXPECTED_SHOT, gunIndex));
        }
    }

    @Override
    public void showShootingStop(int gunIndex) {
        logger.info(String.format(STOP_SHOOTING, gunIndex));
    }

    @Override
    public void showCanShoot(int gunIndex, boolean canShoot) {
        if (canShoot) {
            logger.info(String.format(SHOOTING_ALLOWED, gunIndex));
        } else {
            logger.info(String.format(SHOOTING_BLOCKED, gunIndex));
        }
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
}
