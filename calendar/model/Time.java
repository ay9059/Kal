/*
 * Time.java
 * PROVIDED file for the Kal GUI lab in csci142.
 */

package calendar.model;

import edu.rit.cs.Range;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.function.Function;

/**
 * A representation of the time of day
 */
public class Time implements Comparable< Time > {

    /**
     * Number of hours in a day
     */
    public static final int HRS_PER_DAY = 24;

    /**
     * How many hours before a half-day clock wraps around
     */
    public static final int HALF_DAY_HRS = HRS_PER_DAY / 2;

    /**
     * The indicators for ante-meridiem and post-meridiem, used
     * in 12-hour time formats
     */
    public enum Meridiem {AM, PM}

    /**
     * How many minutes are in one hour
     */
    public static final int MINS_PER_HR = 60;

    /**
     * Will Time objects be output in 12-hour format?
     */
    private static boolean AM_PM = false;

    /**
     * Change how times are converted to strings.
     * @param hour24 true =&gt; 24-hour format; false =&gt; 12-hour format.
     */
    public static void set24Hour( boolean hour24 ) {
        AM_PM = !hour24;
    }

    /**
     * The number of minutes since midnight
     */
    private final int minutes;

    /**
     * Create a Time object based on the number of minutes since midnight.
     * @param minutes number of minutes since midnight
     */
    public Time( int minutes ) {
        this.minutes = minutes;
    }

    /**
     * Create a Time object from a 24-hour description of the hour and
     * the minute.
     * @param hour number of hours (0-23) since midnight
     * @param minute number of minutes past the hour
     */
    public Time( int hour, int minute ) {
        this( hour * MINS_PER_HR + minute );
    }

    /**
     * Create a Time object from a 12-hour description of the hour and
     * the minute, plus whether it is before or after noon. The twelfth
     * hour is handled in the conventional way, although using
     * hour=0 will work as well.
     * @param hour number of hours (0-11) since midnight or noon, 12 means 0.
     * @param minute number of minutes past the hour
     * @param meridiem choice of before or after noon.
     */
    public Time( int hour, int minute, Meridiem meridiem ) {
        this(
                hour == Time.HALF_DAY_HRS ?
                        meridiem == Meridiem.AM ?
                                0 :
                                Time.HALF_DAY_HRS
                        :
                                meridiem == Meridiem.AM ?
                                        hour :
                                        hour + Time.HALF_DAY_HRS,
                minute
        );
    }

    /**
     * Convert a string representing time to a Time object.
     * The acceptable formats are
     * <pre>
     *     d:d
     *     d:d m
     * </pre>
     * where <code>d</code> is a 1-2 digit decimal number representing
     * hours (pre-<code>:</code>) or minutes (post-<code>:</code>)
     * and <code>m</code> is the <em>meridiem</em> indicator:
     * <code>AM</code> or <code>PM</code>. (Lower case acceptable.)
     * Absence of the meridiem indicator implies 24-hour time, but
     * bounds checking is not done on the numbers.
     *
     * @param timeStr See format in description.
     * @return the Time instance created
     * @throws NumberFormatException if <code>timeStr</code> cannot be converted
     */
    public static Time fromString( String timeStr ) {
        int hour, minute;

        String[] parts = timeStr.split( ":" );
        if ( parts.length != 2 ) throw new NumberFormatException( timeStr );
        hour = Integer.parseInt( parts[ 0 ] );

        String[] subParts = parts[ 1 ].split( " " );
        if ( subParts.length > 2 ) throw new NumberFormatException( timeStr );
        minute = Integer.parseInt( subParts[ 0 ] );

        if ( subParts.length == 2 ) { // Meridiem indicator is present.
            switch ( subParts[ 1 ] ) {
                case "AM":
                case "am":
                    if ( hour == Time.HALF_DAY_HRS ) {
                        hour = 0;
                    }
                    break;
                case "PM":
                case "pm":
                    if ( hour != Time.HALF_DAY_HRS ) {
                        hour += Time.HALF_DAY_HRS;
                    }
                    break;
                default:
                    throw new NumberFormatException( timeStr );
            }
        }
        if (
                hour < 0 || hour >= Time.HRS_PER_DAY ||
                minute < 0 || minute >= Time.MINS_PER_HR
        ) {
            throw new NumberFormatException( "Bad time " + hour + ':' + minute );
        }
        return new Time( hour, minute );
    }

    /** a lambda expression to deal with hours labeled "12" */
    private static final Function< Integer, Integer > weird = h -> h == 0 ? 12 : h;

    /**
     * Get a string representation of the Time object.
     * @return a string in a format compatible with
     *         {@link Calendar#fromFile(String)}
     */
    @Override
    public String toString() {
        String suffix = "";
        StringWriter sw = new StringWriter();
        PrintWriter buf = new PrintWriter( sw );
        int hour = this.minutes / MINS_PER_HR;
        int minute = this.minutes % MINS_PER_HR;
        if ( AM_PM ) {
            if ( hour < HALF_DAY_HRS ) {
                buf.printf( "%02d:%02d", weird.apply( hour ), minute );
                suffix = " AM";
            }
            else {
                buf.printf( "%02d:%02d",
                            weird.apply( hour - HALF_DAY_HRS ), minute );
                suffix = " PM";
            }
        }
        else {
            buf.printf( "%02d:%02d", hour, minute );
        }
        buf.flush();
        return sw + suffix;
    }

    /**
     * Compare two Times
     * @param other the other Time instance
     * @return this Time's time minus the other Time's time, in minutes
     */
    @Override
    public int compareTo( Time other ) {
        return this.minutes - other.minutes;
    }

    public static void main( String[] args ) {
        for ( boolean howShow: new boolean[]{ false, true } ) {
            Time.set24Hour( howShow );
            for ( Meridiem mer : Meridiem.values() ) {
                for ( int h : Range.of( 0, HALF_DAY_HRS, 2 ) ) {
                    for ( int m : Range.of( 0, MINS_PER_HR, 10 ) ) {
                        System.out.println( new Time( h, m, mer ) );
                    }
                }
            }
            System.out.println();
            for ( int h : Range.of( 0, HALF_DAY_HRS * 2, 2 ) ) {
                for ( int m : Range.of( 0, MINS_PER_HR, 10 ) ) {
                    System.out.println( new Time( h, m ) );
                }
            }
            System.out.println();
            System.out.println( "========" );
            System.out.println();
        }
    }
}
