package beans;

import org.primefaces.PrimeFaces;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.sql.*;
import java.util.*;
import java.util.Date;
import java.util.List;

public class LessonSheduleBean implements Serializable {

    private Connection connection;
    private Integer id=null;
    private Lesson selectedLesson;
    private Calendar calendar = new GregorianCalendar(2020, 8 , 2);
    private Calendar maxDate=new GregorianCalendar(2020,11,27);
    private Date selectedDate=calendar.getTime();
    private Timestamp selectedTimeStart=Timestamp.valueOf("2020-09-02 00:00:00");
    private Timestamp selectedTimeFinish=Timestamp.valueOf("2020-09-03 00:00:00");
    private String selectedGroup;
    private Integer selectedTeacherId;
    private Integer selectedNewGroup;
    private Integer selectedNewTeacherId;
    private Integer selectedNewAudId;
    private Date newTimeStart;
    private Date newTimeFinish;
    private Date audTimeStart;
    private Date audTimeFinish;
    private Integer freeAud;
    private Date teacherTimeStart;
    private Date teacherTimeFinish;
    private String freeTeacher;
    private Integer selectedTeacherMaterialId;
    private Integer selectedMaterialId;
    private HashMap<Integer,String> materialsName=new HashMap<>();
    private HashMap<Integer,Integer> materialsLevel=new HashMap<>();

    public Integer getSelectedTeacherMaterialId() {
        return selectedTeacherMaterialId;
    }

    public void setSelectedTeacherMaterialId(Integer selectedTeacherMaterialId) {
        this.selectedTeacherMaterialId = selectedTeacherMaterialId;
    }

    public Integer getSelectedMaterialId() {
        return selectedMaterialId;
    }

    public void setSelectedMaterialId(Integer selectedMaterialId) {
        this.selectedMaterialId = selectedMaterialId;
    }

    public Date getTeacherTimeStart() {
        return teacherTimeStart;
    }

    public void setTeacherTimeStart(Date teacherTimeStart) {
        this.teacherTimeStart = teacherTimeStart;
    }

    public Date getTeacherTimeFinish() {
        return teacherTimeFinish;
    }

    public void setTeacherTimeFinish(Date teacherTimeFinish) {
        this.teacherTimeFinish = teacherTimeFinish;
    }

    public String getFreeTeacher() {
        return freeTeacher;
    }

    public void setFreeTeacher(String freeTeacher) {
        this.freeTeacher = freeTeacher;
    }

    public Integer getFreeAud() {
        return freeAud;
    }

    public void setFreeAud(Integer freeAud) {
        this.freeAud = freeAud;
    }

    public Date getAudTimeStart() {
        return audTimeStart;
    }

    public void setAudTimeStart(Date audTimeStart) {
        this.audTimeStart = audTimeStart;
    }

    public Date getAudTimeFinish() {
        return audTimeFinish;
    }

    public void setAudTimeFinish(Date audTimeFinish) {
        this.audTimeFinish = audTimeFinish;
    }

    public Integer getSelectedNewGroup() {
        return selectedNewGroup;
    }

    public void setSelectedNewGroup(Integer selectedNewGroup) {
        this.selectedNewGroup = selectedNewGroup;
    }

    public Integer getSelectedNewTeacherId() {
        return selectedNewTeacherId;
    }

    public void setSelectedNewTeacherId(Integer selectedNewTeacherId) {
        this.selectedNewTeacherId = selectedNewTeacherId;
    }

    public Integer getSelectedNewAudId() {
        return selectedNewAudId;
    }

    public void setSelectedNewAudId(Integer selectedNewAudId) {
        this.selectedNewAudId = selectedNewAudId;
    }

    public Date getNewTimeStart() {
        return newTimeStart;
    }

    public void setNewTimeStart(Date newTimeStart) {
        this.newTimeStart = newTimeStart;
    }

    public Date getNewTimeFinish() {
        return newTimeFinish;
    }

    public void setNewTimeFinish(Date newTimeFinish) {
        this.newTimeFinish = newTimeFinish;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Integer> getLevels(){
        List<Integer> levels=new ArrayList<>();
        for (int i=0;i<11;i++){
            levels.add(i);
        }
        return levels;
    }

    public List<Material> getMaterials(){
        ResultSet resultSet;
        PreparedStatement preparedStatement;
        List<Material> points = new ArrayList<>();
        try {
            preparedStatement = connection.prepareStatement("select * FROM МАТЕРИАЛ_ПРЕПОДАВАТЕЛЯ");
            preparedStatement.execute();
            resultSet = preparedStatement.getResultSet();
            while(resultSet.next()) {
                Material point = new Material();
                point.setId(resultSet.getInt(1));
                point.setName(resultSet.getString(2));
                point.setLevel(resultSet.getInt(3));
                materialsName.put(point.getId(),point.getName());
                materialsLevel.put(point.getId(),point.getLevel());
                points.add(point);
            }
            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return points;
    }

    public void addNewMaterial(){
        ResultSet resultSet;
        CallableStatement preparedStatement;
        try {
            preparedStatement = connection.prepareCall("{call ВЫДАТЬ_МАТЕРИАЛ_ПРЕПОДАВАТЕЛЮ(?,?,?)}");
            preparedStatement.setInt(1,selectedTeacherMaterialId);
            preparedStatement.setString(2,materialsName.get(selectedMaterialId));
            preparedStatement.setInt(3,materialsLevel.get(selectedMaterialId));
            preparedStatement.execute();
            resultSet = preparedStatement.getResultSet();
            String out;
            while(resultSet.next()) {
                out=resultSet.getString(1);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Результат:", out));
            }
            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void getGroupLevel(){
        ResultSet resultSet;
        PreparedStatement preparedStatement;
        Integer level=null;
        try {
            preparedStatement = connection.prepareStatement("select УРОВЕНЬ FROM ГРУППА WHERE НАЗВАНИЕ=?");
            preparedStatement.setString(1,selectedLesson.getGroupName());
            preparedStatement.execute();
            resultSet = preparedStatement.getResultSet();
            while(resultSet.next()) {
                level=resultSet.getInt(1);
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Successful","Уровень группы "+selectedLesson.getGroupName()+"="+level));
            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void click() {
        calendar.setTime(selectedDate);
        selectedTimeStart=Timestamp.valueOf(calendar.get(Calendar.YEAR)+"-"+(calendar.get(Calendar.MONTH)+1)+"-"+
                calendar.get(Calendar.DAY_OF_MONTH)+" "+"00:00:00");
        System.out.println(selectedTimeStart.toString());
        calendar.add(Calendar.DAY_OF_MONTH,1);
        selectedTimeFinish=Timestamp.valueOf(calendar.get(Calendar.YEAR)+"-"+(calendar.get(Calendar.MONTH)+1)+"-"+
                calendar.get(Calendar.DAY_OF_MONTH)+" "+"00:00:00");
        System.out.println(selectedTimeFinish.toString());
        calendar.add(Calendar.DAY_OF_MONTH,-1);
        PrimeFaces.current().ajax().update("form");
    }

    public Date getSelectedDate() {
        return selectedDate;
    }

    public void setSelectedDate(Date date1) {
        this.selectedDate = date1;
    }

    public String getSelectedGroup() {
        return selectedGroup;
    }

    public void setSelectedGroup(String selectedGroup) {
        this.selectedGroup = selectedGroup;
    }

    public Integer getSelectedTeacherId() {
        return selectedTeacherId;
    }

    public void setSelectedTeacherId(Integer selectedTeacher) {
        this.selectedTeacherId = selectedTeacher;
    }

    public Lesson getSelectedLesson() {
        return selectedLesson;
    }

    public void setSelectedLesson(Lesson selectedLesson) {
        this.selectedLesson = selectedLesson;
    }

    public void generate(){
        try{
            CallableStatement preparedStatement=connection.prepareCall("{call СОЗДАТЬ_РАСПИСАНИЕ(?,?)}");
            preparedStatement.setInt(1,selectedLesson.getId());
            System.out.println("Selected id: "+selectedLesson.getId());
            preparedStatement.setDate(2, java.sql.Date.valueOf((maxDate.get(Calendar.YEAR)+"-"+(maxDate.get(Calendar.MONTH)+1)+"-"+maxDate.get(Calendar.DAY_OF_MONTH))));
            System.out.println(preparedStatement.toString());
            boolean result=preparedStatement.execute();
            System.out.println("Result: "+result);
            preparedStatement.close();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Successful", "Расписание обновлено"));
        }catch(SQLException sql){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Error", sql.getMessage()));
        }
    }

    public List<Lesson> getLessons() {
        ResultSet resultSet;
        PreparedStatement preparedStatement;
        System.out.println("Connection="+connection);
        ArrayList<Lesson> points = new ArrayList<Lesson>();
        try {
            preparedStatement = connection.prepareStatement("select * FROM ЗАНЯТИЕ WHERE ВРЕМЯ_НАЧАЛА>=? AND ВРЕМЯ_КОНЦА<=?");
            preparedStatement.setTimestamp(1,selectedTimeStart);
            preparedStatement.setTimestamp(2,selectedTimeFinish);
            preparedStatement.execute();
            resultSet = preparedStatement.getResultSet();
            while(resultSet.next()) {
                Lesson point = new Lesson();
                point.setId(resultSet.getInt(1));
                point.setGroupId(resultSet.getInt(2));
                point.setTeacher_id(resultSet.getInt(3));
                point.setAud_id(resultSet.getInt(4));
                point.setStart_time(resultSet.getTimestamp(5));
                point.setFinish_time(resultSet.getTimestamp(6));
                point.setSemester_id(resultSet.getInt(7));
                point.setGroupName(stateGroupName(connection,point.getGroupId()));
                point.setTeacherName(stateTeacherName(connection,point.getTeacher_id()));
                points.add(point);
            }
            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return points;
    }

    public List<Integer> getAuditories() {
        List<Integer> groups=new ArrayList<>();
        try{
            PreparedStatement newPreparedStatement = connection.prepareStatement("select НОМЕР FROM АУДИТОРИЯ");
            newPreparedStatement.execute();
            ResultSet groupResultSet = newPreparedStatement.getResultSet();
            while(groupResultSet.next()) {
                groups.add(groupResultSet.getInt(1));
            }
            groupResultSet.close();
            newPreparedStatement.close();
        }catch (SQLException sql){
            sql.printStackTrace();
        }
        return groups;
    }

    public void addNewLesson(){
       if (newTimeStart==null || newTimeFinish==null){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Error date", "Поля Время начала и Время конца должны быть заполнены"));
            return;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(newTimeStart);
        cal.set(Calendar.MILLISECOND, 0);
        Calendar cal2=Calendar.getInstance();
        cal2.setTime(newTimeFinish);
        cal2.set(Calendar.MILLISECOND,0);
        if (cal.get(Calendar.YEAR)!=cal2.get(Calendar.YEAR) || cal.get(Calendar.MONTH)!=cal2.get(Calendar.MONTH) || cal.get(Calendar.DAY_OF_MONTH)!=cal2.get(Calendar.DAY_OF_MONTH) || (cal2.get(Calendar.HOUR_OF_DAY)<=cal.get(Calendar.HOUR_OF_DAY))
            || (cal2.get(Calendar.HOUR_OF_DAY)-cal.get(Calendar.HOUR_OF_DAY)<1) || (cal2.get(Calendar.HOUR_OF_DAY)-cal.get(Calendar.HOUR_OF_DAY)>4)){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Error date", "Ошибочная дата"));
            return;
        }
        if (selectedNewAudId==null || selectedNewGroup==null || selectedNewTeacherId==null){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Error fields", "Необходимо заполнить все поля"));
            return;
        }
        try{
            PreparedStatement newPreparedStatement = connection.prepareStatement("INSERT INTO ЗАНЯТИЕ(ГРУППА_ИД,ПРЕП_ИД,АУД_ИД,ВРЕМЯ_НАЧАЛА,ВРЕМЯ_КОНЦА,СЕМЕСТР_ИД) " +
                    "VALUES (?,?,?,?,?,?)");
            newPreparedStatement.setInt(1,selectedNewGroup);
            newPreparedStatement.setInt(2,selectedNewTeacherId);
            newPreparedStatement.setInt(3,selectedNewAudId);
            newPreparedStatement.setTimestamp(4,new Timestamp(cal.getTimeInMillis()));
            newPreparedStatement.setTimestamp(5,new Timestamp(cal2.getTimeInMillis()));
            newPreparedStatement.setInt(6,2);
            newPreparedStatement.execute();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Successful", "Занятие добавлено"));
            newPreparedStatement.close();
        }catch(SQLException sql){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Error during adding", sql.getMessage()));
        }
    }

    private String stateGroupName(Connection connection, Integer id){
        String name=null;
        try{
            PreparedStatement newPreparedStatement = connection.prepareStatement("select НАЗВАНИЕ FROM ГРУППА WHERE ГРУППА_ИД=?");
            newPreparedStatement.setInt(1,id);
            newPreparedStatement.execute();
            ResultSet groupResultSet = newPreparedStatement.getResultSet();
            while(groupResultSet.next()) {
                name=groupResultSet.getString(1);
            }
            groupResultSet.close();
            newPreparedStatement.close();
        }catch (SQLException sql){
            sql.printStackTrace();
        }
        return name;
    }

    public List<Group> getGroups(){
        List<Group> groups=new ArrayList<>();
        try{
            PreparedStatement newPreparedStatement = connection.prepareStatement("select ГРУППА_ИД,НАЗВАНИЕ,ПРЕП_ИД from ГРУППА");
            newPreparedStatement.execute();
            ResultSet groupResultSet = newPreparedStatement.getResultSet();
            while(groupResultSet.next()) {
                Group group=new Group();
                String name=null;
                group.setId(groupResultSet.getInt(1));
                group.setName(groupResultSet.getString(2));
                PreparedStatement preparedStatement=connection.prepareStatement("select ФАМИЛИЯ, ИМЯ FROM ПРЕПОДАВАТЕЛЬ WHERE ПРЕП_ИД=?");
                preparedStatement.setInt(1,groupResultSet.getInt(3));
                preparedStatement.execute();
                ResultSet resultSet=preparedStatement.getResultSet();
                while(resultSet.next()){
                    name=resultSet.getString(1)+" "+resultSet.getString(2);
                }
                group.setTeacherName(name);
                groups.add(group);
            }
            groupResultSet.close();
            newPreparedStatement.close();
        }catch (SQLException sql){
            sql.printStackTrace();
        }
        return groups;
    }

    public List<Teacher> getTeachers(){
        List<Teacher> teachers=new ArrayList<>();
        try{
            PreparedStatement newPreparedStatement = connection.prepareStatement("select ПРЕП_ИД,ФАМИЛИЯ,ИМЯ from ПРЕПОДАВАТЕЛЬ");
            newPreparedStatement.execute();
            ResultSet groupResultSet = newPreparedStatement.getResultSet();
            while(groupResultSet.next()) {
                Teacher teacher=new Teacher();
                teacher.setId(groupResultSet.getInt(1));
                teacher.setSurname(groupResultSet.getString(2));
                teacher.setName(groupResultSet.getString(3));
                teacher.setFio(teacher.getSurname()+" "+teacher.getName());
                teachers.add(teacher);
            }
            groupResultSet.close();
            newPreparedStatement.close();
        }catch (SQLException sql){
            sql.printStackTrace();
        }
        return teachers;
    }

    public void changeTeacher(){
        if (selectedTeacherId==null || selectedGroup==null){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Error during changing a teacher", "Поля Группа и Новый преподаватель обязательны для заполнения"));
            return;
        }
        try{
            PreparedStatement preparedStatement=connection.prepareStatement("UPDATE ГРУППА SET ПРЕП_ИД=? WHERE НАЗВАНИЕ=?");
            preparedStatement.setInt(1,selectedTeacherId);
            preparedStatement.setString(2,selectedGroup);
            boolean result=preparedStatement.execute();
            System.out.println("Result: "+result+".Group:"+selectedGroup+",teacher:"+selectedTeacherId);
            preparedStatement.close();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Successful", "Преподаватель группы успешно заменен"));
        }catch(SQLException sql){
            sql.printStackTrace();
        }
    }

    private String stateTeacherName(Connection connection, Integer id){
        String name=null;
        try{
            PreparedStatement newPreparedStatement = connection.prepareStatement("select ФАМИЛИЯ,ИМЯ FROM ПРЕПОДАВАТЕЛЬ WHERE ПРЕП_ИД=?");
            newPreparedStatement.setInt(1,id);
            newPreparedStatement.execute();
            ResultSet groupResultSet = newPreparedStatement.getResultSet();
            while(groupResultSet.next()) {
                name=groupResultSet.getString(1)+" "+groupResultSet.getString(2);
            }
            groupResultSet.close();
            newPreparedStatement.close();
        }catch (SQLException sql){
            sql.printStackTrace();
        }
        return name;
    }

    public void searchFreeAud(){
        if (audTimeStart==null || audTimeFinish==null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Error:", "Поля Время начала и время конца обязательны для заполнения"));
            return;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(audTimeStart);
        cal.set(Calendar.MILLISECOND, 0);
        Calendar cal2=Calendar.getInstance();
        cal2.setTime(audTimeFinish);
        cal2.set(Calendar.MILLISECOND,0);
        if (cal.get(Calendar.YEAR)!=cal2.get(Calendar.YEAR) || cal.get(Calendar.MONTH)!=cal2.get(Calendar.MONTH) || cal.get(Calendar.DAY_OF_MONTH)!=cal2.get(Calendar.DAY_OF_MONTH) || (cal2.get(Calendar.HOUR_OF_DAY)<=cal.get(Calendar.HOUR_OF_DAY))
                || (cal2.get(Calendar.HOUR_OF_DAY)-cal.get(Calendar.HOUR_OF_DAY)<1) || (cal2.get(Calendar.HOUR_OF_DAY)-cal.get(Calendar.HOUR_OF_DAY)>4)){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Error date", "Ошибочная дата (меньше часа или более 4 часов)"));
            return;
        }
        try{
            Integer aud=null;
            CallableStatement newPreparedStatement = connection.prepareCall("{call НАЙТИ_СВОБОДНУЮ_АУДИТОРИЮ(?,?)}");
            newPreparedStatement.setTimestamp(1,new Timestamp(cal.getTimeInMillis()));
            newPreparedStatement.setTimestamp(2,new Timestamp(cal2.getTimeInMillis()));
            newPreparedStatement.execute();
            ResultSet resultSet=newPreparedStatement.getResultSet();
            while(resultSet.next()){
                aud=resultSet.getInt(1);
                System.out.println("Номер свободной аудитории:"+aud);
                break;
            }
            freeAud=aud;
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Successful", "Номер свободной аудитории:"+aud));
            resultSet.close();
            newPreparedStatement.close();
        }catch(SQLException sql){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Error during searching auditory", sql.getMessage()));
        }
    }

    public void hideAud(){
        freeAud=null;
        audTimeStart=null;
        audTimeFinish=null;
    }

    public void searchFreeTeacher(){
        if (teacherTimeStart==null || teacherTimeFinish==null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Error:", "Поля Время начала и время конца обязательны для заполнения"));
            return;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(teacherTimeStart);
        cal.set(Calendar.MILLISECOND, 0);
        Calendar cal2=Calendar.getInstance();
        cal2.setTime(teacherTimeFinish);
        cal2.set(Calendar.MILLISECOND,0);
        if (cal.get(Calendar.YEAR)!=cal2.get(Calendar.YEAR) || cal.get(Calendar.MONTH)!=cal2.get(Calendar.MONTH) || cal.get(Calendar.DAY_OF_MONTH)!=cal2.get(Calendar.DAY_OF_MONTH) || (cal2.get(Calendar.HOUR_OF_DAY)<=cal.get(Calendar.HOUR_OF_DAY))
                || (cal2.get(Calendar.HOUR_OF_DAY)-cal.get(Calendar.HOUR_OF_DAY)<1) || (cal2.get(Calendar.HOUR_OF_DAY)-cal.get(Calendar.HOUR_OF_DAY)>4)){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Error date", "Ошибочная дата (меньше часа или более 4 часов)"));
            return;
        }
        try{
            String name=null;
            CallableStatement newPreparedStatement = connection.prepareCall("{call НАЙТИ_СВОБОДНОГО_ПРЕПОДАВАТЕЛЯ(?,?)}");
            newPreparedStatement.setTimestamp(1,new Timestamp(cal.getTimeInMillis()));
            newPreparedStatement.setTimestamp(2,new Timestamp(cal2.getTimeInMillis()));
            newPreparedStatement.execute();
            ResultSet resultSet=newPreparedStatement.getResultSet();
            while(resultSet.next()){
                name=resultSet.getString(2)+" "+resultSet.getString(3);
                System.out.println("Имя свободного препода:"+name);
                break;
            }
            freeTeacher=name;
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Successful", "Имя свободного преподавателя:"+name));
            resultSet.close();
            newPreparedStatement.close();
        }catch(SQLException sql){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Error during searching teacher", sql.getMessage()));
        }
    }

    public void hideTeacher(){
        freeTeacher=null;
        teacherTimeStart=null;
        teacherTimeFinish=null;
    }

    @PostConstruct
    private void createTable(){
        try {
            Class.forName("org.postgresql.Driver");
            String url = "jdbc:postgresql://192.168.10.99:5432/studs";
            String login = "*******";
            String password = "******";
            connection = DriverManager.getConnection(url, login, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}