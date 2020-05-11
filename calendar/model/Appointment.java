package calendar.model;


/**
 * Appointment Class is used to keep track of appointments and represent them in terms of date, time and description.
 * They are later used in Calendar where basically each instance of appointment is created using Appointment class.
 *
 * @author Abhishek Yadav
 */
public class Appointment implements Comparable{
    private int date;
    private Time time;
    private String what;
    private boolean removed;


    /**
     * Create an Appointment object and assign its date time and string
     * @param date integer value from 1-31
     * @param time a Time objected created using Time class
     * @param what a string describing the appointment
     */
    public Appointment(int date,Time time, String what){
        this.date=date;
        this.time=time;
        this.what=what;
        this.removed = false;

    }

    /**
     * Compare the time of this instance with other appointment
     * @param other appointment object for which we will compare
     * @return 0 if the two are the same , -1 if this instace is before the other and 1 if this instace
     * comes after the other.
     */
    @Override
    public int compareTo(Object other) {
        if (other instanceof Appointment) {
            Appointment app = (Appointment) other;
            if (app.date < this.date) {
                return 1;

            } else if (app.date == this.date) {
                return this.time.compareTo(app.time);

            } else {
                return -1;
            }

        }
        return -1;
    }

    /**
     * Compares to Appointment objects on the basis of their 'What' , date and time.
     * @param other instance of appointment
     * @return true if the attributes are all same, false otherwise.
     */
    @Override
    public boolean equals(Object other){
        if(other instanceof Appointment) {
            Appointment oth = (Appointment) other;

            return (this.time.equals(oth.time) && this.getDate() == oth.getDate() && this.what.equals(oth.what));
        }
        return false;
    }


    /**
     * This method is used to return the description of the appointment
     * @return string, returns what the appointment is.
     */
    public String getText(){
        return this.what;

    }


    /**
     * This method is used to take dates of various format and convert them into military time for comparison.
     * @param inputLine line of string representing the time
     * @return an appointment object
     */
    public static Appointment fromString(String inputLine){

        System.out.println(inputLine);
        String meridian="NONE";
        String[] separated = inputLine.split(",");
        String[] time_separated = separated[1].split(":");
        String[] minute_separated = time_separated[1].split(" ");
        Appointment appointment;



        try {
            if (minute_separated[1].equals("AM") ||
                    minute_separated[1].equals("PM")) {
                meridian = minute_separated[1];

            }
        }catch (IndexOutOfBoundsException e){

        }

        if(meridian.equals("NONE")) {
            Time time = new Time(Integer.parseInt(time_separated[0]),Integer.parseInt(time_separated[1]));
             appointment = new Appointment(Integer.parseInt(separated[0])
                    , time, separated[2]);


        }else{
            Time time = new Time(Integer.parseInt(time_separated[0]), Integer.parseInt(minute_separated[0]),
                    Time.Meridiem.valueOf(meridian));

            appointment = new Appointment(Integer.parseInt(separated[0])
                    , time, separated[2]);


        }

        return appointment;

    }

    /**
     * This method returns the date of the appointment.
     * @return returns the date, an int
     */
    public int getDate(){
        return this.date;
    }

    /**
     * Return time of the object
     * @return time object
     */
    public Time getTime(){
        return this.time;
    }


    /**
     * compute the hashcode and return an int
     * @return integer value of hashcode
     */
    @Override
    public int hashCode(){
        return this.date*683+this.time.hashCode()+what.hashCode();
    }


    /**
     * Build and return a user friendly representation of the appointment
     * @return String referring to the appointment details
     */
    public String toString(){
        return "on "+ this.date+" "+this.time+"----"+this.what;

    }

    /**
     * Returns a comma-separated value of the appointment, used for storing in file
     * @return a CSV representation of the appointment
     */
    public String csvFormat(){

        StringBuilder sb = new StringBuilder();
        sb.append(this.getDate());
        sb.append(",");
        sb.append(this.getTime());
        sb.append(",");
        sb.append(this.getText());

        return sb.toString();

    }


    /**
     *A helper function used in KalGUI to figure out which appointment is to be removed.
     * @return
     */
    public boolean remove(){
        return this.removed;
    }

    /**
     * sets the removed boolean value of the instance object to x.
     * @param x
     */
    public void setRemoved(boolean x){
        this.removed=x;
    }



}
