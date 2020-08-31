package sample;

import javafx.scene.control.Alert;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * Copyright © 2020 Brock Developer (https://brockdeveloper.github.io)
 *
 * under creative commons license: https://creativecommons.org/licenses/by-nc-sa/4.0/
 * creative commons license: Attribution-NonCommercial-ShareAlike
 *
 * you are free to consult and modify the code as defined by the creative commons.
 * Instead, the name "y2e" is registered by copyrighted.com
 * (view certificate: https://www.copyrighted.com/work/FUSnH886HnPtZb0s)
 */

//class that implements all the utilities used
public class Util
{
    //shows a new dialog with a variable error message
    public static void showAnError(String errorMessage)
    {
        Alert alert = new Alert(Alert.AlertType.ERROR);      //type of message
        alert.setTitle("y2e - an error occurred");
        alert.setHeaderText(null);
        alert.setContentText(errorMessage);                  //variable message

        alert.showAndWait();                                 //show the dialog and wait for user intervention
    }

    //shows a new dialog with a variable error message
    public static boolean checkIp(String ip)
    {
        Pattern p = Pattern.compile("^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$");
        Matcher m = p.matcher(ip);
        return m.find();
    }
}
