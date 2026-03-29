// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.autonomous;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.commands.IntakeCommand;
import frc.robot.subsystems.IntakeSubsystem;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class IntakeUnreadyAutonCommand extends Command {
  /** Creates a new IntakeReadyAutonCommand. */
  IntakeSubsystem intakeSubsystem;

  private final Timer timer = new Timer();

  public IntakeUnreadyAutonCommand(IntakeSubsystem intakeSubsystem) {
    // Use addRequirements() here to declare subsystem dependencies.
    this.intakeSubsystem = intakeSubsystem;

    addRequirements(intakeSubsystem);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    timer.reset();
    timer.start();
    System.out.println("IntakeRunAuton Timer started!");
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    //System.out.println("Moving intake to deployed position...");
    intakeSubsystem.intakeGoToPosition(IntakeCommand.startPos);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    timer.stop();
    System.out.println("IntakeReadyAuton ended!");
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return timer.get() >= 0.5;
  }
}
