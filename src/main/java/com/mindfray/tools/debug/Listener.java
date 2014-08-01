/*
 * Copyright 2014 Sam Tulip.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mindfray.tools.debug;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;
import org.apache.log4j.Logger;

/**
 * 
 * HttpSessionAttributeListener automatically bound in at start up but the 
 * @WebListener annotation. Tracks when objects are added, removed or replace
 * in the session logging the stack trace of the event.
 * <br/><br/>
 * Also checks if the object saved in the session is serializable.
 * <br/><br/>
 * If the logging level is set to debug always logs the stack trace.
 * <br/>
 * If the logging level is set to info always logs when a object is added, removed
 * or replaced in the session, but only logs the stack trace if the object is not
 * serializable.
 * <br/>
 * If the logging level is set to warn only prints the event when an object is
 * not serializable. Also adds the stack trace.
 * 
 * @author Sam Tulip
 */
@WebListener
public class Listener implements HttpSessionAttributeListener {

    private static final Logger LOG = Logger.getLogger(Listener.class);

    /**
     * Logs when an attribute is added. Checks if the object is serializable.
     * @param event 
     */
    @Override
    public void attributeAdded(HttpSessionBindingEvent event) {
        this.log("Added", event.getName(), SerializableUtility.isSerializable(event.getValue()));
    }

    /**
     * Logs when an attribute is added. Checks if the object is serializable.
     * @param event 
     */
    @Override
    public void attributeRemoved(HttpSessionBindingEvent event) {
        this.log("Removed", event.getName(), SerializableUtility.isSerializable(event.getValue()));
    }

    /**
     * Logs when an attribute is added. Checks if the object is serializable.
     * @param event 
     */
    @Override
    public void attributeReplaced(HttpSessionBindingEvent event) {
        this.log("Changed", event.getName(), SerializableUtility.isSerializable(event.getValue()));
    }

    /**
     * Method to do the bulk of the work. The event type is passed from the 
     * other method in this class. Attribute name is always the name from the 
     * event object and serializable is passed from the SerializableUtility.
     * @param eventType
     * @param attributeName
     * @param serializable 
     */
    private void log(final String eventType, final String attributeName, final boolean serializable) {
        final String logString = eventType + " [name=" + attributeName + ",serializable=" + serializable + "]";
        System.out.println( this.getStackTrace(new StringBuilder(logString)).toString() );
        if (LOG.isDebugEnabled()) {
            LOG.debug(this.getStackTrace(new StringBuilder(logString)).toString());
        } else if (LOG.isInfoEnabled()) {
            if( !serializable) {
                LOG.warn(this.getStackTrace(new StringBuilder(logString)).toString());
            } else {
                LOG.info(logString);
            }
        } else if (!serializable) {
            LOG.warn(this.getStackTrace(new StringBuilder(logString)).toString());
        }
    }

    /**
     * Simple Utility method to get the stack trace and append it to the string builder
     * @param sb The StringBuilder to append the stack trace to.
     * @return The StringBuilder passed in as sb.
     */
    private StringBuilder getStackTrace(final StringBuilder sb) {
        final Exception exception = new Exception();
        StackTraceElement[] stackTrace = exception.getStackTrace();
        sb.append(" @ ").append( System.lineSeparator() );
        for (int i = 3; i < stackTrace.length; i++) {
            StackTraceElement element = stackTrace[i];
            sb.append(element.toString()).append( System.lineSeparator() );
        }
        return sb;
    }

}
