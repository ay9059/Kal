/*
 * Controller.java
 * PROVIDED file for the Kal GUI lab in csci142.
 */

package calendar.view_controller.text;

import calendar.model.Appointment;
import calendar.model.Calendar;
import calendar.model.Time;
import edu.rit.cs.Range;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UncheckedIOException;
import java.util.LinkedList;
import java.util.List;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Consumer;

/**
 * Created by jeh on 3/11/17.
 */
public class Controller {

    public final static String PROMPT = "Kal> ";

    public final static String NEW = "new";
    public final static String REMOVE = "remove";
    public final static String SHOW = "show";
    public final static String SAVE = "save";
    public final static String QUIT = "quit";

    private Calendar model;
    private Scanner in;
    private PrintWriter out;

    private final int currentDate = 0;

    static final Map< String, String > help = new LinkedHashMap<>();

    static {
        help.put( NEW, NEW + " date,time,appointment" );
        help.put( SHOW, SHOW + " [date]" );
        help.put( REMOVE, REMOVE + " date,time" );
        help.put( SAVE, SAVE );
        help.put( QUIT, QUIT );
    }

    private final Map< String, Consumer< String > > actions = new HashMap<>();

    {
        actions.put( NEW, this::newAppt );
        actions.put( SHOW, this::show );
        actions.put( REMOVE, this::removeAppt );
        actions.put( SAVE, this::save );
    }

    public Controller(
            Calendar model,
            Scanner in,
            PrintWriter out ) {
        this.model = model;
        this.in = in;
        this.out = out;
    }

    public void mainLoop() {
        String line;
        String cmd;
        String args;
        this.out.print( PROMPT );
        this.out.flush();
        while ( this.in.hasNextLine() ) {
            line = this.in.nextLine();
            int spacePos = line.indexOf( " " );
            if ( spacePos == -1 ) {
                cmd = line;
                args = "";
            }
            else {
                cmd = line.substring( 0, spacePos );
                args = line.substring( spacePos + 1 );
            }
            if ( cmd.equals( QUIT ) ) {
                break;
            }
            if ( !help.containsKey( cmd ) ) {
                this.showCommands( "" );
            }
            else if ( actions.containsKey( cmd ) ) {
                try {
                    this.actions.get( cmd ).accept( args );
                }
                catch( Exception e ) {
                    this.out.print( "Action failed: " );
                    Throwable orig = e.getCause() == null ? e : e.getCause();
                    this.out.println( orig );
                }
            }
            else {
                this.out.println( "Internal error" );
            }
            out.print( PROMPT );
            this.out.flush();
        }
    }

    /* =========== specific command handlers =========== */

    private void showCommands( String args ) {
        assert args.equals( "" );
        for ( Map.Entry< String, String > entry: help.entrySet() ) {
            this.out.printf( "%10s: %s\n", entry.getKey(), entry.getValue() );
        }
    }

    private void newAppt( String args ) {
        String[] parts = args.split( "," );
        if ( parts.length == 3 ) { // date, time, what
            model.add(
                    Integer.parseInt( parts[ 0 ].trim() ),
                    Time.fromString( parts[ 1 ].trim() ),
                    parts[ 2 ].trim()
            );
        }
        else if ( this.currentDate != 0 ) { // time, what
            model.add(
                    this.currentDate,
                    Time.fromString( parts[ 0 ].trim() ),
                    parts[ 1 ].trim()
            );
        }
    }

    private void show( String args ) {
        List< Appointment > allAppts = null;
        if ( args.isEmpty() ) {
            allAppts = new LinkedList<>();
            for ( int date: Range.of( 1, this.model.numDays() + 1 ) ) {
                allAppts.addAll( this.model.appointmentsOn( date ) );
            }
        }
        else {
            int date = Integer.parseInt( args );
            allAppts = this.model.appointmentsOn( date );
        }
        allAppts.forEach( this.out::println );
    }

    /**
     * Warning: This code will not search based on the appointment's
     * textt string. It will remove the first appointment it finds at
     * the given date and time.
     * @param args the string describing the entire appointment
     *             (format described at {@link Appointment#fromString(String)})
     * @throws NumberFormatException if anything in the args string is amiss
     */
    private void removeAppt( String args ) {
        String[] parts = args.split( "," );
        if ( parts.length == 3 ) {
            Appointment appt = Appointment.fromString( args );
            this.model.remove( appt );
        }
        else if ( this.currentDate != 0 ) {
            Appointment appt = new Appointment(
                    this.currentDate,
                    Time.fromString( parts[ 0 ].trim() ),
                    parts[ 1 ]
            );
            this.model.remove( appt );
        }
        else {
            Appointment appt = new Appointment(
                    Integer.parseInt( parts[ 0 ].trim() ),
                    Time.fromString( parts[ 1 ].trim() ),
                    ""
            );
            this.model.remove( appt );
        }
    }

    /**
     * Save the calendar back into the file from which it was read.
     * If the calendar was not read in from a file, an
     * UncheckedIOException is raised.
     * @param args not used
     */
    private void save( String args ) {
        try {
            this.model.toFile();
        }
        catch( IOException e ) {
            throw new UncheckedIOException( e );
        }
    }

}
