package com.example.ElearningTLU;

import com.example.ElearningTLU.Dto.Request.CourseRequest;
import com.example.ElearningTLU.Dto.DepartmentDto;
import com.example.ElearningTLU.Dto.MajorDto;
import com.example.ElearningTLU.Entity.*;
import com.example.ElearningTLU.Repository.*;
import com.example.ElearningTLU.Services.CourseService.CourseService;
import com.example.ElearningTLU.Services.DepartmentService.DepartmentService;
import com.example.ElearningTLU.Services.MajorService.MajorService;
import com.example.ElearningTLU.Services.SemesterService.SemesterGroupService;
import com.example.ElearningTLU.Utils.TimeTableUtils;
import jakarta.annotation.PostConstruct;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;


@SpringBootApplication
public class ElearningTluApplication {
	@Autowired
	private PersonRepository personRepository;
	@Autowired
	public GenerationRepository generationRepository;
	@Autowired
	public GroupStudentRepository groupStudentRepository;
	@Autowired
	public RoomRepository roomRepository;
	@Autowired
	public SemesterRepository semesterRepository;
	@Autowired
	public SemesterGroupService semesterGroupService;
	@Autowired
	public SemesterGroupRepository semesterGroupRepository;
	@Autowired
	public CourseService courseService;
	@Autowired
	public DepartmentService departmentService;
	@Autowired
	public MajorService majorService;

	@Autowired
	public TimeTableUtils timeTableUtils;

	public static void main(String[] args) {
		SpringApplication.run(ElearningTluApplication.class, args);
		System.out.println("Program Is Running");
	}

	@PostConstruct
	public void run() {
		this.UpdateData();
				this.UpdateNganh();
		this.updateMonHoc();

//		System.out.println(c		alculateDelayToMidnight());
//		long period = 10000;
//		Timer timer = new Timer();
//		timer.scheduleAtFixedRate(new DailyTask(),calculateDelayToMidnight(),period);
		this.AutoUpdate();

	}
	class DailyTask extends TimerTask {
		@Override
		public void run() {
			AutoUpdate();
//			AutoUpdate();
//			System.out.println("ham Nay Tu dong Cap Nhat");
		}
	}
	public void UpdateTimeTable()
	{
		LocalDate date = LocalDate.of(2024,9,10);
		System.out.println("Cap Nhat Thoi Khoa Bieu");
		if(this.semesterGroupRepository.FindSemesterGroupByNowTime(date.toString()).isPresent())
		{
			System.out.println("Da Cap Nhat");
//			Semester_Group semesterGroup= this.semesterGroupRepository.FindSemesterGroupByNowTime(date.toString()).get();
//			timeTableUtils.AutoUpdateTimeTableForStudent(semesterGroup);
		}
		else
		{
			System.out.println("Ko dc Update");
		}
	}

	public void UpdateData() {

		if (this.generationRepository.findAll().isEmpty()) {
			for (int i = 1; i <= 10; i++) {
				Generation generation = new Generation();
				generation.setGenerationId("K" + i);
				generation.setGenerationName("Khóa " + i);
				generation.setStartYear(2019 + i);
				this.generationRepository.save(generation);
			}
		}
		if (this.groupStudentRepository.findAll().isEmpty()) {
			int n = 3;
			for (int i = 1; i <= 3; i++) {
				GroupStudent groupStudent = new GroupStudent();
				groupStudent.setGroupId("N" + i);
				groupStudent.setGroupName("Nhóm " + i);
				groupStudent.setNum(n);
				n--;
				this.groupStudentRepository.save(groupStudent);
			}
		}
		if (this.roomRepository.findAll().isEmpty()) {
			for (int i = 1; i < 10; i++) {
				for (int j = 1; j < 10; j++) {
					Room room = new Room();
					if (j % 2 == 0) {
						room.setRoomId("P" + i + "0" + j);
						room.setRoomName("Phòng " + i + "0" + j);
						room.setSeats(80);
						this.roomRepository.save(room);
					} else {
						room.setRoomId("P" + i + "0" + j);
						room.setRoomName("Phòng " + i + "0" + j);
						room.setSeats(40);
						this.roomRepository.save(room);
					}
				}
			}
		}
		if (this.semesterRepository.findAll().isEmpty()) {
			for (int i = 1; i <= 3; i++) {
				Semester semester = new Semester();
				semester.setSemesterId("K" + i);
				semester.setSemesterName("Kì " + i);
				this.semesterRepository.save(semester);
			}
		}
	}
	public void AutoUpdate()
	{
//		this.semesterGroupService.AutoUpdate();
//		Person person1=this.personRepository.findAllPersonByRole(Role.ADMIN.name()).get().get(0);
//		System.out.println(person1.getUserName());
		if(this.personRepository.findAdmin().isEmpty())
		{
			System.out.println("koko");
			Person person = new Person();
			person.setPersonId("ADMIN");
			person.setRole(Role.ADMIN);
			person.setUserName("admin@thanglong.edu.vn");
			person.setPassword(new BCryptPasswordEncoder().encode("admin"));
			this.personRepository.save(person);
		}
	}
	public long calculateDelayToMidnight() {
		ZonedDateTime now = ZonedDateTime.now(ZoneId.systemDefault());

		// Đặt giờ, phút, giây thành 0
		ZonedDateTime midnight = now.with(LocalTime.MIDNIGHT);
		System.out.println("Thoi Gian nua dem:"+midnight);

		// Nếu thời điểm hiện tại đã qua nửa đêm, thì tính đến nửa đêm hôm sau
		if (now.isAfter(midnight)) {
			midnight = midnight.plusDays(1);
		}

		// Tính khoảng thời gian còn lại đến nửa đêm
		long minutesToMidnight = now.until(midnight, ChronoUnit.MINUTES);

		System.out.println("Thời gian còn lại đến nửa đêm: " + minutesToMidnight + " phút");
		return minutesToMidnight;
	}
	public void UpdateNganh()
	{
		String file="D://DataElearning//DanhSachKhoa.xlsx";
		try (FileInputStream fileInputStream = new FileInputStream(new File(file))) {
			Workbook workbook = new XSSFWorkbook(fileInputStream);
			Sheet sheet = workbook.getSheetAt(0); // Lấy sheet đầu tiên
			DepartmentDto departmentDto= new DepartmentDto();
			for (Row row : sheet) {
				if(row.getRowNum()==0)
				{
					continue;
				}
				departmentDto.setDepartmentId(row.getCell(0).getStringCellValue());
				departmentDto.setDepartmentName(row.getCell(1).getStringCellValue());
				this.departmentService.addDepartment(departmentDto);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		String file1="D://DataElearning//DanhSachKhoa.xlsx";
		try (FileInputStream fileInputStream = new FileInputStream(new File(file))) {
			Workbook workbook = new XSSFWorkbook(fileInputStream);
			Sheet sheet = workbook.getSheetAt(1); // Lấy sheet đầu tiên
			MajorDto majorDto= new MajorDto();
			for (Row row : sheet) {
				if(row.getRowNum()==0)
				{
					continue;
				}
				majorDto.setMajorId(row.getCell(0).getStringCellValue());
				majorDto.setMajorName(row.getCell(1).getStringCellValue());
				majorDto.setDepartment(row.getCell(2).getStringCellValue());
				this.majorService.addMajor(majorDto);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}


	}

	public void updateMonHoc()
	{
		CourseRequest course = new CourseRequest();
		String file="D://DataElearning//DanhSachMon.xlsx";
		try (FileInputStream fileInputStream = new FileInputStream(new File(file))) {
			Workbook workbook = new XSSFWorkbook(fileInputStream);
			Sheet sheet = workbook.getSheetAt(0); // Lấy sheet đầu tiên

			for (Row row : sheet) {
				if (row.getRowNum()==0)
				{
					continue;
				}
//				System.out.println("hihii"+row.getCell(0).getStringCellValue());
				course.setCourseId(row.getCell(0).getStringCellValue());
				course.setCourseName(row.getCell(1).getStringCellValue());
				double numericValue = row.getCell(2).getNumericCellValue();
				course.setCredits((int) numericValue);
				double numericValue1 = row.getCell(3).getNumericCellValue();
				course.setCoefficient(numericValue1);
//				System.out.println(row.getCell(5).getStringCellValue());
				if(row.getCell(5).getStringCellValue().equals("COSO")) {

				course.setType(CourseType.COSO);
				}
				else if(row.getCell(5).getStringCellValue().equals("COSONGANH"))
				{
					course.setType(CourseType.COSONGANH);

				}
				else
				{
					course.setType(CourseType.CHUYENNGANH);
				}

				course.setReqiId(new ArrayList<>());
				double numericValue3 = row.getCell(6).getNumericCellValue();
				course.setRequestCredits((int) numericValue3);
				if(row.getCell(4)!=null)
				{
					System.out.println(row.getCell(4).getStringCellValue());
//					List<String> id = new ArrayList<>();
					for(String i: row.getCell(4).getStringCellValue().split(","))
					{
						course.getReqiId().add(i);
					}
				}
				else
				{
					course.setReqiId(null);
				}
				if(row.getCell(7)!=null)
				{
					List<String> id= new ArrayList<>();
					for(String i: row.getCell(7).getStringCellValue().split(","))
					{
						id.add(i);
					}
					course.setDepartmentId(id);
				}
				else
				{
					course.setDepartmentId(null);
				}
				if(row.getCell(8)!=null)
				{
					List<String> id = new ArrayList<>();

					for(String i: row.getCell(8).getStringCellValue().split(","))
					{
						id.add(i);
					}
					course.setMajorId(id);
				}
				else
				{
					course.setMajorId(null);
				}
				this.courseService.addCourse(course);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}



}
