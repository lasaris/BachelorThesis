package cz.muni.fgdovin.bachelorthesis.core;

import com.espertech.esper.client.ConfigurationException;
import com.espertech.esper.client.EPStatement;
import com.espertech.esper.client.EventType;
import com.espertech.esper.client.dataflow.EPDataFlowAlreadyExistsException;
import com.espertech.esper.client.dataflow.EPDataFlowInstantiationException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This class is used to control Esper,
 * manage exceptions and convert
 * user input to Esper-preferred
 * one and vice versa.
 *
 * @author Filip Gdovin
 * @version 9. 2. 2015
 */
@Service
public class EsperUserFriendlyServiceImpl implements EsperUserFriendlyService {

    private static final Logger logger = Logger.getLogger(EsperUserFriendlyServiceImpl.class);

    @Autowired
    private EsperService esperService;

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean addEventType(String eventName, Map<String, Object> schema) {
        try {
            this.esperService.addEventType(eventName, schema);
        } catch (ConfigurationException ex) {
            logger.warn("Error while adding new event type!" ,ex);
            return false;
        }
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean removeEventType(String eventName) {
        try {
            this.esperService.removeEventType(eventName);
        } catch (ConfigurationException ex) {
            logger.info("Error while removing event type!" ,ex);
            return false;
        }
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String showEventType(String eventName) {
        EventType myEvent = null;
        try {
            myEvent = this.esperService.showEventType(eventName);
        } catch (NullPointerException ex) {
            logger.warn("No event type with given name["+ eventName+"] was found.", ex);
            return "";
        }
        return myEvent.getName() + ":" + myEvent.getPropertyNames();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> showEventTypes() {
        EventType[] allEvents = this.esperService.showEventTypes();
        if((allEvents == null) || (allEvents.length == 0)) {
            return null;
        }
        List<String> result = new ArrayList<String>();
        for(int i = 0; i < allEvents.length; i++) {
            result.add(showEventType(allEvents[i].getName()));
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean addDataflow(String dataflowName, String dataflowProperties) {
        boolean result = true;
        try {
            this.esperService.addDataflow(dataflowName, dataflowProperties);
        } catch (NullPointerException ex) {
            logger.warn("Failed to create dataflow!", ex);
            result = false;
        } catch (EPDataFlowInstantiationException ex) {
            logger.error("Failed to instantiate new dataflow!", ex);
            result = false;
        } catch (EPDataFlowAlreadyExistsException ex) {
            logger.error("Failed to save new dataflow instance, which causes it to be unreachable by its name!", ex);
            result = false;
        } catch (IllegalStateException ex) {
            logger.error("Failed to start new dataflow!", ex);
            result = false;
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean removeDataflow(String dataflowName) {
        try {
            this.esperService.removeDataflow(dataflowName);
        } catch (NullPointerException ex) {
            logger.info("Failed to remove dataflow " + dataflowName + ".", ex);
            return false;
        }
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String showDataflow(String dataflowName) {
        EPStatement myDataflow = this.esperService.showDataflow(dataflowName);
        if(myDataflow == null) {
            return null;
        }
        return myDataflow.getName() + "[" + myDataflow.getState() + "]:\n\"" + myDataflow.getText() + "\"\n";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> showDataflows() {
        String[] allDataFlows = this.esperService.showDataflows();
        List<String> result = new ArrayList<String>();

        if((allDataFlows == null) || (allDataFlows.length == 0)) {
            logger.info("No dataflows present.");
            return result;
        }

        //in case we would like to receive more detailed info sometimes
        for(int i = 0; i < allDataFlows.length; i++) {
            result.add(showDataflow(allDataFlows[i]));
        }
        return result;
    }
}
