package org.mort11.configs.constants;

import edu.wpi.first.math.util.Units;

public class VisionConstants {
        // Camera / Limelight Names
        // public static final String FRONT_CAMERA_NAME = "frontCamera";
        public static final String LIMELIGHT_FRONT_LEFT = "limelight4-FrontLeft";
        public static final String LIMELIGHT_FRONT_RIGHT = "limelight4-FrontRight";
        public static final String LIMELIGHT_BACK_LEFT = "limelight4-BackLeft";
        public static final String LIMELIGHT_BACK_RIGHT = "limelight4-BackRight";

        // Field Dimensions (in meters)
        public static final double FIELD_LENGTH = 16.54;
        public static final double FIELD_WIDTH = 8.07;

        // Hub AprilTag IDs
        // Blue hub pairs: 19&20, 21&24, 25&26, 27&18
        public static final int[] BLUE_HUB_TAG_IDS = { 18, 19, 20, 21, 24, 25, 26, 27 };
        // Red hub pairs: 11&2, 3&4, 5&8, 9&10
        public static final int[] RED_HUB_TAG_IDS  = { 2, 3, 4, 5, 8, 9, 10, 11 };
    
        // AprilTag IDs and layout (optional, if you want to reference tags by ID)
        // public static final int[] APRIL_TAG_IDS = {1, 2, 3, 4, 5, 6, 7, 8};
    
        // Camera settings / limits (example)
        public static final double MAX_LED_MODE = 3; // 0=off, 1=blink, 2=on, 3=default
        public static final double MIN_LED_MODE = 0;
    
        // Optional: Camera mounting offset (from robot center) in meters
        // public static final double CAMERA_X_OFFSET = 0.0;
        // public static final double CAMERA_Y_OFFSET = 0.0;
        // public static final double CAMERA_Z_OFFSET = 0.5; // height of camera from floor
        // public static final double CAMERA_PITCH = 0.0;    // in degrees
        // public static final double CAMERA_YAW = 0.0;      // in degrees
        // public static final double CAMERA_ROLL = 0.0;     // in degrees

        // Camera pose offsets from robot center (floor level)
        // TODO: Replace inch values with your actual measured distances
        // forward/side/up are in meters (converted from inches below)
        // roll/pitch/yaw are in degrees
        // pitch: negative = camera tilted upward
        // yaw: 0=front, 90=left, 180=back, -90=right
        public static final double[] CAM_FRONT_LEFT = {
            Units.inchesToMeters(10.5),   // forward
            Units.inchesToMeters(10.5),  // side (negative = right of center)
            Units.inchesToMeters(19.5),   // up (height from floor)
            0, -30, 45                    // roll, pitch, yaw
        };
        public static final double[] CAM_FRONT_RIGHT = {
            Units.inchesToMeters(10.5),
            Units.inchesToMeters(-10.5),
            Units.inchesToMeters(19.5),
            0, -30, -45
        };
        public static final double[] CAM_BACK_LEFT = {
            Units.inchesToMeters(-10.5),
            Units.inchesToMeters(10.5),
            Units.inchesToMeters(19.5),
            0, -30, -135
        };
        public static final double[] CAM_BACK_RIGHT = {
            Units.inchesToMeters(-10.5),
            Units.inchesToMeters(-10.5),
            Units.inchesToMeters(19.5),
            0, -30, 135
        };
    }
