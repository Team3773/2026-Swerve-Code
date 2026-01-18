package frc.robot.subsystems;

import com.revrobotics.spark.SparkFlex;
import com.revrobotics.spark.SparkLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class FuelShooterSubsystem extends SubsystemBase {

    private SparkFlex upperMotor;
    private SparkFlex lowerMotor;

    public FuelShooterSubsystem() {

        upperMotor = new SparkFlex(Constants.ShooterConstants.shooterID, MotorType.kBrushless);
        lowerMotor = new SparkFlex(Constants.ShooterConstants.shooterID, MotorType.kBrushless);
        
    }

        public void runShooter(double speed) {
        upperMotor.set(speed);
        lowerMotor.set(speed);
    }

        public void stopShooter() {
        upperMotor.set(0);
        lowerMotor.set(0);
    
    }
}
