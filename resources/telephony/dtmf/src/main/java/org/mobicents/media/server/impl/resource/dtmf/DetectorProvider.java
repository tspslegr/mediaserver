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

package org.mobicents.media.server.impl.resource.dtmf;

import java.util.concurrent.atomic.AtomicInteger;

import org.mobicents.media.server.scheduler.PriorityQueueScheduler;
import org.mobicents.media.server.spi.dtmf.DtmfDetector;
import org.mobicents.media.server.spi.dtmf.DtmfDetectorProvider;

/**
 * @author Henrique Rosa (henrique.rosa@telestax.com)
 *
 */
public class DetectorProvider implements DtmfDetectorProvider {

    private final PriorityQueueScheduler scheduler;
    private final AtomicInteger id;
    private final int volume;

    public DetectorProvider(PriorityQueueScheduler scheduler, int volume) {
        this.scheduler = scheduler;
        this.id = new AtomicInteger(0);
        this.volume = volume;
    }

    @Override
    public DtmfDetector provide() {
        DetectorImpl detector = new DetectorImpl(nextId(), this.scheduler);
        detector.setVolume(this.volume);
        return detector;
    }

    private String nextId() {
        return "dtmf-detector" + id.getAndIncrement();
    }

}