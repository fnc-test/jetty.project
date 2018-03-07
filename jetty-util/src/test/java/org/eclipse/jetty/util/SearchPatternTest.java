//
//  ========================================================================
//  Copyright (c) 1995-2018 Mort Bay Consulting Pty. Ltd.
//  ------------------------------------------------------------------------
//  All rights reserved. This program and the accompanying materials
//  are made available under the terms of the Eclipse Public License v1.0
//  and Apache License v2.0 which accompanies this distribution.
//
//      The Eclipse Public License is available at
//      http://www.eclipse.org/legal/epl-v10.html
//
//      The Apache License v2.0 is available at
//      http://www.opensource.org/licenses/apache2.0.php
//
//  You may elect to redistribute this code under either of these licenses.
//  ========================================================================
//

package org.eclipse.jetty.util;

import static org.junit.Assert.*;

import java.nio.charset.StandardCharsets;

import org.junit.Assert;
import org.junit.Test;

public class 

{

    
    @Test
    public void testBasicSearch()
    {
        byte[] p1 = new String("truth").getBytes(StandardCharsets.US_ASCII);
        byte[] p2 = new String("evident").getBytes(StandardCharsets.US_ASCII);
        byte[] p3 = new String("we").getBytes(StandardCharsets.US_ASCII);
        byte[] d = new String("we hold these truths to be self evident").getBytes(StandardCharsets.US_ASCII);
        
        // Testing Compiled Pattern p1 "truth"
        SearchPattern sp1 = SearchPattern.compile(p1);
        Assert.assertEquals(14,sp1.match(d, 0, d.length));        
        Assert.assertEquals(14,sp1.match(d,14,p1.length));        
        Assert.assertEquals(14,sp1.match(d,14,p1.length+1));
        Assert.assertEquals(-1,sp1.match(d,14,p1.length-1));        
        Assert.assertEquals(-1,sp1.match(d,15,d.length-15));
        
        // Testing Compiled Pattern p2 "evident"
        SearchPattern sp2 = SearchPattern.compile(p2);        
        Assert.assertEquals(32,sp2.match(d, 0, d.length));        
        Assert.assertEquals(32,sp2.match(d,32,p2.length));        
        Assert.assertEquals(32,sp2.match(d,32,p2.length));        
        Assert.assertEquals(-1,sp2.match(d,32,p2.length-1));        
        Assert.assertEquals(-1,sp2.match(d,33,d.length-33));
        
        // Testing Compiled Pattern p3 "evident"
        SearchPattern sp3 = SearchPattern.compile(p3);   
        Assert.assertEquals( 0,sp3.match(d, 0, d.length));
        Assert.assertEquals( 0,sp3.match(d, 0, p3.length));
        Assert.assertEquals( 0,sp3.match(d, 0, p3.length+1));
        Assert.assertEquals(-1,sp3.match(d, 0, p3.length-1));
        Assert.assertEquals(-1,sp3.match(d, 1, d.length-1));
        
    }
    
    
    @Test
    public void testDoubleMatch()
    {
        byte[] p = new String("violent").getBytes(StandardCharsets.US_ASCII);
        byte[] d = new String("These violent delights have violent ends.").getBytes(StandardCharsets.US_ASCII);
        SearchPattern sp = SearchPattern.compile(p);
        Assert.assertEquals( 6,sp.match(d, 0, d.length));
        Assert.assertEquals(-1,sp.match(d, 6, p.length-1));
        Assert.assertEquals(28,sp.match(d, 7, d.length-7));
        Assert.assertEquals(28,sp.match(d, 28, d.length-28));
        Assert.assertEquals(-1,sp.match(d, 29, d.length-29));
    }

    
    @Test
    public void testAlmostMatch()
    {
        byte[] p = new String("violent").getBytes(StandardCharsets.US_ASCII);
        byte[] d = new String("vio lent violen v iolent violin vioviolenlent viiolent").getBytes(StandardCharsets.US_ASCII);
        SearchPattern sp = SearchPattern.compile(p);
        Assert.assertEquals(-1,sp.match(d, 0, d.length));
    }
    
    
    @Test
    public void testOddSizedPatterns()
    {
        // Test Large Pattern
        byte[] p = new String("pneumonoultramicroscopicsilicovolcanoconiosis").getBytes(StandardCharsets.US_ASCII);
        byte[] d = new String("pneumon").getBytes(StandardCharsets.US_ASCII);
        SearchPattern sp = SearchPattern.compile(p);
        Assert.assertEquals(-1,sp.match(d, 0, d.length));
        
        // Test Single Character Pattern
        p = new String("s").getBytes(StandardCharsets.US_ASCII);
        d = new String("the cake is a lie").getBytes(StandardCharsets.US_ASCII);
        sp = SearchPattern.compile(p);
        Assert.assertEquals(10,sp.match(d, 0, d.length));
    }
    
    
    @Test
    public void testEndsWith()
    {
        byte[] p = new String("pneumonoultramicroscopicsilicovolcanoconiosis").getBytes(StandardCharsets.US_ASCII);
        byte[] d = new String("pneumonoultrami").getBytes(StandardCharsets.US_ASCII);
        SearchPattern sp = SearchPattern.compile(p);
        Assert.assertEquals(15,sp.endsWith(d,0,d.length));
        
        p = new String("abcdefghijklmnopqrstuvwxyz").getBytes(StandardCharsets.US_ASCII);
        d = new String("abcdefghijklmnopqrstuvwxyzabcdefghijklmno").getBytes(StandardCharsets.US_ASCII);
        sp = SearchPattern.compile(p);
        Assert.assertEquals(0,sp.match(d,0,d.length));
        Assert.assertEquals(-1,sp.match(d,1,d.length-1));
        Assert.assertEquals(15,sp.endsWith(d,0,d.length));
        
        p = new String("abcdefghijklmnopqrstuvwxyz").getBytes(StandardCharsets.US_ASCII);
        d = new String("abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz").getBytes(StandardCharsets.US_ASCII);
        sp = SearchPattern.compile(p);
        Assert.assertEquals(0,sp.match(d,0,d.length));
        Assert.assertEquals(26,sp.match(d,1,d.length-1));
        Assert.assertEquals(26,sp.endsWith(d,0,d.length));
        
        //test no match
        p = new String("hello world").getBytes(StandardCharsets.US_ASCII);
        d = new String("there is definitely no match in here").getBytes(StandardCharsets.US_ASCII);
        sp = SearchPattern.compile(p);
        Assert.assertEquals(0,sp.endsWith(d,0,d.length));
    }
    
    
    @Test
    public void testStartsWith()
    {
        byte[] p = new String("abcdefghijklmnopqrstuvwxyz").getBytes(StandardCharsets.US_ASCII);
        byte[] d = new String("ijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz").getBytes(StandardCharsets.US_ASCII);
        SearchPattern sp = SearchPattern.compile(p);
        Assert.assertEquals(18,sp.match(d,0,d.length));
        Assert.assertEquals(-1,sp.match(d,19,d.length-19));
        Assert.assertEquals(18,sp.startsWith(d,0,d.length,8));
        
        p = new String("abcdefghijklmnopqrstuvwxyz").getBytes(StandardCharsets.US_ASCII);
        d = new String("ijklmnopqrstuvwxyz abcdefghijklmnopqrstuvwxyz").getBytes(StandardCharsets.US_ASCII);
        sp = SearchPattern.compile(p);
        Assert.assertEquals(19,sp.match(d,0,d.length));
        Assert.assertEquals(-1,sp.match(d,20,d.length-20));
        Assert.assertEquals(18,sp.startsWith(d,0,d.length,8));
        
        p = new String("abcdefghijklmnopqrstuvwxyz").getBytes(StandardCharsets.US_ASCII);
        d = new String("abcdefghijklmnopqrstuvwxyz abcdefghijklmnopqrstuvwxyz").getBytes(StandardCharsets.US_ASCII);
        sp = SearchPattern.compile(p);
        Assert.assertEquals(26,sp.startsWith(d,0,d.length,0));
        
        //test no match
        p = new String("hello world").getBytes(StandardCharsets.US_ASCII);
        d = new String("there is definitely no match in here").getBytes(StandardCharsets.US_ASCII);
        sp = SearchPattern.compile(p);
        Assert.assertEquals(0,sp.startsWith(d,0,d.length,0));  
        
        //test large pattern small buffer
        p = new String("abcdefghijklmnopqrstuvwxyz").getBytes(StandardCharsets.US_ASCII);
        d = new String("mnopqrs").getBytes(StandardCharsets.US_ASCII);
        sp = SearchPattern.compile(p);
        Assert.assertEquals(7,sp.startsWith(d,0,d.length,12));  
    }
}
