// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Comparator;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public final class FindMeetingQuery {
    /* 
    * Given  alist of events in one day and a request for a meeting, 
    * returns all the timeRanges when the meetik can take place so that each attendee can attend without having anothe roverlapping event.
    */
    public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {
        events = new ArrayList(events);
        events.removeIf(event -> !existCommonAttendees(event.getAttendees(), request.getAttendees()));
        List<TimeRange> unavailableTimes = getListOfTimeRanges(events);
        List<TimeRange> reducedUnavailableTimes = getReducedListOfTimeRanges(unavailableTimes);
        List<TimeRange> availableTimeRanges = getComplementerTimeRanges(reducedUnavailableTimes);
        removeTooShortTimeRanges(availableTimeRanges, request.getDuration());
        return availableTimeRanges;
    }

    /* 
    * Removes from the list the time ranges which are shorter than minDuration.
    */ 
    private void removeTooShortTimeRanges(List<TimeRange> timeRanges, long minDuration) {
        timeRanges.removeIf(timeRange -> timeRange.duration() < minDuration);
    }

    /* 
    * Given the timeRanges in one day, this function returns the list of complementerTimeRanges that are in the same day, but are not present in timeRanegs.
    * The union of timeRanges and complementerTimeRanges is an entire day.
    */
    private List<TimeRange> getComplementerTimeRanges(List<TimeRange> timeRanges) {
        List<TimeRange> complementerTimeRanges = new LinkedList<TimeRange>();
        int lastRangeEndPoint = 0;
        for (TimeRange timeRange : timeRanges) {
             if (lastRangeEndPoint < timeRange.start()) {
                 complementerTimeRanges.add(TimeRange.fromStartEnd(lastRangeEndPoint, timeRange.start(), false));
             }
             lastRangeEndPoint = timeRange.end();
        }
        if (lastRangeEndPoint < TimeRange.END_OF_DAY ) {
            complementerTimeRanges.add(TimeRange.fromStartEnd(lastRangeEndPoint, TimeRange.END_OF_DAY, true));
        }
        return complementerTimeRanges;
    }

    /*
    * Removes the duplications from the overlapping timeRanges, and merges them into one single TimeRange. 
    * Returns a list containing a minimal number of timeRanges, which do not overlpa, but they do contain each original timeRange.
    */
    private List<TimeRange> getReducedListOfTimeRanges(List<TimeRange> timeRanges) {
        List<TimeRange> reducedTimeRanges = new LinkedList<>();
        if (timeRanges.size()==0) {
            return reducedTimeRanges;
        }
        TimeRange expandableTimeRange = timeRanges.get(0);
        TimeRange currentTimeRange;
        for (int i = 1; i<timeRanges.size(); i++) {
            currentTimeRange = timeRanges.get(i);
            if (expandableTimeRange.overlaps(currentTimeRange)) { 
                //unite this timeRange with the previous one(s)
                expandableTimeRange = expandableTimeRange.getUnion(currentTimeRange);
            }
            else { 
                //add the previous timeRange to the list of reduced timeRanges, because it can't be expanded anymore
                //start a new cuurentTimeRange
                reducedTimeRanges.add(expandableTimeRange);
                expandableTimeRange = currentTimeRange;
            }
        }
        reducedTimeRanges.add(expandableTimeRange);
        return reducedTimeRanges;
    }

    /* 
    * creates a list containing the time ranges only out of the events. 
    */
    private List<TimeRange> getListOfTimeRanges(Collection<Event> events) {
        List<TimeRange> result = new ArrayList<>();
        for (Event event : events) {
            result.add(event.getWhen());
        }
        return result;
    }

    /* 
    * Checks if there is exists an intersection(a common attendee) of the attendee list of two events.
    */
    private boolean existCommonAttendees(Collection<String> attendeesEventA, Collection<String> attendeesEventB) {
        //swap collections so that I can iterate through the shorter one
        if (attendeesEventA.size()>attendeesEventB.size()) { 
            Collection<String> helper = attendeesEventA;
            attendeesEventA = attendeesEventB;
            attendeesEventB = helper;
        }

        //itarate through the attendees of event A and check if any of them also attends event B. If so,return true
        Iterator iteratorB = attendeesEventB.iterator(); 
        boolean foundCommonAttendee = false;
        while (iteratorB.hasNext() && !foundCommonAttendee) { 
            if (attendeesEventA.contains(iteratorB.next())) {
                foundCommonAttendee=true;
            }
        } 
        return foundCommonAttendee;
    }
}