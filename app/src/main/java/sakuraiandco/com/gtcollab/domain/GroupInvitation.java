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
public class GroupInvitation extends GroupNotification implements Parcelable {

    public static final String BASE_URL = Constants.BASE_URL + "/group-invitations/";

    @Builder
    GroupInvitation(int id, String title, String message, String messageExpanded, User creator, List<Integer> recipients, List<Integer> recipientsReadBy, DateTime timestamp, int groupId) {
        super(id, title, message, messageExpanded, creator, recipients, recipientsReadBy, timestamp, groupId);
    }

    private GroupInvitation(Parcel in) {
        super(in);
    }

    public static final Creator<GroupInvitation> CREATOR = new Creator<GroupInvitation>() {
        @Override
        public GroupInvitation createFromParcel(Parcel in) {
            return new GroupInvitation(in);
        }

        @Override
        public GroupInvitation[] newArray(int size) {
            return new GroupInvitation[size];
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

    public static class GroupInvitationBuilder extends GroupNotificationBuilder {
        GroupInvitationBuilder() {
            super();
        }
    }
}
