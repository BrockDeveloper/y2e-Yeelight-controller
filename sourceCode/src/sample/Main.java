package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/*
 * Copyright © 2020 Brock Developer (https://brockdeveloper.github.io)
 *
 * under creative commons license: https://creativecommons.org/licenses/by-nc-sa/4.0/
 * creative commons license: Attribution-NonCommercial-ShareAlike
 *
 * you are free to consult and modify the code as defined by the cretive commons.
 * Instead, the name "y2e" is registered by copyrighted.com
 * (view certificate: https://www.copyrighted.com/work/FUSnH886HnPtZb0s)
 */

public class Main extends Application
{
    @Override
    public void start(Stage primaryStage) throws Exception
    {
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("y2e - Yeelight controller");
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("img/logo.png")));
        primaryStage.setScene(new Scene(root, 652, 400));
        primaryStage.show();

    }

    public static void main(String[] args)
    {
        launch(args);
    }
}