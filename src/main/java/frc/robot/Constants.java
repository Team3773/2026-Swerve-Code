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
        public static final double intakeSpeed = 0.80;

        // Setpoints
        public static final double intakeDownSetpoint = -0.32;
        public static final double intakeUpSetpoint = -0.07;

        // PID Values
        // !!!!!!DON'T MESS AROUND WITH THE VALUES UNLESS IT IS NOT REACHING THE SETPOINTS!!!!!!
        // IF IT ISN'T GETTING TO THE DOWN SETPOINT, INCREASE THE kD VALUE!
        public static final double intakePivot_kP = 16.0;
        public static final double intakePivot_kI = 0.0;
        public static final double intakePivot_kD = 0.1;
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

        public static final double shooterFastSpeed = -0.7;
        public static final double shooterFastReverseSpeed = 0.7;

        public static final double shooterMaxSpeed = -1.0;
        public static final double shooterMaxReverseSpeed = 1.0;

        public static final double shooterFeedSpeed = -0.75;
        public static final double agitatorSpeed = -0.75;
    }
} //Climb used to have IDs 18,19