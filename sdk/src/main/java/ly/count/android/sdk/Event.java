/*
Copyright (c) 2012, 2013, 2014 Countly

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
*/
package ly.count.android.sdk;

import androidx.annotation.NonNull;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class holds the data for a single Count.ly custom event instance.
 * It also knows how to read & write itself to the Count.ly custom event JSON syntax.
 * See the following link for more info:
 * https://count.ly/resources/reference/custom-events
 */
class Event {
    private static final String SEGMENTATION_KEY = "segmentation";
    private static final String KEY_KEY = "key";
    private static final String COUNT_KEY = "count";
    private static final String SUM_KEY = "sum";
    private static final String DUR_KEY = "dur";
    private static final String TIMESTAMP_KEY = "timestamp";
    private static final String DAY_OF_WEEK = "dow";
    private static final String HOUR = "hour";

    public String key;
    public Map<String, String> segmentation;
    public Map<String, Integer> segmentationInt;
    public Map<String, Double> segmentationDouble;
    public Map<String, Boolean> segmentationBoolean;
    public int count;
    public double sum;
    public double dur;
    public long timestamp;
    public int hour;
    public int dow;

    Event() {
    }

    Event(@NonNull String key) {
        UtilsTime.Instant instant = UtilsTime.getCurrentInstant();

        this.key = key;
        this.timestamp = instant.timestampMs;
        this.hour = instant.hour;
        this.dow = instant.dow;
    }

    /**
     * Creates and returns a JSONObject containing the event data from this object.
     *
     * @return a JSONObject containing the event data from this object
     */
    JSONObject toJSON() {
        final JSONObject json = new JSONObject();

        try {
            json.put(KEY_KEY, key);
            json.put(COUNT_KEY, count);
            json.put(TIMESTAMP_KEY, timestamp);
            json.put(HOUR, hour);
            json.put(DAY_OF_WEEK, dow);

            JSONObject jobj = new JSONObject();
            if (segmentation != null) {
                for (Map.Entry<String, String> pair : segmentation.entrySet()) {
                    jobj.put(pair.getKey(), pair.getValue());
                }
            }

            if (segmentationInt != null) {
                for (Map.Entry<String, Integer> pair : segmentationInt.entrySet()) {
                    jobj.put(pair.getKey(), pair.getValue());
                }
            }

            if (segmentationDouble != null) {
                for (Map.Entry<String, Double> pair : segmentationDouble.entrySet()) {
                    jobj.put(pair.getKey(), pair.getValue());
                }
            }

            if (segmentationBoolean != null) {
                for (Map.Entry<String, Boolean> pair : segmentationBoolean.entrySet()) {
                    jobj.put(pair.getKey(), pair.getValue());
                }
            }

            if (segmentation != null || segmentationInt != null || segmentationDouble != null || segmentationBoolean != null) {
                json.put(SEGMENTATION_KEY, jobj);
            }

            // we put in the sum last, the only reason that a JSONException would be thrown
            // would be if sum is NaN or infinite, so in that case, at least we will return
            // a JSON object with the rest of the fields populated
            json.put(SUM_KEY, sum);

            if (dur > 0) {
                json.put(DUR_KEY, dur);
            }
        } catch (JSONException e) {
            Countly.sharedInstance().L.w("Got exception converting an Event to JSON", e);
        }

        return json;
    }

    /**
     * Factory method to create an Event from its JSON representation.
     *
     * @param json JSON object to extract event data from
     * @return Event object built from the data in the JSON or null if the "key" value is not
     * present or the empty string, or if a JSON exception occurs
     */
    static Event fromJSON(@NonNull final JSONObject json) {
        Event event = new Event();

        try {
            if (!json.isNull(KEY_KEY)) {
                event.key = json.getString(KEY_KEY);
            }
            event.count = json.optInt(COUNT_KEY);
            event.sum = json.optDouble(SUM_KEY, 0.0d);
            event.dur = json.optDouble(DUR_KEY, 0.0d);
            event.timestamp = json.optLong(TIMESTAMP_KEY);
            event.hour = json.optInt(HOUR);
            event.dow = json.optInt(DAY_OF_WEEK);

            if (!json.isNull(SEGMENTATION_KEY)) {
                JSONObject segm = json.getJSONObject(SEGMENTATION_KEY);

                final HashMap<String, String> segmentation = new HashMap<>();
                final HashMap<String, Integer> segmentationInt = new HashMap<>();
                final HashMap<String, Double> segmentationDouble = new HashMap<>();
                final HashMap<String, Boolean> segmentationBoolean = new HashMap<>();

                final Iterator nameItr = segm.keys();
                while (nameItr.hasNext()) {
                    final String key = (String) nameItr.next();
                    if (!segm.isNull(key)) {
                        Object obj = segm.opt(key);

                        if (obj instanceof Double) {
                            //in case it's a double
                            segmentationDouble.put(key, segm.getDouble(key));
                        } else if (obj instanceof Integer) {
                            //in case it's a integer
                            segmentationInt.put(key, segm.getInt(key));
                        } else if (obj instanceof Boolean) {
                            //in case it's a boolean
                            segmentationBoolean.put(key, segm.getBoolean(key));
                        } else {
                            //assume it's String
                            segmentation.put(key, segm.getString(key));
                        }
                    }
                }
                event.segmentation = segmentation;
                event.segmentationDouble = segmentationDouble;
                event.segmentationInt = segmentationInt;
                event.segmentationBoolean = segmentationBoolean;
            }
        } catch (JSONException e) {
            Countly.sharedInstance().L.w("Got exception converting JSON to an Event", e);
            event = null;
        }

        return (event != null && event.key != null && event.key.length() > 0) ? event : null;
    }

    @Override
    public boolean equals(final Object o) {
        if (!(o instanceof Event)) {
            return false;
        }

        final Event e = (Event) o;

        return (key == null ? e.key == null : key.equals(e.key)) &&
            timestamp == e.timestamp &&
            hour == e.hour &&
            dow == e.dow &&
            (segmentation == null ? e.segmentation == null : segmentation.equals(e.segmentation));
    }

    @Override
    public int hashCode() {
        return (key != null ? key.hashCode() : 1) ^
            (segmentation != null ? segmentation.hashCode() : 1) ^
            (timestamp != 0 ? (int) timestamp : 1);
    }
}
