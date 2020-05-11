/*
 * KalPTUI.java
 * PROVIDED file for the Kal GUI lab in csci142.
 */

package calendar.view_controller.text;

import calendar.model.Appointment;
import calendar.model.Calendar;
import edu.rit.cs.ConsoleApplication;

import edu.rit.cs.Observer;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Scanner;

/**
 * @author James Heliotis
 */
public class KalPTUI extends ConsoleApplication implements Observer< Calendar > {

    private Calendar model;

    private boolean busted = false;

    @Override
    public void init() {
        try {
            List< String > cmdLineArgs = this.getArguments();
            if ( cmdLineArgs.isEmpty() ) {
                this.model = new Calendar( 28 );
            }
            else {
                this.model = Calendar.fromFile( cmdLineArgs.get( 0 ) );
            }
            this.model.addObserver( this );
        }
        catch( IOException e ) {
            System.err.println( e.getMessage() );
            this.busted = true;
        }
    }

    @Override
    public void go( Scanner consoleIn, PrintWriter consoleOut ) {
        if ( !this.busted ) {
            consoleOut.println( "Hi, this is Kal!" );
            Controller commandProcessor = new Controller( model, consoleIn,
                                                              consoleOut );
            commandProcessor.mainLoop();
        }
        consoleOut.println( "Calendar shutting down." );
    }

    @Override
    public void stop() {

    }

    public void update( Calendar o, Object arg ) {
        Calendar model = o; // (Calendar)o;
        int date = (Integer)arg;
        List< Appointment > appts = model.appointmentsOn( date );
        appts.forEach( System.out::println );
    }

    public static void main( String[] args ) {
        ConsoleApplication.launch( KalPTUI.class, args );
    }

}
