package frc.robot.subsystems;

import com.ctre.phoenix6.hardware.TalonFXS;
import com.ctre.phoenix6.signals.FeedbackSensorSourceValue;
import com.ctre.phoenix6.signals.MotorArrangementValue;
import com.ctre.phoenix6.signals.SensorDirectionValue;
import com.ctre.phoenix6.hardware.CANcoder;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.configs.CANcoderConfiguration;
import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.configs.TalonFXSConfiguration;
import com.ctre.phoenix6.signals.NeutralModeValue;
import com.ctre.phoenix6.controls.PositionVoltage;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;

import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class IntakeSubsystem extends SubsystemBase {

    private TalonFX intakePivotMotor; //Kraken X44, used for raising/lowering the intake axle
    private CANcoder intakeCancoder; //CANcoder
    private TalonFXS intakeGrabMotor; //CTRE Minion, used for driving the intake wheels and also powering the Hopper Agitator 
    //private SparkMax intakeSecondAgitator; //Another X44 used exclusively for the agitator near the Shooter

    private double requestedPos;

    public IntakeSubsystem() {
        intakePivotMotor = new TalonFX(Constants.IntakeConstants.intakePivotID, "rio");
        intakeGrabMotor = new TalonFXS(Constants.IntakeConstants.intakeGrabID, "rio");
        intakeCancoder = new CANcoder(Constants.IntakeConstants.intakePivotEncoderID);
 
        // TalonFXS Configuration
        TalonFXSConfiguration talonFXSConfiguration = new TalonFXSConfiguration();
        intakeGrabMotor.getConfigurator().apply(talonFXSConfiguration);
        talonFXSConfiguration.Commutation.MotorArrangement = MotorArrangementValue.Minion_JST;
        talonFXSConfiguration.MotorOutput.NeutralMode = NeutralModeValue.Coast;
        intakeGrabMotor.getConfigurator().apply(talonFXSConfiguration);

        // CANcoder Configuration
        CANcoderConfiguration cc_cfg = new CANcoderConfiguration();
        cc_cfg.MagnetSensor.AbsoluteSensorDiscontinuityPoint = 0.5;
        cc_cfg.MagnetSensor.SensorDirection = SensorDirectionValue.CounterClockwise_Positive;
        cc_cfg.MagnetSensor.MagnetOffset = Constants.IntakeConstants.intakePivotEncoderMagneticOffset;
        intakeCancoder.getConfigurator().apply(cc_cfg);

        // TalonFX Configuration
        TalonFXConfiguration fx_cfg = new TalonFXConfiguration();
        fx_cfg.MotorOutput.NeutralMode = NeutralModeValue.Brake;
        fx_cfg.Feedback.FeedbackRemoteSensorID = intakeCancoder.getDeviceID();
        fx_cfg.Feedback.FeedbackSensorSource = FeedbackSensorSourceValue.SyncCANcoder;
        fx_cfg.Feedback.SensorToMechanismRatio = 1.0;//Change As Needed
        fx_cfg.Feedback.RotorToSensorRatio = 12.8;

        var slot0Configs = new Slot0Configs();
        // PIDs
        // !!!!!!DON'T MESS AROUND WITH THE VALUES UNLESS IT IS NOT REACHING THE SETPOINTS!!!!!!
        // IF IT ISN'T GETTING TO THE DOWN SETPOINT, INCREASE THE kD VALUE!

        //slot0Configs.kS = 0.25; // Add 0.25 V output to overcome static friction
        //slot0Configs.kV = 0.12; // A velocity target of 1 rps results in 0.12 V output
        slot0Configs.kP = 12; // A position error of 2.5 rotations results in 12 V output
        slot0Configs.kI = 0; // no output for integrated error
        slot0Configs.kD = 0.9; // A velocity error of 1 rps results in 0.1 V output

        intakePivotMotor.getConfigurator().apply(fx_cfg);
        intakePivotMotor.getConfigurator().apply(slot0Configs);
    }

    public void runIntakeGrabber(boolean leftTriggerPressed) {
        
        if (leftTriggerPressed) {
            intakeGrabMotor.set(Constants.IntakeConstants.intakeSpeed);
            //System.out.println("LT pressed, running intake grabber motors!");
        }
        else {
            intakeGrabMotor.set(0.0);
            //System.out.println("LT is NOT pressed, motors stopped!");
        }
    }

    public void runIntakeGrabberVariable(double speed) {
        intakeGrabMotor.set(speed);
        //intakeSecondAgitator.set(speed);
    }

    public void intakeGoToPosition(double position) {
        //just gonna yoink the pos for SmartDashboard
        requestedPos = position;

        System.out.println("Going to " + position + " rotations...");

        // Trapezoid profile with max velocity 80 rps, max accel 160 rps/s
        final TrapezoidProfile m_profile = new TrapezoidProfile(
            new TrapezoidProfile.Constraints(80, 160));
        // Final target of 200 rot, 0 rps
        TrapezoidProfile.State m_goal = new TrapezoidProfile.State(position, 0);
        TrapezoidProfile.State m_setpoint = new TrapezoidProfile.State();

        // create a position closed-loop request, voltage output, slot 0 configs
        final PositionVoltage m_request = new PositionVoltage(0).withSlot(0);

        // calculate the next profile setpoint
        m_setpoint = m_profile.calculate(0.020, m_setpoint, m_goal);

        // send the request to the device
        m_request.Position = m_setpoint.position;
        m_request.Velocity = m_setpoint.velocity;
        intakePivotMotor.setControl(m_request);
    }

    public void resetPosition() {
        return; //figure out how to reset the pid controller position
    }

    public void periodic() {
        SmartDashboard.putNumber("Intake Requested Pos", 
            requestedPos);
        SmartDashboard.putNumber("Intake CANcoder Pos", 
            intakeCancoder.getAbsolutePosition().getValueAsDouble());
        SmartDashboard.putNumber("Intake CANcoder Vel", 
            intakeCancoder.getVelocity().getValueAsDouble());
    }
}