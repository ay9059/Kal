package calendar.model;

import edu.rit.cs.Observer;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;


/**
 * Calendar class is the backbone for representing a Calendar represntation. Its functionality inclues
 * adding and removing appointments.
 *
 *
 * @author Abhishek Yadav
 */

public class Calendar {


    /**
     * Static Calendar object is created so that it can be used in KalGUI.
     */
    private static Calendar kal;
    private int monthsize;
    /**
     * Create a list of observers to be used for Calendar class
     */
    private List<Observer<Calendar>> observers = new ArrayList<>();
    /**
     * After reading from a file, the file name is stored so that it can later be overwritten
     */
    private static String saveFile;
    /**
     * A HashMap linking the date to the appointment object is used for addition and removal of appointment.
     */
    private HashMap<Integer,ArrayList<Appointment>> appointments = new HashMap<>();


    /**
     * Constructor for a Calendar, takes in the number of days in the month
     * @param monthsize integer in the range 28-31
     */
    public Calendar(int monthsize){
        this.monthsize=monthsize;

    }

    /**
     * This class reads date, time and description of appointment from a file and also stores its name for future
     * reference in toFile.
     * @param fileName refers to the name of the file that is to be saved
     * @return a Calendar object that contains all the appointment details
     * @throws IOException if the file does not exist
     */

    public static Calendar fromFile(String fileName) throws IOException {

        saveFile = fileName;
        if (fileName.equals("NONE")) {
            saveFile=null;
            kal = new Calendar(28);
            return kal;


        } else {
            try {
                Scanner calFile = new Scanner(new File(fileName));
                int monthsize = calFile.nextInt();
                calFile.nextLine();
                kal = new Calendar(monthsize);

                while (calFile.hasNext()) {
                    String s = calFile.nextLine();
                    kal.add(Appointment.fromString(s));

                }
                return kal;

            } catch (NumberFormatException nfe) {
                throw new IOException(nfe);
            }
        }
    }


    /**
     * This method is used to write appointments to a file, the same one that was used to read the appointments
     * initially.
     * @throws IOException if the file does not exist.
     */

    public void toFile() throws IOException {

        if (this.saveFile == null) {
            throw new IOException("Calendar not loaded from a file");
        }
        try (PrintWriter calFile = new PrintWriter(this.saveFile)) {
            calFile.println(monthsize);
            for (int date = 1; date <= this.monthsize; date++) {
                for (Appointment appointment : appointmentsOn(date)) {
                    calFile.println(appointment.csvFormat());

                }
            }

        }
    }


    /**
     * Returns a list of appointments on the date that is specified by the user as a parameter
     * @param date an int value for which the appointments are to be returned
     * @return an arraylist of appointments
     */
    public ArrayList<Appointment> appointmentsOn(int date){
        try {
            Collections.sort(appointments.get(date));
            return this.kal.appointments.get(date);
        }catch (NullPointerException ne){
            ArrayList<Appointment> ap = new ArrayList<>();

            return ap;
        }

    }


    /**
     * This method creates an Observer and adds it to the list of observers in calendar.
     * @param observer
     */
    public void addObserver(Observer<Calendar> observer){
     this.observers.add(observer);


    }


    /**
     * Notifies the observer if a change is detected, this is used in KalGUI class.
     * @param ob referring to Observer
     */
    private void notifyObserver(Object ob){
        if(ob instanceof Observer){
            Observer observer = (Observer) ob;
            observer.update(this,observer);
        }
    }

    /**
     * Used in KalGUI to return the number of days in the model
     * @return
     */
    public int numDays(){
        return this.monthsize;
    }


    /**
     * One of two add methods that adds the specified appointment paramter to the calendar
     * @param appointment the appointment that is to be added.
     */
    public void add(Appointment appointment){
        if(appointments.get(appointment.getDate())!=null) {
            appointments.get(appointment.getDate()).add(0,appointment);
        }else{
            ArrayList<Appointment> a = new ArrayList<>();
            a.add(appointment);

            appointments.put(appointment.getDate(),a);

        }

        notifyObserver(appointment);
    }

    /**
     * The primary add method that takes in the date, time and description of the appointment and adds it to the
     * calendar
     *
     * @param date referring to the date of the appointment
     * @param time time of the appointment
     * @param what description of the appointment
     */
    public void add(int date, Time time, String what){

        Appointment appointment = new Appointment(date,time,what);
        System.out.println("on "+date+" "+time+" "+what);
        add(appointment);
        notifyObserver(appointment);
    }

    /**
     * Removes appointment from the Calendar
     * @param toremove appoinrment that is to be removed
     */
    public void remove(Appointment toremove){

       toremove.setRemoved(true);

       int toRemoveDate= toremove.getDate();
        ArrayList <Appointment> appointment=appointments.get(toRemoveDate);
        ArrayList<Appointment> new_appointment = new ArrayList<>();

       for(int i=0;i<appointments.get(toRemoveDate).size();i++){
           if(appointment.get(i).getTime().compareTo(toremove.getTime())!=0){
               new_appointment.add(appointment.get(i));

           }else{
               //
           }
       }

       appointments.put(toRemoveDate,new_appointment);
        //NOTIFY THE OBSERVER AS YOU REMOVE AN APPOINTMENT
       notifyObserver(toremove);

    }
    /**
     * Return the size of the month in the calendar, used in KalGUI
     * @return
     */
    public int getMonthsize(){
        return this.monthsize;
    }



}



