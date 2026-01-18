// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj.XboxController;
import frc.robot.Constants;
import frc.robot.subsystems.IntakeSubsystem;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class IntakeCommand {
  private final IntakeSubsystem intake;
  private final XboxController controller;
  private final double Speed;

  /** Creates a new IntakeCommand. */
  public IntakeCommand(IntakeSubsystem intake, XboxController controller) {
    this.intake = intake;
    this.controller = controller;
    this.Speed = Constants.IntakeConstants.intakeAxleSpeed;
    // If you later migrate to the WPILib command framework, call addRequirements(intake) here.
  }

  // Called when the command is initially scheduled.
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  public void execute() {
    // Triggers are axes; treat the left trigger as pressed when its axis value exceeds a small threshold
    if (controller.getLeftTriggerAxis() > 0.1) {
      intake.runIntake(Speed);
    } else {
      intake.stopIntake();
    }
  }

  // Called once the command ends or is interrupted.
  public void end(boolean interrupted) {
    intake.stopIntake();
  }

  // Returns true when the command should end.
  public boolean isFinished() {
    return false;
  }
}
