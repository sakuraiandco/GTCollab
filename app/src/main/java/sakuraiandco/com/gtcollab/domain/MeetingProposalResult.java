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
public class MeetingProposalResult extends MeetingNotification implements Parcelable {

    public static final String BASE_URL = Constants.BASE_URL + "/meeting-proposal-results/";

    int meetingProposalId;

    @Builder
    MeetingProposalResult(int id, String title, String message, String messageExpanded, User creator, List<Integer> recipients, List<Integer> recipientsReadBy, DateTime timestamp, int meetingId, int meetingProposalId) {
        super(id, title, message, messageExpanded, creator, recipients, recipientsReadBy, timestamp, meetingId);
        this.meetingProposalId = meetingProposalId;
    }

    private MeetingProposalResult(Parcel in) {
        super(in);
        meetingProposalId = in.readInt();
    }

    public static final Creator<MeetingProposalResult> CREATOR = new Creator<MeetingProposalResult>() {
        @Override
        public MeetingProposalResult createFromParcel(Parcel in) {
            return new MeetingProposalResult(in);
        }

        @Override
        public MeetingProposalResult[] newArray(int size) {
            return new MeetingProposalResult[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(meetingProposalId);
    }

    public static class MeetingProposalResultBuilder extends MeetingNotificationBuilder {
        MeetingProposalResultBuilder() {
            super();
        }
    }
}
