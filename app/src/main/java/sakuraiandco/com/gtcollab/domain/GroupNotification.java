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
public class GroupNotification extends StandardNotification implements Parcelable {

    public static final String BASE_URL = Constants.BASE_URL + "/group-notifications/";

    int groupId;

    @Builder
    GroupNotification(int id, String title, String message, String messageExpanded, User creator, List<Integer> recipients, List<Integer> recipientsReadBy, DateTime timestamp, int groupId) {
        super(id, title, message, messageExpanded, creator, recipients, recipientsReadBy, timestamp);
        this.groupId = groupId;
    }

    GroupNotification(Parcel in) {
        super(in);
        groupId = in.readInt();
    }

    public static final Creator<GroupNotification> CREATOR = new Creator<GroupNotification>() {
        @Override
        public GroupNotification createFromParcel(Parcel in) {
            return new GroupNotification(in);
        }

        @Override
        public GroupNotification[] newArray(int size) {
            return new GroupNotification[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(groupId);
    }

    public static class GroupNotificationBuilder extends StandardNotificationBuilder {
        GroupNotificationBuilder() {
            super();
        }
    }

}
