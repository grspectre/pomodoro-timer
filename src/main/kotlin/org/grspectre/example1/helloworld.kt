/**
 * Created by ant on 02.08.2015.
 */
package org.grspectre.example1

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.Event;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import java.util.Timer;
import java.util.TimerTask;

public open class HelloWorld(): Application() {
    override fun start(primaryStage: Stage?) {
        primaryStage?.setTitle("Pomodoro");
        var btn : Button = Button();
        btn.setText("Start");
        var timer: Timer? = null;
        var indicator: ProgressIndicator? = ProgressIndicator();

        public open class TickTimerRunnable(pr: Double, type: String, ind: ProgressIndicator?, btn: Button?): Runnable {
            public var pr: Double = 0.0;
            public var ind: ProgressIndicator? = null;
            public var type: String = "";
            public var btn: Button? = null;
            init {
                this.pr = pr;
                this.ind = ind;
                this.type = type;
                this.btn = btn;
            }

            public override fun run() {
                when(this.type) {
                    "work" -> {
                        if (pr != 1.0) {
                            this.ind?.setStyle(" -fx-progress-color: #3399ff;");
                            this.ind?.setProgress(pr);
                        }
                    }
                    "rest" -> {
                        if (pr != 1.0) {
                            this.ind?.setStyle(" -fx-progress-color: #66ff66;");
                            this.ind?.setProgress(pr);
                        }
                    }
                    "stop" -> {
                        this.btn?.fire();
                    }
                }
            }
        }

        public open class TickTimer(timeInSecondsWork: Int, timeInSecondsRest: Int, ind: ProgressIndicator?, btn: Button?): TimerTask() {
            public var timeInSecondsWork: Int = 0;
            public var timeInSecondsRest: Int = 0;
            public var timeInSecondsCurrent: Int = 0;
            public var type: String = "work";
            public var ind: ProgressIndicator? = null;
            public var btn: Button? = null;
            init {
                this.timeInSecondsCurrent = timeInSecondsWork;
                this.timeInSecondsWork = timeInSecondsWork;
                this.timeInSecondsRest = timeInSecondsRest;
                this.ind = ind;
                this.btn = btn;
            }

            public override fun run() {
                var pr: Double = 0.0;
                when(this.type) {
                    "work" -> {
                        pr = this.timeInSecondsCurrent.toDouble()/this.timeInSecondsWork.toDouble();
                        this.timeInSecondsCurrent--;
                        if (this.timeInSecondsCurrent == -1) {
                            this.type = "rest";
                            this.timeInSecondsCurrent = this.timeInSecondsRest;
                        }
                    }
                    "rest" -> {
                        pr = this.timeInSecondsCurrent.toDouble()/this.timeInSecondsRest.toDouble();
                        this.timeInSecondsCurrent--;
                        if (this.timeInSecondsCurrent == -1) {
                            this.type = "stop";
                        }
                    }
                }
                var obj: TickTimerRunnable = TickTimerRunnable(pr, type, this.ind, this.btn);
                Platform.runLater(obj);
            }
        }

        val goAction = object: EventHandler<ActionEvent?> {
            public override fun handle(event: ActionEvent?) {
                if (btn.getText() == "Start") {
                    btn.setText("Stop");
                    timer = Timer();
                    var obj: TickTimer = TickTimer(1*60, 30, indicator, btn);
                    timer?.schedule(obj, 0, 1000);
                } else {
                    btn.setText("Start");
                    timer?.cancel();
                    timer?.purge();
                    indicator?.setStyle(" -fx-progress-color: #3399ff;");
                    indicator?.setProgress(-1.0);
                }
            }
        }

        val onClose = object: EventHandler<WindowEvent?> {
            public override fun handle(event: WindowEvent?) {
                timer?.cancel();
                timer?.purge();
            }
        }

        btn.setOnAction(goAction);
        var root : StackPane = StackPane();
        root.getChildren().add(indicator);
        root.getChildren().add(btn);
        var scene: Scene = Scene(root, 250.0, 250.0);
        primaryStage?.setScene(scene);
        primaryStage?.setOnCloseRequest(onClose);
        primaryStage?.setAlwaysOnTop(true);
        primaryStage?.setResizable(false);
        primaryStage?.show();
    }
}