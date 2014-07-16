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
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.logging.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author Sam Tulip
 */
public class SerializableUtility {

    private static final Logger LOG = LogManager.getLogger(SerializableUtility.class);

    public static boolean isSerializable2(Object object) {
        boolean serializable = Serializable.class.isInstance(object);
        if (serializable) {
            Class clazz = object.getClass();
            Field[] fields = clazz.getFields();
            for (Field field : fields) {
                try {
                    field.setAccessible(true);
                    Object sub = field.get(object);
                    serializable = isSerializable2(sub);
                    if (!serializable) {
                        return serializable;
                    }
                } catch (IllegalArgumentException | IllegalAccessException ex) {
                    LOG.error("Could not access " + field.getName() + " of object " + clazz.getName(), ex);
                }
            }
        }
        return serializable;
    }

    public static boolean isSerializable(Object object) {
        ObjectOutputStream out;
        try {
            out = new ObjectOutputStream(new NoopOutputStream());
            out.writeObject(object);
        } catch (NotSerializableException | InvalidClassException e) {
            //we can't serialize this object
            return false;
        } catch (IOException ex) {
            //should never happen
            LOG.fatal("Failed to check serializable status");
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
