package com.vaadin.tutorial.addressbook.backend;

import org.apache.commons.beanutils.BeanUtils;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/** Separate Java service class.
 * Backend implementation for the address book application, with "detached entities"
 * simulating real world DAO. Typically these something that the Java EE
 * or Spring backend services provide.
 */
// Backend service class. This is just a typical Java backend implementation
// class and nothing Vaadin specific.
public class ContactService {

    // Create dummy data by randomly combining first and last names
    static String[] fnames = { "Peter", "Alice", "John", "Mike", "Olivia",
            "Nina", "Alex", "Rita", "Dan", "Umberto", "Henrik", "Rene", "Lisa",
            "Linda", "Timothy", "Daniel", "Brian", "George", "Scott",
            "Jennifer" };
    static String[] lnames = { "Smith", "Johnson", "Williams", "Jones",
            "Brown", "Davis", "Miller", "Wilson", "Moore", "Taylor",
            "Anderson", "Thomas", "Jackson", "White", "Harris", "Martin",
            "Thompson", "Young", "King", "Robinson" };
    // create dummy data for tasks
    static String[] ftask = { "Go get sandals with ", "Destroy the system made by ", "Find the droids we are looking for before ", "Dance all day with  ",
            "Go find lady luck for ", "Defeat the last boss of SuperContra and show ", "Test the software made by ", "Watch the hangover again, like your were told to by ", "Buy a new Calculator for ", "Go to lunch with ",
            "Watch a Cat named ", "Sneak out for dinner and meet up with ", "Purchase a new couch from ", "Sleep happy dreams about ", "Find a four-leafed clover and name it ", "Purchase a kitkat, eat it in front of ",
            "Sing in the shower about ", "Borrow 50$, invest it all, lose it all and give back a cookie to ", "Become the King of France becausec of", "Become a Werewolf like " };
  
    private static ContactService instance;

    public static ContactService createDemoService() {
        if (instance == null) {

            final ContactService contactService = new ContactService();

            Random r = new Random(0);
            //Calendar cal = Calendar.getInstance();
            for (int i = 0; i < 100; i++) {
                
                long    ms = ran(r), ms2 = (ms/2);
                Date    dt = new Date(ms),dt2 = new Date(ms2);
                
                Contact contact = new Contact();
                contact.setFirstName(fnames[r.nextInt(fnames.length)]);
                contact.setLastName(lnames[r.nextInt(fnames.length)]);
                
                
                contact.setTask(ftask[r.nextInt(ftask.length)]+fnames[r.nextInt(fnames.length)]);
                
                
                // Create random dates for start and finsih
                if(ms>ms2) {
                contact.setStartDate(dt2);
                contact.setEndDate(dt);
                }
                else{
                contact.setStartDate(dt);
                contact.setEndDate(dt2);                
                }
                	
                contactService.save(contact);
            }
            instance = contactService;
        }

        return instance;
    }

    private HashMap<Long, Contact> contacts = new HashMap<>();
    private long nextId = 0;

    public synchronized List<Contact> findAll(String stringFilter) {
        ArrayList arrayList = new ArrayList();
        for (Contact contact : contacts.values()) {
            try {
                boolean passesFilter = (stringFilter == null || stringFilter.isEmpty())
                        || contact.toString().toLowerCase()
                                .contains(stringFilter.toLowerCase());
                if (passesFilter) {
                    arrayList.add(contact.clone());
                }
            } catch (CloneNotSupportedException ex) {
                Logger.getLogger(ContactService.class.getName()).log(
                        Level.SEVERE, null, ex);
            }
        }
        Collections.sort(arrayList, new Comparator<Contact>() {

            @Override
            public int compare(Contact o1, Contact o2) {
                return (int) (o2.getId() - o1.getId());
            }
        });
        return arrayList;
    }

    public synchronized long count() {
        return contacts.size();
    }

    public synchronized void delete(Contact value) {
        contacts.remove(value.getId());
    }

    public synchronized void save(Contact entry) {
        if (entry.getId() == null) {
            entry.setId(nextId++);
        }
        try {
            entry = (Contact) BeanUtils.cloneBean(entry);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        contacts.put(entry.getId(), entry);
    }
    //  ran used to generate random long for random dates
    // researched from http://stackoverflow.com/questions/3985392/generate-random-date-of-birth
    public synchronized static long ran(Random a){
    	
        long    ms = -946771200000L + (Math.abs(a.nextLong()) % (70L * 365 * 24 * 60 * 60 * 1000));
        return ms;
   
    }

}
