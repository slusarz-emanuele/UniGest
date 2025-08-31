package it.univaq.unigest.util;

import it.univaq.unigest.gui.Main;

import java.util.Locale;
import java.util.ResourceBundle;

public class ParametrizzazioneHelper {

    private Locale locale;
    private ResourceBundle bundle;

    public ParametrizzazioneHelper(){
        String lingua = Main.getImpostazioni().getLingua().toLowerCase();
        System.out.println("Lingua letta: " + lingua);
        switch(lingua){
            case "english":{
                this.locale = Locale.ENGLISH;
                break;
            }

            case "italiano":{
                this.locale = Locale.ITALIAN;
                break;
            }

            default: this.locale = Locale.ITALIAN;
        }
        bundle = ResourceBundle.getBundle("internationalization/strings", locale);
    }

    public ResourceBundle getBundle(){
        return this.bundle;
    }

    //public static final ResourceBundle bundle = ResourceBundle.getBundle("internationalization/strings", locale);

}
