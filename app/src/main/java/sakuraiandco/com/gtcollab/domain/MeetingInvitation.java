package sakuraiandco.com.gtcollab.domain;

import android.os.Parcel;
import android.os.Parcelable;

import org.joda.time.DateTime;

import java.util.List;

import lombok.Builder;
import lombok.Data;
import sakuraiandco.com.gtcollab.constants.Constants;

/**
 * Created by kaliq on 11/1/2017.
 */

@Data
@Builder
public class MeetingInvitation extends Entity implements Parcelable {

    public static final String BASE_URL = Constants.BASE_URL + "/meeting-invitations/";

    int id;
    int meetingId;
    User creator;
    List<Integer> recipients;
    DateTime timestamp;

    public static final Creator<MeetingInvitation> CREATOR = new Creator<MeetingInvitation>() {
        @Override
        public MeetingInvitation createFromParcel(Parcel in) {
            MeetingInvitation mi = MeetingInvitation.builder()
                    .id(in.readInt())
                    .meetingId(in.readInt())
                    .creator((User) in.readParcelable(User.class.getClassLoader()))
                    .timestamp(new DateTime(in.readString()))
                    .build();
            in.readList(mi.getRecipients(), null);
            return mi;
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
        dest.writeInt(id);
        dest.writeInt(meetingId);
        dest.writeParcelable(creator, flags);
        dest.writeString(timestamp.toString());
        dest.writeList(recipients); // TODO: out of order - messy? better way?
    }
}
