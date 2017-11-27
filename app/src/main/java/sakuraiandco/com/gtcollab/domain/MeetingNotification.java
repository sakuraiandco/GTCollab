package sakuraiandco.com.gtcollab.domain;

import android.os.Parcel;
import android.os.Parcelable;

import org.joda.time.DateTime;

import java.util.List;

import lombok.Builder;
import lombok.Data;
import sakuraiandco.com.gtcollab.constants.Constants;

/**
 * Created by kaliq on 11/26/2017.
 */

@Data
public class MeetingNotification extends StandardNotification implements Parcelable {

    public static final String BASE_URL = Constants.BASE_URL + "/meeting-notifications/";

    int meetingId;

    @Builder
    MeetingNotification(int id, String title, String message, String messageExpanded, User creator, List<Integer> recipients, List<Integer> recipientsReadBy, DateTime timestamp, int meetingId) {
        super(id, title, message, messageExpanded, creator, recipients, recipientsReadBy, timestamp);
        this.meetingId = meetingId;
    }

    MeetingNotification(Parcel in) {
        super(in);
        meetingId = in.readInt();
    }

    public static final Creator<MeetingNotification> CREATOR = new Creator<MeetingNotification>() {
        @Override
        public MeetingNotification createFromParcel(Parcel in) {
            return new MeetingNotification(in);
        }

        @Override
        public MeetingNotification[] newArray(int size) {
            return new MeetingNotification[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(meetingId);
    }

    public static class MeetingNotificationBuilder extends StandardNotificationBuilder {
        MeetingNotificationBuilder() {
            super();
        }
    }
}
