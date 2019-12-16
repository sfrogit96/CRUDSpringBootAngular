package com.th.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.th.exception.ResourceNotFoundException;
import com.th.model.Employee;
import com.th.repository.EmployeeRepository;

@RestController 
@CrossOrigin (origins = "http://localhost:4200")
@RequestMapping("/api/v1")
public class EmployeeController {
	@Autowired
	private EmployeeRepository employeeRepository;
	
	Map<String, String> errors;
	
	@GetMapping ("/employees")
	public List<Employee> getAllEmployees(){
		return employeeRepository.findAll();
	}
	
	@GetMapping("/employees/{id}")
	public ResponseEntity<Employee> getEmployeeById(@PathVariable(value = "id") Long employeeId )
	throws ResourceNotFoundException{
		Employee employee = employeeRepository.findById(employeeId)
		.orElseThrow(() -> new ResourceNotFoundException("Employee not found for this id :: " + employeeId));
		return ResponseEntity.ok().body(employee);
	}
	
	@PostMapping("/employees") //@RequestBody => Chuyen json thanh object
	public ResponseEntity<Object>  createEmployee(@Valid @RequestBody Employee employee, BindingResult bindingResult) {
	 if (bindingResult.hasErrors()) {
		 errors = new HashMap<>();
		 for (FieldError error: bindingResult.getFieldErrors()) {
			 errors.put(error.getField(), error.getDefaultMessage());
		 }
		 return new ResponseEntity<>(errors, HttpStatus.NOT_ACCEPTABLE);
	 }
	 return new ResponseEntity<>(employeeRepository.save(employee), HttpStatus.OK);
		}
	
	@PutMapping("/employees/{id}")
	public ResponseEntity<Employee> updateEmployee(@PathVariable(value = "id") Long employeeId,
			@Valid @RequestBody Employee employeeDetails) throws ResourceNotFoundException{
		Employee employee = employeeRepository.findById(employeeId)
				.orElseThrow(() -> new ResourceNotFoundException("Employee not found for this id :: " + employeeId));
		
		employee.setEmailId(employeeDetails.getEmailId());
		employee.setLastName(employeeDetails.getLastName());
	    employee.setFirstName(employeeDetails.getFirstName());
	    final Employee updatedEmployee = employeeRepository.save(employee);
        
        return ResponseEntity.ok(updatedEmployee);
        		
	}
	
//	@PostMapping("/employees") //@RequestBody => Chuyen json thanh object
//	public Employee createEmployee(@Valid @RequestBody Employee employee) {
//		return employeeRepository.save(employee);
//		
//	}
//	
//	@PutMapping("/employees/{id}")
//	public ResponseEntity<Employee> updateEmployee(@PathVariable(value = "id") Long employeeId,
//			@Valid @RequestBody Employee employeeDetails) throws ResourceNotFoundException{
//		Employee employee = employeeRepository.findById(employeeId)
//				.orElseThrow(() -> new ResourceNotFoundException("Employee not found for this id :: " + employeeId));
//		
//		employee.setEmailId(employeeDetails.getEmailId());
//		employee.setLastName(employeeDetails.getLastName());
//	    employee.setFirstName(employeeDetails.getFirstName());
//	    final Employee updatedEmployee = employeeRepository.save(employee);
//        
//        return ResponseEntity.ok(updatedEmployee);
//        		
//	}
	
	@DeleteMapping("/employees/{id}")
	public Map<String, Boolean> deleteEmployee(@PathVariable(value = "id") Long employeeId)
	throws ResourceNotFoundException{
        Employee employee = employeeRepository.findById(employeeId)
       .orElseThrow(() -> new ResourceNotFoundException("Employee not found for this id :: " + employeeId));
        employeeRepository.delete(employee);
        Map<String, Boolean> response = new HashMap<>();
        
        response.put("deleted", Boolean.TRUE);
        return response;
	}
	
}
