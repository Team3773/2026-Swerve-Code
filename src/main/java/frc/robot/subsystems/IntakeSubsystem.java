package frc.robot.subsystems;

import com.ctre.phoenix6.hardware.TalonFXS; 
import com.ctre.phoenix6.hardware.TalonFX; 
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class IntakeSubsystem extends SubsystemBase {

    private TalonFX intakePivotMotor; //Kraken X44, used for raising/lowering the intake axle
    private TalonFXS intakeGrabMotor; //CTRE Minion, used for driving the intake wheels and also powering the Hopper Agitator 
    private TalonFX intakeSecondAgitator; //Another X44 used exclusively for the agitator near the Shooter

    public IntakeSubsystem() {
        intakePivotMotor = new TalonFX(Constants.IntakeConstants.intakePivotID, "rio");
        intakeGrabMotor = new TalonFXS(Constants.IntakeConstants.intakeGrabID, "rio");
        intakeSecondAgitator = new TalonFX(Constants.IntakeConstants.intakeSecondAgitatorID, "rio");
        
    }

    public void runIntakeGrabber(boolean leftTriggerPressed) {
        
        if (leftTriggerPressed) {
            intakeGrabMotor.set(0.5);
            intakeSecondAgitator.set(0.5);
            System.out.println("LT pressed, running intake grabber motors!");
        }
        else {
            intakeGrabMotor.set(0.0);
            intakeSecondAgitator.set(0.0);
            System.out.println("LT is NOT pressed, motors stopped!");
        }
    }
}
