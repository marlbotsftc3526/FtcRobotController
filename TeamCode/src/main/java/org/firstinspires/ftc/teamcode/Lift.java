package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
public class Lift {
    private LinearOpMode myOpMode = null;
    public DcMotor liftLeft = null;
    public DcMotor liftRight = null;
    PIDController liftLeftPID;
    PIDController liftRightPID;

    public static final double LIFT_KP = 0.01;
    public static final double LIFT_KI = 0;
    public static final double LIFT_KD = 0;
    public static final double MAX_OUT = 0.7;

    public ElapsedTime delay = null;
    public double delayTime = 0.5;


    public enum LiftMode {
        MANUAL,
        HIGH_CHAMBER,
        LOW_CHAMBER,
        HIGH_CHAMBER_SCORE,
        LOW_CHAMBER_SCORE,
        HIGH_BUCKET,
        LOW_BUCKET,
        GROUND,
        HIGH_CHAMBER_TEST
    }
    public LiftMode liftMode = LiftMode.MANUAL;
    public Lift(LinearOpMode opmode) {
        myOpMode = opmode;
    }
    public void init() {
        liftLeft  = myOpMode.hardwareMap.get(DcMotor.class, "liftLeft");
        liftRight  = myOpMode.hardwareMap.get(DcMotor.class, "liftRight");
        liftLeft.setDirection(DcMotor.Direction.REVERSE);
        liftRight.setDirection(DcMotor.Direction.FORWARD);

        liftLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        liftRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        liftLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        liftRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        liftLeftPID = new PIDController(LIFT_KP, LIFT_KI, LIFT_KD, MAX_OUT);
        liftRightPID = new PIDController(LIFT_KP, LIFT_KI, LIFT_KD, MAX_OUT);
        resetLiftPID();
    }
    public void teleOp(){
        myOpMode.telemetry.addData("liftLeft: ", liftLeft.getCurrentPosition());
        myOpMode.telemetry.addData("liftRight: ", liftRight.getCurrentPosition());
        myOpMode.telemetry.addData("lift mode", liftMode);
        if (Math.abs(myOpMode.gamepad2.right_stick_y) > 0.3 && myOpMode.gamepad2.left_bumper) {
            liftMode = LiftMode.MANUAL;
        }


        if (liftMode == LiftMode.MANUAL) {
            liftLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            liftRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            if (Math.abs(myOpMode.gamepad2.right_stick_y) > 0.1 && myOpMode.gamepad2.left_bumper) {
                    liftLeft.setPower(-myOpMode.gamepad2.right_stick_y);
                    liftRight.setPower(-myOpMode.gamepad2.right_stick_y);
            } else {
                if(liftLeft.getCurrentPosition() < 30 && liftRight.getCurrentPosition() < 30){
                    liftLeft.setPower(0);
                    liftRight.setPower(0);
                }else{
                    liftLeft.setPower(0.1);
                    liftRight.setPower(0.1);
                }
            }
        } else if (liftMode == LiftMode.HIGH_CHAMBER) {
            liftToPositionPIDClass(1600);
        }else if (liftMode == LiftMode.HIGH_CHAMBER_SCORE) {
            liftToPositionPIDClass(1000);
        } else if (liftMode == LiftMode.LOW_CHAMBER) {
            liftToPositionPIDClass(600);
        }else if (liftMode == LiftMode.LOW_CHAMBER_SCORE) {
            liftToPositionPIDClass(200);
        } else if (liftMode == LiftMode.HIGH_BUCKET) {
            liftToPositionPIDClass(2950);
        } else if (liftMode == LiftMode.LOW_BUCKET) {
            liftToPositionPIDClass(1600);
        }else if (liftMode == LiftMode.GROUND) {
            liftToPositionPIDClass(30);
        }else if(liftMode == LiftMode.HIGH_CHAMBER_TEST){
            liftToPositionPIDClass(480);
        }

    }
    public void update(){
        if (liftMode == LiftMode.HIGH_CHAMBER) {
            liftToPositionPIDClass(1400);
        }else if (liftMode == LiftMode.HIGH_CHAMBER_SCORE) {
            liftToPositionPIDClass(1000);
        } else if (liftMode == LiftMode.LOW_CHAMBER) {
            liftToPositionPIDClass(600);
        }else if (liftMode == LiftMode.LOW_CHAMBER_SCORE) {
            liftToPositionPIDClass(100);
        } else if (liftMode == LiftMode.HIGH_BUCKET) {
            liftToPositionPIDClass(2950);
        } else if (liftMode == LiftMode.LOW_BUCKET) {
            liftToPositionPIDClass(1600);
        }else if (liftMode == LiftMode.GROUND) {
            liftToPositionPIDClass(30);
        }
    }

    public void liftToPositionPIDClass(double targetPosition) {
        double outLeft = liftLeftPID.calculate(targetPosition, liftLeft.getCurrentPosition());
        double outRight = liftRightPID.calculate(targetPosition, liftRight.getCurrentPosition());

        if(outLeft >= 0) {
            liftLeft.setPower(Math.min(MAX_OUT, outLeft));
            liftRight.setPower(Math.min(MAX_OUT, outRight));
        }else{
            liftLeft.setPower(Math.max(-MAX_OUT, outLeft));
            liftRight.setPower(Math.max(-MAX_OUT, outRight));
        }

    }

    public void resetLift(double speed) {
        liftLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        liftRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        if (liftLeft.getCurrentPosition() >= 5) {
            liftLeft.setPower(speed);
            liftRight.setPower(speed);
        } else {
            liftLeft.setPower(0);
            liftRight.setPower(0);
        }

    }


    public void resetLiftPID() {
        liftLeftPID.reset();
        liftRightPID.reset();
    }
}
