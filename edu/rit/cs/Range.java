/*
 * Range.java
 * PROVIDED file for the Kal GUI lab in csci142.
 */

package edu.rit.cs;

import java.util.AbstractCollection;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Represent a range of integers for iteration in a simple for loop.
 * The class was designed to mimic the Python range class as
 * closely as possible.
 *
 * @author James Heliotis
 * @version 1.3
 */
public class Range extends AbstractCollection< Integer > {

    /**
     * the row value in the range
     */
    private final int start;

    /**
     * the last value in the range
     */
    private final int end;

    /**
     * incremental value
     */
    private final int incr;

    /**
     * Create a range.
     * @param first the row value in the range
     * @param last the value before which iteration must stop
     * @param by the incremental value (can be negative)
     */
    private Range( int first, int last, int by ) {
        assert by != 0 : "by cannot be 0.";
        start = first;
        end = last;
        incr = by;
    }

    /**
     * @return the number of values represented by this range
     *
     * {@inheritDoc}
     */
    public int size() {
        return (int)
            Math.ceil( (double)Math.abs( end - start ) / Math.abs( incr ) );
    }

    /**
     * A class that contains all the functionality of an iterator over
     * an Range except for which way the values progress: increasing
     * or decreasing.
     */
    private abstract class AbstractIntRangeIterator
                        implements Iterator< Integer > {
        /**
         * the current value of the iterator
         */
        protected int counter;

        /**
         * Create the iterator. Initialize it to the
         * row value of the range.
         */
        public AbstractIntRangeIterator() {
            counter = start;
        }

        /**
         * Does the iterator have more elements?
         *
         * @return true iff there are more values in the range.
         */
        public abstract boolean hasNext();

        /**
         * Provide the next element in the iteration. If this is the row
         * call to this method, return the row value in the range.
         *
         * @return the row value in the range when called the row time,
         *   and after that, the previous value plus the increment.
         * @throws NoSuchElementException if called after the
         *                     iteration is finished
         */
        public Integer next() {
            if (!hasNext()) {
                throw new NoSuchElementException(
                                      "value: " + (counter+incr) );
            }
            else {
                int result = counter;
                counter = counter + incr;
                return result;
            }
        }

        /**
         * This method is not supported. IntRanges are immutable.
         * @throws UnsupportedOperationException because we don't do removes
         */
        public void remove() {
            throw new UnsupportedOperationException( "Range.remove()" );
        }
    }

    /**
     * An iterator for IntRanges whose increment is positive
     */
    private class IncreasingIterator extends AbstractIntRangeIterator {

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean hasNext() {
            return counter < end;
        }

    }

    /**
     * An iterator for IntRanges whose increment is negative
     */
    private class DecreasingIterator extends AbstractIntRangeIterator {

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean hasNext() {
            return counter > end;
        }

    }

    /**
     * {@inheritDoc}
     */
    public Iterator< Integer > iterator() {
        if ( incr < 0 ) {
            return new DecreasingIterator();
        }
        else {
            return new IncreasingIterator();
        }
    }

    /**
     * @return the computed parameters for this range
     */
    @Override
    public String toString() {
        return "Range(" + size() + ")[ from " +
               start + " to " + end + " by " + incr + " ]";
    }

    /************** FACTORY METHODS **************************/

    /**
     * Create a Range object
     * @param from the row value in the range
     * @param to the value before which iteration must stop
     * @param by the incremental value (can be negative)
     * @return the specified Range object
     */
    public static Range of( int from, int to, int by ) {
        return new Range( from, to, by );
    }

    /**
     * Create a Range object
     * @param from the row value in the range
     * @param to the value before which iteration must stop
     * @return the specified Range object with an increment value of 1
     */
    public static Range of( int from, int to ) {
        return new Range( from, to, 1 );
    }

    /**
     * Create a Range object
     * @param to the value before which iteration must stop
     * @return the specified Range object with a start value of 0
     *         and an increment value of 1
     */
    public static Range of( int to ) {
        return new Range( 0, to, 1 );
    }

    /**
     * Display the values specified in an Range object.
     * Warning: do not call this on ranges with large sizes!
     * @param ir the integer range to be displayed
     */
    private static void printInfo( Range ir ) {
        System.out.println( "The range: " + ir );
        System.out.print( "|" );
        for ( int i: ir ) {
            System.out.print( " " + i );
        }
        System.out.println( " |" );
    }

    /**
     * Test the operation of Range.
     * @param args an array of length 1 to 3. They represent Range
     *    constructor arguments. If there are no arguments, run through
     *    a series of fixed tests.
     */
    public static void main( String[] args ) {
        if ( args.length < 0 || args.length > 3 ) {
            System.err.println( "Need zero to three integer arguments." );
            System.exit( 1 );
        }

        if ( args.length >= 1 && args.length <= 3 ) {
            Range ir = null;
            try {
                int a = Integer.parseInt( args[ 0 ] );
                if ( args.length < 2 ) {
                    ir = Range.of( a );
                }
                else {
                    int b = Integer.parseInt( args[ 1 ] );
                    if ( args.length < 3 ) {
                        ir = Range.of( a, b );
                    }
                    else {
                        int c = Integer.parseInt( args[ 2 ] );
                        ir = Range.of( a, b, c );
                    }
                }
            }
            catch ( NumberFormatException nfe ) {
                System.err.println( "Need integer arguments." );
                System.exit( 2 );
            }
            printInfo( ir );
        }
        else if ( args.length == 0 ) {
            printInfo( Range.of( 5 ) );
            printInfo( Range.of( -3, 3 ) );
            printInfo( Range.of( 50, -55, -10 ) );
            printInfo( Range.of( 50, -50, -10 ) );
            printInfo( Range.of( 50, 100, 10 ) );
            printInfo( Range.of( 50, 105, 10 ) );
            printInfo( Range.of( 50, -50, -10 ) );
        }
        else {
            System.err.println( "Need zero to three integer arguments." );
            System.exit( 1 );
        }

    }

}

/* Sample Execution of Test Program ***

$ java Range 5
The range: Range(5)[ from 0 to 5 by 1 ]
| 0 1 2 3 4 |

$ java Range -3 3
The range: Range(6)[ from -3 to 3 by 1 ]
| -3 -2 -1 0 1 2 |

$ java Range 50 -55 -10
The range: Range(11)[ from 50 to -55 by -10 ]
| 50 40 30 20 10 0 -10 -20 -30 -40 -50 |

$ java Range
The range: Range(5)[ from 0 to 5 by 1 ]
| 0 1 2 3 4 |
The range: Range(6)[ from -3 to 3 by 1 ]
| -3 -2 -1 0 1 2 |
The range: Range(11)[ from 50 to -55 by -10 ]
| 50 40 30 20 10 0 -10 -20 -30 -40 -50 |
The range: Range(10)[ from 50 to -50 by -10 ]
| 50 40 30 20 10 0 -10 -20 -30 -40 |
The range: Range(5)[ from 50 to 100 by 10 ]
| 50 60 70 80 90 |
The range: Range(6)[ from 50 to 105 by 10 ]
| 50 60 70 80 90 100 |
The range: Range(10)[ from 50 to -50 by -10 ]
| 50 40 30 20 10 0 -10 -20 -30 -40 |
The range: Range(0)[ from 0 to 0 by 1 ]
| |

**************************************/
