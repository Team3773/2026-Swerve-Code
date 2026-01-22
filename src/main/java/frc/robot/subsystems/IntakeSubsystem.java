package frc.robot.subsystems;

import com.ctre.phoenix6.hardware.TalonFXS; 
import com.ctre.phoenix6.hardware.TalonFX; 
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class IntakeSubsystem extends SubsystemBase {

    private TalonFXS intakePivotMotor; //CTRE Minion, used for raising/lowering the intake axle
    private TalonFX intakeGrabMotor; //Kraken X44, used for driving the intake wheels and also powering the hopper agitator

    public IntakeSubsystem() {
        intakePivotMotor = new TalonFXS(Constants.IntakeConstants.intakePivotID, "rio");
        intakeGrabMotor = new TalonFX(Constants.IntakeConstants.intakeGrabID, "rio");
        
    }
}
