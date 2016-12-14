/*
 *     This file is part of Development, a MapleStory Emulator Project.
 *     Copyright (C) 2015 Eric Smith <muffinman75013@yahoo.com>
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 */
package client;

import java.util.ArrayList;
import java.util.List;
import tools.data.output.MaplePacketLittleEndianWriter;

/**
 * ExtendSP
 *
 * @author Eric
 */
public class ExtendSP {

    public final List<SPSet> lSPSet = new ArrayList<>();
    public int m_nTotalSP;

    public ExtendSP() {
        SPSet pSPSet;
        for (int i = 1; i <= 10; i++) {
            pSPSet = new SPSet();
            pSPSet.nJobLevel = i;
            pSPSet.nSP = 0;
        }
    }

    public ExtendSP(int nJobLevel, int[] anSP) {
        SPSet pSPSet;
        for (int i = 1; i <= nJobLevel; i++) {
            pSPSet = new SPSet();
            pSPSet.nJobLevel = i;
            pSPSet.nSP = anSP[i - 1];
        }
    }

    public void Encode(MaplePacketLittleEndianWriter mplew) {
        mplew.write(lSPSet.size());
        for (SPSet pSPSet : lSPSet) {
            mplew.write(pSPSet.nJobLevel);
            mplew.write(pSPSet.nSP);
        }
    }

    public class SPSet {

        public int nJobLevel;
        public int nSP;
    }
}
