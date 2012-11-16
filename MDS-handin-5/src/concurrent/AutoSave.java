package concurrent;

import java.io.*;
import java.util.concurrent.TimeUnit;
import javax.xml.bind.*;
import serialization.Cal;

public class AutoSave implements Runnable {

    private final Cal cal;
    private final int saveInterval;
    private final FileOutputStream out;
    

    public AutoSave(Cal cal, FileOutputStream out, int secondInterval) {        
        this.cal = cal;
        this.saveInterval = secondInterval;
        this.out = out;
    }

    @Override
    public void run() {
        while(true) {
            try {
                TimeUnit.SECONDS.sleep(saveInterval);
                writeCal();
            } catch (InterruptedException | JAXBException | FileNotFoundException ex) {
                System.out.println(ex);
            } 
        }
    }

    private void writeCal() throws JAXBException, FileNotFoundException {
        JAXBContext context = JAXBContext.newInstance(Cal.class);
        context.createMarshaller().marshal(cal, out);
    }
}
