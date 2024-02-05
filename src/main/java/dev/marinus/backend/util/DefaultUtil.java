package dev.marinus.backend.util;

import dev.marinus.backend.model.content.impl.WelcomeScreenContent;

public class DefaultUtil {

    public static WelcomeScreenContent createDefaultWelcomeScreenContent() {
        WelcomeScreenContent welcomeScreenContent = new WelcomeScreenContent();
        welcomeScreenContent.setName("Default Welcome Screen");
        welcomeScreenContent.getWelcomeMessage().add("<span style='color: " + "{[{terminal#highlightColor}]}" + ";'><span style='text-decoration-line: underline; color: " + "{[{terminal#warningColor}]}" + "'>Hello World!</span> I´m Marinus.</span>");
        welcomeScreenContent.getWelcomeMessage().add("<span style='color: " + "{[{terminal#informationColor}]}" + "; line-height: 0.4'> > Mit 16 Jahren habe ich meine Leidenschaft für das Programmieren entdeckt.</span>");
        welcomeScreenContent.getWelcomeMessage().add("<span style='color: " + "{[{terminal#informationColor}]}" + "; line-height: 0.4'> > Über 8 Jahre Erfahrung in Java.</span>");
        welcomeScreenContent.getWelcomeMessage().add("<span style='color: " + "{[{terminal#informationColor}]}" + "; line-height: 0.4'> > Schnelle Einarbeitung in neue Sprachen/Frameworks durch langjährige Erfahrung.</span>");
        welcomeScreenContent.getWelcomeMessage().add("<span style='color: " + "{[{terminal#informationColor}]}" + "; line-height: 0.4'> > In Zukunft strebe ich eine Position als Programmierer in Ihrem Unternehmen an, um meine Fähigkeiten weiter auszubauen");
        welcomeScreenContent.getWelcomeMessage().add("<span style='color: " + "{[{terminal#informationColor}]}" + "; line-height: 0.4'>   und einen wesentlichen Beitrag zum Erfolg ihres Unternehmens zu leisten.</span>");
        return welcomeScreenContent;
    }
}