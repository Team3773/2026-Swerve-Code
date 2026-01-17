package frc.robot.subsystems;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class ClimbSubsystem extends SubsystemBase {

    private SparkMax winchMotor;

    public ClimbSubsystem() {

        winchMotor = new SparkMax(Constants.ClimbConstants.winchID, MotorType.kBrushless);

    }
    
}
