// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import java.util.function.BooleanSupplier;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import frc.robot.subsystems.FuelShooterSubsystem;
import frc.robot.subsystems.IntakeSubsystem;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class ShooterIntakeCommand extends ParallelCommandGroup {
  /** Creates a new ShooterIntakeCommand. */
  public ShooterIntakeCommand(FuelShooterSubsystem fuelShooterSubsystem,
                              IntakeSubsystem intakeSubsystem,
                              BooleanSupplier rightTrigger) {
    // Run shooter and intake grabber in parallel, both using the same trigger supplier
    addCommands(
      new ShooterCommand(fuelShooterSubsystem, rightTrigger),
      new IntakeGrabCommand(intakeSubsystem, rightTrigger)
    );

    // declare subsystem requirements for the group (optional since each child adds theirs)
    addRequirements(fuelShooterSubsystem, intakeSubsystem);
  }
}
