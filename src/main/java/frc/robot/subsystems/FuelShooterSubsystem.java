package frc.robot.subsystems;

import com.revrobotics.spark.SparkFlex;
import com.revrobotics.spark.SparkLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.Constants.ShooterConstants;

public class FuelShooterSubsystem extends SubsystemBase {

    private SparkFlex shooterFeedMotor;
    private SparkFlex shooterMotor;

    public FuelShooterSubsystem() {

        shooterFeedMotor = new SparkFlex(Constants.ShooterConstants.shooterFeedID, MotorType.kBrushless);
        shooterMotor = new SparkFlex(Constants.ShooterConstants.shooterShooterID, MotorType.kBrushless);

    } 

    public void motorControl(boolean rightTriggerPressed) { //This probably needs to be rewritten
        if (rightTriggerPressed) {
            shooterFeedMotor.set(ShooterConstants.shooterSpeed);
            shooterMotor.set(ShooterConstants.shooterSpeed);
            System.out.println("RT pressed, running shooter motors!");
        }
        else {
            shooterFeedMotor.set(0.0);
            shooterMotor.set(0.0);
            System.out.println("RT is NOT pressed, motors stopped!");
        }
    }
    public void runMotor(double speed) {
        shooterFeedMotor.set(speed);
        shooterMotor.set(speed);
        System.out.println("Shooter motors' speed set to" + speed);
    }
    
}
