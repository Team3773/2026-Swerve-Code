package frc.robot;

public final class Constants {
    //Make classes for each subsystem
    //TODO: put in the correct CAN IDs when we actually hook these up
    public static final class IntakeConstants {
        //CAN IDs
        public static final int intakePivotID = 20;
        public static final int intakeGrabID = 21;
        public static final int intakeSecondAgitatorID = 22;
    }

    public static final class ShooterConstants {
        //CAN IDs
        public static final int shooterFeedID = 23;
        public static final int shooterSecondFeedID = 24;
        public static final int shooterShooterID = 25;
        public static final int shooterShooterFollowingID = 26;
        //Speeds
        public static final double shooterSpeed = 1.0; //TODO: Change these to 1.0 when ready
        public static final double shooterReverseSpeed = -1.0;
    }

    public static final class ClimbConstants {
        //CAN IDs
        public static final int winchID = 27;
        //Speeds
        public static final double winchMotorSpeed = 0.5;
    }
}
