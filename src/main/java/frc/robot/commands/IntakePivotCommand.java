// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import java.util.function.BooleanSupplier;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants;
import frc.robot.subsystems.IntakeSubsystem;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class IntakePivotCommand extends Command {
  BooleanSupplier button1, button2;
  IntakeSubsystem intakeSubsystem;
  double startPosition = Constants.IntakeConstants.intakePivotStartPosition;
  double lowerPosition = Constants.IntakeConstants.intakePivotDownPosition;

  /** Creates a new IntakePivotCommand. */
  public IntakePivotCommand(BooleanSupplier button1, BooleanSupplier button2, IntakeSubsystem intakeSubsystem) {
    this.button1 = button1;
    this.button2 = button2;
    this.intakeSubsystem = intakeSubsystem;
    addRequirements(intakeSubsystem);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    this.intakeSubsystem.resetEncoder();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (button1.getAsBoolean()) {
      this.intakeSubsystem.goToPosition(lowerPosition);
    } else if (button2.getAsBoolean()) {
      this.intakeSubsystem.goToPosition(startPosition);
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    intakeSubsystem.stopMotor();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
