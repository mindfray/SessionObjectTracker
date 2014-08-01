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

import java.io.IOException;
import java.io.InvalidClassException;
import java.io.NotSerializableException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import org.apache.log4j.Logger;

/**
 *
 * @author Sam Tulip
 */
public class SerializableUtility {
    
    private static final Logger LOG = Logger.getLogger(SerializableUtility.class);

    /**
     * Checks if an object is Serializable
     * @param object The object we want to check if serializable
     * @return true if the object can be serialized.
     */
    public static boolean isSerializable(Object object) {
        /**
         * This method checks if an object is serializable by serializing it to
         * a special output stream designed to do nothing. There are performance
         * considerations serializing the object but as this tool is designed to 
         * aid debug the performance considerations can be ignored.
         */
        ObjectOutputStream out;
        try {
            out = new ObjectOutputStream(new NoopOutputStream());
            out.writeObject(object);
        } catch (NotSerializableException | InvalidClassException e) {
            //we can't serialize this object
            return false;
        } catch (IOException ex) {
            LOG.error("Failed to check serializable status", ex);
            throw new RuntimeException("Failed to check serializable status");
        }
        return true;
    }

    private static class NoopOutputStream extends OutputStream {

        @Override
        public void write(int b) throws IOException {
            //noop
        }

    }

}
