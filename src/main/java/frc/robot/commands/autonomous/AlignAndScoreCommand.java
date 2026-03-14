package frc.robot.commands.autonomous;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.path.PathConstraints;

import edu.wpi.first.apriltag.AprilTagFieldLayout;
import edu.wpi.first.apriltag.AprilTagFields;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Transform2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

import frc.robot.subsystems.CommandSwerveDrivetrain;

import java.net.URL;
import java.util.List;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class AlignAndScoreCommand extends SequentialCommandGroup {

    private static final String LIMELIGHT = "limelight"; // update if your limelight has a custom name

    // How far in front of the tag the robot stops (meters).
    // Measure from the tag face to your robot's scoring contact point.
    private static final double STANDOFF_METERS = 0.5;
    private static final double LATERAL_OFFSET_METERS = -0.5; // negative = right, positive = left

    // Conservative constraints for the final precise approach.
    // Your robot's free speed is 5.72 m/s — these are intentionally slower for accuracy.
    private static final PathConstraints CONSTRAINTS = new PathConstraints(
        2.0,                            // max velocity m/s
        1.5,                            // max acceleration m/s²
        Units.degreesToRadians(360),    // max angular velocity rad/s
        Units.degreesToRadians(540)     // max angular acceleration rad/s²
    );

    private static AprilTagFieldLayout loadFieldLayout() {
    try {
        URL url = new URL("http://limelight.local:5807/apriltagfieldlayout");
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(url, AprilTagFieldLayout.class);
    } catch (Exception e) {
        DriverStation.reportError("[AlignAndScore] Failed to fetch field layout from Limelight: " + e.getMessage(), false);
        return new AprilTagFieldLayout(List.of(), 0, 0); // empty fallback
    }
}

private static final AprilTagFieldLayout FIELD_LAYOUT = loadFieldLayout();

    /**
     * @param drive   Your CommandSwerveDrivetrain instance
     * @param scorer  Your scoring subsystem
     * @param tagId   The AprilTag ID to align to (from the field layout)
     */
    public AlignAndScoreCommand(CommandSwerveDrivetrain drive, int tagId) {
        addCommands(
            // Step 1: Snap odometry to vision before pathfinding starts.
            // This corrects any drift accumulated during the preceding auto path
            // so PathPlanner generates a path from the robot's true field position.
            Commands.runOnce(() -> drive.updateVisionPose(LIMELIGHT)),

            // Step 2: Pathfind to the offset pose in front of the tag.
            // Commands.defer() re-evaluates the target at runtime (after step 1 corrects pose)
            // rather than baking in the pose at command construction time.
            Commands.defer(
                () -> buildAlignCommand(drive, tagId),
                Set.of(drive)
            )
        );
    }

    // -------------------------------------------------------------------------

    private Command buildAlignCommand(CommandSwerveDrivetrain drive, int tagId) {
        Pose2d targetPose = getOffsetPose(tagId);

        if (targetPose == null) {
            return Commands.print("[AlignAndScore] Tag " + tagId + " not found in field layout — skipping align.");
        }

        return AutoBuilder.pathfindToPose(targetPose, CONSTRAINTS, 0.0);
    }

    private static Pose2d getOffsetPose(int tagId) {
        Optional<Pose3d> tagPose = FIELD_LAYOUT.getTagPose(tagId);
        if (tagPose.isEmpty()) return null;

        // transformBy applies the offset in the tag's own coordinate frame:
        //   +X = directly away from the tag face (toward the robot approach direction)
        //   Rotation2d(PI) = robot faces toward the tag (front of robot toward tag)
        //
        // If your scorer/intake is on the BACK of the robot, change to new Rotation2d(0)
        // If you need a lateral offset (e.g. left/right of center), change the Y value
        return tagPose.get().toPose2d().transformBy(
            new Transform2d(
                new Translation2d(STANDOFF_METERS, LATERAL_OFFSET_METERS), // adjust lateral offset as needed
                new Rotation2d(Math.PI)
            )
        );
    }
}
