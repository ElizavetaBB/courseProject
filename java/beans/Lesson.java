package beans;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Objects;

public class Lesson implements Serializable {
    private Integer id;
    private Integer groupId;
    private String groupName;
    private Integer teacher_id;
    private String teacherName;
    private Integer aud_id;
    private String audName;
    private Timestamp start_time;
    private Timestamp finish_time;
    private Integer semester_id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Integer getTeacher_id() {
        return teacher_id;
    }

    public void setTeacher_id(Integer teacher_id) {
        this.teacher_id = teacher_id;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public Integer getAud_id() {
        return aud_id;
    }

    public void setAud_id(Integer aud_id) {
        this.aud_id = aud_id;
    }

    public String getAudName() {
        return audName;
    }

    public void setAudName(String audName) {
        this.audName = audName;
    }

    public Timestamp getStart_time() {
        return start_time;
    }

    public void setStart_time(Timestamp start_time) {
        this.start_time = start_time;
    }

    public Timestamp getFinish_time() {
        return finish_time;
    }

    public void setFinish_time(Timestamp finish_time) {
        this.finish_time = finish_time;
    }

    public Integer getSemester_id() {
        return semester_id;
    }

    public void setSemester_id(Integer semester_id) {
        this.semester_id = semester_id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Lesson lesson = (Lesson) o;
        return Objects.equals(id, lesson.id) && Objects.equals(groupId, lesson.groupId) && Objects.equals(teacher_id, lesson.teacher_id) && Objects.equals(aud_id, lesson.aud_id) && Objects.equals(start_time, lesson.start_time) && Objects.equals(finish_time, lesson.finish_time) && Objects.equals(semester_id, lesson.semester_id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, groupId, teacher_id, aud_id, start_time, finish_time, semester_id);
    }
}
