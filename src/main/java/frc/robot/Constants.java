package frc.robot;

public final class Constants {
    //Make classes for each subsystem
    //CAN does not care that there is a gap in the IDs, but it will care when the bus is not chained correctly
    public static final class IntakeConstants {
        //CAN IDs
        public static final int intakePivotID = 23;
        public static final int intakeGrabID = 20;
        public static final int intakePivotEncoderID = 21;
        public static final double intakePivotEncoderMagneticOffset = 0;
        //Speeds
        public static final double intakeSpeed = -0.85;
    }

    public static final class ShooterConstants {
        //CAN IDs
        public static final int shooterFeedID = 14;
        public static final int shooterSecondFeedID = 15;
        public static final int shooterShooterID = 16;
        public static final int shooterShooterFollowingID = 17;
        public static final int shooterAgitatorID = 22;
        //Speeds
        public static final double shooterSpeed = -0.55; //Was -0.7
        public static final double shooterReverseSpeed = 0.55;
        public static final double shooterFeedSpeed = -0.75;
        public static final double agitatorSpeed = -0.75;
    }

    public static final class ClimbConstants { //Ignore these
        //CAN IDs
        public static final int winchID = 24;
        public static final int armID = 19;
        public static final int armEncoderID = 18;
        public static final double armEncoderMagneticOffset = 0;
        //Speeds
        public static final double winchMotorSpeed = 1.0;
    }
}