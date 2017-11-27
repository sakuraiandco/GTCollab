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
public class MeetingInvitation extends MeetingNotification implements Parcelable {

    public static final String BASE_URL = Constants.BASE_URL + "/group-invitations/";

    @Builder
    MeetingInvitation(int id, String title, String message, String messageExpanded, User creator, List<Integer> recipients, List<Integer> recipientsReadBy, DateTime timestamp, int meetingId) {
        super(id, title, message, messageExpanded, creator, recipients, recipientsReadBy, timestamp, meetingId);
    }

    private MeetingInvitation(Parcel in) {
        super(in);
    }

    public static final Creator<MeetingInvitation> CREATOR = new Creator<MeetingInvitation>() {
        @Override
        public MeetingInvitation createFromParcel(Parcel in) {
            return new MeetingInvitation(in);
        }

        @Override
        public MeetingInvitation[] newArray(int size) {
            return new MeetingInvitation[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
    }

    public static class MeetingInvitationBuilder extends MeetingNotificationBuilder {
        MeetingInvitationBuilder() {
            super();
        }
    }
}
