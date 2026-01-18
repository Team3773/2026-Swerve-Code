// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.Constants;
import frc.robot.subsystems.FuelShooterSubsystem;
import frc.robot.subsystems.IntakeSubsystem;

public class ShooterCommand {
    private final FuelShooterSubsystem shooter;
    private final IntakeSubsystem intake;
    private final CommandXboxController controller;

    public ShooterCommand(FuelShooterSubsystem shooter, IntakeSubsystem intake, CommandXboxController controller) {
        this.shooter = shooter;
        this.intake = intake;
        this.controller = controller;
        // Note: addRequirements is provided by CommandBase; to use that functionality,
        // extend CommandBase and ensure the WPILib dependency is available on the classpath.
    }

    public void initialize() {}

    public void execute() {
        // If right bumper is pressed, run shooter and intake at half speed; otherwise stop them.
        if (controller.rightTrigger().getAsBoolean()) {
            shooter.runShooter(Constants.ShooterConstants.shooterSpeed);
            intake.runIntake(Constants.IntakeConstants.intakeAxleSpeed);
        } else {
            shooter.stopShooter();
            intake.stopIntake();
        }
    }

    public void end(boolean interrupted) {
        shooter.stopShooter();
        intake.stopIntake();
    }

    public boolean isFinished() {
        return false;
    }
}
