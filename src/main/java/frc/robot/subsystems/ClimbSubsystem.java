package frc.robot.subsystems;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.spark.ClosedLoopSlot;
import com.revrobotics.spark.FeedbackSensor;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class ClimbSubsystem extends SubsystemBase {

    private SparkMax winchMotor;

    private SparkClosedLoopController closedLoopController;
    private RelativeEncoder encoder;
    
    private final double winchMotorSpeed = Constants.ClimbConstants.winchMotorSpeed;

    public ClimbSubsystem() {

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
}


