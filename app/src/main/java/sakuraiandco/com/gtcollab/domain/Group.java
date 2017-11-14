package sakuraiandco.com.gtcollab.domain;

import android.os.Parcel;
import android.os.Parcelable;

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
public class Group extends Entity implements Parcelable {

    public static final String BASE_URL = Constants.BASE_URL + "/groups/";

    int id;
    String name;
    int courseId;
    User creator;
    List<Integer> members;

    public static final Creator<Group> CREATOR = new Creator<Group>() {
        @Override
        public Group createFromParcel(Parcel in) {
            Group g = Group.builder()
                    .id(in.readInt())
                    .name(in.readString())
                    .courseId(in.readInt())
                    .creator((User)in.readParcelable(User.class.getClassLoader()))
                    .members(new ArrayList<Integer>())
                    .build();
            in.readList(g.getMembers(), null);
            return g;
        }

        @Override
        public Group[] newArray(int size) {
            return new Group[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeInt(courseId);
        dest.writeParcelable(creator, flags);
        dest.writeList(members); // TODO: out of order - messy? better way?
    }
}
