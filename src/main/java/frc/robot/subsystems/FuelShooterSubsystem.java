package frc.robot.subsystems;

import com.revrobotics.spark.SparkFlex;
import com.revrobotics.spark.SparkLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class FuelShooterSubsystem extends SubsystemBase {

    private SparkFlex shooterMotor;

    public FuelShooterSubsystem() {

        shooterMotor = new SparkFlex(Constants.ShooterConstants.shooterID, MotorType.kBrushless);

    } 

    
}
