package sakuraiandco.com.gtcollab.domain;

import android.os.Parcel;
import android.os.Parcelable;

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
public class Course extends Entity implements Parcelable {

    public static final String BASE_URL = Constants.BASE_URL + "/courses/";

    int id;
    String name;
    String subjectCode;
    String courseNumber;
    List<String> sections;
    List<MeetingTime> meetingTimes;
    List<Integer> members;
    int numMembers;
    boolean isCancelled;

    public String getShortName() {
        return subjectCode + " " + courseNumber; // TODO: replace with this
    }

    public static final Creator<Course> CREATOR = new Creator<Course>() {
        @Override
        public Course createFromParcel(Parcel in) {
            Course c = Course.builder()
                    .id(in.readInt())
                    .name(in.readString())
                    .subjectCode(in.readString())
                    .courseNumber(in.readString())
                    .sections(in.createStringArrayList())
                    .meetingTimes(in.createTypedArrayList(MeetingTime.CREATOR))
                    .members(new ArrayList<Integer>())
                    .numMembers(in.readInt())
                    .isCancelled(in.readByte() != 0)
                    .build();
            in.readList(c.getMembers(), null);
            return c;
        }

        @Override
        public Course[] newArray(int size) {
            return new Course[size];
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
        dest.writeString(subjectCode);
        dest.writeString(courseNumber);
        dest.writeStringList(sections);
        dest.writeTypedList(meetingTimes);
        dest.writeInt(numMembers);
        dest.writeByte((byte) (isCancelled ? 1 : 0));
        dest.writeList(members); // TODO: out of order - messy? better way?
    }

    @Data
    @Builder
    public static class MeetingTime implements Parcelable {
        String meetDays;
        LocalTime startTime;
        LocalTime endTime;

        public static final Creator<MeetingTime> CREATOR = new Creator<MeetingTime>() {
            @Override
            public MeetingTime createFromParcel(Parcel in) {
                return MeetingTime.builder()
                        .meetDays(in.readString())
                        .startTime(new LocalTime(in.readString()))
                        .endTime(new LocalTime(in.readString()))
                        .build();
            }

            @Override
            public MeetingTime[] newArray(int size) {
                return new MeetingTime[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(meetDays);
            dest.writeString(startTime.toString());
            dest.writeString(endTime.toString());
        }
    }

}
