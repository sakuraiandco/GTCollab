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
public class GroupInvitation extends Entity implements Parcelable {

    public static final String BASE_URL = Constants.BASE_URL + "/group-invitations/";

    int id;
    int groupId;
    User creator;
    List<Integer> recipients;
    DateTime timestamp;

    public static final Creator<GroupInvitation> CREATOR = new Creator<GroupInvitation>() {
        @Override
        public GroupInvitation createFromParcel(Parcel in) {
            GroupInvitation gi = GroupInvitation.builder()
                    .id(in.readInt())
                    .groupId(in.readInt())
                    .creator((User) in.readParcelable(User.class.getClassLoader()))
                    .timestamp(new DateTime(in.readString()))
                    .build();
            in.readList(gi.getRecipients(), null);
            return gi;
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
        dest.writeInt(id);
        dest.writeInt(groupId);
        dest.writeParcelable(creator, flags);
        dest.writeString(timestamp.toString());
        dest.writeList(recipients); // TODO: out of order - messy? better way?
    }
}
