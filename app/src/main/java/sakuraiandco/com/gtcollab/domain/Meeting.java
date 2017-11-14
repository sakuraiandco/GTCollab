package sakuraiandco.com.gtcollab.domain;

import android.os.Parcel;
import android.os.Parcelable;

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
public class Meeting extends Entity implements Parcelable {

    public static final String BASE_URL = Constants.BASE_URL + "/meetings/";

    int id;
    String name;
    String location;
    String description;
    LocalDate startDate;
    LocalTime startTime;
    int durationMinutes;
    int courseId;
    User creator;
    List<Integer> members;

    public static final Creator<Meeting> CREATOR = new Creator<Meeting>() {
        @Override
        public Meeting createFromParcel(Parcel in) {
            Meeting m = Meeting.builder()
                    .id(in.readInt())
                    .name(in.readString())
                    .location(in.readString())
                    .description(in.readString())
                    .startDate(new LocalDate(in.readString()))
                    .startTime(new LocalTime(in.readString()))
                    .durationMinutes(in.readInt())
                    .courseId(in.readInt())
                    .creator((User)in.readParcelable(User.class.getClassLoader()))
                    .members(new ArrayList<Integer>())
                    .build();
            in.readList(m.getMembers(), null);
            return m;
        }

        @Override
        public Meeting[] newArray(int size) {
            return new Meeting[size];
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
        dest.writeString(location);
        dest.writeString(description);
        dest.writeString(startDate.toString());
        dest.writeString(startTime.toString());
        dest.writeInt(durationMinutes);
        dest.writeInt(courseId);
        dest.writeParcelable(creator, flags);
        dest.writeList(members); // TODO: out of order - messy? better way?
    }
}
