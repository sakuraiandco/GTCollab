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
 * Created by kaliq on 11/26/2017.
 */

@Data
public class MeetingProposal extends MeetingNotification implements Parcelable {

    public static final String BASE_URL = Constants.BASE_URL + "/meeting-proposals/";

    String location;
    LocalDate startDate;
    LocalTime startTime;
    List<Integer> responsesReceived;
    int expirationMinutes;
    boolean applied;
    boolean closed;

    @Builder
    MeetingProposal(int id, String title, String message, String messageExpanded, User creator, List<Integer> recipients, List<Integer> recipientsReadBy, DateTime timestamp, int meetingId, String location, LocalDate startDate, LocalTime startTime, List<Integer> responsesReceived, int expirationMinutes, boolean applied, boolean closed) {
        super(id, title, message, messageExpanded, creator, recipients, recipientsReadBy, timestamp, meetingId);
        this.location = location;
        this.startDate = startDate;
        this.startTime = startTime;
        this.responsesReceived = responsesReceived;
        this.expirationMinutes = expirationMinutes;
        this.applied = applied;
        this.closed = closed;
    }

    private MeetingProposal(Parcel in) {
        super(in);
        responsesReceived = new ArrayList<>();

        location = in.readString();
        startDate = new LocalDate(in.readString());
        startTime = new LocalTime(in.readString());
        expirationMinutes = in.readInt();
        applied = in.readByte() != 0;
        closed = in.readByte() != 0;
        in.readList(responsesReceived, null);
    }

    public static final Creator<MeetingProposal> CREATOR = new Creator<MeetingProposal>() {
        @Override
        public MeetingProposal createFromParcel(Parcel in) {
            return new MeetingProposal(in);
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
        super.writeToParcel(dest, flags);
        dest.writeString(location);
        dest.writeString(startDate.toString());
        dest.writeString(startTime.toString());
        dest.writeInt(expirationMinutes);
        dest.writeByte((byte) (applied ? 1 : 0));
        dest.writeByte((byte) (closed ? 1 : 0));
        dest.writeList(responsesReceived); // TODO: out of order - messy? better way?
    }

    public static class MeetingProposalBuilder extends MeetingNotificationBuilder {
        MeetingProposalBuilder() {
            super();
        }
    }
}
