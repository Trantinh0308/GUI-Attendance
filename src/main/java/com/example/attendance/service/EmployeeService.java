package com.example.attendance.service;

import com.example.attendance.model.Employee;
import com.example.attendance.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {
    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }
    public List<Employee> getAllEmployees(){
        return (List<Employee>) employeeRepository.findAll();
    }
    public Optional<Employee> getEmployeeById(long id){
        return employeeRepository.findById(id);
    }
    public Employee saveEmployee(Employee employee){
        return employeeRepository.save(employee);
    }
}
