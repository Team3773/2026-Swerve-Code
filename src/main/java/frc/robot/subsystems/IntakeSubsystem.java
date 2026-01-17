package frc.robot.subsystems;

import com.ctre.phoenix6.hardware.TalonFXS; 
import com.ctre.phoenix6.hardware.TalonFX; 
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class IntakeSubsystem extends SubsystemBase {

    private TalonFXS intakePivotMotor; //CTRE Minion, used for raising/lowering the intake axle
    private TalonFX intakeAxleMotor; //Kraken X44, used for driving the intake wheels

    public IntakeSubsystem() {
        intakePivotMotor = new TalonFXS(Constants.IntakeConstants.intakePivotID, "rio");
        intakeAxleMotor = new TalonFX(Constants.IntakeConstants.intakeAxleID, "rio");
        
    }
}
