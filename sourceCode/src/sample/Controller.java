package sample;

import com.FileUtility.FileUtility;
import com.mollin.yapi.YeelightDevice;
import com.mollin.yapi.enumeration.YeelightProperty;
import com.mollin.yapi.exception.YeelightResultErrorException;
import com.mollin.yapi.exception.YeelightSocketException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.Scanner;

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


//listener of the main window
public class Controller
{
    private YeelightDevice device;      //the Yeelight bulb

    @FXML
    private TextField deviceId;         //id of the device (as ip.ip.ip.ip:port)
    @FXML
    private Slider brightControl;       //slider that control the bulb brightness
    @FXML
    private Label brightVisualize;      //show the bulb brightness
    @FXML
    private ImageView image;            //ray of light when the light is on

    private boolean isOn=false;         //power status of the bulb

    //fxml setup on startup
    public void initialize()
    {
        File saved=new File("saved.brk");                                  //open the file where is the IP
        Scanner input=FileUtility.inputMode(saved);                                 //Scanner from file

        if(input.hasNextLine())                                                     //only if an IP exist
        {
            String ip=input.nextLine();                                             //get the ip
            deviceId.setText(ip);                                                   //set the texField

            try
            {
                device=new YeelightDevice(ip);                                       //try to pairing
            }
            catch(YeelightSocketException e)
            {
               Util.showAnError("can't find the bulb. Check the guide.");
               return;
            }

            Map<YeelightProperty, String> status= null;                               //get the status of the bulb
            try
            {
                status = device.getProperties();
            }
            catch (YeelightResultErrorException e)
            {
                //already handled
            }
            catch (YeelightSocketException e)
            {
                //already handled
            }

            for (Map.Entry<YeelightProperty, String> entry : status.entrySet())            //read the status
            {
                String k = String.valueOf(entry.getKey());                                  //type of key (bright,color,etc.)
                String v = entry.getValue();                                                //value of the key

                if (k.equals("POWER"))                                                     //get the status, key power
                {
                    if (v.equals("on"))                                                    //if the lamp is on
                        isOn = false;                                                       //set the power status false (explained in line 79)
                    else
                        isOn = true;                                                        //Set the power status false (explained in line 79)

                    //Set the power status (explained in line 79): This method change the power status of the lamp:
                    //when is on, it turns the bulb of and vice versa. To sync it with the bulb, set the isOn variable the
                    //opposite of the real power status, so if the bulb is on the method will turn it on, if the bulb is
                    //off the method will turn it off: same power status, but now all is sync.
                    turnOnOff();
                }

                if (k.equals("BRIGHTNESS"))                                                //get the status, brightness
                {
                    int brightness = Integer.parseInt(v);
                    brightControl.setMax(100);                                            //set max to 100%
                    brightControl.setMin(0);                                              //set min to 0% (is not off)
                    brightControl.setValue(brightness);                                   //set the brightness
                    brightVisualize.setText(v + "%");                                     //show the percentage
                }
            }
        }
        input.close();
    }

    //search and pair the bulb on the lan only if the IP is new
    public void findTheLight()
    {
        String ip=deviceId.getText();

        if(Util.checkIp(ip)==false)                                                  //check is the ip is a correct ip (x.x.x.x)
        {
            Util.showAnError("Please, enter a correct IP.");
            return;                                                                  //if not correct, do nothing
        }

        File saved=new File("saved.brk");                                   //save the ip on file
        PrintWriter output=FileUtility.outputMode(saved);
        output.println(ip);
        output.close();

        this.initialize();                                                            //apply the changes
    }

    //set the brightness of the bulb (user input)
    public void setBrightness()
    {
        int value=(int)brightControl.getValue();                                      //get the value from the user

        brightVisualize.setText(Integer.toString(value) + "%");                       //show the percentage

        try
        {
            device.setBrightness(value);                                              //set the brightness
        }
        catch (YeelightResultErrorException e)                                        //com. error
        {
            Util.showAnError("Please, try again.");
        }
        catch (YeelightSocketException e)                                             //com. error
        {
            Util.showAnError("Please, try again.");
        }
    }

    //set the color of the bulb (user input, get the color from the button)
    public void setColor(ActionEvent action)
    {
        Button colorButton=(Button)action.getSource();                                //get the button
        String style=colorButton.getStyle();                                          //get the css style of the button
        int indexColor=style.indexOf("#");                                            //search the hex color
        String realColor=style.substring(indexColor,indexColor+7);                    //get the hex color

        Color finish=java.awt.Color.decode(realColor);                                //get the rgb color

        try
        {
            device.setRGB(finish.getRed(),finish.getGreen(),finish.getBlue());        //set the color
        }
        catch (YeelightResultErrorException e)                                        //com. error
        {
            Util.showAnError("Please, try again.");
        }
        catch (YeelightSocketException e)                                             //com.error
        {
            Util.showAnError("Please, try again.");
        }
    }

    //set the power of the bulb
    public void turnOnOff()
    {
        try
        {
            device.setPower(!isOn);                                                   //if is on -> turn off and vice versa
        }
        catch (YeelightResultErrorException e)
        {
            Util.showAnError("Please, try again.");
        }
        catch (YeelightSocketException e)
        {
            Util.showAnError("Please, try again.");
        }
        isOn=!isOn;                                                                   //sync the power status

        image.setVisible(isOn);                                                       //set the ray of light when the light is on
    }

    //set a defined scene (pc, reading, relax)
    public void setScene(ActionEvent action)
    {
        Button sceneButton = (Button) action.getSource();                             //get the button

        if(sceneButton.getText().equals("PC"))                                        //pc scene
        {
            try
            {
                device.setRGB(47,53,230);                                    //set the color
                device.setBrightness(60);                                            //set the brightness
            }
            catch (YeelightResultErrorException e)
            {
                Util.showAnError("Please, try again.");
            }
            catch (YeelightSocketException e)
            {
                Util.showAnError("Please, try again.");
            }

            brightControl.setValue(60);                                              //sync the brightness
            brightVisualize.setText(Integer.toString(60) + "%");
        }

        if(sceneButton.getText().equals("READING"))                                 //reading scene
        {
            try
            {
                device.setRGB(309,399,0);                                   //set the color
                device.setBrightness(100);                                          //set the brightness
                device.setColorTemperature(3500);                                   //set the color temperatura (warm)
            }
            catch (YeelightResultErrorException e)
            {
                e.printStackTrace();
            }
            catch (YeelightSocketException e) {
                Util.showAnError("Please, try again.");
            }

            brightControl.setValue(100);                                            //sync the brightness
            brightVisualize.setText(Integer.toString(100) + "%");
        }

        if(sceneButton.getText().equals("RELAX"))                                   //relax scene
        {
            try
            {
                device.setRGB(167,65,0);                                   //set the color
                device.setColorTemperature(3200);                                  //set the temperature
                device.setHSV(24,100);                                    //set the hue and saturation
                device.setBrightness(50);                                         //set brightness
            }
            catch (YeelightResultErrorException e)
            {
                Util.showAnError("Please, try again.");
            }
            catch (YeelightSocketException e)
            {
                Util.showAnError("Please, try again.");
            }

            brightControl.setValue(100);                                            //sync the brightness
            brightVisualize.setText(Integer.toString(50) + "%");
        }
    }

    //open the user manual (online, on BrockDeveloper site)
    public void openInstruction()
    {
        String url="https://brockdeveloper.github.io/y2e-Yeelight-controller";
        try
        {
            Desktop.getDesktop().browse(new URI(url));                              //open website (default browser)
        }
        catch (IOException e)
        {
            Util.showAnError("Please, try again.");
        }
        catch (URISyntaxException e)
        {
            Util.showAnError("Please, try again.");
        }
    }
}