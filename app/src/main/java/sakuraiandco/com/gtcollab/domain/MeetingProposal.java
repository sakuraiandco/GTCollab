package sakuraiandco.com.gtcollab.domain;

import android.os.Parcel;
import android.os.Parcelable;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import java.util.ArrayList;
import java.util.List;

import lombok.Builder;
import lombok.Data;
import sakuraiandco.com.gtcollab.constants.Constants;

/**
 * Created by kaliq on 10/14/2017.
 */

@Data
@Builder
public class MeetingProposal extends Entity implements Parcelable {

    public static final String BASE_URL = Constants.BASE_URL + "/meeting-proposals/";

    int id;
    int meetingId;
    String location;
    LocalDate startDate;
    LocalTime startTime;
    User creator;
    DateTime timestamp;
    List<Integer> responsesReceived;
    int expirationMinutes;
    boolean applied;
    boolean closed;

    public static final Creator<MeetingProposal> CREATOR = new Creator<MeetingProposal>() {
        @Override
        public MeetingProposal createFromParcel(Parcel in) {
            MeetingProposal mp =  MeetingProposal.builder()
                    .id(in.readInt())
                    .meetingId(in.readInt())
                    .location(in.readString())
                    .startDate(new LocalDate(in.readString()))
                    .startTime(new LocalTime(in.readString()))
                    .creator((User)in.readParcelable(User.class.getClassLoader()))
                    .timestamp(new DateTime(in.readString()))
                    .responsesReceived(new ArrayList<Integer>())
                    .expirationMinutes(in.readInt())
                    .applied(in.readByte() != 0)
                    .closed(in.readByte() != 0)
                    .build();
            in.readList(mp.getResponsesReceived(), null);
            return mp;
        }

        @Override
        public MeetingProposal[] newArray(int size) {
            return new MeetingProposal[size];
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
        dest.writeString(location);
        dest.writeString(startDate.toString());
        dest.writeString(startTime.toString());
        dest.writeParcelable(creator, flags);
        dest.writeString(timestamp.toString());
        dest.writeInt(expirationMinutes);
        dest.writeByte((byte) (applied ? 1 : 0));
        dest.writeByte((byte) (closed ? 1 : 0));
        dest.writeList(responsesReceived); // TODO: out of order - messy? better way?
    }
}
