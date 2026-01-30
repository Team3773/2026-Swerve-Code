package frc.robot.subsystems;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.controls.PositionVoltage;
import com.ctre.phoenix6.hardware.TalonFXS;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.spark.ClosedLoopSlot;
import com.revrobotics.spark.FeedbackSensor;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.SparkLowLevel.MotorType;

import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class ClimbSubsystem extends SubsystemBase {

    private SparkMax winchMotor;
    private TalonFXS armMotor;

    private SparkClosedLoopController closedLoopController;
    private RelativeEncoder encoder;
    
    private final double winchMotorSpeed = Constants.ClimbConstants.winchMotorSpeed;

    public ClimbSubsystem() {
        //Configure the winch motor
        winchMotor = new SparkMax(Constants.ClimbConstants.winchID, MotorType.kBrushless);

        closedLoopController = winchMotor.getClosedLoopController();
    
        encoder = winchMotor.getEncoder();

        SparkMaxConfig winchConfig = new SparkMaxConfig();

        winchConfig.encoder
        .positionConversionFactor(1.0/324.0)
        .velocityConversionFactor(1.0/324.0);

        winchConfig.closedLoop
            .feedbackSensor(FeedbackSensor.kPrimaryEncoder)
            // Set PID values for position control. We don't need to pass a closed loop
            // slot, as it will default to slot 0.
            .p(1)
            .i(0)
            .d(0)
            .outputRange(-1, 1)
            // Set PID values for velocity control in slot 1
            .p(1, ClosedLoopSlot.kSlot1)
            .i(0, ClosedLoopSlot.kSlot1)
            .d(0, ClosedLoopSlot.kSlot1)
            .velocityFF(1.0 / 5767, ClosedLoopSlot.kSlot1)
            .outputRange(-1, 1, ClosedLoopSlot.kSlot1);


        winchConfig
                .smartCurrentLimit(50)
                .idleMode(IdleMode.kBrake);
        
        winchConfig.apply(winchConfig);
        
        winchMotor.configure(winchConfig, ResetMode.kResetSafeParameters, PersistMode.kNoPersistParameters);

        //Configure the arm motor
        // in init function, set slot 0 gains
        var slot0Configs = new Slot0Configs();
        slot0Configs.kP = 2.4; // An error of 1 rotation results in 2.4 V output
        slot0Configs.kI = 0; // no output for integrated error
        slot0Configs.kD = 0.1; // A velocity of 1 rps results in 0.1 V output

        armMotor.getConfigurator().apply(slot0Configs);
    }

public void runWinch(){
    winchMotor.set(winchMotorSpeed);
  }
public void stopWinch(){
    winchMotor.set(0.0);
  }
public void releaseWinch(){
    winchMotor.set(-winchMotorSpeed);
  }
  public void armGoToPosition(double position) {
    // Trapezoid profile with max velocity 80 rps, max accel 160 rps/s
        final TrapezoidProfile m_profile = new TrapezoidProfile(
            new TrapezoidProfile.Constraints(80, 160));
        // Final target of var rot, 0 rps
        TrapezoidProfile.State m_goal = new TrapezoidProfile.State(position, 0);
        TrapezoidProfile.State m_setpoint = new TrapezoidProfile.State();

        // create a position closed-loop request, voltage output, slot 0 configs
        final PositionVoltage m_request = new PositionVoltage(0).withSlot(0);

        // calculate the next profile setpoint
        m_setpoint = m_profile.calculate(0.020, m_setpoint, m_goal);

        // send the request to the device
        m_request.Position = m_setpoint.position;
        m_request.Velocity = m_setpoint.velocity;
        armMotor.setControl(m_request);
  }
}


