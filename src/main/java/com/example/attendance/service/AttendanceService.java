package com.example.attendance.service;

import com.example.attendance.dto.AttendanceDTO;
import com.example.attendance.dto.AttendanceDTO2;
import com.example.attendance.dto.AttendanceDTO3;
import com.example.attendance.errors.BadRequestAlertException;
import com.example.attendance.model.Attendance;
import com.example.attendance.model.Employee;
import com.example.attendance.repository.AttendanceRepository;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import static org.hibernate.id.IdentifierGenerator.ENTITY_NAME;

@Service
public class AttendanceService {
    private final Logger log = LoggerFactory.getLogger(AttendanceService.class);
    private final SimpMessagingTemplate messagingTemplate;
    private final AttendanceRepository attendanceRepository;
    private final EmployeeService employeeService;

    public AttendanceService(SimpMessagingTemplate messagingTemplate, AttendanceRepository attendanceRepository, EmployeeService employeeService) {
        this.messagingTemplate = messagingTemplate;
        this.attendanceRepository = attendanceRepository;
        this.employeeService = employeeService;
    }
    public Attendance save(AttendanceDTO attendanceDTO) {
        if (attendanceDTO.getEmployeeId() == null) {
            log.debug("Employee ID not found: {}", attendanceDTO);
            throw new BadRequestAlertException("Id not exists", ENTITY_NAME, "idexists");
        }

        Optional<Employee> employee = employeeService.getEmployeeById(attendanceDTO.getEmployeeId());
        if (employee.isPresent()) {

            AttendanceDTO2 attendanceDTO2 = new AttendanceDTO2();
            attendanceDTO2.setEmployeeName(employee.get().getFullName());
            attendanceDTO2.setImageCode(attendanceDTO.getImageCode());
            attendanceDTO2.setDepartment(employee.get().getDepartment());
            attendanceDTO2.setTime(attendanceDTO.getDate() + " " + attendanceDTO.getTime());
            messagingTemplate.convertAndSend("/topic/data", attendanceDTO2);

            Attendance attendanceRequest = new Attendance();
            attendanceRequest.setEmployee(employee.get());
            attendanceRequest.setDeviceName("Camera");
            attendanceRequest.setImageCode(attendanceDTO.getImageCode());
            attendanceRequest.setDate(attendanceDTO.getDate());
            attendanceRequest.setTime(attendanceDTO.getTime());
            if (!attendanceRepository.existsByEmployee_IdAndDate(attendanceDTO.getEmployeeId(),attendanceDTO.getDate())){
                attendanceRequest.setStatus("Check in");
            }
            else attendanceRequest.setStatus("Check out");

            return attendanceRepository.save(attendanceRequest);
        } else {
            throw new BadRequestAlertException("Employee not found", ENTITY_NAME, "employeeNotFound");
        }
    }

    public List<AttendanceDTO3> getAttendanceByEmployeeAndDate(Long employeeId, Date attendanceDate) {
        return attendanceRepository.findAllAttendanceByEmployeeIdAndDate(employeeId, attendanceDate);
    }

    public List<AttendanceDTO3> getAttendanceByDate(Date attendanceDate) {
        return attendanceRepository.findAllAttendanceByDate(attendanceDate);
    }

}
