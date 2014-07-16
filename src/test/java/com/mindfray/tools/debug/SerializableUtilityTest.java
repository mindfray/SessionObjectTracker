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

import java.io.Serializable;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.Assert;

/**
 *
 * @author Sam Tulip
 */
public class SerializableUtilityTest extends SerializableUtility {

    private static class SerializableObject implements Serializable {
        
    }
    
    private static class UnSerializableObject {
        
    }
    
    private static class ContainerObject implements Serializable{
        
        private final SerializableObject privateSerializableField;
        
        public ContainerObject() {
            this.privateSerializableField = new SerializableObject();
        }
    }
    
    private static class ContainerObjectTwo implements Serializable {
        
        private final UnSerializableObject privateUnSerializableField;
        
        public ContainerObjectTwo() {
            this.privateUnSerializableField = new UnSerializableObject();
        }
    }
    
    private static class ContainerObjectThree implements Serializable{
        
        private final ContainerObject object;
        
        public ContainerObjectThree() {
            this.object = new ContainerObject();
        }
    }
    
    private static class ContainerObjectFour implements Serializable{
        
        private final ContainerObjectTwo object;
        
        public ContainerObjectFour() {
            this.object = new ContainerObjectTwo();
        }
    }
    
    /**
     * Test of isSerializable method, of class SerializableUtility.
     */
    @Test
    public void testIsSerializable() {
        System.out.println("isSerializable");
        Assert.assertEquals(true, SerializableUtility.isSerializable( new SerializableObject() ));
        Assert.assertEquals(false, SerializableUtility.isSerializable( new UnSerializableObject() ));
        Assert.assertEquals(true, SerializableUtility.isSerializable( new ContainerObject() ));
        Assert.assertEquals(false, SerializableUtility.isSerializable( new ContainerObjectTwo()));
        Assert.assertEquals(true, SerializableUtility.isSerializable( new ContainerObjectThree() ));
        Assert.assertEquals(false, SerializableUtility.isSerializable( new ContainerObjectFour()));
    }
    
}
