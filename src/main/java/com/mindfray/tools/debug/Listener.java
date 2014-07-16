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

import java.io.PrintStream;
import java.io.PrintWriter;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author Sam Tulip
 */
@WebListener
public class Listener implements HttpSessionAttributeListener {

    private static final Logger LOG = LogManager.getLogger(Listener.class);

    @Override
    public void attributeAdded(HttpSessionBindingEvent event) {
        this.log("Added", event.getName(), SerializableUtility.isSerializable(event.getValue()));
    }

    @Override
    public void attributeRemoved(HttpSessionBindingEvent event) {
        this.log("Removed", event.getName(), SerializableUtility.isSerializable(event.getValue()));
    }

    @Override
    public void attributeReplaced(HttpSessionBindingEvent event) {
        this.log("Changed", event.getName(), SerializableUtility.isSerializable(event.getValue()));
    }

    private void log(final String eventType, final String attributeName, final boolean serializable) {
        final String logString = eventType + " [name=" + attributeName + ",serializable=" + serializable + "]";
        System.out.println( this.getStackTrace(new StringBuilder(logString)) );
        if (LOG.isDebugEnabled()) {
            LOG.debug(this.getStackTrace(new StringBuilder(logString)));
        } else if (LOG.isInfoEnabled()) {
            LOG.info(logString);
        } else if (LOG.isWarnEnabled() && !serializable) {
            LOG.warn(this.getStackTrace(new StringBuilder(logString)));
        }
    }

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
