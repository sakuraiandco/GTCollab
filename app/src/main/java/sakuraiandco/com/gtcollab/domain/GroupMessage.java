package sakuraiandco.com.gtcollab.domain;

import android.os.Parcel;
import android.os.Parcelable;

import org.joda.time.DateTime;

import lombok.Builder;
import lombok.Data;
import sakuraiandco.com.gtcollab.constants.Constants;

/**
 * Created by kaliq on 10/14/2017.
 */

@Data
@Builder
public class GroupMessage extends Entity implements Parcelable {

    public static final String BASE_URL = Constants.BASE_URL + "/group-messages/";

    int id;
    String content;
    int groupId;
    User creator;
    DateTime timestamp;

    public static final Creator<GroupMessage> CREATOR = new Creator<GroupMessage>() {
        @Override
        public GroupMessage createFromParcel(Parcel in) {
            return GroupMessage.builder()
                    .id(in.readInt())
                    .content(in.readString())
                    .groupId(in.readInt())
                    .creator((User) in.readParcelable(User.class.getClassLoader()))
                    .timestamp(new DateTime(in.readString()))
                    .build();
        }

        @Override
        public GroupMessage[] newArray(int size) {
            return new GroupMessage[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(content);
        dest.writeInt(groupId);
        dest.writeParcelable(creator, flags);
        dest.writeString(timestamp.toString());
    }
}
