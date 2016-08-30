/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2016, Telestax Inc and individual contributors
 * by the @authors tag. 
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
        
package org.mobicents.media.control.mgcp.pkg.au.pc;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import org.junit.AfterClass;
import org.junit.Test;
import org.mobicents.media.control.mgcp.pkg.MgcpEvent;
import org.mobicents.media.control.mgcp.pkg.MgcpEventObserver;
import org.mobicents.media.control.mgcp.pkg.au.ReturnCode;
import org.mobicents.media.server.impl.resource.dtmf.DtmfEventImpl;
import org.mobicents.media.server.spi.dtmf.DtmfDetector;
import org.mobicents.media.server.spi.player.Player;
import org.mockito.ArgumentCaptor;

import com.google.common.util.concurrent.ListeningScheduledExecutorService;
import com.google.common.util.concurrent.MoreExecutors;

/**
 * @author Henrique Rosa (henrique.rosa@telestax.com)
 *
 */
public class PlayCollectTest {
    
    private static final ScheduledExecutorService threadPool = Executors.newScheduledThreadPool(1);

    @AfterClass
    public static void cleanup() {
        threadPool.shutdown();
    }

    @Test
    public void testCollectWithEndInputKey() throws InterruptedException {
        // given
        final Map<String, String> parameters = new HashMap<>(5);
        parameters.put("mx", "100");
        parameters.put("eik", "A");

        final Player player = mock(Player.class);
        final DtmfDetector detector = mock(DtmfDetector.class);
        final ListeningScheduledExecutorService executor = MoreExecutors.listeningDecorator(threadPool);
        final MgcpEventObserver observer = mock(MgcpEventObserver.class);
        final PlayCollect pc = new PlayCollect(player, detector, parameters, executor);

        // when
        final ArgumentCaptor<MgcpEvent> eventCaptor = ArgumentCaptor.forClass(MgcpEvent.class);

        pc.observe(observer);
        pc.execute();

        pc.detectorListener.process(new DtmfEventImpl(detector, "1", -30));
        pc.detectorListener.process(new DtmfEventImpl(detector, "2", -30));
        pc.detectorListener.process(new DtmfEventImpl(detector, "3", -30));
        pc.detectorListener.process(new DtmfEventImpl(detector, "A", -30));

        // then
        verify(detector, times(1)).activate();
        verify(player, never()).activate();
        verify(observer, timeout(50)).onEvent(eq(pc), eventCaptor.capture());

        assertEquals(String.valueOf(ReturnCode.SUCCESS.code()), eventCaptor.getValue().getParameter("rc"));
        assertEquals("123", eventCaptor.getValue().getParameter("dc"));
        assertEquals("1", eventCaptor.getValue().getParameter("na"));
    }

}
